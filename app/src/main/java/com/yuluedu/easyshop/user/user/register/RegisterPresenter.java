package com.yuluedu.easyshop.user.user.register;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
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

public class RegisterPresenter extends MvpNullObjectBasePresenter<RegisterView>{
    //业务，执行网络请求，完成注册
    //在特定的地方，触发对应UI操作

    // TODO: 2017/4/19 0019 环信相关
    private Call call;
    //视图销毁，取消网络请求
    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);

        if (call != null)call.cancel();
    }


    public void register(String username, String password){
        //显示进度条
        getView().showPrb();

        call = EasyShopClient.getInstance().register(username, password);
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                //隐藏进度条
                getView().hidePrb();
                //显示异常信息
                getView().showMsg(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                //隐藏进度条
                getView().hidePrb();
                //拿到返回结果
                UserResult result = new Gson().fromJson(body,UserResult.class);
                //根据不同的结果码处理
                if (result.getCode() == 1){
                    //成功提示
                    getView().showMsg("注册成功");
                    //用户信息保存到本地配置当中
                    User user = result.getData();
                    CachePreferences.setUser(user);
                    //执行注册成功的方法
                    getView().registerSuccess();
                }else if (result.getCode() == 2){
                    //提示失败信息
                    getView().showMsg(result.getMessage());
                    //执行注册失败的方法
                    getView().registerFailed();
                }else{
                    getView().showMsg("未知错误！");
                }
            }
        });
    }

}
