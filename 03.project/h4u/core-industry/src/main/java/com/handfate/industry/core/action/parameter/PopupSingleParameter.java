/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.core.action.parameter;

import com.handfate.industry.core.action.PopupSingleAction;

/**
 *
 * @author vtsoft
 */
public class PopupSingleParameter {
    String label = null; 
    String databaseFieldName = null;
    String dataType = null;
    boolean isMandatory = false;
    Integer dataLength = 100;
    String format = null;
    String caption = null;
    boolean useToSearch = false; 
    String searchMandatory = null;
    boolean isCollapsed = false;
    Object searchDefaultValue = null;
    boolean visibleAdd = true; 
    boolean visibleEdit = true; 
    boolean enableAdd = true;
    boolean enableEdit = true;
    PopupSingleAction popup = null;
    Integer column = 2; 
    String defaultValue = null;
    String defaultCaption = null;
    String idColumn = null;
    String nameColumn = null;
    String cboTableName = null;
    String filterParentColumn = null;
    String filterChildColumn = null;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDatabaseFieldName() {
        return databaseFieldName;
    }

    public void setDatabaseFieldName(String databaseFieldName) {
        this.databaseFieldName = databaseFieldName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public Integer getDataLength() {
        return dataLength;
    }

    public void setDataLength(Integer dataLength) {
        this.dataLength = dataLength;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public boolean isUseToSearch() {
        return useToSearch;
    }

    public void setUseToSearch(boolean useToSearch) {
        this.useToSearch = useToSearch;
    }

    public String getSearchMandatory() {
        return searchMandatory;
    }

    public void setSearchMandatory(String searchMandatory) {
        this.searchMandatory = searchMandatory;
    }

    public boolean isCollapsed() {
        return isCollapsed;
    }

    public void setIsCollapsed(boolean isCollapsed) {
        this.isCollapsed = isCollapsed;
    }

    public Object getSearchDefaultValue() {
        return searchDefaultValue;
    }

    public void setSearchDefaultValue(Object searchDefaultValue) {
        this.searchDefaultValue = searchDefaultValue;
    }

    public boolean isVisibleAdd() {
        return visibleAdd;
    }

    public void setVisibleAdd(boolean visibleAdd) {
        this.visibleAdd = visibleAdd;
    }

    public boolean isVisibleEdit() {
        return visibleEdit;
    }

    public void setVisibleEdit(boolean visibleEdit) {
        this.visibleEdit = visibleEdit;
    }

    public boolean isEnableAdd() {
        return enableAdd;
    }

    public void setEnableAdd(boolean enableAdd) {
        this.enableAdd = enableAdd;
    }

    public boolean isEnableEdit() {
        return enableEdit;
    }

    public void setEnableEdit(boolean enableEdit) {
        this.enableEdit = enableEdit;
    }

    public PopupSingleAction getPopup() {
        return popup;
    }

    public void setPopup(PopupSingleAction popup) {
        this.popup = popup;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefaultCaption() {
        return defaultCaption;
    }

    public void setDefaultCaption(String defaultCaption) {
        this.defaultCaption = defaultCaption;
    }

    public String getIdColumn() {
        return idColumn;
    }

    public void setIdColumn(String idColumn) {
        this.idColumn = idColumn;
    }

    public String getNameColumn() {
        return nameColumn;
    }

    public void setNameColumn(String nameColumn) {
        this.nameColumn = nameColumn;
    }

    public String getCboTableName() {
        return cboTableName;
    }

    public void setCboTableName(String cboTableName) {
        this.cboTableName = cboTableName;
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
    
}
