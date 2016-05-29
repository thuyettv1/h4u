/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.core.action.parameter;

import com.handfate.industry.core.action.component.MultiUploadField;

/**
 *
 * @author vtsoft
 */
public class MultiUploadFieldParameter {
    private String label = null;
    private MultiUploadField component = null;
    private String attachTableName = null;
    private String dataType = null;
    private boolean isMandatory = false;
    private Integer dataLength = 100; 
    private String format = null; 
    private String caption = null;
    private boolean isCollapsed = false; 
    private String fileDirectory = null; 
    private int maxFileSize = 10;
    private String idConnectColumn = null;
    private String attachColumn = null;
    private String idPrimary = null;
    private String attachSequence = null;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public MultiUploadField getComponent() {
        return component;
    }

    public void setComponent(MultiUploadField component) {
        this.component = component;
    }

    public String getAttachTableName() {
        return attachTableName;
    }

    public void setAttachTableName(String attachTableName) {
        this.attachTableName = attachTableName;
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

    public int getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(int maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public String getIdConnectColumn() {
        return idConnectColumn;
    }

    public void setIdConnectColumn(String idConnectColumn) {
        this.idConnectColumn = idConnectColumn;
    }

    public String getAttachColumn() {
        return attachColumn;
    }

    public void setAttachColumn(String attachColumn) {
        this.attachColumn = attachColumn;
    }

    public String getIdPrimary() {
        return idPrimary;
    }

    public void setIdPrimary(String idPrimary) {
        this.idPrimary = idPrimary;
    }

    public String getAttachSequence() {
        return attachSequence;
    }

    public void setAttachSequence(String attachSequence) {
        this.attachSequence = attachSequence;
    }    
    
}
