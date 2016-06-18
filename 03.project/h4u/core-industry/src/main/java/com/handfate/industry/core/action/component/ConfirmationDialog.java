package com.handfate.industry.core.action.component;

import com.handfate.industry.core.util.ResourceBundleUtils;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

/**
 * @since 12/12/2014
 * @author HienDM
 */
public class ConfirmationDialog extends Window implements ClickListener {
    public Button yesButton;
    
    public static interface Callback {
        /**
         * Gọi sự kiện khi click nút bấm
         * 
         * @since 12/12/2014 HienDM
         * @param buttonName tên nút bấm (yes, no hoặc cancel)
         */
        void onDialogResult(String buttonName);
    }

    public static final String YES = "Common.Yes";
    public static final String NO = "Common.No";
    public static final String CANCEL = "Common.Cancel";

    public Callback m_callback;

    /**
     * Hàm khởi tạo dialog yes/no
     * 
     * @since 12/12/2014 HienDM
     * @param caption Tiêu đề của dialog
     * @param message Nội dung trong dialog
     * @param callback xử lý sự kiện click nút bấm yes/no
     */
    public ConfirmationDialog(String caption, String message, Callback callback) {
        this(caption, message, callback, 
                ResourceBundleUtils.getLanguageResource(YES), 
                ResourceBundleUtils.getLanguageResource(YES), 
                ResourceBundleUtils.getLanguageResource(NO));
    }

    /**
     * Provides a confirmation dialog with a custom set of buttons.
     * 
     * @since 12/12/2014 HienDM
     * @param caption Tiêu đề của dialog
     * @param message Nội dung trong dialog
     * @param callback Xử lý sự kiện click nút bấm yes/no
     * @param defaultButton Nút bấm mặc định khi ấn phím Enter
     * @param buttonNames Các nút bấm
     */
    public ConfirmationDialog(String caption, String message, Callback callback, String defaultButton, String... buttonNames) {
        super(caption);

        if (buttonNames == null || buttonNames.length <= 1) {
            throw new IllegalArgumentException("Need at least one button name!");
        }
        /*if (callback == null) {
            throw new IllegalArgumentException("Need a callback!");
        }*/

        m_callback = callback;

        setWidth("30em");
        setModal(true);

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);
        addComponents(message, defaultButton, buttonNames);
    }

    /**
     * Xử lý sự kiện click nút bấm
     * 
     * @since 12/12/2014 HienDM
     */
    public void buttonClick(ClickEvent event) {
        UI localMainUI = (UI)this.getParent();
        if(localMainUI != null){
            localMainUI.removeWindow(this);
        }

        AbstractComponent comp = (AbstractComponent) event.getComponent();
        m_callback.onDialogResult((String) comp.getData());
    }

    /**
     * Tạo giao diện dialog
     * 
     * @since 12/12/2014 HienDM
     * @param message Nội dung trong dialog
     * @param buttonNames Các nút bấm
     */
    private void addComponents(String message, String defaultButton, String... buttonNames) {
        if (message != null) {
            ((VerticalLayout)this.getContent()).addComponent(new Label(message));
        }

        GridLayout gl = new GridLayout(buttonNames.length + 1, 1);
        gl.setSpacing(true);
        gl.setWidth("100%");

        gl.addComponent(new Label(" "));
        gl.setColumnExpandRatio(0, 1.0f);

        for (String buttonName : buttonNames) {
            Button button = new Button(buttonName, this);
            button.setData(buttonName);
            
            if(buttonName.equals(ResourceBundleUtils.getLanguageResource(YES))) yesButton = button;
            
            if (defaultButton != null && defaultButton.equals(buttonName)) {
                button.setStyleName(Reindeer.BUTTON_DEFAULT);
                button.setClickShortcut(KeyCode.ENTER);
                button.focus();
            }
            gl.addComponent(button);
        }

        ((VerticalLayout)this.getContent()).addComponent(gl);
    }
}