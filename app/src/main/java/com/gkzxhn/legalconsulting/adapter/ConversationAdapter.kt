package com.gkzxhn.legalconsulting.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.gkzxhn.legalconsulting.R
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter
import kotlinx.android.synthetic.main.item_conversation.view.*
import java.util.*


/**
 * Explanation：
 * @author LSX
 * Created on 2018/9/10.
 */

class ConversationAdapter(private val mContext: Context, private val data: List<String>?) : RecyclerView.Adapter<ConversationAdapter.ViewHolder>() {


    private var mDatas: ArrayList<String> = ArrayList()
    private var onItemClickListener: MultiItemTypeAdapter.OnItemClickListener? = null
    private var mCurrentIndex = -1

    /**
     *  获取当前项实体
     */
    fun getCurrentItem(): String {
        return mDatas[mCurrentIndex]
    }

    fun setOnItemClickListener(onItemClickListener: MultiItemTypeAdapter.OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    /**
     * 更新数据
     */
    fun updateItems(mDatas: List<String>?) {
        this.mDatas.clear()
        if (mDatas != null && mDatas.isNotEmpty()) {
            this.mDatas.addAll(mDatas)
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_conversation, null)
        view.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        return ViewHolder(view)
    }

    class ViewHolder(view: View?) : RecyclerView.ViewHolder(view!!)


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView) {
            val entity = mDatas[position]
            tv_conversation_item_name.text = entity+position
            holder.itemView.setOnClickListener({
                mCurrentIndex = position
                onItemClickListener?.onItemClick(this,holder,position)
            })
        }
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

}
