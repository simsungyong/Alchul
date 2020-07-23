package com.example.hong.alchul.manager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.hong.alchul.R;
import com.example.hong.alchul.parttime.MyFragment2;
import com.example.hong.alchul.request.eventRequest;
import com.example.hong.alchul.request.listRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

public class manager_frag1 extends Fragment {
    View view;
    private Context context;

    private ArrayList<UserItem> data = null;
    List list = new ArrayList();
    String userId;
    String userName;
    String userPhoneNum;
    String userStat;
    String storeCode;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_manager_frag1, container,false );
        context = container.getContext();
        userId = getArguments().getString("UserId");
        userName = getArguments().getString("UserName");
        userPhoneNum = getArguments().getString("UserPhoneNum");
        userStat = getArguments().getString("UserStat");
        storeCode = getArguments().getString("StoreCode");
        listView = (ListView)view.findViewById(R.id.List_view);
        data = new ArrayList<>();






        Response.Listener<String> responseListener = new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray success = jsonResponse.getJSONArray("response");
                    for(int i=0; i<success.length(); i++){
                        HashMap<String,String> InputData1 = new HashMap<>();

                        JSONObject item = success.getJSONObject(i);
                        String part = item.getString("userName");
                        //list.add(part);
                        //InputData1.put("userName", part);
                        String phonenum=item.getString("userPhoneNum");
                        //InputData1.put("userPhoneNum", phonenum );

                        UserItem user = new UserItem(part, phonenum);

                        data.add(user);

                     }//알바이름이랑 폰번호를 받아와서 어레이리스트에 넣는다. 그 다음 리스트뷰에 넣을 Data에 add한다.

                    UserAdapter adapter = new UserAdapter(context ,R.layout.user_item, data);
                    listView.setAdapter(adapter); //data에 있는 값들을 user-item있는 레이아웃과 매칭한다.




                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        };



        listRequest list_user = new listRequest(storeCode, userId ,responseListener);

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(list_user);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {



                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("직원: "+data.get(position).getName())
                        .setPositiveButton("전화걸기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String tel="tel:"+data.get(position).getPhone();
                        startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
                    }
                }).setNegativeButton("근무기록", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, user_calendar.class);
                        intent.putExtra("userName", data.get(position).getName());
                        intent.putExtra("userPhoneNum", data.get(position).getPhone());
                        intent.putExtra("storeCode", userStat);
                        startActivity(intent);
                    }
                })
                        .create()
                        .show();
            }
        });




        return view;
    }
}