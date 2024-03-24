package dev.cisnux.contactapi.controller;

import dev.cisnux.contactapi.entity.User;
import dev.cisnux.contactapi.model.*;
import dev.cisnux.contactapi.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;

    @PostMapping(path = "/api/contacts", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public WebResponse<ContactResponse> create(User user, @RequestBody CreateContactRequest createContactRequest) {
        final var contactResponse = contactService.create(user, createContactRequest);
        return WebResponse.<ContactResponse>builder().data(contactResponse).build();
    }

    @GetMapping(path = "/api/contacts/{contactId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<ContactResponse> get(User user, @PathVariable("contactId") String contactId) {
        final var contactResponse = contactService.get(user, contactId);
        return WebResponse.<ContactResponse>builder().data(contactResponse).build();
    }

    @PutMapping(path = "/api/contacts/{contactId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<ContactResponse> update(User user, @PathVariable("contactId") String contactId, @RequestBody UpdateContactRequest updateContactRequest) {
        final var contactResponse = contactService.update(user, updateContactRequest.withId(contactId));
        return WebResponse.<ContactResponse>builder().data(contactResponse).build();
    }

    @DeleteMapping(path = "/api/contacts/{contactId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<String> delete(User user, @PathVariable("contactId") String contactId) {
        contactService.delete(user, contactId);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(path = "/api/contacts", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<List<ContactResponse>> search(
            User user,
            @RequestParam(value = "name", required = false)
            String name,
            @RequestParam(value = "email", required = false)
            String email,
            @RequestParam(value = "phone", required = false)
            String phone,
            @RequestParam(value = "page", required = false, defaultValue = "1")
            Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10")
            Integer size
    ) {
        final var searchContractRequest = SearchContactRequest.builder().name(name).email(email).phone(phone).page(page - 1).size(size).build();

        final var contactResponses = contactService.search(user, searchContractRequest);
        return WebResponse.<List<ContactResponse>>builder()
                .data(contactResponses.getContent())
                .pagingResponse(PagingResponse.builder()
                        .currentPage(contactResponses.getNumber() + 1)
                        .totalPage(contactResponses.getTotalPages())
                        .size(contactResponses.getSize())
                        .build())
                .build();
    }
}
