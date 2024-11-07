package com.sanapyeong.mtvs_3rd_dreamplanet.test;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/dev")
public class TestController {

    @GetMapping("/sending-test")
    @Operation(summary = "GET 요청 테스트", description = "GET 요청 테스트 API")
    public Test sendingTest() {
        Test test = new Test();
        test.setContent("new content");

        return test;
    }

    @PostMapping("/receiving-test")
    @Operation(summary = "POST 요청 테스트", description = "POST 요청 테스트 API")
    public void receivingTest(@RequestBody Test test) {

        Test receivedTest = test;

        System.out.println(receivedTest.toString());
    }
}
