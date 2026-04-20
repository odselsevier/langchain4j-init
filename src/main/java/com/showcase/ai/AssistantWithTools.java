package com.showcase.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

/**
 * AI Service that has access to {@link InventoryTools} via tool calling.
 * The LLM decides autonomously when to invoke the tool to fetch "real-time" data.
 */
public interface AssistantWithTools {

    @SystemMessage("""
            You are a helpful warehouse assistant. When the user asks about
            stock levels, use the available inventory tool to look up the
            current quantity. Always include the SKU in your response.
            """)
    String chat(@UserMessage String userMessage);
}
