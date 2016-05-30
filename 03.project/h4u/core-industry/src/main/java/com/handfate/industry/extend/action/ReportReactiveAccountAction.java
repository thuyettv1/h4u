/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.BaseAction;
import com.handfate.industry.extend.util.DateTimeUtils;
import com.vaadin.ui.HorizontalLayout;
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
public class ReportReactiveAccountAction extends BaseAction {

    /**
     YenNTH8
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khá»Ÿi táº¡o tham sá»‘
        setTableName("V_ACTIVED_ACC");
        setIdColumnName("id");
        setPageLength(25);
        setTableType(INT_NORMAL_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("CUSTOMER_NAME");
        setSortAscending(true);       
        setAllowAdd(false);
        setAllowDelete(false);
        setAllowEdit(false);
        setAllowExport(true);
        setTemplateFile("report_reactive_acc.xls");
        setTemplateHeight(9);
        //ThÃªm cÃ¡c thÃ nh pháº§n
        addTextFieldToForm("id", new TextField(), "id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Report.customerName", new TextField(), "CUSTOMER_NAME", "string", false, null, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Report.ServiceName", new TextField(), "SERVICE_NAME", "string", false, null, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Report.postageNameUsing", new TextField(), "POSTAGE_NAME", "string", false, null, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Report.accName", new TextField(), "ACCOUNT_NAME", "string", false, null, null, null, false, false, null, false, null, true, true, true, true, null);

        List lstParam = new ArrayList();
        lstParam.add(DateTimeUtils.getFirstDayInCurrentMonth());
        lstParam.add(DateTimeUtils.getLastDayInCurrentMonth());
        addTextFieldToForm("Report.ChangeRestore", new PopupDateField(), "CHANGE_DATE", "date", true, null, null, null, true, false, "date:and_mandatory:366", false, lstParam, true, true, true, true, null);
        addTextFieldToForm("Report.UserName", new TextField(), "USER_NAME_CHANGE", "string", false, null, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Report.CauseChangeRestore", new TextField(), "CAUSE_CHANGE_HIS", "string", false, null, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Report.departmentManager", new TextField(), "DEPARTMENT", "string", false, null, null, null, true, false, null, false, null, true, true, true, true, null);

        return initPanel(2);
    }
}
