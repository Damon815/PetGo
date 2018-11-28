package com.example.administrator.pet_go.Explore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.pet_go.R;

import java.util.List;

/**
 * Created by Administrator on 2018/11/25.
 */

public class MyAdapter_infodetails extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<String> data;
    private Context context;
    //	构造器，接收数据
    public MyAdapter_infodetails(List<String> data, Context context){
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
            convertView= mInflater.inflate(R.layout.listitem_details, parent,false);
            //viewHolder.type =convertView.findViewById(R.id.type);
            viewHolder.picture=convertView.findViewById(R.id.img);

            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder)convertView.getTag();
        }
        //Log.e("table",data.get(position).table+"1");

        Glide.with(context)
                .load(data.get(position))
                .placeholder(R.drawable.wait)
                .into(viewHolder.picture);


        return convertView;
    }

    class ViewHolder{ ImageView picture; }


}
