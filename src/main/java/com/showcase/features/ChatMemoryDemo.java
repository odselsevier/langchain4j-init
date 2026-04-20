package com.showcase.features;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrates multi-turn conversation with persistent chat memory.
 *
 * <p>{@link MessageWindowChatMemory} keeps the last N messages so the model
 * retains context across turns without exceeding token limits.
 */
public class ChatMemoryDemo {

    private static final Logger log = LoggerFactory.getLogger(ChatMemoryDemo.class);

    /** Simple conversational interface. */
    public interface Conversationalist {
        String chat(String userMessage);
    }

    /**
     * Runs a short multi-turn dialogue to show memory in action.
     */
    public static void run(ChatLanguageModel model) {
        log.info("=== Chat Memory Demo ===");

        Conversationalist bot = AiServices.builder(Conversationalist.class)
                .chatLanguageModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();

        String r1 = bot.chat("My name is Alice.");
        log.info("Turn 1 → {}", r1);

        String r2 = bot.chat("What is my name?");
        log.info("Turn 2 → {}", r2);

        String r3 = bot.chat("Summarise our conversation so far.");
        log.info("Turn 3 → {}", r3);
    }
}
