## 流程

[NDK概念](https://developer.android.google.cn/ndk/guides/concepts?hl=zh-cn#naa)

为 Android 开发原生应用的一般流程如下：

1. 设计应用，确定要使用 Java 实现的部分，以及要以原生代码形式实现的部分。
> 注意：虽然可以完全避免使用 Java，但您可能会发现 Android Java 框架对于控制显示和界面等任务很有用。
2. 像创建任何其他 Android 项目一样创建一个 Android 应用项目。
3. 如果要编写纯原生应用，请在 AndroidManifest.xml 中声明 NativeActivity 类。如需了解详情，请参阅原生 activity 和应用。
4. 在“JNI”目录中创建一个描述原生库（包括名称、标记、关联库和要编译的源文件）的 Android.mk 文件。
5. 或者，您也可以创建一个配置目标 ABI、工具链、发布/调试模式和 STL 的 Application.mk 文件。对于其中任何您未指明的项，将分别使用以下默认值：
- ABI：所有非弃用的 ABI
- 模式：发布
- STL：系统
6. 将原生源代码放在项目的 jni 目录下。
7. 使用 ndk-build 编译原生（.so、.a）库。
8. 构建 Java 组件，生成可执行 .dex 文件。
9. 将所有内容封装到一个 APK 文件中，包括 .so、.dex 以及应用运行所需的其他文件。