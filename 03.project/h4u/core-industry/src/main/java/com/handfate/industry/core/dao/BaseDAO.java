package com.handfate.industry.core.dao;

import com.handfate.industry.core.action.BaseAction;
import static com.handfate.industry.core.action.BaseAction.INT_COMPONENT;
import static com.handfate.industry.core.action.BaseAction.INT_LABEL;
import static com.handfate.industry.core.action.BaseAction.INT_TO_DATE_COMPONENT;
import com.handfate.industry.core.action.PopupMultiAction;
import com.handfate.industry.core.action.component.MultiUploadField;
import com.handfate.industry.core.action.component.UploadField;
import com.handfate.industry.core.oracle.C3p0Connector;
import com.handfate.industry.core.util.EncryptDecryptUtils;
import com.handfate.industry.core.util.VaadinUtils;
import com.vaadin.data.Item;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @since 11/03/2014
 * @author HienDM
 */
public class BaseDAO {

    /**
     * Hàm tìm kiếm dữ liệu
     *
     * @param lstComponent danh sách thông tin
     * @param tableName tên bảng dữ liệu
     * @param listParameter danh sách tham số
     * @return danh sách dữ liệu
     * @since 14/12/2014 HienDM
     */
    public List<Map> searchData(List<List> lstComponent,
            String tableName, List listParameter) throws Exception {
        String cql = " select " + lstComponent.get(0).get(BaseAction.INT_DB_FIELD_NAME);
        for (int i = 1; i < lstComponent.size(); i++) {
            cql += ", " + lstComponent.get(i).get(BaseAction.INT_DB_FIELD_NAME);
        }
        cql += " from " + tableName;
        return C3p0Connector.queryData(cql);
    }

    /**
     * Hàm thêm dữ liệu
     *
     * @param lstComponent danh sách thông tin
     * @param tableName tên bảng dữ liệu
     * @param idField vị trí cột khoá chính
     * @param sequenceName tên sequence
     * @param connection kết nối tới cơ sở dữ liệu
     * @return dữ liệu khóa chính sau khi thêm mới
     * @since 14/11/2014 HienDM
     */
    public long insertData(List<List> lstComponent, String tableName, int idField, String sequenceName, Connection connection) throws Exception {
        List lstParameter = new ArrayList();
        String cql = " insert into " + tableName + " (" + lstComponent.get(idField).get(BaseAction.INT_DB_FIELD_NAME);
        int count = 0;
        for (int i = 0; i < lstComponent.size(); i++) {
            // Bỏ qua nếu là multiUpload
            if (!(lstComponent.get(i).get(BaseAction.INT_COMPONENT) instanceof MultiUploadField)) // Bỏ qua nếu trường này chỉ có trong view, không có trong table
            {
                if (!(lstComponent.get(i).get(BaseAction.INT_FORMAT) != null && lstComponent.get(i).get(BaseAction.INT_FORMAT).equals("OnlyView"))) {
                    if (i != idField) {
                        List lstRow = new ArrayList();
                        Object data;
                        if (lstComponent.get(i).get(BaseAction.INT_COMPONENT) instanceof UploadField) {
                            data = lstComponent.get(i).get(BaseAction.INT_FILE_PATH);
                        } else {
                            data = ((AbstractField) lstComponent.get(i).get(BaseAction.INT_COMPONENT)).getValue();
                        }
                        if (lstComponent.get(i).size() > BaseAction.INT_SYSDATE
                                && lstComponent.get(i).get(BaseAction.INT_SYSDATE) instanceof Boolean
                                && lstComponent.get(i).get(INT_TO_DATE_COMPONENT) instanceof PopupDateField) {
                            data = new java.util.Date();
                            lstRow.add(data);
                            lstRow.add(lstComponent.get(i).get(BaseAction.INT_DATA_TYPE));
                            lstParameter.add(lstRow);
                            cql += ", " + lstComponent.get(i).get(BaseAction.INT_DB_FIELD_NAME);
                            ((PopupDateField) lstComponent.get(i).get(BaseAction.INT_COMPONENT)).setValue((Date) data);
                            count++;
                        } else if (lstComponent.get(i).size() > BaseAction.INT_LOGINUSER
                                && lstComponent.get(i).get(BaseAction.INT_LOGINUSER) != null
                                && lstComponent.get(i).get(BaseAction.INT_LOGINUSER).equals("LoginUser")) {
                            data = VaadinUtils.getSessionAttribute("G_UserId").toString();
                            lstRow.add(data);
                            lstRow.add("long");
                            lstParameter.add(lstRow);
                            cql += ", " + lstComponent.get(i).get(BaseAction.INT_DB_FIELD_NAME);
                            ComboBox cbo = (ComboBox) lstComponent.get(i).get(BaseAction.INT_COMPONENT);
                            if (cbo.containsId(data)) {
                                cbo.setValue(data);
                            } else {
                                cbo.addItem(data);
                                cbo.setItemCaption(data, VaadinUtils.getSessionAttribute("G_UserName").toString());
                                cbo.setValue(data);
                            }
                            count++;
                        } else if (data != null && !data.toString().trim().equals("")) {
                            if ((lstComponent.get(i).get(BaseAction.INT_COMPONENT) instanceof PasswordField)
                                    && (boolean) lstComponent.get(i).get(BaseAction.INT_IS_PASSWORD)) {
                                EncryptDecryptUtils edu = new EncryptDecryptUtils();
                                data = edu.encodePassword(data.toString().trim());
                            }
                            if (lstComponent.get(i).get(BaseAction.INT_DATA_TYPE).equals("boolean")) {
                                if (data.toString().equals("true")) {
                                    data = "1";
                                }
                                if (data.toString().equals("false")) {
                                    data = "0";
                                }
                            }
                            if (lstComponent.get(i).get(BaseAction.INT_DATA_TYPE).equals("date")) {
                                data = ((PopupDateField) lstComponent.get(i).get(BaseAction.INT_COMPONENT)).
                                        getValue();
                            }
                            lstRow.add(data);
                            lstRow.add(lstComponent.get(i).get(BaseAction.INT_DATA_TYPE));
                            lstParameter.add(lstRow);
                            cql += ", " + lstComponent.get(i).get(BaseAction.INT_DB_FIELD_NAME);
                            count++;
                        }
                    }
                }
            }
        }
        long id = C3p0Connector.getSequenceValue(sequenceName);
        cql += ") values (" + id;
        for (int i = 0; i < count; i++) {
            cql += ",?";
        }
        cql += ") ";
        C3p0Connector.excuteDataByType(cql, lstParameter, connection);
        return id;
    }

