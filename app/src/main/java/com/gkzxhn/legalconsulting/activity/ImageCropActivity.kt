package com.gkzxhn.legalconsulting.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Point
import android.net.Uri
import android.provider.MediaStore
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.common.Constants
import com.gkzxhn.legalconsulting.utils.ImageUtils
import com.gkzxhn.legalconsulting.utils.logE
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_image_crop.*
import java.io.File
import java.io.IOException

/**
 * @author：liushaoxiang
 * @date：2018/10/24 1:59 PM
 * @description：智能裁剪页面
 * #                                                   #
 * <p>
 */
class ImageCropActivity : BaseActivity() {
    override fun init() {
        initPoint()
        setOnClick()
    }

    override fun provideContentViewId(): Int {
        return R.layout.activity_image_crop
    }

    //图片高比
    private val ID_RATIO = 740f / 740f
    private var mProgressDialog: ProgressDialog? = null
    private lateinit var uri: Uri


    private fun setOnClick() {
        tv_save.setOnClickListener {
            mProgressDialog = ProgressDialog(this@ImageCropActivity)
            mProgressDialog!!.show()

            Observable.create<File> {
                var crop = iv_crop.crop()
                /****** 需要旋转  可以放开 ******/
                if (crop.width < crop.height) {
                    crop = rotateCrop(crop)
                }
                try {
                    val uriFile = File(uri.path)
                    val cacheDir = File(externalCacheDir, "photo")
                    val file = File(cacheDir, "${uriFile.nameWithoutExtension}_crop.jpg")
                    ImageUtils.compressImage(crop,file, 1000)
                    it.onNext(file)
                } catch (e: Exception) {
                    e.message!!.logE(this)
                }
            }
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        runOnUiThread {
                            mProgressDialog!!.dismiss()
                            val data = Intent()
                            data.putExtra(Constants.CROP_PATH, it.absolutePath)
                            setResult(Activity.RESULT_OK, data)
                            finish()
                        }
                    }, {
                        it.message!!.logE(this)
                    })
        }
    }

    private fun initPoint() {
        uri = intent.getParcelableExtra<Uri>(Constants.INTENT_CROP_IMAGE_URI)
        iv_crop.setImageURI(uri)
        val bitmap = iv_crop.bitmap
        try {
            iv_crop.setImageToCrop(MediaStore.Images.Media.getBitmap(contentResolver, uri))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val canRightCrop = iv_crop.canRightCrop()
        canRightCrop.toString().logE(this)

        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height
        val height = (bitmapWidth / ID_RATIO).toInt()
        val cropPoint = arrayOfNulls<Point>(4)
        val y = bitmapHeight / 4
        cropPoint[0] = Point(0, y)
        cropPoint[1] = Point(bitmapWidth, y)
        cropPoint[2] = Point(bitmapWidth, height + y)
        cropPoint[3] = Point(0, height + y)
        iv_crop.cropPoints = cropPoint
    }

    /**
     * 逆时针旋转图片
     * @param crop bitmap
     */
    private fun rotateCrop(crop: Bitmap): Bitmap {

        val matrix = Matrix()
        matrix.postRotate(-90f)

        return Bitmap.createBitmap(crop, 0, 0, crop.width, crop.height, matrix, true)

    }
}