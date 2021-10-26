// IRemoteService.aidl
// 第二类AIDL文件的例子
package com.chang.android.aidl.service;

// Declare any non-default types here with import statements
// 导入所需要使用的非默认支持数据类型的包
import com.chang.android.aidl.service.Person;
import com.chang.android.aidl.service.IDataCallback;
import com.chang.android.aidl.service.IPlayerEventDispatcher;

interface IRemoteService {

    // 所有的返回值前都不需要加任何东西，不管是什么数据类型
    // 传参时除了Java基本类型、String和CharSequence之外的类型，都需要在前面加上定向tag，具体加什么量需而定
    void addPerson(in Person person);
    void removePerson(int index);
    void updatePerson(in Person person);
    Person query(String name);

    List<Person> queryAll(); // 同步
    void loadAll(IDataCallback callback); // 异步

    // 设置回调
    void setDataCallback(IDataCallback callback);

    // 注册与反注册 listener
    void registerPlayerListener(IPlayerEventDispatcher l);
    void unregisterPlayerListener(IPlayerEventDispatcher l);
}