package com.chang.android.jna.demo;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 * Description: JNA invoke hmi so.
 *
 * JNA 直接调用动态库杉树
 * 1.
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
        int adm_sysi4hmi_checkInit(CheckInitInfo initInfo);
        int adm_sysi4hmi_triggerCheck(int a);

        @Structure.FieldOrder({"callback"})
        class CheckInitInfo extends Structure {

            public CheckResultNotifyCallback callback;

            public CheckInitInfo(CheckResultNotifyCallback callback) {
                this.callback = callback;
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

        interface CheckResultNotifyCallback extends com.sun.jna.Callback {
            void callback(int eCmdidType, String sessionId, String state, Pointer pstPolicyInfo, int errorCode);
        }
    }
}
