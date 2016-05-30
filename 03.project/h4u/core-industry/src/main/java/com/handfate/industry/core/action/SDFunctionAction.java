/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.core.action;

import com.handfate.industry.core.MainUI;
import static com.handfate.industry.core.MainUI.mainLogger;
import com.handfate.industry.core.action.component.ConfirmationDialog;
import com.handfate.industry.core.dao.BuildConfigDAO;
import com.handfate.industry.core.util.EncryptDecryptUtils;
import com.handfate.industry.core.util.FileUtils;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.io.File;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author HienDM1
 */
public class SDFunctionAction extends BaseAction {

    private String configPath = System.getProperty("user.dir") + File.separator + "src"
            + File.separator + "main" + File.separator + "resources"
            + File.separator + "com" + File.separator + "handfate"
            + File.separator + "industry" + File.separator + "core"
            + File.separator + "config" + File.separator;
    
    private String functionConfigPath = System.getProperty("user.dir") + File.separator + "src"
            + File.separator + "main" + File.separator + "resources"
            + File.separator + "com" + File.separator + "handfate"
            + File.separator + "industry" + File.separator + "extend"
            + File.separator + "config" + File.separator;
    
    /**
     * Hàm khởi tạo giao diện
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI Vùng giao diện của chức năng
     * @return Giao diện sau khi khởi tạo
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khởi tạo tham số
        setTableName("sd_function");
        setIdColumnName("id");
        setPageLength(25);
        setTableType(INT_TREE_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("ord");
        setParentColumnName("parent_id");
        setRootId("0");
        setSortAscending(true);
        setSequenceName("sm_menu_seq");
        setQueryWhereCondition(" and create_user is not null  ");
        
        //Thêm các thành phần
        addTextFieldToForm("MenuID", new TextField(), "id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Menu.Form.Description", new TextField(), "description", "string", true, 200, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.Key", new TextField(), "menu_name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addCheckBoxToForm("SDFunction.IsGroup", new CheckBox(), "is_group", "boolean", 
                true, 1, null, "SDFunction.IsGroup.Caption", false, false, null, false, null, true, true, true, true, "SDFunction.IsGroup.Enable", "SDFunction.IsGroup.Disable");        
        setComponentAsLoginUser("User.CreateUser", "create_user", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null , null, INT_VIEWGROUP_ALL);
        addTextFieldToForm("Menu.Form.ORD", new TextField(), "ord", "int", true, 100, "long>0", null, false, false, null, false, null, true, true, true, true, null);
        addSinglePopupToForm("Menu.Form.Parent", "parent_id", "int", false, 10, null, null, true, null, false, null, true, true, true, true, new PopupSingleSDFunctionAction(localMainUI), 2,
                null, "", "id", "description", "sd_function", null, null);        
        addCheckBoxToForm("Menu.Form.Status", new CheckBox(), "is_enable", "boolean", 
                true, 1, null, "Common.Status.Enable", false, true, null, false, null, true, true, true, true, "Common.Status.Enable", "Common.Status.Disable");
        addTextFieldToForm("SDFunction.ConfigFile", new TextField(), "config_file", "string", false, 100, null, null, true, false, null, false, null, true, true, true, true, null);        
        addCheckBoxToForm("SDFunction.IsExtend", new CheckBox(), "is_extend", "boolean", 
                false, 1, null, "SDFunction.IsExtend.Caption", false, false, null, false, null, true, true, true, true, "SDFunction.IsExtend.Enable", "SDFunction.IsExtend.Disable");
        addTextFieldToForm("SDFunction.VietNam", new TextField(), "viet_nam", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.English", new TextField(), "english", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        Object[][] columnInterface = {{"1","1"},{"2","2"},{"3","3"}};
        addComboBoxToForm("SDFunction.ColumnInterface", new ComboBox(), "column_interface", "int",
                false, 1, null, null, false, false, null, false, null, true, true, true, true, columnInterface, "2", "2");
        addTextFieldToForm("SDFunction.TableName", new TextField(), "table_name", "string", false, 30, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.ViewName", new TextField(), "view_name", "string", false, 30, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.IDColumnName", new TextField(), "id_column_name", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        Object[][] PageLengthData = {{"5","5"},{"10","10"},{"25","25"},{"50","50"},{"100","100"},{"600","600"}};
        addComboBoxToForm("SDFunction.PageLength", new ComboBox(), "page_length", "int",
                false, 10, null, null, false, false, null, false, null, true, true, true, true, PageLengthData, "25", "25");        
        Object[][] TableTypeData = {{"1","SDFunction.TableType.Normal"},{2,"SDFunction.TableType.NumberPage"},{3,"SDFunction.TableType.Tree"}};
        addComboBoxToForm("SDFunction.TableType", new ComboBox(), "table_type", "int",
                false, 10, null, null, false, false, null, false, null, true, true, true, true, TableTypeData, "1", "SDFunction.TableType.Normal");
        addTextFieldToForm("SDFunction.SortColumnName", new TextField(), "sort_column_name", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.ParentColumnName", new TextField(), "parent_column_name", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        Object[][] SortData = {{"1","SDFunction.ASC"},{"2","SDFunction.DESC"}};
        addComboBoxToForm("SDFunction.SortAscending", new ComboBox(), "sort_ascending", "int",
                false, 10, null, null, false, false, null, false, null, true, true, true, true, SortData, "1", "SDFunction.ASC");
        addTextFieldToForm("SDFunction.SequenceName", new TextField(), "sequence_name", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);
        
        // Bổ sung tree dữ liệu
        addTextFieldToForm("SDFunction.TreeVietNam", new TextField(), "tree_viet_nam", "string", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.TreeEnglish", new TextField(), "tree_english", "string", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.TreeIdColumn", new TextField(), "tree_id_column", "string", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.TreeNameColumn", new TextField(), "tree_name_column", "string", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.TreeParentColumn", new TextField(), "tree_parent_column", "string", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.TreeTableName", new TextField(), "tree_table_name", "string", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.TreeConnectColumn", new TextField(), "tree_connect_column", "string", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("SDFunction.WhereQuery", new TextField(), "where_query", "string", false, 50, null, null, false, false, null, false, null, true, true, true, true, null);        
        addTextFieldToForm("SDFunction.TreeWhereQuery", new TextField(), "tree_where_query", "string", false, 100, null, null, false, false, null, false, null, true, true, true, true, null);
        addMultiPopupToForm("SDFunction.WhereParam", false, false, new PopupMultiFunctionParamAction(localMainUI), 2, null, "sd_func_param", "id", "function_id", null, null, null, null, null);
        addMultiPopupToForm("SDFunction.TreeWhereParam", false, false, new PopupMultiTreeParamAction(localMainUI), 2, null, "sd_tree_param", "id", "function_id", null, null, null, null, null);        
        // End: Bổ sung tree dữ liệu
        
        addMultiPopupToForm("SDFunction.Component", false, false, new PopupMultiComponentAction(localMainUI), 2, null, "sd_component", "id", "function_id", null, null, null, null, null);        
        
        CheckBox che1 = (CheckBox)getComponent("is_group");
        che1.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    if (che1.getValue()) {
                        clearComponent();
                        setEnableComponent(false);
                    }
                    else {
                        setEnableComponent(true);
                    }
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }                
            }
        });        
        
        Button buttonRelease = new Button(ResourceBundleUtils.getLanguageResource("Button.Release"));
        buttonRelease.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
//                    EncryptDecryptUtils edUtils = new EncryptDecryptUtils();
//                    edUtils.XOREncrypt(configPath + "Language.properties", configPath + "Language.conf");
//                    edUtils.XOREncrypt(configPath + "Language_en_US.properties", configPath + "Language_en_US.conf");
//                    edUtils.XOREncrypt(configPath + "Language_vi_VN.properties", configPath + "Language_vi_VN.conf");
                    
//                    edUtils.XOREncrypt(configPath + "Language_encrypt.conf", configPath + "Language.properties");
//                    edUtils.XOREncrypt(configPath + "Language_en_US_encrypt.conf", configPath + "Language_en_US.properties");
//                    edUtils.XOREncrypt(configPath + "Language_vi_VN_encrypt.conf", configPath + "Language_vi_VN.properties");
                    
                    buttonReleaseProject();
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }                
            }
        });
        addButton(buttonRelease);
        
        Button buttonCopy = new Button(ResourceBundleUtils.getLanguageResource("Button.Release"));
        buttonRelease.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    buttonReleaseProject();
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }                
            }
        });
        addButton(buttonRelease);
        
        return initPanel(2);
    }

    /*
    @Override
    public void afterAddData(Connection connection, long id) throws Exception {
        BuildConfigDAO buildDao = new BuildConfigDAO();
        buildDao.synchronizeMenu(connection);
    }

    @Override
    public void afterEditData(Connection connection, long id) throws Exception {
        BuildConfigDAO buildDao = new BuildConfigDAO();
        buildDao.synchronizeMenu(connection);
    }

    @Override
    public void afterDeleteData(Connection connection, Object[] deleteArray) throws Exception {
        BuildConfigDAO buildDao = new BuildConfigDAO();
        buildDao.synchronizeMenu(connection);
    }
    */
    
