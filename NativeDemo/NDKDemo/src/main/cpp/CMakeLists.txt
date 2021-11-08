# 如需指示 CMake 根据原生源代码创建原生库，请向您的构建脚本添加 cmake_minimum_required() 和 add_library() 命令：

# Sets the minimum version of CMake required to build your native library.
# This ensures that a certain set of CMake features is available to
# your build.
# 指定cmake最小版本
cmake_minimum_required(VERSION 3.4.1)

# Declares and names the project.
#project("test")

# abiFilters "armeabi", "armeabi-v7a", "arm64-v8a", "x86", "x86_64", "mips", "mips64"

# Specifies a library name, specifies whether the library is STATIC or
# SHARED, and provides relative paths to the source code. You can
# define multiple libraries by adding multiple add_library() commands,
# and CMake builds them for you. When you build your app, Gradle
# automatically packages shared libraries with your APK.

add_library( # Specifies the name of the library.
        # 注意这里的library name，下文在java（kotlin）类中需要用到
        # 库的名称
        native-lib

        # Sets the library as a shared library.
        # 将库设置成共享库
        SHARED

        # Provides a relative path to your source file(s).
        # 设置源码的路径
        native-lib.cpp
        jni-test.cpp
        jni-callback-test.cpp) # 注意是路径，而不是包，不可用.区分，需要使用/ ,如 src/main/cpp/native-lib.cpp

# Specifies a path to native header files.
include_directories( src/main/cpp/include/ )

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
        log)

# 为了让您的原生库能够调用 log 库中的函数，您需要使用 CMake 构建脚本中的 target_link_libraries() 命令来关联这些库：
# Links your native library against one or more other native libraries.
target_link_libraries( # Specifies the target library.
        native-lib

        # Links the log library to the target library.
        ${log-lib})