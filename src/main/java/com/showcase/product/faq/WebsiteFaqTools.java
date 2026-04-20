package com.showcase.product.faq;

import dev.langchain4j.agent.tool.Tool;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Simple lexical retrieval over website FAQ content.
 */
public class WebsiteFaqTools {

    private static final Map<String, String> FAQ = new LinkedHashMap<>();

    static {
        FAQ.put("events", "Upcoming events include our AI Search Webinar, RAG Office Hours, and Java + LLM Meetup. Check the website events page for exact upcoming dates.");
        FAQ.put("pricing", "Pricing FAQ: Starter is free, Pro is 29 USD/month, and Team is 99 USD/month with shared workspaces.");
        FAQ.put("support", "Support FAQ: Email support is available Monday-Friday, 09:00-17:00 UTC. Critical incidents have a 4-hour response target.");
        FAQ.put("deployment", "Deployment FAQ: We support Docker Compose for local demos and Kubernetes for production environments.");
    }

    /**
     * Returns the most relevant FAQ snippet for the user's question.
     *
     * <p>If the query has no lexical overlap with known FAQ topics, this method
     * returns a fallback message instead of forcing an unrelated FAQ match.
     *
     * @param userQuestion end-user question text (nullable)
     * @return matched FAQ context or an explicit no-match message
     */
    @Tool("Search website FAQ entries and return the most relevant context")
    public String searchFaq(String userQuestion) {
        String q = userQuestion == null ? "" : userQuestion.toLowerCase(Locale.ROOT);

        int bestScore = -1;
        String bestKey = null;
        for (String key : FAQ.keySet()) {
            int score = score(q, key);
            if (score > bestScore) {
                bestScore = score;
                bestKey = key;
            }
        }

        if (bestScore <= 0 || bestKey == null) {
            return "FAQ match (none): No direct FAQ topic matched. Available topics: events, pricing, support, deployment.";
        }

        return "FAQ match (%s): %s".formatted(bestKey, FAQ.get(bestKey));
    }

    private int score(String q, String key) {
        int score = 0;
        if (q.contains(key)) {
            score += 10;
        }
        if (key.equals("events") && (q.contains("event") || q.contains("webinar") || q.contains("meetup"))) {
            score += 5;
        }
        if (key.equals("pricing") && (q.contains("price") || q.contains("plan") || q.contains("cost"))) {
            score += 5;
        }
        if (key.equals("support") && (q.contains("support") || q.contains("help") || q.contains("incident"))) {
            score += 5;
        }
        if (key.equals("deployment") && (q.contains("deploy") || q.contains("docker") || q.contains("kubernetes"))) {
            score += 5;
        }
        return score;
    }
}
