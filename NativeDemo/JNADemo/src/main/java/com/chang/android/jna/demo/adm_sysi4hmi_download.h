/**
 * @file adm_sysi4hmi_download.h
 * @author mengwei@abupdate.com
 * @brief 通过HMI与主控模块做download交互的接口定义，包括触发下载、下载进度显示、下载结果显示、查询下载进度、查询下载结果等。
 * @version 0.1
 * @date 2021-05-08
 * 
 * @copyright Copyright © 2018 All rights 上海艾拉比智能科技有限公司
 * 
 */
#ifndef __ADM_SYSI4HMI_DOWNLOAD_H__
#define __ADM_SYSI4HMI_DOWNLOAD_H__

#ifdef __cplusplus
extern "C" {
#endif

#define EXPORT_FUN __attribute__((visibility("default")))

#include "adm_typedefs.h"
#include "adm_sysi4hmi_common.h"


/***************************************************************************Trigger download*********************************************************************************/
/**
 * @brief User click to trigger version download.
 * 
 * @param eActionType   [OUT]   ADM_SYSI4HMI_ACTION_AUTH_TYPE_E*    Whether the requested trigger action is allowed
 * @return D32S     0:successed, Non-0:failed 
 */
extern D32S adm_sysi4hmi_triggerDownload(ADM_SYSI4HMI_ACTION_AUTH_TYPE_E* eActionType);
/***************************************************************************Trigger download*********************************************************************************/

/***************************************************************************Cancel download*********************************************************************************/
/**
 * @brief User clicks to trigger to cancel download.
 * 
 * @param eCmdidType        [IN]    const ADM_SYSI4HMI_CMDID_TYPE_E    Type of detection trigger.
 * @param eActionType       [OUT]   ADM_SYSI4HMI_ACTION_AUTH_TYPE_E*    Whether the requested trigger action is allowed
 * @param errorCode         [OUT]   D32S*   Result of cancel download
 * @return  D32S                            0:successed, Non-0:failed
 */
extern D32S adm_sysi4hmi_cancelDownload(const ADM_SYSI4HMI_CMDID_TYPE_E eCmdidType, ADM_SYSI4HMI_ACTION_AUTH_TYPE_E* eActionType, D32S* errorCode);
/***************************************************************************Cancel download*********************************************************************************/

/***************************************************************************Pause download*********************************************************************************/
/**
 * @brief User clicks to trigger to pause download.
 * 
 * @param eCmdidType        [IN]    const ADM_SYSI4HMI_CMDID_TYPE_E    Type of detection trigger.
 * @param eActionType       [OUT]   ADM_SYSI4HMI_ACTION_AUTH_TYPE_E*    Whether the requested trigger action is allowed
 * @param errorCode         [OUT]   D32S*   Result of pause download
 * @return  D32S                            0:successed, Non-0:failed
 */
extern D32S adm_sysi4hmi_pauseDownload(const ADM_SYSI4HMI_CMDID_TYPE_E eCmdidType, ADM_SYSI4HMI_ACTION_AUTH_TYPE_E* eActionType, D32S* errorCode);
/***************************************************************************Pause download*********************************************************************************/

/***************************************************************************Trigger verify conditions************************************************************************/
/**
 * @brief Trigger condition detection
 * 
 * @param eVerifiedType     [IN]    const ADM_SYSI4HMI_CONDITIONS_VERIFIED_TYPE_E       Types of condition detection
 * @param cycleTime         [IN]    const D16U  Condition detection cycle, unit: Second, when everifiedType = ADM_SYSI4HMI_DOWNLOAD_CONDITIONS_VERIFIED_TYPE_CYCLE
 * @param timeout           [IN]    const D16U  Period detection condition timeout, unit:Second, when everifiedType = ADM_SYSI4HMI_DOWNLOAD_CONDITIONS_VERIFIED_TYPE_CYCLE
 * @param errorCode         [OUT]   D32S*   Error code of triggering verify conditions.
 * @return D32S             0:successed, Non-0:failed
 */
extern D32S adm_sysi4hmi_downloadContitionsVerified(const ADM_SYSI4HMI_CONDITIONS_VERIFIED_TYPE_E eVerifiedTYpe, const D16U cycleTime, const D16U timeout, D32S* errorCode);

// TODO: error code to be defined
/**
 * @brief Notification of verification results of download conditions.
 * 
 * @param eCmdidType        [IN]    const ADM_SYSI4HMI_CMDID_TYPE_E    Type of detection trigger.
 * @param state             [IN]    const DCHAR*    Task_State: Task_Download_Prepare
 * @param condsArrarList    [IN]    const ADM_SYSI4HMI_CONDITION_DATA_ARRAY_T*   Condition value data array 
 * @param verifyResult      [IN]    const D32S  Conditional check result, 0:successed, Non-0: error code
 * @param priv                              [IN/OUT]    void*                               Reserved parameters
 * @return D32S             0:successed, Non-0:failed 
 */
typedef D32S (*ADM_SYSI4HMI_DOWNLOAD_CONDITIONS_VERIFIED_RESULT_NOTIFY_CALLBACK_F)(const ADM_SYSI4HMI_CMDID_TYPE_E eCmdidType, const DCHAR* state, const ADM_SYSI4HMI_CONDITION_DATA_ARRAY_T* condsArrarList, const D32S verifyResult, void* priv);
/***************************************************************************Trigger verify conditions************************************************************************/

/***************************************************************************Notify policy download progress******************************************************************/
/**
 * @brief Definition of the progress information structure of the upgrade package download.
 * 
 */
typedef struct {
    D64S    savedSize;                                          /**!< The size of the downloaded upgrade package, unit:byte. */
    D64S    totalSize;                                          /**!< The total size of the upgrade package being downloaded, unit:byte */
    D32S    speed;                                              /**!< Download speed of upgrade package, unit:byte/s */
    D32S    remainTime;                                         /**!< Estimated remaining time to download, unit:second */
} ADM_SYSI4HMI_DOWNLOAD_PROGRESS_INFO_T;

/**
 * @brief Policy download progress notification.
 * 
 * @param eCmdidType        [IN]    const ADM_SYSI4HMI_CMDID_TYPE_E    Type of detection trigger.
 * @param state             [IN]    const DCHAR*    Task_State: Task_Download_Ing
 * @param pstPolicyDownloadProgress [IN]    const ADM_SYSI4HMI_DOWNLOAD_PROGRESS_INFO_T*     The progress information structure of the upgrade package download.
 * @param priv                              [IN/OUT]    void*                               Reserved parameters
 * @return D32S             0:successed, Non-0:failed 
 */
typedef D32S (*ADM_SYSI4HMI_POLICY_DOWNLOAD_PROGRESS_NOTIFY_CALLBACK_F)(const ADM_SYSI4HMI_CMDID_TYPE_E eCmdidType, const DCHAR* state, const ADM_SYSI4HMI_DOWNLOAD_PROGRESS_INFO_T* pstPolicyDownloadProgress, void* priv);
/***************************************************************************Notify policy download progress******************************************************************/

/***************************************************************************Notify policy download result********************************************************************/
/**
 * @brief Definition of the result information structure of the upgrade package download.
 * 
 */
typedef struct {
    D32S result;                                                /**!< The result of the remote module downloading the upgrade package */
    DCHAR*  expectVal;                                          /**!< Description of the reason for the failure, expected value */
    DCHAR*  realVal;                                            /**!< Description of the reason for the failure, real value */
} ADM_SYSI4HMI_DOWNLOAD_RESULT_INFO_T;

/**
 * @brief Policy download result notification.
 * 
 * @param eCmdidType        [IN]    const ADM_SYSI4HMI_CMDID_TYPE_E    Type of detection trigger.
 * @param state             [IN]    const DCHAR*    Task_State: Task_Download_Successed/Task_Download_Failed/Task_Download_Paused/Task_Download_Completed
 * @param pstPolicyDownloadResult   [IN]    const ADM_SYSI4HMI_DOWNLOAD_RESULT_INFO_T*   The result information structure of the upgrade package download.
 * @param priv                              [IN/OUT]    void*                               Reserved parameters
 * @return D32S             0:successed, Non-0:failed  
 */
typedef D32S (*ADM_SYSI4HMI_POLICY_DOWNLOAD_RESULT_NOTIFY_CALLBACK_F)(const ADM_SYSI4HMI_CMDID_TYPE_E eCmdidType, const DCHAR* state, const ADM_SYSI4HMI_DOWNLOAD_RESULT_INFO_T* pstPolicyDownloadResult, void* priv);
/***************************************************************************Notify policy download result********************************************************************/

/**
 * @brief Identifies the download progress information of the ECU
 * 
 */
typedef struct {
    ADM_SYSI4HMI_ECU_IDEN_INFO_T* pstEcuIdenInfo;                    /**!< Identification information of ECU upgrade package download */
    ADM_SYSI4HMI_DOWNLOAD_PROGRESS_INFO_T* pstEcuDownloadProgress;   /**!< The progress information structure of the upgrade package download. */
} ADM_SYSI4HMI_ECU_DOWNLOAD_PROGRESS_INFO_T;

/**
 * @brief ECU progress information array structure definition
 * 
 */
typedef struct {
    ADM_SYSI4HMI_ECU_DOWNLOAD_PROGRESS_INFO_T** ecuDownloadProgressArray;    /**!< ECU progress information array */
    D16U    ecuDownloadProgressArrayLength;                                 /**!< Length of ECU progress information array */
} ADM_SYSI4HMI_ECU_DOWNLOAD_PROGRESS_ARRAY_INFO_T;

/**
 * @brief Identifies the download result information of the ECU
 * 
 */
typedef struct {
    ADM_SYSI4HMI_ECU_IDEN_INFO_T* pstEcuIdenInfo;                    /**!< Identification information of ECU upgrade package download */
    DCHAR*  state;                                                  /**!< Ecu_state: Ecu_Download_Finished*/
    ADM_SYSI4HMI_DOWNLOAD_RESULT_INFO_T* pstEcuDownloadResult;       /**!< The result information structure of the upgrade package download. */
} ADM_SYSI4HMI_ECU_DOWNLOAD_RESULT_INFO_T;



typedef struct {
    ADM_SYSI4HMI_ECU_DOWNLOAD_RESULT_INFO_T** ecuDownloadResultArray;    /**!< ECU result information array */
    D16U    ecuDownloadResultArrayLength;                               /**!< Length of ECU result information array */
} ADM_SYSI4HMI_ECU_DOWNLOAD_RESULT_ARRAY_INFO_T;

/***************************************************************************Notify one ECU download progress & result ********************************************************************/
/**
 * @brief Ecu download progress notification.
 * 
 * @param eCmdidType        [IN]    const ADM_SYSI4HMI_CMDID_TYPE_E    Type of detection trigger.
 * @param sessionId         [IN]    const DCHAR*        The sessionId of the strategy
 * @param state             [IN]    const DCHAR*        The state of Task_State: Task_Download_Ing
 * @param pstEcuIdenInfo    [IN]    const ADM_SYSI4HMI_ECU_IDEN_INFO_T*  Identification information of ECU upgrade package download
 * @param pstEcuDownloadProgress    [IN]    const ADM_SYSI4HMI_DOWNLOAD_PROGRESS_INFO_T*  Download progress of ECU.
 * @param priv                              [IN/OUT]    void*                               Reserved parameters
 * @return D32S             0:successed, Non-0:failed
 */
typedef D32S (*ADM_SYSI4HMI_ECU_DOWNLOAD_PROGRESS_NOTIFY_CALLBACK_F)(const ADM_SYSI4HMI_CMDID_TYPE_E eCmdidType, const D16U sessionId, const DCHAR* state, const ADM_SYSI4HMI_ECU_IDEN_INFO_T* pstEcuIdenInfo, const ADM_SYSI4HMI_DOWNLOAD_PROGRESS_INFO_T* pstEcuDownloadProgress, void* priv);

/**
 * @brief Ecu download result notification.
 * 
 * @param eCmdidType        [IN]    const ADM_SYSI4HMI_CMDID_TYPE_E    Type of detection trigger.
 * @param sessionId         [IN]    const DCHAR*        The sessionId of the strategy
 * @param state             [IN]    const DCHAR*        The state of Task_State: Task_Download_Ing
 * @param pstEcuIdenInfo    [IN]    const ADM_SYSI4HMI_ECU_IDEN_INFO_T*  Identification information of ECU upgrade package download
 * @param pstEcuDownloadResult  [IN]    const ADM_SYSI4HMI_DOWNLOAD_RESULT_INFO_T*   Identifies the download result information of the ECU.
 * @param priv                              [IN/OUT]    void*                               Reserved parameters
 * @return D32S             0:successed, Non-0:failed 
 */
typedef D32S (*ADM_SYSI4HMI_ECU_DOWNLOAD_RESULT_NOTIFY_CALLBACK_F)(const ADM_SYSI4HMI_CMDID_TYPE_E eCmdidType, const D16U sessionId, const DCHAR* state, const ADM_SYSI4HMI_ECU_IDEN_INFO_T* pstEcuIdenInfo, const ADM_SYSI4HMI_DOWNLOAD_RESULT_INFO_T* pstEcuDownloadResult, void* priv);
/***************************************************************************Notify one ECU download progress & result ********************************************************************/

/***************************************************************************Notify all ECU download progress & result ********************************************************************/
/**
 * @brief All Ecu download progress notification.
 * 
 * @param eCmdidType        [IN]    const ADM_SYSI4HMI_CMDID_TYPE_E    Type of detection trigger.
 * @param sessionId         [IN]    const D16U          The sessionId of the strategy
 * @param state             [IN]    const DCHAR*        Task_State: Task_Download_Ing  
 * @param pstEcuDownloadProgressArrayInfo   [IN] const ADM_SYSI4HMI_ECU_DOWNLOAD_PROGRESS_ARRAY_INFO_T*  ECU progress information array structure.
 * @param pstPolicyDownloadProgress [IN]    const ADM_SYSI4HMI_DOWNLOAD_PROGRESS_INFO_T*     The progress information structure of the upgrade package download.
 * @param priv                              [IN/OUT]    void*                               Reserved parameters
 * @return D32S             0:successed, Non-0:failed 
 */
typedef D32S (*ADM_SYSI4HMI_ALL_ECU_DOWNLOAD_PROGRESS_NOTIFY_CALLBACK_F)(const ADM_SYSI4HMI_CMDID_TYPE_E eCmdidType, const D16U sessionId, const DCHAR* state, const ADM_SYSI4HMI_ECU_DOWNLOAD_PROGRESS_ARRAY_INFO_T* pstEcuDownloadProgressArrayInfo, const ADM_SYSI4HMI_DOWNLOAD_PROGRESS_INFO_T* pstPolicyDownloadProgress, void* priv);


/**
 * @brief All Ecu download result notification.
 * 
 * @param eCmdidType        [IN]    const ADM_SYSI4HMI_CMDID_TYPE_E    Type of detection trigger.
 * @param sessionId         [IN]    const D16U          The sessionId of the strategy
 * @param state             [IN]    const DCHAR*        Task_State: Task_Download_Paused/Task_Download_Completed/Task_Download_Successed/Task_Download_Failed/Task_Transfer_xxx/Task_Unpack_xxx
 * @param pstEcuDownloadResultArrayInfo     [IN] const ADM_SYSI4HMI_ECU_DOWNLOAD_RESULT_ARRAY_INFO_T*    Ecu result information array structure.
 * @param pstPolicyDownloadResult   [IN]    const ADM_SYSI4HMI_DOWNLOAD_RESULT_INFO_T*   The result information structure of the upgrade package download.
 * @param priv                              [IN/OUT]    void*                               Reserved parameters
 * @return D32S             0:successed, Non-0:failed
 */
typedef D32S (*ADM_SYSI4HMI_ALL_ECU_DOWNLOAD_RESULT_NOTIFY_CALLBACK_F)(const ADM_SYSI4HMI_CMDID_TYPE_E eCmdidType, const D16U sessionId, const DCHAR* state, const ADM_SYSI4HMI_ECU_DOWNLOAD_RESULT_ARRAY_INFO_T* pstEcuDownloadResultArrayInfo, const ADM_SYSI4HMI_DOWNLOAD_RESULT_INFO_T* pstPolicyDownloadResult, void* priv);
/***************************************************************************Notify all ECU download progress & result ********************************************************************/

/***************************************************************************Query all ECU download progress & result *********************************************************************/
/**
 * @brief Request to query the download progress of all current ECUs.
 * 
 * @param eCmdidType        [OUT]    ADM_SYSI4HMI_CMDID_TYPE_E*    Type of detection trigger.
 * @param sessionId         [OUT]    D16U*          The sessionId of the strategy
 * @param state             [OUT]    DCHAR**        Task_State: Task_Download_Ing  
 * @param pstEcuDownloadProgressArrayInfo   [OUT] ADM_SYSI4HMI_ECU_DOWNLOAD_PROGRESS_ARRAY_INFO_T**  ECU progress information array structure.
 * @param pstPolicyDownloadProgress [OUT]    ADM_SYSI4HMI_DOWNLOAD_PROGRESS_INFO_T**     The progress information structure of the upgrade package download.
 * @return  D32S                            0:successed, Non-0:failed
 */
extern D32S adm_sysi4hmi_queryAllEcuDownloadProgress(ADM_SYSI4HMI_CMDID_TYPE_E* eCmdidType, D16U* sessionId, DCHAR** state, ADM_SYSI4HMI_ECU_DOWNLOAD_PROGRESS_ARRAY_INFO_T** pstEcuDownloadProgressArrayInfo, ADM_SYSI4HMI_DOWNLOAD_PROGRESS_INFO_T** pstPolicyDownloadProgress);

/**
 * @brief Request to query the download result of all current ECUs.
 * 
 * @param eCmdidType        [OUT]    ADM_SYSI4HMI_CMDID_TYPE_E*    Type of detection trigger.
 * @param sessionId         [OUT]    D16U*          The sessionId of the strategy
 * @param state             [OUT]    DCHAR**        Task_State: Task_Download_Paused/Task_Download_Completed/Task_Download_Successed/Task_Download_Failed/Task_Transfer_xxx/Task_Unpack_xxx
 * @param pstEcuDownloadResultArrayInfo     [OUT] ADM_SYSI4HMI_ECU_DOWNLOAD_RESULT_ARRAY_INFO_T**    Ecu result information array structure.
 * @param pstPolicyDownloadResult   [OUT]    ADM_SYSI4HMI_DOWNLOAD_RESULT_INFO_T**   The result information structure of the upgrade package download.
 * @return  D32S                            0:successed, Non-0:failed
 */
extern D32S adm_sysi4hmi_queryAllEcuDownloadResult(ADM_SYSI4HMI_CMDID_TYPE_E* eCmdidType, D16U* sessionId, DCHAR** state, ADM_SYSI4HMI_ECU_DOWNLOAD_RESULT_ARRAY_INFO_T** pstEcuDownloadResultArrayInfo, ADM_SYSI4HMI_DOWNLOAD_RESULT_INFO_T** pstPolicyDownloadResult);
/***************************************************************************Query all ECU download progress & result *********************************************************************/


/***************************************************************************SYSI4HMI-DOWNLOAD init*****************************************************************************************/
/**
 * @brief The SYSI4HMI-DOWNLOAD initialization structure definition
 * 
 */
typedef struct {
    ADM_SYSI4HMI_DOWNLOAD_CONDITIONS_VERIFIED_RESULT_NOTIFY_CALLBACK_F condsVerifiedResultCallback;     /**!< Notification of verification results of download conditions. */
    ADM_SYSI4HMI_POLICY_DOWNLOAD_PROGRESS_NOTIFY_CALLBACK_F policyDownloadProgressNoifyCallback;        /**!< Policy download progress notification. */
    ADM_SYSI4HMI_POLICY_DOWNLOAD_RESULT_NOTIFY_CALLBACK_F   policyDownloadResultNotifyCallback;         /**!< Policy download result notification. */
    ADM_SYSI4HMI_ECU_DOWNLOAD_PROGRESS_NOTIFY_CALLBACK_F    ecuDownloadProgressNotifyCallback;          /**!< Ecu download progress notification. */
    ADM_SYSI4HMI_ECU_DOWNLOAD_RESULT_NOTIFY_CALLBACK_F  ecuDownloadResultNotifyCallback;                /**!< Ecu download result notification. */
    ADM_SYSI4HMI_ALL_ECU_DOWNLOAD_PROGRESS_NOTIFY_CALLBACK_F    allEcuDownloadProgressCallback;         /**!< All Ecu download progress notification. */
    ADM_SYSI4HMI_ALL_ECU_DOWNLOAD_RESULT_NOTIFY_CALLBACK_F  allEcuDownloadResultCallback;               /**!< All Ecu download result notification. */
} ADM_SYSI4HMI_DOWNLOAD_INIT_INFO_T;

/**
 * @brief The SYSI4HMI-DOWNLOAD module initializes the interface
 * @note Use adm_sysi4hmi_downloadDeinit to release the initialization information
 * 
 * @param pstInitInfo   [IN]        const ADM_SYSI4HMI_DOWNLOAD_INIT_INFO_T*        The SYSI4HMI-DOWNLOAD initialization structure
 * @param priv          [IN/OUT]    void*                               Reserved parameters
 * @return D32S         0:successed, Non-0:failed    
 */
extern D32S adm_sysi4hmi_downloadInit(const ADM_SYSI4HMI_DOWNLOAD_INIT_INFO_T* initInfo, void* priv);

/**
 * @brief Release initialization information
 * @note Used to release the information initialized by the adm_sysi4hmi_downloadInit function
 */
extern D32S adm_sysi4hmi_downloadDeinit();
/***************************************************************************SYSI4HMI-DOWNLOAD init*****************************************************************************************/

#ifdef __cplusplus  // close out "C" linkage in case of c++ compiling
}
#endif

#endif//__ADM_SYSI4HMI_DOWNLOAD_H__