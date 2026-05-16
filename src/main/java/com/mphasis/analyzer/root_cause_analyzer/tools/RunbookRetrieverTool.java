package com.mphasis.analyzer.root_cause_analyzer.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class RunbookRetrieverTool {

    private static final Logger log = LoggerFactory.getLogger(RunbookRetrieverTool.class);

    public record RunbookRequest(String errorSignature) {}
    public record RunbookResponse(String resolutionSteps) {}

    @Bean
    @Description("Use this tool to find the Standard Operating Procedure (Runbook) for a specific error signature or component failure.")
    public Function<RunbookRequest, RunbookResponse> getRunbook() {
        return request -> {
            log.info("Agent invoked RunbookRetrieverTool for signature: {}", request.errorSignature());
            return new RunbookResponse(
                "1. Isolate the affected node. 2. Scale up read replicas to handle load. 3. Restart the connection pooler."
            );
        };
    }
}