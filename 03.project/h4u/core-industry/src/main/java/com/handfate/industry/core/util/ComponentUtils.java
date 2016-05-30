package com.handfate.industry.core.util;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * @since 23/03/2014
 * @author HienDM
 */
public class ComponentUtils {
    
    /**
     * Hàm khởi tạo đường phân cách giao diện
     *
     * @since 23/03/2014 HienDM
     * @return Đường phân cách giao diện
     */
    public static HorizontalLayout buildSeparator() throws Exception {
        HorizontalLayout layout = buildEmptyHorizontalLayout();
        layout.setWidth("100%");
        layout.setHeight("6px");
        layout.addStyleName("PanelSeparator");
        return layout;
    }
    
    /**
     * Hàm khởi tạo layout rỗng
     * Vaadin ném ra ngoại lệ khi layout rỗng, để Vaadmin không ném ra ngoại
     * lệ ta nhét một Label rỗng vào trong layout.
     * Trong lập trình giao diện thỉnh thoảng ta cần những layout rỗng để tạo
     * khoảng trống
     *
     * @since 23/03/2014 HienDM
     * @return Layout rỗng
     */
    public static HorizontalLayout buildEmptyHorizontalLayout() throws Exception {
        HorizontalLayout layout = new HorizontalLayout();
        Label emptyLabel = new Label("");
        //Nhet Label rong vao layout rong
        layout.addComponent(emptyLabel);
        return layout;        
    }
    
    /**
     * Hàm khởi tạo layout rỗng
     * Vaadin ném ra ngoại lệ khi layout rỗng, để Vaadmin không ném ra ngoại
     * lệ ta nhét một Label rỗng vào trong layout.
     * Trong lập trình giao diện thỉnh thoảng ta cần những layout rỗng để tạo
     * khoảng trống
     *
     * @since 23/03/2014 HienDM
     * @return Layout rỗng
     */
    public static VerticalLayout buildEmptyVerticalLayout() throws Exception {
        VerticalLayout layout = new VerticalLayout();
        Label emptyLabel = new Label("");
        //Nhet Label rong vao layout rong
        layout.addComponent(emptyLabel);
        return layout;        
    }    
}
