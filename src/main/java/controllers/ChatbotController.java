package controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatbotController {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @PostMapping
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> payload) {
        String userMessage = payload.get("message");

        if (!isVeterinaryRelated(userMessage)) {
            Map<String, String> response = new HashMap<>();
            response.put("response", "I'm sorry, but I can only assist you with questions related to veterinary medicine.");
            return ResponseEntity.ok(response);
        }

        RestTemplate restTemplate = new RestTemplate();
        String openaiUrl = "https://api.openai.com/v1/chat/completions";

        String systemMessage = "You are a helpful and knowledgeable assistant specializing in veterinary medicine. Provide short and precise answers to the following questions:";
        List<Map<String, String>> messages = List.of(
                Map.of("role", "system", "content", systemMessage),
                Map.of("role", "user", "content", userMessage)
        );

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 150);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + openaiApiKey);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(openaiUrl, requestEntity, Map.class);

        String botResponse = (String) ((Map<String, Object>) ((Map<String, Object>) ((List<Object>) responseEntity.getBody().get("choices")).get(0)).get("message")).get("content");

        String recommendation = getRecommendation(userMessage);
        botResponse += "\n\n" + recommendation;

        Map<String, String> response = new HashMap<>();
        response.put("response", botResponse);

        return ResponseEntity.ok(response);
    }

    private String getRecommendation(String userMessage) {
        String lowerCaseMessage = userMessage.toLowerCase();

        if (containsAny(lowerCaseMessage, "teeth", "dental", "breath", "caries", "gum", "tartar", "plaque")) {
            return "We recommend making an appointment with our Dentistry Service for proper diagnosis and treatment.";
        } else if (containsAny(lowerCaseMessage, "broken", "leg", "fissure", "fracture", "surgery", "orthopedic", "ligament")) {
            return "We recommend making an appointment with our Surgery Service for proper diagnosis and treatment.";
        } else if (containsAny(lowerCaseMessage, "vaccine", "vaccination", "needle", "syringe", "immunization", "shots")) {
            return "We recommend making an appointment with our Vaccine Service for proper diagnosis and treatment.";
        } else if (containsAny(lowerCaseMessage, "blood", "check-up", "regular", "tests", "examination", "health", "wellness")) {
            return "We recommend making an appointment with our Veterinary Care Service for proper diagnosis and treatment.";
        } else if (containsAny(lowerCaseMessage, "groom", "bath", "fur", "haircut")) {
            return "We recommend making an appointment with our Grooming Service for proper grooming and care.";
        } else if (containsAny(lowerCaseMessage, "emergency", "urgent", "critical")) {
            return "We recommend seeking immediate help with our Emergency Care Service.";
        } else if (containsAny(lowerCaseMessage, "diet", "food", "nutrition", "weight")) {
            return "We recommend making an appointment with our Nutrition Consultation Service for proper dietary advice and management.";
        } else {
            return "We recommend making an appointment with our Medical Consultation Service for proper diagnosis and treatment.";
        }
    }

    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private boolean isVeterinaryRelated(String userMessage) {
        String lowerCaseMessage = userMessage.toLowerCase();
        String[] veterinaryKeywords = {
                "teeth", "dental", "breath", "caries", "gum", "tartar", "plaque",
                "broken", "leg", "fissure", "fracture", "surgery", "orthopedic", "ligament",
                "vaccine", "vaccination", "needle", "syringe", "immunization", "shots",
                "blood", "check-up", "regular", "tests", "examination", "health", "wellness",
                "groom", "bath", "fur", "haircut",
                "emergency", "urgent", "critical",
                "diet", "food", "nutrition", "weight"
        };
        return containsAny(lowerCaseMessage, veterinaryKeywords);
    }
}