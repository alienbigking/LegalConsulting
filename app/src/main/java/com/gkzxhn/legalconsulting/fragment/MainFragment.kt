package com.gkzxhn.legalconsulting.fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.activity.NotificationActivity
import com.gkzxhn.legalconsulting.activity.QualificationAuthenticationActivity
import com.gkzxhn.legalconsulting.adapter.MainAdapter
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.common.RxBus
import com.gkzxhn.legalconsulting.entity.RxBusBean
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.net.RetrofitClient
import com.gkzxhn.legalconsulting.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.main_fragment.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Response
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import kotlinx.android.synthetic.main.main_fragment.iv_main_message_top as ivMessageTop
import kotlinx.android.synthetic.main.main_fragment.tv_home_edit_order as tvEditOrder
import kotlinx.android.synthetic.main.main_fragment.tv_home_get_order as tvGetOrder
import kotlinx.android.synthetic.main.main_fragment.tv_main_top_end as tvCertification
import kotlinx.android.synthetic.main.main_fragment.tv_main_top_title as tvTopTitle
import kotlinx.android.synthetic.main.main_fragment.v_home_select_line1 as vSelectLine1
import kotlinx.android.synthetic.main.main_fragment.v_home_select_line2 as vSelectLine2
import kotlinx.android.synthetic.main.main_fragment.vp_home as VpHome


/**
 * Explanation: 首页
 * @author LSX
 *    -----2018/9/7
 */
