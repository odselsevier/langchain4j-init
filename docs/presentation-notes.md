# langchain4j-init — 5-Minute Presentation Script

> **Target audience:** Java developers and tech leads evaluating LLM integration.
> **Duration:** ~5 minutes (10 slides)

---

## Slide 1 — Title (15 s)

"Welcome. Today I'll walk you through **langchain4j-init** — 
a repo that brings large-language-model capabilities into a single Java codebase."

---

## Slide 2 — The Problem (30 s)

"Most LLM demos are Python notebooks. Enterprise teams need something that
fits their existing Java stack — strong types, testability, and CI/CD.
That's the gap this project fills."

---

## Slide 3 — Architecture at a Glance (30 s)

"The architecture has three layers. A CLI entry point delegates to feature
demos. Those demos talk to **AiService** interfaces — which are dynamic
proxies backed by Ollama or OpenAI. A Milvus vector store handles RAG."

---

## Slide 4 — Design Patterns in Java (45 s)

"Let me highlight four patterns we use:

1. **Dynamic Proxy** — `AiServices.create()` generates the implementation of
   `SupportExtractorService` and `AssistantWithTools` at runtime. You declare
   the interface; the framework builds the proxy.
2. **Builder** — Every model, memory, and ingestor is assembled via fluent
   builders (`OllamaChatModel.builder()`, `AiServices.builder()`).
3. **Strategy** — The ingestion pipeline accepts any `EmbeddingModel` and any
   `EmbeddingStore` implementation, so you can swap Milvus for an in-memory
   store in tests.
4. **Record / Value Object** — `TicketDetails` is a Java record that acts as a
   strongly-typed DTO for structured LLM output."

---

## Slide 5 — Code Spotlight: Structured Output (30 s)

"Here's the magic: you define an interface with `@SystemMessage`, return a
Java record, and LangChain4j handles prompt engineering, JSON parsing, and
deserialization. Zero boilerplate."

```java
public interface SupportExtractorService {
    @SystemMessage("You are a support-ticket classifier...")
    TicketDetails extract(@UserMessage String rawMessage);
}
```

---

## Slide 6 — Code Spotlight: Tool Calling (30 s)

"Tool calling lets the LLM invoke **your** Java methods. Annotate a method
with `@Tool`, wire it into the AiService builder, and the model decides
when to call it — like looking up inventory in real time."

```java
@Tool("Get current inventory count for a given SKU")
public String checkInventory(String sku) { ... }
```

---

## Slide 7 — RAG Ingestion Pipeline (30 s)

"For retrieval-augmented generation we use a four-stage pipeline:
Load → Split → Embed → Store. The `IngestionPipeline` class composes
a `DocumentSplitter`, an `EmbeddingModel`, and an `EmbeddingStoreIngestor`.
Strategy pattern again — swap any piece independently."

---

## Slide 8 — Interactive MDX Documentation (30 s)

"Documentation isn't an afterthought. We use **MDX** files with embedded
Mermaid diagrams — sequence diagrams, flowcharts, and state machines — that
render live in GitHub and in any MDX-compatible site (Docusaurus, Nextra).
The docs evolve alongside the code in the same PR."

---

## Slide 9 — Java ↔ MDX Synergy (30 s)

"Here's the synergy: every Java interface and pipeline class has a matching
Mermaid diagram in MDX. Change the code, update the diagram in the same commit.
Developers get executable code **and** visual documentation — no context
switching, no stale wikis."

---

## Slide 10 — Developer Experience & Testing (30 s)

"We prioritise DX:
- `docker compose up` gives you Ollama + Milvus in seconds.
- Unit tests run with a fake embedding model — no Docker needed.
- Integration tests use **Testcontainers** to spin up Ollama automatically.
- A single CLI entry point lets you run any demo: `extract`, `tools`,
  `memory`, `ingest`, or `all`."

---

## Closing (15 s)

"To summarise: clean Java patterns, interactive MDX docs, and a frictionless
developer experience. The repo is ready to fork — thank you!"
