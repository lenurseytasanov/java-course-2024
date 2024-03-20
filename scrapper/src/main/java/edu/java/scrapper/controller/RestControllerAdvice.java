package edu.java.scrapper.controller;

import edu.java.dto.ApiErrorResponse;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handle(Exception e) {
        return new ResponseEntity<>(new ApiErrorResponse(
            "default error", "400",
            "error", e.getMessage(),
            Arrays.stream(e.getStackTrace())
                .map(StackTraceElement::toString)
                .toArray(String[]::new)),
            HttpStatus.BAD_REQUEST);
    }
}
