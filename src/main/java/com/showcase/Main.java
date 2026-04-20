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

import java.nio.file.Path;
import java.util.List;

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
        String ollamaUrl = envOrDefault("OLLAMA_BASE_URL", "http://localhost:11434");
        String modelName = envOrDefault("OLLAMA_MODEL", "mistral");

        ChatLanguageModel chatModel = OllamaChatModel.builder()
                .baseUrl(ollamaUrl)
                .modelName(modelName)
                .build();

        String demo = args.length > 0 ? args[0] : "all";

        switch (demo) {
            case "extract" -> StructuredOutputDemo.run(chatModel);
            case "tools"   -> ToolCallingDemo.run(chatModel);
            case "memory"  -> ChatMemoryDemo.run(chatModel);
            case "ingest"  -> runIngestion(ollamaUrl, modelName);
            default        -> runAll(chatModel, ollamaUrl, modelName);
        }
    }

    private static void runAll(ChatLanguageModel chatModel,
                               String ollamaUrl,
                               String modelName) throws Exception {
        StructuredOutputDemo.run(chatModel);
        ToolCallingDemo.run(chatModel);
        ChatMemoryDemo.run(chatModel);
        runIngestion(ollamaUrl, modelName);
    }

    private static void runIngestion(String ollamaUrl, String modelName) throws Exception {
        log.info("=== RAG Ingestion Demo ===");

        EmbeddingModel embeddingModel = OllamaEmbeddingModel.builder()
                .baseUrl(ollamaUrl)
                .modelName(modelName)
                .build();

        EmbeddingStore<TextSegment> store = new InMemoryEmbeddingStore<>();

        IngestionPipeline pipeline = new IngestionPipeline(embeddingModel, store, 300, 30);

        Path docsDir = Path.of("src/main/resources/documents");
        List<Document> docs = DocumentLoaderUtil.loadTextFiles(docsDir);
        pipeline.ingest(docs);

        log.info("Ingestion finished — embeddings stored in-memory.");
    }

    private static String envOrDefault(String key, String defaultValue) {
        String val = System.getenv(key);
        return (val != null && !val.isBlank()) ? val : defaultValue;
    }
}
