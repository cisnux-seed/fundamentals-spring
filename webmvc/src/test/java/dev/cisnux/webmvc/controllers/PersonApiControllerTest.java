package dev.cisnux.webmvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cisnux.webmvc.models.CreatePersonRequest;
import dev.cisnux.webmvc.models.CreateSocialMediaRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PersonApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPerson() throws Exception {
        final var request = new CreatePersonRequest();
        request.setFirstName("Cisnux");
        request.setMiddleName("Fajra");
        request.setLastName("Risqulla");
        request.setEmail("fajra@gmail.com");
        request.setPhone("08213123");
        request.setHobbies(List.of("Coding", "Reading", "Gaming"));
        request.setSocialMedias(new ArrayList<>());
        request.getSocialMedias().add(new CreateSocialMediaRequest("Facebook", "facebook.com/ProgrammerZamanNow"));
        request.getSocialMedias().add(new CreateSocialMediaRequest("Instagram", "instagram.com/ProgrammerZamanNow"));

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                post("/api/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
        ).andExpectAll(
                status().isCreated(),
                content().json(jsonRequest)
        );
    }

    @Test
    void createPersonValidationError() throws Exception{
        final var request = new CreatePersonRequest();
        request.setMiddleName("Fajra");
        request.setLastName("Risqulla");
        request.setEmail("fajra@gmail");
        request.setHobbies(List.of("", "Reading", "Gaming"));
        request.setSocialMedias(new ArrayList<>());
        request.getSocialMedias().add(new CreateSocialMediaRequest("Facebook", "facebook.com/ProgrammerZamanNow"));
        request.getSocialMedias().add(new CreateSocialMediaRequest("Instagram", "instagram.com/ProgrammerZamanNow"));

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                post("/api/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
        ).andExpectAll(
                status().isBadRequest(),
                content().string(Matchers.containsString("Validation Error"))
        );
    }
}