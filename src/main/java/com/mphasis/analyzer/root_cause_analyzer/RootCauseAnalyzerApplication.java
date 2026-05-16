package com.mphasis.analyzer.root_cause_analyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(
    exclude = { DataSourceAutoConfiguration.class }, 
    excludeName = { "org.springframework.ai.autoconfigure.vectorstore.pgvector.PgVectorStoreAutoConfiguration" }
)
public class RootCauseAnalyzerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RootCauseAnalyzerApplication.class, args);
    }
}