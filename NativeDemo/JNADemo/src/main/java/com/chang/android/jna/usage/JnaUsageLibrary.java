package com.chang.android.jna.usage;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.ShortByReference;

/**
 * 动态库函数映射
 * <p>
 * Created by Nicholas Sean on 2021/11/10 11:00 上午.
 *
 * @version 1.0
 */
public interface JnaUsageLibrary extends Library {
    /**
     * Native.load 第一个参数的用法
     * 1. 当项目用于 Android 设备时，需按照 Android 规则使用 CMakeLists 中设置的名，如 JNADemo；
     * 2. 非 Android，当动态库放置于系统目录时，可省去固定路径和后缀名，如 libJNADemo；
     * 3. 非 Android，当动态库未放置于系统目录时，需要写固定路径和全名，如System.getProperty("user.dir") + "/JNADemo/libs/armeabi-v7a/libJNADemo.so"
     */
    JnaUsageLibrary INSTANCE = Native.load(JnaConstants.isAppUse ? "JNADemo" : System.getProperty("user.dir") + "/JNADemo/src/main/jniLibs/armeabi-v7a/libJNADemo.so", JnaUsageLibrary.class);

    /***************************** JNA 用法 start *************************************/
    /***************************** Java 调原生方法 start *******************************/
    /*
    jna 简单调用 c 函数
     */
    int helloWorld();

    /*
    基本数据类型
     */
    int basicDataTypeTest(byte a, short b, int c, long d, float e, double f);

    /*
    指针和数组
     */
    // 定义: 其中char *直接使用String类型替代，unsigned char * 可转为byte[]数组
    void arrayTest(String pStr, byte[] paStr);

    /*
    基本数据类型指针
     */
    void basicDataTypePointerTest(ByteByReference pByte, ShortByReference pShort, IntByReference pInt, LongByReference pLong, FloatByReference pFloat, DoubleByReference pDouble);

    /*
    指向动态内存的指针（调用者动态声请内存）
     */
    void mallocTest(Memory pStr);

    /*
    二级指针
    二级指针做输入与输出 -> 做输入，主调函数分配内存，被调函数使用；做输出，被调函数分配内存，把运算的结果，以指针做函数参数甩出来。
     */
    // 传递out参数，c内部使用后重新赋值返回给java
    void doublePointTest(PointerByReference ppInt, PointerByReference ppStr);
    // 主动释放C内部动态申请的内存
    void freePoint(Pointer pt);

    /*
    传递结构体
    注意：2、3、4、5都可以将参数型类改为User，调用的地方既可以传User.ByReference也可以传User。
     */
    // 1. 值传递
    int setUser(User.ByValue user);
    // 2. 指针传递
    int setUserPoint(User.ByReference pUser);
    int setUserPoint(User pUser); // C端参数为User指针，那Java端对应参数既可以为User.ByReference也可以为User
    // 3. 获取结构体(out)(由主调者分配内存)
    void getUser(User.ByReference pUser);
    // 4. 获取结构体(由被调者分配内存，注意要主动释放)
    User.ByReference getUser2();
    // 5. 释放内存空间
    void freeUser(User.ByReference user);

    /*
    传递结构体数组
     */
    // 传递结构体数组（此处的数组类型只能是User[]，不能是User.ByReference[]，调用的地方也只能是new User().toArray(length)方式创建数组）
    int setUserArray(User[] users, int arrayLength);
    // 获取结构体数组（二维数组一维化）
    void getUserArray(User.ByReference pUser, IntByReference pArrayLength);
    // 获取结构体数组（这里的返回值可以写为User.ByReference，调用地方的接收为结构体.ByReference，并用.toArray(length)转为数组）
    User getUserArray2(IntByReference pArrayLength);
    // 释放内存空间
    void freeUsers(User[] users);

    /*
    获取复杂结构体
    注意：2、3、4、5都可以将参数型类改为Company，调用的地方既可以传Company.ByReference也可以传Company，但是Company内部嵌套的User必须为User.ByReference。
     */
    // 1. 传递复杂结构体（传递结构体嵌套结构体）
    void setCompany(Company.ByReference pCompany);
    // 2. 获取复杂结构体（获取结构体嵌套结构体）
    void getCompany(Company.ByReference pCompany);
    // 3. 释放复杂结构体嵌套结构体内存空间
    void freeCompanyInternalPointer(Company.ByReference company);
    // 4. 获取复杂结构体
    Company.ByReference getCompany2();
    // 5. 释放复杂结构体嵌套结构体内存空间
    void freeCompany(Company.ByReference pCompany);
    /***************************** Java 调原生方法 end *******************************/

    /***************************** 原生方法调 Java start *******************************/
    interface Fp extends Callback {
        int invoke(int sum);
    }
    // 简单 native 调 java
    int getValue(int left, int right, Fp fp);

    interface FetchUserCallback extends Callback {
        void invoke(User.ByReference user);
    }
    // 异步获取结构体
    void asyncGetUser(FetchUserCallback callback);

    interface FetchUserArrayCallback extends Callback {
        void apply(User.ByReference userArray);
    }
    // 异步获取结构体数组
    void asyncGetUserArray(IntByReference pArrayLength, FetchUserArrayCallback callback);

    interface FetchCompanyCallback extends Callback {
        void apply(Company.ByReference company);
    }
    // 异步获取复杂结构体
    void asyncGetCompany(FetchCompanyCallback callback);
    /***************************** 原生方法调 Java end *******************************/
    /***************************** JNA 用法 end *************************************/
}
