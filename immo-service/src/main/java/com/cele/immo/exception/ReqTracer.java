package com.cele.immo.exception;

import brave.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReqTracer {
    @Autowired
    private Tracer tracer;

    public String traceId() {
        return tracer.currentSpan().context().traceIdString();
    }
}
