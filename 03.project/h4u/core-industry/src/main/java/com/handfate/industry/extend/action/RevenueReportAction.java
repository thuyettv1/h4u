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
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class RevenueReportAction extends BaseAction  {

    /**
     * Hàm khởi tạo giao diện
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI Vùng giao diện của chức năng
     * @return Giao diện sau khi khởi tạo
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khởi tạo tham số
        setTableName("V_REVENUE_REPORT");
        setIdColumnName("id");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("customer_name");
        setSortAscending(true);
        setAllowAdd(false);
        setAllowDelete(false);
        setAllowEdit(false);
        setAllowExport(true);
        setTemplateFile("report_revenue.xls");
        setTemplateHeight(8);
        
        //Thêm các thành phần
        addTextFieldToForm("CustomerTypeID", new TextField(), "id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Customer.CustomerName", new TextField(), "customer_name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("Customer.Address", new TextField(), "customer_address", "string", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("Service.name", new TextField(), "service_name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("Postage.name", new TextField(), "postage_name", "string", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("Policy.discountContract", new TextField(), "discount_contract", "float", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("Report.TotalCost", new TextField(), "total_cost", "float", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("Contract.signDate", new PopupDateField(), "SIGN_DATE", "date", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.number", new TextField(), "contract_number", "string", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("Contract.CreateUser", new TextField(), "contract_create_user", "string", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("Service.GroupId", new TextField(), "service_departement", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);        
        
        List lstParam = new ArrayList();
        lstParam.add(DateTimeUtils.getFirstDayInCurrentMonth());
        lstParam.add(DateTimeUtils.getLastDayInCurrentMonth());
        addTextFieldToForm("Invoice.CreateDate", new PopupDateField(), "INVOICE_CREATE_DATE", "date", true, 50, null, null, true, false, "date:and_mandatory:366", false, lstParam, true, true, true, true, null);
        addTextFieldToForm("Invoice.InvoiceNumber", new TextField(), "INVOICE_NUMBER", "string", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);        
       
        return initPanel(2);
    }    
}
