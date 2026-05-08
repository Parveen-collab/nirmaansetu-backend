package com.nirmaansetu.backend.modules.reviews.service;

import com.nirmaansetu.backend.modules.reviews.dto.SentimentResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SentimentAnalysisService {

    private final ChatClient chatClient;

    public SentimentAnalysisService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem("You are a sentiment analysis agent for a construction sector platform. " +
                        "Your task is to analyze user reviews and feedback. " +
                        "Detect if the review is POSITIVE, NEGATIVE, or NEUTRAL. " +
                        "Also, flag the review if it contains hate speech, fraud attempts, or extremely toxic behavior. " +
                        "Respond ONLY in JSON format with fields: sentiment (String), score (Double 0.0-1.0), flagged (Boolean), flagReason (String or null).")
                .build();
    }

    public SentimentResultDto analyzeSentiment(String text) {
        log.info("Analyzing sentiment for text: {}", text);
        try {
            return chatClient.prompt()
                    .user(text)
                    .call()
                    .entity(SentimentResultDto.class);
        } catch (Exception e) {
            log.error("Error analyzing sentiment: {}", e.getMessage());
            // Fallback in case of AI service failure
            return SentimentResultDto.builder()
                    .sentiment("UNKNOWN")
                    .score(0.5)
                    .flagged(false)
                    .build();
        }
    }
}
