package dev.cisnux.contactapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cisnux.contactapi.entity.User;
import dev.cisnux.contactapi.model.*;
import dev.cisnux.contactapi.resolver.UserArgumentResolver;
import dev.cisnux.contactapi.service.ContactService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.Period;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserArgumentResolver userArgumentResolver;

    @MockBean
    private ContactService contactService;

    @Nested
    @DisplayName("when create")
    class CreateTest {
        @DisplayName("by invalid token should return unauthorized (401)")
        @Test
        void crateContactByInvalidToken_ShouldReturnUnauthorized() throws Exception {
            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().plus(Period.ofDays(10)).toEpochMilli());

            final var createContactRequest = CreateContactRequest.builder()
                    .firstName("cisnux")
                    .lastName("risqulla")
                    .email("csxexample.com")
                    .build();

            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

            mockMvc.perform(post("/api/contacts")
                    .header("X-API-TOKEN", "invalid token")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createContactRequest))
            ).andExpectAll(status().isUnauthorized()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.errors());
            });
        }

        @DisplayName("by invalid email should return bad request (400)")
        @Test
        void crateContactByInvalidEmail_ShouldReturnBadRequest() throws Exception {
            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().plus(Period.ofDays(10)).toEpochMilli());

            final var createContactRequest = CreateContactRequest.builder()
                    .firstName("cisnux")
                    .lastName("risqulla")
                    .email("csxexample.com")
                    .build();

            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(user);
            when(contactService.create(Mockito.any(), Mockito.any())).thenThrow(new ConstraintViolationException("invalid email", null));

            mockMvc.perform(post("/api/contacts")
                    .header("X-API-TOKEN", "token")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createContactRequest))
            ).andExpectAll(status().isBadRequest()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.errors());
            });
        }

        @DisplayName("by valid token and request body should return created status (201)")
        @Test
        void createContactByValidBodyRequestAndToken_ShouldCreated() throws Exception {
            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().plus(Period.ofDays(10)).toEpochMilli());

            final var createContactRequest = CreateContactRequest.builder()
                    .firstName("cisnux")
                    .lastName("risqulla")
                    .email("csxexample@gmail.com")
                    .build();

            final var expectedContactResp = ContactResponse.builder()
                    .id("id-1")
                    .firstName(createContactRequest.firstName())
                    .lastName(createContactRequest.lastName())
                    .email(createContactRequest.email())
                    .build();

            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(user);
            when(contactService.create(Mockito.any(), Mockito.any())).thenReturn(expectedContactResp);

            mockMvc.perform(post("/api/contacts")
                    .header("X-API-TOKEN", "token")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createContactRequest))
            ).andExpectAll(status().isCreated()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<ContactResponse>>() {
                });
                assertNotNull(webResponse.data());
                assertEquals(expectedContactResp, webResponse.data());
            });
        }
    }

    @DisplayName("when get contact")
    @Nested
    class GetTest {
        @DisplayName("by valid token and not found should return not found (404)")
        @Test
        void getContactByValidTokenAndNotFoundId() throws Exception {
            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().plus(Period.ofDays(10)).toEpochMilli());


            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(user);
            when(contactService.get(Mockito.any(), Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "contact is not found"));

            mockMvc.perform(get("/api/contacts/19191").header("X-API-TOKEN", "token").accept(MediaType.APPLICATION_JSON)).andExpectAll(status().isNotFound()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.errors());
            });
        }

        @DisplayName("by valid token and existed id should return contact response (200)")
        @Test
        void getContactByValidTokenAndExistedId() throws Exception {
            final var contactResponse = ContactResponse.builder()
                    .id("id-1")
                    .phone("012029392")
                    .email("cisnux@gmail.com")
                    .firstName("cisnux")
                    .lastName("risqulla")
                    .build();

            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().plus(Period.ofDays(10)).toEpochMilli());


            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(user);
            when(contactService.get(Mockito.any(), Mockito.any())).thenReturn(contactResponse);

            mockMvc.perform(get("/api/contacts/19191").header("X-API-TOKEN", "token").accept(MediaType.APPLICATION_JSON)).andExpectAll(status().isOk()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<ContactResponse>>() {
                });
                assertNotNull(webResponse.data());
                assertEquals(contactResponse, webResponse.data());
            });
        }
    }

    @DisplayName("when update contact")
    @Nested
    class UpdateTest {
        @DisplayName("by valid token and not found should return not found (404)")
        @Test
        void updateContactByValidTokenAndNotFoundId() throws Exception {
            final var updateContactRequest = UpdateContactRequest.builder()
                    .firstName("fajra")
                    .lastName("risqulla")
                    .phone("029292939")
                    .email("newemail@gmail.com")
                    .build();

            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().plus(Period.ofDays(10)).toEpochMilli());


            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(user);
            when(contactService.update(Mockito.any(), Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "contact is not found"));

            mockMvc.perform(put("/api/contacts/19191").header("X-API-TOKEN", "token").content(objectMapper.writeValueAsString(updateContactRequest)).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)).andExpectAll(status().isNotFound()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.errors());
            });
        }

        @DisplayName("by valid token and not found should return contact response (200)")
        @Test
        void updateContactByValidTokenAndExistedId() throws Exception {
            final var updateContactRequest = UpdateContactRequest.builder()
                    .firstName("fajra")
                    .lastName("risqulla")
                    .phone("029292939")
                    .email("newemail@gmail.com")
                    .build();

            final var contactResponse = ContactResponse.builder()
                    .id("id-1")
                    .phone(updateContactRequest.phone())
                    .email(updateContactRequest.email())
                    .firstName(updateContactRequest.firstName())
                    .lastName(updateContactRequest.lastName())
                    .build();

            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().plus(Period.ofDays(10)).toEpochMilli());


            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(user);
            when(contactService.update(Mockito.any(), Mockito.any())).thenReturn(contactResponse);

            mockMvc.perform(put("/api/contacts/id-1").header("X-API-TOKEN", "token").content(objectMapper.writeValueAsString(updateContactRequest)).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)).andExpectAll(status().isOk()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<ContactResponse>>() {
                });
                assertNotNull(webResponse.data());
                assertEquals(contactResponse, webResponse.data());
            });
        }
    }

    @DisplayName("when delete")
    @Nested
    class DeleteTest {
        @DisplayName("by valid token and not found should return not found (404)")
        @Test
        void deleteContactByValidTokenAndNotFoundId() throws Exception {
            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().plus(Period.ofDays(10)).toEpochMilli());


            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(user);
            doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "contact is not found"))
                    .when(contactService)
                    .delete(Mockito.any(), Mockito.any());

            mockMvc.perform(delete("/api/contacts/19191").header("X-API-TOKEN", "token").accept(MediaType.APPLICATION_JSON)).andExpectAll(status().isNotFound()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.errors());
            });
        }

        @DisplayName("by valid token and not found should return contact response (200)")
        @Test
        void deleteContactByValidTokenAndExistedId() throws Exception {
            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().plus(Period.ofDays(10)).toEpochMilli());

            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(user);
            doNothing()
                    .when(contactService)
                    .delete(Mockito.any(), Mockito.any());

            mockMvc.perform(delete("/api/contacts/id-1").header("X-API-TOKEN", "token").accept(MediaType.APPLICATION_JSON)).andExpectAll(status().isOk()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.data());
            });
        }
    }

    @Nested
    @DisplayName("when search")
    class SearchTest {

        @DisplayName("by name should return contacts")
        @Test
        public void searchByName() throws Exception {
            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().plus(Period.ofDays(10)).toEpochMilli());

            final var contacts = new PageImpl<>(Stream.of(10).map(integer -> ContactResponse
                    .builder()
                    .firstName("first name " + integer)
                    .lastName("last name " + integer)
                    .email("test" + integer + "@gmail.com")
                    .phone("102929292" + integer).build()).toList(), PageRequest.of(0, 2), 10);

            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(user);

            when(contactService.search(Mockito.any(), Mockito.any())).thenReturn(contacts);

            mockMvc.perform(get("/api/contacts")
                    .header("X-API-TOKEN", "token")
                    .accept(MediaType.APPLICATION_JSON)
                    .queryParam("name", "first")
            ).andExpectAll(status().isOk()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<ContactResponse>>>() {
                });
                assertNotNull(webResponse.data());
                assertEquals(contacts.getContent().size(), webResponse.data().size());
            });
        }

        @DisplayName("by email should return contacts")
        @Test
        public void searchByEmailAndPhone() throws Exception {
            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().plus(Period.ofDays(10)).toEpochMilli());

            final var contacts = new PageImpl<>(Stream.of(10).map(integer -> ContactResponse
                    .builder()
                    .firstName("first name " + integer)
                    .lastName("last name " + integer)
                    .email("test" + integer + "@gmail.com")
                    .phone("102929292" + integer).build()).toList(), PageRequest.of(0, 2), 10);

            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(user);
            when(contactService.search(Mockito.any(), Mockito.any())).thenReturn(contacts);

            mockMvc.perform(get("/api/contacts")
                    .header("X-API-TOKEN", "token")
                    .accept(MediaType.APPLICATION_JSON)
                    .queryParam("email", "test@gmail.com")
                    .queryParam("phone", "292993883")
            ).andExpectAll(status().isOk()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<ContactResponse>>>() {
                });
                assertNotNull(webResponse.data());
                assertEquals(contacts.getContent().size(), webResponse.data().size());
            });
        }

        @DisplayName("by invalid token should return unauthorized")
        @Test
        public void searchByInvalidToken_ShouldReturnUnauthorized() throws Exception {
            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().plus(Period.ofDays(10)).toEpochMilli());

            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

            mockMvc.perform(get("/api/contacts")
                    .header("X-API-TOKEN", "invalid_token")
                    .accept(MediaType.APPLICATION_JSON)
                    .queryParam("email", "test@gmail.com")
                    .queryParam("phone", "292993883")
            ).andExpectAll(status().isUnauthorized()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.errors());
            });
        }
    }

}

