/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.MainUI;
import com.handfate.industry.core.action.BaseAction;
import static com.handfate.industry.core.action.BaseAction.INT_LABEL;
import static com.handfate.industry.core.action.BaseAction.INT_VIEWGROUP_RECURSIVE;
import com.handfate.industry.core.action.PopupSingleUserAction;
import com.handfate.industry.core.action.component.ComboboxItem;
import com.handfate.industry.core.action.component.ConfirmationDialog;
import com.handfate.industry.core.action.component.PagedTable;
import com.handfate.industry.core.action.component.UploadField;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
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
public class InvoiceNumberImportAction extends BaseAction {    
    
    private TextField txtFrom = new TextField();
    private TextField txtTo = new TextField();
    
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
        setAllowEdit(false);
        setQueryWhereCondition(" and (owner is null or owner in (select user_id from sm_users where group_id in (select group_id from v_group where is_enable = 1 and path like ?))) ");
        List lstParameter = new ArrayList();
        lstParameter.add("/%" + VaadinUtils.getSessionAttribute("G_GroupId").toString() + "%/");
        setQueryWhereParameter(lstParameter);
        
        addTextFieldToForm("id", new TextField(), "id", "int", true, 10, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("InvoiceNumber.InvoiceBox", new TextField(), "iv_box", "string", true, 100, "int>=0", null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Invoice.InvoiceNumber", new TextField(), "iv_num", "string", false, 10, "int>=0", null, true, false, null, false, null, false, false, true, true, null);
        addTextFieldToForm("InvoiceNumber.TemplateNumber", new TextField(), "template_number", "string", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("InvoiceNumber.InvoiceSign", new TextField(), "invoice_sign", "string", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("InvoiceNumber.BuyDate", new PopupDateField(), "buy_date", "date", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addSinglePopupToForm("InvoiceNumber.Owner", "owner", "int", false, 50, null, null, true, null, false, null, false, false, true, true, new PopupSingleUserAction(localMainUI), 2,
                null, "", "user_id", "user_name", "SM_USERS",null,null);
        Object[][] status =   {{"2", "InvoiceNumber.Status.Used"},
                                    {"3", "InvoiceNumber.Status.Lost"},
                                    {"4", "InvoiceNumber.Status.Delete"},
                                    {"5", "InvoiceNumber.Status.Cancel"},
                                    {"6", "Transaction.payment"}};
        addComboBoxToForm("Menu.Form.Status", new ComboBox(), "status", "int",
                false, 2, null, null, true, false, null, false, null, false, false, true, true, status, "1", "InvoiceNumber.Status.New");
        
        Date toDate = new Date();
        Date fromDate = new Date(toDate.getTime() - 60l * 24l * 3600l * 1000l);
        List lstParam = new ArrayList();
        lstParam.add(fromDate);
        lstParam.add(toDate);
        setComponentAsSysdate("User.CreateDate", "create_date", true, "date:and_mandatory:366", false, lstParam);
        setComponentAsLoginUser("User.CreateUser", "create_user", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null , null, INT_VIEWGROUP_ALL);
        
        addCustomizeComponentToForm("InvoiceNumber.from", txtFrom, "string", true, 100, "int>0", null, false, true, true, true, true, null);
        addCustomizeComponentToForm("InvoiceNumber.to", txtTo, "string", true, 100, "int>0", null, false, true, true, true, true, null);
        
        return initPanel(2);
    }
    
    @Override
    public void afterPrepareAdd() {
        for(Object listener : buttonUpdate.getListeners(ClickEvent.class)){
            buttonUpdate.removeListener(ClickEvent.class, listener);
        }
        buttonUpdate.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    buttonImportClick();
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
    }
    
    public void buttonImportClick() throws Exception {
        if (validateInput("add")) {// Kiểm tra dữ liệu đầu vào
            int from = Integer.parseInt(txtFrom.getValue());
            int to = Integer.parseInt(txtTo.getValue());
            if (from > to) {
                Notification.show(ResourceBundleUtils.getLanguageResource("InvoiceNumber.Error.FromTo"),
                        null, Notification.Type.ERROR_MESSAGE);
            } else {            
                ConfirmationDialog.Callback ccbl = new ConfirmationDialog.Callback() {
                    @Override
                    public void onDialogResult(String buttonName) {
                        Connection connection = null;                    
                        try {
                            connection = C3p0Connector.getInstance().getConnection();
                            connection.setAutoCommit(false);                        
                            if (buttonName.equals(ResourceBundleUtils.getLanguageResource("Common.Yes"))) {
                                String cql = " insert into bm_iv_num (id, iv_box, iv_num, template_number, "
                                        + " invoice_sign, buy_date, status, create_date, create_user) "
                                        + " values (bm_iv_num_seq.nextval, ?, ?, ?, ?, ?, 1, sysdate, ?) ";
                                List<List> lstBatch = new ArrayList();
                                int txtLength = txtFrom.getValue().length();
                                if(txtTo.getValue().length() > txtFrom.getValue().length()) {
                                    txtLength = txtTo.getValue().length();
                                }
                                for (int n = from; n <= to; n++) {                                
                                    String temp = "" + n;
                                    String ivNum = "";
                                    for(int j = temp.length(); j < txtLength; j++) {
                                        temp = "0" + temp;
                                    }
                                    ivNum = temp;
                                    List lstParameter = new ArrayList();
                                    lstParameter.add(((AbstractField)getComponent("iv_box")).getValue());
                                    lstParameter.add(ivNum);
                                    lstParameter.add(((AbstractField)getComponent("template_number")).getValue());
                                    lstParameter.add(((AbstractField)getComponent("invoice_sign")).getValue());
                                    lstParameter.add(((AbstractField)getComponent("buy_date")).getValue());
                                    lstParameter.add(Long.parseLong(VaadinUtils.getSessionAttribute("G_UserId").toString()));
                                    lstBatch.add(lstParameter);
                                }
                                C3p0Connector.excuteDataBatch(cql, lstBatch, connection);  
                                connection.commit();
                                updateForm();
                                updateDataRefreshData();
                                clearForm();
                                Notification.show(ResourceBundleUtils.getLanguageResource("Common.AddSuccess"),
                                        null, Notification.Type.WARNING_MESSAGE);
                            }
                        } catch (Exception ex) {
                            if (connection != null) {
                                try {
                                    connection.rollback();
                                } catch (SQLException e) {
                                    VaadinUtils.handleException(e);
                                    MainUI.mainLogger.debug("Install error: ", e);
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
                                connection = null;
                            }
                        }
                    }
                };
                mainUI.addWindow(new ConfirmationDialog(
                        ResourceBundleUtils.getLanguageResource("Common.Confirm"),
                        ResourceBundleUtils.getLanguageResource("Common.ConfirmExecute"), ccbl));
            }
        }         
    }    
}
