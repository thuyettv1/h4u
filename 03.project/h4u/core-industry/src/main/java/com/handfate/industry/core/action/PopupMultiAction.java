package com.handfate.industry.core.action;

import com.handfate.industry.core.MainUI;
import static com.handfate.industry.core.action.BaseAction.INT_COMPONENT;
import static com.handfate.industry.core.action.BaseAction.INT_IS_COLLAPSED;
import static com.handfate.industry.core.action.BaseAction.INT_LABEL;
import com.handfate.industry.core.action.component.ComboboxItem;
import com.handfate.industry.core.action.component.DownloadLink;
import com.handfate.industry.core.action.component.UploadField;
import com.handfate.industry.core.dao.BuildConfigDAO;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.data.Item;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @since 14/11/2014
 * @author HienDM
 */
public class PopupMultiAction extends BaseConfigAction{

    public Table tableData;
    public Window popupWindow = new Window();
    public Table storeTable = new Table();
    public int attachSize = 0;
    public List<List> lstAttach = new ArrayList();
    public BaseAction parent;

    public BaseAction getParent() {
        return parent;
    }

    public void setParent(BaseAction parent) {
        this.parent = parent;
    }

    /**
     * Hàm khởi tạo giao diện
     *
     * @since 19/11/2014 HienDM
     * @param column số cột trình bày giao diện
     * @param tableResult combobox tại màn hình cha để lưu dữ liệu
     * @param window window của popup
     * @param lstAttach list đính kèm
     * @return Giao diện sau khi khởi tạo
     */    
    public HorizontalLayout initPanel(int column, Table tableResult, Window window, List<List> lstAttach) throws Exception {
        if(layoutPanel != null) layoutPanel.removeAllComponents();
        if(mainPanel != null) mainPanel.removeAllComponents();
        tableData = tableResult;
        popupWindow = window;
        setAllowAdd(false);
        setAllowEdit(false);
        setAllowDelete(false);
        setAllowDetail(false);
        this.attachSize = lstAttach.size();
        this.lstAttach = lstAttach;
        if (hasTreeSearch) {
            buttonArea.setWidth(popupTreeFormWidth);
        } else {
            buttonArea.setWidth(popupFormWidth);
        }
        setIncludeOrder(false);
        return super.initPanel(column);
    }

    /**
     * Hàm khởi tạo giao diện vùng dữ liệu
     *
     * @since 15/10/2014 HienDM
     * @return Giao diện dữ liệu
     */ 
    @Override
    public VerticalLayout buildNormalDataPanel() throws Exception {        
        VerticalLayout dataPanel = super.buildNormalDataPanel();
        
        //Thêm panel nút bấm add dữ liệu
        dataPanel.addComponent(buildAddPanelButton());
        
        // Thêm bảng lưu trữ dữ liệu
        if(!(storeTable != null && storeTable.size() > 0))
            storeTable = buildStoreTable();
        dataPanel.addComponent(storeTable);
        if(tableData.size() > 0 && storeTable.size() == 0) {
            Object[] allItem = ((java.util.Collection) table.getItemIds()).toArray();
            int size = storeTable.size();
            int countON = size;
            for (int i = 0; i < allItem.length; i++) {
                if (tableData.containsId(allItem[i])) {
                    countON++;
                    Object[] data = new Object[lstComponent.size() - 1];
                    Item item = table.getItem(allItem[i]);
                    int count = 0;
                    if (includeOrder) {
                        data[0] = countON;
                    }
                    for (int j = 0; j < lstComponent.size(); j++) {
                        if (j != idField) {
                            data[count] = item.getItemProperty(((Label) lstComponent.get(j).get(INT_LABEL)).getValue()).
                                    getValue();
                            count++;
                        }
                    }
                    storeTable.addItem(data, allItem[i].toString());
                }
            }            
        }        
        
        //Thêm panel nút bấm cập nhật dữ liệu
        dataPanel.addComponent(buildUpdatePanelButton());

        if(hasTreeSearch) {
            dataPanel.setWidth(popupTreeFormWidth);
            formArea.setWidth(popupTreeFormWidth);            
        } else {
            dataPanel.setWidth(popupFormWidth);
            formArea.setWidth(popupFormWidth);
        }
        
        return dataPanel;
    }

