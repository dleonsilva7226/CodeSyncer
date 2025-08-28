package com.codesyncer.backend.controller;
import com.codesyncer.backend.model.CodeSyncerEvent;
import com.codesyncer.backend.service.AiService;
import com.codesyncer.backend.service.DiffService;
import com.codesyncer.backend.model.Diff;
import com.codesyncer.backend.service.SyncService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.util.*;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/sync")
public class SyncController {

    private SyncService syncService;
    private AiService aiService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public SyncController (SyncService syncService, AiService aiService) {
        this.syncService = syncService;
        this.aiService = aiService;
    }

    @GetMapping("/ping")
    public ResponseEntity<?> ping () {
        Map<String, String> res = Map.of("success", "sync controller is alive");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping(
        value = "/suggest",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<String[]> provideSuggestions (
            @RequestPart("backendCode") MultipartFile backendCode,
            @RequestPart("frontendCode") MultipartFile frontendCode,
            @RequestPart("author") String author
    ) {
        if (backendCode.isEmpty() || frontendCode.isEmpty()) {
            String[] missingFileMessage =  {"Please Upload both Backend and Frontend Code"};
            return new ResponseEntity<String[]>(missingFileMessage, HttpStatus.BAD_REQUEST);
        }

        System.out.println("REACHES HERE 3!!!!!");
        String codeRecommendation = this.syncService.createCodeRecommendation(backendCode, frontendCode, author);
        CodeSyncerEvent event = new CodeSyncerEvent(
                "SYNC_SUGGEST",
                author,
                "Refactoring Suggestions Provided",
                LocalDateTime.now(),
                new HashMap<String, Object>()
        );


        System.out.println("Broadcasting sync completion for user: " + author);
        messagingTemplate.convertAndSend("/topic/sync-events",
                "User " + author + " completed sync suggestions");
        System.out.println("REACHES HERE 4!!!!!");
        String[] suggestions = codeRecommendation.split("\\r?\\n");
        return new ResponseEntity<String[]>(suggestions, HttpStatus.OK);
    }

    @GetMapping("/test-broadcast")
    public ResponseEntity<String> testBroadcast() {
        System.out.println("About to broadcast test message");
        messagingTemplate.convertAndSend("/topic/sync-events", "Test message from server");
        System.out.println("Broadcast completed successfully");
        return ResponseEntity.ok("Broadcast sent to /topic/sync-events");
    }


}