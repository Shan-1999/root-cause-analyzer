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

🔌 API Documentation & Specifications
1. Ingest Telemetry Context
Used to populate the in-memory vector store with contextual datasets.

Endpoint: POST /api/v1/ingestion/logs

Content-Type: application/json

Payload Array:

JSON
[
  "[ERROR] PaymentGateway Timeout at 10:42 AM - Connection Refused by upstream provider",
  "[WARN] High CPU load detected on database node 2 due to unoptimized query",
  "SOP-401: If PaymentGateway times out, scale up replicas immediately and check upstream status."
]
Response: Successfully ingested 3 documents into the Vector Store ready for RAG.

2. Trigger SRE Agent Analysis
Executes the stateless single-turn diagnosis wrapper.

Endpoint: POST /api/v1/incidents/analyze

Content-Type: text/plain

Payload Body: URGENT: Customers are reporting HTTP 500 errors during checkout. Database node 2 is running hot.

Response Output (JSON Contract):

JSON
{
  "rootCause": "PaymentGateway timeout due to connection refusal by upstream provider and unoptimized database queries",
  "recommendedActions": "Scale up read replicas, restart connection pooler, and optimize database queries"
}
💻 Local Quickstart Installation Guide
Prerequisites
Java 21 JDK installed

Maven 3.x wrapper script access

An active Groq API Key (gsk_...)

1. Clone & Export Credentials
Clone the project repository to your working directory and export your security credentials directly into your environmental path boundaries:

Bash
# On Windows PowerShell
$env:GROQ_API_KEY="your_actual_gsk_api_key_here"

# On Linux/macOS Bash
export GROQ_API_KEY="your_actual_gsk_api_key_here"
2. Build & Launch Application
Boot the embedded Tomcat server using the standard Maven boot runtime wrapper:

Bash
.\mvnw.cmd clean spring-boot:run
Note: On your very first startup, the application container will pause for approximately 15-30 seconds. This is expected behavior as the internal TransformersEmbeddingModel securely downloads the raw open-source all-MiniLM-L6-v2 tokenizer files into your local cache directory.

3. Access the VS Code UI Console
Open your preferred web browser and navigate directly to the frontend workspace index:

Plaintext
http://localhost:8080/index.html
🎯 Live Evaluation Demo Playbook
To demonstrate the full capability of the agent during evaluation, save this sample log text into a local file (e.g., incident_leak.log) and feed it directly into the dashboard file attachment interface:

Plaintext
[WARN] 2026-05-16 11:16:12 - [GC (Allocation Failure) Garbage Collection paused system threads for 4.82 seconds]
[METRIC] 2026-05-16 11:17:00 - JVM Memory Allocation - Heap Used: 3.98GB / Allocated: 4.00GB (99.5% Used)
[ERROR] 2026-05-16 11:17:04 - java.lang.OutOfMemoryError: Java heap space
	at com.mphasis.auth.TokenCache.leakMemorySimulation(TokenCache.java:142)
[FATAL] 2026-05-16 11:17:10 - org.apache.catalina.core.StandardServer : JVM crashed due to Memory Exception.
[INFO] RUNBOOK-SOP-911: For JVM OutOfMemory errors, capture the heap dump file (*.hprof) and scale container allocations via Kubernetes scripts.
Expected Output Behavior:
When you click Run Diagnostics, you can witness the internal engine lifecycle on your console logs:

The AI Agent intercepts the text stream.

It pauses execution to call checkMetrics and matches vector similarity parameters using the Local ONNX Store.

It resolves the exact missing hprof heap dump requirements and extracts recommendations from RUNBOOK-SOP-911.

It isolates the memory leak and streams a parsed JSON summary back to the UI screen with zero formatting brackets.
