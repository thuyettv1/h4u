/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.handfate.industry.extend.action;

import com.handfate.industry.core.action.BaseAction;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @since 14/11/2014
 * @author HienDM1
 */
public class H4UInvoiceAction extends BaseAction {

    public TextField txtTotalCost = new TextField();

    /**
     * Hàm khởi tạo giao diện
     *
     * @since 19/11/2014 HienDM
     * @param localMainUI Vùng giao diện các chức năng
     * @return Giao diện sau khi khởi tạo
     */
    public HorizontalLayout init(UI localMainUI) throws Exception {
        //Khá»Ÿi táº¡o tham sá»‘
        setTableName("h4u_invoice");
        setTableName("v_h4u_invoice");
        setIdColumnName("invoice_id");
        setPageLength(25);
        setTableType(INT_PAGED_TABLE);
        setMainUI(localMainUI);
        setSortColumnName("invoice_id");
        setSortAscending(false);
        setSequenceName("h4u_invoice_seq");
        //ThÃªm cÃ¡c thÃ nh pháº§n
        addTextFieldToForm("InvoiceID", new TextField(), "invoice_id", "int", true, 50, null, null, false, false, null, false, null, true, true, true, true, null);

        //setComponentAsLoginUser("User.CreateUser", "create_user_id", false, null, false, null, false, true, new PopupSingleUserAction(localMainUI), null, null, INT_VIEWGROUP_ALL);
        // Them customer ID theo hop dong 
        addSinglePopupToForm("Khách hàng", "RECEIVE_USER_ID", "int", true, 50, null, null, true, null, false, null, true, true, true, true, new PopupSingleCustomerAction(localMainUI), 2,
                null, "", "user_id", "user_name", "sm_users", null, null);
        

        Object[][] invoiceStatus = {{2, "Invoice.Paid"}, {3, "Invoice.Cancel"}, {4, "Invoice.NoPay"}};
        addComboBoxToForm("Invoice.Status", new ComboBox(), "state", "int",
                true, 50, null, null, true, false, null, false, null, false, false, true, true, invoiceStatus, "1", "Invoice.Unpaid");
        addTextFieldToForm("Giá nhà", new TextField(), "price", "float", false, 18, null, null, false, false, null, false, null, true, true, false, false, null);
        addTextFieldToForm("Giá vệ sinh", new TextField(), "cleaning_price", "float", false, 18, null, null, false, false, null, false, null, true, true, false, false, null);
        addTextFieldToForm("Giá điện", new TextField(), "electric_price", "float", false, 18, null, null, false, false, null, false, null, true, true, false, false, null);
        addTextFieldToForm("Giá nước", new TextField(), "water_price", "float", false, 18, null, null, false, false, null, false, null, true, true, false, false, null);
        addTextFieldToForm("Giá internet", new TextField(), "internet_price", "float", false, 18, null, null, false, false, null, false, null, true, true, false, false, null);
        addTextFieldToForm("Giá TH cáp", new TextField(), "television_price", "float", false, 18, null, null, false, false, null, false, null, true, true, false, false, null);
        addTextFieldToForm("Tổng cộng", new TextField(), "total_price", "float", false, 18, null, null, false, false, null, false, null, true, false, false, false, null);
        addTextFieldToForm("Thực thu", new TextField(), "actual_price", "float", false, 18, null, null, false, false, null, false, null, true, true, true, true, null);
        

        // Set tham số tìm kiếm mặc định
        Date toDate = new Date();
        Date fromDate = new Date(toDate.getTime() - 60l * 24l * 3600l * 1000l);
        List lstParam = new ArrayList();
        lstParam.add(fromDate);
        lstParam.add(toDate);
        setComponentAsSysdate("User.CreateDate", "create_date", true, "date:and_mandatory:366", false, lstParam);

        return initPanel(2);
    }