    /**
     * Hàm thêm dữ liệu
     *
     * @param lstComponentMulti danh sách thông tin
     * @param id khoá chính của đối tượng cha
     * @param connection kết nối tới cơ sở dữ liệu
     * @since 14/11/2014 HienDM
     */
    public void insertDataAttach(List<List> lstComponentMulti, long id, Connection connection) throws Exception {
        for (int k = 0; k < lstComponentMulti.size(); k++) {
            List lstComponent = lstComponentMulti.get(k);
            if (lstComponent.get(BaseAction.INT_MULTI_IDFIELD) != null) {
                String cql = " insert into " + lstComponent.get(BaseAction.INT_MULTI_ATTACHTABLE) + " ("
                        + lstComponent.get(BaseAction.INT_MULTI_IDFIELD) + ","
                        + lstComponent.get(BaseAction.INT_MULTI_IDCONNECT) + ","
                        + lstComponent.get(BaseAction.INT_MULTI_IDPOPUP);
                int count = 0;
                List<List> lstAttach = (List) lstComponent.get(BaseAction.INT_MULTI_ATTACH);
                if (lstAttach != null && !lstAttach.isEmpty()) {
                    for (int i = 0; i < lstAttach.size(); i++) {
                        cql += ", " + lstAttach.get(i).get(BaseAction.INT_DB_FIELD_NAME);
                        count++;
                    }
                }
                cql += ") values (" + lstComponent.get(BaseAction.INT_MULTI_SEQUENCE) + ".nextval";
                for (int i = 0; i < count + 2; i++) {
                    cql += ",?";
                }
                cql += ") ";

                List<List> lstBatch = new ArrayList();
                Table table = (Table) lstComponent.get(BaseAction.INT_MULTI_TABLE);
                Object[] allItemId = table.getItemIds().toArray();
                for (int n = 0; n < allItemId.length; n++) {
                    List<List> lstParameter = new ArrayList();
                    List lstRow = new ArrayList();
                    lstRow.add(id);
                    lstRow.add("long");
                    lstParameter.add(lstRow);
                    List lstRow1 = new ArrayList();
                    lstRow1.add(allItemId[n].toString());
                    lstRow1.add("long");
                    lstParameter.add(lstRow1);
                    if (lstAttach != null && !lstAttach.isEmpty()) {
                        for (int m = 0; m < lstAttach.size(); m++) {
                            List lstRow2 = new ArrayList();
                            Item data = table.getItem(allItemId[n]);
                            HorizontalLayout txtLayout = ((HorizontalLayout) data.getItemProperty(
                                    ((Label) lstAttach.get(m).get(INT_LABEL)).getValue()).getValue());
                            Object cellData = ((TextField) txtLayout.getComponent(0)).getValue();
                            if (cellData != null) {
                                lstRow2.add(cellData.toString());
                            } else {
                                lstRow2.add("");
                            }
                            lstRow2.add(lstAttach.get(m).get(BaseAction.INT_DATA_TYPE));
                            lstParameter.add(lstRow2);
                        }
                    }
                    lstBatch.add(lstParameter);
                }
                C3p0Connector.excuteDataByTypeBatch(cql, lstBatch, connection);
            } else {
                String cql = " update " + lstComponent.get(BaseAction.INT_MULTI_TABLENAME)
                        + " set " + lstComponent.get(BaseAction.INT_MULTI_IDCONNECT) + " = ? "
                        + " where " + lstComponent.get(BaseAction.INT_MULTI_IDPOPUP) + " = ? ";
                List<List> lstBatch = new ArrayList();
                Table table = (Table) lstComponent.get(BaseAction.INT_MULTI_TABLE);
                Object[] allItemId = table.getItemIds().toArray();
                for (int n = 0; n < allItemId.length; n++) {
                    List<List> lstParameter = new ArrayList();
                    List lstRow = new ArrayList();
                    lstRow.add(id);
                    lstRow.add("long");
                    lstParameter.add(lstRow);
                    List lstRow1 = new ArrayList();
                    lstRow1.add(allItemId[n].toString());
                    lstRow1.add("long");
                    lstParameter.add(lstRow1);
                    lstBatch.add(lstParameter);
                }
                C3p0Connector.excuteDataByTypeBatch(cql, lstBatch, connection);
            }
        }
    }

