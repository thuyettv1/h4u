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
public class H4UContractAction extends BaseAction {

    /**
     * HÃ m khá»Ÿi táº¡o giao diá»‡n
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI VÃ¹ng giao diá»‡n cá»§a chá»©c nÄƒng
     * @return Giao diá»‡n sau khi khá»Ÿi táº¡o
     */
    private TextField txtMonth = new TextField();
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
        
        buildTreeSearch("Nhà cho thuê",
                "select house_id, name from h4u_house where 1=1 ", null,
                "house_id", "name", null, "0", " and room_id in (select room_id from h4u_room where house_id = ?) ",
                true);        

        addTextFieldToForm("ContractID", new TextField(), "CONTRACT_ID", "int", true, 50, null, null, false, false, null, false, null, true, true, false, true, null);
        addTextFieldToForm("Mã hợp đồng", new TextField(), "contract_code", "string", true, 50, null, null, true, false, null, false, null, true, true, true, false, null);
        addSinglePopupToForm("Bên A", "PARTY_A_ID", "int", true, 50, null, null, true, null, false, null, true, true, true, true, new PopupSingleAllUserAction(localMainUI), 2,
                null, "", "user_id", "user_name", "sm_user", null, null);
        addSinglePopupToForm("Bên B", "PARTY_B_ID", "int", true, 50, null, null, true, null, false, null, true, true, true, true, new PopupSingleAllUserAction(localMainUI), 2,
                null, "", "user_id", "user_name", "sm_user", null, null);
        addSinglePopupToForm("Thuộc phòng", "room_id", "int", true, 50, null, null, true, null, false, null, true, true, true, true, new PopupSingleRoomAction(localMainUI), 2,
                null, "", "room_id", "name", "h4u_room", null, null);
        addTextFieldToForm("Giá phòng", new TextField(), "price", "long", true, 18, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Giá điện", new TextField(), "ELECTRIC_PRICE", "long", true, 18, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Giá nước", new TextField(), "WATER_PRICE", "long", true, 18, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Giá internet", new TextField(), "INTERNET_PRICE", "long", true, 18, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Giá truyền hình", new TextField(), "TELEVISION_PRICE", "long", true, 18, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Giá máy giặt", new TextField(), "WASHING_PRICE", "long", true, 18, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Giá vệ sinh", new TextField(), "CLEANING_PRICE", "long", true, 18, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Số người", new TextField(), "NUMBER_PERSON", "int", true, 2, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Ngày ký hơp đồng", new PopupDateField(), "CREATE_DATE", "date", false, null, null, null, false, false, null, false, null, false, false, true, true, null);
        addTextFieldToForm("Ngày tính tiền", new PopupDateField(), "START_DATE", "date", true, null, null, null, false, false, null, false, null, true, true, true, true, null);
        Object[][] state = {{"1", "Chưa ký"}, {2, "Đã ký"}, {3, "Đã thanh lý"}, {4, "Đã hủy"}};
        addComboBoxToForm("Trạng thái", new ComboBox(), "state", "int",
                true, 50, null, null, false, false, null, false, null, true, true, true, true, state, "1", "Chưa ký");
        addTextFieldToForm("Ngày kết thúc", new PopupDateField(), "END_DATE", "date", false, null, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Tiền đặt cọc", new TextField(), "DEPOSIT", "int", true, 18, null, null, true, false, null, false, null, true, true, true,true, null);
        addTextFieldToForm("Tiền phạt", new TextField(), "FORFEIT", "int", true, 18, null, null, true, false, null, false, null, true, true, true,true, null);
        addTextFieldToForm("Tiền nợ", new TextField(), "debit", "int", true, 18, null, null, true, false, null, false, null, true, true, true,true, "0");
        MultiUploadField fileAttach = new MultiUploadField();
        addMultiUploadFieldToForm("File đính kèm", fileAttach, "H4U_CONTRACT_ATTACH", "file", false, null, null, null, false, ContractAction.class.toString(), 5, "contract_id", "ATTACH_FILE", "id", "h4u_contract_attach_seq");
        addTextFieldToForm("Ghi chú", new TextField(), "note", "string", false, 200, null, null, false, false, null, false, null, true, true, true, true, null);
        addCustomizeToSearchForm("Tháng hoá đơn", txtMonth, "string", false, 100, "int>0", null, false, false, false, false, false, null);
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

        Button buttonDownload = new Button("Tải về");
        Button buttonExport = new Button("Xuất hợp đồng");

        buttonExport.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    buttonExportClick();
                    buttonExport.setEnabled(false);
                    buttonDownload.setEnabled(true);
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        addButton(buttonExport);
        panelButton.addComponent(buttonExport);

        buttonDownload.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    buttonExport.setEnabled(true);
                    buttonDownload.setEnabled(false);
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        addButton(buttonDownload);
        panelButton.addComponent(buttonDownload);
        buttonDownload.setEnabled(false);

        AdvancedFileDownloader downloaderForLink = new AdvancedFileDownloader();
        downloaderForLink.addAdvancedDownloaderListener(new AdvancedFileDownloader.AdvancedDownloaderListener() {
            @Override
            public void beforeDownload(AdvancedFileDownloader.DownloaderEvent downloadEvent) {
                try {
                    // download file
                    String filePath = ResourceBundleUtils.getConfigureResource("FileBaseDirectory") + File.separator
                            + "Temp" + File.separator + "ContractTemplate.docx";
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

        Property.ValueChangeListener lsRoomCombo = new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    if (getCurrentForm() == BaseAction.INT_ADD_FORM) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                        Date date = new Date();
                        ComboBox cbo = (ComboBox) getComponent("room_id");
                        String getRoomName = "select r.*,h.*, r.name as room_name  from h4u_room r join h4u_house h on h.HOUSE_ID = r.HOUSE_ID where room_id = ?";
                        List serPara = new ArrayList();
                        serPara.add(cbo.getValue());
                        DecimalFormat decimalFormat = new DecimalFormat("#");
                        List<Map> lGrCode = C3p0Connector.queryData(getRoomName, serPara);
                        getComponent("contract_code").setValue(lGrCode.get(0).get("room_name") + "-" + dateFormat.format(date)+"");
                        getComponent("price").setValue(decimalFormat.format(lGrCode.get(0).get("refer_price")));
                        getComponent("electric_price").setValue(decimalFormat.format(lGrCode.get(0).get("electric_price")));
                        getComponent("water_price").setValue(decimalFormat.format(lGrCode.get(0).get("water_price")));
                        getComponent("internet_price").setValue(decimalFormat.format(lGrCode.get(0).get("internet_price")));
                        getComponent("television_price").setValue(decimalFormat.format(lGrCode.get(0).get("television_price")));
                        getComponent("washing_price").setValue(decimalFormat.format(lGrCode.get(0).get("washing_price")));
                        getComponent("cleaning_price").setValue(decimalFormat.format(lGrCode.get(0).get("clean_price")));
                        getComponent("DEPOSIT").setValue("3000000");
                        getComponent("FORFEIT").setValue("2000000");
                    }
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }
            }
        };
        ComboBox cboRoom = (ComboBox) getComponent("room_id");
        cboRoom.addValueChangeListener(lsRoomCombo);
        //cboRoom.addValueChangeListener(lsPolicyCombo);
        return initPanel(2);
    }

