package com.example.administrator.pet_go.Comment


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.administrator.pet_go.JavaBean.Moment
import com.example.administrator.pet_go.R
import com.example.administrator.pet_go.Util.DataUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_item_list.*
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread
import java.net.URL


import java.util.ArrayList

/**
 * Created by Administrator on 2018/11/24/024.
 */


@SuppressLint("ValidFragment")
class MomentFragment : Fragment {


    val context: Activity

    constructor(context: Activity) : super() {
        this.context = context
    }

    private val host = "${DataUtil.host}/moment"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        val gson = Gson()

        async {
            val preferences = container?.context?.getSharedPreferences("userinfo",Context.MODE_PRIVATE)
            val user_id = preferences?.getString("user_id","")
            val url  = "$host?uid=$user_id"
            val response = URL(url).readText()
            uiThread {
                Log.d("json----",response)
                val list: ArrayList<Moment> = gson.fromJson(response, object :TypeToken<ArrayList<Moment>>(){}.type)
                for (moment in list) {
                    Log.d("list",moment.content)
                }

                val momentAdapter = MomentAdapter(context, list)
                list_moment.adapter = momentAdapter
//                momentAdapter.notifyDataSetChanged()
            }
        }
        val add = view.findViewById<FloatingActionButton>(R.id.fb_add)
        add.setOnClickListener {
            val intent = Intent()
            intent.setClass(container?.context,UploadMomentActivity::class.java)
            startActivity(intent)
        }
        return view
    }


}