    /**
     * Hàm xóa dữ liệu đi kèm
     *
     * @param lstComponentMulti danh sách thông tin
     * @param id khoá chính của đối tượng cha
     * @param connection kết nối tới cơ sở dữ liệu
     * @since 14/11/2014 HienDM
     */
    public void deleteDataAttach(List<List> lstComponentMulti, long id, Connection connection) throws Exception {
        for (int k = 0; k < lstComponentMulti.size(); k++) {
            List lstComponent = lstComponentMulti.get(k);
            List<List> lstBatch = new ArrayList();
            String cql = "";
            Object[] allItemId;
            String cqlAttach = " select " + lstComponent.get(BaseAction.INT_MULTI_IDPOPUP)
                    + " from " + lstComponent.get(BaseAction.INT_MULTI_ATTACHTABLE)
                    + " where " + lstComponent.get(BaseAction.INT_MULTI_IDCONNECT)
                    + " = ? ";
            List lstParamAttach = new ArrayList();
            lstParamAttach.add(id);
            List<Map> lstAttachId = C3p0Connector.queryData(cqlAttach, lstParamAttach);
            allItemId = new Object[lstAttachId.size()];
            for (int a = 0; a < lstAttachId.size(); a++) {
                allItemId[a] = lstAttachId.get(a).get(lstComponent.get(BaseAction.INT_MULTI_IDPOPUP));
            }

            if (lstComponent.size() > BaseAction.INT_MULTI_OLDIDS
                    && lstComponent.get(BaseAction.INT_MULTI_OLDIDS) != null) {
                lstComponent.set(BaseAction.INT_MULTI_OLDIDS, allItemId);
            } else if (lstComponent.size() == BaseAction.INT_MULTI_OLDIDS) {
                lstComponent.add(allItemId);
            }

            if (lstComponent.get(BaseAction.INT_MULTI_IDFIELD) != null) {
                cql = " delete from " + lstComponent.get(BaseAction.INT_MULTI_ATTACHTABLE)
                        + " where " + lstComponent.get(BaseAction.INT_MULTI_IDCONNECT) + " = ? and "
                        + lstComponent.get(BaseAction.INT_MULTI_IDPOPUP) + " = ? ";
                for (int i = 0; i < allItemId.length; i++) {
                    List<List> lstParameter = new ArrayList();
                    List lstRow = new ArrayList();
                    lstRow.add(id);
                    lstRow.add("long");
                    lstParameter.add(lstRow);
                    List lstRow1 = new ArrayList();
                    lstRow1.add(allItemId[i]);
                    lstRow1.add("long");
                    lstParameter.add(lstRow1);
                    lstBatch.add(lstParameter);
                }
            } else {
                cql = " update " + lstComponent.get(BaseAction.INT_MULTI_ATTACHTABLE)
                        + " set " + lstComponent.get(BaseAction.INT_MULTI_IDCONNECT) + " = null"
                        + " where " + lstComponent.get(BaseAction.INT_MULTI_IDPOPUP) + " = ? ";
                for (int i = 0; i < allItemId.length; i++) {
                    List<List> lstParameter = new ArrayList();
                    List lstRow1 = new ArrayList();
                    lstRow1.add(allItemId[i]);
                    lstRow1.add("long");
                    lstParameter.add(lstRow1);
                    lstBatch.add(lstParameter);
                }
            }
            C3p0Connector.excuteDataByTypeBatch(cql, lstBatch, connection);
        }
    }

