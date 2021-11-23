package com.chang.android.jna.usage;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.ShortByReference;

/**
 * JnaUsageLibrary 方法测试.
 * 1. 该测试类用于 main 函数测试
 *    请到 JNADemo 目录执行 ./make_so.sh 脚本后（gcc 编译生成 so 并移动到 jniLibs/armeabi-v7a），在执行该类中的 main 函数；
 * <p>
 * Created by Nicholas Sean on 2021/11/10 11:06 上午.
 *
 * @version 1.0
 */
public class JnaUsageTest {

    private static final int SUCCESS = 0;

    private static boolean isSuccess(int code) {
        return code == SUCCESS;
    }

    public static void main(String args[]) {
//        helloWorld();

//        basicDataTypeTest();

//        arrayTest();

//        basicDataTypePointerTest();

        mallocTest();

//        doublePointTest();

//        setUserAndUserPoint();

//        getUser();

//        getUser2();

//        setUserArray();

//        getUserArray();

//        getUserArray2();

//        setCompany();

//        getCompany();

//        getCompany2();

//        getValue();

//        asyncGetUser();

//        asyncGetUserArray();

//        asyncGetCompany();
    }

    /**
     * jna 简单调用 c 函数
     *
     * 这里有个问题，为什么 java 端会比 c 端先打印？难道是通讯慢？
     */
    private static void helloWorld() {
        int ret = JnaUsageLibrary.INSTANCE.helloWorld();
        if (isSuccess(ret)) {
            System.out.println("java printf: Hello world!");
        }
    }

    /**
     * 基本数据类型
     *
     * 这里有和 helloWorld 一样的时序问题？
     */
    private static void basicDataTypeTest() {
        byte a = 1;
        short b = 2;
        int c  = 3;
        long d = 4;
        float e = 1.6f;
        double f = 2.5f;
        int ret = JnaUsageLibrary.INSTANCE.basicDataTypeTest(a, b, c, d, e, f);
        if (isSuccess(ret)) {
            System.out.println("java printf: ret = " + ret);
        }
    }

    /**
     * 指针和数组
     */
    private static void arrayTest() {
        String pStr = "Nicholas Sean";
        byte[] paStr = new byte[10];
        for (int i = 0; i < paStr.length; i++) {
            paStr[i] = (byte) ('a' + i);
        }
        JnaUsageLibrary.INSTANCE.arrayTest(pStr, paStr);
    }

    /**
     * 基本数据类型指针
     *
     * 测试 C 中的入参与出参
     */
    private static void basicDataTypePointerTest() {
        ByteByReference pByte = new ByteByReference((byte) 1);
        ShortByReference pShort = new ShortByReference((short) 2);
        IntByReference pInt = new IntByReference(4);
        LongByReference pLong = new LongByReference(8);
        FloatByReference pFloat = new FloatByReference(4);
        DoubleByReference pDouble = new DoubleByReference(8);
        JnaUsageLibrary.INSTANCE.basicDataTypePointerTest(
                pByte,
                pShort,
                pInt,
                pLong,
                pFloat,
                pDouble);
        System.out.println("java printf: out " +
                "pByte=" + pByte.getValue() +
                ", pShort=" + pShort.getValue() +
                ", pInt = " + pInt.getValue() +
                ", pLong=" + pLong.getValue() +
                ", pFloat=" + pFloat.getValue() +
                ", out pDouble=" + pDouble.getValue());
    }

    /**
     * 指向动态内存的指针（调用者动态声请内存）
     */
    private static void mallocTest() {
        Memory memory = new Memory(20);
        JnaUsageLibrary.INSTANCE.mallocTest(memory);
        System.out.println("java printf: out memory=" + memory.getString(0));

        // 合适的时候手动释放直接内存（参考 Memory 中的 finalize 方法）
        freeMemory(memory);
    }

    /**
     * 合适的时候手动释放直接内存（参考 Memory 中的 finalize 方法）
     *
     * @param memory
     */
    private static void freeMemory(Memory memory) {
        long peer = Pointer.nativeValue(memory);
        if (peer != 0) {
            Native.free(peer); // 手动释放内存
            Pointer.nativeValue(memory, 0);
        }
    }

