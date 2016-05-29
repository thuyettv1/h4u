package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.PopupMultiAction;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 * @since 14/11/2014
 * @author HienDM
 */

public class PopupMultiContractGoodAction extends PopupMultiAction {

    /**
     * Hàm khởi tạo giao diện
     * @since 19/11/2014 HienDM
     * @param localMainUI Vùng giao diện của chức năng
     */ 
    public PopupMultiContractGoodAction(UI localMainUI) throws Exception {
        //Khởi tạo tham số
        setTableName("v_policy_good");
        setIdColumnName("POLICY_GOOD_ID");
        setNameColumn("GOOD_CODE");
        setPageLength(5);
        setTableType(INT_NORMAL_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("POLICY_CODE");        
        setSortAscending(true);
     //   setSequenceName("bm_good_seq");
        
        //Thêm các thành phần
        addTextFieldToForm("PolicyGoodID", new TextField(), "POLICY_GOOD_ID", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Good.code", new TextField(), "GOOD_CODE", "string", true, 200, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Policy.code", new TextField(), "POLICY_CODE", "string", true, 200, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Good.price", new TextField(), "PRICE", "int", true, 10, null, null, false, false, null, false, null, true, true, true, true, null);
        
       
    }
}
