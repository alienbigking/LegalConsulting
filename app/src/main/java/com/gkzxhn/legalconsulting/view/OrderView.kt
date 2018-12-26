package com.gkzxhn.legalconsulting.view

import android.graphics.Bitmap

/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */

interface OrderView : BaseView {

    fun onFinish()
    fun setDescription(description: String)
    fun setName(name: String)
    fun setTime(time: String)
    fun setReward(reward: String)
    fun setNextText(str: String)
    fun setAllbgColor(color: Int)
    fun setOrderState(str: String)
    fun setOrderStateColor(color: Int)
    fun setBottomSelectVisibility(visibility: Int)
    fun setShowOrderInfo(visibility: Int, time: String, name: String)
    fun setOrderType(str1: String)
    fun setImage1(bitmap: Bitmap)
    fun setImage2(bitmap: Bitmap)
    fun setOrderNumber(time: String)
    fun setOrderImage(avatarURL: String?)
    fun getOrderMoeny(): String
}