    /**
     * Hàm khởi tạo bảng lưu trữ
     *
     * @since 04/01/2015 HienDM
     * @return bảng lưu trữ
     */
    private Table buildStoreTable() {
        Table temp = new Table();
        temp.removeAllItems();
        temp.setWidth("100%");
        temp.setSelectable(true);
        temp.setMultiSelect(true);
        temp.setColumnCollapsingAllowed(true);
        temp.setPageLength(storeTable.getPageLength());

        int order = 0;
        if (includeOrder) {
            order = 1;
        }
        int count = 0;
        for (int i = 0; i < lstComponent.size(); i++) {
            if (includeOrder) {
                temp.addContainerProperty(ResourceBundleUtils.getLanguageResource(
                        "Common.Order"), Integer.class, null);
            }
            if (i != idField) {
                if (lstComponent.get(i).get(INT_COMPONENT) instanceof ComboBox) {
                    temp.addContainerProperty(ResourceBundleUtils.getLanguageResource(
                            ((Label) lstComponent.get(i).get(INT_LABEL)).getValue()), ComboboxItem.class, null);
                } else if (lstComponent.get(i).get(INT_COMPONENT) instanceof UploadField) {
                    temp.addContainerProperty(ResourceBundleUtils.getLanguageResource(
                            ((Label) lstComponent.get(i).get(INT_LABEL)).getValue()), DownloadLink.class, null);
                } else {
                    temp.addContainerProperty(ResourceBundleUtils.getLanguageResource(
                            ((Label) lstComponent.get(i).get(INT_LABEL)).getValue()), String.class, null);
                }
                if ((boolean) lstComponent.get(i).get(INT_IS_COLLAPSED)) {
                    temp.setColumnCollapsed(table.getContainerPropertyIds().toArray()[count + order], true);
                } else {
                    temp.setColumnCollapsed(table.getContainerPropertyIds().toArray()[count + order], false);
                }
                count++;
            }
        }
        return temp;        
    }
    
    /**
     * Hàm khởi tạo giao diện vùng nút bấm thêm bản ghi
     *
     * @since 04/01/2015 HienDM
     * @return giao diện vùng nút bấm thêm bản ghi
     */    
    private HorizontalLayout buildAddPanelButton() throws Exception {
        HorizontalLayout panelAddButtonPopup = new HorizontalLayout();
        panelAddButtonPopup.setHeight("50px");
        if(hasTreeSearch) {
            panelAddButtonPopup.setWidth(popupTreeFormWidth);          
        } else {
            panelAddButtonPopup.setWidth(popupFormWidth);
        }
        panelAddButtonPopup.setSpacing(true);

        Button btnAddPopup = new Button(ResourceBundleUtils.getLanguageResource("Button.Add"));
        btnAddPopup.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    Object[] selectedArray = ((java.util.Collection) table.getValue()).toArray();
                    int order = 0;
                    if (includeOrder) {
                        order = 1;
                    }
                    int size = storeTable.size();
                    int countON = size;
                    for(int i = 0; i < selectedArray.length; i++) {
                        if(!storeTable.containsId(selectedArray[i])) {
                            countON++;
                            Object[] data = new Object[lstComponent.size() - 1 + order];
                            Item item = table.getItem(selectedArray[i]);
                            int count = order;
                            if(includeOrder) data[0] = countON;
                            for(int j=0; j < lstComponent.size(); j++) {
                                if(j != idField) {
                                    data[count] = item.getItemProperty(((Label)lstComponent.get(j).get(INT_LABEL)).getValue()).
                                                    getValue();
                                    count++;
                                }
                            }
                            storeTable.addItem(data, selectedArray[i].toString());
                        }
                    }
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        panelAddButtonPopup.addComponent(btnAddPopup);
        panelAddButtonPopup.setComponentAlignment(btnAddPopup, Alignment.MIDDLE_CENTER);        
        return panelAddButtonPopup;
    }

