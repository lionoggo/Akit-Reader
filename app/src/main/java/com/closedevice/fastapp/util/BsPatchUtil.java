package com.closedevice.fastapp.util;

public class BsPatchUtil {
    static {
        System.loadLibrary("apkpatch");
    }

    public static native int patch(String oldApk, String newApk, String patch);
}
