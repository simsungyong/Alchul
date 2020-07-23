package com.example.hong.alchul.request;

import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

//request를 받아서 결과값을 Response.Listener에 전달하는 LoginRequest class
public class LoginRequest extends StringRequest {
    // 사용된 php파일 이름: Login.php
    final static private String URL = "http://18.221.234.141/teamproject/Login.php";
    private Map<String, String> parameters;


    public LoginRequest(String userId, String userPassword,Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        // 파라미터값으로 userId, userPassword 전달
        parameters = new HashMap<>();
        parameters.put("userId", userId);
        parameters.put("userPassword", userPassword);
    }

    public Map<String, String> getParams() {
        return parameters;
    }

}
