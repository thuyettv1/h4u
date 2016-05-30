package com.handfate.industry.core.oracle;

/*
 * Kết nối C3p0 JDBC
 */
import com.handfate.industry.core.util.EncryptDecryptUtils;
import com.handfate.industry.core.util.ResourceBundleUtils;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class C3p0Connector {
    public static String DATABASE_CONFIG_PATH;
    public static C3p0Connector datasource;
    public static ComboPooledDataSource cpds;
    
    private C3p0Connector() throws Exception {
        DATABASE_CONFIG_PATH = C3p0Connector.class.getClassLoader().getResource("").getPath()
                + File.separator + "com" + File.separator + "handfate"
                + File.separator + "industry" + File.separator + "core"
                + File.separator + "config" + File.separator; 
        String databaseConfigFile = "com.handfate.industry.core.config.Database";
        String driverClass = ResourceBundleUtils.getOtherResource("driverClass",databaseConfigFile);
        String jdbcURL = ResourceBundleUtils.getOtherResource("jdbcURL",databaseConfigFile);
        String user = ResourceBundleUtils.getOtherResource("user",databaseConfigFile);
        String password = ResourceBundleUtils.getOtherResource("password",databaseConfigFile);
        String minPoolSize = ResourceBundleUtils.getOtherResource("minPoolSize",databaseConfigFile);
        String acquireIncrement = ResourceBundleUtils.getOtherResource("acquireIncrement",databaseConfigFile);
        String maxPoolSize = ResourceBundleUtils.getOtherResource("maxPoolSize",databaseConfigFile);
        String maxStatements = ResourceBundleUtils.getOtherResource("maxStatements",databaseConfigFile);            

        String encrypt = ResourceBundleUtils.getConfigureResource("encryptDatabase");
        if(encrypt != null && encrypt.equals("true")) {
            File cassandraConfigFile = new File(DATABASE_CONFIG_PATH + "Database.conf");
            if(cassandraConfigFile.exists()) {
                // Giai ma va doc thong tin tu file config
                EncryptDecryptUtils edUtils = new EncryptDecryptUtils();
                String decryptString = edUtils.decryptFile(DATABASE_CONFIG_PATH + "Database.conf");
                String[] properties = decryptString.split("\r\n");
                for (String property : properties) {
                    String[] arrInformation = property.split("=", 2);
                    if (arrInformation.length == 2) {
                        if(arrInformation[0].equals("driverClass")) driverClass = arrInformation[1];
                        if(arrInformation[0].equals("jdbcURL")) jdbcURL = arrInformation[1];
                        if(arrInformation[0].equals("user")) user = arrInformation[1];
                        if(arrInformation[0].equals("password")) password = arrInformation[1];
                        if(arrInformation[0].equals("minPoolSize")) minPoolSize = arrInformation[1];
                        if(arrInformation[0].equals("acquireIncrement")) acquireIncrement = arrInformation[1];
                        if(arrInformation[0].equals("maxPoolSize")) maxPoolSize = arrInformation[1];
                        if(arrInformation[0].equals("maxStatements")) maxStatements = arrInformation[1];
                    }
                }
            }
        }
        cpds = new ComboPooledDataSource();
        cpds.setDriverClass(driverClass); //loads the jdbc driver
        cpds.setJdbcUrl(jdbcURL);
        cpds.setUser(user);
        cpds.setPassword(password);

        // the settings below are optional -- c3p0 can work with defaults
        cpds.setMinPoolSize(Integer.parseInt(minPoolSize));
        cpds.setAcquireIncrement(Integer.parseInt(acquireIncrement));
        cpds.setMaxPoolSize(Integer.parseInt(maxPoolSize));
        cpds.setMaxStatements(Integer.parseInt(maxStatements));            
    }

    public static C3p0Connector getInstance() throws Exception {
        if (datasource == null) {
            datasource = new C3p0Connector();
            return datasource;
        } else {
            return datasource;
        }
    }

    /**
     * Hàm mở kết nối tới cơ sở dữ liệu
     *
     * @return kết nối tới cơ sở dữ liệu
     * @since 22/07/2014 HienDM
     */    
    public Connection getConnection() throws SQLException {
        return this.cpds.getConnection();
    }    
    
    /**
     * Hàm tạo tìm kiếm dữ liệu
     *
     * @since 13/03/2014 HienDM
     * @param query Câu lệnh truy vấn dữ liệu
     * @param connection kết nối tới database
     * @return Các bản ghi tìm kiếm được
     */
    public static List<Map> queryData(String query, Connection connection) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            rs = preparedStatement.executeQuery();
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int columnCount = rsMetaData.getColumnCount();
            List lstResult = new ArrayList();
            while(rs.next()) {
                Map row = new HashMap();
                for (int i = 1; i <= columnCount; ++i) {
                    Object obj = rs.getObject(i);
                    row.put(rsMetaData.getColumnName(i).toLowerCase(), obj);
                }
                lstResult.add(row);
            }
            return lstResult;
        } finally {
            if (rs != null) {
                rs.close();
            }            
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }
    
    /**
     * Hàm tạo tìm kiếm dữ liệu
     *
     * @since 13/03/2014 HienDM
     * @param query Câu lệnh truy vấn dữ liệu
     * @return Các bản ghi tìm kiếm được
     */
    public static List<Map> queryData(String query) throws Exception {
        Connection connection = null;
        try {             
            connection = C3p0Connector.getInstance().getConnection();
            return queryData(query, connection);
        } finally {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        }
    }

    public static PreparedStatement setPreparedStatement (PreparedStatement preparedStatement, List lstParameter) 
            throws Exception {
        if(lstParameter != null) {
            for (int i = 0; i < lstParameter.size(); i++) {
                if (lstParameter.get(i) instanceof Integer) {
                    if (lstParameter.get(i) != null) {
                        preparedStatement.setInt(i + 1, (Integer) lstParameter.get(i));
                    } else {
                        preparedStatement.setNull(i + 1, java.sql.Types.NULL);
                    }
                } else if (lstParameter.get(i) instanceof Long) {
                    if (lstParameter.get(i) != null) {
                        preparedStatement.setLong(i + 1, (Long) lstParameter.get(i));
                    } else {
                        preparedStatement.setNull(i + 1, java.sql.Types.NULL);
                    }
                } else if (lstParameter.get(i) instanceof Boolean) {
                    if (lstParameter.get(i) != null) {
                        preparedStatement.setBoolean(i + 1, (Boolean) lstParameter.get(i));
                    } else {
                        preparedStatement.setNull(i + 1, java.sql.Types.NULL);
                    }
                } else if (lstParameter.get(i) instanceof Float) {
                    if (lstParameter.get(i) != null) {
                        preparedStatement.setFloat(i + 1, (Float) lstParameter.get(i));
                    } else {
                        preparedStatement.setNull(i + 1, java.sql.Types.NULL);
                    }
                } else if (lstParameter.get(i) instanceof Double) {
                    if (lstParameter.get(i) != null) {
                        preparedStatement.setDouble(i + 1, (Double) lstParameter.get(i));
                    } else {
                        preparedStatement.setNull(i + 1, java.sql.Types.NULL);
                    }
                } else if (lstParameter.get(i) instanceof Short) {
                    if (lstParameter.get(i) != null) {
                        preparedStatement.setShort(i + 1, (Short) lstParameter.get(i));
                    } else {
                        preparedStatement.setNull(i + 1, java.sql.Types.NULL);
                    }
                } else if (lstParameter.get(i) instanceof String) {
                    if (lstParameter.get(i) != null) {
                        preparedStatement.setNString(i + 1, (String) lstParameter.get(i));
                    } else {
                        preparedStatement.setNull(i + 1, java.sql.Types.NULL);
                    }
                } else if (lstParameter.get(i) instanceof java.util.Date) {
                    if (lstParameter.get(i) != null) {
                        preparedStatement.setTimestamp(i + 1, new Timestamp(((java.util.Date) lstParameter.get(i)).getTime()));
                    } else {
                        preparedStatement.setNull(i + 1, java.sql.Types.NULL);
                    }
                } else if (lstParameter.get(i) == null) {
                    preparedStatement.setNull(i + 1, java.sql.Types.NULL);
                }
            }
        }
        return preparedStatement;
    }

    /**
     * Hàm truy vấn dữ liệu theo tham số
     *
     * @since 13/03/2014 HienDM
     * @param query Câu lệnh truy vấn dữ liệu trong database
     * @param lstParameter Tham số truyền vào câu lệnh
     * @return Các bản ghi tìm kiếm được
     */
    public static List<Map> queryData(String query, List lstParameter) throws Exception {
        Connection connection = null;
        try {
            connection = C3p0Connector.getInstance().getConnection();
            return queryData(query, lstParameter, connection);            
        } finally {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        }        
    }
    
    /**
     * Hàm truy vấn dữ liệu theo tham số
     *
     * @since 13/03/2014 HienDM
     * @param query Câu lệnh truy vấn dữ liệu trong database
     * @param lstParameter Tham số truyền vào câu lệnh
     * @param connection Kết nối tới cơ sở dữ liệu
     * @return Các bản ghi tìm kiếm được
     */
    public static List<Map> queryData(String query, List lstParameter, Connection connection) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement = setPreparedStatement(preparedStatement, lstParameter);
            rs = preparedStatement.executeQuery();
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int columnCount = rsMetaData.getColumnCount();
            List lstResult = new ArrayList();
            while (rs.next()) {
                Map row = new HashMap();
                for (int i = 1; i <= columnCount; ++i) {
                    Object obj = rs.getObject(i);
                    row.put(rsMetaData.getColumnName(i).toLowerCase(), obj);
                }
                lstResult.add(row);
            }
            return lstResult;            
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }        
    }

    /**
     * Hàm tạo tìm kiếm dữ liệu
     *
     * @since 13/03/2014 HienDM
     * @param query Câu lệnh truy vấn dữ liệu
     * @return Các bản ghi tìm kiếm được
     */
    public static List<List> queryDataToList(String query) throws Exception {
        Connection connection = null;
        try {
            connection = C3p0Connector.getInstance().getConnection();
            return queryDataToList(query, connection);
        } finally {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        }
    }

    /**
     * Hàm tạo tìm kiếm dữ liệu
     *
     * @since 13/03/2014 HienDM
     * @param query Câu lệnh truy vấn dữ liệu
     * @param connection Kết nối tới database
     * @return Các bản ghi tìm kiếm được
     */
    public static List<List> queryDataToList(String query, Connection connection) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            rs = preparedStatement.executeQuery();
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int columnCount = rsMetaData.getColumnCount();
            List lstResult = new ArrayList();
            while (rs.next()) {
                List row = new ArrayList();
                for (int i = 1; i <= columnCount; ++i) {
                    Object obj = rs.getObject(i);
                    row.add(obj);
                }
                lstResult.add(row);
            }
            return lstResult;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    /**
     * Hàm truy vấn dữ liệu theo tham số
     *
     * @since 13/03/2014 HienDM
     * @param query Câu lệnh truy vấn dữ liệu trong database
     * @param lstParameter Tham số truyền vào câu lệnh
     * @return Các bản ghi tìm kiếm được
     */
    public static List<List> queryDataToList(String query, List lstParameter) throws Exception {
        Connection connection = null;
        try {
            connection = C3p0Connector.getInstance().getConnection();
            return queryDataToList(query, lstParameter, connection);
        } finally {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        }
    }
    
    /**
     * Hàm truy vấn dữ liệu theo tham số
     *
     * @since 13/03/2014 HienDM
     * @param query Câu lệnh truy vấn dữ liệu trong database
     * @param lstParameter Tham số truyền vào câu lệnh
     * @param connection Kết nối tới cơ sở dữ liệu
     * @return Các bản ghi tìm kiếm được
     */
    public static List<List> queryDataToList(String query, List lstParameter, Connection connection) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement = setPreparedStatement(preparedStatement, lstParameter);
            rs = preparedStatement.executeQuery();
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int columnCount = rsMetaData.getColumnCount();
            List lstResult = new ArrayList();
            while (rs.next()) {
                List row = new ArrayList();
                for (int i = 1; i <= columnCount; ++i) {
                    Object obj = rs.getObject(i);
                    row.add(obj);
                }
                lstResult.add(row);
            }
            return lstResult;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    /**
     * Hàm cập nhật cơ sở dữ liệu
     *
     * @since 13/03/2014 HienDM
     * @param query Câu lệnh cập nhật dữ liệu trong cassandra
     */
    public static void excuteData(String query) throws Exception {
        Connection connection = null;
        try {
            connection = C3p0Connector.getInstance().getConnection();
            excuteData(query, connection);
        } finally {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        }
    }

    /**
     * Hàm cập nhật cơ sở dữ liệu
     *
     * @since 13/03/2014 HienDM
     * @param query Câu lệnh cập nhật dữ liệu trong cassandra
     * @param connection kết nối tới cơ sở dữ liệu
     */
    public static void excuteData(String query, Connection connection) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }
    
    /**
     * Hàm cập nhật cơ sở dữ liệu
     *
     * @since 13/03/2014 HienDM
     * @param query Câu lệnh cập nhật dữ liệu trong cassandra
     * @param lstParameter Tham số truyền vào câu lệnh
     */
    public static void excuteData(String query, List lstParameter) throws Exception {
        Connection connection = null;
        try {
            connection = C3p0Connector.getInstance().getConnection();
            excuteData(query, lstParameter, connection);
        } finally {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        }
    }

    /**
     * Hàm cập nhật cơ sở dữ liệu
     *
     * @since 13/03/2014 HienDM
     * @param query Câu lệnh cập nhật dữ liệu trong cassandra
     * @param lstParameter Tham số truyền vào câu lệnh
     * @param connection kết nối tới cơ sở dữ liệu
     */
    public static void excuteData(String query, List lstParameter, Connection connection) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement = setPreparedStatement(preparedStatement, lstParameter);
            preparedStatement.executeUpdate();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }
    
    /**
     * Hàm cập nhật cơ sở dữ liệu
     *
     * @since 13/03/2014 HienDM
     * @param query Câu lệnh cập nhật dữ liệu trong cassandra
     * @param lstBatch Tham số truyền vào câu lệnh
     */
    public static void excuteDataBatch(String query, List<List> lstBatch) throws Exception {
        Connection connection = null;
        try {
            connection = C3p0Connector.getInstance().getConnection();
            excuteDataBatch(query, lstBatch, connection);
        } finally {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        }
    }
    
    /**
     * Hàm cập nhật cơ sở dữ liệu
     *
     * @since 13/03/2014 HienDM
     * @param query Câu lệnh cập nhật dữ liệu trong cassandra
     * @param lstBatch Tham số truyền vào câu lệnh
     * @param connection kết nối tới cơ sở dữ liệu
     */
    public static void excuteDataBatch(String query, List<List> lstBatch, Connection connection) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            for (int k = 0; k < lstBatch.size(); k++) {
                List<List> lstParameter = lstBatch.get(k);
                preparedStatement = setPreparedStatement(preparedStatement, lstParameter);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }
    
    /**
     * Hàm cập nhật cơ sở dữ liệu
     *
     * @since 13/03/2014 HienDM
     * @param query Câu lệnh cập nhật dữ liệu trong cassandra
     * @param lstParameter Tham số truyền vào câu lệnh
     */
    public static void excuteDataByType(String query, List<List> lstParameter) throws Exception {
        Connection connection = null;
        try {
            connection = C3p0Connector.getInstance().getConnection();
            excuteDataByType(query, lstParameter, connection);
        } finally {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        }
    }
    
    public static PreparedStatement setPreparedStatementByType (PreparedStatement preparedStatement, List<List> lstParameter) 
            throws Exception {
        if(lstParameter != null) {
            for (int i = 0; i < lstParameter.size(); i++) {
                if (lstParameter.get(i).get(1).equals("int")) {
                    if (lstParameter.get(i).get(0) != null && !lstParameter.get(i).get(0).toString().isEmpty()) {
                        preparedStatement.setInt(i + 1, Integer.parseInt(lstParameter.get(i).get(0).toString()));
                    } else {
                        preparedStatement.setNull(i + 1, java.sql.Types.NULL);
                    }
                } else if (lstParameter.get(i).get(1).equals("long")) {
                    if (lstParameter.get(i).get(0) != null && !lstParameter.get(i).get(0).toString().isEmpty()) {
                        preparedStatement.setLong(i + 1, Long.parseLong(lstParameter.get(i).get(0).toString()));
                    } else {
                        preparedStatement.setNull(i + 1, java.sql.Types.NULL);
                    }
                } else if (lstParameter.get(i).get(1).equals("boolean")) {
                    if (lstParameter.get(i).get(0) != null && !lstParameter.get(i).get(0).toString().isEmpty()) {
                        preparedStatement.setInt(i + 1, Integer.parseInt(lstParameter.get(i).get(0).toString()));
                    } else {
                        preparedStatement.setNull(i + 1, java.sql.Types.NULL);
                    }
                } else if (lstParameter.get(i).get(1).equals("float")) {
                    if (lstParameter.get(i).get(0) != null && !lstParameter.get(i).get(0).toString().isEmpty()) {
                        preparedStatement.setFloat(i + 1, Float.parseFloat(lstParameter.get(i).get(0).toString()));
                    } else {
                        preparedStatement.setNull(i + 1, java.sql.Types.NULL);
                    }
                } else if (lstParameter.get(i).get(1).equals("double")) {
                    if (lstParameter.get(i).get(0) != null && !lstParameter.get(i).get(0).toString().isEmpty()) {
                        preparedStatement.setDouble(i + 1, Double.parseDouble(lstParameter.get(i).get(0).toString()));
                    } else {
                        preparedStatement.setNull(i + 1, java.sql.Types.NULL);
                    }
                } else if (lstParameter.get(i).get(1).equals("short")) {
                    if (lstParameter.get(i).get(0) != null && !lstParameter.get(i).get(0).toString().isEmpty()) {
                        preparedStatement.setShort(i + 1, Short.parseShort(lstParameter.get(i).get(0).toString()));
                    } else {
                        preparedStatement.setNull(i + 1, java.sql.Types.NULL);
                    }
                } else if (lstParameter.get(i).get(1).equals("string") || lstParameter.get(i).get(1).equals("file")) {
                    if (lstParameter.get(i).get(0) != null && !lstParameter.get(i).get(0).toString().isEmpty()) {
                        preparedStatement.setNString(i + 1, lstParameter.get(i).get(0).toString());
                    } else {
                        preparedStatement.setNull(i + 1, java.sql.Types.NULL);
                    }
                } else if (lstParameter.get(i).get(1).equals("date")) {
                    if (lstParameter.get(i).get(0) != null && !lstParameter.get(i).get(0).toString().isEmpty()) {
                        preparedStatement.setTimestamp(i + 1, new Timestamp(((java.util.Date) lstParameter.get(i).get(0)).getTime()));
                    } else {
                        preparedStatement.setDate(i + 1, null);
                    }
                }
            }
        }
        return preparedStatement;
    }

    
    /**
     * Hàm cập nhật cơ sở dữ liệu
     *
     * @since 13/03/2014 HienDM
     * @param query Câu lệnh cập nhật dữ liệu trong cassandra
     * @param lstParameter Tham số truyền vào câu lệnh
     * @param connection kết nối tới database
     */
    public static void excuteDataByType(String query, List<List> lstParameter, Connection connection) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement = setPreparedStatementByType(preparedStatement,lstParameter);
            preparedStatement.executeUpdate();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }
    
    /**
     * Hàm cập nhật cơ sở dữ liệu theo lô
     *
     * @since 13/03/2014 HienDM
     * @param query Câu lệnh cập nhật dữ liệu trong cassandra
     * @param lstBatch Tham số truyền vào câu lệnh
     */
    public static void excuteDataByTypeBatch(String query, List<List> lstBatch) throws Exception {
        Connection connection = null;
        try {
            connection = C3p0Connector.getInstance().getConnection();
            excuteDataByTypeBatch(query, lstBatch, connection);
        } finally {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        }
    }

    /**
     * Hàm cập nhật cơ sở dữ liệu theo lô
     *
     * @since 13/03/2014 HienDM
     * @param query Câu lệnh cập nhật dữ liệu trong cassandra
     * @param lstBatch Tham số truyền vào câu lệnh
     * @param connection kết nối tới database
     */
    public static void excuteDataByTypeBatch(String query, List<List> lstBatch, Connection connection) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            for (int k = 0; k < lstBatch.size(); k++) {
                List<List> lstParameter = lstBatch.get(k);
                preparedStatement = setPreparedStatementByType(preparedStatement,lstParameter);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    /**
     * Hàm lấy dữ liệu sequence
     *
     * @since 03/01/2015 HienDM
     * @param sequence Sequence
     * @return dữ liệu Sequence
     */
    public static long getSequenceValue(String sequence) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            long myId = -1l;
            String sqlIdentifier = "select " + sequence + ".NEXTVAL from dual";
            connection = C3p0Connector.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(sqlIdentifier);
            rs = preparedStatement.executeQuery();
            if(rs.next()) myId = rs.getLong(1);
            return myId;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
                connection = null;
            }
        }
    }
}
