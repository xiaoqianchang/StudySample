/**
 * @file adm_sysi4hmi_common.h
 * @author mengwei@abupdate.com
 * @brief SYSI4HMI模块使用的通用结构头文件，包含：ECU的标识信息、前置条件的配置信息等。
 * @version 0.1
 * @date 2021-05-08
 * 
 * @copyright Copyright © 2018 All rights 上海艾拉比智能科技有限公司
 * 
 */
#ifndef __ADM_SYSI4HMI_COMMON_H__
#define __ADM_SYSI4HMI_COMMON_H__

#ifdef __cplusplus
extern "C" {
#endif

#define EXPORT_FUN __attribute__((visibility("default")))

#include "adm_typedefs.h"


/**
 * @brief Whether the requested trigger action is allowed
 * 
 */
typedef enum {
    ADM_SYSI4HMI_ACTION_AUTH_TYPE_ALLOW = 1,         /**!< Allowed */
    ADM_SYSI4HMI_ACTION_AUTH_TYPE_NOT_ALLOW,         /**!< Not allowed */
    ADM_SYSI4HMI_ACTION_AUTH_TYPE_INVALID            /**!< Invalid */
} ADM_SYSI4HMI_ACTION_AUTH_TYPE_E;

/**
 * @brief Types of condition detection.
 * 
 */
typedef enum {
    ADM_SYSI4HMI_CONDITIONS_VERIFIED_TYPE_ONCE = 1,     /**!< Only once */
    ADM_SYSI4HMI_CONDITIONS_VERIFIED_TYPE_CYCLE,        /**!< Period */
    ADM_SYSI4HMI_CONDITIONS_VERIFIED_TYPE_INVALID       /**!< Invalid */
} ADM_SYSI4HMI_CONDITIONS_VERIFIED_TYPE_E;


/**
 * @brief Structure definition used to uniquely identify an ECU.
 * 
 */
typedef struct {
    DCHAR* partNum;             /**!< Part number*/
    D32S requestId;             /**!< Request ID */
    D32S responseId;            /**!< Response ID */
    D32S swDid;                 /**!< The did of the software version, used to distinguish different upgrade parts of the same requestId ECU */
    DCHAR* name;                /**!< Part name */
    DCHAR*  softVer;            /**!< Software version number */
} ADM_SYSI4HMI_ECU_IDEN_INFO_T;

/**
 * @brief Type definition of detection trigger.
 * 
 */
typedef enum {
    ADM_SYSI4HMI_CMDID_TYPE_TRIGGER_USER = 1,      /**!< User click by HUT */
    ADM_SYSI4HMI_CMDID_TYPE_TRIGGER_IGN,           /**!< IGN */
    ADM_SYSI4HMI_CMDID_TYPE_TRIGGER_TSP,           /**!< TSP push */
    ADM_SYSI4HMI_CMDID_TYPE_TRIGGER_PHONE,         /**!< User click by phone */
    ADM_SYSI4HMI_CMDID_TYPE_TRIGGER_CYCLE,         /**!< Cycle */
    ADM_SYSI4HMI_CMDID_TYPE_INVALID                /**!< Invalid */
} ADM_SYSI4HMI_CMDID_TYPE_E;

/* variable define */
typedef struct variable_struct
{
	D16U  data_type;           /*<< data type */
	D16U  data_length;         /*<< data length */
	union V
	{
		D8U   value[1];
		D32U  value_u32;
		D32S  value_s32;
		D64U  value_u64;
		float value_f;
		D8U*  value_s;
	}v;
}variable_st, *variable_pt;

/**
 * @brief Data definition of condition value
 * 
 */
typedef struct {
    D32S msgId;                     /**!< MsgId of vehicle state */
    variable_pt expectVariable;     /**!< Expect vehicle state */
    variable_pt realVariable;       /**!< Real vehicle state */
} ADM_SYSI4HMI_CONDITION_DATA_T;

/**
 * @brief Condition value data array definition
 * 
 */
typedef struct {
    ADM_SYSI4HMI_CONDITION_DATA_T**  condsArray;     /**!< Condition value data array */
    D16U condsArrayLength;                          /**!< Length of condition valud data array */
} ADM_SYSI4HMI_CONDITION_DATA_ARRAY_T;

#ifdef __cplusplus  // close out "C" linkage in case of c++ compiling
}
#endif

#endif//__ADM_SYSI4HMI_COMMON_H__