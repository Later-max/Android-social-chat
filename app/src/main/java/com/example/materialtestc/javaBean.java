package com.example.materialtestc;

import cn.bmob.v3.BmobObject;

public class javaBean extends BmobObject {
        private String name;
        private String photo;

        public String getName(){
            return name;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getPhoto(){
            return photo;
        }
        public void setPhoto(String photo){
            this.photo = photo;
        }
}
