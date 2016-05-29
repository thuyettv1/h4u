/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.core.action;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 *
 * @author HienDM1
 */
public class PopupSingleGroupAction extends PopupSingleAction {
    /**
     * Hàm khởi tạo giao diện
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI Vùng giao diện của chức năng
     */
    public PopupSingleGroupAction(UI localMainUI) throws Exception {
        //Khởi tạo tham số
        setTableName("sm_group");
        setIdColumnName("group_id");
        setNameColumn("group_name");
        setPageLength(25);
        setTableType(INT_TREE_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("ord");
        setParentColumnName("parent_id");
        setRootId("0");
        setSortAscending(true);
        setSequenceName("sm_group_seq");

        //Thêm các thành phần
        addTextFieldToForm("GroupID", new TextField(), "group_id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Group.GroupName", new TextField(), "group_name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Group.Description", new TextField(), "description", "string", false, 200, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Group.ORD", new TextField(), "ord", "int", true, 100, "long>0", null, false, false, null, false, null, true, true, true, true, null);
        addComboBoxToForm("Group.Parent", new ComboBox(), "parent_id", "int",
                true, 50, null, null, false, false, null, false, null, true, true, true, true, "select group_id, group_name from sm_group where is_enable = 1", null, "sm_group",
                "group_id", "int", "group_name", "0", "Group.List",
                true, false, null, null);
        addCheckBoxToForm("Group.Status", new CheckBox(), "is_enable", "boolean",
                false, 10, null, null, false, false, null, false, null, true, true, true, true, "Common.Status.Enable", "Common.Status.Disable");
    }
}
