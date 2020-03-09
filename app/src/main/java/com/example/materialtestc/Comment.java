package com.example.materialtestc;


import cn.bmob.v3.BmobObject;

public class Comment extends BmobObject {
    String name;//评论者
    String content; //评论内容
    String soal;//信息的来源
    String time;//获取当时时间


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setSoal(String soal) {
        this.soal = soal;
    }

    public String getSoal() {
        return soal;
    }
    public void setTime(String time){
        this.time = time;
    }
    public String getTime(){
        return time;
    }
}
