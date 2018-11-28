package com.example.administrator.pet_go.Chat

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.administrator.pet_go.R
import com.example.administrator.pet_go.UI.GlideCircleTransform

/**
 * Created by Administrator on 2018/11/28/028.
 */

class ChatListAdapter(private val context: Activity,private val head_list: MutableList<String>,private val name_list: MutableList<String>) : RecyclerView.Adapter<ChatListAdapter.MyHolder>() {

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        val head = head_list[position]
        val name = name_list[position]

        holder.you_name.text = name
        Glide.with(context).load(head).transform(GlideCircleTransform(context)).into(holder.you_head)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder? {
        val view = LayoutInflater.from(context).inflate(R.layout.chat_item,parent,false)
        return MyHolder(view)
    }



    override fun getItemCount(): Int {
        return head_list.size
    }

    inner class MyHolder(item: View) : RecyclerView.ViewHolder(item){
        val you_head = item.findViewById<ImageView>(R.id.you_head)
        val you_name = item.findViewById<TextView>(R.id.you_name)
        val chat_time = item.findViewById<TextView>(R.id.time)
    }
}
