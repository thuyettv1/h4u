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
public class LoginUserParameter {
    private String label = null;
    private String databaseFieldName = null; 
    private boolean useToSearch = false;
    private String searchMandatory = null;
    private boolean isCollapsed = false;
    private Object searchDefaultValue = null;
    private boolean onlyView = false;
    private boolean onlyEdit = false;
    private PopupSingleAction popup = null;
    private String filterParentColumn = null;
    private String filterChildColumn = null;
    private int onlyViewGroup = 0;

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

    public boolean isOnlyView() {
        return onlyView;
    }

    public void setOnlyView(boolean onlyView) {
        this.onlyView = onlyView;
    }

    public boolean isOnlyEdit() {
        return onlyEdit;
    }

    public void setOnlyEdit(boolean onlyEdit) {
        this.onlyEdit = onlyEdit;
    }

    public PopupSingleAction getPopup() {
        return popup;
    }

    public void setPopup(PopupSingleAction popup) {
        this.popup = popup;
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

    public int getOnlyViewGroup() {
        return onlyViewGroup;
    }

    public void setOnlyViewGroup(int onlyViewGroup) {
        this.onlyViewGroup = onlyViewGroup;
    }
    
}
