package com.closedevice.fastapp.base.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.MySwipeRefreshLayout;
import android.view.View;

import com.closedevice.fastapp.AppContext;
import com.closedevice.fastapp.R;
import com.closedevice.fastapp.api.exception.ApiException;
import com.closedevice.fastapp.base.BaseSubscriber;
import com.closedevice.fastapp.model.base.Entity;
import com.closedevice.fastapp.model.response.wx.WXResult;
import com.closedevice.fastapp.view.empty.EmptyLayout;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;


public class BaseDetailFragment extends BaseFragment implements MySwipeRefreshLayout.OnRefreshListener {// OnItemClickListener

    protected MySwipeRefreshLayout mRefreshLayout;
    protected EmptyLayout mEmptyLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRefreshLayout = (MySwipeRefreshLayout) view.findViewById(R.id.srl_refresh);
        mEmptyLayout = (EmptyLayout) view.findViewById(R.id.error_layout);
        if (mRefreshLayout != null) {
            mRefreshLayout.setColorSchemeResources(R.color.main_red, R.color.main_gray, R.color.main_black, R.color.main_purple);
            mRefreshLayout.setOnRefreshListener(this);
        }

        requestData();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        //cancelReadCache();
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        sendRequestData();
    }


    public BaseSubscriber getSubscriber() {
        return new BaseSubscriber<WXResult<?>>() {
            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof HttpException) {
                    AppContext.toastShort("服务暂不可用");
                } else if (e instanceof IOException) {
                    AppContext.toastShort("无法连接服务器");
                } else if (e instanceof ApiException) {
                    ApiException exception = (ApiException) e;
                    if (exception.isTokenExpried()) {
                        AppContext.toast(exception.getMessage());
                    } else {
                        AppContext.toast(e.getMessage());
                    }
                }
                executeOnLoadDataError(e.getMessage());
                executeOnLoadFinish();

            }

            @Override
            public void onNext(WXResult<?> result) {
                if (result.isOk()) {
                    Entity entry = (Entity) result.getNewslist();
                    if (entry != null) {
                        if (mEmptyLayout != null)
                            mEmptyLayout.setErrorType(EmptyLayout.STATE_HIDE_LAYOUT);
                        executeOnLoadDataSuccess(entry);
                        executeOnLoadFinish();
                    } else {
                        executeOnLoadDataError(null);
                    }

                }
            }


        };
    }


    protected void requestData() {
        if (mEmptyLayout != null)
            mEmptyLayout.setErrorType(EmptyLayout.STATE_NETWORK_LOADING);
        sendRequestData();
    }


    protected void sendRequestData() {
    }

    protected void executeOnLoadDataSuccess(Entity entity) {
    }


    protected void executeOnLoadDataError(String object) {
        mEmptyLayout.setErrorType(EmptyLayout.STATE_NETWORK_ERROR);
        mEmptyLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mState = STATE_REFRESH;
                mEmptyLayout.setErrorType(EmptyLayout.STATE_NETWORK_LOADING);
                sendRequestData();
            }
        });
    }

    protected void executeOnLoadFinish() {
        if (mRefreshLayout != null)
            mRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
