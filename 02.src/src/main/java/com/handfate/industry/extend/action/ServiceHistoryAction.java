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
import com.handfate.industry.core.action.component.PagedTable;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.handfate.industry.extend.util.GetDateOld;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.handfate.industry.extend.parallel.TransactionTask;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
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
public class ServiceHistoryAction extends BaseAction {

    public Table historyTable = new Table();

    /**
     * HÃ m khá»Ÿi táº¡o giao diá»‡n
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI VÃ¹ng giao diá»‡n cá»§a chá»©c nÄƒng
     * @return Giao diá»‡n sau khi khá»Ÿi táº¡o
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khá»Ÿi táº¡o tham sá»‘
        setTableName("bm_contract");
        setIdColumnName("CONTRACT_ID");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("code");
        setSortAscending(true);
        setSequenceName("bm_contract_seq");
        setAllowAdd(false);
        setAllowEdit(false);
        setAllowDelete(false);

        addTextFieldToForm("ContractID", new TextField(), "CONTRACT_ID", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.code", new TextField(), "code", "string", true, 50, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Contract.number", new TextField(), "CONTRACT_NUMBER", "string", true, 50, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Contract.name", new TextField(), "name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.payer", new TextField(), "PAYER", "string", false, 75, null, null, true, false, null, false, null, true, false, true, true, null);
        addTextFieldToForm("Contract.signDate", new PopupDateField(), "SIGN_DATE", "date", true, null, null, null, false, false, null, false, null, true, false, true, true, null);
        addTextFieldToForm("Contract.acceptanceDate", new PopupDateField(), "ACCEPTANCE_DATE", "date", true, null, null, null, false, false, null, false, null, true, false, true, true, null);
        addTextFieldToForm("Contract.expireDate", new PopupDateField(), "EXPIRE_DATE", "date", false, null, null, null, false, false, null, false, null, true, false, true, true, null);

        Object[][] paytype = {{2, "Transaction.bankplus"}, {3, "Transaction.card"}, {4, "Transaction.cash"}};
        addComboBoxToForm("Transaction.paymentType", new ComboBox(), "payment_method", "int",
                false, 50, null, null, false, false, null, false, null, true, true, true, true, paytype, "1", "Transaction.transfer");

        addComboBoxToForm("Contract.customer", new ComboBox(), "CUSTOMER_ID", "int",
                true, 50, null, null, true, false, null, false, null, true, true, true, false, "select CUSTOMER_ID, code from bm_customer", null, "bm_customer",
                "CUSTOMER_ID", "int", "code", null, null, false, true, null, null);
        // addComponentToForm("Customer.CustomerName", new TextField(), "customer_name", "string", true, 150, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.address", new TextField(), "address", "string", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addComboBoxToForm("Policy.ServiceID", new ComboBox(), "service_id", "int",
                true, 50, null, null, true, false, null, false, null, true, true, true, false, "select service_id, code from bm_service", null, "bm_service",
                "service_id", "int", "code", null, null, false, true, null, null);

        addComboBoxToForm("Contract.policyID", new ComboBox(), "policy_id", "int",
                true, 50, null, null, true, false, null, false, null, true, true, true, false, "select policy_id, code, service_id from bm_policy", null, "bm_policy",
                "policy_id", "int", "code", null, null, false, true, "service_id", "service_id");

        // muc dich: chi hien thi tren datagrid, khong cho nhap tren man hinh them moi        
        Object[][] contractStatus = {{2, "Contract.Active"}, {3, "Contract.Expired"}, {4, "Contract.Suspended"}, {5, "Contract.Canceled"}, {6, "Contract.Termination"}};
        addComboBoxToForm("Contract.status", new ComboBox(), "status", "int",
                false, 50, null, null, true, false, null, false, null, false, false, true, true, contractStatus, "1", "Contract.New");

        addTextFieldToForm("Contract.startCost", new TextField(), "start_cost", "long", false, 50, null, null, false, false, null, false, null, false, false, true, true, null);
        Date toDate = new Date();
        Date fromDate = new Date(toDate.getTime() - 60l * 24l * 3600l * 1000l);
        List lstParam = new ArrayList();
        lstParam.add(fromDate);
        lstParam.add(toDate);
        setComponentAsSysdate("User.CreateDate", "create_date", true, "date:and_mandatory:366", false, lstParam);
        setComponentAsLoginUser("User.CreateUser", "create_user", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null, null, INT_VIEWGROUP_ALL);
 //       addCustomizeComponentToForm("History.Reason", new TextField(), "string", true, 200, null, null, false, true, true, true, true, null);

        //Them nut ngung dich vu do khach hang
        Button buttonPauseService = new Button(ResourceBundleUtils.getLanguageResource("Button.PauseService"));
        buttonPauseService.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    Object[] deleteArray = ((java.util.Collection) table.getValue()).toArray();// ID cua nhung ban ghi da chon
                    if (validatePauseService(deleteArray)) {
                        buttonPauseServiceClick();
                    }
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        addButton(buttonPauseService);

//Them nut cham dut dich vu do khach hang
        Button buttonStopService = new Button(ResourceBundleUtils.getLanguageResource("Button.StopService"));
        buttonStopService.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    Object[] deleteArray = ((java.util.Collection) table.getValue()).toArray();// ID cua nhung ban ghi da chon
                    if (validateStopService(deleteArray)) {
                        buttonStopServiceClick();
                    }
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        addButton(buttonStopService);
        //Them nut khoi phuc dich vu do khach hang
        Button buttonRestoreService = new Button(ResourceBundleUtils.getLanguageResource("Button.RestoreService"));
        buttonRestoreService.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    Object[] deleteArray = ((java.util.Collection) table.getValue()).toArray();// ID cua nhung ban ghi da chon
                    if (validateRestoreService(deleteArray)) {
                        buttonRestoreServiceClick();
                    }
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        addButton(buttonRestoreService);
        return initPanel(2);
    }

    /**
     * Hàm khởi tạo giao diện vùng dữ liệu
     *
     * @since 15/10/2014 HienDM
     * @return Giao diện dữ liệu
     */
    @Override
    public VerticalLayout buildNormalDataPanel() throws Exception {
        VerticalLayout dataPanel = super.buildNormalDataPanel();

        // Thêm bảng history
        if (!(historyTable != null && historyTable.size() > 0)) {
            historyTable = buildHistoryTable();
        }
        dataPanel.addComponent(historyTable);

        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                try {
                    long id = Long.parseLong(itemClickEvent.getItemId().toString());
                    List<Map> lstHistory = selectHistory(id);
                    insertToHistoryTable(lstHistory);
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        return dataPanel;
    }

    /**
     * Hàm truy vấn dữ liệu bảng history
     *
     * @since 04/01/2015 HienDM
     * @return bảng lưu trữ
     */
    private List<Map> selectHistory(long id) throws Exception {
        String accSQL = " select id_his, CHANGE_DATE_HIS, (select code from bm_contract \n"
                + "                  where contract_id = bm_contract_his.contract_id) \n"
                + "                  contract_code, customer_name,\n"
                + "                  (case type_Change\n"
                + "                  when 1 then 'Thay đổi chính sách' \n"
                + "                  when 2 then 'Gia hạn hợp đồng'\n"
                + "                  when 3 then 'Chấm dứt hợp đồng'\n"
                + "                  when 4 then 'Tạm ngưng dịch vụ'\n"
                + "                  when 5 then 'Khôi phục'\n"
                + "                  end)type_change,\n"
                + "                  (case status_old\n"
                + "                  when 1 then 'Tạo mới'\n"
                + "                  when 2 then 'Hoạt động'\n"
                + "                  when 3 then 'Hợp đồng đã hết hạn'\n"
                + "                  when 4 then 'Hợp đồng tạm ngưng'\n"
                + "                  when 5 then 'Hợp đồng bị hủy'\n"
                + "                  when 6 then 'Chấm dứt hợp đồng'\n"
                + "                  end)status_old,\n"
                + "                  (case status_new\n"
                + "                  when 1 then 'Tạo mới'\n"
                + "                  when 2 then 'Hoạt động'\n"
                + "                  when 3 then 'Hợp đồng đã hết hạn'\n"
                + "                  when 4 then 'Hợp đồng tạm ngưng'\n"
                + "                  when 5 then 'Hợp đồng bị hủy'\n"
                + "                  when 6 then 'Chấm dứt hợp đồng'\n"
                + "                  end)status_new,CAUSE_CHANGE_HIS, change_date_old                \n"
                + "                 \n"
                + "               from bm_contract_his  where contract_id=? order by CHANGE_DATE_HIS desc";
        List lstParameter = new ArrayList();
        lstParameter.add(id);
        return C3p0Connector.queryData(accSQL, lstParameter);
    }

    /**
     * Hàm khởi tạo bảng history
     *
     * @since 04/01/2015 HienDM
     * @return bảng lưu trữ
     */
    public PagedTable buildHistoryTable() {
        PagedTable temp = new PagedTable();
        temp.removeAllItems();
        temp.setWidth("100%");
        temp.setSelectable(true);
        temp.setMultiSelect(true);
        temp.setColumnCollapsingAllowed(true);
        temp.setPageLength(25);

        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("changeService.changeDate"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("Contract.code"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("ContractHis.typeChange"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("ContractHis.causeChangeHis"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("Contract.statusOld"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("Contract.statusNew"), String.class, null);

        return temp;
    }

    /**
     * Hàm khởi tạo bảng history
     *
     * @since 04/01/2015 HienDM
     * @return bảng lưu trữ
     */
    public void insertToHistoryTable(List<Map> lstHistory) {
        historyTable.removeAllItems();
        for (int i = 0; i < lstHistory.size(); i++) {
            Object[] data = new Object[6];
            Map row = lstHistory.get(i);
            if (row.get("change_date_his") != null) {
                data[0] = row.get("change_date_his").toString();
            }
            if (row.get("contract_code") != null) {
                data[1] = row.get("contract_code").toString();
            }

            if (row.get("type_change") != null) {
                data[2] = row.get("type_change").toString();
            }
            if (row.get("cause_change_his") != null) {
                data[3] = row.get("cause_change_his").toString();
            }
            if (row.get("status_old") != null) {
                data[4] = row.get("status_old").toString();
            }
            if (row.get("status_new") != null) {
                data[5] = row.get("status_new").toString();
            }

//         if (row.get("change_date_old") != null) {
//         data[7] = row.get("change_date_old").toString();
//         }         
            historyTable.addItem(data, row.get("id_his").toString());
        }
    }

    @Override
    public void afterEditData(Connection connection, long id) throws Exception {

    }

    public void buttonPauseServiceClick() throws Exception {
        Object[] deleteArray = ((java.util.Collection) table.getValue()).toArray();// ID cua nhung ban ghi da chon
        if (deleteArray != null && deleteArray.length > 0) {
            if (checkPermission(deleteArray)) {
//                if (validatePauseService(deleteArray)) {
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

                                String sqlUpdateContract = "update bm_contract a set a.status = 4 where a.contract_id = ?";
                                String sqlInsertContractHis = "insert into bm_contract_his(id_his,contract_id,cause_change_his,change_date_his,status_old,type_change,status_new,change_date_old,change_user_login_id_his)"
                                        + " values(bm_contract_his_seq.nextval,?,?,sysdate,?,4,4,?,?)";
                                String sqlUpdateAccount = "update bm_account a set a.status = 2 where a.contract_id = ?";
                                String sqlInsertAccountHis = "insert into bm_account_his(id_his, account_id,status_old,cause_change_his,change_date_his,status_new,change_date_old,contract_id)"
                                        + " values (bm_account_his_seq.nextval,?,?,?,sysdate,2,?,?) ";
                                List lstBatch1 = new ArrayList();
                                List lstBatch2 = new ArrayList();
                                List lstBatchAcc = new ArrayList();
                                String des = "Ngưng do khách hàng yêu cầu";

                                for (int i = 0; i < deleteArray.length; i++) {
                                    List lstParameter1 = new ArrayList();
                                    List lstParameter2 = new ArrayList();

                                    List ConID = new ArrayList();
                                    ConID.add(deleteArray[i]);
                                    ConID.add("long");

                                    lstParameter1.add(ConID);
                                    lstParameter2.add(ConID);
                                    lstBatch1.add(lstParameter1);

                                    List reasonChange = new ArrayList();
                                    reasonChange.add(des);
                                    reasonChange.add("string");
                                    lstParameter2.add(reasonChange);
                                    // lstParameter2.add(dateRow);

                                    Item data = table.getItem(deleteArray[i]);
                                    Object comboData = data.getItemProperty(ResourceBundleUtils.getLanguageResource("Contract.status")).getValue();
                                    int conStatus = Integer.parseInt(((ComboboxItem) comboData).getValue().toString());

                                    List contractStatus = new ArrayList();
                                    contractStatus.add(conStatus);
                                    contractStatus.add("int");
                                    lstParameter2.add(contractStatus);

                                    long contractID = Long.parseLong(deleteArray[i].toString());
                                    Date dateChangeOldContract = GetDateOld.getChangeDateOldContract(contractID);
                                    List dateChangeOld = new ArrayList();
                                    dateChangeOld.add(dateChangeOldContract);
                                    dateChangeOld.add("date");

                                    lstParameter2.add(dateChangeOld);
                                    lstParameter2.add(userRow);

                                    lstBatch2.add(lstParameter2);
                                    List<Map> lstAccID = getAccountID(contractID);
                                    for (int j = 0; j < lstAccID.size(); j++) {
                                        long accountID = Long.parseLong(lstAccID.get(j).get("account_id").toString());
                                        int accountStatus = Integer.parseInt(lstAccID.get(j).get("status").toString());
                                        List lstAccParameter = new ArrayList();

                                        List IDAcc = new ArrayList();
                                        IDAcc.add(accountID);
                                        IDAcc.add("long");
                                        lstAccParameter.add(IDAcc);

                                        List accStatus = new ArrayList();
                                        accStatus.add(accountStatus);
                                        accStatus.add("int");
                                        lstAccParameter.add(accStatus);

                                        lstAccParameter.add(reasonChange);
                                        //  lstAccParameter.add(dateRow);

                                        Date dateChangeOldAccount = GetDateOld.getChangeDateOldAccount(accountID);
                                        List dateAccChangeOld = new ArrayList();
                                        dateAccChangeOld.add(dateChangeOldAccount);
                                        dateAccChangeOld.add("date");
                                        lstAccParameter.add(dateAccChangeOld);
                                        lstAccParameter.add(ConID);
                                        lstBatchAcc.add(lstAccParameter);

                                    }

                                }
// Thao tac voi Contract
                                C3p0Connector.excuteDataByTypeBatch(sqlUpdateContract, lstBatch1, connection);
                                C3p0Connector.excuteDataByTypeBatch(sqlInsertContractHis, lstBatch2, connection);
// Thao tac voi account                                
                                C3p0Connector.excuteDataByTypeBatch(sqlUpdateAccount, lstBatch1, connection);
                                C3p0Connector.excuteDataByTypeBatch(sqlInsertAccountHis, lstBatchAcc, connection);
                                connection.commit();
                                updateDataRefreshData();
                                Notification.show(ResourceBundleUtils.getLanguageResource("Common.PauseServiceSuccess"),
                                        null, Notification.Type.WARNING_MESSAGE);

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

    public void buttonStopServiceClick() throws Exception {
        Object[] deleteArray = ((java.util.Collection) table.getValue()).toArray();// ID cua nhung ban ghi da chon
        if (deleteArray != null && deleteArray.length > 0) {
            if (checkPermission(deleteArray)) {
//                if (validatePauseService(deleteArray)) {
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

                                String sqlUpdateContract = "update bm_contract a set a.status = 5 where a.contract_id = ?";
                                String sqlInsertContractHis = "insert into bm_contract_his(id_his,contract_id,cause_change_his,change_date_his,status_old,type_change,status_new,change_date_old,change_user_login_id_his)"
                                        + " values(bm_contract_his_seq.nextval,?,?,sysdate,?,3,5,?,?)";
                                String sqlUpdateAccount = "update bm_account a set a.status = 4 where a.contract_id = ?";
                                String sqlInsertAccountHis = "insert into bm_account_his(id_his, account_id,status_old,cause_change_his,change_date_his,status_new,change_date_old,contract_id)"
                                        + " values (bm_account_his_seq.nextval,?,?,?,sysdate,4,?,?) ";
                                List lstBatch1 = new ArrayList();
                                List lstBatch2 = new ArrayList();
                                List lstBatchAcc = new ArrayList();
                                String des = "Dừng hợp đồng do khách hàng yêu cầu";

                                for (int i = 0; i < deleteArray.length; i++) {
                                    List lstParameter1 = new ArrayList();
                                    List lstParameter2 = new ArrayList();

                                    List ConID = new ArrayList();
                                    ConID.add(deleteArray[i]);
                                    ConID.add("long");

                                    lstParameter1.add(ConID);
                                    lstParameter2.add(ConID);
                                    lstBatch1.add(lstParameter1);

                                    List reasonChange = new ArrayList();
                                    reasonChange.add(des);
                                    reasonChange.add("string");
                                    lstParameter2.add(reasonChange);
                                    //   lstParameter2.add(dateRow);

                                    Item data = table.getItem(deleteArray[i]);
                                    Object comboData = data.getItemProperty(ResourceBundleUtils.getLanguageResource("Contract.status")).getValue();
                                    int conStatus = Integer.parseInt(((ComboboxItem) comboData).getValue().toString());

                                    List contractStatus = new ArrayList();
                                    contractStatus.add(conStatus);
                                    contractStatus.add("int");
                                    lstParameter2.add(contractStatus);

                                    long contractID = Long.parseLong(deleteArray[i].toString());
                                    Date dateChangeOldContract = GetDateOld.getChangeDateOldContract(contractID);
                                    List dateChangeOld = new ArrayList();
                                    dateChangeOld.add(dateChangeOldContract);
                                    dateChangeOld.add("date");

                                    lstParameter2.add(dateChangeOld);
                                    lstParameter2.add(userRow);
                                    lstBatch2.add(lstParameter2);
                                    List<Map> lstAccID = getAccountID(contractID);
                                    for (int j = 0; j < lstAccID.size(); j++) {
                                        long accountID = Long.parseLong(lstAccID.get(j).get("account_id").toString());
                                        int accountStatus = Integer.parseInt(lstAccID.get(j).get("status").toString());
                                        List lstAccParameter = new ArrayList();

                                        List IDAcc = new ArrayList();
                                        IDAcc.add(accountID);
                                        IDAcc.add("long");
                                        lstAccParameter.add(IDAcc);

                                        List accStatus = new ArrayList();
                                        accStatus.add(accountStatus);
                                        accStatus.add("int");
                                        lstAccParameter.add(accStatus);

                                        lstAccParameter.add(reasonChange);
                                        //   lstAccParameter.add(dateRow);

                                        Date dateChangeOldAccount = GetDateOld.getChangeDateOldAccount(accountID);
                                        List dateAccChangeOld = new ArrayList();
                                        dateAccChangeOld.add(dateChangeOldAccount);
                                        dateAccChangeOld.add("date");
                                        lstAccParameter.add(dateAccChangeOld);
                                        lstAccParameter.add(ConID);
                                        lstBatchAcc.add(lstAccParameter);

                                    }

                                }
// Thao tac voi Contract
                                C3p0Connector.excuteDataByTypeBatch(sqlUpdateContract, lstBatch1, connection);
                                C3p0Connector.excuteDataByTypeBatch(sqlInsertContractHis, lstBatch2, connection);
// Thao tac voi account                                
                                C3p0Connector.excuteDataByTypeBatch(sqlUpdateAccount, lstBatch1, connection);
                                C3p0Connector.excuteDataByTypeBatch(sqlInsertAccountHis, lstBatchAcc, connection);

// Tinh tien no den thoi diem hien tai                                
                                for (int i = 0; i < deleteArray.length; i++) {

                                    long contractID = Long.parseLong(deleteArray[i].toString());

                                    // Tinh lai cuoc phat sinh cua cac account trong hop dong bi cham dut khi dang thuc hien tai ngay cham dut
                                    TransactionTask tranTask = new TransactionTask();
                                    List<Map> tranList = new ArrayList();
                                    tranList = tranTask.getListTransaction(contractID, 1,connection);
                                    tranTask.insertTransaction(tranList, connection);

                                }
                                connection.commit();
                                updateDataRefreshData();

                                Notification.show(ResourceBundleUtils.getLanguageResource("Common.StopServiceSuccess"),
                                        null, Notification.Type.WARNING_MESSAGE);

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

    public void buttonRestoreServiceClick() throws Exception {
        Object[] deleteArray = ((java.util.Collection) table.getValue()).toArray();// ID cua nhung ban ghi da chon
        if (deleteArray != null && deleteArray.length > 0) {
            if (checkPermission(deleteArray)) {
//                if (validatePauseService(deleteArray)) {
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

                                String sqlUpdateContract = "update bm_contract a set a.status = 2 where a.contract_id = ?";
                                String sqlInsertContractHis = "insert into bm_contract_his(id_his,contract_id,cause_change_his,change_date_his,status_old,type_change,status_new,change_date_old,change_user_login_id_his)"
                                        + " values(bm_contract_his_seq.nextval,?,?,sysdate,?,5,2,?,?)";
                               //TH: ngung nhung 1 so account van dang hoat dong
                                //String sqlUpdateAccount = "update bm_account a set a.status = 5 where a.contract_id = ? and status = 2";
                                 String sqlUpdateAccount = "update bm_account a set a.status = 5 where a.contract_id = ?";
                                String sqlInsertAccountHis = "insert into bm_account_his(id_his, account_id,status_old,cause_change_his,change_date_his,status_new,change_date_old,contract_id)"
                                        + " values (bm_account_his_seq.nextval,?,?,?,sysdate,5,?,?) ";
                                List lstBatch1 = new ArrayList();
                                List lstBatch2 = new ArrayList();
                                List lstBatchAcc = new ArrayList();
                                String des = "Khôi phục hợp đồng do khách hàng yêu cầu";

                                for (int i = 0; i < deleteArray.length; i++) {
                                    List lstParameter1 = new ArrayList();
                                    List lstParameter2 = new ArrayList();

                                    List ConID = new ArrayList();
                                    ConID.add(deleteArray[i]);
                                    ConID.add("long");

                                    lstParameter1.add(ConID);
                                    lstParameter2.add(ConID);
                                    lstBatch1.add(lstParameter1);

                                    List reasonChange = new ArrayList();
                                    reasonChange.add(des);
                                    reasonChange.add("string");
                                    lstParameter2.add(reasonChange);
                                    //   lstParameter2.add(dateRow);

                                    Item data = table.getItem(deleteArray[i]);
                                    Object comboData = data.getItemProperty(ResourceBundleUtils.getLanguageResource("Contract.status")).getValue();
                                    int conStatus = Integer.parseInt(((ComboboxItem) comboData).getValue().toString());

                                    List contractStatus = new ArrayList();
                                    contractStatus.add(conStatus);
                                    contractStatus.add("int");
                                    lstParameter2.add(contractStatus);

                                    long contractID = Long.parseLong(deleteArray[i].toString());
                                    Date dateChangeOldContract = GetDateOld.getChangeDateOldContract(contractID);
                                    List dateChangeOld = new ArrayList();
                                    dateChangeOld.add(dateChangeOldContract);
                                    dateChangeOld.add("date");

                                    lstParameter2.add(dateChangeOld);
                                    lstParameter2.add(userRow);
                                    lstBatch2.add(lstParameter2);
                                    List<Map> lstAccID = getAccountID(contractID);
                                    for (int j = 0; j < lstAccID.size(); j++) {
                                        long accountID = Long.parseLong(lstAccID.get(j).get("account_id").toString());
                                        int accountStatus = Integer.parseInt(lstAccID.get(j).get("status").toString());
                                     //   if(accountStatus==2){
                                        List lstAccParameter = new ArrayList();

                                        List IDAcc = new ArrayList();
                                        IDAcc.add(accountID);
                                        IDAcc.add("long");
                                        lstAccParameter.add(IDAcc);

                                        List accStatus = new ArrayList();
                                        accStatus.add(accountStatus);
                                        accStatus.add("int");
                                        lstAccParameter.add(accStatus);

                                        lstAccParameter.add(reasonChange);
                                        // lstAccParameter.add(dateRow);

                                        Date dateChangeOldAccount = GetDateOld.getChangeDateOldAccount(accountID);
                                        List dateAccChangeOld = new ArrayList();
                                        dateAccChangeOld.add(dateChangeOldAccount);
                                        dateAccChangeOld.add("date");
                                        lstAccParameter.add(dateAccChangeOld);
                                        lstAccParameter.add(ConID);
                                        lstBatchAcc.add(lstAccParameter);
                                     //   }
                                    }

                                }
// Thao tac voi Contract
                                C3p0Connector.excuteDataByTypeBatch(sqlUpdateContract, lstBatch1, connection);
                                C3p0Connector.excuteDataByTypeBatch(sqlInsertContractHis, lstBatch2, connection);
// Thao tac voi account                                
                                C3p0Connector.excuteDataByTypeBatch(sqlUpdateAccount, lstBatch1, connection);
                                C3p0Connector.excuteDataByTypeBatch(sqlInsertAccountHis, lstBatchAcc, connection);
                                connection.commit();
                                updateDataRefreshData();
                                Notification.show(ResourceBundleUtils.getLanguageResource("Common.RestoreServiceSuccess"),
                                        null, Notification.Type.WARNING_MESSAGE);

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

    public List<Map> getAccountID(long contractID) throws Exception {
        String sql = "select account_id,status from bm_account where contract_id = ?";
        List IDParameter = new ArrayList();
        IDParameter.add(contractID);
        List<Map> LAccID = C3p0Connector.queryData(sql, IDParameter);
        return LAccID;
    }

    /*
     validate cho truong hop tam ngung dich vu
     Hop dong dang o trang thai huy hoac da thanh ly
     */
    public boolean validatePauseService(Object[] selectedArray) throws Exception {
        // dang o trang thai chua thanh toan
        int i = 0;
        String notify = "Contract Status InActive";
        long invID = 0, invStt = 0;
        for (i = 0; i < selectedArray.length; i++) {

            Item data = table.getItem(selectedArray[i]);

            Object comboData = data.getItemProperty(ResourceBundleUtils.getLanguageResource("Contract.status")).getValue();
            int conStatus = Integer.parseInt(((ComboboxItem) comboData).getValue().toString());

            if (conStatus != 2) {
                Notification.show(ResourceBundleUtils.getLanguageResource(notify),
                        null, Notification.Type.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    public boolean validateStopService(Object[] selectedArray) throws Exception {
        // dang o trang thai chua thanh toan
        int i = 0;
        String notify = "Contract Status InActive or pause";
        long invID = 0, invStt = 0;
        for (i = 0; i < selectedArray.length; i++) {

            Item data = table.getItem(selectedArray[i]);

            Object comboData = data.getItemProperty(ResourceBundleUtils.getLanguageResource("Contract.status")).getValue();
            int conStatus = Integer.parseInt(((ComboboxItem) comboData).getValue().toString());

            if (conStatus != 6 && conStatus != 2) {
                Notification.show(ResourceBundleUtils.getLanguageResource(notify),
                        null, Notification.Type.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    public boolean validateRestoreService(Object[] selectedArray) throws Exception {
        // dang o trang thai chua thanh toan
        int i = 0;
        String notify = "Contract Status no pause";
        for (i = 0; i < selectedArray.length; i++) {
            Item data = table.getItem(selectedArray[i]);
            Object comboData = data.getItemProperty(ResourceBundleUtils.getLanguageResource("Contract.status")).getValue();
            int conStatus = Integer.parseInt(((ComboboxItem) comboData).getValue().toString());
            if (conStatus == 4) {
                String conHis = "select * from bm_contract_his where contract_id = ? order by change_date_his desc ";
                List serPara = new ArrayList();
                serPara.add(Long.parseLong(selectedArray[i].toString()));
                List<Map> lstConHis = C3p0Connector.queryData(conHis, serPara);
                if (lstConHis.size() > 0) {
                    String causeChange = lstConHis.get(0).get("cause_change_his").toString();
                    String des = "Ngưng do nợ cước";
                    if (causeChange.equalsIgnoreCase(des)) {
                        notify = "Contract no pay";
                        Notification.show(ResourceBundleUtils.getLanguageResource(notify),
                                null, Notification.Type.ERROR_MESSAGE);
                        return false;
                    }
                }
            }
            if (conStatus != 4) {
                Notification.show(ResourceBundleUtils.getLanguageResource(notify),
                        null, Notification.Type.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }
}
