package com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.controller;

import com.sanapyeong.mtvs_3rd_dreamplanet.ResponseMessage;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.dto.MyUniverseTrainFindResponseDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.services.MyUniverseTrainService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Tag(name="My Universe Train Controller", description = "My Universe Train Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/dev")
public class MyUniverseTrainController {

    private final MyUniverseTrainService myUniverseTrainService;

    @Autowired
    public MyUniverseTrainController(
            MyUniverseTrainService myUniverseTrainService
    ){
        this.myUniverseTrainService = myUniverseTrainService;
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

}
