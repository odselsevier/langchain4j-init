package com.showcase;

import com.showcase.ai.InventoryTools;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link InventoryTools} — validates tool methods
 * independent of any LLM.
 */
class InventoryToolsTest {

    private final InventoryTools tools = new InventoryTools();

    @Test
    void checkInventoryShouldReturnFormattedStock() {
        String result = tools.checkInventory("WIDGET-99");
        assertTrue(result.contains("SKU-WIDGET-99"));
        assertTrue(result.contains("42"));
    }

    @Test
    void restockOrderShouldReturnConfirmation() {
        String result = tools.restockOrder("GADGET-7", 100);
        assertTrue(result.contains("100"));
        assertTrue(result.contains("SKU-GADGET-7"));
    }
}
