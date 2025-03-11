package com.fourroro.nolleogasil_backend.apiPayLoad.Exception;

public class InvalidJWTException extends RuntimeException {
    public InvalidJWTException(String message) {
        super(message);
    }
}
