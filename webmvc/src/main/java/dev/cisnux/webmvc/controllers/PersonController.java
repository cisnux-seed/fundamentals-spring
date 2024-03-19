package dev.cisnux.webmvc.controllers;

import dev.cisnux.webmvc.models.CreatePersonRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@Slf4j
public class PersonController {

    @PostMapping(value = "/person", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    // use @ModelAttribute for form request
    public ResponseEntity<String> createPerson(@Valid @ModelAttribute CreatePersonRequest request, BindingResult bindingResult) {
        log.info(request.toString());

        final var errors = bindingResult.getFieldErrors();
        if (!errors.isEmpty()) {

            errors.forEach(fieldError -> {
                System.out.println(fieldError.getField() + " : " + fieldError.getDefaultMessage());
            });

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You send invalid data");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Success create person " +
                request.getFirstName() + " " +
                request.getMiddleName() + " " +
                request.getLastName() + " " +
                "with email " + request.getEmail() + " " +
                "and phone " + request.getPhone() +
                " with address " +
                request.getAddress().getStreet() + ", " +
                request.getAddress().getCity() + ", " +
                request.getAddress().getCountry() + ", " +
                request.getAddress().getPostalCode());

    }
}
