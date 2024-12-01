package com.sanapyeong.mtvs_3rd_dreamplanet.likes.controller;


import com.sanapyeong.mtvs_3rd_dreamplanet.ResponseMessage;
import com.sanapyeong.mtvs_3rd_dreamplanet.component.UserTokenStorage;
import com.sanapyeong.mtvs_3rd_dreamplanet.likes.services.LikesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Tag(name="Likes Controller", description = "Likes Controller")
@RestController
@RequestMapping("/v1/dev")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class LikesController {

    private final LikesService likesService;
    private final UserTokenStorage userTokenStorage;

    @Autowired
    public LikesController(
            LikesService likesService,
            UserTokenStorage userTokenStorage
    ) {
        this.likesService = likesService;
        this.userTokenStorage = userTokenStorage;
    }

    @PostMapping("/likes")
    @Operation()
    public ResponseEntity<?> createLikes(
        @RequestParam Long postingInfoId,
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

        likesService.createLikes(userId, postingInfoId);


        ResponseMessage responseMessage = new ResponseMessage(200, "열차 정보 반환 성공", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }
}
