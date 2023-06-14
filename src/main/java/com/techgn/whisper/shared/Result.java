package com.techgn.whisper.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Result<T> {
    private static final String DEFAULT_MESSAGE = "Success";

    @Setter(AccessLevel.NONE)
    private String message;
    private T value;

    public Result() {
        this.message = DEFAULT_MESSAGE;
        this.value = null;
    }

    public Result(T value) {
        this.message = DEFAULT_MESSAGE;
        this.value = value;
    }

    public <U> Result(Result<U> result) {
        this.message = result.message;
        if (this.value != null && this.value.getClass().equals(result.value.getClass())) {
            this.value = (T) result.value;
        }
    }

    public Result(String message, T value) {
        this.message = message;
        this.value = value;
    }

    public Result(String message) {
        this.message = message;
    }

    public void addValidation(String message) {
        if (Objects.equals(this.message, DEFAULT_MESSAGE)) {
            this.message = "";
        }
        if (Objects.equals(this.message, "")) {
            this.message += String.format("%s", message);
        } else {
            this.message += String.format("\n%s", message);
        }
    }

    public Result<T> setBusinessMessage(String message) {
        this.message = message;
        return this;
    }

    public <U> Result<T> setFromAnother(Result<U> result) {
        this.message = result.message;
        if (this.value != null && this.value.getClass().equals(result.value.getClass())) {
            this.value = (T) result.value;
        }
        return this;
    }

    @JsonIgnore
    public boolean isInvalid() {
        return !Objects.equals(message, DEFAULT_MESSAGE) || message.length() == 0;
    }
}
