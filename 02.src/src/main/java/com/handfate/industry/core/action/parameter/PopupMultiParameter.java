/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.core.action.parameter;

import com.handfate.industry.core.action.PopupMultiAction;
import java.util.List;

/**
 *
 * @author vtsoft
 */
public class PopupMultiParameter {
    String label = null;
    boolean isMandatory = false;
    boolean isCollapsed = false;
    PopupMultiAction popup = null;
    Integer column = 2; 
    List<List> lstAttachField = null; 
    String tableName = null;
    String idPopup = null; 
    String idConnect = null; 
    String idFieldDB = null;
    String sequenceName = null; 
    String filterParentColumn = null; 
    String filterChildColumn = null;
    String filterTableName = null;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public boolean isCollapsed() {
        return isCollapsed;
    }

    public void setIsCollapsed(boolean isCollapsed) {
        this.isCollapsed = isCollapsed;
    }

    public PopupMultiAction getPopup() {
        return popup;
    }

    public void setPopup(PopupMultiAction popup) {
        this.popup = popup;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public List<List> getLstAttachField() {
        return lstAttachField;
    }

    public void setLstAttachField(List<List> lstAttachField) {
        this.lstAttachField = lstAttachField;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getIdPopup() {
        return idPopup;
    }

    public void setIdPopup(String idPopup) {
        this.idPopup = idPopup;
    }

    public String getIdConnect() {
        return idConnect;
    }

    public void setIdConnect(String idConnect) {
        this.idConnect = idConnect;
    }

    public String getIdFieldDB() {
        return idFieldDB;
    }

    public void setIdFieldDB(String idFieldDB) {
        this.idFieldDB = idFieldDB;
    }

    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public String getFilterParentColumn() {
        return filterParentColumn;
    }

    public void setFilterParentColumn(String filterParentColumn) {
        this.filterParentColumn = filterParentColumn;
    }

    public String getFilterChildColumn() {
        return filterChildColumn;
    }

    public void setFilterChildColumn(String filterChildColumn) {
        this.filterChildColumn = filterChildColumn;
    }

    public String getFilterTableName() {
        return filterTableName;
    }

    public void setFilterTableName(String filterTableName) {
        this.filterTableName = filterTableName;
    }
    

}
