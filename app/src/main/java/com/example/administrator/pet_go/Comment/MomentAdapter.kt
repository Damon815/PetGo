package com.example.administrator.pet_go.Comment

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.administrator.pet_go.JavaBean.Comment

import com.example.administrator.pet_go.JavaBean.Moment
import com.example.administrator.pet_go.R
import com.example.administrator.pet_go.UI.RoundRectImageview
import com.example.administrator.pet_go.Util.DataUtil
import com.example.administrator.pet_go.Util.ImageUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.szysky.customize.siv.SImageView
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread
import java.net.URL
import java.net.URLEncoder
import java.util.ArrayList

/**
 * Created by Administrator on 2018/11/24/024.
 */

class MomentAdapter internal constructor(private val context: Context, private val list: MutableList<Moment>) : RecyclerView.Adapter<MomentAdapter.MyViewHolder>() {

    private val host = "${DataUtil.host}/comment"
   init {
       for (moment in list) {
           Log.d("adapter",moment.content)
       }
   }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.d("list_size","$itemCount")
        val moment = list[position]
        holder.name.text = moment.name
        holder.time.text = moment.time
        holder.content.text = moment.content
        Log.d("content",moment.content)

        holder.like_count.text = ""+moment.like_count
        holder.comment_count.text = ""+moment.comment_count
        val like = moment.isLike
        val red = context.resources.getColor(R.color.heart)
        val gray = context.resources.getColor(R.color.gray)
        if (like) holder.like.setColorFilter(red) else holder.like.setColorFilter(gray)



        //点赞绑定
        holder.like.setOnClickListener {
            val like = moment.isLike
            if (like){
                //前端
                holder.like.setColorFilter(gray)
                holder.like_count.text = ""+(moment.like_count-1)
                //JavaBean
                moment.like_count -= 1
                moment.isLike = false

            }else{
                //前端
                holder.like.setColorFilter(red)
                holder.like_count.text = ""+(moment.like_count+1)
                //JavaBean
                moment.like_count +=1
                moment.isLike = true

            }
            //发送给后台
            async {
                val id = moment.id
                val like = moment.isLike
                val preferences = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE)
                val user_id = preferences.getString("user_id","")
                val like_count = moment.like_count
                val host = "${DataUtil.host}/addLike"
                val url = "$host?mid=$id&like=$like&uid=$user_id"
                val response = URL(url).readText()
                uiThread {
                    if (response.toLowerCase().equals("true")){
                        Toast.makeText(context,"点赞成功",Toast.LENGTH_SHORT)
                    }
                }

            }
        }
        //获取头像与图片
        async {
            val header_url = moment.header_url
            val header = ImageUtils.getURLimage(header_url)
            val picture_url = moment.picture_url
            var picture: Bitmap? = null
            if (!picture_url.isEmpty()){
                picture = ImageUtils.getURLimage(picture_url)
            }
            uiThread {
                holder.header.setBitmap(header)
                if (picture!=null){
                    holder.picture.setImageBitmap(picture)
                    holder.picture.visibility = View.VISIBLE
                }
            }
        }

            async {

                //去访问后台查看评论数据(通过moment id  //返回json对象
                val mid = moment.id
                val url = "$host?mid=$mid"
                val response = URL(url).readText()
                var commentAdapter: CommentAdapter
                uiThread {
                    val comments: ArrayList<Comment> = Gson().fromJson(response,object : TypeToken<ArrayList<Comment>>(){}.type)
                    commentAdapter = CommentAdapter(context, comments)
                    holder.list_comment.adapter = commentAdapter
//                    notifyDataSetChanged()
                }
            }
        //添加评论
        holder.input_comment.setOnEditorActionListener { v, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_SEND){

                async {
                    //添加评论
                    val host = "${DataUtil.host}/insertComment"
                    val content = v.text.toString()
                    val preferences = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE)
                    val user_id = preferences.getString("user_id","")
                    val id = moment.id
                    val content_ = URLEncoder.encode(content,"utf-8")
                    val url = "$host?cuid=$user_id&mid=$id&content=$content_"
                    val response = URL(url).readText()
                    uiThread {
                        if (response.toLowerCase().equals("false")){

                        }else{
                            val user_name = response
                            val comment = Comment(user_name,content)

                            val adapter  = holder.list_comment.adapter
                            if (adapter != null){
                                val comment_adapter = adapter as CommentAdapter
                                comment_adapter.addItem(comment)
//                        Log.d("asdjflaksjdf","caoaooaoaa")
                            }else{

                                val list = ArrayList<Comment>()
                                list.add(comment)
                                val adapter = CommentAdapter(context,list)
                                holder.list_comment.adapter = adapter
                            }
                            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                            v.text = ""
                            moment.comment_count += 1
                            val count =moment.comment_count
                            holder.comment_count.text = "$count"

                        }
                    }
                }
            }
            false
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var header: SImageView
        var name: TextView
        var time: TextView
        var picture: RoundRectImageview
        var content: TextView
        var like: ImageView
        var like_count: TextView
        var comment: ImageView
        var comment_count: TextView
        var list_comment: RecyclerView
        var input_comment: EditText
        init {
            header = itemView.findViewById(R.id.sv_header)
            name = itemView.findViewById(R.id.tv_name)
            time = itemView.findViewById(R.id.time)
            picture = itemView.findViewById(R.id.iv_picture)
            content = itemView.findViewById(R.id.tv_content)
            like = itemView.findViewById(R.id.iv_zan)
            comment = itemView.findViewById(R.id.iv_comment)
            like_count = itemView.findViewById(R.id.tv_like_count)
            comment_count = itemView.findViewById(R.id.tv_comment_count)
            list_comment = itemView.findViewById(R.id.list_comment)
            input_comment = itemView.findViewById(R.id.et_comment)
            val manager = LinearLayoutManager(itemView.context)
            manager.orientation = LinearLayoutManager.VERTICAL
            manager.isAutoMeasureEnabled = true
            list_comment.layoutManager = manager

        }
    }
}
