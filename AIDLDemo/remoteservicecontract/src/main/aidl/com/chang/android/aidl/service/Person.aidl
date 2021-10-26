// Person.aidl
// 第一类AIDL文件的例子
// 这个文件的作用是引入了一个序列化对象 Person 供其他的AIDL文件使用
// 注意：Person.aidl与Person.java的包名应当是一样的
package com.chang.android.aidl.service;

// Declare any non-default types here with import statements
// 导入所需要使用的非默认支持数据类型的包
// 注意parcelable是小写
parcelable Person;