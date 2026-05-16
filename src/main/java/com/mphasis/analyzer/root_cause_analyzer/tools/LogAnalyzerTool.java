package com.mphasis.analyzer.root_cause_analyzer.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class LogAnalyzerTool {
    
    private static final Logger log = LoggerFactory.getLogger(LogAnalyzerTool.class);
    private final VectorStore vectorStore;

    // Inject the Vector Store!
    public LogAnalyzerTool(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public record LogRequest(String serviceName, String query) {}
    public record LogResponse(String relevantLogs) {}

    @Bean
    @Description("Use this tool to search the database for relevant logs using semantic similarity.")
    public Function<LogRequest, LogResponse> searchLogs() {
        return request -> {
            log.info("Agent executing RAG search for logs related to: {}", request.query());
            
            String results = vectorStore.similaritySearch(request.query()).stream()
                    .map(Document::getContent)
                    .collect(Collectors.joining("\n"));
                    
            if (results.isEmpty()) {
                 return new LogResponse("No relevant logs found in the system.");
            }
            return new LogResponse("Found the following system logs:\n" + results);
        };
    }
}