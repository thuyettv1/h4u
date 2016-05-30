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
import com.handfate.industry.core.action.component.DownloadLink;
import com.handfate.industry.core.action.component.PagedTable;
import com.handfate.industry.core.action.component.UploadField;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class ContractHistoryAction extends BaseAction {

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
        setAllowDelete(false);
       
        addTextFieldToForm("ContractID", new TextField(), "CONTRACT_ID", "int", true, 50, null, null, false, false,null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.code", new TextField(), "code", "string", true, 50, null, null, true, false,null, false, null, true, true, true, false, null);
        addTextFieldToForm("Contract.number", new TextField(), "CONTRACT_NUMBER", "string", true, 50, null, null, true, false,null, false, null, true, true, true, false, null);
        addTextFieldToForm("Contract.name", new TextField(), "name", "string", true, 100, null, null, true, false,null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.payer", new TextField(), "PAYER", "string", false, 75, null, null, true, false,null, false, null, true, false, true, true, null);
        addTextFieldToForm("Contract.signDate", new PopupDateField(), "SIGN_DATE", "date", true, null, null, null, false, false,null, false, null, true, false, true, true, null);
        addTextFieldToForm("Contract.acceptanceDate", new PopupDateField(), "ACCEPTANCE_DATE", "date", true, null, null, null, false, false,null, false, null, true, false, true, true, null);
        addTextFieldToForm("Contract.expireDate", new PopupDateField(), "EXPIRE_DATE", "date", false, null, null, null, false, false,null, false, null, true, false, true, true, null);

        Object[][] paytype = {{2, "Transaction.bankplus"}, {3, "Transaction.card"}, {4, "Transaction.cash"}};
        addComboBoxToForm("Transaction.paymentType", new ComboBox(), "payment_method", "int",
                false, 50, null, null, false, false,null, false, null, true, true, true, false, paytype, "1", "Transaction.transfer");   

        addComboBoxToForm("Contract.customer", new ComboBox(), "CUSTOMER_ID", "int",
                true, 50, null, null, true, false, null, false, null, true, true, true, false, "select CUSTOMER_ID, code from bm_customer", null, "bm_customer", 
                "CUSTOMER_ID", "int", "code", null, null, false, true, null, null);
        addTextFieldToForm("Customer.CustomerName", new TextField(), "customer_name", "string", true, 150, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.address", new TextField(), "address", "string", false, 100, null, null, false, false,null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.delegate", new TextField(), "delegate", "string", false, 50, null, null, false, false,null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.delegateRole", new TextField(), "delegate_Role", "string",false, 50, null, null, false, false,null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.delegatePhone", new TextField(), "delegate_phone", "string", false, 50, null, null, false, false,null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.delegateMobile", new TextField(), "delegate_mobile", "string", false, 50, null, null, false, false,null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.delegateEmail", new TextField(), "delegate_email", "string", false, 50, null, null, false, false,null, false, null, true, true, true, true, null);
        addComboBoxToForm("Policy.ServiceID", new ComboBox(), "service_id", "int",
                true, 50, null, null, true, false, null, false, null, true, false, true, false, "select service_id, code from bm_service", null, "bm_service", 
                "service_id", "int", "code", null, null, false, true,null, null);  
        
        addComboBoxToForm("Contract.policyID", new ComboBox(), "policy_id", "int",
                true, 50, null, null, true, false, null, false, null, true, false, true, false, "select policy_id, code, service_id from bm_policy", null, "bm_policy", 
                "policy_id", "int", "code", null, null, false, true,"service_id", "service_id");
                
        // muc dich: chi hien thi tren datagrid, khong cho nhap tren man hinh them moi        
        addTextFieldToForm("Contract.startCost", new TextField(), "start_cost", "long", false, 50, null, null, false, false, null, false, null, false, false, true, true, null);  
        Date toDate = new Date();
        Date fromDate = new Date(toDate.getTime() - 60l * 24l * 3600l * 1000l);
        List lstParam = new ArrayList();
        lstParam.add(fromDate);
        lstParam.add(toDate);
        setComponentAsSysdate("User.CreateDate", "create_date", true, "date:and_mandatory:366", false, lstParam);
        setComponentAsLoginUser("User.CreateUser", "create_user", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null , null, INT_VIEWGROUP_RECURSIVE);
        
        addCustomizeComponentToForm("History.Reason", txtReason, "string", true, 100, null, null, false, true, true, true, true, null);
        addCustomizeComponentToForm("History.Requester", txtRequester, "string", true, 100, null, null, false, true, true, true, true, null);
        addCustomizeComponentToForm("Customer.IDNo", txtRequesterIdNO, "string", true, 100, null, null, false, true, true, true, true, null);
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
     * Hàm truy vấn dữ liệu bảng history
     *
     * @since 04/01/2015 HienDM
     * @return bảng lưu trữ
     */
    private List<Map> selectHistory(long id, Connection connection) throws Exception {
        String accSQL = " select id_his, CHANGE_DATE_HIS, (select code from bm_contract "
                + " where contract_id = bm_contract_his.contract_id) "
                + " contract_code, customer_name, address, delegate, "
                + " delegate_Role, delegate_phone, delegate_mobile, delegate_email, PERSON_REQUEST_CHANGE_HIS, "
                + " CMT_PERSON_REQUEST_CHANGE_HIS, CAUSE_CHANGE_HIS, "
                + " (select user_name from sm_users where user_id = bm_contract_his.CHANGE_USER_LOGIN_ID_HIS) change_user, status_new "
                + " from bm_contract_his where contract_id = ? and TYPE_CHANGE = 6 order by CHANGE_DATE_HIS desc ";
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
        String accSQL = " select id_his, CHANGE_DATE_HIS, (select code from bm_contract "
                + " where contract_id = bm_contract_his.contract_id) "
                + " contract_code, customer_name, address, delegate, "
                + " delegate_Role, delegate_phone, delegate_mobile, delegate_email, PERSON_REQUEST_CHANGE_HIS, "
                + " CMT_PERSON_REQUEST_CHANGE_HIS, CAUSE_CHANGE_HIS, "
                + " (select user_name from sm_users where user_id = bm_contract_his.CHANGE_USER_LOGIN_ID_HIS) change_user, status_new "
                + " from bm_contract_his where contract_id = ? and TYPE_CHANGE = 6 order by CHANGE_DATE_HIS desc ";
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
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("Customer.CustomerName"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("Contract.address"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("Contract.delegate"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("Contract.delegateRole"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("Contract.delegatePhone"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("Contract.delegateMobile"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("Contract.delegateEmail"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("History.Requester"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("Customer.IDNo"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("History.Reason"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("changeService.changeUser"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("Contract.status"), String.class, null);
        
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
            Object[] data = new Object[14];
            Map row = lstHistory.get(i);
            if(row.get("change_date_his") != null)
                data[0] = row.get("change_date_his").toString();
            if(row.get("contract_code") != null)
                data[1] = row.get("contract_code").toString();
            if(row.get("customer_name") != null)
                data[2] = row.get("customer_name").toString();
            if(row.get("address") != null)
                data[3] = row.get("address").toString();
            if(row.get("delegate") != null)
                data[4] = row.get("delegate").toString();
            if(row.get("delegate_role") != null)
                data[5] = row.get("delegate_role").toString();
            if(row.get("delegate_phone") != null)
                data[6] = row.get("delegate_phone").toString();
            if(row.get("delegate_mobile") != null)
                data[7] = row.get("delegate_mobile").toString();
            if(row.get("delegate_email") != null)
                data[8] = row.get("delegate_email").toString();
            if(row.get("person_request_change_his") != null)
                data[9] = row.get("person_request_change_his").toString();
            if(row.get("cmt_person_request_change_his") != null)
                data[10] = row.get("cmt_person_request_change_his").toString();
            if(row.get("cause_change_his") != null)
                data[11] = row.get("cause_change_his").toString();
            if(row.get("change_user") != null)
                data[12] = row.get("change_user").toString();
            if(row.get("status_new") != null) {
                String statusNew = "";
                if(row.get("status_new") != null) {
                    if(row.get("status_new").toString().equals("1")) 
                        statusNew = ResourceBundleUtils.getLanguageResource("Contract.New");
                    if(row.get("status_new").toString().equals("2")) 
                        statusNew = ResourceBundleUtils.getLanguageResource("Contract.Active");
                    if(row.get("status_new").toString().equals("3")) 
                        statusNew = ResourceBundleUtils.getLanguageResource("Contract.Expired");
                    if(row.get("status_new").toString().equals("4")) 
                        statusNew = ResourceBundleUtils.getLanguageResource("Contract.Suspended");
                    if(row.get("status_new").toString().equals("5")) 
                        statusNew = ResourceBundleUtils.getLanguageResource("Contract.Canceled");
                    if(row.get("status_new").toString().equals("6")) 
                        statusNew = ResourceBundleUtils.getLanguageResource("Contract.Termination");
                }
                data[13] = statusNew;
            }
            historyTable.addItem(data, row.get("id_his").toString());
        }
    }
    
    /**
     * Hàm thực hiện sau khi sửa dữ liệu
     *
     * @since 04/01/2015 HienDM
     */
    @Override
    public void afterEditData(Connection connection, long id) throws Exception {   
        Long loginId = Long.parseLong(VaadinUtils.getSessionAttribute("G_UserId").toString());
        String accSQL = " insert into bm_contract_his (id_his, contract_id, customer_name, address, delegate, delegate_role, "
                + " delegate_phone, delegate_mobile, delegate_email, PERSON_REQUEST_CHANGE_HIS, "
                + " CMT_PERSON_REQUEST_CHANGE_HIS, CAUSE_CHANGE_HIS, type_change, CHANGE_DATE_HIS, CHANGE_USER_LOGIN_ID_HIS ) values "
                + " (bm_contract_his_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,6, sysdate, ?) ";
        List lstParameter = new ArrayList();
        lstParameter.add(id);
        lstParameter.add(getComponent("customer_name").getValue());
        lstParameter.add(getComponent("address").getValue());
        lstParameter.add(getComponent("delegate").getValue());
        lstParameter.add(getComponent("delegate_role").getValue());
        lstParameter.add(getComponent("delegate_phone").getValue());
        lstParameter.add(getComponent("delegate_mobile").getValue());
        lstParameter.add(getComponent("delegate_email").getValue());
        lstParameter.add(txtRequester.getValue());
        lstParameter.add(txtRequesterIdNO.getValue());
        lstParameter.add(txtReason.getValue());
        lstParameter.add(loginId);
        
        C3p0Connector.excuteData(accSQL, lstParameter, connection);
        
        List<Map> lstHistory = selectHistory(id, connection);
        insertToHistoryTable(lstHistory);
    }
}
