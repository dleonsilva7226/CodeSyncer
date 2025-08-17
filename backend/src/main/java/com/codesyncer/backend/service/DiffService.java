package com.codesyncer.backend.service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import com.codesyncer.backend.exceptions.ContentLimitExceededException;
import com.codesyncer.backend.exceptions.FileTooLargeException;
import com.codesyncer.backend.exceptions.NonTextFileException;
import com.codesyncer.backend.model.Diff;
import com.codesyncer.backend.model.Sync;
import com.codesyncer.backend.repository.DiffRepo;
import com.codesyncer.backend.repository.SyncRepo;
import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import io.micrometer.core.instrument.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public class DiffService {

    private final SyncRepo syncRepo;
    private final DiffRepo diffRepo;
    private final AiService aiService;
    private final long MAX_FILE_SIZE = 1048576;
    private static final int  MAX_LINES = 10_000;
    private static final int  MAX_LINE_LEN = 4_000;
    public static final String UTF8_BOM = "\uFEFF";

    @Autowired
    public DiffService(SyncRepo syncRepo, DiffRepo diffRepo, AiService aiService) {
        this.syncRepo = syncRepo;
        this.diffRepo = diffRepo;
        this.aiService = aiService;
    }

    public String createCodeRecommendation(MultipartFile backendCode, MultipartFile frontendCode, String author) {
        String convertedBackendCode = convertMultipartFiletoString(backendCode);
        String convertedFrontendCode = convertMultipartFiletoString(frontendCode);

        String prompt = """
        You are an AI assistant analyzing code differences between a frontend and backend.
        Your task is to provide specific, actionable refactoring recommendations to improve synchronization.
        
        Follow these rules for your response:
        1. Only output a numbered list of suggestions — one per line.
        2. Do not add explanations or extra text outside the list.
        3. Keep each suggestion short and actionable.
        
        Here is the code to analyze:
        ----FRONTEND CODE HERE----
        %s
        
        ----BACKEND CODE HERE----
        %s
        
        
        """.formatted(convertedFrontendCode, convertedBackendCode);

        String codeRecResponse = aiService.chat(prompt);
        Sync newSync = new Sync(convertedBackendCode, convertedFrontendCode, author);
        newSync.setCodeRecommendation(codeRecResponse);
        syncRepo.save(newSync);
        return newSync.getCodeRecommendation();
    }

    public List<Diff> provideLineDiffs(
        MultipartFile oldFile,
        MultipartFile newFile,
        boolean ignoreWhitespace,
        boolean ignoreCase,
        int contextLines,
        boolean saveToDB
    ) {
        try {
            //what the UI will see
            String oldDisplayText = readFileAndStripBOM(oldFile);
            String newDisplayText = readFileAndStripBOM(newFile);
            String[] oldDisplayLines = oldDisplayText.split("\n");
            String[] newDisplayLines = newDisplayText.split("\n");

            // used for matching
            String oldCompareText = applyFlags(oldDisplayText, ignoreWhitespace, ignoreCase);
            String newCompareText = applyFlags(newDisplayText, ignoreWhitespace, ignoreCase);
            String[] oldCompareLines = oldCompareText.split("\n");
            String[] newCompareLines = newCompareText.split("\n");

            Patch<String> compareLineDiffs = DiffUtils.diff(Arrays.asList(oldCompareLines), Arrays.asList(newCompareLines));
            int totalDeltaSize = compareLineDiffs.getDeltas().size();
            List<Diff> allDiffHunks = new ArrayList<Diff>();
            for (int i = 0; i < totalDeltaSize; i++) {
                AbstractDelta<String> delta = compareLineDiffs.getDeltas().get(i);
                String type = delta.getType().toString().toLowerCase();
                int oldStartLine = delta.getSource().getPosition() + 1;
                int oldEndLine = oldStartLine + delta.getSource().getLines().size();
                List<String> oldLines = delta.getSource().getLines();
                int newStartLine = delta.getTarget().getPosition() + 1;
                int newEndLine = newStartLine + delta.getTarget().getLines().size();
                List<String> newLines = delta.getTarget().getLines();
                // Calculate context lines
                int oldContextBeforeStart = Math.max(0, oldStartLine - contextLines);
                int oldContextAfterEnd = Math.min(oldDisplayLines.length - 1, oldEndLine + contextLines);
                int newContextBeforeStart = Math.max(0, newStartLine - contextLines);
                int newContextAfterEnd = Math.min(newDisplayLines.length - 1, newEndLine + contextLines);
                List <String> oldContextBeforeStartLines = new ArrayList<String>(Arrays.asList(oldDisplayLines).subList(oldContextBeforeStart, oldStartLine));
                List <String> oldContextAfterEndLines = new ArrayList<String>(Arrays.asList(oldDisplayLines).subList(oldEndLine + 1, oldContextAfterEnd));
                List <String> newContextBeforeStartLines = new ArrayList<String>(Arrays.asList(newDisplayLines).subList(newContextBeforeStart, newStartLine));
                List <String> newContextAfterEndLines = new ArrayList<String>(Arrays.asList(newDisplayLines).subList(newEndLine + 1, newContextAfterEnd));
                allDiffHunks.add(new Diff(
                    type,
                    oldStartLine,
                    oldEndLine,
                    oldLines,
                    newStartLine,
                    newEndLine,
                    newLines,
                    oldContextBeforeStartLines,
                    oldContextAfterEndLines,
                    newContextBeforeStartLines,
                    newContextAfterEndLines
                ));
            }

            if (saveToDB) {
                for (Diff currDiff : allDiffHunks) { diffRepo.save(currDiff); }
            }

            return allDiffHunks;

        } catch (Error | FileTooLargeException | IOException e) {
            System.out.println("EXCEPTION OCCURRED => " + e);
            return new ArrayList<Diff>();
        }
    }


    public String applyFlags(String text, boolean ignoreWhitespace, boolean ignoreCase) {
        if (ignoreWhitespace) { text = text.replace(" ", "").replace("\t", ""); }
        if (ignoreCase) { text = text.toLowerCase(); }
        return text;
    }

    public String readFileAndStripBOM(MultipartFile newFile) throws FileTooLargeException, NonTextFileException, IOException {
        if (newFile.getSize() > MAX_FILE_SIZE) {
            throw new FileTooLargeException("File Too large. Each file must be ≤ 1 MB.", newFile.getSize(), MAX_FILE_SIZE);
        }

        byte[] bytes = newFile.getBytes();
        //binary sniffing
        int sniff = Math.min(bytes.length, 8192);
        for (int i = 0; i < sniff; i++) {
            if (bytes[i] == 0x00) {
                throw new NonTextFileException("Looks binary; please upload a text source file.");
            }
        }

        String text = new String(bytes, StandardCharsets.UTF_8);

        if (text.startsWith(UTF8_BOM)) {
            text = text.substring(1);
        }

        text = text.replace("\r\n", "\n").replace("\r", "\n");

        //Content caps
        String[] lines = text.split("/n", -1);
        if (lines.length > MAX_LINES) {
            throw new ContentLimitExceededException("Too many lines", lines.length, MAX_LINES);
        }

        for (String line : lines) {
            if (line.length() > MAX_LINE_LEN) {
                throw new ContentLimitExceededException("Line too long", line.length(), MAX_LINE_LEN);
            }
        }

        return text;

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