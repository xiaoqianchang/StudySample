package com.planet.light2345.seclib;

import android.content.Context;
import android.text.TextUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author chenyi
 * @date 2018/9/29
 */
public class SecService {

  static {
    try {
      System.loadLibrary("planetlight2345");
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  private native static byte[] encryptIn(Context context, byte[] input);

  private native static byte[] decryptIn(Context context, byte[] input);

  private native static byte[] encryptP1(Context context, byte[] input, byte[] p1);

  private native static byte[] decryptP1(Context context, byte[] input, byte[] p1);

  private native static byte[] encrypt2(Context context, byte[] input, byte[] p1, byte[] p2);

  private native static byte[] decrypt2(Context context, byte[] input, byte[] p1, byte[] p2);

  /**
   * 加密
   *
   * @param p0 需要加密数据
   * @param p1 key
   * @return 返回解密后的字符串
   */
  public static String encryptStringNoEncoded(final Context context, String p0, String p1) {
    if (!TextUtils.isEmpty(p0)) {
      try {
        byte[] encrypt = encryptP1(context,
            p0.getBytes("UTF-8"),
            p1.getBytes("UTF-8"));
        return new String(encrypt, "UTF-8");
      } catch (Throwable e) {
        e.printStackTrace();
      }
    }
    return "";
  }

  /**
   * 加密
   *
   * @param p0 需要加密数据
   * @param p1 key
   * @return 返回解密后的字符串并URLEncoder.encode
   */
  public static String encryptString(final Context context, String p0, String p1) {
    try {
      return URLEncoder.encode(encryptStringNoEncoded(context, p0, p1), "UTF-8");
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return "";
  }

  /**
   * 解密
   *
   * @param p0 需要解密数据
   * @param p1 key
   */
  public static String decryptString(Context context, String p0, String p1) {
    if (!TextUtils.isEmpty(p0)) {
      try {
        String decodeString = URLDecoder.decode(p0, "UTF-8");
        byte[] decrypt = decryptP1(context, decodeString.getBytes("UTF-8"), p1.getBytes("UTF-8"));
        return new String(decrypt);
      } catch (Throwable e) {
        e.printStackTrace();
      }
    }
    return "";
  }


  /**
   * 加密
   *
   * @param p0 需要加密的数据
   */
  public static String encryptString(final Context context, String p0) {
    if (!TextUtils.isEmpty(p0)) {
      try {
        byte[] encrypt = encryptIn(context, p0.getBytes("UTF-8"));
        return URLEncoder.encode(new String(encrypt, "UTF-8"), "UTF-8");
      } catch (Throwable e) {
        e.printStackTrace();
      }
    }
    return "";
  }

  /**
   * 解密
   *
   * @param p0 需要解密的数据
   */
  public static String decryptString(Context context, String p0) {
    if (!TextUtils.isEmpty(p0)) {
      try {
        String decodeString = URLDecoder.decode(p0, "UTF-8");
        byte[] decrypt = decryptIn(context, decodeString.getBytes("UTF-8"));
        return new String(decrypt);
      } catch (Throwable e) {
        e.printStackTrace();
      }
    }
    return "";
  }

  /**
   * 加密
   *
   * @param p0 需要加密的数据
   * @param p1 用户uid
   * @param p2 用户salt
   */
  public static String encyptString(final Context context, String p0, String p1, String p2) {
    if (!TextUtils.isEmpty(p0)) {
      try {
        byte[] encrypt = encrypt2(context, p0.getBytes("UTF-8"), p1.getBytes("UTF-8"),
            p2.getBytes("UTF-8"));
        return URLEncoder.encode(new String(encrypt, "UTF-8"), "UTF-8");
      } catch (Throwable e) {
        e.printStackTrace();
      }
    }
    return "";
  }

  /**
   * 解密
   *
   * @param p0 需要解密的数据
   * @param p1 用户uid
   * @param p2 用户salt
   */
  public static String decryptString(Context context, String p0, String p1, String p2) {
    if (!TextUtils.isEmpty(p0)) {
      try {
        String decodeString = URLDecoder.decode(p0, "UTF-8");
        byte[] decrypt = decrypt2(context, decodeString.getBytes("UTF-8"),
            p1.getBytes("UTF-8"),
            p2.getBytes("UTF-8"));
        return new String(decrypt);
      } catch (Throwable e) {
        e.printStackTrace();
      }
    }
    return "";
  }


}
