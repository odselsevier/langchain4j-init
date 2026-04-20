package com.showcase.rag;

import dev.langchain4j.data.document.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads local text files from a directory and converts them into
 * LangChain4j {@link Document} objects ready for ingestion.
 */
public class DocumentLoaderUtil {

    private static final Logger log = LoggerFactory.getLogger(DocumentLoaderUtil.class);

    private DocumentLoaderUtil() { }

    /**
     * Reads every {@code .txt} file under the given directory into Documents.
     *
     * @param directory path to the folder containing text files
     * @return list of documents
     */
    public static List<Document> loadTextFiles(Path directory) throws IOException {
        List<Document> docs = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*.txt")) {
            for (Path file : stream) {
                String content = Files.readString(file);
                Document doc = Document.from(content);
                docs.add(doc);
                log.debug("Loaded document: {} ({} chars)", file.getFileName(), content.length());
            }
        }
        log.info("Loaded {} document(s) from {}", docs.size(), directory);
        return docs;
    }
}
