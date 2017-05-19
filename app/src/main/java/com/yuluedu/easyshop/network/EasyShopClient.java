package com.yuluedu.easyshop.network;

import com.google.gson.Gson;
import com.yuluedu.easyshop.model.CachePreferences;
import com.yuluedu.easyshop.model.GoodsUpLoad;
import com.yuluedu.easyshop.model.User;

import java.io.File;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by gameben on 2017-04-17.
 */

public class EasyShopClient {
    private static EasyShopClient easyShopClient;

    private OkHttpClient okHttpClient;
    private Gson gson;
    public EasyShopClient() {
        //添加日志拦截器
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        //设置拦截级别
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        okHttpClient = new OkHttpClient.Builder()
                //添加日志拦截器
                        .addInterceptor(httpLoggingInterceptor)
                        .build();

        gson = new Gson();
    }
    public static EasyShopClient getInstance(){
        if (easyShopClient == null){
            easyShopClient = new EasyShopClient();
        }
        return easyShopClient;
    }



    //登陆
    public Call login(String username, String password){
        RequestBody requestBody = new FormBody.Builder()
                .add("username",username)
                .add("password",password)
                .build();

        //2.
        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL+EasyShopApi.LOGIN)
                .post(requestBody)
                .build();

        return okHttpClient.newCall(request);
    }

    //注册
    public Call register(String username, String password){
        RequestBody requestBody = new FormBody.Builder()
                .add("username",username)
                .add("password",password)
                .build();

        //2.
        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL+EasyShopApi.REGISTER)
                .post(requestBody)
                .build();

        return okHttpClient.newCall(request);
    }

    //修改头像
    public Call uploadAvatar(File file){
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //传一个用户类JSON字符串格式
                .addFormDataPart("user",gson.toJson(CachePreferences.getUser()))
                .addFormDataPart("image",file.getName(),
                        RequestBody.create(MediaType.parse("image/png"),file))
                .build();

        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.UPDATA)
                .post(requestBody)
                .build();

        return okHttpClient.newCall(request);
    }
    //修改昵称
    public Call uploadUser(User user){
        //构架请求体（多部分形式）
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //传一个用户实体类，转换为json字符串（Gson）
                .addFormDataPart("user",gson.toJson(user))
                .build();

        //构建请求
        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.UPDATA)
                .post(requestBody)
                .build();

        return okHttpClient.newCall(request);
    }
    //获取所有商品
    public Call getGoods(int pageNo,String type){
        RequestBody requestBody = new FormBody.Builder()
                .add("pageNo",String.valueOf(pageNo))
                .add("type",type)
                .build();

        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.GETGOODS)
                .post(requestBody)
                .build();

        return okHttpClient.newCall(request);
    }

    //获取商品详情
    public Call getGoodsData(String goodsUuid){
        RequestBody requestBody = new FormBody.Builder()
                .add("uuid",goodsUuid)
                .build();

        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.DETAIL)
                .post(requestBody)
                .build();

        return okHttpClient.newCall(request);
    }
    //获取个人商品
    public Call getPersonData(int pageNo,String type,String master){
        RequestBody requestBody = new FormBody.Builder()
                .add("pageNo",String.valueOf(pageNo))
                .add("master",master)
                .add("type",type)
                .build();

        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL + EasyShopApi.GETGOODS)
                .post(requestBody)
                .build();

        return okHttpClient.newCall(request);

    }
    //删除商品
    public Call deleteGoods(String uuid){

        RequestBody requestBody = new FormBody.Builder()
                .add("uuid",uuid)
                .build();


        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL+EasyShopApi.DELETE)
                .post(requestBody)
                .build();
        return okHttpClient.newCall(request);
    }
    //上传商品
    public Call upLoad(GoodsUpLoad goodsUpLoad, ArrayList<File> files){
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("good", gson.toJson(goodsUpLoad));

        //将所有图片文件添加进来
        for (File file : files) {
            builder.addFormDataPart("image", file.getName(),
                    RequestBody.create(MediaType.parse("image/png"), file));
        }


        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(EasyShopApi.BASE_URL+EasyShopApi.UPLOADGOODS)
                .post(requestBody)
                .build();
        return okHttpClient.newCall(request);
    }
}
