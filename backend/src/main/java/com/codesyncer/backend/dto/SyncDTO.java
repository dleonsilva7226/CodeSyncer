package com.codesyncer.backend.dto;

//kind of like a filter in which you can just send stuff over like sew
public class SyncDTO {
    private String content;
    private String author;
    private String timeStamp;


    public SyncDTO () {}

    public void setContent(String value) {
        this.content = value;
    }

    public String getContent() {
        return this.content;
    }

    public void setAuthor(String value) {
        this.author = value;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setTimeStamp(String value) {
        this.timeStamp = value;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

}
