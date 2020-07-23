package com.example.hong.alchul;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.hong.alchul.LoginActivity;
import com.example.hong.alchul.R;
import com.example.hong.alchul.request.ConnectStoreRequest;
import com.example.hong.alchul.request.RegisterRequest;
import com.example.hong.alchul.request.storeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class NoticeRoom extends AppCompatActivity {

    String title;
    String userName;
    String time;
    String content;

    TextView nameView;
    TextView titleView;
    TextView contentView;
    TextView timeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_room);

        Intent intent = getIntent();
        userName = intent.getStringExtra("UserName");
        time = intent.getStringExtra("Time");
        content = intent.getStringExtra("Content");
        title = intent.getStringExtra("Title");

        nameView = (TextView) findViewById(R.id.nameView);
        titleView = (TextView) findViewById(R.id.titleView);
        contentView = (TextView) findViewById(R.id.contentView);
        timeView = (TextView) findViewById(R.id.timeView);

        nameView.setText(userName);
        titleView.setText(title);
        contentView.setText(content);
        timeView.setText(time);

    }


}
