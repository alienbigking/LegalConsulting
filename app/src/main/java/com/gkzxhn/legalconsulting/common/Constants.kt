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
    val REQUESTCODE_CHOOSE_MAJORS = 0x111
    val RESULTCODE_CHOOSE_MAJORS = 0x112
    //地址填写返回值
    val RESULT_EDIT_ADDRESS = "RESULT_eidt_address"
    val RESULTCODE_EDIT_ADDRESS = 23


    /****** SharedPreferences 相关******/

//    存储项目验证Token的状态
    val SP_TOKEN = "SP_Token"
    val SP_PHONE = "SP_PHONE"
    val SP_NAME = "sp_user_name"
    val SP_LAWOFFICE = "sp_user_lawOffice"
    //    头像的绝对路径
    val SP_AVATARFILE = "sp_avatarFile"
    //    头像的base64码
    val SP_AVATAR_THUMB = "sp_avatar_thumb"

    /**
     *PENDING_CERTIFIED("待认证"),
    PENDING_APPROVAL("待审核"),
    APPROVAL_FAILURE("审核失败"),
    CERTIFIED("已认证");
     */
    val SP_CERTIFICATIONSTATUS = "SP_CERTIFICATIONSTATUS"

    /**
     * 头像裁剪URI
     */
    val INTENT_CROP_IMAGE_URI = "intent_crop_image_uri"

    /**
     * 裁剪过后的绝对路径
     */
    val CROP_PATH = "crop_path"


    /****** 字符常量 ******/

    /****** 待认证 ******/
    val PENDING_CERTIFIED = "PENDING_CERTIFIED"
    /****** 待审核 ******/
    val PENDING_APPROVAL = "PENDING_APPROVAL"
    /****** 审核失败 ******/
    val APPROVAL_FAILURE = "APPROVAL_FAILURE"
    /****** 已认证 ******/
    val CERTIFIED = "CERTIFIED"


    // ("待付款")
    val ORDER_STATE_PENDING_PAYMENT = "PENDING_PAYMENT"
    // ("待审核")
    val ORDER_STATE_PENDING_APPROVAL = "PENDING_APPROVAL"
    // ("待接单")
    val ORDER_STATE_PENDING_RECEIVING = "PENDING_ACCEPT"
    // ("处理中")
    val ORDER_STATE_ACCEPTED = "ACCEPTED"
    // ("已完成")
    val ORDER_STATE_COMPLETE = "COMPLETE"
    // ("已拒绝")
    val ORDER_STATE_REFUSED = "REFUSED"
    // ("已取消")
    val ORDER_STATE_CANCELLED = "CANCELLED"

    /****** 与后台约定的前缀 ******/
    val BASE_64_START = "data:image/png;base64,"
    //    val APK_ADRESS="LegalConsulting.apk"
    val APK_ADRESS = "legal.apk"

}