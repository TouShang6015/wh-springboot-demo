package com.test.openai.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author WuHao
 * @since 2023/5/24 10:52
 */
public interface ChatCompletionService {

    boolean send(String content, SseEmitter sseEmitter);

}
