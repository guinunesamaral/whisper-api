package com.techgn.whisper.utils;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class StringUtils {
    public static Function<String, Boolean> stringHasValue = s -> s != null && !s.isEmpty();
    public static BiFunction<String, String, Boolean> stringsAreDifferent = (s1, s2) -> !Objects.equals(s1, s2);
}
