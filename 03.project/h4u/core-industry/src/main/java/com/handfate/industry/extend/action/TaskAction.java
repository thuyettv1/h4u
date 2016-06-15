/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.BaseAction;
import com.handfate.industry.core.action.PopupSingleUserAction;
import com.handfate.industry.core.action.UserAction;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class TaskAction extends BaseAction {

    /**
     * HÃ m khá»Ÿi táº¡o giao diá»‡n
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI VÃ¹ng giao diá»‡n cá»§a chá»©c nÄƒng
     * @return Giao diá»‡n sau khi khá»Ÿi táº¡o
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khá»Ÿi táº¡o tham sá»‘
        setTableName("h4u_task");
        setIdColumnName("task_ID");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("task_ID");
        setSortAscending(false);
        setSequenceName("h4u_task_seq");

        addTextFieldToForm("TASKID", new TextField(), "TASK_ID", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Tên nhiệm vụ", new TextField(), "task_title", "string", true, 200, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Mô tả", new TextField(), "task_description", "string", true, 2000, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Ngày bắt đầu", new PopupDateField(), "START_DATE", "date", false, null, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Deadline", new PopupDateField(), "DUE_DATE", "date", false, null, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Ngày hoàn thành", new PopupDateField(), "RESOLVE_DATE", "date", false, null, null, null, false, false, null, false, null, true, true, true, true, null);
        addSinglePopupToForm("Người thực hiện", "assign_user_id", "int", true, 2000, null, null, true, null, false, null, true, true, true, true, new PopupSingleUserAction(localMainUI), 2,
                null, "", "user_id", "user_name", "sm_user", null, null);
        addSinglePopupToForm("Người theo dõi", "monitor_user_id", "int", true, 50, null, null, true, null, false, null, true, true, true, true, new PopupSingleUserAction(localMainUI), 2,
                null, "", "user_id", "user_name", "sm_user", null, null);
        addSinglePopupToForm("Người phê duyệt", "check_user_id", "int", true, 50, null, null, true, null, false, null, true, true, true, true, new PopupSingleUserAction(localMainUI), 2,
                null, "", "user_id", "user_name", "sm_user", null, null);
        Object[][] state = {{1, "Mới"},{2, "Đang thực hiện"}, {3, "Đã báo cáo"}, {4, "Hoàn thành"}};
        addComboBoxToForm("Trạng thái", new ComboBox(), "state", "int",
                true, 50, null, null, true, false, null, false, null, true, true, true, true, state, "1", "Mới");
        Object[][] type = {{1, "Bình Thường"},{2, "Gấp"}, {3, "Rất gấp"}};
        addComboBoxToForm("Loại nhiệm vụ", new ComboBox(), "task_type", "int",
                true, 50, null, null, true, false, null, false, null, true, true, true, true, type, "1", "Bình thường");

        return initPanel(2);
    }
    public static void main(String[] args) {
        System.out.println("========"+UserAction.class.toString());
    }
}
