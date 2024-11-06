package com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.controller;

import com.sanapyeong.mtvs_3rd_dreamplanet.ResponseMessage;
import com.sanapyeong.mtvs_3rd_dreamplanet.component.UserTokenStorage;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.dto.MyUniverseTrainFindResponseDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.services.MyUniverseTrainService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Tag(name="My Universe Train Controller", description = "My Universe Train Controller")
@RestController
@RequestMapping("/v1/dev")
public class MyUniverseTrainController {

    private final MyUniverseTrainService myUniverseTrainService;
    private final UserTokenStorage userTokenStorage;

    @Autowired
    public MyUniverseTrainController(
            MyUniverseTrainService myUniverseTrainService,
            UserTokenStorage userTokenStorage
    ){
        this.myUniverseTrainService = myUniverseTrainService;
        this.userTokenStorage = userTokenStorage;
    }

    @GetMapping("/my-universe-trains")
    public ResponseEntity<?> findMyUniverseTrainByUserId(
            @RequestParam Long userId
    ){
        // Response Message 기본 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

        // 만약 입력된 토큰에 해당하는 유저가 없다면
        if (userId == null) {
            ResponseMessage responseMessage = new ResponseMessage(404, "사용자 없음", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.NOT_FOUND);
        }

        MyUniverseTrainFindResponseDTO findResult =
                myUniverseTrainService.findMyUniverseTrainByUserId(userId);

        responseMap.put("myUniverseTrain", findResult);

        ResponseMessage responseMessage = new ResponseMessage(200, "열차 정보 반환 성공", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    @PostMapping("/my-universe-trains")
    public ResponseEntity<?> createMyUniverseTrain(
            @RequestParam String trainName,
            HttpServletRequest request
    ){
        // Response Message 기본 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

        // 헤더에서 토큰 추출
        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Authorization header: " + authorizationHeader);

        // User Token Storage에서 해당 토큰에 맞는 유저 식별
        Long userId = userTokenStorage.getToken(authorizationHeader);

        // 만약 입력된 토큰에 해당하는 유저가 없다면
        if (userId == null) {
            ResponseMessage responseMessage = new ResponseMessage(404, "사용자 없음", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.NOT_FOUND);
        }

        myUniverseTrainService.createMyUniverseTrain(userId, trainName);

        ResponseMessage responseMessage = new ResponseMessage(201, "나만의 우주열차 생성 성공", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.CREATED);
    }
}
