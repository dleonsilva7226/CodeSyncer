package com.codesyncer.backend.controller;
import com.codesyncer.backend.model.CodeSyncerEvent;
import com.codesyncer.backend.model.Merge;
import com.codesyncer.backend.service.AiService;
import com.codesyncer.backend.service.DiffService;
import com.codesyncer.backend.model.Diff;
import com.codesyncer.backend.service.MergeService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/merge")
public class MergeController {

    private MergeService mergeService;
    private AiService aiService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public MergeController(MergeService mergeService, AiService aiService) {
        this.mergeService = mergeService;
        this.aiService = aiService;
    }

    @GetMapping("/ping")
    public ResponseEntity<?> ping () {
        Map<String, String> res = Map.of("success", "merge controller is alive");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping(
        value = "/suggestion",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> mergeSuggestion (
        @RequestPart(value = "oldFile") MultipartFile oldFile,
        @RequestPart(value = "newFile") MultipartFile newFile,
        @RequestPart(value = "author") String author
    ) {
        if (oldFile.isEmpty() || newFile.isEmpty()) {
            return new ResponseEntity<>(Map.of("message", "Missing file(s)"), HttpStatus.BAD_REQUEST);
        }

        Merge mergeSuggestion = this.mergeService.createMerge(oldFile, newFile, author, true);

        CodeSyncerEvent event = new CodeSyncerEvent(
                "MERGE_SUGGEST",
                author,
                "Refactoring Suggestions Provided",
                LocalDateTime.now(),
                new HashMap<String, Object>()
        );


        System.out.println("Broadcasting sync completion for user: " + author);
        messagingTemplate.convertAndSend("/topic/merge-events",
                "User " + author + " completed sync suggestions");
        return new ResponseEntity<Merge>(mergeSuggestion, HttpStatus.OK);

    }

    @PostMapping("/accept/{mergeID}")
    public ResponseEntity<?> acceptMergeSuggestion (@PathVariable Long mergeID) {
        Optional<Merge> acceptedMerge = this.mergeService.saveMergeSuggestion(mergeID);
        if (acceptedMerge.isPresent()) {
            return new ResponseEntity<Merge>(acceptedMerge.get(), HttpStatus.OK);
        }
        Map<String, String> notFoundResponse = Map.of("message", "Merge suggestion not found");
        return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/reject/{mergeID}")
    public ResponseEntity<?> rejectMergeSuggestion (@PathVariable Long mergeID) {
        Map<String, String> deletionResponse = this.mergeService.rejectMergeSuggestion(mergeID);
        return new ResponseEntity<>(deletionResponse, HttpStatus.OK);
    }
}