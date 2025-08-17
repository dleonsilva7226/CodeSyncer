package com.codesyncer.backend.controller;
import com.codesyncer.backend.service.AiService;
import com.codesyncer.backend.service.DiffService;
import com.codesyncer.backend.model.Diff;
import com.codesyncer.backend.service.SyncService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.Min;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/sync")
public class SyncController {

    private SyncService syncService;
    private AiService aiService;

    @Autowired
    public SyncController (SyncService syncService, AiService aiService) {
        this.syncService = syncService;
        this.aiService = aiService;
    }

    @GetMapping("/ping")
    public ResponseEntity<?> ping () {
        Map<String, String> res = Map.of("success", "backend is alive");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/suggest")
    public ResponseEntity<String[]> provideSuggestions (
            @RequestPart("backendCode") MultipartFile backendCode,
            @RequestPart("frontendCode") MultipartFile frontendCode,
            @RequestPart("author") String author
    ) {
        if (backendCode.isEmpty() || frontendCode.isEmpty()) {
            String[] missingFileMessage =  {"Please Upload both Backend and Frontend Code"};
            return new ResponseEntity<String[]>(missingFileMessage, HttpStatus.BAD_REQUEST);
        }

        String codeRecommendation = this.syncService.createCodeRecommendation(backendCode, frontendCode, author);
        String[] suggestions = codeRecommendation.split("\\r?\\n");
        return new ResponseEntity<String[]>(suggestions, HttpStatus.OK);
    }
}