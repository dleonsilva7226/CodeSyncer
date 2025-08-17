package com.codesyncer.backend.service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import com.codesyncer.backend.model.Sync;
import com.codesyncer.backend.repository.SyncRepo;
import io.micrometer.core.instrument.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SyncService {
    private final SyncRepo syncRepo;
    private final AiService aiService;
    private final long MAX_FILE_SIZE = 1048576;
    private static final int  MAX_LINES = 10_000;
    private static final int  MAX_LINE_LEN = 4_000;
    public static final String UTF8_BOM = "\uFEFF";

    @Autowired
    public SyncService(SyncRepo syncRepo, AiService aiService) {
        this.syncRepo = syncRepo;
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

    public String convertMultipartFiletoString (MultipartFile file) {
        if (file == null || file.isEmpty()) { return ""; }

        try (InputStream inputStream = file.getInputStream()) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "Error occurred: " + e;
        }
    }
}
