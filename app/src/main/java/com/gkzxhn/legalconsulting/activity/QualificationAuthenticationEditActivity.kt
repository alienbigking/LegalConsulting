package com.gkzxhn.legalconsulting.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.View
import com.afollestad.materialdialogs.GravityEnum
import com.afollestad.materialdialogs.MaterialDialog
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.presenter.QualificationAuthenticationEditPresenter
import com.gkzxhn.legalconsulting.utils.*
import com.gkzxhn.legalconsulting.view.QualificationAuthenticationEditView
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_qualification_authentication_edit.*
import java.io.File
import kotlinx.android.synthetic.main.activity_qualification_authentication_edit.tv_qualification_authentication_edit_send as send
import kotlinx.android.synthetic.main.default_top.iv_default_top_back as back
import kotlinx.android.synthetic.main.default_top.tv_default_top_title as topTitle

/**
 * Explanation: 资格认证信息填写页面
 * @author LSX
 *    -----2018/9/7
 */

class QualificationAuthenticationEditActivity : BaseActivity(), QualificationAuthenticationEditView {

    private val TAKE_PHOTO_IMAGE_1 = 101       //拍执业证书
    private val CHOOSE_PHOTO_IMAGE_1 = 102      //选择执业证书
    private val CROP_IMAGE_1 = 103     //智能裁剪执业证书

    private val TAKE_PHOTO_IMAGE_2 = 104       //拍年度考核证书
    private val CHOOSE_PHOTO_IMAGE_2 = 105      //选择年度考核证书
    private val CROP_IMAGE_2 = 106     //智能裁剪年度考核证书

    private val TAKE_PHOTO_IMAGE_3 = 107       //拍身份证正面
    private val CHOOSE_PHOTO_IMAGE_3 = 108      //选择身份证正面
    private val CROP_IMAGE_3 = 109     //智能裁剪身份证正面

    private val TAKE_PHOTO_IMAGE_4 = 110      //拍身份证背面
    private val CHOOSE_PHOTO_IMAGE_4 = 111      //选择身份证背面
    private val CROP_IMAGE_4 = 112     //智能裁剪身份证背面

    lateinit var photoDir: File

    var mTakePhotoUri: Uri? = null      //拍照uri

    lateinit var mPresenter: QualificationAuthenticationEditPresenter

    //    专业领域的集合
    var selectString: ArrayList<String> = arrayListOf()

    var provinceCode = ""
    var provinceName = ""
    var cityName = ""
    var cityCode = ""
    var countyCode = ""
    var countyName = ""
    /****** 详细地址 ******/
    var addressContent = ""

    override fun provideContentViewId(): Int {
        return R.layout.activity_qualification_authentication_edit
    }

    override fun init() {
        mPresenter = QualificationAuthenticationEditPresenter(this, this)
        topTitle.text = "资格认证"
        if (intent.getBooleanExtra("again_Authentication", false)) {
            /****** 重新认证时先获取信息 ******/
            mPresenter.getCertification()
            tv_qualification_authentication_top.text = "重新认证，直接修改编辑资料提交申请"
            tv_qualification_authentication_top2.visibility = View.GONE
        }

        back.setOnClickListener {
            finish()
        }

        send.setOnClickListener {
            mPresenter.send()
        }

        v_qualification_authentication_certificate_photos_bg.setOnClickListener {
            showListDialog("1.jpg", false, TAKE_PHOTO_IMAGE_1, CHOOSE_PHOTO_IMAGE_1)
        }
        v_qualification_authentication_record_photo_bg.setOnClickListener {
            showListDialog("2.jpg", false, TAKE_PHOTO_IMAGE_2, CHOOSE_PHOTO_IMAGE_2)
        }
        v_qualification_authentication_id1.setOnClickListener {
            showListDialog("3.jpg", false, TAKE_PHOTO_IMAGE_3, CHOOSE_PHOTO_IMAGE_3)
        }
        v_qualification_authentication_id2.setOnClickListener {
            showListDialog("4.jpg", false, TAKE_PHOTO_IMAGE_4, CHOOSE_PHOTO_IMAGE_4)
        }

        photoDir = File(externalCacheDir, "photo")
        if (!photoDir.exists()) {
            photoDir.mkdirs()
        }
    }

