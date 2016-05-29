package com.handfate.industry.extend.parallel;

import com.handfate.industry.core.MainUI;
import com.handfate.industry.core.oracle.C3p0Connector;
import static com.handfate.industry.core.oracle.C3p0Connector.setPreparedStatement;
import com.handfate.industry.extend.util.GetDateOld;
import com.handfate.industry.core.util.VaadinUtils;
import com.handfate.industry.extend.action.HomeAction;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

public class InvoiceTask extends TimerTask {

    @Override
    public void run() {
        Connection connection = null;
        
        try {
            MainUI.mainLogger.debug("Bat dau tien trinh cap nhat trang chu");
            connection = C3p0Connector.getInstance().getConnection();
            connection.setAutoCommit(false);
            insertHomepage(connection);
            connection.commit();
            String sql = "    SELECT charts, cost, code FROM bm_homepage order by charts ";
            HomeAction.lstHomePage = C3p0Connector.queryData(sql, new ArrayList());
            MainUI.mainLogger.debug("Ket thuc tien trinh cap nhat trang chu");
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
        
        try {
            MainUI.mainLogger.debug("Bat dau tien trinh chot hoa don");
            connection = C3p0Connector.getInstance().getConnection();
            connection.setAutoCommit(false);
            List<Map> check = new ArrayList();  
            check = isCheckInvoice();
            if (check!=null && check.size() > 0) {
                updateInvoice(connection);
                connection.commit();
            }
            MainUI.mainLogger.debug("Ket thuc tien trinh chot hoa don");
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
        
        try {
            MainUI.mainLogger.debug("Bat dau tien trinh tam ngung hop dong do no cuoc");
            connection = C3p0Connector.getInstance().getConnection();
            connection.setAutoCommit(false);
            List<Map> lstTran = new ArrayList();
            lstTran = isCheckLockInvoice();
            if (lstTran != null && lstTran.size() > 0) {
                updateAccount(lstTran,connection);
                connection.commit();
            }
            MainUI.mainLogger.debug("Ket thuc tien trinh tam ngung hop dong do no cuoc");
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
        
        try {
            MainUI.mainLogger.debug("Bat dau tien trinh khoa hop dong het han hoac tam ngung lau");
            connection = C3p0Connector.getInstance().getConnection();
            connection.setAutoCommit(false);
            List<Map> lstContractExprie = listContractExprie();
            String desExprie = "Hợp đồng hết hạn";
            if (lstContractExprie != null && lstContractExprie.size() > 0) {
                updateContractAccount(lstContractExprie, connection, desExprie);
                connection.commit();
                MainUI.mainLogger.debug("Hop dong het han");
            }
            List<Map> lstContractPause = listContractPause();
            String desPause = "Hợp đồng tạm ngưng quá thời hạn khôi phục";
            if (lstContractPause != null && lstContractPause.size() > 0) {
                updateContractAccountPause(lstContractPause, connection, desPause);
                connection.commit();
                MainUI.mainLogger.debug("Khoa hop dong do tam ngung qua lau hoac het han");
            }
            MainUI.mainLogger.debug("Ket thuc tien trinh khoa hop dong het han hoac tam ngung lau");
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
        
        try {
            MainUI.mainLogger.debug("Bat dau chuyen hoa don tu chua thanh toan sang no cuoc theo cau hinh");
            connection = C3p0Connector.getInstance().getConnection();
            connection.setAutoCommit(false);
            updateInvoiceChange(connection);
            connection.commit();
            MainUI.mainLogger.debug("Ket thuc chuyen hoa don tu chua thanh toan sang no cuoc theo cau hinh");
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
     *Kiem tra trong bang cau hinh, neu ngay hien tai la ngay dong hoa don
     */
    public List<Map> isCheckInvoice() throws Exception {
        String sql = "select * from bm_config where code = 'INVOICE_CLOSE_DATE' and value = to_char(sysdate,'dd')";
        return C3p0Connector.queryData(sql);
    }

    public void updateInvoice(Connection connection) throws Exception {
        String sql = "update bm_invoice set close = 1 where TO_CHAR ( TO_DATE (create_date, 'dd/mm/yyyy'),'mm-yyyy') = TO_CHAR (TO_DATE (ADD_MONTHS (SYSDATE, -1),'dd/mm/yyyy'),  'mm-yyyy')";          
        PreparedStatement preparedStatement = null;
        try {           
            preparedStatement = connection.prepareStatement(sql);        
            preparedStatement.executeUpdate();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
           
        }
    }

    public List<Map> isCheckLockInvoice() throws Exception {
        String sql = "select * from bm_sale_transaction where account_id is not null and invoice_id in (select invoice_id from bm_invoice where round((sysdate - create_date),0) >= (select value from bm_config where code = 'DAYS_FOR_DEBT') and status = 4)\n"
                + " and contract_id in (select contract_id from bm_contract where status =2)";
        List<Map> lstTran = new ArrayList();
// Lay danh sach cac giao dich can co account can ngung dich vu        
        lstTran = C3p0Connector.queryData(sql);
        return lstTran;
    }

    public void updateAccount(List<Map> lstTran, Connection connection) throws Exception {
        String sql1 = "insert into bm_account_his(id_his, account_id,status_old,cause_change_his,change_date_his,status_new,change_date_old,contract_id) values ("
                + "                            bm_account_his_seq.nextval,?,1,?,sysdate,3,?,?) ";
        String sql2 = "update bm_account set status = 3 where account_id = ?";
        String sql3 = "update bm_contract set status = 4 where contract_id = ?";
        String sql4 = "insert into bm_contract_his(id_his,contract_id,cause_change_his,change_date_his,status_old,type_change,status_new,change_date_old) values ("
                + "bm_contract_his_seq.nextval,?,?,sysdate,2,4,4,?)";
        // String sql = "select * from bm_sale_transaction where account_id is not null and invoice_id in (select invoice_id from bm_invoice where round((sysdate - create_date),0) >= (select value from bm_config where code = 'DAYS_FOR_DEBT') and status = 4)";
        List lstBatch1 = new ArrayList();
        List lstBatch2 = new ArrayList();
        List lstBatch3 = new ArrayList();
        List lstBatch4 = new ArrayList();
        // List<Map> lstTran = new ArrayList();
// Lay danh sach cac giao dich can co account can ngung dich vu        
        // lstTran = C3p0Connector.queryData(sql);

        for (int i = 0; i < lstTran.size(); i++) {
            long accountID = Long.parseLong(lstTran.get(i).get("account_id").toString());
            long contractID = Long.parseLong(lstTran.get(i).get("contract_id").toString());
            Date dateChangeOldAccount = GetDateOld.getChangeDateOldAccount(accountID);
            Date dateChangeOldContract = GetDateOld.getChangeDateOldContract(contractID);
            // Date systemDate = new Date();
            String des = "Ngưng do nợ cước";

            List lstParameter1 = new ArrayList();
            List lstParameter2 = new ArrayList();
            List lstParameter3 = new ArrayList();
            List lstParameter4 = new ArrayList();

            List lstRow1 = new ArrayList();
            lstRow1.add(accountID);
            lstRow1.add("long");

            List lstRow2 = new ArrayList();
            lstRow2.add(des);
            lstRow2.add("string");

            List lstRow4 = new ArrayList();
            lstRow4.add(dateChangeOldAccount);
            lstRow4.add("date");

            List lstRow5 = new ArrayList();
            lstRow5.add(contractID);
            lstRow5.add("long");

            List lstRow6 = new ArrayList();
            lstRow6.add(dateChangeOldContract);
            lstRow6.add("date");

            lstParameter1.add(lstRow1);
            lstParameter1.add(lstRow2);
            //      lstParameter1.add(lstRow3);
            lstParameter1.add(lstRow4);
            lstParameter1.add(lstRow5);

            lstParameter2.add(lstRow1);
            lstParameter3.add(lstRow5);

// Cau SQL 4 them du lieu vao bang contract_his               
            lstParameter4.add(lstRow5);
            lstParameter4.add(lstRow2);
            //    lstParameter4.add(lstRow3);
            lstParameter4.add(lstRow6);

            lstBatch1.add(lstParameter1);
            lstBatch2.add(lstParameter2);
            lstBatch3.add(lstParameter3);
            lstBatch4.add(lstParameter4);
        }

        C3p0Connector.excuteDataByTypeBatch(sql1, lstBatch1, connection);
        C3p0Connector.excuteDataByTypeBatch(sql2, lstBatch2, connection);
        C3p0Connector.excuteDataByTypeBatch(sql3, lstBatch3, connection);
        C3p0Connector.excuteDataByTypeBatch(sql4, lstBatch4, connection);
    }
    
    public List<Map> listContractExprie() throws Exception {
        String sql = "select * from bm_contract con where  TO_DATE (expire_date, 'dd/mm/yyyy') <= TO_DATE (SYSDATE,'dd/mm/yyyy')\n"
                + "and status=2";

        List<Map> lstTran = new ArrayList();
        // Lay danh sach cac giao dich can co account can ngung dich vu        
        lstTran = C3p0Connector.queryData(sql);
        return lstTran;

    }

    public List<Map> listContractPause() throws Exception {
        String sql = "select * from bm_contract where status = 4 and contract_id in (select distinct contract_id from bm_contract_his having (SYSDATE - max(change_date_his)) > (select value from bm_config where code = 'MAX_PAUSE_DAY')  group by contract_id )";

        List<Map> lstTran = new ArrayList();
        // Lay danh sach cac giao dich can co account can ngung dich vu        
        lstTran = C3p0Connector.queryData(sql);
        return lstTran;
    }

    /**
     * Khi chuong trinh thuc hien chay, kiem tra giao dich thang truoc da duoc
     * sinh chua neu giao dich da duoc sinh thi tien trinh se ko chay tiep
     */
    public void updateContractAccount(List<Map> lstTran, Connection connection, String des) throws Exception {

        String sqlUpdateContract = "update bm_contract a set a.status = 3 where a.contract_id = ?";
        String sqlInsertContractHis = "insert into bm_contract_his(id_his,contract_id,cause_change_his,change_date_his,status_old,type_change,status_new,change_date_old)"
                + " values(bm_contract_his_seq.nextval,?,?,sysdate,?,3,3,?)";
        String sqlUpdateAccount = "update bm_account a set a.status = 4 where a.contract_id = ?";
        String sqlInsertAccountHis = "insert into bm_account_his(id_his, account_id,status_old,cause_change_his,change_date_his,status_new,change_date_old,contract_id)"
                + " values (bm_account_his_seq.nextval,?,?,?,sysdate,4,?,?) ";
        List lstBatch1 = new ArrayList();
        List lstBatch2 = new ArrayList();
        List lstBatchAcc = new ArrayList();
        //  String des = "Hết hạn hợp đồng";
//        String sql = "select * from bm_contract con where  TO_DATE (expire_date, 'dd/mm/yyyy') = TO_DATE (SYSDATE,'dd/mm/yyyy')";
//
//        List<Map> lstTran = new ArrayList();
//        // Lay danh sach cac giao dich can co account can ngung dich vu        
//        lstTran = C3p0Connector.queryData(sql);
        for (int i = 0; i < lstTran.size(); i++) {
            List lstParameter1 = new ArrayList();
            List lstParameter2 = new ArrayList();

            long contractID = Long.parseLong(lstTran.get(i).get("contract_id").toString());
            List ConID = new ArrayList();
            ConID.add(contractID);
            ConID.add("long");

            lstParameter1.add(ConID);
            lstParameter2.add(ConID);
            lstBatch1.add(lstParameter1);

            List reasonChange = new ArrayList();
            reasonChange.add(des);
            reasonChange.add("string");
            lstParameter2.add(reasonChange);

            List contractStatus = new ArrayList();
            contractStatus.add(lstTran.get(i).get("status").toString());
            contractStatus.add("int");
            lstParameter2.add(contractStatus);

            Date dateChangeOldContract = GetDateOld.getChangeDateOldContract(contractID);
            List dateChangeOld = new ArrayList();
            dateChangeOld.add(dateChangeOldContract);
            dateChangeOld.add("date");

            lstParameter2.add(dateChangeOld);

            lstBatch2.add(lstParameter2);
            List<Map> lstAccID = getAccountID(contractID);
            for (int j = 0; j < lstAccID.size(); j++) {
                long accountID = Long.parseLong(lstAccID.get(j).get("account_id").toString());
                int accountStatus = Integer.parseInt(lstAccID.get(j).get("status").toString());
                List lstAccParameter = new ArrayList();

                List IDAcc = new ArrayList();
                IDAcc.add(accountID);
                IDAcc.add("long");
                lstAccParameter.add(IDAcc);

                List accStatus = new ArrayList();
                accStatus.add(accountStatus);
                accStatus.add("int");
                lstAccParameter.add(accStatus);

                lstAccParameter.add(reasonChange);

                Date dateChangeOldAccount = GetDateOld.getChangeDateOldAccount(accountID);
                List dateAccChangeOld = new ArrayList();
                dateAccChangeOld.add(dateChangeOldAccount);
                dateAccChangeOld.add("date");
                lstAccParameter.add(dateAccChangeOld);
                lstAccParameter.add(ConID);
                lstBatchAcc.add(lstAccParameter);

            }

        }
// Thao tac voi Contract
        C3p0Connector.excuteDataByTypeBatch(sqlUpdateContract, lstBatch1, connection);
        C3p0Connector.excuteDataByTypeBatch(sqlInsertContractHis, lstBatch2, connection);
// Thao tac voi account                                
        C3p0Connector.excuteDataByTypeBatch(sqlUpdateAccount, lstBatch1, connection);
        C3p0Connector.excuteDataByTypeBatch(sqlInsertAccountHis, lstBatchAcc, connection);

    }

    public void updateContractAccountPause(List<Map> lstTran, Connection connection, String des) throws Exception {

        String sqlUpdateContract = "update bm_contract a set a.status = 6 where a.contract_id = ?";
        String sqlInsertContractHis = "insert into bm_contract_his(id_his,contract_id,cause_change_his,change_date_his,status_old,type_change,status_new,change_date_old)"
                + " values(bm_contract_his_seq.nextval,?,?,sysdate,?,3,6,?)";
        String sqlUpdateAccount = "update bm_account a set a.status = 4 where a.contract_id = ?";
        String sqlInsertAccountHis = "insert into bm_account_his(id_his, account_id,status_old,cause_change_his,change_date_his,status_new,change_date_old,contract_id)"
                + " values (bm_account_his_seq.nextval,?,?,?,sysdate,4,?,?) ";
        List lstBatch1 = new ArrayList();
        List lstBatch2 = new ArrayList();
        List lstBatchAcc = new ArrayList();
        //  String des = "Hết hạn hợp đồng";
//        String sql = "select * from bm_contract con where  TO_DATE (expire_date, 'dd/mm/yyyy') = TO_DATE (SYSDATE,'dd/mm/yyyy')";
//
//        List<Map> lstTran = new ArrayList();
//        // Lay danh sach cac giao dich can co account can ngung dich vu        
//        lstTran = C3p0Connector.queryData(sql);
        for (int i = 0; i < lstTran.size(); i++) {
            List lstParameter1 = new ArrayList();
            List lstParameter2 = new ArrayList();

            long contractID = Long.parseLong(lstTran.get(i).get("contract_id").toString());
            List ConID = new ArrayList();
            ConID.add(contractID);
            ConID.add("long");

            lstParameter1.add(ConID);
            lstParameter2.add(ConID);
            lstBatch1.add(lstParameter1);

            List reasonChange = new ArrayList();
            reasonChange.add(des);
            reasonChange.add("string");
            lstParameter2.add(reasonChange);

            List contractStatus = new ArrayList();
            contractStatus.add(lstTran.get(i).get("status").toString());
            contractStatus.add("int");
            lstParameter2.add(contractStatus);

            Date dateChangeOldContract = GetDateOld.getChangeDateOldContract(contractID);
            List dateChangeOld = new ArrayList();
            dateChangeOld.add(dateChangeOldContract);
            dateChangeOld.add("date");

            lstParameter2.add(dateChangeOld);

            lstBatch2.add(lstParameter2);
            List<Map> lstAccID = getAccountID(contractID);
            for (int j = 0; j < lstAccID.size(); j++) {
                long accountID = Long.parseLong(lstAccID.get(j).get("account_id").toString());
                int accountStatus = Integer.parseInt(lstAccID.get(j).get("status").toString());
                List lstAccParameter = new ArrayList();

                List IDAcc = new ArrayList();
                IDAcc.add(accountID);
                IDAcc.add("long");
                lstAccParameter.add(IDAcc);

                List accStatus = new ArrayList();
                accStatus.add(accountStatus);
                accStatus.add("int");
                lstAccParameter.add(accStatus);

                lstAccParameter.add(reasonChange);

                Date dateChangeOldAccount = GetDateOld.getChangeDateOldAccount(accountID);
                List dateAccChangeOld = new ArrayList();
                dateAccChangeOld.add(dateChangeOldAccount);
                dateAccChangeOld.add("date");
                lstAccParameter.add(dateAccChangeOld);
                lstAccParameter.add(ConID);
                lstBatchAcc.add(lstAccParameter);

            }

        }
// Thao tac voi Contract
        C3p0Connector.excuteDataByTypeBatch(sqlUpdateContract, lstBatch1, connection);
        C3p0Connector.excuteDataByTypeBatch(sqlInsertContractHis, lstBatch2, connection);
// Thao tac voi account                                
        C3p0Connector.excuteDataByTypeBatch(sqlUpdateAccount, lstBatch1, connection);
        C3p0Connector.excuteDataByTypeBatch(sqlInsertAccountHis, lstBatchAcc, connection);

    }

    public List<Map> getAccountID(long contractID) throws Exception {
        String sql = "select account_id,status from bm_account where contract_id = ?";
        List IDParameter = new ArrayList();
        IDParameter.add(contractID);
        List<Map> LAccID = C3p0Connector.queryData(sql, IDParameter);
        return LAccID;
    }

    /**
     * Cập nhật trạng thái từ chưa thanh toán sang nợ cước, nếu như hóa đơn chưa
     * được thanh toán quá thời gian config
     *
     * @param connection
     * @throws Exception
     */
    public void updateInvoiceChange(Connection connection) throws Exception {
        String sql = "update bm_invoice set status = 4 where status =1 and (sysdate - create_date)>= (select value from bm_config where code = 'DAYS_FOR_NO_PAY')";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }

        }

    }
    
    public void insertHomepage(Connection connection) throws Exception {
        String sqlDelte = "delete bm_homepage";
        C3p0Connector.excuteData(sqlDelte, new ArrayList(), connection);
        
        String sql = " INSERT INTO bm_homepage (charts, cost, code)   "
                + "  (SELECT   1 charts, SUM (cost) cost, (SELECT   code "
                + "                                           FROM   bm_service "
                + "                                          WHERE   service_id = st.service_id) "
                + "                                            code "
                + "     FROM   bm_sale_transaction st "
                + "    WHERE   status = 3 AND TO_CHAR (from_date, 'MM/yyyy') = ? "
                + " GROUP BY   service_id "
                + " UNION "
                + "   SELECT   2 charts, SUM (cost) cost, group_code code "
                + "     FROM   (SELECT   cost, "
                + "                      (SELECT   group_code "
                + "                         FROM   sm_group "
                + "                        WHERE   GROUP_ID = (SELECT   GROUP_ID "
                + "                                              FROM   bm_service "
                + "                                             WHERE   service_id = st.service_id)) "
                + "                          group_code "
                + "               FROM   bm_sale_transaction st "
                + "              WHERE   status = 3 AND TO_CHAR (from_date, 'MM/yyyy') = ?) "
                + " GROUP BY   group_code "
                + " UNION "
                + " SELECT   3 charts, SUM (cost), TO_CHAR(?) code "
                + "   FROM   bm_sale_transaction "
                + "  WHERE   status = 3 AND TO_CHAR (from_date, 'MM/yyyy') = ? "
                + " UNION "
                + " SELECT   3 charts, SUM (cost), TO_CHAR(?) code "
                + "   FROM   bm_sale_transaction "
                + "  WHERE   status = 3 AND TO_CHAR (from_date, 'MM/yyyy') = ? "
                + " UNION "
                + " SELECT   3 charts, SUM (cost), TO_CHAR(?) code "
                + "   FROM   bm_sale_transaction "
                + "  WHERE   status = 3 AND TO_CHAR (from_date, 'MM/yyyy') = ? ) ";

        SimpleDateFormat formatter = new SimpleDateFormat("MM/yyyy");
        Date currentDate = new Date();
        String currentMonth = formatter.format(currentDate);
        Calendar cal = Calendar.getInstance();
        Integer month = cal.get(Calendar.MONTH) + 1;
        Integer year = cal.get(Calendar.YEAR);
        String dateMinus1 = "";
        String dateMinus2 = "";
        if (month == 1) {
            dateMinus1 = "12/" + (year - 1);
            dateMinus2 = "11/" + (year - 1);
        } else if (month == 2) {
            dateMinus1 = "01/" + year;
            dateMinus2 = "12/" + (year - 1);
        } else {
            Integer monthMinus1 = month - 1;
            Integer monthMinus2 = month - 2;
            if (monthMinus1.toString().length() == 1) {
                dateMinus1 = "0" + monthMinus1 + "/" + year;
            } else {
                dateMinus1 = monthMinus1.toString() + "/" + year;
            }
            if (monthMinus2.toString().length() == 1) {
                dateMinus2 = "0" + monthMinus2 + "/" + year;
            } else {
                dateMinus2 = monthMinus2.toString() + "/" + year;
            }
        }

        List lstParameter = new ArrayList();
        lstParameter.add(currentMonth);
        lstParameter.add(currentMonth);
        lstParameter.add(currentMonth);
        lstParameter.add(currentMonth);
        lstParameter.add(dateMinus1);
        lstParameter.add(dateMinus1);
        lstParameter.add(dateMinus2);
        lstParameter.add(dateMinus2);
        C3p0Connector.excuteData(sql, lstParameter, connection);
    }    
}
