package com.gkzxhn.legalconsulting.view

/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */

interface OrderView : BaseView {

    fun onFinish()
    fun setName(name: String)
    fun setTime(time: String)
    fun setReward(reward: String)
    fun setNextText(visibility: Int,str: String)
    fun setAllbgColor(color: Int)
    fun setOrderState(str: String)
    fun setOrderStateColor(color: Int)
    fun setOrderType(str1: String)
    fun setOrderNumber(time: String)
    fun setOrderImage(userName: String?)
    fun setShowOrderState(visibility: Int, stateName: String, getTime: String, completeTime: String)
    fun setShowGetMoney(visibility: Int, getMoney: String, getMoneyTime: String)
    fun setOrderStateNameColor(color: Int)
    fun setShowEvaluation(visibility: Int, ServiceSesults: String, ServiceInfo: String, star: Int)
}