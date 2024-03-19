package dev.cisnux.webmvc.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class DateController {

//    @GetMapping(path = "/date")
//    public void getDate(@RequestParam(name = "date") LocalDate date, HttpServletResponse response) throws IOException {
//        response.getWriter().write(DateTimeFormatter.ISO_LOCAL_DATE.format(date));
//    }

    @ResponseBody
    @GetMapping(path = "/date")
    public String getDate(@RequestParam(name = "date") LocalDate date, HttpServletResponse response) throws IOException {
        return DateTimeFormatter.ISO_LOCAL_DATE.format(date);
    }
}
