package com.yuluedu.easyshop.main.shop.details;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yuluedu.easyshop.R;
import com.yuluedu.easyshop.commons.ActivityUtils;
import com.yuluedu.easyshop.components.AvatarLoadOptions;
import com.yuluedu.easyshop.components.ProgressDialogFragment;
import com.yuluedu.easyshop.model.CachePreferences;
import com.yuluedu.easyshop.model.GoodsDetail;
import com.yuluedu.easyshop.model.User;
import com.yuluedu.easyshop.network.EasyShopApi;
import com.yuluedu.easyshop.user.user.login.LoginActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

public class GoodsDetailActivity extends MvpActivity<GoodsDetailView,GoodsDetailPresenter> implements GoodsDetailView {


    private static final String UUID = "uuid";
    //从不同的页面进入详情页的状态值，0 = 从市场页面，1 = 从我的页面进来的
    private static final String STATE = "state";

    public static Intent getStateIntent(Context context, String uuid, int state) {
        Intent intent = new Intent(context,GoodsDetailActivity.class);
        intent.putExtra(UUID,uuid);
        intent.putExtra(STATE,state);
        return intent;
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    /*使用开源库CircleIndicator来实现ViewPager的圆点指示器。*/
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    @BindView(R.id.tv_detail_name)
    TextView tv_detail_name;
    @BindView(R.id.tv_detail_price)
    TextView tv_detail_price;
    @BindView(R.id.tv_detail_master)
    TextView tv_detail_master;
    @BindView(R.id.tv_detail_describe)
    TextView tv_detail_describe;
    @BindView(R.id.tv_goods_delete)
    TextView tv_goods_delete;
    @BindView(R.id.tv_goods_error)
    TextView tv_goods_error;
    @BindView(R.id.btn_detail_message)
    Button btn_detail_message;

    private String str_uuid;//商品的uuid
    private ArrayList<ImageView> list;
    private ArrayList<String> list_uri;//存放图片路径的集合
    //viewpager的适配器
    private GoodsDetailAdapter adapter;
    private ActivityUtils activityUtils;
    private User goods_user;
    private ProgressDialogFragment progressDialogFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activityUtils = new ActivityUtils(this);

        list = new ArrayList<>();
        list_uri = new ArrayList<>();
        adapter = new GoodsDetailAdapter(list);
        adapter.setListener(new GoodsDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {
                //activityUtils.showToast("点击图片，跳转到图片详情页");
                Intent intent = GoodsDetailInfoActivity.getIntent(getApplicationContext(),list_uri);
                startActivity(intent);
            }
        });
        viewPager.setAdapter(adapter);

        initView();
    }

    private void initView() {
        //拿到uuid
        str_uuid = getIntent().getStringExtra(UUID);
        //来自哪个页面
        int btn_show = getIntent().getIntExtra(STATE,0);
        //如果=1，来自我的页面
        if (btn_show == 1){
            tv_goods_delete.setVisibility(View.VISIBLE);//显示“删除”
            btn_detail_message.setVisibility(View.GONE);//隐藏“发消息"
        }
        presenter.getData(str_uuid);//获取商品详情，业务
    }

    //点击，发消息，删除
    @OnClick({R.id.btn_detail_message,R.id.tv_goods_delete})
    public void onClick(View view){
        //判断登录状态
        if (CachePreferences.getUser().getName() == null){
            activityUtils.startActivity(LoginActivity.class);
            return;
        }

        switch (view.getId()){
            //发消息
            case R.id.btn_detail_message:
                activityUtils.showToast("跳转到环信发消息的页面,待实现");
                break;
            //删除
            case R.id.tv_goods_delete:
                //activityUtils.showToast("执行删除操作,待实现");
                //执行删除操作
                //弹出一个警告，询问用户是否要删除
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.goods_title_delete);
                builder.setMessage(R.string.goods_info_delete);

                builder.setPositiveButton(R.string.goods_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //执行删除方法
                        presenter.delete(str_uuid);
                    }
                });

                //设置取消按钮
                builder.setNegativeButton(R.string.popu_cancle,null);
                builder.create().show();
                break;
        }
    }

    //左上角返回，需实现的方法
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public GoodsDetailPresenter createPresenter() {
        return new GoodsDetailPresenter();
    }







    //_____________________
    @Override
    public void showProgress() {
        if (progressDialogFragment == null) progressDialogFragment = new ProgressDialogFragment();
        if (progressDialogFragment.isVisible()) return;
        progressDialogFragment.show(getSupportFragmentManager(),"fragment_progress_dialog");
    }

    @Override
    public void hideProgress() {
        progressDialogFragment.dismiss();
    }

    @Override
    public void setImageData(ArrayList<String> arrayList) {
        list_uri = arrayList;
        //加载图片
        for (int i = 0; i < list_uri.size(); i++) {
            ImageView view = new ImageView(this);
            ImageLoader.getInstance().displayImage(
                    EasyShopApi.IMAGE_URL + list_uri.get(i),
                    view, AvatarLoadOptions.build_item());
            //添加到图片控件集合中
            list.add(view);
        }
        adapter.notifyDataSetChanged();
        //确认Viewpage显示页面数量之后，创建圆点指示器
        indicator.setViewPager(viewPager);
    }

    @Override
    public void setData(GoodsDetail data, User goods_user) {
        //数据展示
        this.goods_user = goods_user;
        tv_detail_name.setText(data.getName());
        tv_detail_price.setText(getString(R.string.goods_money, data.getPrice()));
        tv_detail_master.setText(getString(R.string.goods_detail_master, goods_user.getNick_name()));
        tv_detail_describe.setText(data.getDescription());
    }

    @Override
    public void showError() {
        tv_goods_error.setVisibility(View.VISIBLE);
        toolbar.setTitle("商品过期不存在");
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    @Override
    public void deleteEnd() {
        finish();
    }
}
