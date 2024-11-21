package com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import com.sanapyeong.mtvs_3rd_dreamplanet.ResponseMessage;
import com.sanapyeong.mtvs_3rd_dreamplanet.aiService.ImageGenerationService;
import com.sanapyeong.mtvs_3rd_dreamplanet.aiService.PreSetCallService;
import com.sanapyeong.mtvs_3rd_dreamplanet.aiService.PromptInspectionService;
import com.sanapyeong.mtvs_3rd_dreamplanet.component.UserTokenStorage;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.dto.CompletedWorkSaveDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.dto.DotImageUrlFindResponseDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.dto.PlayedBlockPlanetCreateRequestDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.entities.PlayedBlockPlanet;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.services.PlayedBlockPlanetService;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.services.S3Service;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.services.TempBlockPlanetStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name="Played Block Planet Controller", description = "Played Block Planet Controller")
@RestController
@RequestMapping("/v1/dev")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class PlayedBlockPlanetController {

    private final PlayedBlockPlanetService playedBlockPlanetService;
    private final UserTokenStorage userTokenStorage;
    private final S3Service s3Service;
    private final PromptInspectionService promptInspectionService;
    private final ImageGenerationService imageGenerationService;
    private final TempBlockPlanetStorageService tempBlockPlanetStorageService;
    private final PreSetCallService preSetCallService;

    @Autowired
    public PlayedBlockPlanetController(
            PlayedBlockPlanetService playedBlockPlanetService,
            UserTokenStorage userTokenStorage,
            S3Service s3Service,
            PromptInspectionService promptInspectionService,
            ImageGenerationService imageGenerationService,
            TempBlockPlanetStorageService tempBlockPlanetStorageService,
            PreSetCallService preSetCallService
    ) {
        this.playedBlockPlanetService = playedBlockPlanetService;
        this.userTokenStorage = userTokenStorage;
        this.s3Service = s3Service;
        this.promptInspectionService = promptInspectionService;
        this.imageGenerationService = imageGenerationService;
        this.tempBlockPlanetStorageService = tempBlockPlanetStorageService;
        this.preSetCallService = preSetCallService;
    }

    // 플레이 할 블록 행성 생성
    @PostMapping("/played-block-planets")
    @Operation(summary = "플레이 할 블록 행성 생성 API", description = "플레이 할 블록 행성 생성 API")
    public ResponseEntity<?> createPlayedBlockPlanet(
            @RequestParam String photonPlanetId
    ) {
        // Response Message 기본 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

        // 포톤 서버 id를 파라미터로 입력받아, 새로운 객체 생성
        PlayedBlockPlanet playedBlockPlanet = new PlayedBlockPlanet(photonPlanetId);

        // 해당 객체를 저장한 후, played block planet의 id 받아옴
        Long id = playedBlockPlanetService.createPlayedBlockPlanet(playedBlockPlanet);

        //responseMap에 playedBlockPlanetId 삽입
        responseMap.put("playedBlockPlanetId", id);

        // 정상적으로 저장되었을 경우
        ResponseMessage responseMessage = new ResponseMessage(201, "플레이 할 블록 행성 생성 성공", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.CREATED);
    }

    // 도트 이미지 생성
    @PatchMapping("/played-block-planets/dot-images")
    @Operation(summary = "도트 이미지 생성", description = "컬러 및 흑백 도트 이미지 생성 API")
    public ResponseEntity<?> createDotImage(
            @RequestParam Long playedBlockPlanetId,
            @RequestParam String prompt
    ) throws IOException {

        // Response Message 기본 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

        // 존재하는 played block planet인지 확인
        PlayedBlockPlanet playedBlockPlanet = playedBlockPlanetService.findPlayedBlockPlanetById(playedBlockPlanetId);
        if(playedBlockPlanet == null){
            return ResponseEntity.notFound().build();
        }

//        // ai image 생성 요청 api
//        // 성공 여부 반환
//
//        String translatedText = promptInspectionService.inspectPrompt(prompt);
//
//        if(translatedText == null){
//            ResponseMessage responseMessage = new ResponseMessage(400, "올바르지 못한 프롬프트입니다.", responseMap);
//            return new ResponseEntity<>(responseMessage, headers, HttpStatus.BAD_REQUEST);
//        }
//
//        // 성공 시, 컬러 도트 이미지 및 흑백 도트 이미지 반환
//        // 두 이미지 S3 서버에 저장 후 URL 주소 반환
//        List<MultipartFile> dotImages = null;
//
//        try {
//            dotImages = imageGenerationService.generateDotImage(translatedText);
//        } catch (IOException e) {
//            ResponseMessage responseMessage = new ResponseMessage(500, "컬러 및 흑백 도트 이미지 생성 실패", responseMap);
//            return new ResponseEntity<>(responseMessage, headers, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        MultipartFile colorDotImage = dotImages.get(0);
//        MultipartFile blackAndWhiteDotImage = dotImages.get(1);
//
//        String colorDotImageURL = s3Service.saveColorDotImage(colorDotImage, playedBlockPlanetId);
//        String blackAndWhiteDotImageURL = s3Service.saveBlackAndWhiteDotImage(blackAndWhiteDotImage, playedBlockPlanetId);
//
//        // 프롬프트 및 URL 주소 저장
//        playedBlockPlanetService.savePrompt(playedBlockPlanetId, prompt);
//        playedBlockPlanetService.saveColorDotImage(playedBlockPlanetId, colorDotImageURL);
//        playedBlockPlanetService.saveBlackAndWhiteDotImage(playedBlockPlanetId, blackAndWhiteDotImageURL);
//
//
//        responseMap.put("colorDotImage", colorDotImageURL);
//        responseMap.put("blackAndWhiteDotImage", blackAndWhiteDotImageURL);



        // ai image 생성 요청 api
        // 성공 여부 반환

        String translatedText = promptInspectionService.inspectPrompt(prompt);

        if(translatedText == null){
            ResponseMessage responseMessage = new ResponseMessage(400, "올바르지 못한 프롬프트입니다.", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.BAD_REQUEST);
        }

        // prompt 저장
        playedBlockPlanetService.savePrompt(playedBlockPlanetId, prompt);

        // 성공 시, 컬러 도트 이미지 및 흑백 도트 이미지 반환
        // 두 이미지 S3 서버에 저장 후 URL 주소 반환
        List<MultipartFile> dotImages = null;

        List<DotImageUrlFindResponseDTO> dotImageUrlList = null;

        // AI 호출을 통한 이미지 생성
        for(int i = 1; i < 3; i++) {

            try {
                dotImages = imageGenerationService.generateDotImage(translatedText);
            } catch (IOException e) {
                ResponseMessage responseMessage = new ResponseMessage(500, "컬러 및 흑백 도트 이미지 생성 실패", responseMap);
                return new ResponseEntity<>(responseMessage, headers, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            MultipartFile colorDotImage = dotImages.get(0);
            MultipartFile blackAndWhiteDotImage = dotImages.get(1);

            String colorDotImageURL = s3Service.saveColorDotImage(colorDotImage, playedBlockPlanetId, (long) i);
            String blackAndWhiteDotImageURL = s3Service.saveBlackAndWhiteDotImage(blackAndWhiteDotImage, playedBlockPlanetId, (long) i);

            // URL 주소 저장
            tempBlockPlanetStorageService.saveDotImageURL(playedBlockPlanetId, (long) i, colorDotImageURL, blackAndWhiteDotImageURL);

            DotImageUrlFindResponseDTO dotImageUrl
                    = new DotImageUrlFindResponseDTO((long) i, colorDotImageURL, blackAndWhiteDotImageURL);

            dotImageUrlList.add(dotImageUrl);
        }

        // 프리셋 호출
        dotImages = preSetCallService.generateDotImage();

        MultipartFile colorDotImage = dotImages.get(0);
        MultipartFile blackAndWhiteDotImage = dotImages.get(1);

        String colorDotImageURL = s3Service.saveColorDotImage(colorDotImage, playedBlockPlanetId, 3L);
        String blackAndWhiteDotImageURL = s3Service.saveBlackAndWhiteDotImage(blackAndWhiteDotImage, playedBlockPlanetId, 3L);

        // URL 주소 저장
        tempBlockPlanetStorageService.saveDotImageURL(playedBlockPlanetId, 3L, colorDotImageURL, blackAndWhiteDotImageURL);

        DotImageUrlFindResponseDTO dotImageUrl
                = new DotImageUrlFindResponseDTO( 3L, colorDotImageURL, blackAndWhiteDotImageURL);

        dotImageUrlList.add(dotImageUrl);

        responseMap.put("dotImageUrlList", dotImageUrlList);

        ResponseMessage responseMessage = new ResponseMessage(201, "컬러 및 흑백 도트 이미지 저장 성공", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.CREATED);
    }

    @PatchMapping("/played-block-planets/choice")
    @Operation(summary = "도트 이미지 선택", description = "도트 이미지 세트 선택 API")
    public ResponseEntity<?> choiceDotImage(
            @RequestParam Long playedBlockPlanetId,
            @RequestBody DotImageUrlFindResponseDTO dotImageUrl
    ){
        // Response Message 기본 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

        // 존재하는 played block planet인지 확인
        PlayedBlockPlanet playedBlockPlanet = playedBlockPlanetService.findPlayedBlockPlanetById(playedBlockPlanetId);
        if(playedBlockPlanet == null){
            // 블록 행성 없음
            ResponseMessage responseMessage = new ResponseMessage(404, "해당하는 블록 행성 플레이 없음", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.NOT_FOUND);
        }

        playedBlockPlanetService.saveIdx(playedBlockPlanetId, dotImageUrl.getIdx());
        playedBlockPlanetService.saveColorDotImage(playedBlockPlanetId, dotImageUrl.getColorDotImageUrl());
        playedBlockPlanetService.saveBlackAndWhiteDotImage(playedBlockPlanetId, dotImageUrl.getBlackAndWhiteDotImageUrl());

        ResponseMessage responseMessage = new ResponseMessage(201, "도트 이미지 url 및 idx 저장 성공", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.CREATED);
    }


    // 완성 작품 저장
    @PatchMapping(
            value = "/played-block-planets/completed-work",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "블록 행성 완성 작품 저장", description = "블록 행성 완성 작품 저장 API")
    public ResponseEntity<?> saveCompletedWork(
            @RequestParam Long playedBlockPlanetId,
            @RequestParam MultipartFile completedWork,
            HttpServletRequest request
    ) throws IOException {
        // Response Message 기본 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

        // 존재하는 played block planet인지 확인
        PlayedBlockPlanet playedBlockPlanet = playedBlockPlanetService.findPlayedBlockPlanetById(playedBlockPlanetId);
        if(playedBlockPlanet == null){
            // 블록 행성 없음
            ResponseMessage responseMessage = new ResponseMessage(404, "해당하는 블록 행성 플레이 없음", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.NOT_FOUND);
        }

        // S3 서버 이미지 저장
        // 저장 후, URL 주소 반환
        String completedWorkURL = s3Service.saveCompleteWork(completedWork, playedBlockPlanetId);

        // URL 주소 저장
        playedBlockPlanetService.saveCompletedWork(playedBlockPlanetId, completedWorkURL);

        responseMap.put("completedWork", completedWorkURL);

        ResponseMessage responseMessage = new ResponseMessage(201, "완성 작품 저장 성공", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.CREATED);
    }

//    @GetMapping("/played-block-planet/preset-test")
}
