package com.gkzxhn.legalconsulting.presenter

import android.content.Context
import com.gkzxhn.legalconsulting.entity.*
import com.gkzxhn.legalconsulting.model.IQualificationAuthenticationModel
import com.gkzxhn.legalconsulting.model.iml.QualificationAuthenticationModel
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.utils.ImageUtils
import com.gkzxhn.legalconsulting.utils.showToast
import com.gkzxhn.legalconsulting.view.QualificationAuthenticationEditView
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import rx.android.schedulers.AndroidSchedulers
import java.io.File
import java.util.LinkedHashMap
import kotlin.collections.ArrayList

/**
 * @author：liushaoxiang
 * @date：2018/10/30 11:56 AM
 * @description： * @classname：认证页面信息
 */

class QualificationAuthenticationEditPresenter(context: Context, view: QualificationAuthenticationEditView) : BasePresenter<IQualificationAuthenticationModel, QualificationAuthenticationEditView>(context, QualificationAuthenticationModel(), view) {
    val STRING_ID = "id"
    val STRING_64 = "base64"
    var map = LinkedHashMap<String, String>()

    fun send() {
        when {
            mView?.getAddress()?.isEmpty()!! -> mContext?.showToast("需填写真实地址")
            mView?.getName()?.isEmpty()!! -> mContext?.showToast("需填写真实姓名")
            mView?.getDescription()?.isEmpty()!! -> mContext?.showToast("请填写个人简介")
            mView?.getGender()?.isEmpty()!! -> mContext?.showToast("请填写完成再进行此操作")
            mView?.getLawOffice()?.isEmpty()!! -> mContext?.showToast("需填写执业机构")
            mView?.getProfessional()?.isEmpty()!! -> mContext?.showToast("请选择专业领域")
            mView?.getYear() == -1 -> mContext?.showToast("需填写职业年限")
            map[STRING_ID+1]==null -> mContext?.showToast("需上传律师执业证书")
            map[STRING_ID+2]==null -> mContext?.showToast("需上传律师年度考核")
            map[STRING_ID+3]==null -> mContext?.showToast("需上传身份证")
            map[STRING_ID+4]==null -> mContext?.showToast("需上传身份证")
            else -> certification()
        }


    }

    /**
     * @methodName： created by liushaoxiang on 2018/10/26 2:07 PM.
     * @description：上传文件
     */
    fun uploadFiles(file: File, position: Int) {
        val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        mContext?.let {
            mModel.uploadFiles(it, body)
                    .unsubscribeOn(AndroidSchedulers.mainThread())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(object : HttpObserver<UploadFile>(it) {
                        override fun success(date: UploadFile) {
                            date.id?.let {
                                val fileBase64Str = ImageUtils.imageToBase64(file.path)
                                if (fileBase64Str != null) {

                                    map[STRING_ID + position] = date.id
                                    map[STRING_64 + position] = fileBase64Str
                                }
                            }
                        }
                    })
        }
    }

    /**
     * @methodName ： created by liushaoxiang on 2018/11/1 10:43 AM.
     * @description ：  律师认证
     */
    private fun certification() {
        var qualificationAuthentication = QualificationAuthentication()
        var lawOfficeAddress: LawOfficeAddress? = LawOfficeAddress()

        var certificatePictures: CertificatePictures? = CertificatePictures()
        val certificatePicturesList: ArrayList<CertificatePictures>? = ArrayList()
        var categories: ArrayList<String>? = ArrayList()


        var assessmentPictures: AssessmentPictures? = AssessmentPictures()
        var assessmentPicturesList: ArrayList<AssessmentPictures>? = ArrayList()


        var identificationPictures: IdentificationPictures? = IdentificationPictures()
        var identificationPicturesList: ArrayList<IdentificationPictures>? = ArrayList()

        qualificationAuthentication.name = mView?.getName()
        qualificationAuthentication.gender = mView?.getGender()
        qualificationAuthentication.description = mView?.getDescription()
        qualificationAuthentication.level = "FIRST"
        qualificationAuthentication.lawOffice = mView?.getLawOffice()
        qualificationAuthentication.workExperience = mView?.getYear()!!


        var professional = mView?.getProfessional()
        for (s in professional.toString().split("、")) {
            when (s) {
                "财产纠纷" ->
                    categories?.add("TRAFFIC_ACCIDENT")
                "婚姻家庭" ->
                    categories?.add("TRAFFIC_ACCIDENT")
                "交通事故" ->
                    categories?.add("TRAFFIC_ACCIDENT")
                "工伤赔偿" -> categories?.add("TRAFFIC_ACCIDENT")

                "合同纠纷" -> categories?.add("TRAFFIC_ACCIDENT")

                "刑事辩护" -> categories?.add("TRAFFIC_ACCIDENT")

                "房产纠纷" -> categories?.add("TRAFFIC_ACCIDENT")

                "劳动就业" -> categories?.add("TRAFFIC_ACCIDENT")
            }

            //    TRAFFIC_ACCIDENT, CRIMINAL_DEFENSE, CONTRACT_DISPUTE,
//    WORK_COMPENSATION, PROPERTY_DISPUTES, LABOR_EMPLOYMENT,
//    HOUSING_DISPUTES, MARRIAGE_FAMILY];

        }
        qualificationAuthentication.categories = categories


        lawOfficeAddress?.id = "000"
        lawOfficeAddress?.cityName = "000"
        lawOfficeAddress?.cityCode = "000"
        lawOfficeAddress?.countryCode = "000"
        lawOfficeAddress?.countryName = "000"
        lawOfficeAddress?.provinceCode = "000"
        lawOfficeAddress?.provinceName = "000"
        lawOfficeAddress?.countyCode = "000"
        lawOfficeAddress?.countyName = "000"
        /****** 地址信息 ******/
        lawOfficeAddress?.streetDetail = mView?.getAddress()

        qualificationAuthentication.lawOfficeAddress = lawOfficeAddress

        /****** 证书图片 ******/
        certificatePictures?.id = "1"
        certificatePictures?.fileId = map[STRING_ID + 1]
        certificatePictures?.thumb = map[STRING_64 + 1]
        certificatePicturesList?.add(certificatePictures!!)
        certificatePicturesList?.add(certificatePictures!!)
        qualificationAuthentication.certificatePictures = certificatePicturesList

        /****** 评估照片 ******/
        assessmentPictures?.id = "2"
        assessmentPictures?.fileId = map[STRING_ID + 2]
        assessmentPictures?.thumb = map[STRING_64 + 2]
        assessmentPicturesList?.add(assessmentPictures!!)
        assessmentPicturesList?.add(assessmentPictures!!)
        qualificationAuthentication.assessmentPictures = assessmentPicturesList

        /****** 身份证 ******/
        identificationPictures?.id = "3"
        identificationPictures?.fileId = map[STRING_ID + 3]
        identificationPictures?.thumb = map[STRING_64 + 3]
        identificationPicturesList?.add(identificationPictures!!)
        identificationPictures?.id = "4"
        identificationPictures?.fileId = map[STRING_ID + 4]
        identificationPictures?.thumb = map[STRING_64 + 4]
        identificationPicturesList?.add(identificationPictures!!)
        qualificationAuthentication.identificationPictures = identificationPicturesList

        val dataJson = Gson().toJson(qualificationAuthentication)
        var Body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), dataJson)
        mModel.certification(mContext!!, Body)
                .unsubscribeOn(AndroidSchedulers.mainThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : HttpObserver<Response<Void>>(mContext!!) {
                    override fun success(t: Response<Void>) {
                        when (t.code()) {

                        }
                    }
                })
    }

}

