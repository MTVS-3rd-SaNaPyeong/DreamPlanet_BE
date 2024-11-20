package com.sanapyeong.mtvs_3rd_dreamplanet.aiService;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PreSetCallService {

    public List<MultipartFile> generateDotImage() throws IOException {

        String url = "http://221.163.19.142:17777/images";
        List<MultipartFile> dotImages = new ArrayList<>();

        // HttpClient 생성
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet get = new HttpGet(url);
            get.setHeader("Content-Type", "application/json");

            // GET 요청 수행 및 응답 처리
            try (CloseableHttpResponse response = httpClient.execute(get)) {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    // 응답 본문을 바이트 배열로 변환
                    byte[] responseBody = EntityUtils.toByteArray(responseEntity);

                    // 바운더리로 분할
                    String boundary = "boundary123456";  // API에서 사용한 boundary를 설정
                    byte[][] filesData = splitMultipartData(responseBody, boundary);

                    System.out.println(filesData);

                    System.out.println(filesData.length);
                    // 각 파일 데이터를 MultipartFile로 변환 후 리스트에 추가
                    for (int i = 0; i < filesData.length; i++) {
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(filesData[i]);
                        MultipartFile multipartFile = new MockMultipartFile(
                                "file" + i,
                                "generatedImage" + i + ".png",
                                MediaType.IMAGE_PNG_VALUE,
                                byteArrayInputStream
                        );
                        dotImages.add(multipartFile);
                    }
                }
            }
        }
        return dotImages;
    }

    private byte[][] splitMultipartData(byte[] data, String boundary) {
        String boundaryString = "--" + boundary;
        byte[] boundaryBytes = boundaryString.getBytes(StandardCharsets.UTF_8);
        List<byte[]> fileData = new ArrayList<>();

        int pos = 0;
        while (pos < data.length) {
            // boundary를 찾기
            int boundaryIndex = indexOf(data, boundaryBytes, pos);
            if (boundaryIndex == -1) break; // 더 이상 boundary가 없으면 종료

            // boundary 다음 파트로 이동
            int nextPartIndex = boundaryIndex + boundaryBytes.length;

            // Content-Type 확인을 위해서 다음 boundary까지의 데이터를 파싱
            int nextBoundaryIndex = indexOf(data, boundaryBytes, nextPartIndex);
            if (nextBoundaryIndex == -1) break; // 마지막 파트

            // 현재 파트의 데이터 추출
            byte[] part = Arrays.copyOfRange(data, nextPartIndex, nextBoundaryIndex);

            // 이미지 데이터인지 확인 (헤더에 "Content-Type: image/png"가 있는지)
            String partString = new String(part, StandardCharsets.UTF_8);
            if (partString.contains("Content-Type: image/png")) {
                int headerEndIndex = partString.indexOf("\r\n\r\n");
                if (headerEndIndex > 0) {
                    int contentStartIndex = headerEndIndex + 4;
                    byte[] content = Arrays.copyOfRange(part, contentStartIndex, part.length);
                    fileData.add(content);
                }
            }

            // 다음 위치로 이동
            pos = nextBoundaryIndex;
        }
        return fileData.toArray(new byte[0][0]);
    }

    // 바이트 배열에서 특정 패턴을 찾기 위한 헬퍼 메서드
    private int indexOf(byte[] data, byte[] pattern, int start) {
        for (int i = start; i <= data.length - pattern.length; i++) {
            boolean found = true;
            for (int j = 0; j < pattern.length; j++) {
                if (data[i + j] != pattern[j]) {
                    found = false;
                    break;
                }
            }
            if (found) return i;
        }
        return -1;
    }
}
