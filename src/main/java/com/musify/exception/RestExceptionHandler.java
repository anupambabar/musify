package com.musify.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HandlerMethod handlerMethod) {

        LOGGER.debug(">>>>>>>>>>>>>>> EXCEPTION THROWN From :: Class:" + handlerMethod.getClass() + " : Method:" + handlerMethod.getMethod());
        LOGGER.debug(">>>>>>>>>>>>>>> EXCEPTION STACKTRACE :: ", ex);

        String error = "Malformed JSON request";
        return buildResponseEntity(new RestAPIError(HttpStatus.BAD_REQUEST, error, ex));
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public final ResponseEntity<Object> handleHttpServerErrorException(HttpServerErrorException ex, HandlerMethod handlerMethod) {

        LOGGER.debug(">>>>>>>>>>>>>>> EXCEPTION THROWN From :: Class:" + handlerMethod.getClass() + " : Method:" + handlerMethod.getMethod());
        LOGGER.debug(">>>>>>>>>>>>>>> EXCEPTION STACKTRACE :: ", ex);

        String error = "";

        if ("500".equals(ex.getRawStatusCode()))
            error = error + "Internal Server Error";
        else
            error = error + "Service Unavailable";

        return buildResponseEntity(new RestAPIError(ex.getStatusCode(), error, ex));
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    public final ResponseEntity<Object> handleHttpStatusCodeException(HttpStatusCodeException ex, HandlerMethod handlerMethod) {

        LOGGER.debug(">>>>>>>>>>>>>>> EXCEPTION THROWN From :: Class:" + handlerMethod.getClass() + " : Method:" + handlerMethod.getMethod());
        LOGGER.debug(">>>>>>>>>>>>>>> EXCEPTION STACKTRACE :: ", ex);

        String error = "";

        if ("404".equals(ex.getRawStatusCode()))
            error = error + "Request Not Found";
        else if ("401".equals(ex.getRawStatusCode()))
            error = error + "Unauthorized Access";
        else
            error = error + "Failure Response Received";

        return buildResponseEntity(new RestAPIError(ex.getStatusCode(), error, ex));
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, HandlerMethod handlerMethod) {

        LOGGER.debug(">>>>>>>>>>>>>>> EXCEPTION THROWN From :: Class:" + handlerMethod.getClass() + " : Method:" + handlerMethod.getMethod());
        LOGGER.debug(">>>>>>>>>>>>>>> EXCEPTION STACKTRACE :: ", ex);

        return buildResponseEntity(new RestAPIError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex));
    }

    private ResponseEntity<Object> buildResponseEntity(RestAPIError restAPIError) {
        return new ResponseEntity<>(restAPIError, restAPIError.getStatus());
    }

}
