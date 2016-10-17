package com.closedevice.fastapp.util;


import android.content.Context;
import android.content.DialogInterface;

import com.closedevice.fastapp.AppConstant;
import com.closedevice.fastapp.R;
import com.closedevice.fastapp.api.remote.ApiFactory;
import com.closedevice.fastapp.base.BaseApplication;
import com.closedevice.fastapp.model.response.fir.Version;
import com.closedevice.fastapp.router.Router;
import com.closedevice.fastapp.view.dialog.DialogHelper;
import com.closedevice.fastapp.view.dialog.WaitDialog;

import java.lang.ref.WeakReference;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UpdateManager {


    private WeakReference<Context> mContext;

    private boolean isShow = false;

    private WaitDialog mWaitDialog;
    private Version mVersion;


    public UpdateManager(Context context, boolean isShow) {
        mContext = new WeakReference<>(context);
        this.isShow = isShow;
    }

    public boolean haveNew() {

        boolean haveNew = false;
        int curVersionCode = OSUtil.getVersionCode();

        if ((Integer.parseInt(mVersion.getVersion()) > curVersionCode)) {

            haveNew = true;
        }
        return haveNew;
    }

    public void checkUpdate() {
        if (isShow) {
            showCheckDialog();
        }

        ApiFactory.getFirApi().getVersion(AppConstant.KEY_APP_ID, AppConstant.TOKEN_FIR).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Version>() {

            @Override
            public void onCompleted() {
                finshCheck();
            }

            @Override
            public void onError(Throwable e) {
                hideCheckDialog();
                if (isShow) {
                    showFaileDialog();
                }

            }

            @Override
            public void onNext(Version version) {
                hideCheckDialog();
                mVersion = version;
            }
        });

    }

    private void finshCheck() {
        if (haveNew()) {
            showUpdateInfo();
        } else {
            if (isShow) {
                showLatestDialog();
            }
        }
    }

    private void showCheckDialog() {
        if ((mContext.get() == null))
            return;
        if (mWaitDialog == null) {
            mWaitDialog = DialogHelper.getWaitDialog(mContext.get(),
                    BaseApplication.resources().getString(R.string.update_checking));
        }
        mWaitDialog.show();
    }

    private void hideCheckDialog() {
        if (mWaitDialog != null) {
            mWaitDialog.dismiss();
        }
    }

    private void showUpdateInfo() {
        if (mVersion == null || mContext.get() == null) {
            return;
        }


        DialogHelper.getConfirmDialog(mContext.get(), mVersion.getName(), mVersion.getChangelog(), BaseApplication.resources().getString(R.string.update_now), BaseApplication.resources().getString(R.string.update_delay), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Router.startDownloadService(mContext.get(), mVersion.getInstallUrl(), BaseApplication.resources().getString(R.string.update_notice));
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(false).show();


    }

    private void showLatestDialog() {
        if (mContext.get() == null) return;
        DialogHelper.getMessageDialog(mContext.get(), BaseApplication.resources().getString(R.string.update_no_new)).show();
    }

    private void showFaileDialog() {
        if (mContext.get() == null)
            DialogHelper.getMessageDialog(mContext.get(), BaseApplication.resources().getString(R.string.update_check_error)).show();
    }


}
