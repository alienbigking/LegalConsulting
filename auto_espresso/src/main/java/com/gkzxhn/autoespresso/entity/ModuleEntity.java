package com.gkzxhn.autoespresso.entity;

/**模块
 * Created by Raleigh.Luo on 18/2/24.
 */

public class ModuleEntity {
    private String moduleName;//模块名称
    private String moduleNumber;//模块编号
    private String className;//测试类名
    private String classPackageName;//所在包名
    private String sharedPreferencesName;//本地存储名称
    private int firstRow;//第一行
    private int lastRow;//最后一行

    public String getSharedPreferencesName() {
        return sharedPreferencesName;
    }

    public void setSharedPreferencesName(String sharedPreferencesName) {
        this.sharedPreferencesName = sharedPreferencesName;
    }

    public int getFirstRow() {
        return firstRow;
    }

    public void setFirstRow(int firstRow) {
        this.firstRow = firstRow;
    }

    public int getLastRow() {
        return lastRow;
    }

    public void setLastRow(int lastRow) {
        this.lastRow = lastRow;
    }

    public ModuleEntity() {
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleNumber() {
        return moduleNumber;
    }

    public void setModuleNumber(String moduleNumber) {
        this.moduleNumber = moduleNumber;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassPackageName() {
        return classPackageName;
    }

    public void setClassPackageName(String classPackageName) {
        this.classPackageName = classPackageName;
    }
}