    /**
     * Hàm thêm dữ liệu
     *
     * @param lstComponent danh sách thông tin
     * @param tableName tên bảng dữ liệu
     * @param idField khóa chính
     * @param connection kết nối tới cơ sở dữ liệu
     * @since 14/11/2014 HienDM
     */
    public void updateData(List<List> lstComponent, String tableName, int idField, Connection connection) throws Exception {
        List lstParameter = new ArrayList();
        String cql = " update " + tableName + " set ";
        boolean firstLoop = true;
        for (int i = 0; i < lstComponent.size(); i++) {
            // Bỏ quả nếu là multiUpload
            if (lstComponent.get(i).get(BaseAction.INT_COMPONENT) instanceof MultiUploadField) {
                continue;
            }
            // Bỏ qua nếu là sysdate
            if (lstComponent.get(i).size() > BaseAction.INT_SYSDATE
                    && lstComponent.get(i).get(BaseAction.INT_SYSDATE) instanceof Boolean
                    && lstComponent.get(i).get(INT_TO_DATE_COMPONENT) instanceof PopupDateField) {
                continue;
            }
            // Bỏ qua nếu là login user
            if (lstComponent.get(i).size() > BaseAction.INT_LOGINUSER
                    && lstComponent.get(i).get(BaseAction.INT_LOGINUSER) != null
                    && lstComponent.get(i).get(BaseAction.INT_LOGINUSER).equals("LoginUser")) {
                continue;
            }
            // Bỏ qua nếu trường này chỉ có trong view, không có trong table
            if (lstComponent.get(i).get(BaseAction.INT_FORMAT) != null
                    && lstComponent.get(i).get(BaseAction.INT_FORMAT).equals("OnlyView")) {
                continue;
            }
            if (i != idField) {
                List lstRow = new ArrayList();
                Object data = new Object();
                boolean checkFileChange = true;
                if (lstComponent.get(i).get(BaseAction.INT_COMPONENT) instanceof UploadField) {
                    if (lstComponent.get(i).size() > BaseAction.INT_FILE_EDIT) {
                        if (lstComponent.get(i).get(BaseAction.INT_FILE_EDIT) != null
                                && lstComponent.get(i).get(BaseAction.INT_FILE_PATH).toString().isEmpty()) {
                            checkFileChange = false;
                        } else {
                            data = lstComponent.get(i).get(BaseAction.INT_FILE_PATH);
                        }
                    }
                } else {
                    data = ((AbstractField) lstComponent.get(i).get(BaseAction.INT_COMPONENT)).getValue();
                }
                if (!((lstComponent.get(i).get(BaseAction.INT_COMPONENT) instanceof PasswordField)
                        && ((boolean) lstComponent.get(i).get(BaseAction.INT_IS_PASSWORD))
                        && data.toString().trim().equals(""))) {
                    if (checkFileChange) {
                        if ((lstComponent.get(i).get(BaseAction.INT_COMPONENT) instanceof PasswordField)
                                && (boolean) lstComponent.get(i).get(BaseAction.INT_IS_PASSWORD)) {
                            EncryptDecryptUtils edu = new EncryptDecryptUtils();
                            data = edu.encodePassword(data.toString().trim());
                        }
                        if (lstComponent.get(i).get(BaseAction.INT_DATA_TYPE).equals("boolean")) {
                            if (data.toString().equals("true")) {
                                data = "1";
                            }
                            if (data.toString().equals("false")) {
                                data = "0";
                            }
                        }
                        if (lstComponent.get(i).get(BaseAction.INT_DATA_TYPE).equals("date")) {
                            data = ((PopupDateField) lstComponent.get(i).get(BaseAction.INT_COMPONENT)).
                                    getValue();
                        }
                        lstRow.add(data);
                        lstRow.add(lstComponent.get(i).get(BaseAction.INT_DATA_TYPE));
                        lstParameter.add(lstRow);
                        if (firstLoop) {
                            cql += lstComponent.get(i).get(BaseAction.INT_DB_FIELD_NAME) + " = ? ";
                        } else {
                            cql += ", " + lstComponent.get(i).get(BaseAction.INT_DB_FIELD_NAME) + " = ? ";
                        }
                        firstLoop = false;
                    }
                }
            }
        }
        cql += " where " + lstComponent.get(idField).get(BaseAction.INT_DB_FIELD_NAME) + " = ?";
        List idRow = new ArrayList();
        idRow.add(((AbstractField) lstComponent.get(idField).get(BaseAction.INT_COMPONENT)).getValue());
        idRow.add(lstComponent.get(idField).get(BaseAction.INT_DATA_TYPE));
        lstParameter.add(idRow);
        C3p0Connector.excuteDataByType(cql, lstParameter, connection);
    }

