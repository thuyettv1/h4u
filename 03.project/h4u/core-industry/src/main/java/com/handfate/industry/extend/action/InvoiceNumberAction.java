/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.MainUI;
import com.handfate.industry.core.action.BaseAction;
import com.handfate.industry.core.action.PopupSingleAllUserAction;
import com.handfate.industry.core.action.PopupSingleUserAction;
import com.handfate.industry.core.action.component.ComboboxItem;
import com.handfate.industry.core.action.component.ConfirmationDialog;
import com.handfate.industry.core.action.component.PagedTable;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class InvoiceNumberAction extends BaseAction {
    public Table historyTable = new Table();
    public TextField txtReason = new TextField();
    public Button buttonAssign = new Button(ResourceBundleUtils.getLanguageResource("InvoiceNumber.Action.Assign"));
    public Button buttonGetBack = new Button(ResourceBundleUtils.getLanguageResource("InvoiceNumber.Action.GetBack"));
    public Button buttonDeleteInvoice = new Button(ResourceBundleUtils.getLanguageResource("InvoiceNumber.Action.Delete"));
    public Button buttonCancelInvoice = new Button(ResourceBundleUtils.getLanguageResource("InvoiceNumber.Action.Cancel"));
    public Button buttonLostInvoice = new Button(ResourceBundleUtils.getLanguageResource("InvoiceNumber.Action.Lost"));
    
    public HorizontalLayout init(UI localMainUI) throws Exception {
        setTableName("bm_iv_num");
        setIdColumnName("id");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("id");
        setSortAscending(false);
        setSequenceName("bm_iv_num_SEQ");
        setAllowDelete(false);
        setQueryWhereCondition(" and (owner is null or owner in (select user_id from sm_users where group_id in (select group_id from v_group where is_enable = 1 and path like ?))) ");
        List lstParameter = new ArrayList();
        lstParameter.add("%/" + VaadinUtils.getSessionAttribute("G_GroupId").toString() + "/%");
        setQueryWhereParameter(lstParameter);
        
        addTextFieldToForm("id", new TextField(), "id", "int", true, 10, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("InvoiceNumber.InvoiceBox", new TextField(), "iv_box", "string", true, 100, "int>=0", null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Invoice.InvoiceNumber", new TextField(), "iv_num", "string", true, 10, "int>=0", null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("InvoiceNumber.TemplateNumber", new TextField(), "template_number", "string", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("InvoiceNumber.InvoiceSign", new TextField(), "invoice_sign", "string", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("InvoiceNumber.BuyDate", new PopupDateField(), "buy_date", "date", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addSinglePopupToForm("InvoiceNumber.Owner", "owner", "int", false, 50, null, null, true, null, false, null, false, false, true, true, new PopupSingleAllUserAction(localMainUI), 2,
                null, "", "user_id", "user_name", "SM_USERS",null,null);
        Object[][] status =   {{"2", "InvoiceNumber.Status.Used"},
                                    {"3", "InvoiceNumber.Status.Lost"},
                                    {"4", "InvoiceNumber.Status.Delete"},
                                    {"5", "InvoiceNumber.Status.Cancel"},
                                    {"6", "Transaction.payment"}};
        addComboBoxToForm("Menu.Form.Status", new ComboBox(), "status", "int",
                true, 2, null, null, true, false, null, false, null, false, false, true, true, status, "1", "InvoiceNumber.Status.New");

        Date toDate = new Date();
        Date fromDate = new Date(toDate.getTime() - 60l * 24l * 3600l * 1000l);
        List lstParam = new ArrayList();
        lstParam.add(fromDate);
        lstParam.add(toDate);
        setComponentAsSysdate("User.CreateDate", "create_date", true, "date:and_mandatory:366", false, lstParam);
        setComponentAsLoginUser("User.CreateUser", "create_user", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null , null, INT_VIEWGROUP_ALL);
        
        addCustomizeToSearchForm("History.Reason", txtReason, "string", false, 100, null, null, false, false, false, true, true, null);
        
        //Them nut giao hoá đơn
        buttonAssign.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                buttonAssignClick();
            }
        });
        addButton(buttonAssign);
        
        //Them nut thu hồi hoá đơn
        buttonGetBack.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                buttonGetBackClick();
            }
        });
        addButton(buttonGetBack);
        
        //Them nut xóa hoá đơn
        buttonDeleteInvoice.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                buttonDeleteClick();
            }
        });
        addButton(buttonDeleteInvoice);
        
        //Them nút hủy hoá đơn
        buttonCancelInvoice.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                buttonCancelClick();
            }
        });
        addButton(buttonCancelInvoice);
        
        //Them nút cập nhật mất
        buttonLostInvoice.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                buttonLostClick();
            }
        });
        addButton(buttonLostInvoice);
        
        return initPanel(2);
    }
    
    public void buttonAssignClick() {
        try {
            Object[] selectedArray = ((java.util.Collection) table.getValue()).toArray();// ID cua nhung ban ghi da chon
            boolean check = true;
            for (int i = 0; i < selectedArray.length; i++) {
                Item data = table.getItem(selectedArray[i]);
                ComboboxItem cboItem = (ComboboxItem) data.getItemProperty(
                        ResourceBundleUtils.getLanguageResource("InvoiceNumber.Owner")).getValue();
                if (getComponent("owner").getValue() != null && cboItem.getValue() != null && cboItem.getValue().toString().equals(
                        getComponent("owner").getValue().toString())) {
                    Notification.show(ResourceBundleUtils.getLanguageResource("InvoiceNumber.Error.AssignExist"),
                            null, Notification.Type.ERROR_MESSAGE);
                    check = false;
                    break;
                }
                ComboboxItem cboStatus = (ComboboxItem) data.getItemProperty(
                        ResourceBundleUtils.getLanguageResource("Menu.Form.Status")).getValue();
                if (!(cboStatus.getValue() != null && cboStatus.getValue().toString().equals("1"))) {
                    Notification.show(ResourceBundleUtils.getLanguageResource("InvoiceNumber.Error.AssignStatus"),
                            null, Notification.Type.ERROR_MESSAGE);
                    check = false;
                    break;
                }
            }
            if (check) {
                if (selectedArray != null && selectedArray.length > 0) {
                    if (getComponent("owner").getValue() != null) {
                        ConfirmationDialog.Callback ccbl = new ConfirmationDialog.Callback() {
                            @Override
                            public void onDialogResult(String buttonName) {
                                Connection connection = null;
                                try {
                                    if (buttonName.equals(ResourceBundleUtils.getLanguageResource("Common.Yes"))) {
                                        connection = C3p0Connector.getInstance().getConnection();
                                        connection.setAutoCommit(false);
                                        String sql = " insert into bm_iv_num_HIS (id, owner, action, action_date, invoice_number_id, change_user) "
                                                + " values (bm_iv_num_HIS_SEQ.nextval, ?, 1 , sysdate, ?, ? )";
                                        List lstBatch = new ArrayList();
                                        for (int i = 0; i < selectedArray.length; i++) {
                                            List lstParameter = new ArrayList();
                                            List lstRow = new ArrayList();
                                            lstRow.add(getComponent("owner").getValue().toString());
                                            lstRow.add("long");
                                            lstParameter.add(lstRow);
                                            List lstRow1 = new ArrayList();
                                            lstRow1.add(selectedArray[i].toString());
                                            lstRow1.add("long");
                                            lstParameter.add(lstRow1);
                                            List lstRow2 = new ArrayList();
                                            lstRow2.add(VaadinUtils.getSessionAttribute("G_UserId").toString());
                                            lstRow2.add("long");
                                            lstParameter.add(lstRow2);
                                            lstBatch.add(lstParameter);
                                        }
                                        C3p0Connector.excuteDataByTypeBatch(sql, lstBatch, connection);

                                        String sql1 = " update bm_iv_num set owner = ? where id = ? ";
                                        List lstBatch1 = new ArrayList();
                                        for (int i = 0; i < selectedArray.length; i++) {
                                            List lstParameter = new ArrayList();
                                            List lstRow = new ArrayList();
                                            lstRow.add(getComponent("owner").getValue().toString());
                                            lstRow.add("long");
                                            lstParameter.add(lstRow);
                                            List lstRow1 = new ArrayList();
                                            lstRow1.add(selectedArray[i].toString());
                                            lstRow1.add("long");
                                            lstParameter.add(lstRow1);
                                            lstBatch1.add(lstParameter);
                                        }
                                        C3p0Connector.excuteDataByTypeBatch(sql1, lstBatch1, connection);
                                        connection.commit();
                                        Notification.show(ResourceBundleUtils.getLanguageResource("InvoiceNumber.AssignSuccess"),
                                                null, Notification.Type.WARNING_MESSAGE);
                                        updateDataRefreshData();
                                        table.setValue(selectedArray[0]);
                                        long id = Long.parseLong(selectedArray[0].toString());
                                        List<Map> lstHistory = selectHistory(id);
                                        insertToHistoryTable(lstHistory);
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
                    } else {
                        Notification.show(ResourceBundleUtils.getLanguageResource("InvoiceNumber.Error.ChooseOwner"),
                                null, Notification.Type.ERROR_MESSAGE);
                    }
                } else {
                    Notification.show(ResourceBundleUtils.getLanguageResource("Common.SelectRequireAtLeast1"),
                            null, Notification.Type.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            VaadinUtils.handleException(ex);
            MainUI.mainLogger.debug("Install error: ", ex);
        }        
    }
    
    public void buttonGetBackClick() {
        try {
            Object[] selectedArray = ((java.util.Collection) table.getValue()).toArray();// ID cua nhung ban ghi da chon
            boolean check = true;
            for (int i = 0; i < selectedArray.length; i++) {
                Item data = table.getItem(selectedArray[i]);
                ComboboxItem cboItem = (ComboboxItem) data.getItemProperty(
                        ResourceBundleUtils.getLanguageResource("InvoiceNumber.Owner")).getValue();
                if (cboItem.getValue() == null || cboItem.getValue().isEmpty()) {
                    Notification.show(ResourceBundleUtils.getLanguageResource("InvoiceNumber.Error.GetBackExist"),
                            null, Notification.Type.ERROR_MESSAGE);
                    check = false;
                    break;
                }
            }
            if (check) {
                if (selectedArray != null && selectedArray.length > 0) {
                    ConfirmationDialog.Callback ccbl = new ConfirmationDialog.Callback() {
                        @Override
                        public void onDialogResult(String buttonName) {
                            Connection connection = null;
                            try {
                                if (buttonName.equals(ResourceBundleUtils.getLanguageResource("Common.Yes"))) {
                                    connection = C3p0Connector.getInstance().getConnection();
                                    connection.setAutoCommit(false);
                                    String sql = " insert into bm_iv_num_HIS (id, owner, action, action_date, invoice_number_id, change_user) "
                                            + " values (bm_iv_num_HIS_SEQ.nextval, ?, 6 , sysdate, ?, ? )";
                                    List lstBatch = new ArrayList();
                                    for (int i = 0; i < selectedArray.length; i++) {
                                        List lstParameter = new ArrayList();
                                        List lstRow = new ArrayList();
                                        Item data = table.getItem(selectedArray[i]);
                                        ComboboxItem cboItem = (ComboboxItem) data.getItemProperty(
                                                ResourceBundleUtils.getLanguageResource("InvoiceNumber.Owner")).getValue();
                                        lstRow.add(cboItem.getValue().toString());
                                        lstRow.add("long");
                                        lstParameter.add(lstRow);
                                        List lstRow1 = new ArrayList();
                                        lstRow1.add(selectedArray[i].toString());
                                        lstRow1.add("long");
                                        lstParameter.add(lstRow1);
                                        List lstRow2 = new ArrayList();
                                        lstRow2.add(VaadinUtils.getSessionAttribute("G_UserId").toString());
                                        lstRow2.add("long");
                                        lstParameter.add(lstRow2);
                                        lstBatch.add(lstParameter);
                                    }
                                    C3p0Connector.excuteDataByTypeBatch(sql, lstBatch, connection);

                                    String sql1 = " update bm_iv_num set owner = null where id = ? ";
                                    List lstBatch1 = new ArrayList();
                                    for (int i = 0; i < selectedArray.length; i++) {
                                        List lstParameter = new ArrayList();
                                        List lstRow1 = new ArrayList();
                                        lstRow1.add(selectedArray[i].toString());
                                        lstRow1.add("long");
                                        lstParameter.add(lstRow1);
                                        lstBatch1.add(lstParameter);
                                    }
                                    C3p0Connector.excuteDataByTypeBatch(sql1, lstBatch1, connection);
                                    connection.commit();
                                    Notification.show(ResourceBundleUtils.getLanguageResource("InvoiceNumber.GetBackSuccess"),
                                            null, Notification.Type.WARNING_MESSAGE);
                                    updateDataRefreshData();
                                    table.setValue(selectedArray[0]);
                                    long id = Long.parseLong(selectedArray[0].toString());
                                    List<Map> lstHistory = selectHistory(id);
                                    insertToHistoryTable(lstHistory);
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
                } else {
                    Notification.show(ResourceBundleUtils.getLanguageResource("Common.SelectRequireAtLeast1"),
                            null, Notification.Type.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            VaadinUtils.handleException(ex);
            MainUI.mainLogger.debug("Install error: ", ex);
        }        
    }
    
    public void buttonDeleteClick() {
        try {
            Object[] selectedArray = ((java.util.Collection) table.getValue()).toArray();// ID cua nhung ban ghi da chon
            boolean check = true;
            for (int i = 0; i < selectedArray.length; i++) {
                Item data = table.getItem(selectedArray[i]);
                ComboboxItem cboStatus = (ComboboxItem) data.getItemProperty(
                        ResourceBundleUtils.getLanguageResource("Menu.Form.Status")).getValue();
                if (!(cboStatus.getValue() != null && cboStatus.getValue().toString().equals("1"))) {
                    Notification.show(ResourceBundleUtils.getLanguageResource("InvoiceNumber.Error.DeleteStatus"),
                            null, Notification.Type.ERROR_MESSAGE);
                    check = false;
                    break;
                }
            }
            if(txtReason.getValue() == null || txtReason.getValue().trim().isEmpty()) {
                Notification.show(ResourceBundleUtils.getLanguageResource("InvoiceNumber.Error.ReasonRequire"),
                        null, Notification.Type.ERROR_MESSAGE);
                check = false;
            }
            if (check) {
                if (selectedArray != null && selectedArray.length > 0) {
                    ConfirmationDialog.Callback ccbl = new ConfirmationDialog.Callback() {
                        @Override
                        public void onDialogResult(String buttonName) {
                            Connection connection = null;
                            try {
                                if (buttonName.equals(ResourceBundleUtils.getLanguageResource("Common.Yes"))) {
                                    connection = C3p0Connector.getInstance().getConnection();
                                    connection.setAutoCommit(false);
                                    String sql = " insert into bm_iv_num_HIS (id, owner, action, action_date, invoice_number_id, change_user, reason) "
                                            + " values (bm_iv_num_HIS_SEQ.nextval, ?, 4 , sysdate, ?, ?, ? )";
                                    List lstBatch = new ArrayList();
                                    for (int i = 0; i < selectedArray.length; i++) {
                                        List lstParameter = new ArrayList();
                                        List lstRow = new ArrayList();
                                        Item data = table.getItem(selectedArray[i]);
                                        ComboboxItem cboItem = (ComboboxItem) data.getItemProperty(
                                                ResourceBundleUtils.getLanguageResource("InvoiceNumber.Owner")).getValue();                                        
                                        lstRow.add(cboItem.getValue());
                                        lstRow.add("long");
                                        lstParameter.add(lstRow);
                                        List lstRow1 = new ArrayList();
                                        lstRow1.add(selectedArray[i].toString());
                                        lstRow1.add("long");
                                        lstParameter.add(lstRow1);
                                        List lstRow2 = new ArrayList();
                                        lstRow2.add(VaadinUtils.getSessionAttribute("G_UserId").toString());
                                        lstRow2.add("long");
                                        lstParameter.add(lstRow2);
                                        List lstRow3 = new ArrayList();
                                        lstRow3.add(txtReason.getValue());
                                        lstRow3.add("string");
                                        lstParameter.add(lstRow3);  
                                        lstBatch.add(lstParameter);
                                    }
                                    C3p0Connector.excuteDataByTypeBatch(sql, lstBatch, connection);
                                    
                                    String sql1 = " update bm_iv_num set status = 4 where id = ? ";
                                    List lstBatch1 = new ArrayList();
                                    for (int i = 0; i < selectedArray.length; i++) {
                                        List lstParameter = new ArrayList();
                                        List lstRow1 = new ArrayList();
                                        lstRow1.add(selectedArray[i].toString());
                                        lstRow1.add("long");
                                        lstParameter.add(lstRow1);
                                        lstBatch1.add(lstParameter);
                                    }
                                    C3p0Connector.excuteDataByTypeBatch(sql1, lstBatch1, connection);
                                    connection.commit();
                                    
                                    Notification.show(ResourceBundleUtils.getLanguageResource("InvoiceNumber.DeleteSuccess"),
                                            null, Notification.Type.WARNING_MESSAGE);
                                    updateDataRefreshData();
                                    table.setValue(selectedArray[0]);
                                    long id = Long.parseLong(selectedArray[0].toString());
                                    List<Map> lstHistory = selectHistory(id);
                                    insertToHistoryTable(lstHistory);
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
                } else {
                    Notification.show(ResourceBundleUtils.getLanguageResource("Common.SelectRequireAtLeast1"),
                            null, Notification.Type.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            VaadinUtils.handleException(ex);
            MainUI.mainLogger.debug("Install error: ", ex);
        }        
    }
    
    public void buttonCancelClick() {
        try {
            Object[] selectedArray = ((java.util.Collection) table.getValue()).toArray();// ID cua nhung ban ghi da chon
            if (selectedArray != null && selectedArray.length > 0) {
                boolean check = true;
                for (int i = 0; i < selectedArray.length; i++) {
                    Item data = table.getItem(selectedArray[i]);
                    ComboboxItem cboStatus = (ComboboxItem) data.getItemProperty(
                            ResourceBundleUtils.getLanguageResource("Menu.Form.Status")).getValue();
                    if (!(cboStatus.getValue() != null && cboStatus.getValue().toString().equals("2"))) {
                        Notification.show(ResourceBundleUtils.getLanguageResource("InvoiceNumber.Error.CancelStatus"),
                                null, Notification.Type.ERROR_MESSAGE);
                        check = false;
                        break;
                    }
                }
                if(txtReason.getValue() == null || txtReason.getValue().trim().isEmpty()) {
                    Notification.show(ResourceBundleUtils.getLanguageResource("InvoiceNumber.Error.ReasonRequire"),
                            null, Notification.Type.ERROR_MESSAGE);
                    check = false;
                }
                if (check) {
                    ConfirmationDialog.Callback ccbl = new ConfirmationDialog.Callback() {
                        @Override
                        public void onDialogResult(String buttonName) {
                            Connection connection = null;
                            try {
                                if (buttonName.equals(ResourceBundleUtils.getLanguageResource("Common.Yes"))) {
                                    connection = C3p0Connector.getInstance().getConnection();
                                    connection.setAutoCommit(false);
                                    String sql = " insert into bm_iv_num_HIS (id, owner, action, action_date, invoice_number_id, change_user, reason) "
                                            + " values (bm_iv_num_HIS_SEQ.nextval, ?, 5 , sysdate, ?, ?, ? )";
                                    List lstBatch = new ArrayList();
                                    for (int i = 0; i < selectedArray.length; i++) {
                                        List lstParameter = new ArrayList();
                                        List lstRow = new ArrayList();
                                        Item data = table.getItem(selectedArray[i]);
                                        ComboboxItem cboItem = (ComboboxItem) data.getItemProperty(
                                                ResourceBundleUtils.getLanguageResource("InvoiceNumber.Owner")).getValue();                                        
                                        lstRow.add(cboItem.getValue());
                                        lstRow.add("long");
                                        lstParameter.add(lstRow);
                                        List lstRow1 = new ArrayList();
                                        lstRow1.add(selectedArray[i].toString());
                                        lstRow1.add("long");
                                        lstParameter.add(lstRow1);
                                        List lstRow2 = new ArrayList();
                                        lstRow2.add(VaadinUtils.getSessionAttribute("G_UserId").toString());
                                        lstRow2.add("long");
                                        lstParameter.add(lstRow2);
                                        List lstRow3 = new ArrayList();
                                        lstRow3.add(txtReason.getValue());
                                        lstRow3.add("string");
                                        lstParameter.add(lstRow3);
                                        lstBatch.add(lstParameter);
                                    }
                                    C3p0Connector.excuteDataByTypeBatch(sql, lstBatch, connection);
                                    
                                    String sql1 = " update bm_iv_num set status = 5 where id = ? ";
                                    List lstBatch1 = new ArrayList();
                                    for (int i = 0; i < selectedArray.length; i++) {
                                        List lstParameter = new ArrayList();
                                        List lstRow1 = new ArrayList();
                                        lstRow1.add(selectedArray[i].toString());
                                        lstRow1.add("long");
                                        lstParameter.add(lstRow1);
                                        lstBatch1.add(lstParameter);
                                    }
                                    C3p0Connector.excuteDataByTypeBatch(sql1, lstBatch1, connection);
                                    
                                    connection.commit();
                                    Notification.show(ResourceBundleUtils.getLanguageResource("InvoiceNumber.CancelSuccess"),
                                            null, Notification.Type.WARNING_MESSAGE);
                                    updateDataRefreshData();
                                    table.setValue(selectedArray[0]);
                                    long id = Long.parseLong(selectedArray[0].toString());
                                    List<Map> lstHistory = selectHistory(id);
                                    insertToHistoryTable(lstHistory);
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
        } catch (Exception ex) {
            VaadinUtils.handleException(ex);
            MainUI.mainLogger.debug("Install error: ", ex);
        }        
    }
    
    public void buttonLostClick() {
        try {
            Object[] selectedArray = ((java.util.Collection) table.getValue()).toArray();// ID cua nhung ban ghi da chon
            boolean check = true;
            if(txtReason.getValue() == null || txtReason.getValue().trim().isEmpty()) {
                Notification.show(ResourceBundleUtils.getLanguageResource("InvoiceNumber.Error.ReasonRequire"),
                        null, Notification.Type.ERROR_MESSAGE);
                check = false;
            }
            if (check) {
                if (selectedArray != null && selectedArray.length > 0) {
                    ConfirmationDialog.Callback ccbl = new ConfirmationDialog.Callback() {
                        @Override
                        public void onDialogResult(String buttonName) {
                            Connection connection = null;
                            try {
                                if (buttonName.equals(ResourceBundleUtils.getLanguageResource("Common.Yes"))) {
                                    connection = C3p0Connector.getInstance().getConnection();
                                    connection.setAutoCommit(false);
                                    String sql = " insert into bm_iv_num_HIS (id, owner, action, action_date, invoice_number_id, change_user, reason) "
                                            + " values (bm_iv_num_HIS_SEQ.nextval, ?, 3 , sysdate, ?, ?, ? )";
                                    List lstBatch = new ArrayList();
                                    for (int i = 0; i < selectedArray.length; i++) {
                                        List lstParameter = new ArrayList();
                                        List lstRow = new ArrayList();
                                        Item data = table.getItem(selectedArray[i]);
                                        ComboboxItem cboItem = (ComboboxItem) data.getItemProperty(
                                                ResourceBundleUtils.getLanguageResource("InvoiceNumber.Owner")).getValue();
                                        lstRow.add(cboItem.getValue());
                                        lstRow.add("long");
                                        lstParameter.add(lstRow);
                                        List lstRow1 = new ArrayList();
                                        lstRow1.add(selectedArray[i].toString());
                                        lstRow1.add("long");
                                        lstParameter.add(lstRow1);
                                        List lstRow2 = new ArrayList();
                                        lstRow2.add(VaadinUtils.getSessionAttribute("G_UserId").toString());
                                        lstRow2.add("long");
                                        lstParameter.add(lstRow2);
                                        List lstRow3 = new ArrayList();
                                        lstRow3.add(txtReason.getValue());
                                        lstRow3.add("string");
                                        lstParameter.add(lstRow3);
                                        lstBatch.add(lstParameter);
                                    }
                                    C3p0Connector.excuteDataByTypeBatch(sql, lstBatch, connection);
                                    
                                    String sql1 = " update bm_iv_num set status = 3 where id = ? ";
                                    List lstBatch1 = new ArrayList();
                                    for (int i = 0; i < selectedArray.length; i++) {
                                        List lstParameter = new ArrayList();
                                        List lstRow1 = new ArrayList();
                                        lstRow1.add(selectedArray[i].toString());
                                        lstRow1.add("long");
                                        lstParameter.add(lstRow1);
                                        lstBatch1.add(lstParameter);
                                    }
                                    C3p0Connector.excuteDataByTypeBatch(sql1, lstBatch1, connection);
                                    
                                    connection.commit();
                                    Notification.show(ResourceBundleUtils.getLanguageResource("InvoiceNumber.LostSuccess"),
                                            null, Notification.Type.WARNING_MESSAGE);
                                    updateDataRefreshData();
                                    table.setValue(selectedArray[0]);
                                    long id = Long.parseLong(selectedArray[0].toString());
                                    List<Map> lstHistory = selectHistory(id);
                                    insertToHistoryTable(lstHistory);
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
                } else {
                    Notification.show(ResourceBundleUtils.getLanguageResource("Common.SelectRequireAtLeast1"),
                            null, Notification.Type.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            VaadinUtils.handleException(ex);
            MainUI.mainLogger.debug("Install error: ", ex);
        }        
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
        String accSQL = " select id, ACTION_DATE, ACTION, (select user_name from sm_users "
                + " where user_id = OWNER) owner, "
                + " reason, (select user_name from sm_users "
                + " where user_id = change_user) change_user "
                + " from bm_iv_num_his where INVOICE_NUMBER_ID = ? order by ACTION_DATE desc ";
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
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("InvoiceNumber.Action"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("InvoiceNumber.Owner"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("History.Reason"), String.class, null);
        temp.addContainerProperty(ResourceBundleUtils.getLanguageResource("changeService.changeUser"), String.class, null);

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
        for (int i = 0; i < arrayIds.length; i++) {
            historyTable.removeItem(arrayIds[i]);
        }
        for (int i = 0; i < lstHistory.size(); i++) {
            Object[] data = new Object[5];
            Map row = lstHistory.get(i);
            if (row.get("action_date") != null) {
                data[0] = row.get("action_date").toString();
            }
            if (row.get("action") != null) {
                if(row.get("action").toString().equals("1")) data[1] = ResourceBundleUtils.getLanguageResource("InvoiceNumber.Action.Assign");
                if(row.get("action").toString().equals("2")) data[1] = ResourceBundleUtils.getLanguageResource("InvoiceNumber.Action.Used");
                if(row.get("action").toString().equals("3")) data[1] = ResourceBundleUtils.getLanguageResource("InvoiceNumber.Action.Lost");
                if(row.get("action").toString().equals("4")) data[1] = ResourceBundleUtils.getLanguageResource("InvoiceNumber.Action.Delete");
                if(row.get("action").toString().equals("5")) data[1] = ResourceBundleUtils.getLanguageResource("InvoiceNumber.Action.Cancel");
                if(row.get("action").toString().equals("6")) data[1] = ResourceBundleUtils.getLanguageResource("InvoiceNumber.Action.GetBack");
                if(row.get("action").toString().equals("7")) data[1] = ResourceBundleUtils.getLanguageResource("Button.PayDebt");
            }
            if (row.get("owner") != null) {
                data[2] = row.get("owner").toString();
            }
            if (row.get("reason") != null) {
                data[3] = row.get("reason").toString();
            }
            if (row.get("change_user") != null) {
                data[4] = row.get("change_user").toString();
            }
            historyTable.addItem(data, row.get("id").toString());
        }
    }    
}
