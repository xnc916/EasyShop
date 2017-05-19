package com.yuluedu.easyshop.main.me.personInfo;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by gameben on 2017-04-21.
 */

public interface PersonView extends MvpView{

    void showPrb();

    void hidePrb();

    void showMsg(String msg);
    //用来更新头像
    void updataAvatar(String url);
}
