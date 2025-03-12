package com.fourroro.nolleogasil_backend.apiPayLoad.Exception;

import com.fourroro.nolleogasil_backend.apiPayLoad.code.BaseErrorCode;

public class UsersException extends GeneralException {
    public UsersException(BaseErrorCode errorCode){super (errorCode); }
}
