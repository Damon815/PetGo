package com.example.administrator.pet_go.JavaBean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Administrator on 2018/11/24/024.
 */

public class Comment {
    private String cid;
    private String user_id;
    private String user_name;
    private String content;

    public Comment(String user_name, String content) {
        this.user_name = user_name;
        this.content = content;
    }

    public String getUser_name() {

        return user_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getContent() {
        return content;
    }

}
