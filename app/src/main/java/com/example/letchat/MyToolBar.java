package com.example.letchat;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyToolBar extends Toolbar {
    private TextView pageRemark;
    private Button pageBack;

    public MyToolBar (Context context, AttributeSet attrs) {
        super(context,attrs);
        initView(context,attrs);
    }

    private void initView(final Context context, AttributeSet attributeSet) {
        LayoutInflater.from(context).inflate(R.layout.toolbar_page, this);

        pageRemark = (TextView) findViewById(R.id.page_remark);
        pageBack = (Button) findViewById(R.id.button_back);

        init(context, attributeSet);
    }

    public void init(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.MyToolBar);
        String remark = typedArray.getString(R.styleable.MyToolBar_remark);

        pageRemark.setText(remark);
    }

    public void setLeftIconOnClickListener(OnClickListener l) {
        pageBack.setOnClickListener(l);
    }

    public void setRightIconOnClickListener(OnClickListener l) {
        pageRemark.setOnClickListener(l);
    }
}
