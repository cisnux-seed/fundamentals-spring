package dev.cisnux.webmvc.resolvers;

import dev.cisnux.webmvc.models.Partner;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

@Component
public class PartnerArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Partner.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final var servletRequest = ((HttpServletRequest) webRequest.getNativeRequest());
        final var apiKey = servletRequest.getHeader("X-API-KEY");
        return Optional.ofNullable(apiKey).map(s -> new Partner(s, "Sample Partner")).orElseThrow(
                () -> new RuntimeException("Unauthorized Exception")
        );
    }
}
