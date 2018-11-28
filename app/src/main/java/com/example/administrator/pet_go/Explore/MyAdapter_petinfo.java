package com.example.administrator.pet_go.Explore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.pet_go.JavaBean.MyContent_petinfo;
import com.example.administrator.pet_go.R;

import java.util.List;

/**
 * Created by Administrator on 2018/11/25.
 */

public class MyAdapter_petinfo extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<MyContent_petinfo> data;
    private Context context;
    //	构造器，接收数据
    public MyAdapter_petinfo(List<MyContent_petinfo> data, Context context){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {

        return data == null? 0:data.size();
    }

    @Override
    public Object getItem(int position) {

        return data.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null) {
            viewHolder=new ViewHolder();
            convertView= mInflater.inflate(R.layout.listitem, parent,false);
            //viewHolder.type =convertView.findViewById(R.id.type);
            viewHolder.name=convertView.findViewById(R.id.text_name);
            viewHolder.variety=convertView.findViewById(R.id.text_variety);
            viewHolder.sex=convertView.findViewById(R.id.text_sex);
            viewHolder.age=convertView.findViewById(R.id.text_age);
            viewHolder.MyLinear=convertView.findViewById(R.id.My_Linear);
            viewHolder.picture=convertView.findViewById(R.id.img);

            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        //Log.e("table",data.get(position).table+"1");
        viewHolder.name.setText("name:"+data.get(position).getName());
        viewHolder.variety.setText(data.get(position).getVariety());
        viewHolder.sex.setText(data.get(position).getSex());
        viewHolder.age.setText(data.get(position).getAge()+"岁");

        Glide.with(context)
                .load(data.get(position).getPicture().get(0))
                .placeholder(R.drawable.wait)
                .into(viewHolder.picture);

        viewHolder.MyLinear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("name",data.get(position).getName());
                bundle.putString("username",data.get(position).getUsername());
                bundle.putString("type",data.get(position).getType());
                bundle.putString("variety",data.get(position).getVariety());
                bundle.putString("sex",data.get(position).getSex());
                bundle.putString("usersex",data.get(position).getUsersex());
                bundle.putInt("userage",data.get(position).getUserage());
                bundle.putInt("age",data.get(position).getAge());
                bundle.putInt("uid",data.get(position).getUid());
                bundle.putInt("pid",data.get(position).getPid());
                bundle.putString("note",data.get(position).getNote());
                bundle.putString("address",data.get(position).getAddress());

                int length=data.get(position).getPicture().size();
                String  pictures[]=new String[length];
                for(int i=0;i<length;i++){
                    pictures[i]=data.get(position).getPicture().get(i);
                    bundle.putString("picture"+i,pictures[i]);
                }
                bundle.putInt("length",length);

                Intent intent=new Intent(context,piDetailsActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);


            }
        });







        return convertView;
    }

    class ViewHolder{
        TextView name;
        TextView variety;
        TextView sex;
        TextView age;
        ImageView picture;
        LinearLayout MyLinear;

    }

}



