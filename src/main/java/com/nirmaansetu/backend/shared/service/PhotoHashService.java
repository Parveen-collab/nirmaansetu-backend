package com.nirmaansetu.backend.shared.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//This class is a utility service for generating a unique fingerprint (hash) of images/files 🔐.
//It uses SHA-256 hashing to identify files — very useful for duplicate detection, integrity checks, and security.
@Service
public class PhotoHashService {

    public String calculateHash(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try (InputStream is = file.getInputStream()) {
            return calculateHash(is);
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not calculate file hash", e);
        }
    }

    public String calculateHashFromUrl(String urlString) {
        if (urlString == null || urlString.isEmpty()) {
            return null;
        }
        try {
            URI uri = new URI(urlString);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            try (InputStream is = connection.getInputStream()) {
                return calculateHash(is);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not calculate hash from URL: " + urlString, e);
        }
    }

    private String calculateHash(InputStream is) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            digest.update(buffer, 0, bytesRead);
        }
        byte[] hashBytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
