package com.chang.android.jna.demo;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.ByReference;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

import java.util.Arrays;
import java.util.List;

/**
 * Description: JNA invoke hmi so.
 *
 * JNA 直接调用动态链接库
 *
 * 注意
 * 1. Structure子类中的公共字段的顺序，必须与C语言中的结构的顺序一致。否则会有问题！（如果JNA中的次序和C中的次序相反，那么不会报错，但是数据将会被传递到错误的字段中去。）
 * 2. Structure类有两个内部接口Structure.ByReference和Structure.ByValue。这两个接口仅仅是标记。如果一个类实现Structure.ByReference接口，就表示这个类代表结构体指针。如果一个类实现Structure.ByValue接口，就表示这个类代表结构体本身。
 * 3. 只要涉及到结构体的传递，必须使用ByReference或者ByValue中的一种
 * 4. 指针和引用的传递使用ByReference
 * 5. 拷贝参数传递使用ByValue
 *
 * <p>
 * Created by Nicholas Sean on 2021/5/21 10:24 AM.
 *
 * @version 1.0
 */
public class JnaHmi {

    /******************* 以下为 JNA 直接调用动态库函数 *****************************/
    public interface JnaLibrary extends Library {
        // 获取 JNA 实例
        JnaLibrary INSTANCE = Native.load("sysi4hmi", JnaLibrary.class);

        // 声明动态库中的函数(名字一样)
        int adm_sysi4hmi_checkInit(CheckInitInfo initInfo); // 用于初始化SOA-Check模块

        /**
         * 请求UC触发检测版本
         * @return eActionType. Whether the requested trigger action is allowed. 0:successed, Non-0:failed
         */
        int adm_sysi4hmi_triggerCheck();

        /**
         * 查询版本检测的结果
         * @return
         */
        int adm_sysi4hmi_queryCheckResult(IntByReference eCmdidType, PointerByReference sessionId, PointerByReference state, PstPolicyInfo ppstPolicyInfo, IntByReference errorCode);

        @Structure.FieldOrder({"checkResultCallback"})
        class CheckInitInfo extends Structure {

            public CheckResultNotifyCallback checkResultCallback;

            public CheckInitInfo(CheckResultNotifyCallback callback) {
                this.checkResultCallback = callback;
            }

            // 结构体的引用
            static class ByReference extends CheckInitInfo implements Structure.ByReference {
                public ByReference(CheckResultNotifyCallback callback) {
                    super(callback);
                }
            }

            // 结构体对象
            static class ByValue extends CheckInitInfo implements Structure.ByValue {
                public ByValue(CheckResultNotifyCallback callback) {
                    super(callback);
                }
            }

            // 该方法与类上的注解一样，只写一边就行
            //            @Override
            //            protected List<String> getFieldOrder() {
            //                return Arrays.asList("callback");
            //            }
        }

        // 通过回调函数实现原生代码调用Java代码
        interface CheckResultNotifyCallback extends com.sun.jna.Callback {
            /**
             *
             * @param eCmdidType 检测触发器类型
             * @param sessionId The session id of the platform response
             * @param state Task_State: Task_Check_Successed/Task_Check_Failed
             * @param pstPolicyInfo HMI display strategy file information structure.
             * @param errorCode Error code of the reason for failure, 0 for success
             */
            void invoke(int eCmdidType, String sessionId, String state, Pointer pstPolicyInfo, int errorCode);
        }

        /**
         * MI display strategy file information structure.
         */
        class PstPolicyInfo extends Structure {
            public String policyFileUrl; // Download address of the policy file
            public long policyFileSize; // Size of the policy file, unit:Byte
            public String policyFileHash; // Hash of the policy file(SHA256 sum

            // 这个类代表结构体指针
            static class ByReference extends PstPolicyInfo implements Structure.ByReference {}
            // 这个类代表结构体本身
            static class ByValue extends PstPolicyInfo implements Structure.ByValue {}

            @Override
            protected List<String> getFieldOrder() {
                return Arrays.asList("policyFileUrl", "policyFileSize", "policyFileHash");
            }

            @Override
            public String toString() {
                return "PstPolicyInfo{" + "policyFileUrl='" + policyFileUrl + '\'' + ", policyFileSize=" + policyFileSize + ", policyFileHash='" + policyFileHash + '\'' + '}';
            }
        }
    }
}
