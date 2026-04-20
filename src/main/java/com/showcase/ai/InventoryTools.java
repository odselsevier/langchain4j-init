package com.showcase.ai;

import dev.langchain4j.agent.tool.Tool;

/**
 * Java methods exposed as LLM tools. LangChain4j will surface these
 * to the model, which can choose to invoke them during a conversation.
 */
public class InventoryTools {

    @Tool("Get current inventory count for a given SKU identifier")
    public String checkInventory(String sku) {
        // Mock implementation — replace with a real DB / API call
        return "SKU-%s: 42 units in stock".formatted(sku);
    }

    @Tool("Place a restock order for the given SKU and quantity")
    public String restockOrder(String sku, int quantity) {
        return "Restock order placed: %d units of SKU-%s".formatted(quantity, sku);
    }
}
