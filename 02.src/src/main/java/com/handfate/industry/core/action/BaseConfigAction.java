/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.core.action;

import com.handfate.industry.core.action.component.MultiUploadField;
import com.handfate.industry.core.action.component.UploadField;
import com.handfate.industry.core.action.parameter.CheckBoxParameter;
import com.handfate.industry.core.action.parameter.ComboBoxConstantParameter;
import com.handfate.industry.core.action.parameter.ComboBoxParameter;
import com.handfate.industry.core.action.parameter.ComponentParameter;
import com.handfate.industry.core.action.parameter.DateParameter;
import com.handfate.industry.core.action.parameter.LoginUserParameter;
import com.handfate.industry.core.action.parameter.MultiUploadFieldParameter;
import com.handfate.industry.core.action.parameter.OnlyInViewParameter;
import com.handfate.industry.core.action.parameter.PopupMultiParameter;
import com.handfate.industry.core.action.parameter.PopupSingleParameter;
import com.handfate.industry.core.action.parameter.SysdateParameter;
import com.handfate.industry.core.action.parameter.UploadFieldParameter;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author HienDM1
 */
public class BaseConfigAction extends BaseAction {
    private String configFileName = "";

    public String getConfigFileName() {
        return configFileName;
    }

    public void setConfigFileName(String configFileName) {
        this.configFileName = configFileName;
    }    
    
