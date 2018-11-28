package com.example.administrator.pet_go.Me

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.administrator.pet_go.JavaBean.User
import com.example.administrator.pet_go.R
import com.example.administrator.pet_go.UI.GlideCircleTransform
import kotlinx.android.synthetic.main.activity_my_info.*


class MyInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_info)
        //        CircleImageView icon = findViewById(R.id.my_head);

        val user = intent.getSerializableExtra("user") as User

        user_name.text = user.name
        user_sex.text = user.sex
        user_id.text = user.uid
        user_address.text = "${user.province}-${user.city}"
        Glide.with(this).load(user.head).transform(GlideCircleTransform(this)).into(sv_me_head)

    }
}

