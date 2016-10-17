package com.closedevice.fastapp.model.response;


public interface BaseResult<T> {
    boolean isOk();

    T getData();
}
