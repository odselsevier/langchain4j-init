package com.showcase;

import com.showcase.features.ToolCallingDemo;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * CLI entry point for the website FAQ demo.
 *
 * <p>Usage examples:
 * <ul>
 *   <li>{@code java -jar langchain4j-init.jar}</li>
 *   <li>{@code java -jar langchain4j-init.jar faq}</li>
 *   <li>{@code java -jar langchain4j-init.jar faq "What events are next?"}</li>
 *   <li>{@code java -jar langchain4j-init.jar "How many products are in stock for WIDGET-99?"}</li>
 * </ul>
 */
public class Main {

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

        String question = parseQuestion(args);
        ToolCallingDemo.run(chatModel, question);
    }

    private static String parseQuestion(String[] args) {
        if (args == null || args.length == 0) {
            return null;
        }

        if ("faq".equalsIgnoreCase(args[0]) || "tools".equalsIgnoreCase(args[0])) {
            if (args.length <= 1) {
                return null;
            }
            return String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
        }

        // If command is omitted, treat the full CLI input as the user's question.
        return String.join(" ", args);
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (IOException ignored) {
            // Defaults are used when the properties file cannot be loaded.
        }
        return props;
    }

    private static String envOrDefault(String key, String defaultValue) {
        String val = System.getenv(key);
        return (val != null && !val.isBlank()) ? val : defaultValue;
    }
}
