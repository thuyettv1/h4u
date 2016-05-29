/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.PopupSingleAction;
import com.handfate.industry.core.action.PopupSingleUserAction;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HienDM1
 */
public class PopupSingleInvoiceNumberAction extends PopupSingleAction {
    /**
     * Hàm khởi tạo giao diện
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI Vùng giao diện của chức năng
     */
    public PopupSingleInvoiceNumberAction(UI localMainUI) throws Exception {
        setTableName("v_iv_num");
        setIdColumnName("id");
        setNameColumn("num");
        setPageLength(10);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("id");
        setSortAscending(false);
        setQueryWhereCondition(" and owner = ? and status = 1 ");
        List lstParameter = new ArrayList();
        lstParameter.add(Long.parseLong(VaadinUtils.getSessionAttribute("G_UserId").toString()));
        setQueryWhereParameter(lstParameter);
        
        addTextFieldToForm("id", new TextField(), "id", "int", true, 10, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Invoice.InvoiceNumber", new TextField(), "num", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("InvoiceNumber.TemplateNumber", new TextField(), "template_number", "string", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("InvoiceNumber.InvoiceSign", new TextField(), "invoice_sign", "string", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("InvoiceNumber.BuyDate", new PopupDateField(), "buy_date", "date", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addSinglePopupToForm("InvoiceNumber.Owner", "owner", "int", false, 50, null, null, true, null, false, null, false, false, true, true, new PopupSingleUserAction(localMainUI), 2,
                null, "", "user_id", "user_name", "SM_USERS",null,null);
        Object[][] status =   {{"2", "InvoiceNumber.Status.Used"},
                                    {"3", "InvoiceNumber.Status.Lost"},
                                    {"4", "InvoiceNumber.Status.Delete"},
                                    {"5", "InvoiceNumber.Status.Cancel"},
                                    {"6", "InvoiceNumber.Status.Expire"}};
        addComboBoxToForm("Menu.Form.Status", new ComboBox(), "status", "int",
                true, 2, null, null, true, false, null, false, null, false, false, true, true, status, "1", "InvoiceNumber.Status.New");

        setComponentAsSysdate("User.CreateDate", "create_date", false, null, false, null);
        setComponentAsLoginUser("User.CreateUser", "create_user", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null , null, INT_VIEWGROUP_ALL);     
    }
}
