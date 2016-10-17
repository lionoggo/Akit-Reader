package com.closedevice.fastapp.api.exception;


public class ApiException extends RuntimeException {
    private int errorCode;
    private String extendErrorMsg;

    public ApiException(int code, String msg) {
        super(msg);
        this.errorCode = code;
    }


    public ApiException(int code, String msg, String extendErrorMsg) {
        super(msg);
        this.errorCode = code;
        this.extendErrorMsg = extendErrorMsg;
    }


    public int getErrorCode() {
        return errorCode;
    }

    public boolean isTokenExpried() {
        return errorCode == ApiErrorCode.ERROR_USER_AUTHORIZED;
    }

    public boolean isInvlidClient() {
        return errorCode == ApiErrorCode.ERROR_CLIENT_AUTHORIZED;
    }


    public String getExtendErrorMsg() {
        return extendErrorMsg;
    }

}
