/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.BaseAction;
import com.handfate.industry.core.action.PopupMultiUserAction;
import com.handfate.industry.core.action.component.UploadField;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.handfate.industry.core.util.Base64Utils;
import com.handfate.industry.core.util.FileUtils;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class H4URevenueAction extends BaseAction {

    /**
     * HÃ m khá»Ÿi táº¡o giao diá»‡n
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI VÃ¹ng giao diá»‡n cá»§a chá»©c nÄƒng
     * @return Giao diá»‡n sau khi khá»Ÿi táº¡o
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khá»Ÿi táº¡o tham sá»‘
        setTableName("h4u_revenue");
        setIdColumnName("ID");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("ID");
        setSortAscending(false);
        setSequenceName("h4u_revenue_seq");
        
        buildTreeSearch("Nhà cho thuê",
                "select house_id, name from h4u_house where 1=1 ", null,
                "house_id", "name", null, "0", " and id in (select revenue_id from h4u_revenue_house where house_id = ?) ",
                true);

        addTextFieldToForm("ID", new TextField(), "ID", "int", true, 50, null, null, false, false, null, false, null, true, true, false, true, null);
        addTextFieldToForm("Tên", new TextField(), "name", "string", true, 100, null, null, true, false, null, false, null, true, true, true, true, null);
        addTextFieldToForm("Ngày hạch toán", new PopupDateField(), "create_date", "date", true, null, null, null, true, false, null, false, null, true, true, true, true, null);
        addUploadFieldToForm("File đính kèm", new UploadField(), "ATTACH_FILE", "file", false, null, null, null, false, H4URevenueAction.class.toString(), 10);
        addMultiPopupToForm("Nhà cho thuê", true, false, new PopupMultiHouseAction(localMainUI), 2, null,
                "h4u_revenue_house", "house_id", "revenue_id", "id", "h4u_revenue_house_seq", null, null, null);                
        addMultiPopupToForm("Người nhận", false, false, new PopupMultiUserAction(localMainUI), 2, null,
                "h4u_revenue_user", "user_id", "revenue_id", "id", "h4u_revenue_user_seq", null, null, null);
        
        return initPanel(2);
    }

    @Override
    public void afterAddData(Connection connection, long id) throws Exception {
        Table houseTable = getMultiComponent("h4u_house");
        Object[] houses = houseTable.getItemIds().toArray();
        String inQuery = "(" + houses[0];
        for (int i = 0; i < houses.length; i++) {
            inQuery += "," + houses[i];
        }
        inQuery += ")";
        
        String SQL = " SELECT   a.name house_name, b.name room_name, c.actual_price " +
                    "   FROM   h4u_house a, h4u_room b, h4u_invoice c, h4u_contract d " +
                    "  WHERE       c.create_date > ? " +
                    "          AND c.create_date < ? " +
                    "          AND a.house_id = b.house_id " +
                    "          AND b.room_id = d.room_id " +
                    "          AND d.contract_id = c.contract_id " +
                    " 	 AND a.house_id in " + inQuery +
                    " ORDER BY house_name, room_name ";
        
        Calendar c = Calendar.getInstance();   // this takes current date
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date fromDate = c.getTime();
        c.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);        
        Date toDate = c.getTime();
        List lstParam = new ArrayList();
        lstParam.add(fromDate);
        lstParam.add(toDate);
        
        List<Map> lstRevenue = C3p0Connector.queryData(SQL, lstParam);
        
        SQL = " SELECT   a.cost_name, a.value, (SELECT   name " +
            "                                   FROM   h4u_house " +
            "                                  WHERE   house_id = a.house_id) " +
            "                                    house_name " +
            "   FROM   h4u_cost a " +
            "  WHERE    a.house_id in " + inQuery +
            " ORDER BY house_name, a.cost_type ";
        
        List<Map> lstCost = C3p0Connector.queryData(SQL);
        
        List<Object[]> lstData = new ArrayList();
        int count = 0;
        int costIndex = 0;
        int revenueIndex = 0;
        int totalRevenue = 0;
        int totalCost = 0;
        boolean runRevenue = true;
        boolean runCost = true;
        int totalRevenueAll = 0;
        int totalCostAll = 0;
        boolean first = true;
        
        while((revenueIndex < lstRevenue.size()) || (costIndex < lstCost.size())) {
            if(lstRevenue.size() > 0 && revenueIndex > 0 && revenueIndex < lstRevenue.size())
                runRevenue = lstRevenue.get(revenueIndex-1).get("house_name").equals(lstRevenue.get(revenueIndex).get("house_name"));
            else runRevenue = false;
            if(lstCost.size() > 0 && costIndex > 0 && costIndex < lstCost.size())
                runCost = lstCost.get(costIndex-1).get("house_name").equals(lstCost.get(costIndex).get("house_name"));
            else runCost = false;
            
            if(first) {
                first = false;
                runRevenue = true;
                runCost = true;
            }
            
            if(runRevenue && runCost) {
                count++;
                Object[] row = new Object[6];
                row[0] = count;
                row[1] = getRowValue(lstRevenue, revenueIndex, "house_name");
                row[2] = getRowValue(lstRevenue, revenueIndex, "room_name");
                if(getRowValue(lstRevenue, revenueIndex, "actual_price").isEmpty()) row[3] = 0;
                else row[3] = Integer.parseInt(getRowValue(lstRevenue, revenueIndex, "actual_price"));
                row[4] = getRowValue(lstCost, costIndex, "cost_name");
                if(getRowValue(lstCost, costIndex, "value").isEmpty()) row[5] = 0;
                else row[5] = Integer.parseInt(getRowValue(lstCost, costIndex, "value"));
                if(!getRowValue(lstRevenue, revenueIndex, "actual_price").isEmpty())
                    totalRevenue += Integer.parseInt(getRowValue(lstRevenue, revenueIndex, "actual_price"));
                if(!getRowValue(lstCost, costIndex, "value").isEmpty())
                    totalCost += Integer.parseInt(getRowValue(lstCost, costIndex, "value"));
                lstData.add(row);
                revenueIndex++;
                costIndex++;
            } else if(runRevenue) {
                count++;
                Object[] row = new Object[6];
                row[0] = count;
                row[1] = getRowValue(lstRevenue, revenueIndex, "house_name");
                row[2] = getRowValue(lstRevenue, revenueIndex, "room_name");
                if(getRowValue(lstRevenue, revenueIndex, "actual_price").isEmpty()) row[3] = 0;
                else row[3] = Integer.parseInt(getRowValue(lstRevenue, revenueIndex, "actual_price"));
                if(!getRowValue(lstRevenue, revenueIndex, "actual_price").isEmpty())
                    totalRevenue += Integer.parseInt(getRowValue(lstRevenue, revenueIndex, "actual_price"));
                row[4] = "";
                row[5] = "";
                lstData.add(row);
                revenueIndex++;
            } else if(runCost) {
                count++;
                Object[] row = new Object[6];
                row[0] = count;
                row[1] = getRowValue(lstCost, costIndex, "house_name");
                row[2] = "";
                row[3] = "";
                row[4] = getRowValue(lstCost, costIndex, "cost_name");
                if(getRowValue(lstCost, costIndex, "value").isEmpty()) row[5] = 0;
                else row[5] = Integer.parseInt(getRowValue(lstCost, costIndex, "value"));
                if(!getRowValue(lstCost, costIndex, "value").isEmpty())
                    totalCost += Integer.parseInt(getRowValue(lstCost, costIndex, "value"));
                lstData.add(row);
                costIndex++;
            } else {
                // ban ghi tong gia tien
                count++;
                Object[] rowt = new Object[6];
                rowt[0] = count;
                rowt[1] = "Tổng";
                rowt[2] = "";
                rowt[3] = totalRevenue;
                rowt[4] = "";
                rowt[5] = totalCost;
                lstData.add(rowt);
                
                totalRevenueAll += totalRevenue;
                totalCostAll += totalCost;
                totalRevenue = 0;
                totalCost = 0;  
                
                // Ban ghi phan cach hai nha
                Object[] rowSpace = new Object[6];
                lstData.add(rowSpace);
                
                // ban ghi tiep theo
                count++;
                Object[] row = new Object[6];
                row[0] = count;
                if (lstRevenue.size() > 0) {
                    row[1] = getRowValue(lstRevenue, revenueIndex, "house_name");
                    row[2] = getRowValue(lstRevenue, revenueIndex, "room_name");
                    if(getRowValue(lstRevenue, revenueIndex, "actual_price").isEmpty()) row[3] = 0;
                    else row[3] = Integer.parseInt(getRowValue(lstRevenue, revenueIndex, "actual_price"));
                } else {
                    row[1] = "";
                    row[2] = "";
                    row[3] = "";
                }
                if (lstCost.size() > 0) {
                    row[4] = getRowValue(lstCost, costIndex, "cost_name");
                    if(getRowValue(lstCost, costIndex, "value").isEmpty()) row[5] = 0;
                    else row[5] = Integer.parseInt(getRowValue(lstCost, costIndex, "value"));
                } else {
                    row[4] = "";
                    row[5] = "";
                }
                lstData.add(row);
                if (!getRowValue(lstRevenue, revenueIndex, "actual_price").isEmpty()) {
                    totalRevenue += Integer.parseInt(getRowValue(lstRevenue, revenueIndex, "actual_price"));
                }
                if (!getRowValue(lstCost, costIndex, "value").isEmpty()) {
                    totalCost += Integer.parseInt(getRowValue(lstCost, costIndex, "value"));
                }
                revenueIndex++;
                costIndex++;                
            }
        }
        
        // ban ghi tong gia tien
        count++;
        Object[] rowt = new Object[6];
        rowt[0] = count;
        rowt[1] = "Tổng";
        rowt[2] = "";
        rowt[3] = totalRevenue;
        rowt[4] = "";
        rowt[5] = totalCost;
        lstData.add(rowt); 
        
        totalRevenueAll += totalRevenue;
        totalCostAll += totalCost;
        
        // ban ghi tong gia tien tat ca cac nha
        count++;
        Object[] rowtAll = new Object[6];
        rowtAll[0] = count;
        rowtAll[1] = "TỔNG TẤT CẢ";
        rowtAll[2] = "";
        rowtAll[3] = totalRevenueAll;
        rowtAll[4] = "";
        rowtAll[5] = totalCostAll;
        lstData.add(rowtAll);         
        
        
        Object[][] arrData = new Object[lstData.size()+1][6];
        String[] row0 = {"","","","","",""};
        arrData[0] = row0;
        for(int i = 0; i < lstData.size(); i++) {
            arrData[i+1] = lstData.get(i);
        }
        
        String strTemplate = ResourceBundleUtils.getConfigureResource("FileBaseDirectory") + "Templates"
                + File.separator + "RevenueTemplate.xls";   
        Calendar cal = Calendar.getInstance();
        String fileName = "" + cal.get(Calendar.YEAR)
                + (cal.get(Calendar.MONTH) + 1) + cal.get(Calendar.DATE)
                + cal.get(Calendar.HOUR) + cal.get(Calendar.MINUTE) + cal.get(Calendar.SECOND)
                + "_" + "revenue";
        String encodeFileName = Base64Utils.encodeBytes(fileName.getBytes())
                + ".xls";        
        
        String strPath = "H4URevenueAction" + File.separator + encodeFileName;
        String strFile = ResourceBundleUtils.getConfigureResource("FileBaseDirectory") + strPath;
        
        List lstParamExport = new ArrayList();
        List lstRow = new ArrayList();
        lstRow.add("$name");
        lstRow.add(getComponent("name").getValue());
        lstParamExport.add(lstRow);
        FileUtils.exportExcelWithTemplate(arrData, strTemplate, strFile, 6, lstParamExport);
        
        List lstUpdateParam = new ArrayList();
        lstUpdateParam.add(strPath);
        lstUpdateParam.add(id);
        SQL = " update h4u_revenue set attach_file = ? where id = ? ";        
        C3p0Connector.excuteData(SQL, lstUpdateParam, connection);
    }

    public String getRowValue(List<Map> lstData, int index, String name) {
        String data = ""; 
        if(index < lstData.size() && lstData.get(index).get(name) != null) 
            data = lstData.get(index).get(name).toString();
        return data;
    }
}
