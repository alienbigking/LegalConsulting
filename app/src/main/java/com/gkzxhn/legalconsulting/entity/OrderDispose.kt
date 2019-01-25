package com.gkzxhn.legalconsulting.entity

/**
 * @classname：OrderDispose
 * @author：liushaoxiang
 * @date：2018/11/8 9:35 AM
 * @description： 我的咨询（指定 单）
 */

 class OrderDispose {
    /**
     * content : [{"id":"c7b23636-0b88-48b7-93e3-d5f1d4079bd8","number":"00000001181107031854","status":"PROCESSING","customer":{"id":"71108fd2-e809-4115-84d3-c374c9850e28","name":"18508474396","phoneNumber":"18508474396"},"categories":["TRAFFIC_ACCIDENT","MARRIAGE_FAMILY"],"description":"订单测试测试一下看看吧！","reward":50,"createdTime":"2018-11-07T03:18:55.000+0000","receivingTime":null},{"id":"5eac0d15-bf39-437f-908d-36c4296b1e34","number":"00000001181107033102","status":"COMPLETE","customer":{"id":"71108fd2-e809-4115-84d3-c374c9850e28","name":"18508474396","phoneNumber":"18508474396"},"categories":["WORK_COMPENSATION","TRAFFIC_ACCIDENT"],"description":"赶快换手机开机","reward":40,"createdTime":"2018-11-07T03:31:02.000+0000","receivingTime":null},{"id":"f53cd6e4-693b-4d23-8c8d-cc840da2cf77","number":"00000001181107033328","status":"PROCESSING","customer":{"id":"fba84909-e5e1-48be-bc49-058e4ee5bc70","name":"17388982062","phoneNumber":"17388982062"},"categories":["TRAFFIC_ACCIDENT","PROPERTY_DISPUTES"],"description":"发货","reward":60,"createdTime":"2018-11-07T03:33:28.000+0000","receivingTime":null}]
     * totalPages : 1
     * last : true
     * totalElements : 3
     * sort : [{"direction":"ASC","property":"createdTime","ignoreCase":false,"nullHandling":"NATIVE","ascending":true,"descending":false}]
     * first : true
     * numberOfElements : 3
     * size : 10
     * number : 0
     */

    var totalPages: Int = 0
    var last: Boolean = false
    var totalElements: Int = 0
    var first: Boolean = false
    var numberOfElements: Int = 0
    var size: Int = 0
    var number: Int = 0
    var content: List<ContentBean>? = null
    var sort: List<SortBean>? = null

    class ContentBean {
        /**
         * id : c7b23636-0b88-48b7-93e3-d5f1d4079bd8
         * number : 00000001181107031854
         * status : PROCESSING
         * customer : {"id":"71108fd2-e809-4115-84d3-c374c9850e28","name":"18508474396","phoneNumber":"18508474396"}
         * categories : ["TRAFFIC_ACCIDENT","MARRIAGE_FAMILY"]
         * description : 订单测试测试一下看看吧！
         * reward : 50
         * createdTime : 2018-11-07T03:18:55.000+0000
         * receivingTime : null
         */

        var id: String? = null
        var number: String? = null
        var status: String? = null
        var customer: CustomerBean? = null
        var description: String? = null
        var reward: Double = 0.0
        var videoDuration: Double = 0.0
        var createdTime: String? = null
        var type: String? = null
        var receivingTime: Any? = null
        var category: String?= null

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
    }

    class SortBean {
        /**
         * direction : ASC
         * property : createdTime
         * ignoreCase : false
         * nullHandling : NATIVE
         * ascending : true
         * descending : false
         */

        var direction: String? = null
        var property: String? = null
        var isIgnoreCase: Boolean = false
        var nullHandling: String? = null
        var isAscending: Boolean = false
        var isDescending: Boolean = false
    }
}