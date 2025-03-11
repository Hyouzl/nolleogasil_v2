package com.fourroro.nolleogasil_backend.apiPayLoad.Exception;

import com.fourroro.nolleogasil_backend.apiPayLoad.code.BaseErrorCode;
import com.fourroro.nolleogasil_backend.apiPayLoad.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }
}