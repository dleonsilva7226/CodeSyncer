package com.codesyncer.backend.exceptions;

public class ContentLimitExceededException extends RuntimeException {
    private final long totalContentLength;
    private final long maxContentLength;

    public ContentLimitExceededException(String errorMessage, long totalContentLength, long maxContentLength) {
        super(errorMessage);
        this.totalContentLength = totalContentLength;
        this.maxContentLength = maxContentLength;
    }
}
