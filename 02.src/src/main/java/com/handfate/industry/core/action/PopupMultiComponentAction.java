package com.handfate.industry.core.action;

import static com.handfate.industry.core.MainUI.mainLogger;
import com.handfate.industry.core.dao.BuildConfigDAO;
import com.handfate.industry.core.util.ComponentUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @since 14/11/2014
 * @author HienDM
 */

public class PopupMultiComponentAction extends PopupMultiAction {
    private HorizontalLayout emptyLayout1;
    private HorizontalLayout emptyLayout2;
    private List<String> lstEditStore = new ArrayList();

    private int PMA_NEWLABEL1 = 0;
    private int PMA_NEWLABEL2 = 5;
    private int PMA_CHECKBOX1 = 3;
    private int PMA_CHECKBOX2 = 8;
    private int PMA_COMBOBOX1 = 4;
    private int PMA_COMBOBOX2 = 9;    
    
    /**
     * Hàm khởi tạo giao diện
     * @since 19/11/2014 HienDM
     * @param localMainUI Vùng giao diện của chức năng
     */ 
    public PopupMultiComponentAction(UI localMainUI) throws Exception {
        //Khởi tạo tham số
        setTableName("sd_component");
        setIdColumnName("id");
        setNameColumn("viet_nam");
        setPageLength(10);
        storeTable.setPageLength(10);
        setTableType(INT_NORMAL_TABLE);
        setMainUI(localMainUI);
        setRootId("0");
        setSortAscending(true);
        setSequenceName("sd_component_seq");
        emptyLayout1 = ComponentUtils.buildEmptyHorizontalLayout();
        emptyLayout2 = ComponentUtils.buildEmptyHorizontalLayout();

        //Thêm các thành phần
        addTextFieldToForm("id", new TextField(), "id", "int", true, 10, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.VietNam", new TextField(), "viet_nam", "string", true, 50, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.English", new TextField(), "english", "string", true, 50, null, null, true, false, null, false, null, true, true, true, true, null);
        Object[][] ComponentTypeData = 
            {{"TextField","TextField"},{"TextArea","TextArea"},{"DateField","DateField"},{"TimeField","TimeField"},
            {"SysDate","SysDate"},{"LoginUser","LoginUser"},{"CheckBox","CheckBox"},{"ComboBox","ComboBox"},
            {"ConstantComboBox","ConstantComboBox"},{"SinglePopup","SinglePopup"},{"MultiPopup","MultiPopup"},
            {"UploadField","UploadField"},{"MultiUploadField","MultiUploadField"},{"OnlyInViewField","OnlyInViewField"}};
        addComboBoxToForm("SDComponent.Type", new ComboBox(), "type", "string",
                true, 20, null, null, false, false, null, false, null, true, true, true, true, ComponentTypeData, "TextField", "TextField");
        
        addTextFieldToForm("SDFunction.Key", new TextField(), "param0", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Parameter 1", new TextField(), "param1", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Parameter 2", new TextField(), "param2", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Parameter 3", new TextField(), "param3", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Parameter 4", new TextField(), "param4", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Parameter 5", new TextField(), "param5", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Parameter 6", new TextField(), "param6", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Parameter 7", new TextField(), "param7", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Parameter 8", new TextField(), "param8", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Parameter 9", new TextField(), "param9", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Parameter 10", new TextField(), "param10", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Parameter 11", new TextField(), "param11", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Parameter 12", new TextField(), "param12", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Parameter 13", new TextField(), "param13", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Parameter 14", new TextField(), "param14", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Parameter 15", new TextField(), "param15", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Parameter 16", new TextField(), "param16", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Parameter 17", new TextField(), "param17", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Parameter 18", new TextField(), "param18", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Parameter 19", new TextField(), "param19", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Parameter 20", new TextField(), "param20", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Parameter 21", new TextField(), "param21", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Parameter 22", new TextField(), "param22", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Parameter 23", new TextField(), "param23", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Parameter 24", new TextField(), "param24", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Parameter 25", new TextField(), "param25", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        
        // Danh sach tham so
        addMultiPopupToForm("SDComponent.ListParam", false, false, new PopupMultiComponentParamAction(localMainUI), 2, null, "v_com_param", "id", "component_id", null, null, null, null, null);
    }
    
    @Override
    public void beforeInitPanel() throws Exception {
        // Hiển thị các nút thêm mới, sửa, xóa
        setAllowAdd(true);
        setAllowEdit(true);
        setAllowDelete(true);
    }
    
    @Override
    public void afterInitPanel() throws Exception {
        ((ComboBox) getComponent("type")).setPageLength(20);
        Property.ValueChangeListener listener = new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    rebuildPanel();
                    displayComponent();
                    clearParameter();   
                    setDefaultStateParameter();
                    changeComponentType();
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }
            }
        };
        ComboBox cbo = (ComboBox) getComponent("type");
        cbo.addValueChangeListener(listener);
    }

    @Override
    public void afterPrepareAdd() throws Exception {
        ComboBox cbo = (ComboBox) getComponent("type");
        cbo.setValue("TextField");
        hideLabel();
        rebuildPanel();
        displayComponent();
        clearParameter();   
        setDefaultStateParameter();
        changeComponentType();
    }
    
    @Override
    public void afterPrepareEdit() throws Exception {
        saveState();
        hideLabel();
        rebuildPanel();
        displayComponent();   
        setDefaultStateParameter();
        changeComponentType();
        setDefaultData();
    }
