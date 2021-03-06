/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.core.action;

import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class UserProfileAction extends BaseAction  {

    /**
     * Hàm khởi tạo giao diện
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI Vùng giao diện các chức năng
     * @return Giao diện sau khi khởi tạo
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khởi tạo tham số
        setTableName("sm_users");
        setIdColumnName("user_id");
        setPageLength(25);
        setTableType(INT_NORMAL_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("group_id");
        setSortAscending(true);
        setSequenceName("sm_users_seq");        
        
        //Thêm các thành phần
        addTextFieldToForm("UserID", new TextField(), "user_id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("User.UserName", new TextField(), "user_name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("User.Password", new PasswordField(), "password", "string", true, 100, null, null, false, true, null, false, null, true, true, true, true, null);

//        addSinglePopupToForm("User.GroupId", "group_id", "int", true, 50, null, null, true, null, false, null, true, true, true, false, new PopupSingleGroupAction(localMainUI), 2,
//                null, "", "group_id", "group_name", "sm_group", null, null);
//        addComboBoxToForm("User.RoleId", new ComboBox(), "role_id", "int",
//                true, 50, null, null, false, false, null, false, null, true, true, true, true, "select role_id, role_name from sm_role where is_enable = 1", null, "sm_role",
//                "role_id", "int", "role_name", "0", "Role.List",
//                true, false, null, null);
        addTextFieldToForm("User.Birthday", new PopupDateField(), "birthday", "date", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        Object[][] genderData = {{"1","Common.Man"},{2,"Common.Woman"}};
        addComboBoxToForm("User.Gender", new ComboBox(), "gender", "int",
                true, 50, null, null, false, false, null, false, null, true, true, true, true, genderData, "1", "Common.Man");
        addTextFieldToForm("User.FirstName", new TextField(), "first_name", "string", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("User.LastName", new TextField(), "last_name", "string", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("User.Email", new TextField(), "email", "string", true, 100, "email", null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("User.Mobile", new TextField(), "mobile", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("User.Country", new TextField(), "country", "string", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);
//        addCheckBoxToForm("User.Status", new CheckBox(), "is_enable", "boolean",
//                false, 10, null, null, false, false, null, false, null, true, true, true, true, "Common.Status.Enable", "Common.Status.Disable");
        setComponentAsSysdate("User.CreateDate", "create_date", false, null, false, null);
        setComponentAsLoginUser("User.CreateUser", "create_user", false, null, false, null, false, false, new PopupSingleUserAction(localMainUI), null , null, INT_VIEWGROUP_ALL);
        
        HorizontalLayout LayoutArea = initPanel(2);
        List lstId = new ArrayList();
        lstId.add(VaadinUtils.getSessionAttribute("G_UserId").toString());
        table.setValue(lstId);
        buttonEditClick();
        table.setVisible(false);
        buttonCancel.setVisible(false);
        setNotUpdateForm(true);
        return LayoutArea;
    }
    
    
}
