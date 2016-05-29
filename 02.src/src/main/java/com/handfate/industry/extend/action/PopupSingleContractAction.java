/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.BaseAction;
import com.handfate.industry.core.action.PopupSingleAction;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author YenNTH8
 */
public class PopupSingleContractAction extends PopupSingleAction {
    /**
     * Hàm khởi tạo giao diện
     *
     * @since 19/11/2014 YenNTH8
     * @param localMainUI Vùng giao diện của chức năng
     */
    public PopupSingleContractAction(UI localMainUI) throws Exception {
        //Khởi tạo tham số
        setTableName("bm_contract");
        setIdColumnName("contract_id");
        setNameColumn("code");
        setPageLength(10);     
        setMainUI(localMainUI);
        setSortColumnName("code");      
        setSortAscending(true);
        setSequenceName("bm_contract_seq");

        //Thêm các thành phần
        addTextFieldToForm("ContractID", new TextField(), "CONTRACT_ID", "int", true, 50, null, null, false, false,null, false, null, true, true, true, true, null);           
        addTextFieldToForm("Contract.code", new TextField(), "code", "string", true, 50, null, null, true, false,null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.name", new TextField(), "name", "string", true, 100, null, null, true, false,null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.number", new TextField(), "CONTRACT_NUMBER", "string", true, 50, null, null, true, false,null, false, null, true, true, true, true, null);     
        addComboBoxToForm("Contract.customer", new ComboBox(), "CUSTOMER_ID", "int",
                true, 50, null, null, true, false, null, false, null, true, true, true, true, "select CUSTOMER_ID, code from bm_customer", null, "bm_customer", 
                "CUSTOMER_ID", "int", "code", null, null, false, true,null,null); 
        addTextFieldToForm("Contract.delegate", new TextField(), "delegate", "string", true, 50, null, null, true, false,null, false, null, true, true, true, true, null);
        addComboBoxToForm("Policy.ServiceID", new ComboBox(), "service_id", "int",
                true, 50, null, null, true, false, null, false, null, true, true, true, true, "select service_id, code from bm_service", null, "bm_service", 
                "service_id", "int", "code", null, null, false, true,null,null);
        addTextFieldToForm("Contract.accout", new TextField(), "AMOUNT_OF_ACCOUNT", "int", false, 10, null, null, false, false, null, false, null, true, true, true, true, null);  
        }
    @Override
    public void prepareOpenPopup() throws Exception {
        String query = " and 1 = 1 ";
        setQueryWhereCondition("");
        setQueryWhereParameter(new ArrayList());
        setIsChangeDefaultSearch(true);    
        if(getParent() instanceof AcceptanceAction){
            // neu la add, edit --> co dkien
            query = " and status = 1 and create_user = ?  ";
            List lstParam = new ArrayList();
            lstParam.add(Integer.parseInt(VaadinUtils.getSessionAttribute("G_UserId").toString()));
            setQueryWhereParameter(lstParam);
        }
        if(getParent() instanceof AccountAction){
            // neu la add, edit --> co dkien
            if(getParent().getCurrentForm()!= BaseAction.INT_SEARCH_FORM ){
                query = " and status = 1 and amount_of_account>(select count(b.contract_id) from bm_account b where bm_contract.contract_id = b.contract_id) ";
            }
        }
        setQueryWhereCondition(query);
    }
}
