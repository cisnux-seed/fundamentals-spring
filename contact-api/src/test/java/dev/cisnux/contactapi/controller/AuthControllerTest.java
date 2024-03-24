package dev.cisnux.contactapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cisnux.contactapi.entity.User;
import dev.cisnux.contactapi.model.LoginRequest;
import dev.cisnux.contactapi.model.TokenResponse;
import dev.cisnux.contactapi.model.WebResponse;
import dev.cisnux.contactapi.repostory.UserRepository;
import dev.cisnux.contactapi.resolver.UserArgumentResolver;
import dev.cisnux.contactapi.service.AuthService;
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
@DisplayName("authentication scenarios")
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserArgumentResolver userArgumentResolver;

    @MockBean
    private UserRepository userRepository;

    @DisplayName("when login")
    @Nested
    public class LoginTest {
        @DisplayName("by valid request body then should be success (200)")
        @Test
        public void loginByValidRequestBody_shouldSuccess() throws Exception {
            final var loginRequest = LoginRequest.builder().username("cisnux").password("cisnux123").build();
            final var expectedTokenResponse = TokenResponse
                    .builder()
                    .token("dummy token")
                    .expiredAt(Instant.now().toEpochMilli())
                    .build();

            when(authService.login(loginRequest)).thenReturn(expectedTokenResponse);

            mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(loginRequest))).andExpectAll(status().isOk()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<TokenResponse>>() {
                });
                assertNotNull(webResponse.data());
                assertNotNull(webResponse.data().token());
                assertNotNull(webResponse.data().expiredAt());

                assertEquals(expectedTokenResponse, webResponse.data());
            });

            verify(authService, times(1))
                    .login(loginRequest);
        }

        @DisplayName("by invalid name then should return unauthorized (401)")
        @Test
        public void loginByInvalidUsername_ShouldReturnUnauthorized() throws Exception {
            final var invalidLoginRequest = LoginRequest.builder().username("csx").password("cisnux123").build();

            doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "name or password is wrong")).when(authService).login(invalidLoginRequest);

            mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(invalidLoginRequest))).andExpectAll(status().isUnauthorized()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<TokenResponse>>() {
                });
                assertNotNull(webResponse.errors());
                assertEquals(webResponse.errors(), "name or password is wrong");
            });

            verify(authService, times(1)).login(invalidLoginRequest);
        }

        @DisplayName("by invalid password then should return unauthorized (401)")
        @Test
        public void loginByInvalidPassword_ShouldReturnUnauthorized() throws Exception {
            final var invalidLoginRequest = LoginRequest.builder().username("cisnux").password("csx123").build();

            doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "name or password is wrong")).when(authService).login(invalidLoginRequest);

            mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(invalidLoginRequest))).andExpectAll(status().isUnauthorized()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<TokenResponse>>() {
                });
                assertNotNull(webResponse.errors());
                assertEquals(webResponse.errors(), "name or password is wrong");
            });

            verify(authService, times(1)).login(invalidLoginRequest);
        }

        @DisplayName("by empty name then should return bad request (400)")
        @Test
        public void loginByEmptyUsername_ShouldReturnBadRequest() throws Exception {
            final var loginRequest = LoginRequest.builder().username("").password("cisnux123").build();

            doThrow(new ConstraintViolationException("name is empty", null)).when(authService).login(loginRequest);

            mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(loginRequest))).andExpectAll(status().isBadRequest()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.errors());
            });

            verify(authService, times(1)).login(loginRequest);
        }

        @DisplayName("by null name then should return bad request (400)")
        @Test
        public void loginByNullUsername_ShouldReturnBadRequest() throws Exception {
            final var loginRequest = LoginRequest.builder().username(null).password("cisnux123").build();

            doThrow(new ConstraintViolationException("name is null", null)).when(authService).login(loginRequest);

            mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(loginRequest))).andExpectAll(status().isBadRequest()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.errors());
            });

            verify(authService, times(1)).login(loginRequest);
        }

        @DisplayName("by empty password then should return bad request (400)")
        @Test
        public void loginByEmptyPassword_ShouldReturnBadRequest() throws Exception {
            final var loginRequest = LoginRequest.builder().username("cisnux").password("").build();

            doThrow(new ConstraintViolationException("password is empty", null)).when(authService).login(loginRequest);

            mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(loginRequest))).andExpectAll(status().isBadRequest()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.errors());
            });

            verify(authService, times(1)).login(loginRequest);
        }

        @DisplayName("by null password then should return bad request (400)")
        @Test
        public void loginByNullPassword_ShouldReturnBadRequest() throws Exception {
            final var loginRequest = LoginRequest.builder().username("cisnux").password(null).build();

            doThrow(new ConstraintViolationException("password is null", null)).when(authService).login(loginRequest);

            mockMvc.perform(post("/api/auth/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(loginRequest))).andExpectAll(status().isBadRequest()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.errors());
            });

            verify(authService, times(1)).login(loginRequest);
        }
    }

    @DisplayName("when logout")
    @Nested
    class Logout {
        @DisplayName("by invalid token then should return unauthorized (401)")
        @Test
        void logoutByInvalidToken_ThenShouldReturnUnauthorized() throws Exception {
            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().minus(Period.ofDays(10)).toEpochMilli());

            when(userRepository.findFirstByToken(Mockito.any())).thenReturn(Optional.of(user));
            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
            doNothing()
                    .when(authService).logout(user);

            mockMvc.perform(delete("/api/auth/logout").accept(MediaType.APPLICATION_JSON)).andExpectAll(status().isUnauthorized()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.errors());
            });
        }

        @DisplayName("by invalid token then should return status ok (200)")
        @Test
        void logoutByValidToken_ThenShouldReturnUnauthorized() throws Exception {
            final var user = new User();
            user.setUsername("cisnux");
            user.setName("Cisnux Risqulla");
            user.setPassword("cisnux123");
            user.setToken("token");
            user.setTokenExpired(Instant.now().plus(Period.ofDays(10)).toEpochMilli());

            when(userRepository.findFirstByToken(Mockito.any())).thenReturn(Optional.of(user));
            when(userArgumentResolver.supportsParameter(Mockito.any())).thenReturn(true);
            when(userArgumentResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(user);
            doNothing()
                    .when(authService).logout(user);

            mockMvc.perform(delete("/api/auth/logout").accept(MediaType.APPLICATION_JSON)).andExpectAll(status().isOk()).andDo(result -> {
                final var webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                });
                assertNotNull(webResponse.data());
            });
        }
    }
}