package com.gkzxhn.legalconsulting.entity

/**
 * @classname：OrderReceiving
 * @author：liushaoxiang
 * @date：2018/11/7 2:57 PM
 * @description： 抢单
 */

data class OrderReceiving(
        var content: List<OrderReceivingContent>?,
        var last: Boolean,
        var totalPages: Int,
        var totalElements: Int,
        var first: Boolean,
        var sort: String?,
        var numberOfElements: Int,
        var size: Int,
        var number: Int
)

class OrderReceivingCustomer {
    var id: String? = null
    var name: String? = null
    var username: String? = null
    var phoneNumber: String? = null
    var avatarURL: String? = null
    var avatarThumb: String? = null
    var avatarFileId: String? = null
}

class OrderReceivingContent {
    var id: String? = null
    var number: String? = null
    var customer: OrderReceivingCustomer? = null
    var category: String? = null
    var description: String? = null
    var reward: Double = 0.0
    var createdTime: String? = null
}

