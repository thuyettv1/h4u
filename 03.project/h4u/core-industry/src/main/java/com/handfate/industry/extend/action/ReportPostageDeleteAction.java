/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.BaseAction;
import com.handfate.industry.core.action.component.UploadField;
import com.handfate.industry.extend.util.DateTimeUtils;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class ReportPostageDeleteAction extends BaseAction {

    /**
     * YenNTH8
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khá»Ÿi táº¡o tham sá»‘
        setTableName("v_delete_postage");
        setIdColumnName("id");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("CUSTOMER_NAME");
        setSortAscending(true);
        setAllowAdd(false);
        setAllowDelete(false);
        setAllowEdit(false);
        setAllowExport(true);
        setTemplateFile("Report_Customer_Pay.xls");
        setTemplateHeight(9);
        //ThÃªm cÃ¡c thÃ nh pháº§n
        addTextFieldToForm("viewID", new TextField(), "id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        //  addComponentToForm("Transaction.AccountCreater", new TextField(), "code", "string", true, 100, null, null, true, false);             

        addTextFieldToForm("Report.customerName", new TextField(), "CUSTOMER_NAME", "string", false, null, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Report.customerAddress", new TextField(), "CUSTOMER_ADDRESS", "string", false, null, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Report.ServiceName", new TextField(), "SERVICE_NAME", "string", false, null, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Report.InvoiceNumber", new TextField(), "INVOICE_NUMBER", "string", false, null, null, null, false, false, null, false, null, true, true, true, true, null);

        List lstParam = new ArrayList();
        lstParam.add(DateTimeUtils.getFirstDayInCurrentMonth());
        lstParam.add(DateTimeUtils.getLastDayInCurrentMonth());
        addTextFieldToForm("Report.InvoiceDate", new PopupDateField(), "CREATE_DATE", "date", false, null, null, null, true, false, "date:and_mandatory:366", false, lstParam, true, true, true, true, null);
        addTextFieldToForm("Report.deleteDate", new PopupDateField(), "PAY_DATE", "date", false, null, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Report.TotalCost", new TextField(), "TOTAL_COST", "int", true, null, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Report.UserNameDelete", new TextField(), "PAY_USER", "string", false, null, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Report.UserNameInvoice", new TextField(), "user_name", "string", false, null, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Report.department", new TextField(), "DEPARTMENT", "string", false, null, null, null, true, false, null, false, null, true, true, true, true, null);

        return initPanel(2);
    }
}
