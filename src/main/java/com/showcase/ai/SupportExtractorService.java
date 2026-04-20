package com.showcase.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

/**
 * Declarative AI Service that extracts structured {@link TicketDetails}
 * from an unstructured customer support message.
 *
 * <p>LangChain4j generates the implementation at runtime — no manual
 * prompt engineering or JSON parsing required.
 */
public interface SupportExtractorService {

    @SystemMessage("""
            You are a support-ticket classifier. Extract the customer name,
            issue category, severity (low / medium / high / critical), and
            a one-sentence summary from the raw message.
            """)
    TicketDetails extract(@UserMessage String rawMessage);
}