    /**
     * Hàm lấy tất cả dữ liệu
     *
     * @param lstComponent danh sách thông tin
     * @param tableName tên bảng dữ liệu
     * @param whereCondition điều kiện where
     * @param lstWhereParameter tham số điều kiện where
     * @return danh sách dữ liệu trả về
     * @since 14/11/2014 HienDM
     */
    public List<Map> selectAllData(List<List> lstComponent, String tableName,
            String whereCondition, List lstWhereParameter) throws Exception {
        boolean checkWhereCondition = false;
        String cql = " select " + lstComponent.get(0).get(BaseAction.INT_DB_FIELD_NAME);
        for (int i = 1; i < lstComponent.size(); i++) {
            if (!(lstComponent.get(i).get(BaseAction.INT_COMPONENT) instanceof MultiUploadField)) {
                if ((lstComponent.get(i).size() > BaseAction.INT_POPUP_BUTTON)
                        && (lstComponent.get(i).get(BaseAction.INT_POPUP_BUTTON) instanceof Button)) {
                    String aliasTableName = lstComponent.get(i).get(BaseAction.INT_COMBOBOX_TABLENAME).toString();
                    String aliasDbFieldName = lstComponent.get(i).get(BaseAction.INT_DB_FIELD_NAME).toString();
                    if (aliasTableName.length() > 10) {
                        aliasTableName = aliasTableName.substring(0, 10);
                    }
                    if (aliasDbFieldName.length() > 10) {
                        aliasDbFieldName = aliasDbFieldName.substring(0, 10);
                    }

                    cql += ", " + lstComponent.get(i).get(BaseAction.INT_DB_FIELD_NAME);
                    cql += ", " + "(select " + lstComponent.get(i).get(BaseAction.INT_COMBOBOX_NAMECOLUMN) + " from "
                            + lstComponent.get(i).get(BaseAction.INT_COMBOBOX_TABLENAME) + " "
                            + aliasTableName
                            + aliasDbFieldName
                            + " where "
                            + aliasTableName
                            + aliasDbFieldName + "."
                            + lstComponent.get(i).get(BaseAction.INT_COMBOBOX_IDCOLUMN) + " = "
                            + tableName + "." + lstComponent.get(i).get(BaseAction.INT_DB_FIELD_NAME) + ") "
                            + aliasTableName
                            + aliasDbFieldName;
                } else {
                    cql += ", " + lstComponent.get(i).get(BaseAction.INT_DB_FIELD_NAME);
                }
            }
            // Kiểm tra nếu bắt buộc nhập điều kiện tìm kiếm để giới hạn dữ liệu lấy từ DB thì thêm mệnh đề where
            if (lstComponent.get(i).get(BaseAction.INT_SEARCH_MANDATORY) != null) {
                checkWhereCondition = true;
            }
        }
        cql += " from " + tableName;
        List lstParameter = new ArrayList();
        if (checkWhereCondition || !whereCondition.isEmpty()) {
            //Thêm mệnh đề where
            cql += " where 1 = 1 ";
            if (checkWhereCondition) {
                for (int i = 0; i < lstComponent.size(); i++) {
                    if (lstComponent.get(i).size() > BaseAction.INT_LOGINUSER
                            && lstComponent.get(i).get(BaseAction.INT_LOGINUSER) != null
                            && lstComponent.get(i).get(BaseAction.INT_LOGINUSER).equals("LoginUser")) {
                        if ((boolean) lstComponent.get(i).get(BaseAction.INT_LOGINUSER_ONLYVIEW)) {
                            cql += " and " + lstComponent.get(i).get(BaseAction.INT_DB_FIELD_NAME) + " = ? ";
                            lstParameter.add(Long.parseLong(VaadinUtils.getSessionAttribute("G_UserId").toString()));
                        } else if (lstComponent.get(i).get(BaseAction.INT_LOGINUSER_ONLYVIEWGROUP).equals(1)) {
                            cql += " and " + lstComponent.get(i).get(BaseAction.INT_DB_FIELD_NAME)
                                    + " in (select user_id from sm_users where group_id = ?) ";
                            lstParameter.add(Long.parseLong(VaadinUtils.getSessionAttribute("G_UserId").toString()));
                        } else if (lstComponent.get(i).get(BaseAction.INT_LOGINUSER_ONLYVIEWGROUP).equals(2)) {
                            cql += " and " + lstComponent.get(i).get(BaseAction.INT_DB_FIELD_NAME)
                                    + " in (select user_id from sm_users where group_id in "
                                    + "(select group_id from v_group where path like ?)) ";
                            lstParameter.add("%/" + VaadinUtils.getSessionAttribute("G_GroupId").toString() + "/%");
                        }
                    }
                    if (lstComponent.get(i).get(BaseAction.INT_SEARCH_MANDATORY) != null) {
                        String format = lstComponent.get(i).get(BaseAction.INT_SEARCH_MANDATORY).toString();
                        if (format.split(":")[0].equals("date")) {  // Kiểu thời gian
                            PopupDateField pdfFrom = (PopupDateField) lstComponent.get(i).get(BaseAction.INT_COMPONENT);
                            PopupDateField pdfTo = (PopupDateField) lstComponent.get(i).get(BaseAction.INT_TO_DATE_COMPONENT);
                            SimpleDateFormat formatterTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            if (pdfFrom.getValue() != null) {
                                Date fromValue = new Date();
                                if (pdfFrom.getResolution().equals(Resolution.DAY)) {
                                    fromValue = formatterTime.parse(formatter.format(pdfFrom.getValue()) + " 00:00:00");
                                } else {
                                    fromValue = new Date(pdfFrom.getValue().getTime());
                                }
                                fromValue.setTime(fromValue.getTime() - 1000);
                                cql += " and " + lstComponent.get(i).get(BaseAction.INT_DB_FIELD_NAME) + " >= ? ";
                                lstParameter.add(fromValue);
                            }
                            if (pdfTo.getValue() != null) {
                                Date toValue = new Date();
                                if (pdfFrom.getResolution().equals(Resolution.DAY)) {
                                    toValue = formatterTime.parse(formatter.format(pdfTo.getValue()) + " 23:59:59");
                                } else {
                                    toValue = new Date(pdfTo.getValue().getTime());
                                }
                                toValue.setTime(toValue.getTime() + 1000);
                                cql += " and " + lstComponent.get(i).get(BaseAction.INT_DB_FIELD_NAME) + " <= ? ";
                                lstParameter.add(toValue);
                            }
                        } else // Kiểu số nguyên (id)
                         if (!(lstComponent.get(i).get(BaseAction.INT_COMPONENT) instanceof UploadField
                                    || lstComponent.get(i).get(BaseAction.INT_COMPONENT) instanceof MultiUploadField)) {
                                if (((AbstractField) lstComponent.get(i).get(BaseAction.INT_COMPONENT)).getValue() != null) {
                                    cql += " and " + lstComponent.get(i).get(BaseAction.INT_DB_FIELD_NAME) + " = ? ";
                                    lstParameter.add(((AbstractField) lstComponent.get(i).get(BaseAction.INT_COMPONENT)).getValue());
                                }
                            }
                    }
                }
            }
            if (!whereCondition.isEmpty()) {
                cql += whereCondition;
                if (lstWhereParameter != null) {
                    for (int i = 0; i < lstWhereParameter.size(); i++) {
                        lstParameter.add(lstWhereParameter.get(i));
                    }
                }
            }
        }
        if (checkWhereCondition || !whereCondition.isEmpty()) {
            return C3p0Connector.queryData(cql, lstParameter);
        } else {
            return C3p0Connector.queryData(cql);
        }
    }

