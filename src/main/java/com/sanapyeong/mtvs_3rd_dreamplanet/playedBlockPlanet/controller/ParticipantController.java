package com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.controller;

import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.entities.Participant;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.entities.PlayedBlockPlanet;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.services.ParticipantService;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.services.PlayedBlockPlanetService;
import com.sanapyeong.mtvs_3rd_dreamplanet.user.entities.User;
import com.sanapyeong.mtvs_3rd_dreamplanet.user.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequiredArgsConstructor
@RequestMapping("/v1/dev")
public class ParticipantController {

    private final ParticipantService participantService;
    private final PlayedBlockPlanetService playedBlockPlanetService;
    private final UserService userService;

    @Autowired
    public ParticipantController(
            ParticipantService participantService,
            PlayedBlockPlanetService playedBlockPlanetService,
            UserService userService
    ) {
        this.participantService = participantService;
        this.playedBlockPlanetService = playedBlockPlanetService;
        this.userService = userService;
    }

    @PostMapping("/participants")
    public ResponseEntity<?> enterPlayedBlockPlanet(
            // @RequestBody ParticipantRegisterDTO participantInfo
            @RequestParam("playedBlockPlanetId") Long playedBlockPlanetId,
            @RequestParam("userId") Long userId,
            HttpServletRequest request
    ) {

        // String authorizationHeader = request.getHeader("Authorization");

        if(userId == null) {
            // return ErrorResponseHandler.get(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        try {
            PlayedBlockPlanet playedBlockPlanet = playedBlockPlanetService.findPlayedBlockPlanetById(playedBlockPlanetId);
            User user = userService.findUserById(userId);

            Participant participant = new Participant(playedBlockPlanet, user);
            participantService.registerParticipant(participant);
        } catch (Exception e) {
            return null;
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
