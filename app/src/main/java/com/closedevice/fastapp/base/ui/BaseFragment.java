package com.closedevice.fastapp.base.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.closedevice.fastapp.AppContext;
import com.closedevice.fastapp.R;
import com.closedevice.fastapp.base.inter.IBaseFragment;
import com.closedevice.fastapp.view.dialog.IDialog;
import com.closedevice.fastapp.view.dialog.WaitDialog;
import com.closedevice.rxlife.RxFragment;


public class BaseFragment extends RxFragment implements
        View.OnClickListener, IBaseFragment {
    /**
     * 正常
     */
    public static final int STATE_NONE = 0;
    /**
     * 刷新中
     */
    public static final int STATE_REFRESH = 1;
    /**
     * 加载更多
     */
    public static final int STATE_LOADMORE = 2;
    /**
     * 没有更多
     */
    public static final int STATE_NOMORE = 3;
    /**
     * 下拉中,但还未触发刷新
     */
    public static final int STATE_PRESSNONE = 4;

    public static int mState = STATE_NONE;

    protected LayoutInflater mInflater;

    public AppContext getApplication() {
        return (AppContext) getActivity().getApplication();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mInflater = inflater;
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected int getLayoutId() {
        return 0;
    }

    protected View inflateView(int resId) {
        return this.mInflater.inflate(resId, null);
    }

    public boolean onBackPressed() {
        return false;
    }

    protected void hideDialog() {
        FragmentActivity activity = getActivity();
        if (activity instanceof IDialog) {
            ((IDialog) activity).hideWaitDialog();
        }
    }

    protected WaitDialog showDialog() {
        return showDialog(R.string.loading);
    }

    protected WaitDialog showDialog(int resid) {
        FragmentActivity activity = getActivity();
        if (activity instanceof IDialog) {
            return ((IDialog) activity).showWaitDialog(resid);
        }
        return null;
    }

    protected WaitDialog showDialog(String str) {
        FragmentActivity activity = getActivity();
        if (activity instanceof IDialog) {
            return ((IDialog) activity).showWaitDialog(str);
        }
        return null;
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void initData() {


    }

    @Override
    public void onClick(View v) {

    }
}
