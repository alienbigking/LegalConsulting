package com.gkzxhn.legalconsulting.view

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
    fun setNextText(reward: String)
    fun setAllbgColor(color: Int)
    fun setOrderState(str: String)
    fun setOrderStateColor(color: Int)
    fun setBottomSelectVisibility(visibility: Int)
    fun setShowOrderInfo(visibility: Int, time: String, name: String)
    fun setOrderType(str1: String, str2: String, str3: String)
}