    fun onClickQualificationAuthentication(view: View) {
        when (view.id) {
        /****** 专业领域 ******/
            R.id.v_qualification_authentication_professional_field_bg -> {
                val intent = Intent(this, ChooseMajorsActivity::class.java)
                intent.putStringArrayListExtra(Constants.INTENT_SELECTSTRING, selectString)
                startActivityForResult(intent, Constants.REQUESTCODE_CHOOSE_MAJORS)
            }
        /****** 律所地址 ******/
            R.id.v_qualification_authentication_address_bg -> {
                val intent = Intent(this, EditAddressActivity::class.java)
                intent.putExtra("address", getAddress())
                intent.putExtra("cityName", cityName)
                intent.putExtra("countyName", countyName)
                intent.putExtra("provinceName", provinceName)
                startActivityForResult(intent, Constants.REQUESTCODE_CHOOSE_MAJORS)
            }
        /****** 律师等级 ******/
            R.id.v_qualification_authentication_level_bg -> {
                mPresenter.showDialog()
            }
        }
    }

    override fun onFinish() {
        finish()
    }

    override fun setSelectStr(selectStr: ArrayList<String>) {
        selectString = selectStr
    }

    override fun getSelectStr(): ArrayList<String> {
        return selectString
    }

    override fun setName(string: String) {
        et_qualification_authentication_name.setText(string)
    }

    override fun setGender(string: String) {
        var isMale = string == "MALE"
        rb_qualification_authentication_sex_man.isChecked = isMale
        rb_qualification_authentication_sex_woman.isChecked = !isMale
    }

    override fun setDescription(string: String) {
        et_qualification_authentication_personal_profile.setText(string)
    }

    override fun setLawOffice(string: String) {
        et_qualification_authentication_institution.setText(string)
    }

    @SuppressLint("SetTextI18n")
    override fun setAddress(provinceName: String, cityName: String, countyName: String, streetDetail: String) {
        tv_qualification_authentication_address_content.text = provinceName + cityName + countyName + streetDetail
        this.provinceName = provinceName
        this.cityName = cityName
        this.countyName = countyName
        this.addressContent = streetDetail
    }

    override fun setProfessional(string: String) {
        tv_qualification_authentication_professional_list.text = string
    }

    override fun setYear(string: String) {
        et_qualification_authentication_year.setText(string)
    }

    override fun setImage1(decodeFile: String) {
        ProjectUtils.loadImageByFileID(this,decodeFile,iv_qualification_authentication_certificate_photos_bg)
    }

    override fun setImage2(decodeFile: String) {
        ProjectUtils.loadImageByFileID(this,decodeFile,iv_qualification_authentication_record_photo_bg)
    }

    override fun setImage3(decodeFile: String) {
        ProjectUtils.loadImageByFileID(this,decodeFile,iv_qualification_authentication_id11)
    }

    override fun setImage4(decodeFile: String) {
        ProjectUtils.loadImageByFileID(this,decodeFile,iv_qualification_authentication_id22)
    }

    override fun getName(): String {
        return et_qualification_authentication_name.text.trim().toString()
    }

    override fun getGender(): String {
        return if (rb_qualification_authentication_sex_man.isChecked) {
            "MALE"
        } else "FEMALE"
    }

    override fun getDescription(): String {
        return et_qualification_authentication_personal_profile.text.trim().toString()
    }

    /****** 执业机构 ******/
    override fun getLawOffice(): String {
        return et_qualification_authentication_institution.text.trim().toString()
    }

    override fun getAddress(): String {
        val str = addressContent
        return if (str == getString(R.string.please_fill_in)) {
            ""
        } else str
    }

    override fun getCityname(): String {
        return cityName
    }

    override fun getCitycode(): String {
        return cityCode
    }

    override fun getCountycode(): String {
        return countyCode
    }

    override fun getCountyname(): String {
        return countyName
    }

    override fun getProvincecode(): String {
        return provinceCode
    }

    override fun getProvincename(): String {
        return provinceName
    }

    /****** 专业领域 ******/
    override fun getProfessional(): String {
        val str = tv_qualification_authentication_professional_list.text.trim().toString()
        return if (str == getString(R.string.please_select)) {
            ""
        } else str
    }

    override fun getYear(): Int {
        val year = et_qualification_authentication_year.text.trim().toString()
        return if (year.isEmpty()) {
            -1
        } else {
            year.toInt()
        }
    }

