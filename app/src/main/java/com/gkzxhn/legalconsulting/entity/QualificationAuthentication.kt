package com.gkzxhn.legalconsulting.entity

/**
 * @classname：JsonRootBean
 * @author：liushaoxiang
 * @date：2018/11/1 10:32 AM
 * @description： 律师认证信息类
 */
class QualificationAuthentication {

    var name: String? = null
    var gender: String? = null
    var description: String? = null
    var categories: List<String>? = null
    var workExperience: Int = 0
    var level: String? = null
    var lawOffice: String? = null
    var lawOfficeAddress: LawOfficeAddress? = null
    var certificatePictures: List<CertificatePictures>? = null
    var assessmentPictures: List<AssessmentPictures>? = null
    var identificationPictures: List<IdentificationPictures>? = null

}

class IdentificationPictures {

    var fileId: String? = null
    var thumbFileId: String? = null

}

class LawOfficeAddress {

    var id: String? = null
    var countryCode: String? = null
    var countryName: String? = null
    var provinceCode: String? = null
    var provinceName: String? = null
    var cityCode: String? = null
    var cityName: String? = null
    var countyCode: String? = null
    var countyName: String? = null
    var streetDetail: String? = null

}

class CertificatePictures {

    var fileId: String? = null
    var thumbFileId: String? = null

}

class AssessmentPictures {

    var fileId: String? = null
    var thumbFileId: String? = null

}