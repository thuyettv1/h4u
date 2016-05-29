/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.BaseAction;
import static com.handfate.industry.core.action.BaseAction.INT_VIEWGROUP_ALL;
import com.handfate.industry.core.action.PopupSingleUserAction;
import com.handfate.industry.core.action.component.UploadField;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class DiscountAction extends BaseAction  {

    public HorizontalLayout init(UI localMainUI) throws Exception {
        setTableName("BM_Discount");
        setIdColumnName("ID");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("POLICY_ID");
        setSortAscending(false);
        setSequenceName("BM_Discount_SEQ");

        addTextFieldToForm("id", new TextField(), "ID", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addSinglePopupToForm("Contract.policyID", "policy_id", "int", true, 50, null, null, true, null, false, null, true, true, true, true, new PopupSinglePolicyAction(localMainUI), 2,
                null, "", "policy_id", "code", "v_policy_postage",null,null);                 
        addTextFieldToForm("Policy.discountContract", new TextField(), "DISCOUNT_CONTRACT", "int", true, 5, "float>=0<=100", null, false, false, null, false, null, true, true, true, true, null);
        
        Object[][] postageType = {{0, "Service.inActive"}};
        addComboBoxToForm("Discount.status", new ComboBox(), "status", "int",
                true, 50, null, null, true, false, null, false, null, true, true, true, true, postageType, "1", "Service.active");
        addTextFieldToForm("Good.note", new TextArea(), "NOTE", "string", false, 500, null, null, false, false, null, false, null, true, true, true, true, null);        
        
        Date toDate = new Date();
        Date fromDate = new Date(toDate.getTime() - 60l * 24l * 3600l * 1000l);
        List lstParam = new ArrayList();
        lstParam.add(fromDate);
        lstParam.add(toDate);
        setComponentAsSysdate("User.CreateDate", "create_date", true, "date:and_mandatory:366", false, lstParam);
        setComponentAsLoginUser("User.CreateUser", "create_user", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null , null, INT_VIEWGROUP_ALL);        
        
        return initPanel(2);//So column dc chia tren giao dien
    }    
    @Override 
    public boolean validateEdit (long DisID) throws Exception{
        // discount da duoc su dung thi khong duoc sua
        String sql = "select * from bm_contract where discount_id = ?  ";
        List Para = new ArrayList();
        Para.add(DisID);
        List<Map> LCon = C3p0Connector.queryData(sql, Para);
        if (!LCon.isEmpty()) {
            Notification.show(ResourceBundleUtils.getLanguageResource("Discount.Error.IsUsed"),
                                            null, Notification.Type.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
