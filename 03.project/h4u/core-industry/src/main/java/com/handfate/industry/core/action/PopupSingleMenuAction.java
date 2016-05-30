package com.handfate.industry.core.action;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 * @since 14/11/2014
 * @author HienDM
 */

public class PopupSingleMenuAction extends PopupSingleAction {

    /**
     * Hàm khởi tạo giao diện
     * @since 19/11/2014 HienDM
     * @param localMainUI Vùng giao diện của chức năng
     */ 
    public PopupSingleMenuAction(UI localMainUI) throws Exception {
        //Khởi tạo tham số
        setTableName("sm_menu");
        setIdColumnName("menu_id");
        setNameColumn("description");
        setPageLength(25);
        setTableType(INT_TREE_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("ord");
        setParentColumnName("parent_id");
        setRootId("0");
        setSortAscending(true);
        setSequenceName("sm_menu_seq");
        setQueryWhereCondition(" and create_user is not null  ");
        
        //Thêm các thành phần
        addTextFieldToForm("MenuID", new TextField(), "menu_id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Menu.Form.Description", new TextField(), "description", "string", true, 200, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.Key", new TextField(), "menu_name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Menu.Form.ORD", new TextField(), "ord", "int", true, 100, "long>0", null, false, false, null, false, null, true, true, true, true, null);
        addComboBoxToForm("Menu.Form.Parent", new ComboBox(), "parent_id", "int",
                true, 50, null, null, true, false, null, false, null, true, true, true, true, "select menu_id, description from sm_menu", null, "sm_menu", 
                "menu_id", "int", "description", "0", "SDFunction.ListMenu",
                true, false, null, null);        
        addCheckBoxToForm("Menu.Form.Status", new CheckBox(), "is_enable", "boolean", 
                true, 1, null, "Common.Status.Enable", false, false, null, false, null, true, true, true, true, "Common.Status.Enable", "Common.Status.Disable");
        setComponentAsLoginUser("User.CreateUser", "create_user", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null , null, INT_VIEWGROUP_ALL);
    }    
}
