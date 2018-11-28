package com.example.administrator.pet_go.Explore;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.example.administrator.pet_go.JavaBean.MyContent_petinfo;
import com.example.administrator.pet_go.JavaBean.MyContent_pictures;
import com.example.administrator.pet_go.JavaBean.MyContent_tyANDva;
import com.example.administrator.pet_go.R;
import com.example.administrator.pet_go.UI.RefreshableView;
import com.example.administrator.pet_go.Util.DataUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@SuppressLint("ValidFragment")
public class HomePageFragment extends Fragment implements View.OnClickListener {

    private Button open,sure,close;
    private ViewPager myVP;
    private DrawerLayout drawerLayout;
    private LinearLayout mDotContainer,MyLinear;
    private List<ImageView> list0;
    private List<MyContent_petinfo> list1;
    private List<MyContent_tyANDva> list2;
    private ListView lv;
    private MyAdapter_banner myAdapter_banner;
    private MyAdapter_petinfo myAdapter_petinfo;
    private MyContent_pictures pp;
    private MyContent_petinfo myContent_petinfo;
    RefreshableView refreshableView;
    MyHandler myHandler;
    private EditText age,province,city;
    private Spinner spinner0,spinner1,spinner2;
    private String type1="",variety1="",sex1="",province1,city1;
    private int age1;
    private String[] type2;
    private ArrayList<String> variety2;
    private TextView text_type,text_variety,text_sex;


    private int mPreviousPosition;
    private final int CHANGE_PAGE = 1;
    private boolean play = false;
    //private String pictures;
    private static final String TAG = "MainActivity";
    private String pictures="";
    private String information="";
    private String searchresult="";
    private AMapLocationClient mlocationClient;

    private Activity activity;

    public HomePageFragment(Activity activity){
        super();
        this.activity = activity;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_home_page,container,false);



        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)!=
        PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION} ,2);
        }
        getLocation();
        initView(view);
        initEvent();
        initData();
        initListener();
        myHandler=  new MyHandler(getActivity().getMainLooper());

        //!!!!!!!!!!!想把广告位换成活的就把下面两行放出来！！！！！！
//        MyThread m=new MyThread();
//        new Thread(m).start();

        MyThread1 m1=new MyThread1();
        new Thread(m1).start();

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                //设置拉出布局的宽度
                MyLinear.setX(slideOffset * drawerView.getWidth());

//                Log.e(TAG, "onDrawerSlide: " + "滑动时执行");
//                Log.e(TAG, "onDrawerSlide偏移量: " + slideOffset);
//                Log.e(TAG, "onDrawerSlide偏移的宽度: " + drawerView.getWidth());
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
//                Log.e(TAG, "onDrawerSlide: " + "完全展开时执行");
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
//                Log.e(TAG, "onDrawerSlide: " + "完全关闭时执行");
            }

            @Override
            public void onDrawerStateChanged(int newState) {
//                Log.e(TAG, "onDrawerSlide: " + "改变状态时时执行");
            }
        });
