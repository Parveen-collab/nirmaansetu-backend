package com.nirmaansetu.backend.modules.enquiries.service;

import com.nirmaansetu.backend.modules.enquiries.dto.EnquiryAiResponseDto;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EnquiryAiService {

    private final ChatClient chatClient;

    public EnquiryAiService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public EnquiryAiResponseDto categorizeEnquiry(String message) {
        String template = """
                You are an expert construction consultant. Categorize the following enquiry message.
                Extract the type of work (e.g., Plumbing, Electrical, Masonry, Carpentry, Painting, etc.) and the urgency (Low, Medium, High).
                
                Enquiry Message: {message}
                
                Return the result in JSON format with fields: "workType" and "urgency".
                """;

        PromptTemplate promptTemplate = new PromptTemplate(template);
        Prompt prompt = promptTemplate.create(Map.of("message", message));

        return chatClient.prompt(prompt)
                .call()
                .entity(new ParameterizedTypeReference<EnquiryAiResponseDto>() {});
    }
}
