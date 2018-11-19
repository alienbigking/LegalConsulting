package com.gkzxhn.legalconsulting.presenter

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.TextView
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.common.Constants
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
    var level = ""
    var levelStr = ""
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
            level.isEmpty() -> mContext?.showToast("需填写律师等级")
            map[STRING_ID + 1] == null -> mContext?.showToast("需上传律师执业证书")
            map[STRING_ID + 2] == null -> mContext?.showToast("需上传律师年度考核")
            map[STRING_ID + 3] == null -> mContext?.showToast("需上传身份证")
            map[STRING_ID + 4] == null -> mContext?.showToast("需上传身份证")
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
                        override fun success(t: UploadFile) {
                            t.id?.let {
                                val fileBase64Str = ImageUtils.imageToBase64(file.path)
                                if (fileBase64Str != null) {
                                    map[STRING_ID + position] = t.id
                                    /****** 与后台约定的前缀 ******/
                                    var startStr = Constants.BASE_64_START
                                    map[STRING_64 + position] = startStr + fileBase64Str
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
        var identificationPictures2: IdentificationPictures? = IdentificationPictures()
        var identificationPicturesList: ArrayList<IdentificationPictures>? = ArrayList()

        qualificationAuthentication.name = mView?.getName()
        qualificationAuthentication.gender = mView?.getGender()
        qualificationAuthentication.description = mView?.getDescription()
        qualificationAuthentication.level = level
        qualificationAuthentication.lawOffice = mView?.getLawOffice()
        qualificationAuthentication.workExperience = mView?.getYear()!!


        var professional = mView?.getProfessional()
        for (s in professional.toString().split("、")) {
            when (s) {
                "财产纠纷" -> categories?.add("PROPERTY_DISPUTES")
                "婚姻家庭" -> categories?.add("MARRIAGE_FAMILY")
                "交通事故" -> categories?.add("TRAFFIC_ACCIDENT")
                "工伤赔偿" -> categories?.add("WORK_COMPENSATION")

                "合同纠纷" -> categories?.add("CONTRACT_DISPUTE")

                "刑事辩护" -> categories?.add("CRIMINAL_DEFENSE")

                "房产纠纷" -> categories?.add("HOUSING_DISPUTES")

                "劳动就业" -> categories?.add("LABOR_EMPLOYMENT")
            }
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
        certificatePictures?.fileId = map[STRING_ID + 1]
        certificatePictures?.thumb = map[STRING_64 + 1]
        certificatePicturesList?.add(certificatePictures!!)
        qualificationAuthentication.certificatePictures = certificatePicturesList

        /****** 评估照片 ******/
        assessmentPictures?.fileId = map[STRING_ID + 2]
        assessmentPictures?.thumb = map[STRING_64 + 2]
        assessmentPicturesList?.add(assessmentPictures!!)
        qualificationAuthentication.assessmentPictures = assessmentPicturesList

        /****** 身份证 ******/
        identificationPictures?.fileId = map[STRING_ID + 3]
        identificationPictures?.thumb = map[STRING_64 + 3]
        identificationPicturesList?.add(identificationPictures!!)
        identificationPictures2?.fileId = map[STRING_ID + 4]
        identificationPictures2?.thumb = map[STRING_64 + 4]
        identificationPicturesList?.add(identificationPictures2!!)
        qualificationAuthentication.identificationPictures = identificationPicturesList

        val dataJson = Gson().toJson(qualificationAuthentication)
        val Body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), dataJson)
        mModel.certification(mContext!!, Body)
                .unsubscribeOn(AndroidSchedulers.mainThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : HttpObserver<Response<Void>>(mContext!!) {
                    override fun success(t: Response<Void>) {
                        when (t.code()) {
                            201 -> {
                                mContext?.showToast("提交成功")
                                mView?.onFinish()
                            }
                        }
                    }
                })
    }


    /**
     * @methodName： created by liushaoxiang on 2018/11/12 5:34 PM.
     * @description：显示律师等级的弹窗
     */
    fun showDialog() {
        var dialog = android.app.Dialog(mContext)//可以在style中设定dialog的样式
        dialog.setContentView(R.layout.dialog_level)
        var lp = dialog.window.attributes
        lp.gravity = Gravity.BOTTOM
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window.attributes = lp
        //设置该属性，dialog可以铺满屏幕
        dialog.window.setBackgroundDrawable(null)
        dialog.show()
        slideToUp(dialog.window.findViewById(R.id.cl_level_dialog_all))

        val tvOne = dialog.findViewById<TextView>(R.id.tv_level_dialog_one)
        val tvTwo = dialog.findViewById<TextView>(R.id.tv_level_dialog_two)
        val tvThird = dialog.findViewById<TextView>(R.id.tv_level_dialog_third)
        val tvFour = dialog.findViewById<TextView>(R.id.tv_level_dialog_four)

        val ivBack = dialog.findViewById<ImageView>(R.id.iv_dialog_back)
        val ivOne = dialog.findViewById<ImageView>(R.id.iv_level_one)
        val ivTwo = dialog.findViewById<ImageView>(R.id.iv_level_two)
        val ivThird = dialog.findViewById<ImageView>(R.id.iv_level_third)
        val ivFour = dialog.findViewById<ImageView>(R.id.iv_level_four)


        val tvVerify = dialog.findViewById<TextView>(R.id.tv_level_verify)
        tvOne.setOnClickListener {
            checkSelect(ivOne, ivTwo, ivThird, ivFour, 1)
        }
        tvTwo.setOnClickListener {
            checkSelect(ivOne, ivTwo, ivThird, ivFour, 2)
        }
        tvThird.setOnClickListener {
            checkSelect(ivOne, ivTwo, ivThird, ivFour, 3)
        }
        tvFour.setOnClickListener {
            checkSelect(ivOne, ivTwo, ivThird, ivFour, 4)
        }
        tvVerify.setOnClickListener {
            dialog.dismiss()
            mView?.setLevel(levelStr)
        }
        ivBack.setOnClickListener {
            dialog.dismiss()
        }

    }

    /**
     * @methodName： created by liushaoxiang on 2018/11/13 9:45 AM.
     * @description：点击律师 等级条目 切换选择状态并保存律师等级string
     */
    private fun checkSelect(ivOne: ImageView, ivTwo: ImageView, ivThird: ImageView, ivFour: ImageView, i: Int) {
        ivOne.setImageResource(R.mipmap.ic_checked_false)
        ivTwo.setImageResource(R.mipmap.ic_checked_false)
        ivThird.setImageResource(R.mipmap.ic_checked_false)
        ivFour.setImageResource(R.mipmap.ic_checked_false)
        when (i) {
            1 -> {
                ivOne.setImageResource(R.mipmap.ic_checked_true)
                levelStr = "一级律师 (高级律师)"
                level = "FIRST"
            }

            2 -> {
                ivTwo.setImageResource(R.mipmap.ic_checked_true)
                levelStr = "二级律师 (副高级律师)"
                level = "SECOND"
            }
            3 -> {
                ivThird.setImageResource(R.mipmap.ic_checked_true)
                levelStr = "三级律师 (中级律师)"
                level = "THIRD"
            }
            4 -> {
                ivFour.setImageResource(R.mipmap.ic_checked_true)
                levelStr = "四级律师 (初级律师)"
                level = "FOURTH"
            }
        }
    }

    /**
     * @methodName： created by liushaoxiang on 2018/11/12 5:34 PM.
     * @description：弹窗的动画
     */
    private fun slideToUp(view: View) {
        var slide = TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f)

        slide.duration = 400
        slide.fillAfter = true
        slide.isFillEnabled = true
        view.startAnimation(slide)
    }


}

