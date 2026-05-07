package com.nirmaansetu.backend.shared.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.io.File;

@Service
@Slf4j
public class OcrVerificationService {

    private final ChatClient chatClient;

    public OcrVerificationService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem("You are an expert document verification assistant. Your task is to extract information from identity documents and verify if they match the provided details.")
                .build();
    }

    @Data
    public static class VerificationResult {
        private boolean success;
        private String extractedName;
        private String extractedIdNumber;
        private String confidence;
        private String reason;
    }

    public VerificationResult verifyDocument(String filePath, String expectedName, String expectedIdNumber) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new RuntimeException("File not found: " + filePath);
            }
            Resource resource = new FileSystemResource(file);
            
            String promptText = """
                Analyze this identity document.
                1. Extract the Full Name.
                2. Extract the ID Number (Aadhaar or License).
                3. Compare with expected values:
                   - Expected Name: {expectedName}
                   - Expected ID Number: {expectedIdNumber}
                
                Verification is successful if the name and ID number match the expected values. 
                Allow for minor OCR errors or formatting differences (e.g., spaces in Aadhaar number).
                """;

            return chatClient.prompt()
                    .user(u -> u.text(promptText)
                            .param("expectedName", expectedName)
                            .param("expectedIdNumber", expectedIdNumber)
                            .media(MimeTypeUtils.IMAGE_JPEG, resource))
                    .call()
                    .entity(VerificationResult.class);

        } catch (Exception e) {
            log.error("Error during OCR verification for file: {}", filePath, e);
            VerificationResult result = new VerificationResult();
            result.setSuccess(false);
            result.setReason("Error processing document: " + e.getMessage());
            return result;
        }
    }
}
