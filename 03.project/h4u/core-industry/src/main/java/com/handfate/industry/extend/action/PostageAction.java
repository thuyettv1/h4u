/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.BaseAction;
import com.handfate.industry.core.action.PopupSingleUserAction;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class PostageAction extends BaseAction {

    /*YenNTH8
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {

        setTableName("bm_postage");
        setIdColumnName("postage_id");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("postage_id");
        setSortAscending(false);
        setSequenceName("bm_postage_seq");
        addTextFieldToForm("postageID", new TextField(), "postage_id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Postage.code", new TextField(), "code", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Postage.name", new TextField(), "name", "string", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addComboBoxToForm("Policy.ServiceID", new ComboBox(), "service_id", "int",
                true, 50, null, null, true, false, null, false, null, true, true, true, true, "select service_id, code from bm_service", null, "bm_service",
                "service_id", "int", "code", null, null, false, false, null, null);
        addTextFieldToForm("Postage.period", new TextField(), "period", "int", true, 10, "int>0", null, false, false, null, false, null, true, true, true, true, null);
        Object[][] postageType = {{2, "Postage.Postpaid"}};
        addComboBoxToForm("Postage.type", new ComboBox(), "type", "int",
                true, 50, null, null, true, false, null, false, null, true, true, true, true, postageType, "1", "Postage.prepay");

        addTextFieldToForm("Postage.note", new TextArea(), "note", "string", false, 150, null, null, false, false, null, false, null, true, true, true, true, null);
        
        Date toDate = new Date();
        Date fromDate = new Date(toDate.getTime() - 60l * 24l * 3600l * 1000l );
        List lstParam = new ArrayList();
        lstParam.add(fromDate);
        lstParam.add(toDate);
        setComponentAsSysdate("User.CreateDate", "create_date", true, "date:and_mandatory:366", false, lstParam);
        setComponentAsLoginUser("User.CreateUser", "create_user", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null, null, INT_VIEWGROUP_ALL);
        return initPanel(2);
    }

    @Override
    public boolean validateEdit(long gID) throws Exception {
        // Goi cuoc da duoc gan cho chinh sach roi thi khong duoc sua
        String sql = "select a.postage_id from bm_policy a where a.postage_id = ? ";
        List gIDPara = new ArrayList();
        gIDPara.add(gID);
        List<Map> LgID = C3p0Connector.queryData(sql, gIDPara);
        if (!LgID.isEmpty()) {
            Notification.show(ResourceBundleUtils.getLanguageResource("Postage.Error.IsUsed"),
                    null, Notification.Type.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public int getPeriod(long policyID) throws Exception {
        String getPeriodSQL = "select pos.period from bm_policy po left join bm_postage pos on po.postage_id = pos.postage_id "
                + "where po.policy_id = ?";
        int period = 0;
        List policyIDPara = new ArrayList();
        policyIDPara.add(policyID);
        List<Map> LPeriod = C3p0Connector.queryData(getPeriodSQL, policyIDPara);
        if (!LPeriod.isEmpty()) {
            period = Integer.parseInt(LPeriod.get(0).get("period").toString());
        }
        return period;
    }

    @Override
    public boolean validateDelete(Object[] selectedArray) throws Exception {
        int i = 0, idDelete = 0;
        String sqlToCheck = "select postage_id from bm_policy a where a.postage_id = ?";
        for (i = 0; i < selectedArray.length; i++) {
            idDelete = Integer.parseInt(selectedArray[i].toString());
            List para = new ArrayList();
            para.add(idDelete);
            List<Map> lstRecord = C3p0Connector.queryData(sqlToCheck, para);
            if (lstRecord.get(0).get("postage_id") != null) {
                Notification.show(ResourceBundleUtils.getLanguageResource("postage.notDelete.fk.policy"),
                        null, Notification.Type.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }
}