//        handleSSLHandshake();






       String info="{data:[{\"type\": \"宠物狗\", \"variety\": [\"边境牧羊犬\", \"博美犬\", \"柴犬\", \"腊肠犬\", \"杜宾犬\", \"德国牧羊犬\", \"柯基犬\", \"藏獒\", \"比熊犬\", \"雪纳瑞犬\", \"贵宾犬\", \"高加索犬\"]}, {\"type\": \"宠物猫\", \"variety\": [\"暹罗猫\", \"波斯猫\", \"挪威森林猫\", \"孟买猫\", \"英国短毛猫\", \"布偶猫\", \"土耳其梵猫\", \"山东狮子猫\", \"折耳猫\", \"缅因猫\", \"异国短毛猫\", \"新加坡猫\"]}, {\"type\": \"宠物鼠\", \"variety\": [\"仓鼠\", \"布丁仓鼠\", \"龙猫\", \"老婆婆仓鼠\", \"金狐仓鼠\", \"紫衣仓鼠\", \"奶茶仓鼠\", \"黑熊仓鼠\", \"熊猫仓鼠\", \"三线仓鼠\", \"纯白龙猫\", \"银狐仓鼠\"]}, {\"type\": \"宠物兔\", \"variety\": [\"伊拉兔\", \"荷兰垂耳兔\", \"荷兰侏儒兔\", \"迷你垂耳兔\", \"荷兰兔\", \"巨型安哥拉兔\", \"法国垂耳兔\", \"海棠兔\", \"巨型花明兔\", \"英国垂耳兔\", \"加利福尼亚兔\", \"比利时兔\"]}, {\"type\": \"观赏鱼\", \"variety\": [\"鹦鹉鱼\", \"龙鱼\", \"锦鲤鱼\", \"银龙鱼\", \"罗汉鱼\", \"孔雀鱼\", \"地图鱼\", \"小丑鱼\", \"斗鱼\", \"接吻鱼\", \"神仙鱼\", \"财神鱼\"]}, {\"type\": \"观赏鸟\", \"variety\": [\"百灵鸟\", \"暗绿绣眼鸟\", \"画眉鸟\", \"太平鸟\", \"相思鸟\", \"八哥鸟\", \"鹩哥\", \"虎皮鹦鹉\", \"金丝雀\", \"黄鹂\", \"云雀\", \"黄雀\"]}, {\"type\": \"观赏龟\", \"variety\": [\"缅甸陆龟\", \"黄头侧颈龟\", \"大鳄龟\", \"蛋龟\", \"火焰龟\", \"中华草龟\", \"鹰嘴龟\", \"猪鼻龟\", \"地图龟\", \"平背麝香龟\", \"玳瑁\", \"金钱龟\"]}, {\"type\": \"宠物蛇\", \"variety\": [\"赤练蛇\", \"喜玛拉雅白头蛇\", \"太攀蛇\", \"金环蛇\", \"眼镜王蛇\", \"玉米蛇\", \"水蚺蛇\", \"黑曼巴蛇\", \"黄金蟒\", \"网纹蟒\", \"球蟒\", \"蝮蛇\"]}, {\"type\": \"蜘蛛\", \"variety\": [\"智利火玫瑰\", \"泰国金属蓝\", \"红玫瑰蜘蛛\", \"巴西巨人金毛\", \"巴西红毛蜘蛛\", \"铁锈红巴布\", \"墨西哥火膝头\", \"海地咖啡食鸟蜘蛛\", \"泰国斑马脚\", \"墨西哥金背\", \"哥斯达黎加斑马脚\", \"秘鲁金铜\"]}, {\"type\": \"蜥蜴\", \"variety\": [\"鬃狮蜥\", \"绿鬣蜥\", \"中华石龙子\", \"中国水龙\", \"蝾螈\", \"四线石龙子\", \"火焰石龙子\", \"伞蜥\", \"犰狳蜥\", \"海鬣蜥\", \"新疆岩蜥\", \"盾甲蜥\"]}, {\"type\": \"鳄鱼\", \"variety\": [\"泰鳄\", \"凯门鳄\", \"侏儒凯门鳄\", \"湾鳄\", \"扬子鳄\", \"印度鳄\", \"尼罗鳄\"]}, {\"type\": \"宠物猴\", \"variety\": [\"松鼠猴\", \"狨猴\", \"指猴\", \"懒猴\", \"金丝猴\", \"日本袖珍石猴\", \"狐猴\", \"眼镜猴\", \"猕猴\", \"婴猴\"  ,  \"赤猴\", \"白面僧帽猴\"]}, {\"type\": \"宠物猪\", \"variety\": [\"小香猪\", \"越南大肚猪\", \"胡利亚尼猪\", \"麝香猪\", \"香猪\"]}, {\"type\": \"狐狸\", \"variety\": [\"蓝狐\", \"银狐\", \"大耳狐\", \"南非狐\", \"藏狐\", \"银黑狐\", \"阿富汗狐\", \"沙狐\", \"敏狐\", \"吕佩尔狐\", \"孟加拉狐\", \"彩狐\"]}, {\"type\": \"松鼠\", \"variety\": [\"欧洲红松鼠\", \"岩松鼠\", \"雪地松鼠\", \"长吻松鼠\", \"黄山松鼠\", \"土拔鼠\"]}, {\"type\": \"宠物貂\", \"variety\": [\"貂\", \"蒙眼貂\", \"东方色貂\", \"火焰色貂\", \"香槟色貂\", \"黑眼雪貂\", \"红眼雪貂\"]}]}";
        try {
            list2=new ArrayList<>();
            JSONObject jsonObject=new JSONObject(info);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            for(int i=0;i<jsonArray.length();i++){
                MyContent_tyANDva myContent_tyANDva=new Gson().fromJson(jsonArray.getString(i),MyContent_tyANDva.class);
                list2.add(myContent_tyANDva);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        type2=new String[list2.size()+1];
        type2[0]="";
        for(int i=0;i<list2.size();i++){
            type2[i+1]=list2.get(i).getType();
        }

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String[] sexs = getResources().getStringArray(R.array.sex);
                sex1="";
                sex1=sexs[pos];
                text_sex.setText(sex1);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, type2);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinner0 .setAdapter(adapter);
        spinner0.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

            //                String[] languages = getResources().getStringArray(R.array.languages);
            //                Toast.makeText(MainActivity.this, "你点击的是:"+languages[pos], 2000).show();
                type1=type2[pos];
                text_type.setText(type1);
                variety2=new ArrayList<>();

                variety2=list2.get(pos).getVariety();



                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, variety2);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //绑定 Adapter到控件
                spinner1 .setAdapter(adapter);
                spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int pos, long id) {

                        //                String[] languages = getResources().getStringArray(R.array.languages);
                        //                Toast.makeText(MainActivity.this, "你点击的是:"+languages[pos], 2000).show();
                        variety2=new ArrayList<>();
                        if (variety2.size()!=0){
                            variety1=variety2.get(pos);

                        }

                        text_variety.setText(variety1);
                        variety2=new ArrayList<>();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Another interface callback
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });


        return view;

    }

    private void getLocation() {
        mlocationClient = new AMapLocationClient(activity.getApplicationContext());
        AMapLocationClientOption mlocationClientOption = new AMapLocationClientOption();
        mlocationClientOption.setLocationCacheEnable(true);
        mlocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mlocationClient.setLocationOption(mlocationClientOption);
        mlocationClient.startLocation();
        mlocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null){
                    if (aMapLocation.getErrorCode() == 0){
//                        address = aMapLocation.address
                        String country = aMapLocation.getCountry();
                        String province = aMapLocation.getProvince();
                        String city = aMapLocation.getCity();
//                        i+=1
//                        address_show.text = "您的位置是${i} :$country-$province-$city 详细地址：$address"
                        if (!country.isEmpty()){
                            mlocationClient.stopLocation();

                            OkHttpClient okHttpClient = new OkHttpClient();
                            SharedPreferences sharedPreferences = activity.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                            String user_id = sharedPreferences.getString("user_id", "");
                            String host = DataUtil.host+"/address";
                            try {
                                String province_ = URLEncoder.encode(province,"utf-8");
                                String city_ = URLEncoder.encode(city,"utf-8");
                                String url  = host+"?province="+province_+"&city="+city_+"&uid="+user_id;
                                Request request = new Request.Builder()
                                        .url(url)
                                        .build();

                                okHttpClient.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {

                                    }
                                });
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }else{
//                        Log.d("amapErrorCode","${aMapLocation.errorCode}")
                    }

                }
            }
        });
    }
