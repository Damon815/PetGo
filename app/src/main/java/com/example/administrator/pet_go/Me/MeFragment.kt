package com.example.administrator.pet_go.Me

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.example.administrator.pet_go.JavaBean.User
import com.example.administrator.pet_go.Pet.MyPetsActivity
import com.example.administrator.pet_go.Pet.RequestMatchActivity
import com.example.administrator.pet_go.Pet.UploadPetActivity
import com.example.administrator.pet_go.R
import com.example.administrator.pet_go.UI.GlideCircleTransform
import com.example.administrator.pet_go.Util.DataUtil
import com.example.administrator.pet_go.login.LoginActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_me.*
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread
import java.net.URL

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@SuppressLint("ValidFragment")
class MeFragment(private val activity: Activity) : Fragment() {




    private lateinit var user: User

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val inflate = inflater.inflate(R.layout.fragment_me, container, false)





        val upload_pet = inflate.findViewById<LinearLayout>(R.id.pet_upload)

        val request_list = inflate.findViewById<LinearLayout>(R.id.my_quest)

        val head = inflate.findViewById<ImageView>(R.id.sv_me_head)

        val mypets = inflate.findViewById<LinearLayout>(R.id.my_pet)
        val return_login = inflate.findViewById<LinearLayout>(R.id.return_login)
        return_login.setOnClickListener {
            val intent = Intent()
            intent.setClass(activity,LoginActivity::class.java)
            startActivity(intent)
        }

        mypets.setOnClickListener {
            val intent = Intent()
            intent.setClass(activity,MyPetsActivity::class.java)
            startActivity(intent)
        }
        upload_pet.setOnClickListener {
            val intent = Intent()
            intent.setClass(activity,UploadPetActivity::class.java)
            startActivity(intent)
        }

        request_list.setOnClickListener {
            val intent = Intent()
            intent.setClass(activity,RequestMatchActivity::class.java)
            startActivity(intent)
        }

        head.setOnClickListener {
            val intent = Intent()
            intent.setClass(activity,MyInfoActivity::class.java)
            intent.putExtra("user",user)
            startActivity(intent)
        }

        async {
            val sharedPreferences = activity.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
            val user_id = sharedPreferences.getString("user_id", "")
            val host = "${DataUtil.host}/getUser"
            val url = "$host?uid=$user_id"
            val response = URL(url).readText()
            val gson = Gson()
            user = gson.fromJson(response, User::class.java)
            val name = user.name
            uiThread {
                tv_user_name.text = name
                Log.d("user_header",user.head)
                Glide.with(activity).load(user.head).transform(GlideCircleTransform(activity)).into(head)

            }
        }

        return inflate

    }


}// Required empty public constructor
