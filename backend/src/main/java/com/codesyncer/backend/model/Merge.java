package com.codesyncer.backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "merges")
public class Merge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "author")
    private String author; // optional, or linked via user ID if needed

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "merge_description", columnDefinition = "TEXT")
    private String description; // short summary of the change, could be AI-generated or human-written

    @Column(name = "old_code", columnDefinition = "TEXT")
    private String oldCode;

    @Column(name = "new_code", columnDefinition = "TEXT")
    private String newCode;

    @Column(name = "merged_code", columnDefinition = "TEXT")
    private String mergedCode;

    @Column(name = "merge_reason", columnDefinition = "TEXT")
    private String mergeReason; // optional: what caused the merge (e.g. backend updated to support new frontend)

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Column(name = "source_diff", columnDefinition = "TEXT")
    private List<Diff> diff; // if available, store the computed diff

    @Column(name = "is_accepted")
    private Boolean isAccepted; // if user approved the AI merge

    public Merge() {}

    public Merge(String author, String description, String oldCode, String newCode, String mergedCode, String mergeReason, List<Diff> diff) {
        this.author = author;
        this.description = description;
        this.oldCode = oldCode;
        this.newCode = newCode;
        this.mergedCode = mergedCode;
        this.mergeReason = mergeReason;
        this.diff = diff;
        this.isAccepted = false;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId () {
        return this.id;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription () {
        return description;
    }

    public String getOldCode() {
        return oldCode;
    }

    public String getNewCode() {
        return newCode;
    }

    public String getMergedCode() {
        return mergedCode;
    }

    public String getMergeReason() {
        return mergeReason;
    }

    public List<Diff> getDiff() {
        return diff;
    }

    public Boolean getIsAccepted() {
        return isAccepted;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOldCode(String oldCode) {
        this.oldCode = oldCode;
    }

    public void setNewCode(String newCode) {
        this.newCode = newCode;
    }

    public void setMergedCode(String mergedCode) {
        this.mergedCode = mergedCode;
    }

    public void setMergeReason(String mergeReason) {
        this.mergeReason = mergeReason;
    }

    public void setDiff(List<Diff> diff) {
        this.diff = diff;
    }

    public void setIsAccepted(Boolean accepted) {
        isAccepted = accepted;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Merge{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", timestamp=" + timestamp +
                ", description='" + description + '\'' +
                ", oldCode='" + oldCode + '\'' +
                ", newCode='" + newCode + '\'' +
                ", mergedCode='" + mergedCode + '\'' +
                ", mergeReason='" + mergeReason + '\'' +
                ", diff=" + diff +
                ", isAccepted=" + isAccepted +
                '}';
    }
}

