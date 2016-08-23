/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.core.action;

import com.handfate.industry.core.MainUI;
import static com.handfate.industry.core.MainUI.mainLogger;
import com.handfate.industry.core.action.component.Banner;
import com.handfate.industry.core.action.component.Footer;
import com.handfate.industry.core.dao.BaseDAO;
import com.handfate.industry.core.util.ComponentUtils;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.handfate.industry.extend.action.HomeAction;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.UI;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HienDM
 */
public class LayoutAction {

    public VerticalLayout buildLeftLayout(String userName, boolean includeHome, boolean includeHelp,
            boolean includeExit, VerticalLayout bodyLayout, UI localMainUI, HorizontalSplitPanel container, Long userId) throws Exception {
        VerticalLayout containerLayout = new VerticalLayout();
        containerLayout.setSizeFull();
        VerticalLayout leftLayout = new VerticalLayout();
        leftLayout.setHeight("99.3%");
        containerLayout.addComponent(leftLayout);
        
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("253px");
        topLayout.setHeight("79px");
        topLayout.addStyleName("top-menu-layout");
        Embedded topMenu = new Embedded(null, new ThemeResource("img/industry/layout/hf_top_menu.png"));
        HorizontalLayout menuIcon = new HorizontalLayout();
        menuIcon.addStyleName("menu-button-layout");
        menuIcon.setWidth("43px");
        menuIcon.setHeight("35px");
        
        HorizontalLayout menuIconHover = new HorizontalLayout();
        menuIconHover.addStyleName("menu-button-hover");
        menuIconHover.setWidth("43px");
        menuIconHover.setHeight("35px");
        menuIconHover.setVisible(false);
        containerLayout.addComponent(menuIconHover);
        
        menuIcon.addLayoutClickListener(new LayoutClickListener() {
            @Override
            public void layoutClick(LayoutClickEvent event) {
                try {
                    leftLayout.setVisible(false);
                    menuIconHover.setVisible(true);
                    container.setSplitPosition(50, Sizeable.Unit.PIXELS);
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }                
            }
        });
        
        menuIconHover.addLayoutClickListener(new LayoutClickListener() {
            @Override
            public void layoutClick(LayoutClickEvent event) {
                try {
                    leftLayout.setVisible(true);
                    menuIconHover.setVisible(false);
                    container.setSplitPosition(253, Sizeable.Unit.PIXELS);
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }
            }
        });        
        
        //topLayout.addComponent(topMenu);
        topLayout.addComponent(menuIcon);
        leftLayout.addComponent(topLayout);
        leftLayout.setExpandRatio(topLayout, 0f);
        
        // <editor-fold defaultstate="collapsed" desc="Control bar">
        VerticalLayout controlBar = new VerticalLayout();
        controlBar.setWidth("100%");
        controlBar.addStyleName("ControlBar");
        controlBar.setSpacing(true);
        leftLayout.addComponent(controlBar);
        leftLayout.setExpandRatio(controlBar, 0f);
        
        HorizontalLayout leftSpace = new HorizontalLayout();
        leftSpace.setWidth("5px");
        controlBar.addComponent(leftSpace);
        
        HorizontalLayout row1 = new HorizontalLayout();
        controlBar.addComponent(row1);
        row1.setSizeFull();
        // welcome
        HorizontalLayout cell1 = new HorizontalLayout();
        row1.addComponent(cell1);
        HorizontalLayout cell11 = new HorizontalLayout();
        Embedded iconUser = new Embedded(null, new ThemeResource("img/industry/layout/hf_user.png"));
        cell11.addComponent(iconUser);        
        cell11.setComponentAlignment(iconUser, Alignment.MIDDLE_LEFT);
        
        Label userLabel = new Label(ResourceBundleUtils.getLanguageResource("Common.Hi") + " " + userName + "! ");
        cell11.addComponent(userLabel);
        cell11.setComponentAlignment(userLabel, Alignment.MIDDLE_LEFT);
        cell1.addComponent(cell11);
        
        cell11.addLayoutClickListener(new LayoutClickListener() {
            @Override
            public void layoutClick(LayoutClickEvent event) {
                try {
                    bodyLayout.removeAllComponents();
                    LayoutAction layoutAction = new LayoutAction();
                    UserProfileAction userProfileAction = new UserProfileAction();
                    bodyLayout.addComponent(layoutAction.buildBodyPage(userProfileAction.init(localMainUI),
                            ResourceBundleUtils.getLanguageResource("Menu.User.ChangeProfile"),
                            0f, 100f, 0f));
                    bodyLayout.addComponent(layoutAction.buildFooter());
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }
            }
        });
        
        HorizontalLayout cell2 = new HorizontalLayout();
        row1.addComponent(cell2);        
        cell2.setSpacing(true);
        Embedded iconVN = new Embedded(null, new ThemeResource("img/industry/layout/hf_vn.png"));
        iconVN.addClickListener(new MouseEvents.ClickListener() {
            public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
                try {
                    VaadinUtils.setLanguageLocation("_vi_VN");
                    Page.getCurrent().open(VaadinUtils.getContextPath(), null);
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }
            }
        });
        cell2.addComponent(iconVN);
        cell2.setComponentAlignment(iconVN, Alignment.MIDDLE_RIGHT);        
        
