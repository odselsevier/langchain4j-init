package com.showcase.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Orchestrates the document ingestion pipeline:
 * <ol>
 *   <li>Accept pre-loaded {@link Document}s</li>
 *   <li>Split them into chunks using a recursive character splitter</li>
 *   <li>Embed each chunk with the configured {@link EmbeddingModel}</li>
 *   <li>Persist embeddings into the {@link EmbeddingStore}</li>
 * </ol>
 */
public class IngestionPipeline {

    private static final Logger log = LoggerFactory.getLogger(IngestionPipeline.class);

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;
    private final int chunkSize;
    private final int chunkOverlap;

    public IngestionPipeline(EmbeddingModel embeddingModel,
                             EmbeddingStore<TextSegment> embeddingStore,
                             int chunkSize,
                             int chunkOverlap) {
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
        this.chunkSize = chunkSize;
        this.chunkOverlap = chunkOverlap;
    }

    /**
     * Runs the full ingestion pipeline for the supplied documents.
     *
     * @param documents list of documents to ingest
     */
    public void ingest(List<Document> documents) {
        log.info("Starting ingestion of {} document(s) — chunk size={}, overlap={}",
                documents.size(), chunkSize, chunkOverlap);

        DocumentSplitter splitter = DocumentSplitters.recursive(chunkSize, chunkOverlap);

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(splitter)
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        ingestor.ingest(documents);

        log.info("Ingestion complete for {} document(s)", documents.size());
    }
}
