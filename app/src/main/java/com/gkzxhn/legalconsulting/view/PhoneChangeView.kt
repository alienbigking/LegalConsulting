package com.gkzxhn.legalconsulting.view

/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */

interface PhoneChangeView : BaseView {
    fun getPhone(): String
    fun getCode(): String
    fun stopCountDown()
    fun startCountDown(int: Int)
    fun onFinish()
}