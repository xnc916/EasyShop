package com.yuluedu.easyshop.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.yuluedu.easyshop.R;
import com.yuluedu.easyshop.commons.ActivityUtils;
import com.yuluedu.easyshop.main.me.MeFragment;
import com.yuluedu.easyshop.main.shop.ShopFragment;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindViews({R.id.tv_shop,R.id.tv_message,R.id.tv_mail_list,R.id.tv_me})
    TextView[] textViews;
    @BindView(R.id.main_title)
    TextView mainTitle;
    @BindView(R.id.main_toobar)
    Toolbar mainToobar;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private ActivityUtils activityUtils;


    //点击2次返回，退出程序
    private boolean isExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        activityUtils = new ActivityUtils(this);
        //初始化视图
        init();
    }

    private void init() {
        viewpager.setAdapter(unLoginAdapter);


        //刚进来默认选择市场
        textViews[0].setSelected(true);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //text view 全部为选择
                for (TextView textview : textViews){
                    textview.setSelected(false);
                }

                mainTitle.setText(textViews[position].getText());
                textViews[position].setSelected(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }
    private FragmentPagerAdapter unLoginAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            switch(position){
                //市场
                case 0:
                    return new ShopFragment();
                //消息
                case 1:
                    return new UnLoginFragment();
                //通讯录
                case 2:
                    return new UnLoginFragment();
                    //我的
                case 3:
                    return new MeFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    //textview 点击事件
    @OnClick({R.id.tv_shop,R.id.tv_message,R.id.tv_mail_list,R.id.tv_me})
    public void onClick(TextView textview){
        for (int i = 0;i < textViews.length;i++){
            textViews[i].setSelected(false);
            textViews[i].setTag(i);
        }

        //设置选择效果
        textview.setSelected(true);
        //不要平滑效果，第二个参数为false
        viewpager.setCurrentItem((int)textview.getTag(),false);
        mainTitle.setText(textViews[(int) textview.getTag()].getText());

    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (!isExit){
            isExit = true;

            activityUtils.showToast("再点一次就退出");
            // 如果两秒内再次点击就推出
            viewpager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);

        }else {
            finish();
        }

    }
}
