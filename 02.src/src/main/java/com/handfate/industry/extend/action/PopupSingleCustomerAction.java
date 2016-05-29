/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.PopupSingleAction;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 *
 * @author HienDM1
 */
public class PopupSingleCustomerAction extends PopupSingleAction {
    /**
     * Hàm khởi tạo giao diện
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI Vùng giao diện của chức năng
     */
    public PopupSingleCustomerAction(UI localMainUI) throws Exception {
        //Khởi tạo tham số
        setTableName("bm_customer");
        setIdColumnName("customer_id");
        setNameColumn("name");
        setPageLength(10);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("name");
        setSortAscending(true);
        setSequenceName("bm_customer_seq");
//        buildTreeSearch("Customer.TypeId", 
//                "select type_id, name from bm_customer_type", null, 
//                "type_id", "name", null, "0", true, "type_id");
        //Thêm các thành phần
        addTextFieldToForm("CustomerID", new TextField(), "customer_id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        Object[][] type = {{2, "Customer.community"}};
        addComboBoxToForm("Customer.type", new ComboBox(), "type", "int",
                true, 50, null, null, true, false, null, false, null, true, true, true, true, type, "1", "Customer.individual");
        addTextFieldToForm("Customer.CustomerName", new TextField(), "name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("Customer.CustomerCode", new TextField(), "code", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("Customer.IDNo", new TextField(), "id_no", "string", false, 50, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Customer.TaxCode", new TextField(), "taxcode", "string", false, 50, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Customer.RepresentativeName", new TextField(), "representative_name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Customer.RepresentativeMobile", new TextField(), "representative_mobile", "string", true, 50, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Customer.RepresentativeEmail", new TextField(), "representative_email", "string", true, 100, "email", null, true, false, null, false, null, true, true, true, true, null);
        addComboBoxToForm("Customer.CustomerType", new ComboBox(), "type_id", "int",
                true, 50, null, null, true, false, null, false, null, true, true, true, true, "select type_id, name from bm_customer_type where is_enable = 1", null, "bm_customer_type", 
                "type_id", "int", "name", null, null, false, false,null, null);        
    }
}
