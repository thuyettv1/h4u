/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import static com.handfate.industry.core.MainUI.mainLogger;
import com.handfate.industry.core.action.BaseAction;
import com.handfate.industry.core.action.PopupSingleAction;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.UserError;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 *
 * @author HienDM1
 */
public class PopupUpdateCustomerAction extends PopupSingleAction {
    /**
     * Hàm khởi tạo giao diện
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI Vùng giao diện của chức năng
     */
    public PopupUpdateCustomerAction(UI localMainUI) throws Exception {
        //Khởi tạo tham số
        setTableName("bm_customer");
        setIdColumnName("customer_id");
        setNameColumn("code");
        setPageLength(10);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("customer_id");
        setSortAscending(false);
        setSequenceName("bm_customer_seq");

        addTextFieldToForm("CustomerID", new TextField(), "customer_id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        Object[][] type = {{2, "Customer.community"}};
        addComboBoxToForm("Customer.type", new ComboBox(), "type", "int",
                true, 50, null, null, true, false, null, false, null, true, true, true, true, type, "1", "Customer.individual");
        addTextFieldToForm("Customer.CustomerName", new TextField(), "name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Customer.CustomerCode", new TextField(), "code", "string", true, 100, null, null, true, false, null, false, null, true, true, false, false, null);
        addTextFieldToForm("Customer.Birthday", new PopupDateField(), "birthday", "date", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        Object[][] genderData = {{2, "Female"}};
        addComboBoxToForm("Customer.Gender", new ComboBox(), "gender", "int",
                false, 50, null, null, false, false, null, false, null, true, true, true, true, genderData, "1", "Male");
        addTextFieldToForm("Customer.IDNo", new TextField(), "id_no", "string", false, 50, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Customer.IDDate", new PopupDateField(), "id_date", "date", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Customer.IDPlace", new TextField(), "id_place", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Customer.TaxCode", new TextField(), "taxcode", "string", false, 50, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Customer.Address", new TextArea(), "address", "string", false, 200, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Customer.BusinessLicenses", new TextField(), "business_licenses", "string", false, 50, null, null, true, false, null, true, null, true, true, false, false, null);
        addTextFieldToForm("Customer.FoundedDate", new PopupDateField(), "founded_date", "date", false, 100, null, null, false, false, null, false, null, true, true, false, false, null);
        addTextFieldToForm("Customer.RepresentativeName", new TextField(), "representative_name", "string", false, 100, null, null, true, false, null, false, null, true, true, false, false, null);
        addTextFieldToForm("Customer.RepresentativePosition", new TextField(), "representative_position", "string", false, 100, null, null, false, false, null, false, null, true, true, false, false, null);
        addTextFieldToForm("Customer.RepresentativeMobile", new TextField(), "representative_mobile", "string", false, 20, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Customer.RepresentativeEmail", new TextField(), "representative_email", "string", false, 100, "email", null, false, false, null, false, null, true, true, true, true, null);
        addComboBoxToForm("Customer.CustomerType", new ComboBox(), "type_id", "int",
                false, 50, null, null, false, false, null, false, null, true, true, true, true, "select type_id, name, type from bm_customer_type where is_enable = 1", null, "bm_customer_type",
                "type_id", "int", "name", null, null, false, false, "type", "type");
        Property.ValueChangeListener listener = new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    ComboBox cbo = (ComboBox) getComponent("type");
                    if(cbo.getValue() != null) {
                        int cboValue = Integer.parseInt(cbo.getValue().toString());
                        if (cboValue == 1) {// CA NHAN
                            getComponent("birthday").setEnabled(true);
                            getComponent("gender").setEnabled(true);
                            getComponent("id_no").setEnabled(true);
                            getComponent("id_date").setEnabled(true);
                            getComponent("id_place").setEnabled(true);
                            getComponent("business_licenses").setValue("");
                            getComponent("business_licenses").setEnabled(false);
                            getComponent("founded_date").setValue(null);
                            getComponent("founded_date").setEnabled(false);
                            getComponent("representative_name").setValue("");
                            getComponent("representative_name").setEnabled(false);
                            getComponent("representative_position").setValue("");
                            getComponent("representative_position").setEnabled(false);
                        }
                        if (cboValue == 2) {
                            getComponent("birthday").setValue(null);
                            getComponent("birthday").setEnabled(false);
                            getComponent("gender").setEnabled(false);
                            getComponent("gender").setValue(null);
                            getComponent("id_no").setEnabled(false);
                            getComponent("id_no").setValue("");
                            getComponent("id_date").setEnabled(false);
                            getComponent("id_date").setValue(null);
                            getComponent("id_place").setEnabled(false);
                            getComponent("id_place").setValue("");
                            getComponent("business_licenses").setEnabled(true);
                            getComponent("founded_date").setEnabled(true);
                            getComponent("representative_name").setEnabled(true);
                            getComponent("representative_position").setEnabled(true);
                        }
                    }
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }
            }
        };
        ComboBox cbo = (ComboBox) getComponent("type");
        cbo.addValueChangeListener(listener);
        
        TextField idNo = (TextField) getComponent("id_no");
        idNo.addBlurListener(new FieldEvents.BlurListener(){
            @Override
             public void blur(FieldEvents.BlurEvent event) {
                 try {
                     if (getCurrentForm() != BaseAction.INT_SEARCH_FORM) {
                         getComponent("code").setValue(getComponent("id_no").getValue().toString());
                     }
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }
            }
        });
        
        TextField businessLC = (TextField) getComponent("business_licenses");
        businessLC.addBlurListener(new FieldEvents.BlurListener(){
            @Override
             public void blur(FieldEvents.BlurEvent event) {
                 try {
                     if (getCurrentForm() != BaseAction.INT_SEARCH_FORM) {
                         getComponent("code").setValue(getComponent("business_licenses").getValue().toString());
                     }
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }
            }
        });
    }

    @Override
    public boolean prepareEdit() throws Exception {
        try {
            ComboBox cbo = (ComboBox) getComponent("type");
            int cboValue = Integer.parseInt(cbo.getValue().toString());
            if(cbo.getValue() != null) {            
                if (cboValue == 1) {
                    getComponent("birthday").setEnabled(true);
                    getComponent("gender").setEnabled(true);
                    getComponent("id_no").setEnabled(true);
                    getComponent("id_date").setEnabled(true);
                    getComponent("id_place").setEnabled(true);
                    getComponent("business_licenses").setEnabled(false);
                    getComponent("founded_date").setEnabled(false);
                    getComponent("representative_name").setEnabled(false);
                    getComponent("representative_position").setEnabled(false);
                }
                if (cboValue == 2) {
                    getComponent("birthday").setEnabled(false);
                    getComponent("gender").setEnabled(false);
                    getComponent("id_no").setEnabled(false);
                    getComponent("id_date").setEnabled(false);
                    getComponent("id_place").setEnabled(false);
                    getComponent("business_licenses").setEnabled(true);
                    getComponent("founded_date").setEnabled(true);
                    getComponent("representative_name").setEnabled(true);
                    getComponent("representative_position").setEnabled(true);
                }
            }
        } catch (Exception ex) {
            VaadinUtils.handleException(ex);
            mainLogger.debug("Industry error: ", ex);
        }
        return true;
    }
    
    @Override
    public void afterPrepareEdit() throws Exception {
        getComponentLabel("id_no").setStyleName("BoldLabel");
        getComponentLabel("business_licenses").setStyleName("BoldLabel");
    }    
    
    @Override
    public void afterPrepareAdd() throws Exception {
        getComponentLabel("id_no").setStyleName("BoldLabel");
        getComponentLabel("business_licenses").setStyleName("BoldLabel");
    }    
    
    @Override
    public boolean validateAdd() throws Exception{
        getComponent("id_no").setComponentError(null);
        getComponent("business_licenses").setComponentError(null);        
        ComboBox cbo = (ComboBox) getComponent("type");
        if(cbo.getValue() != null) {   
            int cboValue = Integer.parseInt(cbo.getValue().toString()); 
            if (cboValue == 1) {
                if(!(getComponent("id_no").getValue() != null && 
                        !getComponent("id_no").getValue().toString().isEmpty())) {
                    getComponent("id_no").setComponentError(new UserError("[" + 
                            ResourceBundleUtils.getLanguageResource("Customer.IDNo") +
                            "]" +
                        ResourceBundleUtils.getLanguageResource("Common.Error.IsRequired")));
                }
            } 
            if (cboValue == 2) {
                if (!(getComponent("business_licenses").getValue() != null
                        && !getComponent("business_licenses").getValue().toString().isEmpty())) {
                    getComponent("business_licenses").setComponentError(new UserError("["
                            + ResourceBundleUtils.getLanguageResource("Customer.BusinessLicenses")
                            + "]"
                            + ResourceBundleUtils.getLanguageResource("Common.Error.IsRequired")));
                }            
            }
        }
        return true;
    }
    
    @Override
    public boolean validateEdit(long id) throws Exception{
        return true;
    }    
    
    @Override
    public void beforeInitPanel() throws Exception {
        setAllowAdd(true);
    }
}
