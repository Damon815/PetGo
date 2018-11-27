package com.example.administrator.pet_go.JavaBean;

import java.util.List;

/**
 * Created by Administrator on 2018/11/24/024.
 */

public class Moment {
    private String id; //
    private String user_id; //用户ID
    private String header_url; //头像
    private String name;  //昵称
    private String time; //发布时间
    private String picture_url; //发布的图片
    private String content; //发布的内容
    private boolean like; //是否点赞
    private int like_count;  //点赞数
    private int comment_count;//评论数

//    public Moment(String header_url, String name, String time, String picture_url, String content, boolean like, int like_count, int comment_count) {
//        this.header_url = header_url;
//        this.name = name;
//        this.time = time;
//        this.picture_url = picture_url;
//        this.content = content;
//        this.like = like;
//        this.like_count = like_count;
//        this.comment_count = comment_count;
//    }



    public String getHeader_url() {
        return header_url;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public boolean isLike() {
        return like;
    }

    public int getLike_count() {
        return like_count;
    }

    public int getComment_count() {
        return comment_count;
    }


    public void setLike(boolean like) {
        this.like = like;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }


}
