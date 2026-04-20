package com.showcase;

import com.showcase.features.ChatMemoryDemo;
import com.showcase.features.StructuredOutputDemo;
import com.showcase.features.ToolCallingDemo;
import com.showcase.rag.DocumentLoaderUtil;
import com.showcase.rag.IngestionPipeline;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

/**
 * CLI entry point that runs each demo in sequence.
 *
 * <p>Usage: {@code java -jar langchain4j-init.jar [demo]}
 * <ul>
 *   <li>{@code extract}  — Structured output extraction</li>
 *   <li>{@code tools}    — Tool calling</li>
 *   <li>{@code memory}   — Chat memory persistence</li>
 *   <li>{@code ingest}   — RAG document ingestion</li>
 *   <li>(no args)        — Run all demos</li>
 * </ul>
 */
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        Properties props = loadProperties();

        String ollamaUrl = envOrDefault("OLLAMA_BASE_URL",
                props.getProperty("ollama.base.url", "http://localhost:11434"));
        String modelName = envOrDefault("OLLAMA_MODEL",
                props.getProperty("ollama.model.name", "mistral"));

        ChatLanguageModel chatModel = OllamaChatModel.builder()
                .baseUrl(ollamaUrl)
                .modelName(modelName)
                .build();

        String demo = args.length > 0 ? args[0] : "all";

        switch (demo) {
            case "extract" -> StructuredOutputDemo.run(chatModel);
            case "tools"   -> ToolCallingDemo.run(chatModel);
            case "memory"  -> ChatMemoryDemo.run(chatModel);
            case "ingest"  -> runIngestion(ollamaUrl, modelName, props);
            default        -> runAll(chatModel, ollamaUrl, modelName, props);
        }
    }

    private static void runAll(ChatLanguageModel chatModel,
                               String ollamaUrl,
                               String modelName,
                               Properties props) throws Exception {
        StructuredOutputDemo.run(chatModel);
        ToolCallingDemo.run(chatModel);
        ChatMemoryDemo.run(chatModel);
        runIngestion(ollamaUrl, modelName, props);
    }

    private static void runIngestion(String ollamaUrl, String modelName,
                                     Properties props) throws Exception {
        log.info("=== RAG Ingestion Demo ===");

        EmbeddingModel embeddingModel = OllamaEmbeddingModel.builder()
                .baseUrl(ollamaUrl)
                .modelName(modelName)
                .build();

        EmbeddingStore<TextSegment> store = new InMemoryEmbeddingStore<>();

        int chunkSize = parseIntOrDefault(props.getProperty("rag.chunk.size"), 300);
        int chunkOverlap = parseIntOrDefault(props.getProperty("rag.chunk.overlap"), 30);
        String docsPath = props.getProperty("rag.documents.path", "src/main/resources/documents");

        IngestionPipeline pipeline = new IngestionPipeline(embeddingModel, store, chunkSize, chunkOverlap);

        Path docsDir = Path.of(docsPath);
        List<Document> docs = DocumentLoaderUtil.loadTextFiles(docsDir);
        pipeline.ingest(docs);

        log.info("Ingestion finished — embeddings stored in-memory.");
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream is = Main.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (IOException e) {
            log.warn("Could not load application.properties, using defaults", e);
        }
        return props;
    }

    private static int parseIntOrDefault(String value, int defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            log.warn("Invalid integer '{}', using default {}", value, defaultValue);
            return defaultValue;
        }
    }

    private static String envOrDefault(String key, String defaultValue) {
        String val = System.getenv(key);
        return (val != null && !val.isBlank()) ? val : defaultValue;
    }
}