    /**
     * Hàm lấy dữ liệu theo câu lệnh truy vấn
     *
     * @param cql câu lệnh truy vấn
     * @return danh sách dữ liệu trả về
     * @since 14/11/2014 HienDM
     */
    public List<Map> selectAllData(String cql) throws Exception {
        return C3p0Connector.queryData(cql);
    }

    /**
     * Hàm thêm dữ liệu
     *
     * @param tableName tên bảng dữ liệu
     * @param idColumnName khóa chính
     * @param deleteArray mảng dữ liệu cần xóa
     * @param connection kết nối tới cơ sở dữ liệu
     * @since 14/11/2014 HienDM
     */
    public void deleteData(String tableName, String idColumnName, Object[] deleteArray, Connection connection) throws Exception {
        List lstBatch = new ArrayList();
        for (int i = 0; i < deleteArray.length; i++) {
            List lstParameter = new ArrayList();
            List lstRow = new ArrayList();
            lstRow.add(deleteArray[i]);
            lstRow.add("int");
            lstParameter.add(lstRow);
            lstBatch.add(lstParameter);
        }
        String cql = "delete from " + tableName + " where " + idColumnName + " = ? ";
        C3p0Connector.excuteDataByTypeBatch(cql, lstBatch, connection);
    }

    /**
     * Hàm lấy dữ liệu theo câu lệnh truy vấn
     *
     * @param cql câu lệnh truy vấn
     * @param userName tên đăng nhập
     * @return danh sách dữ liệu trả về
     * @since 14/11/2014 HienDM
     */
    public List<Map> selectUser(String cql, String userName) throws Exception {
        List lstParameter = new ArrayList();
        lstParameter.add(userName);
        return C3p0Connector.queryData(cql, lstParameter);
    }

