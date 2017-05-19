package com.yuluedu.easyshop.main.shop;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.yuluedu.easyshop.model.GoodsInfo;

import java.util.List;

/**
 * Created by gameben on 2017-04-23.
 */

public interface ShopView extends MvpView {

    //数据刷新--处理中
    void showRefresh();

    //数据刷新--刷新出错
    void showRefreshError(String msg);

    //数据刷新--刷新结束
    void showRefreshEnd();

    //数据刷新--隐藏下拉视图
    void hideRefresh();

    //数据刷新--添加刷新到数据
    void addRefreshData(List<GoodsInfo> data);


    //加载更多--加载中
    void showLoadMoreLoading();

    //加载更多--加载错误
    void showLoadMoreError(String msg);

    //加载更多--没有更多数据
    void showLoadMoreEnd();

    //加载更多--隐藏加载更多视图
    void hideLoadMore();

    //加载更多--添加加载到的数据
    void addMoreData(List<GoodsInfo> data);

    //消息提示
    void showMessage(String msg);

}
