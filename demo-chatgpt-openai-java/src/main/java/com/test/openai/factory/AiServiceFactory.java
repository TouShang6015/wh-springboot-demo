package com.test.openai.factory;

import cn.hutool.core.lang.Assert;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.config.properties.OpenAiModel;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.service.OpenAiService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.util.Optional;

/**
 * openAiService 工厂
 * <p>* 如无需代理配置请使用 {@link OpenAiService}</p>
 * @author WuHao
 * @since 2023/5/24 10:00
 */
public class AiServiceFactory {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10L);

    public static OpenAiService createService() {
        String token = Optional.ofNullable(OpenAiModel.getKeys()).orElseThrow(() -> new RuntimeException("ApiKey不能为空，请检查参数配置")).stream().findFirst().orElse(null);

        Assert.notEmpty(token,() -> new RuntimeException("ApiKey不能为空，请检查参数配置"));

        ObjectMapper mapper = OpenAiService.defaultObjectMapper();
        // 设置代理
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(OpenAiModel.getProxyHost(), OpenAiModel.getProxyPort()));
        OkHttpClient client = OpenAiService.defaultClient(token, DEFAULT_TIMEOUT).newBuilder()
                .proxy(proxy)
                .build();
        Retrofit retrofit = OpenAiService.defaultRetrofit(client, mapper);

        return new OpenAiService(retrofit.create(OpenAiApi.class), client.dispatcher().executorService());

    }

}
