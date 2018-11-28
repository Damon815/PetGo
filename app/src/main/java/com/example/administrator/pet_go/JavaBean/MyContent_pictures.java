package com.example.administrator.pet_go.JavaBean;

/**
 * Created by Administrator on 2018/11/26.
 */

public class MyContent_pictures {
    private String picture0;
    private String picture1;
    private String picture2;
    private String picture3;

    //public String getPicture0() { return picture0; }
    public void setPicture0(String picture0) { this.picture0 = picture0; }
    //public String getPicture1() { return picture1; }
    public void setPicture1(String picture1) { this.picture1 = picture1; }
    //public String getPicture2() { return picture2; }
    public void setPicture2(String picture2) { this.picture2 = picture2; }
    //public String getPicture3() { return picture3; }
    public void setPicture3(String picture3) { this.picture3 = picture3; }

    public String getPictures(int i){
        switch (i){
            case 0:
                return picture0;
            case 1:
                return picture1;
            case 2:
                return picture2;
            case 3:
                return picture3;
            default:
                return picture0;

        }

    }


}
