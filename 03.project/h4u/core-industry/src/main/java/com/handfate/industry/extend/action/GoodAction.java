/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.BaseAction;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class GoodAction extends BaseAction  {

    /**
     * HÃƒÂ m khÃ¡Â»Å¸i tÃ¡ÂºÂ¡o giao diÃ¡Â»â€¡n
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI VÃƒÂ¹ng giao diÃ¡Â»â€¡n cÃ¡Â»Â§a chÃ¡Â»Â©c nÃ„Æ’ng
     * @return Giao diÃ¡Â»â€¡n sau khi khÃ¡Â»Å¸i tÃ¡ÂºÂ¡o
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //KhÃ¡Â»Å¸i tÃ¡ÂºÂ¡o tham sÃ¡Â»â€˜
        setTableName("BM_GOOD");
        setIdColumnName("GOOD_ID");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("GOOD_ID");
        setSortAscending(false);
        setSequenceName("BM_GOOD_SEQ");
        //ThÃƒÂªm cÃƒÂ¡c thÃƒÂ nh phÃ¡ÂºÂ§n
        addTextFieldToForm("Good.id", new TextField(), "GOOD_ID", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Good.code", new TextField(), "CODE", "string", true, 50, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Good.name", new TextField(), "NAME", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        //addComponentToForm("Good.createDate", new PopupDateField(), "CREATED_DATE", "date", false, 100, null, null, false, false, null, false, null);
        addTextFieldToForm("Good.note", new TextArea(), "NOTE", "string", false, 200, null, null, false, false, null, false, null, true, true, true, true, null);        
        setComponentAsSysdate("Good.createDate","CREATED_DATE",false,null,true,null);
        return initPanel(2);//So column dc chia tren giao dien
    }    
    @Override // hang hoa da duoc gan cho chinh sach thi khong duoc sua
    public boolean validateEdit (long gID) throws Exception{
        // hang hoa da duoc gan cho chinh sach thi khong duoc sua
        String sql = "select good_id from bm_policy_good where good_id = ? ";
        List gIDPara = new ArrayList();
        gIDPara.add(gID);
        List<Map> LgID = C3p0Connector.queryData(sql, gIDPara);
        if (!LgID.isEmpty()) {
            Notification.show(ResourceBundleUtils.getLanguageResource("Good.Error.IsUsed"),
                                            null, Notification.Type.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
            
}
