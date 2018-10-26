package com.gkzxhn.legalconsulting.fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.View
import com.gkzxhn.legalconsulting.utils.ImageUtils
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.activity.*
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.entity.LawyersInfo
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.net.RetrofitClient
import com.gkzxhn.legalconsulting.utils.ProjectUtils
import com.gkzxhn.legalconsulting.utils.StringUtils
import kotlinx.android.synthetic.main.user_fragment.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File


/**
 * Explanation: 个人中心
 * @author LSX
 *    -----2018/9/7
 */

class UserFragment : BaseFragment(), View.OnClickListener {


    override fun provideContentViewId(): Int {
        return R.layout.user_fragment
    }

    override fun init() {
        getLawyersInfo()
    }

    override fun initListener() {
        v_user_get_order_bg.setOnClickListener(this)
        v_user_my_money_bg.setOnClickListener(this)
        v_user_rz_bg.setOnClickListener(this)
        v_user_all_order_bg.setOnClickListener(this)
        v_user_set_bg.setOnClickListener(this)
        v_user_top_bg.setOnClickListener(this)
        /****** 给个人信息栏设置背影触摸变化 ******/
        ProjectUtils.addViewTouchChange(v_user_top_bg)

    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.v_user_get_order_bg -> {
                val intent = Intent(context, OrderGetSettingActivity::class.java)
                val state = if (tv_user_fragment_get_order_state.text == "忙碌") "1" else "2"
                intent.putExtra(Constants.ORDER_GET_STATE, state)
                context?.startActivity(intent)
            }
            R.id.v_user_my_money_bg -> {
                context?.startActivity(Intent(context, BountyActivity::class.java))
            }
            R.id.v_user_rz_bg -> {
                context?.startActivity(Intent(context, QualificationAuthenticationShowActivity::class.java))
            }
//            所有订单
            R.id.v_user_all_order_bg -> {
                context?.startActivity(Intent(context, AllOrderActivity::class.java))
            }
//            设置
            R.id.v_user_set_bg -> {
                context?.startActivity(Intent(context, SettingActivity::class.java))
            }
//            个人信息栏
            R.id.v_user_top_bg -> {
                context?.startActivity(Intent(context, UserSettingActivity::class.java))
            }
        }
    }


    override fun onResume() {
        super.onResume()
        init()
    }

    /**
     * @methodName： created by liushaoxiang on 2018/10/22 3:31 PM.
     * @description：获取律师信息
     */
    private fun getLawyersInfo() {
        context?.let {
            RetrofitClient.getInstance(it).mApi?.getLawyersInfo()
                    ?.subscribeOn(Schedulers.io())
                    ?.unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<LawyersInfo>(it) {
                        override fun success(date: LawyersInfo) {
                            tv_user_phone.text = StringUtils.phoneChange(date.phoneNumber)
                            tv_user_name.text = date.name
                            tv_user_fragment_get_order_state.text = if (date.profiles?.serviceStatus == "BUSY") "忙碌" else "接单"
                            val file = File(contexts?.cacheDir, "user_icon_" + System.currentTimeMillis() + ".jpg")
                            if (date.profiles?.avatarThumb != null) {
                                val base64ToFile = ImageUtils.base64ToFile(date.profiles.avatarThumb!!, file.absolutePath)
                                if (base64ToFile) {
                                    val decodeFile = BitmapFactory.decodeFile(file.absolutePath)
                                    iv_user_icon.setImageBitmap(decodeFile)
                                }

                            }
                        }

                    })
        }

    }

}
