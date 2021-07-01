// IRemoteService.aidl
package com.chang.android.aidl.service;

// Declare any non-default types here with import statements
import com.chang.android.aidl.service.Person;

interface IRemoteService {

    void addPerson(in Person person);
    void removePerson(int index);
    void updatePerson(in Person person);
    Person query(String name);
    List<Person> queryAll();
}