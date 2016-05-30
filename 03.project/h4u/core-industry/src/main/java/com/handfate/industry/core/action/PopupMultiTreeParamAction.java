package com.handfate.industry.core.action;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.List;

/**
 * @since 14/11/2014
 * @author HienDM
 */

public class PopupMultiTreeParamAction extends PopupMultiAction {

    /**
     * Hàm khởi tạo giao diện
     * @since 19/11/2014 HienDM
     * @param localMainUI Vùng giao diện của chức năng
     */ 
    public PopupMultiTreeParamAction(UI localMainUI) throws Exception {
        //Khởi tạo tham số
        setTableName("sd_tree_param");
        setIdColumnName("id");
        setNameColumn("value");
        setPageLength(10);
        setTableType(INT_NORMAL_TABLE);
        setMainUI(localMainUI);
        setSortAscending(true);
        setSequenceName("sd_tree_param_seq");
        
        //Thêm các thành phần
        addTextFieldToForm("ID", new TextField(), "id", "int", true, 10, null, null, false, false, null, false, null, true, true, true, true, null);
        Object[][] type = {{"int","int"},{"long", "long"},{"float", "float"},{"double", "double"},{"string", "string"},{"login user", "login user"},{"login group", "login group"},{"sysdate", "sysdate"}};
        addComboBoxToForm("SDComponent.ListParam.Type", new ComboBox(), "type", "string",
                true, 50, null, null, true, false, null, false, null, true, true, true, true, type, "int", "int");
        addTextFieldToForm("SDComponent.ListParam.Value", new TextField(), "value", "string", true, 50, null, null, true, false, null, false, null, true, true, true, true, null);
    }
    
    @Override
    public void beforeInitPanel() throws Exception {
        setAllowAdd(true);
        setAllowEdit(true);
        setAllowDelete(true);
    }
    
    @Override
    public void prepareOpenPopup() throws Exception {
        String query = " and (function_id = ? or function_id is null) ";
        setIsChangeDefaultSearch(true);
        Object id = parent.getComponent("id").getValue();
        if(id != null && id != "") {
            int functionId = Integer.parseInt(id.toString());        
            List lstParameter = new ArrayList();
            lstParameter.add(functionId);  
            setQueryWhereParameter(lstParameter);
            setQueryWhereCondition(query);
        } else {
            query = " and function_id is null  ";  
            setQueryWhereCondition(query); 
            setQueryWhereParameter(null);
        }
    }    
}
