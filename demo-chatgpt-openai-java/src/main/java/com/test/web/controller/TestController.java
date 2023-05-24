package com.test.web.controller;

import com.test.openai.factory.AiServiceFactory;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author WuHao
 * @since 2023/5/23 13:10
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/testChat")
    public String testChat() throws UnsupportedEncodingException {
        OpenAiService service = AiServiceFactory.createService();

        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), URLDecoder.decode("取一个3个字的中文名字，要求姓氏为吴", "UTF-8"));

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
                .blockingForEach(System.err::println);

        service.shutdownExecutor();
        return null;
    }

}
