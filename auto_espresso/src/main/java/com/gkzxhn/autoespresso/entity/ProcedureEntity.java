package com.gkzxhn.autoespresso.entity;

import java.util.ArrayList;

/**测试步骤
 * Created by Raleigh.Luo on 18/2/28.
 */

public class ProcedureEntity {
    private String testingProcedure;//测试步骤
    private String viewType;//控件类型
    private String action;//操作类型
    //<参数>
    private ArrayList<String> params=null;

    public String getTestingProcedure() {
        return testingProcedure;
    }

    public void setTestingProcedure(String testingProcedure) {
        this.testingProcedure = testingProcedure;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ArrayList<String> getParams() {
        if(params==null)params=new ArrayList<>();
        return params;
    }

    public void setParams(ArrayList<String> params) {
        this.params = params;
    }
}
