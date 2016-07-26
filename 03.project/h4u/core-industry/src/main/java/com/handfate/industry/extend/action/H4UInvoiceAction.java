/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.MainUI;
import com.handfate.industry.core.action.BaseAction;
import com.handfate.industry.core.util.AdvancedFileDownloader;
import com.handfate.industry.core.util.FileUtils;
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
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        addComponentOnlyViewToForm("Số người ở", "number_person", null, true, null, true, null);
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
        addTextFieldToForm("Tổng cộng", new TextField(), "total_price", "float", false, 18, null, null, false, false, null, false, null, true, false, false, true, null);
        addTextFieldToForm("Thực thu", new TextField(), "actual_price", "float", false, 18, null, null, false, false, null, false, null, true, true, true, true, null);
        
        PopupDateField pd = new PopupDateField();
        PopupDateField pd1 = new PopupDateField();
        PopupDateField pd2 = new PopupDateField();
        pd.setResolution(Resolution.SECOND);
        pd1.setResolution(Resolution.SECOND);
        pd2.setResolution(Resolution.SECOND);
        addTextFieldToForm("User.CreateDate", pd, "create_date", "date", true, 100, null, null, true, false, null, false, null, false, false, false, false, null);
        
        
        addTextFieldToForm("Số điện đầu", new TextField(), "electric_start_index", "int", false, 6, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Số điện cuối", new TextField(), "electric_end_index", "int", false, 6, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Ngày bắt đầu", pd1, "start_date", "date", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Ngày kết thúc", pd2, "end_date", "date", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        Button buttonDownload = new Button("Tải về");
        Button buttonExport = new Button("Xuất hóa đơn");

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
                        + "Temp" + File.separator + "Invoice.xls";
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
    
    @Override
    public void afterInitPanel() throws Exception {
        List lstCom = getComponentList("create_date");
        ((PopupDateField) lstCom.get(INT_TO_DATE_COMPONENT)).setValue(toDate);
        ((PopupDateField) lstCom.get(INT_COMPONENT)).setValue(fromDate);        
    }    
    
    private void buttonExportClick() throws Exception {
        Object[] printArray = ((java.util.Collection) table.getValue()).toArray();
        if (printArray != null && printArray.length >= 1) {
            if (checkPermission(printArray)) {
                String strTemplate = ResourceBundleUtils.getConfigureResource("FileBaseDirectory") + "Templates"
                        + File.separator + "template_invoice.xls";
                String filePath = ResourceBundleUtils.getConfigureResource("FileBaseDirectory") + File.separator
                        + "Temp" + File.separator + "Invoice.xls";
                // Dien tham so
                List<Object[][]> lstExportData = new ArrayList();
                List<List> lstParams = new ArrayList();
                
                for(int i = 0; i < printArray.length; i++) {
                    Item data = table.getItem(printArray[i]);
                    String month = data.getItemProperty(ResourceBundleUtils.getLanguageResource("User.CreateDate")).getValue().toString().substring(3);
                    List lstParameter = new ArrayList();
                    List lstRow = new ArrayList();
                    lstRow.add("$month");
                    lstRow.add(month);
                    lstParameter.add(lstRow);

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
                    if(startIndex.isEmpty()) startIndex = "0";
                    List lstRow11 = new ArrayList();
                    lstRow11.add("$first_number");
                    lstRow11.add(startIndex);
                    lstParameter.add(lstRow11); 

                    String endIndex = data.getItemProperty("Số điện cuối").getValue().toString();
                    if(endIndex.isEmpty()) endIndex = "0";
                    List lstRow12 = new ArrayList();
                    lstRow12.add("$last_number");
                    lstRow12.add(endIndex);
                    lstParameter.add(lstRow12);                 

                    Object[][] exportData = {{"","","","","","","","","",""}};
                    lstExportData.add(exportData);
                    List lstTemp = new ArrayList();
                    lstTemp.add(roomName);
                    lstTemp.add(lstParameter);
                    lstParams.add(lstTemp);
                }
                FileUtils.exportExcelWithTemplateMultiSheet(lstExportData, strTemplate, filePath, 44, lstParams);
            }
        } else {
            Notification.show("Bạn phai chon 1 hợp đồng", null, Notification.Type.ERROR_MESSAGE);
        }
    }
}