    /**
     * 二级指针
     */
    private static void doublePointTest() {
        // 构造 int 二级指针
        IntByReference intByReference = new IntByReference(100);
        Pointer pInt = intByReference.getPointer();
        PointerByReference ppInt = new PointerByReference(pInt);
        // 构造 char 二级指针
        PointerByReference ppStr = new PointerByReference();

        try {
            JnaUsageLibrary.INSTANCE.doublePointTest(ppInt, ppStr);
            System.out.println("java printf: out ppInt=" + ppInt.getValue().getInt(0) + ", ppStr=" + ppStr.getValue().getString(0));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JnaUsageLibrary.INSTANCE.freePoint(ppStr.getValue());
        }
    }

    /**
     * 传递结构体
     *
     * 值传递和指针传递
     */
    private static void setUserAndUserPoint() {
        // 构造结构体对象
        User.ByValue userValue = new User.ByValue();
        userValue.id = new NativeLong(1);
        userValue.name = "Nicholas Sean";
        userValue.age = 18;
        JnaUsageLibrary.INSTANCE.setUser(userValue);
        System.out.println("java printf: setUser out " + userValue.toString());

        // 构造结构体对象指针
        User.ByReference userReference = new User.ByReference();
        userReference.id = new NativeLong(3);
        userReference.name = "Nicholas Sean";
        userReference.age = 20;
        JnaUsageLibrary.INSTANCE.setUserPoint(userReference);
        System.out.println("java printf: setUserPoint out " + userReference.toString());

        // 上面结构体对象指针可以改为直接new对象（效果一样）
//        User user = new User();
//        user.id = new NativeLong(5);
//        user.name = "Nicholas Sean";
//        user.age = 20;
//        JnaUsageLibrary.INSTANCE.setUserPoint(user);
//        System.out.println("java printf: setUserPoint out " + user.toString());
    }

    /**
     * 获取结构体
     */
    private static void getUser() {
        User.ByReference userReference = new User.ByReference();
        JnaUsageLibrary.INSTANCE.getUser(userReference);
        System.out.println("java printf: " + userReference.toString());
    }