        Embedded iconEN = new Embedded(null, new ThemeResource("img/industry/layout/hf_en.png"));
        iconEN.addClickListener(new MouseEvents.ClickListener() {
            public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
                try {
                    VaadinUtils.setLanguageLocation("_en_US");
                    Page.getCurrent().open(VaadinUtils.getContextPath(), null);
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }
            }
        });
        cell2.addComponent(iconEN);
        cell2.setComponentAlignment(iconEN, Alignment.MIDDLE_RIGHT);
        row1.setExpandRatio(cell1, 10f);
        row1.setExpandRatio(cell2, 0f);
        
        //row 2
        HorizontalLayout row2 = new HorizontalLayout();
        controlBar.addComponent(row2);
        row2.setSizeFull();
        //home
        HorizontalLayout cell3 = new HorizontalLayout();
        row2.addComponent(cell3);
        HorizontalLayout cell31 = new HorizontalLayout();
        Embedded iconHome = new Embedded(null, new ThemeResource("img/industry/layout/hf_home.png"));
        cell31.addComponent(iconHome);
        cell31.setComponentAlignment(iconHome, Alignment.MIDDLE_LEFT);
        Label homeLabel = new Label(ResourceBundleUtils.getLanguageResource("HomePage.TitlePage"));
        cell31.addComponent(homeLabel);
        cell31.setComponentAlignment(homeLabel, Alignment.MIDDLE_LEFT);        
        cell3.addComponent(cell31);
        
        cell31.addLayoutClickListener(new LayoutClickListener() {
            @Override
            public void layoutClick(LayoutClickEvent event) {
                try {
                    bodyLayout.removeAllComponents();
                    HomeAction homeAction = new HomeAction();
                    LayoutAction layoutAction = new LayoutAction();
                    bodyLayout.addComponent(layoutAction.buildBodyPage(homeAction.initHomePanel(),
                            ResourceBundleUtils.getLanguageResource("HomePage.TitlePage"),
                            0f, 100f, 0f));
                    bodyLayout.addComponent(layoutAction.buildFooter());
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }
            }
        });
        
        // exit
        HorizontalLayout cell4 = new HorizontalLayout();
        row2.addComponent(cell4);
        HorizontalLayout cell41 = new HorizontalLayout();
        Embedded iconExit = new Embedded(null, new ThemeResource("img/industry/layout/hf_exit.png"));
        cell41.addComponent(iconExit);
        cell41.setComponentAlignment(iconExit, Alignment.MIDDLE_RIGHT);        
        
        Label logoutLabel = new Label(ResourceBundleUtils.getLanguageResource("Common.Logout"));
        cell41.addComponent(logoutLabel);
        cell41.setComponentAlignment(logoutLabel, Alignment.MIDDLE_RIGHT);
        cell4.addComponent(cell41);
        cell41.addLayoutClickListener(new LayoutClickListener() {
            @Override
            public void layoutClick(LayoutClickEvent event) {
                try {
                    VaadinSession.getCurrent().getSession().invalidate();
                    VaadinUtils.deleteAllCookie();
                    Page.getCurrent().setLocation(ResourceBundleUtils.getConfigureResource("LogoutURL"));
                } catch (Exception ex) {
                    VaadinUtils.handleException(ex);
                    mainLogger.debug("Industry error: ", ex);
                }                
            }
        });
        
        row2.setExpandRatio(cell3, 10f);
        row2.setExpandRatio(cell4, 0f);

        // </editor-fold>               
        
        HorizontalLayout menuBar = new HorizontalLayout();
        leftLayout.addComponent(menuBar);
        leftLayout.setExpandRatio(menuBar, 10f);
        BaseDAO baseDao = new BaseDAO();        
        List<Map> lstMenuData = baseDao.getAllMenuItem(userId);
        TreeTable table = new TreeTable();
        table.setSelectable(true);
        table.setMultiSelect(false);
        table.addContainerProperty(ResourceBundleUtils.getLanguageResource(
                "Menu.System.Function"), String.class, null);
        for(int i = 0; i < lstMenuData.size(); i++) {
            Object[] data = new Object[1];
            data[0] = ResourceBundleUtils.getLanguageResource(lstMenuData.get(i).get("menu_name").toString());
            table.addItem(data,lstMenuData.get(i).get("menu_id").toString());
        }
        
        for (int i = 0; i < lstMenuData.size(); i++) {
            if (lstMenuData.get(i).get("parent_id") != null && !lstMenuData.get(i).get("parent_id").toString().equals("0")) {
                table.setParent(lstMenuData.get(i).get("menu_id").toString(),
                        lstMenuData.get(i).get("parent_id").toString());
            }
        }
        
        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                for(int i=0; i < lstMenuData.size(); i++) {
                    if(lstMenuData.get(i).get("menu_id").toString().equals(itemClickEvent.getItemId().toString())) {
                        String menuText = lstMenuData.get(i).get("menu_name").toString();
                        try {
                            LayoutAction layoutAction = new LayoutAction();
                            boolean check = true;
                            
                            int numberOfMenu = Integer.parseInt(ResourceBundleUtils.getOtherResource(
                                    "NumberOfMenu", "com.handfate.industry.core.config.Menu"));
                            for(int j = 1; j <= numberOfMenu; j++) {
                                String menuInfo = ResourceBundleUtils.getOtherResource(
                                    "Menu" + j, "com.handfate.industry.core.config.Menu");
                                if (menuText.equals(menuInfo.split(";")[0])) {
                                    String className = "";
                                    if(menuInfo.split(";")[2].equals("0")) {
                                        className = "com.handfate.industry.core.action.BaseConfigAction";
                                    } else if(menuInfo.split(";")[2].equals("1")) {
                                        className = "com.handfate.industry.core.action." + menuInfo.split(";")[1];
                                    } else {
                                        className = "com.handfate.industry.extend.action." + menuInfo.split(";")[1];
                                    }
                                    Class action = Class.forName(className);
                                    BaseAction bcaction = (BaseAction)action.newInstance();
                                    bcaction.setConfigFileName("com.handfate.industry.extend.config." + menuInfo.split(";")[1]);
                                    bodyLayout.removeAllComponents();
                                    bodyLayout.addComponent(layoutAction.buildBodyPage(bcaction.init(localMainUI),
                                            ResourceBundleUtils.getLanguageResource(menuText),
                                            0f, 100f, 0f));
                                    check = false;
                                    break;
                                }
                            }
                            if(check) {
                                ((TreeTable) table).setCollapsed(itemClickEvent.getItemId(), 
                                            !((TreeTable) table).isCollapsed(itemClickEvent.getItemId()));
                            } else {
                                bodyLayout.addComponent(layoutAction.buildFooter());                                
                            }
                        } catch (Exception ex) {
                            VaadinUtils.handleException(ex);
                            mainLogger.debug("Industry error: ", ex);
                        }
                    }
                }
            }
        });
        menuBar.addComponent(table);
        table.setColumnWidth(ResourceBundleUtils.getLanguageResource(
                "Menu.System.Function"), 400);
        int screenHeight = Page.getCurrent().getBrowserWindowHeight();
        int pageLength = 0;
        if(Page.getCurrent().getWebBrowser().isChrome())
            pageLength = Math.round((screenHeight - 226)/20) - 2;
        if(Page.getCurrent().getWebBrowser().isFirefox())
            pageLength = Math.round((screenHeight - 226)/21) - 2;
        table.setPageLength(pageLength);
        table.setWidth("253px");
        
