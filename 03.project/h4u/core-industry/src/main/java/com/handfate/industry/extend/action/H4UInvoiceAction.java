/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.MainUI;
import com.handfate.industry.core.action.BaseAction;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.handfate.industry.core.util.AdvancedFileDownloader;
import com.handfate.industry.core.util.FileUtils;
import com.handfate.industry.core.util.MailSender;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.data.Item;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.viettel.nonPDFconvert.Converter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class H4UInvoiceAction extends BaseAction {

    public TextField txtTotalCost = new TextField();
    public Date fromDate;
    public Date toDate;

    /**
     * Hàm khởi tạo giao diện
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI Vùng giao diện các chức năng
     * @return Giao diện sau khi khởi tạo
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khá»Ÿi táº¡o tham sá»‘
        setTableName("h4u_invoice");
        setViewName("v_h4u_invoice");
        setIdColumnName("invoice_id");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("room_name");
        setSortAscending(true);
        setSequenceName("h4u_invoice_seq");

        buildTreeSearch("Nhà cho thuê",
                "select house_id, name from h4u_house where 1=1 ", null,
                "house_id", "name", null, "0", " and contract_id in (select contract_id from h4u_contract where room_id in (select room_id from h4u_room where house_id = ?)) ",
                true);

        // Set tham số tìm kiếm mặc định
        Calendar c = Calendar.getInstance();   // this takes current date
        c.set(Calendar.DAY_OF_MONTH, 1);
        fromDate = c.getTime();
        c.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
        toDate = c.getTime();
        List lstParam = new ArrayList();
        lstParam.add(fromDate);
        lstParam.add(toDate);
        //addQueryWhereCondition(" and create_date > ? and create_date < ? ");
        //addQueryWhereParameter(lstParam);

        //ThÃªm cÃ¡c thÃ nh pháº§n
        addTextFieldToForm("InvoiceID", new TextField(), "invoice_id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addComponentOnlyViewToForm("Phòng", "room_name", null, false, null, false, null);
        //setComponentAsLoginUser("User.CreateUser", "create_user_id", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null, null, INT_VIEWGROUP_ALL);
        // Them customer ID theo hop dong 
        addSinglePopupToForm("Khách hàng", "RECEIVE_USER_ID", "int", true, 50, null, null, true, null, false, null, true, true, true, true, new PopupSingleCustomerAction(localMainUI), 2,
                null, "", "user_id", "user_name", "sm_users", null, null);
        addComponentOnlyViewToForm("Điện thoại", "phone", null, false, null, false, null);        
        addComponentOnlyViewToForm("Số người ở", "number_person", null, false, null, true, null);
        addComponentOnlyViewToForm("Tháng", "month_invoice", null, false, null, false, null);
        Object[][] invoiceStatus = {{"0", "Invoice.Unpaid"}, {"1", "Invoice.Paid"}, {"2", "Invoice.Cancel"}, {"3", "Invoice.NoPay"}};
        addComboBoxToForm("Invoice.Status", new ComboBox(), "state", "int",
                true, 50, null, null, true, false, null, false, null, false, false, true, true, invoiceStatus, "0", "Invoice.Unpaid");
        addTextFieldToForm("Giá nhà", new TextField(), "price", "float", false, 18, null, null, false, false, null, false, null, true, true, false, true, null);
        addTextFieldToForm("Giá vệ sinh", new TextField(), "cleaning_price", "float", false, 18, null, null, false, false, null, false, null, true, true, false, true, null);
        addTextFieldToForm("Giá điện", new TextField(), "electric_price", "float", false, 18, null, null, false, false, null, false, null, true, true, false, true, null);
        addTextFieldToForm("Giá nước", new TextField(), "water_price", "float", false, 18, null, null, false, false, null, false, null, true, true, false, true, null);
        addTextFieldToForm("Giá internet", new TextField(), "internet_price", "float", false, 18, null, null, false, false, null, false, null, true, true, false, true, null);
        addTextFieldToForm("Giá TH cáp", new TextField(), "television_price", "float", false, 18, null, null, false, false, null, false, null, true, true, false, true, null);
        addTextFieldToForm("Giá máy giặt", new TextField(), "washing_price", "float", false, 18, null, null, false, false, null, false, null, true, true, false, true, null);

        PopupDateField pd = new PopupDateField();
        PopupDateField pd1 = new PopupDateField();
        PopupDateField pd2 = new PopupDateField();
        pd.setResolution(Resolution.SECOND);
        pd1.setResolution(Resolution.SECOND);
        pd2.setResolution(Resolution.SECOND);
        addTextFieldToForm("User.CreateDate", pd, "create_date", "date", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);

        addTextFieldToForm("Số điện đầu", new TextField(), "electric_start_index", "int", false, 6, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Số điện cuối", new TextField(), "electric_end_index", "int", false, 6, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Số điện đầu khác (Nếu có)", new TextField(), "E_START_INDEX", "int", false, 6, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Số điện cuối khác (Nếu có)", new TextField(), "e_end_index", "int", false, 6, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Tiền nợ tháng trước", new TextField(), "debit", "int", true, 18, null, null, true, false, null, false, null, true, true, true, true, "0");
        addTextFieldToForm("Ghi chú", new TextField(), "note", "string", false, 200, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Ngày bắt đầu", pd1, "start_date", "date", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Ngày kết thúc", pd2, "end_date", "date", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Tổng cộng", new TextField(), "total_price", "float", false, 18, null, null, false, false, null, false, null, true, true, false, false, null);
        addTextFieldToForm("Thực thu", new TextField(), "actual_price", "float", false, 18, null, null, false, false, null, false, null, true, true, true, true, null);
        Button buttonDownload = new Button("Tải về");
        Button buttonExport = new Button("Xuất hóa đơn");
        Button buttonSendMail = new Button("Gửi Email");
        buttonSendMail.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    sendEmail();
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        addButton(buttonSendMail);
        panelButton.addComponent(buttonSendMail);
        
        buttonExport.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    if(buttonExportClick()) {
                        buttonExport.setEnabled(false);
                        buttonDownload.setEnabled(true);
                    }
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
                            + "Temp" + File.separator + "Invoice.pdf";
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
        
        return initPanel(2);
    }

    public void sendEmail() throws Exception {
        Object[] sendArray = ((java.util.Collection) table.getValue()).toArray();
        if (sendArray != null && sendArray.length >= 1) {
            String inQuery = "(" + sendArray[0];
            for (int i = 0; i < sendArray.length; i++) {
                inQuery += "," + sendArray[i];
            }
            inQuery += ")";
            
            String SQL = " SELECT   c.invoice_id, a.email " +
                        "   FROM   sm_users a, h4u_contract b, h4u_invoice c " +
                        "  WHERE       a.user_id = b.party_b_id " +
                        "          AND b.contract_id = c.contract_id " +
                        "          AND c.invoice_id IN " + inQuery;

            List<Map> lstEmail = C3p0Connector.queryData(SQL);
            HashMap<String, String> mapEmail = new HashMap();
            for(int i = 0; i < lstEmail.size(); i++) {
                mapEmail.put(lstEmail.get(i).get("invoice_id").toString(), lstEmail.get(i).get("email").toString());
            }
            
            for(int i = 0; i < sendArray.length; i++) {
                Object[] exportArray = new Object[1];
                exportArray[0] = sendArray[i];
                Calendar cal = Calendar.getInstance();
                String fileName = "" + cal.get(Calendar.YEAR)
                        + (cal.get(Calendar.MONTH) + 1) + cal.get(Calendar.DATE)
                        + cal.get(Calendar.HOUR) + cal.get(Calendar.MINUTE) + cal.get(Calendar.SECOND)
                        + "_" + "revenue" + UUID.randomUUID();
                String filePath = ResourceBundleUtils.getConfigureResource("FileBaseDirectory")
                    + "Temp" + File.separator + fileName;
                try {
                    exportInvoiceFile(exportArray, filePath);
                    List lstFiles = new ArrayList();
                    List file = new ArrayList();
                    file.add("Hoa_don.pdf");
                    file.add(filePath);
                    lstFiles.add(file);
                    MailSender ms = new MailSender();
                    ms.sendMail(mapEmail.get(sendArray[i].toString()), "Hoá đơn tiền phòng", "H4U kính gửi quý khách hóa đơn tiền phòng!", lstFiles);
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);                    
                }
            }
        } else {
            Notification.show("Bạn phải chon 1 hóa đơn", null, Notification.Type.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void afterInitPanel() throws Exception {
//        List lstCom = getComponentList("create_date");
//        ((PopupDateField) lstCom.get(INT_TO_DATE_COMPONENT)).setValue(toDate);
//        ((PopupDateField) lstCom.get(INT_COMPONENT)).setValue(fromDate);        
    }

    private boolean buttonExportClick() throws Exception {
        Object[] printArray = ((java.util.Collection) table.getValue()).toArray();
        if (printArray != null && printArray.length >= 1) {
            String filePath = ResourceBundleUtils.getConfigureResource("FileBaseDirectory") + File.separator
                    + "Temp" + File.separator + "Invoice";            
            exportInvoiceFile(printArray, filePath);
            return true;
        } else {
            Notification.show("Bạn phải chon 1 hóa đơn", null, Notification.Type.ERROR_MESSAGE);
            return false;
        }
    }

    public void exportInvoiceFile(Object[] printArray, String filePath) throws Exception {
        String strTemplate = ResourceBundleUtils.getConfigureResource("FileBaseDirectory") + "Templates"
                + File.separator + "template_invoice.xls";
        // Dien tham so
        List<Object[][]> lstExportData = new ArrayList();
        List<List> lstParams = new ArrayList();

        for (int i = 0; i < printArray.length; i++) {
            Item data = table.getItem(printArray[i]);
            String month = data.getItemProperty(ResourceBundleUtils.getLanguageResource("User.CreateDate")).getValue().toString().substring(3,5);
            int intMonth = Integer.parseInt(month.replace("0", ""));
            List lstParameter = new ArrayList();
            List lstRow = new ArrayList();
            lstRow.add("$month");
            lstRow.add("" + intMonth);            
            lstParameter.add(lstRow);
            List lstMonth = new ArrayList();
            lstMonth.add("$month1");
            lstMonth.add("" + (intMonth - 1));            
            lstParameter.add(lstMonth);            

            String roomName = data.getItemProperty("Phòng").getValue().toString();
            List lstRow1 = new ArrayList();
            lstRow1.add("$room_name");
            lstRow1.add(roomName);
            lstParameter.add(lstRow1);

            String customer = data.getItemProperty("Khách hàng").getValue().toString();
            List lstRow2 = new ArrayList();
            lstRow2.add("$customer");
            lstRow2.add(customer);
            lstParameter.add(lstRow2);            

            String houseMoney = data.getItemProperty("Giá nhà").getValue().toString();
            List lstRow3 = new ArrayList();
            lstRow3.add("$house_money");
            lstRow3.add(houseMoney);
            lstParameter.add(lstRow3);

            String electricMoney = data.getItemProperty("Giá điện").getValue().toString();
            List lstRow4 = new ArrayList();
            lstRow4.add("$electric_money");
            lstRow4.add(electricMoney);
            lstParameter.add(lstRow4);

            String waterMoney = data.getItemProperty("Giá nước").getValue().toString();
            List lstRow5 = new ArrayList();
            lstRow5.add("$water_money");
            lstRow5.add(waterMoney);
            lstParameter.add(lstRow5);

            String cleanMoney = data.getItemProperty("Giá vệ sinh").getValue().toString();
            List lstRow6 = new ArrayList();
            lstRow6.add("$clean_money");
            lstRow6.add(cleanMoney);
            lstParameter.add(lstRow6);

            String capMoney = data.getItemProperty("Giá TH cáp").getValue().toString();
            List lstRow7 = new ArrayList();
            lstRow7.add("$cap_money");
            lstRow7.add(capMoney);
            lstParameter.add(lstRow7);

            String washMoney = data.getItemProperty("Giá máy giặt").getValue().toString();
            List lstRow8 = new ArrayList();
            lstRow8.add("$wash_money");
            lstRow8.add(washMoney);
            lstParameter.add(lstRow8);

            String internetMoney = data.getItemProperty("Giá internet").getValue().toString();
            List lstRow9 = new ArrayList();
            lstRow9.add("$internet_money");
            lstRow9.add(internetMoney);
            lstParameter.add(lstRow9);

            String numberPerson = data.getItemProperty("Số người ở").getValue().toString();
            List lstRow10 = new ArrayList();
            lstRow10.add("$person_num");
            lstRow10.add(numberPerson);
            lstParameter.add(lstRow10);

            String startIndex = data.getItemProperty("Số điện đầu").getValue().toString();
            if (startIndex.isEmpty()) {
                startIndex = "0";
            }
            List lstRow11 = new ArrayList();
            lstRow11.add("$first_number");
            lstRow11.add(startIndex);
            lstParameter.add(lstRow11);

            String endIndex = data.getItemProperty("Số điện cuối").getValue().toString();
            if (endIndex.isEmpty()) {
                endIndex = "0";
            }
            List lstRow12 = new ArrayList();
            lstRow12.add("$last_number");
            lstRow12.add(endIndex);
            lstParameter.add(lstRow12);

            String startIndex1 = data.getItemProperty("Số điện đầu khác (Nếu có)").getValue().toString();
            if (startIndex1.isEmpty()) {
                startIndex1 = "0";
            }
            List lstRow13 = new ArrayList();
            lstRow13.add("$first_number1");
            lstRow13.add(startIndex1);
            lstParameter.add(lstRow13);

            String endIndex1 = data.getItemProperty("Số điện cuối khác (Nếu có)").getValue().toString();
            if (endIndex1.isEmpty()) {
                endIndex1 = "0";
            }
            List lstRow14 = new ArrayList();
            lstRow14.add("$last_number1");
            lstRow14.add(endIndex1);
            lstParameter.add(lstRow14);

            String debit = data.getItemProperty("Tiền nợ tháng trước").getValue().toString();
            if (debit.isEmpty()) {
                debit = "0";
            }
            List lstRow15 = new ArrayList();
            lstRow15.add("$debit");
            lstRow15.add(debit);
            lstParameter.add(lstRow15);
            
            String phone = data.getItemProperty("Điện thoại").getValue().toString();
            List lstRow16 = new ArrayList();
            lstRow16.add("$phone");
            lstRow16.add(phone);
            lstParameter.add(lstRow16);            
            
            Object[][] exportData = {{"", "", "", "", "", "", "", "", "", ""}};
            lstExportData.add(exportData);
            List lstTemp = new ArrayList();
            lstTemp.add(roomName);
            lstTemp.add(lstParameter);
            lstParams.add(lstTemp);
        }
        FileUtils.exportExcelWithTemplateMultiSheet(lstExportData, strTemplate, filePath + ".xls", 48, lstParams);
        Converter c = new Converter();
        ByteArrayOutputStream baos = c.toPDF(filePath + ".xls", "xls", false);
        FileOutputStream out = new FileOutputStream(filePath + ".pdf");
        baos.writeTo(out);
        baos.close();
        out.close();
    }
    
    @Override
    public void beforeEditData(Connection connection, long id) throws Exception {
        System.out.println("before edit");
        try {
            double total = 0;
            double price = Double.valueOf(((TextField) getComponent("price")).getValue());
            double cleaning_price = Double.valueOf(((TextField) getComponent("cleaning_price")).getValue());
            double water_price = Double.valueOf(((TextField) getComponent("water_price")).getValue());
            double internet_price = Double.valueOf(((TextField) getComponent("internet_price")).getValue());
            double television_price = Double.valueOf(((TextField) getComponent("television_price")).getValue());
            double washing_price = Double.valueOf(((TextField) getComponent("washing_price")).getValue());
            double electric_price = Double.valueOf(((TextField) getComponent("electric_price")).getValue());
            double electric_start_index = Double.valueOf(((TextField) getComponent("electric_start_index")).getValue());
            double electric_end_index = Double.valueOf(((TextField) getComponent("electric_end_index")).getValue());
            double e_start_index = Double.valueOf(((TextField) getComponent("e_start_index")).getValue());
            double e_end_index = Double.valueOf(((TextField) getComponent("e_end_index")).getValue());
            double debit = Double.valueOf(((TextField) getComponent("debit")).getValue());
            int num_person = 1;//Double.valueOf(((TextField) getComponent("num_person")).getValue());
            String sqlGetNP = "Select number_person from v_h4u_invoice where invoice_id =" + id;
            int num = C3p0Connector.checkData(sqlGetNP, connection);
            if (num > 0) {
                num_person = num;
            }
            total = price + num_person * (cleaning_price + water_price + washing_price)
                    + internet_price + television_price
                    + electric_price * (electric_end_index + e_end_index - e_start_index - electric_start_index) + debit;
//        double totalPrice = ((BigDecimal) currMAp.get("price")).doubleValue()
//                            + ((BigDecimal) currMAp.get("cleaning_price")).doubleValue()
//                            + ((BigDecimal) currMAp.get("water_price")).doubleValue()
//                            + ((BigDecimal) currMAp.get("internet_price")).doubleValue()
//                            + ((BigDecimal) currMAp.get("television_price")).doubleValue()
//                            + ((BigDecimal) currMAp.get("washing_price")).doubleValue();
            System.out.println("======total=====" + total);
            getComponent("total_price").setValue(total + "");
            if (debit > 0) {
                getComponent("note").setValue("Nợ tháng trước : " + debit);
            }
        } catch (Exception ex) {
            System.out.println("Error");
        }

    }

}
