package com.mphasis.analyzer.root_cause_analyzer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;

@Service
public class IncidentAnalyzerService {

    private static final Logger log = LoggerFactory.getLogger(IncidentAnalyzerService.class);
    private final ChatClient chatClient;

    public IncidentAnalyzerService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
            .defaultSystem("You are an elite Site Reliability Engineer (SRE) Agent. " +
                           "Your job is to analyze production incidents. " +
                           "You MUST use the provided tools to gather logs, check metrics, and retrieve runbooks before drawing a conclusion. " +
                           "Do not guess. Reason step-by-step through the data you retrieve. " +
                           "Provide a final summary detailing the Root Cause and Recommended Actions.")
            .defaultFunctions("searchLogs", "checkMetrics", "getRunbook") 
            .build();
    }

   public IncidentSummary analyzeIncident(String incidentDescription) {
    log.info("Starting Agentic Analysis...");
    
    var converter = new BeanOutputConverter<>(IncidentSummary.class);
    
    // Append the JSON formatting instructions to the user prompt
    String promptWithFormat = incidentDescription + "\n\n" + converter.getFormat();

    String rawContent = chatClient.prompt()
            .user(promptWithFormat)
            .call()
            .content();
            
    return converter.convert(rawContent);
}
}