    private void buttonMakeInvoiceClick() throws Exception {
         if(txtMonth.getValue() ==null || txtMonth.getValue().toString().isEmpty()){
            Notification.show("Bạn phải chon tháng tạo hóa đơn",
                    null, Notification.Type.ERROR_MESSAGE);
            return;
        }
        Object[] invoiceArray = ((java.util.Collection) table.getValue()).toArray();
        if (invoiceArray != null && invoiceArray.length == 1) {
            if (checkPermission(invoiceArray)) {
                // Tao file truoc khi download
                BaseDAO baseDao = new BaseDAO();
                List<Map> listmap = baseDao.getContractInfo(Integer.valueOf((String) invoiceArray[0]));
                if (listmap != null && !listmap.isEmpty()) {
                    Map currMAp = listmap.get(0);
                    Connection con = C3p0Connector.getInstance().getConnection();
                    String sqlCheckExistInvoice = "Select count(*) from h4u_invoice where to_char(START_DATE,'mm') = "+"0"+txtMonth.getValue()+" and contract_id ="+
                            ((BigDecimal) currMAp.get("contract_id")).doubleValue();
                    System.out.println(sqlCheckExistInvoice);
                    int numberOfInvoice = C3p0Connector.checkData(sqlCheckExistInvoice, con);
                    if (numberOfInvoice > 0) {
                        Notification.show("Đã tạo hóa đơn tháng cho hợp đồng này",
                                null, Notification.Type.ERROR_MESSAGE);
                        return;
                    }
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.MONTH,Integer.valueOf(txtMonth.getValue().toString())-1);
                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    Date startDate = cal.getTime();
                    cal.set(Calendar.DATE, 31);
                    Date endDate = cal.getTime();
                    System.out.println("start"+startDate+ " and end : "+endDate);
                    String sqlInsert = "Insert into H4U_INVOICE (INVOICE_ID,CONTRACT_ID,INVOICE_TYPE,"
                            + "STATE,ELECTRIC_START_INDEX,ELECTRIC_END_INDEX,"
                            + "PRICE,CLEANING_PRICE,WATER_PRICE,INTERNET_PRICE,"
                            + "TELEVISION_PRICE,WASHING_PRICE,CREATE_USER_ID,RECEIVE_USER_ID,"
                            + "CREATE_DATE,RESOLVE_DATE,DESCRIPTION,ELECTRIC_PRICE,ACTUAL_PRICE,"
                            + "START_DATE,END_DATE,TOTAL_PRICE,NOTE)"
                            + " values (h4u_invoice_seq.nextval,?,1,"
                            + "0,0,0,"
                            + "?,?,?,?,"
                            + "?,?,?,?,"
                            + "sysdate,sysdate,'',?,0,"
                            + "?,?,?,'')";
                    List isrtConHisPara = new ArrayList();
                    isrtConHisPara.add(((BigDecimal) currMAp.get("contract_id")).doubleValue());
                    isrtConHisPara.add(((BigDecimal) currMAp.get("price")).doubleValue());
                    isrtConHisPara.add(((BigDecimal) currMAp.get("cleaning_price")).doubleValue());
                    isrtConHisPara.add(((BigDecimal) currMAp.get("water_price")).doubleValue());
                    isrtConHisPara.add(((BigDecimal) currMAp.get("internet_price")).doubleValue());
                    isrtConHisPara.add(((BigDecimal) currMAp.get("television_price")).doubleValue());
                    isrtConHisPara.add(((BigDecimal) currMAp.get("washing_price")).doubleValue());
                    isrtConHisPara.add(Long.parseLong(VaadinUtils.getSessionAttribute("G_UserId").toString()));
                    isrtConHisPara.add(((BigDecimal) currMAp.get("party_b_id")).doubleValue());
                    isrtConHisPara.add(((BigDecimal) currMAp.get("electric_price")).doubleValue());
                    isrtConHisPara.add(startDate);
                    isrtConHisPara.add(endDate);
                    double totalPrice = ((BigDecimal) currMAp.get("price")).doubleValue()
                            + ((BigDecimal) currMAp.get("cleaning_price")).doubleValue()
                            + ((BigDecimal) currMAp.get("water_price")).doubleValue()
                            + ((BigDecimal) currMAp.get("internet_price")).doubleValue()
                            + ((BigDecimal) currMAp.get("television_price")).doubleValue()
                            + ((BigDecimal) currMAp.get("washing_price")).doubleValue();
                    isrtConHisPara.add(totalPrice);
                    C3p0Connector.excuteData(sqlInsert, isrtConHisPara, con);
                    con.commit();
                    con.close();
                    System.out.println("Tạo thành công");
                    Notification.show("Tạo hóa đơn thành công",
                            null, Notification.Type.ERROR_MESSAGE);
                }
            }
        } else {
            Notification.show("Bạn phải chon ít nhất 1 hợp đồng",
                    null, Notification.Type.ERROR_MESSAGE);
        }
    }

    private void buttonExportClick() throws Exception {
        Object[] printArray = ((java.util.Collection) table.getValue()).toArray();
        if (printArray != null && printArray.length == 1) {
            if (checkPermission(printArray)) {
                String filePath = ResourceBundleUtils.getConfigureResource("FileBaseDirectory") + File.separator
                        + "Temp" + File.separator + "ContractTemplate.docx";
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
                        if (((String) key).equalsIgnoreCase("room_id")) {
                            double room_id = ((BigDecimal) currMAp.get(key)).doubleValue();
                            Map<String, String> mapEq = getEquipmentInfo(room_id);
                            for (String key1 : mapEq.keySet()) {
                                variables.addTextVariable(new TextVariable("#{" + key1 + "}", mapEq.get(key1) + ""));
                            }
                        } else if (((String) key).equalsIgnoreCase("end_date")) {
                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            String endDate = currMAp.get(key) != null ? dateFormat.format(currMAp.get(key)) : "Vô hạn";
                            variables.addTextVariable(new TextVariable("#{" + key + "}", endDate));
                        } else if (((String) key).equalsIgnoreCase("supply_date")) {
                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            String endDate = currMAp.get(key) != null ? dateFormat.format(currMAp.get(key)) : "";
                            variables.addTextVariable(new TextVariable("#{" + key + "}", endDate));
                        }

                    }
                    Date startDate = (Date) currMAp.get("start_date");
                    System.out.println("startdate : " + startDate.getDate());
                    int numberOfDays = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH) - startDate.getDate() + 1;
                    long first_paid = (long) ((long) (numberOfDays * 1000) / Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
                            * ((BigDecimal) currMAp.get("price")).doubleValue()) / 1000;
                    long total_first_paid = first_paid + ((BigDecimal) currMAp.get("deposit")).longValue();
                    variables.addTextVariable(new TextVariable("#{first_paid}", first_paid + ""));
                    variables.addTextVariable(new TextVariable("#{total_first_paid}", total_first_paid + ""));
                    // fill template
                    docx.fillTemplate(variables);

                    // save filled .docx file
                    docx.save(filePath);
                }
            }
        } else {
            Notification.show("Bạn phai chon 1 hợp đồng",
                    null, Notification.Type.ERROR_MESSAGE);
        }
    }

    public Map<String, String> getEquipmentInfo(double room_id) throws Exception {
        String cql = "select count(*) nb,'eq_'||equipment_type_id eq  from h4u_equipment where room_id=? group by equipment_type_id";
        List lstParameter = new ArrayList();
        lstParameter.add(room_id);
        Map<String, String> mapEquipment = new HashMap<>();
        mapEquipment.put("eq_20", "0");
        mapEquipment.put("eq_21", "0");
        mapEquipment.put("eq_22", "0");
        mapEquipment.put("eq_23", "0");
        mapEquipment.put("eq_24", "0");
        mapEquipment.put("eq_25", "0");
        mapEquipment.put("eq_26", "0");
        mapEquipment.put("eq_27", "0");
        mapEquipment.put("eq_28", "0");
        mapEquipment.put("eq_29", "0");
        mapEquipment.put("eq_30", "0");
        mapEquipment.put("eq_31", "0");
        mapEquipment.put("eq_32", "0");
        mapEquipment.put("eq_33", "0");
        mapEquipment.put("eq_34", "0");
        List<Map> lstMap = C3p0Connector.queryData(cql, lstParameter);
        if (lstMap != null && !lstMap.isEmpty()) {
            for (Map currMap : lstMap) {
                String nb = "";
                String eq = "";
                for (Object key : currMap.keySet()) {
                    if (((String) key).equalsIgnoreCase("nb")) {
                        nb = currMap.get(key) + "";
                    } else {
                        eq = "" + currMap.get(key);
                    }
                }
                mapEquipment.put(eq, nb);
            }
        }
        return mapEquipment;
    }

}
