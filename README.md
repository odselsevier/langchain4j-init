# langchain4j-init

> Java with LangChain4j and RAG good and bad use cases overview.

## Fastest Path To Value (2-3 minutes)

If you only want to evaluate "is this useful for my team?", run one demo first.

```bash
# 1) Start only Ollama (no Milvus needed for quick wins)
docker compose up -d ollama

# 2) Pull a local model
docker exec ollama ollama pull mistral

# 3) Build
mvn -q -DskipTests package

# 4) Run one high-signal demo
java -jar target/langchain4j-init-1.0.0-SNAPSHOT.jar tools
# alternatives: extract | memory
```

What you should see:
- `tools`: model can call Java methods (`@Tool`) to answer with backend data
- `extract`: unstructured support text becomes a typed Java record
- `memory`: multi-turn context retention

## Full Stack (includes RAG ingestion)

Use this when you also want vector-search infrastructure and ingestion flow.

```bash
# 1) Start all services
docker compose up -d

# 2) Pull model
docker exec ollama ollama pull mistral

# 3) Build + run all demos
mvn clean package -DskipTests
java -jar target/langchain4j-init-1.0.0-SNAPSHOT.jar
# or only RAG ingestion demo
java -jar target/langchain4j-init-1.0.0-SNAPSHOT.jar ingest
```

## Prerequisites

### Minimum (quick wins)

| Tool | Version |
|---|---|
| Java (JDK) | 17+ |
| Maven | 3.9+ |
| Docker Desktop | 4.x |

### Recommended (full stack comfort)

- 8 GB+ RAM when running Ollama + Milvus together

## What This Repo Demonstrates

| Developer Problem | Demo | Immediate Benefit |
|---|---|---|
| "I need typed output, not string parsing." | `extract` | Structured data extraction into Java records |
| "I need model + backend actions." | `tools` | Tool-calling over Java methods (`@Tool`) |
| "I need context over multiple turns." | `memory` | Conversation continuity via chat memory |
| "I need docs-grounded answers." | `ingest` | RAG ingestion pipeline (load -> split -> embed -> store) |

## Good Fit vs Bad Fit for LangChain4j

### Good fit

- You need typed AI integration in a Java/Spring codebase
- You need tool-calling into business systems (inventory, payments, CRM)
- You need RAG or memory abstractions without building plumbing from scratch
- You want testable components and swappable model/store implementations

### Usually not worth LangChain4j

- One-off prompt scripts or throwaway prototypes
- A single plain completion call with no tools, no memory, no retrieval
- Ultra-low-latency paths where framework overhead is unnecessary
- Teams not using Java as a primary runtime

## Important Demo Notes

- `InventoryTools` uses mocked data (`"42 units in stock"`) to demonstrate tool-calling mechanics.
- Current `ingest` demo stores embeddings in `InMemoryEmbeddingStore` for simplicity.
- Milvus is provided in `docker-compose.yml` as production-like infrastructure for extension.

## Project Structure

```text
.
├── docker-compose.yml              # Ollama + Milvus local stack
├── pom.xml                         # Java 17+ / LangChain4j dependencies
├── src
│   ├── main
│   │   ├── java/com/showcase
│   │   │   ├── ai/                 # AiService interfaces & tool definitions
│   │   │   ├── rag/                # Ingestion pipeline components
│   │   │   ├── features/           # Demos: extract, tools, memory
│   │   │   └── Main.java           # CLI entry point
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── logback.xml
│   │       └── documents/          # Sample docs for RAG ingestion
│   └── test/java/com/showcase      # Unit + integration tests
└── docs
    ├── architecture.mdx            # Mermaid diagrams: AI service flow
    ├── rag-pipeline.mdx            # Mermaid diagrams: RAG ingestion flow
    ├── presentation-notes.md       # 12-slide speaker notes
    └── slides.html                 # HTML slide deck
```

## Running Tests

```bash
# Unit tests only (no Docker required)
mvn test

# Integration tests only (requires Docker)
mvn verify -DskipUnitTests

# All tests
mvn verify
```

## Documentation

- [Architecture - AI Service Orchestration](docs/architecture.mdx)
- [RAG Pipeline - Document Ingestion](docs/rag-pipeline.mdx)
- [5-Minute Presentation - Speaker Notes](docs/presentation-notes.md)
- [Slide Deck (HTML)](docs/slides.html) - open in any browser, navigate with arrow keys
