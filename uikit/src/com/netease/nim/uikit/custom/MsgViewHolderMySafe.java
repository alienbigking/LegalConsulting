package com.netease.nim.uikit.custom;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;

/**
 * Created by zhoujianghua on 2015/8/6.
 */

public class MsgViewHolderMySafe extends MsgViewHolderBase {

    protected TextView title;
    protected TextView OrderTime;
    protected ImageView image;
    protected RelativeLayout all;

    public MsgViewHolderMySafe(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_mysafe;
    }

    @Override
    protected void inflateContentView() {
        title = findViewById(R.id.message_item_title_myself);
        image = findViewById(R.id.message_item_image_safe);
        all = findViewById(R.id.message_item_myself_container);
        OrderTime = findViewById(R.id.message_item_order_time);
    }

    @Override
    protected void bindContentView() {
        final MySafeAttachment attachment = (MySafeAttachment) message.getAttachment();
        title.setText(attachment.getCategory());
        OrderTime.setText(attachment.getOrderTime());
        if (attachment.getCategory() != null) {
            switch (attachment.getCategory()) {
                case "财产纠纷":
                    image.setImageResource(R.drawable.ic_cwjf);
                    break;
                case "婚姻家庭":
                    image.setImageResource(R.drawable.ic_hyjt);
                    break;
                case "交通事故":
                    image.setImageResource(R.drawable.ic_jtsg);
                    break;
                case "工伤赔偿":
                    image.setImageResource(R.drawable.ic_gspc);
                    break;
                case "合同纠纷":
                    image.setImageResource(R.drawable.ic_htjf);
                    break;
                case "刑事辩护":
                    image.setImageResource(R.drawable.ic_xs);
                    break;
                case "房产纠纷":
                    image.setImageResource(R.drawable.fcjf);
                    break;
                case "劳动就业":
                    image.setImageResource(R.drawable.ic_ld);
                    break;
                default:
                    image.setImageResource(R.drawable.ic_xs);
                    break;
            }
        }

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /****** 隐式调用 打开订单页 ******/
                // 实例化Intent
                Intent intent = new Intent();
                //设置Intent的Action属性
                intent.setAction("android.intent.action.OrderActivity");
                intent.putExtra("orderId", attachment.getCid());
                intent.putExtra("orderState", 2);
                context.startActivity(intent);

            }
        });

    }
}

