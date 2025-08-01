package com.codesyncer.backend.dto;

public class SyncRequest {
    private String content;
    private String author;
    private String timeStamp;

    public SyncRequest(String content, String author, String timeStamp)
    {
        this.content = content;
        this.author = author;
        this.timeStamp = timeStamp;
    }

    public SyncRequest() {}

    public void setContent (String value) {
        this.content = value;
    }

    public void setAuthor (String value) {
        this.author = value;
    }

    public void setTimeStamp (String value) {
        this.timeStamp = value;
    }

    public String getContent() {
        return this.content;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }
}
