package com.example.hong.alchul;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.hong.alchul.manager.MainActivity_manager;
import com.example.hong.alchul.manager.manager_frag1;
import com.example.hong.alchul.manager.manager_home;
import com.example.hong.alchul.parttime.MyFragment3;
import com.example.hong.alchul.parttime.partime_home;
import com.example.hong.alchul.request.LoginRequest;
import com.example.hong.alchul.request.noticeRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class NoticeActivity extends AppCompatActivity {

    String userId;
    String userName;
    String userPhoneNum;
    String userStat;
    String storeCode;

    TextView checkButton, name;
    EditText title, content;
    Bundle bundle = new Bundle(7);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_notice);

        Intent intent = getIntent();
        userId = intent.getStringExtra("UserId");
        userName = intent.getStringExtra("UserName");
        userPhoneNum = intent.getStringExtra("UserPhoneNum");
        userStat = intent.getStringExtra("UserStat");
        storeCode = intent.getStringExtra("StoreCode");
        title = (EditText) findViewById(R.id.edit1);
        content = (EditText)findViewById(R.id.edit2);

        name = (TextView)findViewById(R.id.name);



        name.setText(userName);

    }


    public void check(View view) {
        String title1 = title.getText().toString();
        String content1 = content.getText().toString();


        if(title1==null || title1.equals("")||content1==null||content1.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(NoticeActivity.this);
            builder.setMessage("제목과 내용을 기입해 주세요")
                    .setPositiveButton("확인", null)
                    .create()
                    .show();
        }
        else{
            if (userStat.equals("manager")) {
                Intent intent = new Intent(NoticeActivity.this, manager_home.class);
                intent.putExtra("UserId", userId);
                intent.putExtra("UserName", userName);
                intent.putExtra("UserPhoneNum", userPhoneNum);
                intent.putExtra("UserStat", userStat);
                intent.putExtra("StoreCode", storeCode);
                intent.putExtra("title", title1);
                intent.putExtra("content", content1);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(NoticeActivity.this, partime_home.class);
                intent.putExtra("UserId", userId);
                intent.putExtra("UserName", userName);
                intent.putExtra("UserPhoneNum", userPhoneNum);
                intent.putExtra("UserStat", userStat);
                intent.putExtra("StoreCode", storeCode);
                intent.putExtra("title", title1);
                intent.putExtra("content", content1);
                startActivity(intent);
            }
        }
    }

}
