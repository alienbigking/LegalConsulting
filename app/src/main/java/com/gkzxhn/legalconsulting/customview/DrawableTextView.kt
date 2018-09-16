package com.gkzxhn.legalconsulting.customview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import com.gkzxhn.legalconsulting.R

/**
 * @classname：DrawableTextView
 * @author：PrivateXiaoWu
 * @date：2018/9/10 8:55
 * @description：
 */

class DrawableTextView : android.support.v7.widget.AppCompatTextView {

    private var src: Drawable? = null
    private var drawablePosition: Int = 0
    private var drawableWidth: Int = 0
    private var drawableHeight: Int = 0
    private val mBitmap: Bitmap? = null
    private var flag: Boolean = false

    constructor(context: Context) : super(context)

    @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {

        val array = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView, defStyleAttr, 0)
        //默认设置为“college”图片
        src = array.getDrawable(R.styleable.DrawableTextView_drawableSrc)
        Log.d(TAG, "DrawableTextView: " + src!!)
        //默认设置为左边
        drawablePosition = array.getInt(R.styleable.DrawableTextView_drawablePosition, LEFT)
        Log.d(TAG, "DrawableTextView: $drawablePosition")
        //默认为20dp宽
        drawableWidth = array.getDimensionPixelSize(R.styleable.DrawableTextView_drawableWidth, 0)
        Log.d(TAG, "DrawableTextView: $drawableWidth")
        //默认为20dp长
        drawableHeight = array.getDimensionPixelSize(R.styleable.DrawableTextView_drawableHeight, 0)
        Log.d(TAG, "DrawableTextView: $drawableHeight")

        flag = array.getBoolean(R.styleable.DrawableTextView_drawable_flags, false)
        array.recycle()
        drawDrawable()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (flag) {
            paint.flags = Paint.UNDERLINE_TEXT_FLAG
            paint.isAntiAlias = true
        }
    }

    private fun drawDrawable() {
        if (src != null) {
            val bitmap = (src as BitmapDrawable).bitmap
            val drawable: Drawable
            if (drawableWidth != 0 && drawableHeight != 0) {
                drawable = BitmapDrawable(resources, getBitmap(bitmap, drawableWidth, drawableHeight))
            } else {
                drawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, true))
            }
            when (drawablePosition) {
                LEFT -> this.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                TOP -> this.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
                RIGHT -> this.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
                BOTTOM -> this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable)
                else -> {
                }
            }
        }
    }

    fun setDrawable(drawable: Drawable) {
        src = drawable
        drawDrawable()
    }


    fun getBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        //实际的大小
        val totalWidth = bitmap.width
        val totalHeight = bitmap.height
        //        int a = width;
        //        int b = height;
        //计算缩放比例
        val scaleWidth = width.toFloat() / totalWidth
        val scaleHeight = height.toFloat() / totalHeight
        val matrix = Matrix()
        //提交缩放
        matrix.postScale(scaleWidth, scaleHeight)
        Log.d(TAG, "宽: " + totalWidth + "高:" + totalHeight)

        //得到缩放后的图片

        return Bitmap.createBitmap(bitmap, 0, 0, totalWidth, totalHeight, matrix, true)
    }

    companion object {
        val TAG = "DrawableTextView"
        val LEFT = 1
        val TOP = 2
        val RIGHT = 3
        val BOTTOM = 4
    }

}
