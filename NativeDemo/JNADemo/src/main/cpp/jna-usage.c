//
// Created by 肖昌 on 2021/11/10.
//
// 该文件测试 jna 的用法

//字符串格式化符号
//%c 		格式化字符及其 ASCII 码
//%s 		格式化字符串
//%d 		格式化整数
//%o 		格式化无符号八进制数
//%x 		格式化无符号十六进制数
//%X 		格式化无符号十六进制数（大写）
//%f 		格式化定点数，可指定小数点后的精度
//%e 		用科学计数法格式化定点数
//%E 		作用同 %e，用科学计数法格式化定点数
//%g 		根据值的大小决定使用 %f 或者%e
//%G 		作用同 %g,根据值的大小决定使用 %F 或者%E

// int *pInt
// 获取指针变量的地址，使用取地址运算符&，如 int *pb = &pInt;
// 获取指针变量指向的数据，使用取值运算符*，如 printf("pInt=%d", *pInt);

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

/*
 * jna 简单调用 c 函数
 */
int helloWorld() {
    printf("c printf: Hello world!\n");
    return 0;
}

/*
 * 基本数据类型
 */
int basicDataTypeTest(char a, short b, int c, long d, float e, double f) {
    printf("c printf: in char=%d, short=%d, int=%d, long=%ld, float=%f, double=%f\n", a, b, c, d, e, f);
    return 0;
}

/*
 * 指针和数组
 */
// 这里的 char 指针即可表示 byte 数组也可以表示字符串string
void arrayTest(char *pStr, unsigned char *paStr) {
    printf("c printf: in pStr = %s\n", pStr);
    printf("c printf: in paStr = %s, element list : ", paStr);
    for (int i = 0; i < 10; i++) {
        printf("%c ", paStr[i]);
    }
}

/*
 * 基本数据类型指针
 */
void basicDataTypePointerTest(char *pByte, short *pShort, int *pInt, long *pLong, float *pFloat, double *pDouble) {
    printf("c printf: in pByte=%d, pShort=%d, pInt=%d, pLong=%ld, pFloat=%f, pDouble=%f\n", *pByte, *pShort, *pInt, *pLong, *pFloat, *pDouble);
    *pByte = 1;
    *pShort = 2;
    *pInt = 10;
    *pLong = 20;
    *pFloat = 25.56;
    *pDouble = 55.55;
}

/*
 * 指向动态内存的指针（调用者动态声请内存）
 */
void mallocTest(char *pStr) {
    strcpy(pStr, "I am from c!");
}
// c 自己调用mallocTest，动态声请内存（Java使用Memory类与之相对应）
//int main() {
//    char *pStr = malloc(sizeof(char)*32);
//    mallocTest(pStr);
//    free(pStr);
//    return 0;
//}

/*
 * 二级指针
 */
// 二级指针做输入与输出 -> 做输入，主调函数分配内存，被调函数使用；做输出，被调函数分配内存，把运算的结果，以指针做函数参数甩出来。
void doublePointTest(int **ppInt, char **ppStr) {
    printf("c printf: in ppInt=%d", **ppInt);
    **ppInt = 10086;

//    *ppStr = "I am from c!"; // 效果和下面一样
    *ppStr = malloc(10 * sizeof(char));
    strcpy(*ppStr, "I am from c!");
}
// 释放内存
void freePoint(void *pt) {
    if (pt != NULL) {
        free(pt);
    }
}
// c 自己调用doublePointTest
//int main() {
//    int a = 100;
//    int *pInt = &a;
//    char *pStr = NULL;
//
//    doublePointTest(&pInt, &pStr);
//
//    printf("after int:%d\n", *pInt);
//    printf("out str:%s\n", pStr);
//
//    //函数中动态申请内存，必须释放
//    freePoint(pStr);
//
//    system("pause");
//}

// 申明结构体
typedef struct {
    long id;
    char *name;
    int age;
} User;

/*
 * 传递结构体
 */
