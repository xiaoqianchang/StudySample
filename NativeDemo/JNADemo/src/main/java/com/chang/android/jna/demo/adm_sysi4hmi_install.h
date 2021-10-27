/**
 * @file adm_sysi4hmi_install.h
 * @author mengwei@abupdate.com
 * @brief 通过HMI与主控模块做install交互的接口定义，包括触发安装、安装进度显示、安装结果显示、查询安装进度、查询安装结果等。
 * @version 0.1
 * @date 2021-05-08
 * 
 * @copyright Copyright © 2018 All rights 上海艾拉比智能科技有限公司
 * 
 */
#ifndef __ADM_SYSI4HMI_INSTALL_H__
#define __ADM_SYSI4HMI_INSTALL_H__

#ifdef __cplusplus
extern "C" {
#endif

#define EXPORT_FUN __attribute__((visibility("default")))

#include "adm_typedefs.h"
#include "adm_sysi4hmi_common.h"

/***************************************************************************Schdule install*********************************************************************************/
/**
 * @brief The user triggers the setting of the scheduled installation time.
 * 
 * @param eCmdidType        [IN]    const ADM_SYSI4HMI_CMDID_TYPE_E    Type of detection trigger.
 * @param eActionType       [OUT]   ADM_SYSI4HMI_ACTION_AUTH_TYPE_E*    Whether the requested trigger action is allowed
 * @param schduleTime       [IN]    const D16U  Schdule install timestamp, unit: second
 * @return  D32S                            0:successed, Non-0:failed
 */
extern D32S adm_sysi4hmi_schduleInstall(const ADM_SYSI4HMI_CMDID_TYPE_E eCmdidType, ADM_SYSI4HMI_ACTION_AUTH_TYPE_E* eActionType, const D16U schduleTime);
/***************************************************************************Schdule install*********************************************************************************/

/***************************************************************************Cancel install*********************************************************************************/
/**
 * @brief User triggers to cancel installation.
 * 
 * @param eCmdidType        [IN]    const ADM_SYSI4HMI_CMDID_TYPE_E    Type of detection trigger.
 * @param eActionType       [OUT]   ADM_SYSI4HMI_ACTION_AUTH_TYPE_E*    Whether the requested trigger action is allowed
 * @return  D32S                            0:successed, Non-0:failed
 */
extern D32S adm_sysi4hmi_cancelInstall(const ADM_SYSI4HMI_CMDID_TYPE_E eCmdidType, ADM_SYSI4HMI_ACTION_AUTH_TYPE_E* eActionType);
/***************************************************************************Cancel install*********************************************************************************/


/***************************************************************************Trigger install*********************************************************************************/
/**
 * @brief User click to trigger install callback processing function.
 * 
 * @param eActionType                       [OUT]       ADM_SYSI4HMI_ACTION_AUTH_TYPE_E*     Whether the requested trigger action is allowed
 * @return  D32S                            0:successed, Non-0:failed
 */
extern D32S adm_sysi4hmi_triggerInstall(ADM_SYSI4HMI_ACTION_AUTH_TYPE_E* eActionType);
/***************************************************************************Trigger install*********************************************************************************/



/***************************************************************************Trigger verify conditions************************************************************************/
/**
 * @brief Trigger condition detection
 * 
 * @param eVerifiedType     [IN]    const ADM_SYSI4HMI_CONDITIONS_VERIFIED_TYPE_E       Types of condition detection
 * @param cycleTime         [IN]    const D16U  Condition detection cycle, unit: Second, when everifiedType = ADM_SYSI4HMI_INSTALL_CONDITIONS_VERIFIED_TYPE_CYCLE
 * @param timeout           [IN]    const D16U  Period detection condition timeout, unit:Second, when everifiedType = ADM_SYSI4HMI_INSTALL_CONDITIONS_VERIFIED_TYPE_CYCLE
 * @param errorCode         [OUT]   D32S*   Error code of triggering verify conditions.
 * @return D32S             0:successed, Non-0:failed
 */
extern D32S adm_sysi4hmi_installConditionsVerified(const ADM_SYSI4HMI_CONDITIONS_VERIFIED_TYPE_E eVerifiedType, const D16U cycleTime, const D16U timeout, D32S* errorCode);

// TODO: error code to be defined
/**
 * @brief Notification of verification results of install conditions.
 * 
 * @param eCmdidType        [IN]    const ADM_SYSI4HMI_CMDID_TYPE_E    Type of detection trigger.
 * @param state             [IN]    const DCHAR*    Task_State: Task_Install_Prepare
 * @param condsArrarList    [IN]    const ADM_SYSI4HMI_CONDITION_DATA_ARRAY_T*   Condition value data array 
 * @param verifyResult      [IN]    const D32S  Conditional check result, 0:successed, Non-0: error code
 * @param priv                              [IN/OUT]    void*                               Reserved parameters
 * @return D32S             0:successed, Non-0:failed 
 */
typedef D32S (*ADM_SYSI4HMI_INSTALL_CONDITIONS_VERIFIED_RESULT_NOTIFY_CALLBACK_F)(const ADM_SYSI4HMI_CMDID_TYPE_E eCmdidType, const DCHAR* state, const ADM_SYSI4HMI_CONDITION_DATA_ARRAY_T* condsArrarList, const D32S verifyResult, void* priv);
/***************************************************************************Trigger verify conditions************************************************************************/


/***************************************************************************Notify policy install progress******************************************************************/
/**
 * @brief Definition of the progress information structure of the upgrade package install.
 * 
 */
typedef struct {
    D16U progress;          /**!< Upgrade progress value */
} ADM_SYSI4HMI_INSTALL_PROGRESS_INFO_T;

/**
 * @brief Policy install progress notification.
 * 
 * @param eCmdidType        [IN]    const ADM_SYSI4HMI_CMDID_TYPE_E    Type of detection trigger.
 * @param state             [IN]    const DCHAR*    Task_State: Task_Install_Ing
 * @param pstPolicyInstallProgress [IN]    const ADM_SYSI4HMI_INSTALL_PROGRESS_INFO_T*     The progress information structure of the upgrade package install.
 * @param priv                              [IN/OUT]    void*                               Reserved parameters
 * @return D32S             0:successed, Non-0:failed 
 */
typedef D32S (*ADM_SYSI4HMI_POLICY_INSTALL_PROGRESS_NOTIFY_CALLBACK_F)(const ADM_SYSI4HMI_CMDID_TYPE_E eCmdidType, const DCHAR* state, const ADM_SYSI4HMI_INSTALL_PROGRESS_INFO_T* pstPolicyInstallProgress, void* priv);
/***************************************************************************Notify policy install progress******************************************************************/


/***************************************************************************Notify policy install result********************************************************************/
/**
 * @brief Definition of the result information structure of the upgrade package install.
 * 
 */
typedef struct {
    D32S result;                                                /**!< The result of the remote module installing the upgrade package */
    DCHAR*  expectVal;                                          /**!< Description of the reason for the failure, expected value */
    DCHAR*  realVal;                                            /**!< Description of the reason for the failure, real value */
} ADM_SYSI4HMI_INSTALL_RESULT_INFO_T;

/**
 * @brief Policy install result notification.
 * 
 * @param eCmdidType        [IN]    const ADM_SYSI4HMI_CMDID_TYPE_E    Type of detection trigger.
 * @param state             [IN]    const DCHAR*    Task_State: Task_Install_Successed/Task_Install_Failed/Task_Install_Paused/Task_Install_Completed
 * @param pstPolicyInstallResult   [IN]    const ADM_SYSI4HMI_INSTALL_RESULT_INFO_T*   The result information structure of the upgrade package install.
 * @param priv                              [IN/OUT]    void*                               Reserved parameters
 * @return D32S             0:successed, Non-0:failed  
 */
typedef D32S (*ADM_SYSI4HMI_POLICY_INSTALL_RESULT_NOTIFY_CALLBACK_F)(const ADM_SYSI4HMI_CMDID_TYPE_E eCmdidType, const DCHAR* state, const ADM_SYSI4HMI_INSTALL_RESULT_INFO_T* pstPolicyInstallResult, void* priv);
/***************************************************************************Notify policy install result********************************************************************/


/**
 * @brief Identifies the install progress information of the ECU
 * 
 */
typedef struct {
    DCHAR*  state;                                                  /**!< Ecu_state: Ecu_Install_Ing/Ecu_Install_Finished*/
} ADM_UIM_SOA_INSTALL_PROGRESS_INFO_T;


/**
 * @brief Identifies the install result information of the ECU
 * 
 */
typedef struct {
    DCHAR*  state;                                                  /**!< Ecu_state: Ecu_Install_Ing/Ecu_Install_Finished*/
} ADM_UIM_SOA_INSTALL_RESULT_INFO_T;


/**
 * @brief Identifies the install progress information of the ECU
 * 
 */
typedef struct {
    ADM_SYSI4HMI_ECU_IDEN_INFO_T* pstEcuIdenInfo;                    /**!< Identification information of ECU upgrade package install */
    DCHAR*  state;                                                  /**!< Ecu_state: Ecu_Install_Ing/Ecu_Install_Finished*/
    ADM_SYSI4HMI_INSTALL_PROGRESS_INFO_T* pstEcuInstallProgress;   /**!< The progress information structure of the upgrade package install. */
} ADM_SYSI4HMI_ECU_INSTALL_PROGRESS_INFO_T;

/**
 * @brief ECU progress information array structure definition
 * 
 */
typedef struct {
    ADM_SYSI4HMI_ECU_INSTALL_PROGRESS_INFO_T** ecuInstallProgressArray;    /**!< ECU progress information array */
    D16U    ecuInstallProgressArrayLength;                                 /**!< Length of ECU progress information array */
} ADM_SYSI4HMI_ECU_INSTALL_PROGRESS_ARRAY_INFO_T;

/**
 * @brief Identifies the install result information of the ECU
 * 
 */
typedef struct {
    ADM_SYSI4HMI_ECU_IDEN_INFO_T* pstEcuIdenInfo;                    /**!< Identification information of ECU upgrade package install */
    DCHAR*  state;                                                  /**!< Ecu_state: Ecu_Install_Finished*/
    ADM_SYSI4HMI_INSTALL_RESULT_INFO_T* pstEcuInstallResult;       /**!< The result information structure of the upgrade package install. */
} ADM_SYSI4HMI_ECU_INSTALL_RESULT_INFO_T;



typedef struct {
    ADM_SYSI4HMI_ECU_INSTALL_RESULT_INFO_T** ecuInstallResultArray;    /**!< ECU result information array */
    D16U    ecuInstallResultArrayLength;                               /**!< Length of ECU result information array */
} ADM_SYSI4HMI_ECU_INSTALL_RESULT_ARRAY_INFO_T;

/***************************************************************************Notify one ECU install progress & result ********************************************************************/
/**
 * @brief Ecu install progress notification.
 * 
 * @param eCmdidType        [IN]    const ADM_SYSI4HMI_CMDID_TYPE_E    Type of detection trigger.
 * @param sessionId         [IN]    const D16U        The sessionId of the strategy
 * @param pstEcuInstallProgressInfo    [IN]    const ADM_SYSI4HMI_ECU_INSTALL_PROGRESS_INFO_T*      Identifies the install progress information of the ECU.
 * @param priv                              [IN/OUT]    void*                               Reserved parameters
 * @return D32S             0:successed, Non-0:failed
 */
typedef D32S (*ADM_SYSI4HMI_ECU_INSTALL_PROGRESS_NOTIFY_CALLBACK_F)(const ADM_SYSI4HMI_CMDID_TYPE_E eCmdidType, const D16U sessionId, const ADM_SYSI4HMI_ECU_INSTALL_PROGRESS_INFO_T* pstEcuInstallProgressInfo, void* priv);

/**
 * @brief Ecu install result notification.
 * 
 * @param eCmdidType        [IN]    const ADM_SYSI4HMI_CMDID_TYPE_E    Type of detection trigger.
 * @param sessionId         [IN]    const D16U        The sessionId of the strategy
 * @param pstEcuInstallResultInfo  [IN]    const ADM_SYSI4HMI_ECU_INSTALL_RESULT_INFO_T*   Identifies the install result information of the ECU.
 * @param priv                              [IN/OUT]    void*                               Reserved parameters
 * @return D32S             0:successed, Non-0:failed 
 */
typedef D32S (*ADM_SYSI4HMI_ECU_INSTALL_RESULT_NOTIFY_CALLBACK_F)(const ADM_SYSI4HMI_CMDID_TYPE_E eCmdidType, const D16U sessionId, const ADM_SYSI4HMI_ECU_INSTALL_RESULT_INFO_T* pstEcuInstallResultInfo, void* priv);
/***************************************************************************Notify one ECU install progress & result ********************************************************************/


/***************************************************************************Notify all ECU install progress & result ********************************************************************/
/**
 * @brief All Ecu install progress notification.
 * 
 * @param eCmdidType        [IN]    const ADM_SYSI4HMI_CMDID_TYPE_E    Type of detection trigger.
 * @param sessionId         [IN]    const D16U          The sessionId of the strategy
 * @param state             [IN]    const DCHAR*        Task_State: Task_Install_Ing  
 * @param pstEcuInstallProgressArrayInfo   [IN] const ADM_SYSI4HMI_ECU_INSTALL_PROGRESS_ARRAY_INFO_T*  ECU progress information array structure.
 * @param pstPolicyInstallProgress [IN]    const ADM_UIM_SOA_INSTALL_PROGRESS_INFO_T*     The progress information structure of the upgrade package install.
 * @return D32S             0:successed, Non-0:failed 
 */
typedef D32S (*ADM_SYSI4HMI_ALL_ECU_INSTALL_PROGRESS_NOTIFY_CALLBACK_F)(const ADM_SYSI4HMI_CMDID_TYPE_E eCmdidType, const D16U sessionId, const DCHAR* state, const ADM_SYSI4HMI_ECU_INSTALL_PROGRESS_ARRAY_INFO_T* pstEcuInstallProgressArrayInfo, const ADM_UIM_SOA_INSTALL_PROGRESS_INFO_T* pstPolicyInstallProgress, void* priv);

/**
 * @brief All Ecu install result notification.
 * 
 * @param eCmdidType        [IN]    const ADM_SYSI4HMI_CMDID_TYPE_E    Type of detection trigger.
 * @param sessionId         [IN]    const D16U          The sessionId of the strategy
 * @param state             [IN]    const DCHAR*        Task_State: Task_Install_Paused/Task_Install_Completed/Task_Install_Successed/Task_Install_Failed/Task_Transfer_xxx/Task_Unpack_xxx
 * @param pstEcuInstallResultArrayInfo     [IN] const ADM_SYSI4HMI_ECU_INSTALL_RESULT_ARRAY_INFO_T*    Ecu result information array structure.
 * @param pstPolicyInstallResult   [IN]    const ADM_UIM_SOA_INSTALL_RESULT_INFO_T*   The result information structure of the upgrade package install.
 * @return D32S             0:successed, Non-0:failed
 */
typedef D32S (*ADM_SYSI4HMI_ALL_ECU_INSTALL_RESULT_NOTIFY_CALLBACK_F)(const ADM_SYSI4HMI_CMDID_TYPE_E eCmdidType, const D16U sessionId, const DCHAR* state, const ADM_SYSI4HMI_ECU_INSTALL_RESULT_ARRAY_INFO_T* pstEcuInstallResultArrayInfo, const ADM_UIM_SOA_INSTALL_RESULT_INFO_T* pstPolicyInstallResult, void* priv);
/***************************************************************************Notify all ECU install progress & result ********************************************************************/

/***************************************************************************Query all ECU install progress & result *********************************************************************/
/**
 * @brief Request to query the install progress of all current ECUs.
 * 
 * @param eCmdidType        [OUT]    ADM_SYSI4HMI_CMDID_TYPE_E*    Type of detection trigger.
 * @param sessionId         [OUT]    D16U*          The sessionId of the strategy
 * @param state             [OUT]    DCHAR**        Task_State: Task_Install_Ing  
 * @param pstEcuInstallProgressArrayInfo   [OUT] ADM_SYSI4HMI_ECU_INSTALL_PROGRESS_ARRAY_INFO_T**  ECU progress information array structure.
 * @param pstEcuInstallProgressInfo    [OUT]    ADM_UIM_SOA_INSTALL_PROGRESS_INFO_T**      Identifies the install progress information of the ECU.
 * @return  D32S                            0:successed, Non-0:failed
 */
extern D32S adm_sysi4hmi_queryAllEcuInstallProgress(ADM_SYSI4HMI_CMDID_TYPE_E* eCmdidType, D16U* sessionId, DCHAR** state, ADM_SYSI4HMI_ECU_INSTALL_PROGRESS_ARRAY_INFO_T** pstEcuInstallProgressArrayInfo, ADM_UIM_SOA_INSTALL_PROGRESS_INFO_T** pstPolicyInstallProgress);

/**
 * @brief Request to query the install result of all current ECUs.
 * 
 * @param eCmdidType        [OUT]    ADM_SYSI4HMI_CMDID_TYPE_E*    Type of detection trigger.
 * @param sessionId         [OUT]    D16U*          The sessionId of the strategy
 * @param state             [OUT]    DCHAR**        Task_State: Task_Install_Paused/Task_Install_Completed/Task_Install_Successed/Task_Install_Failed/Task_Transfer_xxx/Task_Unpack_xxx
 * @param pstEcuInstallResultArrayInfo     [OUT] ADM_SYSI4HMI_ECU_INSTALL_RESULT_ARRAY_INFO_T**    Ecu result information array structure.
 * @param pstPolicyInstallResult   [OUT]    ADM_UIM_SOA_INSTALL_RESULT_INFO_T**   The result information structure of the upgrade package install.
 * @return  D32S                            0:successed, Non-0:failed
 */
extern D32S adm_sysi4hmi_queryAllEcuIntallResult(ADM_SYSI4HMI_CMDID_TYPE_E* eCmdidType, D16U* sessionId, DCHAR** state, ADM_SYSI4HMI_ECU_INSTALL_RESULT_ARRAY_INFO_T** pstEcuInstallResultArrayInfo, ADM_UIM_SOA_INSTALL_RESULT_INFO_T** pstPolicyInstallResult);
/***************************************************************************Query all ECU install progress & result *********************************************************************/



/***************************************************************************SYSI4HMI-INSTALL init*****************************************************************************************/
/**
 * @brief The SYSI4HMI-INSTALL initialization structure definition
 * 
 */
typedef struct {
    ADM_SYSI4HMI_INSTALL_CONDITIONS_VERIFIED_RESULT_NOTIFY_CALLBACK_F condsVerifiedResultCallback;      /**!< Notification of verification results of install conditions. */
    ADM_SYSI4HMI_POLICY_INSTALL_PROGRESS_NOTIFY_CALLBACK_F  policyInstallProgressNotifyCallback;        /**!< Policy install progress notification. */
    ADM_SYSI4HMI_POLICY_INSTALL_RESULT_NOTIFY_CALLBACK_F    policyInstallResultNotifyCallback;          /**!< Policy install result notification. */
    ADM_SYSI4HMI_ECU_INSTALL_PROGRESS_NOTIFY_CALLBACK_F ecuInstallProgressNotifyCallback;               /**!< Ecu install progress notification. */
    ADM_SYSI4HMI_ECU_INSTALL_RESULT_NOTIFY_CALLBACK_F   ecuInstallResultNotifyCallback;                 /**!< Ecu install result notification. */
    ADM_SYSI4HMI_ALL_ECU_INSTALL_PROGRESS_NOTIFY_CALLBACK_F allEcuInstallProgressNotifyCallback;        /**!< All Ecu install progress notification. */
    ADM_SYSI4HMI_ALL_ECU_INSTALL_RESULT_NOTIFY_CALLBACK_F   allEcuInstallResultNotifyCallback;          /**!< All Ecu install result notification. */
} ADM_SYSI4HMI_INSTALL_INIT_INFO_T;

/**
 * @brief The SYSI4HMI-INSTALL module initializes the interface
 * @note Use adm_sysi4hmi_installDeinit to release the initialization information
 * 
 * @param pstInitInfo   [IN]        const ADM_SYSI4HMI_INSTALL_INIT_INFO_T*        The SYSI4HMI-INSTALL initialization structure
 * @param priv          [IN/OUT]    void*                               Reserved parameters
 * @return D32S         0:successed, Non-0:failed    
 */
extern D32S adm_sysi4hmi_installInit(const ADM_SYSI4HMI_INSTALL_INIT_INFO_T* initInfo, void* priv);

/**
 * @brief Release initialization information
 * @note Used to release the information initialized by the adm_sysi4hmi_installInit function
 */
extern D32S adm_sysi4hmi_installDeinit();
/***************************************************************************SYSI4HMI-INSTALL init*****************************************************************************************/

#ifdef __cplusplus  // close out "C" linkage in case of c++ compiling
}
#endif


#endif//__ADM_SYSI4HMI_INSTALL_H__
