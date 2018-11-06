package com.gkzxhn.legalconsulting.view

/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */

interface QualificationAuthenticationEditView : BaseView {

    fun getName(): String
    fun getGender(): String
    fun getDescription(): String
    fun getLawOffice(): String
    fun getAddress(): String
    fun getProfessional(): String
    fun getYear(): Int
    fun onFinish()


}