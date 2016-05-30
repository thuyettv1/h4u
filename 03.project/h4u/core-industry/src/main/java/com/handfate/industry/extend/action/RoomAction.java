/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import static com.handfate.industry.core.MainUI.mainLogger;
import com.handfate.industry.core.action.BaseAction;
import static com.handfate.industry.core.action.BaseAction.INT_VIEWGROUP_ALL;
import com.handfate.industry.core.action.PopupSingleUserAction;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.server.UserError;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class RoomAction extends BaseAction {

    /**
     * HÃ m khá»Ÿi táº¡o giao diá»‡n
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI VÃ¹ng giao diá»‡n cá»§a chá»©c nÄƒng
     * @return Giao diá»‡n sau khi khá»Ÿi táº¡o
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khá»Ÿi táº¡o tham sá»‘
        setTableName("H4U_ROOM");
        setIdColumnName("room_id");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("name");
        setSortAscending(false);
        setSequenceName("h4u_room_seq");
        buildTreeSearch("Nhà cho thuê",
                "select house_id, name from h4u_house", null,
                "house_id", "name", null, "0", true, "house_id");
        //ThÃªm cÃ¡c thÃ nh pháº§n
                
        addTextFieldToForm("id", new TextField(), "room_id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Tên", new TextField(), "name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Ngày nhận phòng", new PopupDateField(), "start_date", "date", false, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Diện tích(m2)", new TextField(), "acreage", "int", true, 3, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Mô tả", new TextArea(), "description", "string", false, 200, null, null, false, false, null, false, null, true, true, true, true, null);  
        addComboBoxToForm("Nhà", new ComboBox(), "house_id", "int",
                true, 50, null, null, false, false, null, false, null, true, true, true, true, "select house_id, name from h4u_house", null, "h4u_house",
                "house_id", "int", "name", "0", "Danh sách nhà",
                true, false, null, null);   
        addTextFieldToForm("Tầng", new TextField(), "stage", "int", true, 3, null, null, true, false, null, false, null, true, true, true, false, null);
        addCheckBoxToForm("Khép kín", new CheckBox(), "has_toilet", "boolean",
                false, 10, null, null, false, false, null, false, null, true, true, true, true, "Khép kín", "Không khép kín");
        addTextFieldToForm("Giá tham khảo", new TextField(), "refer_price", "int", true, 18, null, null, true, false, null, false, null, true, true, true, false, null);
        
        return initPanel(2);
    }    
}
