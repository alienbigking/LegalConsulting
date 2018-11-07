package com.gkzxhn.legalconsulting.fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v4.view.ViewPager
import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.activity.QualificationAuthenticationEditActivity
import com.gkzxhn.legalconsulting.adapter.MainAdapter
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.common.RxBus
import com.gkzxhn.legalconsulting.entity.LawyersInfo
import com.gkzxhn.legalconsulting.utils.logE
import kotlinx.android.synthetic.main.main_fragment.*
import rx.android.schedulers.AndroidSchedulers
import java.util.*
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
class MainFragment : BaseFragment() {

    var tbList: MutableList<BaseFragment>? = null
    private var mainAdapter: MainAdapter? = null

    override fun provideContentViewId(): Int {
        return R.layout.main_fragment
    }


    override fun init() {
        loadTopUI()
        tbList = ArrayList()
        tbList?.add(OrderReceivingFragment())
        tbList?.add(OrderDisposeFragment())
        mainAdapter = MainAdapter(childFragmentManager, tbList)
        VpHome.adapter = mainAdapter
        VpHome.offscreenPageLimit = 3

        RxBus.instance.toObserverable(LawyersInfo::class.java)
                .cache()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    loadTopUI()
                }, {
                    it.message.toString().logE(this)
                })
    }

    private fun loadTopUI() {
        tv_main_name.text=App.SP.getString(Constants.SP_NAME, "- - - -")
        tv_home_address.text="执业律所："+App.SP.getString(Constants.SP_LAWOFFICE, "- - - -")

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
            Constants.CERTIFIED -> tvCertification.text = "已认证"
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

            startActivity(Intent(context, QualificationAuthenticationEditActivity::class.java))
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
}
