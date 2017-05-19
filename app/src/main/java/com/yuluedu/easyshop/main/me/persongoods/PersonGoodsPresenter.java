package com.yuluedu.easyshop.main.me.persongoods;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.yuluedu.easyshop.main.shop.ShopView;
import com.yuluedu.easyshop.model.CachePreferences;
import com.yuluedu.easyshop.model.GoodsResult;
import com.yuluedu.easyshop.network.EasyShopClient;
import com.yuluedu.easyshop.network.UICallBack;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by gameben on 2017-04-24.
 */

public class PersonGoodsPresenter extends MvpNullObjectBasePresenter<ShopView>{

    private Call call;
    private int pageInt = 1;
    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null) call.cancel();
    }

    //刷新数据
    public void refreshData(final String type) {
        call = EasyShopClient.getInstance().getPersonData(1,type, CachePreferences.getUser().getName());
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().showRefreshError(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                GoodsResult goodsResult = new Gson().fromJson(body,GoodsResult.class);
                switch (goodsResult.getCode()) {
                    case 1:
                        if (goodsResult.getDatas().size() == 0) {
                            getView().showRefreshEnd();
                        } else {
                            getView().addRefreshData(goodsResult.getDatas());
                            getView().hideRefresh();
                        }
                        pageInt = 2;
                        break;
                    default:
                        getView().showRefreshError(goodsResult.getMessage());
                }
            }
        });
    }
    //加载数据
    public void loadData(String type) {
        getView().showLoadMoreLoading();
        call = EasyShopClient.getInstance().getPersonData(pageInt, type, CachePreferences.getUser().getName());
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().showLoadMoreError(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                GoodsResult goodsResult = new Gson().fromJson(body, GoodsResult.class);
                switch (goodsResult.getCode()) {
                    case 1:
                        if (goodsResult.getDatas().size() == 0) {
                            getView().showLoadMoreEnd();
                        } else {
                            getView().addMoreData(goodsResult.getDatas());
                            getView().hideLoadMore();
                        }
                        pageInt++;
                        break;
                    default:
                        getView().showLoadMoreError(goodsResult.getMessage());
                }
            }
        });
    }
}
