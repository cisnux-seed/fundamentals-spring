package dev.cisnux.contactapi.service;

import dev.cisnux.contactapi.entity.Contact;
import dev.cisnux.contactapi.entity.User;
import dev.cisnux.contactapi.model.ContactResponse;
import dev.cisnux.contactapi.model.CreateContactRequest;
import dev.cisnux.contactapi.model.SearchContactRequest;
import dev.cisnux.contactapi.model.UpdateContactRequest;
import dev.cisnux.contactapi.repostory.ContactRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;
    private final ValidationService validationService;

    @Transactional
    @Override
    public ContactResponse create(User user, CreateContactRequest createContactRequest) {
        validationService.validateObject(createContactRequest);

        final var contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName(createContactRequest.firstName());
        contact.setLastName(createContactRequest.lastName());
        contact.setEmail(createContactRequest.email());
        contact.setPhone(createContactRequest.phone());
        contact.setUser(user);

        contactRepository.save(contact);

        return ContactResponse
                .builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public ContactResponse get(User user, String id) {
        final var contact = contactRepository.findFirstByUserAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact is not found"));
        return ContactResponse.builder()
                .id(contact.getId())
                .email(contact.getEmail())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .phone(contact.getPhone())
                .build();
    }

    @Transactional
    @Override
    public ContactResponse update(User user, UpdateContactRequest updateContactRequest) {
        validationService.validateObject(updateContactRequest);
        final var contact = contactRepository.findFirstByUserAndId(user, updateContactRequest.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact is not found"));

        contact.setFirstName(updateContactRequest.firstName());
        contact.setLastName(updateContactRequest.lastName());
        contact.setEmail(updateContactRequest.email());
        contact.setPhone(updateContactRequest.phone());

        contactRepository.save(contact);

        return ContactResponse.builder()
                .id(contact.getId())
                .email(contact.getEmail())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .phone(contact.getPhone())
                .build();
    }

    @Transactional
    @Override
    public void delete(User user, String contactId) {
        final var contact = contactRepository.findFirstByUserAndId(user, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact is not found"));

        contactRepository.delete(contact);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ContactResponse> search(User user, SearchContactRequest request) {
        final var specification = (Specification<Contact>) (root, query, builder) -> {
            final var predicates = new ArrayList<Predicate>();
            predicates.add(builder.equal(root.get("user"), user));

            Optional.ofNullable(request.name()).ifPresent(
                    name -> {
                        log.info("name: " + name);
                        predicates.add(builder.or(
                                builder.like(root.get("firstName"), '%' + name + '%'),
                                builder.like(root.get("lastName"), '%' + name + '%'))
                        );
                    }
            );

            Optional.ofNullable(request.email()).ifPresent(
                    email -> {
                        log.info("email: " + email);
                        predicates.add(builder.like(root.get("email"), '%' + email + '%'));
                    }
            );

            Optional.ofNullable(request.phone()).ifPresent(
                    phone -> {
                        log.info("phone: " + phone);
                        predicates.add(builder.like(root.get("phone"), '%' + phone + '%'));
                    }
            );

            log.info("predicates : " + predicates.size());

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        final var pageable = PageRequest.of(request.page(), request.size());
        final var contacts = contactRepository.findAll(specification, pageable);
        log.info("contacts: " + contacts.getContent());
        final var contactResponses = contacts.getContent().stream().map(contact -> ContactResponse.builder()
                        .id(contact.getId())
                        .firstName(contact.getFirstName())
                        .lastName(contact.getLastName())
                        .email(contact.getEmail())
                        .phone(contact.getPhone())
                        .build())
                .toList();

        return new PageImpl<>(contactResponses, pageable, contacts.getTotalPages());
    }
}
