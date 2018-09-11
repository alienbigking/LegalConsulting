package com.gkzxhn.legalconsulting.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import com.gkzxhn.legalconsulting.R;


/**
 * @classname：DrawableTextView
 * @author：PrivateXiaoWu
 * @date：2018/9/10 8:55
 * @description：
 */

public class DrawableTextView extends android.support.v7.widget.AppCompatTextView {
    public static final String TAG = "DrawableTextView";

    public static final int LEFT = 1, TOP = 2, RIGHT = 3, BOTTOM = 4;

    private Drawable src;
    private int drawablePosition;
    private int drawableWidth;
    private int drawableHeight;
    private Bitmap mBitmap;
    private boolean flag;

    public DrawableTextView(Context context) {
        super(context);
    }

    public DrawableTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView, defStyleAttr, 0);
        //默认设置为“college”图片
        src = array.getDrawable(R.styleable.DrawableTextView_drawableSrc);
        Log.d(TAG, "DrawableTextView: " + src);
        //默认设置为左边
        drawablePosition = array.getInt(R.styleable.DrawableTextView_drawablePosition, LEFT);
        Log.d(TAG, "DrawableTextView: " + drawablePosition);
        //默认为20dp宽
        drawableWidth = array.getDimensionPixelSize(R.styleable.DrawableTextView_drawableWidth, 0);
        Log.d(TAG, "DrawableTextView: " + drawableWidth);
        //默认为20dp长
        drawableHeight = array.getDimensionPixelSize(R.styleable.DrawableTextView_drawableHeight, 0);
        Log.d(TAG, "DrawableTextView: " + drawableHeight);

        flag = array.getBoolean(R.styleable.DrawableTextView_drawable_flags, false);
        array.recycle();
        drawDrawable();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (flag) {
            getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            getPaint().setAntiAlias(true);
        }
    }

    private void drawDrawable() {
        if (src != null) {
            Bitmap bitmap = ((BitmapDrawable) src).getBitmap();
            Drawable drawable;
            if (drawableWidth != 0 && drawableHeight != 0) {
                drawable = new BitmapDrawable(getResources(), getBitmap(bitmap, drawableWidth, drawableHeight));
            } else {
                drawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true));
            }
            switch (drawablePosition) {
                case LEFT:
                    this.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                    break;
                case TOP:
                    this.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
                    break;
                case RIGHT:
                    this.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                    break;
                case BOTTOM:
                    this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
                    break;
                default:
                    break;
            }
        }
    }

    public void setDrawable(Drawable drawable) {
        src = drawable;
        drawDrawable();
    }


    public Bitmap getBitmap(Bitmap bitmap, int width, int height) {
        //实际的大小
        int totalWidth = bitmap.getWidth();
        int totalHeight = bitmap.getHeight();
//        int a = width;
//        int b = height;
        //计算缩放比例
        float scaleWidth = (float) width / totalWidth;
        float scaleHeight = (float) height / totalHeight;
        Matrix matrix = new Matrix();
        //提交缩放
        matrix.postScale(scaleWidth, scaleHeight);
        Log.d(TAG, "宽: " + totalWidth + "高:" + totalHeight);

        //得到缩放后的图片
        Bitmap bitmapResult = Bitmap.createBitmap(bitmap, 0, 0, totalWidth, totalHeight, matrix, true);

        return bitmapResult;
    }

}
