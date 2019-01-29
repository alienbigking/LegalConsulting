package com.netease.nim.uikit.custom;

import com.alibaba.fastjson.JSONObject;

/**
 * @classname：myselfeAttachment
 * @author：liushaoxiang
 * @date：2018/12/28 2:34 PM
 * @description：
 */


public class MySafeAttachment extends CustomAttachment {
    private String title;
    private String subTitle;
    private String category;
    private String categoryType;
    private String cid;
    private String OrderTime;

    private final String KEY_TITLE = "title";
    private final String KEY_SUBTITLE = "subTitle";
    private final String KEY_CATEGORY = "category";
    private final String KEY_CATEGORYTYPE = "categoryType";
    private final String KEY_CID = "cid";
    private final String KEY_ORDER_TIME = "OrderTime";
    public MySafeAttachment() {
        super(CustomAttachmentType.mysafe);
    }

    public String getTitle() {
        return title;
    }

    public String getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(String orderTime) {
        OrderTime = orderTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    @Override
    protected void parseData(com.alibaba.fastjson.JSONObject data) {
        title = data.getString(KEY_TITLE);
        subTitle = data.getString(KEY_SUBTITLE);
        category = data.getString(KEY_CATEGORY);
        categoryType = data.getString(KEY_CATEGORYTYPE);
        cid = data.getString(KEY_CID);
        OrderTime = data.getString(KEY_ORDER_TIME);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();

        data.put(KEY_TITLE, getTitle());
        data.put(KEY_SUBTITLE, getSubTitle());
        data.put(KEY_CATEGORY, getCategory());
        data.put(KEY_CATEGORYTYPE, getCategoryType());
        data.put(KEY_CID, getCid());
        data.put(KEY_ORDER_TIME, getOrderTime());

        return data;

    }
}
