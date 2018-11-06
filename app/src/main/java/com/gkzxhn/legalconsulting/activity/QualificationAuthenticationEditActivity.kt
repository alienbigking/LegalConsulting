package com.gkzxhn.legalconsulting.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
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
import com.gkzxhn.legalconsulting.common.Constants.REQUESTCODE_CHOOSE_MAJORS
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
    override fun onFinish() {
        finish()
    }


    override fun getName(): String {
        return et_qualification_authentication_name.text.trim().toString()
    }

    override fun getGender(): String {
        return if (rb_qualification_authentication_sex_man.isChecked)
            "MALE"
        else "FEMALE"
    }

    override fun getDescription(): String {
        return et_qualification_authentication_personal_profile.text.trim().toString()
    }

    /****** 执业机构 ******/
    override fun getLawOffice(): String {
        return et_qualification_authentication_institution.text.trim().toString()
    }

    override fun getAddress(): String {
        val str = tv_qualification_authentication_address_content.text.trim().toString()
        return if (str == getString(R.string.please_fill_in))
            "" else str
    }

    /****** 专业领域 ******/
    override fun getProfessional(): String {
        val str = tv_qualification_authentication_professional_list.text.trim().toString()
        return if (str == getString(R.string.please_select))
            "" else str
    }

    override fun getYear(): Int {
        val year = et_qualification_authentication_year.text.trim().toString()
        return if (year.isEmpty()) {
            -1
        }else{
            year.toInt()
        }
    }


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
    var selectString: ArrayList<String>? = arrayListOf()

    override fun provideContentViewId(): Int {
        return R.layout.activity_qualification_authentication_edit
    }

    override fun init() {
        mPresenter = QualificationAuthenticationEditPresenter(this, this)
        topTitle.text = "资格认证"
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
                startActivityForResult(intent, REQUESTCODE_CHOOSE_MAJORS)
            }
        /****** 律所地址 ******/
            R.id.v_qualification_authentication_address_bg -> {
                val intent = Intent(this, EditAddressActivity::class.java)
                intent.putExtra("address",getAddress())
                startActivityForResult(intent, REQUESTCODE_CHOOSE_MAJORS)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUESTCODE_CHOOSE_MAJORS) {
            when (resultCode) {
            /****** 专业领域返回 ******/
                Constants.RESULTCODE_CHOOSE_MAJORS -> {
                    selectString = data?.getStringArrayListExtra(Constants.RESULT_CHOOSE_MAJORS)
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
                    val addressContent = data?.getStringExtra(Constants.RESULT_EDIT_ADDRESS)
                    tv_qualification_authentication_address_content.text = addressContent
                }
            }
        }

        if (resultCode == Activity.RESULT_OK) {
            mTakePhotoUri.toString().logE(this)
            when (requestCode) {
            /****** 拍照返回 ******/
                TAKE_PHOTO_IMAGE_1 -> {
                    val file = ProjectUtils.uri2File(File(externalCacheDir, "photo"), mTakePhotoUri!!)
                    val bitmap = ImageUtils.decodeSampledBitmapFromFilePath(file.absolutePath, 720, 720)
                    bitmap.compressImage(file, 2000)!!
                    /****** 部分机型会自动旋转 这里旋转恢复 ******/
                    val readPictureDegree = SystemUtil.readPictureDegree(file.absolutePath)
                    SystemUtil.rotateBitmap(bitmap, readPictureDegree)

                    cropImage(mTakePhotoUri, CROP_IMAGE_1)

                }
            /****** 拍照返回 ******/
                TAKE_PHOTO_IMAGE_2 -> {
                    val file = ProjectUtils.uri2File(File(externalCacheDir, "photo"), mTakePhotoUri!!)
                    val bitmap = ImageUtils.decodeSampledBitmapFromFilePath(file.absolutePath, 720, 720)
                    bitmap.compressImage(file, 2000)!!
                    /****** 部分机型会自动旋转 这里旋转恢复 ******/
                    val readPictureDegree = SystemUtil.readPictureDegree(file.absolutePath)
                    SystemUtil.rotateBitmap(bitmap, readPictureDegree)

                    cropImage(mTakePhotoUri, CROP_IMAGE_2)

                }
            /****** 拍照返回 ******/
                TAKE_PHOTO_IMAGE_3 -> {
                    val file = ProjectUtils.uri2File(File(externalCacheDir, "photo"), mTakePhotoUri!!)
                    val bitmap = ImageUtils.decodeSampledBitmapFromFilePath(file.absolutePath, 720, 720)
                    bitmap.compressImage(file, 2000)!!
                    /****** 部分机型会自动旋转 这里旋转恢复 ******/
                    val readPictureDegree = SystemUtil.readPictureDegree(file.absolutePath)
                    SystemUtil.rotateBitmap(bitmap, readPictureDegree)

                    cropImage(mTakePhotoUri, CROP_IMAGE_3)

                }
            /****** 拍照返回 ******/
                TAKE_PHOTO_IMAGE_4 -> {
                    val file = ProjectUtils.uri2File(File(externalCacheDir, "photo"), mTakePhotoUri!!)
                    val bitmap = ImageUtils.decodeSampledBitmapFromFilePath(file.absolutePath, 720, 720)
                    bitmap.compressImage(file, 2000)!!
                    /****** 部分机型会自动旋转 这里旋转恢复 ******/
                    val readPictureDegree = SystemUtil.readPictureDegree(file.absolutePath)
                    SystemUtil.rotateBitmap(bitmap, readPictureDegree)

                    cropImage(mTakePhotoUri, CROP_IMAGE_4)

                }
            /****** 选择图片返回 ******/
                CHOOSE_PHOTO_IMAGE_1 -> {
                    val file = FileUtils.getFileByUri(data!!.data, this)
                    if (file?.exists()!!) {
                        val bitmap = ImageUtils.decodeSampledBitmapFromFilePath(file.absolutePath, 720, 720)
                        bitmap.compressImage(file, 2000)!!
                    }
                    cropImage(data.data, CROP_IMAGE_1)
                }
            /****** 选择图片返回 ******/
                CHOOSE_PHOTO_IMAGE_2 -> {
                    val file = FileUtils.getFileByUri(data!!.data, this)
                    if (file?.exists()!!) {
                        val bitmap = ImageUtils.decodeSampledBitmapFromFilePath(file.absolutePath, 720, 720)
                        bitmap.compressImage(file, 2000)!!
                    }
                    cropImage(data.data, CROP_IMAGE_2)
                }
            /****** 选择图片返回 ******/
                CHOOSE_PHOTO_IMAGE_3 -> {
                    val file = FileUtils.getFileByUri(data!!.data, this)
                    if (file?.exists()!!) {
                        val bitmap = ImageUtils.decodeSampledBitmapFromFilePath(file.absolutePath, 720, 720)
                        bitmap.compressImage(file, 2000)!!
                    }
                    cropImage(data.data, CROP_IMAGE_3)
                }
            /****** 选择图片返回 ******/
                CHOOSE_PHOTO_IMAGE_4 -> {
                    val file = FileUtils.getFileByUri(data!!.data, this)
                    if (file?.exists()!!) {
                        val bitmap = ImageUtils.decodeSampledBitmapFromFilePath(file.absolutePath, 720, 720)
                        bitmap.compressImage(file, 2000)!!
                    }
                    cropImage(data.data, CROP_IMAGE_4)
                }
            /****** 智能裁剪 ******/
                CROP_IMAGE_1 -> {
                    val path = data!!.getStringExtra(Constants.CROP_PATH)
                    iv_qualification_authentication_certificate_photos_bg.setPadding(0, 0, 0, 0)
                    val bitmap = BitmapFactory.decodeFile(path)
                    iv_qualification_authentication_certificate_photos_bg.setImageBitmap(bitmap)
                    mPresenter.uploadFiles(File(path),1)
                }
            /****** 智能裁剪 ******/
                CROP_IMAGE_2 -> {
                    val path = data!!.getStringExtra(Constants.CROP_PATH)
                    iv_qualification_authentication_record_photo_bg.setPadding(0, 0, 0, 0)
                    val bitmap = BitmapFactory.decodeFile(path)
                    iv_qualification_authentication_record_photo_bg.setImageBitmap(bitmap)
                    mPresenter.uploadFiles(File(path),2)
                }
            /****** 智能裁剪 ******/
                CROP_IMAGE_3 -> {
                    val path = data!!.getStringExtra(Constants.CROP_PATH)
                    iv_qualification_authentication_id11.setPadding(0, 0, 0, 0)
                    val bitmap = BitmapFactory.decodeFile(path)
                    iv_qualification_authentication_id11.setImageBitmap(bitmap)
                    mPresenter.uploadFiles(File(path),3)
                }
            /****** 智能裁剪 ******/
                CROP_IMAGE_4 -> {
                    val path = data!!.getStringExtra(Constants.CROP_PATH)
                    iv_qualification_authentication_id22.setPadding(0, 0, 0, 0)
                    val bitmap = BitmapFactory.decodeFile(path)
                    iv_qualification_authentication_id22.setImageBitmap(bitmap)
                    mPresenter.uploadFiles(File(path),4)
                }
                else -> {

                }
            }
        }


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
                            Log.d(javaClass.simpleName, permission.name + " is denied. More info should be provided.");
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
                openCameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1); // 调用前置摄像头
            } else {
                openCameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 2); //
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
                    if (permission.granted) {
                        // 用户已经同意该权限
                        if (++storageFlag == 2) {
                            chooseAlbum(fileName, type)
                        }
                        Log.d(javaClass.simpleName, permission.name + " is granted.")
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                        Log.d(javaClass.simpleName, permission.name + " is denied. More info should be provided.");
                    } else {
                        // 用户拒绝了该权限，并且选中『不再询问』
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
        openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
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
