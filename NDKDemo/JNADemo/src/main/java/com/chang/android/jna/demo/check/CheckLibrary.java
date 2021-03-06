package com.chang.android.jna.demo.check;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;
/**
 * JNA Wrapper for library <b>check</b><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public interface CheckLibrary extends Library {
	public static final String JNA_LIBRARY_NAME = "sysi4hmi";
	public static final CheckLibrary INSTANCE = Native.load(CheckLibrary.JNA_LIBRARY_NAME, CheckLibrary.class);
	public static class ADM_SYSI4HMI_CHECK_HMI_POLICY_INFO_T extends Structure {
		/**
		 * < Download address of the policy file<br>
		 * C type : DCHAR*
		 */
		public CheckLibrary.DCHAR policyFileUrl;
		/**
		 * < Size of the policy file, unit:Byte<br>
		 * C type : D64S
		 */
		public CheckLibrary.D64S policyFileSize;
		/**
		 * < Hash of the policy file(SHA256 sum)<br>
		 * C type : DCHAR*
		 */
		public CheckLibrary.DCHAR policyFileHash;
		public ADM_SYSI4HMI_CHECK_HMI_POLICY_INFO_T() {
			super();
		}
		protected List<String> getFieldOrder() {
			return Arrays.asList("policyFileUrl", "policyFileSize", "policyFileHash");
		}
		/**
		 * @param policyFileUrl < Download address of the policy file<br>
		 * C type : DCHAR*<br>
		 * @param policyFileSize < Size of the policy file, unit:Byte<br>
		 * C type : D64S<br>
		 * @param policyFileHash < Hash of the policy file(SHA256 sum)<br>
		 * C type : DCHAR*
		 */
		public ADM_SYSI4HMI_CHECK_HMI_POLICY_INFO_T(CheckLibrary.DCHAR policyFileUrl, CheckLibrary.D64S policyFileSize, CheckLibrary.DCHAR policyFileHash) {
			super();
			this.policyFileUrl = policyFileUrl;
			this.policyFileSize = policyFileSize;
			this.policyFileHash = policyFileHash;
		}
		public ADM_SYSI4HMI_CHECK_HMI_POLICY_INFO_T(Pointer peer) {
			super(peer);
		}
		public static class ByReference extends ADM_SYSI4HMI_CHECK_HMI_POLICY_INFO_T implements Structure.ByReference {

		};
		public static class ByValue extends ADM_SYSI4HMI_CHECK_HMI_POLICY_INFO_T implements Structure.ByValue {

		};
	};
	public static class ADM_SYSI4HMI_CHECK_INIT_INFO_T extends Structure {
		/**
		 * < Processing the result information returned by the check.<br>
		 * C type : ADM_SYSI4HMI_CHECK_RESULT_NOTIFY_CALLBACK_F
		 */
		public CheckLibrary.ADM_SYSI4HMI_CHECK_RESULT_NOTIFY_CALLBACK_F checkResultCallback;
		public ADM_SYSI4HMI_CHECK_INIT_INFO_T() {
			super();
		}
		protected List<String> getFieldOrder() {
			return Arrays.asList("checkResultCallback");
		}
		/**
		 * @param checkResultCallback < Processing the result information returned by the check.<br>
		 * C type : ADM_SYSI4HMI_CHECK_RESULT_NOTIFY_CALLBACK_F
		 */
		public ADM_SYSI4HMI_CHECK_INIT_INFO_T(CheckLibrary.ADM_SYSI4HMI_CHECK_RESULT_NOTIFY_CALLBACK_F checkResultCallback) {
			super();
			this.checkResultCallback = checkResultCallback;
		}
		public ADM_SYSI4HMI_CHECK_INIT_INFO_T(Pointer peer) {
			super(peer);
		}
		public static class ByReference extends ADM_SYSI4HMI_CHECK_INIT_INFO_T implements Structure.ByReference {

		};
		public static class ByValue extends ADM_SYSI4HMI_CHECK_INIT_INFO_T implements Structure.ByValue {

		};
	};
	public interface ADM_SYSI4HMI_CHECK_RESULT_NOTIFY_CALLBACK_F extends Callback {
		int apply(CheckLibrary.ADM_SYSI4HMI_CMDID_TYPE_E eCmdidType, CheckLibrary.DCHAR sessionId, CheckLibrary.DCHAR state, CheckLibrary.ADM_SYSI4HMI_CHECK_HMI_POLICY_INFO_T pstPolicyInfo, int errorCode, Pointer priv);
	};
	/**
	 * @brief User click to trigger version check.<br>
	 * <br>
	 * @param eActionType   [OUT]   ADM_SYSI4HMI_ACTION_AUTH_TYPE_E*    Whether the requested trigger action is allowed<br>
	 * @return D32S     0:successed, Non-0:failed<br>
	 * Original signature : <code>int adm_sysi4hmi_triggerCheck(ADM_SYSI4HMI_ACTION_AUTH_TYPE_E*)</code><br>
	 * <i>native declaration : line 30</i>
	 */
	int adm_sysi4hmi_triggerCheck(CheckLibrary.ADM_SYSI4HMI_ACTION_AUTH_TYPE_E eActionType);
	/**
	 * @brief Request to query the detected result information.<br>
	 * <br>
	 * @param eCmdidType        [OUT]    ADM_SYSI4HMI_CMDID_TYPE_E*    Type of detection trigger.<br>
	 * @param sessionId         [IN]    DCHAR**    The session id of the platform response<br>
	 * @param state             [OUT]    DCHAR**    Task_State: Task_Check_Successed/Task_Check_Failed<br>
	 * @param pstPolicyInfo     [OUT]    ADM_SYSI4HMI_CHECK_HMI_POLICY_INFO_T**  HMI display strategy file information structure.<br>
	 * @param errorCode         [OUT]    D32S*  Error code of the reason for failure, 0 for success<br>
	 * @return  D32S                            0:successed, Non-0:failed<br>
	 * Original signature : <code>int adm_sysi4hmi_queryCheckResult(ADM_SYSI4HMI_CMDID_TYPE_E*, DCHAR**, DCHAR**, ADM_SYSI4HMI_CHECK_HMI_POLICY_INFO_T**, int*)</code><br>
	 * <i>native declaration : line 66</i><br>
	 * @deprecated use the safer methods {@link #adm_sysi4hmi_queryCheckResult(check.CheckLibrary.ADM_SYSI4HMI_CMDID_TYPE_E, check.CheckLibrary.DCHAR[], check.CheckLibrary.DCHAR[], check.CheckLibrary.ADM_SYSI4HMI_CHECK_HMI_POLICY_INFO_T.ByReference[], java.nio.IntBuffer)} and {@link #adm_sysi4hmi_queryCheckResult(check.CheckLibrary.ADM_SYSI4HMI_CMDID_TYPE_E, check.CheckLibrary.DCHAR[], check.CheckLibrary.DCHAR[], check.CheckLibrary.ADM_SYSI4HMI_CHECK_HMI_POLICY_INFO_T.ByReference[], com.sun.jna.ptr.IntByReference)} instead
	 */
	@Deprecated
	int adm_sysi4hmi_queryCheckResult(CheckLibrary.ADM_SYSI4HMI_CMDID_TYPE_E eCmdidType, PointerByReference sessionId, PointerByReference state, PointerByReference ppstPolicyInfo, IntByReference errorCode);
	/**
	 * @brief Request to query the detected result information.<br>
	 * <br>
	 * @param eCmdidType        [OUT]    ADM_SYSI4HMI_CMDID_TYPE_E*    Type of detection trigger.<br>
	 * @param sessionId         [IN]    DCHAR**    The session id of the platform response<br>
	 * @param state             [OUT]    DCHAR**    Task_State: Task_Check_Successed/Task_Check_Failed<br>
	 * @param pstPolicyInfo     [OUT]    ADM_SYSI4HMI_CHECK_HMI_POLICY_INFO_T**  HMI display strategy file information structure.<br>
	 * @param errorCode         [OUT]    D32S*  Error code of the reason for failure, 0 for success<br>
	 * @return  D32S                            0:successed, Non-0:failed<br>
	 * Original signature : <code>int adm_sysi4hmi_queryCheckResult(ADM_SYSI4HMI_CMDID_TYPE_E*, DCHAR**, DCHAR**, ADM_SYSI4HMI_CHECK_HMI_POLICY_INFO_T**, int*)</code><br>
	 * <i>native declaration : line 66</i>
	 */
	int adm_sysi4hmi_queryCheckResult(CheckLibrary.ADM_SYSI4HMI_CMDID_TYPE_E eCmdidType, CheckLibrary.DCHAR sessionId[], CheckLibrary.DCHAR state[], CheckLibrary.ADM_SYSI4HMI_CHECK_HMI_POLICY_INFO_T.ByReference ppstPolicyInfo[], IntBuffer errorCode);
	/**
	 * @brief Request to query the detected result information.<br>
	 * <br>
	 * @param eCmdidType        [OUT]    ADM_SYSI4HMI_CMDID_TYPE_E*    Type of detection trigger.<br>
	 * @param sessionId         [IN]    DCHAR**    The session id of the platform response<br>
	 * @param state             [OUT]    DCHAR**    Task_State: Task_Check_Successed/Task_Check_Failed<br>
	 * @param pstPolicyInfo     [OUT]    ADM_SYSI4HMI_CHECK_HMI_POLICY_INFO_T**  HMI display strategy file information structure.<br>
	 * @param errorCode         [OUT]    D32S*  Error code of the reason for failure, 0 for success<br>
	 * @return  D32S                            0:successed, Non-0:failed<br>
	 * Original signature : <code>int adm_sysi4hmi_queryCheckResult(ADM_SYSI4HMI_CMDID_TYPE_E*, DCHAR**, DCHAR**, ADM_SYSI4HMI_CHECK_HMI_POLICY_INFO_T**, int*)</code><br>
	 * <i>native declaration : line 66</i>
	 */
	int adm_sysi4hmi_queryCheckResult(CheckLibrary.ADM_SYSI4HMI_CMDID_TYPE_E eCmdidType, CheckLibrary.DCHAR sessionId[], CheckLibrary.DCHAR state[], CheckLibrary.ADM_SYSI4HMI_CHECK_HMI_POLICY_INFO_T.ByReference ppstPolicyInfo[], IntByReference errorCode);
	/**
	 * @brief The SYSI4HMI-CHECK module initializes the interface.<br>
	 * @note  adm_sysi4hmi_check to release the initialization information<br>
	 * <br>
	 * @param initInfo          [IN]    const ADM_SYSI4HMI_CHECK_INIT_INFO_T*       The SYSI4HMI-CHECK initialization structure<br>
	 * @param priv          [IN/OUT]    void*                               Reserved parameters<br>
	 * @return D32S         0:successed, Non-0:failed<br>
	 * Original signature : <code>int adm_sysi4hmi_checkInit(const ADM_SYSI4HMI_CHECK_INIT_INFO_T*, void*)</code><br>
	 * <i>native declaration : line 86</i>
	 */
	int adm_sysi4hmi_checkInit(CheckLibrary.ADM_SYSI4HMI_CHECK_INIT_INFO_T initInfo, Pointer priv);
	/**
	 * @brief Release initialization information<br>
	 * @note Used to release the information initialized by the adm_sysi4hmi_checkInit function<br>
	 * Original signature : <code>int adm_sysi4hmi_check()</code><br>
	 * <i>native declaration : line 92</i>
	 */
	int adm_sysi4hmi_check();
	public static class DCHAR extends PointerType {
		public DCHAR(Pointer address) {
			super(address);
		}
		public DCHAR() {
			super();
		}

		public String getValue() {
			return getPointer().getString(0);
		}
	};
	public static class ADM_SYSI4HMI_CMDID_TYPE_E extends PointerType {
		public ADM_SYSI4HMI_CMDID_TYPE_E(Pointer address) {
			super(address);
		}
		public ADM_SYSI4HMI_CMDID_TYPE_E() {
			super();
		}

		public int getValue() {
			return getPointer().getInt(0);
		}
	};
	public static class ADM_SYSI4HMI_ACTION_AUTH_TYPE_E extends PointerType {
		public ADM_SYSI4HMI_ACTION_AUTH_TYPE_E(Pointer address) {
			super(address);
		}
		public ADM_SYSI4HMI_ACTION_AUTH_TYPE_E() {
			super();
		}

		public int getValue() {
			return getPointer().getInt(0);
		}
	};
	public static class D64S extends PointerType {
		public D64S(Pointer address) {
			super(address);
		}
		public D64S() {
			super();
		}

		public Byte getValue() {
			return getPointer().getByte(0);
		}
	};
}
