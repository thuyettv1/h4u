/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.core.action.parameter;

import com.handfate.industry.core.action.component.UploadField;

/**
 *
 * @author vtsoft
 */
public class UploadFieldParameter {
    private String label = null;
    private UploadField component = null;
    private String databaseFieldName = null;
    private String dataType = null;
    private boolean isMandatory = false;
    private Integer dataLength = 100; 
    private String format = null; 
    private String caption = null;
    private boolean isCollapsed = false; 
    private String fileDirectory = null; 
    private boolean isPicture = false;
    private int maxFileSize = 10;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public UploadField getComponent() {
        return component;
    }

    public void setComponent(UploadField component) {
        this.component = component;
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

    public boolean isCollapsed() {
        return isCollapsed;
    }

    public void setIsCollapsed(boolean isCollapsed) {
        this.isCollapsed = isCollapsed;
    }

    public String getFileDirectory() {
        return fileDirectory;
    }

    public void setFileDirectory(String fileDirectory) {
        this.fileDirectory = fileDirectory;
    }

    public boolean isPicture() {
        return isPicture;
    }

    public void setIsPicture(boolean isPicture) {
        this.isPicture = isPicture;
    }

    public int getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(int maxFileSize) {
        this.maxFileSize = maxFileSize;
    }
    
    
}
