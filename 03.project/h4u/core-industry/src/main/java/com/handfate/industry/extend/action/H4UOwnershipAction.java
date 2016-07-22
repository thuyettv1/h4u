/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.BaseAction;
import com.handfate.industry.core.action.PopupSingleAllUserAction;
import com.handfate.industry.core.action.UserAction;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class H4UOwnershipAction extends BaseAction {

    /**
     * HÃ m khá»Ÿi táº¡o giao diá»‡n
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI VÃ¹ng giao diá»‡n cá»§a chá»©c nÄƒng
     * @return Giao diá»‡n sau khi khá»Ÿi táº¡o
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khá»Ÿi táº¡o tham sá»‘
        setTableName("h4u_ownership");
        setIdColumnName("ownership_ID");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("ownership_ID");
        setSortAscending(false);
        setSequenceName("h4u_ownership_seq");
        //setQueryWhereCondition("and assign_user_id =? or check_user_id=? or monitor_user_id=?");
//        queryWhereParameter.add(Long.parseLong(VaadinUtils.getSessionAttribute("G_UserId").toString()));
//        queryWhereParameter.add(Long.parseLong(VaadinUtils.getSessionAttribute("G_UserId").toString()));
//        queryWhereParameter.add(Long.parseLong(VaadinUtils.getSessionAttribute("G_UserId").toString()));
//        setQueryWhereParameter(queryWhereParameter);

        addTextFieldToForm("ownershipID", new TextField(), "ownership_ID", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Phần trăm", new TextField(), "percent", "int", true, 18, null, null, true, false, null, false, null, true, true, true, true, null);
        //addTextFieldToForm("Số tài khoản", new TextField(), "bank_number", "string", true, 18, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Ghi chú", new TextField(), "description", "string", false, 2000, null, null, true, false, null, false, null, true, true, true, true, null);
        addComboBoxToForm("Nhà", new ComboBox(), "house_id", "int",
                true, 50, null, null, false, false, null, false, null, true, true, true, true, "select house_id, name from h4u_house", null, "h4u_house",
                "house_id", "int", "name", "0", "Danh sách nhà",
                true, false, null, null); 
        addSinglePopupToForm("Cổ đông", "user_id", "int", true, 2000, null, null, true, null, false, null, true, true, true, true, new PopupSingleAllUserAction(localMainUI), 2,
                null, "", "user_id", "user_name", "sm_user", null, null);
        return initPanel(2);
    }

    public static void main(String[] args) {
        System.out.println("========" + UserAction.class.toString());
    }
}
