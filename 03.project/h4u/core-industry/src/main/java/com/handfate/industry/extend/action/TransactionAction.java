/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.MainUI;
import com.handfate.industry.core.action.BaseAction;
import com.handfate.industry.core.action.component.ComboboxItem;
import com.handfate.industry.core.action.component.ConfirmationDialog;
import com.handfate.industry.core.action.component.UploadField;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class TransactionAction extends BaseAction {

    /**
     * HÃ m khá»Ÿi táº¡o giao diá»‡n
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI VÃ¹ng giao diá»‡n cá»§a chá»©c nÄƒng
     * @return Giao diá»‡n sau khi khá»Ÿi táº¡o
     */
    TextField txtReason = new TextField();

    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khá»Ÿi táº¡o tham sá»‘
        setTableName("v_sale_transaction");
        setIdColumnName("transaction_id");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("transaction_id");
        setSortAscending(false);
        //  setSequenceName("bm_sale_transaction_seq");
        setAllowAdd(false);
        setAllowDelete(false);
        setAllowEdit(false);
        
        setQueryWhereCondition(" AND create_user IN (SELECT   user_id\n" +
                                "                           FROM   sm_users\n" +
                                "                          WHERE   GROUP_ID IN (SELECT   GROUP_ID\n" +
                                "                                                 FROM   v_group\n" +
                                "                                                WHERE   PATH LIKE ?)) ");
        List lstParameter = new ArrayList();
        lstParameter.add("%/" + VaadinUtils.getSessionAttribute("G_GroupId").toString() + "/%");
        setQueryWhereParameter(lstParameter);
        
        //ThÃªm cÃ¡c thÃ nh pháº§n
        addTextFieldToForm("TransactionID", new TextField(), "transaction_id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        //  addComponentToForm("Transaction.AccountCreater", new TextField(), "code", "string", true, 100, null, null, true, false);             

        addSinglePopupToForm("Account.Contract", "contract_id", "int", true, 50, null, null, true, null, false, null, true, true, true, true, new PopupSingleContractAction(localMainUI), 2,
                null, "", "contract_id", "code", "bm_contract", null, null);
        addTextFieldToForm("Transaction.Account", new TextField(), "account_name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Service.code", new TextField(), "SERVICE_CODE", "string", false, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Postage.code", new TextField(), "POSTAGE_CODE", "string", false, 100, null, null, true, false, null, false, null, true, true, true, true, null);

        Date toDate = new Date();
        Date fromDate = new Date(toDate.getTime() - 60l * 24l * 3600l * 1000l);
        List lstParam = new ArrayList();
        lstParam.add(fromDate);
        lstParam.add(toDate);
        addTextFieldToForm("Transaction.fromDate", new PopupDateField(), "from_date", "date", false, null, null, null, true, false, "date:and_mandatory:366", false, lstParam, true, true, true, true, null);
        addTextFieldToForm("Transaction.toDate", new PopupDateField(), "to_date", "date", false, null, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Transaction.cost", new TextField(), "cost", "int", true, 10, null, null, false, false, null, false, null, true, true, true, true, null);
        Object[][] transactionStatus = {{2, "Transaction.invoiced"}, {3, "Transaction.payment"}, {4, "Transaction.cancel"}};
        addComboBoxToForm("Transaction.status", new ComboBox(), "status", "int",
                false, 50, null, null, true, false, null, false, null, true, true, true, true, transactionStatus, "1", "Transaction.NoInvoice");

        //Ly do huy giao dich
        /*
        addCustomizeToSearchForm("History.Reason", txtReason, "string", false, 100, null, null, false, false, false, true, true, null);
        Button buttonDeleteTran = new Button(ResourceBundleUtils.getLanguageResource("Button.DeleteTran"));
        buttonDeleteTran.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    Object[] deleteArray = ((java.util.Collection) table.getValue()).toArray();// ID cua nhung ban ghi da chon
                    if (validateStopTran(deleteArray)) {
                        buttonStopTranClick();
                    }
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
         addButton(buttonDeleteTran);*/
        return initPanel(2);
    }

    public boolean validateStopTran(Object[] selectedArray) throws Exception {
        // dang o trang thai chua thanh toan
        int i = 0;
        String notify = "Tran Status NoInvoice";
        for (i = 0; i < selectedArray.length; i++) {
            Item data = table.getItem(selectedArray[i]);
            Object comboData = data.getItemProperty(ResourceBundleUtils.getLanguageResource("Transaction.status")).getValue();
            int conStatus = Integer.parseInt(((ComboboxItem) comboData).getValue().toString());
            if (conStatus != 1) {
                Notification.show(ResourceBundleUtils.getLanguageResource(notify),
                        null, Notification.Type.ERROR_MESSAGE);
                return false;
            }
        }
        if (txtReason.getValue() == null || txtReason.getValue().trim().isEmpty()) {
            Notification.show(ResourceBundleUtils.getLanguageResource("InvoiceNumber.Error.ReasonRequire"),
                    null, Notification.Type.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public void buttonStopTranClick() throws Exception {
        Object[] deleteArray = ((java.util.Collection) table.getValue()).toArray();// ID cua nhung ban ghi da chon
        if (deleteArray != null && deleteArray.length > 0) {
            if (checkPermission(deleteArray)) {
                ConfirmationDialog.Callback ccbl = new ConfirmationDialog.Callback() {
                    @Override
                    public void onDialogResult(String buttonName) {
                        Connection connection = null;
                        try {
                            if (buttonName.equals(ResourceBundleUtils.getLanguageResource("Common.Yes"))) {
                                connection = C3p0Connector.getInstance().getConnection();
                                connection.setAutoCommit(false);
                                List lstBatch = new ArrayList();
                                String updateTran = "update bm_sale_transaction set status = 4,reason_stop = ? where transaction_id = ?";
                                for (int i = 0; i < deleteArray.length; i++) {
                                    List lstParameter = new ArrayList();
                                    List lstReason = new ArrayList();
                                    lstReason.add(txtReason.getValue());
                                    lstReason.add("string");
                                    lstParameter.add(lstReason);

                                    List lstTranID = new ArrayList();
                                    lstTranID.add(deleteArray[i]);
                                    lstTranID.add("long");
                                    lstParameter.add(lstTranID);
                                    lstBatch.add(lstParameter);
                                }

                                C3p0Connector.excuteDataByTypeBatch(updateTran, lstBatch, connection);

                                connection.commit();
                                Notification.show(ResourceBundleUtils.getLanguageResource("Transaction.CancelSuccess"),
                                        null, Notification.Type.WARNING_MESSAGE);
                                updateDataRefreshData();
                                table.setValue(deleteArray[0]);
                            }
                        } catch (Exception ex) {
                            VaadinUtils.handleException(ex);
                            MainUI.mainLogger.debug("Install error: ", ex);
                        } finally {
                            if (connection != null) {
                                try {
                                    connection.close();
                                } catch (Exception ex) {
                                    VaadinUtils.handleException(ex);
                                    MainUI.mainLogger.debug("Install error: ", ex);
                                }
                            }
                            connection = null;
                        }
                    }
                };
                mainUI.addWindow(new ConfirmationDialog(
                        ResourceBundleUtils.getLanguageResource("Common.Confirm"),
                        ResourceBundleUtils.getLanguageResource("Common.ConfirmExecute"), ccbl));
            }
        } else {
            Notification.show(ResourceBundleUtils.getLanguageResource("Common.SelectRequireAtLeast1"),
                    null, Notification.Type.ERROR_MESSAGE);
        }
    } 
}
