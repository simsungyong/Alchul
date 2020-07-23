package com.example.hong.alchul.manager;

public class UserItem {
    private String name;
    private String phone;


    public String getName(){
        return name;
    }

    public String getPhone(){
        return phone;
    }

    public UserItem(String name, String phone){
        this.name = name;
        this.phone = phone;
    }
}
