// IRemoteService.aidl
package com.chang.android.aidl.service;

// Declare any non-default types here with import statements
import com.chang.android.aidl.service.Person;
import com.chang.android.aidl.service.IDataCallback;
import com.chang.android.aidl.service.IPlayerEventDispatcher;

interface IRemoteService {

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