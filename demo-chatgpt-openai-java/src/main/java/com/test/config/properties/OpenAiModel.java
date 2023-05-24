package com.test.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * openai yml 配置信息
 * @author WuHao
 * @since 2023/5/23 13:09
 */
@Configuration
@ConfigurationProperties(prefix = "openai")
public class OpenAiModel {

    /**
     * 代理地址
     */
    private static String proxyHost;
    /**
     * 代理端口
     */
    private static Integer proxyPort;
    /**
     * openai apikey
     */
    private static List<String> keys;

    public static List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        OpenAiModel.keys = keys;
    }

    public static String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        OpenAiModel.proxyHost = proxyHost;
    }

    public static Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        OpenAiModel.proxyPort = proxyPort;
    }
}
