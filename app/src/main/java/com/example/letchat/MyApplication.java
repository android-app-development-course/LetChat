package com.example.letchat;

import android.app.Application;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MyApplication extends Application {
    private ScrollView svResult;
    private LinearLayout messageView;
    private ObjectInputStream m_input;
    private ObjectOutputStream m_output;
    private MyBaseAdapter mAdapter;
    private Socket socket;

    public void onCreate() {
        super.onCreate();
    }

    public Socket createSocket() {
        try {
            socket = new Socket("47.240.17.177",5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return socket;
    }

    public void setSvResult(ScrollView svResult) {
        this.svResult = svResult;
    }

    public ScrollView getSvResult() {
        return svResult;
    }

    public void setMessageView(LinearLayout messageView) {
        this.messageView = messageView;
    }

    public LinearLayout getMessageView() {
        return messageView;
    }

    public ObjectInputStream getObjectInputStream() {
        return m_input;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return m_output;
    }

    public void setObjectInputStream(ObjectInputStream m_input) {
        this.m_input = m_input;
    }

    public void setObjectOutputStream(ObjectOutputStream m_output) {
        this.m_output = m_output;
    }

    public MyBaseAdapter getmAdapter() {
        return mAdapter;
    }

    public void setmAdapter(MyBaseAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }
}
