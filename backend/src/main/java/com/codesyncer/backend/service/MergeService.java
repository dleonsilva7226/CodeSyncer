package com.codesyncer.backend.service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.codesyncer.backend.model.Diff;
import com.codesyncer.backend.model.Merge;
import io.micrometer.core.instrument.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.codesyncer.backend.repository.MergeRepo;

@Service
public class MergeService {
    private final MergeRepo mergeRepo;
    private final DiffService diffService;
    private final AiService aiService;

    @Autowired
    public MergeService(MergeRepo mergeRepo, AiService aiService, DiffService diffService) {
        this.mergeRepo = mergeRepo;
        this.aiService = aiService;
        this.diffService = diffService;
    }

    public Merge createMerge(MultipartFile oldFile, MultipartFile newFile, String author, boolean saveToDB) {
        String convertedOldFile = convertMultipartFiletoString(oldFile);
        String convertedNewFile = convertMultipartFiletoString(newFile);
        List<Diff> diffInfo = diffService.provideLineDiffs(oldFile, newFile, false, false, 0, saveToDB);

        String prompt = """
        You are an intelligent code assistant. You are given two versions of code: an "old" version and a "new" version. Your job is to suggest a merged version that:
 
        - Preserves any valuable logic or structure from the old version
        - Integrates the intended changes in the new version
        - Ensures the final output is syntactically valid and logically sound
        - Keeps consistent formatting and code style
        - Does not duplicate or omit important functionality

        OLD CODE:
        %s

        NEW CODE:
        %s
        
        Also here is the diff info reported for these two code files:
        %s

        Please return the merged code snippet, with an explanation and a description.
        Format it so that the code snippet, explanation and the description are all separated by a ,,, symbol (in this order mentioned)
        """.formatted(convertedOldFile, convertedNewFile, diffInfo.toString());

        String mergeResponse = aiService.chat(prompt);
        String[] mergeResArr = mergeResponse.split(",,,");
        Merge newMerge;
        if (mergeResArr.length < 3) {
            newMerge = new Merge(author, "", convertedOldFile, convertedNewFile, mergeResponse, "", diffInfo);
        } else {
            newMerge = new Merge(author, mergeResArr[2], convertedOldFile, convertedNewFile, mergeResArr[0], mergeResArr[1], diffInfo);
        }

        mergeRepo.save(newMerge);
        return newMerge;
    }

    public Optional<Merge> saveMergeSuggestion(long mergeID) {
        Optional<Merge> mergeSuggestion = mergeRepo.findById(mergeID);
        if (mergeSuggestion.isPresent()) {
           Merge merge = mergeSuggestion.get();
           merge.setIsAccepted(true);
           Merge saved = mergeRepo.save(merge);
           return Optional.of(saved);
        }
        return Optional.empty();
    }

    public Map<String, String> rejectMergeSuggestion(long mergeID) {
        Optional <Merge> mergeSuggestion = mergeRepo.findById(mergeID);
        if (mergeSuggestion.isEmpty()) {
            return Map.of("message", "Merge suggestion not found");
        } else {
            mergeRepo.delete(mergeSuggestion.get());
            return Map.of("message", "Merge suggestion deleted successfully");
        }

    }


    public String convertMultipartFiletoString (MultipartFile file) {
        if (file == null || file.isEmpty()) { return ""; }

        try (InputStream inputStream = file.getInputStream()) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "Error occurred: " + e;
        }
    }
}
