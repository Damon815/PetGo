package com.example.administrator.pet_go.Pet

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.administrator.pet_go.JavaBean.Pet
import com.example.administrator.pet_go.JavaBean.User
import com.example.administrator.pet_go.R
import com.example.administrator.pet_go.Util.DataUtil
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_quest_match.*
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread
import java.net.URL


class RequestMatchActivity : AppCompatActivity() {

    val users = ArrayList<User>()
    val pets = ArrayList<Pet>()
    val states = ArrayList<Int>()
    val ownPets = ArrayList<Pet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest_match)

        getRequestData()
        val requestMatchAdapter = RequestMatchAdapter(this, users, pets, states,ownPets)
        rv_request_list.adapter = requestMatchAdapter


    }

    private fun getRequestData() {
        val sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        val user_id = sharedPreferences.getString("user_id", "")
        async {
            val host  = "${DataUtil.host}/requestMatchList"
            val url = "$host?uid=$user_id"
            val response = URL(url).readText()

            uiThread {
                if (!response.isEmpty()){
                    val gson = Gson()
                    val jsonArray = gson.toJsonTree(response).asJsonArray
                    for (jsonElement in jsonArray ){
                        val asJsonObject = jsonElement.asJsonObject
                        val userJson = asJsonObject.get("user")
                        val petJson = asJsonObject.get("pet")
                        val state = asJsonObject.get("state").asInt
                        val ownPetJson = asJsonObject.get("ownPet")
                        val user = gson.fromJson<User>(userJson,User::class.java)
                        val pet = gson.fromJson<Pet>(petJson,Pet::class.java)
                        val ownPet = gson.fromJson<Pet>(ownPetJson,Pet::class.java)
                        users.add(user)
                        pets.add(pet)
                        states.add(state)
                        ownPets.add(ownPet)
                    }
                }
            }

        }
    }

}
