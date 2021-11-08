## NativeDemo

Native开发demo工程.

### sample

- seclib   加解密 lib

### NativeApp

### JNADemo

该 module 是通过 Jna 框架直接调用源文件中的函数。

### JNIDemo

该 module 是通过 java 命令编译为 class 文件、生成头文件，然后通过 gcc 命令编译源文件为 so 。

基本介绍请看 JNIDemo 中 README.md 文件。

### seclib

该 module 是通过 nuk-build 编译配置工具将源文件编译为 so 。

### NDKDemo

该 module 是通过 CMake 编译配置工具将源文件编译为 so 。