    /**
     * 获取结构体
     */
    private static void getUser2() {
        User.ByReference userReference = null;
        try {
            userReference = JnaUsageLibrary.INSTANCE.getUser2();
            if (userReference == null) {
                return;
            }
            System.out.println("java printf: getUser2 " + userReference.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JnaUsageLibrary.INSTANCE.freeUser(userReference);
        }
    }

    /**
     * 传递结构体数组
     */
    private static void setUserArray() {
        int length = 5;
        User user = new User();
        User[] users = (User[]) user.toArray(length);
        for (int i = 0; i < users.length; i++) {
            users[i].id = new NativeLong(500235 + i);
            users[i].name = "Nicholas Sean" + i;
            users[i].age = 18 + i;
        }
        /*
        如下通过new来创建数组，将会报如下异常：
        Exception in thread "main" java.lang.IllegalArgumentException: Structure array elements must use contiguous memory (bad backing address at Structure array index 1)
            at com.sun.jna.Structure.structureArrayCheck(Structure.java:2213)
            at com.sun.jna.Structure.autoWrite(Structure.java:2244)
            at com.sun.jna.Function.convertArgument(Function.java:617)
            at com.sun.jna.Function.invoke(Function.java:345)
            at com.sun.jna.Library$Handler.invoke(Library.java:265)
            at com.sun.proxy.$Proxy0.setUserArray(Unknown Source)
            at com.chang.android.jna.usage.JnaUsageTest.setUserArray(JnaUsageTest.java:269)
            at com.chang.android.jna.usage.JnaUsageTest.main(JnaUsageTest.java:54)
         */
//        User[] users = new User[length];
//        for (int i = 0; i < users.length; i++) {
//            User user = new User();
//            user.id = new NativeLong(500235 + i);
//            user.name = "Nicholas Sean" + i;
//            user.age = 18 + i;
//            users[i] = user;
//        }
        JnaUsageLibrary.INSTANCE.setUserArray(users, length);
    }

    /**
     * 获取结构体数组（二维数组一维化）---- 这里 C 实现还有点问题
     */
    private static void getUserArray() {
        User.ByReference[] users = null;
        try {
            IntByReference lengthReference = new IntByReference();
            User.ByReference userReference = new User.ByReference();
            JnaUsageLibrary.INSTANCE.getUserArray(userReference, lengthReference);
            users = (User.ByReference[]) userReference.toArray(lengthReference.getValue());
            for (User user : users) {
                System.out.println("java printf: " + user.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            JnaUsageLibrary.INSTANCE.freeUser(users);
        }
    }

    /**
     * 获取结构体数组
     */
    private static void getUserArray2() {
        User[] users = null;
        try {
            IntByReference lengthReference = new IntByReference();
            User retUser = JnaUsageLibrary.INSTANCE.getUserArray2(lengthReference);
            System.out.println("java printf: out arrayLength = " + lengthReference.getValue());
            if (retUser == null) {
                return;
            }
            users = (User[]) retUser.toArray(lengthReference.getValue());
            for (User user : users) {
                System.out.println("java printf: " + user.toString());
            }
            // 这里可以把数组在传递给 C ，然后 C 做业务逻辑
//            JnaUsageLibrary.INSTANCE.setUserArray(users, lengthReference.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JnaUsageLibrary.INSTANCE.freeUsers(users);
        }
    }

    /**
     * 传递复杂结构体
     */
    private static void setCompany() {
        int length = 5;
        Company.ByReference companyReference = new Company.ByReference();
        companyReference.id = new NativeLong(123);
        companyReference.name = "Adup";
        for (int i = 0; i < length; i++) {
            User.ByReference userReference = new User.ByReference();
            userReference.id = new NativeLong(500235 + i);
            userReference.name = "Nicholas Sean" + i;
            userReference.age = 18 + i;
        }
        companyReference.userArrayLength = 5;
//        companyReference.userArray = new User.ByReference().toArray(length);
    }

    /**
     * 获取复杂结构体
     */
    private static void getCompany() {
        Company.ByReference company = null;
        try {
            company = new Company.ByReference();
            JnaUsageLibrary.INSTANCE.getCompany(company);
            System.out.println("java printf: " + company.toString());
            User.ByReference[] users = (User.ByReference[]) company.userArray.toArray(company.userArrayLength);
            for (User user : users) {
                System.out.println("java printf: " + user.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JnaUsageLibrary.INSTANCE.freeCompanyInternalPointer(company);
        }
    }

    /**
     * 获取复杂结构体
     */
    private static void getCompany2() {
        Company.ByReference company = null;
        try {
            company = JnaUsageLibrary.INSTANCE.getCompany2();
            if (company == null) {
                return;
            }
            System.out.println("java printf: " + company.toString());
            User.ByReference[] users = (User.ByReference[]) company.userArray.toArray(company.userArrayLength);
            for (User user : users) {
                System.out.println("java printf: " + user.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JnaUsageLibrary.INSTANCE.freeCompany(company);
        }
    }

    /**
     * 简单 native 调 java
     */
    private static void getValue() {
        JnaUsageLibrary.INSTANCE.getValue(1, 2, new JnaUsageLibrary.Fp() {
            @Override
            public int invoke(int sum) {
                System.out.println("java printf: sum=" + sum);
                return 0;
            }
        });
    }

    /**
     * 异步获取结构体
     */
    private static void asyncGetUser() {
        JnaUsageLibrary.INSTANCE.asyncGetUser(new JnaUsageLibrary.FetchUserCallback() {
            @Override
            public void invoke(User.ByReference user) {
                if (user != null) {
                    System.out.println("java printf: " + user.toString());
                }
                // 注意要释放 C 内存
            }
        });
    }

    /**
     * 异步获取结构体数组
     */
    private static void asyncGetUserArray() {
        IntByReference lengthReference = new IntByReference();
        JnaUsageLibrary.INSTANCE.asyncGetUserArray(lengthReference, new JnaUsageLibrary.FetchUserArrayCallback() {
            @Override
            public void apply(User.ByReference userArray) {
                if (userArray != null) {
                    User[] users = (User[]) userArray.toArray(lengthReference.getValue());
                    for (User user : users) {
                        System.out.println("java printf: " + user.toString());
                    }
                    // 注意要释放 C 内存
                }
            }
        });
    }

    /**
     * 异步获取复杂结构体
     */
    private static void asyncGetCompany() {
        JnaUsageLibrary.INSTANCE.asyncGetCompany(new JnaUsageLibrary.FetchCompanyCallback() {
            @Override
            public void apply(Company.ByReference company) {
                if (company != null) {
                    System.out.println("java printf: " + company.toString());
                    User[] users = (User[]) company.userArray.toArray(company.userArrayLength);
                    for (User user : users) {
                        System.out.println("java printf: " + user.toString());
                    }
                    JnaUsageLibrary.INSTANCE.freeCompanyInternalPointer(company);
                }
            }
        });
    }

}
