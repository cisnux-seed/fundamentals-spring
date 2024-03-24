package dev.cisnux.contactapi.resolver;

import dev.cisnux.contactapi.entity.User;
import dev.cisnux.contactapi.repostory.UserRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return User.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("run");
        final var servletRequest = ((HttpServletRequest) webRequest.getNativeRequest());
        final var token = servletRequest.getHeader("X-API-TOKEN");
        log.info("TOKEN {}", token);
        if (StringUtils.isBlank(token))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");

        final var user = userRepository.findFirstByToken(token).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

        log.info("USER {}", user);
        if (user.getTokenExpired() < Instant.now().toEpochMilli())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");

        return user;
    }
}