    @Override
    public void afterAddData(Connection connection, long id) throws Exception {
        // cập nhật trạng thái "đã lập hóa đơn" cho giao dịch bán hàng 
//        Table table = getMultiComponent("bm_sale_transaction");
//        String cql = " update bm_sale_transaction "
//                + " set status = 2 "
//                + " where transaction_id = ?";
//        List<List> lstBatch = new ArrayList();
//
//        Object[] allItemId = table.getItemIds().toArray();
//        for (int n = 0; n < allItemId.length; n++) {
//            List<List> lstParameter = new ArrayList();
//            List lstRow1 = new ArrayList();
//            lstRow1.add(allItemId[n].toString());
//            lstRow1.add("long");
//            lstParameter.add(lstRow1);
//            lstBatch.add(lstParameter);
//        }
//        C3p0Connector.excuteDataByTypeBatch(cql, lstBatch, connection);
//        // anhptn: update tong gia tri hoa don - khi tao moi hoa don, count tu cac transaction
//        String sql = "update bm_invoice inv SET "
//                + " inv.cost = (SELECT SUM (tr.cost ) FROM bm_sale_transaction tr where tr.invoice_id = ?) "
//                + " where inv.invoice_id =?";
//        List<List> lstParameter = new ArrayList();
//        List lstRow1 = new ArrayList();
//        lstRow1.add(id);
//        lstRow1.add("long");
//        lstParameter.add(lstRow1);
//        lstParameter.add(lstRow1);
//        C3p0Connector.excuteDataByType(sql, lstParameter, connection);
//        // anhptn: cap nhat close = 0 cho hoa don moi tao
//        String udCloseSql = "update bm_invoice inv set inv.close = 0 where inv.invoice_id = ?";
//        List<List> lstInvPara = new ArrayList();
//        lstInvPara.add(lstRow1);
//        C3p0Connector.excuteDataByType(udCloseSql, lstInvPara, connection);
//        // anhptn: cap nhat status = 1 Hoa don chua thanh toan
//        String udStatusSql = "update bm_invoice inv set inv.status = 1 where inv.invoice_id = ?";
//        C3p0Connector.excuteDataByType(udStatusSql, lstInvPara, connection);
//
//        // Cập nhật history số hóa đơn
//        String sqlHis = " insert into bm_iv_num_HIS (id, owner, action, action_date, invoice_number_id, change_user) "
//                + " values (bm_iv_num_HIS_SEQ.nextval, "
//                + " (select owner from bm_iv_num_HIS where id = (select max(id) from bm_iv_num_HIS where id = ?)) , 2 , sysdate, ?, ?)";
//        List lstParamHis = new ArrayList();
//        List lstRow = new ArrayList();
//        lstRow.add(getComponent("invoice_number").getValue());
//        lstRow.add("long");
//        lstParamHis.add(lstRow);
//        List lstRowHis1 = new ArrayList();
//        lstRowHis1.add(getComponent("invoice_number").getValue());
//        lstRowHis1.add("long");
//        lstParamHis.add(lstRowHis1);
//        List lstRowHis2 = new ArrayList();
//        lstRowHis2.add(VaadinUtils.getSessionAttribute("G_UserId").toString());
//        lstRowHis2.add("long");
//        lstParamHis.add(lstRowHis2);
//        C3p0Connector.excuteDataByType(sqlHis, lstParamHis, connection);
//
//        String sql1 = " update bm_iv_num set status = 2 where id = ? ";
//        List lstParam = new ArrayList();
//        List lstRow2 = new ArrayList();
//        lstRow2.add(getComponent("invoice_number").getValue());
//        lstRow2.add("long");
//        lstParam.add(lstRow2);
//        C3p0Connector.excuteDataByType(sql1, lstParam, connection);
//        connection.commit();
//        updateDataRefreshData();
    }

