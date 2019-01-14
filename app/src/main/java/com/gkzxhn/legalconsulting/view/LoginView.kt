package com.gkzxhn.legalconsulting.view

/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */

interface LoginView : BaseView {
    fun getPhone(): String
    fun getCode(): String
    fun stopCountDown()
    fun startCountDown(int: Int)
    fun onFinish()
    fun getRememberState(): Boolean
}