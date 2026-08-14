package com.nexora.order.service;

import com.nexora.cart.entity.Cart;
import com.nexora.cart.entity.CartItem;
import com.nexora.cart.repository.CartRepository;
import com.nexora.common.enums.OrderStatus;
import com.nexora.common.exception.ResourceNotFoundException;
import com.nexora.common.exception.ValidationException;
import com.nexora.inventory.service.InventoryService;
import com.nexora.order.dto.*;
import com.nexora.order.entity.CustomerOrder;
import com.nexora.order.entity.OrderItem;
import com.nexora.order.entity.SellerOrder;
import com.nexora.order.repository.CustomerOrderRepository;
import com.nexora.order.repository.SellerOrderRepository;
import com.nexora.product.entity.Product;
import com.nexora.product.entity.ProductImage;
import com.nexora.product.entity.ProductOffer;
import com.nexora.seller.entity.Seller;
import com.nexora.seller.security.SellerContextService;
import com.nexora.user.entity.Address;
import com.nexora.user.entity.User;
import com.nexora.user.repository.AddressRepository;
import com.nexora.user.security.UserContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerOrderRepository orderRepository;
    private final SellerOrderRepository sellerOrderRepository;
    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;
    private final UserContextService userContextService;
    private final SellerContextService sellerContextService;
    private final InventoryService inventoryService;
    private final OrderNotificationService notificationService;

    @Transactional
    public OrderDetailDto createFromCart(CreateOrderRequest request) {
        User user = userContextService.getCurrentUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ValidationException("Panier vide"));

        if (cart.getItems().isEmpty()) {
            throw new ValidationException("Panier vide");
        }

        Address address = addressRepository.findById(request.getAddressId())
                .filter(a -> a.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Adresse introuvable"));

        validateCartItems(cart);

        CustomerOrder order = CustomerOrder.builder()
                .orderNumber(generateOrderNumber())
                .user(user)
                .shippingLabel(address.getLabel())
                .shippingStreet(address.getStreet())
                .shippingCity(address.getCity())
                .shippingPostalCode(address.getPostalCode())
                .shippingCountry(address.getCountry())
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)
                .build();

        Map<Seller, List<CartItem>> bySeller = cart.getItems().stream()
                .collect(Collectors.groupingBy(item -> item.getProductOffer().getSeller()));

        BigDecimal orderTotal = BigDecimal.ZERO;
        int orderItemCount = 0;

        for (Map.Entry<Seller, List<CartItem>> entry : bySeller.entrySet()) {
            Seller seller = entry.getKey();
            List<CartItem> items = entry.getValue();

            SellerOrder sellerOrder = SellerOrder.builder()
                    .order(order)
                    .seller(seller)
                    .status(OrderStatus.PENDING)
                    .subtotal(BigDecimal.ZERO)
                    .build();
            order.getSellerOrders().add(sellerOrder);

            BigDecimal subtotal = BigDecimal.ZERO;
            int sellerItemCount = 0;

            for (CartItem cartItem : items) {
                ProductOffer offer = cartItem.getProductOffer();
                Product product = offer.getProduct();
                String imageUrl = resolveImageUrl(product);
                BigDecimal lineTotal = cartItem.getUnitPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

                OrderItem orderItem = OrderItem.builder()
                        .sellerOrder(sellerOrder)
                        .productOffer(offer)
                        .productName(product.getName())
                        .productSlug(product.getSlug())
                        .sellerName(seller.getCompanyName())
                        .imageUrl(imageUrl)
                        .quantity(cartItem.getQuantity())
                        .unitPrice(cartItem.getUnitPrice())
                        .lineTotal(lineTotal)
                        .build();
                sellerOrder.getItems().add(orderItem);

                inventoryService.decreaseStockForOrder(
                        offer,
                        cartItem.getQuantity(),
                        "Commande " + order.getOrderNumber()
                );

                subtotal = subtotal.add(lineTotal);
                sellerItemCount += cartItem.getQuantity();
            }

            sellerOrder.setSubtotal(subtotal);
            sellerOrder.setItemCount(sellerItemCount);
            orderTotal = orderTotal.add(subtotal);
            orderItemCount += sellerItemCount;
        }

        order.setTotalAmount(orderTotal);
        order.setItemCount(orderItemCount);

        CustomerOrder saved = orderRepository.save(order);
        cart.getItems().clear();
        cartRepository.save(cart);

        notificationService.notifyOrderCreated(saved);
        return toDetailDto(saved);
    }

    @Transactional(readOnly = true)
    public List<OrderSummaryDto> getMyOrders() {
        User user = userContextService.getCurrentUser();
        return orderRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(this::toSummaryDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderDetailDto getMyOrder(Long orderId) {
        User user = userContextService.getCurrentUser();
        CustomerOrder order = orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Commande introuvable"));
        return toDetailDto(order);
    }

    @Transactional(readOnly = true)
    public List<OrderSummaryDto> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toSummaryDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderDetailDto getOrderById(Long orderId) {
        CustomerOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande introuvable"));
        return toDetailDto(order);
    }

    @Transactional(readOnly = true)
    public List<SellerOrderDto> getSellerOrders() {
        Seller seller = sellerContextService.requireApprovedSeller();
        return sellerOrderRepository.findBySellerOrderByCreatedAtDesc(seller).stream()
                .map(this::toSellerOrderDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public SellerOrderDto getSellerOrder(Long sellerOrderId) {
        Seller seller = sellerContextService.requireApprovedSeller();
        SellerOrder sellerOrder = sellerOrderRepository.findByIdAndSeller(sellerOrderId, seller)
                .orElseThrow(() -> new ResourceNotFoundException("Commande introuvable"));
        return toSellerOrderDto(sellerOrder);
    }

    @Transactional
    public SellerOrderDto updateSellerOrderStatus(Long sellerOrderId, UpdateOrderStatusRequest request) {
        Seller seller = sellerContextService.requireApprovedSeller();
        SellerOrder sellerOrder = sellerOrderRepository.findByIdAndSeller(sellerOrderId, seller)
                .orElseThrow(() -> new ResourceNotFoundException("Commande introuvable"));

        validateStatusTransition(sellerOrder.getStatus(), request.getStatus());
        sellerOrder.setStatus(request.getStatus());
        SellerOrder saved = sellerOrderRepository.save(sellerOrder);

        syncOrderStatus(saved.getOrder());
        notificationService.notifySellerOrderStatusUpdated(
                saved.getOrder(),
                seller.getCompanyName(),
                request.getStatus().name()
        );
        return toSellerOrderDto(saved);
    }

    private void syncOrderStatus(CustomerOrder order) {
        List<SellerOrder> sellerOrders = sellerOrderRepository.findByOrderId(order.getId());
        if (sellerOrders.isEmpty()) {
            return;
        }

        boolean allCancelled = sellerOrders.stream()
                .allMatch(so -> so.getStatus() == OrderStatus.CANCELLED);
        if (allCancelled) {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
            return;
        }

        boolean allDelivered = sellerOrders.stream()
                .allMatch(so -> so.getStatus() == OrderStatus.DELIVERED);
        if (allDelivered) {
            order.setStatus(OrderStatus.DELIVERED);
            orderRepository.save(order);
            return;
        }

        boolean anyShipped = sellerOrders.stream()
                .anyMatch(so -> so.getStatus() == OrderStatus.SHIPPED || so.getStatus() == OrderStatus.DELIVERED);
        if (anyShipped) {
            order.setStatus(OrderStatus.SHIPPED);
            orderRepository.save(order);
        }
    }

    private void validateStatusTransition(OrderStatus current, OrderStatus next) {
        if (current == OrderStatus.CANCELLED || current == OrderStatus.DELIVERED) {
            throw new ValidationException("Impossible de modifier une commande " + current.name());
        }
        if (next == OrderStatus.PENDING) {
            throw new ValidationException("Transition de statut invalide");
        }
    }

    private void validateCartItems(Cart cart) {
        for (CartItem item : cart.getItems()) {
            ProductOffer offer = item.getProductOffer();
            if (!offer.isActive()) {
                throw new ValidationException("L'offre « " + offer.getProduct().getName() + " » n'est plus disponible");
            }
            if (offer.getStock() == null || offer.getStock() < item.getQuantity()) {
                throw new ValidationException("Stock insuffisant pour « " + offer.getProduct().getName() + " »");
            }
        }
    }

    private String generateOrderNumber() {
        String prefix = "NX-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String candidate;
        do {
            candidate = prefix + "-" + String.format("%04d", new Random().nextInt(10000));
        } while (orderRepository.existsByOrderNumber(candidate));
        return candidate;
    }

    private String resolveImageUrl(Product product) {
        return product.getImages().stream()
                .filter(ProductImage::isPrimary)
                .findFirst()
                .or(() -> product.getImages().stream().findFirst())
                .map(ProductImage::getUrl)
                .orElse(null);
    }

    private OrderSummaryDto toSummaryDto(CustomerOrder order) {
        return OrderSummaryDto.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .itemCount(order.getItemCount())
                .sellerCount(order.getSellerOrders().size())
                .createdAt(order.getCreatedAt())
                .build();
    }

    private OrderDetailDto toDetailDto(CustomerOrder order) {
        return OrderDetailDto.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .itemCount(order.getItemCount())
                .shippingLabel(order.getShippingLabel())
                .shippingStreet(order.getShippingStreet())
                .shippingCity(order.getShippingCity())
                .shippingPostalCode(order.getShippingPostalCode())
                .shippingCountry(order.getShippingCountry())
                .sellerOrders(order.getSellerOrders().stream().map(this::toSellerOrderDto).toList())
                .createdAt(order.getCreatedAt())
                .build();
    }

    private SellerOrderDto toSellerOrderDto(SellerOrder sellerOrder) {
        return SellerOrderDto.builder()
                .id(sellerOrder.getId())
                .sellerName(sellerOrder.getSeller().getCompanyName())
                .status(sellerOrder.getStatus())
                .subtotal(sellerOrder.getSubtotal())
                .itemCount(sellerOrder.getItemCount())
                .items(sellerOrder.getItems().stream().map(this::toOrderItemDto).toList())
                .createdAt(sellerOrder.getCreatedAt())
                .build();
    }

    private OrderItemDto toOrderItemDto(OrderItem item) {
        return OrderItemDto.builder()
                .id(item.getId())
                .productName(item.getProductName())
                .productSlug(item.getProductSlug())
                .sellerName(item.getSellerName())
                .imageUrl(item.getImageUrl())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .lineTotal(item.getLineTotal())
                .build();
    }
}
