package com.example.hong.alchul.parttime;


import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.UserManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.hong.alchul.AlramWorkstart;
import com.example.hong.alchul.R;
import com.example.hong.alchul.getmap;
import com.example.hong.alchul.model.UserModel;
import com.example.hong.alchul.request.GpsRequest;
import com.example.hong.alchul.request.MyFragment1_request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.graphics.Color.parseColor;
import static com.android.volley.VolleyLog.TAG;

public class MyFragment1 extends Fragment {
    View view;
    private Context context;
    long mNow, mEnd;
    Date mDate, mEndDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat mFormat1 = new SimpleDateFormat("yyyy-MM-dd");
    String startwork, endwork, startday;
    Button btn_start, btn_end, mapbutton;
    String userId;
    String userName;
    String userPhoneNum;
    String userStat;
    String storeCode;
    String enter="";
    private getmap gps;
    double latitude, longitude;





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_part1, container,false );                     //fragment화면 설정
        context = container.getContext();
        btn_start = (Button)view.findViewById(R.id.start);
        btn_end = (Button)view.findViewById(R.id.end);
        userId = getArguments().getString("UserId");
        userName = getArguments().getString("UserName");
        userPhoneNum = getArguments().getString("UserPhoneNum");
        userStat = getArguments().getString("UserStat");
        storeCode = getArguments().getString("StoreCode");



        TextView textView1, textView2, textView3, textView4;
        textView1 = (TextView)view.findViewById(R.id.textView1);
        textView2 = (TextView)view.findViewById(R.id.textView2);
        textView3 = (TextView)view.findViewById(R.id.textView3);
        textView1.setText("ID  :  "+userId);
        textView2.setText("이름  :  "+userName);
        textView3.setText("전화번호  :  "+userPhoneNum);



        mapbutton = (Button)view.findViewById(R.id.map);

        mapbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                gps = new getmap(context);
                //GPS사용유무
                if(gps.isGetLocation()){
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    Log.i("gps long", String.valueOf(longitude));
                    Log.i("gps lat", String.valueOf(latitude));


                    Toast.makeText(context, "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude, Toast.LENGTH_LONG).show();

                    Response.Listener<String> gpsListener = new Response.Listener<String>() {
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                JSONArray success = jsonResponse.getJSONArray("response");

                                JSONObject item = success.getJSONObject(0);
                                Double lat = item.getDouble("latitude");
                                Double lon = item.getDouble("longitude");

                                Location start = new Location("start");
                                Location end = new Location("end");

                                start.setLatitude(latitude);
                                start.setLongitude(longitude);
                                end.setLatitude(lat);
                                end.setLongitude(lon);

                                double distance = start.distanceTo(end);

                                Log.i("gps위도", String.valueOf(lat));
                                Log.i("gps경도", String.valueOf(lon));

                                Log.i("거리", "distance"+distance);
                                if(distance<50){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setMessage("인증되었습니다")
                                            .setPositiveButton("확인", null)
                                            .create()
                                            .show();
                                    enter = "OK";
                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setMessage("가게가 아닙니다.")
                                            .setPositiveButton("확인", null)
                                            .create()
                                            .show();
                                }

                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                        }
                    };

                    GpsRequest gps = new GpsRequest(storeCode, gpsListener);
                    RequestQueue queue = Volley.newRequestQueue(context);
                    queue.add(gps);


                } else {
                    // GPS 를 사용할수 없으므로
                    gps.showSettingsAlert();
                }




            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(enter==""){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("출근인증을 하세요^^")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                }else{

                    sendWorktime();

                    mNow=System.currentTimeMillis();
                    mDate=new Date(mNow);
                    startwork = mFormat.format(mDate);         //시간까지 있는 형식
                    startday  = mFormat1.format(mDate);     //날짜만 있는 형식
                    btn_end.setEnabled(true);//처음상태는 출근버튼이 활성화되있고 퇴근버튼 비활성화. 출근버튼누르면 출근버튼 비활성화. 퇴근버튼 활성화
                    btn_start.setEnabled(false);
                    Toast.makeText(context, startwork+"에"+"출근하였습니다.", Toast.LENGTH_SHORT).show();

                } }          //출근 버튼눌렀을 때.
        });

        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEnd=System.currentTimeMillis();
                mEndDate=new Date(mNow);
                endwork = mFormat.format(mDate);
                btn_end.setEnabled(false);
                btn_start.setEnabled(true);

                //퇴근 버튼을 눌렀을때, 출근시간 데이터와 퇴근시간데이터를 데이터베이스에 저장

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                            }
                            else {

                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                };
                MyFragment1_request m = new MyFragment1_request(userName, startwork, endwork, startday, responseListener);
                RequestQueue queue = Volley.newRequestQueue(context);
                queue.add(m);                   //MyFragment1_requset 생성자 호출. php로 연결을 하여 원하는 정보를 DB에서 빼온다



                Toast.makeText(context, endwork+"퇴근하였습니다.", Toast.LENGTH_SHORT).show();      //퇴근버튼눌렀을때 디비저장.변수 startwork랑 endwork에 시간 저장.

            }
        });
        return view;
    }

    void sendWorktime() {

        Gson gson = new Gson();

        AlramWorkstart alramWorkstart = new AlramWorkstart();

        alramWorkstart.to = "fYzziuURl-I:APA91bFkPIvLhy_4trbxr_yhJYRKe1Y6Ur89NB27JHp_ir0Wg57TGB8cOinLZuTORZ1adqzp4tRPEUhn7rXYgY4KN7SgskQfyIKtnAmjA0bdjWlb7Jp4XwxHZQY8ZrNnbQcFRgiku-Ck";
        alramWorkstart.workStart.title = userName;
        alramWorkstart.workStart.text = userName+"님이 출근하셨습니다.";

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"), gson.toJson(alramWorkstart));

        Request request = new Request.Builder()
                .header("Content-Type", "application/json")
                .addHeader("Authorization", "key=AIzaSyCE3QX5ZOjT8pNZoxou5iSCB3EYm141PUI")
                .url("https://gcm-http.googleapis.com/gcm/send")
                .post(requestBody)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {

            }
        });

    }

}