    /**
     * Hàm khởi tạo giao diện
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI Vùng giao diện của chức năng
     * @return Giao diện sau khi khởi tạo
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khởi tạo tham số
        setMainUI(localMainUI);
        
        if(!ResourceBundleUtils.getOtherResource("TableName", configFileName).equals("TableName"))
            setTableName(ResourceBundleUtils.getOtherResource("TableName", configFileName));
        if(!ResourceBundleUtils.getOtherResource("ViewName", configFileName).equals("ViewName"))
            setViewName(ResourceBundleUtils.getOtherResource("ViewName", configFileName));        
        if(!ResourceBundleUtils.getOtherResource("IdColumnName", configFileName).equals("IdColumnName"))
            setIdColumnName(ResourceBundleUtils.getOtherResource("IdColumnName", configFileName));
        if(!ResourceBundleUtils.getOtherResource("PageLength", configFileName).equals("PageLength"))
            setPageLength(Integer.parseInt(ResourceBundleUtils.getOtherResource("PageLength", configFileName)));
        if(!ResourceBundleUtils.getOtherResource("TableType", configFileName).equals("TableType"))
            setTableType(Integer.parseInt(ResourceBundleUtils.getOtherResource("TableType", configFileName)));
        if(!ResourceBundleUtils.getOtherResource("SortColumnName", configFileName).equals("SortColumnName"))
            setSortColumnName(ResourceBundleUtils.getOtherResource("SortColumnName", configFileName));
        if(!ResourceBundleUtils.getOtherResource("ParentColumnName", configFileName).equals("ParentColumnName"))
            setParentColumnName(ResourceBundleUtils.getOtherResource("ParentColumnName", configFileName));
        setRootId("0");
        if(!ResourceBundleUtils.getOtherResource("SortAscending", configFileName).equals("SortAscending"))
            setSortAscending(Boolean.parseBoolean(ResourceBundleUtils.getOtherResource("SortAscending", configFileName)));
        if(!ResourceBundleUtils.getOtherResource("SequenceName", configFileName).equals("SequenceName"))
            setSequenceName(ResourceBundleUtils.getOtherResource("SequenceName", configFileName));
        if(!ResourceBundleUtils.getOtherResource("WhereQuery", configFileName).equals("WhereQuery")) {
            setQueryWhereCondition(ResourceBundleUtils.getOtherResource("WhereQuery", configFileName));
            if(ResourceBundleUtils.getOtherResource("WhereQuery", configFileName).contains("?")) 
                setQueryWhereParameter(getListParamFromString(
                        ResourceBundleUtils.getOtherResource("WhereQueryParam", configFileName),";I;",";X;"));
        }
        
        if(!ResourceBundleUtils.getOtherResource("TreeName", configFileName).equals("TreeName") &&
                !ResourceBundleUtils.getOtherResource("TreeIdColumn", configFileName).equals("TreeIdColumn") &&
                !ResourceBundleUtils.getOtherResource("TreeNameColumn", configFileName).equals("TreeNameColumn") &&
                !ResourceBundleUtils.getOtherResource("TreeTableName", configFileName).equals("TreeTableName") &&
                !ResourceBundleUtils.getOtherResource("TreeConnectColumn", configFileName).equals("TreeConnectColumn") &&
                !ResourceBundleUtils.getOtherResource("TreeWhereQuery", configFileName).equals("TreeWhereQuery")) {
            List lstParam = null;
            if(ResourceBundleUtils.getOtherResource("TreeWhereQuery", configFileName).contains("?")) {
                lstParam = getListParamFromString(
                        ResourceBundleUtils.getOtherResource("TreeWhereQueryParam", configFileName),";I;",";X;");
            }
            
            String strQuery = "";
            String parentComlumn = null;
            if(!ResourceBundleUtils.getOtherResource("TreeParentColumn", configFileName).equals("TreeParentColumn")) {
                parentComlumn = ResourceBundleUtils.getOtherResource("TreeParentColumn", configFileName);
                strQuery = "select " + ResourceBundleUtils.getOtherResource("TreeIdColumn", configFileName) + 
                            ", " + ResourceBundleUtils.getOtherResource("TreeNameColumn", configFileName) + 
                            ", " + ResourceBundleUtils.getOtherResource("TreeParentColumn", configFileName) +                         
                            " from " + ResourceBundleUtils.getOtherResource("TreeTableName", configFileName) +
                            " where 1 = 1 " + ResourceBundleUtils.getOtherResource("TreeWhereQuery", configFileName); 
            } else {
                strQuery = "select " + ResourceBundleUtils.getOtherResource("TreeIdColumn", configFileName) + 
                            ", " + ResourceBundleUtils.getOtherResource("TreeNameColumn", configFileName) + 
                            " from " + ResourceBundleUtils.getOtherResource("TreeTableName", configFileName) +
                            " where 1 = 1 " + ResourceBundleUtils.getOtherResource("TreeWhereQuery", configFileName);
            }
            
            buildTreeSearch(ResourceBundleUtils.getOtherResource("TreeName", configFileName),
                    strQuery, lstParam,
                    ResourceBundleUtils.getOtherResource("TreeIdColumn", configFileName), 
                    ResourceBundleUtils.getOtherResource("TreeNameColumn", configFileName), 
                    parentComlumn, "0", true, ResourceBundleUtils.getOtherResource("TreeConnectColumn", configFileName));
            
            
        }

        int numberOfComponent = Integer.parseInt(ResourceBundleUtils.getOtherResource("NumberOfComponent", configFileName));
        
        for (int i = 0; i < numberOfComponent; i++) {
            List lstAttribute = ResourceBundleUtils.getConstantListConfig("Component" + i, configFileName, ";I;");
            if(ResourceBundleUtils.getOtherResource("IdColumnName", configFileName).equals(lstAttribute.get(2))) {
                setParamToTextField(lstAttribute);
                break;
            }
        }
        
        for (int i = 0; i < numberOfComponent; i++) {
            List lstAttribute = ResourceBundleUtils.getConstantListConfig("Component" + i, configFileName, ";I;");
            if(!ResourceBundleUtils.getOtherResource("IdColumnName", configFileName).equals(lstAttribute.get(2))) {
                setParamToTextField(lstAttribute);
                setParamToDateField(lstAttribute);
                setParamToTimeField(lstAttribute);
                setParamToSysdate(lstAttribute);
                setParamToLoginUser(lstAttribute, localMainUI);
                setParamToCheckBox(lstAttribute);
                setParamToComboBox(lstAttribute);
                setParamToConstantComboBox(lstAttribute);
                setParamToSinglePopup(lstAttribute);
                setParamToMultiPopup(lstAttribute);
                setParamToUploadField(lstAttribute);
                setParamToMultiUploadField(lstAttribute);
                setParamToOnlyInViewField(lstAttribute);
            }
        }
        
        int columnInterface = 1;
        if(!ResourceBundleUtils.getOtherResource("ColumnInterface", configFileName).equals("ColumnInterface")) {
            columnInterface = Integer.parseInt(ResourceBundleUtils.getOtherResource("ColumnInterface", configFileName));
        }
        return initPanel(columnInterface);
    }
    
    /**
     * Hàm chuyển chuỗi dữ liệu thành kiểu list
     *
     * @since 03/05/2015 HienDM
     * @param strParam chuỗi dữ liệu
     * @param separateParent chuỗi phân cách cấp độ 1
     * @param separateChild chuỗi phân cách cấp độ 2
     * @return List dữ liệu
     */    
    private List getListParamFromString(String strParam, String separateParent, String separateChild) throws Exception{
        List lstParam = new ArrayList();
        String[] rowParam = null;
        if(strParam != null && !strParam.isEmpty()) {
            if(strParam.contains(separateParent)) {
                rowParam = strParam.split(separateParent);
            }
            else {
                rowParam = new String[1];
                rowParam[0] = strParam;
            }
            for(int i = 0; i < rowParam.length; i++) {
                String strCellParam = rowParam[i];
                String[] cellParam = null;
                if(strCellParam.contains(separateChild)) {
                    cellParam = strCellParam.split(separateChild);
                    Object data = null;
                    if(cellParam[0].equals("int")) data = Integer.parseInt(cellParam[1]);
                    else if(cellParam[0].equals("long")) data = Long.parseLong(cellParam[1]);
                    else if(cellParam[0].equals("float")) data = Float.parseFloat(cellParam[1]);
                    else if(cellParam[0].equals("double")) data = Double.parseDouble(cellParam[1]);
                    else if(cellParam[0].equals("string")) data = cellParam[1];
                    else if(cellParam[0].equals("login user")) data = Long.parseLong(VaadinUtils.getSessionAttribute("G_UserId").toString());
                    else if(cellParam[0].equals("login group")) data = Long.parseLong(VaadinUtils.getSessionAttribute("G_GroupId").toString());
                    else if(cellParam[0].equals("sysdate")) data = new Date();
                    lstParam.add(data);
                }                   
            }
        }  
        return lstParam;
    }
    
