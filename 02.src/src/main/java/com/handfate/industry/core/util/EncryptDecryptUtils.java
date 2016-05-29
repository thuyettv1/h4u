package com.handfate.industry.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Encoder;

public class EncryptDecryptUtils {

    private final byte[] key = {-95, -29, -62, 25, 25, -83, -18, -85};
    private final String algorithm = "DES";
    private SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
    private final int UTF_8_BufferSize = 8192;

    /**
     * Hàm khởi tạo
     *
     * @since 26/03/2014 HienDM
     */
    public EncryptDecryptUtils() {
        keySpec = new SecretKeySpec(key, algorithm);
    }

    /**
     * Hàm mã hóa mảng byte
     *
     * @since 26/03/2014 HienDM
     * @param arrByte mảng byte cần mã hóa
     * @return mảng byte đã mã hóa
     */
    public byte[] encrypt(byte[] arrByte) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(1, keySpec);
        byte[] data = cipher.doFinal(arrByte);

        return data;
    }

    /**
     * Hàm giải mã mảng byte
     *
     * @since 26/03/2014 HienDM
     * @param arrByte mảng byte cần giải mã
     * @return mảng byte đã giải mã
     */
    public byte[] decrypt(byte[] arrByte) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(2, keySpec);
        return cipher.doFinal(arrByte);
    }

    /**
     * Hàm mã hóa file
     *
     * @since 26/03/2014 HienDM
     * @param originalFilePath đường dẫn file chưa mã hóa
     * @param encryptedFilePath đường dẫn file sẽ được mã hóa
     */

    public void encryptFile(String originalFilePath, String encryptedFilePath) throws Exception {
        FileInputStream stream = new FileInputStream(originalFilePath);
        OutputStream out = new FileOutputStream(encryptedFilePath);
        try {
            int bytesRead = 0;
            byte[] buffer = new byte[UTF_8_BufferSize];
            while ((bytesRead = stream.read(buffer, 0, UTF_8_BufferSize)) != -1) {
                byte[] cloneBuffer = new byte[bytesRead];
                if (bytesRead < buffer.length) {
                    for (int i = 0; i < bytesRead; i++) {
                        cloneBuffer[i] = buffer[i];
                    }
                }
                out.write(encrypt(cloneBuffer));
            }
        } finally {
            stream.close();
            out.close();
        }
    }

    /**
     * Hàm giải mã file
     *
     * @since 26/03/2014 HienDM
     * @param encryptedFilePath đường dẫn file đã mã hóa
     * @param decryptedFilePath đường dẫn file sẽ được giải mã
     */
    public void decryptFile(String encryptedFilePath, String decryptedFilePath) throws Exception {
        FileInputStream stream = new FileInputStream(encryptedFilePath);
        OutputStream out = new FileOutputStream(decryptedFilePath);
        try {
            int bytesRead = 0;
            byte[] buffer = new byte[UTF_8_BufferSize];

            while ((bytesRead = stream.read(buffer, 0, UTF_8_BufferSize)) != -1) {
                byte[] cloneBuffer = new byte[bytesRead];
                if (bytesRead < buffer.length) {
                    for (int i = 0; i < bytesRead; i++) {
                        cloneBuffer[i] = buffer[i];
                    }
                }
                out.write(decrypt(cloneBuffer));
            }
        } finally {
            stream.close();
            out.close();
        }
    }

    /**
     * Hàm giải mã file
     *
     * @since 26/03/2014 HienDM
     * @param encryptedFilePath đường dẫn file đã mã hóa
     * @return chuỗi chứa nội dung đã giải mã
     */
    public String decryptFile(String encryptedFilePath) throws Exception {
        StringBuilder returnValue = new StringBuilder();
        FileInputStream stream = new FileInputStream(encryptedFilePath);
        try {
            int bytesRead = 0;
            byte[] buffer = new byte[UTF_8_BufferSize];
            while ((bytesRead = stream.read(buffer, 0, UTF_8_BufferSize)) != -1) {
                byte[] cloneBuffer = new byte[bytesRead];
                if (bytesRead < buffer.length) {
                    for (int i = 0; i < bytesRead; i++) {
                        cloneBuffer[i] = buffer[i];
                    }
                }
                returnValue.append(new String(decrypt(cloneBuffer)));
            }
        } finally {
            stream.close();
        }
        return returnValue.toString();
    }

    /**
     * Hàm giải mã file
     *
     * @since 26/03/2014 HienDM
     * @param encryptedFilePath đường dẫn file đã mã hóa
     * @return chuỗi chứa nội dung đã giải mã
     */
    public StringBuilder decryptFileToStringBuilder(String encryptedFilePath) throws Exception {
        String inputFilePath = encryptedFilePath + ".tmp";
        XOREncrypt(encryptedFilePath, inputFilePath);        
        FileInputStream inputStream = new FileInputStream(inputFilePath);
        StringBuilder returnValue = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, FileUtils.UTF_8));
        try {
            StringBuilder line =  new StringBuilder();
            while (!(line.append(br.readLine())).toString().equals("null")) {
                returnValue.append(line);
                returnValue.append(System.getProperty("line.separator"));
                line =  new StringBuilder();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            br.close();
            inputStream.close();
        }
        
        File tempFile = new File(inputFilePath);
        tempFile.delete();
        String result = returnValue.substring(0, returnValue.length() - 4);
        returnValue = new StringBuilder();
        returnValue.append(result);
        return returnValue;
    }
    
    /**
     * Hàm giải mã file
     *
     * @since 26/03/2014 HienDM
     * @param stream stream đã mã hóa
     * @return chuỗi chứa nội dung đã giải mã
     */
    public String decryptFile(FileInputStream stream) throws Exception {
        StringBuilder returnValue = new StringBuilder();
        try {
            int bytesRead = 0;
            byte[] buffer = new byte[UTF_8_BufferSize];

            while ((bytesRead = stream.read(buffer, 0, UTF_8_BufferSize)) != -1) {

                byte[] cloneBuffer = new byte[bytesRead];
                if (bytesRead < buffer.length) {
                    for (int i = 0; i < bytesRead; i++) {
                        cloneBuffer[i] = buffer[i];
                    }
                }
                returnValue.append(new String(decrypt(cloneBuffer)));
            }
        } finally {
            stream.close();
        }
        return returnValue.toString();
    }
    
    /**
     * Hàm mã hóa mật khẩu một chiều SHA-1
     *
     * @since 26/03/2014 HienDM
     * @param clearTextPassword
     * @return chuỗi mật khẩu đã mã hóa
     */
    public String encodePassword(String clearTextPassword) throws Exception {
        if(ResourceBundleUtils.getConfigureResource("passwordSalt") != null)
            clearTextPassword = ResourceBundleUtils.getConfigureResource("passwordSalt") + clearTextPassword;
        MessageDigest md = null;
        md = MessageDigest.getInstance("SHA-1"); 
        md.update(clearTextPassword.getBytes("UTF-8")); 
        byte raw[] = md.digest(); 
        String hash = (new BASE64Encoder()).encode(raw); 
        return hash;            
    }
    
    public void XOREncrypt(String inputFile, String outputFile) throws Exception{     
        int[] key = {1987,2015};
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(inputFile), 2048);
        FileOutputStream out = new FileOutputStream(outputFile);
        try {
            int read = -1;
            int totalRead = 0;
            long totalSize = (new File(inputFile)).length();
            long curPercentage = -1;
            long tmpPercentage = -1;
            do {
                read = in.read();
                out.write(read ^ key[totalRead % (key.length - 1)]);
                totalRead++;
                tmpPercentage = ((100 * totalRead) / totalSize);
                if (tmpPercentage % 5 == 0 && tmpPercentage != curPercentage) {
                    curPercentage = tmpPercentage;
                }
            } while (read != -1);
        } finally {
            in.close();
            out.flush();
            out.close();
        }
    }

    public void XOREncrypt(String inputFile, String keyFile, String outputFile) throws Exception{
        int[] key = readKey(keyFile);
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(inputFile), 2048);
        FileOutputStream out = new FileOutputStream(outputFile);
        try {
            int read = -1;
            int totalRead = 0;
            long totalSize = (new File(inputFile)).length();
            long curPercentage = -1;
            long tmpPercentage = -1;
            do {
                read = in.read();
                out.write(read ^ key[totalRead % (key.length - 1)]);
                totalRead++;
                tmpPercentage = ((100 * totalRead) / totalSize);
                if (tmpPercentage % 5 == 0 && tmpPercentage != curPercentage) {
                    curPercentage = tmpPercentage;
                }
            } while (read != -1);
        } finally {
            in.close();
            out.flush();
            out.close();
        }
    }    
    
    private int[] readKey(String keyFile) throws Exception{        
        if ((new File(keyFile)).length() <= 0) {
            throw new Exception("key size is zero!");
        }
        int[] fileContents = new int[(new Long((new File(keyFile)).length())).intValue() + 1];
        FileInputStream in = new FileInputStream(keyFile);
        try {
            int totalRead = 0;
            int read = -1;
            do {
                read = in.read();
                fileContents[totalRead] = read;
                totalRead++;
            } while (read != -1);
        } finally {
            in.close();
        }
        return fileContents;
    }    
    
    /*public static void encryptDatabaseFile(String path) throws Exception {
        String configFile = path + "Database.properties";
        String encryptFile = path + "Database.conf";
        File databaseConfigFile = new File(configFile);
        File databaseEncryptFile = new File(encryptFile);
        if (databaseConfigFile.exists()) {
            databaseConfigFile.delete();
        }
        if (databaseEncryptFile.exists()) {
            databaseEncryptFile.delete();
        }

        String databaseConfigResource = "com.handfate.industry.core.config.Database";
        String driverClass = ResourceBundleUtils.getOtherResource("driverClass",databaseConfigResource);
        String jdbcURL = ResourceBundleUtils.getOtherResource("jdbcURL",databaseConfigResource);
        String user = ResourceBundleUtils.getOtherResource("user",databaseConfigResource);
        String password = ResourceBundleUtils.getOtherResource("password",databaseConfigResource);
        String minPoolSize = ResourceBundleUtils.getOtherResource("minPoolSize",databaseConfigResource);
        String acquireIncrement = ResourceBundleUtils.getOtherResource("acquireIncrement",databaseConfigResource);
        String maxPoolSize = ResourceBundleUtils.getOtherResource("maxPoolSize",databaseConfigResource);
        String maxStatements = ResourceBundleUtils.getOtherResource("maxStatements",databaseConfigResource); 
            
        String databaseInformation
                = String.format("driverClass=" + driverClass + "%s"
                        + "jdbcURL=" + jdbcURL + "%s"
                        + "user=" + user + "%s"
                        + "password=" + password + "%s"
                        + "minPoolSize=" + minPoolSize + "%s"
                        + "acquireIncrement=" + acquireIncrement + "%s"
                        + "maxPoolSize=" + maxPoolSize + "%s"
                        + "maxStatements=" + maxStatements,
                        System.getProperty("line.separator"),
                        System.getProperty("line.separator"),
                        System.getProperty("line.separator"),
                        System.getProperty("line.separator"),
                        System.getProperty("line.separator"),
                        System.getProperty("line.separator"),
                        System.getProperty("line.separator"));

        FileUtils fileUtils = new FileUtils();
        fileUtils.writeStringToFile(databaseInformation, configFile, FileUtils.UTF_8);
        EncryptDecryptUtils edutils = new EncryptDecryptUtils();
        edutils.encryptFile(configFile, encryptFile);
    }
    
    public static void main(String[] args) {
        try {
            encryptDatabaseFile("D:\\MyCareer\\Projects\\HF_140224_Industry\\06.SOURCE\\industry_oracle_2.0\\core-industry\\src\\main\\resources\\com\\handfate\\industry\\core\\config\\");
            System.out.println("Ma hoa file thanh cong");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }*/
}
