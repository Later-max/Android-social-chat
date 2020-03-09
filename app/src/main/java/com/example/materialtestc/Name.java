package com.example.materialtestc;

import cn.bmob.v3.BmobObject;

public class Name extends BmobObject {
    String Name;//用户名
    String number;//学号
    String home;//寝室号

    public void setName(String Name){
        this.Name = Name;
    }
    public String getName(){
        return Name;
    }
    public void setNumber(String number){this.number = number;}
    public  String getNumber(){return number;}
    public void setHome(String home){this.home = home;}
    public String getHome(){return home;}
}
