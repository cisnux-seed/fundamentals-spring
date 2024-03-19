package dev.cisnux.webmvc.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestTemplateTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    void addTodo() {
        final var url = "http://localhost:" + port + "/todos";

        final var headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        final var form = new LinkedMultiValueMap<String, Object>();
        form.add("todo", "Belajar Spring WebMVC");

        final var request = new RequestEntity<MultiValueMap<String, Object>>(form, headers, HttpMethod.POST, URI.create(url));

        // for list/array we can use parameterized otherwise, we can use class reflection
        final var response = restTemplate.exchange(request, new ParameterizedTypeReference<List<String>>() {
        });

        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Belajar Spring WebMVC", response.getBody().getFirst());
    }

    @Test
    void getTodos() {
        final var url = "http://localhost:" + port + "/todos";

        final var headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        final var request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));

        final var response = restTemplate.exchange(request, new ParameterizedTypeReference<List<String>>() {
        });

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Belajar Spring WebMVC", response.getBody().get(0));
    }
}