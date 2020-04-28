package com.ring.utilsdemo.utils;

import android.text.TextUtils;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by ring on 2019/6/28.
 */
public class AESUtil {

    private final static String PY = "qwertyuiopasdfgh";

    /**
     * 数据加密
     *
     * @param key       加密密钥
     * @param cleartext 加密前的内容
     * @return 加密后的数据
     */
    public static String encrypt(String key, String cleartext) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(cleartext)) {
            return cleartext;
        }
        byte[] raw = InsecureSHA1PRNGKeyDerivator.deriveInsecureKey(key.getBytes(), 16);
        SecretKeySpec secretKeySpec = new SecretKeySpec(raw, "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(PY.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(cleartext.getBytes());
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cleartext;
    }

    /**
     * 数据解密
     *
     * @param key       解密秘钥
     * @param encrypted 解密前内容
     * @return 解密后的数据
     */
    public static String decrypt(String key, String encrypted) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(encrypted)) {
            return encrypted;
        }
        byte[] raw = InsecureSHA1PRNGKeyDerivator.deriveInsecureKey(key.getBytes(), 16);
        SecretKeySpec secretKeySpec = new SecretKeySpec(raw, "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(PY.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] enc = Base64.decode(encrypted, Base64.DEFAULT);
            byte[] decrypted = cipher.doFinal(enc);
            return new String(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypted;
    }

}
