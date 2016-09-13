/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.BaseAction;
import com.handfate.industry.extend.util.DateTimeUtils;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class H4URevenueFieldAction extends BaseAction  {

    /**
     * Hàm khởi tạo giao diện
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI Vùng giao diện của chức năng
     * @return Giao diện sau khi khởi tạo
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khởi tạo tham số
        setTableName("V_H4U_REVENUE_FIELD");
        setIdColumnName("id");
        setPageLength(25);
        setTableType(INT_NORMAL_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("create_month");
        setSortAscending(false);
        setAllowAdd(false);
        setAllowDelete(false);
        setAllowEdit(false);
        setAllowExport(true);
        setTemplateFile("RevenueFieldTemplate.xls");
        setTemplateHeight(7);
        
        //Thêm các thành phần
        addTextFieldToForm("ID", new TextField(), "id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        
        Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        List lstMonth = new ArrayList();
        for(int i = 2016; i <= currentYear; i++) {
            for(int j = 1; j <= 12; j++) {
                if(i == currentYear && j > currentMonth) break;
                String strMonth = "" + j + "/" + i;
                if(strMonth.length() < 7) strMonth = "0" + strMonth;
                lstMonth.add(strMonth);
            }
        }
        Object[][] arrMonth = new Object[lstMonth.size()][2];
        for(int i = 0; i < lstMonth.size(); i++) {
            arrMonth[i][0] = lstMonth.get(i);
            arrMonth[i][1] = lstMonth.get(i);
        }
        
        addComboBoxToForm("Tháng", new ComboBox(), "create_month", "string",
                true, 100, null, null, true, false, null, false, null, false, false, true, true, arrMonth, null, null);
        addTextFieldToForm("Nhà", new TextField(), "house_name", "string", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("Tiền nhà", new TextField(), "house_price", "int", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("Điện", new TextField(), "electric", "int", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("Nước", new TextField(), "water", "int", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("Internet", new TextField(), "internet", "int", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("Truyền hình cáp", new TextField(), "television", "int", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("Vệ sinh", new PopupDateField(), "clean", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Máy giặt", new TextField(), "wash", "int", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("Dự kiến", new TextField(), "total", "int", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("Thực tế", new TextField(), "actual", "int", true, 100, null, null, false, false, null, false, null, true, true, true, true, null);        
               
        return initPanel(2);
    }    
}
