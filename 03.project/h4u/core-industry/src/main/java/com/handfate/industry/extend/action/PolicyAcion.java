/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import static com.handfate.industry.core.MainUI.mainLogger;
import com.handfate.industry.core.action.BaseAction;
import static com.handfate.industry.core.action.BaseAction.INT_VIEWGROUP_RECURSIVE;
import com.handfate.industry.core.action.PopupSingleUserAction;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.data.Property;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
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
public class PolicyAcion extends BaseAction  {

    /**
     * HÃ m khá»Ÿi táº¡o giao diá»‡n
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI VÃ¹ng giao diá»‡n cá»§a chá»©c nÄƒng
     * @return Giao diá»‡n sau khi khá»Ÿi táº¡o
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khá»Ÿi táº¡o tham sá»‘
        setTableName("bm_policy");
        setIdColumnName("policy_id");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("create_date");
        setSortAscending(false);
        setSequenceName("bm_policy_seq");
      
        //ThÃªm cÃ¡c thÃ nh pháº§n
        addTextFieldToForm("PolicyID", new TextField(), "policy_id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Policy.code", new TextField(), "code", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("Policy.name", new TextField(), "name", "string", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);
//        addComponentToForm("Policy.discountContract", new TextField(), "discount_contract", "float", false, 5, "float>=0<=100", null, false, false, null, false, null, true, true, true, true, null);
//        addComponentToForm("Policy.discountPostage", new TextField(), "discount_postage", "float", false, 5, "float>=0<=100", null, false, false, null, false, null, true, true, true, true, null);                       
        addTextFieldToForm("Policy.fromDate", new PopupDateField(), "from_date", "date", true, 50, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Policy.toDate", new PopupDateField(), "to_date", "date", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addSinglePopupToForm("Policy.ServiceID", "service_id", "int", true, 50, null, null, true, null, false, null, true, true, true, true, new PopupSingleServiceAction(localMainUI), 2,
                null, "", "service_id", "code", "bm_service",null,null); 
       addComboBoxToForm("Policy.PostageID", new ComboBox(), "postage_info_id", "int",
                true, 50, null, null, true, false, null, false, null, true, true, true, true, "select postage_id, code,service_id from bm_postage_info", null, "bm_postage_info", 
                "postage_id", "int", "code", null, null, false, false,"service_id","service_id");
       addTextFieldToForm("Postage.period", new TextField(), "period", "int", false, 10, "int>0", null, false, false, null, false, null, true, true, true, true, null);
       Object[][] postageType = {{2, "Postage.Postpaid"}};
       addComboBoxToForm("Postage.type", new ComboBox(), "paid_type", "int",
                true, 50, null, null, true, false, null, false, null, true, true, true, true, postageType, "1", "Postage.prepay");
       
 // chon hang hoa gan voi chinh sach
        List<List> lstAttachField = new ArrayList();
        List lstRow = addComponentToMultiPopup("Good.price", "price", "long", true, 10, "long>=0");
        lstAttachField.add(lstRow);
        addMultiPopupToForm("Good.code", false, false, new PopupMultiGoodAction(localMainUI), 2, lstAttachField, 
                "bm_policy_good", "good_id", "policy_id",  "id", "bm_policy_good_seq",null,null,null);     
        addTextFieldToForm("Postage.price", new TextField(), "POSTAGE_PRICE", "int", true, 50, "int", null, false, false, null, false, null, true, true, true, true, null);    
        addTextFieldToForm("Policy.note", new TextArea(), "note", "string", false, 500, null, null, false, false, null, false, null, true, true, true, true, null);
        
        Date toDate = new Date();
        Date fromDate = new Date(toDate.getTime() - 60l * 24l * 3600l * 1000l );
        List lstParam = new ArrayList();
        lstParam.add(fromDate);
        lstParam.add(toDate);
        setComponentAsSysdate("User.CreateDate", "create_date", true, "date:and_mandatory:366", false, lstParam);
        setComponentAsLoginUser("User.CreateUser", "create_user", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null , null, INT_VIEWGROUP_ALL);
        
        Property.ValueChangeListener listener = new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    if(getComponent("paid_type").getValue() != null) {
                        if(getComponent("paid_type").getValue().equals("1"))
                            getComponent("period").setEnabled(true);
                        if(getComponent("paid_type").getValue().equals("2")){
                            getComponent("period").setValue("");
                            getComponent("period").setEnabled(false);
                        }
                    }
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }
            }
        };
        ComboBox cboPaidType = (ComboBox) getComponent("paid_type");
        cboPaidType.addValueChangeListener(listener);
        
        return initPanel(2);
    }    
    private boolean checkPeriod()throws Exception{
        if(getComponent("paid_type").getValue().equals("1")){ // tra truoc
            if(getComponent("period").getValue().toString().equals("")){
                Notification.show(ResourceBundleUtils.getLanguageResource("Policy.Error.PeriodIsNull"),
                                            null, Notification.Type.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }
    @Override
    public boolean validateEdit(long id) throws Exception{
        if (!checkPeriod()){
            return false;
        }
        PopupDateField fromDatePop = (PopupDateField) getComponent("from_date");
        Date fromDate = new Date();
        fromDate = fromDatePop.getValue();
        PopupDateField toDatePop = (PopupDateField) getComponent("to_date");
        Date toDate = new Date();
        toDate = toDatePop.getValue();
        if(toDate.before(fromDate)){
            Notification.show(ResourceBundleUtils.getLanguageResource("Policy.Error.TodateBeforeFromDate"),
                                            null, Notification.Type.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    @Override
     public boolean validateAdd() throws Exception{
         if(!checkPeriod()){
             return false;
         }
        PopupDateField fromDatePop = (PopupDateField) getComponent("from_date");
        Date fromDate = new Date();
        fromDate = fromDatePop.getValue();
        PopupDateField toDatePop = (PopupDateField) getComponent("to_date");
        Date toDate = new Date();
        toDate = toDatePop.getValue();
        if(toDate.before(fromDate)){
            Notification.show(ResourceBundleUtils.getLanguageResource("Policy.Error.TodateBeforeFromDate"),
                                            null, Notification.Type.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
     @Override
    public void afterAddData(Connection connection, long id) throws Exception {
        // kiem tra bm_postage xem da co policy dung chua
//        String ckUsed = "select postage_id from bm_postage where postage_info_id = ? and policy_id is not null";
//        List Para = new ArrayList();
//        Para.add(getComponent("postage_info_id").getValue());
//        List <Map> post = C3p0Connector.queryData(ckUsed, Para, connection);
//        long postID = 0;
//        if(post.isEmpty()){ //Chua dung --> them moi --> cap nhat bm_postage
//            String getPosID = "select postage_id from bm_postage where postage_info_id = ?";
//            List getPostIDPara = new ArrayList();
//            getPostIDPara.add(getComponent("postage_info_id").getValue());
//            List <Map> lPosID = C3p0Connector.queryData(getPosID, getPostIDPara, connection);
//            long postID = Long.parseLong(lPosID.get(0).get("postage_id").toString());
//            // update postage_bm --> period, type
//            String udBmPos = "update bm_postage set type = ?, period = ? , policy_id = ? where postage_id =? ";
//            List udBmPosPara = new ArrayList();
//            udBmPosPara.add(getComponent("paid_type").getValue());
//            udBmPosPara.add(getComponent("period").getValue());
//            udBmPosPara.add(id);
//            udBmPosPara.add(postID);
//            C3p0Connector.excuteData(udBmPos, udBmPosPara, connection);
//        }else{ 
            // TAO row BM_POSTAGE MOI
            String isrtPos = "insert into bm_postage p (p.postage_id, p.code, p.name, p.service_id,  p.note, p.create_user, p.create_date,p.type, p.period, p.postage_info_id, p.policy_id) " +
                    " select bm_postage_seq.nextval, i.code, i.name, i.service_id, i.note, i.create_user, i.create_date, ? , ? , ?, ? "
                    + " from bm_postage_info i  where i.postage_id = ?";
            List isrtPosPara = new ArrayList();
            isrtPosPara.add(getComponent("paid_type").getValue());
            isrtPosPara.add(getComponent("period").getValue());
            isrtPosPara.add(getComponent("postage_info_id").getValue());
            isrtPosPara.add(id);
            isrtPosPara.add(getComponent("postage_info_id").getValue());
            C3p0Connector.excuteData(isrtPos, isrtPosPara, connection);
            // lay lai postageID moi tao
            String getNewPosID = "select postage_id from bm_postage where policy_id = ?";
            List getNewPosIDPara = new ArrayList();
            getNewPosIDPara.add(id);
            List <Map> lPosID = C3p0Connector.queryData(getNewPosID, getNewPosIDPara, connection);
            long postID = Long.parseLong(lPosID.get(0).get("postage_id").toString());
//        }
        // update postage_id
        String udPosID = "Update bm_policy set postage_id = ? where policy_id = ?";
        List udPostIDPara = new ArrayList();
        udPostIDPara.add(postID);
        udPostIDPara.add(id);
        C3p0Connector.excuteData(udPosID, udPostIDPara, connection);
    }
    @Override
    public void afterEditData(Connection connection, long id) throws Exception {
        // update postage_id
        String udPos = "update bm_postage set type = ? , period = ? where policy_id = ?";
        List udPosPara = new ArrayList();
        udPosPara.add(getComponent("paid_type").getValue());
        udPosPara.add(getComponent("period").getValue());
        udPosPara.add(id);
        C3p0Connector.excuteData(udPos, udPosPara, connection);
        String udPol = "update bm_postage p " +
                " set p.code = ( select b.code from bm_postage_info b where b.postage_id = ? ), " +
                " p.name = ( select b.name from bm_postage_info b where b.postage_id = ? ),  " +
                " p.service_id = ( select b.service_id from bm_postage_info b where b.postage_id = ? ),   " +
                " p.note = ( select b.note from bm_postage_info b where b.postage_id = ? ),   " +
                " p.create_user = ( select b.create_user from bm_postage_info b where b.postage_id = ? ),  " +
                " p.create_date = ( select b.create_date from bm_postage_info b where b.postage_id = ? ),  " +
                " p.type = ?, " +
                " p.period = ? ," +
                " p.postage_info_id = ?" +
                " where p.policy_id = ? ";
        List udPolPara = new ArrayList();
        udPolPara.add(getComponent("postage_info_id").getValue());
        udPolPara.add(getComponent("postage_info_id").getValue());
        udPolPara.add(getComponent("postage_info_id").getValue());
        udPolPara.add(getComponent("postage_info_id").getValue());
        udPolPara.add(getComponent("postage_info_id").getValue());
        udPolPara.add(getComponent("postage_info_id").getValue());
        udPolPara.add(getComponent("paid_type").getValue());
        udPolPara.add(getComponent("period").getValue());
        udPolPara.add(getComponent("postage_info_id").getValue());
        udPolPara.add(id);
        C3p0Connector.excuteData(udPol, udPolPara, connection);
    }
    @Override
    public void afterDeleteData(Connection connection, Object[] deleteArray) throws Exception {
        // xoa bm_postage_id 
        String delPos ="delete from bm_postage where policy_id =?";
        for (int i = 0; i< deleteArray.length ; i++){
            List delPosPara = new ArrayList ();
            delPosPara.add(deleteArray[i]);
            C3p0Connector.excuteData(delPos, delPosPara, connection);
        }
    }

}