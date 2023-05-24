package com.test.openai.service.impl;

import cn.hutool.core.text.StrPool;
import cn.hutool.json.JSONUtil;
import com.test.openai.factory.AiServiceFactory;
import com.test.openai.service.ChatCompletionService;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author WuHao
 * @since 2023/5/24 10:58
 */
@Slf4j
@Service
public class ChatCompletionServiceImpl implements ChatCompletionService {

    @SneakyThrows
    @Override
    public boolean send(String content, SseEmitter sseEmitter) {
        OpenAiService service = AiServiceFactory.createService();

        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), URLDecoder.decode(content, "UTF-8"));

        messages.add(systemMessage);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .n(1)
                .maxTokens(50)
                .logitBias(new HashMap<>())
                .build();

        service.streamChatCompletion(chatCompletionRequest)
                .doOnError(Throwable::printStackTrace)
                .blockingForEach(chunk -> {
                    log.info(JSONUtil.toJsonStr(chunk));
                    List<ChatCompletionChoice> choices = chunk.getChoices();
                    String sendContent = Optional.ofNullable(choices)
                            .orElseGet(ArrayList::new)
                            .stream()
                            .map(item -> Optional.ofNullable(
                                    Optional.ofNullable(item.getMessage()).orElseGet(ChatMessage::new).getContent()
                            ).orElse(StrPool.LF))
                            .collect(Collectors.joining());
                    sseEmitter.send(sendContent, MediaType.APPLICATION_JSON);
                });

        service.shutdownExecutor();
        return true;
    }

}
