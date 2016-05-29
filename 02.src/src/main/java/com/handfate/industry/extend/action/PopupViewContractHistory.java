/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.BaseAction;
import static com.handfate.industry.core.action.BaseAction.INT_LABEL;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 *
 * @author HienDM1
 */
public class PopupViewContractHistory {
    public Window popupWindow = new Window();
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
     * @param window window của popup
     * @return Giao diện sau khi khởi tạo
     */
    /*public HorizontalLayout initPanel(int column, Window window) throws Exception {
        if(layoutPanel != null) layoutPanel.removeAllComponents();
        if(mainPanel != null) mainPanel.removeAllComponents();
        cboData = cboResult;
        popupWindow = window;
        setAllowAdd(false);
        setAllowEdit(false);
        setAllowDelete(false);
        setAllowDetail(false);
        if (hasTreeSearch) {
            buttonArea.setWidth(popupTreeFormWidth);
        } else {
            buttonArea.setWidth(popupFormWidth);
        }
        return super.initPanel(column);
    }
    
    @Override
    public VerticalLayout buildNormalDataPanel() throws Exception {        
        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                cboData.removeAllItems();
                cboData.addItem(itemClickEvent.getItemId().toString());
                String strCaption = itemClickEvent.getItem().getItemProperty(
                        ((Label) lstComponent.get(nameField).get(INT_LABEL)).getValue())
                        .getValue().toString();
                cboData.setItemCaption(itemClickEvent.getItemId().toString(), strCaption);
                cboData.setValue(itemClickEvent.getItemId().toString());
                if (mainUI != null) {
                    mainUI.removeWindow(popupWindow);
                }
            }
        });
        VerticalLayout dataPanel = super.buildNormalDataPanel();
        if(hasTreeSearch) {
            dataPanel.setWidth(popupTreeFormWidth);
            formArea.setWidth(popupTreeFormWidth);            
        } else {
            dataPanel.setWidth(popupFormWidth);
            formArea.setWidth(popupFormWidth);
        }
        return dataPanel;
    }*/

}
