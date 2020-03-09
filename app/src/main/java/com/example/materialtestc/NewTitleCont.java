package com.example.materialtestc;

import cn.bmob.v3.BmobObject;

public class NewTitleCont extends BmobObject {
    String title;
    String cont;

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }

    public void setCont(String cont){
        this.cont = cont;
    }
    public String getCont(){
        return cont;
    }
}
