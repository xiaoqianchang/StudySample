# 如需指示 CMake 根据原生源代码创建原生库，请向您的构建脚本添加 cmake_minimum_required() 和 add_library() 命令：

# Sets the minimum version of CMake required to build your native library.
# This ensures that a certain set of CMake features is available to
# your build.
# 指定cmake最小版本(限定cmake支持最低版本)
cmake_minimum_required(VERSION 3.4.1)

# Declares and names the project.
#project("test")

# abiFilters "armeabi", "armeabi-v7a", "arm64-v8a", "x86", "x86_64", "mips", "mips64"

# 设置so输出的目录（PROJECT_SOURCE_DIR=CmakeLists.txt所在的目录（如果不存在后面的子目录会自动创建），ANDROID_ABI=build.gradle中abiFilters配置的值）
#set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/libs/${ANDROID_ABI})

# 定义搜索路径
# 定义头文件的搜索路径
# Specifies a path to native header files.
include_directories( src/main/cpp/include/ )
# 定义库文件的搜索路径（添加需要链接的库文件路径，注意这里是全路径），该用法已经被废弃
#link_libraries( $ndk/toolchains/llvm/prebuilt/darwin-x86_64/sysroot/usr/lib/aarch64-linux-android/28/ )
# target_link_libraries 要在 add_executable 之后，link_libraries 要在 add_executable 之前

# Specifies a library name, specifies whether the library is STATIC or
# SHARED, and provides relative paths to the source code. You can
# define multiple libraries by adding multiple add_library() commands,
# and CMake builds them for you. When you build your app, Gradle
# automatically packages shared libraries with your APK.

# 添加一个可执行程序
add_library( # Specifies the name of the library.
        # 注意这里的library name，下文在java（kotlin）类中需要用到
        # 库的名称
        native-lib

        # Sets the library as a shared library.
        # STATIC：静态库，是目标文件的归档文件，在链接其它目标的时候使用
        # SHARED：动态库，会被动态链接，在运行时被加载
        # MODULE：模块库，是不会被链接到其它目标中的插件，但是可能会在运行时使用dlopen-系列的函数动态链接
        # 将库设置成共享库
        SHARED

        # Provides a relative path to your source file(s).
        # 设置源码的路径（相对于当前文件），多个源码文件用空格分开
        native-lib.cpp) # 注意是路径，而不是包，不可用.区分，需要使用/ ,如 src/main/cpp/native-lib.cpp

# 添加 NDK API

# Android 平台上已存在预构建的 NDK 库，因此您无需构建 NDK 库或将其打包到 APK 中。由于这些 NDK 库已位于 CMake
# 搜索路径中，因此您甚至无需指定本地安装的 NDK 库的位置，您只需为 CMake 提供您想要使用的库的名称，并将其与您
# 自己的原生库相关联即可。

# 添加 find_library() 命令以找到 NDK 库并将其路径存储为一个变量。您可以使用此变量在构建
# 脚本的其他部分引用 NDK 库。以下示例会找到 Android 专有的日志支持库，并将其路径存储在 log-lib 中：
find_library( # Defines the name of the path variable that stores the location of the NDK library.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        # $ndk/toolchains/llvm/prebuilt/darwin-x86_64/sysroot/usr/lib/aarch64-linux-android/28/liblog.so
        log)

# 将库文件链接到可执行程序上
# 为了让您的原生库能够调用 log 库中的函数，您需要使用 CMake 构建脚本中的 target_link_libraries() 命令来关联这些库：
# Links your native library against one or more other native libraries.
target_link_libraries( # Specifies the target library.
        native-lib

        # Links the log library to the target library.
        ${log-lib})

### 上面是标准模板 ###
### 下面添加其他库 ###

# 添加 NDK 原生库

# 如需提供本地 NDK 库的路径，您可以使用 Android Studio 自动为您定义的 ANDROID_NDK 路径变量。
# 以下命令可以指示 CMake 将 NDK 中源文件 android_native_app_glue.c（负责管理 NativeActivity 生命周期事件和触摸输入）
# 构建至静态库，并将其与 native-lib 关联：
add_library(
        app-glue
        STATIC
        ${ANDROID_NDK}/sources/android/native_app_glue/android_native_app_glue.c )

# You need to link static libraries against your shared native library.
target_link_libraries( native-lib app-glue ${log-lib} )

# 添加其他预构建库

# 添加预构建库的步骤与为 CMake 指定其他要构建的原生库的步骤相似。不过，由于库已构建，因此您需要使用
# IMPORTED 标志指示 CMake 您只想要将此库导入到您的项目中：
add_library(
        imported-lib
        SHARED
        IMPORTED )
# 然后，您需要使用 set_target_properties() 命令指定库的路径，具体命令如下所示。
#
# 某些库会针对特定的 CPU 架构或应用二进制接口 (ABI) 提供单独的软件包，并将其整理到单独的目录中。此方法既有助于
# 库充分利用特定的 CPU 架构，又能让您只使用所需的库版本。如需向 CMake 构建脚本添加库的多个 ABI 版本，而不必为
# 库的每个版本编写多个命令，您可以使用 ANDROID_ABI 路径变量。此变量使用的是 NDK 支持的一组默认 ABI，或者您手
# 动配置 Gradle 而让其使用的一组经过过滤的 ABI。例如：
set_target_properties( # Specifies the target library.
        imported-lib

        # Specifies the parameter you want to define.
        PROPERTIES IMPORTED_LOCATION

        # Provides the path to the library you want to import.
        imported-lib/src/${ANDROID_ABI}/libimported-lib.so )
# 为了让 CMake 能够在编译时找到头文件，您需要使用 include_directories() 命令并包含相应头文件的路径：
include_directories( imported-lib/include/ )
# 如需将预构建库关联到您自己的原生库，请将其添加到 CMake 构建脚本的 target_link_libraries() 命令中：
target_link_libraries( native-lib imported-lib app-glue ${log-lib} )