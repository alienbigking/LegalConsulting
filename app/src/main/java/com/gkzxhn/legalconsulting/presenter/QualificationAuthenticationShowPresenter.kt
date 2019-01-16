package com.gkzxhn.legalconsulting.presenter

import android.content.Context
import com.gkzxhn.legalconsulting.entity.QualificationAuthentication
import com.gkzxhn.legalconsulting.model.IQualificationAuthenticationModel
import com.gkzxhn.legalconsulting.model.iml.QualificationAuthenticationModel
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.utils.ProjectUtils
import com.gkzxhn.legalconsulting.view.QualificationAuthenticationShowView
import rx.android.schedulers.AndroidSchedulers

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
                                val categoriesString = ProjectUtils.categoriesConversion(s)
                                professional = "$professional、$categoriesString"
                            }
                            mView?.setProfessional(professional.substring(1))
                            mView?.setYear(t.workExperience.toString() + "年")

                            val certificatePictures = t.certificatePictures!![0].thumbFileId.toString()
                            if (certificatePictures.isNotEmpty()) {
                                mView?.setImage1(certificatePictures)
                            }

                            val assessmentPictures = t.assessmentPictures!![0].thumbFileId.toString()
                            if (assessmentPictures.isNotEmpty()) {
                                mView?.setImage2(assessmentPictures)
                            }
                            val identificationPictures1 = t.identificationPictures!![0].thumbFileId.toString()
                            if (identificationPictures1.isNotEmpty()) {
                                mView?.setImage3(identificationPictures1)
                            }
                            val identificationPictures2 = t.identificationPictures!![1].thumbFileId.toString()
                            if (identificationPictures2.isNotEmpty()) {
                                mView?.setImage4(identificationPictures2)
                            }
                        }
                    })
        }
    }

}