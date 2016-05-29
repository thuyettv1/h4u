/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.MainUI;
import com.handfate.industry.core.action.BaseAction;
import static com.handfate.industry.core.action.BaseAction.INT_LABEL;
import static com.handfate.industry.core.action.BaseAction.INT_VIEWGROUP_RECURSIVE;
import com.handfate.industry.core.action.PopupSingleUserAction;
import com.handfate.industry.core.action.component.ComboboxItem;
import com.handfate.industry.core.action.component.ConfirmationDialog;
import com.handfate.industry.core.action.component.PagedTable;
import com.handfate.industry.core.action.component.UploadField;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class InvoiceNumberAMAction extends InvoiceNumberAction {

    @Override
    public HorizontalLayout init(UI localMainUI) throws Exception {
        setTableName("bm_iv_num");
        setIdColumnName("id");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("id");
        setSortAscending(false);
        setSequenceName("bm_iv_num_SEQ");
        setAllowDelete(false);
        setAllowAdd(false);
        setAllowEdit(false);
        setQueryWhereCondition(" and owner in (select user_id from sm_users where group_id in (select group_id from v_group where is_enable = 1 and path like ?)) ");
        List lstParameter = new ArrayList();
        lstParameter.add("/%" + VaadinUtils.getSessionAttribute("G_GroupId").toString() + "%/");
        setQueryWhereParameter(lstParameter);
        
        addTextFieldToForm("id", new TextField(), "id", "int", true, 10, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("InvoiceNumber.InvoiceBox", new TextField(), "iv_box", "string", true, 100, "int>=0", null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Invoice.InvoiceNumber", new TextField(), "iv_num", "int", true, 10, "int>=0", null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("InvoiceNumber.TemplateNumber", new TextField(), "template_number", "string", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("InvoiceNumber.InvoiceSign", new TextField(), "invoice_sign", "string", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("InvoiceNumber.BuyDate", new PopupDateField(), "buy_date", "date", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addSinglePopupToForm("InvoiceNumber.Owner", "owner", "int", false, 50, null, null, true, null, false, null, false, false, true, true, new PopupSingleUserAction(localMainUI), 2,
                null, "", "user_id", "user_name", "SM_USERS",null,null);
        Object[][] status =   {{"2", "InvoiceNumber.Status.Used"},
                                    {"3", "InvoiceNumber.Status.Lost"},
                                    {"4", "InvoiceNumber.Status.Delete"},
                                    {"5", "InvoiceNumber.Status.Cancel"},
                                    {"6", "InvoiceNumber.Status.Expire"},
                                    {"7", "Transaction.payment"}};
        addComboBoxToForm("Menu.Form.Status", new ComboBox(), "status", "int",
                true, 2, null, null, true, false, null, false, null, false, false, true, true, status, "1", "InvoiceNumber.Status.New");

        Date toDate = new Date();
        Date fromDate = new Date(toDate.getTime() - 60l * 24l * 3600l * 1000l);
        List lstParam = new ArrayList();
        lstParam.add(fromDate);
        lstParam.add(toDate);
        setComponentAsSysdate("User.CreateDate", "create_date", true, "date:and_mandatory:366", false, lstParam);
        setComponentAsLoginUser("User.CreateUser", "create_user", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null , null, INT_VIEWGROUP_ALL);
        
        addCustomizeToSearchForm("History.Reason", txtReason, "string", false, 100, null, null, false, false, false, true, true, null);
        
        //Them nut giao hoá đơn
        buttonAssign.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                buttonAssignClick();
            }
        });
        addButton(buttonAssign);
        
        return initPanel(2);
    }
}
