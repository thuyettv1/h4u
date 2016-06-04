/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.MainUI;
import com.handfate.industry.core.action.BaseAction;
import com.handfate.industry.core.action.PopupSingleUserAction;
import com.handfate.industry.core.action.UserAction;
import com.handfate.industry.core.dao.BaseDAO;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
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
public class H4UContractAction extends BaseAction {

    /**
     * HÃ m khá»Ÿi táº¡o giao diá»‡n
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI VÃ¹ng giao diá»‡n cá»§a chá»©c nÄƒng
     * @return Giao diá»‡n sau khi khá»Ÿi táº¡o
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khá»Ÿi táº¡o tham sá»‘
        setTableName("h4u_contract");
        setIdColumnName("CONTRACT_ID");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("CONTRACT_ID");
        setSortAscending(false);
        setSequenceName("h4u_contract_seq");

        addTextFieldToForm("ContractID", new TextField(), "CONTRACT_ID", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Mã hợp đồng", new TextField(), "contract_code", "string", true, 50, null, null, true, false, null, false, null, true, true, true, true, null);
        addSinglePopupToForm("Bên A", "PARTY_A_ID", "int", true, 50, null, null, true, null, false, null, true, true, true, true, new PopupSingleUserAction(localMainUI), 2,
                null, "", "user_id", "user_name", "sm_user", null, null);
        addSinglePopupToForm("Bên B", "PARTY_B_ID", "int", true, 50, null, null, true, null, false, null, true, true, true, true, new PopupSingleUserAction(localMainUI), 2,
                null, "", "user_id", "user_name", "sm_user", null, null);
        addSinglePopupToForm("Thuộc phòng", "room_id", "int", true, 50, null, null, true, null, false, null, true, true, true, true, new PopupSingleRoomAction(localMainUI), 2,
                null, "", "room_id", "name", "h4u_room", null, null);
        addTextFieldToForm("Giá phòng", new TextField(), "price", "int", true, 18, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Giá điện", new TextField(), "ELECTRIC_PRICE", "int", true, 18, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Giá nước", new TextField(), "WATER_PRICE", "int", true, 18, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Giá internet", new TextField(), "INTERNET_PRICE", "int", true, 18, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Giá truyền hình", new TextField(), "TELEVISION_PRICE", "int", true, 18, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Giá vệ sinh", new TextField(), "CLEANING_PRICE", "int", true, 18, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Số người", new TextField(), "NUMBER_PERSON", "int", true, 2, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Ngày ký hơp đồng", new PopupDateField(), "CREATE_DATE", "date", false, null, null, null, false, false, null, false, null, false, false, true, true, null);
        addTextFieldToForm("Ngày tính tiền", new PopupDateField(), "START_DATE", "date", false, null, null, null, false, false, null, false, null, false, false, true, true, null);
        Object[][] state = {{2, "Đã ký"}, {3, "Đã thanh lý"}, {4, "Đã hủy"}};
        addComboBoxToForm("Trạng thái", new ComboBox(), "state", "int",
                true, 50, null, null, false, false, null, false, null, true, true, true, true, state, "1", "Chưa ký");
        addTextFieldToForm("Ngày kết thúc", new PopupDateField(), "END_DATE", "date", false, null, null, null, false, false, null, false, null, false, false, true, true, null);
        addTextFieldToForm("Tiền đặt cọc", new TextField(), "DEPOSIT", "int", true, 18, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Tiền phạt", new TextField(), "FORFEIT", "int", true, 18, null, null, true, false, null, false, null, true, true, true, false, null);

        Button buttonPrint = new Button("In hợp đồng");
        buttonPrint.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    buttonPrintContractClick();
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        addButton(buttonPrint);
        return initPanel(2);
    }
    public static void main(String[] args) {
        System.out.println("========"+UserAction.class.toString());
    }
}
