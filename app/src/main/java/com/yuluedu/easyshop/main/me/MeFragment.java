package com.yuluedu.easyshop.main.me;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yuluedu.easyshop.R;
import com.yuluedu.easyshop.commons.ActivityUtils;
import com.yuluedu.easyshop.components.AvatarLoadOptions;
import com.yuluedu.easyshop.main.me.personInfo.PersonActivity;
import com.yuluedu.easyshop.main.me.persongoods.PersonGoodsActivity;
import com.yuluedu.easyshop.model.CachePreferences;
import com.yuluedu.easyshop.network.EasyShopApi;
import com.yuluedu.easyshop.user.user.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gameben on 2017-04-16.
 */

public class MeFragment extends Fragment {
    @BindView(R.id.iv_user_head)
    ImageView iv_user_head;//用户头像
    @BindView(R.id.tv_login)
    TextView tv_login;//用户名
    private ActivityUtils activityUtils;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_me,container,false);
            activityUtils = new ActivityUtils(this);
            ButterKnife.bind(this,view);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //判断是否登录，显示昵称和头像
        if (CachePreferences.getUser().getName() == null) return;//未登录，跳出
        //刚注册还没有昵称和头像
        if (CachePreferences.getUser().getNick_name() == null) {
            tv_login.setText("请输入昵称");
        }else{
            tv_login.setText(CachePreferences.getUser().getNick_name());
        }
        ImageLoader.getInstance()
                .displayImage(EasyShopApi.IMAGE_URL + CachePreferences.getUser().getHead_Image()
                        ,iv_user_head, AvatarLoadOptions.build());
    }

    @OnClick({R.id.iv_user_head,R.id.tv_person_info, R.id.tv_login, R.id.tv_person_goods, R.id.tv_goods_upload})
    public void onClick(View view){
        //需要判断用户是否登录，从而决定跳转的位置
        if (CachePreferences.getUser().getName() == null){
            activityUtils.startActivity(LoginActivity.class);
            return;
        }

        switch (view.getId()){
            case R.id.iv_user_head:
            case R.id.tv_login:
            case R.id.tv_person_info:
                //跳转到个人信息页面
                activityUtils.startActivity(PersonActivity.class);
                break;
            case R.id.tv_person_goods:
                //跳转到我的商品页面
                //activityUtils.showToast("跳转到我的商品页面,待实现");
                activityUtils.startActivity(PersonGoodsActivity.class);
                break;
            case R.id.tv_goods_upload:
                //跳转到商品上传的页面
                activityUtils.showToast("跳转到商品上传的页面，待实现");
                break;

        }



    }
}
