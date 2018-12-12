package com.gkzxhn.legalconsulting.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.utils.showToast
import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.bean.CityBean
import com.lljjcoder.bean.DistrictBean
import com.lljjcoder.bean.ProvinceBean
import com.lljjcoder.citywheel.CityConfig
import com.lljjcoder.style.citypickerview.CityPickerView
import kotlinx.android.synthetic.main.activity_edit_address.*

/**
 * @classname：地址填写
 * @author：liushaoxiang
 * @date：2018/10/28 5:36 PM
 * @description：
 */

class EditAddressActivity : BaseActivity() {
    lateinit var cityPicker: CityPickerView
    var provinceCode = ""
    var provinceName = ""
    var cityName = ""
    var cityCode = ""
    var countyCode = ""
    var countyName = ""

    override fun init() {
        cityPicker = CityPickerView()
        cityPicker.init(this)
        //添加默认的配置，不需要自己定义
        val cityConfig = CityConfig.Builder().build()
        cityPicker.setConfig(cityConfig)
        val address = intent.getStringExtra("address").toString()
        provinceName = intent.getStringExtra("provinceName").toString()
        cityName = intent.getStringExtra("cityName").toString()
        countyName = intent.getStringExtra("countyName").toString()
        if (address.isNotEmpty() && countyName.isNotEmpty()) {
            et_edit_address.setText(address)
            tv_edit_address.text = provinceName + cityName + countyName
        }

        tv_edit_save.setOnClickListener {
            val address = et_edit_address.text.trim().toString()
            if (address.isNotEmpty() && countyName.isNotEmpty()) {
                var intent = Intent()
                intent.putExtra(Constants.RESULT_EDIT_ADDRESS, address)
                intent.putExtra(Constants.RESULT_EDIT_ADDRESS_PROVINCECODE, provinceCode)
                intent.putExtra(Constants.RESULT_EDIT_ADDRESS_PROVINCENAME, provinceName)
                intent.putExtra(Constants.RESULT_EDIT_ADDRESS_CITYNAME, cityName)
                intent.putExtra(Constants.RESULT_EDIT_ADDRESS_CITYCODE, cityCode)
                intent.putExtra(Constants.RESULT_EDIT_ADDRESS_COUNTYCODE, countyCode)
                intent.putExtra(Constants.RESULT_EDIT_ADDRESS_COUNTYNAME, countyName)
                setResult(Constants.RESULTCODE_EDIT_ADDRESS, intent)
                finish()
            } else {
                showToast("还未填写完成！")
            }
        }
        iv_edit_address_back.setOnClickListener { onBackPressed() }
        tv_edit_address.setOnClickListener {
            /****** 如果键盘没有关掉 执行关掉代码 ******/
            val view = window.peekDecorView()
            if (view != null) {
                val inputManger =  getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManger.hideSoftInputFromWindow(view.windowToken, 0);
            }

            //显示
            cityPicker.showCityPicker()

        }

        cityPicker.setOnCityItemClickListener(object : OnCityItemClickListener() {
            @SuppressLint("SetTextI18n")
            override fun onSelected(province: ProvinceBean?, city: CityBean?, district: DistrictBean?) {
                super.onSelected(province, city, district)

                //省份
                if (province != null) {
                }

                //城市
                if (city != null) {
                }

                //地区
                if (district != null) {
                }

                provinceCode = province?.id.toString()
                provinceName = province?.name.toString()
                cityCode = city?.id.toString()
                cityName = city?.name.toString()
                countyCode = district?.id.toString()
                countyName = district?.name.toString()

                tv_edit_address.text = provinceName + cityName + countyName
            }

        })
    }


    override fun provideContentViewId(): Int {
        return R.layout.activity_edit_address
    }

}
