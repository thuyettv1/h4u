/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.*;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class EquipmentTypeAction extends BaseAction  {

    /**
     * Hàm khởi tạo giao diện
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI Vùng giao diện của chức năng
     * @return Giao diện sau khi khởi tạo
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khởi tạo tham số
        setTableName("h4u_equipment_type");
        setIdColumnName("id");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("name");
        setSortAscending(true);
        setSequenceName("h4u_equipment_type_seq");

        //Thêm các thành phần
        addTextFieldToForm("id", new TextField(), "id", "int", true, 5, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("loại thiết bị", new TextField(), "name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("Description", new TextArea(), "description", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);

        return initPanel(1);
    }    
}