    /**
     * Hàm chuyển chuỗi dữ liệu thành kiểu mảng
     * @since 03/05/2015 HienDM
     * @param strParam chuỗi dữ liệu
     * @param separateParent chuỗi phân cách cấp độ 1
     * @param separateChild chuỗi phân cách cấp độ 2
     * @return Mảng dữ liệu
     */    
    private Object[][] getArrayParamFromString(String strParam, String separateParent, String separateChild) throws Exception{
        Object[] rowParam = null;
        Object[][] resultArray = null;
        if(strParam != null && !strParam.isEmpty()) {
            if(strParam.contains(separateParent)) {
                rowParam = strParam.split(separateParent);
            }
            else {
                rowParam = new String[1];
                rowParam[0] = strParam;
            }
            resultArray = new Object[rowParam.length][2];
            for(int i = 0; i < rowParam.length; i++) {
                String strCellParam = rowParam[i].toString();
                String[] cellParam = null;
                if(strCellParam.contains(separateChild)) {
                    Object[] cellArray = new Object[2];
                    cellParam = strCellParam.split(separateChild);
                    Object data = null;
                    if(cellParam[0].equals("int")) data = Integer.parseInt(cellParam[1]);
                    else if(cellParam[0].equals("long")) data = Long.parseLong(cellParam[1]);
                    else if(cellParam[0].equals("float")) data = Float.parseFloat(cellParam[1]);
                    else if(cellParam[0].equals("double")) data = Double.parseDouble(cellParam[1]);
                    else if(cellParam[0].equals("string")) data = cellParam[1];
                    else if(cellParam[0].equals("login user")) data = Long.parseLong(VaadinUtils.getSessionAttribute("G_UserId").toString());
                    else if(cellParam[0].equals("login group")) data = Long.parseLong(VaadinUtils.getSessionAttribute("G_GroupId").toString());
                    else if(cellParam[0].equals("sysdate")) data = new Date();  
                    else data = "";
                    cellArray[0] = data.toString();
                    cellArray[1] = cellParam[2];
                    resultArray[i] = cellArray;
                }                   
            }
        }  
        return resultArray;
    }    
    
    private void setParamToTextField(List lstAttribute) throws Exception {
        if(lstAttribute.get(0).equals("TextField") || lstAttribute.get(0).equals("TextArea")) {
            ComponentParameter cp = new ComponentParameter();
            if(lstAttribute.get(0).equals("TextField"))
                cp.setComponent(new TextField());
            else 
                cp.setComponent(new TextArea());
            if(!lstAttribute.get(1).equals("null")) cp.setLabel(lstAttribute.get(1).toString());
            if(!lstAttribute.get(2).equals("null")) cp.setDatabaseFieldName(lstAttribute.get(2).toString());
            if(!lstAttribute.get(3).equals("null")) cp.setDataType(lstAttribute.get(3).toString());
            if(!lstAttribute.get(4).equals("null")) cp.setIsMandatory(Boolean.parseBoolean(lstAttribute.get(4).toString()));
            if(!lstAttribute.get(5).equals("null")) cp.setDataLength(Integer.parseInt(lstAttribute.get(5).toString()));
            if(!lstAttribute.get(6).equals("null")) cp.setFormat(lstAttribute.get(6).toString());
            if(!lstAttribute.get(7).equals("null")) cp.setCaption(lstAttribute.get(7).toString());
            if(!lstAttribute.get(8).equals("null")) cp.setUseToSearch(Boolean.parseBoolean(lstAttribute.get(8).toString()));
            if(!lstAttribute.get(9).equals("null")) cp.setIsPassword(Boolean.parseBoolean(lstAttribute.get(9).toString()));
            if(!lstAttribute.get(10).equals("null")) cp.setSearchMandatory(lstAttribute.get(10).toString());
            if(!lstAttribute.get(11).equals("null")) cp.setIsCollapsed(Boolean.parseBoolean(lstAttribute.get(11).toString()));
            if(!lstAttribute.get(12).equals("null")) cp.setSearchDefaultValue(lstAttribute.get(12));
            if(!lstAttribute.get(13).equals("null")) cp.setVisibleAdd(Boolean.parseBoolean(lstAttribute.get(13).toString()));
            if(!lstAttribute.get(14).equals("null")) cp.setVisibleEdit(Boolean.parseBoolean(lstAttribute.get(14).toString()));
            if(!lstAttribute.get(15).equals("null")) cp.setEnableAdd(Boolean.parseBoolean(lstAttribute.get(15).toString()));
            if(!lstAttribute.get(16).equals("null")) cp.setEnableEdit(Boolean.parseBoolean(lstAttribute.get(16).toString()));
            if(!lstAttribute.get(17).equals("null")) cp.setDefaultValue(lstAttribute.get(17));
            addTextFieldToForm(cp);
        }
    }
    
