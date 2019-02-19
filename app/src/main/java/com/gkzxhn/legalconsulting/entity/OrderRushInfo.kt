package com.gkzxhn.legalconsulting.entity

/**
 * @classname：
 * @author：liushaoxiang
 * @date：2018/11/8 2:17 PM
 * @description：
 */
class OrderRushInfo {

    /**
     * id : 37df7e48-efeb-4c3d-aa86-acb8189708e0
     * number : 00000001181107055420
     * categories : ["WORK_COMPENSATION"]
     * attachments : [{"id":"a042ac3c-fea3-4723-b3c6-96f4222df9fc","fileId":"e2d268a5-5101-4787-8810-5fcc86a97f57","thumb":"data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAASABIAAD/4QBM
     * customer : {"id":"fba84909-e5e1-48be-bc49-058e4ee5bc70","name":"17388982062","phoneNumber":"17388982062"}
     * description : vmgsskljdkssd
     * reward : 50
     * createdTime : 2018-11-07T05:54:21.000+0000
     */

    var id: String? = null
    var number: String? = null
    var customer: CustomerBean? = null
    var description: String? = null
    var reward: Double = 0.0
    var createdTime: String? = null
    var category: String? = null
    var attachments: List<AttachmentsBean>? = null


    class CustomerBean {
        /**
         * id : fba84909-e5e1-48be-bc49-058e4ee5bc70
         * name : 17388982062
         * phoneNumber : 17388982062
         */

        var id: String? = null
        var name: String? = null
        var username: String? = null
        var phoneNumber: String? = null
        var avatarURL: String? = null
        var avatarThumb: String? = null
        var avatarFileId: String? = null
    }

    class AttachmentsBean {
        /**
         * id : a042ac3c-fea3-4723-b3c6-96f4222df9fc
         * fileId : e2d268a5-5101-4787-8810-5fcc86a97f57
         * thumb : data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAA
         */

        var id: String? = null
        var fileId: String? = null
        var thumb: String? = null
    }
}
