package com.codesyncer.backend.exceptions;

public class NonTextFileException extends RuntimeException {
    public NonTextFileException(String message) { super(message); }
    public NonTextFileException(String message, Throwable cause) { super(message, cause); }
}
