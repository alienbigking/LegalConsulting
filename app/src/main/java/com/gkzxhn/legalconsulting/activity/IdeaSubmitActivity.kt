package com.gkzxhn.legalconsulting.activity

import android.view.View
import com.gkzxhn.legalconsulting.R
import com.gkzxhn.legalconsulting.net.HttpObserver
import com.gkzxhn.legalconsulting.net.RetrofitClient
import com.gkzxhn.legalconsulting.utils.ProjectUtils
import com.gkzxhn.legalconsulting.utils.showToast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_idea_submit.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Response
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

/**
 * @classname：IdeaSubmitActivity
 * @author：liushaoxiang
 * @date：2018/10/12 11:55 AM
 * @description：意见 反馈
 */
class IdeaSubmitActivity : BaseActivity() {

    override fun provideContentViewId(): Int {
        return R.layout.activity_idea_submit
    }

    override fun init() {
        ProjectUtils.addViewTouchChange(tv_idea_submit_send)
    }

    fun onClickIdeaSubmit(view: View) {
        when (view.id) {
            R.id.iv_idea_submit_back -> {
                finish()
            }
            R.id.tv_idea_submit_send -> {
                val titile = et_idea_submit_title.text.trim().toString()
                val content = et_idea_submit_content.text.trim().toString()
                if (titile.isNotEmpty() && content.isNotEmpty()) {
                    submitSend(titile, content)
                } else {
                    showToast("请完成输入再提交")
                }
            }
        }
    }

    private fun submitSend(titile: String, content: String) {
        var map = LinkedHashMap<String, String>()
        map["title"] = titile
        map["content"] = content
        var Body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(map))
        RetrofitClient.getInstance(this).mApi?.feedback(Body)
                ?.subscribeOn(Schedulers.io())
                ?.unsubscribeOn(AndroidSchedulers.mainThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : HttpObserver<Response<Void>>(this) {
                    override fun success(t: Response<Void>) {
                        when (t.code()) {
                            204 -> {
                                showToast("反馈成功")
                                finish()
                            }
                            else -> {
                                showToast("错误码：" + t.code().toString() + t.message())
                            }
                        }
                    }
                })
    }

}