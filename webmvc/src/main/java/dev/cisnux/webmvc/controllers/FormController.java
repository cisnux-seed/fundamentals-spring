package dev.cisnux.webmvc.controllers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class FormController {
    @PostMapping(path = "/form/hello", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String hello(@RequestParam(name = "name") String name) {
        return """
                <html>
                <body><h1>Hello $name</h1></body>
                </html>
                """.replace("$name", name);
    }

    @PostMapping(path = "/form/person", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public String createPerson(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "birthDate") LocalDate birthDate,
            @RequestParam(name = "address") String address
    ) {
        return "Success create Person with name : " + name +
                ", birthDate : " + DateTimeFormatter.ISO_LOCAL_DATE.format(birthDate) +
                ", address : " + address;
    }
}
