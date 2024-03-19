package dev.cisnux.webmvc.interceptors;

import dev.cisnux.webmvc.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final var session = request.getSession(true);
        final var user = (User) session.getAttribute("user");
        if(user == null){
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}
