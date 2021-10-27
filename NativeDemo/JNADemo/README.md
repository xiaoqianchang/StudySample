## JNADemo

JNA 是在 JNI 的基础上做的封装，减少使用 JNI 的繁杂工作。但是也带来了难点，
JNA 的难点是 C 与 Java 对应的数据结构转换。

通过使用 JNAerator 自动转换头文件为对应的 java 代码（自动转换数据结构）;
先进入 jnaerator-0.12-shaded.jar 的目录，在用如下命令打开 JNAeratorStudio
`java -jar jnaerator-0.12-shaded.jar`