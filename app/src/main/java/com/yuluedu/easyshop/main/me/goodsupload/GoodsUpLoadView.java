package com.yuluedu.easyshop.main.me.goodsupload;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by gameben on 2017-04-25.
 */

public interface GoodsUpLoadView extends MvpView{
    void showPrb();

    void hidePrb();

    void upLoadSuccess();

    void showMsg(String msg);
}
