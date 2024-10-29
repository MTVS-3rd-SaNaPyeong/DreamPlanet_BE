package com.sanapyeong.mtvs_3rd_dreamplanet.user.controller;

import com.sanapyeong.global.security.utils.JwtUtil;
import com.sanapyeong.mtvs_3rd_dreamplanet.ResponseMessage;
import com.sanapyeong.mtvs_3rd_dreamplanet.component.UserTokenStorage;
import com.sanapyeong.mtvs_3rd_dreamplanet.user.dto.UserLoginDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.user.dto.UserSignUpDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.user.entities.User;
import com.sanapyeong.mtvs_3rd_dreamplanet.user.services.UserService;
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
    public ResponseEntity<ResponseMessage> signUp(@RequestBody UserSignUpDTO userInfo){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

        User user = new User(
                userInfo.getNickname(),
                userInfo.getLoginId(),
                BCrypt.hashpw(userInfo.getLoginPw(), BCrypt.gensalt())
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

        ResponseMessage responseMessage = new ResponseMessage(200, "로그인 성공", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){

        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader == null){
            return null;
        }

        userTokenStorage.removeToken(authorizationHeader);
        return ResponseEntity.ok("로그아웃 성공");
    }
}
