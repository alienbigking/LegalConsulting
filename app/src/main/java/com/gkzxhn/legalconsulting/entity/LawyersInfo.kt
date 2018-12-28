package com.gkzxhn.legalconsulting.entity

/**
 * @classname：LawyersInfo
 * @author：liushaoxiang
 * @date：2018/10/18 4:37 PM
 * @description：律师信息
 */

data class LawyersInfo(
        val number: String?,
        val username: String?,
        val name: String?,
        val phoneNumber: String?,
        var avatarThumb: String? ,
        var avatarFileId: String? ,
        var orderStatus: String? ,
        /******
         * PENDING_CERTIFIED("待认证"),
        PENDING_APPROVAL("待审核"),
        APPROVAL_FAILURE("审核失败"),
        CERTIFIED("已认证");
         ******/
        var certificationStatus: String? ,
        var orderCount: Int? ,
        var score: Int? ,
        var rewardAmount: Double? ,
        /****** 接单状态 ******/
        var serviceStatus: String? ,
        var lawOffice: String? ,
        var level: String? ,
        var description: String?,
        var alipayBind: Boolean?,
        var categories: List<String>?
)
