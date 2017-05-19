package com.yuluedu.easyshop.main.me.goodsupload;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.yuluedu.easyshop.commons.MyFileUtils;
import com.yuluedu.easyshop.model.GoodsUpLoad;
import com.yuluedu.easyshop.model.GoodsUpLoadResult;
import com.yuluedu.easyshop.model.ImageItem;
import com.yuluedu.easyshop.network.EasyShopClient;
import com.yuluedu.easyshop.network.UICallBack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by gameben on 2017-04-25.
 */

public class GoodsUpLoadPresenter extends MvpNullObjectBasePresenter<GoodsUpLoadView>{
    private Call call;

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (call != null) call.cancel();
    }
    //商品上传
    public void upLoad(GoodsUpLoad goodsUpLoad, List<ImageItem> list){
        getView().showPrb();
        call = EasyShopClient.getInstance().upLoad(goodsUpLoad,getFiles(list));
        call.enqueue(new UICallBack() {
            @Override
            public void onFailureUI(Call call, IOException e) {
                getView().hidePrb();
                getView().showMsg(e.getMessage());
            }

            @Override
            public void onResponseUI(Call call, String body) {
                getView().hidePrb();
                GoodsUpLoadResult result = new Gson().fromJson(body,GoodsUpLoadResult.class);
                getView().showMsg(result.getMessage());
                //上传成功
                if (result.getCode() == 1){
                    getView().upLoadSuccess();
                }
            }
        });
    }

    //根据imageItem（图片路径）获取图片文件
    private ArrayList<File> getFiles(List<ImageItem> list){
        ArrayList<File> files = new ArrayList<>();
        for (ImageItem imageItem : list){
            //根据图片路径，拿到图片文件
            File file = new File(MyFileUtils.SD_PATH + imageItem.getImagePath());
            files.add(file);
        }
        return files;
    }


}
