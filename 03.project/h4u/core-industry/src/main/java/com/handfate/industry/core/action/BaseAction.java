package com.handfate.industry.core.action;

import com.family.common.util.CSVTransformer;
import com.handfate.industry.core.MainUI;
import static com.handfate.industry.core.MainUI.mainLogger;
import com.handfate.industry.core.action.component.ComboboxItem;
import com.handfate.industry.core.action.component.ConfirmationDialog;
import com.handfate.industry.core.action.component.DownloadLink;
import com.handfate.industry.core.action.component.MultiUploadField;
import com.handfate.industry.core.action.component.PagedTable;
import com.handfate.industry.core.action.component.UploadField;
import com.handfate.industry.core.action.parameter.CheckBoxParameter;
import com.handfate.industry.core.action.parameter.ComboBoxConstantParameter;
import com.handfate.industry.core.action.parameter.ComboBoxParameter;
import com.handfate.industry.core.action.parameter.ComponentParameter;
import com.handfate.industry.core.action.parameter.DateParameter;
import com.handfate.industry.core.action.parameter.LoginUserParameter;
import com.handfate.industry.core.action.parameter.MultiUploadFieldParameter;
import com.handfate.industry.core.action.parameter.OnlyInViewParameter;
import com.handfate.industry.core.action.parameter.PopupMultiParameter;
import com.handfate.industry.core.action.parameter.PopupSingleParameter;
import com.handfate.industry.core.action.parameter.SysdateParameter;
import com.handfate.industry.core.action.parameter.UploadFieldParameter;
import com.handfate.industry.core.dao.BaseDAO;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.handfate.industry.core.util.AdvancedFileDownloader;
import com.handfate.industry.core.util.AdvancedFileDownloader.AdvancedDownloaderListener;
import com.handfate.industry.core.util.AdvancedFileDownloader.DownloaderEvent;
import com.handfate.industry.core.util.Base64Utils;
import com.handfate.industry.core.util.ComponentUtils;
import com.handfate.industry.core.util.EmailValidator;
import com.handfate.industry.core.util.EncryptDecryptUtils;
import com.handfate.industry.core.util.FileUtils;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @since 14/11/2014
 * @author HienDM
 */

public class BaseAction {
    public List<List> lstComponent = new ArrayList();
    public List<List> lstComponentMulti = new ArrayList();
    public List<List> lstCustomizeComponent = new ArrayList();
    public int mainTextField = 0;
    public int descriptionField = 0;
    public int idField = -1;
    public int sortField = -1;
    public int parentField = -1;
    public boolean sortAscending = true;
    public boolean hasTreeSearch = false;
    public boolean recursiveTreeSearch = true;
    public int pageLength = 25;
    public int tableType = 1;    
    public String idColumnName = "";
    public int nameField = -1;
    public String nameColumn = "";
    public String tableName = "";
    public String viewName = "";
    public String sequenceName = "";
    public String sortColumnName = "";
    public String sortColumnType = "";
    public String rootId = "";
    public String parentColumnName = "";
    public boolean includeOrder = true;
    public int numberOfTD = 2;
    public String tableQuery = "";
    public List tableQueryParameter = new ArrayList();
    public String queryWhereCondition = "";
    public List queryWhereParameter = new ArrayList();
    public String queryWhereFilter = "";
    public ArrayList queryWhereFilterParameter = new ArrayList();
    public String treeSelectedId = "";
    public String columnConnectTree = "";
    public boolean allowAdd = true;
    public boolean allowEdit = true;
    public boolean allowDelete = true;
    public boolean allowSearch = true;
    public boolean allowExport = false;
    public boolean allowDetail = false;
    public boolean isChangeDefaultSearch = true;
    public List<Map> lstTableData = new ArrayList();
    public List<List> lstMandatorySearchValue = new ArrayList();
    public String popupWidth = "900px";
    public String popupFormWidth = "880px";
    public String popupTreeFormWidth = "650px";
    public String popupHeight = "650px";
    public List<List> lstFilter = new ArrayList();
    public String templateFile = "";
    public int templateHeight = 0;
    public List<Button> lstButton = new ArrayList();
    public boolean checkPopupFilter = false;
    public List<List> lstExportParameter = new ArrayList();
    public int currentForm = 1;
    public static final int INT_SEARCH_FORM = 1;
    public static final int INT_ADD_FORM = 2;
    public static final int INT_EDIT_FORM = 3;
    public static final int INT_VIEW_FORM = 4;
    public boolean refreshAlready = false;
    public boolean ignoreAttachWhenEdit = false;
    public boolean notUpdateForm = false;
    
    public HorizontalLayout layoutPanel = new HorizontalLayout();
    public VerticalLayout mainPanel = new VerticalLayout();
    public VerticalLayout dataArea = new VerticalLayout();
    public VerticalLayout buttonArea = new VerticalLayout();
    public VerticalLayout formArea = new VerticalLayout();
    public HorizontalLayout panelButton = new HorizontalLayout();
    public Table table;
    public TextField txtButtonState = new TextField();
    public UI mainUI = null;
    public HorizontalLayout treeLayout = new HorizontalLayout();
    public Tree treeSearch;
    
    public static final int INT_LABEL = 0;
    public static final int INT_COMPONENT = 1;
    public static final int INT_DB_FIELD_NAME = 2;
    public static final int INT_DATA_TYPE = 3;
    public static final int INT_MANDATORY = 4;
    public static final int INT_DATA_LENGTH = 5;
    public static final int INT_FORMAT = 6;
    public static final int INT_CAPTION = 7;
    public static final int INT_USE_TO_SEARCH = 8;
    public static final int INT_IS_PASSWORD = 9;
    public static final int INT_SEARCH_MANDATORY = 10;
    public static final int INT_IS_COLLAPSED = 11;
    public static final int INT_SEARCH_DEFAULT = 12;
    public static final int INT_VISIBLE_ADD = 13;
    public static final int INT_VISIBLE_EDIT = 14;
    public static final int INT_ENABLE_ADD = 15;
    public static final int INT_ENABLE_EDIT = 16;
    
    // TextField & Date
    public static final int INT_DEFAULT = 17;
    
    // File
    public static final int INT_FILE_TABLEATTACH = 2;
    public static final int INT_FILE_DIRECTORY = 17;
    public static final int INT_FILE_ISPICTURE = 18;
    public static final int INT_FILE_PATH = 19;
    public static final int INT_FILE_EDIT = 20;
    public static final int INT_FILE_SIZE = 21;
    public static final int INT_FILE_IDCONNECT = 22;
    public static final int INT_FILE_ATTACH = 23;
    public static final int INT_FILE_PRIMARY = 24;
    public static final int INT_FILE_SEQUENCE = 25;
    // Date
    public static final int INT_TO_DATE_COMPONENT = 18;
    public static final int INT_SYSDATE = 19;
    // CheckBox
    public static final int INT_CHECKBOX_DEFAULT = 9;    
    public static final int INT_CHECKBOX_ENABLE = 17;    
    public static final int INT_CHECKBOX_DISABLE = 18;    
    // ComboBox
    public static final int INT_COMBOBOX_DEFAULTVALUE = 17;
    public static final int INT_COMBOBOX_DEFAULTCAPTION = 18;
    public static final int INT_COMBOBOX_REFRESH = 19;
    public static final int INT_COMBOBOX_MULTILANGUAGE = 20;
    public static final int INT_COMBOBOX_QUERY = 21;
    public static final int INT_COMBOBOX_PARAMETER = 22;
    public static final int INT_COMBOBOX_IDCOLUMN = 23;
    public static final int INT_COMBOBOX_IDTYPE = 24;
    public static final int INT_COMBOBOX_NAMECOLUMN = 25;
    public static final int INT_COMBOBOX_TABLENAME = 26;
    public static final int INT_COMBOBOX_FILTERPARENT = 27;
    public static final int INT_COMBOBOX_FILTERCHILD = 28;
    public static final int INT_COMBOBOX_DATA = 29;
    // PopupSingleAction
    public static final int INT_POPUP_BUTTON = 27;
    public static final int INT_POPUP_DELETE = 28;
    // PopupMultiAction
    public static final int INT_MULTI_LABEL = 0;
    public static final int INT_MULTI_TABLE = 1;
    public static final int INT_MULTI_MANDATORY = 2;
    public static final int INT_MULTI_COLLAPSED = 3;
    public static final int INT_MULTI_BROWSE = 4;
    public static final int INT_MULTI_ATTACH = 5;
    public static final int INT_MULTI_TABLENAME = 6;
    public static final int INT_MULTI_IDPOPUP = 7;
    public static final int INT_MULTI_IDCONNECT = 8;
    public static final int INT_MULTI_IDFIELD = 9;
    public static final int INT_MULTI_SEQUENCE = 10;
    public static final int INT_MULTI_POPUP = 11;
    public static final int INT_MULTI_OLDIDS = 12;
    public static final int INT_MULTI_ATTACHTABLE = 13;
    // set component as login user
    public static final int INT_LOGINUSER = 29;
    public static final int INT_LOGINUSER_ONLYVIEW = 30;
    public static final int INT_LOGINUSER_ONLYEDIT = 31;
    public static final int INT_LOGINUSER_ONLYVIEWGROUP = 32;
    
    public final int INT_NORMAL_TABLE = 1;//Bang co Scroll    
    public final int INT_PAGED_TABLE = 2;//Bang Co phan trang
    public final int INT_TREE_TABLE = 3;//Bang co cay

    public static final int INT_VIEWGROUP_ALL = 0;
    public static final int INT_VIEWGROUP_ONE = 1;
    public static final int INT_VIEWGROUP_RECURSIVE = 2;    
    
    public static final String INVOICE_CLOSE_DATE = "INVOICE_CLOSE_DATE";
    public static final String DAYS_FOR_EDIT_ACCEPTANCE_DAY = "DAYS_FOR_EDIT_ACCEPTANCE_DAY";
    
    public Button buttonFind;
    public Button buttonUpdate;
    public Button buttonCancel;
    public Button buttonPreExport;
    public String configFileName = "";

    public UI getMainUI() {
        return mainUI;
    }

    public void setMainUI(UI mainUI) {
        this.mainUI = mainUI;
    }

    public int getIdField() {
        return idField;
    }

    public void setIdField(int idField) {
        this.idField = idField;
    }

    public int getPageLength() {
        return pageLength;
    }

    public void setPageLength(int pageLength) {
        this.pageLength = pageLength;
    }

    public String getIdColumnName() {
        return idColumnName;
    }

    public void setIdColumnName(String idColumnName) {
        if(idColumnName != null) this.idColumnName = idColumnName.toLowerCase();
    }

    public int getMainTextField() {
        return mainTextField;
    }

    public void setMainTextField(int mainTextField) {
        this.mainTextField = mainTextField;
    }

    public int getDescriptionField() {
        return descriptionField;
    }

    public void setDescriptionField(int descriptionField) {
        this.descriptionField = descriptionField;
    }

    public int getTableType() {
        return tableType;
    }

    public void setTableType(int tableType) {
        this.tableType = tableType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName.toLowerCase();
        if(this.viewName == null || this.viewName.toLowerCase().trim().equals("")) 
            viewName = tableName.toLowerCase();
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public int getSortField() {
        return sortField;
    }

    public void setSortField(int sortField) {
        this.sortField = sortField;
    }

    public boolean isSortAscending() {
        return sortAscending;
    }

    public void setSortAscending(boolean sortAscending) {
        this.sortAscending = sortAscending;
    }

    public String getSortColumnName() {
        return sortColumnName;
    }

    public void setSortColumnName(String sortColumnName) {
        this.sortColumnName = sortColumnName.toLowerCase();
    }

    public String getSortColumnType() {
        return sortColumnType;
    }

    public void setSortColumnType(String sortColumnType) {
        this.sortColumnType = sortColumnType;
    }

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public int getParentField() {
        return parentField;
    }

    public void setParentField(int parentField) {
        this.parentField = parentField;
    }

    public String getParentColumnName() {
        return parentColumnName;
    }

    public void setParentColumnName(String parentColumnName) {
        this.parentColumnName = parentColumnName;
    }

    public boolean isIncludeOrder() {
        return includeOrder;
    }

    public void setIncludeOrder(boolean includeOrder) {
        this.includeOrder = includeOrder;
    }

    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName.toLowerCase();
    }

    public boolean isHasTreeSearch() {
        return hasTreeSearch;
    }

    public void setHasTreeSearch(boolean hasTreeSearch) {
        this.hasTreeSearch = hasTreeSearch;
    }

    public String getTableQuery() {
        return tableQuery;
    }

    public void setTableQuery(String tableQuery) {
        this.tableQuery = tableQuery;
    }    

    public String getTreeSelectedId() {
        return treeSelectedId;
    }

    public void setTreeSelectedId(String treeSelectedId) {
        this.treeSelectedId = treeSelectedId;
    }

    public boolean isRecursiveTreeSearch() {
        return recursiveTreeSearch;
    }

    public void setRecursiveTreeSearch(boolean recursiveTreeSearch) {
        this.recursiveTreeSearch = recursiveTreeSearch;
    }

    public boolean isAllowAdd() {
        return allowAdd;
    }

    public void setAllowAdd(boolean allowAdd) {
        this.allowAdd = allowAdd;
    }

    public boolean isAllowEdit() {
        return allowEdit;
    }

    public void setAllowEdit(boolean allowEdit) {
        this.allowEdit = allowEdit;
    }

    public boolean isAllowDelete() {
        return allowDelete;
    }

    public void setAllowDelete(boolean allowDelete) {
        this.allowDelete = allowDelete;
    }

    public boolean isAllowSearch() {
        return allowSearch;
    }

    public void setAllowSearch(boolean allowSearch) {
        this.allowSearch = allowSearch;
    }

    public boolean isAllowExport() {
        return allowExport;
    }

    public void setAllowExport(boolean allowExport) {
        this.allowExport = allowExport;
    }
    
    public String getNameColumn() {
        return nameColumn;
    }

    public void setNameColumn(String nameColumn) {
        if(nameColumn != null) this.nameColumn = nameColumn.toLowerCase();
    }

    public String getQueryWhereCondition() {
        return queryWhereCondition;
    }

    public void setQueryWhereCondition(String queryWhereCondition) {
        this.queryWhereCondition = " " + queryWhereCondition + " ";
    }
    
    public void addQueryWhereCondition(String queryWhereCondition) {
        this.queryWhereCondition += " " + queryWhereCondition + " ";
    }

    public List getQueryWhereParameter() {
        return queryWhereParameter;
    }

    public void setQueryWhereParameter(List queryWhereParameter) {
        this.queryWhereParameter = queryWhereParameter;
    }
    
    public void addQueryWhereParameter(List queryWhereParameter) {
        this.queryWhereParameter.addAll(queryWhereParameter);
    }

    public boolean isIsChangeDefaultSearch() {
        return isChangeDefaultSearch;
    }

    public void setIsChangeDefaultSearch(boolean isChangeDefaultSearch) {
        this.isChangeDefaultSearch = isChangeDefaultSearch;
    }

    public String getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(String templateFile) {
        this.templateFile = templateFile;
    }

    public int getTemplateHeight() {
        return templateHeight;
    }

    public void setTemplateHeight(int templateHeight) {
        this.templateHeight = templateHeight;
    }

    public boolean isAllowDetail() {
        return allowDetail;
    }

    public void setAllowDetail(boolean allowDetail) {
        this.allowDetail = allowDetail;
    }

    public int getCurrentForm() {
        return currentForm;
    }

    public void setCurrentForm(int currentForm) {
        this.currentForm = currentForm;
    }

    public boolean isIgnoreAttachWhenEdit() {
        return ignoreAttachWhenEdit;
    }

    public void setIgnoreAttachWhenEdit(boolean ignoreAttachWhenEdit) {
        this.ignoreAttachWhenEdit = ignoreAttachWhenEdit;
    }

    public boolean isNotUpdateForm() {
        return notUpdateForm;
    }

    public void setNotUpdateForm(boolean notUpdateForm) {
        this.notUpdateForm = notUpdateForm;
    }

    public String getConfigFileName() {
        return configFileName;
    }

    public void setConfigFileName(String configFileName) {
        this.configFileName = configFileName;
    } 
    
    public HorizontalLayout init(UI localMainUI) throws Exception {
        return null;
    }
    
