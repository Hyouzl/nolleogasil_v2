package com.fourroro.nolleogasil_backend.apiPayLoad.code;

public interface BaseErrorCode {

    ErrorReasonDTO getReason();

    ErrorReasonDTO getReasonHttpStatus();
}