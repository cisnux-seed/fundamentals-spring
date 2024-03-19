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
class FormControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void formHello() throws Exception {
        mockMvc.perform(
                post("/form/hello")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .queryParam("name", "fajra")
        ).andExpectAll(status().isOk(), header().string(HttpHeaders.CONTENT_TYPE, Matchers.containsString(MediaType.TEXT_HTML_VALUE)), content().string(Matchers.containsString("Hello fajra")));
    }

    @Test
    void formCreate() throws Exception {
        mockMvc.perform(
                post("/form/person")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "fajra")
                        .param("birthDate", "2020-10-10")
                        .param("address", "Jakarta Selatan")
        ).andExpectAll(status().isOk(), content().string(Matchers.containsString("Success create Person with name : fajra, " +
                "birthDate : 2020-10-10, " +
                "address : Jakarta Selatan")));
    }
}