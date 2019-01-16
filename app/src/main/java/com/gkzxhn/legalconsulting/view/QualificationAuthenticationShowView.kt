package com.gkzxhn.legalconsulting.view

/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */

interface QualificationAuthenticationShowView : BaseView {

    fun setName(string: String)
    fun setGender(string: String)
    fun setDescription(string: String)
    fun setLawOffice(string: String)
    fun setAddress(string: String)
    fun setProfessional(string: String)
    fun setYear(string: String)
    fun setImage1(decodeFile: String)
    fun setImage2(decodeFile: String)
    fun setImage3(decodeFile: String)
    fun setImage4(decodeFile: String)

}