package com.gkzxhn.legalconsulting.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.gkzxhn.legalconsulting.fragment.BaseFragment

/**
 * Explanation:
 * @author LSX
 *    -----2018/9/7
 */

class MainAdapter(  fm: FragmentManager, private var mFragments: List<BaseFragment>?) : FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        return mFragments!![position]
    }

    override fun getCount(): Int {
        return mFragments?.size ?: 0
    }

}