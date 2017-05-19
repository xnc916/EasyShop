package com.yuluedu.easyshop.network;


import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by gameben on 2017-04-18.
 */

public abstract class UICallBack implements Callback {

    //拿到主线成的handler
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onFailure(final Call call, final IOException e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onFailureUI(call, e);
            }
        });
    }

    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        //判断是否响应成功
        if (!response.isSuccessful()){
            throw new IOException("error code:" + response.code());
        }
        //拿到响应体
        final String json = response.body().string();

        handler.post(new Runnable() {
            @Override
            public void run() {
                onResponseUI(call,json);
            }
        });
    }

    public abstract void onFailureUI(Call call, IOException e);
    public abstract void onResponseUI(Call call, String body);
}
