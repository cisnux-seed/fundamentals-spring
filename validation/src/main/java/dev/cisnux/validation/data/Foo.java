package dev.cisnux.validation.data;

import dev.cisnux.validation.constraint.Palindrome;

public record Foo(@Palindrome String bar) {
}
