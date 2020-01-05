package com.example.letchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.Map;


public class PageActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView pageRemark;
    private EditText inputBox;
    private Button sendButton;
    private LinearLayout messageView;
    private MessageBox messageBox;
    private String messageContent;
    private ScrollView svResult;
    private MyHelper myHelper;
    private Date date;
    private MyApplication app;
    private ObjectOutputStream m_output;
    private MyBaseAdapter mAdapter;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_page);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        app = (MyApplication) this.getApplicationContext();

        Intent intent = getIntent();
        String remark = intent.getStringExtra("remark");
        final String id = intent.getStringExtra("id");

        //pageRemark = (TextView) findViewById(R.id.page_remark);
        inputBox = (EditText) findViewById(R.id.input_box_page);
        sendButton = (Button) findViewById(R.id.send_button);
        messageView = (LinearLayout) findViewById(R.id.message_view);
        svResult = (ScrollView) findViewById(R.id.scrollview_page);
        app.setMessageView(messageView);
        app.setSvResult(svResult);

        date = new Date();

        //获取用户的账号和密码
        Map<String, String> userInfo = FileSaveUserInfo.getUserInfo(this);
        final String number = userInfo.get("number");

        //获取输出
        m_output = app.getObjectOutputStream();

        //获取聊天记录
        String tableName = "v"+ id;
        myHelper = new MyHelper(this);
        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor = db.query(tableName,null,null,null,null,null,null);
        if (cursor.moveToFirst()) {
            do {
                String num = cursor.getString(cursor.getColumnIndex("num"));
                messageContent = cursor.getString(cursor.getColumnIndex("message"));
                if (num.equals("0")) {
                    LeftMessageBox a = new LeftMessageBox(PageActivity.this);
                    a.setMessage_content(messageContent);
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
                    //svResult.fullScroll(ScrollView.FOCUS_DOWN);
                } else {
                    MessageBox b = new MessageBox(PageActivity.this);
                    b.setMessage_content(messageContent);
                    messageView.addView(b);
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
                    //svResult.fullScroll(ScrollView.FOCUS_DOWN);
                }
            } while(cursor.moveToNext());
        }
        db.close();

        inputBox.setMaxLines(4);
        inputBox.setHorizontallyScrolling(false);

        sendButton.setOnClickListener(this);
        inputBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                switch (i) {
                    case EditorInfo.IME_ACTION_SEND:
                        messageContent = inputBox.getText().toString();
                        MessageBox a = new MessageBox(PageActivity.this);
                        a.setMessage_content(messageContent);
                        messageView.addView(a);
                        //可能会导致移至最低端后addview还没添加
                        //svResult.fullScroll(ScrollView.FOCUS_DOWN);
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
                        inputBox.setText("");

                        //添加至数据库
                        myHelper.insertChatRecord(id,"1",messageContent,date.getTime());

                        //修改friendList表的lastRecord值
                        long _id = Long.parseLong(id);
                        myHelper.motifyLastRecord(_id, messageContent);
                        mAdapter = app.getmAdapter();
                        mAdapter.motifyLastRecord(id, messageContent);
                        mAdapter.notifyDataSetChanged();

                        //发送聊天报文
                        final String msg = "message," + number + "," + id + "," + messageContent;
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    m_output.writeObject(msg);
                                    m_output.flush();
                                    /*m_output.writeObject(null);
                                    m_output.flush();*/
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.input_box_page:
                messageContent = inputBox.getText().toString();
                MessageBox a = new MessageBox(PageActivity.this);
                a.setMessage_content(messageContent);
                messageView.addView(a);
                //可能会导致移至最低端后addview还没添加
                //svResult.fullScroll(ScrollView.FOCUS_DOWN);
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
                inputBox.setText("");
                break;*/
            default:
                break;
        }
    }

    public void click(View v) {
        finish();
    }

}
