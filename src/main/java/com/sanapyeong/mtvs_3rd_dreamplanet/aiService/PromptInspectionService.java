package com.sanapyeong.mtvs_3rd_dreamplanet.aiService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PromptInspectionService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String inspectPrompt(String prompt){

        String url = "https://4529-59-13-225-125.ngrok-free.app/process_text";

        // 헤더 설정 (필요한 경우)
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // 요청 본문 설정
        String requestBody = String.format("{\"prompt\":\"%s\"}", prompt);

        // HttpEntity 객체 생성
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // POST 요청 전송
        ResponseEntity<String> response = restTemplate.postForEntity(
                url, request, String.class
        );

        System.out.println(response.getBody());

        // JSON 파싱하여 translated_text 추출
        String correctedText = null;
        String translatedText = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            correctedText = root.path("text_processing").path("corrected_text").asText();
            translatedText = root.path("text_processing").path("translated_text").asText();
        } catch (Exception e) {
            return null;
        }

        if(correctedText.equals("'INVALID'")){
            return null;
        }

        return translatedText;
    }
}
