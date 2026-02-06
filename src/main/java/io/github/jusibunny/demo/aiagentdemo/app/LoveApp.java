package io.github.jusibunny.demo.aiagentdemo.app;


import io.github.jusibunny.demo.aiagentdemo.advisor.CustomLoggerAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@Component
public class LoveApp {

    private static final String SYSTEM_PROMPT = "扮演深耕恋爱心理领域的专家。开场向用户表明身份，告知用户可倾诉恋爱难题。" +
            "围绕单身、恋爱、已婚三种状态提问：单身状态询问社交圈拓展及追求心仪对象的困扰；" +
            "恋爱状态询问沟通、习惯差异引发的矛盾；已婚状态询问家庭责任与亲属关系处理的问题。" +
            "引导用户详述事情经过、对方反应及自身想法，以便给出专属解决方案。";

    private final ChatClient chatClient;

    public LoveApp(ChatModel dashscopeChatModel) {

        // 初始化基于文件的会话记忆
        // String fileDir = System.getProperty("user.dir") + "/tmp/chat-memory";
        // ChatMemory chatMemory = new FileBasedChatMemory(fileDir);

        // 初始化基于内存的会话记忆
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(20)
                .build();

        // ChatMemory chatMemory = new InMemoryChatMemory();

        this.chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        // new MessageChatMemoryAdvisor(chatMemory),
                        // new SimpleLoggerAdvisor(), // Spring AI 内置日志拦截器（DEBUG 级别）
                        new CustomLoggerAdvisor() // 自定义日志 Advisor，可按需开启
                        // new ReReadingAdvisor() // Re2 Advisor
                )
                .build();
    }

    /**
     * AI Chat 基础对话（支持多轮对话记忆）
     *
     * @param message        用户输入
     * @param conversationId 会话ID
     * @return 返回结果
     */
    public String doChat(String message, String conversationId) {
        ChatResponse chatResponse = this.chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec
                                .param(ChatMemory.CONVERSATION_ID, conversationId)
                        // .param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId)
                        // .param(AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10)
                )
                .call()
                .chatResponse();
        String text = chatResponse.getResult().getOutput().getText();
        log.info("text: {}", text);
        return text;
    }

    /**
     * AI 恋爱报告功能（实战结构化输出）
     *
     * @param message        用户输入
     * @param conversationId 会话 ID
     * @return 恋爱报告
     */
    public LoveReport doChatWithReport(String message, String conversationId) {
        LoveReport loveReport = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成恋爱结果，标题为{用户名}的恋爱报告，内容为建议列表")
                .user(message)
                .advisors(spec -> spec
                                .param(ChatMemory.CONVERSATION_ID, conversationId)
                        // .param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId)
                        // .param(AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10)
                )
                .call()
                .entity(LoveReport.class);
        log.info("loveReport: {}", loveReport);
        return loveReport;
    }

    record LoveReport(String title, List<String> suggestions) {
    }

    /**
     * AI 基础对话（支持多轮对话记忆，SSE 流式传输）
     *
     * @param message        用户消息
     * @param conversationId 会话 ID
     * @return 响应流
     */
    public Flux<String> doChatByStream(String message, String conversationId) {
        return chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec
                                .param(ChatMemory.CONVERSATION_ID, conversationId)
                        // .param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId)
                        // .param(AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10)
                )
                .stream()
                .content();
    }
}
