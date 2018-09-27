package com.gkzxhn.autoespresso.operate;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.screenshot.ScreenCapture;
import android.support.test.runner.screenshot.Screenshot;
import android.view.View;

import com.gkzxhn.autoespresso.util.TUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
/**截屏
 * Created by Raleigh.Luo on 18/3/13.
 */

public class TScreenshot {
    //val SD_ROOT_PATH = Environment.getExternalStorageDirectory().path + "/automator"
//    val SD_SCREENSHOT_PATH = SD_ROOT_PATH + "/takeScreenshot/"

    /**
     * 截屏 无Api版本限制
     * Creates a ScreenCapture that contains a Bitmap of the given activity's root View hierarchy content
     */
    public static ScreenCapture capture(Activity activity)
    {
        return Screenshot.capture(activity);
    }

    /**
     * 截控件 无Api版本限制
     * 保存需调用saveBitmap方法
     * Creates a ScreenCapture that contains a Bitmap of the given view's hierarchy content.
     */
    public static  ScreenCapture capture(View view )  {
        return Screenshot.capture(view);
    }
    /**
     * 截屏 api 18以上
     * 保存需调用saveBitmap方法
     * Creates a ScreenCapture that contains a Bitmap of the visible screen content for Build.VERSION_CODES.JELLY_BEAN_MR2 and above
     */
    public static ScreenCapture capture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return  Screenshot.capture();
        }
        return null;
    }
    /**
     * 截屏
     * 保存需调用saveBitmap方法
     */
    public static Bitmap  take_screenshot(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {//api 18以上
            return InstrumentationRegistry.getInstrumentation().getUiAutomation().takeScreenshot();
        } else{
            return capture(activity).getBitmap();
        }
    }

    /** 保存图片
     * @param bitmap
     * @param imageSaveDir 图片保存的路径，如/storage0/myDir/photo/
     * @return 图片存储的完整路径
     */
    public static String save_bitmap(Bitmap bitmap ,String  imageSaveDir) {
        TUtils.createDir(imageSaveDir);
        String ramdom = UUID.randomUUID().toString().replace("-", "")+".jpg";

        //        String jpegPath = imageSaveDir + ramdom + ".jpg";
        String jpegPath = imageSaveDir + ramdom;
        try {
            FileOutputStream fout =new  FileOutputStream(jpegPath);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            // .i(tag, "saveJpeg：存储完毕！");
        } catch (IOException e) {
            // Log.i(tag, "saveJpeg:存储失败！");
            e.printStackTrace();
        }

        return jpegPath;
    }
}