    /**
     * Hàm khởi tạo giao diện chức năng
     *
     * @since 18/11/2014 HienDM
     * @param column số cột trình bày giao diện
     * @return Giao diện chức năng
     */
    public HorizontalLayout initPanel(int column) throws Exception {
        beforeInitPanel();
        if(viewName == null || viewName.isEmpty()) viewName = tableName;
        if(tableType == INT_NORMAL_TABLE) table = new Table();
        if(tableType == INT_PAGED_TABLE) table = new PagedTable();
        if(tableType == INT_TREE_TABLE) {
            table = new TreeTable();
            includeOrder = false;
        }
        numberOfTD = column;
        if(sortField == -1) sortField = idField;
        if(sortColumnName.equals("")) sortColumnName = idColumnName;
        if(sortColumnType.equals("")) sortColumnType = "int";
        
        ((Component)lstComponent.get(idField).get(INT_COMPONENT)).setVisible(false);                
        formArea = buildSearchFormPanel(column);
        mainPanel = buildMainPanel();
        
        panelButton = buildPanelButton();
        buttonArea.addComponent(panelButton);        
        buttonArea.setComponentAlignment(panelButton, Alignment.MIDDLE_CENTER);
        buttonArea.setHeight("50px");

        
        //Tạo các filter dữ liệu
        for(int i = 0; i < lstFilter.size(); i++) {
            for(int j=0; j < lstComponent.size(); j++) {
                if(lstComponent.get(j).get(INT_DB_FIELD_NAME).equals(
                        lstFilter.get(i).get(0))) {
                    if(lstComponent.get(j).get(INT_COMPONENT) instanceof ComboBox) {
                        String filterStore = lstFilter.get(i).get(0).toString();
                        ComboBox cbo = (ComboBox) lstComponent.get(j).get(INT_COMPONENT);                        
                        filterData(cbo, filterStore);
                    }
                }
            }
        }
        
        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                if (itemClickEvent.isDoubleClick()) {
                    try {
                        buttonDetailClick();
                    } catch(Exception ex) {
                        VaadinUtils.handleException(ex);
                        MainUI.mainLogger.debug("Install error: ", ex);
                    }
                }
            }
        });        
        
        afterInitPanel();
        return layoutPanel;
    }
    
    /**
     * Hàm sắp xếp giao diện chức năng
     *
     * @since 24/05/2015 HienDM
     * @return Giao diện chức năng
     */    
    public VerticalLayout buildMainPanel() throws Exception {
        VerticalLayout buildMain = new VerticalLayout();
        buildMain.addComponent(formArea);
        buildMain.addComponent(buttonArea);
        buildMain.addComponent(dataArea);
        if(dataArea != null) dataArea.removeAllComponents();
        dataArea.addComponent(buildDataPanel());
        
        buildMain.setExpandRatio(formArea, 0f);
        buildMain.setExpandRatio(buttonArea, 0f);
        buildMain.setExpandRatio(dataArea, 100f);
        
        if(hasTreeSearch) {
            treeLayout.setStyleName("DivScrollHorizontal");
            layoutPanel.addComponent(treeLayout);
        }
        layoutPanel.addComponent(buildMain);
        if(hasTreeSearch) {
            layoutPanel.setExpandRatio(buildMain, 70f);
            buildMain.setStyleName("CenterBodySmall");
            treeSelectedId = rootId;
        } else {
            buildMain.setStyleName("CenterBody");
        }
        return buildMain;
    }
    
    /**
     * Hàm thực hiện sau khi khởi tạo panel
     *
     * @since 02/01/2015 HienDM
     */
    public void afterInitPanel() throws Exception {}
    
    /**
     * Hàm thực hiện trước khi khởi tạo panel
     *
     * @since 02/01/2015 HienDM
     */
    public void beforeInitPanel() throws Exception {}    
    
    /**
     * Hàm thêm điều kiện where cho câu query dữ liệu
     *
     * @since 18/11/2014 HienDM
     * @param query Câu lệnh query dữ liệu
     */
    public void addQueryWherePopup(String query) {
        addQueryWhereCondition(query);
        queryWhereFilter = queryWhereCondition;
        queryWhereFilterParameter = (ArrayList) ((ArrayList) queryWhereParameter).clone();
    }
    
    /**
     * Hàm filter dữ liệu một trường theo giá trị của trường khác
     *
     * @since 18/11/2014 HienDM
     * @param cbo ComboBox cha
     * @param parentColumn cột dữ liệu cha trong database
     */
    private void filterData(ComboBox cbo, String parentColumn) {
        Property.ValueChangeListener listener = new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    for (int m = 0; m < lstFilter.size(); m++) {
                        if (lstFilter.get(m).get(0).equals(parentColumn)) {
                            List lstChild = (List) lstFilter.get(m).get(1);
                            for (int n = 0; n < lstChild.size(); n++) {
                                for (int k = 0; k < lstComponent.size(); k++) {
                                    if (lstComponent.get(k).get(INT_DB_FIELD_NAME).equals(lstChild.get(n))) {
                                        if (lstComponent.get(k).get(INT_COMPONENT) instanceof ComboBox) {
                                            ComboBox component = (ComboBox) lstComponent.get(k).get(INT_COMPONENT);
                                            component.removeAllItems();
                                            if (cbo.getValue() != null) {
                                                Object defaultValue = lstComponent.get(k).get(INT_COMBOBOX_DEFAULTVALUE);
                                                List<Map> lstData = (List<Map>) lstComponent.get(k).get(INT_COMBOBOX_DATA);
                                                if (defaultValue != null) {
                                                    component.addItem(defaultValue.toString());
                                                    Object defaultCaption = lstComponent.get(k).get(INT_COMBOBOX_DEFAULTCAPTION);
                                                    if(defaultCaption != null)
                                                        component.setItemCaption(defaultValue, 
                                                                ResourceBundleUtils.getLanguageResource(defaultCaption.toString()));
                                                }
                                                for (int i = 0; i < lstData.size(); i++) {
                                                    Map rows = lstData.get(i);
                                                    if( rows.get(lstComponent.get(k).get(INT_COMBOBOX_FILTERCHILD)) != null) {
                                                        if (rows.get(lstComponent.get(k).get(INT_COMBOBOX_FILTERCHILD)).toString().equals(
                                                                cbo.getValue().toString())) {
                                                            Object celldata = new Object();

                                                            celldata = rows.get(lstComponent.get(k).get(INT_COMBOBOX_IDCOLUMN));
                                                            if (celldata != null) {
                                                                component.addItem(celldata.toString());
                                                            } else {
                                                                component.addItem("");
                                                            }

                                                            String cellCaption = "";
                                                            if ((boolean) lstComponent.get(k).get(INT_COMBOBOX_MULTILANGUAGE)) {
                                                                cellCaption = ResourceBundleUtils.getLanguageResource(rows.get(
                                                                        lstComponent.get(k).get(INT_COMBOBOX_NAMECOLUMN)).toString());
                                                            } else {
                                                                cellCaption = rows.get(lstComponent.get(k).get(INT_COMBOBOX_NAMECOLUMN)).toString();
                                                            }
                                                            if (cellCaption != null) {
                                                                component.setItemCaption(celldata.toString(), cellCaption);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                for (int k = 0; k < lstComponentMulti.size(); k++) {
                                    if (lstComponentMulti.get(k).get(INT_MULTI_TABLENAME).equals(lstChild.get(n))) {
                                        if (lstComponentMulti.get(k).get(INT_MULTI_TABLE) instanceof Table) {
                                            ((Table) lstComponentMulti.get(k).get(INT_MULTI_TABLE)).removeAllItems();
                                            ((PopupMultiAction) lstComponentMulti.get(k).get(INT_MULTI_POPUP)).storeTable.removeAllItems();
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }
            }
        };
        cbo.addValueChangeListener(listener);
    }
    
    /**
     * Hàm thêm component tùy chỉnh vào giao diện nhập
     *
     * @param label mô tả
     * @param component thành phần trên giao diện
     * @param dataType loại dữ liệu
     * @param isMandatory bắt buộc nhập
     * @param dataLength độ dài dữ liệu
     * @param format kiểm tra định dạng dữ liệu:
     *     ví dụ đơn giản: int,long,float,double,email
     * @param caption mô tả thêm
     * @param isPassword Có phải password không
     * @param visibleAdd Hiển thị tại màn hình thêm mới
     * @param visibleEdit Hiển thị tại màn hình sửa
     * @param enableAdd Hiệu lực tại màn hình thêm mới
     * @param enableEdit Hiệu lực tại màn hình sửa
     * @param defaultValue Giá trị mặc định tại màn hình thêm mới và màn hình sửa
     * @since 18/11/2014 HienDM
     */
    public void addCustomizeComponentToForm(String label, Component component, String dataType,
            boolean isMandatory, Integer dataLength, String format, String caption, 
            boolean isPassword, boolean visibleAdd, boolean visibleEdit, boolean enableAdd, 
            boolean enableEdit, Object defaultValue) {
        List lstCell = new ArrayList();
        lstCell.add(new Label(ResourceBundleUtils.getLanguageResource(label)));
        if(dataType.equals("date")) {
            if(((PopupDateField)component).getResolution().equals(Resolution.DAY))
                ((PopupDateField)component).setDateFormat("dd/MM/yyyy");
            else 
                ((PopupDateField)component).setDateFormat("dd/MM/yyyy HH:mm:ss");
        }
        lstCell.add(component);
        lstCell.add("");
        lstCell.add(dataType);
        lstCell.add(isMandatory);
        lstCell.add(dataLength);
        lstCell.add(format);
        lstCell.add(ResourceBundleUtils.getLanguageResource(caption));
        lstCell.add(false);
        lstCell.add(isPassword);
        lstCell.add("");
        lstCell.add(false);
        lstCell.add("");
        lstCell.add(visibleAdd);
        lstCell.add(visibleEdit);
        lstCell.add(enableAdd);
        lstCell.add(enableEdit);
        lstCell.add(defaultValue);
        component.setCaption(ResourceBundleUtils.getLanguageResource(caption));
        component.setWidth("90%");
        lstCustomizeComponent.add(lstCell);        
    }

    /**
     * Hàm thêm component tùy chỉnh vào giao diện nhập
     *
     * @param label mô tả
     * @param component thành phần trên giao diện
     * @param dataType loại dữ liệu
     * @param isMandatory bắt buộc nhập
     * @param dataLength độ dài dữ liệu
     * @param format kiểm tra định dạng dữ liệu:
     *     ví dụ đơn giản: int,long,float,double,email
     * @param caption mô tả thêm
     * @param isPassword Có phải password không
     * @param visibleAdd Hiển thị tại màn hình thêm mới
     * @param visibleEdit Hiển thị tại màn hình sửa
     * @param enableAdd Hiệu lực tại màn hình thêm mới
     * @param enableEdit Hiệu lực tại màn hình sửa
     * @param defaultValue Giá trị mặc định tại màn hình thêm mới và màn hình sửa
     * @since 18/11/2014 HienDM
     */
    public void addCustomizeToSearchForm(String label, Component component, String dataType,
            boolean isMandatory, Integer dataLength, String format, String caption, 
            boolean isPassword, boolean visibleAdd, boolean visibleEdit, boolean enableAdd, 
            boolean enableEdit, Object defaultValue) {
        List lstCell = new ArrayList();
        lstCell.add(new Label(ResourceBundleUtils.getLanguageResource(label)));
        if(dataType.equals("date")) {
            if(((PopupDateField)component).getResolution().equals(Resolution.DAY))
                ((PopupDateField)component).setDateFormat("dd/MM/yyyy");
            else
                ((PopupDateField)component).setDateFormat("dd/MM/yyyy HH:mm:ss");
        }
        lstCell.add(component);
        lstCell.add("");
        lstCell.add(dataType);
        lstCell.add(isMandatory);
        lstCell.add(dataLength);
        lstCell.add(format);
        lstCell.add(ResourceBundleUtils.getLanguageResource(caption));
        lstCell.add(true);
        lstCell.add(isPassword);
        lstCell.add("");
        lstCell.add(false);
        lstCell.add("");
        lstCell.add(visibleAdd);
        lstCell.add(visibleEdit);
        lstCell.add(enableAdd);
        lstCell.add(enableEdit);
        lstCell.add(defaultValue);
        component.setCaption(ResourceBundleUtils.getLanguageResource(caption));
        component.setWidth("90%");
        lstCustomizeComponent.add(lstCell);        
    }
    
    /**
     * Hàm thêm TextField vào giao diện nhập
     *
     * @param p Tham số truyền vào
     * @since 18/01/2015 HienDM
     */
    public void addTextFieldToForm(ComponentParameter p) {
        addTextFieldToForm(p.getLabel(), p.getComponent(), p.getDatabaseFieldName(), p.getDataType(),
                p.isMandatory(), p.getDataLength(), p.getFormat(), p.getCaption(), p.isUseToSearch(),
                p.isPassword(), p.getSearchMandatory(), p.isCollapsed(), p.getSearchDefaultValue(),
                p.isVisibleAdd(), p.isVisibleEdit(), p.isEnableAdd(), p.isEnableEdit(), p.getDefaultValue());
    }        
    
    /**
     * Hàm thêm TextField vào giao diện nhập
     *
     * @param label mô tả
     * @param component thành phần trên giao diện
     * @param databaseFieldName tên cột tương ứng trong database
     * @param dataType loại dữ liệu
     * @param isMandatory bắt buộc nhập
     * @param dataLength độ dài dữ liệu
     * @param format kiểm tra định dạng dữ liệu:
     *     ví dụ đơn giản: int,long,float,double,email
     * @param caption mô tả thêm
     * @param useToSearch sử dụng để tìm kiếm
     * @param isPassword Có phải password không
     * @param searchMandatory Bắt buộc nhập khi tìm kiếm (Để lấy dữ liệu mới từ DB) 
     *     ví dụ: date:and_mandatory:100 (kiểu thời gian, luôn bắt buộc nhập, không vượt quá khoảng 100 ngày)
     *     int:or_mandatory (kiểu số nguyên, chỉ bắt buộc nhập khi tất cả trường bắt buộc chưa có dữ liệu)
     * @param isCollapsed Có ẩn cột trên table không
     * @param searchDefaultValue Giá trị tìm kiếm mặc định
     * @param visibleAdd Hiển thị tại màn hình thêm mới
     * @param visibleEdit Hiển thị tại màn hình sửa
     * @param enableAdd Hiệu lực tại màn hình thêm mới
     * @param enableEdit Hiệu lực tại màn hình sửa
     * @param defaultValue Giá trị mặc định tại màn hình thêm mới và màn hình sửa
     * @since 18/11/2014 HienDM
     */
    public void addTextFieldToForm(String label, Component component, 
            String databaseFieldName, String dataType,
            boolean isMandatory, Integer dataLength, String format, String caption, 
            boolean useToSearch, boolean isPassword, String searchMandatory, 
            boolean isCollapsed, Object searchDefaultValue, boolean visibleAdd,
            boolean visibleEdit, boolean enableAdd, boolean enableEdit, Object defaultValue) {
        databaseFieldName = databaseFieldName.toLowerCase();
        dataType = dataType.toLowerCase();
        List lstCell = new ArrayList();
        lstCell.add(new Label(ResourceBundleUtils.getLanguageResource(label)));
        if(dataType.equals("date") && searchMandatory != null) {
            ((PopupDateField)component).setValue((Date)((List)searchDefaultValue).get(0));
            List row = new ArrayList();
            row.add(databaseFieldName);
            row.add(searchDefaultValue);
            lstMandatorySearchValue.add(row);  
        }
        if(dataType.equals("date")) {
            if(((PopupDateField)component).getResolution().equals(Resolution.DAY))
                ((PopupDateField)component).setDateFormat("dd/MM/yyyy");
            else 
                ((PopupDateField)component).setDateFormat("dd/MM/yyyy HH:mm:ss");
        }
        lstCell.add(component);
        lstCell.add(databaseFieldName);
        lstCell.add(dataType);
        lstCell.add(isMandatory);
        lstCell.add(dataLength);
        lstCell.add(format);
        lstCell.add(ResourceBundleUtils.getLanguageResource(caption));
        lstCell.add(useToSearch);
        lstCell.add(isPassword);
        lstCell.add(searchMandatory);
        lstCell.add(isCollapsed);
        lstCell.add(searchDefaultValue);
        lstCell.add(visibleAdd);
        lstCell.add(visibleEdit);
        lstCell.add(enableAdd);
        lstCell.add(enableEdit);
        lstCell.add(defaultValue);
        component.setCaption(ResourceBundleUtils.getLanguageResource(caption));
        component.setWidth("90%");        
        if(databaseFieldName.equals(idColumnName)) {
            idField = lstComponent.size();
        }
        if(databaseFieldName.equals(sortColumnName)) {
            sortField = lstComponent.size();
            sortColumnType = dataType;
        }
        if(databaseFieldName.equals(parentColumnName)) {
            parentField = lstComponent.size();
        }
        if (databaseFieldName.equals(nameColumn)) {
            nameField = lstComponent.size();
        }        
        if(dataType.equals("date")) {
            if(searchMandatory != null && searchDefaultValue != null) {
                PopupDateField pdfToDate = new PopupDateField();
                pdfToDate.setValue((Date)((List)searchDefaultValue).get(1));
                if (((PopupDateField) component).getResolution().equals(Resolution.DAY)) {
                    pdfToDate.setDateFormat("dd/MM/yyyy");
                } else {
                    pdfToDate.setDateFormat("dd/MM/yyyy HH:mm:ss");
                }
                lstCell.add(pdfToDate);
            } else {
                lstCell.add(new PopupDateField());
            }
        }
        lstComponent.add(lstCell);        
    }

    /**
     * Hàm thêm TextArea vào giao diện nhập
     *
     * @param p Tham số truyền vào
     * @since 18/01/2015 HienDM
     */
    public void addTextAreaToForm(ComponentParameter p) {
        addTextFieldToForm(p.getLabel(), p.getComponent(), p.getDatabaseFieldName(), p.getDataType(),
                p.isMandatory(), p.getDataLength(), p.getFormat(), p.getCaption(), p.isUseToSearch(),
                p.isPassword(), p.getSearchMandatory(), p.isCollapsed(), p.getSearchDefaultValue(),
                p.isVisibleAdd(), p.isVisibleEdit(), p.isEnableAdd(), p.isEnableEdit(), p.getDefaultValue());
    } 
    
    /**
     * Hàm thêm TextArea vào giao diện nhập
     *
     * @param label mô tả
     * @param component thành phần trên giao diện
     * @param databaseFieldName tên cột tương ứng trong database
     * @param dataType loại dữ liệu
     * @param isMandatory bắt buộc nhập
     * @param dataLength độ dài dữ liệu
     * @param format kiểm tra định dạng dữ liệu:
     *     ví dụ đơn giản: int,long,float,double,email
     * @param caption mô tả thêm
     * @param useToSearch sử dụng để tìm kiếm
     * @param isPassword Có phải password không
     * @param searchMandatory Bắt buộc nhập khi tìm kiếm (Để lấy dữ liệu mới từ DB) 
     *     ví dụ: date:and_mandatory:100 (kiểu thời gian, luôn bắt buộc nhập, không vượt quá khoảng 100 ngày)
     *     int:or_mandatory (kiểu số nguyên, chỉ bắt buộc nhập khi tất cả trường bắt buộc chưa có dữ liệu)
     * @param isCollapsed Có ẩn cột trên table không
     * @param searchDefaultValue Giá trị tìm kiếm mặc định
     * @param visibleAdd Hiển thị tại màn hình thêm mới
     * @param visibleEdit Hiển thị tại màn hình sửa
     * @param enableAdd Hiệu lực tại màn hình thêm mới
     * @param enableEdit Hiệu lực tại màn hình sửa
     * @param defaultValue Giá trị mặc định tại màn hình thêm mới và màn hình sửa
     * @since 18/11/2014 HienDM
     */
    public void addTextAreaToForm(String label, Component component, 
            String databaseFieldName, String dataType,
            boolean isMandatory, Integer dataLength, String format, String caption, 
            boolean useToSearch, boolean isPassword, String searchMandatory, 
            boolean isCollapsed, Object searchDefaultValue, boolean visibleAdd,
            boolean visibleEdit, boolean enableAdd, boolean enableEdit, Object defaultValue) {
        addTextFieldToForm(label, component, 
            databaseFieldName, dataType,
            isMandatory, dataLength, format, caption, 
            useToSearch, isPassword, searchMandatory, 
            isCollapsed, searchDefaultValue, visibleAdd,
            visibleEdit, enableAdd, enableEdit, defaultValue);        
    }    
    
    /**
     * Hàm thêm component vào giao diện nhập
     *
     * @param p Tham số truyền vào
     * @since 18/01/2015 HienDM
     */
    public void addDateToForm(DateParameter p) {
        addDateToForm(p.getLabel(), p.getComponent(), p.getDatabaseFieldName(), 
                p.isMandatory(), p.getCaption(), p.isUseToSearch(),
                p.getSearchMandatory(), p.isCollapsed(), p.getSearchDefaultValue(),
                p.isVisibleAdd(), p.isVisibleEdit(), p.isEnableAdd(), p.isEnableEdit(), p.getDefaultValue());
    }    
    
    /**
     * Hàm thêm component vào giao diện nhập
     *
     * @param label mô tả
     * @param component thành phần trên giao diện
     * @param databaseFieldName tên cột tương ứng trong database
     * @param isMandatory bắt buộc nhập
     * @param caption mô tả thêm
     * @param useToSearch sử dụng để tìm kiếm
     * @param searchMandatory Bắt buộc nhập khi tìm kiếm (Để lấy dữ liệu mới từ DB) 
     *     ví dụ: date:and_mandatory:100 (kiểu thời gian, luôn bắt buộc nhập, không vượt quá khoảng 100 ngày)
     *     int:or_mandatory (kiểu số nguyên, chỉ bắt buộc nhập khi tất cả trường bắt buộc chưa có dữ liệu)
     * @param isCollapsed Có ẩn cột trên table không
     * @param searchDefaultValue Giá trị tìm kiếm mặc định
     * @param visibleAdd Hiển thị tại màn hình thêm mới
     * @param visibleEdit Hiển thị tại màn hình sửa
     * @param enableAdd Hiệu lực tại màn hình thêm mới
     * @param enableEdit Hiệu lực tại màn hình sửa
     * @param defaultValue Giá trị mặc định tại màn hình thêm mới và màn hình sửa
     * @since 18/11/2014 HienDM
     */
    public void addDateToForm(String label, Component component, 
            String databaseFieldName, boolean isMandatory, String caption, 
            boolean useToSearch, String searchMandatory, 
            boolean isCollapsed, Object searchDefaultValue, boolean visibleAdd,
            boolean visibleEdit, boolean enableAdd, boolean enableEdit, Object defaultValue) {
        databaseFieldName = databaseFieldName.toLowerCase();
        String dataType = "date";
        List lstCell = new ArrayList();
        lstCell.add(new Label(ResourceBundleUtils.getLanguageResource(label)));
        if(dataType.equals("date") && searchMandatory != null) {
            ((PopupDateField)component).setValue((Date)((List)searchDefaultValue).get(0));
            List row = new ArrayList();
            row.add(databaseFieldName);
            row.add(searchDefaultValue);
            lstMandatorySearchValue.add(row);  
        }
        if(dataType.equals("date")) {
            if(((PopupDateField)component).getResolution().equals(Resolution.DAY))
                ((PopupDateField)component).setDateFormat("dd/MM/yyyy");
            else 
                ((PopupDateField)component).setDateFormat("dd/MM/yyyy HH:mm:ss");
        }
        lstCell.add(component);
        lstCell.add(databaseFieldName);
        lstCell.add(dataType);
        lstCell.add(isMandatory);
        lstCell.add(20);
        lstCell.add("date");
        lstCell.add(ResourceBundleUtils.getLanguageResource(caption));
        lstCell.add(useToSearch);
        lstCell.add(false);
        lstCell.add(searchMandatory);
        lstCell.add(isCollapsed);
        lstCell.add(searchDefaultValue);
        lstCell.add(visibleAdd);
        lstCell.add(visibleEdit);
        lstCell.add(enableAdd);
        lstCell.add(enableEdit);
        lstCell.add(defaultValue);
        component.setCaption(ResourceBundleUtils.getLanguageResource(caption));
        component.setWidth("90%");        
        if(databaseFieldName.equals(idColumnName)) {
            idField = lstComponent.size();
        }
        if(databaseFieldName.equals(sortColumnName)) {
            sortField = lstComponent.size();
            sortColumnType = dataType;
        }
        if(databaseFieldName.equals(parentColumnName)) {
            parentField = lstComponent.size();
        }
        if (databaseFieldName.equals(nameColumn)) {
            nameField = lstComponent.size();
        }        
        if(dataType.equals("date")) {
            if(searchMandatory != null && searchDefaultValue != null) {
                PopupDateField pdfToDate = new PopupDateField();
                pdfToDate.setValue((Date)((List)searchDefaultValue).get(1));
                if (((PopupDateField) component).getResolution().equals(Resolution.DAY)) {
                    pdfToDate.setDateFormat("dd/MM/yyyy");
                } else {
                    pdfToDate.setDateFormat("dd/MM/yyyy HH:mm:ss");
                }
                lstCell.add(pdfToDate);
            } else {
                lstCell.add(new PopupDateField());
            }
        }
        lstComponent.add(lstCell);        
    }    
    
    /**
     * Hàm thêm component vào giao diện nhập
     *
     * @param p Tham số truyền vào
     * @since 18/11/2014 HienDM
     */    
    public void addComponentOnlyViewToForm(OnlyInViewParameter p) {
        addComponentOnlyViewToForm(p.getLabel(), p.getDatabaseFieldName(), p.getCaption(), 
                p.isUseToSearch(), p.getSearchMandatory(), p.isCollapsed(), p.getSearchDefaultValue());
    }
    
    /**
     * Hàm thêm component vào giao diện nhập
     *
     * @param label mô tả
     * @param databaseFieldName tên cột tương ứng trong database
     * @param caption mô tả thêm
     * @param useToSearch sử dụng để tìm kiếm
     * @param searchMandatory Bắt buộc nhập khi tìm kiếm (Để lấy dữ liệu mới từ DB) 
     *     ví dụ: date:and_mandatory:100 (kiểu thời gian, luôn bắt buộc nhập, không vượt quá khoảng 100 ngày)
     *     int:or_mandatory (kiểu số nguyên, chỉ bắt buộc nhập khi tất cả trường bắt buộc chưa có dữ liệu)
     * @param isCollapsed Có ẩn cột trên table không
     * @param searchDefaultValue Giá trị tìm kiếm mặc định
     * @since 18/11/2014 HienDM
     */
    public void addComponentOnlyViewToForm(String label,
            String databaseFieldName, String caption,
            boolean useToSearch, String searchMandatory, 
            boolean isCollapsed, Object searchDefaultValue) {
        databaseFieldName = databaseFieldName.toLowerCase();
        List lstCell = new ArrayList();
        lstCell.add(new Label(ResourceBundleUtils.getLanguageResource(label)));
        TextField component = new TextField();
        lstCell.add(component);
        lstCell.add(databaseFieldName);
        lstCell.add("string");
        lstCell.add(false);
        lstCell.add("999999999");
        lstCell.add("OnlyView");
        lstCell.add(ResourceBundleUtils.getLanguageResource(caption));
        lstCell.add(useToSearch);
        lstCell.add(false);
        lstCell.add(searchMandatory);
        lstCell.add(isCollapsed);
        lstCell.add(searchDefaultValue);
        lstCell.add(false);
        lstCell.add(false);
        lstCell.add(true);
        lstCell.add(true);
        lstCell.add("");
        component.setCaption(ResourceBundleUtils.getLanguageResource(caption));
        component.setWidth("90%");        
        if(databaseFieldName.equals(idColumnName)) {
            idField = lstComponent.size();
        }
        if(databaseFieldName.equals(sortColumnName)) {
            sortField = lstComponent.size();
            sortColumnType = "string";
        }
        if(databaseFieldName.equals(parentColumnName)) {
            parentField = lstComponent.size();
        }
        if (databaseFieldName.equals(nameColumn)) {
            nameField = lstComponent.size();
        }        

        lstComponent.add(lstCell);        
    }    
    
    /**
     * Hàm thêm component vào giao diện nhập
     *
     * @param label mô tả
     * @param databaseFieldName tên cột tương ứng trong database
     * @param dataType loại dữ liệu
     * @param isMandatory bắt buộc nhập
     * @param dataLength độ dài dữ liệu
     * @param format kiểm tra định dạng dữ liệu: ví dụ đơn giản:
     * int,long,float,double,email
     * @since 01/02/2015 HienDM
     */
    public List addComponentToMultiPopup(String label,
            String databaseFieldName, String dataType,
            boolean isMandatory, Integer dataLength, String format) {
        databaseFieldName = databaseFieldName.toLowerCase();
        dataType = dataType.toLowerCase();
        List lstCell = new ArrayList();
        lstCell.add(new Label(ResourceBundleUtils.getLanguageResource(label)));
        lstCell.add(new TextField());
        lstCell.add(databaseFieldName);
        lstCell.add(dataType);
        lstCell.add(isMandatory);
        lstCell.add(dataLength);
        lstCell.add(format);
        lstCell.add(null);
        lstCell.add(false); // useToSearch
        lstCell.add(false); // isPassword
        lstCell.add(null); // searchMandatory
        lstCell.add(false);
        lstCell.add(null);
        lstCell.add(true);
        lstCell.add(true);
        lstCell.add(true);
        lstCell.add(true);
        lstCell.add(null);
        return lstCell;
    }
    
    /**
     * Hàm thêm component là một sysdate không nhập trên giao diện
     *
     * @param p Tham số truyền vào
     * @since 18/01/2015 HienDM
     */
    public void setComponentAsSysdate(SysdateParameter p) throws Exception {
        setComponentAsSysdate(p.getLabel(), p.getDatabaseFieldName(), p.isUseToSearch(), 
                p.getSearchMandatory(), p.isCollapsed(), p.getSearchDefaultValue());
    }
    
    /**
     * Hàm thêm component là một sysdate không nhập trên giao diện
     *
     * @param label mô tả
     * @param databaseFieldName tên cột tương ứng trong database
     * @param useToSearch sử dụng để tìm kiếm
     * @param searchMandatory Bắt buộc nhập khi tìm kiếm (Để lấy dữ liệu mới từ DB) 
     *     ví dụ: date:and_mandatory:100 (kiểu thời gian, luôn bắt buộc nhập, không vượt quá khoảng 100 ngày)
     *     int:or_mandatory (kiểu số nguyên, chỉ bắt buộc nhập khi tất cả trường bắt buộc chưa có dữ liệu)
     * @param isCollapsed Có ẩn cột trên table không
     * @param searchDefaultValue Giá trị tìm kiếm mặc định
     * @since 18/11/2014 HienDM
     */
    public void setComponentAsSysdate(String label, String databaseFieldName, boolean useToSearch, 
            String searchMandatory, boolean isCollapsed, Object searchDefaultValue) throws Exception {
        databaseFieldName = databaseFieldName.toLowerCase();
        List lstCell = new ArrayList();
        lstCell.add(new Label(ResourceBundleUtils.getLanguageResource(label)));
        PopupDateField component = new PopupDateField();
        component.setResolution(Resolution.SECOND);
        if(((PopupDateField) component).getResolution().equals(Resolution.DAY)) {
            component.setDateFormat("dd/MM/yyyy");
        } else {
            component.setDateFormat("dd/MM/yyyy HH:mm:ss");
        }
        if (searchMandatory != null) {
            ((PopupDateField) component).setValue((Date) ((List) searchDefaultValue).get(0));
            List row = new ArrayList();
            row.add(databaseFieldName);
            row.add(searchDefaultValue);
            lstMandatorySearchValue.add(row);
        }        
        lstCell.add(component);
        lstCell.add(databaseFieldName);
        lstCell.add("date"); //data type
        lstCell.add(false); //mandatory
        lstCell.add(null); //data length
        lstCell.add("date"); //format
        lstCell.add(""); //caption
        lstCell.add(useToSearch); 
        lstCell.add(false); //is password
        lstCell.add(searchMandatory); // is search mandatory
        lstCell.add(isCollapsed); // isCollapsed
        lstCell.add(searchDefaultValue);
        lstCell.add(false); //visibleAdd
        lstCell.add(false); //visibleEdit
        lstCell.add(true); //enableAdd
        lstCell.add(true); //enableEdit
        lstCell.add(null); //defaultValue
        if (databaseFieldName.equals(idColumnName)) {
            idField = lstComponent.size();
        }
        if (databaseFieldName.equals(sortColumnName)) {
            sortField = lstComponent.size();
            sortColumnType = "date";
        }
        if (databaseFieldName.equals(nameColumn)) {
            nameField = lstComponent.size();
        }
        if (searchMandatory != null && searchDefaultValue != null) {
            PopupDateField pdfToDate = new PopupDateField();
            if(((PopupDateField) component).getResolution().equals(Resolution.DAY)) {
                pdfToDate.setDateFormat("dd/MM/yyyy");
            } else {
                pdfToDate.setDateFormat("dd/MM/yyyy HH:mm:ss");
                pdfToDate.setResolution(Resolution.SECOND);
            }
            ((PopupDateField) pdfToDate).setValue((Date) ((List) searchDefaultValue).get(1));
            lstCell.add(pdfToDate);
        } else {
            lstCell.add(new PopupDateField());
        }      
        lstCell.add(true); //set as sysdate (INT_SYSDATE)        
        lstComponent.add(lstCell);
    }
    
    /**
     * Hàm thêm component là tên đăng nhập không nhập trên giao diện
     *
     * @param p tham số truyền vào
     * @since 18/01/2015 HienDM
     */
    public void setComponentAsLoginUser(LoginUserParameter p) throws Exception {
        setComponentAsLoginUser(p.getLabel(), p.getDatabaseFieldName(), p.isUseToSearch(), 
                p.getSearchMandatory(), p.isCollapsed(), p.getSearchDefaultValue(), p.isOnlyView(), 
                p.isOnlyEdit(), p.getPopup(), p.getFilterParentColumn(), p.getFilterChildColumn(),
                p.getOnlyViewGroup());
    }
    
    /**
     * Hàm thêm component là tên đăng nhập không nhập trên giao diện
     *
     * @param label mô tả
     * @param databaseFieldName tên cột tương ứng trong database
     * @param useToSearch sử dụng để tìm kiếm
     * @param searchMandatory Bắt buộc nhập khi tìm kiếm (Để lấy dữ liệu mới từ
     * DB) ví dụ: date:and_mandatory:100 (kiểu thời gian, luôn bắt buộc nhập,
     * không vượt quá khoảng 100 ngày) int:or_mandatory (kiểu số nguyên, chỉ bắt
     * buộc nhập khi tất cả trường bắt buộc chưa có dữ liệu)
     * @param isCollapsed Có ẩn cột trên table không
     * @param searchDefaultValue Giá trị tìm kiếm mặc định 
     * @param onlyView chỉ được xem bản ghi do mình tạo ra
     * @param onlyEdit chỉ được sửa bản ghi do mình tạo ra
     * @param popup Popup chọn người dùng
     * 2: chỉ được xem bản ghi từ group của mình trở xuống
     * @param filterParentColumn Tên cột trong bảng của chức năng hiện tại của component cha khi filter
     * @param filterChildColumn Tên cột trong bảng quan hệ cha con của component cha khi filter
     * @param onlyViewGroup 0: được xem tất, 1: chỉ được xem bản ghi trong group của mình
     * @since 18/11/2014 HienDM
     */
    public void setComponentAsLoginUser(String label, String databaseFieldName, boolean useToSearch,
            String searchMandatory, boolean isCollapsed, Object searchDefaultValue, boolean onlyView,
            boolean onlyEdit, PopupSingleAction popup, String filterParentColumn, String filterChildColumn, 
            int onlyViewGroup
    ) throws Exception{
        databaseFieldName = databaseFieldName.toLowerCase();
        ComboBox component = new ComboBox();
        component.setEnabled(false);
        List lstCell = new ArrayList();
        lstCell.add(new Label(ResourceBundleUtils.getLanguageResource(label)));
        component.setNullSelectionAllowed(true);
        lstCell.add(component);
        lstCell.add(databaseFieldName);
        lstCell.add("long");
        lstCell.add(false);
        lstCell.add(null);
        lstCell.add(null);
        lstCell.add(null);
        lstCell.add(useToSearch);
        lstCell.add(false); // ispassword
        lstCell.add(searchMandatory); // is search mandatory
        lstCell.add(isCollapsed);
        lstCell.add(searchDefaultValue);
        lstCell.add(false); //visibleAdd
        lstCell.add(false); //visibleEdit
        lstCell.add(true); //enableAdd
        lstCell.add(true); //enableEdit
        lstCell.add(null); // default value
        lstCell.add(null);
        lstCell.add(false);
        lstCell.add(false);
        lstCell.add("");
        lstCell.add(null);
        lstCell.add("user_id");
        lstCell.add("");
        lstCell.add("user_name");
        lstCell.add("sm_users");
        component.setCaption(null);
        component.setSizeFull();
        Button btnBrowse = new Button("+");
        btnBrowse.setWidth("30px");
        btnBrowse.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    Window popupWindow = new Window();
                    popupWindow.setWidth(popupWidth);
                    popupWindow.setHeight(popupHeight);
                    popupWindow.setCaption(ResourceBundleUtils.getLanguageResource(label));
                    VerticalLayout bodyPage = new VerticalLayout();
                    bodyPage.addStyleName("page-body");
                    if(filterParentColumn != null && !filterParentColumn.isEmpty()) {
                        List lstParameter = new ArrayList();
                        for(int i = 0; i < lstComponent.size(); i++) {
                            if(lstComponent.get(i).get(INT_DB_FIELD_NAME).equals(filterParentColumn)) {
                                if(lstComponent.get(i).get(INT_COMPONENT) instanceof AbstractField) {
                                    if(((AbstractField)lstComponent.get(i).get(INT_COMPONENT)).getValue() != null) {
                                        popup.setIsChangeDefaultSearch(true);
                                        lstParameter.add(((AbstractField)lstComponent.get(i).get(INT_COMPONENT)).getValue());
                                        popup.queryWhereCondition = popup.queryWhereFilter;
                                        popup.queryWhereParameter = (ArrayList)((ArrayList)popup.queryWhereFilterParameter).clone();
                                        popup.addQueryWhereParameter(lstParameter);
                                        String strQuery = " and " + filterChildColumn + " = ? ";
                                        popup.addQueryWhereCondition(strQuery);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    
                    bodyPage.addComponent(popup.initPanel(2, component, popupWindow));
                    popupWindow.setContent(bodyPage);
                    popupWindow.setModal(true);
                    mainUI.addWindow(popupWindow);
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        Button btnDelete = new Button("-");
        btnDelete.setWidth("30px");
        btnDelete.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    component.setValue(null);
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        lstCell.add(btnBrowse);
        lstCell.add(btnDelete);
        lstCell.add("LoginUser");
        lstCell.add(onlyView);
        lstCell.add(onlyEdit);
        lstCell.add(onlyViewGroup);
        lstComponent.add(lstCell);
        if (databaseFieldName.equals(idColumnName)) {
            idField = lstComponent.size() - 1;
        }
        if (databaseFieldName.equals(sortColumnName)) {
            sortField = lstComponent.size() - 1;
            sortColumnType = "long";
        }
        if (databaseFieldName.equals(parentColumnName)) {
            parentField = lstComponent.size() - 1;
        }
        if (databaseFieldName.equals(nameColumn)) {
            nameField = lstComponent.size() - 1;
        } 
    }    
    
    /**
     * Hàm thêm checkbox vào giao diện nhập
     *
     * @param p Tham số truyền vào
     * @since 18/01/2015 HienDM
     */
    public void addCheckBoxToForm (CheckBoxParameter p) throws Exception {
        addCheckBoxToForm(p.getLabel(), p.getComponent(), p.getDatabaseFieldName(), p.getDataType(), p.isMandatory(),
                p.getDataLength(), p.getFormat(), p.getCaption(), p.isUseToSearch(), p.isDefaultValue(), p.getSearchMandatory(),
                p.isCollapsed(), p.getSearchDefaultValue(), p.isVisibleAdd(), p.isVisibleEdit(), p.isEnableAdd(), p.isEnableEdit(),
                p.getEnable(), p.getDisable());
    }
    
    /**
     * Hàm thêm checkbox vào giao diện nhập
     *
     * @param label mô tả
     * @param component thành phần trên giao diện
     * @param databaseFieldName tên cột tương ứng trong database
     * @param dataType loại dữ liệu
     * @param isMandatory bắt buộc nhập
     * @param dataLength độ dài dữ liệu
     * @param format kiểm tra định dạng dữ liệu:
     *     ví dụ đơn giản: int,long,float,double,email
     * @param caption mô tả thêm
     * @param useToSearch sử dụng để tìm kiếm
     * @param defaultValue có phải password không
     * @param searchMandatory Bắt buộc nhập khi tìm kiếm (Để lấy dữ liệu mới từ DB) 
     *     ví dụ: date:and_mandatory:100 (kiểu thời gian, luôn bắt buộc nhập, không vượt quá khoảng 100 ngày)
     *     int:or_mandatory (kiểu số nguyên, chỉ bắt buộc nhập khi tất cả trường bắt buộc chưa có dữ liệu)
     * @param isCollapsed Có ẩn cột trên table không
     * @param searchDefaultValue Giá trị tìm kiếm mặc định
     * @param visibleAdd Hiển thị tại màn hình thêm mới
     * @param visibleEdit Hiển thị tại màn hình sửa
     * @param enableAdd Hiệu lực tại màn hình thêm mới
     * @param enableEdit Hiệu lực tại màn hình sửa
     * @param enable mô tả trạng thái checkbox enable
     * @param disable mô tả trạng thái checkbox disable
     * @since 18/11/2014 HienDM
     */
    public void addCheckBoxToForm(String label, CheckBox component, 
            String databaseFieldName, String dataType,
            boolean isMandatory, Integer dataLength, String format, String caption,
            boolean useToSearch, boolean defaultValue, String searchMandatory, boolean isCollapsed, 
            Object searchDefaultValue, boolean visibleAdd, boolean visibleEdit, boolean enableAdd, 
            boolean enableEdit, String enable, String disable) throws Exception {
        databaseFieldName = databaseFieldName.toLowerCase();
        dataType = dataType.toLowerCase();
        List lstCell = new ArrayList();
        lstCell.add(new Label(ResourceBundleUtils.getLanguageResource(label)));
        lstCell.add(component);
        lstCell.add(databaseFieldName);
        lstCell.add(dataType);
        lstCell.add(isMandatory);
        lstCell.add(dataLength);
        lstCell.add(format);
        lstCell.add(ResourceBundleUtils.getLanguageResource(caption));
        lstCell.add(useToSearch);
        lstCell.add(defaultValue);
        lstCell.add(searchMandatory); // is search mandatory
        lstCell.add(isCollapsed); // is collapsed
        lstCell.add(searchDefaultValue);
        lstCell.add(visibleAdd);
        lstCell.add(visibleEdit); 
        lstCell.add(enableAdd);
        lstCell.add(enableEdit); 
        lstCell.add(ResourceBundleUtils.getLanguageResource(enable));
        lstCell.add(ResourceBundleUtils.getLanguageResource(disable));
        component.setCaption(ResourceBundleUtils.getLanguageResource(caption));
        component.setWidth("90%");
        lstComponent.add(lstCell);
        if (databaseFieldName.equals(idColumnName)) {
            idField = lstComponent.size() - 1;
        }
        if (databaseFieldName.equals(sortColumnName)) {
            sortField = lstComponent.size() - 1;
            sortColumnType = dataType;
        }
        if(databaseFieldName.equals(parentColumnName)) {
            parentField = lstComponent.size() - 1;
        }
        if (databaseFieldName.equals(nameColumn)) {
            nameField = lstComponent.size() - 1;
        }   
    }
    
    /**
     * Hàm thêm combobox dữ liệu từ database vào giao diện nhập
     * 
     * @param p tham số truyền vào
     * @since 18/01/2015 HienDM
     */
    public void addComboBoxToForm(ComboBoxParameter p) throws Exception {
        addComboBoxToForm(p.getLabel(), p.getComponent(), p.getDatabaseFieldName(), p.getDataType(), p.isMandatory(),
                p.getDataLength(), p.getFormat(), p.getCaption(), p.isUseToSearch(), p.isPassword(), p.getSearchMandatory(),
                p.isCollapsed(), p.getSearchDefaultValue(), p.isVisibleAdd(), p.isVisibleEdit(), p.isEnableAdd(), 
                p.isEnableEdit(), p.getQuery(), p.getLstParameter(), p.getCboTableName(), p.getIdColumn(), p.getIdType(),
                p.getNameColumn(), p.getDefaultValue(), p.getDefaultCaption(), p.isRefresh(), p.isMultiLanguage(), 
                p.getFilterParentColumn(), p.getFilterChildColumn());
    }
    
    /**
     * Hàm thêm combobox dữ liệu từ database vào giao diện nhập
     *
     * @param label mô tả
     * @param component thành phần trên giao diện
     * @param databaseFieldName tên cột tương ứng trong database
     * @param dataType loại dữ liệu
     * @param isMandatory bắt buộc nhập
     * @param dataLength độ dài dữ liệu
     * @param format kiểm tra định dạng dữ liệu:
     *     ví dụ đơn giản: int,long,float,double,email
     * @param caption mô tả thêm
     * @param useToSearch sử dụng để tìm kiếm
     * @param isPassword có phải password không
     * @param searchMandatory Bắt buộc nhập khi tìm kiếm (Để lấy dữ liệu mới từ DB) 
     *     ví dụ: date:and_mandatory:100 (kiểu thời gian, luôn bắt buộc nhập, không vượt quá khoảng 100 ngày)
     *     int:or_mandatory (kiểu số nguyên, chỉ bắt buộc nhập khi tất cả trường bắt buộc chưa có dữ liệu)
     * @param isCollapsed Có ẩn cột trên table không
     * @param searchDefaultValue Giá trị tìm kiếm mặc định
     * @param visibleAdd Hiển thị tại màn hình thêm mới
     * @param visibleEdit Hiển thị tại màn hình sửa
     * @param enableAdd Hiệu lực tại màn hình thêm mới
     * @param enableEdit Hiệu lực tại màn hình sửa
     * @param query câu lệnh truy vấn cơ sở dữ liệu
     * @param lstParameter danh sách tham số cho câu lệnh truy vấn
     * @param cboTableName tên bảng dữ liệu
     * @param idColumn trường dữ liệu id của combobox trong database
     * @param idType kiểu dữ liệu id của combobox trong database
     * @param nameColumn tên dữ liệu trong combobox
     * @param defaultValue id dữ liệu mặc định
     * @param defaultCaption tên dữ liệu mặc định
     * @param isRefresh cập nhật dữ liệu sau khi thêm, sửa, xóa
     * @param isMultiLanguage đa ngôn ngữ
     * @param filterParentColumn trường dữ liệu dùng để filter của component cha
     * @param filterChildColumn trường dữ liệu dùng đề filter của component con
     * @since 18/11/2014 HienDM
     */
    public void addComboBoxToForm(String label, ComboBox component, 
            String databaseFieldName, String dataType,
            boolean isMandatory, Integer dataLength, String format, String caption,
            boolean useToSearch, boolean isPassword, String searchMandatory, boolean isCollapsed, 
            Object searchDefaultValue, boolean visibleAdd, boolean visibleEdit, boolean enableAdd, 
            boolean enableEdit, String query, List lstParameter, 
            String cboTableName, String idColumn, String idType, String nameColumn,
            String defaultValue, String defaultCaption, boolean isRefresh, boolean isMultiLanguage,
            String filterParentColumn, String filterChildColumn
    ) throws Exception {
        if(databaseFieldName != null)databaseFieldName = databaseFieldName.toLowerCase();
        if(idColumn != null)idColumn = idColumn.toLowerCase();
        if(nameColumn != null)nameColumn = nameColumn.toLowerCase();
        if(cboTableName != null)cboTableName = cboTableName.toLowerCase();
        if(filterParentColumn != null)filterParentColumn = filterParentColumn.toLowerCase();
        if(filterChildColumn != null)filterChildColumn = filterChildColumn.toLowerCase();
        dataType = dataType.toLowerCase();
        idType=idType.toLowerCase();
        List lstCell = new ArrayList();
        lstCell.add(new Label(ResourceBundleUtils.getLanguageResource(label)));
        component.setNullSelectionAllowed(!isMandatory); 
        component.setPageLength(30);
        component.setSizeFull();
        component.setCaption(ResourceBundleUtils.getLanguageResource(caption));
        component.setWidth("90%");
        List<Map> lstData = null;
        if (lstParameter != null) {
            lstData = C3p0Connector.queryData(query, lstParameter);
        } else {
            lstData = C3p0Connector.queryData(query);
        }
        if(filterParentColumn == null) {
            for(int i = 0; i < lstData.size(); i++) {
                Map rows = lstData.get(i);
                Object celldata = new Object();

                celldata = rows.get(idColumn);
                if (celldata != null) component.addItem(celldata.toString());
                else component.addItem("");

                String cellCaption = "";
                if(rows.get(nameColumn) != null) {
                    if(isMultiLanguage)
                        cellCaption = ResourceBundleUtils.getLanguageResource(rows.get(nameColumn).toString());
                    else
                        cellCaption = rows.get(nameColumn).toString();
                }
                if (cellCaption != null) component.setItemCaption(celldata.toString(), cellCaption);
            }
            if (searchMandatory != null) {
                component.setValue(searchDefaultValue);
                List row = new ArrayList();
                row.add(databaseFieldName);
                row.add(searchDefaultValue);
                lstMandatorySearchValue.add(row);
            }
        } else {
            searchMandatory = null;
        }
        lstCell.add(component);
        lstCell.add(databaseFieldName);
        lstCell.add(dataType);
        lstCell.add(isMandatory);
        lstCell.add(dataLength);
        lstCell.add(format);
        lstCell.add(ResourceBundleUtils.getLanguageResource(caption));
        lstCell.add(useToSearch);
        lstCell.add(isPassword);
        lstCell.add(searchMandatory); // is search mandatory
        lstCell.add(isCollapsed);
        lstCell.add(searchDefaultValue);
        lstCell.add(visibleAdd);
        lstCell.add(visibleEdit); 
        lstCell.add(enableAdd);
        lstCell.add(enableEdit); 
        lstCell.add(defaultValue);
        lstCell.add(ResourceBundleUtils.getLanguageResource(defaultCaption));
        lstCell.add(isRefresh);
        lstCell.add(isMultiLanguage);        
        lstCell.add(query);
        lstCell.add(lstParameter);
        lstCell.add(idColumn);
        lstCell.add(idType);
        lstCell.add(nameColumn);
        lstCell.add(cboTableName);        
        lstCell.add(filterParentColumn);
        lstCell.add(filterChildColumn);
        lstCell.add(lstData);
        if(filterParentColumn != null) {
            boolean check = true;
            for(int i = 0; i < lstFilter.size(); i++) {
                if(lstFilter.get(i).get(0).equals(filterParentColumn)) {
                    ((List)lstFilter.get(i).get(1)).add(databaseFieldName);
                    check = false;
                }
            }
            if(check) {
                List lstRow = new ArrayList();
                lstRow.add(filterParentColumn);
                List lstChild = new ArrayList();
                lstChild.add(databaseFieldName);
                lstRow.add(lstChild);
                lstFilter.add(lstRow);
            }
        }
        lstComponent.add(lstCell);
        if (databaseFieldName.equals(idColumnName)) {
            idField = lstComponent.size() - 1;
        }
        if (databaseFieldName.equals(sortColumnName)) {
            sortField = lstComponent.size() - 1;
            sortColumnType = dataType;
        }
        if(databaseFieldName.equals(parentColumnName)) {
            parentField = lstComponent.size() - 1;
        }
        if (databaseFieldName.equals(nameColumn)) {
            nameField = lstComponent.size() - 1;
        }   
    }
    
    /**
     * Hàm thêm combobox dữ liệu từ database vào giao diện nhập
     * 
     * @param p tham số truyền vào
     * @since 18/01/2015 HienDM
     */
    public void addComboBoxToForm(ComboBoxConstantParameter p) throws Exception {
        addComboBoxToForm(p.getLabel(), p.getComponent(), p.getDatabaseFieldName(), p.getDataType(), p.isMandatory(),
                p.getDataLength(), p.getFormat(), p.getCaption(), p.isUseToSearch(), p.isPassword(), p.getSearchMandatory(),
                p.isCollapsed(), p.getSearchDefaultValue(), p.isVisibleAdd(), p.isVisibleEdit(), p.isEnableAdd(),
                p.isEnableEdit(), p.getData(), p.getDefaultValue(), p.getDefaultCaption());
    }
    
    /**
     * Hàm thêm combobox dữ liệu fix cứng vào giao diện nhập
     *
     * @param label mô tả
     * @param component thành phần trên giao diện
     * @param databaseFieldName tên cột tương ứng trong database
     * @param dataType loại dữ liệu
     * @param isMandatory bắt buộc nhập
     * @param dataLength độ dài dữ liệu
     * @param format kiểm tra định dạng dữ liệu:
     *     ví dụ đơn giản: int,long,float,double,email
     * @param caption mô tả thêm
     * @param useToSearch dùng để tìm kiếm
     * @param isPassword có phải password không
     * @param searchMandatory Bắt buộc nhập khi tìm kiếm (Để lấy dữ liệu mới từ DB) 
     *     ví dụ: date:and_mandatory:100 (kiểu thời gian, luôn bắt buộc nhập, không vượt quá khoảng 100 ngày)
     *     int:or_mandatory (kiểu số nguyên, chỉ bắt buộc nhập khi tất cả trường bắt buộc chưa có dữ liệu)
     * @param isCollapsed Có ẩn cột trên table không
     * @param searchDefaultValue Giá trị tìm kiếm mặc định
     * @param visibleAdd Hiển thị tại màn hình thêm mới
     * @param visibleEdit Hiển thị tại màn hình sửa
     * @param enableAdd Hiệu lực tại màn hình thêm mới
     * @param enableEdit Hiệu lực tại màn hình sửa
     * @param data dữ liệu fix cứng
     * @param defaultValue id dữ liệu mặc định
     * @param defaultCaption tên dữ liệu mặc định
     * @since 18/11/2014 HienDM
     */
    public void addComboBoxToForm(String label, ComboBox component,
            String databaseFieldName, String dataType,
            boolean isMandatory, Integer dataLength, String format, String caption,
            boolean useToSearch, boolean isPassword, String searchMandatory, boolean isCollapsed,
            Object searchDefaultValue, boolean visibleAdd, boolean visibleEdit, boolean enableAdd, 
            boolean enableEdit, Object[][] data, String defaultValue, String defaultCaption
    ) throws Exception {
        if(databaseFieldName != null)databaseFieldName = databaseFieldName.toLowerCase();
        dataType = dataType.toLowerCase();
        List lstCell = new ArrayList();
        lstCell.add(new Label(ResourceBundleUtils.getLanguageResource(label)));      
        for (int i = 0; i < data.length; i++) {
            if(data[i][0] != null) component.addItem(data[i][0].toString());
            if(data[i][0] != null && data[i][1] != null) 
                component.setItemCaption(
                    data[i][0].toString(), ResourceBundleUtils.getLanguageResource(data[i][1].toString()));
        }
        component.setSizeFull();
        if (searchMandatory != null) {
            component.setValue(searchDefaultValue);
            List row = new ArrayList();
            row.add(databaseFieldName);
            row.add(searchDefaultValue);
            lstMandatorySearchValue.add(row);
        }
        component.setNullSelectionAllowed(!isMandatory);
        lstCell.add(component);
        lstCell.add(databaseFieldName);
        lstCell.add(dataType);
        lstCell.add(isMandatory);
        lstCell.add(dataLength);
        lstCell.add(format);
        lstCell.add(ResourceBundleUtils.getLanguageResource(caption));
        lstCell.add(useToSearch);
        lstCell.add(isPassword);
        lstCell.add(searchMandatory); // is search mandatory
        lstCell.add(isCollapsed);
        lstCell.add(searchDefaultValue);
        lstCell.add(visibleAdd);
        lstCell.add(visibleEdit); 
        lstCell.add(enableAdd);
        lstCell.add(enableEdit); 
        lstCell.add(defaultValue);
        lstCell.add(ResourceBundleUtils.getLanguageResource(defaultCaption));
        component.setCaption(ResourceBundleUtils.getLanguageResource(caption));
        component.setWidth("90%");
        lstComponent.add(lstCell);
        if (databaseFieldName.equals(idColumnName)) {
            idField = lstComponent.size() - 1;
        }
        if (databaseFieldName.equals(sortColumnName)) {
            sortField = lstComponent.size() - 1;
            sortColumnType = dataType;
        }
        if(databaseFieldName.equals(parentColumnName)) {
            parentField = lstComponent.size() - 1;
        }
        if (databaseFieldName.equals(nameColumn)) {
            nameField = lstComponent.size() - 1;
        }   
    }
    
    /**
     * Hàm thêm Single popup vào giao diện nhập
     *
     * @param p tham số truyền vào
     * @since 18/01/2015 HienDM
     */
    public void addSinglePopupToForm(PopupSingleParameter p) throws Exception {
        addSinglePopupToForm(p.getLabel(), p.getDatabaseFieldName(), p.getDataType(), p.isMandatory(), p.getDataLength(),
                p.getFormat(), p.getCaption(), p.isUseToSearch(), p.getSearchMandatory(), p.isCollapsed(), p.getSearchDefaultValue(),
                p.isVisibleAdd(), p.isVisibleEdit(), p.isEnableAdd(), p.isEnableEdit(), p.getPopup(), p.getColumn(), 
                p.getDefaultValue(), p.getDefaultCaption(), p.getIdColumn(), p.getNameColumn(), p.getCboTableName(), 
                p.getFilterParentColumn(), p.getFilterChildColumn());
    }

    /**
     * Hàm thực hiện trước khi mở popup
     *
     * @since 18/01/2015 HienDM
     */
    public void prepareOpenPopup() throws Exception {}

    /**
     * Hàm thêm Single popup vào giao diện nhập
     *
     * @param label mô tả
     * @param databaseFieldName tên cột tương ứng trong database
     * @param dataType loại dữ liệu
     * @param isMandatory bắt buộc nhập
     * @param dataLength độ dài dữ liệu
     * @param format kiểm tra định dạng dữ liệu:
     *     ví dụ đơn giản: int,long,float,double,email
     * @param caption mô tả thêm
     * @param useToSearch dùng để tìm kiếm
     * @param searchMandatory Bắt buộc nhập khi tìm kiếm (Để lấy dữ liệu mới từ DB) 
     *     ví dụ: date:and_mandatory:100 (kiểu thời gian, luôn bắt buộc nhập, không vượt quá khoảng 100 ngày)
     *     int:or_mandatory (kiểu số nguyên, chỉ bắt buộc nhập khi tất cả trường bắt buộc chưa có dữ liệu)
     * @param isCollapsed Có ẩn cột trên table không
     * @param searchDefaultValue Giá trị tìm kiếm mặc định
     * @param visibleAdd Hiển thị tại màn hình thêm mới
     * @param visibleEdit Hiển thị tại màn hình sửa
     * @param enableAdd Hiệu lực tại màn hình thêm mới
     * @param enableEdit Hiệu lực tại màn hình sửa
     * @param popup popup tìm kiếm dữ liệu
     * @param column số cột trình bày giao diện
     * @param defaultValue id dữ liệu mặc định
     * @param defaultCaption tên dữ liệu mặc định
     * @param idColumn trường dữ liệu id của combobox trong database
     * @param nameColumn tên dữ liệu trong combobox
     * @param cboTableName tên bảng dữ liệu
     * @param filterParentColumn trường dữ liệu dùng để filter của component cha
     * @param filterChildColumn trường dữ liệu dùng để filter của component con
     * @since 18/11/2014 HienDM
     */
    public void addSinglePopupToForm(String label, String databaseFieldName, String dataType,
            boolean isMandatory, Integer dataLength, String format, String caption,
            boolean useToSearch, String searchMandatory, boolean isCollapsed,
            Object searchDefaultValue, boolean visibleAdd, boolean visibleEdit, boolean enableAdd,
            boolean enableEdit, PopupSingleAction popup, Integer column, String defaultValue,
            String defaultCaption, String idColumn, String nameColumn, String cboTableName,
            String filterParentColumn, String filterChildColumn
    ) throws Exception {
        if(databaseFieldName != null)databaseFieldName = databaseFieldName.toLowerCase();
        if(idColumn != null)idColumn = idColumn.toLowerCase();
        if(nameColumn != null)nameColumn = nameColumn.toLowerCase();
        if(cboTableName != null)cboTableName = cboTableName.toLowerCase();
        dataType = dataType.toLowerCase();
        ComboBox component = new ComboBox();
        component.setEnabled(false);
        List lstCell = new ArrayList();
        lstCell.add(new Label(ResourceBundleUtils.getLanguageResource(label)));
        component.setNullSelectionAllowed(true);
        lstCell.add(component);
        lstCell.add(databaseFieldName);
        lstCell.add(dataType);
        lstCell.add(isMandatory);
        lstCell.add(dataLength);
        lstCell.add(format);
        lstCell.add(ResourceBundleUtils.getLanguageResource(caption));
        lstCell.add(useToSearch);
        lstCell.add(false); // ispassword
        lstCell.add(searchMandatory); // is search mandatory
        lstCell.add(isCollapsed);
        lstCell.add(searchDefaultValue);
        lstCell.add(visibleAdd);
        lstCell.add(visibleEdit); 
        lstCell.add(enableAdd);
        lstCell.add(enableEdit); 
        lstCell.add(defaultValue); // default value
        BaseDAO baseDao = new BaseDAO();
        if(defaultValue != null && !defaultValue.trim().isEmpty())
            lstCell.add(baseDao.getNameById(idColumn, nameColumn, cboTableName, defaultValue));
        else
            lstCell.add(null);
        lstCell.add(false);
        lstCell.add(false);
        lstCell.add("");
        lstCell.add(null);
        lstCell.add(idColumn);
        lstCell.add("");
        lstCell.add(nameColumn);
        //lstCell.add(cboTableName);
        lstCell.add(popup.getViewName().toLowerCase());
        popup.setParent(this);
        component.setCaption(ResourceBundleUtils.getLanguageResource(caption));
        component.setSizeFull();
        Button btnBrowse = new Button("+");
        btnBrowse.setWidth("30px");
        btnBrowse.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    Window popupWindow = new Window();
                    popupWindow.setWidth(popupWidth);
                    popupWindow.setHeight(popupHeight);
                    popupWindow.setCaption(ResourceBundleUtils.getLanguageResource(label));
                    VerticalLayout bodyPage = new VerticalLayout();
                    bodyPage.addStyleName("page-body");
                    popup.setIsChangeDefaultSearch(true);
                    if(filterParentColumn != null && !filterParentColumn.isEmpty()) {
                        List lstParameter = new ArrayList();
                        for(int i = 0; i < lstComponent.size(); i++) {
                            if(lstComponent.get(i).get(INT_DB_FIELD_NAME).equals(filterParentColumn.toLowerCase())) {
                                if(lstComponent.get(i).get(INT_COMPONENT) instanceof AbstractField) {
                                    if(((AbstractField)lstComponent.get(i).get(INT_COMPONENT)).getValue() != null) {
                                        popup.queryWhereCondition = popup.queryWhereFilter;
                                        popup.queryWhereParameter = (ArrayList)((ArrayList)popup.queryWhereFilterParameter).clone();
                                        lstParameter.add(((AbstractField)lstComponent.get(i).get(INT_COMPONENT)).getValue());
                                        popup.addQueryWhereParameter(lstParameter);
                                        String strQuery = " and " + filterChildColumn.toLowerCase() + " = ? ";
                                        popup.addQueryWhereCondition(strQuery);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    popup.prepareOpenPopup();
                    bodyPage.addComponent(popup.initPanel(column, component, popupWindow));
                    popupWindow.setContent(bodyPage);
                    popupWindow.setModal(true);
                    mainUI.addWindow(popupWindow);
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        Button btnDelete = new Button("-");
        btnDelete.setWidth("30px");
        btnDelete.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    component.setValue(null);
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        lstCell.add(btnBrowse);
        lstCell.add(btnDelete);
        lstComponent.add(lstCell);
        if (databaseFieldName.equals(idColumnName)) {
            idField = lstComponent.size() - 1;
        }
        if (databaseFieldName.equals(sortColumnName)) {
            sortField = lstComponent.size() - 1;
            sortColumnType = dataType;
        }
        if (databaseFieldName.equals(parentColumnName)) {
            parentField = lstComponent.size() - 1;
        }
        if (databaseFieldName.equals(nameColumn)) {
            nameField = lstComponent.size() - 1;
        }
    }
    
    /**
     * Hàm thêm popup multi choice vào giao diện nhập
     * 
     * @param p tham số truyền vào
     * @since 18/01/2015 HienDM
     */
    public void addMultiPopupToForm(PopupMultiParameter p) throws Exception {
        addMultiPopupToForm(p.getLabel(), p.isMandatory(), p.isCollapsed(), p.getPopup(), p.getColumn(), p.getLstAttachField(),
                p.getTableName(), p.getIdPopup(), p.getIdConnect(), p.getIdFieldDB(), p.getSequenceName(),
                p.getFilterParentColumn(), p.getFilterChildColumn(), p.getFilterTableName());
    }
    
    /**
     * Hàm thêm popup multi choice vào giao diện nhập
     *
     * @param label mô tả
     * @param isMandatory bắt buộc nhập
     * @param isCollapsed Có hiển thị trên table không
     * @param popup popup multi choice
     * @param column số cột trình bày giao diện
     * @param lstAttachField danh sách trường dữ liệu đính kèm
     * @param tableName tên bảng dữ liệu
     * @param idPopup khóa chính của bảng đính kèm
     * @param idConnect khóa chính của bảng cha
     * @param idFieldDB khóa chính của bảng quan hệ nhiều nhiều
     * @param sequenceName sequence của bảng đính kèm
     * @param filterParentColumn trường dữ liệu dùng để filter của component cha
     * @param filterChildColumn trường dữ liệu dùng để filter của component con
     * @param filterTableName tên bảng nối filter cha và filter con
     * @since 05/01/2015 HienDM
     */
    public void addMultiPopupToForm(String label, boolean isMandatory, boolean isCollapsed,
            PopupMultiAction popup, Integer column, List<List> lstAttachField, String tableName,
            String idPopup, String idConnect, String idFieldDB, String sequenceName, 
            String filterParentColumn, String filterChildColumn, String filterTableName
    ) throws Exception {
        List lstCell = new ArrayList();
        lstCell.add(new Label(ResourceBundleUtils.getLanguageResource(label)));
        
        Table component = new Table();
        component.removeAllItems();
        component.setPageLength(5);
        component.addContainerProperty(ResourceBundleUtils.getLanguageResource(
                ((Label) popup.lstComponent.get(popup.nameField).get(INT_LABEL)).getValue()), String.class, null);

        if(lstAttachField != null) {
            for (int i = 0; i < lstAttachField.size(); i++) {
                List lstRow = lstAttachField.get(i);
                component.addContainerProperty(((Label)lstRow.get(INT_LABEL)).getValue(), HorizontalLayout.class, null);
            }
        }
        component.setWidth("100%");
        popup.setParent(this);
        lstCell.add(component);
        lstCell.add(isMandatory);
        lstCell.add(isCollapsed);
        Button btnBrowse = new Button("+");
        btnBrowse.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    Window popupWindow = new Window();
                    popupWindow.setWidth(popupWidth);
                    popupWindow.setHeight(popupHeight);
                    popupWindow.setCaption(ResourceBundleUtils.getLanguageResource(label));
                    VerticalLayout bodyPage = new VerticalLayout();
                    bodyPage.addStyleName("page-body");
                    popup.setIsChangeDefaultSearch(true);
                    if(filterParentColumn != null && !filterParentColumn.isEmpty()) {
                        List lstParameter = new ArrayList();
                        for(int i = 0; i < lstComponent.size(); i++) {
                            if(lstComponent.get(i).get(INT_DB_FIELD_NAME).equals(filterParentColumn.toLowerCase())) {
                                if(lstComponent.get(i).get(INT_COMPONENT) instanceof AbstractField) {
                                    popup.queryWhereCondition = popup.queryWhereFilter;
                                    popup.queryWhereParameter = (ArrayList)((ArrayList)popup.queryWhereFilterParameter).clone();  
                                    
                                    TextField idComponent = ((TextField)lstComponent.get(idField).get(INT_COMPONENT));
                                    if(!popup.queryWhereCondition.isEmpty() && idComponent.getValue() != null && !idComponent.getValue().toString().isEmpty()) {
                                        popup.queryWhereCondition = " and (( 1 = 1 " + popup.queryWhereCondition + ") or ("
                                                + idPopup + " in (select " + idPopup + " from "
                                                + tableName + " where " + idConnect + " = ?))) ";
                                        List lstIdConnect = new ArrayList();
                                        lstIdConnect.add(idComponent.getValue());
                                        popup.addQueryWhereParameter(lstIdConnect);
                                    }

                                    String strQuery = "";
                                    if(((AbstractField)lstComponent.get(i).get(INT_COMPONENT)).getValue() != null) {
                                        lstParameter.add(((AbstractField)lstComponent.get(i).get(INT_COMPONENT)).getValue());
                                        popup.addQueryWhereParameter(lstParameter);
                                        
                                        if(filterTableName != null) {
                                            strQuery = " and " + idPopup + " in (select " + idPopup + " from " + 
                                                filterTableName + " where " + filterChildColumn + " = ?) ";
                                        } else {
                                            strQuery = " and " + filterChildColumn + " = ?) ";
                                        }
                                    } else {
                                        strQuery = " and 1 = 0 ";
                                    }
                                    popup.addQueryWhereCondition(strQuery);
                                    break;
                                }
                            }
                        }
                    }
                    popup.prepareOpenPopup();
                    if(lstAttachField != null)
                        bodyPage.addComponent(popup.initPanel(column, component, popupWindow, lstAttachField));
                    else
                        bodyPage.addComponent(popup.initPanel(column, component, popupWindow, new ArrayList()));
                    popupWindow.setContent(bodyPage);
                    popupWindow.setModal(true);
                    mainUI.addWindow(popupWindow);
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        lstCell.add(btnBrowse);
        lstCell.add(lstAttachField);
        lstCell.add(popup.getViewName().toLowerCase());
        lstCell.add(idPopup.toLowerCase());
        lstCell.add(idConnect.toLowerCase());
        if(idFieldDB != null) lstCell.add(idFieldDB.toLowerCase());
        else lstCell.add(null);
        lstCell.add(sequenceName);
        lstCell.add(popup);
        if(filterParentColumn != null) {
            boolean check = true;
            for(int i = 0; i < lstFilter.size(); i++) {
                if(lstFilter.get(i).get(0).equals(filterParentColumn.toLowerCase())) {
                    ((List)lstFilter.get(i).get(1)).add(tableName);
                    check = false;
                }
            }
            if(check) {
                List lstRow = new ArrayList();
                lstRow.add(filterParentColumn.toLowerCase());
                List lstChild = new ArrayList();
                lstChild.add(tableName);
                lstRow.add(lstChild);
                lstFilter.add(lstRow);
            }
        }
        lstCell.add(null);
        lstCell.add(tableName);
        
        lstComponentMulti.add(lstCell);
    }
    
    /**
     * Hàm thêm File vào giao diện nhập
     *
     * @param p tham số truyền vào
     * @since 18/01/2015 HienDM
     */
    public void addUploadFieldToForm(UploadFieldParameter p) throws Exception {
        addUploadFieldToForm(p.getLabel(), p.getComponent(), p.getDatabaseFieldName(), p.getDataType(), p.isMandatory(),
                p.getDataLength(), p.getFormat(), p.getCaption(), p.isCollapsed(), p.getFileDirectory(),
                p.getMaxFileSize());
    }
    
    /**
     * Hàm thêm File vào giao diện nhập
     *
     * @param label mô tả
     * @param component thành phần trên giao diện
     * @param databaseFieldName tên cột tương ứng trong database
     * @param dataType loại dữ liệu
     * @param isMandatory bắt buộc nhập
     * @param dataLength độ dài tên file
     * @param format kiểm tra định dạng dữ liệu:
     *     ví dụ đơn giản: int,long,float,double,email
     * @param caption mô tả thêm
     * @param isCollapsed Có hiển thị trên table không
     * @param isPicture có phải là ảnh không
     * @param fileDirectory Đường dẫn thư mục chứa file
     * @param maxFileSize kích thước file lớn nhất (MByte)
     * @since 18/11/2014 HienDM
     */
    public void addUploadFieldToForm(String label, UploadField component,
            String databaseFieldName, String dataType,
            boolean isMandatory, Integer dataLength, String format, String caption,
            boolean isCollapsed, String fileDirectory, int maxFileSize) throws Exception {
        if(databaseFieldName != null)databaseFieldName = databaseFieldName.toLowerCase();
        dataType = dataType.toLowerCase();
        component.setFieldType(UploadField.FieldType.FILE);
        component.setImmediate(false);
        component.setMaxFileSize(maxFileSize * 1024 * 1024);
        List lstCell = new ArrayList();
        lstCell.add(new Label(ResourceBundleUtils.getLanguageResource(label)));
        lstCell.add(component);
        lstCell.add(databaseFieldName);
        lstCell.add(dataType);
        lstCell.add(isMandatory);
        lstCell.add(dataLength);
        lstCell.add(format);
        lstCell.add(ResourceBundleUtils.getLanguageResource(caption));
        lstCell.add(false); // useToSearch
        lstCell.add(false); // isPassword
        lstCell.add(false); // searchMandatory
        lstCell.add(isCollapsed);
        lstCell.add(null);
        lstCell.add(true);  // visibleAdd
        lstCell.add(true);  // visibleEdit
        lstCell.add(true);  // enableAdd
        lstCell.add(true);  // enableEdit
        lstCell.add(fileDirectory);
        lstCell.add(false);
        lstCell.add(""); // filePath
        lstCell.add(""); // fileEdit
        lstCell.add(maxFileSize);
        component.setCaption(ResourceBundleUtils.getLanguageResource(caption));
        component.setWidth("90%");
        lstComponent.add(lstCell);
        if (databaseFieldName.equals(idColumnName)) {
            idField = lstComponent.size() - 1;
        }
        if (databaseFieldName.equals(sortColumnName)) {
            sortField = lstComponent.size() - 1;
            sortColumnType = dataType;
        }
        if (databaseFieldName.equals(parentColumnName)) {
            parentField = lstComponent.size() - 1;
        }
        if (databaseFieldName.equals(nameColumn)) {
            nameField = lstComponent.size() - 1;
        }   
        if (dataType.equals("date")) {
            lstCell.add(new PopupDateField());
        }
    }

    /**
     * Hàm thêm nhiều File vào giao diện nhập
     *
     * @param p tham số truyền vào
     * @since 28/03/2013 HienDM
     */
    public void addMultiUploadFieldToForm(MultiUploadFieldParameter p) throws Exception {    
        addMultiUploadFieldToForm(p.getLabel(), p.getComponent(),
            p.getAttachTableName(), p.getDataType(),
            p.isMandatory(), p.getDataLength(), p.getFormat(), p.getCaption(),
            p.isCollapsed(), p.getFileDirectory(), p.getMaxFileSize(), 
            p.getIdConnectColumn(), p.getAttachColumn(), p.getIdPrimary(), p.getAttachSequence());
    }
    
    /**
     * Hàm thêm nhiều File vào giao diện nhập
     *
     * @param label mô tả
     * @param component thành phần trên giao diện
     * @param attachTableName tên cột tương ứng trong database
     * @param dataType loại dữ liệu
     * @param isMandatory bắt buộc nhập
     * @param dataLength độ dài tên file
     * @param format kiểm tra định dạng dữ liệu: ví dụ đơn giản:
     * int,long,float,double,email
     * @param caption mô tả thêm
     * @param isCollapsed Có hiển thị trên table không
     * @param isPicture có phải là ảnh không
     * @param fileDirectory Đường dẫn thư mục chứa file
     * @param maxFileSize kích thước file lớn nhất (MByte)
     * @param idConnectColumn cột nối bảng chính và bảng file đính kèm
     * @param attachColumn cột file đính kèm.
     * @param idPrimary id khóa chính
     * @param attachSequence sequence bảng attach
     * @since 28/03/2013 HienDM
     */
    public void addMultiUploadFieldToForm(String label, MultiUploadField component,
            String attachTableName, String dataType,
            boolean isMandatory, Integer dataLength, String format, String caption,
            boolean isCollapsed, String fileDirectory, int maxFileSize, 
            String idConnectColumn, String attachColumn, String idPrimary, String attachSequence) throws Exception {
        if (attachTableName != null) {
            attachTableName = attachTableName.toLowerCase();
        }
        dataType = dataType.toLowerCase();
        component.setImmediate(false);
        component.setMaxSize(Long.valueOf(Integer.valueOf(maxFileSize).toString()) * 1024 * 1024);
        List lstCell = new ArrayList();
        lstCell.add(new Label(ResourceBundleUtils.getLanguageResource(label)));
        lstCell.add(component);
        lstCell.add(attachTableName.toLowerCase());
        lstCell.add(dataType);
        lstCell.add(isMandatory);
        lstCell.add(dataLength);
        lstCell.add(format);
        lstCell.add(ResourceBundleUtils.getLanguageResource(caption));
        lstCell.add(false); // useToSearch
        lstCell.add(false); // isPassword
        lstCell.add(false); // searchMandatory
        lstCell.add(isCollapsed);
        lstCell.add(null);
        lstCell.add(true);  // visibleAdd
        lstCell.add(true);  // visibleEdit
        lstCell.add(true);  // enableAdd
        lstCell.add(true);  // enableEdit
        lstCell.add(fileDirectory);
        lstCell.add(false);
        lstCell.add(""); // filePath
        lstCell.add(""); // fileEdit
        lstCell.add(maxFileSize);
        lstCell.add(idConnectColumn.toLowerCase());
        lstCell.add(attachColumn.toLowerCase());
        lstCell.add(idPrimary.toLowerCase());
        lstCell.add(attachSequence.toLowerCase());
        component.setCaption(ResourceBundleUtils.getLanguageResource(caption));
        component.setWidth("90%");
        component.setSubFolder((FileUtils.extractFileExt(fileDirectory).toString()).substring(1));
        lstComponent.add(lstCell);
        if (attachTableName.equals(idColumnName)) {
            idField = lstComponent.size() - 1;
        }
        if (attachTableName.equals(sortColumnName)) {
            sortField = lstComponent.size() - 1;
            sortColumnType = dataType;
        }
        if (attachTableName.equals(parentColumnName)) {
            parentField = lstComponent.size() - 1;
        }
        if (attachTableName.equals(nameColumn)) {
            nameField = lstComponent.size() - 1;
        }
    }    
    
    private List getRowToBuildFormPanel(List<List> lstComp, boolean hasIdField) {
        Object idvalue = ((AbstractField)lstComponent.get(idField).get(INT_COMPONENT)).getValue();
        List lstAccept = new ArrayList();
        for(int i = 0; i < lstComp.size(); i++) {
            if(hasIdField)
                if(i == idField) continue; // Bỏ qua nếu là ID Field
            if (!(idvalue != null && !idvalue.toString().isEmpty())) {   
                if (lstComp.get(i).get(INT_COMPONENT) instanceof CheckBox) {
                    if((boolean)lstComp.get(i).get(INT_CHECKBOX_DEFAULT))
                        ((CheckBox) lstComp.get(i).get(INT_COMPONENT)).setValue(true);
                    else 
                        ((CheckBox) lstComp.get(i).get(INT_COMPONENT)).setValue(false);
                } else if (lstComp.get(i).get(INT_COMPONENT) instanceof AbstractField) {
                    if (lstComp.get(i).get(INT_DEFAULT) != null ) {
                        ((AbstractField) lstComp.get(i).get(INT_COMPONENT)).setValue(
                                lstComp.get(i).get(INT_DEFAULT));                        
                    }
                }
            }            
            // Bỏ qua nếu là sysdate
            if(lstComp.get(i).size() > INT_SYSDATE && 
                    lstComp.get(i).get(INT_SYSDATE) instanceof Boolean &&
                    lstComp.get(i).get(INT_TO_DATE_COMPONENT) instanceof PopupDateField
                    ) continue;
            // Bỏ qua nếu là login user 
            if(lstComp.get(i).size() > INT_LOGINUSER && 
                    lstComp.get(i).get(INT_LOGINUSER) != null &&
                    lstComp.get(i).get(INT_LOGINUSER).equals("LoginUser")) continue;
            // Bỏ qua nếu visibleAdd = false
            if(lstComp.get(i).size() > INT_VISIBLE_ADD &&
                    lstComp.get(i).get(INT_VISIBLE_ADD).equals(false) &&
                    !(idvalue != null && !idvalue.toString().isEmpty())) {
                continue;
            }
            // Bỏ qua nếu visibleEdit = false
            if(lstComp.get(i).size() > INT_VISIBLE_EDIT &&
                    lstComp.get(i).get(INT_VISIBLE_EDIT).equals(false) &&
                    (idvalue != null && !idvalue.toString().isEmpty())) {
                continue;
            }
            // disable component nếu enableAdd = false
            if(lstComp.get(i).size() > INT_ENABLE_ADD &&
                    lstComp.get(i).get(INT_ENABLE_ADD).equals(false) &&
                    !(idvalue != null && !idvalue.toString().isEmpty())) {
                if(lstComp.get(i).size() > INT_POPUP_BUTTON && 
                        (lstComp.get(i).get(INT_POPUP_BUTTON) instanceof Button)) {
                    ((Button)lstComp.get(i).get(INT_POPUP_BUTTON)).setVisible(false);
                    ((Button)lstComp.get(i).get(INT_POPUP_DELETE)).setVisible(false);
                } else {
                    ((AbstractField)lstComp.get(i).get(INT_COMPONENT)).setEnabled(false);
                }
            }
            // disable component nếu enableEdit = false
            if(lstComp.get(i).size() > INT_ENABLE_EDIT &&
                    lstComp.get(i).get(INT_ENABLE_EDIT).equals(false) &&
                    (idvalue != null && !idvalue.toString().isEmpty())) {
                if (lstComp.get(i).size() > INT_POPUP_BUTTON
                        && (lstComp.get(i).get(INT_POPUP_BUTTON) instanceof Button)) {
                    ((Button) lstComp.get(i).get(INT_POPUP_BUTTON)).setVisible(false);
                    ((Button) lstComp.get(i).get(INT_POPUP_DELETE)).setVisible(false);
                } else {
                    ((AbstractField)lstComp.get(i).get(INT_COMPONENT)).setEnabled(false);
                }
            }
            lstAccept.add(lstComp.get(i));
        }
        return lstAccept;
    }
    
    /**
     * Hàm khởi tạo vùng giao diện nhập dữ liệu
     *
     * @since 18/11/2014 HienDM
     * @param column số lượng cột trình bày giao diện
     * @return Vùng giao diện nhập dữ liệu
     */
    public VerticalLayout buildFormPanel(int column) throws Exception {
        Object idvalue = ((AbstractField)lstComponent.get(idField).get(INT_COMPONENT)).getValue();
        if(idvalue != null && !idvalue.toString().isEmpty()) { // Nếu là màn hình sửa
            this.currentForm = INT_EDIT_FORM;
        } else { // Nếu là màn hình thêm mới
            this.currentForm = INT_ADD_FORM;
        }
        VerticalLayout formPanel = new VerticalLayout();
        formPanel.setSizeFull();
        formPanel.setSpacing(true);
        formPanel.addComponent((Component)lstComponent.get(idField).get(INT_COMPONENT));
        formPanel.addComponent(txtButtonState);
        txtButtonState.setVisible(false);
        List lstAccept = getRowToBuildFormPanel(lstComponent, true);
        
        for(int i = 0; i < lstAccept.size(); i++) {
            List lstRow = new ArrayList();
            for(int j = 0; j < column; j++) {
                if(i < lstAccept.size()) lstRow.add(lstAccept.get(i));
                else { // Dùng phần tử rỗng khi hết phần tử trong lstComponent
                    List lstEmpty = new ArrayList();
                    lstEmpty.add(new Label(" "));
                    lstEmpty.add(ComponentUtils.buildEmptyHorizontalLayout());
                    lstEmpty.add("empty");
                    lstEmpty.add(null);
                    lstEmpty.add(false); //INT_MANDATORY
                    lstRow.add(lstEmpty);
                }
                if(j < column - 1) i++;
            }            
            formPanel.addComponent(buildRow(lstRow, false));
        }
        
        for (int i = 0; i < lstComponentMulti.size(); i++) {
            List lstRow = new ArrayList();
            for(int j = 0; j < column; j++) {
                if(i < lstComponentMulti.size()) lstRow.add(lstComponentMulti.get(i));
                else { // Dùng phần tử rỗng khi hết phần tử trong lstComponent
                    List lstEmpty = new ArrayList();
                    lstEmpty.add(new Label(" "));
                    lstEmpty.add(ComponentUtils.buildEmptyHorizontalLayout());
                    lstEmpty.add("empty");
                    lstEmpty.add(null);
                    lstEmpty.add(false); //INT_MANDATORY
                    lstRow.add(lstEmpty);
                }
                if(j < column - 1) i++;
            }            
            formPanel.addComponent(buildRow(lstRow, false));
        }
        
        List lstAccept2 = getRowToBuildFormPanel(lstCustomizeComponent, false);
        
        for (int i = 0; i < lstAccept2.size(); i++) {
            List lstRow = new ArrayList();
            for(int j = 0; j < column; j++) {
                if(i < lstAccept2.size()) lstRow.add(lstAccept2.get(i));
                else { // Dùng phần tử rỗng khi hết phần tử trong lstComponent
                    List lstEmpty = new ArrayList();
                    lstEmpty.add(new Label(" "));
                    lstEmpty.add(ComponentUtils.buildEmptyHorizontalLayout());
                    lstEmpty.add("empty");
                    lstEmpty.add(null);
                    lstEmpty.add(false); //INT_MANDATORY
                    lstRow.add(lstEmpty);
                }
                if(j < column - 1) i++;
            }            
            formPanel.addComponent(buildRow(lstRow, false));
        } 
        
        treeLayout.setVisible(false);
        //hideComponentNotUseToSearch(false);
        formArea.setStyleName("panel-highlight");
        buttonArea.setStyleName("panel-highlight"); 
        buttonArea.removeAllComponents();
        panelButton = buildUpdatePanelButton();
        buttonArea.addComponent(panelButton);
        buttonArea.setComponentAlignment(panelButton, Alignment.MIDDLE_CENTER);
        if(idField == 0) ((AbstractField)lstComponent.get(1).get(INT_COMPONENT)).focus();
        else ((AbstractField)lstComponent.get(0).get(INT_COMPONENT)).focus();
        return formPanel;
    }

    /**
     * Hàm khởi tạo vùng giao diện xem dữ liệu
     *
     * @since 18/11/2014 HienDM
     * @param column số lượng cột trình bày giao diện
     * @return Vùng giao diện nhập dữ liệu
     */
    public VerticalLayout buildDetailPanel(int column) throws Exception {
        this.currentForm = INT_VIEW_FORM;
        VerticalLayout formPanel = new VerticalLayout();
        formPanel.setSizeFull();
        formPanel.setSpacing(true);
        formPanel.addComponent((Component)lstComponent.get(idField).get(INT_COMPONENT));
        formPanel.addComponent(txtButtonState);
        txtButtonState.setVisible(false);
        List lstAccept = new ArrayList();
        for(int i = 0; i < lstComponent.size(); i++) {
            if(i == idField) continue; // Bỏ qua nếu là ID Field
            lstAccept.add(lstComponent.get(i));
        }
        
        for(int i = 0; i < lstAccept.size(); i++) {
            List lstRow = new ArrayList();
            for(int j = 0; j < column; j++) {
                if(i < lstAccept.size()) lstRow.add(lstAccept.get(i));
                else { // Dùng phần tử rỗng khi hết phần tử trong lstComponent
                    List lstEmpty = new ArrayList();
                    lstEmpty.add(new Label(" "));
                    lstEmpty.add(ComponentUtils.buildEmptyHorizontalLayout());
                    lstEmpty.add("empty");
                    lstEmpty.add(null);
                    lstEmpty.add(false); //INT_MANDATORY
                    lstRow.add(lstEmpty);
                }
                if(j < column - 1) i++;
            }            
            formPanel.addComponent(buildRowDetail(lstRow));
        }
        
        for (int i = 0; i < lstComponentMulti.size(); i++) {
            List lstRow = new ArrayList();
            for(int j = 0; j < column; j++) {
                if(i < lstComponentMulti.size()) lstRow.add(lstComponentMulti.get(i));
                else { // Dùng phần tử rỗng khi hết phần tử trong lstComponent
                    List lstEmpty = new ArrayList();
                    lstEmpty.add(new Label(" "));
                    lstEmpty.add(ComponentUtils.buildEmptyHorizontalLayout());
                    lstEmpty.add("empty");
                    lstEmpty.add(null);
                    lstEmpty.add(false); //INT_MANDATORY
                    lstRow.add(lstEmpty);
                }
                if(j < column - 1) i++;
            }            
            formPanel.addComponent(buildRowDetail(lstRow));
        }
        
        treeLayout.setVisible(false);
        //hideComponentNotUseToSearch(false);
        formArea.setStyleName("panel-highlight");
        buttonArea.setStyleName("panel-highlight"); 
        buttonArea.removeAllComponents();
        panelButton = buildUpdatePanelButton();
        buttonArea.addComponent(panelButton);
        buttonArea.setComponentAlignment(panelButton, Alignment.MIDDLE_CENTER);
        if(idField == 0) ((AbstractField)lstComponent.get(1).get(INT_COMPONENT)).focus();
        else ((AbstractField)lstComponent.get(0).get(INT_COMPONENT)).focus();
        return formPanel;
    }
    
    public void prepareSearch() throws Exception {};
    public void afterPrepareSearch() throws Exception {};
    
    /**
     * Hàm khởi tạo vùng giao diện tìm kiếm dữ liệu
     *
     * @since 18/11/2014 HienDM
     * @param column số lượng cột trình bày giao diện
     * @return Vùng giao diện tìm kiếm dữ liệu
     */
    public VerticalLayout buildSearchFormPanel(int column) throws Exception {
        prepareSearch();
        this.currentForm = INT_SEARCH_FORM;
        VerticalLayout formPanel = new VerticalLayout();
        formPanel.setSizeFull();
        formPanel.setSpacing(true);
        formPanel.addComponent((Component)lstComponent.get(idField).get(INT_COMPONENT));
        formPanel.addComponent(txtButtonState);
        txtButtonState.setVisible(false);
        
        List<List> lstSearch = new ArrayList();
        List<List> lstNotSearch = new ArrayList();
        for(int i = 0; i < lstComponent.size(); i++) {
            if(i == idField) continue; // Bỏ qua nếu là ID Field
            // enable component nếu enableAdd = false
            if(lstComponent.get(i).size() > INT_ENABLE_ADD &&
                        lstComponent.get(i).get(INT_ENABLE_ADD).equals(false)) {
                if(lstComponent.get(i).size() > INT_POPUP_BUTTON && 
                        (lstComponent.get(i).get(INT_POPUP_BUTTON) instanceof Button)) {
                    ((Button)lstComponent.get(i).get(INT_POPUP_BUTTON)).setVisible(true);
                    ((Button)lstComponent.get(i).get(INT_POPUP_DELETE)).setVisible(true);
                } else {
                    ((AbstractField)lstComponent.get(i).get(INT_COMPONENT)).setEnabled(true);
                }
            }
            // enable component nếu enableEdit = false
            if(lstComponent.get(i).size() > INT_ENABLE_EDIT &&
                        lstComponent.get(i).get(INT_ENABLE_EDIT).equals(false)) {
                if(lstComponent.get(i).size() > INT_POPUP_BUTTON && 
                        (lstComponent.get(i).get(INT_POPUP_BUTTON) instanceof Button)) {
                    ((Button)lstComponent.get(i).get(INT_POPUP_BUTTON)).setVisible(true);
                    ((Button)lstComponent.get(i).get(INT_POPUP_DELETE)).setVisible(true);
                } else {
                    ((AbstractField)lstComponent.get(i).get(INT_COMPONENT)).setEnabled(true);
                }
            }
            if((boolean)lstComponent.get(i).get(INT_USE_TO_SEARCH)) {
                lstSearch.add(lstComponent.get(i));
            } else {
                // Bỏ qua nếu là sysdate
                if (lstComponent.get(i).size() > INT_SYSDATE
                        && lstComponent.get(i).get(INT_SYSDATE) instanceof Boolean && 
                        lstComponent.get(i).get(INT_TO_DATE_COMPONENT) instanceof PopupDateField) continue;
                // Bỏ qua nếu là login user 
                if(lstComponent.get(i).size() > INT_LOGINUSER && 
                        lstComponent.get(i).get(INT_LOGINUSER) != null &&
                        lstComponent.get(i).get(INT_LOGINUSER).equals("LoginUser")) continue;
                lstNotSearch.add(lstComponent.get(i));
            }
        }
        for(int i = 0; i < lstComponentMulti.size(); i++) {
            lstNotSearch.add(lstComponentMulti.get(i));
        }
        
        for(int i = 0; i < lstCustomizeComponent.size(); i++) {
            // enable component nếu enableAdd = false
            if(lstCustomizeComponent.get(i).size() > INT_ENABLE_ADD &&
                        lstCustomizeComponent.get(i).get(INT_ENABLE_ADD).equals(false)) {
                if(lstCustomizeComponent.get(i).size() > INT_POPUP_BUTTON && 
                        (lstCustomizeComponent.get(i).get(INT_POPUP_BUTTON) instanceof Button)) {
                    ((Button)lstCustomizeComponent.get(i).get(INT_POPUP_BUTTON)).setVisible(true);
                    ((Button)lstCustomizeComponent.get(i).get(INT_POPUP_DELETE)).setVisible(true);
                } else {
                    ((AbstractField)lstCustomizeComponent.get(i).get(INT_COMPONENT)).setEnabled(true);
                }
            }
            // enable component nếu enableEdit = false
            if(lstCustomizeComponent.get(i).size() > INT_ENABLE_EDIT &&
                        lstCustomizeComponent.get(i).get(INT_ENABLE_EDIT).equals(false)) {
                if(lstCustomizeComponent.get(i).size() > INT_POPUP_BUTTON && 
                        (lstCustomizeComponent.get(i).get(INT_POPUP_BUTTON) instanceof Button)) {
                    ((Button)lstCustomizeComponent.get(i).get(INT_POPUP_BUTTON)).setVisible(true);
                    ((Button)lstCustomizeComponent.get(i).get(INT_POPUP_DELETE)).setVisible(true);
                } else {
                    ((AbstractField)lstCustomizeComponent.get(i).get(INT_COMPONENT)).setEnabled(true);
                }
            }
            if((boolean)lstCustomizeComponent.get(i).get(INT_USE_TO_SEARCH)) {
                lstSearch.add(lstCustomizeComponent.get(i));
            }
        }        
        // Thêm các phần tử dùng để tìm kiếm vào giao diện
        for(int i = 0; i < lstSearch.size(); i++) {
            List lstRow = new ArrayList();
            for(int j = 0; j < column; j++) {
                if(i < lstSearch.size()) lstRow.add(lstSearch.get(i));
                else { // Dùng phần tử rỗng khi hết phần tử trong lstComponent
                    List lstEmpty = new ArrayList();
                    lstEmpty.add(new Label(" "));
                    lstEmpty.add(ComponentUtils.buildEmptyHorizontalLayout());
                    lstEmpty.add("empty");
                    lstEmpty.add(null);
                    lstEmpty.add(false); //INT_MANDATORY
                    lstRow.add(lstEmpty);
                }
                if(j < column - 1) i++;
            }
            formPanel.addComponent(buildRow(lstRow, true));
        }
        VerticalLayout formNotSearchPanel = new VerticalLayout();
        // Thêm các phần tử không dùng để tìm kiếm vào giao diện
        for(int i = 0; i < lstNotSearch.size(); i++) {
            List lstRow = new ArrayList();
            for(int j = 0; j < column; j++) {
                if(i < lstNotSearch.size()) lstRow.add(lstNotSearch.get(i));
                if(j < column - 1) i++;
            }
            formNotSearchPanel.addComponent(buildRow(lstRow, false));
        }
        formPanel.addComponent(formNotSearchPanel);
        treeLayout.setVisible(true);
        buttonArea.removeAllComponents();
        panelButton = buildPanelButton();
        buttonArea.addComponent(panelButton);
        buttonArea.setComponentAlignment(panelButton, Alignment.MIDDLE_CENTER);        
        formArea.removeStyleName("panel-highlight");
        buttonArea.removeStyleName("panel-highlight");        
        hideComponentNotUseToSearch(true,formNotSearchPanel);
        if(!(lstSearch != null && lstSearch.isEmpty()))
            ((AbstractField)lstSearch.get(0).get(INT_COMPONENT)).focus();
        clearForm();
        for (int i = 0; i < lstComponent.size(); i++) {
            for (int j = 0; j < lstMandatorySearchValue.size(); j++) {
                if (lstComponent.get(i).get(INT_SEARCH_MANDATORY) != null) {
                    if (lstComponent.get(i).get(INT_DB_FIELD_NAME).equals(
                            lstMandatorySearchValue.get(j).get(0))) {
                        if(lstMandatorySearchValue.get(j).get(1) instanceof ArrayList) {
                            ((AbstractField) lstComponent.get(i).get(INT_COMPONENT)).setValue(
                                    ((List)lstMandatorySearchValue.get(j).get(1)).get(0));
                            ((AbstractField) lstComponent.get(i).get(INT_TO_DATE_COMPONENT)).setValue(
                                    ((List) lstMandatorySearchValue.get(j).get(1)).get(1));
                        } else {
                            ((AbstractField) lstComponent.get(i).get(INT_COMPONENT)).setValue(
                                    lstMandatorySearchValue.get(j).get(1));
                        }
                    }
                }
            }
        }        
        refreshAlready = false;
        afterPrepareSearch();
        return formPanel;
    }
    
    /**
     * Hàm tạo giao diện theo từng hàng
     *
     * @since 18/11/2014 HienDM
     * @param lstRow danh sách thành phần giao diện
     * @param isSearchForm nếu giao diện search thì trường date bổ sung component to date
     * @return giao diện theo từng hàng
     */
    private HorizontalLayout buildRow(List<List> lstRow, boolean isSearchForm) throws Exception {
        HorizontalLayout row = new HorizontalLayout();
        row.setWidth("100%");
        for(int i = 0; i < lstRow.size(); i++) {
            if(lstRow.get(i).get(INT_MULTI_TABLE) instanceof Table) {
                Label lbl = (Label)lstRow.get(i).get(INT_MULTI_LABEL);
                lbl.removeStyleName("BoldLabel");
                if(lstRow.get(i).size() > INT_MULTI_MANDATORY)
                if (!isSearchForm && (boolean) lstRow.get(i).get(INT_MULTI_MANDATORY)) {
                    lbl.setStyleName("BoldLabel");
                }
                row.addComponent(lbl);
                if(!lstRow.get(i).get(2).equals("empty")) {
                    VerticalLayout popupRow = new VerticalLayout();
                    popupRow.addComponent((Button) lstRow.get(i).get(INT_MULTI_BROWSE));
                    popupRow.addComponent((Component) lstRow.get(i).get(INT_MULTI_TABLE));
                    popupRow.setWidth("90%");
                    row.addComponent(popupRow);                
                } else {
                    row.addComponent((Component)lstRow.get(i).get(INT_COMPONENT));
                }
            } else {
                Label lbl = (Label)lstRow.get(i).get(INT_LABEL);
                lbl.removeStyleName("BoldLabel");
                if(lstRow.get(i).size() > INT_MANDATORY)
                if (!isSearchForm && (boolean) lstRow.get(i).get(INT_MANDATORY)) {
                    lbl.setStyleName("BoldLabel");
                }
                if(lstRow.get(i).size() > INT_SEARCH_MANDATORY)
                if (isSearchForm && lstRow.get(i).get(INT_SEARCH_MANDATORY) != null) {
                    lbl.setStyleName("BoldLabel");
                }                
                row.addComponent(lbl);
                if(lstRow.get(i).get(INT_COMPONENT) instanceof ComboBox) {
                    if(isSearchForm) {
                        ((ComboBox)lstRow.get(i).get(INT_COMPONENT)).setNullSelectionAllowed(true);
                    } else {
                        if(lstRow.get(i).size() > INT_MANDATORY) {
                            boolean allowNull = !(boolean)lstRow.get(i).get(INT_MANDATORY);
                            ((ComboBox)lstRow.get(i).get(INT_COMPONENT)).setNullSelectionAllowed(allowNull);
                        }
                    }
                }
                
                if(lstRow.get(i).get(INT_COMPONENT) instanceof PopupDateField) {
                    ((Component)lstRow.get(i).get(INT_COMPONENT)).setWidth("90%");
                }
                
                if(!lstRow.get(i).get(2).equals("empty")) {
                    if(isSearchForm && lstRow.get(i).get(INT_DATA_TYPE).equals("date")) {
                        VerticalLayout dateArea = new VerticalLayout();
                        HorizontalLayout firstRow = new HorizontalLayout();
                        ((Component)lstRow.get(i).get(INT_COMPONENT)).setWidth("143px");
                        firstRow.addComponent((Component)lstRow.get(i).get(INT_COMPONENT));
                        firstRow.addComponent(new Label(ResourceBundleUtils.getLanguageResource("Common.From")));
                        HorizontalLayout secondRow = new HorizontalLayout();
                        PopupDateField toDate = (PopupDateField)lstRow.get(i).get(INT_TO_DATE_COMPONENT);
                        if(((PopupDateField)lstRow.get(i).get(INT_COMPONENT)).getResolution().equals(Resolution.DAY))
                            toDate.setDateFormat("dd/MM/yyyy");
                        else {
                            toDate.setDateFormat("dd/MM/yyyy HH:mm:ss");
                            toDate.setResolution(Resolution.SECOND);
                        }
                        toDate.setWidth("143px");
                        secondRow.addComponent(toDate);
                        secondRow.addComponent(new Label(ResourceBundleUtils.getLanguageResource("Common.To")));
                        dateArea.addComponent(firstRow);
                        dateArea.addComponent(secondRow);
                        row.addComponent(dateArea);
                    } else if(lstRow.get(i).size() > INT_POPUP_BUTTON && (lstRow.get(i).get(INT_POPUP_BUTTON) instanceof Button)) {
                        HorizontalLayout popupRow = new HorizontalLayout();
                        popupRow.addComponent((Component)lstRow.get(i).get(INT_COMPONENT));
                        popupRow.setExpandRatio((Component)lstRow.get(i).get(INT_COMPONENT), 70f);
                        popupRow.addComponent((Button)lstRow.get(i).get(INT_POPUP_BUTTON));
                        popupRow.addComponent((Button)lstRow.get(i).get(INT_POPUP_DELETE));
                        popupRow.setWidth("90%");
                        row.addComponent(popupRow);
                    } else {
                        row.addComponent((Component)lstRow.get(i).get(INT_COMPONENT));
                    }
                } else {
                    row.addComponent((Component)lstRow.get(i).get(INT_COMPONENT));
                }
            }
        }
        return row;
    }
    
    /**
     * Hàm tạo giao diện xem chi tiết theo từng hàng
     *
     * @since 18/11/2014 HienDM
     * @param lstRow danh sách thành phần giao diện
     * @param isSearchForm nếu giao diện search thì trường date bổ sung component to date
     * @return giao diện theo từng hàng
     */
    private HorizontalLayout buildRowDetail(List<List> lstRow) throws Exception {
        HorizontalLayout row = new HorizontalLayout();
        row.setWidth("100%");
        for(int i = 0; i < lstRow.size(); i++) {
            Label lbl = (Label) lstRow.get(i).get(INT_MULTI_LABEL);
            lbl.setStyleName("BoldLabel");
            row.addComponent(lbl);
            if(lstRow.get(i).get(INT_MULTI_TABLE) instanceof Table) {
                if(!lstRow.get(i).get(2).equals("empty")) {
                    VerticalLayout popupRow = new VerticalLayout();
                    popupRow.addComponent((Component) lstRow.get(i).get(INT_MULTI_TABLE));
                    popupRow.setWidth("90%");
                    row.addComponent(popupRow);
                } else {
                    row.addComponent((Component)lstRow.get(i).get(INT_COMPONENT));
                }
            } else {
                if(!lstRow.get(i).get(2).equals("empty")) {
                    if(lstRow.get(i).get(INT_COMPONENT) instanceof MultiUploadField) {
                        MultiUploadField upload = (MultiUploadField)lstRow.get(i).get(INT_COMPONENT);
                        if(upload.lstDownloadLink != null && !upload.lstDownloadLink.isEmpty()) {
                            row.addComponent(upload.downloadLinkArea);
                            upload.hideDeleteIcon();
                        } else row.addComponent(new Label(""));
                    } else if(lstRow.get(i).get(INT_COMPONENT) instanceof UploadField) {
                        UploadField upload = (UploadField)lstRow.get(i).get(INT_COMPONENT);
                        if (upload.downloadLink.getFilePath() != null) {
                            File downloadFile = new File(ResourceBundleUtils.getConfigureResource("FileBaseDirectory")
                                    + upload.downloadLink.getFilePath());
                            if (!downloadFile.exists()) {
                                upload.downloadLink.setComponentError(new UserError(ResourceBundleUtils.getLanguageResource("Common.FileNotExist")));
                            }
                        }
                        if(upload.downloadLink != null) {
                            if(upload.isPicture) {
                                row.addComponent(upload.image);
                            } else {
                                row.addComponent(upload.downloadLink);
                            }
                        }
                        else row.addComponent(new Label(""));
                    } else {
                        AbstractField field = (AbstractField)lstRow.get(i).get(INT_COMPONENT);
                        if(field.getValue() != null) {
                            if(field instanceof PopupDateField) {
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                if(((PopupDateField)field).getResolution().equals(Resolution.DAY))
                                    formatter = new SimpleDateFormat("dd/MM/yyyy");
                                else
                                    formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                row.addComponent(new Label(formatter.format(field.getValue())));
                            } else if(field instanceof ComboBox) {
                                row.addComponent(new Label(((ComboBox)field).getItemCaption(field.getValue())));
                            } else if(field instanceof CheckBox) { 
                                if(((CheckBox)field).getValue()) {
                                    row.addComponent(new Label(lstRow.get(i).get(INT_CHECKBOX_ENABLE).toString()));
                                } else {
                                    row.addComponent(new Label(lstRow.get(i).get(INT_CHECKBOX_DISABLE).toString()));
                                }
                            } else {
                                row.addComponent(new Label(field.getValue().toString()));
                            }
                        } else {
                            row.addComponent(new Label(""));
                        }
                    }
                } else {
                    row.addComponent((Component)lstRow.get(i).get(INT_COMPONENT));
                }
            }
        }
        return row;
    }
    
    /**
     * Hàm thực hiện nút tìm kiếm
     *
     * @since 15/10/2014 HienDM
     */
    private void buttonFindClick() throws Exception {
        if (validateSearch()) {
            // Kiem tra điều kiện để load dữ liệu mới từ database
            isChangeDefaultSearch = false;
            for (int i = 0; i < lstComponent.size(); i++) {
                for (int j = 0; j < lstMandatorySearchValue.size(); j++) {
                    if (lstComponent.get(i).get(INT_SEARCH_MANDATORY) != null) {
                        if (lstComponent.get(i).get(INT_DB_FIELD_NAME).equals(
                                lstMandatorySearchValue.get(j).get(0))) {
                            if (!(lstComponent.get(i).get(INT_COMPONENT) instanceof UploadField || 
                                    lstComponent.get(i).get(INT_COMPONENT) instanceof MultiUploadField)) {
                                if (lstComponent.get(i).get(INT_COMPONENT) instanceof PopupDateField) {
                                    if (((PopupDateField) lstComponent.get(i).get(INT_COMPONENT)).getValue() == null) {
                                        if (lstMandatorySearchValue.get(j).get(1) != null) {
                                            isChangeDefaultSearch = true;
                                            lstMandatorySearchValue.get(j).set(1, null);
                                        }
                                    } else if (!(((PopupDateField) lstComponent.get(i).get(INT_COMPONENT)).getValue().equals(
                                            ((List) lstMandatorySearchValue.get(j).get(1)).get(0)))) {
                                        isChangeDefaultSearch = true;
                                        ((List) lstMandatorySearchValue.get(j).get(1)).set(0, ((AbstractField) lstComponent.get(i).
                                                get(INT_COMPONENT)).getValue());
                                    }
                                    if (((PopupDateField) lstComponent.get(i).get(INT_COMPONENT)).getValue() == null) {
                                        if (lstMandatorySearchValue.get(j).get(1) != null) {
                                            isChangeDefaultSearch = true;
                                            lstMandatorySearchValue.get(j).set(1, ((AbstractField) lstComponent.get(i).
                                                    get(INT_COMPONENT)).getValue());
                                        }
                                    } else if (!(((PopupDateField) lstComponent.get(i).get(INT_TO_DATE_COMPONENT)).getValue().equals(
                                            ((List) lstMandatorySearchValue.get(j).get(1)).get(1)))) {
                                        isChangeDefaultSearch = true;
                                        ((List) lstMandatorySearchValue.get(j).get(1)).set(1, ((AbstractField) lstComponent.get(i).
                                                get(INT_TO_DATE_COMPONENT)).getValue());
                                    }
                                } else {
                                    if (((AbstractField) lstComponent.get(i).get(INT_COMPONENT)).getValue() == null) {
                                        if (lstMandatorySearchValue.get(j).get(1) != null) {
                                            isChangeDefaultSearch = true;
                                            lstMandatorySearchValue.get(j).set(1, ((AbstractField) lstComponent.get(i).
                                                    get(INT_COMPONENT)).getValue());
                                        }
                                    } else if (!(((AbstractField) lstComponent.get(i).get(INT_COMPONENT)).getValue().equals(
                                            lstMandatorySearchValue.get(j).get(1)))) {
                                        isChangeDefaultSearch = true;
                                        lstMandatorySearchValue.get(j).set(1, ((AbstractField) lstComponent.get(i).
                                                get(INT_COMPONENT)).getValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }
            searchNormalData();
        }        
    }
    
    /**
     * Hàm thực hiện khi nhấn nút thêm
     *
     * @since 18/01/2015 HienDM
     */
    public boolean prepareAdd() throws Exception {return true;}
    public void afterPrepareAdd() throws Exception {}
    
    /**
     * Hàm thực hiện khi nhấn nút sửa
     *
     * @since 18/01/2015 HienDM
     */
    public boolean prepareEdit() throws Exception {return true;}
    public void afterPrepareEdit() throws Exception {}
    
    /**
     * Hàm thực hiện nút thêm mới
     *
     * @since 15/10/2014 HienDM
     */
    private void buttonAddClick() throws Exception {
        if(prepareAdd()) {
            formArea.removeAllComponents();
            clearForm();
            formArea.addComponent(buildFormPanel(numberOfTD));
            txtButtonState.setValue("add");
        }
        afterPrepareAdd();
    }

    /**
     * Hàm kiểm tra điều kiện xóa
     *
     * @since 16/01/2015 HienDM
     */    
    public boolean validateDelete(Object[] selectedArray) throws Exception {
        return true;
    }

    /**
     * Hàm kiểm tra có quyền xóa hay không
     *
     * @since 15/10/2014 HienDM
     */    
    public boolean checkPermission(Object[] selectedArray) throws Exception{
        for(int j = 0; j < selectedArray.length; j++) {
            Item data = table.getItem(selectedArray[j]);
            // Kiểm tra nếu chỉ được sửa bản ghi do mình tạo ra
            for (int i = 0; i < lstComponent.size(); i++) {
                if (lstComponent.get(i).size() > BaseAction.INT_LOGINUSER
                        && lstComponent.get(i).get(BaseAction.INT_LOGINUSER) != null
                        && lstComponent.get(i).get(BaseAction.INT_LOGINUSER).equals("LoginUser")
                        && (boolean) lstComponent.get(i).get(BaseAction.INT_LOGINUSER_ONLYEDIT)) {
                    ComboboxItem cboItem = (ComboboxItem) data.getItemProperty(
                            ((Label) lstComponent.get(i).get(INT_LABEL)).getValue()).getValue();
                    if (!cboItem.getValue().toString().equals(
                            VaadinUtils.getSessionAttribute("G_UserId").toString())) {
                        Notification.show(ResourceBundleUtils.getLanguageResource("Common.Permission.Edit"),
                                null, Notification.Type.ERROR_MESSAGE);
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    /**
     * Hàm thực hiện nút sửa
     *
     * @since 15/10/2014 HienDM
     */
    public void buttonEditClick() throws Exception {
        Object[] selectedArray = ((java.util.Collection) table.getValue()).toArray();
        if(prepareEdit()) {
            if (selectedArray != null && selectedArray.length > 0) {
                if (checkPermission(selectedArray)) {
                    Item data = table.getItem(selectedArray[0]);
                    // Đẩy thông tin từ table vào các trường trên form nhập
                    ((AbstractField) lstComponent.get(idField).get(INT_COMPONENT)).setValue(selectedArray[0].toString());
                    for (int i = 0; i < lstComponent.size(); i++) {
                        // Bỏ qua nếu là sysdate
                        if (lstComponent.get(i).size() > INT_SYSDATE
                                && lstComponent.get(i).get(INT_SYSDATE) instanceof Boolean
                                && lstComponent.get(i).get(INT_TO_DATE_COMPONENT) instanceof PopupDateField) {
                            continue;
                        }
                        // Bỏ qua nếu là login user 
                        if (lstComponent.get(i).size() > INT_LOGINUSER
                                && lstComponent.get(i).get(INT_LOGINUSER).equals("LoginUser")) {
                            continue;
                        }
                        // Bỏ qua nếu trường này chỉ có trong view, không có trong table
                        if (lstComponent.get(i).get(BaseAction.INT_FORMAT) != null && 
                                lstComponent.get(i).get(BaseAction.INT_FORMAT).equals("OnlyView")) {
                            continue;
                        }
                        if (lstComponent.get(i).get(INT_COMPONENT) instanceof MultiUploadField) {
                            BaseDAO baseDao = new BaseDAO();
                            List<Map> lstMultiUpload = baseDao.selectMultiUploadData(lstComponent.get(i), 
                                    Long.parseLong(selectedArray[0].toString()));
                            MultiUploadField upload = (MultiUploadField) lstComponent.get(i).get(INT_COMPONENT);
                            upload.removeAllDownloadLink();
                            for(int n = 0; n < lstMultiUpload.size(); n++) {
                                DownloadLink dl = new DownloadLink();
                                // Giải mã tên file
                                String fileName = lstMultiUpload.get(n).get("attach_file").toString();
                                String pattern = Pattern.quote(System.getProperty("file.separator"));
                                String[] splittedFileName = fileName.split(pattern);
                                if (splittedFileName.length > 1) {
                                    fileName = splittedFileName[splittedFileName.length - 1];
                                } else {
                                    splittedFileName = fileName.split("/");
                                    fileName = splittedFileName[splittedFileName.length - 1];
                                }
                                String ext = FileUtils.extractFileExt(fileName);
                                fileName = (new String(Base64Utils.decode(FileUtils.extractFileNameNotExt(fileName))));
                                fileName = fileName.substring(fileName.indexOf("_") + 1) + ext;
                                if (fileName.length() > 30) {
                                    fileName = fileName.substring(0, 25) + "---" + FileUtils.extractFileExt(fileName);
                                }
                                dl.setCaption(fileName);
                                // End: Giải mã tên file                              
                                
                                // Gán đường dẫn download
                                dl.setFilePath(lstMultiUpload.get(n).get("attach_file").toString());
                                if (dl.getFilePath() != null) {
                                    File downloadFile = new File(ResourceBundleUtils.getConfigureResource("FileBaseDirectory")
                                            + dl.getFilePath());
                                    if (!downloadFile.exists()) {
                                        dl.setComponentError(new UserError(ResourceBundleUtils.getLanguageResource("Common.FileNotExist")));
                                    }
                                }
                                final AdvancedFileDownloader downloaderForLink = new AdvancedFileDownloader();
                                downloaderForLink.addAdvancedDownloaderListener(new AdvancedFileDownloader.AdvancedDownloaderListener() {
                                    @Override
                                    public void beforeDownload(AdvancedFileDownloader.DownloaderEvent downloadEvent) {
                                        String filePath = ResourceBundleUtils.getConfigureResource("FileBaseDirectory")
                                                + dl.getFilePath();
                                        downloaderForLink.setFilePath(filePath);
                                    }
                                });
                                downloaderForLink.extend(dl);
                                // End: Gán đường dẫn download
                                
                                upload.lstDownloadLink.add(dl);
                                
                                // Them link vao vung download
                                HorizontalLayout cell = new HorizontalLayout();
                                Embedded iconDelete = new Embedded(null, new ThemeResource("img/industry/button/delete.png"));
                                cell.addComponent(dl);
                                cell.setComponentAlignment(dl, Alignment.MIDDLE_LEFT);
                                cell.addComponent(iconDelete);
                                cell.setComponentAlignment(iconDelete, Alignment.MIDDLE_LEFT);
                                upload.downloadLinkArea.addComponent(cell);

                                iconDelete.addClickListener(new MouseEvents.ClickListener() {
                                    public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
                                        try {
                                            upload.downloadLinkArea.removeComponent(cell);
                                            upload.lstDownloadLink.remove(dl);
                                        } catch (Exception ex) {
                                            VaadinUtils.handleException(ex);
                                            mainLogger.debug("Industry error: ", ex);
                                        }
                                    }
                                });
                                upload.addComponent(upload.downloadLinkArea);
                                upload.showDeleteIcon();
                                // End: Them link vao vung download
                            }
                        } else if (lstComponent.get(i).get(INT_COMPONENT) instanceof UploadField) {
                            DownloadLink dl = (DownloadLink) data.getItemProperty(
                                    ((Label) lstComponent.get(i).get(INT_LABEL)).getValue()).getValue();
                            ((UploadField) lstComponent.get(i).get(INT_COMPONENT)).setDownloadLink(dl);
                            UploadField upload = (UploadField) lstComponent.get(i).get(INT_COMPONENT);
                            if (upload.downloadLink.getFilePath() != null) {
                                File downloadFile = new File(ResourceBundleUtils.getConfigureResource("FileBaseDirectory")
                                        + upload.downloadLink.getFilePath());
                                if (!downloadFile.exists()) {
                                    upload.downloadLink.setComponentError(new UserError(ResourceBundleUtils.getLanguageResource("Common.FileNotExist")));
                                }
                            }
                            if (lstComponent.get(i).size() > INT_FILE_EDIT) {
                                lstComponent.get(i).set(INT_FILE_EDIT, dl);
                            } else {
                                lstComponent.get(i).add(dl);
                            }
                        } else if (i != idField) {
                            if (((AbstractField) lstComponent.get(i).get(INT_COMPONENT)) instanceof CheckBox) {
                                if (data.getItemProperty(((Label) lstComponent.get(i).get(INT_LABEL)).getValue()).getValue().
                                        toString().equals(ResourceBundleUtils.getLanguageResource(
                                                        lstComponent.get(i).get(INT_CHECKBOX_ENABLE).toString()))) {
                                    ((AbstractField) lstComponent.get(i).get(INT_COMPONENT)).setValue(true);
                                } else {
                                    ((AbstractField) lstComponent.get(i).get(INT_COMPONENT)).setValue(false);
                                }
                            } else if (((AbstractField) lstComponent.get(i).get(INT_COMPONENT)) instanceof ComboBox) {
                                if ((lstComponent.get(i).size() > BaseAction.INT_POPUP_BUTTON)
                                        && (lstComponent.get(i).get(BaseAction.INT_POPUP_BUTTON) instanceof Button)) {
                                    ComboboxItem cboItem = (ComboboxItem) data.getItemProperty(
                                            ((Label) lstComponent.get(i).get(INT_LABEL)).getValue()).getValue();
                                    ComboBox cboData = (ComboBox) lstComponent.get(i).get(INT_COMPONENT);
                                    cboData.removeAllItems();
                                    cboData.addItem(cboItem.getValue());
                                    cboData.setItemCaption(cboItem.getValue(), cboItem.getCaption());
                                    cboData.setValue(cboItem.getValue());
                                } else {
                                    ComboboxItem cboItem = (ComboboxItem) data.getItemProperty(
                                            ((Label) lstComponent.get(i).get(INT_LABEL)).getValue()).getValue();
                                    ((ComboBox) lstComponent.get(i).get(INT_COMPONENT)).setValue(cboItem.getValue());
                                }
                            } else if (((AbstractField) lstComponent.get(i).get(INT_COMPONENT)) instanceof PopupDateField) {
                                if (!data.getItemProperty(((Label) lstComponent.get(i).get(INT_LABEL)).
                                        getValue()).getValue().toString().isEmpty()) {
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                    if(((PopupDateField) lstComponent.get(i).get(INT_COMPONENT)).getResolution().equals(Resolution.DAY))
                                        formatter = new SimpleDateFormat("dd/MM/yyyy");
                                    else
                                        formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                    Date date = formatter.parse(data.getItemProperty(((Label) lstComponent.get(i).
                                            get(INT_LABEL)).getValue()).getValue().toString());
                                    ((PopupDateField) lstComponent.get(i).get(INT_COMPONENT)).setValue(date);
                                }
                            } else if (((AbstractField) lstComponent.get(i).get(INT_COMPONENT)) instanceof PasswordField) {
                                ((PasswordField) lstComponent.get(i).get(INT_COMPONENT)).setValue("");
                            } else {
                                if (data.getItemProperty(((Label) lstComponent.get(i).get(INT_LABEL)).getValue()).
                                        getValue() != null) {
                                    ((AbstractField) lstComponent.get(i).get(INT_COMPONENT)).setValue(
                                            data.getItemProperty(((Label) lstComponent.get(i).get(INT_LABEL)).getValue()).
                                            getValue().toString());
                                }
                            }
                        }
                    }
                    BaseDAO baseDao = new BaseDAO();
                    if (lstComponentMulti != null && !lstComponentMulti.isEmpty()) {
                        for (int k = 0; k < lstComponentMulti.size(); k++) {
                            List lstRow = lstComponentMulti.get(k);
                            List<Map> lstResult = baseDao.selectAttachData(lstRow,
                                    Long.parseLong(selectedArray[0].toString()));
                            if(lstRow.get(INT_MULTI_IDFIELD) != null) {
                                List<List> lstAttach = (List) lstRow.get(INT_MULTI_ATTACH);
                                int size = 0;
                                if(lstAttach != null && !lstAttach.isEmpty()) size = lstAttach.size();
                                for (int i = 0; i < lstResult.size(); i++) {
                                    Object[] rowData = new Object[1 + size];
                                    PopupMultiAction popup = (PopupMultiAction) lstRow.get(INT_MULTI_POPUP);
                                    rowData[0] = lstResult.get(i).get(popup.getNameColumn());
                                    if(lstAttach != null && !lstAttach.isEmpty()) {
                                        for (int j = 0; j < lstAttach.size(); j++) {
                                            HorizontalLayout txtLayout = new HorizontalLayout();
                                            AbstractField txt = new TextField();
                                            txt.setStyleName("scaleShort");
                                            Object txtObject = lstResult.get(i).get(lstAttach.get(j).get(INT_DB_FIELD_NAME));
                                            txtLayout.addComponent(txt);
                                            if (txtObject != null) {
                                                txt.setValue(txtObject.toString());
                                            }
                                            rowData[j + 1] = txtLayout;
                                        }
                                    }
                                    ((Table) lstRow.get(INT_MULTI_TABLE)).addItem(
                                            rowData, lstResult.get(i).get(popup.getIdColumnName()).toString());
                                }
                            } else {
                                for (int i = 0; i < lstResult.size(); i++) {
                                    Object[] rowData = new Object[1];
                                    PopupMultiAction popup = (PopupMultiAction) lstRow.get(INT_MULTI_POPUP);
                                    rowData[0] = lstResult.get(i).get(popup.getNameColumn());
                                    ((Table) lstRow.get(INT_MULTI_TABLE)).addItem(
                                            rowData, lstResult.get(i).get(popup.getIdColumnName()).toString());
                                }                            
                            }
                            if(lstRow.size() > INT_MULTI_OLDIDS) {
                                lstRow.set(INT_MULTI_OLDIDS, ((Table) lstRow.get(INT_MULTI_TABLE)).getItemIds().toArray());
                            } else if(lstRow.size() == INT_MULTI_OLDIDS){
                                lstRow.add(((Table) lstRow.get(INT_MULTI_TABLE)).getItemIds().toArray());
                            }
                        }
                    }
                    formArea.removeAllComponents();
                    formArea.addComponent(buildFormPanel(numberOfTD));
                    txtButtonState.setValue("edit");
                }
            } else {
                Notification.show(ResourceBundleUtils.getLanguageResource("Common.SelectRequire1"),
                        null, Notification.Type.ERROR_MESSAGE);
            }
        }
        afterPrepareEdit();
    }

    /**
     * Hàm thực hiện nút xem chi tiết
     *
     * @since 15/10/2014 HienDM
     */
    private void buttonDetailClick() throws Exception {
        Object[] selectedArray = ((java.util.Collection) table.getValue()).toArray();
        if (selectedArray != null && selectedArray.length > 0) {
            Item data = table.getItem(selectedArray[0]);
            // Đẩy thông tin từ table vào các trường trên form nhập
            ((AbstractField) lstComponent.get(idField).get(INT_COMPONENT)).setValue(selectedArray[0].toString());
            for (int i = 0; i < lstComponent.size(); i++) {
                // Bỏ qua nếu là sysdate
                if (lstComponent.get(i).size() > INT_SYSDATE
                        && lstComponent.get(i).get(INT_SYSDATE) instanceof Boolean
                        && lstComponent.get(i).get(INT_TO_DATE_COMPONENT) instanceof PopupDateField) {
                    if (!data.getItemProperty(((Label) lstComponent.get(i).get(INT_LABEL)).
                            getValue()).getValue().toString().isEmpty()) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        if (((PopupDateField) lstComponent.get(i).get(INT_COMPONENT)).getResolution().equals(Resolution.DAY)) {
                            formatter = new SimpleDateFormat("dd/MM/yyyy");
                        } else {
                            formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        }
                        Date date = formatter.parse(data.getItemProperty(((Label) lstComponent.get(i).
                                get(INT_LABEL)).getValue()).getValue().toString());
                        ((PopupDateField) lstComponent.get(i).get(INT_COMPONENT)).setValue(date);
                    }
                }
                if (lstComponent.get(i).get(INT_COMPONENT) instanceof MultiUploadField) {
                    BaseDAO baseDao = new BaseDAO();
                    List<Map> lstMultiUpload = baseDao.selectMultiUploadData(lstComponent.get(i),
                            Long.parseLong(selectedArray[0].toString()));
                    MultiUploadField upload = (MultiUploadField) lstComponent.get(i).get(INT_COMPONENT);
                    upload.removeAllDownloadLink();
                    for (int n = 0; n < lstMultiUpload.size(); n++) {
                        DownloadLink dl = new DownloadLink();
                        // Giải mã tên file
                        String fileName = lstMultiUpload.get(n).get("attach_file").toString();
                        String pattern = Pattern.quote(System.getProperty("file.separator"));
                        String[] splittedFileName = fileName.split(pattern);
                        if (splittedFileName.length > 1) {
                            fileName = splittedFileName[splittedFileName.length - 1];
                        } else {
                            splittedFileName = fileName.split("/");
                            fileName = splittedFileName[splittedFileName.length - 1];
                        }
                        String ext = FileUtils.extractFileExt(fileName);
                        fileName = (new String(Base64Utils.decode(FileUtils.extractFileNameNotExt(fileName))));
                        fileName = fileName.substring(fileName.indexOf("_") + 1) + ext;
                        if (fileName.length() > 30) {
                            fileName = fileName.substring(0, 25) + "---" + FileUtils.extractFileExt(fileName);
                        }
                        dl.setCaption(fileName);
                                // End: Giải mã tên file                              

                        // Gán đường dẫn download
                        dl.setFilePath(lstMultiUpload.get(n).get("attach_file").toString());
                        if (dl.getFilePath() != null) {
                            File downloadFile = new File(ResourceBundleUtils.getConfigureResource("FileBaseDirectory")
                                    + dl.getFilePath());
                            if (!downloadFile.exists()) {
                                dl.setComponentError(new UserError(ResourceBundleUtils.getLanguageResource("Common.FileNotExist")));
                            }
                        }
                        final AdvancedFileDownloader downloaderForLink = new AdvancedFileDownloader();
                        downloaderForLink.addAdvancedDownloaderListener(new AdvancedFileDownloader.AdvancedDownloaderListener() {
                            @Override
                            public void beforeDownload(AdvancedFileDownloader.DownloaderEvent downloadEvent) {
                                String filePath = ResourceBundleUtils.getConfigureResource("FileBaseDirectory")
                                        + dl.getFilePath();
                                downloaderForLink.setFilePath(filePath);
                            }
                        });
                        downloaderForLink.extend(dl);
                                // End: Gán đường dẫn download

                        upload.lstDownloadLink.add(dl);

                        // Them link vao vung download
                        HorizontalLayout cell = new HorizontalLayout();
                        Embedded iconDelete = new Embedded(null, new ThemeResource("img/industry/button/delete.png"));
                        cell.addComponent(dl);
                        cell.setComponentAlignment(dl, Alignment.MIDDLE_LEFT);
                        cell.addComponent(iconDelete);
                        cell.setComponentAlignment(iconDelete, Alignment.MIDDLE_LEFT);
                        upload.downloadLinkArea.addComponent(cell);

                        iconDelete.addClickListener(new MouseEvents.ClickListener() {
                            public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
                                try {
                                    upload.downloadLinkArea.removeComponent(cell);
                                    upload.lstDownloadLink.remove(dl);
                                } catch (Exception ex) {
                                    VaadinUtils.handleException(ex);
                                    mainLogger.debug("Industry error: ", ex);
                                }
                            }
                        });
                        // End: Them link vao vung download
                    }                 
                } else if (lstComponent.get(i).get(INT_COMPONENT) instanceof UploadField) {
                    DownloadLink dl = (DownloadLink) data.getItemProperty(
                            ((Label) lstComponent.get(i).get(INT_LABEL)).getValue()).getValue();
                    UploadField uploadField = ((UploadField) lstComponent.get(i).get(INT_COMPONENT));
                    uploadField.setDownloadLink(dl);
                    if (lstComponent.get(i).size() > INT_FILE_EDIT) {
                        lstComponent.get(i).set(INT_FILE_EDIT, dl);
                    } else {
                        lstComponent.get(i).add(dl);
                    }
                } else if (i != idField) {
                    if (((AbstractField) lstComponent.get(i).get(INT_COMPONENT)) instanceof CheckBox) {
                        if (data.getItemProperty(((Label) lstComponent.get(i).get(INT_LABEL)).getValue()).getValue().
                                toString().equals(ResourceBundleUtils.getLanguageResource(
                                                lstComponent.get(i).get(INT_CHECKBOX_ENABLE).toString()))) {
                            ((AbstractField) lstComponent.get(i).get(INT_COMPONENT)).setValue(true);
                        } else {
                            ((AbstractField) lstComponent.get(i).get(INT_COMPONENT)).setValue(false);
                        }
                    } else if (((AbstractField) lstComponent.get(i).get(INT_COMPONENT)) instanceof ComboBox) {
                        if ((lstComponent.get(i).size() > BaseAction.INT_POPUP_BUTTON)
                                && (lstComponent.get(i).get(BaseAction.INT_POPUP_BUTTON) instanceof Button)) {
                            ComboboxItem cboItem = (ComboboxItem) data.getItemProperty(
                                    ((Label) lstComponent.get(i).get(INT_LABEL)).getValue()).getValue();
                            ComboBox cboData = (ComboBox) lstComponent.get(i).get(INT_COMPONENT);
                            cboData.removeAllItems();
                            cboData.addItem(cboItem.getValue());
                            cboData.setItemCaption(cboItem.getValue(), cboItem.getCaption());
                            cboData.setValue(cboItem.getValue());
                        } else {
                            ComboboxItem cboItem = (ComboboxItem) data.getItemProperty(
                                    ((Label) lstComponent.get(i).get(INT_LABEL)).getValue()).getValue();
                            ((ComboBox) lstComponent.get(i).get(INT_COMPONENT)).setValue(cboItem.getValue());
                        }
                    } else if (((AbstractField) lstComponent.get(i).get(INT_COMPONENT)) instanceof PopupDateField) {
                        if (!data.getItemProperty(((Label) lstComponent.get(i).get(INT_LABEL)).
                                getValue()).getValue().toString().isEmpty()) {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            if (((PopupDateField) lstComponent.get(i).get(INT_COMPONENT)).getResolution().equals(Resolution.DAY)) {
                                formatter = new SimpleDateFormat("dd/MM/yyyy");
                            } else {
                                formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            }
                            Date date = formatter.parse(data.getItemProperty(((Label) lstComponent.get(i).
                                    get(INT_LABEL)).getValue()).getValue().toString());
                            ((PopupDateField) lstComponent.get(i).get(INT_COMPONENT)).setValue(date);
                        }
                    } else if (((AbstractField) lstComponent.get(i).get(INT_COMPONENT)) instanceof PasswordField) {
                        ((PasswordField) lstComponent.get(i).get(INT_COMPONENT)).setValue("");
                    } else {
                        if (data.getItemProperty(((Label) lstComponent.get(i).get(INT_LABEL)).getValue()).
                                getValue() != null) {
                            ((AbstractField) lstComponent.get(i).get(INT_COMPONENT)).setValue(
                                    data.getItemProperty(((Label) lstComponent.get(i).get(INT_LABEL)).getValue()).
                                    getValue().toString());
                        }
                    }
                }
            }
            BaseDAO baseDao = new BaseDAO();
            if (lstComponentMulti != null && !lstComponentMulti.isEmpty()) {
                for (int k = 0; k < lstComponentMulti.size(); k++) {
                    List lstRow = lstComponentMulti.get(k);
                    List<Map> lstResult = baseDao.selectAttachData(lstRow,
                            Long.parseLong(selectedArray[0].toString()));
                    if(lstRow.get(INT_MULTI_IDFIELD) != null) {
                        List<List> lstAttach = (List) lstRow.get(INT_MULTI_ATTACH);
                        int size = 0;
                        if(lstAttach != null && !lstAttach.isEmpty()) size = lstAttach.size();
                        for (int i = 0; i < lstResult.size(); i++) {
                            Object[] rowData = new Object[1 + size];
                            PopupMultiAction popup = (PopupMultiAction) lstRow.get(INT_MULTI_POPUP);
                            rowData[0] = lstResult.get(i).get(popup.getNameColumn());
                            if(lstAttach != null && !lstAttach.isEmpty()) {
                                for (int j = 0; j < lstAttach.size(); j++) {
                                    HorizontalLayout txtLayout = new HorizontalLayout();
                                    AbstractField txt = new TextField();
                                    txt.setStyleName("scaleShort");
                                    Object txtObject = lstResult.get(i).get(lstAttach.get(j).get(INT_DB_FIELD_NAME));
                                    txtLayout.addComponent(txt);
                                    if (txtObject != null) {
                                        txt.setValue(txtObject.toString());
                                    }
                                    rowData[j + 1] = txtLayout;
                                }
                            }
                            ((Table) lstRow.get(INT_MULTI_TABLE)).addItem(
                                    rowData, lstResult.get(i).get(popup.getIdColumnName()).toString());
                        }
                    } else {
                        for (int i = 0; i < lstResult.size(); i++) {
                            Object[] rowData = new Object[1];
                            PopupMultiAction popup = (PopupMultiAction) lstRow.get(INT_MULTI_POPUP);
                            rowData[0] = lstResult.get(i).get(popup.getNameColumn());
                            ((Table) lstRow.get(INT_MULTI_TABLE)).addItem(
                                    rowData, lstResult.get(i).get(popup.getIdColumnName()).toString());
                        }                            
                    }
                    if(lstRow.size() > INT_MULTI_OLDIDS) {
                        lstRow.set(INT_MULTI_OLDIDS, ((Table) lstRow.get(INT_MULTI_TABLE)).getItemIds().toArray());
                    } else if(lstRow.size() == INT_MULTI_OLDIDS){
                        lstRow.add(((Table) lstRow.get(INT_MULTI_TABLE)).getItemIds().toArray());
                    }
                }
            }
            formArea.removeAllComponents();
            formArea.addComponent(buildDetailPanel(numberOfTD));
            buttonUpdate.setVisible(false);
        } else {
            Notification.show(ResourceBundleUtils.getLanguageResource("Common.SelectRequire1"),
                    null, Notification.Type.ERROR_MESSAGE);
        }        
    }

    /**
     * Hàm thực hiện nút xóa
     *
     * @since 15/10/2014 HienDM
     */
    private void buttonDeleteClick() throws Exception {
        Object[] deleteArray = ((java.util.Collection) table.getValue()).toArray();
        if (deleteArray != null && deleteArray.length > 0) {
            if (checkPermission(deleteArray)) {
                if(validateDelete(deleteArray)) {
                    ConfirmationDialog.Callback ccbl = new ConfirmationDialog.Callback() {
                        @Override
                        public void onDialogResult(String buttonName) {
                            Connection connection = null;
                            try {
                                if (buttonName.equals(ResourceBundleUtils.getLanguageResource("Common.Yes"))) {
                                    BaseDAO baseDao = new BaseDAO();
                                    connection = C3p0Connector.getInstance().getConnection();
                                    connection.setAutoCommit(false);
                                    // Xử lý trước khi xóa dữ liệu
                                    beforeDeleteData(connection, deleteArray);
                                    //---
                                    if (lstComponentMulti != null && !lstComponentMulti.isEmpty()) {
                                        for( int i = 0; i < deleteArray.length; i++) {
                                            long idConnector = Long.parseLong(deleteArray[i].toString());
                                            baseDao.deleteDataAttach(lstComponentMulti, idConnector, connection);                                        
                                        }
                                    }
                                    baseDao.deleteData(tableName, idColumnName, deleteArray, connection);
                                    // Xử lý sau khi xóa dữ liệu
                                    for (int i = 0; i < lstComponent.size(); i++) {
                                        if (lstComponent.get(i).get(INT_COMPONENT) instanceof MultiUploadField) {
                                            long idConnector = Long.parseLong(deleteArray[i].toString());
                                            baseDao.deleteMultiUploadData(lstComponent.get(i), idConnector, connection);
                                        }
                                    }                                    
                                    afterDeleteData(connection, deleteArray);
                                    //---
                                    connection.commit();
                                    for (int j = 0; j < deleteArray.length; j++) {
                                        for (int i = lstTableData.size() - 1; i >= 0; i--) {
                                            if (lstTableData.get(i).get(idColumnName).toString().equals(deleteArray[j])){
                                                lstTableData.remove(i);
                                            }
                                        }
                                    }
                                    updateData();
                                    finishDelete(deleteArray);
                                    Notification.show(ResourceBundleUtils.getLanguageResource("Common.DeleteSuccess"),
                                            null, Notification.Type.WARNING_MESSAGE);
                                }
                            } catch (Exception ex) {
                                if(connection != null) try {
                                    connection.rollback();
                                } catch (SQLException ex1) {
                                    VaadinUtils.handleException(ex);
                                    MainUI.mainLogger.debug("Install error: ", ex1);
                                }
                                VaadinUtils.handleException(ex);
                                MainUI.mainLogger.debug("Install error: ", ex);
                            } finally {
                                if(connection != null) try {
                                    connection.close();
                                } catch (SQLException ex) {
                                    VaadinUtils.handleException(ex);
                                    MainUI.mainLogger.debug("Install error: ", ex);
                                }
                                connection = null;
                            }
                        }
                    };
                    mainUI.addWindow(new ConfirmationDialog(
                            ResourceBundleUtils.getLanguageResource("Common.Confirm"),
                            ResourceBundleUtils.getLanguageResource("Common.ConfirmExecute"), ccbl));
                }
            }
        } else {
            Notification.show(ResourceBundleUtils.getLanguageResource("Common.SelectRequireAtLeast1"),
                    null, Notification.Type.ERROR_MESSAGE);
        }        
    }    

    public void finishAdd(long id) throws Exception {}
    public void finishEdit(long id) throws Exception {}
    public void finishDelete(Object[] arrayId) throws Exception {}
    
    /**
     * Hàm thực hiện nút cập nhật
     *
     * @since 15/10/2014 HienDM
     */
    private void buttonUpdateClick() throws Exception {
        // Thêm mới dữ liệu
        if (validateInput(txtButtonState.getValue())) {// Kiểm tra dữ liệu đầu vào
            ConfirmationDialog.Callback ccbl = new ConfirmationDialog.Callback() {
                @Override
                public void onDialogResult(String buttonName) {
                    Connection connection = null;
                    try {
                        if (buttonName.equals(ResourceBundleUtils.getLanguageResource("Common.Yes"))) {
                            if (txtButtonState.getValue().equals("add")) {
                                for (int i = 0; i < lstComponent.size(); i++) {
                                    if (lstComponent.get(i).get(INT_COMPONENT) instanceof UploadField) {
                                        boolean checkFileNull = true;
                                        UploadField uf = ((UploadField) lstComponent.get(i).get(INT_COMPONENT));
                                        InputStream is = null;
                                        try {
                                            is = uf.getContentAsStream();
                                        } catch (Exception ex) {
                                            checkFileNull = false;
                                        }
                                        if (checkFileNull) {
                                            Calendar cal = Calendar.getInstance();
                                            String subFolder = FileUtils.extractFileExt(lstComponent.get(i).
                                                    get(INT_FILE_DIRECTORY).toString()).substring(1);
                                            String fileDirectory
                                                    = ResourceBundleUtils.getConfigureResource("FileBaseDirectory") + subFolder;
                                            File directory = new File(fileDirectory);
                                            if (!directory.exists()) {
                                                directory.mkdir();
                                            }
                                            String fileName = "" + cal.get(Calendar.YEAR)
                                                    + (cal.get(Calendar.MONTH) + 1) + cal.get(Calendar.DATE)
                                                    + cal.get(Calendar.HOUR) + cal.get(Calendar.MINUTE) + cal.get(Calendar.SECOND)
                                                    + "_" + FileUtils.extractFileNameNotExt(uf.getLastFileName());
                                            String encodeFileName = Base64Utils.encodeBytes(fileName.getBytes())
                                                    + FileUtils.extractFileExt(uf.getLastFileName());
                                            lstComponent.get(i).set(INT_FILE_PATH, subFolder + File.separator + encodeFileName);
                                            FileUtils.writeFileFromInputStream(is, fileDirectory + File.separator + encodeFileName);
                                        }
                                    }
                                }
                                BaseDAO baseDao = new BaseDAO();
                                connection = C3p0Connector.getInstance().getConnection();
                                connection.setAutoCommit(false);
                                // Xử lý trước khi add dữ liệu                                
                                beforeAddData(connection);
                                //---
                                long id = baseDao.insertData(lstComponent, tableName, idField, sequenceName, connection);
                                if (lstComponentMulti != null && !lstComponentMulti.isEmpty()) {
                                    baseDao.insertDataAttach(lstComponentMulti, id, connection);
                                }
                                // Xử lý sau khi add dữ liệu
                                for (int i = 0; i < lstComponent.size(); i++) {
                                    if (lstComponent.get(i).get(INT_COMPONENT) instanceof MultiUploadField) {
                                        baseDao.insertMultiUploadData(lstComponent.get(i), id, connection);
                                    }
                                }
                                afterAddData(connection, id);
                                //---
                                connection.commit();
                                if(!refreshAlready) {
                                    refreshAlready = false;
                                    Map row = new HashMap();
                                    row.put(idColumnName, "" + id);
                                    for (int i = 0; i < lstComponent.size(); i++) {
                                        if (i == idField) {
                                            continue;
                                        }
                                        if (lstComponent.get(i).get(INT_COMPONENT) instanceof CheckBox) {
                                            Integer value = 0;
                                            if (((CheckBox) lstComponent.get(i).get(INT_COMPONENT)).getValue()) {
                                                value = 1;
                                            }
                                            row.put(lstComponent.get(i).get(INT_DB_FIELD_NAME), value);
                                        } else if (lstComponent.get(i).size() > INT_POPUP_BUTTON
                                                && (lstComponent.get(i).get(INT_POPUP_BUTTON) instanceof Button)) {
                                            ComboBox cbo = (ComboBox) lstComponent.get(i).get(INT_COMPONENT);
                                            row.put(lstComponent.get(i).get(INT_DB_FIELD_NAME), cbo.getValue());
                                            
                                            String aliasTableName = lstComponent.get(i).get(BaseAction.INT_COMBOBOX_TABLENAME).toString();
                                            String aliasDbFieldName = lstComponent.get(i).get(BaseAction.INT_DB_FIELD_NAME).toString();
                                            if (aliasTableName.length() > 10) aliasTableName = aliasTableName.substring(0, 10);
                                            if (aliasDbFieldName.length() > 10) aliasDbFieldName = aliasDbFieldName.substring(0, 10);          
                                            row.put(aliasTableName + aliasDbFieldName, cbo.getItemCaption(cbo.getValue()));
                                        } else {
                                            if (lstComponent.get(i).get(INT_COMPONENT) instanceof MultiUploadField) {
                                                // Không làm gì
                                            } else if (lstComponent.get(i).get(INT_COMPONENT) instanceof UploadField) {
                                                row.put(lstComponent.get(i).get(INT_DB_FIELD_NAME),
                                                        lstComponent.get(i).get(INT_FILE_PATH));
                                            } else {
                                                row.put(lstComponent.get(i).get(INT_DB_FIELD_NAME),
                                                        ((AbstractField) lstComponent.get(i).get(INT_COMPONENT)).getValue());
                                            }
                                        }
                                    }
                                    lstTableData.add(row);
                                    updateData();
                                }
                                updateForm();
                                clearForm();
                                finishAdd(id);
                                Notification.show(ResourceBundleUtils.getLanguageResource("Common.AddSuccess"),
                                        null, Notification.Type.WARNING_MESSAGE);
                            }
                            if (txtButtonState.getValue().equals("edit")) {
                                for (int i = 0; i < lstComponent.size(); i++) {
                                    if (lstComponent.get(i).get(INT_COMPONENT) instanceof UploadField) {
                                        boolean checkFileNull = true;
                                        UploadField uf = ((UploadField) lstComponent.get(i).get(INT_COMPONENT));
                                        InputStream is = null;
                                        try {
                                            is = uf.getContentAsStream();
                                        } catch (Exception ex) {
                                            checkFileNull = false;
                                        }
                                        if (checkFileNull) {
                                            Calendar cal = Calendar.getInstance();
                                            String subFolder = FileUtils.extractFileExt(lstComponent.get(i).
                                                    get(INT_FILE_DIRECTORY).toString()).substring(1);
                                            String fileDirectory
                                                    = ResourceBundleUtils.getConfigureResource("FileBaseDirectory") + subFolder;
                                            File directory = new File(fileDirectory);
                                            if (!directory.exists()) {
                                                directory.mkdir();
                                            }
                                            String fileName = "" + cal.get(Calendar.YEAR)
                                                    + (cal.get(Calendar.MONTH) + 1) + cal.get(Calendar.DATE)
                                                    + cal.get(Calendar.HOUR) + cal.get(Calendar.MINUTE) + cal.get(Calendar.SECOND)
                                                    + "_" + FileUtils.extractFileNameNotExt(uf.getLastFileName());
                                            String encodeFileName = Base64Utils.encodeBytes(fileName.getBytes())
                                                    + FileUtils.extractFileExt(uf.getLastFileName());
                                            lstComponent.get(i).set(INT_FILE_PATH, subFolder + File.separator + encodeFileName);
                                            FileUtils.writeFileFromInputStream(is, fileDirectory + File.separator + encodeFileName);
                                        }
                                    }
                                }
                                BaseDAO baseDao = new BaseDAO();
                                connection = C3p0Connector.getInstance().getConnection();
                                connection.setAutoCommit(false);
                                // Xử lý trước khi sửa dữ liệu
                                long idConnector = Long.parseLong(((AbstractField) lstComponent.get(idField).get(
                                    BaseAction.INT_COMPONENT)).getValue().toString());                                
                                beforeEditData(connection, idConnector);
                                //---
                                baseDao.updateData(lstComponent, tableName, idField, connection);
                                if(!isIgnoreAttachWhenEdit()) {
                                    if (lstComponentMulti != null && !lstComponentMulti.isEmpty()) {
                                        baseDao.deleteDataAttach(lstComponentMulti, idConnector, connection);
                                        baseDao.insertDataAttach(lstComponentMulti, idConnector, connection);
                                    }
                                }
                                // Xử lý sau khi sửa dữ liệu
                                for (int i = 0; i < lstComponent.size(); i++) {
                                    if (lstComponent.get(i).get(INT_COMPONENT) instanceof MultiUploadField) {
                                        baseDao.deleteMultiUploadData(lstComponent.get(i), idConnector, connection);
                                        baseDao.insertMultiUploadData(lstComponent.get(i), idConnector, connection);
                                    }
                                }
                                afterEditData(connection, idConnector);
                                //---
                                connection.commit();
                                if(!refreshAlready) {
                                    refreshAlready = false;
                                    for (int i = 0; i < lstTableData.size(); i++) {
                                        if (lstTableData.get(i).get(idColumnName).toString().equals(
                                                ((AbstractField) lstComponent.get(idField).get(INT_COMPONENT)).getValue())) {
                                            for (int j = 0; j < lstComponent.size(); j++) {
                                                if (lstComponent.get(j).get(INT_COMPONENT) instanceof CheckBox) {
                                                    Integer value = 0;
                                                    if (((CheckBox) lstComponent.get(j).get(INT_COMPONENT)).getValue()) {
                                                        value = 1;
                                                    }
                                                    lstTableData.get(i).put(lstComponent.get(j).get(INT_DB_FIELD_NAME), value);
                                                } else {
                                                    // Bỏ qua nếu là sysdate
                                                    if (lstComponent.get(j).size() > BaseAction.INT_SYSDATE
                                                            && lstComponent.get(j).get(BaseAction.INT_SYSDATE) instanceof Boolean
                                                            && lstComponent.get(j).get(INT_TO_DATE_COMPONENT) instanceof PopupDateField) {
                                                        continue;
                                                    }
                                                    // Bỏ qua nếu là login user
                                                    if (lstComponent.get(j).size() > BaseAction.INT_LOGINUSER
                                                            && lstComponent.get(j).get(BaseAction.INT_LOGINUSER) != null
                                                            && lstComponent.get(j).get(BaseAction.INT_LOGINUSER).equals("LoginUser")) {
                                                        continue;
                                                    }
                                                    if (lstComponent.get(j).get(INT_COMPONENT) instanceof MultiUploadField) {
                                                        // Không làm gì
                                                    } else if (lstComponent.get(j).get(INT_COMPONENT) instanceof UploadField) {
                                                        lstTableData.get(i).put(lstComponent.get(j).get(INT_DB_FIELD_NAME),
                                                                lstComponent.get(j).get(INT_FILE_PATH));
                                                    } else if (lstComponent.get(j).size() > INT_POPUP_BUTTON
                                                                && (lstComponent.get(j).get(INT_POPUP_BUTTON) instanceof Button)) {
                                                        ComboBox cbo = (ComboBox) lstComponent.get(j).get(INT_COMPONENT);
                                                        lstTableData.get(i).put(lstComponent.get(j).get(INT_DB_FIELD_NAME),cbo.getValue());
                                                        
                                                        String aliasTableName = lstComponent.get(j).get(BaseAction.INT_COMBOBOX_TABLENAME).toString();
                                                        String aliasDbFieldName = lstComponent.get(j).get(BaseAction.INT_DB_FIELD_NAME).toString();
                                                        if (aliasTableName.length() > 10) aliasTableName = aliasTableName.substring(0, 10);
                                                        if (aliasDbFieldName.length() > 10) aliasDbFieldName = aliasDbFieldName.substring(0, 10);
                                                        lstTableData.get(i).put(aliasTableName + aliasDbFieldName, cbo.getItemCaption(cbo.getValue()));
                                                    } else {
                                                        lstTableData.get(i).put(lstComponent.get(j).get(INT_DB_FIELD_NAME),
                                                                ((AbstractField) lstComponent.get(j).get(INT_COMPONENT)).getValue());
                                                    }
                                                }
                                            }
                                            break;
                                        }
                                    }
                                    updateData();
                                }
                                if(!isNotUpdateForm()) updateForm();
                                finishEdit(idConnector);
                                Notification.show(ResourceBundleUtils.getLanguageResource("Common.UpdateSuccess"),
                                        null, Notification.Type.WARNING_MESSAGE);
                                if(!isNotUpdateForm()) {
                                    formArea.removeAllComponents();
                                    formArea.addComponent(buildSearchFormPanel(numberOfTD));
                                }
                            }
                        }
                    } catch (Exception ex) {
                        if (connection != null) {
                            try {
                                connection.rollback();
                            } catch (SQLException e) {
                                VaadinUtils.handleException(e);
                                MainUI.mainLogger.debug("Install error: ", e);
                            }
                        }
                        VaadinUtils.handleException(ex);
                        MainUI.mainLogger.debug("Install error: ", ex);
                    } finally {
                        if (connection != null) {
                            try {
                                connection.close();
                            } catch (SQLException ex) {
                                VaadinUtils.handleException(ex);
                                MainUI.mainLogger.debug("Install error: ", ex);
                            }
                            connection = null;
                        }
                    }
                }
            };
            mainUI.addWindow(new ConfirmationDialog(
                    ResourceBundleUtils.getLanguageResource("Common.Confirm"),
                    ResourceBundleUtils.getLanguageResource("Common.ConfirmExecute"), ccbl));
        }        
    }        
    
    /**
     * Hàm thực hiện nút bỏ qua
     *
     * @since 15/10/2014 HienDM
     */
    private void buttonCancelClick() throws Exception {
        buttonArea.removeAllComponents();
        HorizontalLayout panelButton = buildPanelButton();
        buttonArea.addComponent(panelButton);
        buttonArea.setComponentAlignment(panelButton, Alignment.MIDDLE_CENTER);
        formArea.removeStyleName("panel-highlight");
        buttonArea.removeStyleName("panel-highlight");
        formArea.removeAllComponents();
        formArea.addComponent(buildSearchFormPanel(numberOfTD));
        clearForm();        
    } 
    
    public void addExportParameter(String name, Object value) {
        List lstRow = new ArrayList();
        lstRow.add(name);
        lstRow.add(value);
        lstExportParameter.add(lstRow);
    }
    
    /**
     * Hàm thực hiện nút xuất file
     * 
     * @param downloaderForLink thành phần download
     * @since 15/10/2014 HienDM
     */
    private void buttonExportClick(AdvancedFileDownloader downloaderForLink) throws Exception {      
        // Tạo file download
        Object[] allItemIds = ((java.util.Collection) table.getItemIds()).toArray();
        Object[][] totalData = new Object[allItemIds.length + 1][table.getColumnHeaders().length];
        int order = 0;
        if (includeOrder) {
            order = 1;
        }
        int countON = 0;
        Object[] header = new Object[lstComponent.size() - 1 + order];
        if(includeOrder) header[0] = ResourceBundleUtils.getLanguageResource("Common.Order");
        int countHeader = 1;
        for (int j = 0; j < lstComponent.size(); j++) {
            if (j != idField) {
                header[countHeader] = ((Label) lstComponent.get(j).get(INT_LABEL)).getValue();
                countHeader++;
            }
        }
        totalData[0] = header;
        for (int i = 0; i < allItemIds.length; i++) {
            countON++;
            Object[] data = new Object[lstComponent.size() - 1 + order];
            Item item = table.getItem(allItemIds[i]);
            int count = order;
            if (includeOrder) {
                data[0] = countON;
            }
            for (int j = 0; j < lstComponent.size(); j++) {
                if (j != idField) {
                    Object value = item.getItemProperty(((Label) lstComponent.get(j).
                            get(INT_LABEL)).getValue()).getValue();
                    if(value instanceof ComboboxItem) {
                        data[count] = ((ComboboxItem)value).getCaption();
                    } else {
                        data[count] = value;
                    }
                    count++;
                }
            }
            totalData[i + 1] = data;
        }
        String strDirectory = ResourceBundleUtils.getConfigureResource("FileBaseDirectory") + 
                "Temp";
        String strTemplate = ResourceBundleUtils.getConfigureResource("FileBaseDirectory") + "Templates" +
                File.separator + templateFile;
        File fileDirectory = new File(strDirectory);
        if(!fileDirectory.exists()) fileDirectory.mkdir();
        EncryptDecryptUtils edu = new EncryptDecryptUtils();
        Calendar cal = Calendar.getInstance();
        String strName = cal.get(Calendar.YEAR) + (cal.get(Calendar.MONTH) + 1) + 
                cal.get(Calendar.DATE) + cal.get(Calendar.HOUR) + cal.get(Calendar.MINUTE) + 
                cal.get(Calendar.SECOND) + VaadinUtils.getSessionAttribute("G_UserName").toString();
        strName = edu.encodePassword(strName);
        String pattern = Pattern.quote(System.getProperty("file.separator"));
        strName = strName.replaceAll(pattern, "_");
        strName = strName.replaceAll("/", "_");
        String strPath = ResourceBundleUtils.getConfigureResource("FileBaseDirectory") + "Temp"
                + File.separator + strName;
        String filePath = "";
        if (ResourceBundleUtils.getConfigureResource("ExcelMaxRow").equals("csv")) {
            filePath = strPath + ".csv";
            CSVTransformer.transformCSV(totalData, filePath);
        } else if (ResourceBundleUtils.getConfigureResource("ExcelMaxRow").equals("excel")) {
            filePath = strPath + ".xls";
            FileUtils.exportExcelWithTemplate(totalData, strTemplate, filePath, templateHeight, lstExportParameter);
        } else {
            // Nếu lớn hơn ExcelMaxRow thì xuất CSV
            if (table.size() > Long.parseLong(
                    ResourceBundleUtils.getConfigureResource("ExcelMaxRow"))) {
                filePath = strPath + ".csv";
                CSVTransformer.transformCSV(totalData, filePath);
            } else { // xuất excel
                filePath = strPath + ".xlsx";
                FileUtils.exportExcel(totalData, filePath);
            }
        }
        //Download file

        File downloadFile = new File(filePath);
        if (!downloadFile.exists()) {
            Notification.show(ResourceBundleUtils.getLanguageResource("Common.FileNotExist"),
                    null, Notification.Type.ERROR_MESSAGE);
        }
        downloaderForLink.setFilePath(filePath);
    }
    
    /**
     * Hàm xử lý trước khi add dữ liệu
     * 
     * @param connection kết nối đến cơ sở dữ liệu
     * @since 15/01/2015 HienDM
     */
    public void beforeAddData(Connection connection) throws Exception {}
    
    /**
     * Hàm xử lý sau khi add dữ liệu
     * 
     * @param connection kết nối đến cơ sở dữ liệu
     * @param id khóa chính dữ liệu
     * @since 15/01/2015 HienDM
     */
    public void afterAddData(Connection connection, long id) throws Exception {}
    
    /**
     * Hàm xử lý trước khi sửa dữ liệu
     * 
     * @param connection kết nối đến cơ sở dữ liệu
     * @param id khóa chính dữ liệu
     * @since 15/01/2015 HienDM
     */
    public void beforeEditData(Connection connection, long id) throws Exception {}
    
    /**
     * Hàm xử lý sau khi sửa dữ liệu
     * 
     * @param connection kết nối đến cơ sở dữ liệu
     * @param id khóa chính dữ liệu
     * @since 15/01/2015 HienDM
     */
    public void afterEditData(Connection connection, long id) throws Exception {}
    
    /**
     * Hàm xử lý trước khi xóa dữ liệu
     * 
     * @param connection kết nối đến cơ sở dữ liệu
     * @param deleteArray danh sách khóa chính của bản ghi cần xóa
     * @since 15/01/2015 HienDM
     */
    public void beforeDeleteData(Connection connection, Object[] deleteArray) throws Exception {}
    
    /**
     * Hàm xử lý sau khi xóa dữ liệu
     * 
     * @param connection kết nối đến cơ sở dữ liệu
     * @param deleteArray danh sách khóa chính của bản ghi cần xóa
     * @since 15/01/2015 HienDM
     */
    public void afterDeleteData(Connection connection, Object[] deleteArray) throws Exception {}
    
    /**
     * Hàm khời tạo vùng giao diện nút bấm
     *
     * @since 15/10/2014 HienDM
     * @return Vùng giao diện các nút bấm
     */
    public HorizontalLayout buildPanelButton() throws Exception {
        if(panelButton != null) panelButton.removeAllComponents();
        panelButton.setSpacing(true);

        buttonFind = new Button(ResourceBundleUtils.getLanguageResource("Button.Find"));
        buttonFind.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    buttonFindClick();
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        if(isAllowSearch()) panelButton.addComponent(buttonFind);
        
        Button buttonAdd = new Button(ResourceBundleUtils.getLanguageResource("Button.Add"));
        buttonAdd.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    buttonAddClick();
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        if(isAllowAdd()) panelButton.addComponent(buttonAdd);

        final Button buttonDetail = new Button(ResourceBundleUtils.getLanguageResource("Button.Detail"));
        buttonDetail.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    buttonDetailClick();
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        if(isAllowDetail()) panelButton.addComponent(buttonDetail);
        
        final Button buttonEdit = new Button(ResourceBundleUtils.getLanguageResource("Button.Edit"));
        buttonEdit.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    buttonEditClick();
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        if(isAllowEdit()) panelButton.addComponent(buttonEdit);

        Button buttonDelete = new Button(ResourceBundleUtils.getLanguageResource("Button.Delete"));
        buttonDelete.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    buttonDeleteClick();
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }                
            }
        });
        if(isAllowDelete()) panelButton.addComponent(buttonDelete);
        
        Button buttonExport = new Button(ResourceBundleUtils.getLanguageResource("Button.Export"));
        final AdvancedFileDownloader downloaderForLink = new AdvancedFileDownloader();
        downloaderForLink.addAdvancedDownloaderListener(new AdvancedDownloaderListener() {
            @Override
            public void beforeDownload(DownloaderEvent downloadEvent) {
                try {
                    buttonExportClick(downloaderForLink);
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                } 
            }
        });        
        downloaderForLink.extend(buttonExport);
        if(isAllowExport()) panelButton.addComponent(buttonExport);
        
        if(lstButton != null && !lstButton.isEmpty()) {
            for (int i = 0; i < lstButton.size(); i++) {
                panelButton.addComponent(lstButton.get(i));
            }
        }
        
        buttonFind.setStyleName(Reindeer.BUTTON_DEFAULT);
        buttonFind.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        return panelButton;
    }
    
    public void addButton(Button button) {
        lstButton.add(button);
    }
    
    /**
     * Hàm kiểm tra dữ liệu đầu vào có hợp lệ hay không
     *
     * @since 07/12/2014 HienDM
     * @return true: hợp lệ, false: không hợp lệ
     */    
    public boolean validateAdd() throws Exception{
        return true;
    }
    
    /**
     * Hàm kiểm tra dữ liệu đầu vào có hợp lệ hay không
     *
     * @since 07/12/2014 HienDM
     * @return true: hợp lệ, false: không hợp lệ
     */    
    public boolean validateEdit(long id) throws Exception{
        return true;
    }
    
    /**
     * Hàm xóa lỗi trên giao diện theo list
     * @param lstComp list component truyền vào
     * @since 15/10/2014 HienDM
     */
    public void clearErrorList(List<List> lstComp) throws Exception{
        for (int i = 0; i < lstComp.size(); i++) {
            if (lstComp.get(i).get(INT_COMPONENT) instanceof MultiUploadField) {
                ((MultiUploadField) lstComp.get(i).get(INT_COMPONENT)).setComponentError(null);
            } else if (lstComp.get(i).get(INT_COMPONENT) instanceof UploadField) {
                ((UploadField) lstComp.get(i).get(INT_COMPONENT)).setComponentError(null);
            } else {
                ((AbstractField) lstComp.get(i).get(INT_COMPONENT)).setComponentError(null);
            }
        }
    }
    
    /**
     * Hàm xóa lỗi trên giao diện
     *
     * @since 02/01/2015 HienDM
     */
    public void clearError() throws Exception {
        clearErrorList(lstComponent);
        for (int i = 0; i < lstComponentMulti.size(); i++) {
            ((Table) lstComponentMulti.get(i).get(INT_MULTI_TABLE)).setComponentError(null);
            
            List<List> lstAttach = (List<List>) lstComponentMulti.get(i).get(INT_MULTI_ATTACH);
            if(lstAttach != null && !lstAttach.isEmpty()) {
                Table attachTable = (Table) lstComponentMulti.get(i).get(INT_MULTI_TABLE);
                Object[] arrayIdAttach = attachTable.getItemIds().toArray();
                for (int m = 0; m < lstAttach.size(); m++) { // danh sách các cột
                    for (int n = 0; n < arrayIdAttach.length; n++) { // danh sách các phần tử trong cột
                        Item itemAttach = attachTable.getItem(arrayIdAttach[n]);
                        HorizontalLayout txtLayout = (HorizontalLayout) itemAttach.getItemProperty(
                                ((Label) lstAttach.get(m).get(INT_LABEL)).getValue()).getValue();
                        TextField txt = (TextField) txtLayout.getComponent(0);
                        txt.removeStyleName("scaleShort");
                        txt.setComponentError(null);
                    }
                }
            }
        }
        clearErrorList(lstCustomizeComponent);
    }
    
    /**
     * Hàm kiểm tra dữ liệu đầu vào có hợp lệ hay không
     *
     * @since 07/12/2014 HienDM
     * @param actionType kiểu tác động insert hay update
     * @return true: hợp lệ, false: không hợp lệ
     */
    public boolean validateInput(String actionType) throws Exception {
        clearError();
        boolean check = true;
        if(!validateInput(actionType, lstComponent)) {
            check = false;
        }
        for (int i = 0; i < lstComponentMulti.size(); i++) {
            if ((boolean) lstComponentMulti.get(i).get(INT_MULTI_MANDATORY)) {
                if (((Table) lstComponentMulti.get(i).get(INT_MULTI_TABLE)).getItemIds().isEmpty()) {
                    ((Table) lstComponentMulti.get(i).get(INT_MULTI_TABLE)).setComponentError(new UserError(
                            "[" + ((Label) lstComponentMulti.get(i).get(INT_LABEL)).getValue() + "] "
                            + ResourceBundleUtils.getLanguageResource("Common.Error.IsRequired")));
                    check = false;
                }
            }
            List<List> lstAttach = (List<List>) lstComponentMulti.get(i).get(INT_MULTI_ATTACH);
            if(lstAttach != null && !lstAttach.isEmpty()) {
                Table attachTable = (Table)lstComponentMulti.get(i).get(INT_MULTI_TABLE);
                Object[] arrayIdAttach = attachTable.getItemIds().toArray();
                for(int m = 0; m < lstAttach.size(); m++) { // danh sách các cột
                    List lstCompInColumn = new ArrayList();
                    for(int n = 0; n < arrayIdAttach.length; n++) { // danh sách các phần tử trong cột
                        ArrayList lstCell = (ArrayList)((ArrayList)lstAttach.get(m)).clone();
                        Item itemAttach = attachTable.getItem(arrayIdAttach[n]);
                        HorizontalLayout txtLayout = (HorizontalLayout)itemAttach.getItemProperty(
                                ((Label)lstCell.get(INT_LABEL)).getValue()).getValue();
                        TextField txt = (TextField)txtLayout.getComponent(0);
                        txt.setStyleName("scaleShort");
                        lstCell.set(INT_COMPONENT, txt);
                        lstCompInColumn.add(lstCell);
                    }
                    if (!validateInput(actionType, lstCompInColumn)) {
                        check = false;
                    }
                }
            }
        }
        if(!validateInput(actionType, lstCustomizeComponent)) {
            check = false;
        }
        if (actionType.equals("add")) {
            if (!validateAdd()) {
                check = false;
            }
        }
        if (actionType.equals("edit")) {
            if (!validateEdit(
                    Long.parseLong(
                            ((AbstractField) lstComponent.get(idField).get(INT_COMPONENT)).getValue().toString()
                    ))) {
                check = false;
            }
        }
        return check;
    }
    
    /**
     * Hàm kiểm tra dữ liệu đầu vào có hợp lệ hay không
     *
     * @since 07/12/2014 HienDM
     * @param actionType kiểu tác động insert hay update
     * @return true: hợp lệ, false: không hợp lệ
     */    
    private boolean validateInput(String actionType, List<List> lstComp) throws Exception {
        boolean check = true;
        Object idvalue = ((AbstractField)lstComponent.get(idField).get(INT_COMPONENT)).getValue();
        for(int i = 0; i < lstComp.size(); i++) {
            // Thiet lap default value tai man hinh them moi
            if (lstComp.get(i).size() > INT_VISIBLE_ADD && lstComp.get(i).get(INT_VISIBLE_ADD).equals(false)
                    && !(idvalue != null && !idvalue.toString().isEmpty())) {
                if (lstComp.get(i).get(INT_COMPONENT) instanceof AbstractField) {
                    if (lstComp.get(i).get(INT_COMPONENT) instanceof CheckBox) {
                        if((boolean) lstComp.get(i).get(INT_CHECKBOX_DEFAULT)) {
                            ((CheckBox) lstComp.get(i).get(INT_COMPONENT)).setValue(true);
                        } else {
                            ((CheckBox) lstComp.get(i).get(INT_COMPONENT)).setValue(false);
                        }
                    } else if (lstComp.get(i).get(INT_DEFAULT) != null) {
                        ((AbstractField) lstComp.get(i).get(INT_COMPONENT)).setValue(
                                lstComp.get(i).get(INT_DEFAULT));
                    }
                }
            }
            if(ResourceBundleUtils.getConfigureResource("strongPassword") != null &&
                    ResourceBundleUtils.getConfigureResource("strongPassword").equals("true")) {
                if(lstComp.get(i).get(INT_COMPONENT) instanceof PasswordField && (boolean)lstComp.get(i).get(INT_IS_PASSWORD)) {
                    String passValue = ((PasswordField)lstComp.get(i).get(INT_COMPONENT)).getValue();
                    if(passValue != null && !passValue.isEmpty()) {
                        // Kiểm tra mật khẩu mạnh
                        String passwordCondition = "((?=.*\\d)(?=.*[a-z])(?=.*[@#$%]).{8,200})";
                        Pattern pattern = Pattern.compile(passwordCondition);
                        Matcher matcher = pattern.matcher(passValue);
                        if(!matcher.matches()) {
                            ((PasswordField)lstComp.get(i).get(INT_COMPONENT)).setComponentError(
                                    new UserError(ResourceBundleUtils.getLanguageResource("Common.Error.StrongPassword")));
                            check = false;
                        }
                    }
                }
            }
            if(lstComp.get(i).get(INT_COMPONENT) instanceof MultiUploadField) {
                MultiUploadField uf = (MultiUploadField)lstComp.get(i).get(INT_COMPONENT);
                if ((boolean) lstComp.get(i).get(INT_MANDATORY)) {
                    if(uf.lstDownloadLink == null || uf.lstDownloadLink.isEmpty()) {
                        uf.setComponentError(new UserError(
                                "[" + ((Label) lstComp.get(i).get(INT_LABEL)).getValue() + "] "
                                + ResourceBundleUtils.getLanguageResource("Common.Error.IsRequired")));
                        check = false;
                    }
                }                
            } else if(lstComp.get(i).get(INT_COMPONENT) instanceof UploadField) {
                UploadField uf = (UploadField)lstComp.get(i).get(INT_COMPONENT);
                String fileName = uf.getLastFileName();
                if(fileName != null) {
                    if(!FileUtils.checkSafeFileName(fileName)) {
                        uf.setComponentError(new UserError(ResourceBundleUtils.
                                getLanguageResource("Common.Error.FileInvalid")));
                        check = false;
                    }
                    if(!FileUtils.isAllowedType(fileName,
                            ResourceBundleUtils.getConfigureResource("FileAllowList"))) {
                        uf.setComponentError(new UserError(
                                ResourceBundleUtils.getLanguageResource("Common.Error.FileFormat") + " (" +
                                ResourceBundleUtils.getConfigureResource("FileAllowList") + ")"));
                        check = false;
                    }
                } else if((boolean)lstComp.get(i).get(INT_MANDATORY)) {
                    uf.setComponentError(new UserError(
                            "[" + ((Label) lstComp.get(i).get(INT_LABEL)).getValue() + "] "
                            + ResourceBundleUtils.getLanguageResource("Common.Error.IsRequired")));
                    check = false;                    
                }
            } else if(!lstComp.get(i).get(INT_DB_FIELD_NAME).toString().toLowerCase().equals(idColumnName)) {
                AbstractField component = (AbstractField)lstComp.get(i).get(INT_COMPONENT);
                if(!(actionType.equals("edit") && (component instanceof PasswordField))) {
                    if(component.getValue() != null && !component.getValue().toString().trim().equals("")) {                        
                        // Validate định dạng dữ liệu
                        if(lstComp.get(i).get(INT_FORMAT) != null) {
                            String format = lstComp.get(i).get(INT_FORMAT).toString().toLowerCase().trim();
                            //validate số nguyên
                            if(format.contains("long") || format.contains("int")) {
                                Long value = null;
                                try {
                                    value = Long.parseLong(component.getValue().toString());
                                } catch (Exception ex) {
                                    component.setComponentError(new UserError(
                                            ResourceBundleUtils.getLanguageResource("Common.ValidateIntegerFailed")));
                                    check = false;
                                }
                                // validate max
                                if(value != null) {
                                    // validate max
                                    if(format.contains("<=")) {
                                        Long maxValue = Long.parseLong(format.split("<=")[1]);
                                        format = format.split("<=")[0];
                                        if(value > maxValue) {
                                            component.setComponentError(new UserError(
                                                ResourceBundleUtils.getLanguageResource("Common.ValidateFormat") + 
                                                        " <= " + maxValue));
                                            check = false;
                                        }
                                    } else if(format.contains("<")) {
                                        Long maxValue = Long.parseLong(format.split("<")[1]);
                                        format = format.split("<")[0];
                                        if (value >= maxValue) {
                                            component.setComponentError(new UserError(
                                                ResourceBundleUtils.getLanguageResource("Common.ValidateFormat") +
                                                        " < " + maxValue));
                                            check = false;
                                        }
                                    }
                                    // validate min
                                    if (format.contains(">=")) {
                                        Long minValue = Long.parseLong(format.split(">=")[1]);
                                        if (value < minValue) {
                                            component.setComponentError(new UserError(
                                                    ResourceBundleUtils.getLanguageResource("Common.ValidateFormat")
                                                    + " >= " + minValue));
                                            check = false;
                                        }
                                    } else if (format.contains(">")) {
                                        Long minValue = Long.parseLong(format.split(">")[1]);
                                        if (value <= minValue) {
                                            component.setComponentError(new UserError(
                                                    ResourceBundleUtils.getLanguageResource("Common.ValidateFormat")
                                                    + " > " + minValue));
                                            check = false;
                                        }
                                    }
                                }
                            }
                            //Validate số thực
                            if (format.contains("double") || format.contains("float")) {
                                Double value = null;
                                try {
                                    value = Double.parseDouble(component.getValue().toString());
                                } catch (Exception ex) {
                                    component.setComponentError(new UserError(
                                            ResourceBundleUtils.getLanguageResource("Common.ValidateDecimalFailed")));
                                    check = false;
                                }
                                // validate max
                                if (value != null) {
                                    // validate max
                                    if (format.contains("<=")) {
                                        Double maxValue = Double.parseDouble(format.split("<=")[1]);
                                        format = format.split("<=")[0];
                                        if (value > maxValue) {
                                            component.setComponentError(new UserError(
                                                    ResourceBundleUtils.getLanguageResource("Common.ValidateFormat")
                                                    + " <= " + maxValue));
                                            check = false;
                                        }
                                    } else if (format.contains("<")) {
                                        Double maxValue = Double.parseDouble(format.split("<")[1]);
                                        format = format.split("<")[0];
                                        if (value >= maxValue) {
                                            component.setComponentError(new UserError(
                                                    ResourceBundleUtils.getLanguageResource("Common.ValidateFormat")
                                                    + " < " + maxValue));
                                            check = false;
                                        }
                                    }
                                    // validate min
                                    if (format.contains(">=")) {
                                        Double minValue = Double.parseDouble(format.split(">=")[1]);
                                        format = format.split(">=")[0];
                                        if (value < minValue) {
                                            component.setComponentError(new UserError(
                                                    ResourceBundleUtils.getLanguageResource("Common.ValidateFormat")
                                                    + " >= " + minValue));
                                            check = false;
                                        }
                                    } else if (format.contains(">")) {
                                        Double minValue = Double.parseDouble(format.split(">")[1]);
                                        format = format.split(">")[0];
                                        if (value <= minValue) {
                                            component.setComponentError(new UserError(
                                                    ResourceBundleUtils.getLanguageResource("Common.ValidateFormat")
                                                    + " > " + minValue));
                                            check = false;
                                        }
                                    }
                                    // validate số lượng chữ số sau dấy phẩy thập phân
                                    if (format.contains(":")) {
                                        Integer doubleScale = Integer.parseInt(format.split(":")[1]);
                                        if (component.getValue().toString().split(".")[1].length() > doubleScale) {
                                            component.setComponentError(new UserError(
                                                    ResourceBundleUtils.getLanguageResource("Common.ValidateFormat")
                                                    + " < " + doubleScale + " " + 
                                                    ResourceBundleUtils.getLanguageResource("Common.ValidateDoubleScale")));
                                            check = false;
                                        }
                                    }
                                }
                            }
                            if (format.contains("email")) {
                                EmailValidator ev = new EmailValidator();
                                if(!ev.validate(component.getValue().toString())) {
                                    component.setComponentError(new UserError(
                                            ResourceBundleUtils.getLanguageResource("Common.ValidateEmailFailed")));
                                    check = false;
                                }
                            }
                        }
                        //Validate độ dài dữ liệu
                        if(!(component instanceof CheckBox)) {
                            if(!lstComp.get(i).get(INT_DATA_TYPE).equals("date")) {
                                if (lstComp.get(i).get(INT_DATA_LENGTH) != null &&
                                        component.getValue().toString().trim().length()
                                        > (int) lstComp.get(i).get(INT_DATA_LENGTH)) {
                                    component.setComponentError(new UserError(
                                            "[" + ((Label) lstComp.get(i).get(INT_LABEL)).getValue() + "] "
                                            + ResourceBundleUtils.getLanguageResource("Common.Error.Length1")
                                            + " " + (int) lstComp.get(i).get(INT_DATA_LENGTH)
                                            + " " + ResourceBundleUtils.getLanguageResource("Common.Error.Length2")));
                                    check = false;
                                }
                            }
                        }
                    } else if((boolean)lstComp.get(i).get(INT_MANDATORY)) {
                        component.setComponentError(new UserError(
                                "[" + ((Label)lstComp.get(i).get(INT_LABEL)).getValue() + "] " +
                                        ResourceBundleUtils.getLanguageResource("Common.Error.IsRequired")));
                        check = false;
                    } else {
                        if (lstComp.get(i).get(INT_DATA_TYPE).equals("date")) {
                            if (component.getErrorMessage() != null) {
                                component.setComponentError(new UserError(
                                    ResourceBundleUtils.getLanguageResource("Common.ValidateDateFailed")));
                                check = false;
                            }
                        }                        
                    }
                }
            }
        }
        return check;
    } 

    /**
     * Hàm khời tạo vùng giao diện nút bấm khi cập nhật
     *
     * @since 15/10/2014 HienDM
     * @return Vùng giao diện các nút bấm khi cập nhật
     */
    private HorizontalLayout buildUpdatePanelButton() throws Exception {
        if(panelButton != null) panelButton.removeAllComponents();
        panelButton.setSpacing(true);

        buttonUpdate = new Button(ResourceBundleUtils.getLanguageResource("Button.Update"));
        buttonUpdate.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    buttonUpdateClick();
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        panelButton.addComponent(buttonUpdate);

        buttonCancel = new Button(ResourceBundleUtils.getLanguageResource("Button.Cancel"));
        buttonCancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    buttonCancelClick();
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        panelButton.addComponent(buttonCancel);
        
        buttonUpdate.setStyleName(Reindeer.BUTTON_DEFAULT);
        buttonUpdate.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        return panelButton;
    }

    /**
     * Hàm xóa và cập nhật dữ liệu nhập trên giao diện
     * @since 15/10/2014 HienDM
     */
    public void updateForm() throws Exception {
        boolean checkFocus = true;
        for (int i = 0; i < lstComponent.size(); i++) {
            if(lstComponent.get(i).get(INT_COMPONENT) instanceof CheckBox) {
                ((CheckBox) lstComponent.get(i).get(INT_COMPONENT)).setValue(false);
            } else if(lstComponent.get(i).get(INT_COMPONENT) instanceof ComboBox && 
                    lstComponent.get(i).size() > INT_COMBOBOX_REFRESH &&
                    (boolean)lstComponent.get(i).get(INT_COMBOBOX_REFRESH)) {
                ComboBox component = (ComboBox)lstComponent.get(i).get(INT_COMPONENT);
                component.removeAllItems();
                Object defaultValue = lstComponent.get(i).get(INT_COMBOBOX_DEFAULTVALUE);
                if(defaultValue != null) {
                    String defaultCaption = (String)lstComponent.get(i).get(INT_COMBOBOX_DEFAULTCAPTION);
                    component.addItem(defaultValue);
                    component.setItemCaption(defaultValue, defaultCaption);
                    component.setValue(defaultValue);
                }
                component.setPageLength(30);
                List<Map> lstData = null;
                String query = (String)lstComponent.get(i).get(INT_COMBOBOX_QUERY);
                List lstParameter = (List)lstComponent.get(i).get(INT_COMBOBOX_PARAMETER);
                boolean isMultiLanguage = (boolean)lstComponent.get(i).get(INT_COMBOBOX_MULTILANGUAGE);
                String idColumn = (String)lstComponent.get(i).get(INT_COMBOBOX_IDCOLUMN);
                String nameColumn = (String)lstComponent.get(i).get(INT_COMBOBOX_NAMECOLUMN);
                if (lstParameter != null) {
                    lstData = C3p0Connector.queryData(query, lstParameter);
                } else {
                    lstData = C3p0Connector.queryData(query);
                }
                
                for(int j = 0; j < lstData.size(); j++) {
                    Map rows = lstData.get(j);
                    Object celldata = new Object();
                    celldata = rows.get(idColumn);
                    if (celldata != null) {
                        component.addItem(celldata.toString());
                    } else {
                        component.addItem("");
                    }

                    String cellCaption = "";
                    if (isMultiLanguage) {
                        cellCaption = ResourceBundleUtils.getLanguageResource(rows.get(nameColumn).toString());
                    } else {
                        cellCaption = rows.get(nameColumn).toString();
                    }
                    if (cellCaption != null) {
                        component.setItemCaption(celldata.toString(), cellCaption);
                    }
                }                
            } else if (lstComponent.get(i).get(INT_COMPONENT) instanceof PopupDateField) {
                ((PopupDateField) lstComponent.get(i).get(INT_COMPONENT)).setValue(null);
            } else if (lstComponent.get(i).get(INT_COMPONENT) instanceof UploadField) {
                ((UploadField) lstComponent.get(i).get(INT_COMPONENT)).buildDefaulLayout();
            } else if (lstComponent.get(i).get(INT_COMPONENT) instanceof MultiUploadField) {
                
            } else {
                ((AbstractField) lstComponent.get(i).get(INT_COMPONENT)).setValue("");
            }
            if(lstComponent.get(i).get(INT_COMPONENT) instanceof MultiUploadField) {
                ((MultiUploadField) lstComponent.get(i).get(INT_COMPONENT)).setComponentError(null);
            } else if(lstComponent.get(i).get(INT_COMPONENT) instanceof UploadField) {
                ((UploadField) lstComponent.get(i).get(INT_COMPONENT)).setComponentError(null);
            } else {
                if(checkFocus) {
                    ((AbstractField) lstComponent.get(i).get(INT_COMPONENT)).focus();
                    checkFocus = false;
                }
                ((AbstractField) lstComponent.get(i).get(INT_COMPONENT)).setComponentError(null);
            }
        }
        for (int i = 0; i < lstComponentMulti.size(); i++) {
            if((boolean)lstComponentMulti.get(i).get(INT_MULTI_MANDATORY)) {
                ((Table)lstComponentMulti.get(i).get(INT_MULTI_TABLE)).setComponentError(null);
            }            
        }        
    }
    
    /**
     * Hàm xóa dữ liệu nhập theo list
     * 
     * @param lstComp List component truyền vào
     * @param checkFocus Có thiết lập focus hay không
     * @since 15/10/2014 HienDM
     */
    public void clearListComponent(List<List> lstComp, boolean checkFocus) throws Exception{
        for (int i = 0; i < lstComp.size(); i++) {
            if (lstComp.get(i).get(INT_COMPONENT) instanceof MultiUploadField) {
                ((MultiUploadField) lstComp.get(i).get(INT_COMPONENT)).setComponentError(null);
                ((MultiUploadField) lstComp.get(i).get(INT_COMPONENT)).removeAllDownloadLink();
            } else if (lstComp.get(i).get(INT_COMPONENT) instanceof UploadField) {
                if (((UploadField) lstComp.get(i).get(INT_COMPONENT)).downloadLink != null) {
                    ((UploadField) lstComp.get(i).get(INT_COMPONENT)).removeDownloadLink();
                }
                ((UploadField) lstComp.get(i).get(INT_COMPONENT)).setComponentError(null);
            } else {
                if (lstComp.get(i).get(INT_SEARCH_MANDATORY) == null
                        || lstComp.get(i).get(INT_SEARCH_MANDATORY).toString().trim().isEmpty()) {
                    if (lstComp.get(i).get(INT_COMPONENT) instanceof CheckBox) {
                        ((CheckBox) lstComp.get(i).get(INT_COMPONENT)).setValue(false);
                    } else if (lstComp.get(i).get(INT_COMPONENT) instanceof PopupDateField) {
                        ((PopupDateField) lstComp.get(i).get(INT_COMPONENT)).setValue(null);
                    } else if (lstComp.get(i).get(INT_COMPONENT) instanceof ComboBox) {
                        ((ComboBox) lstComp.get(i).get(INT_COMPONENT)).setValue(null);
                        if (!((ComboBox) lstComp.get(i).get(INT_COMPONENT)).isNullSelectionAllowed()) {
                            ((ComboBox) lstComp.get(i).get(INT_COMPONENT)).setValue(
                                    lstComp.get(i).get(INT_COMBOBOX_DEFAULTVALUE));
                        }
                    } else {
                        ((AbstractField) lstComp.get(i).get(INT_COMPONENT)).setValue("");
                    }
                }
                if (checkFocus && ((AbstractField) lstComp.get(i).get(INT_COMPONENT)).isVisible()
                        && ((AbstractField) lstComp.get(i).get(INT_COMPONENT)).isEnabled()) {
                    checkFocus = false;
                    ((AbstractField) lstComp.get(i).get(INT_COMPONENT)).focus();
                }
                ((AbstractField) lstComp.get(i).get(INT_COMPONENT)).setComponentError(null);
            }
        }
    }
    
    /**
     * Hàm xóa dữ liệu nhập trên giao diện
     * @since 15/10/2014 HienDM
     */
    public void clearForm() throws Exception {
        clearListComponent(lstComponent, true);
        for (int i = 0; i < lstComponentMulti.size(); i++) {
            ((Table)lstComponentMulti.get(i).get(INT_MULTI_TABLE)).removeAllItems();
            if(lstComponentMulti.get(i).get(INT_MULTI_POPUP) != null) {
                if(((PopupMultiAction)lstComponentMulti.get(i).get(INT_MULTI_POPUP)).storeTable != null)
               ((PopupMultiAction)lstComponentMulti.get(i).get(INT_MULTI_POPUP)).storeTable.removeAllItems();
            }
            ((Table)lstComponentMulti.get(i).get(INT_MULTI_TABLE)).setComponentError(null);
        }
        clearListComponent(lstCustomizeComponent, false);
    }
    
    /**
     * Hàm ẩn những component không dùng để search
     * @param isHide Ẩn component hay không
     * @param formNotSearch Vùng giao diện cần ẩn đi
     * @since 15/10/2014 HienDM
     */
    private void hideComponentNotUseToSearch(boolean isHide, VerticalLayout formNotSearch) throws Exception {
        formNotSearch.setVisible(!isHide);
    }
    
    /**
     * Hàm kiểm tra dữ liệu tìm kiếm
     *
     * @since 02/01/2015 HienDM
     */
    private boolean validateSearch() throws Exception {
        boolean check = true;
        boolean check_or = validateSearchAtLeastOneSuccess();
        for(int i = 0; i < lstComponent.size(); i++) {
            if(lstComponent.get(i).get(INT_COMPONENT) instanceof AbstractField)
                ((AbstractField) lstComponent.get(i).get(INT_COMPONENT)).setComponentError(null);
            else if (lstComponent.get(i).get(INT_COMPONENT) instanceof UploadField)
                ((UploadField) lstComponent.get(i).get(INT_COMPONENT)).setComponentError(null);
            else if (lstComponent.get(i).get(INT_COMPONENT) instanceof MultiUploadField)
                ((MultiUploadField) lstComponent.get(i).get(INT_COMPONENT)).setComponentError(null);
            if(!(lstComponent.get(i).get(INT_COMPONENT) instanceof UploadField)) {
                if(lstComponent.get(i).get(INT_SEARCH_MANDATORY) != null) {
                    String format = lstComponent.get(i).get(INT_SEARCH_MANDATORY).toString();
                    if(check_or) {
                        if(format != null && format.contains(":") && format.split(":")[1].toLowerCase().equals("and_mandatory")) {
                            check = validateSearchDefaultValue(i);
                        }
                    } else {
                        check = validateSearchDefaultValue(i);                   
                    }
                }
            }
        }
        return check;
    }

    /**
     * Hàm kiểm tra nếu có ít nhất một component thoả mãn điều kiện
     *
     * @since 01/03/2014 HienDM
     * @return kiểm tra nếu có ít nhất một component thoả mãn điều kiện
     */
    private boolean validateSearchAtLeastOneSuccess() {
        boolean check_or = false;
        for (int i = 0; i < lstComponent.size(); i++) {
            if (lstComponent.get(i).get(INT_SEARCH_MANDATORY) != null) {
                String format = lstComponent.get(i).get(INT_SEARCH_MANDATORY).toString();
                if (format.split(":")[0].toLowerCase().equals("date")) {
                    PopupDateField fromDate = (PopupDateField) lstComponent.get(i).get(INT_COMPONENT);
                    PopupDateField toDate = (PopupDateField) lstComponent.get(i).get(INT_TO_DATE_COMPONENT);
                    if ((fromDate.getValue() != null && !fromDate.getValue().toString().trim().equals(""))
                            && (toDate.getValue() != null && !toDate.getValue().toString().trim().equals(""))) {
                        if (toDate.getValue().getTime() - fromDate.getValue().getTime()
                                < Long.parseLong(format.split(":")[2]) * 24l * 3600l * 1000l) {
                            check_or = true;
                            break;
                        }
                    }
                } else {
                    if(lstComponent.get(i).get(INT_COMPONENT) instanceof AbstractField) {
                        AbstractField component = (AbstractField) lstComponent.get(i).get(INT_COMPONENT);
                        if (component.getValue() != null && !component.getValue().toString().trim().equals("")) {
                            check_or = true;
                            break;
                        }
                    }
                }
            }
        }
        return check_or;
    }
    
    /**
     * Hàm kiểm tra điều kiện tìm kiếm của các component
     *
     * @since 01/03/2014 HienDM
     * @return kiểm tra điều kiện tìm kiếm của các component
     */
    private boolean validateSearchDefaultValue(int i){
        boolean check = true;
        String format = lstComponent.get(i).get(INT_SEARCH_MANDATORY).toString();
        if (format.split(":")[0].toLowerCase().equals("date")) {
            PopupDateField fromDate = (PopupDateField) lstComponent.get(i).get(INT_COMPONENT);
            PopupDateField toDate = (PopupDateField) lstComponent.get(i).get(INT_TO_DATE_COMPONENT);
            if (!(fromDate.getValue() != null && !fromDate.getValue().toString().trim().equals(""))) {
                fromDate.setComponentError(new UserError(
                        "[" + ((Label) lstComponent.get(i).get(INT_LABEL)).getValue() + "] " + 
                        ResourceBundleUtils.getLanguageResource("Common.From") + " " +
                        ResourceBundleUtils.getLanguageResource("Common.Error.IsRequired")));
                check = false;                
            } else if (!(toDate.getValue() != null && !toDate.getValue().toString().trim().equals(""))) {
                toDate.setComponentError(new UserError(
                        "[" + ((Label) lstComponent.get(i).get(INT_LABEL)).getValue() + "] " + 
                        ResourceBundleUtils.getLanguageResource("Common.To") + " " +
                        ResourceBundleUtils.getLanguageResource("Common.Error.IsRequired")));
                check = false;                
            } else if (toDate.getValue().getTime() - fromDate.getValue().getTime()
                        >= Long.parseLong(format.split(":")[2]) * 24l * 3600l * 1000l) {
                fromDate.setComponentError(new UserError(
                        ResourceBundleUtils.getLanguageResource("Common.To") + " - " +
                        ResourceBundleUtils.getLanguageResource("Common.From") + " < " +
                        Long.parseLong(format.split(":")[2]) + " " +
                        ResourceBundleUtils.getLanguageResource("Common.Day")));
                check = false;
            }
        } else {
            if(lstComponent.get(i).get(INT_COMPONENT) instanceof AbstractField) {
                AbstractField component = (AbstractField) lstComponent.get(i).get(INT_COMPONENT);
                if (!(component.getValue() != null && !component.getValue().toString().trim().equals(""))) {
                    component.setComponentError(new UserError(
                            "[" + ((Label) lstComponent.get(i).get(INT_LABEL)).getValue() + "] "
                            + ResourceBundleUtils.getLanguageResource("Common.Error.IsRequired")));
                    check = false;
                }
            }
        }
        return check;
    }
    
    /**
     * Hàm tìm kiếm dữ liệu
     *
     * @since 15/10/2014 HienDM
     */    
    private void searchNormalData() throws Exception {
        updateData();
        Collection itemIds = table.getItemIds();
        List lstItemId = new ArrayList();
        for (Object itemId : itemIds) {
            Item data = table.getItem(itemId);
            for (int i = 0; i < lstComponent.size(); i++) {
                if ((boolean) lstComponent.get(i).get(INT_USE_TO_SEARCH)) {
                    if (((AbstractField) lstComponent.get(i).get(INT_COMPONENT)) instanceof CheckBox) {
                        boolean checkBoxValue = ((CheckBox) lstComponent.get(i).get(INT_COMPONENT)).getValue();
                        String dataValue = data.getItemProperty(((Label) lstComponent.get(i).get(INT_LABEL)).getValue())
                                .getValue().toString();
                        String enableValue = ResourceBundleUtils.getLanguageResource(
                                (String) lstComponent.get(i).get(INT_CHECKBOX_ENABLE));
                        if (!(checkBoxValue == (dataValue.equals(enableValue)))) {
                            lstItemId.add(itemId);
                        }
                    } else if (((AbstractField) lstComponent.get(i).get(INT_COMPONENT)) instanceof ComboBox) {
                        ComboBox cbo = (ComboBox) lstComponent.get(i).get(INT_COMPONENT);
                        if (cbo.getValue() != null && !cbo.getValue().toString().trim().equals("")) {
                            ComboboxItem cboItem = (ComboboxItem) data.getItemProperty(
                                    ((Label) lstComponent.get(i).get(INT_LABEL)).getValue())
                                    .getValue();
                            if (!cbo.getValue().equals(cboItem.getValue())) {
                                lstItemId.add(itemId);
                            }
                        }
                    } else if (((AbstractField) lstComponent.get(i).get(INT_COMPONENT)) instanceof PopupDateField) {
                        PopupDateField pdfFrom = (PopupDateField) lstComponent.get(i).get(INT_COMPONENT);
                        PopupDateField pdfTo = (PopupDateField) lstComponent.get(i).get(INT_TO_DATE_COMPONENT);
                        Object dataValue = data.getItemProperty(
                                ((Label) lstComponent.get(i).get(INT_LABEL)).getValue()).getValue();
                        if (dataValue != null && !dataValue.toString().trim().isEmpty()) {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            if (((PopupDateField) lstComponent.get(i).get(INT_COMPONENT)).getResolution().equals(Resolution.DAY)) {
                                formatter = new SimpleDateFormat("dd/MM/yyyy");
                            } else {
                                formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            }
                            SimpleDateFormat formatterTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            Date dateValue = formatter.parse(dataValue.toString());
                            if(pdfFrom.getValue() != null) {
                                Date fromValue = new Date();
                                if (pdfFrom.getResolution().equals(Resolution.DAY)) {
                                    fromValue = formatterTime.parse(formatter.format(pdfFrom.getValue()) + " 00:00:00");
                                } else {
                                    fromValue = new Date(pdfFrom.getValue().getTime());
                                }
                                fromValue.setTime(fromValue.getTime() - 1000);
                                if(dateValue.getTime() <= fromValue.getTime()) {
                                    lstItemId.add(itemId);
                                }
                            }
                            if(pdfTo.getValue() != null) {
                                Date toValue = new Date();
                                if (pdfFrom.getResolution().equals(Resolution.DAY)) {
                                    toValue = formatterTime.parse(formatter.format(pdfTo.getValue()) + " 23:59:59");
                                } else {
                                    toValue = new Date(pdfTo.getValue().getTime());
                                }
                                toValue.setTime(toValue.getTime() + 1000);
                                if(dateValue.getTime() >= toValue.getTime()) {
                                    lstItemId.add(itemId);
                                }
                            }
                        }
                    } else {
                        Object dataValue = data.getItemProperty(
                                ((Label) lstComponent.get(i).get(INT_LABEL)).getValue()).getValue();
                        if (dataValue != null) {
                            Object componentValue = ((AbstractField) lstComponent.get(i).get(INT_COMPONENT)).getValue();
                            if (componentValue != null && !componentValue.toString().trim().equals("")) {
                                if(lstComponent.get(i).get(INT_DATA_TYPE).equals("string")) {
                                    if (!dataValue.toString().toLowerCase().trim().contains(componentValue.toString().toLowerCase().trim())) {
                                        lstItemId.add(itemId);
                                    }
                                } else {
                                    if (!dataValue.toString().toLowerCase().trim().equals(componentValue.toString().toLowerCase().trim())) {
                                        lstItemId.add(itemId);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < lstItemId.size(); i++) {
            table.removeItem(lstItemId.get(i));
        }
        if(hasTreeSearch) searchByTree();
        // Cập nhật lại số thứ tự
        if(includeOrder) {
            Object[] arrayItemId = ((java.util.Collection) table.getItemIds()).toArray();
            int countON = 0;
            for (int i = 0; i < arrayItemId.length; i++) {
                countON++;
                Item item = table.getItem(arrayItemId[i]);
                item.getItemProperty(ResourceBundleUtils.getLanguageResource("Common.Order")).setValue(countON);
            }
        }
        if(tableType == INT_PAGED_TABLE) {
            ((PagedTable)table).updateControl();
            ((PagedTable)table).setCurrentPage(1);
        }
    }
    
    /**
     * Hàm tìm kiếm dữ liệu theo tree
     *
     * @since 15/10/2014 HienDM
     */
    private void searchByTree() throws Exception {
        if(!treeSelectedId.equals(rootId)) {
            String tableColumnConnectTree = "";
            for(int i = 0; i < lstComponent.size(); i++) {
                if(lstComponent.get(i).get(INT_DB_FIELD_NAME).toString().equals(
                        columnConnectTree)) {
                    tableColumnConnectTree = ((Label)lstComponent.get(i).get(INT_LABEL)).getValue();
                    break;
                }
            }
            Collection itemIds = table.getItemIds();
            List lstItemId = new ArrayList();
            for (Object itemId : itemIds) {
                String groupId = "";
                if(columnConnectTree.equals(idColumnName)) {
                    groupId = itemId.toString();
                } else {
                    Item data = table.getItem(itemId);
                    ComboboxItem cboItem = (ComboboxItem) data.getItemProperty(
                                tableColumnConnectTree).getValue();
                    if(cboItem != null)
                        groupId = cboItem.getValue().toString();
                    else
                        groupId = "";
                }
                if (isRecursiveTreeSearch()) {
                    if (!checkAncestor(groupId, treeSelectedId)) {
                        lstItemId.add(itemId);
                    }
                } else {
                    if(columnConnectTree.equals(idColumnName)) {
                        if (!treeSearch.getParent(groupId).equals(treeSelectedId)) {
                            lstItemId.add(itemId);
                        }
                    } else {
                        if (!groupId.equals(treeSelectedId)) {
                            lstItemId.add(itemId);
                        }
                    }
                }
            }
            for (int i = 0; i < lstItemId.size(); i++) {
                table.removeItem(lstItemId.get(i));
            }
        }
        if(tableType == INT_PAGED_TABLE) {
            ((PagedTable)table).updateControl();
            ((PagedTable)table).setCurrentPage(1);
        }
    }
    
    /**
     * Hàm kiểm tra tree xem itemId có tổ tiển là compareId hay không
     *
     * @since 15/10/2014 HienDM
     * @param itemId id con
     * @param compareId id tổ tiên
     * @return là tổ tiên hay không
     */     
    private boolean checkAncestor(Object itemId, Object compareId) {
        if(itemId.toString().equals(compareId)) return true;
        if(!itemId.toString().equals(rootId)) {
            if(treeSearch.getParent(itemId) != null) {
                if(treeSearch.getParent(itemId).toString().equals(compareId.toString()))
                    return true;
                else return checkAncestor(treeSearch.getParent(itemId), compareId);
            }
        }
        return false;
    }   
    
    /**
     * Hàm khởi tạo giao diện dữ liệu
     *
     * @since 15/10/2014 HienDM
     * @return Giao diện dữ liệu
     */
    public VerticalLayout buildDataPanel() throws Exception {
        return buildNormalDataPanel();
    }
    
    /**
     * Hàm khởi tạo giao diện vùng dữ liệu
     *
     * @since 15/10/2014 HienDM
     * @return Giao diện dữ liệu
     */
    public VerticalLayout buildNormalDataPanel() throws Exception {
        VerticalLayout dataPanel = new VerticalLayout();
        updateData();        
        dataPanel.addComponent(table);
        if(tableType == INT_PAGED_TABLE) dataPanel.addComponent(((PagedTable)table).createControls());
        return dataPanel;
    }

    /**
     * Hàm cập nhật lại dữ liệu table
     *
     * @since 15/10/2014 HienDM
     */    
    private void updateData() throws Exception {
        // <editor-fold defaultstate="collapsed" desc="Khởi tạo giao diện bảng và truy vấn dữ liệu">
        table.removeAllItems();
        table.setWidth("100%");
        table.setSelectable(true);
        table.setMultiSelect(true);
        table.setColumnCollapsingAllowed(true);
        if (tableType == INT_PAGED_TABLE) {
            ((PagedTable) table).DEFAULT_PAGE_LENGTH = pageLength;
        }
        table.setPageLength(pageLength);

        int order = 0;
        if (includeOrder) {
            order = 1;
        }
        int count = 0;
        for (int i = 0; i < lstComponent.size(); i++) {
            if (includeOrder) {
                table.addContainerProperty(ResourceBundleUtils.getLanguageResource(
                        "Common.Order"), Integer.class, null);
            }
            if (i != idField) {
                if (lstComponent.get(i).get(INT_COMPONENT) instanceof ComboBox) {
                    table.addContainerProperty(ResourceBundleUtils.getLanguageResource(
                            ((Label) lstComponent.get(i).get(INT_LABEL)).getValue()), ComboboxItem.class, null);
                } else if (lstComponent.get(i).get(INT_COMPONENT) instanceof UploadField) {
                    table.addContainerProperty(ResourceBundleUtils.getLanguageResource(
                            ((Label) lstComponent.get(i).get(INT_LABEL)).getValue()), DownloadLink.class, null);
                } else if (lstComponent.get(i).get(INT_COMPONENT) instanceof MultiUploadField) {
                    table.addContainerProperty(ResourceBundleUtils.getLanguageResource(
                            ((Label) lstComponent.get(i).get(INT_LABEL)).getValue()), DownloadLink.class, null);
                } else {
                    table.addContainerProperty(ResourceBundleUtils.getLanguageResource(
                            ((Label) lstComponent.get(i).get(INT_LABEL)).getValue()), String.class, null);
                }
                if ((boolean) lstComponent.get(i).get(INT_IS_COLLAPSED)) {
                    table.setColumnCollapsed(table.getContainerPropertyIds().toArray()[count + order], true);
                } else {
                    table.setColumnCollapsed(table.getContainerPropertyIds().toArray()[count + order], false);
                }
                count++;
            }
        }

        BaseDAO baseDao = new BaseDAO();
        List<Map> lstData = new ArrayList();
        if(ResourceBundleUtils.getConfigureResource("alwaysRefreshData").equals("true"))
            isChangeDefaultSearch = true;
        if (isChangeDefaultSearch) {
            isChangeDefaultSearch = false;
            if (tableQuery.isEmpty()) {
                lstData = baseDao.selectAllData(lstComponent, viewName, queryWhereCondition, queryWhereParameter);
                lstTableData.clear();
                for (int i = 0; i < lstData.size(); i++) {
                    Map row = new HashMap(lstData.get(i));
                    lstTableData.add(row);
                }
            } else {
                if (tableQueryParameter.isEmpty()) {
                    lstData = C3p0Connector.queryData(tableQuery);
                    lstTableData.clear();
                    for (int i = 0; i < lstData.size(); i++) {
                        Map row = new HashMap(lstData.get(i));
                        lstTableData.add(row);
                    }
                } else {
                    lstData = C3p0Connector.queryData(tableQuery, tableQueryParameter);
                    lstTableData.clear();
                    for (int i = 0; i < lstData.size(); i++) {
                        Map row = new HashMap(lstData.get(i));
                        lstTableData.add(row);
                    }
                }
            }
        } else {
            for (int i = 0; i < lstTableData.size(); i++) {
                Map row = new HashMap(lstTableData.get(i));
                lstData.add(row);
            }
        }
        // </editor-fold>

        buildTable(lstData);
        if(tableType == INT_PAGED_TABLE) {
            ((PagedTable)table).updateControl();
            ((PagedTable)table).setCurrentPage(1);
        }
    }
    
    /**
     * Hàm cập nhật lại dữ liệu table có truy xuất dữ liệu database
     *
     * @since 15/10/2014 HienDM
     */ 
    public void updateDataRefreshData() throws Exception{
        refreshAlready = true;
        // Đẩy dữ liệu vào form để tìm kiếm theo điều kiện cũ
        for (int i = 0; i < lstComponent.size(); i++) {
            for (int j = 0; j < lstMandatorySearchValue.size(); j++) {
                if (lstComponent.get(i).get(INT_SEARCH_MANDATORY) != null) {
                    if (lstComponent.get(i).get(INT_DB_FIELD_NAME).equals(
                            lstMandatorySearchValue.get(j).get(0))) {
                        if (!(lstComponent.get(i).get(INT_COMPONENT) instanceof UploadField || 
                                lstComponent.get(i).get(INT_COMPONENT) instanceof MultiUploadField)) {
                            ((AbstractField)lstComponent.get(i).get(INT_COMPONENT)).setValue(
                                    ((List)lstMandatorySearchValue.get(j).get(1)).get(0));
                        }
                    }
                }
            }
        }
        isChangeDefaultSearch = true;
        updateData();
        //Xóa dữ liệu sau khi cập nhật table
        clearForm();
    }
    /**
     * Hàm đẩy dữ liệu vào table
     *
     * @since 15/10/2014 HienDM
     * @return Danh sách dữ liệu liên kết cha con
     */
    private List<List> insertRowToTable(List lstRows, boolean sort) {
        List<List> lstWithoutID = new ArrayList();
        for (int i = 0; i < lstComponent.size(); i++) {
            if (i != idField) {
                lstWithoutID.add(lstComponent.get(i));
            }
        }
        List<List> lstParentChild = new ArrayList();
        int count = 0;
        int firstColumn = 0;
        if (includeOrder) {
            firstColumn = 1;
        }
        
        for (int j = 0; j < lstRows.size(); j++) {
            Map lstData = new HashMap();
            if (sort) {            
                lstData = (Map)(((List<List>)lstRows).get(j).get(1));
            } else {
                lstData = ((List<Map>)lstRows).get(j);
            }
            count++;
            // Cập nhật cha con
            if (tableType == INT_TREE_TABLE) {
                List lstRow = new ArrayList();
                lstRow.add(lstData.get(idColumnName));
                lstRow.add(lstData.get(parentColumnName));
                lstParentChild.add(lstRow);
            }
            Object[] data = new Object[lstWithoutID.size() + firstColumn];
            if (includeOrder) {
                data[0] = count;
            }
            for (int i = 0; i < lstWithoutID.size(); i++) {
                if (lstWithoutID.get(i).get(INT_DATA_TYPE).equals("boolean")) {
                    if (lstWithoutID.get(i).get(INT_COMPONENT) instanceof ComboBox) {
                        Object celldata = lstData.get(lstWithoutID.get(i).get(INT_DB_FIELD_NAME));
                        String cellCaption = "";                        
                        if ((lstWithoutID.get(i).size() > BaseAction.INT_POPUP_BUTTON)
                                && (lstWithoutID.get(i).get(BaseAction.INT_POPUP_BUTTON) instanceof Button)) {
                            String aliasTableName = lstWithoutID.get(i).get(BaseAction.INT_COMBOBOX_TABLENAME).toString();
                            String aliasDbFieldName = lstWithoutID.get(i).get(BaseAction.INT_DB_FIELD_NAME).toString();
                            if (aliasTableName.length() > 10) aliasTableName = aliasTableName.substring(0, 10);
                            if (aliasDbFieldName.length() > 10) aliasDbFieldName = aliasDbFieldName.substring(0, 10);
                            Object captionValue = lstData.get(aliasTableName + aliasDbFieldName);
                            if(captionValue != null) cellCaption = captionValue.toString();
                        } else {
                            if(celldata != null) {
                                if(lstWithoutID.get(i).size() > INT_COMBOBOX_DATA) {
                                    List<Map> lstComboData = (List)lstWithoutID.get(i).get(INT_COMBOBOX_DATA);
                                    if(lstComboData != null && !lstComboData.isEmpty()) {
                                        for(int z = 0; z < lstComboData.size(); z++) {
                                            if(lstComboData.get(z).get(((List)lstWithoutID.get(i)).get(INT_COMBOBOX_IDCOLUMN)) != null) {
                                                if(lstComboData.get(z).get(((List)lstWithoutID.get(i)).get(INT_COMBOBOX_IDCOLUMN)).toString().equals(celldata.toString())) {
                                                    if(lstComboData.get(z).get(((List)lstWithoutID.get(i)).get(INT_COMBOBOX_NAMECOLUMN)) != null)
                                                        cellCaption = lstComboData.get(z).get(((List)lstWithoutID.get(i)).get(INT_COMBOBOX_NAMECOLUMN)).toString();
                                                    else cellCaption = "";
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    ComboBox cbo = (ComboBox) lstWithoutID.get(i).get(INT_COMPONENT);
                                    if(celldata != null) cellCaption = cbo.getItemCaption(celldata.toString());
                                    else cellCaption = "";
                                }
                            } else cellCaption = "";
                        }
                        ComboboxItem cboItem = new ComboboxItem(celldata.toString(), cellCaption);
                        if (celldata != null) {
                            data[i + firstColumn] = cboItem;
                        } else {
                            data[i + firstColumn] = new ComboboxItem();
                        }
                    } else if (lstWithoutID.get(i).get(INT_COMPONENT) instanceof CheckBox) {
                        if (lstData.get(lstWithoutID.get(i).get(INT_DB_FIELD_NAME)) != null) {
                            Integer celldata = Integer.parseInt(lstData.get(lstWithoutID.get(i).get(INT_DB_FIELD_NAME)).toString());
                            if (celldata == 1) {
                                data[i + firstColumn] = lstWithoutID.get(i).get(INT_CHECKBOX_ENABLE);
                            } else if (celldata == 0) {
                                data[i + firstColumn] = lstWithoutID.get(i).get(INT_CHECKBOX_DISABLE);
                            } else {
                                data[i + firstColumn] = "";
                            }
                        }
                    } else {
                        Object celldata = lstData.get(lstWithoutID.get(i).get(INT_DB_FIELD_NAME));
                        if (celldata != null) {
                            data[i + firstColumn] = celldata.toString();
                        } else {
                            data[i + firstColumn] = "";
                        }
                    }
                }
                if (lstWithoutID.get(i).get(INT_DATA_TYPE).equals("int")
                        || lstWithoutID.get(i).get(INT_DATA_TYPE).equals("long")
                        || lstWithoutID.get(i).get(INT_DATA_TYPE).equals("float")
                        || lstWithoutID.get(i).get(INT_DATA_TYPE).equals("double")) {
                    if (lstWithoutID.get(i).get(INT_COMPONENT) instanceof ComboBox) {
                        Object celldata = lstData.get(lstWithoutID.get(i).get(INT_DB_FIELD_NAME));
                        String cellCaption = "";
                        if ((lstWithoutID.get(i).size() > BaseAction.INT_POPUP_BUTTON)
                                && (lstWithoutID.get(i).get(BaseAction.INT_POPUP_BUTTON) instanceof Button)) {
                            String aliasTableName = lstWithoutID.get(i).get(BaseAction.INT_COMBOBOX_TABLENAME).toString();
                            String aliasDbFieldName = lstWithoutID.get(i).get(BaseAction.INT_DB_FIELD_NAME).toString();
                            if (aliasTableName.length() > 10) aliasTableName = aliasTableName.substring(0, 10);
                            if (aliasDbFieldName.length() > 10) aliasDbFieldName = aliasDbFieldName.substring(0, 10);
                            Object captionValue = lstData.get(aliasTableName + aliasDbFieldName);
                            if(captionValue != null) cellCaption = captionValue.toString();
                        } else {
                            if(celldata != null) {
                                if(lstWithoutID.get(i).size() > INT_COMBOBOX_DATA) {
                                    List<Map> lstComboData = (List)lstWithoutID.get(i).get(INT_COMBOBOX_DATA);
                                    if(lstComboData != null && !lstComboData.isEmpty()) {
                                        for(int z = 0; z < lstComboData.size(); z++) {
                                            if(lstComboData.get(z).get(((List)lstWithoutID.get(i)).get(INT_COMBOBOX_IDCOLUMN)) != null) {
                                                if(lstComboData.get(z).get(((List)lstWithoutID.get(i)).get(INT_COMBOBOX_IDCOLUMN)).toString().equals(celldata.toString())) {
                                                    if(lstComboData.get(z).get(((List)lstWithoutID.get(i)).get(INT_COMBOBOX_NAMECOLUMN)) != null)
                                                        cellCaption = lstComboData.get(z).get(((List)lstWithoutID.get(i)).get(INT_COMBOBOX_NAMECOLUMN)).toString();
                                                    else cellCaption = "";
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    ComboBox cbo = (ComboBox) lstWithoutID.get(i).get(INT_COMPONENT);
                                    if(celldata != null) cellCaption = cbo.getItemCaption(celldata.toString());
                                    else cellCaption = "";                                        
                                }
                            } else cellCaption = "";
                        }
                        if (celldata != null) {
                            ComboboxItem cboItem = new ComboboxItem(celldata.toString(), cellCaption);
                            data[i + firstColumn] = cboItem;
                        } else {
                            data[i + firstColumn] = new ComboboxItem();
                        }
                    } else {
                        Object celldata = lstData.get(lstWithoutID.get(i).get(INT_DB_FIELD_NAME));
                        if (celldata != null) {
                            data[i + firstColumn] = celldata.toString();
                        } else {
                            data[i + firstColumn] = "";
                        }
                    }
                }
                if (lstWithoutID.get(i).get(INT_DATA_TYPE).equals("string")) {
                    if (lstWithoutID.get(i).get(INT_COMPONENT) instanceof ComboBox) {
                        Object celldata = lstData.get(lstWithoutID.get(i).get(INT_DB_FIELD_NAME));
                        String cellCaption = "";
                        if ((lstWithoutID.get(i).size() > BaseAction.INT_POPUP_BUTTON)
                                && (lstWithoutID.get(i).get(BaseAction.INT_POPUP_BUTTON) instanceof Button)) {
                            String aliasTableName = lstWithoutID.get(i).get(BaseAction.INT_COMBOBOX_TABLENAME).toString();
                            String aliasDbFieldName = lstWithoutID.get(i).get(BaseAction.INT_DB_FIELD_NAME).toString();
                            if (aliasTableName.length() > 10) aliasTableName = aliasTableName.substring(0, 10);
                            if (aliasDbFieldName.length() > 10) aliasDbFieldName = aliasDbFieldName.substring(0, 10);
                            Object captionValue = lstData.get(aliasTableName + aliasDbFieldName);
                            if(captionValue != null) cellCaption = captionValue.toString();
                        } else {
                            if(celldata != null) {
                                if(lstWithoutID.get(i).size() > INT_COMBOBOX_DATA) {
                                    List<Map> lstComboData = (List)lstWithoutID.get(i).get(INT_COMBOBOX_DATA);
                                    if(lstComboData != null && !lstComboData.isEmpty()) {
                                        for(int z = 0; z < lstComboData.size(); z++) {
                                            if(lstComboData.get(z).get(((List)lstWithoutID.get(i)).get(INT_COMBOBOX_IDCOLUMN)) != null) {
                                                if(lstComboData.get(z).get(((List)lstWithoutID.get(i)).get(INT_COMBOBOX_IDCOLUMN)).toString().equals(celldata.toString())) {
                                                    if(lstComboData.get(z).get(((List)lstWithoutID.get(i)).get(INT_COMBOBOX_NAMECOLUMN)) != null)
                                                        cellCaption = lstComboData.get(z).get(((List)lstWithoutID.get(i)).get(INT_COMBOBOX_NAMECOLUMN)).toString();
                                                    else cellCaption = "";
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    ComboBox cbo = (ComboBox) lstWithoutID.get(i).get(INT_COMPONENT);
                                    if(celldata != null) cellCaption = cbo.getItemCaption(celldata.toString());
                                    else cellCaption = "";                                        
                                }
                            } else cellCaption = "";
                        }
                        ComboboxItem cboItem = new ComboboxItem(celldata.toString(), cellCaption);
                        if (celldata != null) {
                            data[i + firstColumn] = cboItem;
                        } else {
                            data[i + firstColumn] = new ComboboxItem();
                        }
                    } else if (lstWithoutID.get(i).get(INT_COMPONENT) instanceof PasswordField) {
                        // Không làm gì
                    } else {
                        String celldata = (String) lstData.get(lstWithoutID.get(i).get(INT_DB_FIELD_NAME));
                        if (celldata != null) {
                            data[i + firstColumn] = celldata;
                        } else {
                            data[i + firstColumn] = "";
                        }
                    }
                }
                if (lstWithoutID.get(i).get(INT_DATA_TYPE).equals("date")) {
                    if (lstWithoutID.get(i).get(INT_COMPONENT) instanceof PopupDateField) {
                        Date celldata = (Date) lstData.get(lstWithoutID.get(i).get(INT_DB_FIELD_NAME));
                        if (celldata != null) {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            if (((PopupDateField) lstWithoutID.get(i).get(INT_COMPONENT)).getResolution().equals(Resolution.DAY)) {
                                formatter = new SimpleDateFormat("dd/MM/yyyy");
                            } else {
                                formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            }
                            data[i + firstColumn] = formatter.format(celldata);
                        } else {
                            data[i + firstColumn] = "";
                        }
                    } else {
                        Date celldata = (Date) lstData.get(lstWithoutID.get(i).get(INT_DB_FIELD_NAME));
                        if (celldata != null) {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            data[i + firstColumn] = formatter.format(celldata);
                        } else {
                            data[i + firstColumn] = "";
                        }
                    }
                }
                if (lstWithoutID.get(i).get(INT_DATA_TYPE).equals("file")) {
                    if (lstData.get(lstWithoutID.get(i).get(INT_DB_FIELD_NAME)) != null && 
                            !lstData.get(lstWithoutID.get(i).get(INT_DB_FIELD_NAME)).toString().trim().isEmpty() ) {
                        DownloadLink downloadLink = new DownloadLink();
                        String fileName = lstData.get(lstWithoutID.get(i).get(INT_DB_FIELD_NAME)).toString();
                        String pattern = Pattern.quote(System.getProperty("file.separator"));
                        String[] splittedFileName = fileName.split(pattern);
                        if(splittedFileName.length > 1)
                            fileName = splittedFileName[splittedFileName.length - 1];
                        else {
                            splittedFileName = fileName.split("/");
                            fileName = splittedFileName[splittedFileName.length - 1];
                        }
                        fileName = (new String(Base64Utils.decode(FileUtils.extractFileNameNotExt(fileName)))).split("_")[1]
                                + FileUtils.extractFileExt(fileName);
                        if(fileName.length() > 30) fileName = fileName.substring(0,25) + "---" + FileUtils.extractFileExt(fileName);
                        downloadLink.setCaption(fileName);
                        downloadLink.setFilePath(lstData.get(lstWithoutID.get(i).get(INT_DB_FIELD_NAME)).toString());
                        if (downloadLink.getFilePath() != null) {
                            File downloadFile = new File(ResourceBundleUtils.getConfigureResource("FileBaseDirectory")
                                        + downloadLink.getFilePath());
                            if (!downloadFile.exists()) {
                                downloadLink.setComponentError(new UserError(ResourceBundleUtils.getLanguageResource("Common.FileNotExist")));
                            }
                        }
                        final AdvancedFileDownloader downloaderForLink = new AdvancedFileDownloader();
                        downloaderForLink.addAdvancedDownloaderListener(new AdvancedDownloaderListener() {
                            @Override
                            public void beforeDownload(DownloaderEvent downloadEvent) {
                                String filePath = ResourceBundleUtils.getConfigureResource("FileBaseDirectory")
                                        + downloadLink.getFilePath();
                                downloaderForLink.setFilePath(filePath);
                            }
                        });
                        downloaderForLink.extend(downloadLink);
                        data[i + firstColumn] = downloadLink;
                    } else {
                        DownloadLink temp = new DownloadLink();
                        temp.setVisible(false);
                        data[i + firstColumn] = temp;
                    }
                }
            }
            table.addItem(data, "" + lstData.get(idColumnName));
        }
        return lstParentChild;
    }

    /**
     * Hàm sắp xếp dữ liệu
     *
     * @since 16/12/2014 HienDM
     * @param lstRows sắp xếp dữ liệu
     */
    private List<List> sortData(List<Map> lstRows) throws Exception {
        List<List> lstSortData = new ArrayList();
        for (int j = 0; j < lstRows.size(); j++) {
            Map lstData = lstRows.get(j);
            Object dataValue = null;
            dataValue = lstData.get(sortColumnName);
            if (lstSortData.isEmpty()) {
                List lstRow = new ArrayList();
                lstRow.add(dataValue);
                lstRow.add(lstData);
                lstSortData.add(lstRow);
            } else {
                boolean checkAscending = false;
                lstSortData.add(new ArrayList());
                for (int i = lstSortData.size() - 2; i >= 0; i--) {
                    if(lstSortData.get(i).get(0) != null && dataValue != null) {
                        if (sortAscending) {
                            if (sortColumnType.equals("int")) {
                                if (Integer.parseInt(dataValue.toString())
                                        >= Integer.parseInt(lstSortData.get(i).get(0).toString())) {
                                    checkAscending = true;
                                }
                            }
                            if (sortColumnType.equals("string")) {
                                if (((String) dataValue).compareToIgnoreCase(
                                        (String) lstSortData.get(i).get(0)) >= 0) {
                                    checkAscending = true;
                                }
                            }
                            if (sortColumnType.equals("date")) {
                                Date sortDate = null;
                                if (lstSortData.get(i).get(0) instanceof java.sql.Timestamp) {
                                    sortDate = new Date(((java.sql.Timestamp) lstSortData.get(i).get(0)).getTime());
                                } else if (lstSortData.get(i).get(0) instanceof java.sql.Date) {
                                    sortDate = new Date(((java.sql.Date) lstSortData.get(i).get(0)).getTime());
                                } else if (lstSortData.get(i).get(0) instanceof java.util.Date) {
                                    sortDate = (Date) lstSortData.get(i).get(0);
                                }
                                if (((Date)dataValue).after(sortDate) || ((Date)dataValue).equals(sortDate)) {
                                    checkAscending = true;
                                }
                            }                        
                        } else {
                            if (sortColumnType.equals("int")) {
                                if (Integer.parseInt(dataValue.toString())
                                        < Integer.parseInt(lstSortData.get(i).get(0).toString())) {
                                    checkAscending = true;
                                }
                            }
                            if (sortColumnType.equals("string")) {
                                if (((String) dataValue).compareToIgnoreCase(
                                        (String) lstSortData.get(i).get(0)) < 0) {
                                    checkAscending = true;
                                }
                            }
                            if (sortColumnType.equals("date")) {
                                Date sortDate = null;
                                if(lstSortData.get(i).get(0) instanceof java.sql.Timestamp) {
                                    sortDate = new Date(((java.sql.Timestamp)lstSortData.get(i).get(0)).getTime());
                                } else if(lstSortData.get(i).get(0) instanceof java.sql.Date) {
                                    sortDate = new Date(((java.sql.Date)lstSortData.get(i).get(0)).getTime());
                                } else if(lstSortData.get(i).get(0) instanceof java.util.Date) {
                                    sortDate = (Date)lstSortData.get(i).get(0);
                                }
                                if (((Date)dataValue).before(sortDate)) {
                                    checkAscending = true;
                                }                                    
                            }                        
                        }
                    }
                    if (checkAscending) {
                        List lstRow = new ArrayList();
                        lstRow.add(dataValue);
                        lstRow.add(lstData);
                        lstSortData.set(i + 1, lstRow);
                        break;
                    } else if (i == 0) {
                        lstSortData.set(i + 1, lstSortData.get(i));
                        List lstRow = new ArrayList();
                        lstRow.add(dataValue);
                        lstRow.add(lstData);
                        lstSortData.set(i, lstRow);
                        break;
                    } else {
                        lstSortData.set(i + 1, lstSortData.get(i));
                    }
                }
            }
        }
        return lstSortData;
    }
    
    /**
     * Hàm đẩy dữ liệu vào đối tượng Table
     *
     * @since 16/12/2014 HienDM
     * @param lstRows Dữ liệu đẩy vào Table
     */
    private void buildTable(List<Map> lstRows) throws Exception {
        List<List> lstParentChild = new ArrayList();
        if(sortColumnName.isEmpty()) {
            // Đẩy dữ liệu vào bảng
            lstParentChild = insertRowToTable(lstRows, false);
        }
        else {
            // Sắp xếp dữ liệu
            List lstSortData = sortData(lstRows);
            // Đẩy dữ liệu sau khi sắp xếp vào bảng
            lstParentChild = insertRowToTable(lstSortData, true);
        }   
        
        // Tạo tree
        if (tableType == INT_TREE_TABLE) {
            for(int i = 0; i < lstParentChild.size(); i++) {
                if(lstParentChild.get(i) != null && lstParentChild.get(i).get(1) != null 
                        && !lstParentChild.get(i).get(1).equals(rootId)) {
                    ((TreeTable) table).setParent(lstParentChild.get(i).get(0).toString(), 
                            lstParentChild.get(i).get(1).toString());
                }
            }
            for(int i = 0; i < lstParentChild.size(); i++) {
                ((TreeTable) table).setCollapsed(lstParentChild.get(i).get(0).toString(), false);
            }
        }
    }
    
    /**
     * Hàm thêm tree tìm kiếm vào giao diện nhập
     *
     * @param label mô tả
     * @param query câu lệnh truy vấn dữ liệu
     * @param lstParameter danh sách tham số
     * @param idColumn trường id trong database
     * @param nameColumn trường name trong database
     * @param idParent trường id của nút cha trong database (Nếu = null thì là cây 1 cấp)
     * @param rootId trường id của nút gốc
     * @param recursive đệ quy
     * @param columnConnectTree trường dữ liệu kết nối với tree
     * @since 18/11/2014 HienDM
     */
    public void buildTreeSearch(String label, String query, List lstParameter,
            String idColumn, String nameColumn, String idParent, String rootId, 
            boolean recursive, String columnConnectTree
    ) throws Exception {
        if(idColumn != null) idColumn = idColumn.toLowerCase().trim();
        if(nameColumn != null) nameColumn = nameColumn.toLowerCase().trim();
        if(columnConnectTree != null) columnConnectTree = columnConnectTree.toLowerCase().trim();
        if(idParent != null) {
            idParent = idParent.toLowerCase().trim();
            setRecursiveTreeSearch(recursive);
        }
        else {
            setRecursiveTreeSearch(false);
        }
        setHasTreeSearch(true);
        this.columnConnectTree = columnConnectTree;
        treeSearch = new Tree();
        List<Map> lstData = null;
        if (lstParameter != null) {
            lstData = C3p0Connector.queryData(query, lstParameter);
        } else {
            lstData = C3p0Connector.queryData(query);
        }
        this.rootId = rootId;
        treeSearch.addItem(rootId);
        treeSearch.setItemCaption(rootId, ResourceBundleUtils.getLanguageResource(label));
        
        for (int i = 0; i < lstData.size(); i++) {
            Map rows = lstData.get(i);
            Object celldata = new Object();

            celldata = rows.get(idColumn);
            if (celldata != null) {
                treeSearch.addItem(celldata.toString());
            } else {
                treeSearch.addItem("");
            }

            String cellCaption = "";
            cellCaption = rows.get(nameColumn.toLowerCase()).toString();
            
            if (cellCaption != null) {
                treeSearch.setItemCaption(celldata.toString(), cellCaption);
            }
        }
        // build tree panel
        if(idParent != null) {
            for(int i=0; i < lstData.size(); i++) {
                treeSearch.setParent(lstData.get(i).get(idColumn).toString(), 
                        lstData.get(i).get(idParent).toString());
            }
        } else {
            for (int i = 0; i < lstData.size(); i++) {
                treeSearch.setParent(lstData.get(i).get(idColumn).toString(),
                        rootId);
            }            
        }
        
        treeSearch.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                try {
                    if (event.getButton() == ItemClickEvent.BUTTON_LEFT){
                        setTreeSelectedId(event.getItemId().toString());
                        updateData();
                        searchByTree();
                    }
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }
            }
        });
        
        treeLayout.addComponent(treeSearch);
        treeSearch.expandItem(rootId);
    }
    
    /**
     * Hàm thêm tree tìm kiếm vào giao diện nhập
     *
     * @param label mô tả
     * @param query câu lệnh truy vấn dữ liệu
     * @param lstParameter danh sách tham số
     * @param idColumn trường id trong database
     * @param nameColumn trường name trong database
     * @param idParent trường id của nút cha trong database (Nếu = null thì là cây 1 cấp)
     * @param rootId trường id của nút gốc
     * @param whereChildQuery query dữ liệu bảng tham chiếu từ tree
     * @since 29/05/2016 HienDM
     */
    public void buildTreeSearch(String label, String query, List lstParameter,
            String idColumn, String nameColumn, String idParent, String rootId, 
            String whereChildQuery
    ) throws Exception {
        if(idColumn != null) idColumn = idColumn.toLowerCase().trim();
        if(nameColumn != null) nameColumn = nameColumn.toLowerCase().trim();
        if(columnConnectTree != null) columnConnectTree = columnConnectTree.toLowerCase().trim();
        if(idParent != null) {
            idParent = idParent.toLowerCase().trim();
            setRecursiveTreeSearch(false);
        }
        else {
            setRecursiveTreeSearch(false);
        }
        setHasTreeSearch(true);
        this.columnConnectTree = columnConnectTree;
        treeSearch = new Tree();
        List<Map> lstData = null;
        if (lstParameter != null) {
            lstData = C3p0Connector.queryData(query, lstParameter);
        } else {
            lstData = C3p0Connector.queryData(query);
        }
        this.rootId = rootId;
        treeSearch.addItem(rootId);
        treeSearch.setItemCaption(rootId, ResourceBundleUtils.getLanguageResource(label));
        
        for (int i = 0; i < lstData.size(); i++) {
            Map rows = lstData.get(i);
            Object celldata = new Object();

            celldata = rows.get(idColumn);
            if (celldata != null) {
                treeSearch.addItem(celldata.toString());
            } else {
                treeSearch.addItem("");
            }

            String cellCaption = "";
            cellCaption = rows.get(nameColumn.toLowerCase()).toString();
            
            if (cellCaption != null) {
                treeSearch.setItemCaption(celldata.toString(), cellCaption);
            }
        }
        // build tree panel
        if(idParent != null) {
            for(int i=0; i < lstData.size(); i++) {
                treeSearch.setParent(lstData.get(i).get(idColumn).toString(), 
                        lstData.get(i).get(idParent).toString());
            }
        } else {
            for (int i = 0; i < lstData.size(); i++) {
                treeSearch.setParent(lstData.get(i).get(idColumn).toString(),
                        rootId);
            }            
        }
        
        treeSearch.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                try {
                    if (event.getButton() == ItemClickEvent.BUTTON_LEFT){
                        setTreeSelectedId(event.getItemId().toString());
                        queryWhereCondition = whereChildQuery;
                        queryWhereParameter = new ArrayList();
                        queryWhereParameter.add(Integer.parseInt(event.getItemId().toString()));
                        updateData();
                        //searchByTree();
                    }
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }
            }
        });
        
        treeLayout.addComponent(treeSearch);
        treeSearch.expandItem(rootId);
    }    
    
    public AbstractField getComponent(String databaseField) {
        databaseField = databaseField.toLowerCase();
        for(int i = 0; i < lstComponent.size(); i++) {
            if(lstComponent.get(i).get(INT_DB_FIELD_NAME).equals(databaseField)) 
                if(lstComponent.get(i).get(INT_COMPONENT) instanceof AbstractField)
                    return (AbstractField) lstComponent.get(i).get(INT_COMPONENT);
        }
        return null;
    }
    
    public Label getComponentLabel(String databaseField) {
        databaseField = databaseField.toLowerCase();
        for(int i = 0; i < lstComponent.size(); i++) {
            if(lstComponent.get(i).get(INT_DB_FIELD_NAME).equals(databaseField)) 
                if(lstComponent.get(i).get(INT_COMPONENT) instanceof AbstractField)
                    return (Label) lstComponent.get(i).get(INT_LABEL);
        }
        return null;
    }
    
    public Table getMultiComponent(String tableName) {
        tableName = tableName.toLowerCase();
        for(int i = 0; i < lstComponentMulti.size(); i++) {
            if(lstComponentMulti.get(i).get(INT_MULTI_TABLENAME).equals(tableName)) 
                return (Table) lstComponentMulti.get(i).get(INT_MULTI_TABLE);
        }
        return null;
    }
    
    public Button getMultiComponentButton(String tableName) {
        tableName = tableName.toLowerCase();
        for(int i = 0; i < lstComponentMulti.size(); i++) {
            if(lstComponentMulti.get(i).get(INT_MULTI_TABLENAME).equals(tableName)) 
                return (Button)((VerticalLayout)((Table)lstComponentMulti.get(i).get(INT_MULTI_TABLE)).
                        getParent()).getComponent(0);
        }
        return null;
    }    
    
    public Label getMultiComponentLabel(String tableName) {
        tableName = tableName.toLowerCase();
        for (int i = 0; i < lstComponentMulti.size(); i++) {
            if (lstComponentMulti.get(i).get(INT_MULTI_TABLENAME).equals(tableName)) {
                return (Label) lstComponentMulti.get(i).get(INT_LABEL);
            }
        }
        return null;
    }
    
    public Object[] getMultiOldId(String tableName) {
        tableName = tableName.toLowerCase();
        for(int i = 0; i < lstComponentMulti.size(); i++) {
            if(lstComponentMulti.get(i).get(INT_MULTI_TABLENAME).equals(tableName)) 
                return (Object[])lstComponentMulti.get(i).get(BaseAction.INT_MULTI_OLDIDS);
        }
        return null;
    }
    
}

