package com.handfate.industry.core.action;

import static com.handfate.industry.core.MainUI.mainLogger;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.data.Property;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * @since 14/11/2014
 * @author HienDM
 */

public class PopupMultiComponentParamAction extends PopupMultiAction {

    /**
     * Hàm khởi tạo giao diện
     * @since 19/11/2014 HienDM
     * @param localMainUI Vùng giao diện của chức năng
     */ 
    public PopupMultiComponentParamAction(UI localMainUI) throws Exception {
        //Khởi tạo tham số
        setTableName("sd_com_param");
        setViewName("v_com_param");
        setIdColumnName("id");
        setNameColumn("name");
        setPageLength(10);
        setTableType(INT_NORMAL_TABLE);
        setMainUI(localMainUI);
        setSortAscending(true);
        setSequenceName("sd_com_param_seq");
        
        //Thêm các thành phần
        addTextFieldToForm("ID", new TextField(), "id", "int", true, 10, null, null, false, false, null, false, null, true, true, true, true, null);
        addComponentOnlyViewToForm("SDComponent.ListParam.Name", "name", null, false, null, false, null);
        Object[][] type = {{"int","int"},{"long", "long"},{"float", "float"},{"double", "double"},{"string", "string"},{"login user", "login user"},{"login group", "login group"},{"sysdate", "sysdate"}};
        addComboBoxToForm("SDComponent.ListParam.Type", new ComboBox(), "type", "string",
                true, 50, null, null, true, false, null, false, null, true, true, true, true, type, "int", "int");
        addTextFieldToForm("SDComponent.ListParam.Value", new TextField(), "value", "string", true, 50, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDComponent.ListParam.Caption", new TextField(), "caption", "string", false, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.VietNam", new TextField(), "viet_nam", "string", false, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.English", new TextField(), "english", "string", false, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        
        Property.ValueChangeListener listener = new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    if(getComponent("type").getValue() != null) {
                        if(getComponent("type").getValue().equals("login user")) {
                            getComponent("value").setEnabled(false);
                            getComponent("value").setValue("login user");
                        } else if(getComponent("type").getValue().equals("login group")) {
                            getComponent("value").setEnabled(false);
                            getComponent("value").setValue("login group");                        
                        } else if(getComponent("type").getValue().equals("sysdate")) {
                            getComponent("value").setEnabled(false);
                            getComponent("value").setValue("sysdate");                        
                        } else {
                            getComponent("value").setEnabled(true);
                            getComponent("value").setValue("");
                        }
                    } else {
                        getComponent("value").setEnabled(true);
                        getComponent("value").setValue("");
                    }
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }
            }
        };
        ComboBox cboType = (ComboBox) getComponent("type");
        cboType.addValueChangeListener(listener);        
    }
    
    @Override
    public void beforeInitPanel() throws Exception {
        setAllowAdd(true);
        setAllowEdit(true);
        setAllowDelete(true);
    }
    
    @Override
    public void prepareOpenPopup() throws Exception {
        String query = " and (component_id = ? or component_id is null) ";
        setIsChangeDefaultSearch(true);
        Object id = parent.getComponent("id").getValue();
        if(id != null && id != "") {
            int functionId = Integer.parseInt(id.toString());        
            List lstParameter = new ArrayList();
            lstParameter.add(functionId);  
            setQueryWhereParameter(lstParameter);
            setQueryWhereCondition(query);
        } else {
            query = " and component_id is null  ";  
            setQueryWhereCondition(query);
            setQueryWhereParameter(null); 
        }
    }   

    @Override
    public void afterAddData(Connection connection, long id) throws Exception {
        connection.commit();
        updateDataRefreshData();
    }
    
    @Override
    public void afterEditData(Connection connection, long id) throws Exception {
        connection.commit();
        updateDataRefreshData();
    }
    
}