    @Override
    public void afterEditData(Connection connection, long id) throws Exception {
        // cập nhật trạng thái "Chưa lập hóa đơn" cho giao dịch CŨ bán hàng
//        Object[] oldItemId = getMultiOldId("bm_sale_transaction");
//        String cql1 = " update bm_sale_transaction "
//                + " set status = 1 "
//                + " where transaction_id = ?";
//        List<List> lstBatch1 = new ArrayList();
//        for (int n = 0; n < oldItemId.length; n++) {
//            List<List> lstParameter = new ArrayList();
//            List lstRow1 = new ArrayList();
//            lstRow1.add(oldItemId[n].toString());
//            lstRow1.add("long");
//            lstParameter.add(lstRow1);
//            lstBatch1.add(lstParameter);
//        }
//        C3p0Connector.excuteDataByTypeBatch(cql1, lstBatch1, connection);
//
//        // cập nhật trạng thái "đã lập hóa đơn" cho giao dịch bán hàng MỚI
//        Table table = getMultiComponent("bm_sale_transaction");
//        String cql = " update bm_sale_transaction "
//                + " set status = 2 "
//                + " where transaction_id = ?";
//        List<List> lstBatch = new ArrayList();
//
//        Object[] allItemId = table.getItemIds().toArray();
//        for (int n = 0; n < allItemId.length; n++) {
//            List<List> lstParameter = new ArrayList();
//            List lstRow1 = new ArrayList();
//            lstRow1.add(allItemId[n].toString());
//            lstRow1.add("long");
//            lstParameter.add(lstRow1);
//            lstBatch.add(lstParameter);
//        }
//        C3p0Connector.excuteDataByTypeBatch(cql, lstBatch, connection);
//        // anhptn: cap nhat lai tong gia tri hoa don
//        String sql = "update bm_invoice inv SET "
//                + " inv.cost = (SELECT SUM (tr.cost ) FROM bm_sale_transaction tr where tr.invoice_id = ?) "
//                + " where inv.invoice_id =?";
//        List<List> lstParameter = new ArrayList();
//        List lstRow1 = new ArrayList();
//        lstRow1.add(id);
//        lstRow1.add("long");
//        lstParameter.add(lstRow1);
//        lstParameter.add(lstRow1);
//        C3p0Connector.excuteDataByType(sql, lstParameter, connection);
//
//        // Cập nhật history số hóa đơn
//        String sqlHis = " insert into bm_iv_num_HIS (id, owner, action, action_date, invoice_number_id, change_user) "
//                + " values (bm_iv_num_HIS_SEQ.nextval, "
//                + " (select owner from bm_iv_num_HIS where id = (select max(id) from bm_iv_num_HIS where invoice_number_id = ?)) , 2 , sysdate, ?, ?)";
//        List lstParamHis = new ArrayList();
//        List lstRow = new ArrayList();
//        lstRow.add(getComponent("invoice_number").getValue());
//        lstRow.add("long");
//        lstParamHis.add(lstRow);
//        List lstRowHis1 = new ArrayList();
//        lstRowHis1.add(getComponent("invoice_number").getValue());
//        lstRowHis1.add("long");
//        lstParamHis.add(lstRowHis1);
//        List lstRowHis2 = new ArrayList();
//        lstRowHis2.add(VaadinUtils.getSessionAttribute("G_UserId").toString());
//        lstRowHis2.add("long");
//        lstParamHis.add(lstRowHis2);
//        C3p0Connector.excuteDataByType(sqlHis, lstParamHis, connection);
//
//        String sql1 = " update bm_iv_num set status = 2 where id = ? ";
//        List lstParam = new ArrayList();
//        List lstRow2 = new ArrayList();
//        lstRow2.add(getComponent("invoice_number").getValue());
//        lstRow2.add("long");
//        lstParam.add(lstRow2);
//        C3p0Connector.excuteDataByType(sql1, lstParam, connection);
//
//        // Cập nhật history số hóa đơn cũ
//        String sqlInvoiceSelect = " select invoice_number from bm_invoice where invoice_id = ? ";
//        List lstParamInvoice2 = new ArrayList();
//        lstParamInvoice2.add(id);
//        List<Map> lstInvoiceNumber = C3p0Connector.queryData(sqlInvoiceSelect, lstParamInvoice2);
//
//        Object idInvoiceNumber = lstInvoiceNumber.get(0).get("invoice_number");
//        if (idInvoiceNumber != null) {
//            String sqlHisOldSelect = " select status from bm_iv_num where id = ? ";
//            List lstParam2 = new ArrayList();
//            lstParam2.add(Long.parseLong(idInvoiceNumber.toString()));
//            List<Map> lstStatus = C3p0Connector.queryData(sqlHisOldSelect, lstParam2);
//
//            if (lstStatus.get(0).get("status").toString().equals("2")) {
//                // trường hợp trạng thái đang lập hóa đơn thì xóa history
//                String sqlHis1 = " delete from bm_iv_num_HIS where id = (select max(id) from bm_iv_num_HIS where invoice_number_id = ?) ";
//                List lstParamHis1 = new ArrayList();
//                List lstRowH1 = new ArrayList();
//                lstRowH1.add(Long.parseLong(idInvoiceNumber.toString()));
//                lstRowH1.add("long");
//                lstParamHis1.add(lstRowH1);
//                C3p0Connector.excuteDataByType(sqlHis1, lstParamHis1, connection);
//
//                String sqlHis2 = " update bm_iv_num set status = 1 where id = ? ";
//                List lstParamHis2 = new ArrayList();
//                List lstRowH2 = new ArrayList();
//                lstRowH2.add(Long.parseLong(idInvoiceNumber.toString()));
//                lstRowH2.add("long");
//                lstParamHis2.add(lstRowH2);
//                C3p0Connector.excuteDataByType(sqlHis2, lstParamHis2, connection);
//            }
//        }
//        connection.commit();
//        updateDataRefreshData();
    }

