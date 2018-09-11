package com.gkzxhn.legalconsulting.model.iml

import com.gkzxhn.legalconsulting.model.IBaseModel


import java.util.*


/**
 * Explanation:
 * @author LSX
 *    -----2018/9/7
 */
open class BaseModel : IBaseModel {
    val REQUEST_TAG = UUID.randomUUID().toString().replace("-", "")
}
