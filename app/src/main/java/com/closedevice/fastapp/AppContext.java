package com.closedevice.fastapp;

import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.text.TextUtils;

import com.closedevice.fastapp.base.BaseApplication;
import com.closedevice.fastapp.util.LogUtils;
import com.closedevice.fastapp.util.OSUtil;
import com.closedevice.fastapp.util.SafeUtils;
import com.closedevice.fastapp.util.StringUtils;
import com.tencent.smtt.sdk.QbSdk;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;


public class AppContext extends BaseApplication {

    private static final String KEY_APP_ID = "appid";
    private static final String KEY_APK_SIGNATURE = "app_signature";

    private static AppContext instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        Thread.setDefaultUncaughtExceptionHandler(AppCrash.getAppExceptionHandler());
        LogUtils.onStrictMode();
        initQbSdk();
    }

    private void initQbSdk() {
        try {
            QbSdk.allowThirdPartyAppDownload(true);
            QbSdk.initX5Environment(getApplicationContext(), QbSdk.WebviewInitType.FIRSTUSE_AND_PRELOAD, new QbSdk.PreInitCallback() {
                @Override
                public void onCoreInitFinished() {

                }

                @Override
                public void onViewInitFinished(boolean b) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AppContext getInstance() {
        return instance;
    }


    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }


    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }


    public String getAppId() {
        String uniqueID = getProperty(KEY_APP_ID);
        if (StringUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            setProperty(KEY_APP_ID, uniqueID);
        }
        return uniqueID;
    }

    private int clearCacheFolder(File dir, long curTime) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, curTime);
                    }
                    if (child.lastModified() < curTime) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

    public static void setProperty(String key, String value) {
        Editor editor = getPreferences().edit();
        editor.putString(key, value);
        apply(editor);
    }

    public static String getProperty(String key) {
        return getPreferences().getString(key, null);
    }

    public static void removeProperty(String... keys) {
        for (String key : keys) {
            Editor editor = getPreferences().edit();
            editor.putString(key, null);
            apply(editor);
        }
    }


    public void saveSignature(String signature) {
        Editor edit = getPreferences().edit();
        edit.putString(KEY_APK_SIGNATURE, signature);
        apply(edit);
    }

    public String getSignature() {
        String text = getPreferences().getString(KEY_APK_SIGNATURE, "");
        if (TextUtils.isEmpty(text)) {
            Signature signature = OSUtil.getPackageSignature(this, getPackageName());
            try {
                text = StringUtils.hexToString(SafeUtils.getMd5(signature.toByteArray()));
                saveSignature(text);
            } catch (NoSuchAlgorithmException e) {
                LogUtils.i(e.getMessage());
            }

        }


        return text;
    }


    public void setNoImageMode(boolean isChecked) {
        Editor editor = getPreferences().edit();
        editor.putBoolean(AppConstant.KEY_MODE_NO_IMAGE, isChecked);
        apply(editor);
    }

    public boolean isNoImageMode() {
        return getPreferences().getBoolean(AppConstant.KEY_MODE_NO_IMAGE, false);
    }

    public void setAutoCacheMode(boolean isChecked) {
        Editor editor = getPreferences().edit();
        editor.putBoolean(AppConstant.KEY_MODE_AUTO_CACHE, isChecked);
        apply(editor);
    }

    public boolean isAutoCacheMode() {
        return getPreferences().getBoolean(AppConstant.KEY_MODE_AUTO_CACHE, false);
    }
}
