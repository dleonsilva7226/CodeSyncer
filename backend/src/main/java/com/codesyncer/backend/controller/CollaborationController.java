package com.codesyncer.backend.controller;
import com.codesyncer.backend.model.ConflictResult;
import com.codesyncer.backend.model.ConflictWarning;
import com.codesyncer.backend.model.LocationMessage;
import com.codesyncer.backend.service.CollaborationService;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.*;
import org.springframework.messaging.handler.annotation.*;


@Controller
public class CollaborationController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private CollaborationService collaborationService;

    @MessageMapping("/location/update")
    public void handleLocationUpdate(LocationMessage update) {
        collaborationService.updateUserLocation(update);

        collaborationService.cleanupOldLocations();

        ConflictResult result = collaborationService.findConflictingUsers(
            update.getFileName(),
            update.getLineNumber(),
            update.getMergeId(),
            update.getUserId()
        );

        if (result.isHasConflict()) {
            ConflictWarning warning = new ConflictWarning(
                ConflictWarning.ConflictType.CONFLICT_WARNING,
                update.getFileName(),
                result.getUserIds().toArray(new String[0]),
                update.getLineNumber(),
                5,
                update.getMergeId()
            );
            messagingTemplate.convertAndSend("/topic/conflicts", warning);
        }
    }

}
