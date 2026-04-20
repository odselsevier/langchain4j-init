package com.showcase;

import com.showcase.platform.rag.DocumentLoaderUtil;
import dev.langchain4j.data.document.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link DocumentLoaderUtil} — ensures local text files
 * are loaded correctly as LangChain4j Documents.
 */
class DocumentLoaderUtilTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldLoadTextFilesFromDirectory() throws IOException {
        Files.writeString(tempDir.resolve("a.txt"), "Hello world");
        Files.writeString(tempDir.resolve("b.txt"), "Goodbye world");
        // non-.txt file should be ignored
        Files.writeString(tempDir.resolve("c.md"), "# Heading");

        List<Document> docs = DocumentLoaderUtil.loadTextFiles(tempDir);

        assertEquals(2, docs.size());
    }

    @Test
    void shouldReturnEmptyListForEmptyDirectory() throws IOException {
        List<Document> docs = DocumentLoaderUtil.loadTextFiles(tempDir);
        assertTrue(docs.isEmpty());
    }
}
