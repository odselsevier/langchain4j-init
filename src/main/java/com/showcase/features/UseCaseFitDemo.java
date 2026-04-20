package com.showcase.features;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Quick rubric for deciding whether LangChain4j is the right abstraction.
 *
 * <p>This keeps the "good and bad use cases overview" grounded in runnable code,
 * not only in README/slides.
 */
public class UseCaseFitDemo {

    private static final Logger log = LoggerFactory.getLogger(UseCaseFitDemo.class);

    public static void run() {
        log.info("=== LangChain4j Fit Demo (Good vs Bad Use Cases) ===");

        log.info("GOOD FIT:");
        log.info("  1) Typed extraction from messy text -> see `extract` demo");
        log.info("  2) Tool-calling into business systems -> see `tools` demo");
        log.info("  3) Multi-turn context handling -> see `memory` demo");
        log.info("  4) Retrieval pipelines for internal docs -> see `ingest` demo");

        log.info("NOT A GREAT FIT:");
        log.info("  1) One-off prompt script with no tools/memory/retrieval");
        log.info("  2) Ultra-low-latency path where framework abstraction adds overhead");
        log.info("  3) Non-Java runtime where native SDK is simpler");

        log.info("Rule of thumb: if you need orchestration (tools, memory, RAG, typed IO), use LangChain4j.");
    }
}
