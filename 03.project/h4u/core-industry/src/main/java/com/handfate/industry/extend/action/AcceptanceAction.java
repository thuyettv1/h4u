/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.BaseAction;
import com.handfate.industry.core.action.PopupSingleUserAction;
import com.handfate.industry.core.action.component.ComboboxItem;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.handfate.industry.extend.parallel.TransactionTask;
import com.vaadin.data.Item;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @since 14/11/2014
 * @author YenNTH8
 */
public class AcceptanceAction extends BaseAction {

    /*YenNTH8
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {

        setTableName("BM_ACCEPTANCE");
        setIdColumnName("ACCEPTANCE_ID");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("ACCEPTANCE_DATE");
        setSortAscending(false);
        setSequenceName("BM_ACCEPTANCE_SEQ");
        addTextFieldToForm("AcceptanceID", new TextField(), "ACCEPTANCE_ID", "int", true, 10, null, null, false, false, null, false, null, true, true, true, true, null);
        //count gan vao hop dong nao
        addSinglePopupToForm("Account.Contract", "contract_id", "int", true, 50, null, null, true, null, false, null, true, true, true, false, new PopupSingleContractAction(localMainUI), 2,
                null, "", "contract_id", "code", "bm_contract", null, null);
        addTextFieldToForm("Account.acceptanceDate", new PopupDateField(), "acceptance_date", "date", true, 10, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Acceptance.delegate", new TextField(), "delegate", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Acceptance.phone", new TextField(), "phone", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Acceptance.address", new TextField(), "address", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        
        Date toDate = new Date();
        Date fromDate = new Date(toDate.getTime() - 60l * 24l * 3600l * 1000l);
        List lstParam = new ArrayList();
        lstParam.add(fromDate);
        lstParam.add(toDate);
        setComponentAsSysdate("User.CreateDate", "create_date", true, "date:and_mandatory:366", false, lstParam);        
        setComponentAsLoginUser("User.CreateUser", "create_user", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null, null, INT_VIEWGROUP_RECURSIVE);

        return initPanel(2);
    }

    private void insertAccountHis(Connection connection) throws Exception {
        AbstractField contractId = getComponent("contract_id");
        long sContractId = Long.parseLong(contractId.getValue().toString());
        // insert ACCOUNT_HIS
        String isrtAccHisSQL = "INSERT into bm_account_his h (h.id_his, h.contract_id,h.account_id, "
                + " h.status_old, h.status_new, h.change_user_login_id_his, h.change_date_his) "
                + " select bm_account_his_seq.nextval, acc.contract_id, acc.account_id, "
                + " 0 status_old, 1 status_new, ?, ? "
                + " from bm_account acc "
                + " where acc.contract_id = ?";
        List isrtAccHisPara = new ArrayList();
        isrtAccHisPara.add(VaadinUtils.getSessionAttribute("G_UserId").toString());
        isrtAccHisPara.add(getComponent("acceptance_date").getValue());
        isrtAccHisPara.add(sContractId);
        C3p0Connector.excuteData(isrtAccHisSQL, isrtAccHisPara, connection);
    }

    public void updateAccountStartDate(Connection connection) throws Exception {
        // cập nhật start date = acceptance date cho acc cung contract 
        // & kich hoat trang thai hoat dong cho acc
        AbstractField contractId = getComponent("contract_id");
        long sContractId = Long.parseLong(contractId.getValue().toString());
        PopupDateField accepDate = (PopupDateField) getComponent("acceptance_date");
        Date sAccepDate = accepDate.getValue();
        String cql = " update bm_account "
                + " SET start_date = ?,"
                + " status = 1 "
                + " where contract_id = ?";
        List udAccPara = new ArrayList();
        udAccPara.add(sAccepDate);
        udAccPara.add(sContractId);
        C3p0Connector.excuteData(cql, udAccPara, connection);
    }

    private void updateContractExpireDate(Connection connection) throws Exception {
        AbstractField contractId = getComponent("contract_id");
        long sContractId = Long.parseLong(contractId.getValue().toString());
        String setExDateSQL = "UPDATE bm_contract CON "
                + "SET con.expire_date = (select ADD_MONTHS(accep.acceptance_date, pos.period) expire_date "
                + "from bm_contract con left join bm_acceptance accep on con.contract_id = accep.contract_id "
                + "LEFT join bm_policy po on con.policy_id = po.policy_id "
                + "left join bm_postage pos on po.postage_id = pos.postage_id "
                + "where con.contract_id = ?) "
                + "where con.contract_id = ? ";
        List dbConIDPara = new ArrayList();
        dbConIDPara.add(sContractId);
        dbConIDPara.add(sContractId);
        C3p0Connector.excuteData(setExDateSQL, dbConIDPara, connection);
    }

    @Override
    public void afterAddData(Connection connection, long id) throws Exception {
        AbstractField contractId = getComponent("contract_id");
        long sContractId = Long.parseLong(contractId.getValue().toString());
        PopupDateField puDAccepDate = (PopupDateField) getComponent("acceptance_date");
        Date accepDate = puDAccepDate.getValue();
        // cập nhật start date = acceptance date cho acc cung contract 
        // & kich hoat trang thai hoat dong cho acc
        updateAccountStartDate(connection);
        // AnhPTN: cap nhat status contract = 2 ~ Da nghiem thu
        String upSttConSQL = "update  bm_contract con set con.status = 2 where con.contract_id = ?";
        List conIDPara = new ArrayList();
        conIDPara.add(sContractId);
        C3p0Connector.excuteData(upSttConSQL, conIDPara, connection);
        //--
        ContractAction contractAction = new ContractAction();
        int paidType = contractAction.getPaidType(sContractId, connection);
        if (paidType == 1) {
            updateContractExpireDate(connection);// AnhpTN: Set expire_date cho hop dong
        }
        // AnhPTN: tao new transaction cho nghiem thu hop dong
        createContractTransaction(paidType, sContractId, connection);
        // insert ACCOUNT_HIS
        insertAccountHis(connection);
        // insert contract his
        insertContractHistory(sContractId, connection);
        connection.commit();
        // neu nghiem thu cua thang truoc --> goi lai ham tinh giao dich acc
        int lastMonth = Calendar.getInstance().get(Calendar.MONTH) + Calendar.getInstance().get(Calendar.YEAR) * 12;
        Calendar cal = Calendar.getInstance();
        cal.setTime(accepDate);
        int accepMonth = cal.get(Calendar.MONTH) + 1 + cal.get(Calendar.YEAR) * 12;
        if (lastMonth == accepMonth) {
            // Tinh lai cuoc phat sinh cua cac account trong hop dong
            TransactionTask tranTask = new TransactionTask();
            List<Map> tranList = new ArrayList();
            tranList = tranTask.getListTransaction(sContractId, 0, connection);
            tranTask.insertTransaction(tranList, connection);
        }
        updateDataRefreshData();
    }

    private void createContractTransaction(int paidType, long sContractId, Connection connection) throws Exception {
        String sTranCost = "start_cost";
        ContractAction contractAction = new ContractAction();
        if (paidType == 1) {
            sTranCost = "cost";// tra truoc thi lay tong gia tri hop dong
        }
        String sql2 = "insert into bm_sale_transaction tr (tr.transaction_id, tr.from_date, tr.to_date, "
                + "tr.status, tr.contract_id, tr.postage_id, tr.cost, tr.service_id) "
                + "select bm_sale_transaction_seq.nextval, ?,?, "
                + "1, con.contract_id, po.postage_id, con." + sTranCost + ", con.service_id "
                + "from bm_contract con left JOIN bm_policy po on con.policy_id = po.policy_id "
                + "where con.contract_id = ?";
        List lstParameter2 = new ArrayList();
        if (paidType == 1) { // Thoi han transaction = thoi han su dung dich vu
            lstParameter2.add(getComponent("acceptance_date").getValue());
            Date expireDate = contractAction.getContractDate(sContractId, "expire_date", connection);
            lstParameter2.add(expireDate);
        } else {
            lstParameter2.add(getComponent("acceptance_date").getValue());
            lstParameter2.add(getComponent("acceptance_date").getValue());
        }
        lstParameter2.add(sContractId);
        C3p0Connector.excuteData(sql2, lstParameter2, connection);
    }

    @Override
    public void afterEditData(Connection connection, long accepID) throws Exception {
        AbstractField contractId = getComponent("contract_id");
        long sContractId = Long.parseLong(contractId.getValue().toString());
        ContractAction contractAction = new ContractAction();
        int paidType = contractAction.getPaidType(sContractId, connection);
        PopupDateField puaccepDate = (PopupDateField) getComponent("acceptance_date");
        Date accepDate = (Date) puaccepDate.getValue();
        // xoa contract_his khac his nghiem thu lan dau, id = HD
        String delContractHis = "delete from bm_contract_his where contract_id = ? and type_change <> 0";
        List delConHisPara = new ArrayList();
        delConHisPara.add(sContractId);
        C3p0Connector.excuteData(delContractHis, delConHisPara, connection);
        // update contract_his nghiem thu lan dau, sua lai ngay nghiem thu
        String udContractHis = "update bm_contract_his  his "
                + " set his.expire_date = (select con.expire_date from bm_contract con where con.contract_id = his.contract_id )"
                + " where his.contract_id = ? and type_change = 0";
        C3p0Connector.excuteData(udContractHis, delConHisPara, connection);
        // cập nhật start date = acceptance date cho acc cung contract 
        // & kich hoat trang thai hoat dong cho acc
        updateAccountStartDate(connection);
        // AnhpTN: Set lai expire_date cho hop dong
        if (paidType == 1) { // tra truoc
            updateContractExpireDate(connection);
        }

        // xoa moi ban ghi acc trong acc_his cua hop dong
        String delAccHisSQL = "delete bm_account_his h where h.contract_id = ?";
        List delAccHisPara = new ArrayList();
        delAccHisPara.add(sContractId);
        C3p0Connector.excuteData(delAccHisSQL, delAccHisPara, connection);
        // update ACCOUNT_HIS
        insertAccountHis(connection);
        connection.commit();
        // neu nghiem thu cua thang truoc --> goi lai ham tinh giao dich acc
      /*  int lastMonth = Calendar.getInstance().get(Calendar.MONTH) + Calendar.getInstance().get(Calendar.YEAR) * 12;
        Calendar cal = Calendar.getInstance();
        cal.setTime(accepDate);
        int accepMonth = cal.get(Calendar.MONTH) + 1 + cal.get(Calendar.YEAR) * 12;
        if (lastMonth == accepMonth) {
            // Tinh lai cuoc phat sinh cua cac account trong hop dong
            TransactionTask tranTask = new TransactionTask();
            List<Map> tranList = new ArrayList();
            tranList = tranTask.getListTransaction(sContractId, 0, connection);
            tranTask.insertTransaction(tranList, connection);
        }*/
         TransactionTask tranTask = new TransactionTask();
            List<Map> tranList = new ArrayList();
            tranList = tranTask.getListTransaction(sContractId, 0, connection);
            tranTask.insertTransaction(tranList, connection);
        updateDataRefreshData();
    }

    // anhptn: Kiem tra hop dong da tao du account chua
    private boolean isEnoughAccount(long conID) throws Exception {
        // lay so luong account da dang ky trong hop dong
        ContractAction contractAction = new ContractAction();
        long accountsInContract = contractAction.getAmountOfAccountInContract(conID);
        // lay so luong account thuc te da tao
        AccountAction accountAction = new AccountAction();
        long accountsReal = accountAction.getNumberOfCreatedAccount(conID);
        //kiem tra
        if (accountsInContract == accountsReal) {
            return true;
        }
        return false;
    }

    @Override
    //anhptn: Phai co du tai khoan moi duoc tao moi ban ghi nghiem thu
    // ngay nghiem thu phai sau ngay ki hop dong
    public boolean validateAdd() throws Exception {
        Connection connection = C3p0Connector.getInstance().getConnection();
        //Kiem tra so luong acc
        AbstractField abfConID = getComponent("contract_id");
        long conID = Long.parseLong(abfConID.getValue().toString());
        boolean accOK = isEnoughAccount(conID);
        if (!accOK) {
            Notification.show(ResourceBundleUtils.getLanguageResource("Acceptance.Error.NumberOfAccountInvalid"),
                    null, Notification.Type.ERROR_MESSAGE);
            return false;
        }
        // ngay nghiem thu phai sau ngay ki hop dong
        ContractAction contractAction = new ContractAction();
        Date signDate = contractAction.getContractDate(conID, "sign_date", connection);
        Date accepDate = new Date();
        PopupDateField pdfAccepDate = (PopupDateField) getComponent("acceptance_date");
        accepDate = pdfAccepDate.getValue();
        if (accepDate.before(signDate)) {
            Notification.show(ResourceBundleUtils.getLanguageResource("Acceptance.Error.AcceptanceCannotBeforeSignDate"),
                    null, Notification.Type.ERROR_MESSAGE);
            return false;
        }
        // ngay nghiem thu phai < = ngay hien tai
        Date currentDate = new Date();
        if (!accepDate.before(currentDate)) {
            Notification.show(ResourceBundleUtils.getLanguageResource("Acceptance.Error.AcceptanceCannotAfterToday"),
                    null, Notification.Type.ERROR_MESSAGE);
            return false;
        }
        // ngay nghiem thu phai khong qua 1+ thang
        int oldestMonth = Calendar.getInstance().get(Calendar.MONTH) + Calendar.getInstance().get(Calendar.YEAR) * 12;
        Calendar cal = Calendar.getInstance();
        cal.setTime(accepDate);
        int accepMonth = cal.get(Calendar.MONTH) + 1 + cal.get(Calendar.YEAR) * 12;
        if (oldestMonth > accepMonth) {
            Notification.show(ResourceBundleUtils.getLanguageResource("Acceptance.Error.AcceptanceCannotBeforeTodayAMonth"),
                    null, Notification.Type.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    @Override
    public boolean validateEdit(long id) throws Exception {
        // ngay nghiem thu phai sau ngay ki hop dong
        AbstractField abfContractId = getComponent("contract_id");
        long conID = Long.parseLong(abfContractId.getValue().toString());
        ContractAction contractAction = new ContractAction();
        Connection connection = C3p0Connector.getInstance().getConnection();
        Date signDate = contractAction.getContractDate(conID, "sign_date", connection);
        Date accepDate = new Date();
        PopupDateField pdfAccepDate = (PopupDateField) getComponent("acceptance_date");
        accepDate = pdfAccepDate.getValue();
        if (accepDate.before(signDate)) {
            Notification.show(ResourceBundleUtils.getLanguageResource("Acceptance.Error.AcceptanceCannotBeforeSignDate"),
                    null, Notification.Type.ERROR_MESSAGE);
            return false;
        }
        // Chi co transaction nghiem thu hop dong co status = 1 (chua lap hoa don) moi duoc phep sua
        String getTranSttSQL = "select tr.status from bm_sale_transaction tr where tr.contract_id = ? ";
        List conIDParameter = new ArrayList();
        conIDParameter.add(conID);
        List<Map> LTrStt = C3p0Connector.queryData(getTranSttSQL, conIDParameter);
        long tranStt = 0;
        if (!LTrStt.isEmpty()) {
            for (int i = 0; i < LTrStt.size(); i++) {
                tranStt = Long.parseLong(LTrStt.get(i).get("status").toString());
                if (tranStt != 1) {
                    Notification.show(ResourceBundleUtils.getLanguageResource("Acceptance.Error.TransactionIsBilled"),
                            null, Notification.Type.ERROR_MESSAGE);
                    return false;
                }
            }
        }
        // ngay nghiem thu phai < = ngay hien tai
        Date currentDate = new Date();
        if (!accepDate.before(currentDate)) {
            Notification.show(ResourceBundleUtils.getLanguageResource("Acceptance.Error.AcceptanceCannotAfterToday"),
                    null, Notification.Type.ERROR_MESSAGE);
            return false;
        }
        // Thoi gian duoc phep sua nghiem thu trong vong X ngay tu ngay nghiem thu dau tien
        ConfigAction configAct = new ConfigAction();
        long maxDayForEdit = configAct.getValue(DAYS_FOR_EDIT_ACCEPTANCE_DAY);
        String getDayAddAcceptance = "Select change_date_his from bm_contract_his where type_change = 0 and contract_id =? ";
        List getDayAddAcceptancePara = new ArrayList();
        getDayAddAcceptancePara.add(conID);
        List<Map> insrtAccepDate = C3p0Connector.queryData(getDayAddAcceptance, getDayAddAcceptancePara, connection);
        if (!insrtAccepDate.isEmpty()) {
            Date insrtDate = (Date) insrtAccepDate.get(0).get("change_date_his");
            if (daysBetween(insrtDate, currentDate) > maxDayForEdit) {
                Notification.show(ResourceBundleUtils.getLanguageResource("Acceptance.Error.ToLongToEditAcceptanceDate"),
                        null, Notification.Type.ERROR_MESSAGE);
                return false;
            }
        }
        // ngay nghiem thu phai khong qua 1+ thang
        int oldestMonth = Calendar.getInstance().get(Calendar.MONTH) + Calendar.getInstance().get(Calendar.YEAR) * 12;
        Calendar cal = Calendar.getInstance();
        cal.setTime(accepDate);
        int accepMonth = cal.get(Calendar.MONTH) + 1 + cal.get(Calendar.YEAR) * 12;
        if (oldestMonth > accepMonth) {
            Notification.show(ResourceBundleUtils.getLanguageResource("Acceptance.Error.AcceptanceCannotBeforeTodayAMonth"),
                    null, Notification.Type.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    @Override
    public boolean validateDelete(Object[] selectedArray) throws Exception {
        String getTranSttSQL = "select tr.status from bm_sale_transaction tr where tr.contract_id = ? ";
        long conID = 0;
        for (int i = 0; i < selectedArray.length; i++) {
            Item data = table.getItem(selectedArray[i]);
            Object comboData = data.getItemProperty(ResourceBundleUtils.getLanguageResource("Account.Contract")).getValue();
            if (comboData instanceof ComboboxItem) {
                conID = Long.parseLong(((ComboboxItem) comboData).getValue().toString());
            } else {
                conID = Long.parseLong(data.getItemProperty(ResourceBundleUtils.getLanguageResource("Account.Contract")).getValue().toString());
            }
            // Transaction  chua duoc lap hoa don
            List conIDParameter = new ArrayList();
            conIDParameter.add(conID);
            List<Map> LTrStt = C3p0Connector.queryData(getTranSttSQL, conIDParameter);
            long tranStt = 0;
            if (!LTrStt.isEmpty()) {
                for (int j = 0; j < LTrStt.size(); j++) {
                    tranStt = Long.parseLong(LTrStt.get(j).get("status").toString());
                    if (tranStt != 1) {
                        Notification.show(ResourceBundleUtils.getLanguageResource("Acceptance.Error.TransactionIsBilled"),
                                null, Notification.Type.ERROR_MESSAGE);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void afterDeleteData(Connection connection, Object[] deleteArray) throws Exception {
        String udConStt = "update bm_contract con set con.status = 1 where con.contract_id = ?";
        String dlTran = "DELETE FROM bm_sale_transaction tr WHERE tr.contract_id = ? ";
        String udAccStrtDate = "update bm_account a set a.start_date  = null, a.status = 0 where a.contract_id = ?";
        String delAccHisSQL = "delete from bm_account_his h where h.contract_id = ?";
        String delConHisSQL = "delete from bm_contract_his h where h.contract_id = ?";
        long conID = 0;

        for (int i = 0; i < deleteArray.length; i++) {
            Item data = table.getItem(deleteArray[i]);
            Object comboData = data.getItemProperty(ResourceBundleUtils.getLanguageResource("Account.Contract")).getValue();
            conID = Long.parseLong(((ComboboxItem) comboData).getValue().toString());
            List conIDPara = new ArrayList();
            conIDPara.add(conID);
            // cap nhat status Hop dong = 1 chua nghiem thu        
            C3p0Connector.excuteData(udConStt, conIDPara, connection);
            // Xoa ban ghi transaction cua hop dong va acc
            C3p0Connector.excuteData(dlTran, conIDPara, connection);
            // Xoa start date cua cac account, chuyen account thanh chua kich hoat        
            C3p0Connector.excuteData(udAccStrtDate, conIDPara, connection);
//    // xoa moi ban ghi acc trong acc_his cua hop dong            
            C3p0Connector.excuteData(delAccHisSQL, conIDPara, connection);
//    // xoa moi ban ghi con trong con_his 
            C3p0Connector.excuteData(delConHisSQL, conIDPara, connection);
        }

    }

    // HienDM1 cập nhật history
    private void insertContractHistory(long id, Connection connection) throws Exception {
        Long hisId = C3p0Connector.getSequenceValue("bm_contract_his_seq");
        String insertSQL = " insert into bm_contract_his (id_his, contract_id, payer, sign_date, acceptance_date, "
                + "       expire_date, pay_type, address, delegate, "
                + "       delegate_role, delegate_phone, delegate_mobile, "
                + "       delegate_email, cost, accumulated, amount_of_account, "
                + "       start_cost, change_date_his, status_old, status_new, customer_name, "
                + "       type_change, change_user_login_id_his) "
                + " select ? id_his, contract_id, payer, sign_date, ? acceptance_date, "
                + "       expire_date, payment_method, address, delegate, "
                + "       delegate_role, delegate_phone, delegate_mobile, "
                + "       delegate_email, cost, accumulated, amount_of_account, "
                + "       start_cost, ? change_date_his, 1 status_old, 2 status_new, customer_name, "
                + "       0 type_change, ? change_user_login_id_his from bm_contract "
                + " where contract_id = ? ";
        List lstParameter = new ArrayList();
        lstParameter.add(hisId);
        lstParameter.add(getComponent("acceptance_date").getValue());
        lstParameter.add(new Date());
        lstParameter.add(VaadinUtils.getSessionAttribute("G_UserId").toString());
        lstParameter.add(id);
        C3p0Connector.excuteData(insertSQL, lstParameter, connection);

        String sqlGood = " insert into bm_his_policy_good (id, amount, his_id, policy_good_id) "
                + " select bm_his_policy_good_seq.nextval, amount, ?, policy_good_id from BM_CONTRACT_POLICY_GOOD "
                + " where contract_id = ? ";

        List lstParam = new ArrayList();
        lstParam.add(hisId);
        lstParam.add(id);

        C3p0Connector.excuteData(sqlGood, lstParam, connection);
    }

    public int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
}
