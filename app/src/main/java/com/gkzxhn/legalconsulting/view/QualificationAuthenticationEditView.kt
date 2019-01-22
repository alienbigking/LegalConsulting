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
    fun setLevel(levelStr: String)

    fun getCityname(): String
    fun getCitycode(): String
    fun getCountycode(): String
    fun getProvincename(): String
    fun getCountyname(): String
    fun getProvincecode(): String


    fun setName(string: String)
    fun setGender(string: String)
    fun setDescription(string: String)
    fun setLawOffice(string: String)
    fun setAddress(provinceName: String, cityName: String, countyName: String, streetDetail: String)
    fun setProfessional(string: String)
    fun setYear(string: String)
    fun setImage1(decodeFile: String)
    fun setImage2(decodeFile: String)
    fun setImage3(decodeFile: String)
    fun setImage4(decodeFile: String)

    fun setSelectStr(selectStr: ArrayList<String>)
    fun getSelectStr(): ArrayList<String>

}