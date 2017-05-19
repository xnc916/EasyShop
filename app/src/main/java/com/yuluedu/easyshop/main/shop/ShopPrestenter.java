package com.yuluedu.easyshop.main.shop;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.yuluedu.easyshop.model.GoodsResult;
import com.yuluedu.easyshop.network.EasyShopClient;
import com.yuluedu.easyshop.network.UICallBack;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by gameben on 2017-04-23.
 */

public class ShopPrestenter extends MvpNullObjectBasePresenter<ShopView>{

    private Call call;

    private int pageInt = 1;
    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null)call.cancel();
    }

    //刷新数据

    public void refreshData(String type){
        getView().showRefresh();

        call = EasyShopClient.getInstance().getGoods(pageInt,type);
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().showRefreshError(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                GoodsResult goodsResult = new Gson().fromJson(body,GoodsResult.class);
                switch (goodsResult.getCode()){
                    //成功
                    case 1:
                        //还没有商品（服务器没有商品数据）
                        if (goodsResult.getDatas().size() == 0){
                            getView().showRefreshEnd();
                        }else{
                            getView().addRefreshData(goodsResult.getDatas());
                            getView().showRefreshEnd();
                        }
                        //分页改为2，之后要加载更多数据了
                        pageInt = 2;
                        break;
                    default:
                        //失败或其他
                        getView().showRefreshError(goodsResult.getMessage());
                }
            }
        });


    }
    //加载更多

    public void loadData(String type){
        getView().showRefresh();
        call = EasyShopClient.getInstance().getGoods(pageInt,type);
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().showRefreshError(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                GoodsResult goodsResult = new Gson().fromJson(body,GoodsResult.class);
                switch (goodsResult.getCode()){
                    case 1:
                        if (goodsResult.getDatas().size() == 0){
                            getView().showLoadMoreEnd();
                        }else{
                            getView().addMoreData(goodsResult.getDatas());
                            getView().showLoadMoreEnd();
                        }
                        //分页加载，之后要加载新一页的数据
                        pageInt ++;
                        break;
                    default:
                        getView().showLoadMoreError(goodsResult.getMessage());
                }
            }
        });
    }


}
