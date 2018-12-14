package com.gkzxhn.legalconsulting.utils

import android.net.Uri
import android.provider.MediaStore
import com.gkzxhn.legalconsulting.common.App

/**
 * @classname：dd
 * @author：liushaoxiang
 * @date：2018/12/14 2:21 PM
 * @description：
 */
public object dd {
    /**
     * 解决小米手机上获取图片路径为null的情况
     * @param intent
     * @return
     */
   public fun geturi(intent: android.content.Intent): Uri {
        var uri = intent.data
        val type = intent.type
        if (uri!!.scheme == "file" && type!!.contains("image/")) {
            var path: String? = uri.encodedPath
            if (path != null) {
                path = Uri.decode(path)
                val cr = App.mContext.contentResolver
                val buff = StringBuffer()
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'$path'").append(")")
                val cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        arrayOf(MediaStore.Images.ImageColumns._ID),
                        buff.toString(), null, null)
                var index = 0
                cur!!.moveToFirst()
                while (!cur.isAfterLast) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID)
                    // set _id value
                    index = cur.getInt(index)
                    cur.moveToNext()
                }
                if (index == 0) {
                    // do nothing
                } else {
                    val uri_temp = Uri
                            .parse("content://media/external/images/media/$index")
                    if (uri_temp != null) {
                        uri = uri_temp
                    }
                }
            }
        }
        return uri
    }


}
