package com.example.administrator.pet_go.Util;

import android.content.Context;
import android.text.format.DateFormat;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import java.io.File;
import java.util.Date;

/**
 * Created by Administrator on 2018/11/23/023.
 */





public class PictureUploadUtils {
    private static String endpoint = "https://oss-cn-beijing.aliyuncs.com";
    private static String accessKeyId = "LTAIBv2h5yZST7gn";
    private static String accessKeySecret = "FVubqHhZsMAD4c4UEHClnc8WEzE3m3";
    private static String bucketName = "veng-photo";
    private static OSSClient ossClient;



    private Context context;
    public PictureUploadUtils(Context context){
        this.context = context;
    }
    private OSS getOSSClient(){
        OSSCredentialProvider provider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
//        return new OSSClient(PetGo)

        return new OSSClient(context,endpoint,provider);
    }


    public String uploadPortrait(String path){
        String key = getObjectPortraitKey(path);
        return upload(key,path);
    }

    private String upload(String key,String path) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, path);
        try{
            OSS ossClient = getOSSClient();
            PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);
            String url   = ossClient.presignPublicObjectURL(bucketName, key);
            return url;
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getObjectPortraitKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("portrait/%s/%s.jpg", dateString, fileMd5);
    }


    private String getDateString() {
        return DateFormat.format("yyyyMM", new Date()).toString();
    }
}