/*
    @Override
    public void beforeAddData(Connection connection) throws Exception {
        updateToTextField();
    }

    @Override
    public void beforeEditData(Connection connection, long id) throws Exception {
        updateToTextField();
    }
    */
    /**
     * Hàm thay đổi giao diện khi lựa chọn loại component 
     * 
     * @since 17/04/2015 HienDM
     */      
    private void changeComponentType() throws Exception {
        ComboBox cbo = (ComboBox) getComponent("type");
        if (cbo.getValue() != null) {
            String cboValue = cbo.getValue().toString();
            setOnlyInView(cboValue);// ONLY_IN_VIEW_FIELD
            setComponent(cboValue);// TEXT_FIELD
            setDateComponent(cboValue);// DATE_FIELD, TIME_FIELD
            setSysdateComponent(cboValue);// SYSDATE
            setLoginUserComponent(cboValue);// LOGIN_USER
            setCheckBoxComponent(cboValue);// CHECKBOX
            setComboBoxComponent(cboValue);// COMBOBOX
            setConstantComboBoxComponent(cboValue);// CONSTANT_COMBOBOX
            setSinglePopupComponent(cboValue);// SINGLE_POPUP
            setMultiPopupComponent(cboValue);// MULTI_POPUP
            setUploadComponent(cboValue);// UPLOAD_FIELD
            setMultiUploadComponent(cboValue);// MULTI_UPLOAD_FIELD
        }        
    }
    
    @Override
    public void prepareOpenPopup() throws Exception {
        String query = " and (function_id = ? or function_id is null) ";
        setIsChangeDefaultSearch(true);
        Object id = parent.getComponent("id").getValue();
        if(id != null && id != "") {
            int functionId = Integer.parseInt(id.toString());        
            List lstParameter = new ArrayList();
            lstParameter.add(functionId);  
            setQueryWhereParameter(lstParameter);
            setQueryWhereCondition(query);
        } else {
            query = " and function_id is null  ";  
            setQueryWhereCondition(query);  
            setQueryWhereParameter(null);
        }
    }
    
    /**
     * Hàm xóa dữ liệu các parameter 
     * 
     * @since 17/04/2015 HienDM
     */      
    private void clearParameter() throws Exception {
        for(int i = 0; i <= 25; i++) {
            getComponent("param" + i).setValue("");
        }
    }
    
    /**
     * Hàm ẩn toàn bộ các label
     * 
     * @since 17/04/2015 HienDM
     */  
    private void hideLabel() throws Exception {
        for(int i = 1; i <= 25; i++) {
            getComponentLabel("param" + i).setVisible(false);
        }
    }
    
    private void saveState() {
        lstEditStore = new ArrayList();
        for(int i = 0; i <= 25; i++) {
            if(getComponent("param" + i).getValue() != null)
                lstEditStore.add(getComponent("param" + i).getValue().toString());
            else
                lstEditStore.add("");            
        }        
    }
    
    /**
     * Hàm thiết lập trạng thái mặc định của parameter
     * 
     * @since 17/04/2015 HienDM
     */      
    private void setDefaultStateParameter() throws Exception {
        for(int i = 1; i < 25; i++) {
            if(i%2 == 1) {
                ((HorizontalLayout)getComponent("param" + i).getParent()).removeComponent(emptyLayout1);
                ((HorizontalLayout)getComponent("param" + i).getParent()).removeComponent(emptyLayout2);
            }
            if(i%2 == 0) {
                getComponent("param" + i).setVisible(true);
            }
        }
    }
    
    /**
     * Hàm thiết lập giá trị mặc định cho tất cả component
     * 
     * @since 17/04/2015 HienDM
     */      
    private void setDefaultData() throws Exception {
        for(int i = 1; i <= 25; i++) {
            if(i%2 == 1) {
                setDefaultDataToComponent(i);
            }
        }        
    }
    
    /**
     * Hàm cập nhật dữ liệu từ checkbox và combobox vào textfield
     * 
     * @param param số thứ tự hàng
     * @since 17/04/2015 HienDM
     */       
    private void updateToTextField() throws Exception {
        for(int i = 1; i <= 25; i++) {
            if(i%2 == 1) {
                updateToTextField(i);
            }
        }        
    }
    
    /**
     * Hàm thiết lập giá trị mặc định cho component
     * 
     * @param param số thứ tự hàng
     * @since 17/04/2015 HienDM
     */      
    private void setDefaultDataToComponent(int param) throws Exception {
        CheckBox che1 = (CheckBox)((HorizontalLayout)getComponent("param" + param).getParent()).getComponent(PMA_CHECKBOX1);
        if(che1.isVisible()) {
            if(lstEditStore.get(param).equals("true")) {
                che1.setValue(true);
            } else {
                che1.setValue(false);
            }
        }
        ComboBox cbo1 = (ComboBox)((HorizontalLayout)getComponent("param" + param).getParent()).getComponent(PMA_COMBOBOX1);
        if(cbo1.isVisible()) {
            if(cbo1.containsId(lstEditStore.get(param))) {
                cbo1.setValue(lstEditStore.get(param));
            }
        }
        if(param < 25) {
            CheckBox che2 = (CheckBox)((HorizontalLayout)getComponent("param" + (param+1)).getParent()).getComponent(PMA_CHECKBOX2);
            if(che2.isVisible()) {
                if(lstEditStore.get(param+1).equals("true")) {
                    che2.setValue(true);
                } else {
                    che2.setValue(false);
                }
            }
            
            ComboBox cbo2 = (ComboBox)((HorizontalLayout)getComponent("param" + (param+1)).getParent()).getComponent(PMA_COMBOBOX2);
            if(cbo2.isVisible()) {    
                if(cbo2.containsId(lstEditStore.get(param+1))) {
                    cbo2.setValue(lstEditStore.get(param+1));
                }            
            }
        }
    }
    
    /**
     * Hàm cập nhật dữ liệu từ checkbox và combobox vào textfield
     * 
     * @param param số thứ tự hàng
     * @since 17/04/2015 HienDM
     */      
    private void updateToTextField(int param) throws Exception {
        CheckBox che1 = (CheckBox)((HorizontalLayout)getComponent("param" + param).getParent()).getComponent(PMA_CHECKBOX1);
        if (che1.getValue()) getComponent("param" + param).setValue("true");
        else getComponent("param" + param).setValue("false");                            
        ComboBox cbo1 = (ComboBox)((HorizontalLayout)getComponent("param" + param).getParent()).getComponent(PMA_COMBOBOX1);
        getComponent("param" + param).setValue(cbo1.getValue());
        
        if(param < 25) {
            CheckBox che2 = (CheckBox)((HorizontalLayout)getComponent("param" + param).getParent()).getComponent(PMA_CHECKBOX2);
            if (che2.getValue()) getComponent("param" + param).setValue("true");
            else getComponent("param" + param).setValue("false");                            
            ComboBox cbo2 = (ComboBox)((HorizontalLayout)getComponent("param" + param).getParent()).getComponent(PMA_COMBOBOX2);
            getComponent("param" + param).setValue(cbo2.getValue());
        }
    }    
    
    /**
     * Hàm bổ sung sắp xếp lại các thành phần giao diện trên một hàng
     * 
     * @param param số thứ tự hàng
     * @since 17/04/2015 HienDM
     */      
    private void rebuildRowComponent(int param) throws Exception {
        HorizontalLayout row = (HorizontalLayout)getComponent("param" + param).getParent();
        row.removeAllComponents();
        
        row.addComponent(new Label(""));
        row.addComponent(getComponentLabel("param" + param));
        row.addComponent(getComponent("param" + param));
        CheckBox che1 = new CheckBox();
        che1.addValueChangeListener(new ValueChangeListener() {
            public void valueChange(ValueChangeEvent event) {
                try {
                    if (che1.getValue()) getComponent("param" + param).setValue("true");
                    else getComponent("param" + param).setValue("false");                    
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }                
            }
        });
        che1.setVisible(false);
        row.addComponent(che1);
        
        ComboBox cbo1 = new ComboBox();
        cbo1.setVisible(false);
        cbo1.removeAllItems();
        cbo1.setNullSelectionAllowed(false);
        Property.ValueChangeListener listener = new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    getComponent("param" + param).setValue(cbo1.getValue());
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }
            }
        };
        cbo1.addValueChangeListener(listener); 
        cbo1.setWidth("90%");        
        row.addComponent(cbo1);
        
        if(param < 25) {
            row.addComponent(new Label(""));
            row.addComponent(getComponentLabel("param" + (param + 1)));
            row.addComponent(getComponent("param" + (param + 1)));
            CheckBox che2 = new CheckBox();
            che2.addValueChangeListener(new ValueChangeListener() {
                public void valueChange(ValueChangeEvent event) {
                    try {
                        if (che2.getValue()) getComponent("param" + (param+1)).setValue("true");
                        else getComponent("param" + (param+1)).setValue("false");                    
                    } catch (Exception ex) {
                        VaadinUtils.handleException(ex);
                        mainLogger.debug("Industry error: ", ex);
                    }
                }
            });
            che2.setVisible(false);           
            row.addComponent(che2);
            
            ComboBox cbo2 = new ComboBox();
            cbo2.setVisible(false);
            cbo2.removeAllItems();
            cbo2.setNullSelectionAllowed(false);
            Property.ValueChangeListener listener2 = new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    try {
                        getComponent("param" + (param+1)).setValue(cbo2.getValue());
                    } catch (Exception ex) {
                        VaadinUtils.handleException(ex);
                        mainLogger.debug("Industry error: ", ex);
                    }
                }
            };
            cbo2.addValueChangeListener(listener2);   
            cbo2.setWidth("90%");           
            row.addComponent(cbo2);
        } else {
            row.addComponent(emptyLayout1);
            row.addComponent(emptyLayout2);
        }
    }   
    
    /**
     * Hàm bổ sung và sắp xếp lại toàn bộ thành phần giao diện
     * 
     * @since 17/04/2015 HienDM
     */      
    private void rebuildPanel() throws Exception {
        for(int i = 1; i <= 25; i++) {
            if(i%2 == 1) {
                rebuildRowComponent(i);
            }
        }
    }
    
    /**
     * Hàm hiển thị các component và thay đổi độ rộng từng hàng
     * 
     * @since 17/04/2015 HienDM
     */    
    public void displayComponent() throws Exception {        
        for(int i = 1; i <= 25; i++) {
            getComponent("param" + i).setVisible(true);
        }
    }    
    
    /**
     * Hàm thiết lập component 
     * TEXT_FIELD, ONLY_IN_VIEW_FIELD
     * 
     * @param componentType loại component
     * @since 17/04/2015 HienDM
     */     
    private void setComponent(String componentType) throws Exception {
        if (componentType.equals("TextField") || componentType.equals("TextArea")) {
            getComponent("param0").setDescription("VD: User.UserName");
            getComponent("param1").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param1").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Tên cột trong database"); 
            getComponent("param1").setDescription("VD: user_name");
            ((Label)((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Loại dữ liệu"); 
            getComponent("param2").setDescription("VD: string");
            ComboBox cbo1 = (ComboBox)((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_COMBOBOX2);
            cbo1.addItem("string");
            cbo1.addItem("int");
            cbo1.addItem("long");
            cbo1.addItem("float");
            cbo1.addItem("double");
            cbo1.setValue("string");
            getComponent("param2").setVisible(false);
            cbo1.setVisible(true);
            cbo1.setCaption(getComponent("param2").getCaption());

            getComponent("param3").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param3").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Bắt buộc nhập"); 
            getComponent("param3").setDescription("VD: true");
            getComponent("param3").setVisible(false);
            ((HorizontalLayout)getComponent("param3").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);
            ((HorizontalLayout)getComponent("param3").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param3").getCaption());

            ((Label)((HorizontalLayout)getComponent("param4").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Độ dài dữ liệu"); 
            getComponent("param4").setDescription("VD: 100");
            getComponent("param5").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param5").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Định dạng"); 
            getComponent("param5").setDescription("VD: int,long,float,double,date,email,int>0<=100...<br/>"
                    + "VD1: int<br/>"
                    + "VD2: int>0<=100<br/>"
                    + "VD3: int>=0<br/>"
                    + "VD4: long<100<br/>"
                    + "VD5: float>=0.5<=23.5<br/>");
            ((Label)((HorizontalLayout)getComponent("param6").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Mô tả thêm");
            getComponent("param6").setDescription("VD: Tên đăng nhập hệ thống");

            getComponent("param7").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param7").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Sử dụng để tìm kiếm"); 
            getComponent("param7").setDescription("VD: true");
            getComponent("param7").setVisible(false);
            ((HorizontalLayout)getComponent("param7").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);
            ((HorizontalLayout)getComponent("param7").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param7").getCaption());

            ((Label)((HorizontalLayout)getComponent("param8").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Có phải password không"); 
            getComponent("param8").setDescription("VD: false");
            getComponent("param8").setVisible(false);
            ((HorizontalLayout)getComponent("param8").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);
            ((HorizontalLayout)getComponent("param8").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param8").getCaption());

            getComponent("param9").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param9").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Giới hạn dữ liệu tìm kiếm"); 
            getComponent("param9").setDescription("VD1: date:and_mandatory:100<br/>"
                    + "VD2: int:or_mandatory<br/>"
                    + "<b>date:</b> kiểu dữ liệu<br/>"
                    + "<b>and_mandatory:</b> bắt buộc nhập<br/>"
                    + "<b>or_mandatory:</b> bắt buộc nhập khi tất cả các trường bắt buộc nhập khác chưa nhập<br/>"
                    + "<b>100:</b> Từ ngày - đến ngày < 100");
            ((Label)((HorizontalLayout)getComponent("param10").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Có ẩn cột trên table không"); 
            getComponent("param10").setDescription("VD: false");
            getComponent("param10").setVisible(false);
            ((HorizontalLayout)getComponent("param10").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);                            
            ((HorizontalLayout)getComponent("param10").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param10").getCaption());

            getComponent("param11").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param11").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Giá trị tìm kiếm mặc định"); 
            getComponent("param11").setDescription("VD: admin");
            ((Label)((HorizontalLayout)getComponent("param12").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Hiển thị tại màn hình thêm mới");
            getComponent("param12").setDescription("VD: true");
            getComponent("param12").setVisible(false);
            ((HorizontalLayout)getComponent("param12").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);
            ((CheckBox)((HorizontalLayout)getComponent("param12").getParent()).getComponent(PMA_CHECKBOX2)).setValue(true);
            ((HorizontalLayout)getComponent("param12").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param12").getCaption());

            getComponent("param13").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param13").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Hiển thị tại màn hình sửa"); 
            getComponent("param13").setDescription("VD: true");
            getComponent("param13").setVisible(false);
            ((HorizontalLayout)getComponent("param13").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);
            ((CheckBox)((HorizontalLayout)getComponent("param13").getParent()).getComponent(PMA_CHECKBOX1)).setValue(true);
            ((HorizontalLayout)getComponent("param13").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param13").getCaption());

            ((Label)((HorizontalLayout)getComponent("param14").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Hiệu lực tại màn hình thêm mới"); 
            getComponent("param14").setDescription("VD: true");
            getComponent("param14").setVisible(false);
            ((HorizontalLayout)getComponent("param14").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);
            ((CheckBox)((HorizontalLayout)getComponent("param14").getParent()).getComponent(PMA_CHECKBOX2)).setValue(true);
            ((HorizontalLayout)getComponent("param14").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param14").getCaption());

            getComponent("param15").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param15").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Hiệu lực tại màn hình sửa"); 
            getComponent("param15").setDescription("VD: true");
            getComponent("param15").setVisible(false);
            ((HorizontalLayout)getComponent("param15").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);
            ((CheckBox)((HorizontalLayout)getComponent("param15").getParent()).getComponent(PMA_CHECKBOX1)).setValue(true);
            ((HorizontalLayout)getComponent("param15").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param15").getCaption());

            ((Label)((HorizontalLayout)getComponent("param16").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Giá trị nhập mặc định"); 
            getComponent("param16").setDescription("VD1: admin<br/>VD2: 01/01/2015");
            getComponent("param17").getParent().setVisible(false); 
            getComponent("param19").getParent().setVisible(false); 
            getComponent("param21").getParent().setVisible(false); 
            getComponent("param23").getParent().setVisible(false); 
            getComponent("param25").getParent().setVisible(false); 
            
            if(getMultiComponentLabel("v_com_param").getParent() != null)
                getMultiComponentLabel("v_com_param").getParent().setVisible(false);
        }        
    }
    
    /**
     * Hàm thiết lập component 
     * TEXT_FIELD, ONLY_IN_VIEW_FIELD
     * 
     * @param componentType loại component
     * @since 17/04/2015 HienDM
     */     
    private void setOnlyInView(String componentType) throws Exception {
        if (componentType.equals("OnlyInViewField")) {
            getComponent("param0").setDescription("VD: User.UserName");
            getComponent("param1").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param1").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Tên cột trong database"); 
            getComponent("param1").setDescription("VD: user_name");

            ((Label)((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Mô tả thêm");
            getComponent("param2").setDescription("VD: Tên đăng nhập hệ thống");

            getComponent("param3").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param3").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Sử dụng để tìm kiếm"); 
            getComponent("param3").setDescription("VD: true");
            getComponent("param3").setVisible(false);
            ((HorizontalLayout)getComponent("param3").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);
            ((HorizontalLayout)getComponent("param3").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param3").getCaption());

            getComponent("param4").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param4").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Giới hạn dữ liệu tìm kiếm"); 
            getComponent("param4").setDescription("VD1: date:and_mandatory:100<br/>"
                    + "VD2: int:or_mandatory<br/>"
                    + "<b>date:</b> kiểu dữ liệu<br/>"
                    + "<b>and_mandatory:</b> bắt buộc nhập<br/>"
                    + "<b>or_mandatory:</b> bắt buộc nhập khi tất cả các trường bắt buộc nhập khác chưa nhập<br/>"
                    + "<b>100:</b> Từ ngày - đến ngày < 100");
            
            ((Label)((HorizontalLayout)getComponent("param5").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Có ẩn cột trên table không"); 
            getComponent("param5").setDescription("VD: false");
            getComponent("param5").setVisible(false);
            ((HorizontalLayout)getComponent("param5").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);                            
            ((HorizontalLayout)getComponent("param5").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param5").getCaption());

            getComponent("param6").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param6").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Giá trị tìm kiếm mặc định"); 
            getComponent("param6").setDescription("VD1: admin");

            getComponent("param7").getParent().setVisible(false); 
            getComponent("param9").getParent().setVisible(false); 
            getComponent("param11").getParent().setVisible(false); 
            getComponent("param13").getParent().setVisible(false); 
            getComponent("param15").getParent().setVisible(false); 
            getComponent("param17").getParent().setVisible(false); 
            getComponent("param19").getParent().setVisible(false); 
            getComponent("param21").getParent().setVisible(false); 
            getComponent("param23").getParent().setVisible(false); 
            getComponent("param25").getParent().setVisible(false); 
            
            if(getMultiComponentLabel("v_com_param").getParent() != null)
                getMultiComponentLabel("v_com_param").getParent().setVisible(false);
        }        
    }    
    
    /**
     * Hàm thiết lập component 
     * DATE_FIELD, TIME_FIELD
     * 
     * @param componentType loại component
     * @since 17/04/2015 HienDM
     */     
    private void setDateComponent(String componentType) throws Exception {
        if (componentType.equals("DateField") || componentType.equals("TimeField")) {
            getComponent("param0").setDescription("VD: User.UserName");
            getComponent("param1").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param1").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Tên cột trong database"); 
            getComponent("param1").setDescription("VD: user_name");

            ((Label)((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Bắt buộc nhập"); 
            getComponent("param2").setDescription("VD: true");
            getComponent("param2").setVisible(false);
            ((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);
            ((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param2").getCaption());

            getComponent("param3").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param3").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Mô tả thêm"); 
            getComponent("param3").setDescription("VD: Tên đăng nhập hệ thống");

            ((Label)((HorizontalLayout)getComponent("param4").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Sử dụng để tìm kiếm"); 
            getComponent("param4").setDescription("VD: true");
            getComponent("param4").setVisible(false);
            ((HorizontalLayout)getComponent("param4").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);
            ((HorizontalLayout)getComponent("param4").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param4").getCaption());

            ((Label)((HorizontalLayout)getComponent("param5").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Giới hạn dữ liệu tìm kiếm"); 
            getComponent("param5").setDescription("VD1: date:and_mandatory:100<br/>"
                    + "VD2: int:or_mandatory<br/>"
                    + "<b>date:</b> kiểu dữ liệu<br/>"
                    + "<b>and_mandatory:</b> bắt buộc nhập<br/>"
                    + "<b>or_mandatory:</b> bắt buộc nhập khi tất cả các trường bắt buộc nhập khác chưa nhập<br/>"
                    + "<b>100:</b> Từ ngày - đến ngày < 100");
            getComponent("param6").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param6").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Có ẩn cột trên table không"); 
            getComponent("param6").setDescription("VD: false");
            getComponent("param6").setVisible(false);
            ((HorizontalLayout)getComponent("param6").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);                            
            ((HorizontalLayout)getComponent("param6").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param6").getCaption());

            ((Label)((HorizontalLayout)getComponent("param7").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Giá trị tìm kiếm mặc định"); 
            getComponent("param7").setDescription("VD1: 01/01/2015 23:59:59;2592000<br/>Chú thích: Từ ngày;khoảng thời gian(giây);số lần khoảng cách<br/>"
                                                + "VD2: 01/01/2015;2592000<br/>"
                                                + "VD3: sysdate;2592000<br/>"
                                                + "VD4: 01/01/2015;end_of_week<br/>"
                                                + "VD5: sysdate;end_of_week;3<br/>"                    
                                                + "VD6: sysdate;end_of_month<br/>"
                                                + "VD7: 01/01/2015;end_of_month;2<br/>"
                                                + "VD8: 01/01/2015;end_of_year;2<br/>");
            getComponent("param8").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param8").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Hiển thị tại màn hình thêm mới"); 
            getComponent("param8").setDescription("VD: true");
            getComponent("param8").setVisible(false);
            ((HorizontalLayout)getComponent("param8").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);
            ((CheckBox)((HorizontalLayout)getComponent("param8").getParent()).getComponent(PMA_CHECKBOX2)).setValue(true);
            ((HorizontalLayout)getComponent("param8").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param8").getCaption());

            ((Label)((HorizontalLayout)getComponent("param9").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Hiển thị tại màn hình sửa"); 
            getComponent("param9").setDescription("VD: true");
            getComponent("param9").setVisible(false);
            ((HorizontalLayout)getComponent("param9").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);
            ((CheckBox)((HorizontalLayout)getComponent("param9").getParent()).getComponent(PMA_CHECKBOX1)).setValue(true);
            ((HorizontalLayout)getComponent("param9").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param9").getCaption());

            getComponent("param10").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param10").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Hiệu lực tại màn hình thêm mới"); 
            getComponent("param10").setDescription("VD: true");
            getComponent("param10").setVisible(false);
            ((HorizontalLayout)getComponent("param10").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);
            ((CheckBox)((HorizontalLayout)getComponent("param10").getParent()).getComponent(PMA_CHECKBOX2)).setValue(true);
            ((HorizontalLayout)getComponent("param10").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param10").getCaption());

            ((Label)((HorizontalLayout)getComponent("param11").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Hiệu lực tại màn hình sửa"); 
            getComponent("param11").setDescription("VD: true");
            getComponent("param11").setVisible(false);
            ((HorizontalLayout)getComponent("param11").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);
            ((CheckBox)((HorizontalLayout)getComponent("param11").getParent()).getComponent(PMA_CHECKBOX1)).setValue(true);
            ((HorizontalLayout)getComponent("param11").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param11").getCaption());

            getComponent("param12").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param12").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Giá trị nhập mặc định"); 
            getComponent("param12").setDescription("VD1: 01/01/2015<br/>VD2: 01/01/2015 23:59:59");
            
            getComponent("param13").getParent().setVisible(false);
            getComponent("param15").getParent().setVisible(false); 
            getComponent("param17").getParent().setVisible(false); 
            getComponent("param19").getParent().setVisible(false); 
            getComponent("param21").getParent().setVisible(false); 
            getComponent("param23").getParent().setVisible(false); 
            getComponent("param25").getParent().setVisible(false); 
            
            if(getMultiComponentLabel("v_com_param").getParent() != null)
                getMultiComponentLabel("v_com_param").getParent().setVisible(false);
        }        
    } 
    
    /**
     * Hàm thiết lập component 
     * SYSDATE
     * 
     * @param componentType loại component
     * @since 17/04/2015 HienDM
     */     
    private void setSysdateComponent(String componentType) throws Exception {
        if (componentType.equals("SysDate")) {
            getComponent("param0").setDescription("VD: User.UserName");
            getComponent("param1").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param1").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Tên cột trong database"); 
            getComponent("param1").setDescription("VD: user_name");
            ((Label)((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Sử dụng để tìm kiếm"); 
            getComponent("param2").setDescription("VD: true");
            getComponent("param2").setVisible(false);
            ((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);
            ((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param2").getCaption());

            getComponent("param3").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param3").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Giới hạn dữ liệu tìm kiếm"); 
            getComponent("param3").setDescription("VD1: date:and_mandatory:100<br/>"
                    + "VD2: int:or_mandatory<br/>"
                    + "<b>date:</b> kiểu dữ liệu<br/>"
                    + "<b>and_mandatory:</b> bắt buộc nhập<br/>"
                    + "<b>or_mandatory:</b> bắt buộc nhập khi tất cả các trường bắt buộc nhập khác chưa nhập<br/>"
                    + "<b>100:</b> Từ ngày - đến ngày < 100");
            ((Label)((HorizontalLayout)getComponent("param4").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Có ẩn cột trên table không");
            getComponent("param4").setDescription("VD: false");
            getComponent("param4").setVisible(false);
            ((HorizontalLayout)getComponent("param4").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);
            ((HorizontalLayout)getComponent("param4").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param4").getCaption());

            getComponent("param5").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param5").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Giá trị tìm kiếm mặc định"); 
            getComponent("param5").setDescription("VD1: 01/01/2015 23:59:59;2592000<br/>Chú thích: Từ ngày;khoảng thời gian(giây);số lần khoảng cách<br/>"
                                                + "VD2: 01/01/2015;2592000<br/>"
                                                + "VD3: sysdate;2592000<br/>"
                                                + "VD4: 01/01/2015;end_of_week<br/>"
                                                + "VD5: sysdate;end_of_week;3<br/>"                    
                                                + "VD6: sysdate;end_of_month<br/>"
                                                + "VD7: 01/01/2015;end_of_month;2<br/>"
                                                + "VD8: 01/01/2015;end_of_year;2<br/>");
            getComponent("param6").setVisible(false);getComponentLabel("param6").setVisible(false);
            ((HorizontalLayout)getComponent("param6").getParent()).addComponent(emptyLayout1);

            getComponent("param7").getParent().setVisible(false); 
            getComponent("param9").getParent().setVisible(false); 
            getComponent("param11").getParent().setVisible(false); 
            getComponent("param13").getParent().setVisible(false); 
            getComponent("param15").getParent().setVisible(false); 
            getComponent("param17").getParent().setVisible(false); 
            getComponent("param19").getParent().setVisible(false); 
            getComponent("param21").getParent().setVisible(false); 
            getComponent("param23").getParent().setVisible(false); 
            getComponent("param25").getParent().setVisible(false); 
            
            if(getMultiComponentLabel("v_com_param").getParent() != null)
                getMultiComponentLabel("v_com_param").getParent().setVisible(false);
        }
    }    
    
    /**
     * Hàm thiết lập component 
     * LOGIN_USER
     * 
     * @param componentType loại component
     * @since 17/04/2015 HienDM
     */     
    private void setLoginUserComponent(String componentType) throws Exception {
        if (componentType.equals("LoginUser")) {
            getComponent("param0").setDescription("VD: User.UserName");
            getComponent("param1").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param1").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Tên cột trong database"); 
            getComponent("param1").setDescription("VD: user_name");
            ((Label)((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Sử dụng để tìm kiếm"); 
            getComponent("param2").setDescription("VD: true");
            getComponent("param2").setVisible(false);
            ((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);
            ((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param2").getCaption());

            getComponent("param3").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param3").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Giới hạn dữ liệu tìm kiếm"); 
            getComponent("param3").setDescription("VD1: date:and_mandatory:100<br/>"
                    + "VD2: int:or_mandatory<br/>"
                    + "<b>date:</b> kiểu dữ liệu<br/>"
                    + "<b>and_mandatory:</b> bắt buộc nhập<br/>"
                    + "<b>or_mandatory:</b> bắt buộc nhập khi tất cả các trường bắt buộc nhập khác chưa nhập<br/>"
                    + "<b>100:</b> Từ ngày - đến ngày < 100");
            ((Label)((HorizontalLayout)getComponent("param4").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Có ẩn cột trên table không");
            getComponent("param4").setDescription("VD: false");
            getComponent("param4").setVisible(false);
            ((HorizontalLayout)getComponent("param4").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);
            ((HorizontalLayout)getComponent("param4").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param4").getCaption());

            getComponent("param5").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param5").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Giá trị tìm kiếm mặc định"); 
            getComponent("param5").setDescription("VD: admin");
            ((Label)((HorizontalLayout)getComponent("param6").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Chỉ xem bản ghi của mình"); 
            getComponent("param6").setDescription("VD: false");
            getComponent("param6").setVisible(false);
            ((HorizontalLayout)getComponent("param6").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);
            ((HorizontalLayout)getComponent("param6").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param6").getCaption());

            getComponent("param7").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param7").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Chỉ sửa bản ghi của mình"); 
            getComponent("param7").setDescription("VD: false");
            getComponent("param7").setVisible(false);
            ((HorizontalLayout)getComponent("param7").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);
            ((HorizontalLayout)getComponent("param7").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param7").getCaption());

            ((Label)((HorizontalLayout)getComponent("param8").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Lọc dữ liệu theo phòng ban"); 
            ComboBox cbo1 = (ComboBox)((HorizontalLayout)getComponent("param8").getParent()).getComponent(PMA_COMBOBOX2);
            cbo1.addItem("0");
            cbo1.setItemCaption("0", "Trong tất cả phòng ban");
            cbo1.addItem("1");
            cbo1.setItemCaption("1", "Trong phòng ban người đăng nhập");
            cbo1.addItem("2");
            cbo1.setItemCaption("2", "Từ phòng ban người đăng nhập trở xuống");
            getComponent("param8").setVisible(false);
            cbo1.setVisible(true);
            cbo1.setValue("0");
            cbo1.setCaption(getComponent("param8").getCaption());

            getComponent("param9").getParent().setVisible(false); 
            getComponent("param11").getParent().setVisible(false); 
            getComponent("param13").getParent().setVisible(false); 
            getComponent("param15").getParent().setVisible(false); 
            getComponent("param17").getParent().setVisible(false); 
            getComponent("param19").getParent().setVisible(false); 
            getComponent("param21").getParent().setVisible(false); 
            getComponent("param23").getParent().setVisible(false); 
            getComponent("param25").getParent().setVisible(false); 
             
            if(getMultiComponentLabel("v_com_param").getParent() != null)
                getMultiComponentLabel("v_com_param").getParent().setVisible(false);
        }
    }        
    
    /**
     * Hàm thiết lập component 
     * CHECKBOX
     * 
     * @param componentType loại component
     * @since 17/04/2015 HienDM
     */     
    private void setCheckBoxComponent(String componentType) throws Exception {
        if (componentType.equals("CheckBox")) {
            getComponent("param0").setDescription("VD: User.UserName");
            getComponent("param1").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param1").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Tên cột trong database"); 
            getComponent("param1").setDescription("VD: user_name");

            ((Label)((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Bắt buộc nhập"); 
            getComponent("param2").setDescription("VD: true");
            getComponent("param2").setVisible(false);
            ((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);
            ((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param2").getCaption());

            getComponent("param3").getParent().setVisible(true);
            ((Label)((HorizontalLayout)getComponent("param3").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Mô tả thêm"); 
            getComponent("param3").setDescription("VD: Tên đăng nhập hệ thống");

            ((Label)((HorizontalLayout)getComponent("param4").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Sử dụng để tìm kiếm"); 
            getComponent("param4").setDescription("VD: true");
            getComponent("param4").setVisible(false);
            ((HorizontalLayout)getComponent("param4").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);
            ((HorizontalLayout)getComponent("param4").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param4").getCaption());

            getComponent("param5").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param5").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Giới hạn dữ liệu tìm kiếm"); 
            getComponent("param5").setDescription("VD1: date:and_mandatory:100<br/>"
                    + "VD2: int:or_mandatory<br/>"
                    + "<b>date:</b> kiểu dữ liệu<br/>"
                    + "<b>and_mandatory:</b> bắt buộc nhập<br/>"
                    + "<b>or_mandatory:</b> bắt buộc nhập khi tất cả các trường bắt buộc nhập khác chưa nhập<br/>"
                    + "<b>100:</b> Từ ngày - đến ngày < 100");
            ((Label)((HorizontalLayout)getComponent("param6").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Có ẩn cột trên table không"); 
            getComponent("param6").setDescription("VD: false");
            getComponent("param6").setVisible(false);
            ((HorizontalLayout)getComponent("param6").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);                            
            ((HorizontalLayout)getComponent("param6").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param6").getCaption());

            getComponent("param7").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param7").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Giá trị tìm kiếm mặc định"); 
            getComponent("param7").setDescription("VD: true");
            ((Label)((HorizontalLayout)getComponent("param8").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Hiển thị tại màn hình thêm mới"); 
            getComponent("param8").setDescription("VD: true");
            getComponent("param8").setVisible(false);
            ((HorizontalLayout)getComponent("param8").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);
            ((CheckBox)((HorizontalLayout)getComponent("param8").getParent()).getComponent(PMA_CHECKBOX2)).setValue(true);
            ((HorizontalLayout)getComponent("param8").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param8").getCaption());

            getComponent("param9").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param9").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Hiển thị tại màn hình sửa"); 
            getComponent("param9").setDescription("VD: true");
            getComponent("param9").setVisible(false);
            ((HorizontalLayout)getComponent("param9").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);
            ((CheckBox)((HorizontalLayout)getComponent("param9").getParent()).getComponent(PMA_CHECKBOX1)).setValue(true);
            ((HorizontalLayout)getComponent("param9").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param9").getCaption());

            ((Label)((HorizontalLayout)getComponent("param10").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Hiệu lực tại màn hình thêm mới"); 
            getComponent("param10").setDescription("VD: true");
            getComponent("param10").setVisible(false);
            ((HorizontalLayout)getComponent("param10").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);
            ((CheckBox)((HorizontalLayout)getComponent("param10").getParent()).getComponent(PMA_CHECKBOX2)).setValue(true);
            ((HorizontalLayout)getComponent("param10").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param10").getCaption());

            getComponent("param11").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param11").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Hiệu lực tại màn hình sửa"); 
            getComponent("param11").setDescription("VD: true");
            getComponent("param11").setVisible(false);
            ((HorizontalLayout)getComponent("param11").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);
            ((CheckBox)((HorizontalLayout)getComponent("param11").getParent()).getComponent(PMA_CHECKBOX1)).setValue(true);
            ((HorizontalLayout)getComponent("param11").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param11").getCaption());

            ((Label)((HorizontalLayout)getComponent("param12").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Mô tả trạng thái Enable"); 
            getComponent("param12").setDescription("VD: Hoạt động");
            getComponent("param13").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param13").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Mô tả trạng thái Disable"); 
            getComponent("param13").setDescription("VD: Khóa");

            ((Label)((HorizontalLayout)getComponent("param14").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Giá trị mặc định"); 
            getComponent("param14").setDescription("VD: true");
            getComponent("param14").setVisible(false);
            ((HorizontalLayout)getComponent("param14").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);
            ((CheckBox)((HorizontalLayout)getComponent("param14").getParent()).getComponent(PMA_CHECKBOX2)).setValue(true);
            ((HorizontalLayout)getComponent("param14").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param14").getCaption());

            getComponent("param15").getParent().setVisible(false); 
            getComponent("param17").getParent().setVisible(false); 
            getComponent("param19").getParent().setVisible(false); 
            getComponent("param21").getParent().setVisible(false); 
            getComponent("param23").getParent().setVisible(false); 
            getComponent("param25").getParent().setVisible(false); 
             
            if(getMultiComponentLabel("v_com_param").getParent() != null)
                getMultiComponentLabel("v_com_param").getParent().setVisible(false);
        }
    }            
    
    /**
     * Hàm thiết lập component 
     * COMBOBOX
     * 
     * @param componentType loại component
     * @since 17/04/2015 HienDM
     */     
    private void setComboBoxComponent(String componentType) throws Exception {
        if (componentType.equals("ComboBox")) {
            getComponent("param0").setDescription("VD: User.GroupId");
            getComponent("param1").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param1").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Tên cột trong database"); 
            getComponent("param1").setDescription("VD: group_id");
            ((Label)((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Loại dữ liệu"); 
            getComponent("param2").setDescription("VD: string");
            ComboBox cbo1 = (ComboBox)((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_COMBOBOX2);
            cbo1.addItem("string");
            cbo1.addItem("int");
            cbo1.addItem("long");
            cbo1.addItem("float");
            cbo1.addItem("double");
            cbo1.setValue("int");
            getComponent("param2").setVisible(false);
            cbo1.setVisible(true);
            cbo1.setCaption(getComponent("param2").getCaption());

            getComponent("param3").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param3").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Bắt buộc nhập"); 
            getComponent("param3").setDescription("VD: true");
            getComponent("param3").setVisible(false);
            ((HorizontalLayout)getComponent("param3").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);
            ((HorizontalLayout)getComponent("param3").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param3").getCaption());

            ((Label)((HorizontalLayout)getComponent("param4").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Độ dài dữ liệu"); 
            getComponent("param4").setDescription("VD: 100");
            getComponent("param5").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param5").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Định dạng"); 
            getComponent("param5").setDescription("VD: int,long,float,double,date,email,int>0<=100..."
                    + "VD1: int<br/>"
                    + "VD2: int>0<=100<br/>"
                    + "VD3: int>=0<br/>"
                    + "VD4: long<100<br/>"
                    + "VD5: float>=0.5<=23.5<br/>");
            ((Label)((HorizontalLayout)getComponent("param6").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Mô tả thêm"); 
            getComponent("param6").setDescription("VD: Tên đăng nhập hệ thống");

            getComponent("param7").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param7").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Sử dụng để tìm kiếm"); 
            getComponent("param7").setDescription("VD: true");
            getComponent("param7").setVisible(false);
            ((HorizontalLayout)getComponent("param7").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);
            ((HorizontalLayout)getComponent("param7").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param7").getCaption());

            ((Label)((HorizontalLayout)getComponent("param8").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Giới hạn dữ liệu tìm kiếm"); 
            getComponent("param8").setDescription("VD1: date:and_mandatory:100<br/>"
                    + "VD2: int:or_mandatory<br/>"
                    + "<b>date:</b> kiểu dữ liệu<br/>"
                    + "<b>and_mandatory:</b> bắt buộc nhập<br/>"
                    + "<b>or_mandatory:</b> bắt buộc nhập khi tất cả các trường bắt buộc nhập khác chưa nhập<br/>"
                    + "<b>100:</b> Từ ngày - đến ngày < 100");
            getComponent("param9").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param9").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Có ẩn cột trên table không"); 
            getComponent("param9").setDescription("VD: false");
            getComponent("param9").setVisible(false);
            ((HorizontalLayout)getComponent("param9").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);                            
            ((HorizontalLayout)getComponent("param9").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param9").getCaption());

            ((Label)((HorizontalLayout)getComponent("param10").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Giá trị tìm kiếm mặc định"); 
            getComponent("param10").setDescription("VD: 1");
            getComponent("param11").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param11").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Hiển thị tại màn hình thêm mới"); 
            getComponent("param11").setDescription("VD: true");
            getComponent("param11").setVisible(false);
            ((HorizontalLayout)getComponent("param11").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);
            ((CheckBox)((HorizontalLayout)getComponent("param11").getParent()).getComponent(PMA_CHECKBOX1)).setValue(true);
            ((HorizontalLayout)getComponent("param11").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param11").getCaption());

            ((Label)((HorizontalLayout)getComponent("param12").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Hiển thị tại màn hình sửa"); 
            getComponent("param12").setDescription("VD: true");
            getComponent("param12").setVisible(false);
            ((HorizontalLayout)getComponent("param12").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);
            ((CheckBox)((HorizontalLayout)getComponent("param12").getParent()).getComponent(PMA_CHECKBOX2)).setValue(true);
            ((HorizontalLayout)getComponent("param12").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param12").getCaption());

            getComponent("param13").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param13").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Hiệu lực tại màn hình thêm mới"); 
            getComponent("param13").setDescription("VD: true");
            getComponent("param13").setVisible(false);
            ((HorizontalLayout)getComponent("param13").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);
            ((CheckBox)((HorizontalLayout)getComponent("param13").getParent()).getComponent(PMA_CHECKBOX1)).setValue(true);
            ((HorizontalLayout)getComponent("param13").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param13").getCaption());

            ((Label)((HorizontalLayout)getComponent("param14").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Hiệu lực tại màn hình sửa"); 
            getComponent("param14").setDescription("VD: true");
            getComponent("param14").setVisible(false);
            ((HorizontalLayout)getComponent("param14").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);
            ((CheckBox)((HorizontalLayout)getComponent("param14").getParent()).getComponent(PMA_CHECKBOX2)).setValue(true);
            ((HorizontalLayout)getComponent("param14").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param14").getCaption());

            getComponent("param15").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param15").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Tên Table của combobox"); 
            getComponent("param15").setDescription("VD: sm_group ");
            ((Label)((HorizontalLayout)getComponent("param16").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Trường id của combobox"); 
            getComponent("param16").setDescription("VD: group_id ");
            getComponent("param17").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param17").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Kiểu dữ liệu trường id"); 
            ComboBox cbo2 = (ComboBox)((HorizontalLayout)getComponent("param17").getParent()).getComponent(PMA_COMBOBOX1);
            cbo2.addItem("string");
            cbo2.addItem("int");
            cbo2.addItem("long");
            cbo2.addItem("float");
            cbo2.addItem("double");
            cbo2.setValue("int");
            getComponent("param17").setVisible(false);
            cbo2.setVisible(true);
            cbo2.setCaption(getComponent("param17").getCaption());

            ((Label)((HorizontalLayout)getComponent("param18").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Trường name của combobox"); 
            getComponent("param18").setDescription("VD: group_name ");
            getComponent("param19").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param19").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Giá trị mặc định"); 
            getComponent("param19").setDescription("VD: 15 ");
            ((Label)((HorizontalLayout)getComponent("param20").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Mô tả mặc định"); 
            getComponent("param20").setDescription("VD: Phòng bán hàng ");
            getComponent("param21").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param21").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Refress sau khi thêm/sửa/xóa"); 
            getComponent("param21").setDescription("Thường chỉ dùng khi trường này được thiết kế <br/>"
                    + "giống như một parent id");
            getComponent("param21").setVisible(false);
            CheckBox che1 = (CheckBox)((HorizontalLayout)getComponent("param21").getParent()).getComponent(PMA_CHECKBOX1);
            che1.setVisible(true);
            che1.setCaption(getComponent("param21").getCaption());
            che1.setDescription(getComponent("param21").getDescription());

            ((Label)((HorizontalLayout)getComponent("param22").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Đa ngôn ngữ"); 
            getComponent("param22").setDescription("VD: true");
            getComponent("param22").setVisible(false);
            ((HorizontalLayout)getComponent("param22").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);
            ((HorizontalLayout)getComponent("param22").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param22").getCaption());                            

            getComponent("param23").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param23").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Filter theo trường"); 
            getComponent("param23").setDescription("VD: company_id ");
            ((Label)((HorizontalLayout)getComponent("param24").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Trường nối với Filter"); 
            getComponent("param24").setDescription("VD: company_id ");
            
            getComponent("param25").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param25").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Bổ sung Query"); 
            getComponent("param25").setDescription("VD: and user_id = ? and group_id = ?<br/> and is_enable = 1 and description = ? ");            
            
            if(getMultiComponentLabel("v_com_param").getParent() != null) {
                getMultiComponentLabel("v_com_param").setValue("Tham số query");
                getMultiComponentLabel("v_com_param").getParent().setVisible(true);
            }
        }
    }
    
    /**
     * Hàm thiết lập component 
     * CONSTANT_COMBOBOX
     * 
     * @param componentType loại component
     * @since 17/04/2015 HienDM
     */     
    private void setConstantComboBoxComponent(String componentType) throws Exception {
        if (componentType.equals("ConstantComboBox")) {
            getComponent("param0").setDescription("VD: User.GroupId");
            getComponent("param1").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param1").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Tên cột trong database"); 
            getComponent("param1").setDescription("VD: group_id");
            ((Label)((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Loại dữ liệu"); 
            getComponent("param2").setDescription("VD: string");
            ComboBox cbo1 = (ComboBox)((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_COMBOBOX2);
            cbo1.addItem("string");
            cbo1.addItem("int");
            cbo1.addItem("long");
            cbo1.addItem("float");
            cbo1.addItem("double");
            cbo1.setValue("int");
            getComponent("param2").setVisible(false);
            cbo1.setVisible(true);
            cbo1.setCaption(getComponent("param2").getCaption());

            getComponent("param3").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param3").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Bắt buộc nhập"); 
            getComponent("param3").setDescription("VD: true");
            getComponent("param3").setVisible(false);
            ((HorizontalLayout)getComponent("param3").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);
            ((HorizontalLayout)getComponent("param3").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param3").getCaption());

            ((Label)((HorizontalLayout)getComponent("param4").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Độ dài dữ liệu"); 
            getComponent("param4").setDescription("VD: 100");
            getComponent("param5").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param5").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Định dạng"); 
            getComponent("param5").setDescription("VD: int,long,float,double,date,email,int>0<=100..."
                    + "VD1: int<br/>"
                    + "VD2: int>0<=100<br/>"
                    + "VD3: int>=0<br/>"
                    + "VD4: long<100<br/>"
                    + "VD5: float>=0.5<=23.5<br/>");
            ((Label)((HorizontalLayout)getComponent("param6").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Mô tả thêm"); 
            getComponent("param6").setDescription("VD: Tên đăng nhập hệ thống");

            getComponent("param7").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param7").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Sử dụng để tìm kiếm"); 
            getComponent("param7").setDescription("VD: true");
            getComponent("param7").setVisible(false);
            ((HorizontalLayout)getComponent("param7").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);
            ((HorizontalLayout)getComponent("param7").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param7").getCaption());

            ((Label)((HorizontalLayout)getComponent("param8").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Giới hạn dữ liệu tìm kiếm"); 
            getComponent("param8").setDescription("VD1: date:and_mandatory:100<br/>"
                    + "VD2: int:or_mandatory<br/>"
                    + "<b>date:</b> kiểu dữ liệu<br/>"
                    + "<b>and_mandatory:</b> bắt buộc nhập<br/>"
                    + "<b>or_mandatory:</b> bắt buộc nhập khi tất cả các trường bắt buộc nhập khác chưa nhập<br/>"
                    + "<b>100:</b> Từ ngày - đến ngày < 100");
            getComponent("param9").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param9").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Có ẩn cột trên table không"); 
            getComponent("param9").setDescription("VD: false");
            getComponent("param9").setVisible(false);
            ((HorizontalLayout)getComponent("param9").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);                            
            ((HorizontalLayout)getComponent("param9").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param9").getCaption());

            ((Label)((HorizontalLayout)getComponent("param10").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Giá trị tìm kiếm mặc định"); 
            getComponent("param10").setDescription("VD: 1");
            getComponent("param11").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param11").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Hiển thị tại màn hình thêm mới"); 
            getComponent("param11").setDescription("VD: true");
            getComponent("param11").setVisible(false);
            ((HorizontalLayout)getComponent("param11").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);
            ((CheckBox)((HorizontalLayout)getComponent("param11").getParent()).getComponent(PMA_CHECKBOX1)).setValue(true);
            ((HorizontalLayout)getComponent("param11").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param11").getCaption());

            ((Label)((HorizontalLayout)getComponent("param12").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Hiển thị tại màn hình sửa"); 
            getComponent("param12").setDescription("VD: true");
            getComponent("param12").setVisible(false);
            ((HorizontalLayout)getComponent("param12").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);
            ((CheckBox)((HorizontalLayout)getComponent("param12").getParent()).getComponent(PMA_CHECKBOX2)).setValue(true);
            ((HorizontalLayout)getComponent("param12").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param12").getCaption());

            getComponent("param13").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param13").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Hiệu lực tại màn hình thêm mới"); 
            getComponent("param13").setDescription("VD: true");
            getComponent("param13").setVisible(false);
            ((HorizontalLayout)getComponent("param13").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);
            ((CheckBox)((HorizontalLayout)getComponent("param13").getParent()).getComponent(PMA_CHECKBOX1)).setValue(true);
            ((HorizontalLayout)getComponent("param13").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param13").getCaption());

            ((Label)((HorizontalLayout)getComponent("param14").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Hiệu lực tại màn hình sửa"); 
            getComponent("param14").setDescription("VD: true");
            getComponent("param14").setVisible(false);
            ((HorizontalLayout)getComponent("param14").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);
            ((CheckBox)((HorizontalLayout)getComponent("param14").getParent()).getComponent(PMA_CHECKBOX2)).setValue(true);
            ((HorizontalLayout)getComponent("param14").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param14").getCaption());
            getComponent("param15").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param15").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Giá trị mặc định"); 
            getComponent("param15").setDescription("VD: 1 ");
            ((Label)((HorizontalLayout)getComponent("param16").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Mô tả mặc định"); 
            getComponent("param16").setDescription("VD: Mới tạo ");
            
            getComponent("param17").getParent().setVisible(false); 
            getComponent("param19").getParent().setVisible(false); 
            getComponent("param21").getParent().setVisible(false); 
            getComponent("param23").getParent().setVisible(false); 
            getComponent("param25").getParent().setVisible(false); 
             
            if(getMultiComponentLabel("v_com_param").getParent() != null) {
                getMultiComponentLabel("v_com_param").setValue("Dữ liệu Combobox");
                getMultiComponentLabel("v_com_param").getParent().setVisible(true);            
            }
        }
    }    
    
    /**
     * Hàm thiết lập component 
     * SINGLE_POPUP
     * 
     * @param componentType loại component
     * @since 17/04/2015 HienDM
     */     
    private void setSinglePopupComponent(String componentType) throws Exception {
        if (componentType.equals("SinglePopup")) {
            getComponent("param0").setDescription("VD: User.GroupId");
            getComponent("param1").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param1").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Tên cột trong database"); 
            getComponent("param1").setDescription("VD: group_id");
            ((Label)((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Loại dữ liệu"); 
            getComponent("param2").setDescription("VD: string");
            ComboBox cbo1 = (ComboBox)((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_COMBOBOX2);
            cbo1.addItem("string");
            cbo1.addItem("int");
            cbo1.addItem("long");
            cbo1.addItem("float");
            cbo1.addItem("double");
            cbo1.setValue("int");
            getComponent("param2").setVisible(false);
            cbo1.setVisible(true);
            cbo1.setCaption(getComponent("param2").getCaption());

            getComponent("param3").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param3").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Bắt buộc nhập"); 
            getComponent("param3").setDescription("VD: true");
            getComponent("param3").setVisible(false);
            ((HorizontalLayout)getComponent("param3").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);
            ((HorizontalLayout)getComponent("param3").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param3").getCaption());

            ((Label)((HorizontalLayout)getComponent("param4").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Độ dài dữ liệu"); 
            getComponent("param4").setDescription("VD: 100");
            getComponent("param5").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param5").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Định dạng"); 
            getComponent("param5").setDescription("VD: int,long,float,double,date,email,int>0<=100..."
                    + "VD1: int<br/>"
                    + "VD2: int>0<=100<br/>"
                    + "VD3: int>=0<br/>"
                    + "VD4: long<100<br/>"
                    + "VD5: float>=0.5<=23.5<br/>");
            ((Label)((HorizontalLayout)getComponent("param6").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Mô tả thêm"); 
            getComponent("param6").setDescription("VD: Tên đăng nhập hệ thống");

            getComponent("param7").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param7").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Sử dụng để tìm kiếm"); 
            getComponent("param7").setDescription("VD: true");
            getComponent("param7").setVisible(false);
            ((HorizontalLayout)getComponent("param7").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);
            ((HorizontalLayout)getComponent("param7").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param7").getCaption());

            ((Label)((HorizontalLayout)getComponent("param8").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Giới hạn dữ liệu tìm kiếm"); 
            getComponent("param8").setDescription("VD1: date:and_mandatory:100<br/>"
                    + "VD2: int:or_mandatory<br/>"
                    + "<b>date:</b> kiểu dữ liệu<br/>"
                    + "<b>and_mandatory:</b> bắt buộc nhập<br/>"
                    + "<b>or_mandatory:</b> bắt buộc nhập khi tất cả các trường bắt buộc nhập khác chưa nhập<br/>"
                    + "<b>100:</b> Từ ngày - đến ngày < 100");
            getComponent("param9").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param9").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Có ẩn cột trên table không"); 
            getComponent("param9").setDescription("VD: false");
            getComponent("param9").setVisible(false);
            ((HorizontalLayout)getComponent("param9").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);                            
            ((HorizontalLayout)getComponent("param9").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param9").getCaption());

            ((Label)((HorizontalLayout)getComponent("param10").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Giá trị tìm kiếm mặc định"); 
            getComponent("param10").setDescription("VD: 1");
            getComponent("param11").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param11").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Hiển thị tại màn hình thêm mới"); 
            getComponent("param11").setDescription("VD: true");
            getComponent("param11").setVisible(false);
            ((HorizontalLayout)getComponent("param11").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);
            ((CheckBox)((HorizontalLayout)getComponent("param11").getParent()).getComponent(PMA_CHECKBOX1)).setValue(true);
            ((HorizontalLayout)getComponent("param11").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param11").getCaption());

            ((Label)((HorizontalLayout)getComponent("param12").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Hiển thị tại màn hình sửa"); 
            getComponent("param12").setDescription("VD: true");
            getComponent("param12").setVisible(false);
            ((HorizontalLayout)getComponent("param12").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);
            ((CheckBox)((HorizontalLayout)getComponent("param12").getParent()).getComponent(PMA_CHECKBOX2)).setValue(true);
            ((HorizontalLayout)getComponent("param12").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param12").getCaption());

            getComponent("param13").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param13").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Hiệu lực tại màn hình thêm mới"); 
            getComponent("param13").setDescription("VD: true");
            getComponent("param13").setVisible(false);
            ((HorizontalLayout)getComponent("param13").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);
            ((CheckBox)((HorizontalLayout)getComponent("param13").getParent()).getComponent(PMA_CHECKBOX1)).setValue(true);
            ((HorizontalLayout)getComponent("param13").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param13").getCaption());

            ((Label)((HorizontalLayout)getComponent("param14").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Hiệu lực tại màn hình sửa"); 
            getComponent("param14").setDescription("VD: true");
            getComponent("param14").setVisible(false);
            ((HorizontalLayout)getComponent("param14").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);
            ((CheckBox)((HorizontalLayout)getComponent("param14").getParent()).getComponent(PMA_CHECKBOX2)).setValue(true);
            ((HorizontalLayout)getComponent("param14").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param14").getCaption());

            getComponent("param15").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param15").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Tên popup"); 
            ComboBox cbo2 = (ComboBox)((HorizontalLayout)getComponent("param15").getParent()).getComponent(PMA_COMBOBOX1);
            BuildConfigDAO buildDao = new BuildConfigDAO();
            List<Map> lstSinglePopup = buildDao.getSinglePopup();
            if(lstSinglePopup != null && !lstSinglePopup.isEmpty()){
                for(int i=0; i < lstSinglePopup.size(); i++) {
                    cbo2.addItem(lstSinglePopup.get(i).get("config_file").toString());
                    cbo2.setItemCaption(lstSinglePopup.get(i).get("config_file").toString(), 
                            lstSinglePopup.get(i).get("config_file").toString());
                }
            }
            getComponent("param15").setVisible(false);
            cbo2.setVisible(true);
            cbo2.setCaption(getComponent("param15").getCaption());

            ((Label)((HorizontalLayout)getComponent("param16").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Số cột trình bày"); 
            getComponent("param16").setDescription("VD: 2 ");
            getComponent("param17").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param17").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Giá trị mặc định"); 
            getComponent("param17").setDescription("VD: 15 ");
            ((Label)((HorizontalLayout)getComponent("param18").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Mô tả mặc định"); 
            getComponent("param18").setDescription("VD: Phòng bán hàng ");
            getComponent("param19").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param19").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Trường id của combobox"); 
            getComponent("param19").setDescription("VD: group_id ");
            ((Label)((HorizontalLayout)getComponent("param20").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Trường name của combobox"); 
            getComponent("param20").setDescription("VD: group_name ");
            getComponent("param21").getParent().setVisible(true);
            ((Label)((HorizontalLayout)getComponent("param21").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Tên Table của combobox"); 
            getComponent("param21").setDescription("VD: sm_group ");
            ((Label)((HorizontalLayout)getComponent("param22").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Filter theo trường"); 
            getComponent("param22").setDescription("VD: company_id ");
            getComponent("param23").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param23").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Trường nối với Filter"); 
            getComponent("param23").setDescription("VD: company_id ");

            getComponent("param24").setVisible(false);getComponentLabel("param24").setVisible(false);
            ((HorizontalLayout)getComponent("param24").getParent()).addComponent(emptyLayout1);
            
            getComponent("param25").getParent().setVisible(false); 
            
            if(getMultiComponentLabel("v_com_param").getParent() != null)
                getMultiComponentLabel("v_com_param").getParent().setVisible(false);
        }
    }
    
    /**
     * Hàm thiết lập component 
     * MULTI_POPUP
     * 
     * @param componentType loại component
     * @since 17/04/2015 HienDM
     */     
    private void setMultiPopupComponent(String componentType) throws Exception {
        if (componentType.equals("MultiPopup")) {
            getComponent("param0").setDescription("VD: User.GroupId");

            getComponent("param1").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param1").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Bắt buộc nhập"); 
            getComponent("param1").setDescription("VD: true");
            getComponent("param1").setVisible(false);
            ((HorizontalLayout)getComponent("param1").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);
            ((HorizontalLayout)getComponent("param1").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param1").getCaption());

            ((Label)((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Tên popup");
            ComboBox cbo2 = (ComboBox)((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_COMBOBOX2);
            BuildConfigDAO buildDao = new BuildConfigDAO();
            List<Map> lstMultiPopup = buildDao.getMultiPopup();
            if(lstMultiPopup != null && !lstMultiPopup.isEmpty()){
                for(int i=0; i < lstMultiPopup.size(); i++) {
                    cbo2.addItem(lstMultiPopup.get(i).get("config_file").toString());
                    cbo2.setItemCaption(lstMultiPopup.get(i).get("config_file").toString(), 
                            lstMultiPopup.get(i).get("config_file").toString());
                }
            }
            getComponent("param2").setVisible(false);
            cbo2.setVisible(true);
            cbo2.setCaption(getComponent("param2").getCaption());

            getComponent("param3").getParent().setVisible(true);
            ((Label)((HorizontalLayout)getComponent("param3").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Số cột trình bày");
            getComponent("param3").setDescription("VD: 2 ");
            ((Label)((HorizontalLayout)getComponent("param4").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Tên Table nối");
            getComponent("param4").setDescription("VD: sm_role_menu ");
            getComponent("param5").getParent().setVisible(true);
            ((Label)((HorizontalLayout)getComponent("param5").getParent()).getComponent(PMA_NEWLABEL1)).setValue("ID table2(Popup)");
            getComponent("param5").setDescription("VD: id ");
            ((Label)((HorizontalLayout)getComponent("param6").getParent()).getComponent(PMA_NEWLABEL2)).setValue("ID table1(Parent)");
            getComponent("param6").setDescription("VD: role_id ");
            getComponent("param7").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param7").getParent()).getComponent(PMA_NEWLABEL1)).setValue("ID Table nối");
            getComponent("param7").setDescription("VD: menu_id ");
            ((Label)((HorizontalLayout)getComponent("param8").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Sequencee Table nối");
            getComponent("param8").setDescription("VD: sm_role_menu_seq ");
            getComponent("param9").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param9").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Filter theo trường");
            getComponent("param9").setDescription("VD: application_id ");
            ((Label)((HorizontalLayout)getComponent("param10").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Trường nối với Filter");
            getComponent("param10").setDescription("VD: application_id ");
            getComponent("param11").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param11").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Filter Table");
            getComponent("param11").setDescription("VD: sm_app_role ");

            getComponent("param12").setVisible(false);getComponentLabel("param12").setVisible(false);
            ((HorizontalLayout)getComponent("param12").getParent()).addComponent(emptyLayout1);

            getComponent("param13").getParent().setVisible(false); 
            getComponent("param15").getParent().setVisible(false); 
            getComponent("param17").getParent().setVisible(false); 
            getComponent("param19").getParent().setVisible(false); 
            getComponent("param21").getParent().setVisible(false); 
            getComponent("param23").getParent().setVisible(false); 
            getComponent("param25").getParent().setVisible(false); 
            
            if(getMultiComponentLabel("v_com_param").getParent() != null)
                getMultiComponentLabel("v_com_param").getParent().setVisible(false);
        }
    }
    
    /**
     * Hàm thiết lập component 
     * UPLOAD_FIELD
     * 
     * @param componentType loại component
     * @since 17/04/2015 HienDM
     */     
    private void setUploadComponent(String componentType) throws Exception {
        if (componentType.equals("UploadField")) {
            getComponent("param0").setDescription("VD: User.UserName");
            getComponent("param1").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param1").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Tên cột trong database"); 
            getComponent("param1").setDescription("VD: user_name");

            ((Label)((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Bắt buộc nhập"); 
            getComponent("param2").setDescription("VD: true");
            getComponent("param2").setVisible(false);
            ((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);
            ((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param2").getCaption());

            getComponent("param3").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param3").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Độ dài dữ liệu"); 
            getComponent("param3").setDescription("VD: 100");
            ((Label)((HorizontalLayout)getComponent("param4").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Mô tả thêm"); 
            getComponent("param4").setDescription("VD: Tên đăng nhập hệ thống");

            getComponent("param5").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param5").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Có ẩn cột trên table không"); 
            getComponent("param5").setDescription("VD: false");
            getComponent("param5").setVisible(false);
            ((HorizontalLayout)getComponent("param5").getParent()).getComponent(PMA_CHECKBOX1).setVisible(true);                            
            ((HorizontalLayout)getComponent("param5").getParent()).getComponent(PMA_CHECKBOX1).setCaption(getComponent("param5").getCaption());

            ((Label)((HorizontalLayout)getComponent("param6").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Thư mục chứa file"); 
            getComponent("param6").setDescription("VD: UserAction");
            
            getComponent("param7").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param7").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Kích thước tối đa(MB)"); 
            getComponent("param7").setDescription("VD: 5");

            getComponent("param8").setVisible(false);getComponentLabel("param8").setVisible(false);
            ((HorizontalLayout)getComponent("param8").getParent()).addComponent(emptyLayout1);
            
            getComponent("param9").getParent().setVisible(false); 
            getComponent("param11").getParent().setVisible(false); 
            getComponent("param13").getParent().setVisible(false); 
            getComponent("param15").getParent().setVisible(false); 
            getComponent("param17").getParent().setVisible(false); 
            getComponent("param19").getParent().setVisible(false); 
            getComponent("param21").getParent().setVisible(false); 
            getComponent("param23").getParent().setVisible(false); 
            getComponent("param25").getParent().setVisible(false); 
            
            if(getMultiComponentLabel("v_com_param").getParent() != null)
                getMultiComponentLabel("v_com_param").getParent().setVisible(false);
        }
    }    
    
    /**
     * Hàm thiết lập component 
     * MULTI_UPLOAD_FIELD
     * 
     * @param componentType loại component
     * @since 17/04/2015 HienDM
     */     
    private void setMultiUploadComponent(String componentType) throws Exception {
        if (componentType.equals("MultiUploadField")) {
            getComponent("param0").setDescription("VD: User.UserName");
            getComponent("param1").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param1").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Tên Table chứa file"); 
            getComponent("param1").setDescription("VD: user_name");

            getComponent("param2").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Bắt buộc nhập"); 
            getComponent("param2").setDescription("VD: true");
            getComponent("param2").setVisible(false);
            ((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);
            ((HorizontalLayout)getComponent("param2").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param2").getCaption());

            ((Label)((HorizontalLayout)getComponent("param3").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Mô tả thêm"); 
            getComponent("param3").setDescription("VD: Tên đăng nhập hệ thống");

            getComponent("param4").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param4").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Có ẩn cột trên table không"); 
            getComponent("param4").setDescription("VD: false");
            getComponent("param4").setVisible(false);
            ((HorizontalLayout)getComponent("param4").getParent()).getComponent(PMA_CHECKBOX2).setVisible(true);                            
            ((HorizontalLayout)getComponent("param4").getParent()).getComponent(PMA_CHECKBOX2).setCaption(getComponent("param4").getCaption());

            ((Label)((HorizontalLayout)getComponent("param5").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Thư mục chứa file"); 
            getComponent("param5").setDescription("VD: UserAction");

            ((Label)((HorizontalLayout)getComponent("param6").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Kích thước tối đa(MB)"); 
            getComponent("param6").setDescription("VD: 5");

            getComponent("param7").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param7").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Khóa chính Table file"); 
            getComponent("param7").setDescription("VD: attach_id");
            ((Label)((HorizontalLayout)getComponent("param8").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Khóa chính Table chính"); 
            getComponent("param8").setDescription("VD: user_id");
            getComponent("param9").getParent().setVisible(true); 
            ((Label)((HorizontalLayout)getComponent("param9").getParent()).getComponent(PMA_NEWLABEL1)).setValue("Khóa chính Table nối"); 
            getComponent("param9").setDescription("VD: UserAction");
            ((Label)((HorizontalLayout)getComponent("param10").getParent()).getComponent(PMA_NEWLABEL2)).setValue("Sequence của Table nối"); 
            getComponent("param10").setDescription("VD: sm_user_attach_seq");

            getComponent("param11").getParent().setVisible(false); 
            getComponent("param13").getParent().setVisible(false); 
            getComponent("param15").getParent().setVisible(false); 
            getComponent("param17").getParent().setVisible(false); 
            getComponent("param19").getParent().setVisible(false); 
            getComponent("param21").getParent().setVisible(false); 
            getComponent("param23").getParent().setVisible(false); 
            getComponent("param25").getParent().setVisible(false); 
            
            if(getMultiComponentLabel("v_com_param").getParent() != null)
                getMultiComponentLabel("v_com_param").getParent().setVisible(false);
        }
    }        
}
