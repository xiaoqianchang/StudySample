## NDKDemo

### JNI 编程 

c 内部分层，将与 java 交互的一层称为 JNI 层（比如 jni-callback-test.cpp），c 内部其它业务逻辑放到下层或其他层处理。

知识点如下：

- 线程参数传递：如果有多个参数就封装成一个结构体传递。
- 传递回调：为了 jni 层保持整洁性，在 jni 层回调，不要在传递后的某个不知名的地方回调，代码阅读难；如果回调触发代码在 c 层某个深处，建议用函数指针进行传递，最后统一在 jni 层回调。
- java 调用的方法里面的局部变量，在方法调用完毕，就会被 jvm 回收。那么调用到 jni 层的 native 方法也是一样！也就是 jobject callback 这个局部变量在方法调用完毕就会被回收！即使你用全局变量保存依然一样！所以必须加个 NewGlobalRef 。

### 资料

- [配置 CMake](https://developer.android.google.cn/studio/projects/configure-cmake?hl=zh-cn#groovy)

