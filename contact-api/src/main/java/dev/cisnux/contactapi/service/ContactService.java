package dev.cisnux.contactapi.service;

import dev.cisnux.contactapi.entity.User;
import dev.cisnux.contactapi.model.ContactResponse;
import dev.cisnux.contactapi.model.CreateContactRequest;
import dev.cisnux.contactapi.model.SearchContactRequest;
import dev.cisnux.contactapi.model.UpdateContactRequest;
import org.springframework.data.domain.Page;

public interface ContactService {
    ContactResponse create(User user, CreateContactRequest createContactRequest);

    ContactResponse get(User user, String id);

    ContactResponse update(User user, UpdateContactRequest updateContactRequest);

    void delete(User user, String contactId);

    Page<ContactResponse> search(User user, SearchContactRequest searchContactRequest);
}
