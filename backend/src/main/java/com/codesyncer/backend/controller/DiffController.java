package com.codesyncer.backend.controller;
import com.codesyncer.backend.service.DiffService;
import com.codesyncer.backend.model.Diff;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/diff")
public class DiffController {
    private DiffService diffService;

    @Autowired
    public DiffController (DiffService diffService) {
        this.diffService = diffService;
    }

    @GetMapping("/ping")
    public ResponseEntity<?> ping () {
        Map<String, String> res = Map.of("success", "diff controller is alive");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/hunks")
    public ResponseEntity<List<Diff>> provideDiff (
        @RequestParam("oldFile") MultipartFile oldFile,
        @RequestParam("newFile") MultipartFile newFile,
        @RequestParam(value = "ignoreWhitespace", defaultValue = "false") boolean ignoreWhitespace,
        @RequestParam(value = "ignoreCase", defaultValue = "false") boolean ignoreCase,
        @RequestParam(value = "contextLines", defaultValue = "0") int contextLines,
        @RequestParam("saveToDB") boolean saveToDB
    ) {
        if (oldFile.isEmpty() || newFile.isEmpty()) { return ResponseEntity.badRequest().body(Collections.emptyList()); }
        List<Diff> diffs = diffService.provideLineDiffs(oldFile, newFile, ignoreWhitespace, ignoreCase, contextLines, saveToDB);
        return new ResponseEntity<List<Diff>>(diffs, HttpStatus.OK);
    }
}
