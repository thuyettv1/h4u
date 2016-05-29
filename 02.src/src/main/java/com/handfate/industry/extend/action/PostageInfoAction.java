/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.BaseAction;
import static com.handfate.industry.core.action.BaseAction.INT_VIEWGROUP_RECURSIVE;
import com.handfate.industry.core.action.PopupSingleUserAction;
import com.handfate.industry.core.action.component.UploadField;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.handfate.industry.core.util.ResourceBundleUtils;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class PostageInfoAction extends BaseAction {

    /*YenNTH8
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
    
        setTableName("bm_postage_info");
        setIdColumnName("postage_id");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("postage_id");
        setSortAscending(false);
        setSequenceName("bm_postage_info_seq");
        addTextFieldToForm("postageID", new TextField(), "postage_id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Postage.code", new TextField(), "code", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Postage.name", new TextField(), "name", "string", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        
        addSinglePopupToForm("Policy.ServiceID", "service_id", "int", true, 50, null, null, true, null, false, null, true, true, true, true, new PopupSingleServiceAction(localMainUI), 2,
                null, "", "service_id", "code", "bm_service",null,null); 
        
        addTextFieldToForm("Postage.note", new TextArea(), "note", "string", false, 150, null, null, false, false, null, false, null, true, true, true, true, null);
        
        Date toDate = new Date();
        Date fromDate = new Date(toDate.getTime() - 60l * 24l * 3600l * 1000l);
        List lstParam = new ArrayList();
        lstParam.add(fromDate);
        lstParam.add(toDate);
        setComponentAsSysdate("User.CreateDate", "create_date", true, "date:and_mandatory:366", false, lstParam);
        setComponentAsLoginUser("User.CreateUser", "create_user", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null , null, INT_VIEWGROUP_RECURSIVE);
        return initPanel(2);
    }
    
    @Override 
    public boolean validateEdit (long gID) throws Exception{
        // Goi cuoc da duoc gan cho chinh sach roi thi khong duoc sua
        String sql = "select a.postage_id from bm_policy a where a.postage_info_id = ? ";
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
//    public int getPeriod (long policyID) throws Exception {
//        String getPeriodSQL = "select pos.period from bm_policy po left join bm_postage pos on po.postage_id = pos.postage_id " +
//                                "where po.policy_id = ?";
//        int period = 0;
//        List policyIDPara = new ArrayList();
//        policyIDPara.add(policyID);
//        List <Map> LPeriod = C3p0Connector.queryData(getPeriodSQL, policyIDPara);
//        if(!LPeriod.isEmpty()){
//            period = Integer.parseInt(LPeriod.get(0).get("period").toString());
//        }
//        return period;
//    }
    @Override
    public void afterAddData(Connection connection, long id) throws Exception {
        String sql = "insert into bm_postage p (p.postage_id, p.code, p.name, p.note, p.create_user, p.create_date, p.postage_info_id, p.service_id)" +
                        " SELECT  bm_postage_seq.nextval,i.code, i.name, i.note, i.create_user, i.create_date, i.postage_id , i.service_id" +
                        " from bm_postage_info i " +
                        " where i.postage_id = ?";
        List Para = new ArrayList();
        Para.add(id);
        C3p0Connector.excuteData(sql, Para, connection);
    }
    @Override
    public void afterEditData(Connection connection, long id) throws Exception {
        String sql ="update bm_postage p set " +
                    " p.code = (select i.code from bm_postage_info i where i.postage_id = p.postage_info_id), " +
                    " p.name =(select a.name from bm_postage_info a where a.postage_id = p.postage_info_id), " +
                    " p.note =(select b.note from bm_postage_info b where b.postage_id = p.postage_info_id), " +
                    " p.service_id  =(select c.service_id from bm_postage_info c where c.postage_id = p.postage_info_id) " +
                    " where p.postage_info_id = ?";
        List Para = new ArrayList();
        Para.add(id);
        C3p0Connector.excuteData(sql, Para, connection);
    }
    @Override
    public void afterDeleteData(Connection connection, Object[] deleteArray) throws Exception {
        String sql = "delete from bm_postage where postage_info_id = ?";
        for (int i = 0 ; i < deleteArray.length ; i ++){
            List Para = new ArrayList();
            Para.add(deleteArray[i]);
            C3p0Connector.excuteData(sql, Para, connection);
        }
    }
}