//    public static void handleSSLHandshake() {
//        try {
//            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
//                public X509Certificate[] getAcceptedIssuers() {
//                    return new X509Certificate[0];
//                }
//
//                @Override
//                public void checkClientTrusted(X509Certificate[] certs, String authType) {
//                }
//
//                @Override
//                public void checkServerTrusted(X509Certificate[] certs, String authType) {
//                }
//            }};
//
//            SSLContext sc = SSLContext.getInstance("TLS");
//            // trustAllCerts信任所有的证书
//            sc.init(null, trustAllCerts, new SecureRandom());
//            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//                    return true;
//                }
//            });
//        } catch (Exception ignored) {
//        }
//    }

    private void initView(View view){
        AppCompatActivity appCompatActivity= (AppCompatActivity) getActivity();

//        type=view.findViewById(R.id.type);
//        variety=view.findViewById(R.id.variety);
//        sex=view.findViewById(R.id.sex);
        text_variety=view.findViewById(R.id.text_variety);
        text_variety.setText(variety1);
        text_type=view.findViewById(R.id.text_type);
        text_type.setText(type1);
        text_sex=view.findViewById(R.id.text_sex);
        text_sex.setText(sex1);
        spinner0=view.findViewById(R.id.spinner0);
        spinner1=view.findViewById(R.id.spinner1);
        spinner2=view.findViewById(R.id.spinner2);
        age=view.findViewById(R.id.age);
        age.setInputType(InputType.TYPE_CLASS_NUMBER);
        province=view.findViewById(R.id.province);
        city=view.findViewById(R.id.city);
        //error=view.findViewById(R.id.error);
        open=view.findViewById(R.id.open);
        close=view.findViewById(R.id.close);
        sure=view.findViewById(R.id.sure);
        drawerLayout=view.findViewById(R.id.drawer);
        MyLinear=view.findViewById(R.id.My_Linear);
        myVP=view.findViewById(R.id.view_pager);
        mDotContainer=view.findViewById(R.id.dot_container);
        lv=view.findViewById(R.id.content);
        refreshableView=view.findViewById(R.id.refreshable_view);

        myAdapter_banner=new MyAdapter_banner(list0);
        myAdapter_petinfo=new MyAdapter_petinfo(list1,appCompatActivity);
        lv.setAdapter(myAdapter_petinfo);
        myVP.setAdapter(myAdapter_banner);

    }

    private void initData(){
        initViewPagerData();
        myAdapter_banner=new MyAdapter_banner(list0);
        myVP.setAdapter(myAdapter_banner);
        play = true;
        play();
    }

    private void initViewPagerData() {
        list0 = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            int j=i;
            ImageView image = new ImageView(getActivity());
            switch (j){
                case 0:
                    image.setImageResource(R.drawable.pic0);
                    break;
                case 1:
                    image.setImageResource(R.drawable.pic1);
                    break;
                case 2:
                    image.setImageResource(R.drawable.pic2);
                    break;
                case 3:
                    image.setImageResource(R.drawable.pic3);
                    break;
                case 4:
                    image.setImageResource(R.drawable.pic4);
                    break;
            }
            list0.add(image);

            TextView textview = new TextView(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(15, 15);
            if (i == 0) {
                textview.setSelected(true);
            } else {
                lp.leftMargin = 10;
            }
            textview.setLayoutParams(lp);
            textview.setBackgroundResource(R.drawable.banner_dot);
            mDotContainer.addView(textview);
        }
    }

    private void initEvent(){
        open.setOnClickListener(this);
        close.setOnClickListener(this);
        sure.setOnClickListener(this);
//        spinner1.setOnClickListener(this);

        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {

                String m=DownloadInformation();

                Message msg1 = myHandler.obtainMessage();
                msg1.what=2;
                //Bundle是message中的数据
                Bundle bundle = new Bundle();
                bundle.putString("data", m);
                //传递数据
                msg1.setData(bundle);

                myHandler.sendMessage(msg1); // 向Handler发送消息,更新UI
                //UI还是在主线程里更改




                refreshableView.finishRefreshing();
            }
        }, 0);
    }




    //设置延时自动播放
    private void play() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                if(play){
                    handler.sendEmptyMessage(CHANGE_PAGE);
                }

            }
        });
        //延迟3秒后每4秒播放一次
        executor.scheduleAtFixedRate(thread,3,4, TimeUnit.SECONDS);
    }

    public String DownloadPictures(){
        try {

            URL url = new URL("http://47.95.224.200:8080/dish");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.setRequestProperty("Cookie",LogActivity.responseCookie);
            connection.setRequestMethod("POST");//设置请求方式为POST
            connection.setDoOutput(true);//允许写出
            connection.setConnectTimeout(3000);
            connection.setDoInput(true);//允许读入
            //connection.setUseCaches(false);//不使用缓存
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            //connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();//连接


            int code = connection.getResponseCode();
            // 如果返回值正常，数据在网络中是以流的形式得到服务端返回的数据
            if (code == 200) { // 正常响应
                // 从流中读取响应信息
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String line = null;

                while ((line = reader.readLine()) != null) { // 循环从流中读取
                    pictures = pictures + line + "\n";
                }
                reader.close(); // 关闭流
            }
            //断开连接，释放资源
            connection.disconnect();

        }catch (Exception e){
            e.printStackTrace();
        }
        //System.out.println("666"+msg);
        return  pictures;

    }

    class MyThread implements Runnable {
        public void run() {

            String m=DownloadPictures();

            Message msg1 = myHandler.obtainMessage();
            msg1.what=1;
            //Bundle是message中的数据
            Bundle bundle = new Bundle();
            bundle.putString("照片", m);
            //传递数据
            msg1.setData(bundle);

            myHandler.sendMessage(msg1); // 向Handler发送消息,更新UI
            //UI还是在主线程里更改

        }
    }

    public String DownloadInformation(){
        try {

            URL url = new URL("http://10.27.194.15:8080/info");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.setRequestProperty("Cookie",LogActivity.responseCookie);
            connection.setRequestMethod("POST");//设置请求方式为POST
            connection.setDoOutput(true);//允许写出
            connection.setConnectTimeout(3000);
            connection.setDoInput(true);//允许读入
            //connection.setUseCaches(false);//不使用缓存
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            //connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();//连接


            int code = connection.getResponseCode();
            // 如果返回值正常，数据在网络中是以流的形式得到服务端返回的数据
            if (code == 200) { // 正常响应
                // 从流中读取响应信息
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String line = null;
                information="";

                while ((line = reader.readLine()) != null) { // 循环从流中读取
                    information = information + line + "\n";
                }
                reader.close(); // 关闭流
            }
            //断开连接，释放资源
            connection.disconnect();

        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("66666666"+information);
        return  information;

    }

    class MyThread1 implements Runnable {
        public void run() {

            String m=DownloadInformation();

            Message msg1 = myHandler.obtainMessage();
            msg1.what=2;
            //Bundle是message中的数据
            Bundle bundle = new Bundle();
            bundle.putString("data", m);
            //传递数据
            msg1.setData(bundle);

            myHandler.sendMessage(msg1); // 向Handler发送消息,更新UI
            //UI还是在主线程里更改

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
        public void handleMessage(Message msg) {
            AppCompatActivity appCompatActivity=(AppCompatActivity) getActivity();
            if(msg.what==1){
                //用于广告位
                Bundle b = msg.getData();
                String data = b.getString("照片");
                if (!data.equals("")){

                    try {
                        JSONObject jsonObject=new JSONObject(data);
                        if (jsonObject.getString("message").equals("获取成功")){

                            JSONArray jsonArray =jsonObject.getJSONArray("data");
                            int length=jsonArray.length();

                            for(int i=0 ;i<length;i++) {
                                pp = new Gson().fromJson(jsonArray.getString(i), MyContent_pictures.class);

                            }
                            list0 = new ArrayList<>();
                            for (int i=0;i<4;i++){
                                ImageView image = new ImageView(appCompatActivity);
                                Glide.with(appCompatActivity)
                                        .load(pp.getPictures(i))
                                        .placeholder(R.drawable.wait)
                                        .into(image);
                                list0.add(image);

                                TextView textview = new TextView(appCompatActivity);
                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(15, 15);
                                if (i == 0) {
                                    textview.setSelected(true);
                                } else {
                                    lp.leftMargin = 10;
                                }
                                textview.setLayoutParams(lp);
                                textview.setBackgroundResource(R.drawable.banner_dot);
                                mDotContainer.addView(textview);
                            }
                            myAdapter_banner=new MyAdapter_banner(list0);
                            myVP.setAdapter(myAdapter_banner);
                            play = true;
                            play();



                        }else{

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }else if(msg.what==2){
                //用于下方信息推荐
                Bundle b = msg.getData();
                String data = b.getString("data");
                try {
                    if (!data.equals("")){
                        JSONObject jsonObject=new JSONObject(data);
                        if (jsonObject.getString("message").equals("获取成功")){


                            JSONArray jsonArray =jsonObject.getJSONArray("data");
                            int length=jsonArray.length();

                            list1 = new ArrayList<>();

                            for(int i=0 ;i<length;i++) {
                                myContent_petinfo= new Gson().fromJson(jsonArray.getString(i), MyContent_petinfo.class);
                                list1.add(myContent_petinfo);
                            }
//                            Log.e("ssssssssssssssss",list1.get(0).getPicture().get(0));
                            myAdapter_petinfo=new MyAdapter_petinfo(list1,appCompatActivity);
                            lv.setAdapter(myAdapter_petinfo);

                            if (list1.size()==0){
                                Toast.makeText(getContext(),"暂时没有符合要求的萌宠哦~，换个条件看看吧",Toast.LENGTH_SHORT).show();
                            }

                        }else {
////                            lv.setVisibility(View.GONE);
////                            error.setVisibility(View.VISIBLE);
//                            error.setText("服务器走丢了~请稍后重试0.0");
                            Toast.makeText(getContext(),"服务器走丢了~请稍后重试0.0",Toast.LENGTH_SHORT).show();
                        }
                    }else{
//                        lv.setVisibility(View.GONE);
//                        error.setVisibility(View.VISIBLE);
//                        error.setText("服务器走丢了~请稍后重试");
                        Toast.makeText(getContext(),"服务器走丢了~请稍后重试  囧",Toast.LENGTH_SHORT).show();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.open:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.close:
                drawerLayout.closeDrawer(Gravity.LEFT);
                break;
//            case R.id.spinner1:
//                if(variety2==null){
//                    Toast.makeText(getContext(),"请先选择宠物种类",Toast.LENGTH_SHORT).show();
//                }
//                break;
            case R.id.sure:
                //此处向服务器提交搜索请求
//                type1=null;
//                variety1=null;
//                sex1=null;

                age1=0;
                province1=null;
                city1=null;
                if(type1.equals("")&&variety1.equals("")&&sex1.equals("")&&province.getText().toString().equals("")&&age.getText().toString().equals("")&&city.getText().toString().equals("")){
                    Toast.makeText(getContext(),"请至少输入一项筛选条件",Toast.LENGTH_SHORT).show();
                }else {
                    Log.e("type1",type1);
                    Log.e("variety1",variety1);
                    Log.e("sex1",sex1);

                    if (!age.getText().toString().equals("")){
                        age1= Integer.parseInt(age.getText().toString());
                    }
                    if (!province.getText().toString().equals("")){
                        province1=province.getText().toString();
                    }
                    if (!city.getText().toString().equals("")){
                        city1=city.getText().toString();
                    }

                    MyThread2 m=new MyThread2();
                    new Thread(m).start();

                    type1="";
                    variety1="";
                    sex1="";
                    drawerLayout.closeDrawer(Gravity.LEFT);


                }
                break;
        }
    }

    public String DownloadSearchResult(String type,String variety,String sex,String province,String city,int age){
        try {

            String body= "{\"type\":\""+type+"\",\"variety\":\""+variety+"\",\"sex\":\""+sex+"\",\"province\":\""+province+"\",\"city\":\""+city+"\",\"age\":\""+age+"\"}";
            String b = URLEncoder.encode(body,"utf-8");
            URL url = new URL("http://10.27.194.15:8080/searchPet?json="+b);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.setRequestProperty("Cookie",LogActivity.responseCookie);
            connection.setRequestMethod("POST");//设置请求方式为POST
            connection.setDoOutput(true);//允许写出
            connection.setConnectTimeout(3000);
            connection.setDoInput(true);//允许读入
            //connection.setUseCaches(false);//不使用缓存
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            //connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();//连接

//
//            String body1= "{\"type\":\""+type+"\",\"variety\":\""+variety+"\",\"sex\":\""+sex+"\",\"province\":\""+province+"\",\"city\":\""+city+"\",\"age\":\""+age+"\"}";
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
//            writer.write(body1);
//            writer.close();


            int code = connection.getResponseCode();
            // 如果返回值正常，数据在网络中是以流的形式得到服务端返回的数据
            if (code == 200) { // 正常响应
                // 从流中读取响应信息
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String line = null;
                searchresult="";

                while ((line = reader.readLine()) != null) { // 循环从流中读取
                    searchresult = searchresult + line ;
                }
                reader.close(); // 关闭流
            }
            //断开连接，释放资源
            connection.disconnect();

        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("66666666666666666666"+searchresult);
        Log.e("666666666666",searchresult);
        return  searchresult;

    }

    class MyThread2 implements Runnable {
        public void run() {

            String m=DownloadSearchResult(type1,variety1,sex1,province1,city1,age1);

            Message msg1 = myHandler.obtainMessage();
            msg1.what=2;
            //Bundle是message中的数据
            Bundle bundle = new Bundle();
            bundle.putString("data", m);
            //传递数据
            msg1.setData(bundle);

            myHandler.sendMessage(msg1); // 向Handler发送消息,更新UI
            //UI还是在主线程里更改

        }
    }





    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case CHANGE_PAGE:
                    int position = (mPreviousPosition + 1) % list0.size();
                    Log.e(TAG, "handleMessage: " + position);
                    myVP.setCurrentItem(position);
                    break;
            }
        }
    };

    private void initListener() {

        myVP.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if(mPreviousPosition != position){
                    mDotContainer.getChildAt(mPreviousPosition).setSelected(false);
                    mDotContainer.getChildAt(position).setSelected(true);
                    mPreviousPosition = position;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        myVP.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        play = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        play = true;
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executor.shutdown();
        mlocationClient.startLocation();
        mlocationClient.onDestroy();
    }







}
