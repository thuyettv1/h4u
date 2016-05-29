/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.BaseAction;
import com.handfate.industry.core.action.PopupSingleUserAction;
import com.handfate.industry.core.action.component.MultiUploadField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class HouseAction extends BaseAction  {

    /**
     * ANHPTN
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        
        setTableName("H4U_HOUSE");
        setIdColumnName("house_id");
        setPageLength(25);
        setTableType(INT_NORMAL_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("name");
        setSortAscending(true);
        setSequenceName("H4U_HOUSE_SEQ");
        
        addTextFieldToForm("id", new TextField(), "house_id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Tên", new TextField(), "name", "string", false, 10, null, null, false, false, null, false, null, true, true, true, true, null);
        addSinglePopupToForm("Chủ nhà", "owner_user_id", "int", true, 50, null, null, true, null, false, null, true, true, true, true, new PopupSingleOwnerAction(localMainUI), 2,
                null, "", "user_id", "user_name", "sm_user", null, null);        
        addTextFieldToForm("Địa chỉ", new TextField(), "address", "string", false, 200, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Số lượng tầng", new TextField(), "number_stage", "int", true, 3, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Số lượng phòng", new TextField(), "number_room", "int", true, 3, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Mô tả", new TextArea(), "description", "string", false, 200, null, null, false, false, null, false, null, true, true, true, true, null);  
        addTextFieldToForm("Giá điện", new TextField(), "electric_price", "int", true, 18, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Giá nước", new TextField(), "water_price", "int", true, 18, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Giá máy giặt", new TextField(), "washing_price", "int", true, 18, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Giá vệ sinh", new TextField(), "clean_price", "int", true, 18, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Giá truyền hình", new TextField(), "television_price", "int", true, 18, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Giá internet", new TextField(), "internet_price", "int", true, 18, null, null, true, false, null, false, null, true, true, true, false, null);
        addSinglePopupToForm("Người quản lý", "manage_user_id", "int", true, 50, null, null, true, null, false, null, true, true, true, true, new PopupSingleUserAction(localMainUI), 2,
                null, "", "user_id", "user_name", "sm_user", null, null);                
        MultiUploadField fileAttach = new MultiUploadField();
        addMultiUploadFieldToForm("Ảnh nhà", fileAttach, "H4U_HOUSE_ATTACH", "file", false, null, null, null, false, HouseAction.class.toString(), 5, "house_id", "ATTACH_FILE", "id", "h4u_house_attach_seq");
        
        return initPanel(2);//So column dc chia tren giao dien
    }    
}
