package com.example.administrator.pet_go.Pet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.administrator.pet_go.Chat.ChatActivity
import com.example.administrator.pet_go.JavaBean.Pet
import com.example.administrator.pet_go.R
import com.example.administrator.pet_go.UI.RoundRectImageview
import com.example.administrator.pet_go.Util.DataUtil
import com.example.administrator.pet_go.Util.ImageUtils
import kotlinx.android.synthetic.main.activity_request_details.*
import org.jetbrains.anko.async
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.net.URL

class RequestDetailsActivity : AppCompatActivity() {

    private lateinit var pet: Pet
    private lateinit var own_pet: Pet
    private lateinit var request_user_id: String
    private lateinit var request_user_name: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_details)


        pet = intent.getSerializableExtra("pet") as Pet
        own_pet = intent.getSerializableExtra("own_pet") as Pet
        request_user_id = intent.getStringExtra("request_user_id")
        request_user_name = intent.getStringExtra("request_user_name")
        initView(pet,own_pet)


    }

    private fun initView(pet: Pet, own_pet: Pet) {
        own_pet_name.text = "与您的${own_pet.name} 配对"

        request_pet_name.text = pet.name
        request_pet_type.text = pet.type
        request_pet_variety.text = pet.variety
        request_pet_age.text = pet.age
        request_pet_sex.text = pet.sex
        request_pet_content.text = pet.note
        val pictures = pet.picture
        val size = pictures.size

        //发消息
        send_message.setOnClickListener {
            val intent  = Intent()
            //发送本人id和请求人id
            intent.setClass(this,ChatActivity::class.java)
            intent.putExtra("request_user_id",request_user_id)
            intent.putExtra("request_user_name",request_user_name)
            startActivity(intent)
            async {
                val sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE)
                val user_id = sharedPreferences.getString("user_id", "")
                val host = "${DataUtil.host}/chat"
                val url  = "$host?uid0=$user_id&uid1=$request_user_id&talk=您好！&who=0"
                val reponse = URL(url).readText()
            }

        }


        //拒绝
        btn_refuse.setOnClickListener {
            async {
                val host = "${DataUtil.host}/matchState"
                val url = "$host?pet_id=${pet.pid}&own_pet_id${own_pet.pid}&state=${DataUtil.MATCHSTATE_DISAGREE}"
                val response = URL(url).readText()
                uiThread {
                    toast("您已拒绝!")
                }
            }
        }

        //同意
        btn_agree.setOnClickListener {
            async {
                val host = "${DataUtil.host}/matchState"
                val url = "$host?pet_id=${pet.pid}&own_pet_id${own_pet.pid}&state=${DataUtil.MATCHSTATE_AGREE}"
                val response = URL(url).readText()
                uiThread {
                    toast("您已拒绝!")
                }
            }
        }
        if (size>0){
            showPicture(request_pet_picture1,pictures[0])
        }else if(size>1){
            showPicture(request_pet_picture2,pictures[1])
        }else if (size>2){
            showPicture(request_pet_picture3,pictures[2])
        }
    }

    private fun showPicture(pictureView: RoundRectImageview,picture: String){

        async {
            val urlImage = ImageUtils.getURLimage(picture)
            uiThread {
                pictureView.visibility = View.VISIBLE
                pictureView.setImageBitmap(urlImage)
            }
        }
    }


    //退出后 表示已看过
    override fun onStop() {
        super.onStop()
        async {
            val host = "${DataUtil.host}/matchState"
            val url = "$host?pet_id=${pet.pid}&own_pet_id${own_pet.pid}&state=${DataUtil.MATCHSTATE_YET}"
            val response = URL(url).readText()
            uiThread {

            }
        }
    }

}
