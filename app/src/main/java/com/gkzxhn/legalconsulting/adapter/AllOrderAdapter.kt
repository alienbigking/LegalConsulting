package com.gkzxhn.legalconsulting.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.entity.OrderDispose
import com.gkzxhn.legalconsulting.utils.StringUtils
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter
import kotlinx.android.synthetic.main.item_order_all_dispose.view.*
import java.util.*


/**
 * Explanation：所有订单
 * @author LSX
 * Created on 2018/9/10.
 */

class AllOrderAdapter(private val mContext: Context) : RecyclerView.Adapter<AllOrderAdapter.ViewHolder>() {

    private var mDatas: ArrayList<OrderDispose.ContentBean> = ArrayList()
    private var onItemClickListener: MultiItemTypeAdapter.OnItemClickListener? = null
    private var mCurrentIndex = -1

    /**
     *  获取当前项实体
     */
    fun getCurrentItem(): OrderDispose.ContentBean {
        return mDatas[mCurrentIndex]
    }

    fun setOnItemClickListener(onItemClickListener: MultiItemTypeAdapter.OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    /**
     * 更新数据
     */
    fun updateItems(clear: Boolean, mDatas: List<OrderDispose.ContentBean>?) {
        if (clear) {
            this.mDatas.clear()
        }
        if (mDatas != null && mDatas.isNotEmpty()) {
            this.mDatas.addAll(mDatas)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_order_all_dispose, null)
        view.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        return ViewHolder(view)
    }

    class ViewHolder(view: View?) : RecyclerView.ViewHolder(view)


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView) {
            val entity = mDatas[position]
            tv_order_dispose_name.text = entity.customer!!.name
            /****** 价格 ******/
            tv_order_price.text = "￥" + entity.reward
            tv_order_dispose_description.text = entity.description
            tv_order_dispose_time.text = StringUtils.parseDate(entity.createdTime)

            when (entity.status) {
            /******  ******/
                Constants.ORDER_STATE_PENDING_RECEIVING -> {
                    tv_order_dispose_state.text = "待接单"
                }
            /****** 已接单 ******/
                Constants.ORDER_STATE_ACCEPTED -> {
                    tv_order_dispose_state.text = "已接单"
                }
            /****** 已完成 ******/
                Constants.ORDER_STATE_COMPLETE -> {
                    tv_order_dispose_state.text = "已完成"
                }
            /******  已拒绝 ******/
                Constants.ORDER_STATE_REFUSED -> {
                    tv_order_dispose_state.text = "已拒绝"
                }
            /******  已取消 ******/
                Constants.ORDER_STATE_CANCELLED -> {
                    tv_order_dispose_state.text = "已取消"
                }
            }
            holder.itemView.setOnClickListener(android.view.View.OnClickListener {
                mCurrentIndex = position
                onItemClickListener?.onItemClick(this, holder, position)
            })

        }
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

}
