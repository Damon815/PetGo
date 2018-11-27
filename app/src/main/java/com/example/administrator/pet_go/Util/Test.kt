package com.example.administrator.pet_go.Util

import com.example.administrator.pet_go.JavaBean.Moment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by Administrator on 2018/11/25/025.
 */

class Test {
    fun test() {
        Gson().fromJson<Any>("da", object : TypeToken<List<Moment>>() {

        }.type)
    }
}