    /**
     * Hàm khởi tạo giao diện vùng nút bấm cập nhật bản ghi
     *
     * @since 04/01/2015 HienDM
     * @return giao diện vùng nút bấm cập nhật bản ghi
     */  
    private HorizontalLayout buildUpdatePanelButton() throws Exception {
        HorizontalLayout panelUpdateButtonPopup = new HorizontalLayout();
        panelUpdateButtonPopup.setHeight("50px");
        HorizontalLayout panelArea = new HorizontalLayout();
        panelArea.setSpacing(true);
        if(hasTreeSearch) {
            panelUpdateButtonPopup.setWidth(popupTreeFormWidth);          
        } else {
            panelUpdateButtonPopup.setWidth(popupFormWidth);
        }
        panelUpdateButtonPopup.setSpacing(true);

        Button btnUpdatePopup = new Button(ResourceBundleUtils.getLanguageResource("Button.Update"));
        btnUpdatePopup.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    int order = 0;
                    if (includeOrder) {
                        order = 1;
                    }
                    if(tableData != null) tableData.removeAllItems();
                    Object[] allItemId = ((java.util.Collection) storeTable.getItemIds()).toArray();
                    for(int i=0; i < allItemId.length; i++) {
                        Object[] data = new Object[1 + attachSize];
                        Item item = storeTable.getItem(allItemId[i].toString());
                        data[0] = item.getItemProperty(item.getItemPropertyIds().toArray()[order + (nameField - 1)]).getValue();
                        if(data[0] instanceof ComboboxItem) {
                            data[0] = ((ComboboxItem)data[0]).getValue();
                        }
                        for(int j=0; j < attachSize; j++) {
                            HorizontalLayout txtLayout = new HorizontalLayout();
                            TextField txt = new TextField();
                            txt.setStyleName("scaleShort");
                            txtLayout.addComponent(new TextField());                         
                            data[j + 1] = txtLayout ;
                        }
                        tableData.addItem(data, allItemId[i].toString());
                    }
                    if (mainUI != null) {
                        mainUI.removeWindow(popupWindow);
                    }
                    finishUpdateDataToParent();
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        panelArea.addComponent(btnUpdatePopup);
        
        Button btnDeletePopup = new Button(ResourceBundleUtils.getLanguageResource("Button.Delete"));
        btnDeletePopup.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    Object[] selectedArray = ((java.util.Collection) storeTable.getValue()).toArray();
                    for(int i=0; i < selectedArray.length; i++) {
                        storeTable.removeItem(selectedArray[i]);
                    }
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
        });
        panelArea.addComponent(btnDeletePopup);
        panelUpdateButtonPopup.addComponent(panelArea);
        panelUpdateButtonPopup.setComponentAlignment(panelArea, Alignment.MIDDLE_CENTER);
        return panelUpdateButtonPopup;
    }
    
    /**
     * Hàm xử lý sau khi hoàn thành cập nhật dữ liệu tại form cha
     *
     * @since 28/01/2015 HienDM
     */
    public void finishUpdateDataToParent() throws Exception {}
    
    /**
     * Hàm xử lý sau khi xóa dữ liệu
     *
     * @since 29/01/2015 HienDM
     */    
    @Override
    public void finishDelete(Object[] deleteArray) throws Exception {
        // Cập nhật table        
        for(int i = 0; i < deleteArray.length; i++) {
            if(deleteArray[i] != null) {
                if(storeTable.containsId(deleteArray[i].toString())) {
                    storeTable.removeItem(deleteArray[i].toString());
                }
            }
        }
        
        // Cập nhật dữ liệu tại form cha
        for(int i = 0; i < deleteArray.length; i++) {
            if(deleteArray[i] != null) {
                if(tableData.containsId(deleteArray[i].toString())) {
                    tableData.removeItem(deleteArray[i].toString());
                }
            }
        }       
    }

    /**
     * Hàm xử lý sau khi sửa dữ liệu và cập nhật giao diện
     *
     * @since 29/01/2015 HienDM
     */     
    @Override
    public void finishEdit(long id) throws Exception {
        // Cập nhật store table
        if(storeTable.containsId("" + id)) {
            Collection lstProperty = storeTable.getContainerPropertyIds();
            for(Object p : lstProperty) {
                storeTable.getContainerProperty("" + id, p).setValue(
                    table.getContainerProperty("" + id, p).getValue());
            }
        }
        
        // Cập nhật table tại form cha
        if(tableData.containsId("" + id)) {
            Item item = tableData.getItem("" + id);
            tableData.getContainerProperty("" + id, item.getItemPropertyIds().toArray()[(nameField - 1)]).setValue(
                table.getContainerProperty("" + id, item.getItemPropertyIds().toArray()[(nameField - 1)]).getValue());
        }        
    }    
    
}
