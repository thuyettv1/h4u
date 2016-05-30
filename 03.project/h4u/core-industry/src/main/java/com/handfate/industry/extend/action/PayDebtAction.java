/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.MainUI;
import com.handfate.industry.core.action.BaseAction;
import com.handfate.industry.core.action.PopupSingleUserAction;
import com.handfate.industry.core.action.component.ComboboxItem;
import com.handfate.industry.core.action.component.ConfirmationDialog;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class PayDebtAction extends BaseAction {

    /**
     * Hàm khởi tạo giao diện
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI Vùng giao diện các chức năng
     * @return Giao diện sau khi khởi tạo
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khá»Ÿi táº¡o tham sá»‘
        setTableName("bm_invoice");
        setIdColumnName("invoice_id");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("invoice_id");
        setSortAscending(false);
        setSequenceName("bm_invoice_seq");
        setAllowAdd(false);
        setAllowDelete(false);
        setAllowEdit(false);
        
        //ThÃªm cÃ¡c thÃ nh pháº§n
        addTextFieldToForm("InvoiceID", new TextField(), "invoice_id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
//        addComponentToForm("Invoice.Code", new TextField(), "code", "string", false, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addSinglePopupToForm("Invoice.InvoiceNumber", "invoice_number", "int", true, 10, null, null, false, null, false, null, true, true, true, true, new PopupSingleInvoiceNumberAction(localMainUI), 2,
                null, "", "id", "num", "v_iv_num", null, null);
        setComponentAsLoginUser("User.CreateUser", "user_id", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null , null, INT_VIEWGROUP_ALL);
//        addComponentToForm("Invoice.fromDate", new PopupDateField(), "from_date", "date", false, 10, null, null, false, false, null, false, null, true, true, true, true, null);
//        addComponentToForm("Invoice.toDate", new PopupDateField(), "to_date", "date", false, 10, null, null, false, false, null, false, null, true, true, true, true, null);
        // Them customer ID theo hop dong 
        addSinglePopupToForm("Invoice.CustomerID", "customer_id", "int", false, 50, null, null, true, null, false, null, true, true, true, true, new PopupSingleCustomerAction(localMainUI), 2,
                null, "", "customer_id", "code", "bm_customer",null,null);
        addTextFieldToForm("Invoice.TotalCost", new TextField(), "cost", "long", false, 50, null, null, false, false, null, false, null, false, false, true, true, null);  
        Object[][] invoiceStatus = {{2, "Invoice.Paid"}, {3, "Invoice.Cancel"},{4, "Invoice.Debt"} };
        addComboBoxToForm("Invoice.Status", new ComboBox(), "status", "int",
                true, 50, null, null, true, false, null, false, null, true, true, true, true, invoiceStatus, "1", "Invoice.Unpaid");
        addMultiPopupToForm("Transaction.Code", true, false, new PopupMultiTransactionAction(localMainUI), 2, null, 
                "bm_sale_transaction", "transaction_id", "invoice_id", null, null, "customer_id", "customer_id", "v_tran_invoice");
        
//        addComponentToForm("Invoice.ServiceType", new TextField(), "service_type", "string", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
//        addComponentToForm("Invoice.SellerDepartmentName", new TextArea(), "department", "string", false, 200, null, null, false, false, null, false, null, true, true, true, true, null);
//        addComponentToForm("Invoice.PayReason", new TextArea(), "reason_sale", "string", false, 200, null, null, false, false, null, false, null, true, true, true, true, null);
//        setComponentAsSysdate("User.CreateDate", "create_date", true, "date:and_mandatory:40", false, defaultSearchValue);
        
        Date toDate = new Date();
        Date fromDate = new Date(toDate.getTime() - 60l * 24l * 3600l * 1000l);
        List lstParam = new ArrayList();
        lstParam.add(fromDate);
        lstParam.add(toDate);
        setComponentAsSysdate("User.CreateDate", "create_date", true, "date:and_mandatory:366", false, lstParam);
        addTextFieldToForm("Report.deleteDate", new PopupDateField(), "PAY_DATE", "date", false, 10, null, null, false, false, null, false, null, true, true, true, true, null);
        //setComponentAsLoginUser("Report.UserNameDelete", "PAY_USER", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null , null, INT_VIEWGROUP_ALL);
        addSinglePopupToForm("Report.UserNameDelete", "PAY_USER", "int", false, 50, null, null, true, null, false, null, true, true, true, true, new PopupSingleUserAction(localMainUI), 2,
                null, "", "user_id", "user_name", "SM_USERS",null,null);
        
        
        //AnhPTN: them nut gach no
        Button buttonPayDebt = new Button(ResourceBundleUtils.getLanguageResource("Button.PayDebt"));
        buttonPayDebt.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    buttonPayDebtClick();
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }                
            }
        });
        addButton(buttonPayDebt);
        return initPanel(2);
    }
    
    public void buttonPayDebtClick() throws Exception {
        Object[] deleteArray = ((java.util.Collection) table.getValue()).toArray();// ID cua nhung ban ghi da chon
        if (deleteArray != null && deleteArray.length > 0) {
            if (checkPermission(deleteArray)) {
                if(validatePayDebt(deleteArray)) {
                    ConfirmationDialog.Callback ccbl = new ConfirmationDialog.Callback() {
                        @Override
                        public void onDialogResult(String buttonName) {
                            Connection connection = null;
                            try {
                                if (buttonName.equals(ResourceBundleUtils.getLanguageResource("Common.Yes"))) {
                                    connection = C3p0Connector.getInstance().getConnection();
                                    connection.setAutoCommit(false);

                                    // cap nhat trang thai invoice status = 2 da thanh toan
                                    // cap nhat PAY_DATE, PAY_USES
                                    List lstBatch = new ArrayList();
                                    List invIDBatch = new ArrayList();
                                    List userRow = new  ArrayList();
                                    long userID = Long.parseLong(VaadinUtils.getSessionAttribute("G_UserId").toString());
                                    userRow.add(userID);
                                    userRow.add("long");
                                    List dateRow = new  ArrayList();
                                    dateRow.add(new Date());
                                    dateRow.add("date");
                                    String getAccSQL = "select DISTINCT tr.account_id from bm_sale_transaction tr " +
                                                            "left join bm_account ac on tr.account_id = ac.account_id " +
                                                            "where tr.invoice_id = ? " +
                                                            "and ac.status = 3 " +
                                                            "and ac.account_id not in ( " +
                                                            "select DISTINCT tr.account_id from bm_sale_transaction tr left join bm_invoice inv2 on tr.invoice_id = inv2.invoice_id " +
                                                            "where tr.account_id is not null " +
                                                            "and inv2.status = 1 " +
                                                            "and tr.invoice_id is not null " +
                                                            "and tr.invoice_id <> ?) ";
                                    String isrtAccHis ="insert into bm_account_his h (h.id_his, h.account_id, h.status_old, h.status_new, h.cause_change_his, " +
                                                        " h.change_user_login_id_his, h.change_date_his) " +
                                                        " select bm_account_his_seq.nextval, ?, 3, 5, 'Da thanh toan no cuoc'," +
                                                        " ?, ? from dual";
                                    String udAccStt = "update bm_account a set a.status = 5 where a.account_id = ?";
                                    for(int i = 0; i < deleteArray.length; i++) {
                                        List lstParameter = new ArrayList();
                                        List invIDParameter = new ArrayList();
                                        List invIDRow = new ArrayList();
                                        List getAccPara = new ArrayList();
                                        invIDRow.add(deleteArray[i]);
                                        invIDRow.add("long");
                                        getAccPara.add(deleteArray[i]);
                                        getAccPara.add(deleteArray[i]);
                                        lstParameter.add(userRow);
                                        lstParameter.add(dateRow);
                                        lstParameter.add(invIDRow);
                                        invIDParameter.add(invIDRow);
                                        lstBatch.add(lstParameter);
                                        invIDBatch.add(invIDParameter);
                                        // KHOI PHUC LAI CAC ACC BI KHOA
                                        // lay acc dang bi khoa trong cac hoa don, da duoc thanh toan het no
                                        List<Map> LaccID = C3p0Connector.queryData(getAccSQL, getAccPara,connection);
                                        List isrtAccLstBatch = new ArrayList();
                                        List udAccBatch =new ArrayList();
                                        for (int j = 0; j < LaccID.size(); j++){
                                            long accID = Long.parseLong(LaccID.get(j).get("account_id").toString());
                                            List accRow = new ArrayList();
                                            accRow.add(accID);
                                            accRow.add("long");
                                            List isrtAccLstPara =new ArrayList();
                                            isrtAccLstPara.add(accRow);
                                            isrtAccLstPara.add(userRow);
                                            isrtAccLstPara.add(dateRow);
                                            isrtAccLstBatch.add(isrtAccLstPara);
                                            List accPara = new ArrayList();
                                            accPara.add(accRow);
                                            udAccBatch.add(accPara);
                                        }
                                        if(LaccID.size()>0){ // co acc dang kho
                                            // luu lich su bm_account_his     
                                            C3p0Connector.excuteDataByTypeBatch(isrtAccHis,isrtAccLstBatch,connection);
                                            // Restoring cac acc dang bi khoa trong bm_account; acc status = 5
                                            C3p0Connector.excuteDataByTypeBatch(udAccStt,udAccBatch,connection);
                                        }
                                    }
                                    String cql = "update bm_invoice set status = 2, PAY_USER =?, PAY_DATE = ? "
                                            + " where invoice_id = ? ";
                                    C3p0Connector.excuteDataByTypeBatch(cql, lstBatch, connection);
                                    // cap nhat tran status = 3 da thanh toan
                                    String sql = "update bm_sale_transaction set status = 3 where invoice_id =?";
                                    C3p0Connector.excuteDataByTypeBatch(sql, invIDBatch, connection);
                                    
                                    // Cập nhật history
                                    String sqlHis1 = " insert into bm_iv_num_HIS (id, action, action_date, invoice_number_id, change_user) "
                                            + " values (bm_iv_num_HIS_SEQ.nextval, 7 , sysdate, ?, ?)";
                                    List lstBatch1 = new ArrayList();
                                    for (int i = 0; i < deleteArray.length; i++) {
                                        Item item = table.getItem(deleteArray[i]);
                                        ComboboxItem cboItem = (ComboboxItem)(item.getItemProperty(ResourceBundleUtils.getLanguageResource("Invoice.InvoiceNumber")).getValue());
                                        if(cboItem.getValue() != null && !cboItem.getValue().isEmpty()) {
                                            List lstParameter = new ArrayList();
                                            List lstRow1 = new ArrayList();
                                            lstRow1.add(cboItem.getValue());
                                            lstRow1.add("long");
                                            lstParameter.add(lstRow1);
                                            List lstRow2 = new ArrayList();
                                            lstRow2.add(VaadinUtils.getSessionAttribute("G_UserId").toString());
                                            lstRow2.add("long");
                                            lstParameter.add(lstRow2);
                                            lstBatch1.add(lstParameter);
                                        }
                                    }
                                    C3p0Connector.excuteDataByTypeBatch(sqlHis1, lstBatch1, connection);
                                    
                                    String sqlHis2 = " update bm_iv_num set status = 6 where id = ? ";
                                    List lstBatch2 = new ArrayList();
                                    for (int i = 0; i < deleteArray.length; i++) {
                                        Item item = table.getItem(deleteArray[i]);
                                        ComboboxItem cboItem = (ComboboxItem)(item.getItemProperty(ResourceBundleUtils.getLanguageResource("Invoice.InvoiceNumber")).getValue());
                                        if(cboItem.getValue() != null && !cboItem.getValue().isEmpty()) {
                                            List lstParameter = new ArrayList();
                                            List lstRow1 = new ArrayList();
                                            lstRow1.add(cboItem.getValue());
                                            lstRow1.add("long");
                                            lstParameter.add(lstRow1);
                                            lstBatch2.add(lstParameter);
                                        }
                                    }
                                    C3p0Connector.excuteDataByTypeBatch(sqlHis2, lstBatch2, connection);
                                    
                                    connection.commit();

                                    updateDataRefreshData();
                                    Notification.show(ResourceBundleUtils.getLanguageResource("Common.PayDebtSuccess"),
                                            null, Notification.Type.WARNING_MESSAGE);
                                }
                            } catch (Exception ex) {
                                if(connection != null) try {
                                    connection.rollback();
                                } catch (SQLException ex1) {
                                    VaadinUtils.handleException(ex);
                                    MainUI.mainLogger.debug("Install error: ", ex1);
                                }
                                VaadinUtils.handleException(ex);
                                MainUI.mainLogger.debug("Install error: ", ex);
                            } finally {
                                if(connection != null) try {
                                    connection.close();
                                } catch (SQLException ex) {
                                    VaadinUtils.handleException(ex);
                                    MainUI.mainLogger.debug("Install error: ", ex);
                                }
                                connection = null;
                            }
                        }
                    };
                    mainUI.addWindow(new ConfirmationDialog(
                            ResourceBundleUtils.getLanguageResource("Common.Confirm"),
                            ResourceBundleUtils.getLanguageResource("Common.ConfirmExecute"), ccbl));
                }
            }
        } else {
            Notification.show(ResourceBundleUtils.getLanguageResource("Common.SelectRequireAtLeast1"),
                    null, Notification.Type.ERROR_MESSAGE);
        }        
    }
    // anhptn
    private long getInvoiceStatus (long invID) throws Exception{
        String sql = "select status from bm_invoice where invoice_id = ?";
        List invIDParameter = new ArrayList();
        invIDParameter.add(invID);
        List<Map> LInvStt = C3p0Connector.queryData(sql, invIDParameter);
        long invStt = Long.parseLong(LInvStt.get(0).get("status").toString());
        return invStt;
    }
    
    // anhptn
    public boolean validatePayDebt(Object[] selectedArray) throws Exception {
        // dang o trang thai chua thanh toan
        int i = 0;
        String notify = "Invoice Status Invalid";
        long invID =0, invStt = 0;
        for ( i = 0; i <selectedArray.length; i++ ){
            invID = Integer.parseInt( selectedArray[i].toString());
            invStt = getInvoiceStatus(invID);
            if (invStt!=1 && invStt!=4){
                if (invStt == 2) notify = "Invoice.Error.Payed";
                if (invStt == 3) notify = "Invoice.Error.Canceled";
                Notification.show(ResourceBundleUtils.getLanguageResource(notify),
                                            null, Notification.Type.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }
}
