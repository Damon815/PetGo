package com.example.administrator.pet_go.verify;

/**
 * Created by Administrator on 2018/11/23/023.
 */

public class UserVerify implements verify {
    @Override
    public int verify(String string) {
        String[] split = string.split(",");
        String user_id = split[0];
        String password = split[1];
        String host = split[2];


        return 0;
    }
}
