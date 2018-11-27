package com.example.administrator.pet_go.Pet

import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test

/**
 * Created by Administrator on 2018/11/26/026.
 */
class RequestMatchActivityTest {
    @Test
    public fun test(){
        val gson = Gson()
        val json = "{[{user:me1,pet:pet1},{user:user2,pet:pet2}]}"
        val jsonarray = gson.toJsonTree(json).asJsonArray

        for (jsonobject in jsonarray ){
            val asJsonObject = jsonobject.asJsonObject
            val get = asJsonObject.get("user")
            val get1 = asJsonObject.get("pet")
            println(get)
        }
    }
}