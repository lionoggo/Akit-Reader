package com.closedevice.fastapp.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SafeUtils {
    public static byte[] getMd5(byte[] content) throws NoSuchAlgorithmException {
        MessageDigest mdInst = MessageDigest.getInstance("MD5");
        mdInst.update(content);
        return mdInst.digest();
    }

}
