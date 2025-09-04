package com.codesyncer.backend.model;

import java.time.LocalDateTime;
import java.util.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeSyncerEvent {
    private String type;
    private String user;
    private String message;
    private LocalDateTime timestamp;
    private Map<String, Object> data;

    public CodeSyncerEvent() {}

    public CodeSyncerEvent(String type, String user, String message, LocalDateTime timestamp, Map<String, Object> data) {
        this.type = type;
        this.user = user;
        this.message = message;
        this.timestamp = timestamp;
        this.data = data;
    }


}
