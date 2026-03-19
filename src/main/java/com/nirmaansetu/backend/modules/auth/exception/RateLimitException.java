package com.nirmaansetu.backend.modules.auth.exception;

public class RateLimitException extends RuntimeException{
    public RateLimitException(String message) {
        super(message);
    }
}
