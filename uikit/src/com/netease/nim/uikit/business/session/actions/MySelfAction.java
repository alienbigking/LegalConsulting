package com.netease.nim.uikit.business.session.actions;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.custom.MySafeAttachment;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * @classname：MySelfAction
 * @author：liushaoxiang
 * @date：2018/12/29 10:24 AM
 * @description： 自定义的消息
 */
public class MySelfAction extends BaseAction {

    public MySelfAction() {
        /****** 显示的图标的文字 ******/
        super(R.drawable.nim_actionbar_dark_back_icon, R.string.app_name);
    }

    @Override
    public void onClick() {
        MySafeAttachment attachment = new MySafeAttachment();
        attachment.setSubTitle("测试专题");
        attachment.setTitle("自定义消息——————");

        // 以下 "图文链接：" + attachment.getTitle() 用来显示app消息推送时，图片显示的内容。
        IMMessage message = MessageBuilder.createCustomMessage(getAccount(), getSessionType(), "图文链接：" + attachment.getTitle(), attachment);
        sendMessage(message);
    }
}

