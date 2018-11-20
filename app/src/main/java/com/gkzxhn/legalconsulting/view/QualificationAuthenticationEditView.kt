package com.gkzxhn.legalconsulting.view

import android.graphics.Bitmap

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
    fun setLevel(levelStr: String)


    fun setName(string: String)
    fun setGender(string: String)
    fun setDescription(string: String)
    fun setLawOffice(string: String)
    fun setAddress(string: String)
    fun setProfessional(string: String)
    fun setYear(string: String)
    fun setImage1(decodeFile: Bitmap)
    fun setImage2(decodeFile: Bitmap)
    fun setImage3(decodeFile: Bitmap)
    fun setImage4(decodeFile: Bitmap)

}