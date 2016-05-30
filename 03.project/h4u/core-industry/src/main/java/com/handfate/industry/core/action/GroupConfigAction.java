/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.core.action;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

/**
 *
 * @author HienDM1
 */
public class GroupConfigAction extends BaseConfigAction {
    /**
     * Hàm khởi tạo giao diện
     *
     * @since 01/04/2014 HienDM
     * @param localMainUI Vùng giao diện của chức năng
     * @return Giao diện sau khi khởi tạo
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        setConfigFileName("com.handfate.industry.core.config.GroupAction");
        super.init(localMainUI);
        return initPanel(1);
    }   
}
