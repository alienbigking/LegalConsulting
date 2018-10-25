package com.gkzxhn.legalconsulting.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.customview.ClipViewLayout

import java.io.File
import java.io.IOException
import java.io.OutputStream

/**
 * 普通裁剪Activity
 */
class ClipImageActivity : AppCompatActivity(), View.OnClickListener {
    private var clipViewLayout1: ClipViewLayout? = null
    private var clipViewLayout2: ClipViewLayout? = null
    private var back: ImageView? = null
    private var btnCancel: TextView? = null
    private var btnOk: TextView? = null
    //类别 1: qq, 2: weixin
    private var type: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clip_image)
        type = intent.getIntExtra("type", 1)
        initView()
    }

    /**
     * 初始化组件
     */
    fun initView() {
        clipViewLayout1 = findViewById<View>(R.id.clipViewLayout1) as ClipViewLayout
        clipViewLayout2 = findViewById<View>(R.id.clipViewLayout2) as ClipViewLayout
        back = findViewById<View>(R.id.iv_back) as ImageView
        btnCancel = findViewById<View>(R.id.btn_cancel) as TextView
        btnOk = findViewById<View>(R.id.bt_ok) as TextView
        //设置点击事件监听器
        back!!.setOnClickListener(this)
        btnCancel!!.setOnClickListener(this)
        btnOk!!.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        if (type == 1) {
            clipViewLayout1!!.visibility = View.VISIBLE
            clipViewLayout2!!.visibility = View.GONE
            //设置图片资源
            clipViewLayout1!!.setImageSrc(intent.data)
        } else {
            clipViewLayout2!!.visibility = View.VISIBLE
            clipViewLayout1!!.visibility = View.GONE
            //设置图片资源
            clipViewLayout2!!.setImageSrc(intent.data)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_back -> finish()
            R.id.btn_cancel -> finish()
            R.id.bt_ok -> generateUriAndReturn()
        }
    }

    /**
     * 生成Uri并且通过setResult返回给打开的activity
     */
    private fun generateUriAndReturn() {
        //调用返回剪切图
        val zoomedCropBitmap: Bitmap?
        if (type == 1) {
            zoomedCropBitmap = clipViewLayout1!!.clip()
        } else {
            zoomedCropBitmap = clipViewLayout2!!.clip()
        }
        if (zoomedCropBitmap == null) {
            Log.e("android", "zoomedCropBitmap == null")
            return
        }
        val mSaveUri = Uri.fromFile(File(cacheDir, "cropped_" + System.currentTimeMillis() + ".jpg"))
        if (mSaveUri != null) {
            var outputStream: OutputStream? = null
            try {
                outputStream = contentResolver.openOutputStream(mSaveUri)
                if (outputStream != null) {
                    zoomedCropBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                }
            } catch (ex: IOException) {
                Log.e("android", "Cannot open file: $mSaveUri", ex)
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }
            val intent = Intent()
            intent.data = mSaveUri
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

}
