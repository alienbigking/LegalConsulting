package com.netease.nim.uikit.business.session.viewholder.robot;

import android.util.Log;

import com.netease.nimlib.s.f;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

import org.json.JSONObject;

/**
 * @classname：myselfeAttachment
 * @author：liushaoxiang
 * @date：2018/12/28 2:34 PM
 * @description：
 */


public class myselfeAttachment implements MsgAttachment {
    private String title;
    private String subTitle;

    public myselfeAttachment() {
    }

    public myselfeAttachment(String var1) {
        this.fromJson(var1);
    }


    protected void save(JSONObject var1) {
//        f.a(var1, "dur", this.duration);
        Log.e("xiaowu", var1.toString() + "55");
    }

    protected void load(JSONObject var1) {
//        this.duration = (long)f.a(var1, "dur");
        Log.e("xiaowu", var1.toString() + "545");

    }


    private void fromJson(String var1) {
        JSONObject var2 = f.a(var1);
        Log.e("xiaowu", "545");

    }

    myselfeAttachment(String title,String subTitle) {
        this.title = title;
        this.subTitle = subTitle;
    }
    public void fromJson(JSONObject data) {
        Log.e("xiaowu", "545");
        if (data != null) {
//            parseData(data);
        }
    }

    @Override
    public String toJson(boolean send) {
        Log.e("xiaowu", "545");
//        return CustomAttachParser.packData(type, packData());
        return "";
    }

//    protected abstract void parseData(JSONObject data);
//    protected abstract JSONObject packData();
}
