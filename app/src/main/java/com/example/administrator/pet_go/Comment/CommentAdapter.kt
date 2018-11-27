package com.example.administrator.pet_go.Comment

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.administrator.pet_go.JavaBean.Comment
import com.example.administrator.pet_go.R
import kotlin.math.log

/**
 * Created by Administrator on 2018/11/24/024.
 */
class CommentAdapter internal constructor(private val context: Context, private val list: MutableList<Comment>) : RecyclerView.Adapter<CommentAdapter.MyViewHolder>() {
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val comment = list[position]
        holder.user.text =  comment.user_name+":"
        holder.comment.text = comment.content


        holder.user.setOnClickListener {
            //查看评论人的信息
            val user_id = comment.user_id
            //...

        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    fun addItem(comment: Comment){
        list.add(comment)
        notifyItemInserted(list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CommentAdapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.comment_item, null, false))
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var user: TextView
        var comment: TextView
        init {
            user  = itemView.findViewById(R.id.tv_user)
            comment = itemView.findViewById(R.id.tv_comment)
        }
    }

}
