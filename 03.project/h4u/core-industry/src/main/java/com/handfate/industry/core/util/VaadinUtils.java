package com.handfate.industry.core.util;

import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TreeTable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import org.jasig.cas.client.validation.Assertion;

/**
 * @since 11/03/2014
 * @author HienDM
 */
public class VaadinUtils {
    
    public static final String ASSERTION_SESSION_ATTRIBUTE = "_const_cas_assertion_";
    
    /**
     * Hàm lấy thông tin lưu trong Cookie
     * Ví dụ: getCookieValueByName("language")
     * 
     * @since 15/03/2014 HienDM
     * @param name Tên thông tin cần lấy
     * @return Giá trị thông tin cần lấy
     */
    public static String getCookieAttribute(String name) throws Exception {
        // Fetch all cookies from the request
        Cookie[] cookies = VaadinService.getCurrentRequest()
                .getCookies();

        // Iterate to find cookie by its name
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
    
    /**
     * Hàm lưu thông tin vào Cookie
     * Ví dụ: setCookie("language","_vi_VN")
     * 
     * @since 15/03/2014 HienDM
     * @param name Tên thông tin cần lưu
     * @param value Giá trị thông tin cần lưu
     */
    public static void setCookieAttribute(String name, String value) throws Exception {
            Cookie myCookie = new Cookie(name, value);
            myCookie.setPath(VaadinService.getCurrentRequest().getContextPath());
            VaadinService.getCurrentResponse().addCookie(myCookie);        
    }
    
    /**
     * Hàm lấy thông tin đăng nhập
     * @since 15/03/2014 HienDM
     * @return Thông tin đăng nhập
     */
    public static Assertion getAssertion() throws Exception{
        return (Assertion) VaadinService.getCurrentRequest().getWrappedSession().getAttribute(ASSERTION_SESSION_ATTRIBUTE);
    }

    /**
     * Hàm lấy tên người dùng
     * @since 15/03/2014 HienDM
     * @return Tên người dùng
     */
    public static String getFirstName() throws Exception {
        // bo doan code nay de pass qua trang login
        return (String) getAssertion().getPrincipal().getAttributes().get("FIRST_NAME");        
        //return "Hiền";
    }
    
    /**
     * Hàm lấy tên đăng nhập
     * @since 15/03/2014 HienDM
     * @return Tên đăng nhập
     */
    public static String getUserName() throws Exception {
        // bo doan code nay de pass qua trang login
        return (String) getAssertion().getPrincipal().getAttributes().get("USER_NAME");        
        //return "admin";
    }
    
    /**
     * Hàm lấy ngôn ngữ người dùng chọn
     * @since 15/03/2014 HienDM
     * @return Ngôn ngữ người dùng chọn
     */
    public static String getLanguageLocation() throws Exception {
        String languageLocation = (String)VaadinUtils.getSessionAttribute(getUserName() + "_language");
        if(languageLocation == null) {
            VaadinUtils.setSessionAttribute(getUserName() + "_language","");
            languageLocation = "";
        }
        return languageLocation;
    }
    
    /**
     * Hàm thiết lập ngôn ngữ giao diện web cho người dùng
     * @since 15/03/2014 HienDM
     * @param languageLocation Ngôn ngữ
     */    
    public static void setLanguageLocation(String languageLocation) throws Exception {
        VaadinUtils.setSessionAttribute(getUserName() + "_language",languageLocation);
    }
    
    /**
     * Hàm lấy thông tin lưu trong session
     * @since 15/03/2014 HienDM
     * @param key Mã thông tin cần lấy
     * @return Thông tin cần lấy trong session
     */
    public static Object getSessionAttribute(String key) throws Exception{
        return VaadinService.getCurrentRequest().getWrappedSession().getAttribute(key);
    }     
    
    /**
     * Hàm đẩy thông tin vào session
     * @param key Mã thông tin
     * @param value Giá trị cần đẩy vào session
     * @since 15/03/2014 HienDM
     */
    public static void setSessionAttribute(String key, Object value) throws Exception{
        VaadinService.getCurrentRequest().getWrappedSession().setAttribute(key, value);
    }
    
    /**
     * Hàm trả về đường dẫn thư mục chính chứa thư mục WEB-INF
     * @since 15/03/2014 HienDM
     */
    public static String getBasePath() throws Exception {
        return VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
    }

    /**
     * Hàm trả về context path
     *
     * @since 15/03/2014 HienDM
     */
    public static String getContextPath() throws Exception {
        return VaadinServlet.getCurrent().getServletContext().getContextPath();
    }    

    /**
     * Hàm trả về đường dẫn thư mục WEB-NF
     *
     * @since 15/03/2014 HienDM
     */
    public static String getWebInfPath() throws Exception {
        return VaadinServlet.getCurrent().getServletContext().getRealPath("/WEB-INF");
    }
    
    /**
     * Hàm xóa toàn bộ cookie
     *
     * @since 06/01/2014 HienDM
     */    
    public static void deleteAllCookie() throws Exception {
        Cookie[] cookies = VaadinService.getCurrentRequest()
                .getCookies();
        for (Cookie cookie : cookies) {
            cookie.setValue(null);
        }
    }
    
    /**
     * Hàm bắt ngoại lệ
     *
     * @param ex Ngoại lệ
     * @since 06/01/2014 HienDM
     */     
    public static void handleException(Exception ex) {
        if (ex instanceof SQLException) {
            if(((SQLException)ex).getErrorCode() == 2292)
                Notification.show(ResourceBundleUtils.getLanguageResource("Common.Error.ForeignKey"),
                        null, Notification.Type.ERROR_MESSAGE);
            if(((SQLException)ex).getErrorCode() == 1)
                Notification.show(ResourceBundleUtils.getLanguageResource("Common.Error.Duplicate"),
                        null, Notification.Type.ERROR_MESSAGE);
        }        
    }
    
    /**
     * Hàm lấy danh sách tổ tiên của một nút trên tree
     *
     * @param tree cây dữ liệu
     * @param itemId nút dữ liệu cần lấy tổ tiên
     * @return danh sách tổ tiên
     * @since 30/04/2014 HienDM
     */    
    public List getAncestor(TreeTable tree, Object itemId) throws Exception {
        return getRecursiveAncestor(tree, itemId, new ArrayList());
    }
    
    /**
     * Hàm đệ quy lấy danh sách tổ tiên của một nút trên tree
     *
     * @param tree cây dữ liệu
     * @param itemId nút dữ liệu cần lấy tổ tiên
     * @param lstStore danh sách lưu trữ tổ tiên
     * @return danh sách tổ tiên
     * @since 30/04/2014 HienDM
     */    
    public List getRecursiveAncestor(TreeTable tree, Object itemId, List lstStore) throws Exception {
        Object parentId = tree.getParent(itemId);
        if(parentId != null) {            
            lstStore.add(parentId);
            return getRecursiveAncestor(tree, parentId, lstStore);
        } else {
            return lstStore;
        }
    }
    
    /**
     * Hàm đệ quy lấy danh sách tổ tiên của một nút trên tree
     *
     * @param tree cây dữ liệu
     * @param itemId nút dữ liệu cần lấy tổ tiên
     * @param ancestorId nút tổ tiên
     * @return danh sách tổ tiên
     * @since 30/04/2014 HienDM
     */    
    public boolean isAncestor(TreeTable tree, Object itemId, Object ancestorId) throws Exception {
        List lstAncestor = getAncestor(tree, itemId);
        return lstAncestor.contains(ancestorId);
    }    
}