    private void setParamToDateField(List lstAttribute) throws Exception {
        if(lstAttribute.get(0).equals("DateField")) {
            DateParameter cp = new DateParameter();
            cp.setComponent(new PopupDateField());
            if(!lstAttribute.get(1).equals("null")) cp.setLabel(lstAttribute.get(1).toString());
            if(!lstAttribute.get(2).equals("null")) cp.setDatabaseFieldName(lstAttribute.get(2).toString());
            if(!lstAttribute.get(3).equals("null")) cp.setIsMandatory(Boolean.parseBoolean(lstAttribute.get(3).toString()));
            if(!lstAttribute.get(4).equals("null")) cp.setCaption(lstAttribute.get(4).toString());
            if(!lstAttribute.get(5).equals("null")) cp.setUseToSearch(Boolean.parseBoolean(lstAttribute.get(5).toString()));
            if(!lstAttribute.get(6).equals("null")) cp.setSearchMandatory(lstAttribute.get(6).toString());
            if(!lstAttribute.get(7).equals("null")) cp.setIsCollapsed(Boolean.parseBoolean(lstAttribute.get(7).toString()));
            if(!lstAttribute.get(8).equals("null")) cp.setSearchDefaultValue(lstAttribute.get(8));
            if(!lstAttribute.get(9).equals("null")) cp.setVisibleAdd(Boolean.parseBoolean(lstAttribute.get(9).toString()));
            if(!lstAttribute.get(10).equals("null")) cp.setVisibleEdit(Boolean.parseBoolean(lstAttribute.get(10).toString()));
            if(!lstAttribute.get(11).equals("null")) cp.setEnableAdd(Boolean.parseBoolean(lstAttribute.get(11).toString()));
            if(!lstAttribute.get(12).equals("null")) cp.setEnableEdit(Boolean.parseBoolean(lstAttribute.get(12).toString()));
            if(!lstAttribute.get(13).equals("null")) cp.setDefaultValue(lstAttribute.get(13));
            addDateToForm(cp);
        }        
    }
    
    private void setParamToTimeField(List lstAttribute) throws Exception {
        if(lstAttribute.get(0).equals("TimeField")) {
            ComponentParameter cp = new ComponentParameter();
            PopupDateField timeField = new PopupDateField();
            timeField.setResolution(Resolution.SECOND);
            cp.setComponent(timeField);
            if(!lstAttribute.get(1).equals("null")) cp.setLabel(lstAttribute.get(1).toString());
            if(!lstAttribute.get(2).equals("null")) cp.setDatabaseFieldName(lstAttribute.get(2).toString());
            cp.setDataType("date");
            if(!lstAttribute.get(3).equals("null")) cp.setIsMandatory(Boolean.parseBoolean(lstAttribute.get(3).toString()));
            cp.setDataLength(20);
            cp.setFormat("date");
            if(!lstAttribute.get(4).equals("null")) cp.setCaption(lstAttribute.get(4).toString());
            if(!lstAttribute.get(5).equals("null")) cp.setUseToSearch(Boolean.parseBoolean(lstAttribute.get(5).toString()));
            cp.setIsPassword(false);
            if(!lstAttribute.get(6).equals("null")) cp.setSearchMandatory(lstAttribute.get(6).toString());
            if(!lstAttribute.get(7).equals("null")) cp.setIsCollapsed(Boolean.parseBoolean(lstAttribute.get(7).toString()));
            if(!lstAttribute.get(8).equals("null")) cp.setSearchDefaultValue(lstAttribute.get(8));
            if(!lstAttribute.get(9).equals("null")) cp.setVisibleAdd(Boolean.parseBoolean(lstAttribute.get(9).toString()));
            if(!lstAttribute.get(10).equals("null")) cp.setVisibleEdit(Boolean.parseBoolean(lstAttribute.get(10).toString()));
            if(!lstAttribute.get(11).equals("null")) cp.setEnableAdd(Boolean.parseBoolean(lstAttribute.get(11).toString()));
            if(!lstAttribute.get(12).equals("null")) cp.setEnableEdit(Boolean.parseBoolean(lstAttribute.get(12).toString()));
            if(!lstAttribute.get(13).equals("null")) cp.setDefaultValue(lstAttribute.get(13));
            addTextFieldToForm(cp);
        }        
    }
    
    private void setParamToSysdate(List lstAttribute) throws Exception {
        if(lstAttribute.get(0).equals("SysDate")) {
            SysdateParameter cp = new SysdateParameter();
            if(!lstAttribute.get(1).equals("null")) cp.setLabel(lstAttribute.get(1).toString());
            if(!lstAttribute.get(2).equals("null")) cp.setDatabaseFieldName(lstAttribute.get(2).toString());
            if(!lstAttribute.get(3).equals("null")) cp.setUseToSearch(Boolean.parseBoolean(lstAttribute.get(3).toString()));
            if(!lstAttribute.get(4).equals("null")) cp.setSearchMandatory(lstAttribute.get(4).toString());
            if(!lstAttribute.get(5).equals("null")) cp.setIsCollapsed(Boolean.parseBoolean(lstAttribute.get(5).toString()));
            if(!lstAttribute.get(6).equals("null")) cp.setSearchDefaultValue(lstAttribute.get(6));
            setComponentAsSysdate(cp);
        }        
    }
    
