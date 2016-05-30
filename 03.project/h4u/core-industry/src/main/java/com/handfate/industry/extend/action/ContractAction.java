/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.MainUI;
import static com.handfate.industry.core.MainUI.mainLogger;
import com.handfate.industry.core.action.BaseAction;
import com.handfate.industry.core.action.PopupSingleUserAction;
import com.handfate.industry.core.action.component.ComboboxItem;
import com.handfate.industry.core.action.component.ConfirmationDialog;
import com.handfate.industry.core.action.component.MultiUploadField;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class ContractAction extends BaseAction {

    /**
     * HÃ m khá»Ÿi táº¡o giao diá»‡n
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI VÃ¹ng giao diá»‡n cá»§a chá»©c nÄƒng
     * @return Giao diá»‡n sau khi khá»Ÿi táº¡o
     */
    String oldContractCode = "";
    String oldServiceCode = "";
    Date oldSignDate = new Date();
    boolean isFirstLoadEditForm = false;

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

        addTextFieldToForm("ContractID", new TextField(), "CONTRACT_ID", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.code", new TextField(), "code", "string", true, 50, null, null, true, false, null, false, null, true, true, false, false, null);
        addTextFieldToForm("Contract.name", new TextField(), "name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.number", new TextField(), "CONTRACT_NUMBER", "string", true, 50, null, null, true, false, null, false, null, true, true, true, true, null);
        Object[][] contractStatus = {{2, "Contract.Active"}, {3, "Contract.Expired"}, {4, "Contract.Suspended"}, {5, "Contract.Canceled"}, {6, "Contract.Termination"}};
        addComboBoxToForm("Contract.status", new ComboBox(), "status", "int",
                false, 50, null, null, true, false, null, false, null, false, false, true, true, contractStatus, "1", "Contract.New");
        addTextFieldToForm("Contract.payer", new TextField(), "PAYER", "string", false, 75, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.signDate", new PopupDateField(), "SIGN_DATE", "date", true, null, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.acceptanceDate", new PopupDateField(), "ACCEPTANCE_DATE", "date", true, null, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.expireDate", new PopupDateField(), "EXPIRE_DATE", "date", false, null, null, null, false, false, null, false, null, false, false, true, true, null);

        Object[][] paytype = {{2, "Transaction.bankplus"}, {3, "Transaction.card"}, {4, "Transaction.cash"}};
        addComboBoxToForm("Transaction.paymentType", new ComboBox(), "payment_method", "int",
                false, 50, null, null, false, false, null, false, null, true, true, true, true, paytype, "1", "Transaction.transfer");

        addSinglePopupToForm("Invoice.CustomerID", "customer_id", "int", true, 50, null, null, true, null, false, null, true, true, true, true, new PopupUpdateCustomerAction(localMainUI), 2,
                null, "", "customer_id", "code", "bm_customer", null, null);

        addTextFieldToForm("Customer.CustomerName", new TextField(), "customer_name", "string", true, 150, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.delegate", new TextField(), "delegate", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.address", new TextField(), "address", "string", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.delegateRole", new TextField(), "delegate_Role", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.delegatePhone", new TextField(), "delegate_phone", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.delegateMobile", new TextField(), "delegate_mobile", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Contract.delegateEmail", new TextField(), "delegate_email", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);

        addSinglePopupToForm("Policy.ServiceID", "service_id", "int", true, 50, null, null, true, null, false, null, true, true, true, true, new PopupSingleServiceAction(localMainUI), 2,
                null, "", "service_id", "code", "bm_service", null, null);

        Object[][] postageType = {{2, "Postage.Postpaid"}};
        addComboBoxToForm("Contract.paidType", new ComboBox(), "paid_type", "int",
                true, 50, null, null, true, false, null, false, null, true, true, true, true, postageType, "1", "Postage.prepay");
        addSinglePopupToForm("Contract.policyID", "policy_id", "int", true, 50, null, null, true, null, false, null, true, true, true, true, new PopupSinglePolicyAction(localMainUI), 2,
                null, "", "policy_id", "code", "v_policy_postage", null, null);
        addSinglePopupToForm("Contract.discount", "discount_id", "int", false, 50, null, null, false, null, false, null, true, true, true, true, new PopupSingleDiscountAction(localMainUI), 2,
                null, "", "id", "DISCOUNT_CONTRACT", "bm_discount", null, null);

        addTextFieldToForm("Contract.startCost", new TextField(), "start_cost", "long", false, 50, null, null, false, false, null, false, null, false, false, true, true, null);
        addTextFieldToForm("Contract.totalCost", new TextField(), "cost", "long", false, 50, null, null, false, false, null, false, null, false, false, true, true, null);

        addTextFieldToForm("Contract.accout", new TextField(), "AMOUNT_OF_ACCOUNT", "int", true, 10, null, null, false, false, null, false, null, true, true, true, true, null);
        // so luong hang hoa
        // chon hang hoa gan voi chinh sach
        List<List> lstAttachField = new ArrayList();
        List lstRow = addComponentToMultiPopup("Good.amount", "amount", "int", true, 10, "int>0");
        lstAttachField.add(lstRow);
        addMultiPopupToForm("Contract.GoodList", false, false, new PopupMultiContractGoodAction(localMainUI), 2, lstAttachField,
                "bm_contract_policy_good", "POLICY_GOOD_ID", "contract_id", "id", "bm_contract_policy_good_seq", "policy_id", "POLICY_ID", "v_policy_good");

        //phi khoi tao duoc lay tu dich vu
        MultiUploadField fileAttach = new MultiUploadField();
        addMultiUploadFieldToForm("Contract.attachFile", fileAttach, "BM_CONTRACT_ATTACH", "file", false, null, null, null, false, ContractAction.class.toString(), 5, "contract_id", "ATTACH_FILE", "id", "bm_contract_attach_seq");
        //UploadField fileAttach = new UploadField();
        //addUploadFieldToForm("Contract.attachFile", fileAttach, "ATTACH_FILE", "file", false, null, null, null, false, ContractAction.class.toString(), false, 10);
        
        Date toDate = new Date();
        Date fromDate = new Date(toDate.getTime() - 60l * 24l * 3600l * 1000l);
        List lstParam = new ArrayList();
        lstParam.add(fromDate);
        lstParam.add(toDate);
        setComponentAsSysdate("User.CreateDate", "create_date", true, "date:and_mandatory:366", false, lstParam);
        setComponentAsLoginUser("User.CreateUser", "create_user", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null, null, INT_VIEWGROUP_RECURSIVE);

        //AnhPTN: them nut Huy
        Button buttonCancel = new Button(ResourceBundleUtils.getLanguageResource("Button.CancelContract"));
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
        addButton(buttonCancel);

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
                    getComponent("discount_id").setValue(null);
                    if (getCurrentForm() == BaseAction.INT_ADD_FORM) {
                        setContractCode();
                    }
                    if (getCurrentForm() == BaseAction.INT_EDIT_FORM) {
                        if (!isFirstLoadEditForm) {
                            if (isChanged()) {
                                setContractCode();
                            } else {
                                getComponent("code").setValue(oldContractCode);
                            }
                        }
                    }
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
                    getComponent("policy_id").setValue(null);
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }
            }
        };
        ComboBox cboPaidType = (ComboBox) getComponent("paid_type");
        cboPaidType.addValueChangeListener(lsPaidTypeCombo);

        PopupDateField pudSignDate = (PopupDateField) getComponent("sign_date");
        pudSignDate.addBlurListener(new FieldEvents.BlurListener() {
            @Override
            public void blur(FieldEvents.BlurEvent event) {
                try {
                    if (getCurrentForm() == BaseAction.INT_ADD_FORM) {
                        setContractCode();
                    }
                    if (getCurrentForm() == BaseAction.INT_EDIT_FORM) {
                        if (!isFirstLoadEditForm) {
                            if (isChanged()) {
                                setContractCode();
                            } else {
                                getComponent("code").setValue(oldContractCode);
                            }
                        }
                    }
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }
            }
        });

        Property.ValueChangeListener listenerCustomer = new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    ComboBox cboCus = ((ComboBox) getComponent("customer_id"));
                    if (getCurrentForm() != BaseAction.INT_SEARCH_FORM) {
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

    private boolean isChanged() {
        if(getComponent("service_id").getValue() != null)
        if (oldServiceCode.equals(getComponent("service_id").getValue().toString())
                && oldSignDate.equals(getComponent("sign_date").getValue())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean prepareEdit() throws Exception {
        isFirstLoadEditForm = true;
        return true;
    }

    @Override
    public void afterPrepareEdit() throws Exception {
        if (getComponent("code") != null && getComponent("code").getValue() != null) {
            oldContractCode = getComponent("code").getValue().toString();
        }
        if (getComponent("service_id") != null && getComponent("service_id").getValue() != null) {
            oldServiceCode = getComponent("service_id").getValue().toString();
        }
        if (getComponent("sign_date") != null && getComponent("sign_date").getValue() != null) {
            oldSignDate = (Date) getComponent("sign_date").getValue();
        }
        isFirstLoadEditForm = false;
    }

    private void increaseContractSerial(String serial, Date signDate) throws Exception {
        if (serial.equals("000001")) {
            String isrt = "insert into bm_contract_code c (c.id, c.stt, c.department_id, c.create_date)"
                    + " select bm_contract_code_seq.nextval , 1, s.group_id, ? from bm_service s where s.service_id = ?";
            List isrtPara = new ArrayList();
            isrtPara.add(signDate);
            isrtPara.add(getComponent("service_id").getValue());
            C3p0Connector.excuteData(isrt, isrtPara);
        } else {
            // update bm_contract_code
            String upd = "update bm_contract_code c set c.stt =  (c.stt + 1) "
                    + " where c.create_date = ? "
                    + " and c.department_id = (select s.group_id from bm_service s where s.service_id = ?)";
            List udPara = new ArrayList();
            udPara.add(signDate);
            udPara.add(getComponent("service_id").getValue());
            C3p0Connector.excuteData(upd, udPara);
        }
    }

    public void setContractCode() throws Exception {
        String code = "";
        if (getComponent("service_id").getValue() != null && getComponent("sign_date").getValue() != null) {
            // lay group code
            String getGroupNameSQL = "select g.group_code from bm_service s left join sm_group g on s.group_id = g.group_id where s.service_id = ?";
            List serPara = new ArrayList();
            serPara.add(getComponent("service_id").getValue());
            List<Map> lGrCode = C3p0Connector.queryData(getGroupNameSQL, serPara);
            String groupCode = lGrCode.get(0).get("group_code").toString();
            // convert date --> string
            PopupDateField puSignDate = (PopupDateField) getComponent("sign_date");
            Date signDate = puSignDate.getValue();
            DateFormat formatter = new SimpleDateFormat("ddMMyyyy");
            formatter.format(signDate);
            String sDate = formatter.format(signDate);
            // get serial
            String serial = getContractSerialNumber(Long.parseLong(getComponent("service_id").getValue().toString()), signDate);
            // Tao ma HD
            code = serial + groupCode + sDate;
        }
        // set Contract code
        getComponent("code").setValue(code);
    }

    public String getContractSerialNumber(long serID, Date signDate) throws Exception {
        // Kiem tra xem don vi cua Dich vu trong ngay da phat sinh hop dong chua
        String ckContracNumberSQL = "select c.stt from bm_contract_code c left join bm_service s on s.group_id = c.department_id "
                + " where c.create_date = ? "
                + " and s.service_id = ?";
        List serPara = new ArrayList();
        serPara.add(signDate);
        serPara.add(serID);
        List<Map> Lstt = C3p0Connector.queryData(ckContracNumberSQL, serPara);
        if (Lstt.isEmpty()) { // neu chua phat sinh hop dong --> insert them row vao bang theo doi
            return "000001";
        } else {// neu da phat sinh hop dong --> lay so hien tai
            int newStt = Integer.parseInt(Lstt.get(0).get("stt").toString()) + 1;
            int iStt = String.valueOf(newStt).length();
            String code = String.valueOf(newStt);
            for (int i = iStt; i < 6; i++) {
                code = "0" + code;
            }
            return code;
        }
    }

    public boolean validateCancelContract(Object[] selectedArray) throws Exception {
        for (int i = 0; i < selectedArray.length; i++) {
            if (getContractStatus(Long.parseLong(selectedArray[i].toString())) != 1) {
                Notification.show(ResourceBundleUtils.getLanguageResource("Contract.StatusMustBeNew"),
                        null, Notification.Type.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    public void buttonCancelContractClick() throws Exception {
        Object[] deleteArray = ((java.util.Collection) table.getValue()).toArray();// ID cua nhung ban ghi da chon
        if (deleteArray != null && deleteArray.length > 0) {
            if (checkPermission(deleteArray)) {
                if (validateCancelContract(deleteArray)) {
                    ConfirmationDialog.Callback ccbl = new ConfirmationDialog.Callback() {
                        @Override
                        public void onDialogResult(String buttonName) {
                            Connection connection = null;
                            try {
                                if (buttonName.equals(ResourceBundleUtils.getLanguageResource("Common.Yes"))) {
                                    connection = C3p0Connector.getInstance().getConnection();
                                    connection.setAutoCommit(false);
                                    for (int i = 0; i < deleteArray.length; i++) {
                                        // insert Contract History                                    
                                        String isrtConHisSQL = "insert into bm_contract_his h (h.id_his, h.contract_id, h.status_old, h.status_new, "
                                                + " h.change_user_login_id_his , h.change_date_his) "
                                                + " select  bm_contract_his_seq.nextval, con.contract_id,  con.status sttOld, 5 sttNew, "
                                                + " ?, sysdate "
                                                + " from bm_contract con where con.contract_id = ?";
                                        List isrtConHisPara = new ArrayList();
                                        isrtConHisPara.add(Long.parseLong(VaadinUtils.getSessionAttribute("G_UserId").toString()));
                                        isrtConHisPara.add(Long.parseLong(deleteArray[i].toString()));
                                        C3p0Connector.excuteData(isrtConHisSQL, isrtConHisPara, connection);
                                        // cap nhat trang thai contract status = 5: Huy
                                        String udConSttSQL = " update bm_contract con set con.status = 5 where con.contract_id = ? ";
                                        List udConSttPara = new ArrayList();
                                        udConSttPara.add(Long.parseLong(deleteArray[i].toString()));
                                        C3p0Connector.excuteData(udConSttSQL, udConSttPara, connection);
                                    }
                                    connection.commit();
                                    connection.close();

                                    connection = C3p0Connector.getInstance().getConnection();
                                    connection.setAutoCommit(false);
                                    for (int i = 0; i < deleteArray.length; i++) {
                                        // xoa moi account cua hop dong (neu co)
                                        String dlAccSQL = " delete bm_account a where a.contract_id = ? ";
                                        List dlAcctPara = new ArrayList();
                                        dlAcctPara.add(Long.parseLong(deleteArray[i].toString()));
                                        C3p0Connector.excuteData(dlAccSQL, dlAcctPara, connection);
                                    }
                                    connection.commit();
                                    updateDataRefreshData();
                                    Notification.show(ResourceBundleUtils.getLanguageResource("Common.ContractCanceled"),
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
                }
            }
        } else {
            Notification.show(ResourceBundleUtils.getLanguageResource("Common.SelectRequireAtLeast1"),
                    null, Notification.Type.ERROR_MESSAGE);
        }
        return;
    }

    public float getStartCost(long serID, long conID, Connection connection) throws Exception {
        String getSerStartCost = "select NVL( ser.start_cost * con.amount_of_account,0) ServiceStartCost "
                + " FROM bm_contract con "
                + " left join bm_service ser on ser.service_id = con.service_id "
                + " WHERE con.contract_id = ?";
        List getSerStartCostPara = new ArrayList();
        getSerStartCostPara.add(conID);
        List<Map> LSerSC = C3p0Connector.queryData(getSerStartCost, getSerStartCostPara, connection);

        float serStartCost = 0;
        if (LSerSC.get(0).get("servicestartcost") != null) {
            serStartCost = Float.parseFloat(LSerSC.get(0).get("servicestartcost").toString());
        }
        String getSumGood = " SELECT   NVL( SUM (pg.price * cpg.amount) , 0)   good_tot_price "
                + " FROM bm_contract con "
                + " LEFT JOIN bm_policy_good pg ON pg.policy_id = con.policy_id "
                + " LEFT JOIN bm_contract_policy_good cpg ON cpg.contract_id = con.contract_id AND cpg.policy_good_id = pg.id "
                + " WHERE con.contract_id = ?";
        List<Map> LSumGood = C3p0Connector.queryData(getSumGood, getSerStartCostPara, connection);
        float sumGood = 0;
        if (LSumGood.get(0).get("good_tot_price") != null) {
            sumGood = Float.parseFloat(LSumGood.get(0).get("good_tot_price").toString());
        }
        float fStrtCost = serStartCost + sumGood;
        return fStrtCost;
    }

    public float getPostageCost(long conID, Connection connection) throws Exception {
        String ptCostSQL = "select c.amount_of_account * p.postage_price postage_cost "
                + " from bm_contract c left join bm_policy p on c.policy_id = p.policy_id "
                + " where c.contract_id = ?";
        List getptCostPara = new ArrayList();
        getptCostPara.add(conID);
        List<Map> LPtCost = C3p0Connector.queryData(ptCostSQL, getptCostPara, connection);
        float fPtCost = 0;
        if (LPtCost.get(0).get("postage_cost") != null) {
            fPtCost = Float.parseFloat(LPtCost.get(0).get("postage_cost").toString());
        }
        return fPtCost;
    }

    public float getContractDiscount(long disID, Connection connection) throws Exception {
        String gConDiscoutSQL = "select d.discount_contract  from bm_discount d where d.id = ?";
        List getptCostPara = new ArrayList();
        getptCostPara.add(disID);
        List<Map> LConDis = C3p0Connector.queryData(gConDiscoutSQL, getptCostPara, connection);
        float conDiscount = 0;
        if (LConDis != null && !LConDis.isEmpty()) {
            if (LConDis.get(0).get("discount_contract") != null) {
                conDiscount = Float.parseFloat(LConDis.get(0).get("discount_contract").toString());
            }
        }
        return conDiscount;
    }

    public float getContractDiscountFrDB(long conID, Connection connection) throws Exception {
        String sql = "select dis.discount_contract from bm_contract con left join bm_discount dis on con.discount_id = dis.id where con.contract_id = ?";
        List para = new ArrayList();
        para.add(conID);
        List<Map> map = C3p0Connector.queryData(sql, para, connection);
        float dis = 0;
        if (map.get(0).get("discount_contract") != null) {
            dis = Float.parseFloat(map.get(0).get("discount_contract").toString());
        }
        return dis;
    }

    public int getPaidType(long conID, Connection connection) throws Exception {
        String gPaidTypeSQL = "select con.paid_type from bm_contract con where con.contract_id =?";
        List conIDPara = new ArrayList();
        conIDPara.add(conID);
        List<Map> lPaidType = C3p0Connector.queryData(gPaidTypeSQL, conIDPara, connection);
        int iPaitType = 0;
        if (!lPaidType.isEmpty()) {
            iPaitType = Integer.parseInt(lPaidType.get(0).get("paid_type").toString());
        }
        return iPaitType;
    }

    public void updateContractCost(Connection connection, long lContractId) throws Exception {
        // AnhPTN: update tong tien = phi khoi tao dich vụ + gia tri hang hoa
        AbstractField serviceId = getComponent("service_id");
        long lServiceId = Long.parseLong(serviceId.getValue().toString());
        // tinh phi khoi tao
        float fStrtCost = getStartCost(lServiceId, lContractId, connection);
        // tinh tong gia tri hop dong: truong hop tra truoc, truong hop khi co chiet khau hop dong
        AbstractField abfpaidType = getComponent("paid_type");
        long paidType = Long.parseLong(abfpaidType.getValue().toString());
        // tim discount
        long discountID = 0;
        float conDis = 0;
        if (getComponent("discount_id").getValue() != null) {
            discountID = Long.parseLong(getComponent("discount_id").getValue().toString());
            conDis = getContractDiscount(discountID, connection);
        }
        // tinh gia tri khoi tao sau Chiet khau
        fStrtCost = fStrtCost * (100 - conDis) / 100;
        // update gia tri khoi tao
        String sql = "UPDATE bm_contract SET start_cost = ?"
                + " WHERE contract_id = ?";
        List conIDPara = new ArrayList();
        conIDPara.add(fStrtCost);
        conIDPara.add(lContractId);
        C3p0Connector.excuteData(sql, conIDPara, connection);
        float ptCost = 0;
        if (paidType == 1) { // tra truoc
            ptCost = getPostageCost(lContractId, connection);
            // tinh gia tri goi cuoc sau CK
            ptCost = ptCost * (100 - conDis) / 100;
        }
        // lay tong gia dich vu
        float ttCost = fStrtCost + ptCost;
        // update tong gia tri hop dong
        String udTtCostSQL = "UPDATE bm_contract SET cost = ?"
                + " WHERE contract_id = ?";
        List udTtCostPara = new ArrayList();
        udTtCostPara.add(ttCost);
        udTtCostPara.add(lContractId);
        C3p0Connector.excuteData(udTtCostSQL, udTtCostPara, connection);
    }

    @Override
    public void afterAddData(Connection connection, long lContractId) throws Exception {
        // AnhPTN: cap nhat lai tien
        updateContractCost(connection, lContractId);
        // AnhPTN: update status = 1 (tao moi)
        String upSttSql = "update  bm_contract con set con.status = 1 where con.contract_id = ?";
        List udSttPara = new ArrayList();
        udSttPara.add(lContractId);
        C3p0Connector.excuteData(upSttSql, udSttPara, connection);
        // cap nhat serial
        if (!oldContractCode.equals(getComponent("code").getValue())) {
            Date signDate = (Date) getComponent("sign_date").getValue();
            String serial = getComponent("code").getValue().toString().substring(0, 6);
            increaseContractSerial(serial, signDate);
        }
        connection.commit();
        updateDataRefreshData();
    }

    @Override
    public void afterEditData(Connection connection, long lContractId) throws Exception {
        // AnhPTN: cap nhat lai tien
        updateContractCost(connection, lContractId);
        // cap nhat serial
        if (!oldContractCode.equals(getComponent("code").getValue())) {
            Date signDate = (Date) getComponent("sign_date").getValue();
            String serial = getComponent("code").getValue().toString().substring(1, 6);
            increaseContractSerial(serial, signDate);
        }
        connection.commit();
        updateDataRefreshData();
    }

    // anhptn: lay so luong acc dang ky trong hop dong
    public long getAmountOfAccountInContract(long conID) throws Exception {
        String accSQL = "select con.amount_of_account from bm_contract con where con.contract_id = ?";
        List conIDParameter = new ArrayList();
        conIDParameter.add(conID);
        List<Map> LaccCNbr = C3p0Connector.queryData(accSQL, conIDParameter);
        long accountsInContract = Long.parseLong(LaccCNbr.get(0).get("amount_of_account").toString());
        return accountsInContract;
    }

    // AnhPTN: Lay các ngay thuoc  hop dong
    public Date getContractDate(long conID, String dataField, Connection connection) throws Exception {
        // lay ngay ky HD
        Date contractDate = new Date();
        String sql = "select con." + dataField + " from bm_contract con where con.contract_id = ?";
        List conIDParameter = new ArrayList();
        conIDParameter.add(conID);
        List<Map> Ldate = C3p0Connector.queryData(sql, conIDParameter, connection);
        if (!Ldate.isEmpty()) {
            contractDate = (Date) Ldate.get(0).get(dataField);
            return contractDate;
        }
        return null;
    }

    // AnhPTN: lay trang thai hop dong
    public long getContractStatus(long conID) throws Exception {
        Item data = table.getItem(String.valueOf(conID));
        Object comboData = data.getItemProperty(ResourceBundleUtils.getLanguageResource("Contract.status")).getValue();
        long conStt = Long.parseLong(((ComboboxItem) comboData).getValue().toString());
//        String getConSttSQL = "select con.status from bm_contract con where con.contract_id = ? ";
//        List conIDParameter = new ArrayList();
//        conIDParameter.add(conID);
//        List<Map> LConStt = C3p0Connector.queryData(getConSttSQL, conIDParameter);
//        long conStt = Long.parseLong(LConStt.get(0).get("status").toString());
        return conStt;
    }

    public long getContractStatusFrDB(long conID) throws Exception {
        String getConSttSQL = "select con.status from bm_contract con where con.contract_id = ? ";
        List conIDParameter = new ArrayList();
        conIDParameter.add(conID);
        List<Map> LConStt = C3p0Connector.queryData(getConSttSQL, conIDParameter);
        long conStt = 0;
        conStt = Long.parseLong(LConStt.get(0).get("status").toString());
        return conStt;
    }

    // AnhPTN convert status name
    public String getStatusName(long conStt) {
        String sStt = "";
        if (conStt == 2) {
            sStt = "Contract.Acceptance";
        }
        if (conStt == 3) {
            sStt = "Contract.Expired";
        }
        if (conStt == 4) {
            sStt = "Contract.Suspended";
        }
        if (conStt == 5) {
            sStt = "Contract.Canceled";
        }
        if (conStt == 6) {
            sStt = "Contract.Termination";
        }
        return sStt;
    }

    // AnhPTN: check cac date trong hop dong phai hop le
    public boolean isValidContractDate(Date signDate, Date accepDate, Date expireDate) throws Exception {
        if (signDate != null && accepDate != null) {
            if (signDate.after(accepDate)) {
                return false;
            }
            if (expireDate != null) {
                if (signDate.after(expireDate)) {
                    return false;
                }
                if (accepDate.after(expireDate)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean validateAdd() throws Exception {
        // check date
        Date signDate = new Date();
        PopupDateField pdfSignDate = (PopupDateField) getComponent("sign_date");
        signDate = pdfSignDate.getValue();
        Date accepDate = new Date();
        PopupDateField pdfAccepDate = (PopupDateField) getComponent("acceptance_date");
        accepDate = pdfAccepDate.getValue();
        Date expDate = new Date();
        expDate = null;
        PopupDateField pdfExpDate = (PopupDateField) getComponent("expire_date");
        if (pdfExpDate != null) {
            expDate = pdfExpDate.getValue();
        }
        if (!isValidContractDate(signDate, accepDate, expDate)) {
            Notification.show(ResourceBundleUtils.getLanguageResource("Contract.Error.DateInvalid"),
                    null, Notification.Type.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    @Override
    //AnhPTN: hop dong o trang thai stt #1 thi  khong duoc sua
    public boolean validateEdit(long id) throws Exception {
        // check trang thai
        long conStt = getContractStatus(id);
        if (conStt != 1) {
            String sStt = getStatusName(conStt);
            Notification.show(ResourceBundleUtils.getLanguageResource("Contract.Error.StatusInvalid"),
                    null, Notification.Type.ERROR_MESSAGE);
            return false;
        }
        // check ngay
        Date signDate = new Date();
        PopupDateField pdfSignDate = (PopupDateField) getComponent("sign_date");
        signDate = pdfSignDate.getValue();
        Date accepDate = new Date();
        PopupDateField pdfAccepDate = (PopupDateField) getComponent("acceptance_date");
        accepDate = pdfAccepDate.getValue();
        Date expDate = new Date();
        expDate = null;
        PopupDateField pdfExpDate = (PopupDateField) getComponent("expire_date");
        if (pdfExpDate != null) {
            expDate = pdfExpDate.getValue();
        }
        if (!isValidContractDate(signDate, accepDate, expDate)) {
            Notification.show(ResourceBundleUtils.getLanguageResource("Contract.Error.DateInvalid"),
                    null, Notification.Type.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    @Override
    // anhpt nhop dong o trang thai stt #1 thi  khong duoc xoa
    public boolean validateDelete(Object[] selectedArray) throws Exception {
        int i = 0, id = 0; //contract ID
        long conStt = 0;
        for (i = 0; i < selectedArray.length; i++) {
            id = Integer.parseInt(selectedArray[i].toString());
            conStt = getContractStatus(id);
            if (conStt != 1) {
                String sStt = getStatusName(conStt);
                Notification.show(ResourceBundleUtils.getLanguageResource("Contract.Error.StatusInvalid" + " " + sStt),
                        null, Notification.Type.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }
}
