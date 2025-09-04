package com.codesyncer.backend.model;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ConflictResult {

    private boolean hasConflict;
    private List<Long> userIds;
    private int lineNumber;

    public ConflictResult () {
        this.hasConflict = false;
        this.userIds = new ArrayList<Long>();
        this.lineNumber = -1;
    }

    public ConflictResult(boolean hasConflict, List<Long> userIds, int lineNumber) {
        this.hasConflict = hasConflict;
        this.userIds = userIds;
        this.lineNumber = lineNumber;
    }
}
