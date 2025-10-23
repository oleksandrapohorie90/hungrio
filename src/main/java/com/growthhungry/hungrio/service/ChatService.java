package com.growthhungry.hungrio.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model}")
    private String model;

    @Value("${gemini.base-url}")
    private String baseUrl;

    @Value("${gemini.api.version}")
    private String apiVersion;

    private final RestTemplate restTemplate = new RestTemplate();

    public String sendMessage(String userMessage) {
        // Build URL: POST .../v1beta/models/{model}:generateContent?key=API_KEY
String url = String.format(
  "%s/%s/models/%s:generateContent?key=%s",
  baseUrl, apiVersion, model, apiKey
);


        // Minimal request body for Gemini:
        Map<String, Object> requestBody = Map.of(
            "contents", List.of(
                Map.of("parts", List.of(
                    Map.of("text", userMessage)
                ))
            )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, Map.class);

        // Extract: candidates[0].content.parts[0].text (defensive)
        Map body = response.getBody();
        if (body == null) return "";

        Object candidatesObj = body.get("candidates");
        if (!(candidatesObj instanceof List<?> candidates) || candidates.isEmpty()) return "";

        Object firstObj = candidates.get(0);
        if (!(firstObj instanceof Map<?,?> first)) return "";

        Object contentObj = first.get("content");
        if (!(contentObj instanceof Map<?,?> content)) return "";

        Object partsObj = content.get("parts");
        if (!(partsObj instanceof List<?> parts) || parts.isEmpty()) return "";

        Object part0Obj = parts.get(0);
        if (!(part0Obj instanceof Map<?,?> part0)) return "";

        Object textObj = part0.get("text");
        return textObj == null ? "" : textObj.toString();
    }
}
