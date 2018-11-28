package com.example.administrator.pet_go.JavaBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/11/25.
 */

public class MyContent_petinfo {
    private  String  name ,variety,sex,note,type,username,usersex,address;
    private ArrayList<String> picture;
    private  int age,uid,pid,userage;



    //public MyContent_petinfo(ArrayList<String> picture){ this.picture=picture; }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }


    public ArrayList<String> getPicture() {
        for(int i=0;i<picture.size();i++){
            if (picture.get(i).contains("\\r\\n")){
                String p=picture.get(i).replaceAll("\\r\\n","");
                picture.remove(i);
                picture.add(i,p);
            }
        }
        return picture;
    }
    public void setPicture(ArrayList<String> picture) {
        this.picture = picture;
    }

    public String getVariety() {
        return variety;
    }
    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getSex() {
        return sex;
    }
    public void setSex(String sex){ this.sex=sex; }

    public String getUsersex(){return usersex;}
    public void setUsersex(String usersex){this.usersex=usersex;}

    public int getUid(){ return uid; }
    public void setUid( int uid) {
        this.uid = uid;
    }

    public int getPid(){ return pid; }
    public void setPid( int pid) {
        this.pid = pid;
    }

    public int getAge(){ return age; }
    public void setAge( int age) {
        this.age = age;
    }

    public int getUserage(){return userage;}
    public void setUserage(int userage) {
        this.userage = userage;
    }

    public String getNote(){ return note;}
    public void setNote(String note) { this.note=note; }

    public String getType(){ return type;}
    public void setType(String type) { this.type=type; }

    public String getAddress(){return address;}
    public void setAddress(String address){this.address=address;}


}
