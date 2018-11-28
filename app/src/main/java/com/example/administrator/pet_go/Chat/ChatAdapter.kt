package com.example.administrator.pet_go.Chat

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.administrator.pet_go.JavaBean.Chat
import com.example.administrator.pet_go.R
import com.example.administrator.pet_go.UI.GlideCircleTransform

/**
 * Created by Administrator on 2018/11/28/028.
 */

class ChatAdapter(private val context: Activity,private val chatList: MutableList<Chat>) : RecyclerView.Adapter<ChatAdapter.MyHolder>() {

    private val user_id: String
    init {
        val sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        user_id = sharedPreferences.getString("user_id","")
    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.chat_message,parent,false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val chat = chatList[position]
        val chat_uid0 = chat.uid0
        holder.time.text = chat.time
        if (chat_uid0 == user_id){ //我发的
            holder.chat_you.visibility = View.GONE
            holder.chat_me.visibility = View.VISIBLE

            holder.chat_me_text.text = chat.talk
            Glide.with(context).load(chat.head0).transform(GlideCircleTransform(context)).into(holder.chat_me_head)

        }else{
            holder.chat_you.visibility = View.VISIBLE
            holder.chat_me.visibility = View.GONE

            holder.chat_you_text.text = chat.talk
            Glide.with(context).load(chat.head0).transform(GlideCircleTransform(context)).into(holder.chat_you_head)
        }
    }


    override fun getItemCount(): Int {
        return chatList.size
    }
    inner class MyHolder(item: View) : RecyclerView.ViewHolder(item){
        val chat_you = item.findViewById<LinearLayout>(R.id.chat_you) //对方的面板
        val chat_me = item.findViewById<LinearLayout>(R.id.chat_me) //我的面板

        val time = item.findViewById<TextView>(R.id.time)
        val chat_you_head = item.findViewById<ImageView>(R.id.you_head)
        val chat_you_text = item.findViewById<Button>(R.id.you_text)
        val chat_me_head = item.findViewById<ImageView>(R.id.me_head)
        val chat_me_text = item.findViewById<Button>(R.id.me_text)
    }
}
