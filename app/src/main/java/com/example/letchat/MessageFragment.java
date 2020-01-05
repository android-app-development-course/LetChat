package com.example.letchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment implements AdapterView.OnItemClickListener {
    private boolean isGetData = false;
    private Context context;
    private MyHelper myHelper;
    private View view;
    private ListView mListView;
    private List<String> names;
    private List<String> _id;
    private List<String> lastRecord;
    private List<Integer> icons = new ArrayList<Integer>();

    private MyBaseAdapter mAdapter;
    private MyApplication app;

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        super.onPause();
        isGetData = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        myHelper = new MyHelper(getActivity());
        app = (MyApplication) getActivity().getApplicationContext();
        //SQLiteDatabase db = myHelper.getReadableDatabase();

        names = myHelper.findColumn("name");
        _id = myHelper.findColumn("_id");
        lastRecord = myHelper.findColumn("lastRecord");
        for (int i = 0; i < 14; ++i) {
            icons.add(R.mipmap.ic_launcher_round);
        }

        view = inflater.inflate(R.layout.fragment_message, container, false);
        mListView = view.findViewById(R.id.chat_list);
        mAdapter = new MyBaseAdapter(getActivity(),names,_id,lastRecord,icons);
        app.setmAdapter(mAdapter);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //进入当前Fragment
        if (enter && !isGetData) {
            isGetData = true;
            names = myHelper.findColumn("name");
            _id = myHelper.findColumn("_id");
            lastRecord = myHelper.findColumn("lastRecord");
            mAdapter.notifyDataSetChanged();
        } else {
            isGetData = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long getId = mAdapter.getItemId(position);
        switch (position) {
            default:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.PAGE");
                intent.putExtra("remark",names.get(position));
                intent.putExtra("id",String.valueOf(getId));
                startActivity(intent);
                break;
        }
    }

    /*public String findRemark(String column, long id) {
        //List<String> col = new ArrayList<String>();
        String col = String.valueOf(id);
        String result = "";
        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor = db.query("friendList",null,null,null,null,null,null);
        //没有写如果数据库为空的情况
        cursor.moveToFirst();
        do{
            String str = cursor.getString(cursor.getColumnIndex(column));
            if (col.equals(str)) {
                result = cursor.getString(cursor.getColumnIndex("name"));
                return result;
            }
        } while(cursor.moveToNext());
        db.close();
        return result;
    }*/
}