    private void setParamToLoginUser(List lstAttribute, UI localMainUI) throws Exception {
        if(lstAttribute.get(0).equals("LoginUser")) {
            LoginUserParameter cp = new LoginUserParameter();
            if(!lstAttribute.get(1).equals("null")) cp.setLabel(lstAttribute.get(1).toString());
            if(!lstAttribute.get(2).equals("null")) cp.setDatabaseFieldName(lstAttribute.get(2).toString());
            if(!lstAttribute.get(3).equals("null")) cp.setUseToSearch(Boolean.parseBoolean(lstAttribute.get(3).toString()));
            if(!lstAttribute.get(4).equals("null")) cp.setSearchMandatory(lstAttribute.get(4).toString());
            if(!lstAttribute.get(5).equals("null")) cp.setIsCollapsed(Boolean.parseBoolean(lstAttribute.get(5).toString()));
            if(!lstAttribute.get(6).equals("null")) cp.setSearchDefaultValue(lstAttribute.get(6));
            if(!lstAttribute.get(7).equals("null")) cp.setOnlyView(Boolean.parseBoolean(lstAttribute.get(7).toString()));
            if(!lstAttribute.get(8).equals("null")) cp.setOnlyEdit(Boolean.parseBoolean(lstAttribute.get(8).toString()));
            cp.setPopup(new PopupSingleUserAction(localMainUI));
            cp.setFilterParentColumn(null);
            cp.setFilterChildColumn(null);
            if(!lstAttribute.get(9).equals("null")) cp.setOnlyViewGroup(Integer.parseInt(lstAttribute.get(9).toString()));
            setComponentAsLoginUser(cp);
        }        
    }
    
    private void setParamToCheckBox(List lstAttribute) throws Exception {
        if(lstAttribute.get(0).equals("CheckBox")) {
            CheckBoxParameter cp = new CheckBoxParameter();
            cp.setComponent(new CheckBox());
            if(!lstAttribute.get(1).equals("null")) cp.setLabel(lstAttribute.get(1).toString());
            if(!lstAttribute.get(2).equals("null")) cp.setDatabaseFieldName(lstAttribute.get(2).toString());
            cp.setDataType("boolean");
            if(!lstAttribute.get(3).equals("null")) cp.setIsMandatory(Boolean.parseBoolean(lstAttribute.get(3).toString()));
            cp.setDataLength(2);
            cp.setFormat(null);
            if(!lstAttribute.get(4).equals("null")) cp.setCaption(lstAttribute.get(4).toString());
            if(!lstAttribute.get(5).equals("null")) cp.setUseToSearch(Boolean.parseBoolean(lstAttribute.get(5).toString()));
            if(!lstAttribute.get(6).equals("null")) cp.setSearchMandatory(lstAttribute.get(6).toString());
            if(!lstAttribute.get(7).equals("null")) cp.setIsCollapsed(Boolean.parseBoolean(lstAttribute.get(7).toString()));
            if(!lstAttribute.get(8).equals("null")) cp.setSearchDefaultValue(lstAttribute.get(8));
            if(!lstAttribute.get(9).equals("null")) cp.setVisibleAdd(Boolean.parseBoolean(lstAttribute.get(9).toString()));
            if(!lstAttribute.get(10).equals("null")) cp.setVisibleEdit(Boolean.parseBoolean(lstAttribute.get(10).toString()));
            if(!lstAttribute.get(11).equals("null")) cp.setEnableAdd(Boolean.parseBoolean(lstAttribute.get(11).toString()));
            if(!lstAttribute.get(12).equals("null")) cp.setEnableEdit(Boolean.parseBoolean(lstAttribute.get(12).toString()));
            if(!lstAttribute.get(13).equals("null")) cp.setEnable(lstAttribute.get(13).toString());
            if(!lstAttribute.get(14).equals("null")) cp.setDisable(lstAttribute.get(14).toString());
            if(!lstAttribute.get(15).equals("null")) cp.setDefaultValue(Boolean.parseBoolean(lstAttribute.get(15).toString()));
            addCheckBoxToForm(cp);
        }        
    }
    
