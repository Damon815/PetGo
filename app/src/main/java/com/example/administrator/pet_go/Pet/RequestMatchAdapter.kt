package com.example.administrator.pet_go.Pet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.administrator.pet_go.JavaBean.Pet
import com.example.administrator.pet_go.JavaBean.User
import com.example.administrator.pet_go.R
import com.example.administrator.pet_go.Util.DataUtil
import com.example.administrator.pet_go.Util.ImageUtils
import com.szysky.customize.siv.SImageView
import org.jetbrains.anko.async
import org.jetbrains.anko.textColor
import org.jetbrains.anko.uiThread

/**
 * Created by Administrator on 2018/11/26/026.
 */

class RequestMatchAdapter(private val context: Context, private val users: MutableList<User>,
                          private val pets: MutableList<Pet>,
                          private val states: MutableList<Int>,
                          private val ownPets: MutableList<String>) : RecyclerView.Adapter<RequestMatchAdapter.MyViewHolder>(){


    override fun getItemCount(): Int {
       return users.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.request_list_item,parent,false)
        return MyViewHolder(view)
    }
    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        val user = users[position]
        val pet = pets[position]
        val state = states[position]
        holder?.apply{

            if (state == DataUtil.MATCHSTATE_YET){//待定变灰
                yet()
            }else if (state != DataUtil.MATCHSTATE_DISAGREE){ //除不同意之外才加载
                request_name.text = user.name
                request_sex.text = user.sex
                request_province.text = user.province
                request_city.text = user.city

                pet_type.text = pet.type
                pet_name.text = pet.name
                pet_age.text = pet.age
                pet_sex.text = pet.sex

                //获取头像
                async {
                    val header_url = user.head
                    val head_bitmap = ImageUtils.getURLimage(header_url)
                    uiThread {
                        request_header.setBitmap(head_bitmap)
                    }
                }
                request_item.setOnClickListener {
                    //进入申请人的详情
                    val intent  = Intent()
                    intent.setClass(context,RequestDetailsActivity::class.java)
                    val bundle  = Bundle()
                    context.startActivity(intent)
                }
            }
        }

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val request_item = itemView.findViewById<LinearLayout>(R.id.request_item)
        val request_header: SImageView = itemView.findViewById(R.id.request_header)
        val request_name: TextView = itemView.findViewById(R.id.tv_request_name)
        val request_sex = itemView.findViewById<TextView>(R.id.tv_request_sex)
        val request_province = itemView.findViewById<TextView>(R.id.tv_request_province)
        val request_city = itemView.findViewById<TextView>(R.id.tv_request_city)
        val pet_type = itemView.findViewById<TextView>(R.id.tv_pet_type)
        val pet_name = itemView.findViewById<TextView>(R.id.tv_pet_name)
        val pet_age = itemView.findViewById<TextView>(R.id.tv_pet_age)
        val pet_sex = itemView.findViewById<TextView>(R.id.tv_pet_sex)

        fun yet(){
            request_name.textColor = context.resources.getColor(R.color.gray)
            request_sex.textColor = context.resources.getColor(R.color.gray)
            request_province.textColor = context.resources.getColor(R.color.gray)
            request_city.textColor = context.resources.getColor(R.color.gray)
            pet_type.textColor = context.resources.getColor(R.color.gray)
            pet_name.textColor = context.resources.getColor(R.color.gray)
            pet_age.textColor = context.resources.getColor(R.color.gray)
            pet_sex.textColor = context.resources.getColor(R.color.gray)
        }
    }
}