package com.example.hong.alchul.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class noticeRequest extends StringRequest {
    final static private String URL = "http://18.221.234.141/teamproject/WriteNotice.php";
    private Map<String, String> parameters;

    public noticeRequest(String storeCode, String userName, String title, String content, Response.Listener<String> listener) {//스토어 찾는 request생성자
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("storeCode", storeCode);
        parameters.put("userName", userName);
        parameters.put("title", title);
        parameters.put("content", content);

    }

    public Map<String, String> getParams() {
        return parameters;
    }

}


