/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.BaseAction;
import static com.handfate.industry.core.action.BaseAction.INT_VIEWGROUP_ALL;
import static com.handfate.industry.core.action.BaseAction.INT_VIEWGROUP_RECURSIVE;
import com.handfate.industry.core.action.PopupSingleAction;
import com.handfate.industry.core.action.PopupSingleUserAction;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author YenNTH8
 */
public class PopupSingleDiscountAction extends PopupSingleAction {
    /**
     * Hàm khởi tạo giao diện
     *
     * @since 19/11/2014 YenNTH8
     * @param localMainUI Vùng giao diện của chức năng
     */
    public PopupSingleDiscountAction(UI localMainUI) throws Exception {
        //Khởi tạo tham số
        setTableName("bm_discount");
        setIdColumnName("id");
        setNameColumn("DISCOUNT_CONTRACT");
        setPageLength(10); 
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("DISCOUNT_CONTRACT");      
        setSortAscending(true);
        //Thêm các thành phần
        addTextFieldToForm("id", new TextField(), "ID", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addSinglePopupToForm("Contract.policyID", "policy_id", "int", true, 50, null, null, true, null, false, null, true, true, true, true, new PopupSinglePolicyAction(localMainUI), 2,
                null, "", "policy_id", "code", "v_policy_postage",null,null);                 
        addTextFieldToForm("Policy.discountContract", new TextField(), "DISCOUNT_CONTRACT", "int", true, 5, "float>=0<=100", null, false, false, null, false, null, true, true, true, true, null);
        Object[][] postageType = {{0, "Service.inActive"}};
        addComboBoxToForm("Discount.status", new ComboBox(), "status", "int",
                true, 50, null, null, true, false, null, false, null, true, true, true, true, postageType, "1", "Service.active");
        addTextFieldToForm("Good.note", new TextArea(), "NOTE", "string", false, 500, null, null, false, false, null, false, null, true, true, true, true, null);        
        setComponentAsSysdate("Discount.createDate","CREATE_DATE",false,null,true,null);
        setComponentAsLoginUser("User.CreateUser", "create_user", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null , null, INT_VIEWGROUP_ALL);        

        }
    @Override 
    public void prepareOpenPopup() throws Exception {
        setIsChangeDefaultSearch(true);
        String query = "";
        if(getParent() instanceof ContractAction){
            if(getParent().getCurrentForm() != BaseAction.INT_SEARCH_FORM){
                query = " and status = 1 ";
            }
        }
        setQueryWhereCondition(query + " and policy_id = ? ");
        List findPolicyPara = new ArrayList();
        findPolicyPara.add(getParent().getComponent("policy_id").getValue());
        setQueryWhereParameter(findPolicyPara);
    }
}
