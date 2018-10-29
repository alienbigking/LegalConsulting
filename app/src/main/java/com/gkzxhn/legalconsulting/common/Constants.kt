package com.gkzxhn.legalconsulting.common

/**
 * @classname：Constants
 * @author：liushaoxiang
 * @date：2018/10/11 10:54 AM
 * @description：
 */

object Constants {

    /****** 页面跳转相关 ******/

    val ORDER_GET_STATE = "getOrderState"

    //    专业领域选择 页面间传值
    val RESULT_CHOOSE_MAJORS = "Result_Choose_majors"
    val INTENT_SELECTSTRING = "INTENT_SELECTSTRING"
    val REQUESTCODE_CHOOSE_MAJORS = 0x111111
    val RESULTCODE_CHOOSE_MAJORS = 0x111112


    /****** SharedPreferences 相关******/

//    存储项目验证Token的状态
    val SP_TOKEN = "SP_Token"
    val SP_PHONE = "SP_PHONE"
    //    头像的绝对路径
    val SP_AVATARFILE = "sp_avatarFile"
    //    头像的base64码
    val SP_AVATAR_THUMB = "sp_avatar_thumb"

    /**
     * 头像裁剪URI
     */
    val INTENT_CROP_IMAGE_URI = "intent_crop_image_uri"

    /**
     * 裁剪过后的绝对路径
     */
    val CROP_PATH = "crop_path"

}