//        Embedded bottomMenu = new Embedded(null, new ThemeResource("img/industry/layout/hf_bottom_menu.png"));
//        leftLayout.addComponent(bottomMenu);     
//        leftLayout.setExpandRatio(bottomMenu, 0f);
        
        return containerLayout;
    }
    
    /**
     * Hàm khởi tạo vùng header của trang web Ví dụ: buildHeader("Cài đặt",
     * "img/industry/layout/Family_Logo.png", "admin", false, false,false))
     *
     * @since 13/03/2014 HienDM
     * @param pageTitle Tên trang web
     * @param logoPath Đường dẫn ảnh logo
     * @param userName Tên đăng nhập của người dùng
     * @param includeHome Hiển thị icon Home trên header hay không?
     * @param includeHelp Hiển thị icon Help trên header hay không?
     * @param includeExit Hiển thị icon exit trên header hay không?
     * @param bodyLayout Giao diện phần body
     * @param localMainUI Giao diện chương trình
     * @return Giao diện vùng header của trang web
     */
    public HorizontalLayout buildHeader(String pageTitle, String logoPath,
            String userName, boolean includeHome, boolean includeHelp,
            boolean includeExit, VerticalLayout bodyLayout, UI localMainUI) throws Exception {
        //-------- Thanh header cua trang web -----------------------------
        HorizontalLayout titlebar = new HorizontalLayout();
        titlebar.addStyleName("SiteHeaderBar");
        titlebar.setSizeFull();

        // <editor-fold defaultstate="collapsed" desc="Phan ben trai cua thanh header">
        HorizontalLayout leftTitlebar = new HorizontalLayout();
        leftTitlebar.addStyleName("LayoutAlignLeft");
        leftTitlebar.setSpacing(true);
        titlebar.addComponent(leftTitlebar);
        titlebar.setComponentAlignment(leftTitlebar, Alignment.MIDDLE_LEFT);

        HorizontalLayout leftSpace = new HorizontalLayout();
        leftSpace.setWidth("5px");
        leftTitlebar.addComponent(leftSpace);

        Embedded logo = new Embedded(null, new ThemeResource(logoPath));
        leftTitlebar.addComponent(logo);
        leftTitlebar.setComponentAlignment(logo, Alignment.MIDDLE_LEFT);

        // </editor-fold>   

        HorizontalLayout rightTitlebar = new HorizontalLayout();
        rightTitlebar.addStyleName("LayoutAlignRight");
        rightTitlebar.setSpacing(true);
        titlebar.addComponent(rightTitlebar);
        titlebar.setComponentAlignment(rightTitlebar, Alignment.MIDDLE_RIGHT);
        return titlebar;
    }

    /**
     * Hàm khởi tạo vùng phía dưới bị che khuất bởi header để các thành phần
     * khác của trang web không đặt vào vùng này
     *
     * @return Giao diện vùng rỗng có kích thước bằng header
     * @since 13/03/2014 HienDM
     */
    public HorizontalLayout buildHeaderSpace(
            String userName, boolean includeHome, boolean includeHelp,
            boolean includeExit, VerticalLayout bodyLayout) throws Exception {
        //Them phan space 40px ben duoi thanh header
        HorizontalLayout headerSpace = new HorizontalLayout();
        headerSpace.addStyleName("WhiteBar");
        headerSpace.setHeight("40px");
        headerSpace.setWidth("100%");     
        
        return headerSpace;
    }

    /**
     * Hàm khởi tạo vùng banner của trang web
     *
     * @param leftWidth Tỷ lệ phần trăm chiều rộng vùng không gian bên trái
     * @param centerWidth Tỷ lệ phần trăm chiều rộng vùng không gian chính giữa
     * @param rightWidth Tỷ lệ phần trăm chiều rộng vùng không gian bên phải
     * @return Giao diện vùng banner
     * @since 16/03/2014 HienDM
     */
    public HorizontalLayout buildBanner(Float leftWidth, Float centerWidth, Float rightWidth) throws Exception {
        HorizontalLayout banner = new HorizontalLayout();
        banner.setStyleName("Banner");
        banner.setWidth("100%");
        banner.setHeight("220px");

        // Vung ben trai
        if (leftWidth != null && leftWidth != 0f) {
            VerticalLayout leftSpace = buildLeftSpace();
            banner.addComponent(leftSpace);
            banner.setExpandRatio(leftSpace, leftWidth);
        } else {
            VerticalLayout leftSpace = buildLeftSpace();
            leftSpace.setWidth("15px");
            banner.addComponent(leftSpace);
        }

        // Vung trung tam
        VerticalLayout centerBodyPage = new VerticalLayout();
        centerBodyPage.setWidth("100%");
        Banner bannerComponent = new Banner();
        centerBodyPage.addComponent(bannerComponent);
        banner.addComponent(centerBodyPage);
        banner.setExpandRatio(centerBodyPage, centerWidth);

        // Vung ben phai
        if (rightWidth != null && rightWidth != 0f) {
            VerticalLayout rightSpace = buildRightSpace();
            banner.addComponent(rightSpace);
            banner.setExpandRatio(rightSpace, rightWidth);
        } else {
            VerticalLayout rightSpace = buildRightSpace();
            rightSpace.setWidth("15px");
            banner.addComponent(rightSpace);
        }
        banner.addComponent(ComponentUtils.buildSeparator());
        return banner;
    }

    /**
     * Hàm khởi tạo vùng không gian bên trái
     *
     * @return Vùng không gian bên trái
     * @since 16/03/2014 HienDM
     */
    public VerticalLayout buildLeftSpace() throws Exception {
        VerticalLayout leftSpace = new VerticalLayout();
        leftSpace.setWidth("100%");
        leftSpace.setHeight("100%");
        // Them code quang cao o day

        return leftSpace;
    }

    /**
     * Hàm khởi tạo vùng không gian bên phải
     *
     * @return Vùng không gian bên phải
     * @since 16/03/2014 HienDM
     */
    public VerticalLayout buildRightSpace() throws Exception {
        VerticalLayout rightSpace = new VerticalLayout();
        rightSpace.setWidth("100%");
        rightSpace.setHeight("100%");
        // Them code quang cao o day

        return rightSpace;
    }

    /**
     * Hàm khởi tạo vùng giao diện vùng body của trang web. Ví dụ:
     * buildBodyPage(mainContent, "Mã cài đặt sản phẩm", 10f, 80f, 10f) Vùng
     * body là vùng phía dưới banner chia làm 3 phần - Vùng không gian bên trái
     * - Vùng không gian chính giữa - Vùng không gian bên phải
     *
     * @since 16/03/2014 HienDM
     * @param mainContent Giao diện vùng không gian chính giữa
     * @param title Tiêu đề của giao diện vùng không gian chính giữa
     * @param leftWidth Tỷ lệ phần trăm chiều rộng vùng không gian bên trái
     * @param centerWidth Tỷ lệ phần trăm chiều rộng vùng không gian chính giữa
     * @param rightWidth Tỷ lệ phần trăm chiều rộng vùng không gian bên phải
     * @return Giao diện vùng body của trang web
     */
    public HorizontalLayout buildBodyPage(Component mainContent, String title, Float leftWidth, Float centerWidth, Float rightWidth) throws Exception {
        HorizontalLayout bodyPage = new HorizontalLayout();
        bodyPage.setWidth("100%");
        bodyPage.setHeight("100%");
        bodyPage.setStyleName("BodyContainer");

        // Vung ben trai
        if (leftWidth != null && leftWidth != 0f) {
            VerticalLayout leftSpace = buildLeftSpace();
            bodyPage.addComponent(leftSpace);
            bodyPage.setExpandRatio(leftSpace, leftWidth);
        } /*else {
            VerticalLayout leftSpace = buildLeftSpace();
            leftSpace.setWidth("15px");
            bodyPage.addComponent(leftSpace);
        }*/

        // Vung trung tam
        VerticalLayout centerBodyPage = new VerticalLayout();
        centerBodyPage.setWidth("100%");

        // Tieu de
        HorizontalLayout titleBar = new HorizontalLayout();
        titleBar.setWidth("100%");
        titleBar.setHeight("38px");
        titleBar.addStyleName("page-body-title");
        Label lblTitle = new Label("  " + title);
        titleBar.addComponent(lblTitle);
        centerBodyPage.addComponent(titleBar);
        // Giao dien chinh
        mainContent.addStyleName("page-body");
        mainContent.setWidth("100%");
        mainContent.setHeight("100%");
        centerBodyPage.addComponent(mainContent);
        bodyPage.addComponent(centerBodyPage);
        bodyPage.setExpandRatio(centerBodyPage, centerWidth);

        // Vung ben phai
        if (rightWidth != null && rightWidth != 0f) {
            VerticalLayout rightSpace = buildRightSpace();
            bodyPage.addComponent(rightSpace);
            bodyPage.setExpandRatio(rightSpace, rightWidth);
        } /*else {
            VerticalLayout rightSpace = buildRightSpace();
            rightSpace.setWidth("15px");
            bodyPage.addComponent(rightSpace);
        }*/

        return bodyPage;
    }

    /**
     * Hàm tạo giao diện footer
     *
     * @return Giao diện vùng footer
     * @since 19/11/2014 HienDM
     */
    public HorizontalLayout buildFooter() throws Exception {
        HorizontalLayout footerContainer = new HorizontalLayout();
        footerContainer.setWidth("100%");
        footerContainer.setStyleName("FooterBar");
        Footer footer = new Footer();
        footerContainer.addComponent(footer);
        return footerContainer;
    }
    
}
