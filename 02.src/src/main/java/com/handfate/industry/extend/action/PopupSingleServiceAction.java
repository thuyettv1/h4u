/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.BaseAction;
import static com.handfate.industry.core.action.BaseAction.INT_VIEWGROUP_RECURSIVE;
import com.handfate.industry.core.action.PopupSingleAction;
import com.handfate.industry.core.action.PopupSingleGroupAction;
import com.handfate.industry.core.action.PopupSingleUserAction;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 *
 * @author YenNTH8
 */
public class PopupSingleServiceAction extends PopupSingleAction {
    /**
     * Hàm khởi tạo giao diện
     *
     * @since 19/11/2014 YenNTH8
     * @param localMainUI Vùng giao diện của chức năng
     */
    public PopupSingleServiceAction(UI localMainUI) throws Exception {
        //Khởi tạo tham số
        setTableName("bm_service");
        setIdColumnName("service_id");
        setNameColumn("name");
        setPageLength(25);     
        setMainUI(localMainUI);
        setSortColumnName("code");      
        setSortAscending(true);
//        setSequenceName("bm_contract_seq");

        //Thêm các thành phần
        addTextFieldToForm("serviceID", new TextField(), "service_id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Service.code", new TextField(), "code", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Service.name", new TextField(), "name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);

//        Object[][] serviceStatus = {{2, "Service.statusTest"}};
//        addComboBoxToForm("Service.status", new ComboBox(), "status", "int",
//                false, 50, null, null, true, false, null, false, null, true, true, true, true, serviceStatus, "1", "Service.statusDeploy");
        
        Object[][] activeStatus = {{2, "Service.inActive"}};
        addComboBoxToForm("Service.activeStatus", new ComboBox(), "active_status", "int",
                true, 50, null, null, true, false, null, false, null, true, true, true, true, activeStatus, "1", "Service.active");
        
        addTextFieldToForm("Service.startCost", new TextField(), "START_COST", "int", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addSinglePopupToForm("Service.GroupId", "group_id", "int", true, 50, null, null, true, null, false, null, true, true, true, true, new PopupSingleGroupAction(localMainUI), 2,
                null, "", "group_id", "group_name", "sm_group",null,null);

        addTextFieldToForm("Service.note", new TextArea(), "note", "string", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        setComponentAsSysdate("User.CreateDate", "created_date", false, null, false, null);
        setComponentAsLoginUser("User.CreateUser", "create_user", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null , null, INT_VIEWGROUP_RECURSIVE);
        }
    @Override
    public void prepareOpenPopup() throws Exception {
        if(getParent() instanceof PolicyAcion || getParent() instanceof ContractAction || getParent() instanceof PostageInfoAction ){
            setIsChangeDefaultSearch(true);                    
            // neu la add, edit --> co dkien
            if(getParent().getCurrentForm()!= BaseAction.INT_SEARCH_FORM ){
                setQueryWhereCondition(" and active_status = 1");
            }
        }
    }
}
