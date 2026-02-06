package io.github.jusibunny.demo.aiagentdemo.advisor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.chat.client.ChatClientMessageAggregator;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.*;
import reactor.core.publisher.Flux;

@Slf4j
public class CustomLoggerAdvisor implements CallAdvisor, StreamAdvisor {
// public class CustomLoggerAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private ChatClientRequest before(ChatClientRequest request) {
        log.info("AI Request: {}", request.prompt());
        return request;
    }

    private void observeAfter(ChatClientResponse chatClientResponse) {
        log.info("AI Response: {}", chatClientResponse.chatResponse().getResult().getOutput().getText());
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain chain) {
        chatClientRequest = before(chatClientRequest);
        ChatClientResponse chatClientResponse = chain.nextCall(chatClientRequest);
        observeAfter(chatClientResponse);
        return chatClientResponse;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain chain) {
        chatClientRequest = before(chatClientRequest);
        Flux<ChatClientResponse> chatClientResponseFlux = chain.nextStream(chatClientRequest);
        return (new ChatClientMessageAggregator()).aggregateChatClientResponse(chatClientResponseFlux, this::observeAfter);
    }

}

// @Slf4j
// public class CustomLoggerAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {
//
//     @Override
//     public String getName() {
//         return this.getClass().getSimpleName();
//     }
//
//     @Override
//     public int getOrder() {
//         return 0;
//     }
//
//     private AdvisedRequest before(AdvisedRequest request) {
//         log.info("AI Request: {}", request.userText());
//         return request;
//     }
//
//     private void observeAfter(AdvisedResponse advisedResponse) {
//         log.info("AI Response: {}", advisedResponse.response().getResult().getOutput().getText());
//     }
//
//     @Override
//     public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
//
//         advisedRequest = before(advisedRequest);
//
//         AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest);
//
//         observeAfter(advisedResponse);
//
//         return advisedResponse;
//     }
//
//     @Override
//     public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
//
//         advisedRequest = before(advisedRequest);
//
//         Flux<AdvisedResponse> advisedResponses = chain.nextAroundStream(advisedRequest);
//
//         return new MessageAggregator().aggregateAdvisedResponse(advisedResponses, this::observeAfter);
//     }
//
// }

