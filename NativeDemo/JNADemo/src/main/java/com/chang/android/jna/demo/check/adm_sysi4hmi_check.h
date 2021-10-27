/**
 * @file adm_sysi4hmi_check.h
 * @author mengwei@abupdate.com
 * @brief 通过HMI与主控模块做check交互的接口定义，包括触发检测、检测结果显示、查询检测结果等。
 * @version 0.1
 * @date 2021-05-08
 * 
 * @copyright Copyright © 2018 All rights 上海艾拉比智能科技有限公司
 * 
 */
#ifndef __ADM_SYSI4HMI_CHECK_H__
#define __ADM_SYSI4HMI_CHECK_H__

#ifdef __cplusplus
extern "C" {
#endif

#define EXPORT_FUN __attribute__((visibility("default")))

#include "adm_typedefs.h"
#include "adm_sysi4hmi_common.h"


/***************************************************************************Trigger check*********************************************************************************/
/**
 * @brief User click to trigger version check.
 * 
 * @param eActionType   [OUT]   ADM_SYSI4HMI_ACTION_AUTH_TYPE_E*    Whether the requested trigger action is allowed
 * @return D32S     0:successed, Non-0:failed 
 */
extern int adm_sysi4hmi_triggerCheck(ADM_SYSI4HMI_ACTION_AUTH_TYPE_E* eActionType);
/***************************************************************************Trigger check*********************************************************************************/


/***************************************************************************Notify check result***************************************************************************/
typedef struct {
    DCHAR*  policyFileUrl;              /**!< Download address of the policy file */
    D64S    policyFileSize;             /**!< Size of the policy file, unit:Byte */
    DCHAR*  policyFileHash;             /**!< Hash of the policy file(SHA256 sum) */
} ADM_SYSI4HMI_CHECK_HMI_POLICY_INFO_T;

/**
 * @brief Processing the result information returned by the check.
 * 
 * @param eCmdidType        [IN]    const ADM_SYSI4HMI_CMDID_TYPE_E    Type of detection trigger.
 * @param sessionId         [IN]    const DCHAR*    The session id of the platform response
 * @param state             [IN]    const DCHAR*    Task_State: Task_Check_Successed/Task_Check_Failed
 * @param pstPolicyInfo     [IN]    const ADM_SYSI4HMI_CHECK_HMI_POLICY_INFO_T*  HMI display strategy file information structure.
 * @param errorCode         [IN]    const D32S  Error code of the reason for failure, 0 for success
 * @param priv                              [IN/OUT]    void*                               Reserved parameters
 * @return D32S             0:successed, Non-0:failed             
 */
typedef int (*ADM_SYSI4HMI_CHECK_RESULT_NOTIFY_CALLBACK_F)(const ADM_SYSI4HMI_CMDID_TYPE_E eCmdidType, const DCHAR* sessionId, const DCHAR* state, const ADM_SYSI4HMI_CHECK_HMI_POLICY_INFO_T* pstPolicyInfo, const int errorCode, void* priv);
/***************************************************************************Notify check result***************************************************************************/

/***************************************************************************Query check result****************************************************************************/
/**
 * @brief Request to query the detected result information.
 * 
 * @param eCmdidType        [OUT]    ADM_SYSI4HMI_CMDID_TYPE_E*    Type of detection trigger.
 * @param sessionId         [IN]    DCHAR**    The session id of the platform response
 * @param state             [OUT]    DCHAR**    Task_State: Task_Check_Successed/Task_Check_Failed
 * @param pstPolicyInfo     [OUT]    ADM_SYSI4HMI_CHECK_HMI_POLICY_INFO_T**  HMI display strategy file information structure.
 * @param errorCode         [OUT]    D32S*  Error code of the reason for failure, 0 for success
 * @return  D32S                            0:successed, Non-0:failed
 */
extern int adm_sysi4hmi_queryCheckResult(ADM_SYSI4HMI_CMDID_TYPE_E* eCmdidType, DCHAR** sessionId, DCHAR** state, ADM_SYSI4HMI_CHECK_HMI_POLICY_INFO_T** ppstPolicyInfo, int* errorCode);
/***************************************************************************Query check result****************************************************************************/

/***************************************************************************SYSI4HMI-CHECK init****************************************************************************/
/**
 * @brief The SYSI4HMI-CHECK initialization structure definition
 * 
 */
typedef struct {
    ADM_SYSI4HMI_CHECK_RESULT_NOTIFY_CALLBACK_F checkResultCallback;            /**!< Processing the result information returned by the check. */
} ADM_SYSI4HMI_CHECK_INIT_INFO_T;

/**
 * @brief The SYSI4HMI-CHECK module initializes the interface.
 * @note  adm_sysi4hmi_check to release the initialization information
 * 
 * @param initInfo          [IN]    const ADM_SYSI4HMI_CHECK_INIT_INFO_T*       The SYSI4HMI-CHECK initialization structure
 * @param priv          [IN/OUT]    void*                               Reserved parameters
 * @return D32S         0:successed, Non-0:failed   
 */
extern int adm_sysi4hmi_checkInit(const ADM_SYSI4HMI_CHECK_INIT_INFO_T* initInfo, void* priv);

/**
 * @brief Release initialization information
 * @note Used to release the information initialized by the adm_sysi4hmi_checkInit function
 */
extern int adm_sysi4hmi_check(void);
/***************************************************************************SYSI4HMI-CHECK init****************************************************************************/

#ifdef __cplusplus  // close out "C" linkage in case of c++ compiling
}
#endif

#endif//__ADM_SYSI4HMI_CHECK_H__