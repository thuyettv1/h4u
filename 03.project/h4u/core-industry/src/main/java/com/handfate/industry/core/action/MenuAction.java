package com.handfate.industry.core.action;

import com.handfate.industry.core.oracle.C3p0Connector;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * @since 14/11/2014
 * @author HienDM
 */

public class MenuAction extends BaseAction {

    /**
     * Hàm khởi tạo giao diện
     * @since 19/11/2014 HienDM
     * @param localMainUI Vùng giao diện của chức năng
     * @return Giao diện sau khi khởi tạo
     */ 
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khởi tạo tham số
        setTableName("sm_menu");
        setIdColumnName("menu_id");
        setPageLength(25);
        setTableType(INT_TREE_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("ord");
        setParentColumnName("parent_id");
        setRootId("0");
        setSortAscending(true);
        setSequenceName("sm_menu_seq");
        
        //Thêm các thành phần
        addTextFieldToForm("MenuID", new TextField(), "menu_id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Menu.Form.Description", new TextField(), "description", "string", true, 200, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.Key", new TextField(), "menu_name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Menu.Form.ORD", new TextField(), "ord", "int", true, 100, "long>0", null, false, false, null, false, null, true, true, true, true, null);
        addSinglePopupToForm("Menu.Form.Parent", "parent_id", "int", false, 10, null, null, true, null, false, null, true, true, true, true, new PopupSingleMenuAction(localMainUI), 2,
                null, "", "menu_id", "description", "sm_menu", null, null);
        addCheckBoxToForm("Menu.Form.Status", new CheckBox(), "is_enable", "boolean", 
                true, 1, null, "Common.Status.Enable", false, false, null, false, null, true, true, true, true, "Common.Status.Enable", "Common.Status.Disable");
        setComponentAsLoginUser("User.CreateUser", "create_user", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null , null, INT_VIEWGROUP_ALL);
        return initPanel(1);
    }
    
    @Override
    public void afterDeleteData(Connection connection, Object[] deleteArray) throws Exception {
        String sql = " delete sm_role_menu where menu_id = ? ";
        List<List> lstBatch = new ArrayList();
        for(int i = 0; i < deleteArray.length; i++) {
            List lstParameter = new ArrayList();
            List lstRow = new ArrayList();
            lstRow.add(deleteArray[i].toString());
            lstRow.add("long");
            lstParameter.add(lstRow);
            lstBatch.add(lstParameter);
        }
        C3p0Connector.excuteDataByTypeBatch(sql, lstBatch, connection);
    }
}
