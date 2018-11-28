package com.example.administrator.pet_go.Explore;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.pet_go.JavaBean.MyContent_idANDname;
import com.example.administrator.pet_go.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class piDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView name,varietyANDtype,sex,age,note,username,userage,usersex,address;
    private String name1,varietyANDtype1,sex1,note1,username1,usersex1,address1;
    private String message;
    private String msg="";
    private String msg1="";
    private ListView lv;
    private Button match;
    private MyAdapter_infodetails myAdapter_infodetails;
    private List<String> list;
    private List<MyContent_idANDname> list1;
    private int uid0=123;
    private int length,userage1,age1,pid0,uid1,pid1;
    Context context;
    MyHandler myHandler;

    private String[] pet;
    //private int[] pid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pi_details);
        context=this;

        initView();
        initEvent();
        MyThread1 myThread1=new MyThread1();
        new Thread(myThread1).start();
        myHandler=  new MyHandler(this.getMainLooper());
    }
    private void initView(){
        username=this.findViewById(R.id.username);
        userage=this.findViewById(R.id.userage);
        usersex=this.findViewById(R.id.usersex);
        address=this.findViewById(R.id.address);
        name=this.findViewById(R.id.name);
        varietyANDtype=this.findViewById(R.id.typeANDvariety);
        sex=this.findViewById(R.id.sex);
        age=this.findViewById(R.id.age);
        note=this.findViewById(R.id.note);
        match=this.findViewById(R.id.match);
        lv=this.findViewById(R.id.pictures);

        list=getData();
        myAdapter_infodetails=new MyAdapter_infodetails(list,this);
        lv.setAdapter(myAdapter_infodetails);

        Intent intent=getIntent();
        uid1=intent.getIntExtra("uid",0);
        pid1=intent.getIntExtra("pid",0);
        username1=intent.getStringExtra("username");
        username.setText("name of master:"+username1);
        usersex1=intent.getStringExtra("usersex");
        usersex.setText(usersex1);
        userage1=intent.getIntExtra("userage",0);
        userage.setText(userage1+"岁");
        address1=intent.getStringExtra("address");
        address.setText(address1);
        name1=intent.getStringExtra("name");
        name.setText("name of pet:"+name1);
        sex1=intent.getStringExtra("sex");
        sex.setText(sex1);
        age1=intent.getIntExtra("age",0);
        age.setText(age1+"岁");
        note1=intent.getStringExtra("note1");
        note.setText(note1);
        length=intent.getIntExtra("length",0);
        varietyANDtype1="("+intent.getStringExtra("type")+")"+intent.getStringExtra("variety");
        varietyANDtype.setText(varietyANDtype1);

    }

    private void initEvent(){
        match.setOnClickListener(this);
    }

    private ArrayList<String> getData(){
        ArrayList<String> list=new ArrayList<>();
        Intent intent=getIntent();
        for(int i=0;i<length;i++){
            list.add(intent.getStringExtra("picture"+i));
        }
        return list;
    }

