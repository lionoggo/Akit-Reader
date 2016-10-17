package com.closedevice.fastapp.view.empty;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.closedevice.fastapp.R;
import com.closedevice.fastapp.util.OSUtil;
import com.closedevice.fastapp.view.loading.Loading;


public class EmptyLayout extends LinearLayout {
    /**
     * 隐藏布局
     */
    public static final int STATE_HIDE_LAYOUT = 4;
    /**
     * 网络错误
     */
    public static final int STATE_NETWORK_ERROR = 1;
    /**
     * 网络加载中
     */
    public static final int STATE_NETWORK_LOADING = 2;
    /**
     * 没有数据
     */
    public static final int STATE_NODATA = 3;
    /**
     * 没有数据，可点击重新获取
     */
    public static final int STATE_NODATA_ENABLE_CLICK = 5;
    /**
     * 未登录
     */
    public static final int STATE_NO_LOGIN = 6;

    private Loading mLoading;
    private boolean clickEnable = true;
    private final Context mContext;
    public ImageView mIvError;
    private OnClickListener mListener;
    private int mErrorState;
    private String strNoDataContent = "";
    private TextView mTvErrorMsg;

    public EmptyLayout(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        View view = View.inflate(mContext, R.layout.view_error_layout, null);
        mIvError = (ImageView) view.findViewById(R.id.iv_error_layout);
        mTvErrorMsg = (TextView) view.findViewById(R.id.tv_error_layout);
        RelativeLayout mLayout = (RelativeLayout) view.findViewById(R.id.pageerrLayout);
        mLoading = (Loading) view.findViewById(R.id.animProgress);
        setBackgroundColor(-1);
        mIvError.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (clickEnable) {
                    // setErrorType(NETWORK_LOADING);
                    if (mListener != null)
                        mListener.onClick(v);
                }
            }
        });
        addView(view);
    }


    public void dismiss() {
        mErrorState = STATE_HIDE_LAYOUT;
        setVisibility(View.GONE);
    }

    public int getErrorState() {
        return mErrorState;
    }

    public boolean isLoadError() {
        return mErrorState == STATE_NETWORK_ERROR;
    }

    public boolean isLoading() {
        return mErrorState == STATE_NETWORK_LOADING;
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }


    public void setErrorMsg(String msg) {
        mTvErrorMsg.setText(msg);
    }


    public void setErrorImg(int imgId) {
        try {
            mIvError.setImageResource(imgId);
        } catch (Exception e) {
            //not handle
        }
    }

    public void setErrorType(int i) {
        setVisibility(View.VISIBLE);
        switch (i) {
            case STATE_NETWORK_ERROR:
                mErrorState = STATE_NETWORK_ERROR;
                if (OSUtil.hasInternet()) {
                    mTvErrorMsg.setText(R.string.error_view_load_error_click_to_refresh);
                    mIvError.setBackgroundResource(R.drawable.ic_loading_error);
                } else {
                    mTvErrorMsg.setText(R.string.error_view_network_error_click_to_refresh);
                    mIvError.setBackgroundResource(R.drawable.ic_network_error);
                }
                mIvError.setVisibility(View.VISIBLE);
                mLoading.stop();
                mLoading.setVisibility(View.GONE);
                clickEnable = true;
                break;
            case STATE_NETWORK_LOADING:
                mErrorState = STATE_NETWORK_LOADING;
                mLoading.setVisibility(View.VISIBLE);
                mLoading.start();
                mIvError.setVisibility(View.GONE);
                mTvErrorMsg.setText(R.string.error_view_loading);
                clickEnable = false;
                break;
            case STATE_NODATA:
                mErrorState = STATE_NODATA;
                mIvError.setBackgroundResource(R.drawable.ic_empty);
                mIvError.setVisibility(View.VISIBLE);
                mLoading.stop();
                mLoading.setVisibility(View.GONE);
                setTvNoDataContent();
                clickEnable = true;
                break;
            case STATE_HIDE_LAYOUT:
                mLoading.stop();
                setVisibility(View.GONE);
                break;
            case STATE_NODATA_ENABLE_CLICK:
                mErrorState = STATE_NODATA_ENABLE_CLICK;
                mIvError.setBackgroundResource(R.drawable.ic_empty);
                // img.setBackgroundDrawable(SkinsUtil.getDrawable(context,"page_icon_empty"));
                mIvError.setVisibility(View.VISIBLE);
                mLoading.stop();
                mLoading.setVisibility(View.GONE);
                setTvNoDataContent();
                clickEnable = true;
                break;
            default:
                break;
        }
    }

    public void setNoDataContent(String noDataContent) {
        strNoDataContent = noDataContent;
    }

    public void setOnLayoutClickListener(OnClickListener listener) {
        this.mListener = listener;
    }

    public void setTvNoDataContent() {
        if (!strNoDataContent.equals(""))
            mTvErrorMsg.setText(strNoDataContent);
        else
            mTvErrorMsg.setText(R.string.error_view_no_data);
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.GONE)
            mErrorState = STATE_HIDE_LAYOUT;
        super.setVisibility(visibility);
    }
}
