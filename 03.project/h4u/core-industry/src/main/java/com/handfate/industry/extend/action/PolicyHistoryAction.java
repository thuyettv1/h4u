/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.MainUI;
import static com.handfate.industry.core.MainUI.mainLogger;
import com.handfate.industry.core.action.BaseAction;
import static com.handfate.industry.core.action.BaseAction.INT_VIEWGROUP_ALL;
import com.handfate.industry.core.action.PopupSingleUserAction;
import com.handfate.industry.core.action.component.ComboboxItem;
import com.handfate.industry.core.action.component.DownloadLink;
import com.handfate.industry.core.action.component.PagedTable;
import com.handfate.industry.core.action.component.UploadField;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class PolicyHistoryAction extends ContractAction {

    public Table historyTable = new Table();
    public TextField txtReason = new TextField();
    public TextField txtRequester = new TextField();
    public TextField txtRequesterIdNO = new TextField();
    
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
        setSortColumnName("CONTRACT_ID");
        setSortAscending(false);
        setSequenceName("bm_contract_seq");
        setIgnoreAttachWhenEdit(true);
        setAllowAdd(false);
        setAllowDelete(false);

        addTextFieldToForm("ContractID", new TextField(), "CONTRACT_ID", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.code", new TextField(), "code", "string", true, 50, null, null, true, false, null, false, null, true, true, false, false, null);
        addTextFieldToForm("Contract.name", new TextField(), "name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Contract.number", new TextField(), "CONTRACT_NUMBER", "string", true, 50, null, null, true, false, null, false, null, true, true, true, false, null);
        Object[][] contractStatus = {{2, "Contract.Active"}, {3, "Contract.Expired"}, {4, "Contract.Suspended"}, {5, "Contract.Canceled"}, {6, "Contract.Termination"}};
        addComboBoxToForm("Contract.status", new ComboBox(), "status", "int",
                false, 50, null, null, true, false, null, false, null, false, false, true, true, contractStatus, "1", "Contract.New");
        setComponentAsLoginUser("User.CreateUser", "create_user", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null, null, INT_VIEWGROUP_RECURSIVE);
        addTextFieldToForm("Contract.signDate", new PopupDateField(), "SIGN_DATE", "date", true, null, null, null, false, false, null, false, null, true, false, true, true, null);
        addTextFieldToForm("Contract.acceptanceDate", new PopupDateField(), "ACCEPTANCE_DATE", "date", true, null, null, null, false, false, null, false, null, true, false, true, true, null);
        addTextFieldToForm("Contract.expireDate", new PopupDateField(), "EXPIRE_DATE", "date", false, null, null, null, false, false, null, false, null, false, false, true, true, null);
        addTextFieldToForm("Contract.payer", new TextField(), "PAYER", "string", false, 75, null, null, true, false, null, false, null, true, false, true, true, null);
        Object[][] paytype = {{2, "Transaction.bankplus"}, {3, "Transaction.card"}, {4, "Transaction.cash"}};
        addComboBoxToForm("Transaction.paymentType", new ComboBox(), "payment_method", "int",
                false, 50, null, null, false, false, null, false, null, true, false, true, true, paytype, "1", "Transaction.transfer");

        addSinglePopupToForm("Invoice.CustomerID", "customer_id", "int", true, 50, null, null, true, null, false, null, true, false, true, true, new PopupUpdateCustomerAction(localMainUI), 2,
                null, "", "customer_id", "code", "bm_customer", null, null);

        addTextFieldToForm("Customer.CustomerName", new TextField(), "customer_name", "string", true, 150, null, null, true, false, null, false, null, true, false, true, true, null);
        addTextFieldToForm("Contract.delegate", new TextField(), "delegate", "string", false, 50, null, null, false, false, null, false, null, true, false, true, true, null);
        addTextFieldToForm("Contract.address", new TextField(), "address", "string", false, 100, null, null, false, false, null, false, null, true, false, true, true, null);
        addTextFieldToForm("Contract.delegateRole", new TextField(), "delegate_Role", "string", false, 50, null, null, false, false, null, false, null, true, false, true, true, null);
        addTextFieldToForm("Contract.delegatePhone", new TextField(), "delegate_phone", "string", false, 50, null, null, false, false, null, false, null, true, false, true, true, null);
        addTextFieldToForm("Contract.delegateMobile", new TextField(), "delegate_mobile", "string", false, 50, null, null, false, false, null, false, null, true, false, true, true, null);
        addTextFieldToForm("Contract.delegateEmail", new TextField(), "delegate_email", "string", false, 50, null, null, false, false, null, false, null, true, false, true, true, null);

        addSinglePopupToForm("Policy.ServiceID", "service_id", "int", true, 50, null, null, true, null, false, null, true, true, true, false, new PopupSingleServiceAction(localMainUI), 2,
                null, "", "service_id", "code", "bm_service", null, null);

        Object[][] postageType = {{2, "Postage.Postpaid"}};
        addComboBoxToForm("Contract.paidType", new ComboBox(), "paid_type", "int",
                true, 50, null, null, true, false, null, false, null, true, false, true, true, postageType, "1", "Postage.prepay");
        addSinglePopupToForm("Contract.policyID", "policy_id", "int", true, 50, null, null, true, null, false, null, true, true, true, true, new PopupSinglePolicyAction(localMainUI), 2,
                null, "", "policy_id", "code", "v_policy_postage", null, null);
        addSinglePopupToForm("Contract.discount", "discount_id", "int", false, 50, null, null, false, null, false, null, true, true, true, true, new PopupSingleDiscountAction(localMainUI), 2,
                null, "", "id", "DISCOUNT_CONTRACT", "bm_discount", null, null);

        addTextFieldToForm("Contract.startCost", new TextField(), "start_cost", "long", false, 50, null, null, false, false, null, false, null, false, false, true, true, null);
        addTextFieldToForm("Contract.totalCost", new TextField(), "cost", "long", false, 50, null, null, false, false, null, false, null, false, false, true, true, null);
        addTextFieldToForm("Contract.accout", new TextField(), "AMOUNT_OF_ACCOUNT", "int", true, 10, null, null, false, false, null, false, null, true, false, true, true, null);
        // so luong hang hoa
        // chon hang hoa gan voi chinh sach
        List<List> lstAttachField = new ArrayList();
        List lstRow = addComponentToMultiPopup("Good.amount", "amount", "int", true, 10, "int>0");
        lstAttachField.add(lstRow);
        addMultiPopupToForm("Contract.GoodList", false, false, new PopupMultiContractGoodAction(localMainUI), 2, lstAttachField,
                "bm_contract_policy_good", "POLICY_GOOD_ID", "contract_id", "id", "bm_contract_policy_good_seq", "policy_id", "POLICY_ID", "v_policy_good");

        //phi khoi tao duoc lay tu dich vu
        //UploadField fileAttach = new UploadField();
        //addUploadFieldToForm("Contract.attachFile", fileAttach, "ATTACH_FILE", "file", false, null, null, null, false, ContractAction.class.toString(), false, 10);
        Date toDate = new Date();
        Date fromDate = new Date(toDate.getTime() - 60l * 24l * 3600l * 1000l);
        List lstParam = new ArrayList();
        lstParam.add(fromDate);
        lstParam.add(toDate);
        setComponentAsSysdate("User.CreateDate", "create_date", true, "date:and_mandatory:366", false, lstParam);

        addCustomizeComponentToForm("History.Reason", txtReason, "string", true, 100, null, null, false, true, true, true, true, null);
        addCustomizeComponentToForm("History.Requester", txtRequester, "string", true, 100, null, null, false, true, true, true, true, null);
        addCustomizeComponentToForm("Customer.IDNo", txtRequesterIdNO, "string", true, 100, null, null, false, true, true, true, true, null);

        //AnhPTN: them nut Huy
        /*Button buttonCancel = new Button(ResourceBundleUtils.getLanguageResource("Button.CancelContract"));
        buttonCancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    buttonCancelContractClick();
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        addButton(buttonCancel);*/

        Property.ValueChangeListener lsPolicyCombo = new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    getComponent("discount_id").setValue(null);
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }
            }
        };
        ComboBox cboPolicy = (ComboBox) getComponent("policy_id");
        cboPolicy.addValueChangeListener(lsPolicyCombo);
        
        Property.ValueChangeListener lsSerCombo = new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    getComponent("policy_id").setValue(null);
                    setContractCode();
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }
            }
        };
        ComboBox cboSer = (ComboBox) getComponent("service_id");
        cboSer.addValueChangeListener(lsSerCombo);

        Property.ValueChangeListener lsPaidTypeCombo = new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    setContractCode();
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }
            }
        };
        ComboBox cboPaidType = (ComboBox) getComponent("paid_type");
        cboPaidType.addValueChangeListener(lsPaidTypeCombo);

        Property.ValueChangeListener lsSignDatePuD = new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    setContractCode();
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }
            }
        };
        PopupDateField pudSignDate = (PopupDateField) getComponent("sign_date");
        pudSignDate.addValueChangeListener(lsSignDatePuD);

        Property.ValueChangeListener listenerCustomer = new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    ComboBox cboCus = ((ComboBox) getComponent("customer_id"));
                    if (cboCus.getValue() != null && !cboCus.getValue().toString().isEmpty()) {
                        String accSQL = " select name from bm_customer where customer_id = ? ";
                        List lstParameter = new ArrayList();
                        lstParameter.add(Long.parseLong(cboCus.getValue().toString()));
                        List<Map> lstName = C3p0Connector.queryData(accSQL, lstParameter);
                        if (lstName != null && lstName.size() > 0) {
                            if (lstName.get(0).get("name") != null) {
                                ((TextField) getComponent("customer_name")).setValue(lstName.get(0).get("name").toString());
                            }
                        }
                    }
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }
            }
        };
        ComboBox cboCustomer = (ComboBox) getComponent("customer_id");
        cboCustomer.addValueChangeListener(listenerCustomer);
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
        dataPanel.addComponent(historyTable);
        
        HorizontalLayout buttonHisLayout = new HorizontalLayout();
        buttonHisLayout.setWidth("100%");
        Button buttonHistory = new Button(ResourceBundleUtils.getLanguageResource("Button.History"));
        buttonHistory.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    /*Window popupWindow = new Window();
                    popupWindow.setWidth(popupWidth);
                    popupWindow.setHeight(popupHeight);
                    popupWindow.setCaption(ResourceBundleUtils.getLanguageResource("Button.History"));
                    VerticalLayout bodyPage = new VerticalLayout();
                    bodyPage.addStyleName("page-body");
                    bodyPage.addComponent(popup.initPanel(column, component, popupWindow));
                    popupWindow.setContent(bodyPage);
                    popupWindow.setModal(true);
                    mainUI.addWindow(popupWindow);                    */
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        buttonHisLayout.addComponent(buttonHistory);
        buttonHisLayout.setComponentAlignment(buttonHistory, Alignment.MIDDLE_CENTER);
        //dataPanel.addComponent(buttonHisLayout);
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
                + " contract_code, (select name from bm_policy where policy_id = bm_contract_his.policy_id_new) policy_new, "
                + " (select name from bm_policy where policy_id = bm_contract_his.policy_id_old) policy_old, "
                + " (select discount_contract from bm_discount where id = bm_contract_his.discount_new) discount_new, "
                + " (select discount_contract from bm_discount where id = bm_contract_his.discount_old) discount_old, "
                + " PERSON_REQUEST_CHANGE_HIS, CMT_PERSON_REQUEST_CHANGE_HIS, CAUSE_CHANGE_HIS, "
                + " (select user_name from sm_users where user_id = bm_contract_his.CHANGE_USER_LOGIN_ID_HIS) change_user "
                + " from bm_contract_his where contract_id = ? and TYPE_CHANGE = 1 order by CHANGE_DATE_HIS desc ";
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
                + " contract_code, (select name from bm_policy where policy_id = bm_contract_his.policy_id_new) policy_new, "
                + " (select name from bm_policy where policy_id = bm_contract_his.policy_id_old) policy_old, "
                + " (select discount_contract from bm_discount where id = bm_contract_his.discount_new) discount_new, "
                + " (select discount_contract from bm_discount where id = bm_contract_his.discount_old) discount_old, "
                + " PERSON_REQUEST_CHANGE_HIS, CMT_PERSON_REQUEST_CHANGE_HIS, CAUSE_CHANGE_HIS, "
                + " (select user_name from sm_users where user_id = bm_contract_his.CHANGE_USER_LOGIN_ID_HIS) change_user "
                + " from bm_contract_his where contract_id = ? and TYPE_CHANGE = 1 order by CHANGE_DATE_HIS desc ";
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
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("Policy.New"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("Policy.Old"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("Discount.New"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("Discount.Old"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("History.Requester"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("Customer.IDNo"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("History.Reason"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("changeService.changeUser"), String.class, null);
        
        return temp;
    }
    
    /**
     * Hàm khởi tạo bảng history
     *
     * @since 04/01/2015 HienDM
     */
    public void insertToHistoryTable(List<Map> lstHistory) {
        Object[] arrayIds = historyTable.getItemIds().toArray();
        for(int i = 0; i < arrayIds.length; i++) {
            historyTable.removeItem(arrayIds[i]);
        }
        for(int i = 0; i < lstHistory.size(); i++) {
            Object[] data = new Object[10];
            Map row = lstHistory.get(i);
            if(row.get("change_date_his") != null)
                data[0] = row.get("change_date_his").toString();
            if(row.get("contract_code") != null)
                data[1] = row.get("contract_code").toString();
            if(row.get("policy_new") != null)
                data[2] = row.get("policy_new").toString();
            if(row.get("policy_old") != null)
                data[3] = row.get("policy_old").toString();
            if(row.get("discount_new") != null)
                data[4] = row.get("discount_new").toString();
            if(row.get("discount_old") != null)
                data[5] = row.get("discount_old").toString();
            if(row.get("person_request_change_his") != null)
                data[6] = row.get("person_request_change_his").toString();
            if(row.get("cmt_person_request_change_his") != null)
                data[7] = row.get("cmt_person_request_change_his").toString();
            if(row.get("cause_change_his") != null)
                data[8] = row.get("cause_change_his").toString();
            if(row.get("change_user") != null)
                data[9] = row.get("change_user").toString();

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
        Item data = table.getItem("" + id);
        Long hisId = C3p0Connector.getSequenceValue("bm_contract_his_seq");
        ComboboxItem cboItem = (ComboboxItem) data.getItemProperty(
                ResourceBundleUtils.getLanguageResource("Contract.policyID")).getValue();
        ComboboxItem cboDiscount = (ComboboxItem) data.getItemProperty(
                ResourceBundleUtils.getLanguageResource("Contract.discount")).getValue();
        String accSQL = " insert into bm_contract_his (id_his, contract_id, POLICY_ID_NEW, POLICY_ID_OLD, discount_new, discount_old, "
                + " PERSON_REQUEST_CHANGE_HIS, CMT_PERSON_REQUEST_CHANGE_HIS, "
                + " CAUSE_CHANGE_HIS, type_change, CHANGE_DATE_HIS, change_date_old, CHANGE_USER_LOGIN_ID_HIS ) values "
                + " (?,?,?,?,?,?,?,?,?,1, sysdate, "
                + " (select max(CHANGE_DATE_HIS) from bm_contract_his where TYPE_CHANGE = 1 and contract_id = ?), ? ) ";
        List lstParameter = new ArrayList();
        lstParameter.add(hisId);
        lstParameter.add(id);
        lstParameter.add(getComponent("policy_id").getValue());
        lstParameter.add(cboItem.getValue());
        lstParameter.add(getComponent("discount_id").getValue());
        lstParameter.add(cboDiscount.getValue());
        lstParameter.add(txtRequester.getValue());
        lstParameter.add(txtRequesterIdNO.getValue());
        lstParameter.add(txtReason.getValue());
        lstParameter.add(id);
        lstParameter.add(loginId);
        C3p0Connector.excuteData(accSQL, lstParameter, connection);
        
        Table goodTable = getMultiComponent("bm_contract_policy_good");
        Object[] arrayId = goodTable.getItemIds().toArray();
        String sqlGood = " insert into bm_his_policy_good (id, amount, his_id, policy_good_id) values "
                + " (bm_his_policy_good_seq.nextval, ?, ?, ?) ";
        List lstBatch = new ArrayList();
        for(int i=0; i < arrayId.length; i++) {
            Item dataGood = goodTable.getItem(arrayId[i].toString());
            List lstParam = new ArrayList();
            HorizontalLayout txtLayout = (HorizontalLayout)dataGood.getItemProperty(ResourceBundleUtils.getLanguageResource("Good.amount")).getValue();
            TextField txt = (TextField)txtLayout.getComponent(0);
            lstParam.add(txt.getValue());
            lstParam.add(hisId);
            lstParam.add(Long.parseLong(arrayId[i].toString()));
            lstBatch.add(lstParam);
        }
        C3p0Connector.excuteDataBatch(sqlGood, lstBatch, connection);
        
        goodTable.setCaption(null);
        List<Map> lstHistory = selectHistory(id, connection);
        insertToHistoryTable(lstHistory);
    }
    
    @Override
    public void beforeEditData(Connection connection, long id) throws Exception {
        Table goodTable = getMultiComponent("bm_contract_policy_good");
        Object[] arrayId = goodTable.getItemIds().toArray();
        if(arrayId != null && arrayId.length > 0) {
            Long lSumGood = 0L;
            List lstParamPrice = new ArrayList();
            String sqlPrice = "select id, price from bm_policy_good where id in (?";
            lstParamPrice.add(Long.parseLong(arrayId[0].toString()));
            for(int i = 1 ; i < arrayId.length; i++) {
                sqlPrice += ",?";
                lstParamPrice.add(Long.parseLong(arrayId[i].toString()));
            }
            sqlPrice += ")";
            List<Map> lstPrice = C3p0Connector.queryData(sqlPrice, lstParamPrice, connection);
            
            for(int i = 0 ; i < arrayId.length; i++) {
                Item dataGood = goodTable.getItem(arrayId[i].toString());
                HorizontalLayout txtLayout = (HorizontalLayout) dataGood.getItemProperty(ResourceBundleUtils.getLanguageResource("Good.amount")).getValue();
                TextField txt = (TextField) txtLayout.getComponent(0);
                long txtAmount = Long.parseLong(txt.getValue());
                for(int j = 0; j < lstPrice.size(); j++) {
                    if(lstPrice.get(j).get("id").toString().equals(arrayId[i].toString())) {
                        lSumGood += txtAmount * Long.parseLong(lstPrice.get(j).get("price").toString());
                        break;
                    }
                }
            }

            String sql2 = "insert into bm_sale_transaction tr (tr.transaction_id, tr.from_date, tr.to_date, "
                    + "tr.status, tr.contract_id, tr.postage_id, tr.cost, tr.service_id) "
                    + "select bm_sale_transaction_seq.nextval, sysdate, sysdate, "
                    + "1, con.contract_id, po.postage_id, ? cost , con.service_id "
                    + "from bm_contract con left JOIN bm_policy po on con.policy_id = po.policy_id "
                    + "where con.contract_id = ?";
            List lstParameter2 = new ArrayList();
            lstParameter2.add(lSumGood);
            lstParameter2.add(id);
            C3p0Connector.excuteData(sql2, lstParameter2, connection);
        }
    } 
    
    @Override
    public void afterPrepareEdit() throws Exception {
        super.afterPrepareEdit();
        Table goodTable = getMultiComponent("bm_contract_policy_good");
        goodTable.setCaption(ResourceBundleUtils.getLanguageResource("Contract.GoodIncreasing"));
        goodTable.removeAllItems();
    }    
    
    @Override
    public boolean validateEdit(long id) throws Exception {
        return true;
    }    
        
    @Override
    public boolean prepareEdit() throws Exception {
        Object[] selectedArray = ((java.util.Collection) table.getValue()).toArray();
        Item row = table.getItem(selectedArray[0]);
        ComboboxItem cboItem = (ComboboxItem)row.getItemProperty(ResourceBundleUtils.getLanguageResource("Contract.status")).getValue();
        if(!(cboItem != null && cboItem.getValue() != null && cboItem.getValue().toString().equals("2"))) {
            Notification.show(ResourceBundleUtils.getLanguageResource("PolicyHistory.Error.InvalidStatus"),
                    null, Notification.Type.ERROR_MESSAGE);            
            return false;
        }
        return true;
    }     
    
    @Override
    public void afterPrepareSearch() throws Exception {
        Table goodTable = getMultiComponent("bm_contract_policy_good");
        if(goodTable != null) goodTable.setCaption(null);
    }
}
