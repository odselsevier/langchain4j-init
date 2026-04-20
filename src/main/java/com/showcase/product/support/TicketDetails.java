package com.showcase.product.support;

/**
 * Strongly-typed POJO for structured output extraction.
 * LangChain4j automatically maps unstructured LLM responses into this record.
 */
public record TicketDetails(
        String customerName,
        String issueCategory,
        String severity,
        String summary
) {
    @Override
    public String toString() {
        return """
                TicketDetails {
                  customerName  = %s
                  issueCategory = %s
                  severity      = %s
                  summary       = %s
                }""".formatted(customerName, issueCategory, severity, summary);
    }
}
