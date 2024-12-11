package com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.controller;

import com.amazonaws.services.kms.model.NotFoundException;
import com.sanapyeong.mtvs_3rd_dreamplanet.inventory.services.PostingInfoService;
import com.sanapyeong.mtvs_3rd_dreamplanet.ResponseMessage;
import com.sanapyeong.mtvs_3rd_dreamplanet.component.UserTokenStorage;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.dto.MyUniverseTrainFindResponseDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.dto.MyUniverseTrainSummaryFindResponseDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.enums.TrainColor;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.services.MyUniverseTrainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Tag(name="My Universe Train Controller", description = "My Universe Train Controller")
@RestController
@RequestMapping("/v1/dev")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class MyUniverseTrainController {

    private final MyUniverseTrainService myUniverseTrainService;
    private final UserTokenStorage userTokenStorage;
    private final PostingInfoService postingInfoService;

    @Autowired
    public MyUniverseTrainController(
            MyUniverseTrainService myUniverseTrainService,
            UserTokenStorage userTokenStorage,
            PostingInfoService postingInfoService
    ){
        this.myUniverseTrainService = myUniverseTrainService;
        this.userTokenStorage = userTokenStorage;
        this.postingInfoService = postingInfoService;
    }

    @GetMapping("/my-universe-trains")
    @Operation(summary = "나만의 우주 열차 조회", description = "유저 아이디를 통한 나만의 우주 열차 조회 API")
    public ResponseEntity<?> findMyUniverseTrainsByToken(
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

//        List<MyUniverseTrainFindResponseDTO> findResultList = null;
//
//        try {
//            findResultList = myUniverseTrainService.findMyUniverseTrainsByUserId(userId);
//        } catch (IOException e) {
//            ResponseMessage responseMessage = new ResponseMessage(404, "열차 없음", responseMap);
//            return new ResponseEntity<>(responseMessage, headers, HttpStatus.NOT_FOUND);
//        }
//
//        responseMap.put("myUniverseTrainList", findResultList);

        List<MyUniverseTrainSummaryFindResponseDTO> foundList = null;

        try {
            foundList = myUniverseTrainService.findMyUniverseTrainsByUserId(userId);
        } catch (IOException e) {
            ResponseMessage responseMessage = new ResponseMessage(404, "열차 없음", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.NOT_FOUND);
        }

        responseMap.put("myUniverseTrainList", foundList);


        ResponseMessage responseMessage = new ResponseMessage(200, "열차 정보 반환 성공", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    @GetMapping("/my-universe-trains/{id}")
    @Operation(summary="우주 열차 상세 정보 조회", description = "우주 열차 상세 정보 조회 API")
    public ResponseEntity<?> findMyUniverseTrainByTrainId(
            @PathVariable Long id
    ){
        // Response Message 기본 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

        MyUniverseTrainFindResponseDTO findResult = null;

        try {
            findResult = myUniverseTrainService.findById(id);
        } catch (IOException e) {
            ResponseMessage responseMessage = new ResponseMessage(404, "열차 없음", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.NOT_FOUND);
        }

        responseMap.put("myUniverseTrain", findResult);

        ResponseMessage responseMessage = new ResponseMessage(200, "열차 정보 반환 성공", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    @GetMapping("/my-universe-trains/searching")
    @Operation(summary = "우주 열차 검색", description = "열차 이름 또는 고유 코드로 우주 열차 검색")
    public ResponseEntity<?> findMyUniverseTrainBySearchWord(
            @RequestParam String searchWord
    ){
        // Response Message 기본 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

        List<MyUniverseTrainSummaryFindResponseDTO> foundList = null;

        foundList = myUniverseTrainService.findMyUniverseTrainsBySearchWord(searchWord);

        responseMap.put("myUniverseTrainList", foundList);

        ResponseMessage responseMessage = new ResponseMessage(200, "열차 검색 성공", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    @GetMapping("/my-universe-trains/random")
    @Operation(summary = "랜덤 우주 열차 검색", description = "랜덤으로 4개의 우주 열차를 검색")
    public ResponseEntity<?> findRandomMyUniverseTrains(
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

        List<MyUniverseTrainSummaryFindResponseDTO> foundList
                = myUniverseTrainService.findRandomMyUniverseTrains(userId);

        responseMap.put("myUniverseTrainList", foundList);


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

        // 나만의 우주 열차 저장 후, 우주 열차 id 반환
        Long myUniverseTrainId = myUniverseTrainService.createMyUniverseTrain(userId, trainName);

        // PostingInfo에 각 myUniverseTrain과의 조합에 대해서 행 생성
        postingInfoService.savePostingInfoByMyUniverseTrain(userId, myUniverseTrainId);

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
            @RequestParam Long myUniverseTrainId,
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
            myUniverseTrainService.modifyPlanetOrder(userId, planetOrder, myUniverseTrainId);
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
            @RequestParam Long myUniverseTrainId,
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
            myUniverseTrainService.modifyPlanetStatus(userId, planetStatus, myUniverseTrainId);
        } catch (Exception e) {
            ResponseMessage responseMessage = new ResponseMessage(400, "잘못된 요청입니다", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.BAD_REQUEST);
        }

        ResponseMessage responseMessage = new ResponseMessage(200, "planet status 수정 성공", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    @PatchMapping("/my-universe-trains/color-changing")
    @Operation(summary = "나만의 우주 열차 색상 변경", description = "나만의 우주 열차 색상 변경 API")
    public ResponseEntity<?> modifyMyUniverseTrainColor(
            @RequestParam Long myUniverseTrainId,
            @RequestParam TrainColor trainColor,
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
            myUniverseTrainService.modifyMyUniverseTrainColor(myUniverseTrainId, userId, trainColor);
        } catch (NotFoundException e){
            ResponseMessage responseMessage = new ResponseMessage(404, "열차 없음", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e){
            ResponseMessage responseMessage = new ResponseMessage(400, "수정 요청 유저와 열차 소유주 불일치", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.BAD_REQUEST);
        }

        ResponseMessage responseMessage = new ResponseMessage(200, "train color 수정 성공", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }


    @DeleteMapping("/my-universe-trains")
    @Operation(summary = "나만의 우주 열차 삭제", description = "나만의 우주 열차 삭제 API")
    public ResponseEntity<?> deleteMyUniverseTrain(
        @RequestParam Long myUniverseTrainId,
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

        // 나만의 우주 열차와 PostingInfo 삭제\
        try {
            myUniverseTrainService.deleteMyUniverseTrain(myUniverseTrainId, userId);
        }catch (NotFoundException e){
            ResponseMessage responseMessage = new ResponseMessage(404, "열차 없음", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.NOT_FOUND);
        }catch (IllegalArgumentException e){
            ResponseMessage responseMessage = new ResponseMessage(400, "삭제 요청 유저와 열차 소유주 불일치", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.BAD_REQUEST);
        }

        ResponseMessage responseMessage = new ResponseMessage(200, "나만의 우주 열차 삭제 완료", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }
}
