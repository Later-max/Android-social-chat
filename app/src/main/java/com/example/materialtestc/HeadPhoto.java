package com.example.materialtestc;



import cn.bmob.v3.BmobObject;


public class HeadPhoto extends BmobObject {
    String  photo;
    String name;

    public void setPhoto(String  photo){
        this.photo = photo;

    }
    public String  getPhoto(){
        return photo;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
}
