package com.codesyncer.backend.controller;
import com.codesyncer.backend.dto.SyncRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class SyncController {

    @Autowired
    public SyncController () {}

    @GetMapping("/ping")
    public ResponseEntity<?> ping () {
        return new ResponseEntity<>("backend is alive", HttpStatus.OK);
    }

    @PostMapping("/sync")
    public ResponseEntity<?> sync (@RequestBody SyncRequest syncRequest) {
        System.out.println(syncRequest.getContent());
        System.out.println(syncRequest.getAuthor());
        System.out.println(syncRequest.getTimeStamp());
        Map<String, String> syncResponse = Map.of("Message", "Diff received");
        return new ResponseEntity<>(syncResponse, HttpStatus.OK);
    }

}
