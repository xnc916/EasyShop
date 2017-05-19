package com.yuluedu.easyshop.user.user.login;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by gameben on 2017-04-20.
 */

public interface LoginView extends MvpView{
    //显示进度条
    void showPrb();
    //隐藏进度条
    void hidePrb();
//登录失败
    void loginFailed();
//登陆成功
    void loginSuccess();
    //提示信息
    void showMsg(String msg);
}
