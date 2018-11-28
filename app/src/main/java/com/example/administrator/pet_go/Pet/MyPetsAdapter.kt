package com.example.administrator.pet_go.Pet

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.administrator.pet_go.JavaBean.Pet
import com.example.administrator.pet_go.R
import com.example.administrator.pet_go.UI.GlideCircleTransform

/**
 * Created by Administrator on 2018/11/28/028.
 */
class MyPetsAdapter(private val activity: Activity,private val list: MutableList<Pet>) : RecyclerView.Adapter<MyPetsAdapter.MyHolder>() {



    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        Log.d("ggafsadf","来了")
        val pet = list[position]
        holder.pet_name.text = pet.name
        holder.pet_note.text = pet.note
        holder.pet_age.text = pet.age
        holder.pet_sex.text = pet.sex
        holder.pet_variety.text = pet.variety
        Log.d("variety",pet.variety)
        Glide.with(activity).load(pet.picture[0]).transform(GlideCircleTransform(activity)).into(holder.pet_picture)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHolder {
        val view = LayoutInflater.from(activity).inflate(R.layout.my_pet_item, parent, false)
        return MyHolder(view)
    }


    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val pet_picture: ImageView
        val pet_name: TextView
        val pet_note:TextView
        val pet_sex:TextView
        val pet_age:TextView
        val pet_variety:TextView

        init {
            pet_picture = itemView.findViewById(R.id.pet_picture)
            pet_name = itemView.findViewById(R.id.pet_name)
            pet_note = itemView.findViewById(R.id.pet_note)
            pet_sex = itemView.findViewById(R.id.pet_sex)
            pet_age = itemView.findViewById(R.id.pet_age)
            pet_variety = itemView.findViewById(R.id.pet_variety)

        }
    }
}