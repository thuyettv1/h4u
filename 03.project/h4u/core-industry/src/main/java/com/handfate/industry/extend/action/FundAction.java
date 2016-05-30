/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.BaseAction;
import com.handfate.industry.core.action.PopupSingleUserAction;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class FundAction extends BaseAction  {

    /**
     * ANHPTN
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        
        setTableName("BM_FUND");
        setIdColumnName("ID");
        setPageLength(25);
        setTableType(INT_NORMAL_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("add_date");
        setSortAscending(false);
        setSequenceName("BM_FUND_SEQ");
        
        addTextFieldToForm("id", new TextField(), "ID", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addSinglePopupToForm("Người góp", "user_id", "int", false, 50, null, null, true, null, false, null, true, true, true, true, new PopupSingleUserAction(localMainUI), 2,
                null, "", "user_id", "user_name", "SM_USERS",null,null);
        addTextFieldToForm("Số tiền(tr)", new TextField(), "amount", "long", true, 18, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Ngày góp", new PopupDateField(), "add_date", "date", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Ghi chú", new TextArea(), "note", "string", false, 200, null, null, false, false, null, false, null, true, true, true, true, null);        
        
        return initPanel(2);//So column dc chia tren giao dien
    }    
}
