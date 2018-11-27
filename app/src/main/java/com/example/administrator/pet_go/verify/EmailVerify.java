package com.example.administrator.pet_go.verify;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/11/23/023.
 */

public class EmailVerify implements verify {

    public final static int success = 0x11;
    public final static int fail = 0x22;
    public final static int empty = 0x33;
    @Override
    public int verify(String string) {
        boolean email = isEmail(string);
        if (string.isEmpty()){
            return empty;
        }
        if (email){
            return success;
        }else{
            return fail;
        }

    }
    public static boolean isEmail(String string) {
        if (string == null) return false;
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p; Matcher m; p = Pattern.compile(regEx1);
        m = p.matcher(string); if (m.matches())
            return true;
        else return false;
    }

}
