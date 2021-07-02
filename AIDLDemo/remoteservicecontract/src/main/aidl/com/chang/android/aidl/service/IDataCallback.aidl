// IDataCallback.aidl
package com.chang.android.aidl.service;

// Declare any non-default types here with import statements
import com.chang.android.aidl.service.Person;

interface IDataCallback {
    void onDataReady(in List<Person> list);
    void onError(int code, String message);
}