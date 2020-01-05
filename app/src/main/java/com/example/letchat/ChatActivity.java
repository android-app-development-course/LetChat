package com.example.letchat;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;


public class ChatActivity extends FragmentActivity {
    private MyViewPager mPager;
    private List<Fragment> fragmentList;
    private RadioGroup bottomBar;
    private ObjectInputStream m_input;

    private Context context;
    private MyHelper myHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new MessageFragment());
        fragments.add(new SweepFragment());
        fragments.add(new UserFragment());
        FragAdapter adapter = new FragAdapter(getSupportFragmentManager(), fragments);

        mPager = (MyViewPager) findViewById(R.id.viewpager);
        mPager.setAdapter(adapter);

        bottomBar = (RadioGroup) findViewById(R.id.bottom_bar);
        bottomBar.bringToFront();

        bottomBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.message:
                        mPager.setCurrentItem(0,false);
                        break;
                    case R.id.sweep:
                        mPager.setCurrentItem(1,false);
                        break;
                    case R.id.user:
                        mPager.setCurrentItem(2,false);
                        break;
                }
            }

        });

        myHelper = new MyHelper(this);
        //db = myHelper.getWritableDatabase();
        /*String record = "create table if not exists v564175191("
                + "num varchar(10)," + "message text,"
                + "time integer)";
        db.execSQL(record);*/
        //insert(564175390,"李俊义",123426789,"拜拜");
        //insert(564335390,"林鸿沛",1226789,"明天下午我不去那里了，不用等我了你直接去就行了");
        //insert(564175210,"罗凯翔",12342689,"找我干嘛");
        //insert(564755390,"杨元宵",13426789,"今天英雄联盟更新了");
        //insert(564179990,"张岚清",123422789,"憨憨");
        //insert(564115390,"范家铭",121426789,"阿铭");
        //insert(564121340,"张达庆",123446719,"明天不用去上课了");
        insert(564175191,"测试者",121421719,"");
        myHelper.addChatTable(564175191);
        //insert(714452460,"赵志高",123456763,"大学城");
        //insert(1719224963,"张一峻",123136755,"搞笑呢");
        //insert(1719224902,"黄旭星",902313675,"你几时回来哦");
        //insert(1719444902,"陈旭",903313675,"没什么");
        //db.close();
        /*ins("0","明天有没有空？",123425322);
        ins("1","干嘛",12342232);
        ins("0","想出去玩了",421232124);
        ins("1","我也想出去玩了，在学校太无聊了",421232224);
        ins("0","要不去顺德玩吧，比较近而且吃的多哈哈哈",421232121);
        ins("1","可以啊 那大概多少点",521232124);
        ins("0","我明天就上午有两节课",621232124);
        ins("1","我明天上午三节课 那12点出发？",721232124);
        ins("0","可以",821232124);
        ins("0","嗨星！！！",921232124);*/



        /*new Thread() {
            public void run() {
                try {
                    Socket s = new Socket("47.240.17.177",5001);
                    String[] m;
                    m_input = new ObjectInputStream(s.getInputStream());
                    while (true) {
                        m = (String[]) m_input.readObject();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();*/
    }

    public class FragAdapter extends FragmentPagerAdapter {
        public FragAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            fragmentList = fragments;
        }

        public Fragment getItem(int arg0) {
            return fragmentList.get(arg0);
        }

        public int getCount() {
            return fragmentList.size();
        }
    }

    /*class MyHelper extends SQLiteOpenHelper {
        public static final String friendList = "create table friendList ("
                + "_id integer primary key autoincrement," + "name varchar(20),"
                + "time integer," + "lastRecord text)";

        public MyHelper (Context context) {
            super(context, "LetChat.db",null,1);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(friendList);
        }


        public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {}
    }*/

    public void insert(long _id, String name, long time, String lastRecord) {
        myHelper = new MyHelper(this);
        SQLiteDatabase db = myHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_id",_id);
        values.put("name",name);
        values.put("time",time);
        values.put("lastRecord",lastRecord);
        long id = db.insert("friendList",null,values);
        db.close();
    }

    public void ins(String num, String message, long time) {
        SQLiteDatabase db = myHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("num",num);
        values.put("message",message);
        values.put("time",time);
        long id = db.insert("v564175191",null,values);
        db.close();
    }
}


