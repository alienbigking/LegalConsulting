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
import com.gkzxhn.legalconsulting.common.App
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.customview.ClipViewLayout.getRealFilePathFromUri
import com.gkzxhn.legalconsulting.entity.UploadFile
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.net.RetrofitClient
import com.gkzxhn.legalconsulting.net.RetrofitClientLogin
import com.gkzxhn.legalconsulting.utils.*
import com.google.gson.Gson
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_user_setting.*
import kotlinx.android.synthetic.main.default_top.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import java.util.*

/**
 * @classname：UserSettingActivity
 * @author：liushaoxiang
 * @date：2018/10/12 3:52 PM
 * @description：个人账号设置
 */
class UserSettingActivity : BaseActivity() {


    private val TAKE_PHOTO_IMAGE = 101       //拍头像
    private val CHOOSE_PHOTO_IMAGE = 102      //选择头像
    private val CROP_IMAGE = 103     //智能裁剪头像
    private val REQUEST_CROP_PHOTO = 104     //简单裁剪头像
    lateinit var photoDir: File
    var mTakePhotoUri: Uri? = null      //拍照uri
    private var name = ""
    private var phoneNumber = ""

    override fun provideContentViewId(): Int {
        return R.layout.activity_user_setting
    }

    override fun init() {
        initTopTitle()
        ProjectUtils.addViewTouchChange(tv_user_setting_change_phone)
        name = intent.getStringExtra("name")
        phoneNumber = intent.getStringExtra("phoneNumber")
        tv_user_setting_change_name.text = name
        val avatarStr = App.SP?.getString(Constants.SP_AVATARFILE, "")
        if (avatarStr?.isNotEmpty()!!) {
            val decodeFile = BitmapFactory.decodeFile(avatarStr)
            iv_user_setting_image.setImageBitmap(decodeFile)
        }
        tv_user_setting_phone.text = StringUtils.phoneChange(phoneNumber)


        photoDir = File(externalCacheDir, "photo")
        if (!photoDir.exists()) {
            photoDir.mkdirs()
        }
    }

    private fun initTopTitle() {
        tv_default_top_title.text = "个人账号"
        iv_default_top_back.setOnClickListener {
            finish()
        }
    }


    fun onClickUserSetting(view: View) {
        when (view.id) {

        /****** 个人头像 ******/
            R.id.v_user_setting_photo_bg -> {
                showListDialog("id_head.jpg", false)

            }
        /****** 更换手机号 ******/
            R.id.tv_user_setting_change_phone -> {
                var intent = Intent(this, ChangePhoneFirstActivity::class.java)
                intent.putExtra("phoneNumber", phoneNumber)
                startActivity(intent)
                finish()
            }
        }
    }


