package com.example.hong.alchul.manager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hong.alchul.R;
import com.example.hong.alchul.parttime.MyFragment1;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.startActivity;

public class UserAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<UserItem> data;
    private int layout;

    public UserAdapter(Context context, int layout, ArrayList<UserItem> data){
        this.inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layout = layout;
    }

    @Override
    public int getCount(){
        return data.size();
    }

    @Override
    public String getItem(int position){
        return data.get(position).getName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = inflater.inflate(layout,parent, false);

        }
        final UserItem useritem = data.get(position);

        TextView userName = (TextView) convertView.findViewById(R.id.name);
        userName.setText(useritem.getName());

        TextView userPhone = (TextView) convertView.findViewById(R.id.phone);
        userPhone.setText(useritem.getPhone());   // user_item 레이아웃에서 가져옴

        LinearLayout button1 = (LinearLayout)convertView.findViewById(R.id.call);


        return convertView;
    }


}
