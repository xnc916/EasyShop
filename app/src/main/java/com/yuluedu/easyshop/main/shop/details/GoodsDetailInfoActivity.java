package com.yuluedu.easyshop.main.shop.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yuluedu.easyshop.R;
import com.yuluedu.easyshop.components.AvatarLoadOptions;
import com.yuluedu.easyshop.network.EasyShopApi;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

public class GoodsDetailInfoActivity extends AppCompatActivity {


    //需要拿到图片的地址
    private static final String IMAGES = "images";

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.indicator)
    CircleIndicator indicator;

    public static Intent getIntent(Context context, ArrayList<String> imgUrls) {
        Intent intent = new Intent(context, GoodsDetailInfoActivity.class);
        intent.putExtra(IMAGES, imgUrls);
        return intent;
    }


    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail_info);

        list = new ArrayList<>();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        ButterKnife.bind(this);

        list = getIntent().getStringArrayListExtra(IMAGES);

        //复用商品详情页的adapter
        GoodsDetailAdapter adapter = new GoodsDetailAdapter(getImage(list));
        viewpager.setAdapter(adapter);
        adapter.setListener(new GoodsDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {
                finish();
            }
        });
        indicator.setViewPager(viewpager);
    }

    private ArrayList<ImageView> getImage(ArrayList<String> list) {
        ArrayList<ImageView> list_image = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ImageView view = new ImageView(this);
            ImageLoader.getInstance()
                    .displayImage(EasyShopApi.IMAGE_URL + list.get(i),
                            view, AvatarLoadOptions.build_item());
            list_image.add(view);
        }
        return list_image;
    }
}
