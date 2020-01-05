package com.example.letchat;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import android.os.Message;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements TextWatcher {
    private Context context;
    private EditText accountBox;
    private EditText passwordBox;
    private Button loginButton;
    private ImageView loginButtonIcon;
    private MyHelper myHelper;
    private MyApplication app;
    private Date date;
    private View view;
    private ListView mListView;
    private MyBaseAdapter mAdapter;
    private SQLiteDatabase db;
    private ObjectOutputStream m_output;
    private ObjectInputStream m_input;
    private LinearLayout messageView;
    private ScrollView svResult;

    protected static final int LOGIN = 1;
    protected static final int ERROR = 2;
    protected static final int ADD = 3;
    protected static final int MESSAGE = 4;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == LOGIN) {
                String id = accountBox.getText().toString().trim();
                String password = passwordBox.getText().toString().trim();
                boolean isSaveSuccess = FileSaveUserInfo.saveUserInfo(getApplicationContext(), id, password);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.CHAT");
                startActivity(intent);
            }

            if (msg.what == ERROR) {
                loginButtonIcon.setBackgroundResource(R.drawable.login_button_ico);
                loginButton.clearAnimation();
                loginButtonIcon.clearAnimation();
            }

            if (msg.what == ADD) {
                String sid = String.valueOf(msg.obj);
                long senderID = Long.parseLong(sid);
                myHelper.addFriend(senderID,"陈子怡",12435464,"你们已添加好友，开始聊天吧！");
                mAdapter = app.getmAdapter();
                mAdapter.addItem(senderID,"陈子怡","你们已添加好友，开始聊天吧！");
                mAdapter.notifyDataSetChanged();
                myHelper.addChatTable(senderID);
            }

            if (msg.what == MESSAGE) {
                mAdapter = app.getmAdapter();
                String[] n = (String[]) msg.obj;
                long id = Long.parseLong(n[1]);
                myHelper.insertChatRecord(n[1],"0", n[3], date.getTime());
                myHelper.motifyLastRecord(id,n[3]);
                mAdapter.motifyLastRecord(n[1],n[3]);
                mAdapter.notifyDataSetChanged();

                //更新到聊天页面
                messageView = app.getMessageView();
                svResult = app.getSvResult();
                if (messageView != null && svResult != null) {
                    LeftMessageBox a = new LeftMessageBox(getApplicationContext());
                    a.setMessage_content(n[3]);
                    messageView.addView(a);
                    svResult.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            svResult.post(new Runnable() {
                                public void run() {
                                    svResult.fullScroll(View.FOCUS_DOWN);
                                }
                            });
                        }
                    });
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        init();
        passwordBox.setTypeface(Typeface.DEFAULT);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void init() {
        app = (MyApplication) this.getApplicationContext();
        myHelper = new MyHelper(this);
        db = myHelper.getWritableDatabase();
        db.close();

        date = new Date();

        accountBox = (EditText) findViewById(R.id.account_box);
        passwordBox = (EditText) findViewById(R.id.password_box);
        loginButton = (Button) findViewById(R.id.login_button);
        loginButtonIcon = (ImageView) findViewById(R.id.login_button_icon);

        accountBox.addTextChangedListener(this);
        passwordBox.addTextChangedListener(this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButtonIcon.setBackgroundResource(R.drawable.login_button_refresh);
                Animation scale = AnimationUtils.loadAnimation(MainActivity.this, R.anim.login_button_scale);
                loginButton.startAnimation(scale);
                Animation rotate = AnimationUtils.loadAnimation(MainActivity.this, R.anim.login_button_rotate);
                loginButtonIcon.startAnimation(rotate);
                click();
            }
        });
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String accountStr = accountBox.getText().toString().trim();
        String passwordStr = passwordBox.getText().toString().trim();
        if (!accountStr.isEmpty() && !passwordStr.isEmpty()) {
            loginButton.setEnabled(true);
        } else {
            loginButton.setEnabled(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { ;
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public void click() {
        final String id = accountBox.getText().toString().trim();
        final String password = passwordBox.getText().toString().trim();
        final String loginMsg = "login" + "," + id + "," + password;

        new Thread() {
            public void run() {
                try {
                    Socket s = app.createSocket();
                    String m;
                    String n[];
                    Object obj;
                    m_output = new ObjectOutputStream(s.getOutputStream());
                    m_input = new ObjectInputStream(s.getInputStream());
                    app.setObjectOutputStream(m_output);
                    app.setObjectInputStream(m_input);

                    m_output.writeObject(loginMsg);
                    m_output.flush();
                    /*m_output.writeObject(null);
                    m_output.flush();*/

                    while (true) {
                        if ((obj = m_input.readObject()) != null) {
                            m = (String) obj;
                            n = m.split(",");

                            switch (n[0]) {
                                case "login":
                                    if (Boolean.parseBoolean(n[1])) {
                                        Message msg = new Message();
                                        msg.what = LOGIN;
                                        handler.sendMessage(msg);
                                    }
                                    else {
                                        Message msg = new Message();
                                        msg.what = ERROR;
                                        handler.sendMessage(msg);
                                    }
                                    break;

                                //好友请求报文格式："add,请求方ID,被请求方ID"
                                case "add":
                                    //Toast.makeText(getApplicationContext(),"add",Toast.LENGTH_SHORT).show();
                                    Message add = new Message();
                                    add.what = ADD;
                                    add.obj = n[1];
                                    handler.sendMessage(add);
                                    break;

                                //聊天消息报文格式："message,发送方ID,接收方ID,聊天信息"
                                case "message":
                                    Message message = new Message();
                                    message.what = MESSAGE;
                                    message.obj = n;
                                    handler.sendMessage(message);
                                    break;

                                default:
                                    //Toast.makeText(getApplicationContext(),"what?",Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