    @Override
    public void beforeDeleteData(Connection connection, Object[] deleteArray) throws Exception {
        // cập nhật trạng thái "CHƯA lập hóa đơn" cho giao dịch bán hàng
//        String cql = " update bm_sale_transaction "
//                + " set status = 1 "
//                + " where invoice_id = ?";
//        List<List> lstBatch = new ArrayList();
//        for (int a = 0; a < deleteArray.length; a++) {
//            List<List> lstParameter = new ArrayList();
//            List lstRow1 = new ArrayList();
//            lstRow1.add(deleteArray[a].toString());
//            lstRow1.add("long");
//            lstParameter.add(lstRow1);
//            lstBatch.add(lstParameter);
//        }
//        C3p0Connector.excuteDataByTypeBatch(cql, lstBatch, connection);
//
//        for (int k = 0; k < deleteArray.length; k++) {
//            // Cập nhật history số hóa đơn cũ
//            String sqlInvoiceSelect = " select invoice_number from bm_invoice where invoice_id = ? ";
//            List lstParamInvoice2 = new ArrayList();
//            lstParamInvoice2.add(deleteArray[k]);
//            List<Map> lstInvoiceNumber = C3p0Connector.queryData(sqlInvoiceSelect, lstParamInvoice2);
//
//            Object idInvoiceNumber = lstInvoiceNumber.get(0).get("invoice_number");
//            if (idInvoiceNumber != null) {
//                String sqlHisOldSelect = " select status from bm_iv_num where id = ? ";
//                List lstParam2 = new ArrayList();
//                lstParam2.add(Long.parseLong(idInvoiceNumber.toString()));
//                List<Map> lstStatus = C3p0Connector.queryData(sqlHisOldSelect, lstParam2);
//
//                if (lstStatus.get(0).get("status").toString().equals("2")) {
//                    // trường hợp trạng thái đang lập hóa đơn thì xóa history
//                    String sqlHis1 = " delete bm_iv_num_HIS where id = (select max(id) from bm_iv_num_HIS where invoice_number_id = ?) ";
//                    List lstParamHis1 = new ArrayList();
//                    List lstRowH1 = new ArrayList();
//                    lstRowH1.add(Long.parseLong(idInvoiceNumber.toString()));
//                    lstRowH1.add("long");
//                    lstParamHis1.add(lstRowH1);
//                    C3p0Connector.excuteDataByType(sqlHis1, lstParamHis1, connection);
//
//                    String sqlHis2 = " update bm_iv_num set status = 1 where id = ? ";
//                    List lstParamHis2 = new ArrayList();
//                    List lstRowH2 = new ArrayList();
//                    lstRowH2.add(Long.parseLong(idInvoiceNumber.toString()));
//                    lstRowH2.add("long");
//                    lstParamHis2.add(lstRowH2);
//                    C3p0Connector.excuteDataByType(sqlHis2, lstParamHis2, connection);
//                }
//            }
//        }
    }