    /**
     * Hàm lấy dữ liệu theo câu lệnh truy vấn
     *
     * @param lstMulti Danh sách thành phần
     * @param id id của bản ghi cha
     * @return danh sách dữ liệu trả về
     * @since 14/11/2014 HienDM
     */
    public List<Map> selectAttachData(List lstMulti, Long id) throws Exception {
        List lstParameter = new ArrayList();
        PopupMultiAction popup = ((PopupMultiAction) lstMulti.get(BaseAction.INT_MULTI_POPUP));
        String cql = "";

        if (lstMulti.get(BaseAction.INT_MULTI_IDFIELD) != null) {
            String aliasTableName = popup.getTableName();
            String aliasDbFieldName = popup.getIdColumnName();
            if (aliasTableName.length() > 10) {
                aliasTableName = aliasTableName.substring(0, 10);
            }
            if (aliasDbFieldName.length() > 10) {
                aliasDbFieldName = aliasDbFieldName.substring(0, 10);
            }
            cql = " select "
                    + popup.getIdColumnName() + ","
                    + " (select " + popup.getNameColumn()
                    + " from " + lstMulti.get(BaseAction.INT_MULTI_TABLENAME) + " "
                    + aliasTableName + aliasDbFieldName
                    + " where "
                    + aliasTableName + aliasDbFieldName + "."
                    + popup.getIdColumnName() + " = "
                    + lstMulti.get(BaseAction.INT_MULTI_ATTACHTABLE) + "."
                    + lstMulti.get(BaseAction.INT_MULTI_IDPOPUP) + ") " + popup.getNameColumn();
            List<List> lstAttach = (List<List>) lstMulti.get(BaseAction.INT_MULTI_ATTACH);
            if (lstAttach != null && !lstAttach.isEmpty()) {
                for (int i = 0; i < lstAttach.size(); i++) {
                    cql += "," + lstAttach.get(i).get(BaseAction.INT_DB_FIELD_NAME);
                }
            }
            cql += " from " + lstMulti.get(BaseAction.INT_MULTI_ATTACHTABLE)
                    + " where " + lstMulti.get(BaseAction.INT_MULTI_IDCONNECT) + " = ? ";
            lstParameter.add(id);
        } else {
            cql = " select " + popup.getIdColumnName() + "," + popup.getNameColumn()
                    + " from "
                    + ((PopupMultiAction) lstMulti.get(BaseAction.INT_MULTI_POPUP)).getViewName()
                    + " where " + lstMulti.get(BaseAction.INT_MULTI_IDCONNECT) + " = ? ";
            lstParameter.add(id);
        }
        return C3p0Connector.queryData(cql, lstParameter);
    }

    /**
     * Hàm lấy dữ liệu multi upload file
     *
     * @param lstCell thông tin về file upload
     * @param id id của bản ghi cha
     * @return Danh sach file upload
     * @since 14/11/2014 HienDM
     */
    public List<Map> selectMultiUploadData(List lstCell, Long id) throws Exception {
        String cql = "select " + lstCell.get(BaseAction.INT_FILE_PRIMARY) + ","
                + lstCell.get(BaseAction.INT_FILE_IDCONNECT) + ","
                + lstCell.get(BaseAction.INT_FILE_ATTACH)
                + "  from " + lstCell.get(BaseAction.INT_DB_FIELD_NAME) + " where "
                + lstCell.get(BaseAction.INT_FILE_IDCONNECT) + " = ? ";
        List lstParameter = new ArrayList();
        lstParameter.add(id);
        return C3p0Connector.queryData(cql, lstParameter);
    }

    /**
     * Hàm lấy dữ liệu multi upload file
     *
     * @param lstCell thông tin về file upload
     * @param id id của bản ghi cha
     * @param connection kết nối tới cơ sở dữ liệu
     * @since 14/11/2014 HienDM
     */
    public void insertMultiUploadData(List lstCell, Long id, Connection connection) throws Exception {
        MultiUploadField uf = ((MultiUploadField) lstCell.get(INT_COMPONENT));
        if (uf.lstDownloadLink != null && !uf.lstDownloadLink.isEmpty()) {
            String cql = " insert into " + lstCell.get(BaseAction.INT_DB_FIELD_NAME) + " ("
                    + lstCell.get(BaseAction.INT_FILE_PRIMARY) + ","
                    + lstCell.get(BaseAction.INT_FILE_IDCONNECT) + ","
                    + lstCell.get(BaseAction.INT_FILE_ATTACH)
                    + ")  values (" + lstCell.get(BaseAction.INT_FILE_SEQUENCE) + ".nextval,?,?)";
            List<List> lstBacth = new ArrayList();
            for (int i = 0; i < uf.lstDownloadLink.size(); i++) {
                List lstParameter = new ArrayList();
                lstParameter.add(id);
                lstParameter.add(uf.lstDownloadLink.get(i).getFilePath());
                lstBacth.add(lstParameter);
            }
            C3p0Connector.excuteDataBatch(cql, lstBacth, connection);
        }
    }

