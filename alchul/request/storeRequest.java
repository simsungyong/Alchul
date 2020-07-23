package com.example.hong.alchul.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class storeRequest extends StringRequest {
    final static private String URL1 = "http://18.221.234.141/teamproject/RegisterStore.php";
    final static private String URL2 = "http://18.221.234.141/teamproject/FindStore.php";

    private Map<String, String> parameters;

    public storeRequest(String storeName, String storeCode, String userId, String lat, String lon, Response.Listener<String> listener) {//스토어 생성 생성자
        super(Method.POST, URL1, listener, null);
        parameters = new HashMap<>();
        parameters.put("latitude", lat);
        parameters.put("longitude", lon);
        parameters.put("storeName", storeName);
        parameters.put("storeCode", storeCode);
        parameters.put("userId", userId);
    }

    public storeRequest(String storeCode, Response.Listener<String> listener) {//스토어 찾는 request생성자
        super(Method.POST, URL2, listener, null);
        parameters = new HashMap<>();
        parameters.put("storeCode", storeCode);
    }

    public Map<String, String> getParams() {
        return parameters;
    }
}

