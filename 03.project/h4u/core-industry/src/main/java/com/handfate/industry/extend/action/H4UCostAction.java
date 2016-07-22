/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.MainUI;
import static com.handfate.industry.core.MainUI.mainLogger;
import com.handfate.industry.core.action.BaseAction;
import com.handfate.industry.core.action.PopupSingleAllUserAction;
import com.handfate.industry.core.action.component.MultiUploadField;
import com.handfate.industry.core.dao.BaseDAO;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.handfate.industry.core.util.AdvancedFileDownloader;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pl.jsolve.templ4docx.core.Docx;
import pl.jsolve.templ4docx.core.VariablePattern;
import pl.jsolve.templ4docx.variable.TextVariable;
import pl.jsolve.templ4docx.variable.Variables;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class H4UCostAction extends BaseAction {

    /**
     * HÃ m khá»Ÿi táº¡o giao diá»‡n
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI VÃ¹ng giao diá»‡n cá»§a chá»©c nÄƒng
     * @return Giao diá»‡n sau khi khá»Ÿi táº¡o
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khá»Ÿi táº¡o tham sá»‘
        setTableName("h4u_cost");
        setIdColumnName("ID");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("ID");
        setSortAscending(false);
        setSequenceName("h4u_cost_seq");
        
        buildTreeSearch("Nhà cho thuê",
                "select house_id, name from h4u_house where 1=1 ", null,
                "house_id", "name", null, "0", " and house_id = ? ", true);        

        addTextFieldToForm("id", new TextField(), "ID", "int", true, 50, null, null, false, false, null, false, null, true, true, false, true, null);
        Object[][] costs = {{"0", "Tiền nhà"}, {"1", "Tiền điện"}, {2, "Tiền nước"}, {3, "Tiền internet"}, {4, "Chi phí quản lý"}, {5, "Chi phí vệ sinh"}, 
            {6, "Chi phí lọc nước"}, {7, "Chi phí bảo trì internet"}, {8, "Chi phí bảo trì Camera"}, {9, "Chi phí khác"}};
        addComboBoxToForm("Loại chi phí", new ComboBox(), "cost_type", "int",
                true, 50, null, null, false, false, null, false, null, true, true, true, true, costs, null, null);
        addTextFieldToForm("Tên chi phí", new TextField(), "cost_name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addComboBoxToForm("Thuộc nhà", new ComboBox(), "house_id", "int",
                false, 50, null, null, false, false, null, false, null, true, true, true, true, "select house_id, name from h4u_house", null, "h4u_house",
                "house_id", "int", "name", "0", "Danh sách nhà",
                true, false, null, null);           
        addTextFieldToForm("Ngày thanh toán", new PopupDateField(), "PAY_DATE", "date", true, null, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Giá trị", new TextField(), "Value", "int", true, 18, null, null, true, false, null, false, null, true, true, true,true, null);

        Property.ValueChangeListener costTypeCombo = new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    String selectedValue = ((ComboBox)getComponent("cost_type")).getValue().toString();
                    String selectedName = "";
                    if(!selectedValue.equals("9")) selectedName = ((ComboBox)getComponent("cost_type")).getItemCaption(selectedValue);
                    getComponent("cost_name").setValue(selectedName);
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }
            }
        };
        ComboBox cboCostType = (ComboBox) getComponent("cost_type");
        cboCostType.addValueChangeListener(costTypeCombo);        
        
        return initPanel(2);
    }
}
