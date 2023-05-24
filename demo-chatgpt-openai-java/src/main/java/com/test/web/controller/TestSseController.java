package com.test.web.controller;

import cn.hutool.core.util.StrUtil;
import com.test.openai.service.ChatCompletionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author WuHao
 * @since 2023/5/22 9:10
 */
@RestController
@RequestMapping("/sse")
public class TestSseController {

    private static final Map<String, SseEmitter> CACHE = new ConcurrentHashMap<>();

    @Resource
    private ChatCompletionService chatCompletionService;

    @GetMapping(path = "/stream/{clientId}")
    public SseEmitter stream(@PathVariable String clientId) {
        if (StrUtil.isEmpty(clientId))
            return null;
        SseEmitter sseEmitter = new SseEmitter(3600_000L);
        CACHE.put(clientId, sseEmitter);
        sseEmitter.onCompletion(() -> {
            System.out.println("sse已完成，关闭连接");
            CACHE.remove(clientId);
        });
        return sseEmitter;
    }

    @GetMapping(path = "/send")
    public String send(String clientId, String content) throws IOException {
        SseEmitter sseEmitter = CACHE.get(clientId);
        if (Objects.isNull(sseEmitter))
            return null;
        try {
            chatCompletionService.send(content, sseEmitter);
        } catch (Exception e) {
            sseEmitter.complete();
            CACHE.remove(clientId);
            e.printStackTrace();
        }
        return content;
    }

    @GetMapping(path = "/close")
    public String close(String clientId) {
        if (StrUtil.isEmpty(clientId))
            return null;
        SseEmitter sseEmitter = CACHE.get(clientId);
        if (Objects.isNull(sseEmitter))
            return null;
        sseEmitter.complete();
        CACHE.remove(clientId);
        return "close";
    }

}
