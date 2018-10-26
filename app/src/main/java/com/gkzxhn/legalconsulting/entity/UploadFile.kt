package com.gkzxhn.legalconsulting.entity

/**
 * @classname：LawyersInfo
 * @author：liushaoxiang
 * @date：2018/10/18 4:37 PM
 * @description：上传文件
 */

data class UploadFile(
        val id: String?,
        val mime: String?,
        val size: Int?,
        val originalFilename: String?
)
