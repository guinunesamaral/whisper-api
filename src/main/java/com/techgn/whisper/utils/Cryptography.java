package com.techgn.whisper.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class Cryptography {
    public static Function<String, String> encodePassword = (password) -> new BCryptPasswordEncoder().encode(password);
    public static BiFunction<String, String, Boolean> passwordsMatch =
            (rawPassword, encodedPassword) -> new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
}
