package com.example.hong.alchul.manager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.hong.alchul.LoginActivity;
import com.example.hong.alchul.R;
import com.example.hong.alchul.RegisterActivity;
import com.example.hong.alchul.googlemap;
import com.example.hong.alchul.model.UserModel;
import com.example.hong.alchul.request.RegisterRequest;
import com.example.hong.alchul.request.storeRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity_manager extends AppCompatActivity {

    String userId;
    String userPassword;
    String userName;
    String userPhoneNum;
    String userStat;
    String find = "";
    String storeCode;
    String storeName;
    String lat= null;
    String lon= null;

    EditText createText;
    EditText createCodeText;
    TextView mapview;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_manager);

        Intent intent = getIntent();
        userId = intent.getStringExtra("UserId");
        userPassword = intent.getStringExtra("UserPassword");
        userName = intent.getStringExtra("UserName");
        userPhoneNum = intent.getStringExtra("UserPhoneNum");
        userStat = intent.getStringExtra("UserStat");
        lat = intent.getStringExtra("lat");
        lon = intent.getStringExtra("lon");





        createText = (EditText) findViewById(R.id.createText);
        createCodeText = (EditText) findViewById(R.id.createCodeText);
        mapview = (TextView)findViewById(R.id.map);
        textView = (TextView) findViewById(R.id.NameView);
        textView.setText(userName);

        if(lat != null){
            mapview.setText("위도: "+lat+", 경도: " + lon);
        }

        //String message = "회원정보: " + userStat + "\n안녕하십니까 " + userId + "님";

        //Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void createStore(View view) {

        storeCode = createCodeText.getText().toString();
        storeName = createText.getText().toString();

        Intent enterIntent = new Intent(MainActivity_manager.this, manager_home.class);


        // find변수를 이용하여 storeCode 확인이 끝난 후에 생성을 할 수 있도록 해주었다

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

                                  //  String imageUrl = task.getResult().toString();
                                    String token = FirebaseInstanceId.getInstance().getToken();

                                    UserModel userModel = new UserModel();
                                    userModel.userName = userName;
                                    userModel.userPhoneNum = userPhoneNum;
                                    userModel.userStat = userStat;

                                    //userModel.userImage = imageUrl;
                                    userModel.pushToken = token;

                                    FirebaseDatabase.getInstance().getReference().child("users").child(storeCode).child(userStat).setValue(userModel);
                                }
                            });


                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_manager.this);
                            builder.setMessage("가게 등록 성공")
                                    .setPositiveButton("확인", null)
                                    .create()
                                    .show();

                            Intent enterIntent = new Intent(MainActivity_manager.this, manager_home.class);
                            enterIntent.putExtra("UserId", userId);
                            enterIntent.putExtra("UserName", userName);
                            enterIntent.putExtra("UserPhoneNum", userPhoneNum);
                            enterIntent.putExtra("UserStat", userStat);
                            enterIntent.putExtra("StoreCode", storeCode);
                            enterIntent.putExtra("StoreName", storeName);
                            MainActivity_manager.this.startActivity(enterIntent);
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_manager.this);
                            builder.setMessage("가게 등록 실패")
                                    .setNegativeButton("다시 시도", null)
                                    .create()
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            storeRequest store = new storeRequest(storeName, storeCode, userId, lat, lon, responseListener);
            RequestQueue queue = Volley.newRequestQueue(MainActivity_manager.this);
            queue.add(store);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_manager.this);
            builder.setMessage("코드번호를 먼저 확인하십시오")
                    .setNegativeButton("다시 시도", null)
                    .create()
                    .show();
        }

    }

    public void checkStore(View view) {
        Log.i("test", lat);
        storeCode = createCodeText.getText().toString();

        // storeCode에 아무 값도 입력하지 않았을 경우 메세지 출력
        if (storeCode.length() == 0 || lat == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_manager.this);
            builder.setMessage("storeCode를 입력하세요")
                    .setNegativeButton("확인", null)
                    .create()
                    .show();
        }
        else {

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String success = jsonResponse.getString("success");

                        // 해당 storeCode의 가게가 존재하는 경우 success = "SORRY"
                        // 해당 storeCode의 가게가 존재하지 않는경우 success = "OK"

                        // success = "OK"일때 새로운 가게 생성 가능
                        if (success.equals("OK")) {
                            // 해당 이름의 가게를 생성가능하도록 find 변수를 "OK"값으로 변환해준다
                            find = "OK";
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_manager.this);
                            builder.setMessage("생성 가능한 코드입니다")
                                    .setPositiveButton("확인", null)
                                    .create()
                                    .show();
                        }
                        else {
                            find="no";
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_manager.this);
                            builder.setMessage("이미 존재하는 코드입니다.")
                                    .setNegativeButton("다시 시도", null)
                                    .create()
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            // FindStoreRequest 객체 생성
            storeRequest findstore = new storeRequest(storeCode, responseListener);
            RequestQueue queue = Volley.newRequestQueue(MainActivity_manager.this);
            queue.add(findstore);
        }
    }

    // LOGOUT 버튼 눌렀을 때 이벤트 추가
    public void btn_logout(View v) {
        new AlertDialog.Builder(this)
                .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(MainActivity_manager.this, LoginActivity.class);
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


    public void goMap(View view) {
        Intent enterIntent = new Intent(MainActivity_manager.this, googlemap.class);
        enterIntent.putExtra("UserId", userId);
        enterIntent.putExtra("UserName", userName);
        enterIntent.putExtra("UserPhoneNum", userPhoneNum);
        enterIntent.putExtra("UserStat", userStat);
        startActivity(enterIntent);


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
