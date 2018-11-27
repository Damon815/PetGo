package com.example.administrator.pet_go.verify;

/**
 * Created by Administrator on 2018/11/23/023.
 */

public class PasswordVerify implements verify {
    public final static int success = 0x11;
    public final static int fail = 0x22;
    public final static int empty = 0x33;
    @Override
    public int verify(String string) {
        String[] split = string.split(",");
        if (split[0].isEmpty()||split[1].isEmpty()){
            return empty;
        }else if (split[0].equals(split[1])){
            return success;
        }
        return fail;
    }
}