    private void setParamToComboBox(List lstAttribute) throws Exception {
        if(lstAttribute.get(0).equals("ComboBox")) {
            ComboBoxParameter cp = new ComboBoxParameter();
            cp.setComponent(new ComboBox());
            if(!lstAttribute.get(1).equals("null")) cp.setLabel(lstAttribute.get(1).toString());
            if(!lstAttribute.get(2).equals("null")) cp.setDatabaseFieldName(lstAttribute.get(2).toString());
            if(!lstAttribute.get(3).equals("null")) cp.setDataType(lstAttribute.get(3).toString());
            if(!lstAttribute.get(4).equals("null")) cp.setIsMandatory(Boolean.parseBoolean(lstAttribute.get(4).toString()));
            if(!lstAttribute.get(5).equals("null")) cp.setDataLength(Integer.parseInt(lstAttribute.get(5).toString()));
            if(!lstAttribute.get(6).equals("null")) cp.setFormat(lstAttribute.get(6).toString());
            if(!lstAttribute.get(7).equals("null")) cp.setCaption(lstAttribute.get(7).toString());
            if(!lstAttribute.get(8).equals("null")) cp.setUseToSearch(Boolean.parseBoolean(lstAttribute.get(8).toString()));
            cp.setIsPassword(false);
            if(!lstAttribute.get(9).equals("null")) cp.setSearchMandatory(lstAttribute.get(9).toString());
            if(!lstAttribute.get(10).equals("null")) cp.setIsCollapsed(Boolean.parseBoolean(lstAttribute.get(10).toString()));
            if(!lstAttribute.get(11).equals("null")) cp.setSearchDefaultValue(lstAttribute.get(11));
            if(!lstAttribute.get(12).equals("null")) cp.setVisibleAdd(Boolean.parseBoolean(lstAttribute.get(12).toString()));
            if(!lstAttribute.get(13).equals("null")) cp.setVisibleEdit(Boolean.parseBoolean(lstAttribute.get(13).toString()));
            if(!lstAttribute.get(14).equals("null")) cp.setEnableAdd(Boolean.parseBoolean(lstAttribute.get(14).toString()));
            if(!lstAttribute.get(15).equals("null")) cp.setEnableEdit(Boolean.parseBoolean(lstAttribute.get(15).toString()));
            if(!lstAttribute.get(16).equals("null")) cp.setCboTableName(lstAttribute.get(16).toString());
            if(!lstAttribute.get(17).equals("null")) cp.setIdColumn(lstAttribute.get(17).toString());
            if(!lstAttribute.get(18).equals("null")) cp.setIdType(lstAttribute.get(18).toString());
            if(!lstAttribute.get(19).equals("null")) cp.setNameColumn(lstAttribute.get(19).toString());
            if(!lstAttribute.get(20).equals("null")) cp.setDefaultValue(lstAttribute.get(20).toString());
            if(!lstAttribute.get(21).equals("null")) cp.setDefaultCaption(null);
            if(!lstAttribute.get(22).equals("null")) cp.setIsRefresh(Boolean.parseBoolean(lstAttribute.get(22).toString()));
            if(!lstAttribute.get(23).equals("null")) cp.setIsMultiLanguage(Boolean.parseBoolean(lstAttribute.get(23).toString()));
            if(!lstAttribute.get(24).equals("null")) cp.setFilterParentColumn(lstAttribute.get(24).toString());
            
            if(Boolean.parseBoolean(lstAttribute.get(23).toString())) {
                if(!lstAttribute.get(21).equals("null")) {
                    
                }
            }
            
            String filterChild = ""; 
            if(!lstAttribute.get(25).equals("null")) {
                cp.setFilterChildColumn(lstAttribute.get(25).toString());
                filterChild = ", " + cp.getFilterChildColumn();
            }
            lstAttribute.get(25);
            String strQuery = " select " + cp.getIdColumn() + ", " + cp.getNameColumn() + 
                    filterChild + " from " + cp.getCboTableName() + " where 1 = 1 ";
            if(!lstAttribute.get(26).equals("null")) 
                cp.setQuery(strQuery + lstAttribute.get(26).toString());
            else 
                cp.setQuery(strQuery);
            
            List lstParam = getListParamFromString(lstAttribute.get(27).toString(), ";M;", ";X;");
            if(lstParam != null && !lstParam.isEmpty()) cp.setLstParameter(lstParam);
            addComboBoxToForm(cp);
        }        
    }
    
    private void setParamToConstantComboBox(List lstAttribute) throws Exception {
        if(lstAttribute.get(0).equals("ConstantComboBox")) {
            ComboBoxConstantParameter cp = new ComboBoxConstantParameter();
            cp.setComponent(new ComboBox());
            if(!lstAttribute.get(1).equals("null")) cp.setLabel(lstAttribute.get(1).toString());
            if(!lstAttribute.get(2).equals("null")) cp.setDatabaseFieldName(lstAttribute.get(2).toString());
            if(!lstAttribute.get(3).equals("null")) cp.setDataType(lstAttribute.get(3).toString());
            if(!lstAttribute.get(4).equals("null")) cp.setIsMandatory(Boolean.parseBoolean(lstAttribute.get(4).toString()));
            if(!lstAttribute.get(5).equals("null")) cp.setDataLength(Integer.parseInt(lstAttribute.get(5).toString()));
            if(!lstAttribute.get(6).equals("null")) cp.setFormat(lstAttribute.get(6).toString());
            if(!lstAttribute.get(7).equals("null")) cp.setCaption(lstAttribute.get(7).toString());
            if(!lstAttribute.get(8).equals("null")) cp.setUseToSearch(Boolean.parseBoolean(lstAttribute.get(8).toString()));
            cp.setIsPassword(false);
            if(!lstAttribute.get(9).equals("null")) cp.setSearchMandatory(lstAttribute.get(9).toString());
            if(!lstAttribute.get(10).equals("null")) cp.setIsCollapsed(Boolean.parseBoolean(lstAttribute.get(10).toString()));
            if(!lstAttribute.get(11).equals("null")) cp.setSearchDefaultValue(lstAttribute.get(11));
            if(!lstAttribute.get(12).equals("null")) cp.setVisibleAdd(Boolean.parseBoolean(lstAttribute.get(12).toString()));
            if(!lstAttribute.get(13).equals("null")) cp.setVisibleEdit(Boolean.parseBoolean(lstAttribute.get(13).toString()));
            if(!lstAttribute.get(14).equals("null")) cp.setEnableAdd(Boolean.parseBoolean(lstAttribute.get(14).toString()));
            if(!lstAttribute.get(15).equals("null")) cp.setEnableEdit(Boolean.parseBoolean(lstAttribute.get(15).toString()));
            if(!lstAttribute.get(16).equals("null")) cp.setDefaultValue(lstAttribute.get(16).toString());
            if(!lstAttribute.get(17).equals("null")) cp.setDefaultCaption(null);
            if(!lstAttribute.get(18).equals("null")) cp.setData(getArrayParamFromString(lstAttribute.get(18).toString(), ";M;", ";X;"));
            addComboBoxToForm(cp);
        }        
    }
    
