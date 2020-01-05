package com.example.letchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.List;

public class MyBaseAdapter extends BaseSwipeAdapter {
    private Context mContext;
    private List<String> names;
    private List<String> _id;
    private List<String> lastRecord;
    private List<Integer> icons;
    private MyHelper myHelper;
    private MyApplication app;
    private MyBaseAdapter mAdapter;

    public MyBaseAdapter(Context mContext, List<String> names, List<String> _id, List<String> lastRecord, List<Integer> icons) {
        this.mContext = mContext;
        this.names = names;
        this._id = _id;
        this.lastRecord = lastRecord;
        this.icons = icons;
        myHelper = new MyHelper(mContext);
        app = (MyApplication) mContext.getApplicationContext();
    }

    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public View generateView(int position, ViewGroup parent) {
        return LayoutInflater.from(mContext).inflate(R.layout.list_item, null);
    }

    public void fillValues(int position, View convertView) {
        final int pos = position;
        TextView mTextView = convertView.findViewById(R.id.item_tv);
        TextView mTextView1 = convertView.findViewById(R.id.item_tv1);
        mTextView.setText(names.get(position));
        mTextView1.setText(lastRecord.get(position));
        ImageView imageView = convertView.findViewById(R.id.item_image);
        imageView.setBackgroundResource(icons.get(position));
        SwipeLayout swipeLayout =  (SwipeLayout)convertView.findViewById(R.id.swipe);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        //删除item
        LinearLayout bottomView = (LinearLayout) convertView.findViewById(R.id.bottomview);
        bottomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = Long.parseLong(_id.get(pos));
                myHelper.deleteFriend(id);
                names.remove(pos);
                _id.remove(pos);
                lastRecord.remove(pos);
                icons.remove(pos);
                mAdapter = app.getmAdapter();
                mAdapter.notifyDataSetChanged();
            }
        });

    }


    public int getCount() {
        return names.size();
        //return names.length;
    }

    public Object getItem(int position) {
        return names.get(position);
        //return names[position];
    }

    public long getItemId(int position) {
        return Long.parseLong(_id.get(position));
    }

    public void addItem(long id, String name, String lastRecord) {
        String i = String.valueOf(id);
        _id.add(0,i);
        names.add(0,name);
        this.lastRecord.add(0,lastRecord);
    }

    public void motifyLastRecord(String id, String lastRecord) {
        if (_id != null) {
            for (int i = 0; i < _id.size(); ++i) {
                if (id.equals(_id.get(i))) {
                    this.lastRecord.set(i, lastRecord);
                }
            }
        }
    }
}
