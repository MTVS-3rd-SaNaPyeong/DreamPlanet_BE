package com.sanapyeong.mtvs_3rd_dreamplanet.test;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/dev")
public class TestController {

    @GetMapping("/sending-test")
    public Test sendingTest() {
        Test test = new Test();
        test.setContent("new content");

        return test;
    }

    @PostMapping("/receiving-test")
    public void receivingTest(@RequestBody Test test) {

        Test receivedTest = test;

        System.out.println(receivedTest.toString());
    }
}
