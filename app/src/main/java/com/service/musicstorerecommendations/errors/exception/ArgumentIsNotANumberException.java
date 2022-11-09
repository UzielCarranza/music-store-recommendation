package com.service.musicstorerecommendations.errors.exception;

public class ArgumentIsNotANumberException extends RuntimeException {

    public ArgumentIsNotANumberException() {
    }

    public ArgumentIsNotANumberException(String message) {
        super(message);
    }
}
