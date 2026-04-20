package com.showcase;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.net.HttpURLConnection;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test that spins up an Ollama container via Testcontainers.
 *
 * <p>Tagged as "integration" so Surefire excludes it by default.
 * Run with {@code mvn verify} to include it via Failsafe.
 *
 * <p><b>Note:</b> The Ollama container needs a pulled model to be truly useful.
 * This test verifies that the container starts and the API endpoint is reachable.
 */
@Testcontainers
@Tag("integration")
class OllamaContainerIT {

    private static final String OLLAMA_IMAGE = "ollama/ollama:0.6.2";

    @Container
    static final GenericContainer<?> ollama =
            new GenericContainer<>(DockerImageName.parse(OLLAMA_IMAGE))
                    .withExposedPorts(11434)
                    .waitingFor(Wait.forHttp("/").forPort(11434).forStatusCode(200));

    @Test
    void ollamaContainerShouldStart() throws Exception {
        assertTrue(ollama.isRunning(), "Ollama container should be running");

        String host = ollama.getHost();
        int port = ollama.getMappedPort(11434);
        assertNotNull(host);
        assertTrue(port > 0, "Mapped port should be positive");

        String baseUrl = "http://%s:%d".formatted(host, port);

        // Verify the API is reachable with an actual HTTP call
        HttpURLConnection conn = (HttpURLConnection) URI.create(baseUrl).toURL().openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5_000);
        conn.setReadTimeout(5_000);
        int statusCode = conn.getResponseCode();
        conn.disconnect();

        assertEquals(200, statusCode, "Ollama root endpoint should return 200");
    }
}
