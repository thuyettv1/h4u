/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.MainUI;
import com.handfate.industry.core.action.BaseAction;
import com.handfate.industry.core.action.PopupSingleUserAction;
import com.handfate.industry.core.action.component.PagedTable;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.AbstractField;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class ContractExtensionAction extends BaseAction {

    public Table historyTable = new Table();
    public TextField txtReason = new TextField();
    public TextField txtRequester = new TextField();
    public TextField txtRequesterIdNO = new TextField();
    
    /**
     * Ham khoi tao giao dien
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI Vung giao dien cac chuc nang
     * @return Giao dien sau khi khoi tao
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        setTableName("bm_contract");
        setIdColumnName("CONTRACT_ID");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("code");
        setSortAscending(true);
        setSequenceName("bm_contract_seq");
        setAllowAdd(false);
        setAllowDelete(false);
        setAllowEdit(false);
        setQueryWhereCondition(" and paid_type = 1 and (status = 2 or status = 3 ) "); // chi load HD tra truoc
       
        addTextFieldToForm("ContractID", new TextField(), "CONTRACT_ID", "int", false, 50, null, null, false, false,null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.code", new TextField(), "code", "string", false, 50, null, null, true, false,null, false, null, true, true, false, false, null);
        addTextFieldToForm("Contract.number", new TextField(), "CONTRACT_NUMBER", "string", false, 50, null, null, true, false,null, false, null, true, true, false, false, null);
        addTextFieldToForm("Contract.name", new TextField(), "name", "string", false, 100, null, null, true, false,null, false, null, true, true, false, false, null);
        //addComponentToForm("Contract.payer", new TextField(), "PAYER", "string", false, 75, null, null, true, false,null, false, null, true, false, false, false, null);
        addTextFieldToForm("Contract.signDate", new PopupDateField(), "SIGN_DATE", "date", false, null, null, null, false, false,null, false, null, true, false, false, false, null);
        addTextFieldToForm("Contract.acceptanceDate", new PopupDateField(), "ACCEPTANCE_DATE", "date", false, null, null, null, false, false,null, false, null, true, false, false, false, null);
        addTextFieldToForm("Contract.expireDate", new PopupDateField(), "EXPIRE_DATE", "date", false, null, null, null, false, false,null, false, null, false, false, false, false, null);
        addComboBoxToForm("Contract.customer", new ComboBox(), "CUSTOMER_ID", "int",
                true, 50, null, null, true, false, null, false, null, true, true, true, false, "select CUSTOMER_ID, code from bm_customer", null, "bm_customer", 
                "CUSTOMER_ID", "int", "code", null, null, false, true, null, null);
        addTextFieldToForm("Customer.CustomerName", new TextField(), "customer_name", "string", false, 150, null, null, true, false, null, false, null, true, true, false, false, null);
        addTextFieldToForm("Contract.address", new TextField(), "address", "string", false, 100, null, null, false, false,null, false, null, true, true, false, false, null);
        Object[][] paytype = {{2, "Transaction.bankplus"}, {3, "Transaction.card"}, {4, "Transaction.cash"}};
        addComboBoxToForm("Transaction.paymentType", new ComboBox(), "payment_method", "int",
                false, 50, null, null, false, false,null, false, null, true, true, true, false, paytype, "1", "Transaction.transfer");   
        addComboBoxToForm("Policy.ServiceID", new ComboBox(), "service_id", "int",
                false, 50, null, null, true, false, null, false, null, true, true, true, false, "select service_id, code from bm_service", null, "bm_service", 
                "service_id", "int", "code", null, null, false, true,null, null);  
        addComboBoxToForm("Contract.policyID", new ComboBox(), "policy_id", "int",
                false, 50, null, null, true, false, null, false, null, true, true, true, false, "select policy_id, code, service_id from bm_policy", null, "bm_policy", 
                "policy_id", "int", "code", null, null, false, true,"service_id", "service_id");
        // muc dich: chi hien thi tren datagrid, khong cho nhap tren man hinh them moi        
        Object[][] postageType = {{2, "Postage.Postpaid"}};
        addComboBoxToForm("Contract.paidType", new ComboBox(), "paid_type", "int",
                false, 50, null, null, false, false, null, false, null, true, true, false, false, postageType, "1", "Postage.prepay");
        addTextFieldToForm("Contract.startCost", new TextField(), "start_cost", "long", false, 50, null, null, false, false, null, false, null, false, false, true, true, null);  
        addTextFieldToForm("Contract.totalCost", new TextField(), "cost", "long", false, 50, null, null, false, false, null, false, null, false, false, true, true, null);  
        Object[][] contractStatus = {{2, "Contract.Active"}, {3, "Contract.Expired"}, {4, "Contract.Suspended"}, {5, "Contract.Canceled"}, {6, "Contract.Termination"}};
        addComboBoxToForm("Contract.status", new ComboBox(), "status", "int",
                false, 50, null, null, false, false, null, false, null, false, false, true, true, contractStatus, "1", "Contract.New");        
        addTextFieldToForm("Contract.delegate", new TextField(), "delegate", "string", false, 50, null, null, false, false,null, false, null, true, true, false, false, null);
        addTextFieldToForm("Contract.delegateRole", new TextField(), "delegate_Role", "string",false, 50, null, null, false, false,null, false, null, true, true, false, false, null);
        addTextFieldToForm("Contract.delegatePhone", new TextField(), "delegate_phone", "string", false, 50, null, null, false, false,null, false, null, true, true, false, false, null);
        addTextFieldToForm("Contract.delegateMobile", new TextField(), "delegate_mobile", "string", false, 50, null, null, false, false,null, false, null, true, true, false, false, null);
        addTextFieldToForm("Contract.delegateEmail", new TextField(), "delegate_email", "string", false, 50, null, null, false, false,null, false, null, true, true, false, false, null);
        
        Date toDate = new Date();
        Date fromDate = new Date(toDate.getTime() - 60l * 24l * 3600l * 1000l);
        List lstParam = new ArrayList();
        lstParam.add(fromDate);
        lstParam.add(toDate);
        setComponentAsSysdate("User.CreateDate", "create_date", true, "date:and_mandatory:366", false, lstParam);        
        setComponentAsLoginUser("User.CreateUser", "create_user", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null , null, INT_VIEWGROUP_ALL);
        addCustomizeComponentToForm("History.Reason", txtReason, "string", true, 100, null, null, false, true, true, true, true, null);
        addCustomizeComponentToForm("History.Requester", txtRequester, "string", true, 100, null, null, false, true, true, true, true, null);
        addCustomizeComponentToForm("Customer.IDNo", txtRequesterIdNO, "string", true, 100, null, null, false, true, true, true, true, null);
        //AnhPTN: them nut gia han
        Button buttonExtent = new Button(ResourceBundleUtils.getLanguageResource("Button.ExtentContract"));
        buttonExtent.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    buttonEditClick();
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }                
            }
        });
        addButton(buttonExtent);
        
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
                } catch(Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        return dataPanel;
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
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("Customer.CustomerName"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("Contract.address"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("Contract.newExpireDate"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("History.Requester"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("Customer.IDNo"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("History.Reason"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("History.ChangeUser"), String.class, null);
        return temp;
    }
    
    /**
     * Hàm khởi tạo bảng history
     *
     * @since 04/01/2015 HienDM
     * @return bảng lưu trữ
     */
    public void insertToHistoryTable(List<Map> lstHistory) {
        Object[] arrayIds = historyTable.getItemIds().toArray();
        for(int i = 0; i < arrayIds.length; i++) {
            historyTable.removeItem(arrayIds[i]);
        }
        for(int i = 0; i < lstHistory.size(); i++) {
            Object[] data = new Object[9];
            Map row = lstHistory.get(i);
            if(row.get("change_date_his") != null)
                data[0] = row.get("change_date_his").toString();
            if(row.get("contract_code") != null)
                data[1] = row.get("contract_code").toString();
            if(row.get("customer_name") != null)
                data[2] = row.get("customer_name").toString();
            if(row.get("address") != null)
                data[3] = row.get("address").toString();
            if(row.get("expire_date") != null)
                data[4] = row.get("expire_date").toString();
            if(row.get("person_request_change_his") != null)
                data[5] = row.get("person_request_change_his").toString();
            if(row.get("cmt_person_request_change_his") != null)
                data[6] = row.get("cmt_person_request_change_his").toString();
            if(row.get("cause_change_his") != null)
                data[7] = row.get("cause_change_his").toString();
            if(row.get("change_user_login_id_his") != null)
                data[8] = row.get("change_user_login_id_his").toString();
            historyTable.addItem(data, row.get("id_his").toString());
        }
    }
    /**
     * Hàm truy vấn dữ liệu bảng history
     *
     * @since 04/01/2015 HienDM
     * @return bảng lưu trữ
     */
    private List<Map> selectHistory(long id, Connection connection) throws Exception {
        String accSQL = " select id_his, CHANGE_DATE_HIS, " +
                        " (select code from bm_contract where contract_id = bm_contract_his.contract_id) contract_code, " +
                        " customer_name, address, expire_date, PERSON_REQUEST_CHANGE_HIS, CMT_PERSON_REQUEST_CHANGE_HIS, CAUSE_CHANGE_HIS, (select user_name from sm_users where user_id = bm_contract_his.CHANGE_USER_LOGIN_ID_HIS) change_user_login_id_his " +
                        " from bm_contract_his where contract_id = ? and TYPE_CHANGE = 2 order by CHANGE_DATE_HIS desc ";
        List lstParameter = new ArrayList();
        lstParameter.add(id);
        return C3p0Connector.queryData(accSQL, lstParameter, connection);
    }
     /**
     * Hàm truy vấn dữ liệu bảng history
     *
     * @since 04/01/2015 HienDM
     * @return bảng lưu trữ
     */
    private List<Map> selectHistory(long id) throws Exception {
        String accSQL = " select id_his, CHANGE_DATE_HIS, " +
                        " (select code from bm_contract where contract_id = bm_contract_his.contract_id) contract_code, " +
                        " customer_name, address, expire_date, PERSON_REQUEST_CHANGE_HIS, CMT_PERSON_REQUEST_CHANGE_HIS, CAUSE_CHANGE_HIS, (select user_name from sm_users where user_id = bm_contract_his.CHANGE_USER_LOGIN_ID_HIS) change_user_login_id_his " +
                        " from bm_contract_his where contract_id = ? and TYPE_CHANGE = 2 order by CHANGE_DATE_HIS desc ";
        List lstParameter = new ArrayList();
        lstParameter.add(id);
        return C3p0Connector.queryData(accSQL, lstParameter);
    }
    @Override
    public boolean validateEdit(long conID) throws Exception{
        // chinh sach con hieu luc moi co the ap dung tiep
        AbstractField abfPolID = getComponent("policy_id");
        long polID = Long.parseLong(abfPolID.getValue().toString());
        String ckValidPolSQL = "select * from bm_policy po where sysdate between po.from_date and po.to_date and po.policy_id = ?";
        List polPara = new ArrayList();
        polPara.add(polID);
        List <Map> polRows = C3p0Connector.queryData(ckValidPolSQL, polPara);
        if(polRows.isEmpty()){
            Notification.show(ResourceBundleUtils.getLanguageResource("ExtentContract.PolicyExpired"),
                                            null, Notification.Type.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    @Override
    public void afterEditData(Connection connection, long id) throws Exception {   
        // lay thoi han gia han them
        AbstractField abfPolID = getComponent("policy_id");
        long polID = Long.parseLong(abfPolID.getValue().toString());
        PostageAction posAct = new PostageAction();
        int period = posAct.getPeriod(polID);
        // tinh expire date moi
        Date fromDate = new Date();
        Date expireDate = new Date();
        PopupDateField pdfExpireDate = (PopupDateField) getComponent("expire_date");
        expireDate = pdfExpireDate.getValue();
        Calendar cal = Calendar.getInstance(); 
        ContractAction conAct = new ContractAction();
        long conSTT = conAct.getContractStatusFrDB(id);
        if(conSTT == 2){// hd dang hoat dong
            cal.setTime(expireDate); // gia han tu ngay het han
            fromDate = expireDate;
        }
        if(conSTT == 3){ // hd da het han
            cal.setTime(new Date()); // gia han tu ngay thuc hien gia han
        }
        cal.add(Calendar.MONTH, period);
        Date newExpireDate = new Date();
        newExpireDate = cal.getTime();
        // insert CONTRACT_HIS
        String accSQL = " insert into bm_contract_his (id_his, contract_id, customer_name,  "
                + " CHANGE_DATE_HIS, EXPIRE_DATE, type_change, "
                + " PERSON_REQUEST_CHANGE_HIS, CMT_PERSON_REQUEST_CHANGE_HIS, CAUSE_CHANGE_HIS, status_old, status_new, change_user_login_id_his ) values "
                + " (bm_contract_his_seq.nextval, ?, ?, sysdate, ?, 2, ?, ?, ?, ?, 2, ?) ";
        List lstParameter = new ArrayList();
        lstParameter.add(id);
        lstParameter.add(getComponent("customer_name").getValue());
        lstParameter.add(newExpireDate);
        lstParameter.add(txtRequester.getValue());
        lstParameter.add(txtRequesterIdNO.getValue());
        lstParameter.add(txtReason.getValue());
        lstParameter.add(conSTT);
        lstParameter.add(VaadinUtils.getSessionAttribute("G_UserId").toString());
        C3p0Connector.excuteData(accSQL, lstParameter, connection);
        // cap nhat lai DATA GRID history
        List<Map> lstHistory = selectHistory(id, connection);
        insertToHistoryTable(lstHistory);
        // cap nhat expire_date va trang thai trong hop dong conSTT = 2
        String udContractSQL = "update bm_contract con set con.status = 2 , con.expire_date = ? " +
                                " where con.contract_id = ?";
        List udConPara = new ArrayList();
        udConPara.add(newExpireDate);
        udConPara.add(id);
        C3p0Connector.excuteData(udContractSQL, udConPara, connection);
        // add them extend cost vao cost trong hop dong khi la prepaid (paid_type = 1)
        if(getComponent("paid_type").getValue().equals("1")){ 
            // tinh extent cost
            float exCost = 0; 
            float exPostageCost = conAct.getPostageCost(id, connection);
            float discount = conAct.getContractDiscountFrDB(id, connection);
            exCost = exPostageCost * (100-discount)/100;
            String udConCostSQL =" update bm_contract con " +
                                " set con.cost = con.cost + " + exCost +
                                " where con.contract_id = ?";
            List udConCostPara = new ArrayList();
            udConCostPara.add(id);
            C3p0Connector.excuteData(udConCostSQL, udConCostPara, connection);
            // insert transaction
            String isrtTranSQL = "insert into bm_sale_transaction tr (tr.transaction_id, tr.from_date, tr.to_date, " +
                        " tr.status, tr.contract_id, tr.postage_id, tr.cost, tr.service_id) " +
                        " select bm_sale_transaction_seq.nextval, ? from_date,? to_date, " +
                        " 1, con.contract_id, po.postage_id, ? cost, con.service_id " +
                        " from bm_contract con left JOIN bm_policy po on con.policy_id = po.policy_id " +
                        " where con.contract_id = ?"; 
            List isrtTranPara = new ArrayList();
            isrtTranPara.add(fromDate);
            isrtTranPara.add(newExpireDate);
            isrtTranPara.add(exCost);
            isrtTranPara.add(id);
            if(exCost!=0)
                C3p0Connector.excuteData(isrtTranSQL, isrtTranPara, connection);
        }
        
        // insert acount his
        String isrtAccHisSQL = " insert into bm_account_his h (h.id_his, h.account_id, h.status_old, h.status_new, h.change_user_login_id_his, " +
                                " h.cause_change_his, h.person_request_change_his, h.change_date_his, h.cmt_person_request_change_his, " +
                                " h.contract_id) " +
                                " select bm_account_his_seq.nextval, a.account_id, a.status, 5, ?, " +
                                " ?, ?, sysdate, ?, " +
                                " a.contract_id " +
                                " from bm_account a where a.contract_id = ? and a.status = 4 ";
        List isrtAccPara = new ArrayList();
        isrtAccPara.add(VaadinUtils.getSessionAttribute("G_UserId").toString());
        isrtAccPara.add(txtReason.getValue());
        isrtAccPara.add(txtRequester.getValue());
        isrtAccPara.add(txtRequesterIdNO.getValue());
        isrtAccPara.add(id);
        C3p0Connector.excuteData(isrtAccHisSQL, isrtAccPara, connection);
        // accSTT 4 -> 5
        String udAccountSQL = "update bm_account a set a.status = 5 where a.contract_id =? and a.status = 4";
        List udAccountPara = new ArrayList();
        udAccountPara.add(id);
        C3p0Connector.excuteData(udAccountSQL, udAccountPara, connection);
        connection.commit();
        updateDataRefreshData();
    }
}
