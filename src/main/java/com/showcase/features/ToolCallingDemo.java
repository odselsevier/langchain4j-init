package com.showcase.features;

import com.showcase.ai.AssistantWithTools;
import com.showcase.ai.InventoryTools;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrates tool calling: the LLM dynamically invokes local Java methods
 * defined in {@link InventoryTools} to answer questions that need "real-time" data.
 */
public class ToolCallingDemo {

    private static final Logger log = LoggerFactory.getLogger(ToolCallingDemo.class);

    /**
     * Runs the tool-calling scenario.
     */
    public static void run(ChatLanguageModel model) {
        log.info("=== Tool Calling Demo ===");

        AssistantWithTools assistant = AiServices.builder(AssistantWithTools.class)
                .chatLanguageModel(model)
                .tools(new InventoryTools())
                .build();

        String response = assistant.chat("How many units of WIDGET-99 do we have in stock?");
        log.info("Assistant → {}", response);
    }
}
