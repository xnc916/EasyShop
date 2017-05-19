package com.yuluedu.easyshop.main.shop.details;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by gameben on 2017-04-24.
 */

public class GoodsDetailAdapter extends PagerAdapter{
    private ArrayList<ImageView> list;

    public GoodsDetailAdapter(ArrayList<ImageView> list){
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //实例化item
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = list.get(position);
        //实现图片点击跳转到图片展示页
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onItemClick();
                }
            }
        });
        container.addView(imageView);
        return imageView;
    }

    //销毁item
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    // #####################  viewpage的item点击事件（接口回调）
    public interface OnItemClickListener{

        void onItemClick();
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