    private void setParamToSinglePopup(List lstAttribute) throws Exception {
        if(lstAttribute.get(0).equals("SinglePopup")) {
            PopupSingleParameter cp = new PopupSingleParameter();
            if(!lstAttribute.get(1).equals("null")) cp.setLabel(lstAttribute.get(1).toString());
            if(!lstAttribute.get(2).equals("null")) cp.setDatabaseFieldName(lstAttribute.get(2).toString());
            if(!lstAttribute.get(3).equals("null")) cp.setDataType(lstAttribute.get(3).toString());
            if(!lstAttribute.get(4).equals("null")) cp.setIsMandatory(Boolean.parseBoolean(lstAttribute.get(4).toString()));
            if(!lstAttribute.get(5).equals("null")) cp.setDataLength(Integer.parseInt(lstAttribute.get(5).toString()));
            if(!lstAttribute.get(6).equals("null")) cp.setFormat(lstAttribute.get(6).toString());
            if(!lstAttribute.get(7).equals("null")) cp.setCaption(lstAttribute.get(7).toString());
            if(!lstAttribute.get(8).equals("null")) cp.setUseToSearch(Boolean.parseBoolean(lstAttribute.get(8).toString()));
            if(!lstAttribute.get(9).equals("null")) cp.setSearchMandatory(lstAttribute.get(9).toString());
            if(!lstAttribute.get(10).equals("null")) cp.setIsCollapsed(Boolean.parseBoolean(lstAttribute.get(10).toString()));
            if(!lstAttribute.get(11).equals("null")) cp.setSearchDefaultValue(lstAttribute.get(11));
            if(!lstAttribute.get(12).equals("null")) cp.setVisibleAdd(Boolean.parseBoolean(lstAttribute.get(12).toString()));
            if(!lstAttribute.get(13).equals("null")) cp.setVisibleEdit(Boolean.parseBoolean(lstAttribute.get(13).toString()));
            if(!lstAttribute.get(14).equals("null")) cp.setEnableAdd(Boolean.parseBoolean(lstAttribute.get(14).toString()));
            if(!lstAttribute.get(15).equals("null")) cp.setEnableEdit(Boolean.parseBoolean(lstAttribute.get(15).toString()));
            if(!lstAttribute.get(16).equals("null")) {
                PopupSingleAction popupSingle = new PopupSingleAction();
                popupSingle.setConfigFileName("com.handfate.industry.extend.config." + lstAttribute.get(16));                
                cp.setPopup(popupSingle);
            }
            if(!lstAttribute.get(17).equals("null")) cp.setColumn(Integer.parseInt(lstAttribute.get(17).toString()));
            if(!lstAttribute.get(18).equals("null")) cp.setDefaultValue(lstAttribute.get(18).toString());
            if(!lstAttribute.get(19).equals("null")) cp.setDefaultCaption(lstAttribute.get(19).toString());
            if(!lstAttribute.get(20).equals("null")) cp.setIdColumn(lstAttribute.get(20).toString());
            if(!lstAttribute.get(21).equals("null")) cp.setNameColumn(lstAttribute.get(21).toString());
            if(!lstAttribute.get(22).equals("null")) cp.setCboTableName(lstAttribute.get(22).toString());
            if(!lstAttribute.get(23).equals("null")) cp.setFilterParentColumn(lstAttribute.get(23).toString());
            if(!lstAttribute.get(24).equals("null")) cp.setFilterChildColumn(lstAttribute.get(24).toString());
            addSinglePopupToForm(cp);
        }        
    }
    
    private void setParamToMultiPopup(List lstAttribute) throws Exception {
        if(lstAttribute.get(0).equals("MultiPopup")) {
            PopupMultiParameter cp = new PopupMultiParameter();
            if(!lstAttribute.get(1).equals("null")) cp.setLabel(lstAttribute.get(1).toString());
            if(!lstAttribute.get(2).equals("null")) cp.setIsMandatory(Boolean.parseBoolean(lstAttribute.get(2).toString()));
            cp.setIsCollapsed(false);
            if(!lstAttribute.get(3).equals("null")) {
                PopupMultiAction popupMulti = new PopupMultiAction();
                popupMulti.setConfigFileName("com.handfate.industry.extend.config." + lstAttribute.get(3));
                cp.setPopup(popupMulti);
            }
            if(!lstAttribute.get(4).equals("null")) cp.setColumn(Integer.parseInt(lstAttribute.get(4).toString()));
            if(!lstAttribute.get(5).equals("null")) cp.setTableName(lstAttribute.get(5).toString());
            if(!lstAttribute.get(6).equals("null")) cp.setIdPopup(lstAttribute.get(6).toString());
            if(!lstAttribute.get(7).equals("null")) cp.setIdConnect(lstAttribute.get(7).toString());
            if(!lstAttribute.get(8).equals("null")) cp.setIdFieldDB(lstAttribute.get(8).toString());
            if(!lstAttribute.get(9).equals("null")) cp.setSequenceName(lstAttribute.get(9).toString());
            if(!lstAttribute.get(10).equals("null")) cp.setFilterParentColumn(lstAttribute.get(10).toString());
            if(!lstAttribute.get(11).equals("null")) cp.setFilterChildColumn(lstAttribute.get(11).toString());
            if(!lstAttribute.get(12).equals("null")) cp.setFilterTableName(lstAttribute.get(12).toString());
            cp.setLstAttachField(new ArrayList());                
            addMultiPopupToForm(cp);
        }        
    }
    
