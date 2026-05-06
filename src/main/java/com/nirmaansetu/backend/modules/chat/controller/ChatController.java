package com.nirmaansetu.backend.modules.chat.controller;

import com.nirmaansetu.backend.modules.chat.dto.ChatRequestDto;
import com.nirmaansetu.backend.modules.chat.dto.ChatResponseDto;
import com.nirmaansetu.backend.modules.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Chat", description = "AI Support Chat Endpoints")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/message")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Send a message to the AI support bot and get a response")
    public ResponseEntity<ChatResponseDto> chat(@Valid @RequestBody ChatRequestDto request) {
        log.info("Received chat message request");
        ChatResponseDto response = chatService.getResponse(request);
        log.info("Successfully generated chat response");
        return ResponseEntity.ok(response);
    }
}
