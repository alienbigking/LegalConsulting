package com.gkzxhn.legalconsulting.view

import android.app.Activity

/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */

interface BountyView : BaseView {
    fun getActivity():Activity
    fun finishActivity()
    fun setMoney(money: String)
    fun setBindState(bindState: String)
    fun changeBingState(bindState: Boolean)
}