    override fun setLevel(levelStr: String) {
        tv_qualification_authentication_level_list.text = levelStr
    }


    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUESTCODE_CHOOSE_MAJORS) {
            when (resultCode) {
            /****** 专业领域返回 ******/
                Constants.RESULTCODE_CHOOSE_MAJORS -> {
                    selectString = data?.getStringArrayListExtra(Constants.RESULT_CHOOSE_MAJORS)!!
                    var professionalList = ""
                    if (selectString != null && (selectString as java.util.ArrayList<String>).isNotEmpty()) {
                        for (str: String in selectString as java.util.ArrayList<String>) {
                            professionalList = "$professionalList、$str"
                        }
                        tv_qualification_authentication_professional_list.text = professionalList.substring(1, professionalList.length)
                    }
                }
            /****** 律所地址返回 ******/
                Constants.RESULTCODE_EDIT_ADDRESS -> {
                    provinceCode = data?.getStringExtra(Constants.RESULT_EDIT_ADDRESS_PROVINCECODE).toString()
                    provinceName = data?.getStringExtra(Constants.RESULT_EDIT_ADDRESS_PROVINCENAME).toString()
                    cityName = data?.getStringExtra(Constants.RESULT_EDIT_ADDRESS_CITYNAME).toString()
                    cityCode = data?.getStringExtra(Constants.RESULT_EDIT_ADDRESS_CITYCODE).toString()
                    countyCode = data?.getStringExtra(Constants.RESULT_EDIT_ADDRESS_COUNTYCODE).toString()
                    countyName = data?.getStringExtra(Constants.RESULT_EDIT_ADDRESS_COUNTYNAME).toString()
                    addressContent = data?.getStringExtra(Constants.RESULT_EDIT_ADDRESS).toString()
                    setAddress(provinceName, cityName, countyName, addressContent)
                }
            }
        }

        if (resultCode == Activity.RESULT_OK) {
            mTakePhotoUri.toString().logE(this)
            when (requestCode) {
            /****** 拍照返回 ******/
                TAKE_PHOTO_IMAGE_1 -> {
//                    处理返回数据
                    mTakePhotoUri?.let { handleReturnData(it, CROP_IMAGE_1) }
                }
            /****** 拍照返回 ******/
                TAKE_PHOTO_IMAGE_2 -> {
                    mTakePhotoUri?.let { handleReturnData(it, CROP_IMAGE_2) }
                }
            /****** 拍照返回 ******/
                TAKE_PHOTO_IMAGE_3 -> {
                    mTakePhotoUri?.let { handleReturnData(it, CROP_IMAGE_3) }
                }
            /****** 拍照返回 ******/
                TAKE_PHOTO_IMAGE_4 -> {
                    mTakePhotoUri?.let { handleReturnData(it, CROP_IMAGE_4) }
                }
            /****** 选择图片返回 ******/
                CHOOSE_PHOTO_IMAGE_1 -> {
                    handleReturnData(data, CROP_IMAGE_1)
                }
            /****** 选择图片返回 ******/
                CHOOSE_PHOTO_IMAGE_2 -> {
                    handleReturnData(data, CROP_IMAGE_2)
                }
            /****** 选择图片返回 ******/
                CHOOSE_PHOTO_IMAGE_3 -> {
                    handleReturnData(data, CROP_IMAGE_3)
                }
            /****** 选择图片返回 ******/
                CHOOSE_PHOTO_IMAGE_4 -> {
                    handleReturnData(data, CROP_IMAGE_4)
                }
            /****** 智能裁剪 ******/
                CROP_IMAGE_1 -> {
                    val path = data!!.getStringExtra(Constants.CROP_PATH)
                    handleReturnData(path, 1)
                }
            /****** 智能裁剪 ******/
                CROP_IMAGE_2 -> {
                    val path = data!!.getStringExtra(Constants.CROP_PATH)
                    handleReturnData(path, 2)
                }
            /****** 智能裁剪 ******/
                CROP_IMAGE_3 -> {
                    val path = data!!.getStringExtra(Constants.CROP_PATH)
                    handleReturnData(path, 3)
                }
            /****** 智能裁剪 ******/
                CROP_IMAGE_4 -> {
                    val path = data!!.getStringExtra(Constants.CROP_PATH)
                    handleReturnData(path, 4)
                }
                else -> {

                }
            }
        }


    }


    /**
     * @methodName： created by liushaoxiang on 2018/12/10 11:58 AM.
     * @description：裁剪返回后的数据处理
     */
    private fun handleReturnData(path: String?, position: Int) {
        iv_qualification_authentication_certificate_photos_bg.setPadding(0, 0, 0, 0)
        mPresenter.uploadFiles(File(path), position)
    }

    /**
     * @methodName： created by liushaoxiang on 2018/12/10 11:58 AM.
     * @description：选择图片返回后的数据处理
     */
    private fun handleReturnData(data: Intent?, image: Int) {
        val file = FileUtils.getFileByUri(data!!.data, this)
        if (file?.exists()!!) {
            val bitmap = ImageUtils.decodeSampledBitmapFromFilePath(file.absolutePath, 720, 720)
            ImageUtils.compressImage(bitmap, file, 2000)!!
        }
        cropImage(data.data, image)
    }

    /**
     * @methodName： created by liushaoxiang on 2018/12/10 11:58 AM.
     * @description：拍照返回的数据处理
     */
    private fun handleReturnData(mTakePhotoUri: Uri, image: Int) {
        val file = ProjectUtils.uri2File(File(externalCacheDir, "photo"), mTakePhotoUri)
        val bitmap = ImageUtils.decodeSampledBitmapFromFilePath(file.absolutePath, 720, 720)
        ImageUtils.compressImage(bitmap, file, 2000)!!
        /****** 部分机型会自动旋转 这里旋转恢复 ******/
        val readPictureDegree = SystemUtil.readPictureDegree(file.absolutePath)
        SystemUtil.rotateBitmap(bitmap, readPictureDegree)
        cropImage(mTakePhotoUri, image)
    }

    /**
     * 弹出图片来源选择窗
     */
    private fun showListDialog(fileName: String, front: Boolean, type: Int, choose: Int) {
        MaterialDialog.Builder(this)
                .title(getString(R.string.please_choice_photo_from))
                .items(getString(R.string.take_photo), getString(R.string.photo_album))
                .itemsGravity(GravityEnum.START)
                .itemsCallback { dialog, itemView, position, text ->
                    when (position) {
                        0 -> {
                            //拍照
                            requestPermission(fileName, type, front)
                        }
                        1 -> {
                            //相册选择图片
                            requestPermission(fileName, choose)
                        }
                        else -> {
                        }
                    }
                }
                .show()
    }


    /****** 获取拍照权限  ******/
    fun requestPermission(fileName: String, requestCode: Int, front: Boolean) {
        if (Build.VERSION.SDK_INT >= 23) {
            RxPermissions(this)
                    .requestEach(Manifest.permission.CAMERA)
                    .subscribe { permission: Permission ->
                        if (permission.granted) {
                            // 用户已经同意该权限
                            takePhotoFromCamera(fileName, requestCode, front)
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            Log.d(javaClass.simpleName, permission.name + " is denied. More info should be provided.")
                            showToast(getString(R.string.please_agree_permission))
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            Log.d(javaClass.simpleName, permission.name + " is denied.")
                            showToast(getString(R.string.please_agree_permission))
                        }
                    }
        } else {
            takePhotoFromCamera(fileName, requestCode, front)
        }
    }

    /**
     * 相机拍照
     */
    private fun takePhotoFromCamera(fileName: String, requestCode: Int, front: Boolean) {
        val openCameraIntent = Intent(
                MediaStore.ACTION_IMAGE_CAPTURE)
        if (!photoDir.exists()) {
            photoDir.mkdirs()
        }
        val file = File(photoDir, fileName)
        if (Build.VERSION.SDK_INT >= 24) {
            mTakePhotoUri = FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
            openCameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            mTakePhotoUri = Uri.fromFile(file)
        }
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mTakePhotoUri)
        try {
            if (front) {
                //打开前置摄像头
                openCameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1) // 调用前置摄像头
            } else {
                openCameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 2) //
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        startActivityForResult(openCameraIntent, requestCode)
    }

    /**
     * @methodName： created by liushaoxiang on 2018/10/31 10:42 AM.
     * @description：获取相册选择图片权限
     */
    fun requestPermission(fileName: String, type: Int) {
        var storageFlag = 0
        RxPermissions(this)
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe({ permission: Permission ->
                    when {
                        permission.granted -> {
                            // 用户已经同意该权限
                            if (++storageFlag == 2) {
                                chooseAlbum(fileName, type)
                            }
                            Log.d(javaClass.simpleName, permission.name + " is granted.")
                        }
                        permission.shouldShowRequestPermissionRationale -> // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            Log.d(javaClass.simpleName, permission.name + " is denied. More info should be provided.")
                        else -> // 用户拒绝了该权限，并且选中『不再询问』
                            Log.d(javaClass.simpleName, permission.name + " is denied.")
                    }
                }, {
                    it.message.toString().logE(this)
                })
    }

    /**
     * 相册选择图片
     */
    private fun chooseAlbum(fileName: String, type: Int) {
        val openAlbumIntent = Intent(Intent.ACTION_PICK)
        openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果大于等于7.0使用FileProvider
            val mGalleryFile = File(File(externalCacheDir, "photo"), "$fileName.jpg")
            val uriForFile = FileProvider.getUriForFile(this, "$packageName.fileprovider", mGalleryFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivityForResult(Intent.createChooser(openAlbumIntent, "File Browser"), type)
    }

    /**
     * @methodName： created by liushaoxiang on 2018/10/24 1:58 PM.
     * @description：跳转到智能裁剪页面
     */
    private fun cropImage(uri: Uri?, requestCode: Int) {
        val intent = Intent(this, ImageCropActivity::class.java)
        intent.putExtra(Constants.INTENT_CROP_IMAGE_URI, uri)
        startActivityForResult(intent, requestCode)
    }

}
