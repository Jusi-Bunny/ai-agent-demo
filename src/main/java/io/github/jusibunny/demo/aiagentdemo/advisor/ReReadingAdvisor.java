package io.github.jusibunny.demo.aiagentdemo.advisor;


import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.prompt.Prompt;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义 Re2 Advisor
 * 可提高大型语言模型的推理能力
 */
public class ReReadingAdvisor implements CallAdvisor, StreamAdvisor {

    /**
     * 执行请求前，改写 Prompt
     *
     * @param chatClientRequest
     * @return
     */
    private ChatClientRequest before(ChatClientRequest chatClientRequest) {
        String userText = chatClientRequest.prompt().getUserMessage().getText();
        // 添加上下文参数
        chatClientRequest.context().put("re2_input_query", userText);
        // 修改用户提示词
        String newUserText = """
                %s
                Read the question again: %s
                """.formatted(userText, userText);
        Prompt newPrompt = chatClientRequest.prompt().augmentUserMessage(newUserText);
        return new ChatClientRequest(newPrompt, chatClientRequest.context());
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain chain) {
        return chain.nextCall(this.before(chatClientRequest));
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain chain) {
        return chain.nextStream(this.before(chatClientRequest));
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}


// public class ReReadingAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {
//
//     /**
//      * 执行请求前，改写 Prompt
//      *
//      * @param advisedRequest 请求对象
//      * @return 修改后的请求对象
//      */
//     private AdvisedRequest before(AdvisedRequest advisedRequest) {
//
//         Map<String, Object> advisedUserParams = new HashMap<>(advisedRequest.userParams());
//         advisedUserParams.put("re2_input_query", advisedRequest.userText());
//
//         return AdvisedRequest.from(advisedRequest)
//                 .userText("""
//                         {re2_input_query}
//                         Read the question again: {re2_input_query}
//                         """)
//                 .userParams(advisedUserParams)
//                 .build();
//     }
//
//     @Override
//     public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
//         return chain.nextAroundCall(this.before(advisedRequest));
//     }
//
//     @Override
//     public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
//         return chain.nextAroundStream(this.before(advisedRequest));
//     }
//
//     @Override
//     public int getOrder() {
//         return 0;
//     }
//
//     @Override
//     public String getName() {
//         return this.getClass().getSimpleName();
//     }
// }
