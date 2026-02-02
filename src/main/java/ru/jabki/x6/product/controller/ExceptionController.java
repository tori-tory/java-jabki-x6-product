package ru.jabki.x6.product.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.jabki.x6.product.exception.ApiError;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleError(final Exception exception) {
        return  ResponseEntity.badRequest()
                .body(
                        new ApiError(
                                false,
                                exception.getMessage()
                        )
                );
    }
}