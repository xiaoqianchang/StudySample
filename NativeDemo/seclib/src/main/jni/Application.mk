# 默认情况下，NDK 构建系统会为所有非弃用 ABI 生成代码。可以使用 APP_ABI 设置为特定 ABI 生成代码。
# 可以指定多个值，方法是将它们放在同一行上，中间用空格分隔。
# 注意：Gradle 的 externalNativeBuild 会忽略 APP_ABI。请在 splits 块内部使用 abiFilters 块。
APP_ABI := armeabi-v7a arm64-v8a x86

#APP_PLATFORM := android-19