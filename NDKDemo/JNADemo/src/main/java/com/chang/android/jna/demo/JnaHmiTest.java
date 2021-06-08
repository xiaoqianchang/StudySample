package com.chang.android.jna.demo;

import android.util.Log;

import com.chang.android.jna.demo.check.CheckLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

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
            public void invoke(int eCmdidType, String sessionId, String state, Pointer pstPolicyInfo, int errorCode) {
                Log.e(TAG, "adm_sysi4hmi_checkInit: eCmdidType=" + eCmdidType + ", sessionId=" + sessionId + ", state=" + state + ", pstPolicyInfo=" + pstPolicyInfo.getString(0) + ", errorCode=" + errorCode);
            }
        }));
        Log.e(TAG, "adm_sysi4hmi_checkInit=" + check);
        /*
        测试调用普通方法，返回int
         */
        int max = JnaHmi.JnaLibrary.INSTANCE.adm_sysi4hmi_triggerCheck();
        Log.e(TAG, "adm_sysi4hmi_triggerCheck=" + max);

        // 查询版本检测结果
        IntByReference eCmdidType = new IntByReference();
        PointerByReference sessionId = new PointerByReference();
        PointerByReference state = new PointerByReference();
        JnaHmi.JnaLibrary.PstPolicyInfo ppstPolicyInfo = new JnaHmi.JnaLibrary.PstPolicyInfo();
        IntByReference errorCode = new IntByReference();
        int checkResult = JnaHmi.JnaLibrary.INSTANCE.adm_sysi4hmi_queryCheckResult(eCmdidType, sessionId, state, ppstPolicyInfo, errorCode);
        Log.e(TAG, "adm_sysi4hmi_queryCheckResult=" + checkResult +
                ", eCmdidType=" + eCmdidType.getValue() +
                ", sessionId=" + sessionId.getValue().getString(0) +
                ", state=" + state.getValue() +
                ", ppstPolicyInfo=" + ppstPolicyInfo.toString() +
                ", errorCode=" + errorCode.getValue());
    }

    public void testHmiCheckMethod() {
        /*
        测试回调
         */
        int check1 = CheckLibrary.INSTANCE.adm_sysi4hmi_check();
        Log.e(TAG, "adm_sysi4hmi_check:" + check1);

//        int check = CheckLibrary.INSTANCE.adm_sysi4hmi_checkInit(new CheckLibrary.ADM_SYSI4HMI_CHECK_INIT_INFO_T((eCmdidType, sessionId, state, pstPolicyInfo, errorCode, priv) -> {
//            Log.e(TAG, "adm_sysi4hmi_checkInit callback: eCmdidType=" + eCmdidType +
//                    ", sessionId=" + sessionId.getValue() +
//                    ", state=" + state.getValue() +
//                    ", pstPolicyInfo={policyFileUrl: " + pstPolicyInfo.policyFileUrl + ", policyFileSize: " + pstPolicyInfo.policyFileSize + ", policyFileHash: " + pstPolicyInfo.policyFileHash + "}" +
//                    ", errorCode=" + errorCode);
//            return 0;
//        }), Pointer.NULL);
//        Log.e(TAG, "adm_sysi4hmi_checkInit:" + check);

        /*
        测试调用普通方法，返回int
         */
        CheckLibrary.ADM_SYSI4HMI_ACTION_AUTH_TYPE_E eActionType = new CheckLibrary.ADM_SYSI4HMI_ACTION_AUTH_TYPE_E();
        int triggerCheck = CheckLibrary.INSTANCE.adm_sysi4hmi_triggerCheck(eActionType);
        Log.e(TAG, "adm_sysi4hmi_triggerCheck: ret=" + triggerCheck + ", output param eActionType=" + eActionType);

        // 查询版本检测结果
        CheckLibrary.ADM_SYSI4HMI_CMDID_TYPE_E eCmdidType = new CheckLibrary.ADM_SYSI4HMI_CMDID_TYPE_E();
        PointerByReference sessionId = new PointerByReference();
        PointerByReference state = new PointerByReference();
        PointerByReference ppstPolicyInfo = new PointerByReference();
        IntByReference errorCode = new IntByReference();
        int checkResult = CheckLibrary.INSTANCE.adm_sysi4hmi_queryCheckResult(eCmdidType, sessionId, state, ppstPolicyInfo, errorCode);
        Log.e(TAG, "adm_sysi4hmi_queryCheckResult: ret=" + checkResult +
                ", eCmdidType=" + eCmdidType +
                ", sessionId=" + sessionId.getValue().getString(0) +
                ", state=" + state.getValue() +
                ", ppstPolicyInfo=" + ppstPolicyInfo.toString() +
                ", errorCode=" + errorCode.getValue());

    }
}
