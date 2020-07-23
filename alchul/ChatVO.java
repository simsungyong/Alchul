package com.example.hong.alchul;

public class ChatVO {

    public int imageID ;
    public String name;
    public String content;
    public String time;

    public ChatVO(){}

    public ChatVO(int imageID, String name, String content, String time) {
        this.imageID = imageID;
        this.name = name;
        this.content = content;
        this.time = time;
    }

    public int getImageID() {
        return imageID;
    }

    public String getId() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

}