package com.nexora.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressDto {
    private Long id;
    private String label;
    private String street;
    private String city;
    private String postalCode;
    private String country;
    private boolean defaultAddress;
}
