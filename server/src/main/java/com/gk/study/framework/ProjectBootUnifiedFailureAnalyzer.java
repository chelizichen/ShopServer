package com.gk.study.framework;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

public class ProjectBootUnifiedFailureAnalyzer extends AbstractFailureAnalyzer<RuntimeException> {
    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, RuntimeException cause) {
        System.err.println("[Sgrid-Java] [ProjectBootUnifiedFailureAnalyzer] " + cause.getMessage() + "error" + rootFailure);
        return new FailureAnalysis(cause.getMessage(), "error", rootFailure);
    }
}