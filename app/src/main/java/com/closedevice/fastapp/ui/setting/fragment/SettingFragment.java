package com.closedevice.fastapp.ui.setting.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.closedevice.fastapp.AppConstant;
import com.closedevice.fastapp.AppContext;
import com.closedevice.fastapp.R;
import com.closedevice.fastapp.base.ui.BaseFragment;
import com.closedevice.fastapp.cache.CacheCleanManager;
import com.closedevice.fastapp.db.RealmHelper;
import com.closedevice.fastapp.router.Router;
import com.closedevice.fastapp.util.BsPatchUtil;
import com.closedevice.fastapp.util.LogUtils;
import com.closedevice.fastapp.util.OSUtil;
import com.closedevice.fastapp.view.dialog.DialogHelper;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class SettingFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
    @Bind(R.id.ll_setting_like_read)
    LinearLayout llSettingLikeRead;
    @Bind(R.id.ll_setting_recent_read)
    LinearLayout llSettingRecentRead;
    @Bind(R.id.cb_setting_cache)
    AppCompatCheckBox cbSettingCache;
    @Bind(R.id.cb_setting_image)
    AppCompatCheckBox cbSettingNight;
    @Bind(R.id.tv_setting_clear)
    TextView tvSettingClear;
    @Bind(R.id.ll_setting_clear)
    LinearLayout llSettingClear;
    @Bind(R.id.tv_setting_version)
    TextView tvSettingUpdate;
    @Bind(R.id.ll_setting_update)
    LinearLayout llSettingUpdate;
    @Bind(R.id.tv_settting_recent_read)
    TextView tvRecentReadNum;
    @Bind(R.id.tv_settting_liked_read)
    TextView tvRecentLikedNum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        initData();
        initView(view);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    public void initView(View view) {
        ButterKnife.bind(this, view);
        cbSettingCache.setOnCheckedChangeListener(this);
        cbSettingNight.setOnCheckedChangeListener(this);

        setNumAsync(tvRecentReadNum, getReadNum());
        setNumAsync(tvRecentLikedNum, getLikeNum());

        tvSettingUpdate.setText("V " + OSUtil.getVersionName());
        tvSettingClear.setText(CacheCleanManager.getFormatSize(CacheCleanManager.getFolderSize(new File(AppConstant.NET_DATA_PATH))));


    }


    private void setNumAsync(final TextView tv, int num) {
        Observable.just(num).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return integer + "";
                    }
                }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                tv.setText(s);
            }
        });
    }

    private int getReadNum() {
        RealmHelper helper = new RealmHelper(AppContext.context());
        return helper.findAllReadRecord().size();
    }

    private int getLikeNum() {
        RealmHelper helper = new RealmHelper(AppContext.context());
        return helper.findAllLikeRecord().size();
    }


    @Override
    public void initData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_setting_cache:
                AppContext.getInstance().setAutoCacheMode(isChecked);
                break;
            case R.id.cb_setting_image:
                AppContext.getInstance().setNoImageMode(isChecked);
                break;
        }

    }

    @OnClick({R.id.ll_setting_like_read, R.id.ll_setting_recent_read, R.id.ll_setting_clear, R.id.ll_setting_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_setting_like_read:
                Router.showMyLike(getActivity());
                break;
            case R.id.ll_setting_recent_read:
                Router.showRecentRead(getActivity());
                break;
            case R.id.ll_setting_clear:
                showClearDialog();
                break;
            case R.id.ll_setting_update:
//                new UpdateManager(getActivity(), true).checkUpdate();
                smartupdate();
                break;
        }
    }

    private void showClearDialog() {
        DialogHelper.getConfirmDialog(getActivity(), getString(R.string.setting_clear_cache), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                CacheCleanManager.cleanCustomCache(new File(AppConstant.NET_DATA_PATH));
                tvSettingClear.setText(CacheCleanManager.getFormatSize(CacheCleanManager.getFolderSize(new File(AppConstant.NET_DATA_PATH))));
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }


    private void smartupdate() {
        Observable.create(new Observable.OnSubscribe<File>() {
            @Override
            public void call(Subscriber<? super File> subscriber) {
                File newApk = new File(Environment.getExternalStorageDirectory(), "newApk.apk");
                File patch = new File(Environment.getExternalStorageDirectory(), "patch.patch");
                if (!patch.exists()) {
                    subscriber.onError(new IOException("patch file not exist!"));
                    return;
                }
                BsPatchUtil.patch(OSUtil.getApkInstalledSrc(), newApk.getAbsolutePath(), patch.getAbsolutePath());
                if (newApk.exists()) {
                    subscriber.onNext(newApk);
                    subscriber.onCompleted();
                    patch.delete();
                } else {
                    subscriber.onError(new IOException("bspatch failed,file not exist!"));
                }


            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showDialog("正在应用差分包");
                    }
                })
                .subscribe(new Subscriber<File>() {
                    @Override
                    public void onCompleted() {
                        hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideDialog();
                        LogUtils.d(e.getMessage());
                    }

                    @Override
                    public void onNext(File file) {
                        OSUtil.installAPK(getActivity(), file);
                    }
                });

        CompositeSubscription subscriptions = new CompositeSubscription();


        subscriptions.add();


        subscriptions.unsubscribe();

    }





}
