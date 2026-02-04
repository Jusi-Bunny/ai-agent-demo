package io.github.jusibunny.demo.aiagentdemo.demo.invoke;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 使用 Spring AI 调用 Qwen3 模型
 */
@Component
public class SpringAiAiInvoke implements CommandLineRunner {

    @Autowired
    private ChatModel dashscopeChatModel;

    @Override
    public void run(String... args) throws Exception {
        AssistantMessage assistantMessage = dashscopeChatModel.call(new Prompt("你好"))
                .getResult()
                .getOutput();
        System.out.println(assistantMessage.getText());
    }
}
