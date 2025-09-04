package com.codesyncer.backend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationMessage {
    private long userId;
    private String fileName;
    private Integer lineNumber;
    private Long mergeId;

    public LocationMessage() {}

    public LocationMessage(long userId, String fileName, Integer lineNumber, Long mergeId) {
        this.userId = userId;
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.mergeId = mergeId;
    }
}
