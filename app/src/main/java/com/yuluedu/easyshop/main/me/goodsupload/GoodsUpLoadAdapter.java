package com.yuluedu.easyshop.main.me.goodsupload;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.yuluedu.easyshop.R;
import com.yuluedu.easyshop.model.ImageItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gameben on 2017-04-25.
 */

public class GoodsUpLoadAdapter extends RecyclerView.Adapter{

    //适配器数据
    private ArrayList<ImageItem> list = new ArrayList<>();
    private LayoutInflater inflater;

    public GoodsUpLoadAdapter(ArrayList<ImageItem> list, Context context) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

//++++++++++++++++逻辑：模式的选择 start +++++++++
    //编辑时的模式，1 = 有图，2 = 无图（显示加号图片的布局）
    public static final int MODE_NORMAL = 1;
    public static final int MODE_MULTI_SELECT = 2;

    //代表图片的编辑模式
    public int mode;

    //用枚举，便是item类型，有图或无图
    public enum ITEM_TYPE{
        ITEM_NORMAL,ITEM_ADD
    }


    //模式设置
    public void changeMode(int mode){
        this.mode = mode;
        notifyDataSetChanged();
    }

    //获取当前模式
    public int getMode(){
        return mode;
    }


//------------------逻辑：模式的选择 end ---------

//+++++++++++++++++ 外部调用的相关方法  start+++++++++

    //添加图片（imageitem）
    public void add(ImageItem imageItem){
        list.add(imageItem);
    }

    public int getSize(){
        return list.size();
    }

    //获取数据
    public ArrayList<ImageItem> getList(){
        return list;
    }

    //刷新数据
    public void notifyData(){
        notifyDataSetChanged();
    }

//---------------- 外部调用的相关方法  end ------------

    //确定ViewType的值
    @Override
    public int getItemViewType(int position) {
        //当position与图片数量相同时，则为加号布局
        if (position == list.size()) return ITEM_TYPE.ITEM_ADD.ordinal();
        return ITEM_TYPE.ITEM_NORMAL.ordinal();
    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //判断当前显示item的类型，有图或者无图，从而选择不同的ViewHoler（不同的布局）
        if (viewType == ITEM_TYPE.ITEM_NORMAL.ordinal()){
            //有图的ViewHolder
            return new ItemSelectViewHolder(
                    inflater.inflate(R.layout.layout_item_recyclerview,parent,false));
        }else{
            //无图，显示加号的ViewHolder
            return new ItemAddViewHolder(
                    inflater.inflate(R.layout.layout_item_recyclerviewlast,parent,false)
            );
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //判断当前的vh是不是ItemSelectViewHolder的实例
        if (holder instanceof ItemSelectViewHolder) {
            //当前数据
            ImageItem imageItem = list.get(position);
            //拿到当前vh(因为已经判断是vh的实例，所以强转)
            final ItemSelectViewHolder item_select = (ItemSelectViewHolder) holder;
            item_select.photo = imageItem;
            //判断模式（正常，可删除）
            if (mode == MODE_MULTI_SELECT) {
                //可选框可见
                item_select.checkBox.setVisibility(View.VISIBLE);
                //可选框的选择监听
                item_select.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //imageitem中选择状态改变
                        list.get(position).setIsCheck(isChecked);
                    }
                });
                //可选框的改变（根据imageitem的选择状态）
                item_select.checkBox.setChecked(imageItem.isCheck());
            } else if (mode == MODE_NORMAL) {
                //可选框隐藏
                item_select.checkBox.setVisibility(View.GONE);
            }
            //图片设置
            item_select.ivPhoto.setImageBitmap(imageItem.getBitmap());
            //单击图片跳转到图片展示页
            item_select.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到图片详情页
                    if (listener != null) {
                        listener.onPhotoClicked(item_select.photo, item_select.ivPhoto);
                    }
                }
            });

            // 长摁图片改为可删除模式
            item_select.ivPhoto.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //模式改为删除模式
                    mode = MODE_MULTI_SELECT;
                    //更新
                    notifyDataSetChanged();
                    //执行长按的监听事件
                    if (listener != null) {
                        listener.onLongClicked();
                    }
                    return false;
                }
            });
        }

        //判断当前的vh是不是ItemAddViewHolder的实例
        else if (holder instanceof ItemAddViewHolder) {
            ItemAddViewHolder item_add = (ItemAddViewHolder) holder;
            //最多八张图
            if (position == 8) {
                item_add.ib_add.setVisibility(View.GONE);
            } else {
                item_add.ib_add.setVisibility(View.VISIBLE);
            }
            //点击添加图片
            item_add.ib_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //添加图片的监听
                    if (listener != null) {
                        listener.onAddClicked();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        //最多八张图
        return Math.min(list.size() + 1,8);
    }

    //显示添加按钮的ViewHolder
    public static class ItemAddViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ib_recycle_add)ImageButton ib_add;

        public ItemAddViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    //已经有图片的ViewHolder
    public static class ItemSelectViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_photo)
        ImageView ivPhoto;
        @BindView(R.id.cb_check_photo)
        CheckBox checkBox;
        ImageItem photo;//用来控制checkbox的选择属性

        public ItemSelectViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    // #######################  item点击事件（接口回调） ##########################
    public interface OnItemClickedListener {

        //无图，点击添加图片
        void onAddClicked();

        //有图，点击跳转到图片展示页
        void onPhotoClicked(ImageItem photo, ImageView imageView);

        //有图，长按执行删除相关操作
        void onLongClicked();
    }

    private OnItemClickedListener listener;

    public void setListener(OnItemClickedListener listener) {
        this.listener = listener;
    }
}
