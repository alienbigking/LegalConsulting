package com.gkzxhn.legalconsulting.presenter

import android.content.Context
import android.graphics.BitmapFactory
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.entity.QualificationAuthentication
import com.gkzxhn.legalconsulting.model.IQualificationAuthenticationModel
import com.gkzxhn.legalconsulting.model.iml.QualificationAuthenticationModel
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.utils.ImageUtils
import com.gkzxhn.legalconsulting.view.QualificationAuthenticationShowView
import rx.android.schedulers.AndroidSchedulers
import java.io.File

/**
 * @author：liushaoxiang
 * @date：2018/10/30 11:56 AM
 * @description： * @classname：认证页面信息
 */

class QualificationAuthenticationShowPresenter(context: Context, view: QualificationAuthenticationShowView) : BasePresenter<IQualificationAuthenticationModel, QualificationAuthenticationShowView>(context, QualificationAuthenticationModel(), view) {

    fun getCertification() {
        mContext?.let {
            mModel.getCertification(it)
                    .unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<QualificationAuthentication>(it) {
                        override fun success(t: QualificationAuthentication) {
                            mView?.setName(t.name!!)
                            mView?.setGender(t.gender!!)
                            mView?.setDescription(t.description!!)
                            mView?.setAddress(t.lawOfficeAddress!!.streetDetail!!)
                            mView?.setLawOffice(t.lawOffice!!)
                            var professional = ""
                            for (s in t.categories!!) {
                                when (s) {
                                    "PROPERTY_DISPUTES" -> professional = "$professional、财产纠纷"
                                    "MARRIAGE_FAMILY" -> professional = "$professional、婚姻家庭"
                                    "TRAFFIC_ACCIDENT" -> professional = "$professional、交通事故"
                                    "WORK_COMPENSATION" -> professional = "$professional、工伤赔偿"
                                    "CONTRACT_DISPUTE" -> professional = "$professional、合同纠纷"
                                    "CRIMINAL_DEFENSE" -> professional = "$professional、刑事辩护"
                                    "HOUSING_DISPUTES" -> professional = "$professional、房产纠纷"
                                    "LABOR_EMPLOYMENT" -> professional = "$professional、劳动就业"
                                }
                            }
                            mView?.setProfessional(professional.substring(1))
                            mView?.setYear(t.workExperience.toString() + "年")

                            val file = File(mContext?.cacheDir, "pa_show_1" + ".jpg")
                            val certificatePictures = t.certificatePictures!![0].thumb.toString()
                            if (certificatePictures.isNotEmpty()) {
                                val base64ToFile = ImageUtils.base64ToFile(certificatePictures.substring(Constants.BASE_64_START.length), file.absolutePath)
                                if (base64ToFile) {
                                    val decodeFile = BitmapFactory.decodeFile(file.absolutePath)
                                    mView?.setImage1(decodeFile)
                                }
                            }

                            val file2 = File(mContext?.cacheDir, "pa_show_2" + ".jpg")
                            val assessmentPictures = t.assessmentPictures!![0].thumb.toString()
                            if (assessmentPictures.isNotEmpty()) {
                                val base64ToFile = ImageUtils.base64ToFile(assessmentPictures.substring(Constants.BASE_64_START.length), file2.absolutePath)
                                if (base64ToFile) {
                                    val decodeFile = BitmapFactory.decodeFile(file2.absolutePath)
                                    mView?.setImage2(decodeFile)
                                }
                            }
                            val file3 = File(mContext?.cacheDir, "pa_show_3" + ".jpg")
                            val identificationPictures1 = t.identificationPictures!![0].thumb.toString()
                            if (identificationPictures1.isNotEmpty()) {
                                val base64ToFile = ImageUtils.base64ToFile(identificationPictures1.substring(Constants.BASE_64_START.length), file3.absolutePath)
                                if (base64ToFile) {
                                    val decodeFile = BitmapFactory.decodeFile(file3.absolutePath)
                                    mView?.setImage3(decodeFile)
                                }
                            }
                            val file4 = File(mContext?.cacheDir, "pa_show_4" + ".jpg")
                            val identificationPictures2 = t.identificationPictures!![1].thumb.toString()
                            if (identificationPictures2.isNotEmpty()) {
                                val base64ToFile = ImageUtils.base64ToFile(identificationPictures2.substring(Constants.BASE_64_START.length), file4.absolutePath)
                                if (base64ToFile) {
                                    val decodeFile = BitmapFactory.decodeFile(file4.absolutePath)
                                    mView?.setImage4(decodeFile)
                                }
                            }
                        }
                    })
        }
    }

}