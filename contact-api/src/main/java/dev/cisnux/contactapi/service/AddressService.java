package dev.cisnux.contactapi.service;

import dev.cisnux.contactapi.entity.User;
import dev.cisnux.contactapi.model.AddressResponse;
import dev.cisnux.contactapi.model.CreateAddressRequest;
import dev.cisnux.contactapi.model.UpdateAddressRequest;

import java.util.List;

public interface AddressService {
    AddressResponse create(User user, CreateAddressRequest createAddressRequest);

    AddressResponse get(User user, String contactId, String addressId);

    AddressResponse update(User user, UpdateAddressRequest updateAddressRequest);

    void delete(User user, String contactId, String addressId);

    List<AddressResponse> list(User user, String contactId);
}
