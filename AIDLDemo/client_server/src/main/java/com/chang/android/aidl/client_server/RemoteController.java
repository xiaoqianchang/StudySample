package com.chang.android.aidl.client_server;

import android.os.RemoteException;

import com.chang.android.aidl.service.IDataCallback;
import com.chang.android.aidl.service.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 服务端代理类，进行具体业务控制.
 * <p>
 * Created by Nicholas Sean on 2021/7/1 4:33 PM.
 *
 * @version 1.0
 */
public class RemoteController {
    private static final String TAG = "RemoteController";

    private List<Person> mPersonList = new ArrayList<>();
    private IDataCallback mDataCallback;

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

    public void loadAll(IDataCallback callback) {
        if (callback != null) {
            try {
                callback.onDataReady(mPersonList);
            } catch (RemoteException e) {
                e.printStackTrace();
                dataCallbackOnError(callback, 400, "加载失败");
            }
        }
    }

    private void dataCallbackOnError(IDataCallback callback, int i, String message) {
        if (callback != null) {
            try {
                callback.onError(-1, "error");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void setDataCallback(IDataCallback callback) {
        mDataCallback = callback;
    }
}
