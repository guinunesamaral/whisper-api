package com.techgn.whisper.model.interfaces;

public interface Command<T, U> {
    T handle(U object);
}