package io.github.jusibunny.demo.aiagentdemo.app;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class LoveAppTest {

    @Autowired
    private LoveApp loveApp;

    @Test
    void doChat() {

        log.info("开始测试");
        // 生成会话 ID
        String conversationId = UUID.randomUUID().toString();
        // 第一轮对话
        String message = "我单身，想找对象，但是不知道从何开始";
        String result = loveApp.doChat(message, conversationId);
        Assertions.assertNotNull(result);
        // 第二轮对话
        message = "我想让另一半（编程导航）更爱我";
        result = loveApp.doChat(message, conversationId);
        Assertions.assertNotNull(result);
        // 第三轮对话
        message = "我的另一半叫什么来着？刚跟你说过，帮我回忆一下";
        result = loveApp.doChat(message, conversationId);
        Assertions.assertNotNull(result);
    }
}