include $(call all-subdir-makefiles)
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

APP_ABI := armeabi-v7a x86

LOCAL_MODULE    := planetlight2345
#LOCAL_SRC_FILES := $(LOCAL_PATH)/com_search2345_paramencrypt_SecJob.c $(LOCAL_PATH)/encode.c $(LOCAL_PATH)/decode.c $(LOCAL_PATH)/md5.c
ALL_CPP_LIST := $(wildcard $(LOCAL_PATH)/*.c)
ALL_CPP_LIST += $(wildcard $(LOCAL_PATH)/*.h)
LOCAL_LDLIBS := -llog
LOCAL_SRC_FILES := $(ALL_CPP_LIST:$(LOCAL_PATH)/%=%)
include $(BUILD_SHARED_LIBRARY)showfrashow