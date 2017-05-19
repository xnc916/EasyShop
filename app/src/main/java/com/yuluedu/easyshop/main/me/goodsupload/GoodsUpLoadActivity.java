package com.yuluedu.easyshop.main.me.goodsupload;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.yuluedu.easyshop.R;
import com.yuluedu.easyshop.commons.ActivityUtils;
import com.yuluedu.easyshop.commons.ImageUtils;
import com.yuluedu.easyshop.commons.MyFileUtils;
import com.yuluedu.easyshop.components.PicWindow;
import com.yuluedu.easyshop.components.ProgressDialogFragment;
import com.yuluedu.easyshop.model.ImageItem;

import org.hybridsquad.android.library.CropHandler;
import org.hybridsquad.android.library.CropHelper;
import org.hybridsquad.android.library.CropParams;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsUpLoadActivity extends MvpActivity<GoodsUpLoadView,GoodsUpLoadPresenter> implements GoodsUpLoadView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.et_goods_name)
    EditText et_goods_name;
    @BindView(R.id.et_goods_price)
    EditText et_goods_price;
    @BindView(R.id.et_goods_describe)
    EditText et_goods_describe;
    @BindView(R.id.tv_goods_type)
    TextView tv_goods_type;
    @BindView(R.id.tv_goods_delete)
    TextView tv_goods_delete;
    @BindView(R.id.btn_goods_load)
    Button btn_goods_load;

    private final String[] goods_type = {"家用", "电子", "服饰", "玩具", "图书", "礼品", "其它"};
    /*商品种类为自定义*/
    private final String[] goods_type_num = {"household", "electron", "dress", "toy", "book", "gift", "other"};



    private ActivityUtils activityUtils;
    private String str_goods_name;//商品名
    private String str_goods_price;//商品价格
    private String str_goods_type = goods_type_num[0];//商品类型（默认家用）
    private String str_goods_describe;//商品描述

    //模式：普通 =1
    public static final int MODE_DONE = 1;
    //模式：删除 =2
    public static final int MODE_DELETE = 2;
    private int title_mode = MODE_DONE;
    private ArrayList<ImageItem> list = new ArrayList<>();
    private GoodsUpLoadAdapter adapter;
    private PicWindow picWindow;
    private ProgressDialogFragment dialogFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_up_load);
        ButterKnife.bind(this);

        activityUtils = new ActivityUtils(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
    }

    //toolbar返回要实现的方法
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public GoodsUpLoadPresenter createPresenter() {
        return new GoodsUpLoadPresenter();
    }

    private void initView() {

        picWindow = new PicWindow(this, listener);

        //RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        //设置默认动画（item增删动画）
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置固定大小
        recyclerView.setHasFixedSize(true);

        //获取缓存文件夹中的文件
        list = getFilePhoto();

    }


    private PicWindow.Listener listener = new PicWindow.Listener() {
        @Override
        public void toGallery() {
            //相册
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
            Intent intent = CropHelper.buildCropFromGalleryIntent(cropHandler.getCropParams());
            startActivityForResult(intent,CropHelper.REQUEST_CROP);

        }

        @Override
        public void toCamera() {
            //相机
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
            Intent intent = CropHelper.buildCaptureIntent(cropHandler.getCropParams().uri);
            startActivityForResult(intent,CropHelper.REQUEST_CAMERA);


        }
    };

    //图片裁剪的Handler
    private CropHandler cropHandler = new CropHandler() {
        @Override
        public void onPhotoCropped(Uri uri) {
            //需求：裁剪完成后，吧图片保存为bitmap，并且保存到sd中，并且展示出来
            //文件名：就是用系统当前时间，不重复
            String fileName = String.valueOf(System.currentTimeMillis());

            //拿到BitMap（ImageUtils）
            Bitmap bitmap = ImageUtils.readDownsampledImage(uri.getPath(),1080,1920);

            //保存到sd中
            MyFileUtils.saveBitmap(bitmap,fileName);

            //展示出来
            ImageItem imageitem = new ImageItem();
            imageitem.setImagePath(fileName + ".JPEG");
            imageitem.setBitmap(bitmap);
            adapter.add(imageitem);
            adapter.notifyData();


        }

        @Override
        public void onCropCancel() {

        }

        @Override
        public void onCropFailed(String message) {

        }

        @Override
        public CropParams getCropParams() {
            CropParams params = new CropParams();
            params.aspectX = 400;
            params.aspectY = 400;
            return params;
        }

        @Override
        public Activity getContext() {
            return null;
        }
    };

    public ArrayList<ImageItem> getFilePhoto() {
        ArrayList<ImageItem> items = new ArrayList<>();
        //拿到所有图片文件
        File[] files = new File(MyFileUtils.SD_PATH).listFiles();
        if (files != null) {
            for (File file : files) {
                //解码file拿到bitmap
                Bitmap bitmap = BitmapFactory.decodeFile(MyFileUtils.SD_PATH + file.getName());
                ImageItem item = new ImageItem();
                item.setImagePath(file.getName());
                item.setBitmap(bitmap);
                items.add(item);
            }
        }
        return items;
    }


    //__________________________  _________________________________
    @Override
    public void showPrb() {

    }

    @Override
    public void hidePrb() {

    }

    @Override
    public void upLoadSuccess() {

    }

    @Override
    public void showMsg(String msg) {

    }


}
