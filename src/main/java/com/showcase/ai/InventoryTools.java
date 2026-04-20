package com.showcase.ai;

import dev.langchain4j.agent.tool.Tool;

/**
 * Inventory methods exposed as LLM tools.
 */
public class InventoryTools {

    @Tool("Get current inventory count for a given SKU identifier")
    public String checkInventory(String sku) {
        // Mock implementation — replace with a real DB / API call.
        return "SKU-%s: 42 products in stock".formatted(sku);
    }

    @Tool("Place a restock order for the given SKU and quantity")
    public String restockOrder(String sku, int quantity) {
        return "Restock order placed: %d products of SKU-%s".formatted(quantity, sku);
    }
}
