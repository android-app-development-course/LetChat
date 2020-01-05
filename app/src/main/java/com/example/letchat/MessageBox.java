package com.example.letchat;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.LayoutInflater;

public class MessageBox extends RelativeLayout {
    private ImageView avatar_icon;
    private TextView message_content;

    public MessageBox(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.message_box, this);

        avatar_icon = (ImageView) findViewById(R.id.avatar_icon);
        message_content = (TextView) findViewById(R.id.message_content);
    }

    public MessageBox(Context context, AttributeSet attrs) {
        super(context,attrs);

        /*LayoutInflater.from(context).inflate(R.layout.message_box, this,false);

        avatar_icon = (ImageView) findViewById(R.id.avatar_icon);
        message_content = (TextView) findViewById(R.id.message_content);*/
    }

    public void setAvatar_icon() {}

    public void setMessage_content(String str) {
        message_content.setText(str);
    }
}
