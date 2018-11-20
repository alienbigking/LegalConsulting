package com.gkzxhn.legalconsulting.entity

/**
 * @classname：MoneyList
 * @author：liushaoxiang
 * @date：2018/11/19 6:24 PM
 * @description：
 */
class MoneyList {

    /**
     * content : [{"id":"0a4b9059-5068-417b-a845-e29b756190f2","type":"WITHDRAWAL","amount":12.22,"createdTime":"2018-11-19T01:40:53.863+0000"}]
     * totalPages : 1
     * last : true
     * totalElements : 1
     * number : 0
     * size : 10
     * sort : [{"direction":"ASC","property":"createdTime","ignoreCase":false,"nullHandling":"NATIVE","descending":false,"ascending":true}]
     * numberOfElements : 1
     * first : true
     */

    var totalPages: Int = 0
    var last: Boolean = false
    var totalElements: Int = 0
    var number: Int = 0
    var size: Int = 0
    var numberOfElements: Int = 0
    var first: Boolean = false
    var content: List<ContentBean>? = null
    var sort: List<SortBean>? = null

    class ContentBean {
        /**
         * id : 0a4b9059-5068-417b-a845-e29b756190f2
         * type : WITHDRAWAL
         * amount : 12.22
         * createdTime : 2018-11-19T01:40:53.863+0000
         */

        var id: String? = null
        var type: String? = null
        var amount: Double = 0.toDouble()
        var createdTime: String? = null
    }

    class SortBean {
        /**
         * direction : ASC
         * property : createdTime
         * ignoreCase : false
         * nullHandling : NATIVE
         * descending : false
         * ascending : true
         */

        var direction: String? = null
        var property: String? = null
        var isIgnoreCase: Boolean = false
        var nullHandling: String? = null
        var isDescending: Boolean = false
        var isAscending: Boolean = false
    }
}
