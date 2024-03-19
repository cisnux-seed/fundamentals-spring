package dev.cisnux.webmvc.controllers;

import dev.cisnux.webmvc.models.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @PostMapping(path = "/auth/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> login(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse
    ) {
        if ("fajra".equals(username) && "fajra123".equals(password)) {
            // save session on server
            // if we have multiple app
            // every app will have different sessions
            final var session = servletRequest.getSession(true);
            session.setAttribute("user", new User(username));

            // save cookie on user browser
            final var cookie = new Cookie("username", username);
            cookie.setPath("/");
            servletResponse.addCookie(cookie);

            return ResponseEntity.status(HttpStatus.OK).body("OK");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("KO");
        }
    }

    @GetMapping(path = "/auth/user")
    public ResponseEntity<String> getUser(@CookieValue("username") String username){
        return ResponseEntity.ok("Hello " + username);
    }
}
