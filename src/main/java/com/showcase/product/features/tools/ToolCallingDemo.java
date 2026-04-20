package com.showcase.product.features.tools;

import com.showcase.product.assistant.AssistantWithTools;
import com.showcase.product.faq.WebsiteFaqTools;
import com.showcase.product.inventory.InventoryTools;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Website FAQ demo with tool-calling and retrieval-style FAQ lookup.
 */
public class ToolCallingDemo {

    private static final Logger log = LoggerFactory.getLogger(ToolCallingDemo.class);

    public static void run(ChatLanguageModel model, String userQuestion) {
        log.info("=== Website FAQ Demo (RAG-style) ===");

        AssistantWithTools assistant = AiServices.builder(AssistantWithTools.class)
                .chatLanguageModel(model)
                .tools(new WebsiteFaqTools(), new InventoryTools())
                .build();

        String question = (userQuestion == null || userQuestion.isBlank())
                ? "What upcoming events are on the website, and how many products are in stock for WIDGET-99?"
                : userQuestion;

        log.info("User → {}", question);
        String response = assistant.chat(question);
        log.info("Assistant → {}", response);
    }
}
