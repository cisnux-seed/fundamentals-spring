package dev.cisnux.validation.helper;

import org.springframework.stereotype.Component;

@Component
public class StringHelper {

    public boolean isPalindrome(String value) {
        final var reverse = new StringBuilder(value).reverse().toString();
        return value.equals(reverse);
    }
}
