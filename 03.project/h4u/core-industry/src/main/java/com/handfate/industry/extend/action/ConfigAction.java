/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.BaseAction;
import com.handfate.industry.core.action.component.UploadField;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
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
public class ConfigAction extends BaseAction  {

    /**
     * ANHPTN
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        
        setTableName("BM_CONFIG");
        setIdColumnName("ID");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("id");
        setSortAscending(false);
        setSequenceName("BM_CONFIG_SEQ");
        setAllowDelete(false);// khong cho xoa ban ghi config
        
        addTextFieldToForm("Good.id", new TextField(), "ID", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Config.name", new TextField(), "NAME", "string", true, 100, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Config.code", new TextField(), "CODE", "string", true, 50, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Config.value", new TextField(), "value", "long", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Config.note", new TextArea(), "NOTE", "string", false, 200, null, null, false, false, null, false, null, true, true, true, true, null);        
        
        return initPanel(2);//So column dc chia tren giao dien
    }    
    public long getValue (String code) throws Exception {
        String sql ="select c.value  from bm_config c where c.code = ? ";
        List codePara = new ArrayList();
        codePara.add(code);
        List<Map> lValue = C3p0Connector.queryData(sql, codePara);
        long value = Long.parseLong(lValue.get(0).get("value").toString());
        return value;
    }
}
