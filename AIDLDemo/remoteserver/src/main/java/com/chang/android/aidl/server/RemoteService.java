package com.chang.android.aidl.server;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.chang.android.aidl.service.IDataCallback;
import com.chang.android.aidl.service.IPlayerEventDispatcher;
import com.chang.android.aidl.service.IRemoteService;
import com.chang.android.aidl.service.MyRemoteCallbackList;
import com.chang.android.aidl.service.Person;

import java.util.List;

/**
 * Description: $
 * <p>
 * Created by Nicholas Sean on 2021/7/1 3:40 PM.
 *
 * @version 1.0
 */
public class RemoteService extends Service {
    private static final String TAG = "RemoteService";

    private RemoteCallbackList<IPlayerEventDispatcher> mPlayerDispatcher = new MyRemoteCallbackList<>();

    private RemoteServiceImpl mRemoteServiceImpl;
    private RemoteController mRemoteController;

    public static final Intent getIntent(Context ctx) {
        Intent intent = new Intent(ctx, RemoteService.class);
        intent.setPackage("com.chang.android.aidl.server"); // 此处的包名应该为远程服务所在的应用程序包名称
        return intent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        Toast.makeText(this, "远程服务开启", Toast.LENGTH_SHORT).show();

        initRemoteService();
    }

    private void initRemoteService() {
        if (mRemoteServiceImpl == null) {
            mRemoteServiceImpl = new RemoteServiceImpl();
        }
        if (mRemoteController == null) {
            mRemoteController = new RemoteController();
        }
    }

    public RemoteServiceImpl getRemoteServiceImpl () {
        return mRemoteServiceImpl;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initRemoteService();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        initRemoteService();
        return mRemoteServiceImpl;
    }

    class RemoteServiceImpl extends IRemoteService.Stub {
        @Override
        public void addPerson(Person person) throws RemoteException {
            if (mRemoteController != null) {
                mRemoteController.addPerson(person);
            }
        }

        @Override
        public void removePerson(int index) throws RemoteException {
            if (mRemoteController != null) {
                mRemoteController.removePerson(index);
            }
        }

        @Override
        public void updatePerson(Person person) throws RemoteException {
            if (mRemoteController != null) {
                mRemoteController.updatePerson(person);
            }
        }

        @Override
        public Person query(String name) throws RemoteException {
            return null;
        }

        @Override
        public List<Person> queryAll() throws RemoteException {
            if (mRemoteController != null) {
                return mRemoteController.queryAll();
            }
            return null;
        }

        @Override
        public void loadAll(IDataCallback callback) throws RemoteException {
            if (mRemoteController != null) {
                mRemoteController.loadAll(callback);
            }
        }

        @Override
        public void setDataCallback(IDataCallback callback) throws RemoteException {
            mRemoteController.setDataCallback(callback);
        }

        @Override
        public void registerPlayerListener(IPlayerEventDispatcher l) throws RemoteException {
            if (l != null && mPlayerDispatcher != null) {
                mPlayerDispatcher.register(
                        l,
                        new MyRemoteCallbackList.ProcessCookie(Binder
                                .getCallingPid(), Binder.getCallingUid()));
            }
        }

        @Override
        public void unregisterPlayerListener(IPlayerEventDispatcher l) throws RemoteException {
            if (l != null && mPlayerDispatcher != null) {
                mPlayerDispatcher.unregister(l);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        Toast.makeText(this, "远程服务关闭", Toast.LENGTH_SHORT).show();

    }
}
