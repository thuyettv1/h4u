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
import com.handfate.industry.core.action.component.UploadField;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.handfate.industry.extend.util.GetDateOld;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.data.Item;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
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
public class AccountAction extends BaseAction {

    /*YenNTH8
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {

        setTableName("bm_account");
        setIdColumnName("ACCOUNT_ID");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("ACCOUNT_ID");
        setSortAscending(false);
        setSequenceName("bm_account_seq");
        addTextFieldToForm("AccountID", new TextField(), "ACCOUNT_ID", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Account.name", new TextField(), "NAME", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        Object[][] accountStatus = {{1, "Account.Active"}, {2, "Account.RequestLock"}, {3, "Account.DebtLock"}, {4, "Account.Delete"}, {5, "Account.Restoring"}};
        addComboBoxToForm("Account.status", new ComboBox(), "status", "int",
                false, 50, null, null, true, false, null, false, null, false, false, false, false, accountStatus, "0", "Account.NotActive");
        // Account gan vao hop dong nao
        addSinglePopupToForm("Account.Contract", "contract_id", "int", true, 50, null, null, true, null, false, null, true, true, true, true, new PopupSingleContractAction(localMainUI), 2,
                null, "", "contract_id", "code", "bm_contract", null, null);
        addTextFieldToForm("Account.startDate", new PopupDateField(), "start_date", "date", false, 10, null, null, true, false, null, false, null, false, false, true, true, null);

        // Set tham so tim kiem mac dinh
        Date toDate = new Date();
        Date fromDate = new Date(toDate.getTime() - 60l * 24l * 3600l * 1000l);
        List lstParam = new ArrayList();
        lstParam.add(fromDate);
        lstParam.add(toDate);
        setComponentAsSysdate("User.CreateDate", "create_date", true, "date:and_mandatory:366", false, lstParam);
        setComponentAsLoginUser("User.CreateUser", "create_user", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null, null, INT_VIEWGROUP_RECURSIVE);

        //Them nut khoi phuc account
        Button buttonRestore = new Button(ResourceBundleUtils.getLanguageResource("Button.RestoreAccount"));
        buttonRestore.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    Object[] selectArray = ((java.util.Collection) table.getValue()).toArray();// ID cua nhung ban ghi da chon
                    if (validateRestore(selectArray)) {
                        restoreAccountClick(1);
                    }
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        addButton(buttonRestore);
        Button buttonDelete = new Button(ResourceBundleUtils.getLanguageResource("Button.DeleteAccount"));
        buttonDelete.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    Object[] selectArray = ((java.util.Collection) table.getValue()).toArray();// ID cua nhung ban ghi da chon
                    if (validateRestore(selectArray)) {
                        restoreAccountClick(4);
                    }
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        addButton(buttonDelete);
        return initPanel(2);
    }

    // anhPTN: lay so luong acc da tao thuc te cua hop dong
    public long getNumberOfCreatedAccount(long conID) throws Exception {
        String accRSQL = "select count(ac.account_id) number_of_account from bm_account ac where ac.contract_id = ?";
        List conIDParameter = new ArrayList();
        conIDParameter.add(conID);
        List<Map> LaccRNbr = C3p0Connector.queryData(accRSQL, conIDParameter);
        long accountsReal = Long.parseLong(LaccRNbr.get(0).get("number_of_account").toString());
        return accountsReal;
    }

    // AnhPTN: Kiem tra so luong acc tao da vuot qua so luong acc trong Hop dong chua
    private boolean isEnoughAccount(long conID) throws Exception {
        // so luong acc da tao
        long createdAccount = getNumberOfCreatedAccount(conID);
        // so luong acc dang ky
        ContractAction contractAction = new ContractAction();
        long nbrOfAcc = contractAction.getAmountOfAccountInContract(conID);
        if (createdAccount < nbrOfAcc) {
            return true;
        }
        return false;
    }

    private boolean checkDuplicateAccount(long conID, long accID) throws Exception {
        // cung hop dong thi phai khac acc
        String newAcc = getComponent("name").getValue().toString();
        boolean ckDup = false;
        if (getCurrentForm() == BaseAction.INT_EDIT_FORM) {
            String getOldAccName = "select name from bm_account where account_id = ?";
            List getOldAccNamePara = new ArrayList();
            getOldAccNamePara.add(accID);
            List<Map> lOldAccName = C3p0Connector.queryData(getOldAccName, getOldAccNamePara);
            String oldAcc = lOldAccName.get(0).get("name").toString();
            if(!newAcc.equals(oldAcc)){
                ckDup = true;
            }
        } 
        if(getCurrentForm() == BaseAction.INT_ADD_FORM){
            ckDup = true;
        }
        if(ckDup == true) {
            String ckDupAcc = "select * from bm_account where contract_id = ? and name like ?";
            List ckDupAccPara = new ArrayList();
            ckDupAccPara.add(conID);
            ckDupAccPara.add(newAcc);
            List<Map> lAcc = C3p0Connector.queryData(ckDupAcc, ckDupAccPara);
            if (!lAcc.isEmpty()) {
                Notification.show(ResourceBundleUtils.getLanguageResource("Account.Error.AccountNameDuplicate"),
                        null, Notification.Type.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    @Override
    //AnhPTN Validate: So luong acc tao moi khong vuot qua so luong acc dang ky trong hop dong
    public boolean validateAdd() throws Exception {
        AbstractField abfConID = getComponent("contract_id");
        long conID = Long.parseLong(abfConID.getValue().toString());
        if (!isEnoughAccount(conID)) {
            Notification.show(ResourceBundleUtils.getLanguageResource("Account.Error.NumberOfCreatedAccountInvalid"),
                    null, Notification.Type.ERROR_MESSAGE);
            return false;
        }
        checkDuplicateAccount(conID, 0);
        return true;
    }

    @Override
    // AnhPTN: acc o hop dong da nghiem thu thi khong duoc xoa
    public boolean validateDelete(Object[] selectedArray) throws Exception {
        int i = 0, accID = 0; //contract ID
        String gStDateSql = "select a.start_date from bm_account a where a.account_id = ?";
        for (i = 0; i < selectedArray.length; i++) {
            accID = Integer.parseInt(selectedArray[i].toString());
            List accIDPara = new ArrayList();
            accIDPara.add(accID);
            List<Map> LAccStDate = C3p0Connector.queryData(gStDateSql, accIDPara);
            if (LAccStDate.get(0).get("start_date") != null) {
                Notification.show(ResourceBundleUtils.getLanguageResource("Account.Error.AccountAccepted"),
                        null, Notification.Type.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    @Override
    //AnhPTN: acc o hop dong da nghiem thu thi khong duoc sua
    public boolean validateEdit(long accID) throws Exception {
        String gStDateSql = "select a.start_date from bm_account a where a.account_id = ?";
        List accIDPara = new ArrayList();
        accIDPara.add(accID);
        List<Map> LaccID = C3p0Connector.queryData(gStDateSql, accIDPara);
        if (LaccID.get(0).get("start_date") != null) {
            Notification.show(ResourceBundleUtils.getLanguageResource("Account.Error.AccountAccepted"),
                    null, Notification.Type.ERROR_MESSAGE);
            return false;
        }
        AbstractField abfConID = getComponent("contract_id");
        long conID = Long.parseLong(abfConID.getValue().toString());
        checkDuplicateAccount(conID, accID);
        return true;
    }

    @Override
    //AnhPTN: before add, update status = 0: chua kich hoat
    public void afterAddData(Connection connection, long accID) throws Exception {
        String sql = "update bm_account ac set ac.status = 0 where ac.account_id = ?";
        List<List> accIDPara = new ArrayList();
        List accIDRow = new ArrayList();
        accIDRow.add(accID);
        accIDRow.add("long");
        accIDPara.add(accIDRow);
        C3p0Connector.excuteDataByType(sql, accIDPara, connection);
        connection.commit();
        updateDataRefreshData();
    }

    public void restoreAccountClick(long status) throws Exception {
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
                                // cap nhat trang thai hop dong sang tam ngung status = 4

                                List userRow = new ArrayList();
                                long userID = Long.parseLong(VaadinUtils.getSessionAttribute("G_UserId").toString());
                                userRow.add(userID);
                                userRow.add("long");

                                List dateRow = new ArrayList();
                                dateRow.add(new Date());
                                dateRow.add("date");

                                List statusNew = new ArrayList();
                                statusNew.add(status);
                                statusNew.add("long");

//                                String sqlUpdateContract = "update bm_contract a set a.status = 2 where a.contract_id = ?";
//                                String sqlInsertContractHis = "insert into bm_contract_his(id_his,contract_id,cause_change_his,change_date_his,status_old,type_change,status_new,change_date_old,change_user_login_id_his)"
//                                        + " values(bm_contract_his_seq.nextval,?,?,?,?,5,2,?,?)";
                                String sqlUpdateAccount = "update bm_account a set a.status = ? where a.account_id = ?";
                                String sqlInsertAccountHis = "insert into bm_account_his(id_his, account_id,status_old,cause_change_his,change_date_his,status_new,change_date_old,contract_id,change_user_login_id_his)"
                                        + " values (bm_account_his_seq.nextval,?,?,?,?,?,?,?,?) ";
                                List lstBatch1 = new ArrayList();
                                List lstBatch2 = new ArrayList();
                                List lstBatchAcc = new ArrayList();
                                String des = "Khôi phục Account do khách hàng yêu cầu";
                                String desDelete = "Hủy account do khách hàng yêu cầu";

                                for (int i = 0; i < deleteArray.length; i++) {
                                    List lstParameter1 = new ArrayList();
                                    List lstParameter2 = new ArrayList();

                                    lstParameter1.add(statusNew);
                                    List lstAccID = new ArrayList();
                                    lstAccID.add(deleteArray[i]);
                                    lstAccID.add("long");
                                    lstParameter1.add(lstAccID);

                                    lstParameter2.add(lstAccID);
                                    lstBatch1.add(lstParameter1);

                                    Item data = table.getItem(deleteArray[i]);
                                    Object comboData = data.getItemProperty(ResourceBundleUtils.getLanguageResource("Account.status")).getValue();
                                    int conStatus = Integer.parseInt(((ComboboxItem) comboData).getValue().toString());

                                    List contractStatus = new ArrayList();
                                    contractStatus.add(conStatus);
                                    contractStatus.add("int");
                                    lstParameter2.add(contractStatus);

                                    List reasonChange = new ArrayList();
                                    if (status == 1) {
                                        reasonChange.add(des);
                                    } else {
                                        reasonChange.add(desDelete);
                                    }
                                    reasonChange.add("string");

                                    lstParameter2.add(reasonChange);
                                    lstParameter2.add(dateRow);
                                    lstParameter2.add(statusNew);

                                    long accID = Long.parseLong(deleteArray[i].toString());
                                    Date dateChangeOldAccount = GetDateOld.getChangeDateOldAccount(accID);
                                    List dateChangeOld = new ArrayList();
                                    dateChangeOld.add(dateChangeOldAccount);
                                    dateChangeOld.add("date");
                                    lstParameter2.add(dateChangeOld);

                                    Object comboContract = data.getItemProperty(ResourceBundleUtils.getLanguageResource("Account.Contract")).getValue();
                                    long conID = Long.parseLong(((ComboboxItem) comboContract).getValue().toString());
                                    List lstConID = new ArrayList();
                                    lstConID.add(conID);
                                    lstConID.add("long");

                                    lstParameter2.add(lstConID);
                                    lstParameter2.add(userRow);
                                    lstBatch2.add(lstParameter2);
                                }
// Thao tac voi Contract
                                C3p0Connector.excuteDataByTypeBatch(sqlUpdateAccount, lstBatch1, connection);
                                C3p0Connector.excuteDataByTypeBatch(sqlInsertAccountHis, lstBatch2, connection);
                                connection.commit();
                                updateDataRefreshData();
                                if (status == 1) {
                                    Notification.show(ResourceBundleUtils.getLanguageResource("Common.RestoreAccountSuccess"),
                                            null, Notification.Type.WARNING_MESSAGE);
                                } else {
                                    Notification.show(ResourceBundleUtils.getLanguageResource("Common.DeleteAccountSuccess"),
                                            null, Notification.Type.WARNING_MESSAGE);
                                }

                            }
                        } catch (Exception ex) {
                            if (connection != null) {
                                try {
                                    connection.rollback();
                                } catch (SQLException ex1) {
                                    VaadinUtils.handleException(ex);
                                    MainUI.mainLogger.debug("Install error: ", ex1);
                                }
                            }
                            VaadinUtils.handleException(ex);
                            MainUI.mainLogger.debug("Install error: ", ex);
                        } finally {
                            if (connection != null) {
                                try {
                                    connection.close();
                                } catch (SQLException ex) {
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
                // }
            }
        } else {
            Notification.show(ResourceBundleUtils.getLanguageResource("Common.SelectRequireAtLeast1"),
                    null, Notification.Type.ERROR_MESSAGE);
        }
    }

    public boolean validateRestore(Object[] selectedArray) {
        int i = 0;
        String notify = "Account no  Restoring";
        for (i = 0; i < selectedArray.length; i++) {
            Item data = table.getItem(selectedArray[i]);
            Object comboData = data.getItemProperty(ResourceBundleUtils.getLanguageResource("Account.status")).getValue();
            int conStatus = Integer.parseInt(((ComboboxItem) comboData).getValue().toString());
            if (conStatus != 5) {
                Notification.show(ResourceBundleUtils.getLanguageResource(notify),
                        null, Notification.Type.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }

}