    /**
     * Hàm lấy dữ liệu multi upload file
     *
     * @param lstCell thông tin về file upload
     * @param id id của bản ghi cha
     * @param connection kết nối tới cơ sở dữ liệu
     * @since 14/11/2014 HienDM
     */
    public void deleteMultiUploadData(List lstCell, Long id, Connection connection) throws Exception {
        String cql = "delete " + lstCell.get(BaseAction.INT_DB_FIELD_NAME) + " where "
                + lstCell.get(BaseAction.INT_FILE_IDCONNECT) + " = ? ";
        List lstParameter = new ArrayList();
        lstParameter.add(id);
        C3p0Connector.excuteData(cql, lstParameter, connection);
    }

    /**
     * Hàm lấy toàn bộ chức năng chương trình
     *
     * @since 13/03/2014 HienDM
     * @return tất cả chức năng chương trình
     */
    public List<Map> getAllMenuItem(Long userId) throws Exception {
        // Load menu tu database
        List lstParameter = new ArrayList();
        lstParameter.add(userId);
        List<Map> lstMenuData = C3p0Connector.queryData("select menu_id, "
                + " menu_name, description, "
                + " ord, parent_id, "
                + " is_enable from sm_menu where is_enable = 1 "
                + " and menu_id IN (SELECT   menu_id "
                + "                       FROM   sm_role_menu "
                + "                      WHERE   role_id = (SELECT   role_id "
                + "                                           FROM   sm_users "
                + "                                          WHERE   user_id = ?)) order by parent_id, ord", lstParameter);
        return lstMenuData;
    }

    /**
     * Hàm trường name từ Column
     *
     * @param idColumn tên cột id
     * @param nameColumn tên cột name
     * @param table tên bảng
     * @param value giá trị cột id
     * @since 13/03/2014 HienDM
     * @return giá trị cột name
     */
    public Object getNameById(String idColumn, String nameColumn, String table, Object value) throws Exception {
        List lstParameter = new ArrayList();
        lstParameter.add(value);
        List<Map> lstMenuData = C3p0Connector.queryData("select "
                + nameColumn + " from " + table + " where " + idColumn + " = ? ", lstParameter);
        if (lstMenuData != null && !lstMenuData.isEmpty()) {
            return lstMenuData.get(0).get(nameColumn);
        } else {
            return null;
        }
    }

    /**
     * Hàm lấy dữ liệu theo câu lệnh truy vấn
     *
     * @param cql câu lệnh truy vấn
     * @param userName tên đăng nhập
     * @return danh sách dữ liệu trả về
     * @since 14/11/2014 HienDM
     */
    public List<Map> getContractInfo(int contract_id) throws Exception {
        String cql = "select c.*,r.NAME room_name,pc.mobile, pc.supply_date, pc.supply_address, pc.original_address,"
                + " pc.people_id,r.STAGE ,h.ADDRESS house_address,\n"
                + "pa.LAST_NAME || ' ' ||  pa.FIRST_NAME fullname_a,\n"
                + "pc.LAST_NAME || ' ' ||  pc.FIRST_NAME fullname_b  from h4u_contract c \n"
                + "JOIN SM_USERS pa on c.PARTY_A_ID = pa.USER_ID \n"
                + "JOIN SM_USERS pc on c.PARTY_B_ID = pc.USER_ID\n"
                + "JOIN H4U_ROOM r on c.ROOM_ID = r.ROOM_ID\n"
                + "JOIN H4U_HOUSE h on r.HOUSE_ID = h.HOUSE_ID\n"
                + "where contract_id=?";
        List lstParameter = new ArrayList();
        lstParameter.add(contract_id);
        return C3p0Connector.queryData(cql, lstParameter);
    }
    public boolean makeInvoiceFromContract(Map m) throws Exception {
        String cql = "Insert into H4U_INVOICE (INVOICE_ID,CONTRACT_ID,INVOICE_TYPE,STATE,ELECTRIC_START_INDEX,ELECTRIC_END_INDEX,PRICE,CLEAN_PRICE,WATER_PRICE,INTERNET_PRICE,TELEVISION_PRICE,WASHING_PRICE,CREATE_USER_ID,RECEIVE_USER_ID,CREATE_DATE,RESOLVE_DATE,DESCRIPTION,TOTAL_PRICE,ACTUAL_PRICE,START_DATE,END_DATE,ELECTRIC_PRICE,NOTE) "
                + "values ('h4u_invoice_seq.nextval()','1234','123','1','188','999','111','0','90','88','88','88','88','8',to_date('24-06-2016','DD-MM-RRRR'),to_date('24-06-2016','DD-MM-RRRR'),'12','123','1234',to_date('01-06-2016','DD-MM-RRRR'),to_date('30-06-2016','DD-MM-RRRR'),'1500','1');";
         C3p0Connector.queryData(cql);
         return true;
    }

    public static void main(String[] args) throws Exception {
        BaseDAO newBD = new BaseDAO();
        List<Map> listmap = newBD.getContractInfo(1);
        System.out.println("=====" + listmap.size());
    }
}
