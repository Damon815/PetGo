package com.example.administrator.pet_go.Chat

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.administrator.pet_go.JavaBean.Chat
import com.example.administrator.pet_go.R
import com.example.administrator.pet_go.Util.DataUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_chat.*
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread
import java.net.URL

class ChatActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val request_user_name = intent.getStringExtra("request_user_name")
        val request_user_id = intent.getStringExtra("request_user_id")
        toolbar.text = request_user_name
        async {
            val sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE)
            val user_id = sharedPreferences.getString("user_id", "")
            val host = "${DataUtil.host}/chat"
            val url  = "$host?uid0=$user_id&uid1=$request_user_id"
            val response = URL(url).readText()
            val gson = Gson()
            val chatList = gson.fromJson<ArrayList<Chat>>(response, object : TypeToken<ArrayList<Chat>>() {}.type)
            uiThread {

            }
        }

    }
}