//    private void showDialog(final String[] pet){
//        new AlertDialog.Builder(this)
//                .setTitle("请选择您想要与之配对的宠物")
//                .setItems(pet, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog,int which){
//                        table1=pet[which];
//                    }
//                })
//                .setNegativeButton("取消",new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        return;
//                    }
//                })
//                .show();
//    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.match:

                //Toast.makeText(context, "您的爱心已biu出~", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(context)
                        .setTitle("请选择您想要与之配对的宠物")
                        .setItems(pet, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which){
                                pid0=list1.get(which).getPid();
                                //在这里把请求发送者的pid-uid发给服务器
                                MyThread m=new MyThread();
                                new Thread(m).start();

                            }
                        })
                        .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .show();







                break;
        }
    }

    class MyThread implements Runnable {
        public void run() {
            //String s="六味";
            String m=doPost0(uid0,pid0,uid1,pid1);

            Message msg = myHandler.obtainMessage();
            msg.what=1;

            //Bundle是message中的数据
            Bundle bundle = new Bundle();
            bundle.putString("数据",m);

            //传递数据
            msg.setData(bundle);

            myHandler.sendMessage(msg); // 向Handler发送消息,传递信息给主线程

        }
    }

    class MyThread1 implements Runnable {
        public void run() {
            //String s="六味";
            String m=doPost1(uid0);
            JSONObject jsonObject = null;
            if(!m.equals("")){
                try {
                    jsonObject = new JSONObject(m);
                    if(jsonObject.getString("message").equals("获取成功")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        int ii = jsonArray.length();
                        for (int i = 0; i < ii; i++) {
                            MyContent_idANDname idANDname = new Gson().fromJson(jsonArray.getString(i), MyContent_idANDname.class);
                            list1.add(idANDname);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            if (list1!=null){
                pet=new String[list1.size()];
                //pid=new int[list1.size()];
                for (int i=0;i<list1.size();i++){
                    pet[i]=list1.get(i).getName();
                    //pid[i]=list1.get(i).getPid();
                }

            }



        }
    }


    class MyHandler extends Handler {
        public MyHandler() {
        }

        public MyHandler(Looper L) {
            super(L);
        }

        // 必须重写这个方法，用于处理message
        @Override
        public void handleMessage (Message msg) {
            if(msg.what==1){
                // 这里用于更新UI
                Bundle b = msg.getData();
                String data = b.getString("数据");
                try {
                    message=getMsg(data);
                    System.out.print(message+"message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(message.equals("404")){
                    Toast.makeText(context, "配对请求发送失败，请稍后重试~", Toast.LENGTH_SHORT).show();
                    Log.e("message",message);

                }
                else if(message.equals("提交成功")){
                    Toast.makeText(context, "您的爱心已biu出~", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(context, "哥也很迷茫", Toast.LENGTH_SHORT).show();
                    Log.e("message",message);
                }
            }
        }
    }

    public String doPost1(int uid ){
        try {

            String body= "{\"uid\":\""+uid+"\"}";
            String b= URLEncoder.encode(body,"utf-8");

            URL url = new URL("http://10.27.194.15:8080/getPetName?json="+b);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.setRequestProperty("Cookie",LogActivity.responseCookie);
            connection.setRequestMethod("POST");//设置请求方式为POST
            connection.setDoOutput(true);//允许写出
            connection.setConnectTimeout(3000);
            connection.setDoInput(true);//允许读入
            connection.setUseCaches(false);//不使用缓存
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            //connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();//连接


//            String body= "{\"uid\":\""+uid+"\"}";
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
//            writer.write(body);
//            writer.close();
//
            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = connection.getInputStream();
                //String result = is2String(inputStream);//将流转换为字符串。
                //Log.d("kwwl","result============="+result);
            }

            int code = connection.getResponseCode();
            // 如果返回值正常，数据在网络中是以流的形式得到服务端返回的数据
            if (code == 200) { // 正常响应
                // 从流中读取响应信息
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String line = null;



                while ((line = reader.readLine()) != null) { // 循环从流中读取
                    msg1 = msg1 + line + "\n";
                    //Log.e("line",line+"0");

                }
                reader.close(); // 关闭流
            }else{
                //Log.e("ResponseCode","没连上");
            }
            //断开连接，释放资源
            connection.disconnect();


        }catch (Exception e){
            e.printStackTrace();
        }
        //System.out.println(msg+"666");
        return  msg1;

    }

    public String doPost0(int uid0,int pid0,int uid1,int pid1 ){
        try {

            String body= "{\"uid0\":\""+uid0+"\",\"pid0\":\"" + pid0 + "\",\"uid1\":\""+uid1+"\",\"pid1\":\""+pid1+"\"}";
            String b=URLEncoder.encode(body,"utf-8");


            URL url = new URL("http://10.27.194.15:8080/insertWaitMatch?json="+b);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.setRequestProperty("Cookie",LogActivity.responseCookie);
            connection.setRequestMethod("POST");//设置请求方式为POST
            connection.setDoOutput(true);//允许写出
            connection.setConnectTimeout(3000);
            connection.setDoInput(true);//允许读入
            connection.setUseCaches(false);//不使用缓存
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            //connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();//连接


//            String body= "{\"uid0\":\""+uid0+"\",\"pid0\":\"" + pid0 + "\",\"uid1\":\""+uid1+"\",\"pid1\":\""+pid1+"\"}";
//
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
//            writer.write(body);
//            writer.close();
            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = connection.getInputStream();
                //String result = is2String(inputStream);//将流转换为字符串。
                //Log.d("kwwl","result============="+result);
            }

            int code = connection.getResponseCode();
            // 如果返回值正常，数据在网络中是以流的形式得到服务端返回的数据
            if (code == 200) { // 正常响应
                // 从流中读取响应信息
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String line = null;



                while ((line = reader.readLine()) != null) { // 循环从流中读取
                    msg = msg + line + "\n";
                    //Log.e("line",line+"0");

                }
                reader.close(); // 关闭流
            }else{
                //Log.e("ResponseCode","没连上");
            }
            //断开连接，释放资源
            connection.disconnect();


        }catch (Exception e){
            e.printStackTrace();
        }
        //System.out.println(msg+"666");
        return  msg;

    }






    public String getMsg(String msg) throws JSONException {
        if (msg.equals(null)|| msg.equals("")) {
            return "404";

        } else {
            JSONObject jsonObject = new JSONObject(msg);

            //            JSONArray jsonArray = new JSONArray(msg);
            //            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String message = jsonObject.getString("message");
            return message;
        }

    }
}
