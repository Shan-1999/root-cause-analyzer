package com.mphasis.analyzer.root_cause_analyzer.controller;

import com.mphasis.analyzer.root_cause_analyzer.service.IncidentAnalyzerService;
import com.mphasis.analyzer.root_cause_analyzer.service.IncidentSummary; 
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/incidents")
public class IncidentController {

    private final IncidentAnalyzerService analyzerService;

    public IncidentController(IncidentAnalyzerService analyzerService) {
        this.analyzerService = analyzerService;
    }

    @PostMapping("/analyze")
    public IncidentSummary triggerAnalysis(@RequestBody String incidentDetails) {
        return analyzerService.analyzeIncident(incidentDetails);
    }
}