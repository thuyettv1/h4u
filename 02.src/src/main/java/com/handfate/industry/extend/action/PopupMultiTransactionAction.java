package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.PopupMultiAction;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.text.DecimalFormat;

/**
 * @since 14/11/2014
 * @author HienDM
 */

public class PopupMultiTransactionAction extends PopupMultiAction {

    /**
     * Hàm khởi tạo giao diện
     * @since 19/11/2014 HienDM
     * @param localMainUI Vùng giao diện của chức năng
     */ 
    public PopupMultiTransactionAction(UI localMainUI) throws Exception {
        //Khởi tạo tham số
        setTableName("v_tran_invoice");
        setIdColumnName("transaction_id");
        setNameColumn("transaction_code");
        setPageLength(5);
        setTableType(INT_NORMAL_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("to_date");
        setSortAscending(false);
        addQueryWherePopup(" and status = 1 "); // chưa lập hóa đơn
                
      //  setSequenceName("bm_sale_transaction_seq");
        setAllowAdd(false);
        setAllowDelete(false);
        setAllowEdit(false);
        //ThÃªm cÃ¡c thÃ nh pháº§n
        addTextFieldToForm("TransactionID", new TextField(), "transaction_id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Transaction.Code", new TextField(), "transaction_code", "string", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Transaction.type", new TextField(), "TYPE_TRANS", "string", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.code", new TextField(), "CONTRACT_CODE", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addSinglePopupToForm("Invoice.CustomerID", "customer_id", "int", true, 50, null, null, false, null, false, null, true, true, true, true, new PopupSingleCustomerAction(localMainUI), 2,
                null, "", "customer_id", "code", "bm_customer",null,null);
        addTextFieldToForm("Transaction.Account", new TextField(), "account_name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addComboBoxToForm("Policy.ServiceID", new ComboBox(), "service_id", "int",
                true, 50, null, null, true, false, null, false, null, true, true, true, true, "select service_id, code from bm_service", null, "bm_service", 
                "service_id", "int", "code",null, null,false, true,null, null);  
        addTextFieldToForm("Transaction.fromDate", new PopupDateField(), "from_date", "date", false, null, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Transaction.toDate", new PopupDateField(), "to_date", "date", false, null, null, null, false, false, null, false, null, true, true, true, true, null);       
        addTextFieldToForm("Transaction.cost", new TextField(), "cost", "int", true, 10, null, null, false, false, null, false, null, true, true, true, true, null);
    
    }
    @Override
    public void finishUpdateDataToParent() throws Exception {
        if(getParent() instanceof InvoiceAction ){
            InvoiceAction invoiceAction = (InvoiceAction)getParent();
//            DecimalFormat numFormat;
//            numFormat = new DecimalFormat("#,###,###");

//            invoiceAction.txtTotalCost.setValue(String.valueOf(numFormat.format(invoiceAction.getTranSumCost())));         
            invoiceAction.getComponent("cost").setValue(String.valueOf(invoiceAction.getTranSumCost()));         
        }
    }
}
