/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.handfate.industry.extend.util;

import com.handfate.industry.core.oracle.C3p0Connector;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author YenNTH8
 */
public class GetDateOld {
    public static Date getChangeDateOldContract(long contractID) throws Exception {

        String sql = "select change_date_his from bm_contract_his where status_new  = 2 and contract_id = ? order by change_date_his desc";
        List<Map> lstTemp = new ArrayList();
        Date dateChange = null;
        List lstParameter = new ArrayList();

        lstParameter.add(contractID);

        lstParameter.add(lstParameter);

        lstTemp = C3p0Connector.queryData(sql, lstParameter);
        if (lstTemp != null && lstTemp.size() > 0) {
            dateChange = new Date(((java.sql.Timestamp) lstTemp.get(0).get("change_date_his")).getTime());
        } else {
            String cql = "select sign_date from bm_contract where contract_id =?";
            lstTemp = C3p0Connector.queryData(cql, lstParameter);
            if (lstTemp != null && lstTemp.size() > 0) {
                dateChange = new Date(((java.sql.Timestamp) lstTemp.get(0).get("sign_date")).getTime());
            }
        }
        return dateChange;
    }

    public static Date getChangeDateOldAccount(long accountID) throws Exception {

        String sql = "select change_date_his from bm_account_his where account_id = ? and status_new = 1 order by change_date_his desc ";
        List<Map> lstTemp = new ArrayList();
        Date dateChange = null;
        List lstParameter = new ArrayList();

        lstParameter.add(accountID);

        lstTemp = C3p0Connector.queryData(sql, lstParameter);
        if (lstTemp != null && lstTemp.size() > 0) {
            dateChange = new Date(((java.sql.Timestamp) lstTemp.get(0).get("change_date_his")).getTime());
        } else {
            String cql = "select start_date from bm_account where account_id = ? ";
            lstTemp = C3p0Connector.queryData(cql, lstParameter);
            if (lstTemp != null && lstTemp.size() > 0) {
                dateChange = new Date(((java.sql.Timestamp) lstTemp.get(0).get("start_date")).getTime());
            }
        }
        return dateChange;
    }
}
