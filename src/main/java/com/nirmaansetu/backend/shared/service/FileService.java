package com.nirmaansetu.backend.shared.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

//This class is your file upload service 📂 — it handles saving profile images to your server and returning a URL to access them.
@Service
public class FileService {

    private final String uploadDir = "uploads/profiles";

    public String saveProfilePhoto(MultipartFile file) {
        return saveFile(file, "profiles");
    }

    public String saveVerificationDocument(MultipartFile file) {
        return saveFile(file, "documents");
    }

    private String saveFile(MultipartFile file, String subDir) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            Path root = Paths.get(uploadDir, subDir);
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = root.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            return "/api/files/" + subDir + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Could not store file. Error: " + e.getMessage());
        }
    }

    public String getFileSystemPath(String fileUrl) {
        if (fileUrl == null || !fileUrl.startsWith("/api/files/")) {
            return null;
        }
        String relativePath = fileUrl.substring("/api/files/".length());
        return Paths.get(uploadDir, relativePath).toAbsolutePath().toString();
    }
}
