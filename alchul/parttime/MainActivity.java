package com.example.hong.alchul.parttime;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.hong.alchul.LoginActivity;
import com.example.hong.alchul.R;
import com.example.hong.alchul.model.UserModel;
import com.example.hong.alchul.request.ConnectStoreRequest;
import com.example.hong.alchul.request.RegisterRequest;
import com.example.hong.alchul.request.storeRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String userId;
    String userName;
    String userPhoneNum;
    String userStat;
    String userPassword;

    EditText findText;
    String storeCode;

    String find = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Intent intent = getIntent();
        userId = intent.getStringExtra("UserId");
        userName = intent.getStringExtra("UserName");
        userPhoneNum = intent.getStringExtra("UserPhoneNum");
        userPassword = intent.getStringExtra("UserPassword");
        userStat = intent.getStringExtra("UserStat");

        findText = (EditText) findViewById(R.id.findText);

        // 화면 오른쪽에 userName 표시
        TextView textView;
        textView = (TextView) findViewById(R.id.NameView);
        textView.setText(userName);

        // Toast를 이용해 userStat와 userId 표시
        String message = "회원정보: " + userStat + "\n안녕하십니까 " + userId + "님";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        return true;
    }


    // ENTER 버튼 눌렀을 때 이벤트 추가
    public void clickenter(View view) {
        storeCode = findText.getText().toString();

        if (find.equals("OK")) {

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            FirebaseStorage.getInstance().getReference().child("userImages").child(uid).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                   // String imageUrl = task.getResult().toString();
                                    String token = FirebaseInstanceId.getInstance().getToken();

                                    UserModel userModel = new UserModel();
                                    userModel.userName = userName;
                                    userModel.userPhoneNum = userPhoneNum;
                                    userModel.userStat = userStat;
                                  //  userModel.userImage = imageUrl;
                                    userModel.pushToken = token;

                                    FirebaseDatabase.getInstance().getReference().child("users").child(storeCode).child(userStat).child(userName).setValue(userModel);
                                }
                            });


                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("정상적으로 실행하였습니다.")

                                    .setNegativeButton("확인", null)
                                    .create()
                                    .show();
                            Intent enterIntent = new Intent(MainActivity.this, partime_home.class);

                            enterIntent.putExtra("UserId", userId);
                            enterIntent.putExtra("UserName", userName);
                            enterIntent.putExtra("UserPhoneNum", userPhoneNum);
                            enterIntent.putExtra("UserStat", userStat);
                            enterIntent.putExtra("StoreCode", storeCode);

                            MainActivity.this.startActivity(enterIntent);
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("update쿼리가 제대로 작동하지 않았습니다..")
                                    .setNegativeButton("다시 시도", null)
                                    .create()
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            ConnectStoreRequest connectStoreRequest = new ConnectStoreRequest(userId, storeCode, responseListener);
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
            queue.add(connectStoreRequest);

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("코드번호를 먼저 확인하십시오")
                    .setNegativeButton("다시 시도", null)
                    .create()
                    .show();
        }
    }

    // FIND STORE버튼 눌렀을 때 이벤트 추가
    public void clickfind(View view) {
        storeCode = findText.getText().toString();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String success = jsonResponse.getString("success");
                    // 해당 storeCode값의 store이 존재하면 success = "SORRY"
                    if (success.equals("SORRY")) {
                        find = "OK";
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("코드를 찾았습니다")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    }
                    else {
                        find="no";
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("코드번호를 다시 입력하십시오.")
                                .setNegativeButton("다시 시도", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        // FindStoreRequest 객채 생성
        storeRequest findStoreRequest = new storeRequest(storeCode, responseListener);
        // queue 실행
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(findStoreRequest);

    }

    // LOGOUT 버튼 눌렀을 때 이벤트 추가
    public void btn_logout(View v) {
        new AlertDialog.Builder(this)
                .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .show();
    }
    /*
    void passPushTokenToServer() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String token = FirebaseInstanceId.getInstance().getToken();
        Map<String, Object> map = new HashMap<>();
        map.put("pushToken", token);

        FirebaseDatabase.getInstance().getReference().child("users").child(storeCode).child(userStat).child(uid).updateChildren(map);
    }
    */
}