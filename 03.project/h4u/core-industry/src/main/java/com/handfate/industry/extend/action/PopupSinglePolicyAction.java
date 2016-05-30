/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import static com.handfate.industry.core.action.BaseAction.INT_VIEWGROUP_RECURSIVE;
import com.handfate.industry.core.action.PopupSingleAction;
import com.handfate.industry.core.action.PopupSingleUserAction;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author YenNTH8
 */
public class PopupSinglePolicyAction extends PopupSingleAction {
    /**
     * Hàm khởi tạo giao diện
     *
     * @since 19/11/2014 YenNTH8
     * @param localMainUI Vùng giao diện của chức năng
     */
    public PopupSinglePolicyAction(UI localMainUI) throws Exception {
        //Khởi tạo tham số
        setTableName("v_policy_postage");
        setIdColumnName("policy_id");
        setNameColumn("code");
        setPageLength(10); 
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("code");      
        setSortAscending(true);
        //Thêm các thành phần
        addTextFieldToForm("PolicyID", new TextField(), "policy_id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Policy.code", new TextField(), "code", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("Policy.name", new TextField(), "name", "string", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);
//        addComponentToForm("Policy.discountContract", new TextField(), "discount_contract", "float", false, 5, "float>=0<=100", null, false, false, null, false, null, true, true, true, true, null);
//        addComponentToForm("Policy.discountPostage", new TextField(), "discount_postage", "float", false, 5, "float>=0<=100", null, false, false, null, false, null, true, true, true, true, null);                       
        addTextFieldToForm("Policy.fromDate", new PopupDateField(), "from_date", "date", true, 50, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Policy.toDate", new PopupDateField(), "to_date", "date", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addSinglePopupToForm("Policy.ServiceID", "service_id", "int", false, 50, null, null, false, null, false, null, true, true, true, false, new PopupSingleServiceAction(localMainUI), 2,
                null, "", "service_id", "code", "bm_service",null,null); 
        
        addTextFieldToForm("Postage.code", new TextField(), "postage_code", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Postage.name", new TextField(), "postage_name", "string", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Postage.price", new TextField(), "POSTAGE_PRICE", "int", false, 10, null, null, false, false, null, false, null, true, true, true, true, null);    
        addTextFieldToForm("Postage.period", new TextField(), "period", "int", true, 10, null, null, false, false, null, false, null, true, true, true, true, null);
        Object[][] postageType = {{2, "Postage.Postpaid"}};
        addComboBoxToForm("Postage.type", new ComboBox(), "type", "int",
                false, 50, null, null, false, false, null, false, null, true, true, true, true, postageType, "1", "Postage.prepay");
        }
    @Override 
    public void prepareOpenPopup() throws Exception {
        setIsChangeDefaultSearch(true);
        String query = " and 1 = 1 ";
        if(getParent() instanceof ContractAction) {
            query = " and service_id = ? and paid_type = ? and sysdate BETWEEN from_date and to_date + 1 ";
            long serID = 0, type = 0;
            AbstractField abfSerID = getParent().getComponent("service_id");
            AbstractField abfPaidType = getParent().getComponent("paid_type");
            if(abfSerID.getValue() == null || abfPaidType.getValue() == null){
                Notification.show(ResourceBundleUtils.getLanguageResource("PopupPolicy.Error.ChoseServiceAndPaidType"),
                                                null, Notification.Type.ERROR_MESSAGE);
                return;
            }
            serID = Long.parseLong(abfSerID.getValue().toString());
            type = Long.parseLong(abfPaidType.getValue().toString());
            List findPolicyPara = new ArrayList();
            findPolicyPara.add(serID);
            findPolicyPara.add(type);
            setQueryWhereParameter(findPolicyPara);
        }
        if(getParent() instanceof DiscountAction){
            query  = " and sysdate BETWEEN from_date and to_date + 1 " ;
        }
        setQueryWhereCondition(query);
    }
}
