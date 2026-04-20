package com.showcase.product.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

/**
 * AI Service contract for FAQ + inventory product support conversations.
 *
 * <p>Behavior contract:
 * <ul>
 *   <li>Use available tools to gather grounded FAQ and inventory context.</li>
 *   <li>Return concise, plain-language responses.</li>
 *   <li>If inventory is asked, include both SKU and product count.</li>
 * </ul>
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
