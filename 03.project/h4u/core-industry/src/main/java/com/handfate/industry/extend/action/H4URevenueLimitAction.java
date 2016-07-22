/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.BaseAction;
import com.handfate.industry.core.action.PopupMultiUserAction;
import com.handfate.industry.core.action.component.UploadField;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class H4URevenueLimitAction extends BaseAction {

    /**
     * HÃ m khá»Ÿi táº¡o giao diá»‡n
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI VÃ¹ng giao diá»‡n cá»§a chá»©c nÄƒng
     * @return Giao diá»‡n sau khi khá»Ÿi táº¡o
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khá»Ÿi táº¡o tham sá»‘
        setTableName("h4u_revenue");
        setIdColumnName("ID");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("ID");
        setSortAscending(false);
        setSequenceName("h4u_revenue_seq");
        setAllowAdd(false);
        setAllowEdit(false);
        setAllowDelete(false);
        
        addQueryWhereCondition(" and id in (select revenue_id from h4u_revenue_user where user_id = " + VaadinUtils.getSessionAttribute("G_UserId") + ") ");
        
        buildTreeSearch("Nhà cho thuê",
                "select house_id, name from h4u_house where 1=1 ", null,
                "house_id", "name", null, "0", " and id in (select revenue_id from h4u_revenue_house where house_id = ?) "
                        + " and id in (select revenue_id from h4u_revenue_user where user_id = " + VaadinUtils.getSessionAttribute("G_UserId") + ") ",
                true);        

        addTextFieldToForm("ID", new TextField(), "ID", "int", true, 50, null, null, false, false, null, false, null, true, true, false, true, null);
        addTextFieldToForm("Tên", new TextField(), "name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("Ngày hạch toán", new PopupDateField(), "create_date", "date", true, null, null, null, true, false, null, false, null, true, true, true, true, null);
        addUploadFieldToForm("File đính kèm", new UploadField(), "ATTACH_FILE", "file", false, null, null, null, false, H4URevenueLimitAction.class.toString(), 10);
        addMultiPopupToForm("Nhà cho thuê", true, false, new PopupMultiHouseAction(localMainUI), 2, null,
                "h4u_revenue_house", "house_id", "revenue_id", "id", "h4u_revenue_house_seq", null, null, null);                
        addMultiPopupToForm("Người nhận", false, false, new PopupMultiUserAction(localMainUI), 2, null,
                "h4u_revenue_user", "user_id", "revenue_id", "id", "h4u_revenue_user_seq", null, null, null);
        
        return initPanel(2);
    }
}
