# 🛡️ Root Cause Analyzer AI Agent

An enterprise-grade, high-performance SRE incident triage platform engineered entirely within the native **Java/Spring ecosystem**. This solution utilizes a stateful **ReAct (Reasoning + Acting) loop** via **Spring AI** and a local, air-gapped **ONNX Transformers embedding model** to analyze raw system telemetry logs, metric streams, and runbooks completely offline inside the JVM heap.

---

## 🚀 The Paradigm Shift: Why Spring AI vs. Python (LangGraph/LangChain)?

Modern GenAI architectures often introduce an anti-pattern: split-stack development where the business engine lives in Java, but the AI orchestration layer is siloed in a separate Python microservice running LangChain or LangGraph. This introduces significant production overhead:
* Cross-language serialization friction (JSON/gRPC over internal HTTP boundaries).
* Double microservice deployment footprints and uncoordinated scaling dynamics.
* Loose runtime evaluation of dynamic parameters.

### The Spring AI Competitive Advantage:
This project addresses the **Hackathon Bonus Criteria** by keeping the entire intelligence and retrieval pipeline native to **Spring Boot**. 
1. **Compile-Time Type Safety:** Agent tools use strict Java Records and structured Functional interfaces rather than loose Python dictionaries.
2. **Air-Gapped Data Privacy:** Telemetry streams and confidential infrastructure logs are converted into embeddings locally inside memory, avoiding cloud extraction risks and API cost footprints.
3. **Unified Enterprise Visibility:** Incident ingestion, RAG search, and agent tool execution map directly under standard Spring logging frameworks.

---

## 🛠️ Core Technology Stack
* **Core Framework:** Java 21, Spring Boot 3.2.5
* **AI Orchestration Framework:** Spring AI (1.0.0-M1 Core Engine)
* **LLM Engine:** Groq Cloud Inference (`llama-3.3-70b-versatile`)
* **Local RAG Embedding Engine:** ONNX Transformers Engine (`spring-ai-transformers`)
* **Open Source Model Weights:** Hugging Face `all-MiniLM-L6-v2` (Embedded natively into the JVM)
* **Vector Store Database:** In-Memory `SimpleVectorStore`
* **User Interface:** Vanilla JavaScript, Tailwind CSS, Marked.js (VS Code Theme Editor IDE)

---

## 📊 Requirement Alignment Matrix

| Hackathon Objective | How We Implemented It (Java Stack) | Architectural Impact |
| :--- | :--- | :--- |
| **Ingest & Correlate** | `DataIngestionController` REST endpoints | Accepts batch multi-line system logs, alerts, and operational data snapshots natively. |
| **Vector Store Base** | Polymorphic `SimpleVectorStore` | Completely managed via standard Spring context dependency injection container beans. |
| **RAG Retrieval** | `LogAnalyzerTool` + Local Transformers | Translates text tokens into multi-dimensional vectors using local CPU execution loops. |
| **Tool-Use / Function Calls** | Functional `@Bean` declarations decorated with `@Description` | Spring AI translates method parameters into clean JSON Schemas for the LLM to call autonomously. |
| **Agentic Analysis** | Native ReAct Execution within `ChatClient` | The model pauses when identifying a resource, invokes local tools, and merges data dynamically. |
| **Structured Output Parsing**| `BeanOutputConverter` + Java 16 Records | Enforces clean JSON data boundaries back to the web controllers for strict API compliance. |

---

## ⚡ Unified Configuration Layout (`application.yml`)
The framework orchestrates the Groq inference endpoint under the `openai` wrapper configurations while explicitly severing the standard cloud embedding dependencies to maintain complete local security compliance:

```yaml
spring:
  application:
    name: root-cause-analyzer
  ai:
    openai:
      api-key: ${GROQ_API_KEY}
      base-url: [https://api.groq.com/openai](https://api.groq.com/openai)
      chat:
        options:
          model: llama-3.3-70b-versatile
          temperature: 0.1
      embedding:
        enabled: false # Explicitly disables broken cloud ada-002 models