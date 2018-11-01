package com.gkzxhn.legalconsulting.entity

/**
 * @classname：更新的信息类
 * @author：liushaoxiang
 * @date：2018/10/31 3:34 PM
 * @description：
 */
data class UpdateInfo(

//{"code":"0.1.0","number":11,"fileId":"fileId","description":"添加某某功能","force":false}

        val id: String?,
        val code: String?,
        val number: Int?,
        val fileId: String?,
        val description: String?,
        val force: Boolean?
)
