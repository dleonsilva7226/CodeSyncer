package com.codesyncer.backend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name="Diff")
public class Diff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="type")
    private String type;

    @Column(name="old_start_line")
    private int oldStartLine;

    @Column(name="old_end_line")
    private int oldEndLine;

    @Column(name="old_lines")
    private List<String> oldLines;

    @Column(name="new_start_line")
    private int newStartLine;

    @Column(name="new_end_line")
    private int newEndLine;

    @Column(name="new_lines")
    private List<String> newLines;

    @Column(name="old_context_before_start_lines")
    private List<String> oldContextBeforeStartLines;

    @Column(name="old_context_after_end_lines")
    private List<String> oldContextAfterEndLines;

    @Column(name="new_context_before_start_lines")
    private List<String> newContextBeforeStartLines;

    @Column(name="new_context_after_end_lines")
    private List<String> newContextAfterEndLines;


    public Diff() {}

    public Diff (
            String type,
            int oldStartLine,
            int oldEndLine,
            List<String> oldLines,
            int newStartLine,
            int newEndLine,
            List<String> newLines,
            List<String> oldContextBeforeStartLines,
            List<String> oldContextAfterEndLines,
            List<String> newContextBeforeStartLines,
            List<String> newContextAfterEndLines
    ) {
        this.type = type;
        this.oldStartLine = oldStartLine;
        this.oldEndLine = oldEndLine;
        this.oldLines = oldLines;
        this.newStartLine = newStartLine;
        this.newEndLine = newEndLine;
        this.newLines = newLines;
        this.oldContextBeforeStartLines = oldContextBeforeStartLines;
        this.oldContextAfterEndLines = oldContextAfterEndLines;
        this.newContextBeforeStartLines = newContextBeforeStartLines;
        this.newContextAfterEndLines = newContextAfterEndLines;
    }

    public String getType() {
        return type;
    }

    public int getOldStartLine() {
        return oldStartLine;
    }

    public int getOldEndLine() {
        return oldEndLine;
    }

    public List<String> getOldLines() {
        return oldLines;
    }

    public int getNewStartLine() {
        return newStartLine;
    }

    public int getNewEndLine() {
        return newEndLine;
    }

    public List<String> getNewLines() {
        return newLines;
    }

    public List<String> getOldContextBeforeStartLines() {
        return oldContextBeforeStartLines;
    }

    public List<String> getOldContextAfterEndLines() {
        return oldContextAfterEndLines;
    }

    public List<String> getNewContextBeforeStartLines() {
        return newContextBeforeStartLines;
    }

    public List<String> getNewContextAfterEndLines() {
        return newContextAfterEndLines;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setOldStartLine(int oldStartLine) {
        this.oldStartLine = oldStartLine;
    }

    public void setOldEndLine(int oldEndLine) {
        this.oldEndLine = oldEndLine;
    }

    public void setOldLines(List<String> oldLines) {
        this.oldLines = oldLines;
    }

    public void setNewStartLine(int newStartLine) {
        this.newStartLine = newStartLine;
    }

    public void setNewEndLine(int newEndLine) {
        this.newEndLine = newEndLine;
    }

    public void setNewLines(List<String> newLines) {
        this.newLines = newLines;
    }

    public void setOldContextBeforeStartLines(List<String> oldContextBeforeStartLines) {
        this.oldContextBeforeStartLines = oldContextBeforeStartLines;
    }

    public void setOldContextAfterEndLines(List<String> oldContextAfterEndLines) {
        this.oldContextAfterEndLines = oldContextAfterEndLines;
    }

    public void setNewContextBeforeStartLines(List<String> newContextBeforeStartLines) {
        this.newContextBeforeStartLines = newContextBeforeStartLines;
    }

    public void setNewContextAfterEndLines(List<String> newContextAfterEndLines) {
        this.newContextAfterEndLines = newContextAfterEndLines;
    }

    public Long getId () {
        return this.id;
    }

    public void setId (Long value) {
        this.id = value;
    }

    @Override
    public String toString() {
        return "Diff [id=" + id + ", type=" + type +  ", oldStartLine=" + oldStartLine + ", oldEndLine=" + oldEndLine + ", oldLines=" + oldLines.toString() + ", newStartLine=" + newStartLine +  ", newEndLine=" + newEndLine + ", newLines=" + newLines.toString() + "]";
    }
}
