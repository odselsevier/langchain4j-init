package com.showcase.product.inventory;

import dev.langchain4j.agent.tool.Tool;

/**
 * Inventory methods exposed as LLM tools.
 */
public class InventoryTools {

    /**
     * Looks up current stock for a product SKU.
     *
     * @param sku product SKU identifier
     * @return stock summary in plain language
     */
    @Tool("Get current inventory count for a given SKU identifier")
    public String checkInventory(String sku) {
        // Mock implementation — replace with a real DB / API call.
        return "SKU-%s: 42 products in stock".formatted(sku);
    }

    /**
     * Places a restock order for a product SKU.
     *
     * @param sku product SKU identifier
     * @param quantity number of products to restock
     * @return restock confirmation summary
     */
    @Tool("Place a restock order for the given SKU and quantity")
    public String restockOrder(String sku, int quantity) {
        return "Restock order placed: %d products of SKU-%s".formatted(quantity, sku);
    }
}
