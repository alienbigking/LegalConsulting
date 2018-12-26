package com.gkzxhn.legalconsulting.entity

/**
 * @classname：dd
 * @author：liushaoxiang
 * @date：2018/11/8 2:23 PM
 * @description：
 */
class RxBusBean {

    var id: String? = null

    class HomePoint(var show: Boolean,var number: Number)
    /****** 拒绝订单 true为接受 false为拒绝 ******/
    class AcceptOrder(var show: Boolean)
    class HomeTopRedPoint(var show: Boolean)
    class HomeUserInfo(var lawyersInfo: LawyersInfo)

}