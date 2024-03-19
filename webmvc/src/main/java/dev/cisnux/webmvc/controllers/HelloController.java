package dev.cisnux.webmvc.controllers;

import dev.cisnux.webmvc.services.HelloService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Controller
public class HelloController {
    private HelloService helloService;

    @Autowired
    public void setHelloService(HelloService helloService) {
        this.helloService = helloService;
    }

//    @RequestMapping(path = "/hello", method = RequestMethod.GET)
//    @GetMapping(path = "/hello")
//    public void helloWorld(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        response.getWriter().write(helloService.hello(request.getParameter("name")));
//    }

    @GetMapping(path = "/hello")
    public void helloWorld(@RequestParam(name = "name", required = false) String name, HttpServletResponse response) throws IOException {
        response.getWriter().write(helloService.hello(name));
    }

    // will return bad request 400 if name is null
    // bcs name is required
    @GetMapping(path = "/hello/required")
    public void requiredHelloWorld(@RequestParam(name = "name") String name, HttpServletResponse response) throws IOException {
        response.getWriter().write(helloService.hello(name));
    }

    @GetMapping(path = "/web/hello")
    public ModelAndView hello(@RequestParam(name = "name", required = false) String name) {
        return Optional.ofNullable(name).map(s -> new ModelAndView("hello", Map.of(
                "title", "Belajar View",
                "name", name
        ))).orElseGet(() -> new ModelAndView("redirect:/web/hello?name=Guest"));
    }
}
