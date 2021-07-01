package com.chang.android.aidl.client_server;

import com.chang.android.aidl.service.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: Remote 业务控制器
 * <p>
 * Created by Nicholas Sean on 2021/7/1 4:33 PM.
 *
 * @version 1.0
 */
public class RemoteController {
    private static final String TAG = "RemoteController";

    private List<Person> mPersonList = new ArrayList<>();

    public void addPerson(Person person) {
        synchronized (mPersonList) {
            if (mPersonList == null) {
                mPersonList = new ArrayList<>();
            }

            mPersonList.add(person);
        }
    }

    public void removePerson(int index) {
        synchronized (mPersonList) {
            if (mPersonList == null) {
                mPersonList = new ArrayList<>();
            }

            if (index >= 0 && index < mPersonList.size()) {
                mPersonList.remove(index);
            }
        }
    }

    public void updatePerson(Person person) {
        synchronized (mPersonList) {
            if (mPersonList == null) {
                mPersonList = new ArrayList<>();
            }

            int index = mPersonList.indexOf(person);
            if (index < 0 || index >= mPersonList.size()) {
                return;
            }
            mPersonList.set(index, person);
        }
    }

    public List<Person> queryAll() {
        return mPersonList;
    }
}
