package com.gkzxhn.legalconsulting.net.error_exception


/**
 * Explanation:
 * @author LSX
 *    -----2018/9/6
 */

class ApiException(val code: Int, msg: String) : RuntimeException(msg)
