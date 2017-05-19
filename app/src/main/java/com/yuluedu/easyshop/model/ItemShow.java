package com.yuluedu.easyshop.model;

/**
 * Created by gameben on 2017-04-21.
 */

public class ItemShow {
    //单行布局的名称
    private String item_title;
    //单行布局的内容
    private String item_content;

    public ItemShow(String item_title, String item_content){
        this.item_title = item_title;
        this.item_content = item_content;
    }

    public String getItem_title() {
        return item_title;
    }

    public String getItem_content() {
        return item_content;
    }
}
