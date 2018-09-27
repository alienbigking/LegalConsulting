package com.gkzxhn.autoespresso.entity;

import java.util.ArrayList;
import java.util.List;

/**用例
 * Created by Raleigh.Luo on 18/2/23.
 */

public class CaseEntity {
    private String caseNumber;//用例编号
    private String caseName;//用例名称
    private List<ProcedureEntity> procedures;
    public void addProdure(ProcedureEntity entity){
        if(procedures==null)procedures=new ArrayList<>();
        procedures.add(entity);
    }

    public List<ProcedureEntity> getProcedures() {
        return procedures;
    }

    public void setProcedures(List<ProcedureEntity> procedures) {
        this.procedures = procedures;
    }

    public CaseEntity() {}

    public CaseEntity(String caseNumber, String caseName) {
        this.caseNumber = caseNumber;
        this.caseName = caseName;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }


}
