package com.closedevice.fastapp.model.response.wx;

import com.closedevice.fastapp.model.response.BaseResult;


public class WXResult<T> implements BaseResult<T> {
    private int code;
    private String msg;
    private T newslist;

    public T getNewslist() {
        return newslist;
    }

    public void setNewslist(T newslist) {
        this.newslist = newslist;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    @Override
    public boolean isOk() {
        return code == 200;
    }

    @Override
    public T getData() {
        return newslist;
    }
}
