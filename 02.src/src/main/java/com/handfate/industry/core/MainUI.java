package com.handfate.industry.core;

import com.handfate.industry.core.action.LayoutAction;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

import javax.servlet.annotation.WebServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.handfate.industry.core.dao.BaseDAO;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.handfate.industry.extend.action.HomeAction;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;

/**
 * @since 11/03/2014
 * @author HienDM
 */
@Theme("mytheme")
@SuppressWarnings("serial")
@StyleSheet("industry.css")
@JavaScript({"hf_top_menu.png","menu_icon.png","menu_icon_hover.png"})
public class MainUI extends UI {
    public static final Logger mainLogger = LoggerFactory.getLogger(MainUI.class);
        
    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MainUI.class, widgetset = "com.handfate.industry.core.AppWidgetSet")
    public static class MainServlet extends VaadinServlet {        
        @Override
        protected void servletInitialized() throws ServletException {
            super.servletInitialized();
        }

        @Override
        public void destroy() {
            super.destroy();
        }        
    }

    /**
     * Hàm khởi tạo đầu tiên của trang web
     *
     * @since 13/03/2014 HienDM
     * @param request Chứa thông tin người dùng gửi lên máy chủ
     */
    @Override
    public void init(VaadinRequest request) {        
        Connection conn = null;
        try {
            conn = C3p0Connector.getInstance().getConnection();
            boolean reachable = conn.isValid(10);

            if (!reachable) { // neu chua tao database thi can cai dat {can bo sung them mot Flag nua}
                Notification.show(ResourceBundleUtils.getLanguageResource("Common.Error.Database"),
                        null, Notification.Type.ERROR_MESSAGE);
            } else {
                setLoginInforToSession();
                if (VaadinUtils.getLanguageLocation().equals("")) {
                    VaadinUtils.setLanguageLocation("_vi_VN");
                }
                Long userId = Long.parseLong(VaadinUtils.getSessionAttribute("G_UserId").toString());
                updateUI(userId, VaadinUtils.getFirstName());
            }
        } catch (Exception ex) {
            VaadinUtils.handleException(ex);
            mainLogger.debug("Industry error: ", ex);
        } finally {
            try {
                if(conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (Exception ex) {
                VaadinUtils.handleException(ex);
                mainLogger.debug("Industry error: ", ex);
            }
        }
    }
    
    /**
     * Hàm đẩy thông tin người dùng vào session
     *
     * @since 13/03/2014 HienDM
     */    
    public void setLoginInforToSession() throws Exception {
        BaseDAO baseDao = new BaseDAO();
        List<Map> lstData = baseDao.selectUser("select USER_ID, USER_NAME,"
                + "PASSWORD,BIRTHDAY,GENDER,FIRST_NAME,"
                + "LAST_NAME,EMAIL,MOBILE,COUNTRY,"
                + "CREATE_DATE,IS_ENABLE,ROLE_ID,"
                + "(select role_name from sm_role where role_id = sm_users.role_id) role_name,"
                + "(select role_code from sm_role where role_id = sm_users.role_id) role_code,"
                + "GROUP_ID,"
                + "(select group_name from sm_group where group_id = sm_users.group_id) group_name,"                
                + "CREATE_USER from SM_USERS where USER_NAME = ? ", VaadinUtils.getUserName());
        if(lstData != null && lstData.size() > 0) {
            VaadinUtils.setSessionAttribute("G_UserId", lstData.get(0).get("user_id").toString());
            VaadinUtils.setSessionAttribute("G_UserName", lstData.get(0).get("user_name").toString());
            VaadinUtils.setSessionAttribute("G_RoleId", lstData.get(0).get("role_id").toString());
            if(lstData.get(0).get("role_code") != null)
                VaadinUtils.setSessionAttribute("G_RoleCode", lstData.get(0).get("role_code").toString());
            if(lstData.get(0).get("role_name") != null)
                VaadinUtils.setSessionAttribute("G_RoleName", lstData.get(0).get("role_name").toString());
            VaadinUtils.setSessionAttribute("G_GroupId", lstData.get(0).get("group_id").toString());
            if(lstData.get(0).get("group_name") != null)
                VaadinUtils.setSessionAttribute("G_GroupName", lstData.get(0).get("group_name").toString());
        }
    }
    
    public void updateUI(Long userId, String firstName) throws Exception {
        addStyleName("PageBody");
        // Layout cua toan bo trang web

        HorizontalSplitPanel container = new HorizontalSplitPanel();
        container.setSplitPosition(253, Sizeable.Unit.PIXELS);
        container.setLocked(true);
        container.setSizeFull();
        VerticalLayout bodyLayout = new VerticalLayout();
        // Build menu bên trái
        LayoutAction layoutAction = new LayoutAction();
        VerticalLayout leftLayout = layoutAction.buildLeftLayout(firstName,
                true,
                false,
                true, bodyLayout, this, container, userId);
        container.setFirstComponent(leftLayout);

        VerticalLayout mainLayout = new VerticalLayout();
        container.setSecondComponent(mainLayout);
        mainLayout.addStyleName("PageBody");
        setContent(container);

        getPage().setTitle(ResourceBundleUtils.getLanguageResource("Page.BrowserTitlePage"));

        mainLayout.addComponent(bodyLayout);
        HomeAction homeAction = new HomeAction();
        bodyLayout.addComponent(layoutAction.buildBodyPage(homeAction.initHomePanel(),
                ResourceBundleUtils.getLanguageResource("HomePage.TitlePage"),
                0f, 100f, 0f));
        bodyLayout.addComponent(layoutAction.buildFooter());
    }     
}
