package com.example.letchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class UserInfo extends AppCompatActivity {
    private RelativeLayout infoSendMsg;
    private MyHelper myHelper;
    private MyBaseAdapter mAdapter;
    private MyApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_info);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        Intent intent = getIntent();
        String receiverID = intent.getStringExtra("receiverID");
        final long recID = Long.parseLong(receiverID);

        myHelper = new MyHelper(this);
        app = (MyApplication) this.getApplicationContext();
        mAdapter = app.getmAdapter();

        infoSendMsg = (RelativeLayout) findViewById(R.id.info_sendmsg);
        infoSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myHelper.addFriend(recID,"陈子怡",12435464,"你们已添加好友，开始聊天吧！");
                mAdapter = app.getmAdapter();
                mAdapter.addItem(recID,"陈子怡","你们已添加好友，开始聊天吧！");
                mAdapter.notifyDataSetChanged();

                myHelper.addChatTable(recID);

                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();

                /*Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                        startActivity(intent);
                    }
                };
                timer.schedule(task, 2000);*/
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(intent);
            }
        });
    }
}
