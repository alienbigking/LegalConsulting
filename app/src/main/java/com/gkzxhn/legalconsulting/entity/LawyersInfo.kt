package com.gkzxhn.legalconsulting.entity

/**
 * @classname：LawyersInfo
 * @author：liushaoxiang
 * @date：2018/10/18 4:37 PM
 * @description：律师信息
 */

data class LawyersInfo(
        val id: String?,
        val number: String?,
        val username: String?,
        val name: String?,
        val phoneNumber: String?,
        val profiles: Profiles?
)

class Profiles {
    var avatarThumb: String? = null
    var avatarFileId: String? = null
    var orderStatus: String? = null
    /******
     * PENDING_CERTIFIED("待认证"),
    PENDING_APPROVAL("待审核"),
    APPROVAL_FAILURE("审核失败"),
    CERTIFIED("已认证");
     ******/
    var certificationStatus: String? = null
    var orderCount: Int? = null
    var score: Int? = null
    var rewardAmount: Int? = null
    /****** 接单状态 ******/
    var serviceStatus: String? = null
    var lawOffice: String? = null
    var level: String? = null
    var description: String? = null

}