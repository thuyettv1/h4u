package com.handfate.industry.extend.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author HienDM
 */
public class DateTimeUtils {
    
    /**
     * Hàm lấy ngày đầu tiên trong tháng hiện tại
     *
     * @since 08/02/2015 HienDM
     * @return Ngày đầu tiên trong tháng hiện tại
     */
    public static Date getFirstDayInCurrentMonth() throws Exception{
        Calendar cal = Calendar.getInstance();
        Integer month = cal.get(Calendar.MONTH) + 1;
        Integer year = cal.get(Calendar.YEAR);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date firstDay;
        if (month.toString().length() == 1) {
            firstDay = formatter.parse("01/0" + month + "/" + year);
        } else {
            firstDay = formatter.parse("01/" + month + "/" + year);
        }
        return firstDay;
    }
    
    /**
     * Hàm lấy ngày đầu tiên trong tháng hiện tại
     *
     * @since 08/02/2015 HienDM
     * @return Ngày đầu tiên trong tháng hiện tại
     */
    public static Date getLastDayInCurrentMonth() throws Exception { 
        Calendar cal = Calendar.getInstance();
        Integer month = cal.get(Calendar.MONTH) + 1;
        Integer year = cal.get(Calendar.YEAR);
        Integer lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date firstDay;
        if (month.toString().length() == 1) {
            firstDay = formatter.parse("" + lastDay + "/0" + month + "/" + year);
        } else {
            firstDay = formatter.parse("" + lastDay + "/" + month + "/" + year);
        }
        return firstDay;        
    }
}
