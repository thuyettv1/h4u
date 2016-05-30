package com.handfate.industry.extend.parallel;

import java.util.Calendar;
import java.util.Timer;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author YenNTH8
 */
public class Scheduler implements ServletContextListener {

    Timer timerTransactioin;
    Timer timerDaily;

    @SuppressWarnings("static-access")
    public void contextDestroyed(ServletContextEvent arg0) {
        try {
            timerTransactioin.cancel();
            timerDaily.cancel();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            (new TransactionTask()).run();
            (new InvoiceTask()).run();
            timerTransactioin = new Timer();
            timerDaily = new Timer();
            Calendar date = Calendar.getInstance();
            // Thiết lập thời gian bắt đầu: là chủ nhật đầu tiên
            date.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            date.set(Calendar.HOUR, 0);
            date.set(Calendar.MINUTE, 0);
            date.set(Calendar.SECOND, 0);
            date.set(Calendar.MILLISECOND, 0);
            
            timerTransactioin.schedule(new TransactionTask(), date.getTime(), 1000l * 3600l * 24l);
            timerDaily.schedule(new InvoiceTask(), 1000l * 3600l * 24l);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
