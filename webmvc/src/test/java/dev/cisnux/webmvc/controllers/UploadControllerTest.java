package dev.cisnux.webmvc.controllers;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //.contentType(MediaType.MULTIPART_FORM_DATA_VALUE).param("name","cisnux"))
    @Test
    void uploadFile() throws Exception {
        mockMvc.perform(
                multipart("/upload/profile")
                        .file(
                                new MockMultipartFile("profile", "foto.png", "image/png", getClass().getResourceAsStream("/images/foto.png")
                                )
                        )
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .param("name", "cisnux")
        ).andExpectAll(
                status().isOk(),
                content().string(Matchers.containsString("Success save profile cisnux to upload/foto.png"))
        );
    }
}