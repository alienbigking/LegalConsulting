package com.gkzxhn.legalconsulting.fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.activity.*
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.common.RxBus
import com.gkzxhn.legalconsulting.entity.LawyersInfo
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.net.RetrofitClient
import com.gkzxhn.legalconsulting.utils.ImageUtils
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
    var lawyersInfo: LawyersInfo? = null

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
                when (App.SP.getString(Constants.SP_CERTIFICATIONSTATUS, "")) {
                /****** 已认证 ******/
                    Constants.CERTIFIED -> {
                        context?.startActivity(Intent(context, QualificationAuthenticationShowActivity::class.java))
                    }
                    else -> {
                        context?.startActivity(Intent(context, QualificationAuthenticationActivity::class.java))
                    }
                }
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
                val intent = Intent(context, UserSettingActivity::class.java)
                intent.putExtra("name", if (lawyersInfo != null) lawyersInfo?.name else "")
                intent.putExtra("phoneNumber", if (lawyersInfo != null) lawyersInfo?.phoneNumber else "")
                context?.startActivity(intent)
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
                        override fun success(t: LawyersInfo) {
                            App.EDIT.putString(Constants.SP_CERTIFICATIONSTATUS, t.certificationStatus)?.commit()
                            lawyersInfo = t
                            loadUI(t)
                        }

                    })
        }
    }

    /**
     * @methodName： created by liushaoxiang on 2018/10/26 3:46 PM.
     * @description：处理UI数据
     */
    private fun loadUI(date: LawyersInfo) {
        tv_user_phone.text = StringUtils.phoneChange(date.phoneNumber)
        tv_user_name.text = date.name
        tv_user_money.text = "￥"+date.rewardAmount
        tv_user_fragment_get_order_state.text = if (date.serviceStatus == "BUSY") "忙碌" else "接单"
        App.EDIT.putString(Constants.SP_PHONE, date.phoneNumber)?.commit()
        App.EDIT.putString(Constants.SP_NAME, date.name)?.commit()
        App.EDIT.putString(Constants.SP_REWARDAMOUNT, date.rewardAmount.toString())?.commit()
        App.EDIT.putString(Constants.SP_LAWOFFICE, date.lawOffice)?.commit()

        /****** 如果图片和上次一致就不转化了 ******/
        val decodeFile = BitmapFactory.decodeFile(App.SP.getString(Constants.SP_AVATARFILE, ""))
        if (App.SP.getString(Constants.SP_AVATAR_THUMB, "")?.equals(date.avatarThumb)!! && decodeFile != null) {
            iv_user_icon.setImageBitmap(decodeFile)
        } else {
            val file = File(context?.cacheDir, "user_icon_" + System.currentTimeMillis() + ".jpg")
            if (date.avatarThumb != null) {
                val base64ToFile = ImageUtils.base64ToFile(date.avatarThumb!!, file.absolutePath)
                if (base64ToFile) {
                    App.EDIT.putString(Constants.SP_AVATARFILE, file.absolutePath)?.commit()
                    App.EDIT.putString(Constants.SP_AVATAR_THUMB, date.avatarThumb)?.commit()

                    val decodeFile = BitmapFactory.decodeFile(file.absolutePath)
                    iv_user_icon.setImageBitmap(decodeFile)
                }
            } else {
                App.EDIT.putString(Constants.SP_AVATARFILE, "")?.commit()
                App.EDIT.putString(Constants.SP_AVATAR_THUMB, "")?.commit()

            }
        }

        RxBus.instance.post(date)
    }

}
