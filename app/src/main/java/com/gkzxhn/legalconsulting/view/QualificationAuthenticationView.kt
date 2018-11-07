package com.gkzxhn.legalconsulting.view

/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */

interface QualificationAuthenticationView : BaseView {
    fun changeMessage(string: String)
    fun changeQualificationAuthentication(string: String)
    fun changeQualificationAuthenticationVisibility(visibility: Int)
    fun onFinish()

}