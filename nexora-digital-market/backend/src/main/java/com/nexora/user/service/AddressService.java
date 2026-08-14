package com.nexora.user.service;

import com.nexora.common.exception.ResourceNotFoundException;
import com.nexora.user.dto.AddressDto;
import com.nexora.user.dto.AddressRequest;
import com.nexora.user.entity.Address;
import com.nexora.user.entity.User;
import com.nexora.user.repository.AddressRepository;
import com.nexora.user.security.UserContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserContextService userContextService;

    @Transactional(readOnly = true)
    public List<AddressDto> getMyAddresses() {
        User user = userContextService.getCurrentUser();
        return addressRepository.findByUser(user).stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public AddressDto createAddress(AddressRequest request) {
        User user = userContextService.getCurrentUser();

        if (request.isDefaultAddress()) {
            clearDefault(user);
        }

        Address address = Address.builder()
                .user(user)
                .label(request.getLabel())
                .street(request.getStreet())
                .city(request.getCity())
                .postalCode(request.getPostalCode())
                .country(request.getCountry())
                .defaultAddress(request.isDefaultAddress())
                .build();

        return toDto(addressRepository.save(address));
    }

    @Transactional
    public AddressDto updateAddress(Long id, AddressRequest request) {
        Address address = requireOwnedAddress(id);

        if (request.isDefaultAddress()) {
            clearDefault(address.getUser());
        }

        address.setLabel(request.getLabel());
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());
        address.setDefaultAddress(request.isDefaultAddress());

        return toDto(addressRepository.save(address));
    }

    @Transactional
    public void deleteAddress(Long id) {
        Address address = requireOwnedAddress(id);
        addressRepository.delete(address);
    }

    @Transactional
    public AddressDto setDefaultAddress(Long id) {
        Address address = requireOwnedAddress(id);
        clearDefault(address.getUser());
        address.setDefaultAddress(true);
        return toDto(addressRepository.save(address));
    }

    private Address requireOwnedAddress(Long id) {
        User user = userContextService.getCurrentUser();
        return addressRepository.findById(id)
                .filter(a -> a.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Adresse introuvable"));
    }

    private void clearDefault(User user) {
        addressRepository.findByUser(user).forEach(a -> {
            if (a.isDefaultAddress()) {
                a.setDefaultAddress(false);
                addressRepository.save(a);
            }
        });
    }

    private AddressDto toDto(Address address) {
        return AddressDto.builder()
                .id(address.getId())
                .label(address.getLabel())
                .street(address.getStreet())
                .city(address.getCity())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .defaultAddress(address.isDefaultAddress())
                .build();
    }
}
