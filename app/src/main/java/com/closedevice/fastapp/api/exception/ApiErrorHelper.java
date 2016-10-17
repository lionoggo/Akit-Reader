package com.closedevice.fastapp.api.exception;

import android.app.Activity;
import android.content.Context;

import com.closedevice.fastapp.AppContext;
import com.closedevice.fastapp.R;
import com.closedevice.fastapp.base.BaseApplication;
import com.closedevice.fastapp.util.LogUtils;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;


public class ApiErrorHelper {

    private static final java.lang.String TAG = "ApiErrorHelper";

    public static void handleCommonError(Context context, Throwable e) {
        if (e instanceof HttpException || e instanceof IOException) {
            AppContext.toastShort(BaseApplication.resources().getString(R.string.error_no_internet));
            LogUtils.e(TAG, e.getMessage());
        } else if (e instanceof ApiException) {
            handleApiError(context, e);
        } else {
            LogUtils.e(TAG, e.getMessage());
        }


    }

    private static void handleApiError(Context context, Throwable e) {
        ApiException exception = (ApiException) e;
        switch (exception.getErrorCode()) {
            case ApiErrorCode.ERROR_CLIENT_AUTHORIZED:
                AppContext.toastShort(BaseApplication.resources().getString(R.string.error_invalid_client));
                break;
            case ApiErrorCode.ERROR_NO_INTERNET:
                AppContext.toastShort(BaseApplication.resources().getString(R.string.error_no_internet));
                break;
            case ApiErrorCode.ERROR_OTHER:
                AppContext.toast(BaseApplication.resources().getString(R.string.error_other));
                break;
            case ApiErrorCode.ERROR_PARAM_CHECK:
            case ApiErrorCode.ERROR_REQUEST_PARAM:
                LogUtils.e(TAG, "param request error:" + exception.getMessage());
                break;
            case ApiErrorCode.ERROR_USER_AUTHORIZED:
                reLogin(context);
                break;
            default:
                AppContext.toastShort(BaseApplication.resources().getString(R.string.error_unknown));
                break;
        }


    }

    private static void reLogin(final Context context) {
        if (context != null && context instanceof Activity) {
            //need login
        }
    }
}
