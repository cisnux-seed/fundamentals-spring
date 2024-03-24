package dev.cisnux.contactapi.controller;

import dev.cisnux.contactapi.entity.User;
import dev.cisnux.contactapi.model.AddressResponse;
import dev.cisnux.contactapi.model.CreateAddressRequest;
import dev.cisnux.contactapi.model.UpdateAddressRequest;
import dev.cisnux.contactapi.model.WebResponse;
import dev.cisnux.contactapi.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AddressController {
    private final AddressService addressService;

    @PostMapping(
            path = "/api/contacts/{contactId}/addresses",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public WebResponse<AddressResponse> create(User user, @RequestBody CreateAddressRequest createAddressRequest, @PathVariable("contactId") String contactId) {
        final var addressResponse = addressService.create(user, createAddressRequest.withContactId(contactId));
        return WebResponse.<AddressResponse>builder().data(addressResponse).build();
    }

    @GetMapping(
            path = "/api/contacts/{contactId}/addresses/{addressId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<AddressResponse> get(User user, @PathVariable("contactId") String contactId, @PathVariable("addressId") String addressId) {
        final var addressResponse = addressService.get(user, contactId, addressId);
        return WebResponse.<AddressResponse>builder().data(addressResponse).build();
    }

    @PutMapping(
            path = "/api/contacts/{contactId}/addresses/{addressId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<AddressResponse> update(User user, @PathVariable("contactId") String contactId, @PathVariable("addressId") String addressId, @RequestBody UpdateAddressRequest updateAddressRequest) {
        final var addressResponse = addressService.update(user, updateAddressRequest.withContactId(contactId).withAddressId(addressId));
        return WebResponse.<AddressResponse>builder().data(addressResponse).build();
    }

    @DeleteMapping(
            path = "/api/contacts/{contactId}/addresses/{addressId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<String> remove(User user, @PathVariable("contactId") String contactId, @PathVariable("addressId") String addressId) {
        addressService.delete(user, contactId, addressId);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(
            path = "/api/contacts/{contactId}/addresses",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<List<AddressResponse>> get(User user, @PathVariable("contactId") String contactId) {
        final var addressResponses = addressService.list(user, contactId);
        return WebResponse.<List<AddressResponse>>builder().data(addressResponses).build();
    }
}
