package dev.cisnux.webmvc.controllers;

import dev.cisnux.webmvc.services.HelloService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HelloControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HelloService service;

    @BeforeEach
    void setUp() {
        Mockito.when(service.hello(Mockito.anyString()))
                .thenReturn("Hello guys");
    }

    @Test
    void helloName() throws Exception {
        mockMvc.perform(get("/hello").queryParam("name", "cisnux")).andExpectAll(status().isOk(), content().string(Matchers.containsString("Hello guys")));
    }
}
