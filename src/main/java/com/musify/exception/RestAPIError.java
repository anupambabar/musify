package com.musify.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RestAPIError {

    private HttpStatusCode status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;
    private List<RestApiSubError> subErrors;

    private RestAPIError() {
        timestamp = LocalDateTime.now();
    }

    RestAPIError(HttpStatusCode status) {
        this();
        this.status = status;
    }

    RestAPIError(HttpStatusCode status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    RestAPIError(HttpStatusCode status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }
}
