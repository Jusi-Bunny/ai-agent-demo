package io.github.jusibunny.demo.aiagentdemo.demo.invoke;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * 使用 HttpClient 调用 Qwen3 模型
 */
public class HttpAiInvoke {

    public static void main(String[] args) {
        // 从环境变量读取 API Key
        String apiKey = PropertiesReader.getApiKey();

        // 构造 messages
        JSONArray messages = new JSONArray();
        messages.add(new JSONObject()
                .set("role", "system")
                .set("content", "You are a helpful assistant."));
        messages.add(new JSONObject()
                .set("role", "user")
                .set("content", "你是谁？"));

        // 构造请求体
        JSONObject body = new JSONObject()
                .set("model", "qwen3-max")
                .set("input", new JSONObject()
                        .set("messages", messages))
                .set("parameters", new JSONObject()
                        .set("result_format", "message"));

        // 发送请求
        HttpResponse response = HttpRequest.post(
                        "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(body))
                .execute();

        // 输出响应
        System.out.println(response.body());
    }
}
