# langchain4j-init

> Production-ready LangChain4j showcase — AI Services, RAG, Tool Calling & Chat Memory

## Prerequisites

| Tool           | Version  |
|---------------|----------|
| Java (JDK)    | 21+      |
| Maven         | 3.9+     |
| Docker Desktop| 4.x (8 GB+ RAM recommended) |

## Quick Start

```bash
# 1. Start local infrastructure (Ollama + Milvus)
docker compose up -d

# 2. Pull a model into Ollama
docker exec ollama ollama pull mistral

# 3. Build the project
mvn clean package -DskipTests

# 4. Run all demos
java -jar target/langchain4j-init-1.0.0-SNAPSHOT.jar

# Or run a single demo: extract | tools | memory | ingest
java -jar target/langchain4j-init-1.0.0-SNAPSHOT.jar tools
```

## Project Structure

```
.
├── docker-compose.yml              # Ollama + Milvus local stack
├── pom.xml                         # Java 21 / LangChain4j dependencies
├── src
│   ├── main
│   │   ├── java/com/showcase
│   │   │   ├── ai/                 # AiService interfaces & Tool definitions
│   │   │   ├── rag/                # Embedding store & ingestion pipeline
│   │   │   ├── features/           # Demos: structured output, tool calling, memory
│   │   │   └── Main.java           # CLI entry point
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── logback.xml
│   │       └── documents/          # Sample docs for RAG ingestion
│   └── test/java/com/showcase      # Unit + integration tests
└── docs
    ├── architecture.mdx            # Mermaid diagrams: AI Service flow
    └── rag-pipeline.mdx            # Mermaid diagrams: RAG ingestion flow
```

## Key Features

| Feature                | Description |
|------------------------|-------------|
| **@AiService**         | Declarative interfaces that abstract prompt engineering and response parsing |
| **Structured Output**  | Unstructured text → strongly-typed Java records (POJOs) |
| **Tool Calling**       | LLM invokes local Java methods for "real-time" data |
| **Chat Memory**        | Multi-turn persistence via `MessageWindowChatMemory` |
| **RAG Ingestion**      | Load → chunk → embed → store pipeline for local documents |
| **Testcontainers**     | Integration tests with Ollama in Docker — no external services needed |

## Running Tests

```bash
# Unit tests only (no Docker required)
mvn test -DexcludedGroups=integration

# All tests including Testcontainers (requires Docker)
mvn test
```

## Documentation

- [Architecture — AI Service Orchestration](docs/architecture.mdx)
- [RAG Pipeline — Document Ingestion](docs/rag-pipeline.mdx)