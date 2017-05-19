package com.yuluedu.easyshop.main.me.persongoods;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.yuluedu.easyshop.R;
import com.yuluedu.easyshop.commons.ActivityUtils;
import com.yuluedu.easyshop.main.shop.ShopAdapter;
import com.yuluedu.easyshop.main.shop.ShopView;
import com.yuluedu.easyshop.main.shop.details.GoodsDetailActivity;
import com.yuluedu.easyshop.model.GoodsInfo;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class PersonGoodsActivity extends MvpActivity<ShopView, PersonGoodsPresenter> implements ShopView {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    /*下拉刷新和上拉加载的控件*/
    @BindView(R.id.refreshLayout)
    PtrClassicFrameLayout refreshLayout;
    @BindView(R.id.tv_load_error)
    TextView tvLoadError;
    @BindView(R.id.tv_load_empty)
    TextView tvLoadEmpty;

    @BindString(R.string.load_more_end)
    String lodad_more_end;


    private ActivityUtils activityUtils;
    private String pageType = "";//商品类型，空值为全部商品
    private ShopAdapter shopAdapter;//数据展示与市场页面相同，直接复用市场页面的适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_goods);
        ButterKnife.bind(this);
        activityUtils = new ActivityUtils(this);


        shopAdapter = new ShopAdapter();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //设置toobar的监听事件
        
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
        initView();
    }

    private void initView() {
        //初始化RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        shopAdapter.setListener(new ShopAdapter.onItemClickListener() {
            @Override
            public void onItemClicked(GoodsInfo goodsInfo) {
                Intent intent = GoodsDetailActivity.getStateIntent(PersonGoodsActivity.this,
                        goodsInfo.getUuid(),1);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(shopAdapter);
        //初始化RefreshLayout
        refreshLayout.setLastUpdateTimeRelateObject(this);
        refreshLayout.setBackgroundResource(R.color.recycler_bg);
        refreshLayout.setDurationToCloseHeader(1500);
        //刷新，加载回调
        refreshLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                presenter.loadData(pageType);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                presenter.refreshData(pageType);
            }
        });
    }
    //每次进入本页面，如果没有数据，自动刷新
    @Override
    protected void onStart() {
        super.onStart();
        if (shopAdapter.getItemCount() == 0){

            refreshLayout.autoRefresh();
        }
    }
    @NonNull
    @Override
    public PersonGoodsPresenter createPresenter() {
        return new PersonGoodsPresenter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    // ________________________    toolbar 菜单选项相关  ____________________
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //toolbar设置菜单选项
        getMenuInflater().inflate(R.menu.menu_goods_type,menu);
        return true;
    }

    //toolbar菜单对应的单击事件
    private Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.menu_household:
                    presenter.refreshData("household");
                    break;
                case R.id.menu_electron:
                    presenter.refreshData("electron");
                    break;
                case R.id.menu_dress:
                    presenter.refreshData("dress");
                    break;
                case R.id.menu_book:
                    presenter.refreshData("book");
                    break;
                case R.id.menu_toy:
                    presenter.refreshData("toy");
                    break;
                case R.id.menu_gift:
                    presenter.refreshData("gift");
                    break;
                case R.id.menu_other:
                    presenter.refreshData("other");
                    break;
            }
            return false;
        }
    };


    //________________________dd___________________
    @Override
    public void showRefresh() {
        tvLoadEmpty.setVisibility(View.GONE);
        tvLoadError.setVisibility(View.GONE);
    }

    @Override
    public void showRefreshError(String msg) {
        refreshLayout.refreshComplete();
        if (shopAdapter.getItemCount() > 0){
            activityUtils.showToast(msg);
            return;
        }
        tvLoadError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRefreshEnd() {
        refreshLayout.refreshComplete();
        tvLoadEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRefresh() {
        refreshLayout.refreshComplete();
    }

    @Override
    public void addRefreshData(List<GoodsInfo> data) {
        shopAdapter.clear();
        if (data != null) shopAdapter.addData(data);
    }

    @Override
    public void showLoadMoreLoading() {
        tvLoadError.setVisibility(View.GONE);
    }

    @Override
    public void showLoadMoreError(String msg) {
        refreshLayout.refreshComplete();
        if (shopAdapter.getItemCount() > 0){
            activityUtils.showToast(msg);
            return;
        }
        tvLoadError.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadMoreEnd() {
        activityUtils.showToast(lodad_more_end);
        refreshLayout.refreshComplete();
    }

    @Override
    public void hideLoadMore() {
        refreshLayout.refreshComplete();
    }

    @Override
    public void addMoreData(List<GoodsInfo> data) {
        shopAdapter.addData(data);
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }
}
