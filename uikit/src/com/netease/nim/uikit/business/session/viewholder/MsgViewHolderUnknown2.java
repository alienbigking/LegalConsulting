package com.netease.nim.uikit.business.session.viewholder;

import android.widget.TextView;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.viewholder.robot.myselfeAttachment;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;

/**
 * Created by zhoujianghua on 2015/8/6.
 */
public class MsgViewHolderUnknown2 extends MsgViewHolderBase {

    protected TextView title;

    public MsgViewHolderUnknown2(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_unknown;
    }

    @Override
    protected void inflateContentView() {
        title= findViewById(R.id.message_item_unsupport_title);
    }

    @Override
    protected void bindContentView() {
        myselfeAttachment attachment = (myselfeAttachment) message.getAttachment();
        message.getPushContent();
    }
}

