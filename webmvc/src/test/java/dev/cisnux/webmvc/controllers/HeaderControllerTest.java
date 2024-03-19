package dev.cisnux.webmvc.controllers;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HeaderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void headerOk() throws Exception {
        mockMvc.perform(
                get("/header/token")
                        .header("X-TOKEN", "FAJRA")
        ).andExpectAll(
                status().isOk(),
                content().string(Matchers.containsString("OK")
                )
        );
    }

    @Test
    void headerKo() throws Exception {
        mockMvc.perform(
                get("/header/token")
                        .header("X-TOKEN", "FArja")
        ).andExpectAll(
                status().isOk(),
                content().string(Matchers.containsString("KO")
                )
        );
    }
}