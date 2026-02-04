package io.github.jusibunny.demo.aiagentdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "测试接口")
@RestController
@RequestMapping("/health")
public class HealthController {

    @Operation(summary = "测试接口文档是否正常引入")
    @GetMapping
    public String healthCheck() {
        return "ok";
    }
}
