package com.codesyncer.backend.exceptions;

public class FileTooLargeException extends Exception {
    private final long fileSize;
    private final long maxAllowedFileSize;

    public FileTooLargeException(String errorMessage, long fileSize, long maxAllowedFileSize) {
        super(errorMessage);
        this.fileSize = fileSize;
        this.maxAllowedFileSize = maxAllowedFileSize;
    }

    public long getFileSize() {
        return fileSize;
    }

    public long getMaxAllowedFileSize() {
        return maxAllowedFileSize;
    }
}
