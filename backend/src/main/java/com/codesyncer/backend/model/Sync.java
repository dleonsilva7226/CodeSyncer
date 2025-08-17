package com.codesyncer.backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="Sync")
public class Sync {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(columnDefinition="TEXT", name="backend_code")
    private String backendCode;

    @Column(columnDefinition="TEXT", name="frontend_code")
    private String frontendCode;

    @Column(name="author")
    private String author;

    @Column(name="timestamp")
    private LocalDateTime timestamp;

    @Column(name="code_recommendation", columnDefinition="TEXT")
    private String codeRecommendation;

    public Sync() {}

    public Sync(String backendCode, String frontendCode, String author) {
        this.backendCode = backendCode;
        this.frontendCode = frontendCode;
        this.author = author;
        this.timestamp = LocalDateTime.now();
        this.codeRecommendation = "No Code Recommendation";
    }

    public Long getId () {
        return this.id;
    }

    public String getBackendCode() { return this.backendCode; }

    public String getFrontendCode() { return this.frontendCode; }

    public String getAuthor () {
        return this.author;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public String getCodeRecommendation () {
        return this.codeRecommendation;
    }

    public void setId (Long value) {
        this.id = value;
    }

    public void setBackendCode (String value) { this.backendCode = value; }

    public void setFrontendCode (String value) { this.frontendCode = value; }

    public void setAuthor (String value) {
        this.author = value;
    }

    public void setTimestamp(LocalDateTime value) {
        this.timestamp = value;
    }

    public void setCodeRecommendation (String value) {
        this.codeRecommendation = value;
    }

    @Override
    public String toString() {
        return "Diff [id=" + id + ", backendCode " + backendCode +  ", frontendCode " + frontendCode + ", author=" + author + ", timestamp=" + timestamp.toString() + "]";
    }
}