    /**
     * 弹出图片来源选择窗
     */
    private fun showListDialog(fileName: String, front: Boolean) {
        MaterialDialog.Builder(this)
                .title(getString(R.string.please_choice_photo_from))
                .items(getString(R.string.take_photo), getString(R.string.photo_album))
                .itemsGravity(GravityEnum.CENTER)
                .itemsCallback { dialog, itemView, position, text ->
                    when (position) {
                        0 -> {
                            //拍照
                            requestPermission(fileName, TAKE_PHOTO_IMAGE, front)
                        }
                        1 -> {
                            val openAlbumIntent = Intent(Intent.ACTION_PICK)
                            openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果大于等于7.0使用FileProvider
                                val mGalleryFile = File(File(externalCacheDir, "photo"), "12345.jpg")
                                val uriForFile = FileProvider.getUriForFile(this, "${packageName}.fileprovider", mGalleryFile)
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile)
                                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            startActivityForResult(openAlbumIntent, CHOOSE_PHOTO_IMAGE)
                        }
                        else -> {
                        }
                    }
                }
                .show()
    }

    /****** 开始拍照  ******/
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
            mTakePhotoUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", file)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            mTakePhotoUri.toString().logE(this)
            when (requestCode) {
                TAKE_PHOTO_IMAGE -> {
                    val file = uri2File(File(externalCacheDir, "photo"), mTakePhotoUri!!)
                    val bitmap = ImageUtils.decodeSampledBitmapFromFilePath(file.absolutePath, 720, 720)
                    bitmap.compressImage(file, 2000)!!
//                    cropImage(mTakePhotoUri, CROP_IMAGE)

                    val readPictureDegree = SystemUtil.readPictureDegree(file.absolutePath)
                    SystemUtil.rotateBitmap(bitmap, readPictureDegree)
                    gotoClipActivity(Uri.fromFile(file))
                }
                CHOOSE_PHOTO_IMAGE -> {
                    val file = FileUtils.getFileByUri(data!!.data, this)
                    val bitmap = ImageUtils.decodeSampledBitmapFromFilePath(file!!.absolutePath, 720, 720)
                    bitmap.compressImage(file, 2000)!!
//                    cropImage(data.data, CROP_IMAGE)
                    gotoClipActivity(data.data)

                }
                CROP_IMAGE -> {
                    val path = data!!.getStringExtra(Constants.CROP_PATH)
                    iv_user_setting_image.setPadding(0, 0, 0, 0)
                    val bitmap = BitmapFactory.decodeFile(path)
                    iv_user_setting_image.setImageBitmap(bitmap)
//                    idFrontFile = bitmap.compressImage(File(path), 1000)
                }
                REQUEST_CROP_PHOTO -> {
                    if (resultCode == Activity.RESULT_OK) {
                        if (data?.data == null) {
                            return
                        }
                        var uri = data.data
                        val cropImagePath = getRealFilePathFromUri(applicationContext, uri)
                        val bitMap = BitmapFactory.decodeFile(cropImagePath)
                        iv_user_setting_image.setImageBitmap(bitMap)
                        //此处后面可以将bitMap转为二进制上传后台网络
                        uploadFiles(File(cropImagePath))

                    }
                }
                else -> {
                }
            }
        }
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


    private fun uri2File(cacheDir: File, uri: Uri): File {
        val uriFile = File(uri.path)
        return File(cacheDir, uriFile.name)
    }

    /**
     * @methodName： created by liushaoxiang on 2018/10/24 1:58 PM.
     * @description：跳转到普通裁剪页面
     */
    private fun gotoClipActivity(uri: Uri?) {
        if (uri == null) {
            return
        }
        val intent = Intent()
        intent.setClass(this, ClipImageActivity::class.java)

        /****** 一为圆形 二为方形 ******/
        intent.putExtra("type", 2)
        intent.data = uri
        startActivityForResult(intent, REQUEST_CROP_PHOTO)
    }


    /**
     * @methodName： created by liushaoxiang on 2018/10/26 2:06 PM.
     * @description：上传头像
     */
    private fun uploadAvatar(fileId: String, thumb: String) {
        var map = LinkedHashMap<String, String>()
        map["fileId"] = fileId
        map["thumb"] = thumb
        var body2 = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                Gson().toJson(map))
        RetrofitClient.getInstance(this).mApi?.uploadAvatar(body2)
                ?.subscribeOn(Schedulers.io())
                ?.unsubscribeOn(AndroidSchedulers.mainThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : HttpObserver<Response<Void>>(this) {
                    override fun success(date: Response<Void>) {
                        if (date.code() == 200) {
                            showToast("上传成功")
                        }
                    }

                })
    }

    /**
     * @methodName： created by liushaoxiang on 2018/10/26 2:07 PM.
     * @description：上传文件
     */
    private fun uploadFiles(file: File) {
        val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        RetrofitClientLogin.getInstance(this).mApi?.uploadFiles(body)
                ?.subscribeOn(Schedulers.io())
                ?.unsubscribeOn(AndroidSchedulers.mainThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : HttpObserver<UploadFile>(this) {
                    override fun success(date: UploadFile) {
                        date.id?.let {
                            //                            downloadFile(it)
                            val fileBase64Str = ImageUtils.imageToBase64(file.path)
                            if (fileBase64Str != null) {
                                uploadAvatar(date.id, fileBase64Str)
                            }
                        }
                    }
                })
    }

    /**
     * @methodName： created by liushaoxiang on 2018/10/26 2:06 PM.
     * @description： 下载文件
     */
    private fun downloadFile(id: String) {
        RetrofitClientLogin.getInstance(this).mApi?.downloadFile(id)
                ?.subscribeOn(Schedulers.io())
                ?.unsubscribeOn(AndroidSchedulers.mainThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : HttpObserver<Response<Void>>(this) {
                    override fun success(date: Response<Void>) {

                    }
                })
    }

}

