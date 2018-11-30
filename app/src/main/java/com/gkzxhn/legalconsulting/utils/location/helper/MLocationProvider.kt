package com.gkzxhn.legalconsulting.utils.location.helper

import android.content.Context
import android.content.Intent
import android.location.Criteria
import android.location.LocationManager
import android.provider.Settings
import android.text.TextUtils
import com.gkzxhn.legalconsulting.utils.location.activity.LocationAmapActivity
import com.gkzxhn.legalconsulting.utils.location.activity.LocationExtras
import com.gkzxhn.legalconsulting.utils.location.activity.NavigationAmapActivity
import com.netease.nim.uikit.api.model.location.LocationProvider
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialog
import com.netease.nim.uikit.common.util.log.LogUtil

class MLocationProvider : LocationProvider {
    override fun requestLocation(context: Context?, callback: LocationProvider.Callback?) {
        if (!isLocationEnable(context!!)) {
            val alertDialog = EasyAlertDialog(context)
            alertDialog.setMessage("位置服务未开启")
            alertDialog.addNegativeButton("取消", EasyAlertDialog.NO_TEXT_COLOR, EasyAlertDialog.NO_TEXT_SIZE.toFloat()
            ) { alertDialog.dismiss() }
            alertDialog.addPositiveButton("设置", EasyAlertDialog.NO_TEXT_COLOR, EasyAlertDialog.NO_TEXT_SIZE.toFloat()
            ) {
                alertDialog.dismiss()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                try {
                    context.startActivity(intent)
                } catch (e: Exception) {
                    LogUtil.e("LOC", "start ACTION_LOCATION_SOURCE_SETTINGS error")
                }
            }
            alertDialog.show()
            return
        }

        LocationAmapActivity.start(context, callback)
    }

    override fun openMap(context: Context?, longitude: Double, latitude: Double, address: String?) {
        val intent = Intent(context, NavigationAmapActivity::class.java)
        intent.putExtra(LocationExtras.LONGITUDE, longitude)
        intent.putExtra(LocationExtras.LATITUDE, latitude)
        intent.putExtra(LocationExtras.ADDRESS, address)
        context?.startActivity(intent)
    }


    fun isLocationEnable(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val cri = Criteria()
        cri.accuracy = Criteria.ACCURACY_COARSE
        cri.isAltitudeRequired = false
        cri.isBearingRequired = false
        cri.isCostAllowed = false
        val bestProvider = locationManager.getBestProvider(cri, true)
        return !TextUtils.isEmpty(bestProvider)
    }
}