// 传递结构体本身(in)
int setUser(User user) {
    printf("c printf: setUser in id = %ld, name = %s, age = %d\n", user.id, user.name, user.age);
    user.id = 11;
    user.name = "I am from c!";
    user.age = 22;
    // 传递结构体本身(值传递)，上面赋值代码对 C 有效对 Java 无效
    printf("c printf: setUser in id = %ld, name = %s, age = %d\n", user.id, user.name, user.age);
    return 0;
}
// 传递结构体指针(in)
int setUserPoint(User *pUser) {
    printf("c printf: setUserPoint in id = %ld, name = %s, age = %d\n", pUser->id, pUser->name, pUser->age);
    // 传递结构体指针(指针传递)，下面代码有效
    pUser->id = 11;
    pUser->name = "I am from c!";
    pUser->age = 22;
    printf("c printf: setUser in id = %ld, name = %s, age = %d\n", pUser->id, pUser->name, pUser->age);
    return 0;
}
// 获取结构体(out)(由主调者分配内存)
void getUser(User *pUser) {
    pUser->id = 11;
    pUser->name = "I am from c!";
    pUser->age = 22;

    return;
}
// 获取结构体(由被调者分配内存，注意要主动释放)
User * getUser2() {
    User *pUser= malloc(sizeof(User));
    pUser->id = 11;
    pUser->name = "I am from c!";
    pUser->age = 22;

    return pUser;
}
// 释放内存空间
void freeUser(User *pUser) {
    if (pUser != NULL) {
        free(pUser);
        pUser = NULL;
    }
}

/*
 * 传递结构体数组
 */
// 传递结构体数组
int setUserArray(User *pUserArray, int arrayLength) { // 等同于 int readRectArray(Rect[]  RectArray)
    for (int i = 0; i < arrayLength; i++) {
        printf("c printf: in pUserArray:{id=%ld, name=%s, age=%d}\n", pUserArray[i].id, pUserArray[i].name, pUserArray[i].age);
    }
    return 0;
}
// 获取结构体数组（二维数组一维化）---- 这里实现还有点问题
void getUserArray(User **pUserArray, int *pArrayLength) {
    int length = 5;
    *pArrayLength = length;
    User *pArray = malloc(length * sizeof(User));
    for (int i = 0; i < length; i++) {
        pArray[i].id = 100 + (long)i;
        pArray[i].name = "Nicholas Sean";
        pArray[i].age = 18 + i;
    }
    *pUserArray = pArray;

    return;
}
// 获取结构体数组
User * getUserArray2(int *pArrayLength) {
    int length = 5;
    *pArrayLength = length;
    User *pArray = malloc(length * sizeof(User));
    for (int i = 0; i < length; i++) {
        pArray[i].id = 100 + (long)i;
        pArray[i].name = "Nicholas Sean";
        pArray[i].age = 18 + i;
    }
    return pArray;
}
// 释放内存空间
void freeUsers(User *pUser) {
    if (pUser != NULL) {
        free(pUser);
        pUser = NULL;
    }
}

// 申明结构体嵌套
// 如果这里数组字段是 User userArray[100] 的话，在 java 那边该属性对应为 public User.ByValue[] userArray = new User.ByValue[100];
// 如果这里数组字段是 User *userArray[100] 的话，在 java 那边该属性对应为 public User.ByReference[] userArray = new User.ByReference[100];
typedef struct {
    long id;
    char *name;
    User *userArray;
    int userArrayLength;
} Company;

/*
 * 传递复杂结构体
 */
