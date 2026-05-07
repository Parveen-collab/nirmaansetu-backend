package com.nirmaansetu.backend.modules.projects.service;

import com.nirmaansetu.backend.modules.projects.dto.EstimationResponseDto;
import com.nirmaansetu.backend.modules.shop.entity.Material;
import com.nirmaansetu.backend.modules.shop.repository.MaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EstimationServiceTest {

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private ChatClient.Builder chatClientBuilder;

    @Mock
    private ChatClient chatClient;

    private EstimationService estimationService;

    @BeforeEach
    void setUp() {
        when(chatClientBuilder.build()).thenReturn(chatClient);
        estimationService = new EstimationService(chatClientBuilder, materialRepository);
    }

    @Test
    void testEstimateProject() {
        // Arrange
        String description = "I want to build a 20ft boundary wall";
        Material brick = new Material();
        brick.setName("Brick");
        brick.setPrice(10.0);
        brick.setUnit("piece");
        
        when(materialRepository.findAll()).thenReturn(List.of(brick));

        EstimationResponseDto expectedResponse = EstimationResponseDto.builder()
                .totalEstimatedCost(5000.0)
                .aiSummary("Estimated based on bricks")
                .materials(Collections.emptyList())
                .estimatedLaborCost(1000.0)
                .build();

        ChatClient.ChatClientRequestSpec requestSpec = mock(ChatClient.ChatClientRequestSpec.class);
        ChatClient.CallResponseSpec responseSpec = mock(ChatClient.CallResponseSpec.class);

        when(chatClient.prompt(any(Prompt.class))).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(responseSpec);
        when(responseSpec.entity(any(ParameterizedTypeReference.class))).thenReturn(expectedResponse);

        // Act
        EstimationResponseDto result = estimationService.estimateProject(description);

        // Assert
        assertNotNull(result);
        assertEquals(5000.0, result.getTotalEstimatedCost());
        assertEquals("Estimated based on bricks", result.getAiSummary());
    }
}
