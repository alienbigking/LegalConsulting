package com.gkzxhn.legalconsulting.entity

/**
 * @classname：dd
 * @author：liushaoxiang
 * @date：2018/10/18 4:37 PM
 * @description：
 */


data class LawyersInfo(
        val id: String?,
        val number: String?,
        val username: String?,
        val name: String?,
        val phoneNumber: String?,
        val profiles: profiles?
)

class profiles() {
    var avatarThumb: String? = null
    var avatarFileId: Int? = null
    var orderStatus: String? = null
    var orderCount: Int? = null
    var score: Int? = null
    var rewardAmount: Int? = null
    var lawOffice: String? = null
    var level: String? = null
    var certified: Boolean? = null
    var description: String? = null
}