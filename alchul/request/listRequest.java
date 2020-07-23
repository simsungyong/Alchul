package com.example.hong.alchul.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class listRequest extends StringRequest {
    final static private String URL = "http://18.221.234.141/teamproject/getList.php";
    private Map<String, String> parameters;

    public listRequest(String storeCode, String userId, Response.Listener<String> listener) {
        super(Method.POST, URL,listener, null);
        parameters = new HashMap<>();
        parameters.put("storeCode", storeCode);
        parameters.put("userId", userId);

    }
    public Map<String, String> getParams() {
        return parameters;
    }
}
