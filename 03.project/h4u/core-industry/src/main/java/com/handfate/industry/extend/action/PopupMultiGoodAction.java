package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.PopupMultiAction;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 * @since 14/11/2014
 * @author HienDM
 */

public class PopupMultiGoodAction extends PopupMultiAction {

    /**
     * Hàm khởi tạo giao diện
     * @since 19/11/2014 HienDM
     * @param localMainUI Vùng giao diện của chức năng
     */ 
    public PopupMultiGoodAction(UI localMainUI) throws Exception {
        //Khởi tạo tham số
        setTableName("bm_good");
        setIdColumnName("good_id");
        setNameColumn("code");
        setPageLength(5);
        setTableType(INT_NORMAL_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("code");        
        setSortAscending(true);
        setSequenceName("bm_good_seq");
        
        //Thêm các thành phần
        addTextFieldToForm("GoodID", new TextField(), "good_id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Good.code", new TextField(), "code", "string", true, 200, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Good.name", new TextField(), "name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Good.note", new TextArea(), "note", "string", true, 200, null, null, true, false, null, false, null, true, true, true, true, null);
       
    }
}
