package dev.cisnux.contactapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cisnux.contactapi.entity.User;
import dev.cisnux.contactapi.model.RegisterUserRequest;
import dev.cisnux.contactapi.model.UpdateUserRequest;
import dev.cisnux.contactapi.model.UserResponse;
import dev.cisnux.contactapi.model.WebResponse;
import dev.cisnux.contactapi.repostory.UserRepository;
import dev.cisnux.contactapi.resolver.UserArgumentResolver;
import dev.cisnux.contactapi.service.UserService;
import jakarta.validation.ConstraintViolationException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("user scenarios")
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserArgumentResolver userArgumentResolver;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("when user register")
    public class RegisterTest {
        @DisplayName("by valid request body then should be success (201)")
        @Test
        public void registerByValidRequestBody_shouldSuccess() throws Exception {
            final var registerUserRequest = RegisterUserRequest.builder().username("cisnux").password("cisnux123").name("Cisnux Risqulla").build();

            doNothing().when(userService).register(registerUserRequest);

            mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerUserRequest))).andExpectAll(status().isCreated()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.data());
                assertEquals("OK", webResponse.data());
            });

            verify(userService, times(1)).register(registerUserRequest);
        }

        @DisplayName("by existed name then should return bad request (400)")
        @Test
        public void registerByExistedUsername_ShouldReturnBadRequest() throws Exception {
            final var registerUserRequest = RegisterUserRequest.builder().username("cisnux").password("cisnux123").name("Cisnux Risqulla").build();

            doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "name has been used")).when(userService).register(registerUserRequest);

            mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerUserRequest))).andExpectAll(status().isBadRequest()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.errors());
                assertEquals(webResponse.errors(), "name has been used");
            });

            verify(userService, times(1)).register(registerUserRequest);
        }

        @DisplayName("by empty name then should return bad request (400)")
        @Test
        public void registerByEmptyUsername_ShouldReturnBadRequest() throws Exception {
            final var registerUserRequest = RegisterUserRequest.builder().username("").password("cisnux123").name("Cisnux Risqulla").build();

            doThrow(new ConstraintViolationException("name is empty", null)).when(userService).register(registerUserRequest);

            mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerUserRequest))).andExpectAll(status().isBadRequest()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.errors());
            });

            verify(userService, times(1)).register(registerUserRequest);
        }

        @DisplayName("by null name then should return bad request (400)")
        @Test
        public void registerByNullUsername_ShouldReturnBadRequest() throws Exception {
            final var registerUserRequest = RegisterUserRequest.builder().username(null).password("cisnux123").name("Cisnux Risqulla").build();

            doThrow(new ConstraintViolationException("name is null", null)).when(userService).register(registerUserRequest);

            mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerUserRequest))).andExpectAll(status().isBadRequest()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.errors());
            });

            verify(userService, times(1)).register(registerUserRequest);
        }

        @DisplayName("by empty password then should return bad request (400)")
        @Test
        public void registerByEmptyPassword_ShouldReturnBadRequest() throws Exception {
            final var registerUserRequest = RegisterUserRequest.builder().username("cisnux").password("").name("Cisnux Risqulla").build();

            doThrow(new ConstraintViolationException("password is empty", null)).when(userService).register(registerUserRequest);

            mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerUserRequest))).andExpectAll(status().isBadRequest()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.errors());
            });

            verify(userService, times(1)).register(registerUserRequest);
        }

        @DisplayName("by null password then should return bad request (400)")
        @Test
        public void registerByNullPassword_ShouldReturnBadRequest() throws Exception {
            final var registerUserRequest = RegisterUserRequest.builder().username("cisnux").password(null).name("Cisnux Risqulla").build();

            doThrow(new ConstraintViolationException("password is null", null)).when(userService).register(registerUserRequest);

            mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerUserRequest))).andExpectAll(status().isBadRequest()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.errors());
            });

            verify(userService, times(1)).register(registerUserRequest);
        }

        @DisplayName("by empty name then should return bad request (400)")
        @Test
        public void registerByEmptyName_ShouldReturnBadRequest() throws Exception {
            final var registerUserRequest = RegisterUserRequest.builder().username("cisnux").password("cisnux123").name("").build();

            doThrow(new ConstraintViolationException("name is empty", null)).when(userService).register(registerUserRequest);

            mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerUserRequest))).andExpectAll(status().isBadRequest()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.errors());
            });

            verify(userService, times(1)).register(registerUserRequest);
        }

        @DisplayName("by null name then should return bad request (400)")
        @Test
        public void registerByNullName_ShouldReturnBadRequest() throws Exception {
            final var registerUserRequest = RegisterUserRequest.builder().username("cisnux").password("cisnux123").name(null).build();

            doThrow(new ConstraintViolationException("name is null", null)).when(userService).register(registerUserRequest);

            mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerUserRequest))).andExpectAll(status().isBadRequest()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.errors());
            });

            verify(userService, times(1)).register(registerUserRequest);
        }
    }

    @Nested
    @DisplayName("when get user")
    class GetTest {
        @DisplayName("by invalid token then should return unauthorized (401)")
        @Test
        void getUserByInvalidToken_ShouldReturnUnauthorized() throws Exception {
            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

            mockMvc.perform(get("/api/users/current").accept(MediaType.APPLICATION_JSON).header("X-API-TOKEN", "invalid_token")).andExpectAll(status().isUnauthorized()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.errors());
            });
        }

        @DisplayName("by expired token then should return unauthorized (401)")
        @Test
        void getUserExpiredToken_ShouldReturnUnauthorized() throws Exception {
            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().minus(Period.ofDays(10)).toEpochMilli());

            when(userRepository.findFirstByToken(Mockito.any())).thenReturn(Optional.of(user));
            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

            mockMvc.perform(get("/api/users/current").header("X-API-TOKEN", "token").accept(MediaType.APPLICATION_JSON)).andExpectAll(status().isUnauthorized()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<UserResponse>>() {
                });
                assertNotNull(webResponse.errors());
            });
        }

        @DisplayName("by valid token then should return user response (200)")
        @Test
        void getUserByValidToken_ShouldReturnUserResponse() throws Exception {
            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().plus(Period.ofDays(10)).toEpochMilli());

            final var expectedUserResp = UserResponse.builder().name(user.getName()).username(user.getUsername()).build();

            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(user);
            when(userService.getUser(user)).thenReturn(expectedUserResp);

            mockMvc.perform(get("/api/users/current").header("X-API-TOKEN", "token").accept(MediaType.APPLICATION_JSON)).andExpectAll(status().isOk()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<UserResponse>>() {
                });
                assertNotNull(webResponse.data());
                assertEquals(expectedUserResp, webResponse.data());
            });
        }
    }

    @Nested
    @DisplayName("when update user")
    class UpdateTest {
        @DisplayName("by invalid token then should return unauthorized (401)")
        @Test
        void updateUserByInvalidToken_ShouldReturnUnauthorized() throws Exception {
            final var updateUserRequest = UpdateUserRequest.builder().build();

            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

            mockMvc.perform(patch("/api/users/current").content(objectMapper.writeValueAsString(updateUserRequest)).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).header("X-API-TOKEN", "invalid_token")).andExpectAll(status().isUnauthorized()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.errors());
            });
        }

        @DisplayName("by expired token then should return unauthorized (401)")
        @Test
        void updateUserExpiredToken_ShouldReturnUnauthorized() throws Exception {
            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().minus(Period.ofDays(10)).toEpochMilli());
            final var updateUserRequest = UpdateUserRequest.builder().build();

            when(userRepository.findFirstByToken(Mockito.any())).thenReturn(Optional.of(user));
            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

            mockMvc.perform(patch("/api/users/current").header("X-API-TOKEN", "token").content(objectMapper.writeValueAsString(updateUserRequest)).contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)).andExpectAll(status().isUnauthorized()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<UserResponse>>() {
                });
                assertNotNull(webResponse.errors());
            });
        }

        @DisplayName("by valid token then should return user response (200)")
        @Test
        void updateUserByValidToken_ShouldReturnUnauthorized() throws Exception {
            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().plus(Period.ofDays(10)).toEpochMilli());
            final var updateUserRequest = UpdateUserRequest
                    .builder()
                    .name("new name")
                    .build();

            final var expectedUserResp = UserResponse.builder()
                    .username(user.getName())
                    .name(updateUserRequest.name())
                    .build();


            when(userRepository.findFirstByToken(Mockito.any())).thenReturn(Optional.of(user));
            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(user);
            when(userService.updateUser(user, updateUserRequest)).thenReturn(expectedUserResp);

            mockMvc.perform(patch("/api/users/current").header("X-API-TOKEN", "token").content(objectMapper.writeValueAsString(updateUserRequest)).contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)).andExpectAll(status().isOk()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<UserResponse>>() {
                });
                assertNotNull(webResponse.data());
            });
        }
    }
}