package com.mphasis.analyzer.root_cause_analyzer.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class MetricCorrelatorTool {

    private static final Logger log = LoggerFactory.getLogger(MetricCorrelatorTool.class);

    public record MetricRequest(String resourceName, String metricType) {}
    public record MetricResponse(String currentStatus, String anomalyDetected) {}

    @Bean
    @Description("Use this tool to check infrastructure metrics like CPU, Memory, or Network latency for a specific resource.")
    public Function<MetricRequest, MetricResponse> checkMetrics() {
        return request -> {
            log.info("Agent invoked MetricCorrelatorTool for resource: {} metric: {}", request.resourceName(), request.metricType());
            if (request.metricType().toLowerCase().contains("cpu") || request.resourceName().toLowerCase().contains("db")) {
                 return new MetricResponse("CPU Load at 99%", "True: Spike correlates with the incident time.");
            }
            return new MetricResponse("Normal operating parameters", "False");
        };
    }
}