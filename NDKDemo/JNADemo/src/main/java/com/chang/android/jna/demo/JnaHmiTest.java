package com.chang.android.jna.demo;

import android.util.Log;

import com.sun.jna.Pointer;

/**
 * Description: $
 * <p>
 * Created by Nicholas Sean on 2021/5/21 10:36 AM.
 *
 * @version 1.0
 */
public class JnaHmiTest {

    private static final String TAG = "JnaHmiTest";

    public static void main(String args[]) {
        new JnaHmiTest().testHmiMethod();
    }

    public void testHmiMethod() {
        /*
        测试回调
         */
        int check = JnaHmi.JnaLibrary.INSTANCE.adm_sysi4hmi_checkInit(new JnaHmi.JnaLibrary.CheckInitInfo.ByValue(new JnaHmi.JnaLibrary.CheckResultNotifyCallback() {
            @Override
            public void callback(int eCmdidType, String sessionId, String state, Pointer pstPolicyInfo, int errorCode) {
                Log.e(TAG, "adm_sysi4hmi_checkInit: eCmdidType=" + eCmdidType + ", sessionId=" + sessionId + ", state=" + state + ", pstPolicyInfo=" + pstPolicyInfo.toString() + ", errorCode=" + errorCode);
            }
        }));
        Log.e(TAG, "adm_sysi4hmi_checkInit=" + check);
        /*
        测试调用普通方法，返回int
         */
        int max = JnaHmi.JnaLibrary.INSTANCE.adm_sysi4hmi_triggerCheck(check);
        Log.e(TAG, "adm_sysi4hmi_triggerCheck=" + max);
    }
}
