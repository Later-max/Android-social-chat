package com.example.materialtestc;

public class Fruit {
    private String name;
    private int imageId;
    private String name2;//用户名
    public Fruit(String name,int imageId,String name2){
        this.name = name;
        this.imageId = imageId;
        this.name2 = name2;
    }

    public String getName(){
        return name;
    }
    public int getImageId(){
        return imageId;
    }
    public String getName2(){return name2;}
}