class MainFragment : BaseFragment(), CompoundButton.OnCheckedChangeListener {

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        /****** 没有认证通过的时候 跳转认证页面 ******/
        if (ProjectUtils.certificationStatus()) {
            if (isChecked) {
                setOrderState("RECEIVING")
            } else {
                setOrderState("BUSY")
            }
        } else {
            st_home_get_order_state.isChecked=!isChecked
            val tsClickDialog = context?.TsClickDialog("您尚未认证", true)
            val send = tsClickDialog?.findViewById<TextView>(R.id.dialog_save)
            send?.text = "认证"
            send?.setOnClickListener {
                context?.startActivity(Intent(context, QualificationAuthenticationActivity::class.java))
                tsClickDialog.dismiss()
            }
        }
    }

    var tbList: MutableList<BaseFragment>? = null
    private var mainAdapter: MainAdapter? = null
    var categories: ArrayList<String>? = null
    /****** 接单状态 ******/
    var serviceStatus: String = ""

    override fun provideContentViewId(): Int {
        return R.layout.main_fragment
    }

    override fun init() {
        loadTopUI()
        tbList = ArrayList()
        categories = ArrayList()
        tbList?.add(OrderReceivingFragment())
        tbList?.add(OrderDisposeFragment())
        mainAdapter = MainAdapter(childFragmentManager, tbList)
        VpHome.adapter = mainAdapter
        VpHome.offscreenPageLimit = 3

        /****** 接受更新的律师信息 ******/
        RxBus.instance.toObserverable(RxBusBean.HomeUserInfo::class.java)
                .cache()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    categories = it.lawyersInfo.categories as ArrayList<String>?
                    serviceStatus = it.lawyersInfo.serviceStatus.toString()
                    loadTopUI()

                }, {
                    it.message.toString().logE(this)
                })

        /****** 接受控件小红点的消息 ******/
        RxBus.instance.toObserverable(RxBusBean.HomeTopRedPoint::class.java)
                .cache()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    v_top_red_point.visibility = if (it.show) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }, {
                    it.message.toString().logE(this)
                })
    }

    private fun loadTopUI() {
        tv_main_name.text = App.SP.getString(Constants.SP_NAME, "- - - -")
        tv_home_address.text = "执业律所：" + App.SP.getString(Constants.SP_LAWOFFICE, "- - - -")
        if (categories != null && categories!!.isNotEmpty()) {
            tv_home_type1.visibility = View.VISIBLE
            tv_home_type1.text = ProjectUtils.categoriesConversion(categories!![0])
            if (categories!!.size > 1) {
                tv_home_type2.visibility = View.VISIBLE
                tv_home_type2.text = ProjectUtils.categoriesConversion(categories!![1])
            }
            if (categories!!.size > 2) {
                tv_home_type3.visibility = View.VISIBLE
                tv_home_type3.text = ProjectUtils.categoriesConversion(categories!![2])
            }
        }
        /****** 在设置前把监听设空  ******/
        st_home_get_order_state.setOnCheckedChangeListener(null)
        var busy = serviceStatus == "BUSY"
        st_home_get_order_state.isChecked = !busy
        /****** 恢复监听 ******/
        st_home_get_order_state.setOnCheckedChangeListener(this)


        val avatarStr = App.SP.getString(Constants.SP_AVATARFILE, "")
        if (avatarStr?.isNotEmpty()!!) {
            val decodeFile = BitmapFactory.decodeFile(avatarStr)
            iv_main_icon.setImageBitmap(decodeFile)
        }
        when (App.SP.getString(Constants.SP_CERTIFICATIONSTATUS, "")) {
            Constants.PENDING_CERTIFIED -> {
                tvCertification.text = "未认证"
                tv_home_address.visibility = View.GONE
                tvCertification.setTextColor(resources.getColor(R.color.home_top_red))
            }
            Constants.PENDING_APPROVAL -> {
                tvCertification.text = "待审核"
                tv_home_address.visibility = View.GONE
                tvCertification.setTextColor(resources.getColor(R.color.home_top_red))
            }
            Constants.APPROVAL_FAILURE -> {
                tv_home_address.visibility = View.GONE
                tvCertification.text = "未认证"
                tvCertification.setTextColor(resources.getColor(R.color.home_top_red))
            }
            Constants.CERTIFIED -> {
                tvCertification.text = "已认证"
                tv_home_address.visibility = View.VISIBLE
                tvCertification.setTextColor(resources.getColor(R.color.home_top_green))
            }
        }
    }

    override fun initListener() {
        tvGetOrder.setOnClickListener {
            VpHome.currentItem = 0
            selectOneItem()
        }
        tvEditOrder.setOnClickListener {
            VpHome.currentItem = 1
            selectTwoItem()
        }
        ivMessageTop.setOnClickListener {
            startActivity(Intent(context, NotificationActivity::class.java))
        }

        v_home_top_bg.setOnClickListener {
        }

        VpHome.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                if (p0 == 0) {
                    selectOneItem()
                } else if (p0 == 1) {
                    selectTwoItem()
                }
            }

            override fun onPageScrollStateChanged(p0: Int) {
            }
        })
    }

    private fun selectTwoItem() {
        resources.getColor(R.color.main_top_gary).let { it1 -> tvGetOrder.setTextColor(it1) }
        resources.getColor(R.color.main_top_blue).let { it1 -> tvEditOrder.setTextColor(it1) }
        vSelectLine1.visibility = View.INVISIBLE
        vSelectLine2.visibility = View.VISIBLE
    }

    private fun selectOneItem() {
        resources.getColor(R.color.main_top_blue).let { it1 -> tvGetOrder.setTextColor(it1) }
        resources.getColor(R.color.main_top_gary).let { it1 -> tvEditOrder.setTextColor(it1) }
        vSelectLine1.visibility = View.VISIBLE
        vSelectLine2.visibility = View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        loadTopUI()
    }

    private fun setOrderState(OrderState: String) {
        val Body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(OrderState))
        context?.let {
            RetrofitClient.getInstance(it).mApi?.setOrderState(Body)
                    ?.subscribeOn(Schedulers.io())
                    ?.unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<Response<Void>>(it) {
                        override fun success(t: Response<Void>) {
                            when (t.code()) {
                                204 -> {
                                    if (OrderState == "BUSY") {
                                        it.TsDialog("已设置为忙碌状态", true)
                                    } else {
                                        it.TsDialog("已设置为接单状态", true)
                                    }
                                }
                                else -> {
                                    it.showToast(t.code().toString() + t.message())
                                }
                            }
                        }
                    })
        }
    }

}
