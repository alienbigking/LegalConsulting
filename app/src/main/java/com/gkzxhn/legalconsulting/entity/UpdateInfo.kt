package com.gkzxhn.legalconsulting.entity

/**
 * @classname：更新的信息类
 * @author：liushaoxiang
 * @date：2018/10/31 3:34 PM
 * @description：
 */
data class UpdateInfo(
//{"code":"2.0.0","number":228,"fileId":"e2ba540e-7404-4f4d-8655-3febea756b72","description":"1","force":true}
        val code: String?,
        val number: Int?,
        val fileId: String?,
        val description: String?,
        val force: Boolean?
)
