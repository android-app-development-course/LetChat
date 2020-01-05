package com.example.letchat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ObjectOutputStream;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SweepFragment extends Fragment {
    private View view;
    private EditText edit;
    private Button btn;
    private String number;
    private MyApplication app;
    private ObjectOutputStream m_output;
    private MyHelper myHelper;
    private MyBaseAdapter mAdapter;

    public SweepFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        myHelper = new MyHelper(getActivity());
        view = inflater.inflate(R.layout.fragment_sweep, container, false);
        app = (MyApplication) getActivity().getApplicationContext();
        m_output = app.getObjectOutputStream();

        edit = (EditText) view.findViewById(R.id.edit);
        btn = (Button) view.findViewById(R.id.btn);


        Map<String, String> userInfo = FileSaveUserInfo.getUserInfo(getActivity());
        number = userInfo.get("number");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String receiverID = edit.getText().toString();
                long recID = Long.parseLong(receiverID);
                final String message = "add," + number + "," + receiverID;

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            m_output.writeObject(message);
                            m_output.flush();
                            /*m_output.writeObject(null);
                            m_output.flush();*/
                        } catch (Exception e) {
                            System.err.println("发生异常：" + e);
                            e.printStackTrace();
                        }
                    }
                }.start();

                /*myHelper.addFriend(recID,"陈子怡",12435464,"你们已添加好友，开始聊天吧！");
                mAdapter = app.getmAdapter();
                mAdapter.addItem(recID,"陈子怡","你们已添加好友，开始聊天吧！");
                mAdapter.notifyDataSetChanged();

                myHelper.addChatTable(recID);*/

                //Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), UserInfo.class);
                intent.putExtra("receiverID", receiverID);
                startActivity(intent);
            }
        });

        return view;
    }
}
