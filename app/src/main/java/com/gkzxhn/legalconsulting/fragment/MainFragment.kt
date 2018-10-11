package com.gkzxhn.legalconsulting.fragment

import android.content.Intent
import android.support.v4.view.ViewPager
import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.activity.QualificationAuthenticationEditActivity
import com.gkzxhn.legalconsulting.adapter.MainAdapter
import java.util.*
import kotlinx.android.synthetic.main.main_fragment.iv_main_message_top as ivMessageTop
import kotlinx.android.synthetic.main.main_fragment.tv_home_edit_order as tvEditOrder
import kotlinx.android.synthetic.main.main_fragment.tv_home_get_order as tvGetOrder
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
        tbList = ArrayList()
        tbList?.add(OrderReceivingFragment())
        tbList?.add(OrderDisposeFragment())
        mainAdapter = MainAdapter(childFragmentManager, tbList)
        VpHome.adapter = mainAdapter
        VpHome.offscreenPageLimit = 3

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

            startActivity(Intent(context,QualificationAuthenticationEditActivity::class.java))
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


}
