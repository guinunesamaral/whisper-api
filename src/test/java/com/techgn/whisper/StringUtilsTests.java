package com.techgn.whisper;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.techgn.whisper.utils.StringUtils.stringHasValue;
import static com.techgn.whisper.utils.StringUtils.stringsAreDifferent;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringUtilsTests {

    @ParameterizedTest
    @ValueSource(strings = {"1", "c"})
    void stringHasValue(String s) {
        assertTrue(stringHasValue.apply(s));
    }

    @ParameterizedTest
    @CsvSource({"oi, Oi", "oi, tchau"})
    void stringsAreDifferent(String s1, String s2) {
        assertTrue(stringsAreDifferent.apply(s1, s2));
    }

    @ParameterizedTest
    @CsvSource({"oi, Oi"})
    void stringsAreEqual(String s1, String s2) {

    }
}
