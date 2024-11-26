package com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.controller;

import com.sanapyeong.mtvs_3rd_dreamplanet.ResponseMessage;
import com.sanapyeong.mtvs_3rd_dreamplanet.component.UserTokenStorage;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.entities.Participant;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.entities.PlayedBlockPlanet;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.services.ParticipantService;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.services.PlayedBlockPlanetService;
import com.sanapyeong.mtvs_3rd_dreamplanet.user.entities.User;
import com.sanapyeong.mtvs_3rd_dreamplanet.user.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
//@RequiredArgsConstructor
@RequestMapping("/v1/dev")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ParticipantController {

    private final ParticipantService participantService;
    private final PlayedBlockPlanetService playedBlockPlanetService;
    private final UserService userService;
    private final UserTokenStorage userTokenStorage;

    @Autowired
    public ParticipantController(
            ParticipantService participantService,
            PlayedBlockPlanetService playedBlockPlanetService,
            UserService userService,
            UserTokenStorage userTokenStorage
    ) {
        this.participantService = participantService;
        this.playedBlockPlanetService = playedBlockPlanetService;
        this.userService = userService;
        this.userTokenStorage = userTokenStorage;
    }

    // 플레이 할 블록 행성 입장
    @PostMapping("/block-planet-participants")
    @Operation(summary = "플레이 할 블록 행성 입장", description = "블록 행성에 입장할 시, 유저의 참여 정보를 저장하는 API")
    public ResponseEntity<?> enterPlayedBlockPlanet(
            // @RequestBody ParticipantRegisterDTO participantInfo
            @RequestParam("playedBlockPlanetId") Long playedBlockPlanetId,
            HttpServletRequest request
    ) {

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
            ResponseMessage responseMessage = new ResponseMessage(401, "사용자 없음", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.UNAUTHORIZED);
        }

        try {
            PlayedBlockPlanet playedBlockPlanet = playedBlockPlanetService.findPlayedBlockPlanetById(playedBlockPlanetId);
            User user = userService.findUserById(userId);

            Participant participant = new Participant(playedBlockPlanet, user);
            participantService.registerParticipant(participant);
        } catch (Exception e) {
            ResponseMessage responseMessage = new ResponseMessage(400, "블록 행성 입장 불가", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.BAD_REQUEST);
        }

        // 정상적으로 저장되었을 경우
        ResponseMessage responseMessage = new ResponseMessage(201, "블록 행성 입장 성공", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.CREATED);
    }
}