// 传递复杂结构体（待 C 实现）
int setCompany(Company *pCompany) {
    printf("c printf: setCompany in id = %ld, name = %s, userArrayLength = %d\n", pCompany->id, pCompany->name, pCompany->userArrayLength);

    return 0;
}
// 获取复杂结构体
void getCompany(Company *pCompany) {
    int length = 5;
    pCompany->id = 100;
    pCompany->name = "Adup";
    pCompany->userArrayLength = length;
    User *pArray = NULL;
    if (length > 0) { // 此处判断大于0才malloc是为了解决下面memset后还是终止的问题，也就是如果无此判断后面memset后还是会终止。
        pArray = malloc(length * sizeof(User));
    }
    // 当指针指向的数组长度为0时或者指针指向的结构体内部有属性未显示赋值时一定要memset 0，否则内部的指针成员指向随机数（野指针/值）（为新申请的内存做初始化工作）
    // ，当java调用时会出现Null指针异常（SIGSEGV）或者数据异常（不是java数据类型默认值，而是一串数字）。
    // 所以最好在 malloc 后都执行下 memset 。
    /*
     * 函数介绍：
     * void *memset(void *s, int ch, size_t n);
     * 函数解释：将s中当前位置后面的n个字节（typedef unsigned int size_t）用 ch 替换并返回 s 。
     * memset：作用是在一段内存块中填充某个给定的值，它是对较大的结构体或数组进行清零操作的一种最快方法。
     * memset()函数原型是extern void *memset(void *buffer, int c, int count) buffer：为指针或是数组,c：是赋给buffer的值,count：是buffer的长度.
     */
    memset(pArray, 0, sizeof(User) * length); //
    for (int i = 0; i < length; i++) {
        pArray[i].id = 100 + (long)i;
//        pArray[i].name = "Nicholas Sean";
        pArray[i].age = 18 + i;
    }
    pCompany->userArray = pArray;

    return ;
}
// 释放复杂结构体嵌套结构体内存空间
void freeCompanyInternalPointer(Company *pCompany) {
    if (pCompany != NULL) {
        if (pCompany->userArray) {
            free(pCompany->userArray);
            pCompany->userArray = NULL;
        }
    }
}
// C 调用 getCompany
//int main() {
//    Company company;
//    getCompany(&company);
//    printf("company={id=%ld, name=%s, userArrayLength=%d}\n", company.id, company.name, company.userArrayLength);
//    int length = company.userArrayLength;
//    for (int i = 0; i < length; i++) {
//        printf("User={id=%ld, name=%s, age=%d\n", company.userArray[i].id, company.userArray[i].name, company.userArray[i].age);
//    }
//    freeCompanyUInternalPointer(&company);
//}
// 获取复杂结构体
Company * getCompany2() {
    int length = 1;
    Company *pCompany = malloc(sizeof(Company));
    memset(pCompany, 0, sizeof(*pCompany)); // 此处也一样，当分配空间后里面有一个属性未被显示赋值，当java调用时会出现Null指针异常（SIGSEGV）或者数据异常（不是java数据类型默认值，而是一串数字）。
    pCompany->id = 100;
    pCompany->name = "Adup";
    pCompany->userArrayLength = length;
    User *pArray = NULL;
    if (length > 0) {
        pArray = malloc(length * sizeof(User));
    }
    memset(pArray, 0, sizeof(User) * length);
    for (int i = 0; i < length; i++) {
        pArray[i].id = 100 + (long)i;
        pArray[i].name = "Nicholas Sean";
        pArray[i].age = 18 + i;
    }
    pCompany->userArray = pArray;

    return pCompany;
}
// 释放复杂结构体嵌套结构体内存空间
void freeCompany(Company *pCompany) {
    if (pCompany != NULL) {
        if (pCompany->userArray) {
            free(pCompany->userArray);
            pCompany->userArray = NULL;
        }
        free(pCompany);
        pCompany = NULL;
    }
}

/*
 * native 调 java
 */
// 简单 native 调 java
int getValue(int left, int right, int (*fp)(int sum)) {
    fp(left + right);
    return 0;
}
// 异步获取结构体
void asyncGetUser(int (*fetchUser)(User *pUser)) {
    User *pUser = malloc(sizeof(User));
    pUser->id = 11;
    pUser->name = "I am from c!";
    pUser->age = 22;

    fetchUser(pUser);
}
// 异步获取结构体数组
void asyncGetUserArray(int *pArrayLength, void (*fetchUserArray)(User *userArray)) {
    int length = 5;
    *pArrayLength = length;
    User *pArray = malloc(length * sizeof(User));
    for (int i = 0; i < length; i++) {
        pArray[i].id = 100 + (long)i;
        pArray[i].name = "Nicholas Sean";
        pArray[i].age = 18 + i;
    }
    fetchUserArray(pArray);
}
// 异步获取复杂结构体
void asyncGetCompany(void (*fetchCompany)(Company *pCompany)) {
    int length = 5;
    Company *pCompany = malloc(sizeof(Company));
    pCompany->id = 100;
    pCompany->name = "Adup";
    pCompany->userArrayLength = length;
    User *pArray = malloc(length * sizeof(User));
    for (int i = 0; i < length; i++) {
        pArray[i].id = 100 + (long)i;
        pArray[i].name = "Nicholas Sean";
        pArray[i].age = 18 + i;
    }
    pCompany->userArray = pArray;

    fetchCompany(pCompany);
}