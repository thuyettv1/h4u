/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.BaseAction;
import com.handfate.industry.core.action.component.UploadField;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class CustomerTypeAction extends BaseAction  {

    /**
     * Hàm khởi tạo giao diện
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI Vùng giao diện của chức năng
     * @return Giao diện sau khi khởi tạo
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khởi tạo tham số
        setTableName("bm_customer_type");
        setIdColumnName("type_id");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("type_id");
        setSortAscending(true);
        setSequenceName("bm_customer_type_seq");
        /*buildTreeSearch("Customer.TypeId", 
                "select type_id, name, type_id from bm_customer where is_enable = 1", null, 
                "group_id", "group_name", "parent_id", "0", true, "group_id");*/

        //Thêm các thành phần
        addTextFieldToForm("CustomerTypeID", new TextField(), "type_id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        Object[][] type = {{2, "Customer.community"}};
        addComboBoxToForm("Customer.type", new ComboBox(), "type", "int",
                true, 50, null, null, true, false, null, false, null, true, true, true, true, type, "1", "Customer.individual");
        addTextFieldToForm("CustomerType.Name", new TextField(), "name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);        
        addCheckBoxToForm("CustomerType.IsEnable", new CheckBox(), "is_enable", "boolean",
                true, 10, null, null, false, false, null, false, null, true, true, true, true, "Common.Status.Enable", "Common.Status.Disable");
        addTextFieldToForm("CustomerType.Note", new TextArea(), "note", "string", false, 200, null, null, false, false, null, false, null, true, true, true, true, null);
        
        return initPanel(1);
    } 
    @Override
    public boolean validateEdit (long id) throws Exception {
        // lay gia tri is_enable hien tai 
        AbstractField AisEnable = getComponent("is_enable");
         boolean isEnable = Boolean.parseBoolean(AisEnable.getValue().toString());
         if(!isEnable){
             // kiem tra xem co ban ghi nao thuoc bm_customer su dung customer_type_id khong
            String sql = "select cus.customer_id from bm_customer cus  where cus.type_id = ?";
            List lstParameter = new ArrayList();
            lstParameter.add(id);
            List<Map> cusList = C3p0Connector.queryData(sql, lstParameter); 
            // neu co CUS su dung, va isEnable = false --> Khong cho cap nhat va thong bao: khong cap nhat duoc
            if(!cusList.isEmpty()){
                Notification.show(ResourceBundleUtils.getLanguageResource("CustomerType.Error.TypeIsUsed"),
                                            null, Notification.Type.ERROR_MESSAGE);
                return false;
            }
         }         
         return true;
    }
}
