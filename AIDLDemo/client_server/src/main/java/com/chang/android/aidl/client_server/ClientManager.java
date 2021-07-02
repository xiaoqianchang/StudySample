package com.chang.android.aidl.client_server;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.chang.android.aidl.service.IDataCallback;
import com.chang.android.aidl.service.IPlayerEventDispatcher;
import com.chang.android.aidl.service.IRemoteService;
import com.chang.android.aidl.service.Person;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * Description: 客服端与服务端交互控制器.
 * <p>
 * Created by Nicholas Sean on 2021/7/2 5:56 PM.
 *
 * @version 1.0
 */
public class ClientManager {
    private final String TAG = "ClientManager";

    private static volatile ClientManager sInstance;
    private static byte[] sLock = new byte[0];
    private IRemoteService mRemoteService;
    private Context mAppCtx;
    private boolean mConnected = false;
    private boolean mBindRet = false;
    private IDataCallback mDataCallback;

    private ClientManager() {}

    public static ClientManager getInstance() {
        if (sInstance == null) {
            synchronized (sLock) {
                if (sInstance == null) {
                    sInstance = new ClientManager();
                }
            }
        }
        return sInstance;
    }
    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                Log.d(TAG, "onServiceConnected()");
                mConnected = true;
                mBindRet = true;
                mRemoteService = IRemoteService.Stub.asInterface(service);
                mRemoteService.registerPlayerListener(mListenerStub);

                mRemoteService.setDataCallback(mDataCallbackSub);
                Toast.makeText(mAppCtx, "绑定远程服务成功", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            try {
                Log.d(TAG, "onServiceDisconnected()");
                mConnected = false;
                mBindRet = false;
                mRemoteService.unregisterPlayerListener(mListenerStub);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private IPlayerEventDispatcher.Stub mListenerStub = new IPlayerEventDispatcher.Stub() {
        @Override
        public void onBufferingStart() throws RemoteException {

        }

        @Override
        public void onBufferProgress(int percent) throws RemoteException {

        }

        @Override
        public void onBufferingStop() throws RemoteException {

        }

        @Override
        public void onSoundPrepared() throws RemoteException {

        }

        @Override
        public void onPlayStart() throws RemoteException {

        }

        @Override
        public void onPlayPause() throws RemoteException {

        }

        @Override
        public void onPlayStop() throws RemoteException {

        }

        @Override
        public void onSoundPlayComplete() throws RemoteException {

        }

        @Override
        public void onSoundSwitch() throws RemoteException {

        }

        @Override
        public void onPlayProgress(int currPos, int duration) throws RemoteException {

        }

        @Override
        public void onError(int code, String message) throws RemoteException {

        }
    };

    private IDataCallback.Stub mDataCallbackSub = new IDataCallback.Stub() {
        @Override
        public void onDataReady(List<Person> list) throws RemoteException {
            if (mDataCallback != null) {
                mDataCallback.onDataReady(list);
            }
        }

        @Override
        public void onError(int code, String message) throws RemoteException {
            if (mDataCallback != null) {
                mDataCallback.onError(code, message);
            }
        }
    };

    public void setDataCallback(IDataCallback callback) {
        mDataCallback = callback;
    }

    public void init(@NonNull Context context) {
        mAppCtx = context.getApplicationContext();
        try {
            Intent intent = RemoteService.getIntent(context);
            mAppCtx.startService(intent);
            mAppCtx.bindService(intent, mConn, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onAddPerson(@NotNull Person person) {
        try {
            mRemoteService.addPerson(person);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void removePerson(int index) {
        try {
            mRemoteService.removePerson(0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void updatePerson(@NotNull Person person) {
        try {
            mRemoteService.updatePerson(person);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public List<Person> queryAll() {
        try {
            return mRemoteService.queryAll();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadAll(IDataCallback callback) {
        try {
            mRemoteService.loadAll(callback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void release() {
        if (sInstance != null) {
            if (sInstance.mBindRet ||
                    (sInstance.mRemoteService != null
                            && sInstance.mRemoteService.asBinder() != null
                            && sInstance.mRemoteService.asBinder().isBinderAlive()))
            sInstance.mAppCtx.unbindService(sInstance.mConn);
            sInstance.mBindRet = false;
        }
        sInstance.mAppCtx.stopService(RemoteService.getIntent(sInstance.mAppCtx));
        sInstance.mConnected = false;
    }
}
