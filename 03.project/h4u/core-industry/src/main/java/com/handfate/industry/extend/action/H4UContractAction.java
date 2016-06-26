/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.MainUI;
import com.handfate.industry.core.action.BaseAction;
import com.handfate.industry.core.action.PopupSingleUserAction;
import com.handfate.industry.core.action.component.ConfirmationDialog;
import com.handfate.industry.core.action.component.MultiUploadField;
import com.handfate.industry.core.dao.BaseDAO;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.handfate.industry.core.util.AdvancedFileDownloader;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
        addTextFieldToForm("Giá phòng", new TextField(), "price", "long", true, 18, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Giá điện", new TextField(), "ELECTRIC_PRICE", "long", true, 18, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Giá nước", new TextField(), "WATER_PRICE", "long", true, 18, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Giá internet", new TextField(), "INTERNET_PRICE", "long", true, 18, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Giá truyền hình", new TextField(), "TELEVISION_PRICE", "long", true, 18, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Giá máy giặt", new TextField(), "WASHING_PRICE", "long", true, 18, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Giá vệ sinh", new TextField(), "CLEANING_PRICE", "long", true, 18, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Số người", new TextField(), "NUMBER_PERSON", "int", true, 2, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Ngày ký hơp đồng", new PopupDateField(), "CREATE_DATE", "date", false, null, null, null, false, false, null, false, null, false, false, true, true, null);
        addTextFieldToForm("Ngày tính tiền", new PopupDateField(), "START_DATE", "date", false, null, null, null, false, false, null, false, null, false, false, true, true, null);
        Object[][] state = {{2, "Đã ký"}, {3, "Đã thanh lý"}, {4, "Đã hủy"}};
        addComboBoxToForm("Trạng thái", new ComboBox(), "state", "int",
                true, 50, null, null, false, false, null, false, null, true, true, true, true, state, "1", "Chưa ký");
        addTextFieldToForm("Ngày kết thúc", new PopupDateField(), "END_DATE", "date", false, null, null, null, false, false, null, false, null, false, false, true, true, null);
        addTextFieldToForm("Tiền đặt cọc", new TextField(), "DEPOSIT", "int", true, 18, null, null, true, false, null, false, null, true, true, true, false, null);
        addTextFieldToForm("Tiền phạt", new TextField(), "FORFEIT", "int", true, 18, null, null, true, false, null, false, null, true, true, true, false, null);
        MultiUploadField fileAttach = new MultiUploadField();
        addMultiUploadFieldToForm("File đính kèm", fileAttach, "H4U_CONTRACT_ATTACH", "file", false, null, null, null, false, ContractAction.class.toString(), 5, "contract_id", "ATTACH_FILE", "id", "h4u_contract_attach_seq");

        Button buttonMakeInvoice = new Button("Tạo hóa đơn");
        buttonMakeInvoice.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    buttonMakeInvoiceClick();
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        addButton(buttonMakeInvoice);
        panelButton.addComponent(buttonMakeInvoice);
        
        Button buttonExport = new Button("Xuất hợp đồng");
        buttonExport.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    buttonExportClick(buttonExport);
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        addButton(buttonExport);
        panelButton.addComponent(buttonExport);
        
        return initPanel(2);
    }

    private void buttonMakeInvoiceClick() throws Exception {
        Object[] invoiceArray = ((java.util.Collection) table.getValue()).toArray();
        if (invoiceArray != null && invoiceArray.length == 1) {
            if (checkPermission(invoiceArray)) {
                // Tao file truoc khi download
                BaseDAO baseDao = new BaseDAO();
                List<Map> listmap = baseDao.getContractInfo(Integer.valueOf((String) invoiceArray[0]));
                if (listmap != null && !listmap.isEmpty()) {
                    Map currMAp = listmap.get(0);
                    Connection con = C3p0Connector.getInstance().getConnection();
                    String sqlInsert = "Insert into H4U_INVOICE (INVOICE_ID,CONTRACT_ID,INVOICE_TYPE,"
                            + "STATE,ELECTRIC_START_INDEX,ELECTRIC_END_INDEX,"
                            + "PRICE,CLEAN_PRICE,WATER_PRICE,INTERNET_PRICE,"
                            + "TELEVISION_PRICE,WASHING_PRICE,CREATE_USER_ID,RECEIVE_USER_ID,"
                            + "CREATE_DATE,RESOLVE_DATE,DESCRIPTION,TOTAL_PRICE,ACTUAL_PRICE,"
                            + "START_DATE,END_DATE,ELECTRIC_PRICE,NOTE)"
                            + " values (h4u_invoice_seq.nextval,?,1,"
                            + "0,0,0,"
                            + "?,?,?,?,"
                            + "?,?,?,?,"
                            + "sysdate,sysdate,'',?,0,"
                            + "sysdate,sysdate,?,'')";
                    List isrtConHisPara = new ArrayList();
                    isrtConHisPara.add(((BigDecimal)currMAp.get("contract_id")).doubleValue());
                    isrtConHisPara.add(((BigDecimal)currMAp.get("price")).doubleValue());
                    isrtConHisPara.add(((BigDecimal)currMAp.get("cleaning_price")).doubleValue());
                    isrtConHisPara.add(((BigDecimal)currMAp.get("water_price")).doubleValue());
                    isrtConHisPara.add(((BigDecimal)currMAp.get("internet_price")).doubleValue());
                    isrtConHisPara.add(((BigDecimal)currMAp.get("television_price")).doubleValue());
                    isrtConHisPara.add(((BigDecimal)currMAp.get("washing_price")).doubleValue());
                    isrtConHisPara.add(Long.parseLong(VaadinUtils.getSessionAttribute("G_UserId").toString()));
                    
                    isrtConHisPara.add(((BigDecimal)currMAp.get("electric_price")).doubleValue());
                    double totalPrice = ((BigDecimal)currMAp.get("price")).doubleValue()
                            +((BigDecimal)currMAp.get("cleaning_price")).doubleValue()
                            +((BigDecimal)currMAp.get("water_price")).doubleValue()
                            +((BigDecimal)currMAp.get("internet_price")).doubleValue()
                            +((BigDecimal)currMAp.get("television_price")).doubleValue()
                            +((BigDecimal)currMAp.get("washing_price")).doubleValue();
                    isrtConHisPara.add(totalPrice);
                    isrtConHisPara.add(((BigDecimal)currMAp.get("party_b_id")).doubleValue());
                    C3p0Connector.excuteData(sqlInsert, isrtConHisPara, con);
                    con.commit();
                    con.close();
                }
            }
        } else {
            Notification.show("Bạn phải chon ít nhất 1 hợp đồng",
                    null, Notification.Type.ERROR_MESSAGE);
        }
    }

    private void buttonExportClick(Button buttonExport) throws Exception {
        Object[] printArray = ((java.util.Collection) table.getValue()).toArray();
        if (printArray != null && printArray.length == 1) {
            if (checkPermission(printArray)) {
                String strUUID = UUID.randomUUID().toString();
                String filePath = ResourceBundleUtils.getConfigureResource("FileBaseDirectory") + File.separator
                            + "Temp" + File.separator + "ContractTemplate" + strUUID + ".docx";
                // Tao file truoc khi download
                BaseDAO baseDao = new BaseDAO();
                List<Map> listmap = baseDao.getContractInfo(Integer.valueOf((String) printArray[0]));
                if (listmap != null && listmap.size() == 1) {
                    Map currMAp = listmap.get(0);

                    Docx docx = new Docx(ResourceBundleUtils.getConfigureResource("FileBaseDirectory") + File.separator
                            + "Templates" + File.separator + "ContractTemplate.docx");
                    docx.setVariablePattern(new VariablePattern("#{", "}"));
                    // preparing variabl
                    Variables variables = new Variables();
                    for (Object key : currMAp.keySet()) {
                        variables.addTextVariable(new TextVariable("#{" + key + "}", currMAp.get(key) + ""));
                    }
                    // fill template
                    docx.fillTemplate(variables);

                    // save filled .docx file
                    docx.save(filePath);
                }
                
                Button buttonDownload = new Button("Tải về");
                buttonDownload.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        try {
                            buttonExport.setVisible(true);
                            buttonDownload.setVisible(false);
                        } catch (Exception ex) {
                            VaadinUtils.handleException(ex);
                            MainUI.mainLogger.debug("Install error: ", ex);
                        }
                    }
                });
                addButton(buttonDownload);
                panelButton.addComponent(buttonDownload);

                AdvancedFileDownloader downloaderForLink = new AdvancedFileDownloader();
                downloaderForLink.addAdvancedDownloaderListener(new AdvancedFileDownloader.AdvancedDownloaderListener() {
                    @Override
                    public void beforeDownload(AdvancedFileDownloader.DownloaderEvent downloadEvent) {
                        try {
                            // download file
                            File downloadFile = new File(filePath);
                            if (!downloadFile.exists()) {
                                Notification.show(ResourceBundleUtils.getLanguageResource("Common.FileNotExist"),
                                        null, Notification.Type.ERROR_MESSAGE);
                            }
                            downloaderForLink.setFilePath(filePath);
                        } catch (Exception ex) {
                            VaadinUtils.handleException(ex);
                            MainUI.mainLogger.debug("Install error: ", ex);
                        }
                    }
                });
                downloaderForLink.extend(buttonDownload);
                buttonExport.setVisible(false);
            }
        } else {
            Notification.show("Bạn phai chon 1 hợp đồng",
                    null, Notification.Type.ERROR_MESSAGE);
        }
    }
}
