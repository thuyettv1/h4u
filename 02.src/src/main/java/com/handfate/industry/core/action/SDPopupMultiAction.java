/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.core.action;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 *
 * @author HienDM1
 */
public class SDPopupMultiAction extends BaseAction {
    
    /**
     * Hàm khởi tạo giao diện
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI Vùng giao diện của chức năng
     * @return Giao diện sau khi khởi tạo
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khởi tạo tham số
        setTableName("SD_MULTI_POPUP");
        setIdColumnName("id");
        setPageLength(25);
        setTableType(INT_NORMAL_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("ord");
        setSortAscending(true);
        setSequenceName("sm_menu_seq");
        
        //Thêm các thành phần
        addTextFieldToForm("MenuID", new TextField(), "id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Menu.Form.Description", new TextField(), "description", "string", true, 200, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.Key", new TextField(), "menu_name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        setComponentAsLoginUser("User.CreateUser", "create_user", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null , null, INT_VIEWGROUP_ALL);
        addTextFieldToForm("Menu.Form.ORD", new TextField(), "ord", "int", true, 100, "long>0", null, false, false, null, false, null, true, true, true, true, null);
        addSinglePopupToForm("Menu.Form.Parent", "parent_id", "int", false, 10, null, null, true, null, false, null, true, true, true, true, new PopupSingleSDFunctionAction(localMainUI), 2,
                null, "", "id", "description", "sd_function", null, null);        
        addCheckBoxToForm("Menu.Form.Status", new CheckBox(), "is_enable", "boolean", 
                true, 1, null, "Common.Status.Enable", false, true, null, false, null, true, true, true, true, "Common.Status.Enable", "Common.Status.Disable");
        addTextFieldToForm("SDFunction.ConfigFile", new TextField(), "config_file", "string", false, 100, null, null, true, false, null, false, null, true, true, true, true, null);        
        addCheckBoxToForm("SDFunction.IsExtend", new CheckBox(), "is_extend", "boolean", 
                false, 1, null, "SDFunction.IsExtend.Caption", false, false, null, false, null, true, true, true, true, "SDFunction.IsExtend.Enable", "SDFunction.IsExtend.Disable");
        addTextFieldToForm("SDFunction.VietNam", new TextField(), "viet_nam", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.English", new TextField(), "english", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        Object[][] columnInterface = {{"1","1"},{"2","2"},{"3","3"}};
        addComboBoxToForm("SDFunction.ColumnInterface", new ComboBox(), "column_interface", "int",
                false, 1, null, null, false, false, null, false, null, true, true, true, true, columnInterface, "2", "2");
        addTextFieldToForm("SDFunction.TableName", new TextField(), "table_name", "string", false, 30, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.ViewName", new TextField(), "view_name", "string", false, 30, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.IDColumnName", new TextField(), "id_column_name", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        Object[][] PageLengthData = {{"5","5"},{"10","10"},{"25","25"},{"50","50"},{"100","100"},{"600","600"}};
        addComboBoxToForm("SDFunction.PageLength", new ComboBox(), "page_length", "int",
                false, 10, null, null, false, false, null, false, null, true, true, true, true, PageLengthData, "25", "25");        
        Object[][] TableTypeData = {{"1","SDFunction.TableType.Normal"},{2,"SDFunction.TableType.NumberPage"},{3,"SDFunction.TableType.Tree"}};
        addComboBoxToForm("SDFunction.TableType", new ComboBox(), "table_type", "int",
                false, 10, null, null, false, false, null, false, null, true, true, true, true, TableTypeData, "1", "SDFunction.TableType.Normal");
        addTextFieldToForm("SDFunction.SortColumnName", new TextField(), "sort_column_name", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.ParentColumnName", new TextField(), "parent_column_name", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        Object[][] SortData = {{"1","SDFunction.ASC"},{"2","SDFunction.DESC"}};
        addComboBoxToForm("SDFunction.SortAscending", new ComboBox(), "sort_ascending", "int",
                false, 10, null, null, false, false, null, false, null, true, true, true, true, SortData, "1", "SDFunction.ASC");
        addTextFieldToForm("SDFunction.SequenceName", new TextField(), "sequence_name", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        
        // Bổ sung tree dữ liệu
        addTextFieldToForm("SDFunction.TreeVietNam", new TextField(), "tree_viet_nam", "string", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.TreeEnglish", new TextField(), "tree_english", "string", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.TreeIdColumn", new TextField(), "tree_id_column", "string", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.TreeNameColumn", new TextField(), "tree_name_column", "string", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.TreeParentColumn", new TextField(), "tree_parent_column", "string", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.TreeTableName", new TextField(), "tree_table_name", "string", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.TreeConnectColumn", new TextField(), "tree_connect_column", "string", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.WhereQuery", new TextField(), "where_query", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("SDFunction.TreeWhereQuery", new TextField(), "tree_where_query", "string", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addMultiPopupToForm("SDFunction.WhereParam", false, false, new PopupMultiFunctionParamAction(localMainUI), 2, null, "sd_func_param", "id", "function_id", null, null, null, null, null);
        addMultiPopupToForm("SDFunction.TreeWhereParam", false, false, new PopupMultiTreeParamAction(localMainUI), 2, null, "sd_tree_param", "id", "function_id", null, null, null, null, null);        
        // End: Bổ sung tree dữ liệu
        
        return initPanel(2);
    }
    
}
