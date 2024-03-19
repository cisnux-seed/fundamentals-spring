package dev.cisnux.webmvc.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAddressRequest {
    private String street;
    private String city;
    private String country;
    private String postalCode;
}
