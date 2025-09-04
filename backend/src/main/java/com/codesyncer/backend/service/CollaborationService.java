package com.codesyncer.backend.service;
import com.codesyncer.backend.model.ConflictResult;
import com.codesyncer.backend.model.LocationMessage;
import com.codesyncer.backend.model.UserLocation;
import org.springframework.stereotype.*;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import static java.time.temporal.ChronoUnit.MINUTES;

@Service
public class CollaborationService {

    private final ConcurrentHashMap<Long, UserLocation> userLocations;
    private final Duration STALE_AFTER = Duration.of(5, MINUTES);
    private final Clock clock = Clock.systemUTC();

    public CollaborationService() {
        this.userLocations = new ConcurrentHashMap<>();
    }

    public void updateUserLocation (LocationMessage locationMessage) {
        long userId = locationMessage.getUserId();
        if (locationMessage.getLineNumber() == null || locationMessage.getFileName() == null) {
            this.userLocations.remove(userId);
            return;
        }

        UserLocation loc = new UserLocation(
            userId,
            locationMessage.getMergeId(),
            locationMessage.getFileName(),
            locationMessage.getLineNumber(),
            clock.instant()
        );
        this.userLocations.put(userId, loc);
    }

    public ConflictResult findConflictingUsers(String fileName, Integer lineNumber, Long mergeId, long currentUserId) {
        ConflictResult conflictResult = new ConflictResult();
        if (lineNumber == null) {
            return conflictResult;
        }
        Instant cutoff = clock.instant().minus(STALE_AFTER);
        conflictResult.setLineNumber(lineNumber);
        List<Long> userIds = new ArrayList<>();

        for (var currElement: this.userLocations.entrySet()) {
            UserLocation loc = currElement.getValue();
            Long currUserId = loc.getUserId();
            Integer otherLineNumber = loc.getLineNumber();
            if (currUserId == null || Objects.equals(loc.getUserId(), currentUserId)) { continue; }
            if (!Objects.equals(loc.getMergeId(), mergeId)) continue;
            if (!Objects.equals(loc.getFileName(), fileName)) continue;
            if (loc.getTimestamp().isBefore(cutoff)) { continue; }
            if (otherLineNumber == null) { continue; }
            int PROXIMITY_LINES = 5;
            if (Math.abs(otherLineNumber - lineNumber) <= PROXIMITY_LINES) {
                userIds.add(currElement.getKey());
            }
        }
        conflictResult.setUserIds(userIds);
        conflictResult.setHasConflict(!userIds.isEmpty());
        return conflictResult;
    }

    public void cleanupOldLocations() {
        Instant cutoff = clock.instant().minus(STALE_AFTER);
        for (var currElement: this.userLocations.entrySet()) {
            UserLocation currLoc = currElement.getValue();
            if (currLoc != null && currLoc.getTimestamp() != null && currLoc.getTimestamp().isBefore(cutoff)) {
                this.userLocations.remove(currElement.getKey(), currLoc);
            }
        }
    }

}
