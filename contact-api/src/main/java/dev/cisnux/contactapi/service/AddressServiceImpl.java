package dev.cisnux.contactapi.service;

import dev.cisnux.contactapi.entity.Address;
import dev.cisnux.contactapi.entity.User;
import dev.cisnux.contactapi.model.AddressResponse;
import dev.cisnux.contactapi.model.CreateAddressRequest;
import dev.cisnux.contactapi.model.UpdateAddressRequest;
import dev.cisnux.contactapi.repostory.AddressRepository;
import dev.cisnux.contactapi.repostory.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final ContactRepository contactRepository;
    private final AddressRepository addressRepository;
    private final ValidationService validationService;

    @Transactional
    @Override
    public AddressResponse create(User user, CreateAddressRequest createAddressRequest) {
        validationService.validateObject(createAddressRequest);

        final var contact = contactRepository.findFirstByUserAndId(user, createAddressRequest.contactId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact is not found"));

        final var address = new Address();
        address.setId(UUID.randomUUID().toString());
        address.setContact(contact);
        address.setStreet(createAddressRequest.street());
        address.setProvince(createAddressRequest.province());
        address.setCity(createAddressRequest.city());
        address.setCountry(createAddressRequest.country());
        address.setPostalCode(createAddressRequest.postalCode());

        addressRepository.save(address);

        return AddressResponse.builder()
                .id(address.getId())
                .country(address.getCountry())
                .street(address.getStreet())
                .city(address.getCity())
                .postalCode(address.getPostalCode())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public AddressResponse get(User user, String contactId, String addressId) {
        final var contact = contactRepository.findFirstByUserAndId(user, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact is not found"));

        final var address = addressRepository.findFirstByContactAndId(contact, addressId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "address is not found"));

        return AddressResponse.builder()
                .id(address.getId())
                .province(address.getProvince())
                .postalCode(address.getPostalCode())
                .street(address.getStreet())
                .city(address.getCity())
                .country(address.getCountry())
                .build();
    }

    @Transactional
    @Override
    public AddressResponse update(User user, UpdateAddressRequest updateAddressRequest) {
        validationService.validateObject(updateAddressRequest);

        final var contact = contactRepository.findFirstByUserAndId(user, updateAddressRequest.contactId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact is not found"));

        final var address = addressRepository.findFirstByContactAndId(contact, updateAddressRequest.addressId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "address is not found"));

        address.setCity(updateAddressRequest.city());
        address.setCountry(updateAddressRequest.country());
        address.setPostalCode(updateAddressRequest.postalCode());
        address.setStreet(updateAddressRequest.street());
        address.setProvince(updateAddressRequest.province());
        addressRepository.save(address);

        return AddressResponse.builder()
                .id(address.getId())
                .province(address.getProvince())
                .postalCode(address.getPostalCode())
                .street(address.getStreet())
                .city(address.getCity())
                .country(address.getCountry())
                .build();
    }


    @Transactional
    @Override
    public void delete(User user, String contactId, String addressId) {
        final var contact = contactRepository.findFirstByUserAndId(user, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact is not found"));

        final var address = addressRepository.findFirstByContactAndId(contact, addressId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "address is not found"));

        addressRepository.delete(address);
    }

    @Override
    public List<AddressResponse> list(User user, String contactId) {
        final var contact = contactRepository.findFirstByUserAndId(user, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact is not found"));

        return addressRepository.findAllByContact(contact).stream()
                .map(address -> AddressResponse.builder()
                        .id(address.getId())
                        .province(address.getProvince())
                        .postalCode(address.getPostalCode())
                        .street(address.getPostalCode())
                        .city(address.getCity())
                        .country(address.getCountry())
                        .build())
                .toList();
    }
}
