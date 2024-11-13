package com.sanapyeong.mtvs_3rd_dreamplanet.user.controller;

import com.sanapyeong.global.security.utils.JwtUtil;
import com.sanapyeong.mtvs_3rd_dreamplanet.ResponseMessage;
import com.sanapyeong.mtvs_3rd_dreamplanet.component.UserTokenStorage;
import com.sanapyeong.mtvs_3rd_dreamplanet.user.dto.UserLoginDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.user.dto.UserSignUpDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.user.entities.User;
import com.sanapyeong.mtvs_3rd_dreamplanet.user.enums.UserColor;
import com.sanapyeong.mtvs_3rd_dreamplanet.user.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Tag(name="User Controller", description = "User Controller")
@RestController
@RequestMapping("/v1/user")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class UserController {

    private final UserService userService;
    private final UserTokenStorage userTokenStorage;

    @Autowired
    public UserController(
            UserService userService,
            UserTokenStorage userTokenStorage
    ){
        this.userService = userService;
        this.userTokenStorage = userTokenStorage;
    }

//    // 유저 탐색용
//    @GetMapping("/users")
//    public ResponseEntity<User> findUserById(@RequestParam Long id){
//
//        return ResponseEntity.ok(userService.findUserById(id));
//    }

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입", description = "회원가입 API")
    public ResponseEntity<ResponseMessage> signUp(@RequestBody UserSignUpDTO userInfo){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

        User user = new User(
                userInfo.getNickname(),
                userInfo.getLoginId(),
                BCrypt.hashpw(userInfo.getLoginPw(), BCrypt.gensalt()),
                UserColor.BLUE
        );

        try {
            userService.registerUser(user);

            ResponseMessage responseMessage = new ResponseMessage(201, "회원가입 성공", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.CREATED);
//            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
//            return ResponseEntity.ok().headers(headers).body();
        } catch (Exception e) {
            ResponseMessage responseMessage = new ResponseMessage(409, "Error: 이미 존재하는 사용자입니다.", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인 API")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO loginInfo){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

        User user = userService.findUserByLoginId(loginInfo.getLoginId());

        if(user == null){
            ResponseMessage responseMessage = new ResponseMessage(401, "사용자 없음", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.UNAUTHORIZED);

            // return ErrorResponseHandler.get(HttpStatus.UNAUTHORIZED, "사용자 없음")
            // 사용자 없음
        }

        if(!BCrypt.checkpw(loginInfo.getLoginPw(), user.getLoginPw())){
            ResponseMessage responseMessage = new ResponseMessage(401, "비밀번호 불일치", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.UNAUTHORIZED);

            // return ErrorResponseHandler.get(HttpStatus.UNAUTHORIZED, "비밀번호 불일치")
            // 비밀번호 불일치
        }

        String token = JwtUtil.generateToken(loginInfo.getLoginId());
        userTokenStorage.storeToken(token, user.getId());

        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        responseMap.put("nickname", user.getNickname());
        responseMap.put("color", user.getColor());

        ResponseMessage responseMessage = new ResponseMessage(200, "로그인 성공", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그아웃 API")
    public ResponseEntity<?> logout(HttpServletRequest request){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader == null){
            ResponseMessage responseMessage = new ResponseMessage(401, "로그인이 필요합니다.", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.UNAUTHORIZED);
        }

        userTokenStorage.removeToken(authorizationHeader);

        ResponseMessage responseMessage = new ResponseMessage(200, "로그아웃 성공", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    @PatchMapping("/color-changing")
    @Operation(summary = "유저 캐릭터 색상 변경", description = "유저 캐릭터 색상 변경 API")
    public ResponseEntity<?> changeUserColor(
            @RequestParam UserColor color,
            HttpServletRequest request
    ){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Authorization header: " + authorizationHeader);

        // User Token Storage에서 해당 토큰에 맞는 유저 식별
        Long userId = userTokenStorage.getToken(authorizationHeader);

        User user = userService.findUserById(userId);

        userService.modifyUserColor(user, color);

        responseMap.put("color", user.getColor());

        ResponseMessage responseMessage = new ResponseMessage(200, "색상 변경 성공", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }
}