    private void setParamToUploadField(List lstAttribute) throws Exception {
        if(lstAttribute.get(0).equals("UploadField")) {
            UploadFieldParameter cp = new UploadFieldParameter();
            cp.setComponent(new UploadField());
            if(!lstAttribute.get(1).equals("null")) cp.setLabel(lstAttribute.get(1).toString());
            if(!lstAttribute.get(2).equals("null")) cp.setDatabaseFieldName(lstAttribute.get(2).toString());
            cp.setDataType("int");
            if(!lstAttribute.get(3).equals("null")) cp.setIsMandatory(Boolean.parseBoolean(lstAttribute.get(3).toString()));
            if(!lstAttribute.get(4).equals("null")) cp.setDataLength(Integer.parseInt(lstAttribute.get(4).toString()));
            cp.setFormat(null);
            if(!lstAttribute.get(5).equals("null")) cp.setCaption(lstAttribute.get(5).toString());
            if(!lstAttribute.get(6).equals("null")) cp.setIsCollapsed(Boolean.parseBoolean(lstAttribute.get(6).toString()));
            if(!lstAttribute.get(7).equals("null")) cp.setFileDirectory(lstAttribute.get(7).toString());
            if(!lstAttribute.get(8).equals("null")) cp.setMaxFileSize(Integer.parseInt(lstAttribute.get(8).toString()));
            addUploadFieldToForm(cp);
        }        
    }
    
    private void setParamToMultiUploadField(List lstAttribute) throws Exception {
        if(lstAttribute.get(0).equals("MultiUploadField")) {
            MultiUploadFieldParameter cp = new MultiUploadFieldParameter();
            cp.setComponent(new MultiUploadField());
            if(!lstAttribute.get(1).equals("null")) cp.setLabel(lstAttribute.get(1).toString());
            if(!lstAttribute.get(2).equals("null")) cp.setAttachTableName(lstAttribute.get(2).toString());
            cp.setDataType("string");
            if(!lstAttribute.get(3).equals("null")) cp.setIsMandatory(Boolean.parseBoolean(lstAttribute.get(3).toString()));
            cp.setDataLength(100);
            cp.setFormat(null);
            if(!lstAttribute.get(4).equals("null")) cp.setCaption(lstAttribute.get(4).toString());
            if(!lstAttribute.get(5).equals("null")) cp.setIsCollapsed(Boolean.parseBoolean(lstAttribute.get(5).toString()));
            if(!lstAttribute.get(6).equals("null")) cp.setFileDirectory(lstAttribute.get(6).toString());
            if(!lstAttribute.get(7).equals("null")) cp.setMaxFileSize(Integer.parseInt(lstAttribute.get(7).toString()));
            if(!lstAttribute.get(8).equals("null")) cp.setIdConnectColumn(lstAttribute.get(8).toString());
            if(!lstAttribute.get(9).equals("null")) cp.setAttachColumn(lstAttribute.get(9).toString());
            if(!lstAttribute.get(10).equals("null")) cp.setIdPrimary(lstAttribute.get(10).toString());
            if(!lstAttribute.get(11).equals("null")) cp.setAttachSequence(lstAttribute.get(11).toString());
            addMultiUploadFieldToForm(cp);
        }        
    }
    
    private void setParamToOnlyInViewField(List lstAttribute) throws Exception {
        if(lstAttribute.get(0).equals("OnlyInViewField")) {
            OnlyInViewParameter cp = new OnlyInViewParameter();
            if(!lstAttribute.get(1).equals("null")) cp.setLabel(lstAttribute.get(1).toString());
            if(!lstAttribute.get(2).equals("null")) cp.setDatabaseFieldName(lstAttribute.get(2).toString());
            if(!lstAttribute.get(3).equals("null")) cp.setCaption(lstAttribute.get(3).toString());
            if(!lstAttribute.get(4).equals("null")) cp.setUseToSearch(Boolean.parseBoolean(lstAttribute.get(4).toString()));
            if(!lstAttribute.get(5).equals("null")) cp.setSearchMandatory(lstAttribute.get(5).toString());
            if(!lstAttribute.get(6).equals("null")) cp.setIsCollapsed(Boolean.parseBoolean(lstAttribute.get(6).toString()));
            if(!lstAttribute.get(7).equals("null")) cp.setSearchDefaultValue(lstAttribute.get(7));
            addComponentOnlyViewToForm(cp);
        }         
    }
}
