/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.BaseAction;
import com.handfate.industry.core.action.PopupSingleAllUserAction;
import com.handfate.industry.core.action.UserAction;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class H4UInvestAction extends BaseAction {

    /**
     * HÃ m khá»Ÿi táº¡o giao diá»‡n
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI VÃ¹ng giao diá»‡n cá»§a chá»©c nÄƒng
     * @return Giao diá»‡n sau khi khá»Ÿi táº¡o
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khá»Ÿi táº¡o tham sá»‘
        setTableName("h4u_invest");
        setIdColumnName("invest_ID");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("invest_ID");
        setSortAscending(false);
        setSequenceName("h4u_invest_seq");
        //setQueryWhereCondition("and assign_user_id =? or check_user_id=? or monitor_user_id=?");
//        queryWhereParameter.add(Long.parseLong(VaadinUtils.getSessionAttribute("G_UserId").toString()));
//        queryWhereParameter.add(Long.parseLong(VaadinUtils.getSessionAttribute("G_UserId").toString()));
//        queryWhereParameter.add(Long.parseLong(VaadinUtils.getSessionAttribute("G_UserId").toString()));
//        setQueryWhereParameter(queryWhereParameter);

        addTextFieldToForm("investID", new TextField(), "invest_ID", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Số tiền góp vốn", new TextField(), "value", "int", true, 18, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Số tài khoản", new TextField(), "bank_number", "string", true, 18, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Nôi dung", new TextField(), "content", "string", true, 2000, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Ngày góp vốn", new PopupDateField(), "CREATED_DATE", "date", false, null, null, null, false, false, null, false, null, true, true, true, true, null);
        addSinglePopupToForm("Người góp vốn", "sender_user_id", "int", true, 2000, null, null, true, null, false, null, true, true, true, true, new PopupSingleAllUserAction(localMainUI), 2,
                null, "", "user_id", "user_name", "sm_user", null, null);
        addSinglePopupToForm("Người nhận", "receiver_user_id", "int", true, 50, null, null, true, null, false, null, true, true, true, true, new PopupSingleAllUserAction(localMainUI), 2,
                null, "", "user_id", "user_name", "sm_user", null, null);

        return initPanel(2);
    }

    public static void main(String[] args) {
        System.out.println("========" + UserAction.class.toString());
    }
}
