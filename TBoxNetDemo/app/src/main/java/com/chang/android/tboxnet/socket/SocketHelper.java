package com.chang.android.tboxnet.socket;

import android.os.Handler;
import android.os.Looper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Socket connect.
 * https://blog.csdn.net/czx2018/article/details/113919839
 *
 * 1. 在不设置连接超时时间的情况下，socket默认的超时时间应该是20毫秒左右
 * 2. 在不设置setSoTimeout的话，默认的io流读写超时时间大概是120秒
 * <p>
 * Created by Nicholas Sean on 2021/8/4 4:20 PM.
 *
 * @version 1.0
 */
public class SocketHelper {

    private static final int CONNECT_TIME_OUT = 15000; // 连接超时时间
    private static final int SO_TIME_OUT = 10000; // 读写超时时间

    private static LogListener mLogListener;
    private static StringBuilder mLogBuilder = new StringBuilder();
    private static Handler mMainHandler = new Handler(Looper.getMainLooper());

    /**
     * Socket 链接，必须在子线程中执行
     *
     * @param host
     * @param port
     */
    public static void connect(String host, int port) {
        String log;
        String serverName = host;
        Socket client = null;
        try {
            log = "Connecting to " + serverName + " on port " + port;
            notifyLog(log);
            try {
                // 无设置超时时间入口，内部底层有超时时间
//                client = new Socket(serverName, port);
                // 可以主动设置超时时间
                client = new Socket();
                client.connect(new InetSocketAddress(host, port), CONNECT_TIME_OUT); // 设置连接超时毫秒
                client.setSoTimeout(SO_TIME_OUT); // 设置io读取/写出超时时间
            } catch (IOException e) {
                log = e.getMessage();
                notifyLog(log);
                e.printStackTrace();
            }

            log = "Just connected to " + client.getRemoteSocketAddress();
            notifyLog(log);
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            out.writeUTF("Hello from " + client.getLocalSocketAddress());
            out.flush();
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);

            log = "Server says " + in.readUTF();
            notifyLog(log);
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void notifyLog(String log) {
        System.out.println(log);
        mLogBuilder.append(log).append("\n");
        if (isMainThread()) {
            if (mLogListener != null) {
                mLogListener.onLog(mLogBuilder.toString());
            }
        } else {
            mMainHandler.post(() -> {
                if (mLogListener != null) {
                    mLogListener.onLog(mLogBuilder.toString());
                }
            });
        }
    }

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static void setLogListener(LogListener logListener) {
        mLogListener = logListener;
    }

    public interface LogListener {
        void onLog(String log);
    }

    public static void main(String [] args) {
        connect("192.168.0.6", 9012);
    }
}
