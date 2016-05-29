/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.MainUI;
import com.handfate.industry.core.action.BaseAction;
import static com.handfate.industry.core.action.BaseAction.INT_VIEWGROUP_RECURSIVE;
import com.handfate.industry.core.action.PopupSingleGroupAction;
import com.handfate.industry.core.action.PopupSingleUserAction;
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
public class ServicesAction extends BaseAction {

    /**
     * HÃ m khá»Ÿi táº¡o giao diá»‡n
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI VÃ¹ng giao diá»‡n cá»§a chá»©c nÄƒng
     * @return Giao diá»‡n sau khi khá»Ÿi táº¡o
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khá»Ÿi táº¡o tham sá»‘
        setTableName("bm_service");
        setIdColumnName("service_id");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("service_id");
        setSortAscending(false);
        setSequenceName("bm_service_seq");
        setRootId("0");

        buildTreeSearch("User.GroupId",
                "select group_id, group_name, parent_id from v_group where is_enable = 1", null,
                "group_id", "group_name", "parent_id", "0", true, "group_id");

        //ThÃªm cÃ¡c thÃ nh pháº§n
        addTextFieldToForm("serviceID", new TextField(), "service_id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Service.code", new TextField(), "code", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Service.name", new TextField(), "name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);

//        Object[][] serviceStatus = {{2, "Service.statusTest"}};
//        addComboBoxToForm("Service.status", new ComboBox(), "status", "int",
//                false, 50, null, null, true, false, null, false, null, true, true, true, true, serviceStatus, "1", "Service.statusDeploy");
        Object[][] activeStatus = {{2, "Service.inActive"}};
        addComboBoxToForm("Service.activeStatus", new ComboBox(), "active_status", "int",
                true, 50, null, null, true, false, null, false, null, true, true, true, true, activeStatus, "1", "Service.active");

        addTextFieldToForm("Service.startCost", new TextField(), "START_COST", "int", true, 20, "int>0", null, false, false, null, false, null, true, true, true, true, null);
        addSinglePopupToForm("Service.GroupId", "group_id", "int", true, 50, null, null, true, null, false, null, true, true, true, true, new PopupSingleGroupAction(localMainUI), 2,
                null, "", "group_id", "group_name", "sm_group", null, null);

        addTextFieldToForm("Service.note", new TextArea(), "note", "string", false, 500, null, null, false, false, null, false, null, true, true, true, true, null);
        setComponentAsSysdate("User.CreateDate", "created_date", false, null, false, null);
        setComponentAsLoginUser("User.CreateUser", "create_user", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null, null, INT_VIEWGROUP_RECURSIVE);

        //AnhPTN: Thay doi hieu luc goi cuoc
        Button buttonActiveStatus = new Button(ResourceBundleUtils.getLanguageResource("Button.ActiveStatus"));
        buttonActiveStatus.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    buttonActiveStatusClick();
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        addButton(buttonActiveStatus);
        return initPanel(2);
    }

    private void buttonActiveStatusClick() throws Exception {
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
                                // chuyen trang thai thanh nguoc lai
                                String sql = " update bm_service b "
                                        + " set b.active_status = (select 3 - a.active_status  from bm_service a where a.service_id = b.service_id) "
                                        + " where b.service_id = ?";
                                for (int i = 0; i < deleteArray.length; i++) {
                                    List sqlPara = new ArrayList();
                                    sqlPara.add(deleteArray[i]);
                                    C3p0Connector.excuteData(sql, sqlPara, connection);
                                }
                                connection.commit();
                                updateDataRefreshData();
                                Notification.show(ResourceBundleUtils.getLanguageResource("Button.ActiveStatus") + " " + ResourceBundleUtils.getLanguageResource("success"),
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
        } else {
            Notification.show(ResourceBundleUtils.getLanguageResource("Common.SelectRequireAtLeast1"),
                    null, Notification.Type.ERROR_MESSAGE);
        }
        return;
    }

    @Override
    public boolean validateEdit(long serID) throws Exception {
        // Dang su dung cho chinh sach ==> khong duoc sua
        String serIsUsedSQL = "select * from bm_policy where service_id = ?";
        List serIsUsedPara = new ArrayList();
        serIsUsedPara.add(serID);
        List<Map> poList = C3p0Connector.queryData(serIsUsedSQL, serIsUsedPara);
        if (!poList.isEmpty()) {
            Notification.show(ResourceBundleUtils.getLanguageResource("Service.Error.IsUsed"),
                    null, Notification.Type.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    @Override
    public boolean validateDelete(Object[] selectedArray) throws Exception {
        int i = 0, idDelete = 0; 
        String sqlToCheck = "select service_id from bm_policy a where a.service_id = ?";
        for (i = 0; i < selectedArray.length; i++) {
            idDelete = Integer.parseInt(selectedArray[i].toString());
            List para = new ArrayList();
            para.add(idDelete);
            List<Map> lstRecord = C3p0Connector.queryData(sqlToCheck, para);
            if (lstRecord.get(0).get("service_id") != null) {
                Notification.show(ResourceBundleUtils.getLanguageResource("service.notDelete.fk.policy"),
                        null, Notification.Type.ERROR_MESSAGE);
                return false;
            }
        }
        sqlToCheck = "select service_id from bm_contract a where a.service_id = ?";
        for (i = 0; i < selectedArray.length; i++) {
            idDelete = Integer.parseInt(selectedArray[i].toString());
            List para = new ArrayList();
            para.add(idDelete);
            List<Map> lstRecord = C3p0Connector.queryData(sqlToCheck, para);
            if (lstRecord.get(0).get("service_id") != null) {
                Notification.show(ResourceBundleUtils.getLanguageResource("service.notDelete.fk.contract"),
                        null, Notification.Type.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }
}
