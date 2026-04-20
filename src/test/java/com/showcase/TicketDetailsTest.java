package com.showcase;

import com.showcase.ai.TicketDetails;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link TicketDetails} structured-output record.
 */
class TicketDetailsTest {

    @Test
    void recordFieldsShouldBeAccessible() {
        var ticket = new TicketDetails("Alice", "Billing", "high", "Overcharged on invoice");

        assertEquals("Alice", ticket.customerName());
        assertEquals("Billing", ticket.issueCategory());
        assertEquals("high", ticket.severity());
        assertEquals("Overcharged on invoice", ticket.summary());
    }

    @Test
    void toStringShouldContainAllFields() {
        var ticket = new TicketDetails("Bob", "Login", "critical", "Cannot reset password");

        String s = ticket.toString();
        assertTrue(s.contains("Bob"));
        assertTrue(s.contains("Login"));
        assertTrue(s.contains("critical"));
        assertTrue(s.contains("Cannot reset password"));
    }
}
