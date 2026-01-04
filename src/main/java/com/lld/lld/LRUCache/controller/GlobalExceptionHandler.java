package com.lld.lld.LRUCache.controller;

import com.lld.lld.LRUCache.dto.CacheResponse;
import com.lld.lld.LRUCache.exception.CacheException;
import com.lld.lld.LRUCache.exception.KeyNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Global exception handler for the LRU Cache microservice
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(KeyNotFoundException.class)
    public ResponseEntity<CacheResponse> handleKeyNotFoundException(KeyNotFoundException ex) {
        log.error("Key not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(CacheResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(CacheException.class)
    public ResponseEntity<CacheResponse> handleCacheException(CacheException ex) {
        log.error("Cache exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(CacheResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CacheResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Illegal argument: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(CacheResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CacheResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        log.error("Method argument type mismatch: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(CacheResponse.builder()
                .success(false)
                .message("Invalid parameter type: " + ex.getMessage())
                .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CacheResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(CacheResponse.builder()
                .success(false)
                .message("An unexpected error occurred: " + ex.getMessage())
                .build());
    }
}






