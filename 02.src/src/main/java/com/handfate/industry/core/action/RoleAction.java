/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.core.action;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.List;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class RoleAction extends BaseAction  {

    /**
     * Hàm khởi tạo giao diện
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI Vùng giao diện của chức năng
     * @return Giao diện sau khi khởi tạo
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khởi tạo tham số
        setTableName("sm_role");
        setIdColumnName("role_id");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("role_name");
        setSortAscending(true);
        setSequenceName("sm_role_seq");

        //Thêm các thành phần
        addTextFieldToForm("Role.RoleID", new TextField(), "role_id", "int", true, 5, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Role.RoleName", new TextField(), "role_name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("Role.Description", new TextArea(), "description", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);       
        addCheckBoxToForm("Role.IsEnable", new CheckBox(), "is_enable", "boolean",
                false, 10, null, null, false, false, null, false, null, true, true, true, true, "Common.Status.Enable", "Common.Status.Disable");
        addMultiPopupToForm("Menu.System.Function", true, false, new PopupMultiMenuAction(localMainUI), 2, null, 
                "sm_role_menu", "menu_id", "role_id", "id", "sm_role_menu_seq", null, null, null);
        setComponentAsLoginUser("User.CreateUser", "create_user", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null , null, INT_VIEWGROUP_ALL);
        return initPanel(1);
    }    
}
