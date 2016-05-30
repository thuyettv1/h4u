/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.core.action;

import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HienDM1
 */
public class PopupSingleAllUserAction extends PopupSingleAction {
    /**
     * Hàm khởi tạo giao diện
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI Vùng giao diện của chức năng
     */
    public PopupSingleAllUserAction(UI localMainUI) throws Exception {
        //Khởi tạo tham số
        setTableName("sm_users");
        setIdColumnName("user_id");
        setNameColumn("user_name");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("group_id");
        setSortAscending(true);
        setSequenceName("sm_users_seq");
        setRootId(VaadinUtils.getSessionAttribute("G_GroupId").toString());
        
        buildTreeSearch("User.GroupId", 
                "select group_id, group_name, parent_id from v_group where is_enable = 1", null, 
                "group_id", "group_name", "parent_id", "0", true, "group_id");

        //Thêm các thành phần
        addTextFieldToForm("UserID", new TextField(), "user_id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("User.UserName", new TextField(), "user_name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addSinglePopupToForm("User.GroupId", "group_id", "int", true, 50, null, null, false, null, false, null, true, true, true, true, new PopupSingleGroupAction(localMainUI), 2,
                null, "", "group_id", "group_name", "sm_group", null, null);
        addTextFieldToForm("User.FirstName", new TextField(), "first_name", "string", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("User.LastName", new TextField(), "last_name", "string", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("User.Email", new TextField(), "email", "string", true, 100, "email", null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("User.Mobile", new TextField(), "mobile", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
    }
        
}
