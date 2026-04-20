package com.showcase;

import com.showcase.platform.rag.IngestionPipeline;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the {@link IngestionPipeline} with a deterministic fake embedding model
 * and an in-memory store — no LLM or Docker required.
 */
class IngestionPipelineTest {

    /**
     * A trivial embedding model that returns a fixed-dimension vector
     * for any text. Good enough to verify the pipeline wiring.
     */
    static class FakeEmbeddingModel implements EmbeddingModel {
        @Override
        public Response<Embedding> embed(String text) {
            float[] vector = new float[8];
            for (int i = 0; i < vector.length; i++) {
                vector[i] = (float) text.length() / (i + 1);
            }
            return Response.from(Embedding.from(vector));
        }

        @Override
        public Response<Embedding> embed(TextSegment segment) {
            return embed(segment.text());
        }

        @Override
        public Response<List<Embedding>> embedAll(List<TextSegment> segments) {
            List<Embedding> embeddings = segments.stream()
                    .map(s -> embed(s).content())
                    .toList();
            return Response.from(embeddings);
        }

        @Override
        public int dimension() {
            return 8;
        }
    }

    @Test
    void shouldIngestDocumentsIntoStore() {
        var store = new InMemoryEmbeddingStore<TextSegment>();
        var pipeline = new IngestionPipeline(new FakeEmbeddingModel(), store, 100, 10);

        Document doc = Document.from(
                "LangChain4j enables building AI applications in Java. " +
                "It supports tool calling, memory, and RAG out of the box.");

        pipeline.ingest(List.of(doc));

        // After ingestion, the in-memory store should contain at least one entry
        var result = store.search(
                dev.langchain4j.store.embedding.EmbeddingSearchRequest.builder()
                        .queryEmbedding(Embedding.from(new float[8]))
                        .maxResults(10)
                        .build());
        assertFalse(result.matches().isEmpty(),
                "Embedding store should have entries after ingestion");
    }
}
