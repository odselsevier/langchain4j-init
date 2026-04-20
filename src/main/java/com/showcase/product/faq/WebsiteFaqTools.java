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
        FAQ.put("events", "Upcoming events: AI Search Webinar on 2026-05-12, RAG Office Hours on 2026-05-20, and Java + LLM Meetup on 2026-06-02.");
        FAQ.put("pricing", "Pricing FAQ: Starter is free, Pro is 29 USD/month, and Team is 99 USD/month with shared workspaces.");
        FAQ.put("support", "Support FAQ: Email support is available Monday-Friday, 09:00-17:00 UTC. Critical incidents have a 4-hour response target.");
        FAQ.put("deployment", "Deployment FAQ: We support Docker Compose for local demos and Kubernetes for production environments.");
    }

    @Tool("Search website FAQ entries and return the most relevant context")
    public String searchFaq(String userQuestion) {
        String q = userQuestion == null ? "" : userQuestion.toLowerCase(Locale.ROOT);

        int bestScore = -1;
        String bestKey = "events";
        for (String key : FAQ.keySet()) {
            int score = score(q, key);
            if (score > bestScore) {
                bestScore = score;
                bestKey = key;
            }
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
