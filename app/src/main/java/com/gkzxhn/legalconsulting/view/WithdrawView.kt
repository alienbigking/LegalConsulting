package com.gkzxhn.legalconsulting.view

/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */

interface WithdrawView : BaseView {
    fun getName(): String
    fun getMoney(): String
    fun getCode(): String
    fun stopCountDown()
    fun startCountDown(int: Int)
    fun onFinish()
    fun getPhone(): String
    fun setPayInfo(name: String, avatar: String?)
}