package com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.controller;

import com.sanapyeong.mtvs_3rd_dreamplanet.ResponseMessage;
import com.sanapyeong.mtvs_3rd_dreamplanet.component.UserTokenStorage;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.dto.MyUniverseTrainFindResponseDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.services.MyUniverseTrainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Tag(name="My Universe Train Controller", description = "My Universe Train Controller")
@RestController
@RequestMapping("/v1/dev")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
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
    @Operation(summary = "나만의 우주 열차 조회", description = "유저 아이디를 통한 나만의 우주 열차 조회 API")
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
                null;
        try {
            findResult = myUniverseTrainService.findMyUniverseTrainByUserId(userId);
        } catch (IOException e) {
            ResponseMessage responseMessage = new ResponseMessage(404, "열차 없음", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.NOT_FOUND);
        }

        responseMap.put("myUniverseTrain", findResult);

        ResponseMessage responseMessage = new ResponseMessage(200, "열차 정보 반환 성공", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    @PostMapping("/my-universe-trains")
    @Operation(summary = "나만의 우주 열차 생성", description = "나만의 우주 열차 생성 API")
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

    @PatchMapping("/my-universe-trains/modifying-planet-order")
    @Operation(summary = "행성 배치 순서 수정", description = "나만의 우주 열차 행성 배치 순서 수정 API")
    @Parameters({
            @Parameter(name="planetOrder", description = "행성 배치 순서. (앞에 List는 빼주세요!)", example = "[1, 2, 0, 0, 0]")
    })
    public ResponseEntity<?> modifyPlanetOrder(
            @RequestParam String planetOrder,
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

        try {
            myUniverseTrainService.modifyPlanetOrder(userId, planetOrder);
        } catch (Exception e) {
            ResponseMessage responseMessage = new ResponseMessage(400, "잘못된 요청입니다", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.BAD_REQUEST);
        }

        ResponseMessage responseMessage = new ResponseMessage(200, "planet order 수정 성공", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    @PatchMapping("/my-universe-trains/modifying-planet-status")
    @Operation(summary = "행성 배치 여부 수정", description = "나만의 우주 열차 행성 배치 여부 수정 API")
    @Parameters({
            @Parameter(name="planetStatus", description = "행성 배치 상태. (앞에 List는 빼주세요!)", example = "[true, true, false, false, false]")
    })
    public ResponseEntity<?> modifyPlanetStatus(
            @RequestParam String planetStatus,
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

        try {
            myUniverseTrainService.modifyPlanetStatus(userId, planetStatus);
        } catch (Exception e) {
            ResponseMessage responseMessage = new ResponseMessage(400, "잘못된 요청입니다", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.BAD_REQUEST);
        }

        ResponseMessage responseMessage = new ResponseMessage(200, "planet status 수정 성공", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }
}
