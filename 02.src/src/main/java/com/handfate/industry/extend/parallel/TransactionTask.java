package com.handfate.industry.extend.parallel;

import com.handfate.industry.core.MainUI;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.handfate.industry.core.util.VaadinUtils;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

public class TransactionTask extends TimerTask {

    @Override
    public void run() {
        Calendar cal = Calendar.getInstance();
        Connection connection = null;
        try {
            System.out.println("Bat dau sinh giao dich tinh cuoc");
            String sql = " select code, value from bm_config ";
            List<Map> lstResult = C3p0Connector.queryData(sql);
            int dayTransaction = 27;
            for (int i = 0; i < lstResult.size(); i++) {
                if(lstResult.get(i).get("code") != null && 
                        lstResult.get(i).get("code").equals("INVOICE_CLOSE_DATE")) {
                    Object day = lstResult.get(i).get("value");
                    if(day != null) dayTransaction = Integer.parseInt(day.toString());
                }
            }
            if(cal.get(Calendar.DAY_OF_MONTH) == dayTransaction) {            
                connection = C3p0Connector.getInstance().getConnection();
                connection.setAutoCommit(false);

                List<Map> lstTran = new ArrayList();
                List<Map> lstTranOld = new ArrayList();
                lstTranOld = isTransaction();

                if (lstTranOld == null || lstTranOld.isEmpty()) {
                    lstTran = getListTransaction(0, 0, connection);
                    insertTransaction(lstTran, connection);
                    connection.commit();
                    System.out.println("Sinh giao dich tra sau");
                }
            }
        } catch (Exception ex) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex1) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex1);
                }
            }
            VaadinUtils.handleException(ex);
            MainUI.mainLogger.debug("Install error: ", ex);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    VaadinUtils.handleException(ex);
                    MainUI.mainLogger.debug("Install error: ", ex);
                }
            }
            connection = null;
        }
    }

    /**
     * Khi chuong trinh thuc hien chay, kiem tra giao dich thang truoc da duoc
     * sinh chua neu giao dich da duoc sinh thi tien trinh se ko chay tiep
     */
    public List<Map> isTransaction() throws Exception {
        String sql = "select * from bm_sale_transaction where TO_CHAR ( TO_DATE (from_date, 'dd/mm/yyyy'),'mm-yyyy') = TO_CHAR (TO_DATE (ADD_MONTHS (SYSDATE, -1),'dd/mm/yyyy'),  'mm-yyyy')\n"
                + "                and contract_id not in (select contract_id from bm_contract where status =5) and account_id is not null";
        return C3p0Connector.queryData(sql);
    }

    /**
     * Tim Danh sach cac account va hop dong dang hoat dong trong thang can xuat
     * hoat don
     */
    public List<Map> getListTransaction(long conID, int isCurrent, Connection con) throws Exception {
        String sql = "";
        // Tinh cuoc cho thang truoc
        if (isCurrent == 0) {
            if (conID == 0) {
                sql = "select ACCOUNT_ID, CONTRACT_ID, DURATION, to_date(FROM_DATE,'dd/mm/yyyy') from_date, to_Date(TO_DATE,'dd/mm/yyyy')to_date, IS_CHANGE_ACCOUNT, \n"
                        + "to_date(CHANGE_DATE_CONTRACT,'dd/mm/yyyy')CHANGE_DATE_CONTRACT, POLICY_ID_OLD, POLICY_ID_NEW, POLICY_ID, IS_CHANGE_POLICY, \n"
                        + "SERVICE_ID, POSTAGE_ID, POSTAGE_PRICE, DISCOUNT_POSTAGE, SERVICE_ID_NEW, \n"
                        + "POSTAGE_ID_NEW, POSTAGE_PRICE_NEW, DISCOUNT_POSTAGE_NEW, SERVICE_ID_OLD, \n"
                        + "POSTAGE_ID_OLD, POSTAGE_PRICE_OLD, DISCOUNT_POSTAGE_OLD from v_transaction_process ";
            } else {
                //  sql = "select * from v_transaction_process where CONTRACT_ID = ?";// + conID;
                // Kiem tra neu giao dich da sinh cho hop dong trong thang truoc thang hien tai roi thi xoa het cac giao dich di
                // if()
                String delTran = "delete bm_sale_transaction where contract_id = ? and account_id is not null ";
                List param = new ArrayList();
                param.add(conID);
                C3p0Connector.excuteData(delTran, param);
                con.commit();
                List<Map> lstTranOld = new ArrayList();
                lstTranOld = isTransaction();
                if (lstTranOld.size() > 0) {
                    sql = "select ACCOUNT_ID, CONTRACT_ID, DURATION, to_date(FROM_DATE,'dd/mm/yyyy') from_date, to_Date(TO_DATE,'dd/mm/yyyy')to_date, IS_CHANGE_ACCOUNT, \n"
                            + "to_date(CHANGE_DATE_CONTRACT,'dd/mm/yyyy')CHANGE_DATE_CONTRACT, POLICY_ID_OLD, POLICY_ID_NEW, POLICY_ID, IS_CHANGE_POLICY, \n"
                            + "SERVICE_ID, POSTAGE_ID, POSTAGE_PRICE, DISCOUNT_POSTAGE, SERVICE_ID_NEW, \n"
                            + "POSTAGE_ID_NEW, POSTAGE_PRICE_NEW, DISCOUNT_POSTAGE_NEW, SERVICE_ID_OLD, \n"
                            + "POSTAGE_ID_OLD, POSTAGE_PRICE_OLD, DISCOUNT_POSTAGE_OLD from v_transaction_process where CONTRACT_ID = ?";// + conID;
                }
            }
        } else {
            // tinh cuoc cho thang hien tai

            if (conID == 0) {
                sql = "select ACCOUNT_ID, CONTRACT_ID, DURATION, to_date(FROM_DATE,'dd/mm/yyyy') from_date, to_Date(TO_DATE,'dd/mm/yyyy')to_date, IS_CHANGE_ACCOUNT, \n"
                        + "to_date(CHANGE_DATE_CONTRACT,'dd/mm/yyyy')CHANGE_DATE_CONTRACT, POLICY_ID_OLD, POLICY_ID_NEW, POLICY_ID, IS_CHANGE_POLICY, \n"
                        + "SERVICE_ID, POSTAGE_ID, POSTAGE_PRICE, DISCOUNT_POSTAGE, SERVICE_ID_NEW, \n"
                        + "POSTAGE_ID_NEW, POSTAGE_PRICE_NEW, DISCOUNT_POSTAGE_NEW, SERVICE_ID_OLD, \n"
                        + "POSTAGE_ID_OLD, POSTAGE_PRICE_OLD, DISCOUNT_POSTAGE_OLD from v_transaction_current ";
            } else {
                sql = "select ACCOUNT_ID, CONTRACT_ID, DURATION, to_date(FROM_DATE,'dd/mm/yyyy') from_date, to_Date(TO_DATE,'dd/mm/yyyy')to_date, IS_CHANGE_ACCOUNT, \n"
                        + "to_date(CHANGE_DATE_CONTRACT,'dd/mm/yyyy')CHANGE_DATE_CONTRACT, POLICY_ID_OLD, POLICY_ID_NEW, POLICY_ID, IS_CHANGE_POLICY, \n"
                        + "SERVICE_ID, POSTAGE_ID, POSTAGE_PRICE, DISCOUNT_POSTAGE, SERVICE_ID_NEW, \n"
                        + "POSTAGE_ID_NEW, POSTAGE_PRICE_NEW, DISCOUNT_POSTAGE_NEW, SERVICE_ID_OLD, \n"
                        + "POSTAGE_ID_OLD, POSTAGE_PRICE_OLD, DISCOUNT_POSTAGE_OLD from v_transaction_current where CONTRACT_ID = ? ";// + conID;
            }
        }
        List<Map> lstResult = new ArrayList();
        List<Map> lstTemp = new ArrayList();
        List param = new ArrayList();
        param.add(conID);
        if (conID == 0) {
            lstTemp = C3p0Connector.queryData(sql);
        } else {
            lstTemp = C3p0Connector.queryData(sql, param);
        }
        for (int i = 0; i < lstTemp.size(); i++) {
            int isChangePolicy = Integer.parseInt(lstTemp.get(i).get("is_change_policy").toString());
            int isChangeAccount = Integer.parseInt(lstTemp.get(i).get("is_change_account").toString());
            Date fromDate = new Date(((java.sql.Timestamp) lstTemp.get(i).get("from_date")).getTime());
            Date toDate = new Date(((java.sql.Timestamp) lstTemp.get(i).get("to_date")).getTime());
            int totalDays = dayOfMonth(fromDate);
            if (isChangePolicy == 1) {
                //  Date dateChangePolicy = new Date(Date.valueOf(lstTemp.get(i).get("change_date_contract").toString()).getTime());
                Date dateChangePolicy = new Date(((java.sql.Timestamp) lstTemp.get(i).get("change_date_contract")).getTime());

//TH1: Chinh sach thay doi truoc khi accout chuyen sang hoat do  ng
                if (dateChangePolicy.compareTo(fromDate) < 0) {
                    Map transaction = new HashMap();
                    transaction.put("policy_id", lstTemp.get(i).get("policy_id_new"));
                    transaction.put("service_id", lstTemp.get(i).get("service_id_new"));
                    transaction.put("postage_id", lstTemp.get(i).get("postage_id_new"));
                    transaction.put("account_id", lstTemp.get(i).get("account_id"));
                    transaction.put("contract_id", lstTemp.get(i).get("contract_id"));
                    transaction.put("from_date", fromDate);
                    transaction.put("to_date", toDate);
                    int duration = Integer.parseInt(lstTemp.get(i).get("duration").toString());
                    int price = Integer.parseInt(lstTemp.get(i).get("postage_price_new").toString());
                    float discountPostage = Float.parseFloat(lstTemp.get(i).get("discount_postage_new").toString());
                    float totalCost = duration * price * (100 - discountPostage) / (totalDays * 100);
                    transaction.put("total_cost", totalCost);
                    if (totalCost > 0) {
                        lstResult.add(transaction);
                    }
                }

//TH2: Account thay doi sau khi account ngung hoat dong
                if (dateChangePolicy.compareTo(toDate) > 0) {
                    Map transaction = new HashMap();
                    transaction.put("policy_id", lstTemp.get(i).get("policy_id_old"));
                    transaction.put("service_id", lstTemp.get(i).get("service_id_old"));
                    transaction.put("postage_id", lstTemp.get(i).get("postage_id_old"));
                    transaction.put("account_id", lstTemp.get(i).get("account_id"));
                    transaction.put("contract_id", lstTemp.get(i).get("contract_id"));
                    transaction.put("from_date", fromDate);
                    transaction.put("to_date", toDate);

                    int duration = Integer.parseInt(lstTemp.get(i).get("duration").toString());
                    int price = Integer.parseInt(lstTemp.get(i).get("postage_price_old").toString());
                    float discountPostage = Float.parseFloat(lstTemp.get(i).get("discount_postage_old").toString());
                    float totalCost = duration * price * (100 - discountPostage) / (totalDays * 100);

                    transaction.put("total_cost", totalCost);
                    if (totalCost > 0) {
                        lstResult.add(transaction);
                    }
                }
                //Khach hang thay doi goi cuoc khi account dang hoat dong
                if (dateChangePolicy.compareTo(toDate) <= 0 && dateChangePolicy.compareTo(fromDate) >= 0) {
                    Map transaction = new HashMap();
                    transaction.put("policy_id", lstTemp.get(i).get("policy_id_old"));
                    transaction.put("service_id", lstTemp.get(i).get("service_id_old"));
                    transaction.put("postage_id", lstTemp.get(i).get("postage_id_old"));
                    transaction.put("account_id", lstTemp.get(i).get("account_id"));
                    transaction.put("contract_id", lstTemp.get(i).get("contract_id"));
                    transaction.put("from_date", lstTemp.get(i).get("from_date"));
                    transaction.put("to_date", lstTemp.get(i).get("change_date_contract"));

                    long duration = (long) (dateChangePolicy.getTime() - fromDate.getTime() + 24 * 60 * 60 * 1000) / 86400000;
                    int price = Integer.parseInt(lstTemp.get(i).get("postage_price_old").toString());
                    float discountPostage = Float.parseFloat(lstTemp.get(i).get("discount_postage_old").toString());
                    float totalCost = duration * price * (100 - discountPostage) / (totalDays * 100);
                    transaction.put("total_cost", totalCost);
                    if (totalCost > 0) {
                        lstResult.add(transaction);
                    }
                    Date fromDateNew = new Date(dateChangePolicy.getTime() + 24 * 60 * 60 * 1000);
                    int accountID = Integer.parseInt(lstTemp.get(i).get("account_id").toString());
                    /*
                     Map transactionNew = new HashMap();
                     transactionNew.put("policy_id", lstTemp.get(i).get("policy_id_new"));
                     transactionNew.put("account_id", lstTemp.get(i).get("account_id"));
                     transactionNew.put("contract_id", lstTemp.get(i).get("contract_id"));

                   
                     transactionNew.put("from_date", fromDateNew);
                     transactionNew.put("to_date", toDate);
                     transactionNew.put("service_id", lstTemp.get(i).get("service_id_new"));
                     transactionNew.put("postage_id", lstTemp.get(i).get("postage_id_new"));

                     long durationNew = (long) (toDate.getTime() - fromDateNew.getTime()+ 24 * 60 * 60 * 1000) / 86400000;
                     int priceNew = Integer.parseInt(lstTemp.get(i).get("postage_price_new").toString());
                     float discountPostageNew = Float.parseFloat(lstTemp.get(i).get("discount_postage_new").toString());
                     float totalCostNew = durationNew * priceNew * (100 - discountPostageNew) / (totalDays * 100);
                     transactionNew.put("total_cost", totalCostNew);
                     lstResult.add(transactionNew);
                     */
                    for (int j = i + 1; j < lstTemp.size(); j++) {
// Th: Hop dong thay doi chinh sach nhieu lan trong khoang thoi gian account hoat dong                        
                        int isChangePolicyNext = Integer.parseInt(lstTemp.get(j).get("is_change_policy").toString());
                        int accountIDNext = Integer.parseInt(lstTemp.get(j).get("account_id").toString());
                        if (isChangePolicyNext == 1 && accountID == accountIDNext) {
                            Date dateChangePolicyNext = new Date(((java.sql.Timestamp) lstTemp.get(j).get("change_date_contract")).getTime());
                            if (dateChangePolicyNext.compareTo(toDate) <= 0 && dateChangePolicyNext.compareTo(fromDateNew) >= 0) {
                                Map transactionNext = new HashMap();
                                transactionNext.put("policy_id", lstTemp.get(j).get("policy_id_old"));
                                transactionNext.put("service_id", lstTemp.get(j).get("service_id_old"));
                                transactionNext.put("postage_id", lstTemp.get(j).get("postage_id_old"));
                                transactionNext.put("account_id", lstTemp.get(j).get("account_id"));
                                transactionNext.put("contract_id", lstTemp.get(j).get("contract_id"));
                                transactionNext.put("from_date", fromDateNew);
                                transactionNext.put("to_date", lstTemp.get(j).get("change_date_contract"));

                                long durationNext = (long) (dateChangePolicyNext.getTime() - fromDateNew.getTime() + 24 * 60 * 60 * 1000) / 86400000;
                                int priceNext = Integer.parseInt(lstTemp.get(j).get("postage_price_old").toString());
                                float discountPostageNext = Float.parseFloat(lstTemp.get(j).get("discount_postage_old").toString());
                                float totalCostNext = durationNext * priceNext * (100 - discountPostageNext) / (totalDays * 100);
                                transactionNext.put("total_cost", totalCostNext);
                                if (totalCostNext > 0) {
                                    lstResult.add(transactionNext);
                                }
                                fromDateNew = new Date(dateChangePolicyNext.getTime() + 24 * 60 * 60 * 1000);
                                i = j;
                            }
//Th: ngay thay doi chinh sach trung voi ngay ket thuc hoat dong (fromdate tang them 1 ngay de tinh tient heo chinh sach moi nen dateChangePolicy < fromdateNew)                            
                            if (dateChangePolicyNext.compareTo(fromDateNew) < 0) {
                                i = j;
                            }
                        }
                    }
//Tinh tien cho khoang thoi gian sau khi thay doi chinh sach                    
                    Map transactionNew = new HashMap();
                    transactionNew.put("policy_id", lstTemp.get(i).get("policy_id_new"));
                    transactionNew.put("account_id", lstTemp.get(i).get("account_id"));
                    transactionNew.put("contract_id", lstTemp.get(i).get("contract_id"));

                    transactionNew.put("from_date", fromDateNew);
                    transactionNew.put("to_date", toDate);
                    transactionNew.put("service_id", lstTemp.get(i).get("service_id_new"));
                    transactionNew.put("postage_id", lstTemp.get(i).get("postage_id_new"));

                    long durationNew = (long) (toDate.getTime() - fromDateNew.getTime() + 24 * 60 * 60 * 1000) / 86400000;
                    int priceNew = Integer.parseInt(lstTemp.get(i).get("postage_price_new").toString());
                    float discountPostageNew = Float.parseFloat(lstTemp.get(i).get("discount_postage_new").toString());
                    float totalCostNew = durationNew * priceNew * (100 - discountPostageNew) / (totalDays * 100);
                    transactionNew.put("total_cost", totalCostNew);
                    if (totalCostNew > 0) {
                        lstResult.add(transactionNew);
                    }

                }
            } // Khach hang khong thay doi goi cuoc            
            else {
// Khach hang co thay doi thong tin accout (vi du: no cuoc account bi ngung hoat dong 1 thoi gian trong thang)
                if (isChangeAccount == 1) {
                    Map transaction = new HashMap();
                    transaction.put("policy_id", lstTemp.get(i).get("policy_id"));
                    transaction.put("account_id", lstTemp.get(i).get("account_id"));
                    transaction.put("contract_id", lstTemp.get(i).get("contract_id"));
                    transaction.put("from_date", fromDate);
                    transaction.put("to_date", toDate);
                    transaction.put("service_id", lstTemp.get(i).get("service_id"));
                    transaction.put("postage_id", lstTemp.get(i).get("postage_id"));
                    int duration = Integer.parseInt(lstTemp.get(i).get("duration").toString());
                    List<Map> lstAccountHis = isDuplicateChangeAccount(Long.parseLong(lstTemp.get(i).get("account_id").toString()));
                    if (lstAccountHis.size() > 0 && lstAccountHis != null) {
                        // duration = duration - 1;
                        Date changeDate = new Date(((java.sql.Timestamp) lstAccountHis.get(0).get("change_date_his")).getTime());
                        DateFormat shortDf = DateFormat.getDateInstance(DateFormat.SHORT);
                        String sChangeDate = shortDf.format(changeDate);
                        String sFromDate = shortDf.format(fromDate);
// Trường hợp account ngưng hoạt động và khôi phục trong cùng ngay, account bi tinh tien ngay thay doi 2 lan                        
                        if (sChangeDate.equalsIgnoreCase(sFromDate)) {
                            duration = duration - 1;
                        }

                    }
                    int price = Integer.parseInt(lstTemp.get(i).get("postage_price").toString());
                    float discountPostage = Float.parseFloat(lstTemp.get(i).get("discount_postage").toString());
                    float totalCost = duration * price * (100 - discountPostage) / (totalDays * 100);
                    transaction.put("total_cost", totalCost);
                    if (totalCost > 0) {
                        lstResult.add(transaction);
                    }
                } // Trong thang accout hoat dong binh thuong                
                else {
                    Map transaction = new HashMap();
                    transaction.put("policy_id", lstTemp.get(i).get("policy_id"));
                    transaction.put("account_id", lstTemp.get(i).get("account_id"));
                    transaction.put("contract_id", lstTemp.get(i).get("contract_id"));
                    transaction.put("from_date", fromDate);
                    transaction.put("to_date", toDate);
                    transaction.put("service_id", lstTemp.get(i).get("service_id"));
                    transaction.put("postage_id", lstTemp.get(i).get("postage_id"));
                    float discountPostage = Float.parseFloat(lstTemp.get(i).get("discount_postage").toString());
                    float totalCost = Float.parseFloat(lstTemp.get(i).get("postage_price").toString()) * (100 - discountPostage) / 100;;
                    transaction.put("total_cost", totalCost);
                    if (totalCost > 0) {
                        lstResult.add(transaction);
                    }
                }
            }
        }
        return lstResult;
    }

    public void insertTransaction(List<Map> lstTran, Connection connection) throws Exception {
        String sql = "insert into bm_sale_transaction (transaction_id,service_id,account_id,from_date,to_date,status,contract_id,postage_id,cost)\n"
                + "values(bm_sale_transaction_seq.nextval,?,?,?,?,1,?,?,?) ";
        List lstBatch = new ArrayList();
        for (int i = 0; i < lstTran.size(); i++) {
            List lstParameter = new ArrayList();
            List lstRow = new ArrayList();
            lstRow.add(lstTran.get(i).get("service_id").toString());
            lstRow.add("long");
            lstParameter.add(lstRow);

            List lstRow1 = new ArrayList();
            lstRow1.add(lstTran.get(i).get("account_id").toString());
            lstRow1.add("long");
            lstParameter.add(lstRow1);

            List lstRow2 = new ArrayList();
            lstRow2.add(lstTran.get(i).get("from_date"));
            lstRow2.add("date");
            lstParameter.add(lstRow2);

            List lstRow3 = new ArrayList();
            lstRow3.add(lstTran.get(i).get("to_date"));
            lstRow3.add("date");
            lstParameter.add(lstRow3);

            List lstRow4 = new ArrayList();
            lstRow4.add(lstTran.get(i).get("contract_id").toString());
            lstRow4.add("long");
            lstParameter.add(lstRow4);

            List lstRow5 = new ArrayList();
            lstRow5.add(lstTran.get(i).get("postage_id").toString());
            lstRow5.add("long");
            lstParameter.add(lstRow5);

            List lstRow6 = new ArrayList();
            lstRow6.add(lstTran.get(i).get("total_cost").toString());
            lstRow6.add("float");
            lstParameter.add(lstRow6);

            lstBatch.add(lstParameter);
        }
        C3p0Connector.excuteDataByTypeBatch(sql, lstBatch, connection);
    }

    public int dayOfMonth(Date date) {
        int totalDay = 0;
        int month = date.getMonth();
        int year = date.getYear() + 1900;
        System.out.println("Month: " + month);
        int[] daysInMonths = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        if (year % 4 == 0) {
            daysInMonths[1] = 29;   // Sử dụng phương thức kiểm tra năm nhuần ở trên
        }
        System.out.println("Số ngày trong tháng " + (month + 1) + " năm " + year + " : " + daysInMonths[month]);
        totalDay = daysInMonths[month];
        return totalDay;
    }

    /**
     * Kiểm tra xem trong 1 ngày account có thay đổi trạng thái từ hoạt động
     * sang ko hoạt động --> hoat động không. Nếu có trừ duration di 1 ngày
     */
    public List<Map> isDuplicateChangeAccount(long accountID) throws Exception {
        String sql = "select * from bm_account_his his1\n"
                + "where his1.status_new= 1 and his1.account_id in (select his2.account_id from bm_account_his his2 where to_date(his2.change_date_his,'dd/mm/yyyy') = to_date(his1.change_date_his,'dd/mm/yyyy') and his2.status_old = 1)\n"
                + "and his1.account_id = ?";
        List serPara = new ArrayList();
        serPara.add(accountID);
        List<Map> lstAccountHis = C3p0Connector.queryData(sql, serPara);
        return lstAccountHis;
    }
}
