package com.handfate.industry.core.util;

import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @since 11/03/2014
 * @author HienDM
 */

public class ResourceBundleUtils {
    
    public static final String SPLIT_CHARACTER = ";";
    static private ResourceBundle rb = null;

    public ResourceBundleUtils() {
    }

    /**
     * Hàm lấy các đoạn text trên giao diện web theo "mã đoạn text" và "ngôn ngữ của người dùng"
     * từ các file ngôn ngữ "com.handfate.industry.core.config.Language.....properties"
     * Ví dụ: getLanguageResource("Page.TitlePage", "_vi_VN")
     * 
     * @since 15/03/2014 HienDM
     * @param key Mã đoạn text
     */     
    public static String getLanguageResource(String key) {
        try {
            rb = ResourceBundle.getBundle("com.handfate.industry.core.config.Language" + VaadinUtils.getLanguageLocation()); //location _vi_VN
            return rb.getString(key);
        } catch(Exception ex) {
            return key;
        }
    }

    /**
     * Hàm lấy các đoạn text cấu hình của chương trình
     * Ví dụ: getConfigureResource("CycleTime")
     * 
     * từ file cấu hình "com.handfate.industry.core.config.config.properties"
     * @since 15/03/2014 HienDM
     * @param key Mã đoạn text
     */ 
    public static String getConfigureResource(String key) {
        try {
            rb = ResourceBundle.getBundle("com.handfate.industry.core.config.Config");
            return rb.getString(key);
        } catch(Exception ex) {
            return key;
        }
    }

    /**
     * Hàm lấy dữ liệu của các combo box fix cứng theo "mã đoạn text" và "ngôn ngữ của người dùng"
     * từ các file ngôn ngữ "com.handfate.industry.core.config.Language.....properties"
     * Ví dụ: getConstantListByKey("country")
     * 
     * @since 15/03/2014 HienDM
     * @param key Mã đoạn text
     * @param location Ngôn ngữ của người dùng
     */ 
    public static List getConstantListByKey(String key, String location) throws Exception {
        rb = ResourceBundle.getBundle("com.handfate.industry.core.config.Language" + location);
        String[] temp = rb.getString(key).split(SPLIT_CHARACTER);
        List result = new ArrayList();
        for (int i = 0; i < temp.length; i++) {
            result.add(Long.valueOf(temp[i]));
        }
        return result;
    }
    
    /**
     * Hàm lấy dữ liệu của các combo box fix cứng theo "mã đoạn text" và "ngôn ngữ của người dùng"
     * từ các file ngôn ngữ "com.handfate.industry.core.config.Language.....properties"
     * Ví dụ: getConstantListByKey("country")
     * 
     * @since 31/03/2015 HienDM
     * @param key Mã đoạn text
     * @param propertiesFileName Đường dẫn file ".properties" do lập trình viên tự định nghĩa
     * @param splitCharacter ký tự phân cách
     */ 
    public static List getConstantListConfig(String key, String propertiesFileName, String splitCharacter) throws Exception {    
        rb = ResourceBundle.getBundle(propertiesFileName);
        String[] temp = rb.getString(key).split(Pattern.quote(splitCharacter));
        List result = new ArrayList();
        for (int i = 0; i < temp.length; i++) {
            result.add(temp[i]);
        }
        return result;
    }    
    
    /**
     * Hàm lấy các thông tin cấu hình khác từ những file ".properties" do lập trình viên tự định nghĩa
     * Ví dụ: getOtherResource("cluster", "com.handfate.industry.core.config.Cassandra")
     * 
     * @since 15/03/2014 HienDM
     * @param key Mã đoạn text
     * @param propertiesFileName Đường dẫn file ".properties" do lập trình viên tự định nghĩa
     */ 
    public static String getOtherResource(String key, String propertiesFileName) {
        try {
            rb = ResourceBundle.getBundle(propertiesFileName);
            return rb.getString(key);
        } catch(Exception ex) {
            return key;
        }
    }
}

