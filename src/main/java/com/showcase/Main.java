package com.showcase;

import com.showcase.product.features.chat.ChatMemoryDemo;
import com.showcase.product.features.extraction.StructuredOutputDemo;
import com.showcase.product.features.fit.UseCaseFitDemo;
import com.showcase.product.features.tools.ToolCallingDemo;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.util.Locale;
import java.util.Properties;

/**
 * CLI entry point for product-layer demos.
 *
 * <p>Usage examples:
 * <ul>
 *   <li>{@code java -jar langchain4j-init.jar}</li>
 *   <li>{@code java -jar langchain4j-init.jar tools}</li>
 *   <li>{@code java -jar langchain4j-init.jar tools "What events are next?"}</li>
 *   <li>{@code java -jar langchain4j-init.jar fit}</li>
 *   <li>{@code java -jar langchain4j-init.jar extract}</li>
 *   <li>{@code java -jar langchain4j-init.jar memory}</li>
 *   <li>{@code java -jar langchain4j-init.jar "How many products are in stock for PRODUCT-99?"}</li>
 * </ul>
 *
 * <p>If no command is given, the CLI runs the {@code tools} demo. If the first
 * argument is not a known command, the full argument string is treated as a
 * free-form question for the {@code tools} demo.
 */
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        CliInput input = parseInput(args);

        switch (input.command()) {
            case FIT -> UseCaseFitDemo.run();
            case EXTRACT -> runModelBackedCommand(chatModel -> StructuredOutputDemo.run(chatModel));
            case MEMORY -> runModelBackedCommand(chatModel -> ChatMemoryDemo.run(chatModel));
            case TOOLS -> runModelBackedCommand(chatModel -> ToolCallingDemo.run(chatModel, input.question()));
        }
    }

    private static CliInput parseInput(String[] args) {
        if (args == null || args.length == 0) {
            return new CliInput(Command.TOOLS, null);
        }

        String firstArg = args[0].toLowerCase(Locale.ROOT);
        switch (firstArg) {
            case "faq", "tools" -> {
                String question = args.length <= 1
                        ? null
                        : String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
                return new CliInput(Command.TOOLS, question);
            }
            case "fit" -> {
                return new CliInput(Command.FIT, null);
            }
            case "extract" -> {
                return new CliInput(Command.EXTRACT, null);
            }
            case "memory" -> {
                return new CliInput(Command.MEMORY, null);
            }
            default -> {
                return new CliInput(Command.TOOLS, String.join(" ", args));
            }
        }
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

    private static ChatLanguageModel buildChatModel() {
        Properties props = loadProperties();
        String ollamaUrl = envOrDefault("OLLAMA_BASE_URL",
                props.getProperty("ollama.base.url", "http://localhost:11434"));
        String modelName = envOrDefault("OLLAMA_MODEL",
                props.getProperty("ollama.model.name", "mistral"));

        return OllamaChatModel.builder()
                .baseUrl(ollamaUrl)
                .modelName(modelName)
                .build();
    }

    private static void runModelBackedCommand(ModelBackedCommand command) throws Exception {
        ChatLanguageModel chatModel = buildChatModel();
        try {
            command.run(chatModel);
        } catch (RuntimeException e) {
            if (isModelNotFoundError(e)) {
                log.error("Ollama model not found. Configure OLLAMA_MODEL with an installed model or pull one first.");
                log.error("Example: docker exec ollama ollama pull mistral");
                return;
            }
            if (hasCause(e, ConnectException.class)) {
                log.error("Cannot connect to Ollama. Check OLLAMA_BASE_URL and ensure Ollama is running.");
                log.error("Default expected URL is http://localhost:11434");
                return;
            }
            throw e;
        }
    }

    private static boolean isModelNotFoundError(Throwable throwable) {
        String message = throwable == null ? null : throwable.getMessage();
        return message != null && message.toLowerCase(Locale.ROOT).contains("model") && message.toLowerCase(Locale.ROOT).contains("not found");
    }

    private static boolean hasCause(Throwable throwable, Class<? extends Throwable> type) {
        Throwable cursor = throwable;
        while (cursor != null) {
            if (type.isInstance(cursor)) {
                return true;
            }
            cursor = cursor.getCause();
        }
        return false;
    }

    private enum Command {
        TOOLS,
        FIT,
        EXTRACT,
        MEMORY
    }

    private record CliInput(Command command, String question) {
    }

    @FunctionalInterface
    private interface ModelBackedCommand {
        void run(ChatLanguageModel chatModel) throws Exception;
    }
}
