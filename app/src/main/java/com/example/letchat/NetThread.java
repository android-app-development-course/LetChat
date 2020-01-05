package com.example.letchat;

import java.io.DataInputStream;
import java.net.Socket;

public class NetThread extends Thread {
    public void run() {
        try {
            Socket s = new Socket("http://47.240.17.177",5000);
            DataInputStream dataIn = new DataInputStream((s.getInputStream()));
            if (Boolean.parseBoolean(dataIn.readUTF())) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
