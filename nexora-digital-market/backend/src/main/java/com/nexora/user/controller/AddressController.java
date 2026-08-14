package com.nexora.user.controller;

import com.nexora.user.dto.AddressDto;
import com.nexora.user.dto.AddressRequest;
import com.nexora.user.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@Tag(name = "Adresses")
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    @Operation(summary = "Mes adresses")
    public List<AddressDto> getMyAddresses() {
        return addressService.getMyAddresses();
    }

    @PostMapping
    @Operation(summary = "Ajouter une adresse")
    public ResponseEntity<AddressDto> createAddress(@Valid @RequestBody AddressRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.createAddress(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une adresse")
    public AddressDto updateAddress(@PathVariable Long id, @Valid @RequestBody AddressRequest request) {
        return addressService.updateAddress(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une adresse")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/default")
    @Operation(summary = "Définir comme adresse par défaut")
    public AddressDto setDefaultAddress(@PathVariable Long id) {
        return addressService.setDefaultAddress(id);
    }
}
