package io.github.jusibunny.demo.aiagentdemo.demo.invoke;

import java.util.ResourceBundle;

public class PropertiesReader {

    public static String getApiKey() {
        ResourceBundle bundle = ResourceBundle.getBundle("application");
        // 通过 key 获取 value
        return bundle.getString("api.key");
    }
}
