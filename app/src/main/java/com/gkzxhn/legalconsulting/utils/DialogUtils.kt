package com.gkzxhn.legalconsulting.utils


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.TextView
import com.gkzxhn.legalconsulting.R


/**
 * Explanation: dialog工具类
 * @author LSX
 *    -----2018/9/6
 */

//提示dialog
fun Context.TsDialog(msg: String, flag: Boolean) {
    val progressDialog = Dialog(this, R.style.progress_dialog)
    progressDialog.setContentView(R.layout.dialog_ts)
    progressDialog.setCancelable(true)
    progressDialog.setCanceledOnTouchOutside(flag)
    progressDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
    val tv_msg = progressDialog.findViewById<TextView>(R.id.id_tv_loadingmsg)
    val save = progressDialog.findViewById<TextView>(R.id.dialog_save)
    save.setOnClickListener {
        progressDialog.dismiss()
    }
    tv_msg.text = msg
    progressDialog.show()
}

//提示点击dialog
fun Context.TsClickDialog(msg: String, flag: Boolean): Dialog {
    val progressDialog = Dialog(this, R.style.progress_dialog)
    progressDialog.setContentView(R.layout.dialog_ts)
    /****** 物理返回 ******/
    progressDialog.setCancelable(false)
    /****** 点击其它范围 ******/
    progressDialog.setCanceledOnTouchOutside(flag)
    progressDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
    val tv_msg = progressDialog.findViewById<TextView>(R.id.id_tv_loadingmsg)
    tv_msg.text = msg
    progressDialog.show()
    return progressDialog
}

//加载等待dialog
fun Context.loadDialog(msg: String, flag: Boolean): Dialog {
    val progressDialog = Dialog(this, R.style.progress_dialog)
    progressDialog.setContentView(R.layout.dialog_loading)
    progressDialog.setCancelable(true)
    progressDialog.setCanceledOnTouchOutside(flag)
    progressDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
    val tv_msg = progressDialog.findViewById<TextView>(R.id.id_tv_loadingmsg)
    tv_msg.text = msg
    return progressDialog
}

//有取消和确定的dialog
fun Context.selectDialog(msg: String, flag: Boolean): Dialog {
    val progressDialog = Dialog(this, R.style.progress_dialog)
    progressDialog.setContentView(R.layout.dialog_select)
    progressDialog.setCancelable(true)
    progressDialog.setCanceledOnTouchOutside(flag)
    progressDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
    val tv_msg = progressDialog.findViewById<TextView>(R.id.id_tv_loadingmsg)
    val cancel = progressDialog.findViewById<TextView>(R.id.dialog_cancel)
    cancel.setOnClickListener { progressDialog.dismiss() }
    tv_msg.text = msg
    progressDialog.show()
    return progressDialog
}

/**
 * 提示dialog
 * @param context 上下文
 * @param message 加载信息
 * @param flag  点击红白处是否消失，true为消失，false为不可消失
 */
fun Context.UpDialogUrl(context: Context, message: String, flag: Boolean, url: String) {
    val progressDialog = Dialog(context, R.style.progress_dialog)
    progressDialog.setContentView(R.layout.dialog_ts)
    progressDialog.setCancelable(true)
    progressDialog.setCanceledOnTouchOutside(flag)
    progressDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
    val msg = progressDialog.findViewById<TextView>(R.id.id_tv_loadingmsg) as TextView
    val save = progressDialog.findViewById<TextView>(R.id.dialog_save) as TextView
    val title = progressDialog.findViewById<TextView>(R.id.dialog_title) as TextView
    title.text = "发现新版"
    save.text = "更新"
    save.setOnClickListener { _ ->
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        val content_url = Uri.parse(url)
        intent.data = content_url
        context.startActivity(intent)
        progressDialog.dismiss()
    }
    msg.text = message
    progressDialog.show()

}


/**
 * 更新dialog
 * @param context 上下文
 * @param message 加载信息
 * @param flag  点击红白处是否消失，true为消失，false为不可消失
 */
fun Context.UpDialog(context: Context, message: String, flag: Boolean, url: String) {
    val progressDialog = Dialog(context, R.style.progress_dialog)
    progressDialog.setContentView(R.layout.dialog_up)
    progressDialog.setCancelable(true)
    progressDialog.setCanceledOnTouchOutside(flag)
    progressDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
    val msg = progressDialog.findViewById<TextView>(R.id.id_tv_loadingmsg) as TextView
    val save = progressDialog.findViewById<TextView>(R.id.dialog_save) as TextView
    val cancel = progressDialog.findViewById<TextView>(R.id.dialog_cancel) as TextView
    val title = progressDialog.findViewById<TextView>(R.id.dialog_title) as TextView
    title.text = "发现新版本"
    cancel.setOnClickListener { progressDialog.dismiss() }
    save.setOnClickListener {
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        val content_url = Uri.parse(url)
        intent.data = content_url
        context.startActivity(intent)
        progressDialog.dismiss()
    }
    msg.text = message
    progressDialog.show()
}


