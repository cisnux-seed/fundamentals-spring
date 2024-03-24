package dev.cisnux.contactapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cisnux.contactapi.entity.User;
import dev.cisnux.contactapi.model.*;
import dev.cisnux.contactapi.resolver.UserArgumentResolver;
import dev.cisnux.contactapi.service.AddressService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
class AddressControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserArgumentResolver userArgumentResolver;

    @MockBean
    private AddressService addressService;

    @DisplayName("when create")
    @Nested
    class CreateTest {
        @DisplayName("by not found id and valid body request then should return not found (404)")
        @Test
        void createByNotFoundIdAndValidBodyRequest_ShouldReturnNotFound() throws Exception {
            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().plus(Period.ofDays(10)).toEpochMilli());

            final var createAddressRequest = CreateAddressRequest.builder()
                    .country("indonesia")
                    .street("street test")
                    .postalCode("18282")
                    .province("Jambi")
                    .city("Jambi")
                    .build();


            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(user);
            when(addressService.create(Mockito.any(), Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "contact id is not found"));

            mockMvc.perform(post("/api/contacts/contact124124/addresses")
                    .header("X-API-TOKEN", "invalid_token")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createAddressRequest))
            ).andExpectAll(status().isNotFound()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.errors());
            });
        }

        @DisplayName("by existed id and valid body request then should return address response (201)")
        @Test
        void createByExistedIdAndValidBodyRequest_ShouldReturnAddressResponse() throws Exception {
            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().plus(Period.ofDays(10)).toEpochMilli());

            final var createAddressRequest = CreateAddressRequest.builder()
                    .country("indonesia")
                    .street("street test")
                    .postalCode("18282")
                    .province("Jambi")
                    .city("Jambi")
                    .build();

            final var addressResponse = AddressResponse.builder()
                    .id("new-address-id")
                    .country("indonesia")
                    .street("street test")
                    .postalCode("18282")
                    .province("Jambi")
                    .city("Jambi")
                    .build();

            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(user);
            when(addressService.create(Mockito.any(), Mockito.any())).thenReturn(addressResponse);

            mockMvc.perform(post("/api/contacts/contact124124/addresses")
                    .header("X-API-TOKEN", "invalid_token")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createAddressRequest))
            ).andExpectAll(status().isCreated()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<AddressResponse>>() {
                });
                assertNotNull(webResponse.data());
                assertEquals(addressResponse, webResponse.data());
            });
        }
    }

    @Nested
    @DisplayName("when get")
    class GetTest {
        @DisplayName("by not found address id then should return not found (404)")
        @Test
        void getAddressByNotFoundAddressId_ShouldReturnNotFound() throws Exception {
            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().plus(Period.ofDays(10)).toEpochMilli());

            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(user);
            when(addressService.get(Mockito.any(), Mockito.any(), Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "address id is not found"));

            mockMvc.perform(get("/api/contacts/1929349239/addresses/19292929")
                    .header("X-API-TOKEN", "validtoken")
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpectAll(status().isNotFound()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.errors());
            });
        }

        @DisplayName("by existed address id then should return contact response (200)")
        @Test
        void getAddressByExistedAddressId_ShouldReturnContactResponse() throws Exception {
            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().plus(Period.ofDays(10)).toEpochMilli());

            final var addressResponse = AddressResponse.builder()
                    .id("new-address-id")
                    .country("indonesia")
                    .street("street test")
                    .postalCode("18282")
                    .province("Jambi")
                    .city("Jambi")
                    .build();

            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(user);
            when(addressService.get(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(addressResponse);

            mockMvc.perform(get("/api/contacts/1929349239/addresses/19292929")
                    .header("X-API-TOKEN", "invalid_token")
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpectAll(status().isOk()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<AddressResponse>>() {
                });
                assertNotNull(webResponse.data());
                assertEquals(addressResponse, webResponse.data());
            });
        }
    }

    @Nested
    @DisplayName("when update")
    class UpdateTest {
        @DisplayName("by not found address id then should return not found (404)")
        @Test
        void updateAddressByNotFoundAddressId_ShouldReturnNotFound() throws Exception {
            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().plus(Period.ofDays(10)).toEpochMilli());

            final var updateAddressRequest = CreateAddressRequest.builder()
                    .country("indonesia")
                    .street("street test")
                    .postalCode("18282")
                    .province("Jambi")
                    .city("Jambi")
                    .build();

            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(user);
            when(addressService.update(Mockito.any(), Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "address id is not found"));

            mockMvc.perform(put("/api/contacts/1929349239/addresses/19292929")
                    .header("X-API-TOKEN", "validtoken")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateAddressRequest))
            ).andExpectAll(status().isNotFound()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.errors());
            });
        }

        @DisplayName("by existed address id then should return contact response (200)")
        @Test
        void updateAddressByExistedAddressId_ShouldReturnContactResponse() throws Exception {
            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().plus(Period.ofDays(10)).toEpochMilli());

            final var addressResponse = AddressResponse.builder()
                    .id("new-address-id")
                    .country("indonesia")
                    .street("street test")
                    .postalCode("18282")
                    .province("Jambi")
                    .city("Jambi")
                    .build();

            final var updateAddressRequest = CreateAddressRequest.builder()
                    .country("indonesia")
                    .street("street test")
                    .postalCode("18282")
                    .province("Jambi")
                    .city("Jambi")
                    .build();

            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(user);
            when(addressService.update(Mockito.any(), Mockito.any())).thenReturn(addressResponse);

            mockMvc.perform(put("/api/contacts/1929349239/addresses/19292929")
                    .header("X-API-TOKEN", "invalid_token")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateAddressRequest))
            ).andExpectAll(status().isOk()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<AddressResponse>>() {
                });
                assertNotNull(webResponse.data());
                assertEquals(addressResponse, webResponse.data());
            });
        }
    }

    @DisplayName("when remove")
    @Nested
    class RemoveTest {
        @DisplayName("by not found address id then should return not found (404)")
        @Test
        void removeAddressByNotFoundAddressId_ShouldReturnNotFound() throws Exception {
            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().plus(Period.ofDays(10)).toEpochMilli());

            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(user);
            doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "address id is not found"))
                    .when(addressService)
                    .delete(Mockito.any(), Mockito.any(), Mockito.any());

            mockMvc.perform(delete("/api/contacts/1929349239/addresses/19292929")
                    .header("X-API-TOKEN", "validtoken")
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpectAll(status().isNotFound()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.errors());
            });
        }

        @DisplayName("by existed address id then should return contact response (200)")
        @Test
        void deleteAddressByExistedAddressId_ShouldReturnContactResponse() throws Exception {
            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().plus(Period.ofDays(10)).toEpochMilli());

            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(user);
            doNothing()
                    .when(addressService).delete(Mockito.any(), Mockito.any(), Mockito.any());

            mockMvc.perform(delete("/api/contacts/1929349239/addresses/19292929")
                    .header("X-API-TOKEN", "invalid_token")
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpectAll(status().isOk()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.data());
            });
        }
    }

    @Nested
    @DisplayName("when get list")
    class GetListTest {
        @DisplayName("by not found address id then should return not found (404)")
        @Test
        void getListAddressByNotFoundAddressId_ShouldReturnNotFound() throws Exception {
            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().plus(Period.ofDays(10)).toEpochMilli());

            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(user);
            when(addressService.list(Mockito.any(), Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "address id is not found"));

            mockMvc.perform(get("/api/contacts/1929349239/addresses")
                    .header("X-API-TOKEN", "validtoken")
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpectAll(status().isNotFound()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.errors());
            });
        }

        @DisplayName("by existed address id then should return contact response (200)")
        @Test
        void getListAddressByExistedAddressId_ShouldReturnContactResponses() throws Exception {
            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().plus(Period.ofDays(10)).toEpochMilli());

            final var addressResponses = Stream.of(10)
                    .map(integer ->
                            AddressResponse.builder()
                                    .id("new-address-" + integer)
                                    .country("indonesia")
                                    .street("street test")
                                    .postalCode("18282")
                                    .province("Jambi")
                                    .city("Jambi")
                                    .build())
                    .toList();

            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(user);
            when(addressService.list(Mockito.any(), Mockito.any())).thenReturn(addressResponses);

            mockMvc.perform(get("/api/contacts/1929349239/addresses")
                    .header("X-API-TOKEN", "valid_token")
                    .accept(MediaType.APPLICATION_JSON)
            ).andExpectAll(status().isOk()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<AddressResponse>>>() {
                });
                assertNotNull(webResponse.data());
                assertEquals(addressResponses, webResponse.data());
            });

            verify(addressService, times(1)).list(Mockito.any(), Mockito.any());
        }
    }
}