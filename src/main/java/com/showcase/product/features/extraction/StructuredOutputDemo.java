package com.showcase.product.features.extraction;

import com.showcase.product.support.SupportExtractorService;
import com.showcase.product.support.TicketDetails;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrates structured output extraction: the LLM parses an unstructured
 * customer message into a strongly-typed {@link TicketDetails} POJO.
 */
public class StructuredOutputDemo {

    private static final Logger log = LoggerFactory.getLogger(StructuredOutputDemo.class);

    /**
     * Runs the extraction scenario.
     */
    public static void run(ChatLanguageModel model) {
        log.info("=== Structured Output Demo ===");

        SupportExtractorService extractor = AiServices.create(
                SupportExtractorService.class, model);

        String rawMessage = """
                Hi, this is John Doe. I've been trying to reset my password
                for the last two hours and nothing works. The "Forgot password"
                link just gives me a 500 error. This is really urgent because
                I need to access my account for a presentation tomorrow morning.
                """;

        TicketDetails ticket = extractor.extract(rawMessage);
        log.info("Extracted ticket:\n{}", ticket);
    }
}
