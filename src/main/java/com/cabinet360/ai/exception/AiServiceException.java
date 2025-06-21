package com.cabinet360.ai.exception;

public class AiServiceException extends RuntimeException {

    private final String errorCode;

    public AiServiceException(String message) {
        super(message);
        this.errorCode = "AI_SERVICE_ERROR";
    }

    public AiServiceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public AiServiceException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "AI_SERVICE_ERROR";
    }

    public AiServiceException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}