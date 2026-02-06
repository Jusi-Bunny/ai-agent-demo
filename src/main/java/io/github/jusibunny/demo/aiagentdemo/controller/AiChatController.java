package io.github.jusibunny.demo.aiagentdemo.controller;

import io.github.jusibunny.demo.aiagentdemo.app.LoveApp;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;

@RestController
@RequestMapping("/ai")
public class AiChatController {

    @Autowired
    private LoveApp loveApp;

    @Operation(summary = "1 - 通过 SSE 获取对话结果")
    @GetMapping(value = "/love_app/chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithLoveAppSSE(String message, String conversationId) {
        return loveApp.doChatByStream(message, conversationId);
    }

    /**
     * SSE 流式调用 AI 恋爱大师应用
     *
     * @param message
     * @param conversationId
     * @return
     */
    @Operation(summary = "2 - 通过 SSE 获取对话结果")
    @GetMapping(value = "/love_app/chat/sse_emitter")
    public SseEmitter doChatWithLoveAppServerSseEmitter(String message, String conversationId) {
        // 创建一个超时时间较长的 SseEmitter
        SseEmitter sseEmitter = new SseEmitter(180000L); // 3 分钟超时
        // 获取 Flux 响应式数据流并且直接通过订阅推送给 SseEmitter
        loveApp.doChatByStream(message, conversationId)
                .subscribe(chunk -> {
                    try {
                        sseEmitter.send(
                                SseEmitter.event()
                                        .id(conversationId)
                                        .name("message")
                                        .data(chunk)
                        );
                    } catch (IOException e) {
                        sseEmitter.completeWithError(e);
                    }
                }, sseEmitter::completeWithError, sseEmitter::complete);
        // 返回
        return sseEmitter;
    }

}
