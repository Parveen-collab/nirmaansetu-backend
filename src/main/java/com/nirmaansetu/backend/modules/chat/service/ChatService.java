package com.nirmaansetu.backend.modules.chat.service;

import com.nirmaansetu.backend.modules.chat.dto.ChatRequestDto;
import com.nirmaansetu.backend.modules.chat.dto.ChatResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsible for handling AI-powered chat interactions.
 * Uses Retrieval-Augmented Generation (RAG) by combining Spring AI's ChatClient 
 * with a VectorStore for context-aware responses.
 */
@Service
@Slf4j
public class ChatService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    /**
     * Configures the ChatClient with a specialized system persona for the construction sector.
     */
    public ChatService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.chatClient = chatClientBuilder
                .defaultSystem("You are NirmaanSetu Assistant, a specialized AI for the construction sector platform. " +
                        "Your goal is to help users (workers, employers, and shopkeepers) navigate the platform, " +
                        "find relevant profiles, and provide basic construction-related advice. " +
                        "Use the provided context to answer questions accurately. If you don't know the answer, " +
                        "kindly suggest the user to contact human support.")
                .build();
        this.vectorStore = vectorStore;
    }

    /**
     * Processes a user chat request by retrieving relevant context and generating an AI response.
     * 
     * @param request Contains the user's query message.
     * @return ChatResponseDto containing the AI-generated answer.
     */
    public ChatResponseDto getResponse(ChatRequestDto request) {
        String userMessage = request.getMessage();
        log.info("Processing chat request: {}", userMessage);

        // 1. Retrieve relevant context from VectorStore using similarity search
        SearchRequest searchRequest = SearchRequest.query(userMessage).withTopK(5);
        List<Document> similarDocuments = vectorStore.similaritySearch(searchRequest);

        // Extract and combine content from retrieved documents
        String context = similarDocuments.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n\n"));

        // 2. Generate response using ChatClient, injecting the retrieved context into the prompt
        String response = chatClient.prompt()
                .user(u -> u.text("Context information is below:\n" +
                        "---------------------\n" +
                        "{context}\n" +
                        "---------------------\n" +
                        "Given the context information and not prior knowledge, " +
                        "answer the user question: {query}")
                        .param("context", context)
                        .param("query", userMessage))
                .call()
                .content();

        return ChatResponseDto.builder()
                .response(response)
                .build();
    }
}