    /**
     * Hàm thực hiện khi ấn nút tạo chương trình
     *
     * @since 17/04/2015 HienDM
     */    
    private void buttonReleaseProject() throws Exception {
        ConfirmationDialog.Callback ccbl = new ConfirmationDialog.Callback() {
            @Override
            public void onDialogResult(String buttonName) {
                try {
                    List<Map> lstMenu = buildMenuConfigFile();
                    BuildConfigDAO buildDao = new BuildConfigDAO();
                    List<Map> lstSingle = buildDao.getAllSinglePopup();
                    List<Map> lstMulti = buildDao.getAllMultiPopup();                    
                    buildComponentConfigFile(lstMenu);
                    buildComponentConfigFile(lstSingle);
                    buildComponentConfigFile(lstMulti);
                    Notification.show(ResourceBundleUtils.getLanguageResource("Common.UpdateSuccess"),
                            null, Notification.Type.WARNING_MESSAGE);                    
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        };
        mainUI.addWindow(new ConfirmationDialog(
                ResourceBundleUtils.getLanguageResource("Common.Confirm"),
                ResourceBundleUtils.getLanguageResource("Common.ConfirmExecute"), ccbl));        
    }
    
    /**
     * Hàm tạo file Menu.Properties
     * 
     * @return Danh sách menu chương trình
     * @since 17/04/2015 HienDM
     */    
    private List<Map> buildMenuConfigFile() throws Exception {
        BuildConfigDAO buildDao = new BuildConfigDAO();
        List<Map> lstMenu = buildDao.getMenuConfig();
        String menuConfig = "";
        int count = 0;
        for (int i = 0; i < lstMenu.size(); i++) {
            if (lstMenu.get(i).get("config_file") != null
                    && !lstMenu.get(i).get("config_file").toString().trim().isEmpty()) {
                count++;
                menuConfig += "Menu" + count + "="
                        + lstMenu.get(i).get("menu_name") + ";"
                        + lstMenu.get(i).get("config_file") + ";" + lstMenu.get(i).get("is_extend") + "\n";
            }
        }
        menuConfig = "NumberOfMenu=" + count + "\n" + menuConfig;
        FileUtils fileUtils = new FileUtils();
        String menuPath = configPath + "Menu.properties";
        fileUtils.writeStringToFile(menuConfig, menuPath, FileUtils.UTF_8); 
        return lstMenu;
    }
    
    /**
     * Hàm khởi tạo giao diện
     *
     * @since 19/11/2014 HienDM
     * @param lstMenu Danh sách menu chương trình
     */    
    private void buildComponentConfigFile(List<Map> lstMenu) throws Exception {
        if(lstMenu != null && !lstMenu.isEmpty()) {
            BuildConfigDAO buildDao = new BuildConfigDAO();
            // Query Database lấy dữ liệu
            List<Map> lstCom = buildDao.getAllComponent();
            List<Map> lstFuncParam = buildDao.getFunctionParam();
            List<Map> lstTreeParam = buildDao.getTreeParam();
            List<Map> lstComParam = buildDao.getComponentParam();

            EncryptDecryptUtils edUtils = new EncryptDecryptUtils();
            StringBuilder strLanguage = edUtils.decryptFileToStringBuilder(configPath + "Language.conf");
            StringBuilder strLanguageEN = edUtils.decryptFileToStringBuilder(configPath + "Language_en_US.conf");

            FileUtils fileUtil = new FileUtils();

            for (int i = 0; i < lstMenu.size(); i++) {
                Map row = lstMenu.get(i);
                Object fileName = row.get("config_file");
                Object createUser = row.get("create_user");
                if (fileName != null && !fileName.toString().trim().isEmpty() 
                        && createUser != null && !createUser.toString().trim().isEmpty()) {
                    strLanguage.append("\n");
                    strLanguageEN.append("\n");
                    if(row.get("viet_nam") != null && !row.get("viet_nam").toString().trim().isEmpty())
                        strLanguage = addPropertiesLine(strLanguage, (String)row.get("menu_name"), row.get("viet_nam"));
                    if(row.get("tree_viet_nam") != null && !row.get("tree_viet_nam").toString().trim().isEmpty())
                        strLanguage = addPropertiesLine(strLanguage, (String)row.get("menu_name") + ".Tree", row.get("tree_viet_nam"));

                    if(row.get("english") != null && !row.get("english").toString().trim().isEmpty())
                        strLanguageEN = addPropertiesLine(strLanguageEN, (String)row.get("menu_name"), row.get("english"));
                    if(row.get("tree_english") != null && !row.get("tree_english").toString().trim().isEmpty())
                        strLanguageEN = addPropertiesLine(strLanguageEN, (String)row.get("menu_name") + ".Tree", row.get("tree_english"));                

                    // Build file config của từng chức năng
                    StringBuilder actionContent = new StringBuilder();

                    //------build các thông số chức năng-----------------------
                    actionContent = addPropertiesLine(actionContent, "TableName", row.get("table_name"));
                    actionContent = addPropertiesLine(actionContent, "ViewName", row.get("view_name"));
                    actionContent = addPropertiesLine(actionContent, "IdColumnName", row.get("id_column_name"));
                    actionContent = addPropertiesLine(actionContent, "PageLength", row.get("page_length"));
                    actionContent = addPropertiesLine(actionContent, "TableType", row.get("table_type"));
                    actionContent = addPropertiesLine(actionContent, "SortColumnName", row.get("sort_column_name"));
                    actionContent = addPropertiesLine(actionContent, "ParentColumnName", row.get("parent_column_name"));
                    actionContent = addPropertiesLine(actionContent, "RootId", "0");
                    actionContent = addPropertiesLine(actionContent, "SortAscending", row.get("sort_ascending"));
                    actionContent = addPropertiesLine(actionContent, "SequenceName", row.get("sequence_name"));
                    actionContent = addPropertiesLine(actionContent, "WhereQuery", row.get("where_query"));
                    actionContent = addPropertiesLine(actionContent, "TreeName", (String)row.get("menu_name") + ".Tree");
                    actionContent = addPropertiesLine(actionContent, "TreeIdColumn", row.get("tree_id_column"));
                    actionContent = addPropertiesLine(actionContent, "TreeNameColumn", row.get("tree_name_column"));
                    actionContent = addPropertiesLine(actionContent, "TreeParentColumn", row.get("tree_parent_column"));
                    actionContent = addPropertiesLine(actionContent, "TreeTableName", row.get("tree_table_name"));
                    actionContent = addPropertiesLine(actionContent, "TreeConnectColumn", row.get("tree_connect_column"));
                    actionContent = addPropertiesLine(actionContent, "TreeWhereQuery", row.get("tree_where_query"));
                    actionContent = addPropertiesLine(actionContent, "ColumnInterface", row.get("column_interface"));

                    //------build các thành phần giao diện-----------------------
                    int countComponent = 0;
                    for(int j = 0; j < lstCom.size(); j++) {
                        Map rowCom = lstCom.get(j);
                        if(rowCom.get("function_id").equals(row.get("id"))) {
                            if(!rowCom.get("param0").equals("User.CreateDate") && !rowCom.get("param0").equals("User.CreateUser")) {
                                strLanguage = addPropertiesLine(strLanguage, (String)rowCom.get("param0"), rowCom.get("viet_nam"));
                                strLanguageEN = addPropertiesLine(strLanguageEN, (String)rowCom.get("param0"), rowCom.get("english"));
                            }

                            StringBuilder strValue = new StringBuilder();
                            strValue.append(rowCom.get("type"));
                            int numberOfParam = getNumberOfParameter(rowCom.get("type").toString());
                            for(int k = 0; k < numberOfParam; k++) {
                                if(rowCom.get("param" + k) != null && !rowCom.get("param" + k).toString().trim().isEmpty()) {
                                    strValue.append(";I;");
                                    strValue.append(rowCom.get("param" + k));
                                } else {
                                    strValue.append(";I;");
                                    strValue.append("null");
                                }
                            }
                            //------build các tham số component-----------------------
                            if(rowCom.get("type") != null && 
                                    (rowCom.get("type").toString().equals("ComboBox") || 
                                    rowCom.get("type").toString().equals("ConstantComboBox"))) {
                                if(lstComParam != null && !lstComParam.isEmpty()) { 
                                    StringBuilder strValue2 = new StringBuilder();
                                    boolean firstTime = true;
                                    for(int k = 0; k < lstComParam.size(); k++) {
                                        Map rowParam = lstComParam.get(k);
                                        if(rowParam.get("component_id").equals(rowCom.get("id"))) {
                                            if(firstTime) firstTime = false;
                                            else strValue2.append(";M;");
                                            strValue2.append(rowParam.get("type"));
                                            strValue2.append(";X;");
                                            strValue2.append(rowParam.get("value"));
                                            if(rowParam.get("caption") != null && !rowParam.get("caption").toString().trim().isEmpty()) {
                                                strValue2.append(";X;");
                                                strValue2.append(rowParam.get("caption"));
                                                strLanguage = addPropertiesLine(strLanguage, rowParam.get("caption").toString(), rowParam.get("viet_nam"));
                                                strLanguageEN = addPropertiesLine(strLanguageEN, rowParam.get("caption").toString(), rowParam.get("english"));
                                            }
                                        }   
                                    }
                                    if(strValue2.length() > 0) {
                                        strValue.append(";I;");
                                        strValue.append(strValue2);                     
                                    }
                                }
                            }                        
                            actionContent = addPropertiesLine(actionContent, "Component" + countComponent, strValue);

                            countComponent++;
                        }
                    }
                    actionContent = addPropertiesLine(actionContent, "NumberOfComponent", "" + countComponent);

                    //------build các tham số where query-----------------------
                    if(row.get("where_query") != null && row.get("where_query").toString().contains("?")) {
                        if(lstFuncParam != null && !lstFuncParam.isEmpty()) { 
                            StringBuilder strValue = new StringBuilder();
                            boolean firstTime = true;
                            for(int j = 0; j < lstFuncParam.size(); j++) {
                                Map rowParam = lstFuncParam.get(j);
                                if(rowParam.get("function_id").equals(row.get("id"))) {
                                    if(firstTime) firstTime = false;
                                    else strValue.append(";I;");
                                    strValue.append(rowParam.get("type"));
                                    strValue.append(";X;");
                                    strValue.append(rowParam.get("value"));                                
                                }   
                            }
                            actionContent = addPropertiesLine(actionContent, "WhereQueryParam", strValue);
                        }
                    }
                    //------build các tham số tree where query-----------------------
                    if(row.get("tree_where_query") != null && row.get("tree_where_query").toString().contains("?")) {
                        if(lstTreeParam != null && !lstTreeParam.isEmpty()) { 
                            StringBuilder strValue = new StringBuilder();
                            boolean firstTime = true;
                            for(int j = 0; j < lstTreeParam.size(); j++) {
                                Map rowParam = lstTreeParam.get(j);
                                if(rowParam.get("function_id").equals(row.get("id"))) {
                                    if(firstTime) firstTime = false;
                                    else strValue.append(";I;");
                                    strValue.append(rowParam.get("type"));
                                    strValue.append(";X;");
                                    strValue.append(rowParam.get("value"));                                
                                }   
                            }
                            actionContent = addPropertiesLine(actionContent, "TreeWhereQueryParam", strValue);
                        }
                    } 

                    // Ghi ra file
                    fileUtil.writeStringToFile(actionContent.toString(), functionConfigPath + fileName + ".properties", FileUtils.UTF_8);
                }
            }

            // Ghi dữ liệu file language
            fileUtil.writeStringToFile(strLanguage.toString(), configPath + "Language.properties", FileUtils.UTF_8);
            fileUtil.writeStringToFile(strLanguage.toString(), configPath + "Language_vi_VN.properties", FileUtils.UTF_8);
            fileUtil.writeStringToFile(strLanguageEN.toString(), configPath + "Language_en_US.properties", FileUtils.UTF_8);
        }
    }    
        
    /**
     * Hàm lấy loại component dạng text
     * 
     * @param type loại component
     * @since 30/04/2015 HienDM
     */     
    private int getNumberOfParameter(String type) {
        if(type.equals("TextField")) return 17;
        if(type.equals("TextArea")) return 17;
        else if(type.equals("DateField")) return 13;
        else if(type.equals("TimeField")) return 13;
        else if(type.equals("SysDate")) return 6;
        else if(type.equals("LoginUser")) return 9;
        else if(type.equals("CheckBox")) return 15;
        else if(type.equals("ComboBox")) return 26;
        else if(type.equals("ConstantComboBox")) return 17;
        else if(type.equals("SinglePopup")) return 24;
        else if(type.equals("MultiPopup")) return 12;
        else if(type.equals("UploadField")) return 8;
        else if(type.equals("MultiUploadField")) return 12;
        else if(type.equals("OnlyInViewField")) return 9;
        return -1;
    }
    
    /**
     * Hàm bổ sung thêm một dòng vào file cấu hình
     * 
     * @param strContent Nội dung file cấu hình
     * @param key key
     * @param value value
     * @since 30/04/2015 HienDM
     */ 
    private StringBuilder addPropertiesLine(StringBuilder strContent, String key, Object value) {
        if(key != null && !key.trim().isEmpty() && value != null && !value.toString().trim().isEmpty()) {
            strContent.append(key);
            strContent.append("=");
            strContent.append(StringEscapeUtils.escapeJava(value.toString()));
            strContent.append("\n");   
        }
        return strContent;
    }
    
    /**
     * Hàm xóa dữ liệu khi click checkbox Nhóm/Chức năng
     *
     * @since 19/04/2015 HienDM
     */ 
    private void clearComponent() throws Exception {
        getComponent("config_file").setValue("");
        getComponent("is_extend").setValue(null);
        getComponent("viet_nam").setValue("");
        getComponent("english").setValue("");
        getComponent("table_name").setValue("");
        getComponent("view_name").setValue("");
        getComponent("id_column_name").setValue("");
        getComponent("page_length").setValue(null);
        getComponent("table_type").setValue(null);
        getComponent("sort_column_name").setValue("");
        getComponent("parent_column_name").setValue("");
        getComponent("sort_ascending").setValue(null);
        getComponent("sequence_name").setValue("");
        getComponent("where_query").setValue("");
        getMultiComponent("sd_component").removeAllItems();
        getComponent("tree_viet_nam").setValue("");
        getComponent("tree_english").setValue("");
        getComponent("tree_id_column").setValue("");
        getComponent("tree_name_column").setValue("");
        getComponent("tree_parent_column").setValue("");
        getComponent("tree_table_name").setValue("");
        getComponent("tree_connect_column").setValue("");
        getComponent("tree_where_query").setValue("");
        getComponent("column_interface").setValue("");
    }
    
    /**
     * Hàm thiết lập ẩn hiện component
     *
     * @since 19/04/2015 HienDM
     */     
    private void setEnableComponent(boolean enable) throws Exception {
        getComponent("config_file").setEnabled(enable);
        getComponent("is_extend").setEnabled(enable);
        getComponent("viet_nam").setEnabled(enable);
        getComponent("english").setEnabled(enable);
        getComponent("table_name").setEnabled(enable);
        getComponent("view_name").setEnabled(enable);
        getComponent("id_column_name").setEnabled(enable);
        getComponent("page_length").setEnabled(enable);
        getComponent("table_type").setEnabled(enable);
        getComponent("sort_column_name").setEnabled(enable);
        getComponent("parent_column_name").setEnabled(enable);
        getComponent("sort_ascending").setEnabled(enable);
        getComponent("sequence_name").setEnabled(enable);
        getComponent("where_query").setEnabled(enable);
        getMultiComponentButton("sd_component").setEnabled(enable);
        getComponent("tree_viet_nam").setEnabled(enable);
        getComponent("tree_english").setEnabled(enable);
        getComponent("tree_id_column").setEnabled(enable);
        getComponent("tree_name_column").setEnabled(enable);
        getComponent("tree_parent_column").setEnabled(enable);
        getComponent("tree_table_name").setEnabled(enable);
        getComponent("tree_connect_column").setEnabled(enable);
        getComponent("tree_where_query").setEnabled(enable);
        getComponent("column_interface").setEnabled(enable);
    }
}
