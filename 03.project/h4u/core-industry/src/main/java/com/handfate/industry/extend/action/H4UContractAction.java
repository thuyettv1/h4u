/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.family.common.util.CSVTransformer;
import com.handfate.industry.core.MainUI;
import com.handfate.industry.core.action.BaseAction;
import static com.handfate.industry.core.action.BaseAction.INT_LABEL;
import com.handfate.industry.core.action.PopupSingleUserAction;
import com.handfate.industry.core.action.UserAction;
import com.handfate.industry.core.action.component.ComboboxItem;
import com.handfate.industry.core.action.component.ConfirmationDialog;
import com.handfate.industry.core.action.component.MultiUploadField;
import com.handfate.industry.core.dao.BaseDAO;
import com.handfate.industry.core.util.AdvancedFileDownloader;
import com.handfate.industry.core.util.EncryptDecryptUtils;
import com.handfate.industry.core.util.FileUtils;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
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
        MultiUploadField fileAttach = new MultiUploadField();
        addMultiUploadFieldToForm("File đính kèm", fileAttach, "H4U_CONTRACT_ATTACH", "file", false, null, null, null, false, ContractAction.class.toString(), 5, "contract_id", "ATTACH_FILE", "id", "h4u_contract_attach_seq");        
        
        Button buttonPrint = new Button("In hợp đồng");
        buttonPrint.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    buttonPrintClick();
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });        
        addButton(buttonPrint);
        if (isAllowExport()) {
            panelButton.addComponent(buttonPrint);
        }        
        return initPanel(2);
    }
    
    private void buttonPrintClick() throws Exception {        
        final AdvancedFileDownloader downloaderForLink = new AdvancedFileDownloader();
        downloaderForLink.addAdvancedDownloaderListener(new AdvancedFileDownloader.AdvancedDownloaderListener() {
            @Override
            public void beforeDownload(AdvancedFileDownloader.DownloaderEvent downloadEvent) {
                try {
                    // download file
                    String strDirectory = ResourceBundleUtils.getConfigureResource("FileBaseDirectory")
                            + "Temp";

                    File downloadFile = new File(strDirectory + File.separator + "ContractTemplate.docx");
                    if (!downloadFile.exists()) {
                        Notification.show(ResourceBundleUtils.getLanguageResource("Common.FileNotExist"),
                                null, Notification.Type.ERROR_MESSAGE);
                    }
                    downloaderForLink.setFilePath(strDirectory + File.separator + "ContractTemplate.docx");
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        
        ConfirmationDialog confirmDialog = new ConfirmationDialog(
                        ResourceBundleUtils.getLanguageResource("Common.Confirm"),
                        ResourceBundleUtils.getLanguageResource("Common.ConfirmExecute"), null);

        downloaderForLink.extend(confirmDialog.yesButton);               
        
        Object[] printArray = ((java.util.Collection) table.getValue()).toArray();
        if (printArray != null && printArray.length == 1) {            
            if (checkPermission(printArray)) {
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
                    docx.save(ResourceBundleUtils.getConfigureResource("FileBaseDirectory") + File.separator
                            + "Temp" + File.separator + "ContractTemplate.docx");
                }   
                
                confirmDialog.m_callback = new ConfirmationDialog.Callback() {
                    @Override
                    public void onDialogResult(String buttonName) {
                        try {
                            if (buttonName.equals(ResourceBundleUtils.getLanguageResource("Common.Yes"))) {

                            }
                        } catch (Exception ex) {
                            VaadinUtils.handleException(ex);
                            MainUI.mainLogger.debug("Install error: ", ex);
                        }
                    }
                };                                
                mainUI.addWindow(confirmDialog);
                
                
            }
        } else {
            Notification.show("Bạn phai chon 1 hợp đồng",
                    null, Notification.Type.ERROR_MESSAGE);
        }
    }    
       
}
