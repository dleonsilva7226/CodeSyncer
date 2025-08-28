package com.codesyncer.backend.model;

import java.time.LocalDateTime;
import java.util.*;

public class CodeSyncerEvent {
    private String type;
    private String user;
    private String message;
    private LocalDateTime timestamp;
    private Map<String, Object> data;


    public CodeSyncerEvent(String type, String user, String message, LocalDateTime timestamp, Map<String, Object> data) {
        this.type = type;
        this.user = user;
        this.message = message;
        this.timestamp = timestamp;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public String getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

}
