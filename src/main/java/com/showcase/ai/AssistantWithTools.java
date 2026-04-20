package com.showcase.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

/**
 * AI Service for a website FAQ assistant.
 */
public interface AssistantWithTools {

    @SystemMessage("""
            You are a concise FAQ assistant for a developer tools website.
            Use tools to fetch website FAQ context and inventory numbers before answering.
            Always answer in plain natural language.
            If inventory is requested, include both the SKU and product count.
            """)
    String chat(@UserMessage String userMessage);
}
