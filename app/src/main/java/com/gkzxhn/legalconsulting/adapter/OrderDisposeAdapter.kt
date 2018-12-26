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
import com.gkzxhn.legalconsulting.utils.ProjectUtils
import com.gkzxhn.legalconsulting.utils.StringUtils
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter
import kotlinx.android.synthetic.main.item_order_dispose.view.*
import java.util.*

/**
 * Explanation： 我的 （指定单）
 * @author LSX
 * Created on 2018/9/10.
 */

class OrderDisposeAdapter(private val mContext: Context, private val data: List<OrderDispose.ContentBean>?) : RecyclerView.Adapter<OrderDisposeAdapter.ViewHolder>() {

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

    /****** 回调拒单 接单事件 ******/
    private var onItemOrderListener: ItemOrderListener? = null

    fun setOnItemOrderListener(ItemOrderListener: ItemOrderListener) {
        this.onItemOrderListener = ItemOrderListener
    }

    interface ItemOrderListener {
        fun onRefusedListener()
    }

    /**
     * 更新数据
     */
    fun updateItems(clear:Boolean,mDatas: List<OrderDispose.ContentBean>?) {
        if (clear) {
            this.mDatas.clear()
        }
        if (mDatas != null && mDatas.isNotEmpty()) {
            this.mDatas.addAll(mDatas)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_order_dispose, null)
        view.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        return ViewHolder(view)
    }

    class ViewHolder(view: View?) : RecyclerView.ViewHolder(view!!)


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView) {
            val entity = mDatas[position]
            tv_order_dispose_name.text = entity.customer!!.name
            ProjectUtils.loadImage(context,entity.customer!!.avatarURL,iv_order_dispose_item)
            v_item_order_receiving_type.text= ProjectUtils.categoriesConversion(entity.category!!)
            /****** 价格 ******/
            tv_main_top_end.text = "￥" + entity.reward
            tv_order_dispose_description.text = entity.description
            tv_order_dispose_time.text = StringUtils.parseDate(entity.createdTime)

            if (entity.type!="RUSH") {
                /****** 指定单 ******/
                tv_order_dispose_top_type.visibility=View.VISIBLE
                tv_main_top_end.text = ""
            }else{
                tv_order_dispose_top_type.visibility=View.GONE
            }


            /****** 待接单的时候 显示按扭 ******/
            if (entity.status == Constants.ORDER_STATE_PENDING_RECEIVING) {
                tv_order_dispose_refused.visibility = View.VISIBLE
                v_order_dispose_description.visibility = View.VISIBLE
            } else {
                tv_order_dispose_refused.visibility = View.GONE
                v_order_dispose_description.visibility = View.GONE
            }
            holder.itemView.setOnClickListener({
                mCurrentIndex = position
                onItemClickListener?.onItemClick(this, holder, position)
            })

            /****** 拒绝单 ******/
            tv_order_dispose_refused.setOnClickListener {
                mCurrentIndex = position
                onItemOrderListener?.onRefusedListener()
            }
        }
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

}
