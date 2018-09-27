package com.gkzxhn.autoespresso.entity;

/**
 * Created by Raleigh.Luo on 18/3/15.
 */

public class DriverEntity {
    /* 驱动excle 文件路径 项目跟目录下
     * eg:./auto_espresso/src/main/assets/unit_test_case.xls 也可使用绝对路径
     */
    private String driverFilePath;
    /* 待测试包名 applicationId
     * eg:com.gkzxhn.expresso
     */
    private String packageName;
    /* 单元测试路径
    * eg: ./expresso/src/androidTest/java/com.xx.xx.unit 也可使用绝对路径
    */
    private String unitPath;

    public String getDriverFilePath() {
        return driverFilePath;
    }

    public void setDriverFilePath(String driverFilePath) {
        this.driverFilePath = driverFilePath;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getUnitPath() {
        return unitPath;
    }

    public void setUnitPath(String unitPath) {
        this.unitPath = unitPath;
    }
}
