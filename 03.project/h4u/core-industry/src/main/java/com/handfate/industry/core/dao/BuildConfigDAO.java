package com.handfate.industry.core.dao;

import com.handfate.industry.core.oracle.C3p0Connector;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @since 11/03/2014
 * @author HienDM
 */

public class BuildConfigDAO {    
        
    /**
     * Hàm lấy toàn bộ chức năng chương trình
     * @since 13/03/2014 HienDM
     * @return tất cả chức năng chương trình
     */    
    public List<Map> getMenuConfig () throws Exception {
        List<Map> lstMenuData = C3p0Connector.queryData("select * from sd_function");
        return lstMenuData;
    }
    
    /**
     * Hàm lấy toàn bộ single popup của chương trình
     * @since 13/03/2014 HienDM
     * @return toàn bộ single popup của chương trình
     */    
    public List<Map> getSinglePopup () throws Exception {
        List<Map> lstSinglePopup = C3p0Connector.queryData("select id, config_file from sd_single_popup");
        return lstSinglePopup;
    }
    
    /**
     * Hàm lấy toàn bộ multi popup của chương trình
     * @since 13/03/2014 HienDM
     * @return toàn bộ multi popup của chương trình
     */    
    public List<Map> getMultiPopup () throws Exception {
        List<Map> lstMultiPopup = C3p0Connector.queryData("select id, config_file from sd_multi_popup");
        return lstMultiPopup;
    }
    
    /**
     * Hàm lấy toàn bộ multi popup của chương trình
     * @param connection Kết nối cơ sở dữ liệu
     * @since 19/04/2015 HienDM
     */    
    public void synchronizeMenu (Connection connection) throws Exception {
        C3p0Connector.excuteData("DELETE sm_menu", connection);
        C3p0Connector.excuteData("INSERT INTO sm_menu (menu_id, " +
                                "                     menu_name, " +
                                "                     description, " +
                                "                     ord, " +
                                "                     parent_id, " +
                                "                     is_enable, " +
                                "                     create_user) " +
                                "    SELECT   id, " +
                                "             menu_name, " +
                                "             description, " +
                                "             ord, " +
                                "             parent_id, " +
                                "             is_enable, " +
                                "             create_user " +
                                "      FROM   sd_function", connection);
    }      
    
    /**
     * Hàm lấy toàn bộ thành phần giao diện
     * @since 30/04/2015 HienDM
     * @return toàn bộ thành phần giao diện
     */    
    public List<Map> getAllComponent () throws Exception {
        List<Map> lstCom = C3p0Connector.queryData("select * from sd_component");
        return lstCom;
    }
    
    /**
     * Hàm lấy toàn bộ popup single
     * @since 30/04/2015 HienDM
     * @return toàn bộ popup single
     */    
    public List<Map> getAllSinglePopup () throws Exception {
        List<Map> lstPopup = C3p0Connector.queryData("select * from sd_single_popup");
        return lstPopup;
    }      
    
    /**
     * Hàm lấy toàn bộ popup multi
     * @since 30/04/2015 HienDM
     * @return toàn bộ popup multi
     */    
    public List<Map> getAllMultiPopup () throws Exception {
        List<Map> lstPopup = C3p0Connector.queryData("select * from sd_multi_popup");
        return lstPopup;
    } 
    
    /**
     * Hàm lấy toàn bộ tham số điều kiện where trong query của chức năng
     * @since 30/04/2015 HienDM
     * @return tham số điều kiện where trong query của chức năng
     */    
    public List<Map> getFunctionParam () throws Exception {
        List<Map> lstParam = C3p0Connector.queryData("select * from sd_func_param");
        return lstParam;
    }
    
    /**
     * Hàm lấy toàn bộ tham số điều kiện where trong query của tree tại chức năng
     * @since 30/04/2015 HienDM
     * @return tham số điều kiện where trong query của tree tại chức năng
     */    
    public List<Map> getTreeParam () throws Exception {
        List<Map> lstParam = C3p0Connector.queryData("select * from sd_tree_param");
        return lstParam;
    }
    
    /**
     * Hàm lấy toàn bộ tham số điều kiện where trong query của thành phần giao diện
     * @since 30/04/2015 HienDM
     * @return tham số điều kiện where trong query của thành phần giao diện
     */    
    public List<Map> getComponentParam () throws Exception {
        List<Map> lstParam = C3p0Connector.queryData("select * from sd_com_param");
        return lstParam;
    }     
}
