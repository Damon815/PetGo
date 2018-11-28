package com.example.administrator.pet_go.Pet

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.administrator.pet_go.JavaBean.Pet
import com.example.administrator.pet_go.R
import com.example.administrator.pet_go.Util.DataUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_my_pets.*
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread
import java.net.URL

class MyPetsActivity : AppCompatActivity() {

    private lateinit var list: ArrayList<Pet>
    private lateinit var adapter:MyPetsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_pets)

        getData()
        refresh.setOnRefreshListener {
            getData()
            adapter.notifyDataSetChanged()
            refresh.isRefreshing = false
        }





    }

    private fun getData() {
        async {
            val sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE)
            val user_id = sharedPreferences.getString("user_id", "")
            val host = "${DataUtil.host}/getPet"
            val url  = "$host?uid=$user_id"
            val response = URL(url).readText()
            val gson = Gson()
            list = gson.fromJson(response,object : TypeToken<ArrayList<Pet>>(){}.type)

            uiThread {
                Log.d("mypet",list[0].name)
                adapter = MyPetsAdapter(this@MyPetsActivity,list)
                my_pets.adapter = adapter
            }

        }
    }
}
