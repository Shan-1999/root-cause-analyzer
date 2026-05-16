package com.mphasis.analyzer.root_cause_analyzer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/ingestion")
public class DataIngestionController {

    private static final Logger log = LoggerFactory.getLogger(DataIngestionController.class);
    private final VectorStore vectorStore;

    public DataIngestionController(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostMapping("/logs")
    public String ingestSyntheticLogs(@RequestBody List<String> syntheticLogs) {
        log.info("Ingesting {} synthetic logs into the Vector Store...", syntheticLogs.size());
        
        List<Document> documents = syntheticLogs.stream()
                .map(logEntry -> new Document(logEntry, Map.of("source", "synthetic_dataset")))
                .collect(Collectors.toList());
                
        vectorStore.add(documents);
        return "Successfully ingested " + documents.size() + " documents into the Vector Store ready for RAG.";
    }
}