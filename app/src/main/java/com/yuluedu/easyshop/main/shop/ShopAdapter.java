package com.yuluedu.easyshop.main.shop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yuluedu.easyshop.R;
import com.yuluedu.easyshop.components.AvatarLoadOptions;
import com.yuluedu.easyshop.model.GoodsInfo;
import com.yuluedu.easyshop.network.EasyShopApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gameben on 2017-04-23.
 */

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder>{


    //所需数据
    private List<GoodsInfo> list = new ArrayList<>();
    private Context context;

    //添加数据
    public void addData(List<GoodsInfo> data) {
        list.addAll(data);
        //通知更新
        notifyDataSetChanged();
    }

    //清空数据
    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }


    @Override
    public ShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler, parent, false);

        ShopViewHolder shopViewHolder = new ShopViewHolder(view);
        return shopViewHolder;
    }

    @Override
    public void onBindViewHolder(ShopViewHolder holder, final int position) {
        //商品名称
        holder.tv_name.setText(list.get(position).getName());

        //商品价格,替换字符串
        String price = context.getString(R.string.goods_money, list.get(position).getPrice());
        holder.tv_price.setText(price);

        //商品图片
        ImageLoader.getInstance().displayImage(
                EasyShopApi.IMAGE_URL + list.get(position).getPage(),
                holder.imageView, AvatarLoadOptions.build_item());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClicked(list.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ShopViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_item_recycler)
        ImageView imageView;//商品图片
        @BindView(R.id.tv_item_name)
        TextView tv_name;//商品名
        @BindView(R.id.tv_item_price)
        TextView tv_price;//商品价格

        public ShopViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }

    public interface onItemClickListener {

        //点击商品item，跳转到详情页
        void onItemClicked(GoodsInfo goodsInfo);
    }

    private onItemClickListener listener;

    public void setListener(onItemClickListener listener) {
        this.listener = listener;
    }
}
