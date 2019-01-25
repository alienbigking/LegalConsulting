package com.gkzxhn.legalconsulting.entity

/**
 * @classname：dd
 * @author：liushaoxiang
 * @date：2018/11/8 2:23 PM
 * @description：
 */

class OrderMyInfo {

    /**
     * id : 5eac0d15-bf39-437f-908d-36c4296b1e34
     * number : 00000001181107033102
     * status : COMPLETE
     * categories : ["WORK_COMPENSATION","TRAFFIC_ACCIDENT"]
     * customer : {"id":"71108fd2-e809-4115-84d3-c374c9850e28","name":"18508474396","phoneNumber":"18508474396"}
     * lawyer : {"id":"71108fd2-e809-4115-84d3-c374c9850e28","name":"18508474396","phoneNumber":"18508474396"}
     * description : 赶快换手机开机
     * attachments : [{"id":"e27d4772-f574-4
     * reward : 40
     * createdTime : 2018-11-07T03:31:02.000+0000
     * receivingTime : null
     */

    var id: String? = null
    var number: String? = null
    var status: String? = null
//    所属类型（指定单/抢单）
    var type: String? = null
    var customer: CustomerBean? = null
    var description: String? = null
    var reward: Double = 0.0
    var videoDuration: Double = 0.0
    var createdTime: String? = null
    var acceptedTime: String? = null
    var processedTime: String? = null
    var endTime: String? = null
    var category: String? = null
    var attachments: List<AttachmentsBean>? = null




    class CustomerBean {
        /**
         * id : 71108fd2-e809-4115-84d3-c374c9850e28
         * name : 18508474396
         * phoneNumber : 18508474396
         */

        var id: String? = null
        var name: String? = null
        var username: String? = null
        var avatarURL: String? = null
        var avatarThumb: String? = null
        var avatarFileId: String? = null
    }


    class AttachmentsBean {
        /**
         * id : e27d4772-f574-46d8-ad87-7a32ab17d4d3
         * fileId : e0f68ed5-c235-49b8-a31d-e9124dafaed1
         * thumb : data:image/jpeg;base64,/9j/4AAQSkZJ
         */

        var id: String? = null
        var fileId: String? = null
        var thumb: String? = null
    }
}
