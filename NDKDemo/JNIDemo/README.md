## JNIDemo

### 编译生成 so 库并在 java 中调用

so 库的编译可以采用 gcc 。

切换到 jni 目录，对于 tes.c 和 test.cpp 来说，它们的编译指令如下。

``` bash
c:   gcc -shared -I /Library/Java/JavaVirtualMachines/jdk1.8.0_221.jdk/Contents/Home/include -I /Library/Developer/CommandLineTools/SDKs/MacOSX.sdk/System/Library/Frameworks/JavaVM.framework/Versions/A/Headers -fPIC test.c -o libjnitest.so
c++: gcc -shared -I /Library/Java/JavaVirtualMachines/jdk1.8.0_221.jdk/Contents/Home/include -I /Library/Developer/CommandLineTools/SDKs/MacOSX.sdk/System/Library/Frameworks/JavaVM.framework/Versions/A/Headers -fPIC test.cpp -o libjnitest.so
```

生成的 so 位置在 jni 目录里面。

命名解释：
前面 -I 指定 jni.h 文件的位置，后面 -I 指定 jni_md.h 文件的位置。

切换到主目录执行 java -Djava.library.path=jni com.chang.jni.demo.JNITest 在 mac 上有问题，可以在 JNITest 中右键点击 Run 'JNITest.main()'