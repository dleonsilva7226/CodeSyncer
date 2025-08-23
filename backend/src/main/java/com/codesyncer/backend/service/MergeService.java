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

        String codePrompt = """
        You are given OLD and NEW versions of source code and their DIFF.
        Return ONLY the merged code that:
        - Preserves the structure and logic of the OLD version
        - Integrates the intended changes in the NEW version
        - Matches the DIFF
        - Is valid and runnable in the same language as the input
               
        CRITICAL RULES:
        - Return ONLY the code (no explanation or description)
        - You may wrap it in a single fenced code block (```lang ... ```)
               
        OLD CODE:
        %s
               
        NEW CODE:
        %s
               
        DIFF INFO:
        %s
        """.formatted(convertedOldFile, convertedNewFile, diffInfo.toString());

        String mergedCode = sanitizeCode(extractFirstCodeBlockOrAll(aiService.chat(codePrompt)));

        String explanationPrompt = """
        Explain WHY the merged code was created this way, referencing the DIFF.
        Focus on what was preserved from OLD, what was changed from NEW,
        and why the final code makes sense.
        
        Keep it concise (3–6 sentences).
        Plain text only, no code blocks.
        
        OLD CODE:
        %s
        
        NEW CODE:
        %s
        
        DIFF INFO:
        %s
        
        MERGED CODE:
        %s
        """.formatted(convertedOldFile, convertedNewFile, diffInfo.toString(), mergedCode);

        String mergeExplanation = sanitizeText(aiService.chat(explanationPrompt));

        String descriptionPrompt = """
        Summarize what the merged code does in 1–2 sentences.
        Plain text only, no code blocks.
        
        MERGED CODE:
        %s
        """.formatted(mergedCode);

        String mergeDescription = sanitizeText(aiService.chat(descriptionPrompt));

        Merge newMerge = new Merge(
            author,
            mergeDescription,
            convertedOldFile,
            convertedNewFile,
            mergedCode,
            mergeExplanation,
            diffInfo
        );

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

    /** Prefer the first fenced/HTML code block; else return the whole string. */
    private String extractFirstCodeBlockOrAll(String raw) {
        if (raw == null) return "";
        // ```lang\n ... \n```
        var md = java.util.regex.Pattern.compile("```[a-zA-Z0-9_-]*\\s*([\\s\\S]*?)```",
                java.util.regex.Pattern.DOTALL);
        var m = md.matcher(raw);
        if (m.find()) return m.group(1);

        // <code> ... </code>
        var html = java.util.regex.Pattern.compile("(?is)<code[^>]*>([\\s\\S]*?)</code>");
        var h = html.matcher(raw);
        if (h.find()) return h.group(1);

        return raw; // no explicit block, use entire output
    }

    /** Final cleanup for code area. */
    private String sanitizeCode(String code) {
        if (code == null) return "";
        return code
                .replaceAll("```[a-zA-Z0-9_-]*", "") // strip opening ```lang
                .replace("```", "")                  // strip closing ```
                .replaceAll("(?is)</?code>", "")     // strip <code> tags
                .replace("\r\n", "\n")               // normalize EOLs
                .trim();
    }

    /** For explanation/description: trim; drop accidental leading labels or fenced blocks. */
    private String sanitizeText(String s) {
        if (s == null) return "";
        // remove any full fenced blocks the model might have added
        s = s.replaceAll("```[\\s\\S]*?```", "");
        // strip leading labels like "Explanation:" or "Description:"
        s = s.replaceFirst("(?is)^\\s*(explanation|description)\\s*:\\s*", "");
        return s.trim();
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