    // anhPTN: kiem tra trang thai hoa don co phai "da thanh toan" || "huy"
    private boolean isUsed(long id) throws Exception {
//        String sql = "select inv.status from bm_invoice inv where inv.invoice_id = ?";
//        List lstParameter = new ArrayList();
//        lstParameter.add(id);
//        List<Map> lStt = C3p0Connector.queryData(sql, lstParameter);
//        int stt = Integer.parseInt(String.valueOf(lStt.get(0).get("status")));
//        if (stt == 2 || stt == 3) {
//            return true;
//        }
//        return false;
    return true;
    }

    // anhptn: Kiem tra hoa don da dong chua
    private boolean isClosed(long invID) throws Exception {
//        String sql = "select inv.close from bm_invoice inv where inv.invoice_id = ?";
//        List invIDPara = new ArrayList();
//        invIDPara.add(invID);
//        List<Map> lClose = C3p0Connector.queryData(sql, invIDPara);
//        int close = Integer.parseInt(String.valueOf(lClose.get(0).get("close")));
//        if (close == 1) {
//            return true;
//        }
//        return false;
    return true;
    }

    @Override
    // anhptn
    public boolean validateEdit(long id) throws Exception {
        System.out.println("validate");
        
        // neu trang thai hoa don la "da thanh toan" || "huy" --> khong cho cap nhat
//        if (isUsed(id)) {
//            Notification.show(ResourceBundleUtils.getLanguageResource("Invoice.Error.StatusInvalid"),
//                    null, Notification.Type.ERROR_MESSAGE);
//            return false;
//        }
//        // Neu hoa don da dong ==> khong duoc sua bang ghi "chua thanh toan"
//        if (isClosed(id)) {
//            Notification.show(ResourceBundleUtils.getLanguageResource("Invoice.Error.Closed"),
//                    null, Notification.Type.ERROR_MESSAGE);
//            return false;
//        }
        return true;
    }

    @Override
    // anhptn
    public boolean validateDelete(Object[] selectedArray) throws Exception {
        // neu trang thai hoa don la "da thanh toan" || "huy" --> khong cho cap nhat
//        int i = 0, id = 0;
//        for (i = 0; i < selectedArray.length; i++) {
//            id = Integer.parseInt(selectedArray[i].toString());
//            if (isUsed(id)) {
//                Notification.show(ResourceBundleUtils.getLanguageResource("Invoice.Error.StatusInvalid"),
//                        null, Notification.Type.ERROR_MESSAGE);
//                return false;
//            }
//            // Neu hoa don da dong ==> khong duoc sua bang ghi "chua thanh toan"
//            if (isClosed(id)) {
//                Notification.show(ResourceBundleUtils.getLanguageResource("Invoice.Error.Closed"),
//                        null, Notification.Type.ERROR_MESSAGE);
//                return false;
//            }
//        }
        return true;
    }

    @Override
    public boolean prepareEdit() throws Exception {
//        try {
////            txtTotalCost.setValue(getComponent("cost").getValue().toString());
//            Object[] selectedArray = ((java.util.Collection) table.getValue()).toArray();
//            int id = Integer.parseInt(selectedArray[0].toString());
//            if (false) {
//                Notification.show(ResourceBundleUtils.getLanguageResource("Invoice.Error.StatusInvalid"),
//                        null, Notification.Type.ERROR_MESSAGE);
//                return false;
//            }
//            // Neu hoa don da dong ==> khong duoc sua bang ghi "chua thanh toan"
//            if (false) {
//                Notification.show(ResourceBundleUtils.getLanguageResource("Invoice.Error.Closed"),
//                        null, Notification.Type.ERROR_MESSAGE);
//                return false;
//            }
//        } catch (Exception ex) {
//            VaadinUtils.handleException(ex);
//            mainLogger.debug("Industry error: ", ex);
//        }
        return true;
    }

}
