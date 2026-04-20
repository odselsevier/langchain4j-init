package com.showcase;

import com.showcase.product.faq.WebsiteFaqTools;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class WebsiteFaqToolsTest {

    private final WebsiteFaqTools tools = new WebsiteFaqTools();

    @Test
    void eventQuestionsShouldReturnEventContext() {
        String result = tools.searchFaq("What events are coming up?");
        assertTrue(result.toLowerCase().contains("event"));
        assertTrue(result.toLowerCase().contains("upcoming"));
    }

    @Test
    void pricingQuestionsShouldReturnPricingContext() {
        String result = tools.searchFaq("What is your pricing plan?");
        assertTrue(result.toLowerCase().contains("pricing"));
        assertTrue(result.contains("USD"));
    }

    @Test
    void unknownQuestionsShouldReturnNoDirectMatchMessage() {
        String result = tools.searchFaq("What is your mascot animal?");
        assertTrue(result.contains("FAQ match (none)"));
        assertTrue(result.toLowerCase().contains("available topics"));
    }
}
