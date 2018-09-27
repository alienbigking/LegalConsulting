package com.gkzxhn.autoespresso.entity;

/**
 * Created by Raleigh.Luo on 18/3/7.
 */

public class MergedRegionEntity {
    private int firstRow;
    private int lastRow;
    private int firstColumn;
    private int lastColumn;
    private boolean isMergedRegion;
    private String value;

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

    public int getFirstColumn() {
        return firstColumn;
    }

    public void setFirstColumn(int firstColumn) {
        this.firstColumn = firstColumn;
    }

    public int getLastColumn() {
        return lastColumn;
    }

    public void setLastColumn(int lastColumn) {
        this.lastColumn = lastColumn;
    }

    public boolean isMergedRegion() {
        return isMergedRegion;
    }

    public void setMergedRegion(boolean mergedRegion) {
        isMergedRegion = mergedRegion;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
