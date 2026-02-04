package io.github.jusibunny.demo.aiagentdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;

@Slf4j
@SpringBootApplication
public class AiAgentDemoApplication {

    public static void main(String[] args) {
        ConfigurableEnvironment env = SpringApplication.run(AiAgentDemoApplication.class, args).getEnvironment();
        log.info("Knife4j 接口文档地址: http://localhost:{}{}/doc.html", env.getProperty("server.port"), env.getProperty("server.servlet.context-path"));
    }
}
