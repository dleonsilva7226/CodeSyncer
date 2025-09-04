package com.codesyncer.backend.model;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserLocation {
    private Long userId;
    private String fileName;
    private Integer lineNumber;
    private Long mergeId;
    private Instant timestamp;

    public UserLocation() {}

    public UserLocation(Long userId, Long mergeId, String fileName, int lineNumber, Instant timestamp) {
        this.userId = userId;
        this.mergeId = mergeId;
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.timestamp = timestamp;
    }

}
