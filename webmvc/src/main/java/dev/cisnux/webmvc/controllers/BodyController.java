package dev.cisnux.webmvc.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cisnux.webmvc.models.HelloRequest;
import dev.cisnux.webmvc.models.HelloResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BodyController {
    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostMapping(
            path = "/body/hello",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public String body(@RequestBody String requestBody) throws JsonProcessingException {
        final var request = objectMapper.readValue(requestBody, HelloRequest.class);

        final var response = new HelloResponse("Hello " + request.name());

        return objectMapper.writeValueAsString(response);
    }
}
