package com.yuluedu.easyshop.user.user.login;

import android.util.Log;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.yuluedu.easyshop.model.CachePreferences;
import com.yuluedu.easyshop.model.User;
import com.yuluedu.easyshop.model.UserResult;
import com.yuluedu.easyshop.network.EasyShopClient;
import com.yuluedu.easyshop.network.UICallBack;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by gameben on 2017-04-20.
 */

public class LoginPrestenter extends MvpBasePresenter<LoginView>{

    private Call call;

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call !=null) call.cancel();
    }

    public void login(String username, String password){
        //显示进度条
        getView().showPrb();

        call = EasyShopClient.getInstance().login(username, password);

        call.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                //隐藏进度条
                getView().hidePrb();
                getView().showMsg(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                UserResult userResult = new Gson().fromJson(body,UserResult.class);
                if (userResult.getCode() == 1){
                    //保存用户登录信息到本地配置
                    User user = userResult.getData();
                    Log.e("aaa","name = " + user.getName());
                    CachePreferences.setUser(user);
                    getView().loginSuccess();
                    getView().showMsg("登录成功");
                }else if (userResult.getCode() == 2){
                    getView().hidePrb();
                    getView().showMsg(userResult.getMessage());
                    getView().loginFailed();
                }else{
                    getView().hidePrb();
                    getView().showMsg("未知错误");
                }
            }
        });
    }
}
