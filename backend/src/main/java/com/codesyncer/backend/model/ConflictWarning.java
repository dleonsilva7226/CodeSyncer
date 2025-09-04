package com.codesyncer.backend.model;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConflictWarning {

    public enum ConflictType {
        CONFLICT_WARNING
    }

    private ConflictType type;
    private String fileName;
    private String[] conflictingUsers;
    private int lineNumber;
    private int proximity; // number of lines around the conflict
    private long mergeId; // ID of the merge that caused the conflict

    public ConflictWarning() {}

    public ConflictWarning(ConflictType type, String fileName, String[] conflictingUsers, int lineNumber, int proximity, long mergeId) {
        this.type = type;
        this.fileName = fileName;
        this.conflictingUsers = conflictingUsers;
        this.lineNumber = lineNumber;
        this.proximity = proximity;
        this.mergeId = mergeId;
    }
}
