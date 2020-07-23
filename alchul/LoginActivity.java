package com.example.hong.alchul;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.hong.alchul.manager.MainActivity_manager;
import com.example.hong.alchul.manager.manager_home;
import com.example.hong.alchul.parttime.MainActivity;
import com.example.hong.alchul.parttime.partime_home;
import com.example.hong.alchul.request.LoginRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {

    EditText idText;
    EditText passwordText;
    Button loginButton;
    Button registerButton;
    Toolbar mToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        idText = (EditText) findViewById(R.id.idText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        loginButton = (Button) findViewById(R.id.LoginButton);
        registerButton = (Button) findViewById(R.id.registerButton);

    }



    


    // REGISTER 버튼 눌렀을 때 이벤트 추가 -> RegisterActivity(회원등록 이벤트)로 이동
    public void onClick2(View v) {
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        LoginActivity.this.startActivity(registerIntent);
    }

    // LOGIN 버튼 눌렀을 때 이벤트 추가
    public void onClick1(View v) {
        String id = idText.getText().toString();
        final String password = passwordText.getText().toString();

        // Response.Listener형식의 객체 생성 (response 결과값을 받아서 실행할 코드)
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    // 결과값을 받기위한 JSONObject 생성
                    JSONObject jsonResponse = new JSONObject(response);

                    // 결과 json에서 "success"라는 키의 값을 success라는 변수에 boolean 형태로 저장한다.
                    boolean success = jsonResponse.getBoolean("success");

                    /* success의 값은 2가지 결과로 나온다.
                         1.  Id, password가 user테이블에 있는 경우 success = true
                         2.  Id, password가 user테이블에 없는 경우 success = false
                    */

                    // 1. Id, password가 user테이블에 있는 경우
                    if (success) {
                        // response인 Json형식으로 userId, userPhoneNum, userName, userStat, storeCode 값을 가져온다.
                        String userId = jsonResponse.getString("userId");
                        String userPhoneNum = jsonResponse.getString("userPhoneNum");
                        String userName = jsonResponse.getString("userName");
                        String userStat = jsonResponse.getString("userStat");
                        String storeCode = jsonResponse.getString("storeCode");

                        // if -> manager로 로그인 시도했을때, 코드
                        // else -> part timer로 로그인 시도했을때, 코드
                        if(userStat.equals("manager")) {

                            // store코드는 회원 등록시 "123"으로 되어있다.
                            // if -> store코드가 "123"으로 되어있는 형태
                            // else -> 특정 store코드가 있는 회원
                            if (storeCode.equals("123")) {
                                // 특정 코드가 없는 상태로, 코드를 생성할수 있는 MainActivity_manager 액티비티로 이동한다
                                Intent intent1 = new Intent(LoginActivity.this, MainActivity_manager.class);
                                // 이동할 때 intent에 id, name과 같은 값을 넣어준다.
                                intent1.putExtra("UserId", userId);
                                intent1.putExtra("UserName", userName);
                                intent1.putExtra("UserPhoneNum", userPhoneNum);
                                intent1.putExtra("UserPassword", password);
                                intent1.putExtra("UserStat", userStat);
                                LoginActivity.this.startActivity(intent1);
                            } else{
                                // 특정 코드가 있는 상태로, manager_home으로 바로 이동한다.
                                Intent intent = new Intent(LoginActivity.this, manager_home.class);
                                intent.putExtra("UserId", userId);
                                intent.putExtra("UserName", userName);
                                intent.putExtra("UserPhoneNum", userPhoneNum);
                                intent.putExtra("UserStat", userStat);
                                intent.putExtra("StoreCode", storeCode);
                                LoginActivity.this.startActivity(intent);
                            }
                        } else {
                            // if -> store코드가 "123"으로 되어있는 형태
                            // else -> 특정 store코드가 있는 회원
                            if (storeCode.equals("123")) {
                                // 특정 코드가 없는 상태로, 코드를 생성할수 있는 MainActivity 액티비티로 이동한다
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("UserId", userId);
                                intent.putExtra("UserName", userName);
                                intent.putExtra("UserPhoneNum", userPhoneNum);
                                intent.putExtra("UserPassword", password);
                                intent.putExtra("UserStat", userStat);
                                LoginActivity.this.startActivity(intent);
                            } else{
                                // 특정 코드가 있는 상태로, partime_home으로 바로 이동한다.
                                Intent intent = new Intent(LoginActivity.this, partime_home.class);
                                intent.putExtra("UserId", userId);
                                intent.putExtra("UserName", userName);
                                intent.putExtra("UserPhoneNum", userPhoneNum);
                                intent.putExtra("UserStat", userStat);
                                intent.putExtra("StoreCode", storeCode);
                                LoginActivity.this.startActivity(intent);
                            }
                        }
                    }

                    // 2. Id, password가 user테이블에 없는 경우 -> 로그인 실패 메세지 출력
                    else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage("Login에 실패하였습니다.")
                                .setNegativeButton("다시 시도", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    Log.d("test", "debug6");
                    e.printStackTrace();
                }
            }
        };
        // LoginRequest 객채 생성
        LoginRequest loginRequest = new LoginRequest(id, password,responseListener);
        // queue 실행
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);
    }
}
