/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.*;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class EquipmentAction extends BaseAction  {

    /**
     * Hàm khởi tạo giao diện
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI Vùng giao diện của chức năng
     * @return Giao diện sau khi khởi tạo
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khởi tạo tham số
        setTableName("h4u_equipment");
        setIdColumnName("equipment_id");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("name");
        setSortAscending(true);
        setSequenceName("h4u_equipment_seq");

        //Thêm các thành phần
        addTextFieldToForm("ID", new TextField(), "equipment_id", "int", true, 5, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Mã thiết bị", new TextField(), "name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("Serial", new TextArea(), "serial", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addComboBoxToForm("Loại thiết bị", new ComboBox(), "equipment_type_id", "int",
                true, 50, null, null, false, false, null, false, null, true, true, true, true, "select id, name from h4u_equipment_type", null, "h4u_equipment_type",
                "id", "int", "name", "0", "Loại thiết bị",
                true, false, null, null);
        addTextFieldToForm("Nhà sản xuất", new TextField(), "manufactor", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("Năm sản xuất", new TextField(), "year_product", "int", true, 4, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Ngày sử dụng", new PopupDateField(), "start_date", "date", false, 100, null, null, true, false, null, false, null, true, true, true, true, null);        
        Object[][] status = {{2, "Đang sửa chữa"}, {3, "Hỏng"}};
        addComboBoxToForm("Trạng thái", new ComboBox(), "state", "int",
                true, 50, null, null, false, false, null, false, null, true, true, true, true, status, "1", "Đang sử dụng");
        addSinglePopupToForm("Thuộc phòng", "room_id", "int", true, 50, null, null, true, null, false, null, true, true, true, true, new PopupSingleRoomAction(localMainUI), 2,
                null, "", "room_id", "name", "h4u_room", null, null);
        addTextFieldToForm("Giá tham khảo", new TextField(), "refer_price", "int", true, 18, null, null, true, false, null, false, null, true, true, true, false, null);

        return initPanel(2);
    }    
}
