# langchain4j-init — 7.5-Minute Presentation Script

> **Target audience:** Java developers and tech leads evaluating LLM integration.
> **Duration:** ~7.5 minutes (15 slides)

---

## Slide 1 — Title (15 s)

"Welcome. Today I'll walk you through **langchain4j-init** —
Java with LangChain4j and RAG good and bad use cases overview."

---

## Slide 2 — RAG in 2026 Meme (30 s)

"Light smile moment:
people say 'RAG AI is too late in 2026'. That's only true for hype-only demos.
In practice, RAG still matters when paired with tool-calling, memory, and
evaluation discipline."

---

## Slide 3 — Architecture (30 s)

"The architecture has three layers. A CLI entry point delegates to feature
demos. Those demos talk to **AiService** interfaces — which are dynamic
proxies backed by Ollama or OpenAI. A Milvus vector store handles RAG."

---

## Slide 4 — What Is LangChain4j? (35 s)

"LangChain4j is a Java-first framework for building LLM apps with practical abstractions:
AiServices, tool-calling, memory, and RAG utilities. It lets Java teams move faster
without writing glue code for every model call."

---

## Slide 5 — LangChain4j vs Spring AI (40 s)

"Both are strong options.
LangChain4j is framework-agnostic and great when you want portability across Java stacks.
Spring AI is excellent when your team is fully invested in Spring Boot conventions.
For this repo, LangChain4j maps directly to the demo architecture."

---

## Slide 6 — Tech Stack & Tools (40 s)

"Before code patterns, here is the stack we are actually running:
Java 17 + Maven, LangChain4j, Ollama/OpenAI models, Milvus as vector DB,
MinIO as object storage, and etcd for metadata/coordination in the Milvus stack.
Docker Compose makes this reproducible on any developer machine."

---

## Slide 7 — Design Patterns in Java (40 s)

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

## Slide 8 — Code Spotlight: Structured Output (30 s)

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

## Slide 9 — Code Spotlight: Tool Calling (30 s)

"Tool calling lets the LLM invoke **your** Java methods. Annotate a method
with `@Tool`, wire it into the AiService builder, and the model decides
when to call it — like looking up inventory in real time."

```java
@Tool("Get current inventory count for a given SKU")
public String checkInventory(String sku) { ... }
```

---

## Slide 10 — RAG Ingestion Pipeline (30 s)

"For retrieval-augmented generation we use a four-stage pipeline:
Load → Split → Embed → Store. The `IngestionPipeline` class composes
a `DocumentSplitter`, an `EmbeddingModel`, and an `EmbeddingStoreIngestor`.
Strategy pattern again — swap any piece independently."

---

## Slide 11 — What Is RAG? (30 s)

"RAG means retrieval-augmented generation: the model looks up relevant
document chunks first, then writes an answer grounded in those chunks.
In plain terms: don't guess from memory, read first, then respond."

---

## Slide 12 — Use Cases & Benefits (40 s)

"What are we actually presenting from a benefits perspective?
- LangChain4j fit rubric (`fit`) -> faster architecture decisions, less over-engineering.
- Support ticket triage with structured extraction -> faster routing and better SLA performance.
- Inventory-aware assistant via tool-calling -> real-time answers and fewer escalations.
- Knowledge Q&A over internal docs via RAG -> more grounded answers, fewer hallucinations.
- Multi-turn support memory -> less repeated context from users.

This is the key message: faster operations, higher answer quality, lower support cost."

---

## Slide 13 — Interactive MDX Documentation (20 s)

"Documentation isn't an afterthought. We use MDX with embedded Mermaid
diagrams that render in GitHub and MDX sites. Docs evolve in the same PRs
as code, so architecture does not drift."

---

## Slide 14 — Developer Experience & Testing (30 s)

"We prioritise DX:
- `docker compose up` gives you Ollama + Milvus in seconds.
- Unit tests run with a fake embedding model — no Docker needed.
- Integration tests use **Testcontainers** to spin up Ollama automatically.
- A single CLI entry point lets you run any demo: `fit`, `extract`, `tools`,
  `memory`, `ingest`, or `all`."

---

## Closing (15 s)

"To summarise:
- quickest start path gives value in minutes (`tools` or `extract`)
- LangChain4j fits best when you need typed outputs, tool-calling, memory, or RAG
- use plain model calls for tiny one-off prompts

The repo is ready to fork — thank you!"
