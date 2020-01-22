package com.cele.immo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Slf4j
@Component
public class GenericExceptionWrapper extends DefaultErrorAttributes {

    @Autowired
    private ReqTracer tracer;

    GenericExceptionWrapper(ReqTracer tracer) {
        super(false);
    }

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {

        final Throwable error = getError(request);
        final Map<String, Object> errorAttributes = super.getErrorAttributes(request, false);

        errorAttributes.put(ErrorAttribute.TRACE_ID.value, tracer.traceId());

        if (error instanceof GenericException) {
            log.debug("Caught an instance of: {}, err: {}", GenericException.class, error);

            final HttpStatus errorStatus = ((GenericException) error).getStatus();
            errorAttributes.replace(ErrorAttribute.STATUS.value, errorStatus.value());
            errorAttributes.replace(ErrorAttribute.ERROR.value, errorStatus.getReasonPhrase());

            return errorAttributes;
        }
        return errorAttributes;
    }


    enum ErrorAttribute {
        STATUS("status"),
        ERROR("error"),
        TRACE_ID("traceId");

        private final String value;

        ErrorAttribute(String value) {
            this.value = value;
        }
    }
}
