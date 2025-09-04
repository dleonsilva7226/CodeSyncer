package com.codesyncer.backend.controller;
import com.codesyncer.backend.model.ConflictWarning;
import com.codesyncer.backend.model.LocationMessage;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.*;
import org.springframework.messaging.handler.annotation.*;


@Controller
public class CollaborationController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/location-update")
    @SendTo("/topic/conflicts")
    public ConflictWarning handleLocationUpdate(LocationMessage update) {
        // Process the location update and determine if there's a conflict
//        boolean conflictDetected = checkForConflict(update);
//        if (conflictDetected) {
//            return new ConflictWarning("Conflict detected for user: " + update.getUserId());
//        }
        return null; // No conflict
    }

}
