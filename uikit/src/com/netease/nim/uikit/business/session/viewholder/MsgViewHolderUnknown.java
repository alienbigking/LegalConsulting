package com.netease.nim.uikit.business.session.viewholder;

import android.widget.TextView;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

/**
 * Created by zhoujianghua on 2015/8/6.
 */
public class MsgViewHolderUnknown extends MsgViewHolderBase {
    protected TextView title;

    public MsgViewHolderUnknown(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_unknown;
    }

    @Override
    protected boolean isShowHeadImage() {
        if (message.getSessionType() == SessionTypeEnum.ChatRoom) {
            return false;
        }
        return true;
    }

    @Override
    protected void inflateContentView() {
        title= findViewById(R.id.message_item_unsupport_title);
    }

    @Override
    protected void bindContentView() {
        title.setText(message.getContent());
        message.getAttachment();
        message.getConfig();
        message.getNIMAntiSpamOption();
        message.getStatus();
        message.getDirect();
        message.getSessionType();
        message.getLocalExtension();
        message.getAttachStatus();
        message.getMsgType();
        message.getPushPayload();
        message.getRemoteExtension();
        message.getMemberPushOption();
        message.getPushContent();
        message.getRemoteExtension();
        message.getAttachment();
        message.getRemoteExtension();
        message.getRemoteExtension();
    }
}

