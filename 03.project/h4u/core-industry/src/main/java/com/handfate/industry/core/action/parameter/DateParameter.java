/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.core.action.parameter;

import com.vaadin.ui.Component;

/**
 *
 * @author vtsoft
 */
public class DateParameter {
    private String label = null; 
    private Component component = null; 
    private String databaseFieldName = null; 
    private boolean isMandatory = false; 
    private String caption = null;
    private boolean useToSearch = false;
    private String searchMandatory = null;
    private boolean isCollapsed = false; 
    private Object searchDefaultValue = null; 
    private boolean visibleAdd = true;
    private boolean visibleEdit = true; 
    private boolean enableAdd = true; 
    private boolean enableEdit = true; 
    private Object defaultValue = null;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public String getDatabaseFieldName() {
        return databaseFieldName;
    }

    public void setDatabaseFieldName(String databaseFieldName) {
        this.databaseFieldName = databaseFieldName;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(boolean isMandatory) {
        this.isMandatory = isMandatory;
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

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    
}
