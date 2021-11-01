include $(call all-subdir-makefiles)
# Android.mk 文件必须先定义 LOCAL_PATH 变量，此变量表示源文件在开发树中的位置。
# 构建系统提供的宏函数 my-dir 将返回当前目录（Android.mk 文件本身所在的目录）的路径。
LOCAL_PATH := $(call my-dir)

# 借助CLEAR_VARS变量清除除LOCAL_PATH外的所有LOCAL_<name>变量
include $(CLEAR_VARS)

# 该配置放在 Application.mk
#APP_ABI := armeabi-v7a arm64-v8a x86

# LOCAL_MODULE 变量存储要构建的模块的名称（即要生成的共享库的名称，应用的每个模块中使用一次此变量，每个模块名称必须唯一且不含任何空格）
# 构建系统在生成最终共享库文件时，会对 LOCAL_MODULE 的名称自动添加正确的前缀和后缀。例如 libplanetlight2345.so 。
# 注意：如果模块名称的开头已经是 lib，构建系统不会添加额外的 lib 前缀，而是按原样采用模块名称并添加 .so 扩展名。
LOCAL_MODULE    := planetlight2345

# 列举源文件，以空格分隔多个文件
#LOCAL_SRC_FILES := $(LOCAL_PATH)/com_search2345_paramencrypt_SecJob.c $(LOCAL_PATH)/encode.c $(LOCAL_PATH)/decode.c $(LOCAL_PATH)/md5.c
ALL_CPP_LIST := $(wildcard $(LOCAL_PATH)/*.c)
ALL_CPP_LIST += $(wildcard $(LOCAL_PATH)/*.h)
LOCAL_SRC_FILES := $(ALL_CPP_LIST:$(LOCAL_PATH)/%=%)

# 列出外部库，供构建系统在构建二进制文件时使用。每个库名称之前会有 -l（链接）选项。
LOCAL_LDLIBS := -llog

# 必须在文件结尾定义编译类型，指定生成的静态库或者共享库在运行时依赖的共享库模块列表。
include $(BUILD_SHARED_LIBRARY)