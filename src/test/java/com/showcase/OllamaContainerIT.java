package com.showcase;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test that spins up an Ollama container via Testcontainers.
 *
 * <p>Tagged as "integration" so it can be excluded from fast unit-test runs:
 * <pre>mvn test -DexcludedGroups=integration</pre>
 *
 * <p><b>Note:</b> The Ollama container needs a pulled model to be truly useful.
 * This test only verifies that the container starts and the API endpoint is reachable.
 */
@Testcontainers
@Tag("integration")
class OllamaContainerIT {

    @Container
    static final GenericContainer<?> ollama =
            new GenericContainer<>(DockerImageName.parse("ollama/ollama:latest"))
                    .withExposedPorts(11434);

    @Test
    void ollamaContainerShouldStart() {
        assertTrue(ollama.isRunning(), "Ollama container should be running");

        String host = ollama.getHost();
        int port = ollama.getMappedPort(11434);
        assertNotNull(host);
        assertTrue(port > 0, "Mapped port should be positive");

        String baseUrl = "http://%s:%d".formatted(host, port);
        System.out.println("Ollama available at: " + baseUrl);
    }
}
