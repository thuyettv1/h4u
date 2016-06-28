package com.handfate.industry.core.util;

import com.google.gwt.thirdparty.guava.common.io.Files;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.FileResource;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @since 26/03/2014
 * @author HienDM1
 */
public class FileUtils {
    /**
     * Seven-bit ASCII, a.k.a. ISO646-US, a.k.a. the Basic Latin block of the
     * Unicode character set
     */
    public static final String US_ASCII = "US-ASCII";
    /**
     * ISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1
     */
    public static final String ISO_8859_1 = "ISO-8859-1";
    /**
     * Eight-bit UCS Transformation Format
     */
    public static final String UTF_8 = "UTF-8";
    /**
     * Sixteen-bit UCS Transformation Format, big-endian byte order
     */
    public static final String UTF_16BE = "UTF-16BE";
    /**
     * Sixteen-bit UCS Transformation Format, little-endian byte order
     */
    public static final String UTF_16LE = "UTF-16LE"; // su dung cho tieng viet
    /**
     * Sixteen-bit UCS Transformation Format, byte order identified by an
     * optional byte-order mark
     */
    public static final String UTF_16 = "UTF-16";
    
    /**
     * Hàm ghi chuỗi dữ liệu ra File
     * ví dụ: writeStringToFile(String.format("Hello%sthere!",System.getProperty("line.separator")),"D:\\test.txt", UTF_16LE)
     * 
     * @since 26/03/2014 HienDM
     * @param content nội dung chuỗi dữ liệu, để xuống dòng sử dụng
     * "line.separator" ví dụ:
     * String.format("Hello%sthere!",System.getProperty("line.separator"));
     * @param outputFilePath đường dẫn file
     * @param charset Bộ mã ký tự
     */
    public void writeStringToFile(String content, String outputFilePath, String charset) throws Exception {
        BufferedWriter outputFile = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(outputFilePath), charset));        
        try {
            outputFile.write(content);
        } finally {
            outputFile.close();
        }
    }
    
    /**
     * Hàm ghi dữ liệu trong File ra chuỗi
     * ví dụ: String content = writeFileToString("D:\\test.txt",UTF_16LE)
     * 
     * @since 26/03/2014 HienDM
     * @param inputFilePath đường dẫn file
     * @return chuỗi dữ liệu
     * @param charset Bộ mã ký tự
     */
    public String writeFileToString(String inputFilePath, String charset) throws Exception {
        FileInputStream inputStream = new FileInputStream(inputFilePath);
        StringBuilder returnValue = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, charset));
        try {
            StringBuilder line =  new StringBuilder();
            while (!(line.append(br.readLine())).toString().equals("null")) {
                returnValue.append(line);
                returnValue.append(System.getProperty("line.separator"));
                line =  new StringBuilder();
            }
        } finally {
            br.close();
            inputStream.close();
        }
        return returnValue.toString();
    }
    
    /**
     * Kiểm tra an toàn thông tin đường dẫn file
     *
     * @param filePath Tên file
     * @return Đường dẫn file có đảm bảo an toàn thông tin không
     */
    public static boolean checkSafeFileName(String filePath) {
        for (int i = 0; i < filePath.length(); i++) {
            char c = filePath.charAt(i);
            if (c == 0) {
                return false;
            } else if (c == '.') {
                char c2 = filePath.charAt(i + 1);
                if (c2 == '.') {
                    char c3 = filePath.charAt(i + 2);
                    if (c3 == '\\' || c3 == '/') {
                        return false;
                    }
                } else if (c2 == '\\' || c2 == '/') {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Kiểm tra file có đúng định dạng không
     *
     * @param fileName Tên file
     * @param allowedList Các định dạng file được phép
     * @return File có đúng định dạng hay không
     */
    public static boolean isAllowedType(String fileName, String allowedList) {
        if (fileName != null && !fileName.trim().equals("")) {
            String[] allowedType = allowedList.split(",");
            String ext = extractFileExt(fileName).toLowerCase().substring(1);
            for (int i = 0; i < allowedType.length; i++) {
                if (allowedType[i].equals(ext)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    
    /**
     * Lấy đuôi file (định dạng file)
     *
     * @param fileName Tên file
     * @return Đuôi file (định dạng file)
     */    
    public static String extractFileExt(String fileName) {
        int dotPos = fileName.lastIndexOf(".");
        String extension = fileName.substring(dotPos);
        return extension;
    }

    /**
     * Lấy tên file sau khi cắt đuôi file
     *
     * @param fileName Tên file
     * @return Tên file sau khi cắt đuôi file
     */
    public static String extractFileNameNotExt(String fileName) {
        int dotPos = fileName.lastIndexOf(".");
        String fileNameNotExt = dotPos > 0 ? fileName.substring(0, dotPos) : fileName;

        return fileNameNotExt;
    }    
    
    /**
     * Ghi dữ liệu từ inputStream tới file
     *
     * @param is input stream
     * @param filePath Tên file
     */
    public static void writeFileFromInputStream(InputStream is, String filePath) throws Exception {
        OutputStream os = new FileOutputStream(filePath);
        try {
            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);       
        } finally {
            is.close();
            os.close();
        }
    }
    
    /**
     * Hàm lấy FileResource để download
     *
     * @param file File cần download
     * @return FileResource
     */
    public static FileResource createFileResource(File file) {
        return new FileResource(file) {
            @Override
            public DownloadStream getStream() {
                try {
                    final DownloadStream ds = new DownloadStream(new FileInputStream(getSourceFile()), getMIMEType(), getFilename());
                    ds.setParameter("Content-Length", String.valueOf(getSourceFile().length()));
                    ds.setCacheTime(getCacheTime());
                    return ds;
                } catch (final FileNotFoundException e) {
                    return null;
                }
            }
        };
    }
    
    /**
     * Hàm xuất dữ liệu ra file excel
     *
     * @param data dữ liệu
     * @param outputFile đường dẫn file
     */
    public static void exportExcel(Object[][] data, String outputFile) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook(); //Tạo workbook
        XSSFSheet sheet = workbook.createSheet("data"); //Tạo sheet
        for (int i = 0; i < data.length; i++) {
            Row row = sheet.createRow(i);
            Object[] objArr = data[i];
            for (int j = 0; j < objArr.length; j++) {
                Cell cell = row.createCell(j);
                if (objArr[j] instanceof String) {
                    cell.setCellValue((String) objArr[j]);
                } else if (objArr[j] instanceof Integer) {
                    cell.setCellValue((Integer) objArr[j]);
                }
            }
        }//Ghi ra file
        FileOutputStream out = new FileOutputStream(new File(outputFile));
        try {
            workbook.write(out);
        } finally {
            out.close();
        }
    }
    
    /**
     * Hàm xuất dữ liệu ra file excel với template
     *
     * @param data dữ liệu
     * @param templateFile đường dẫn template file
     * @param height vị trí hàng excel tương ứng với header
     * @param outputFile đường dẫn file
     */
    public static void exportExcelWithTemplate(Object[][] data, String templateFile, String outputFile, int height, List<List> lstExportParameter) throws Exception {
        POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(templateFile));
        HSSFWorkbook workbook = new HSSFWorkbook(fs, true);        
        HSSFSheet sheet = workbook.getSheetAt(0); //Tạo sheet
        // Tạo style
        HSSFCellStyle style = workbook.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setWrapText(true);
        HSSFFont hSSFFont = workbook.createFont();
        hSSFFont.setFontName("Times New Roman");
        hSSFFont.setFontHeightInPoints((short) 13);
        style.setFont(hSSFFont);
        
        // Điền tham số phần header
        for (int i = 1; i <= height; i++) {
            Row row = sheet.getRow(i);
            if(row != null) {
                for(int j = 0; j < data[0].length; j++) {
                    Cell cell = row.getCell(j);
                    if(cell != null) {
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                if(cell.getStringCellValue().equals("$date")) {
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                    Date currentDate = new Date();
                                    String strDate = formatter.format(currentDate);
                                    cell.setCellValue(strDate);
                                }
                                for(int k = 0; k < lstExportParameter.size(); k++) {
                                    if (cell.getStringCellValue().equals(lstExportParameter.get(k).get(0))) {
                                        if(lstExportParameter.get(k).get(1) instanceof Double) {
                                            cell.setCellValue((double)lstExportParameter.get(k).get(1));
                                        } else {
                                            cell.setCellValue(lstExportParameter.get(k).get(1).toString());
                                        }
                                    }
                                }
                                break;
                        }
                    }
                }
            }
        }
        
        // Sao chép phần footer
        List lstFooter = new ArrayList();
        int count1 = height + 1;
        for(int i = 0; i < 10; i++) {
            Row row = sheet.getRow(count1);
            List lstRow = new ArrayList();
            if(row != null) {
                for(int j = 0; j < data[0].length; j++) {
                    Cell cell = row.getCell(j);
                    List lstCell = new ArrayList();
                    if(cell != null) {
                        lstCell.add(cell.getCellStyle());
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_NUMERIC:
                                lstCell.add("numeric");
                                lstCell.add(cell.getNumericCellValue());
                                break;
                            case Cell.CELL_TYPE_STRING:
                                lstCell.add("string");
                                lstCell.add(cell.getStringCellValue());
                                break;
                        }
                    }
                    lstRow.add(lstCell);
                }
            }
            lstFooter.add(lstRow);
            count1++;
        }
        int count = height + 1;
        for(int i = 0; i < lstFooter.size(); i++) {
            Row rowCoppy = sheet.createRow(count + data.length);
            List lstRow = (List)lstFooter.get(i);
            if(!lstRow.isEmpty()){
                int countCell = 0;
                for(int j = 0; j < data[0].length; j++) {
                    Cell cellCopy = rowCoppy.createCell(countCell);
                    List lstCell = (List)lstRow.get(j);
                    if(lstCell.size() == 3) {
                        cellCopy.setCellStyle((CellStyle)lstCell.get(0));
                        if(lstCell.get(1).equals("numeric")) {
                            cellCopy.setCellValue((double)lstCell.get(2));
                        } else {
                            cellCopy.setCellValue(lstCell.get(2).toString());
                        }
                    }
                    countCell++;
                }
            }
            count++;
        }
        
        // Ghi dữ liệu báo cáo
        for (int i = 1; i < data.length; i++) {
            Row row = sheet.createRow(i + height - 1);
            Object[] objArr = data[i];
            for (int j = 0; j < objArr.length; j++) {
                Cell cell = row.createCell(j);
                cell.setCellStyle(style);
                if (objArr[j] instanceof String) {
                    cell.setCellValue((String) objArr[j]);
                } else if (objArr[j] instanceof Integer) {
                    cell.setCellValue((Integer) objArr[j]);
                }
            }
        }
        
        // Điền tham số phần footer
        int count2 = height + 1;
        for (int i = 1; i < lstFooter.size(); i++) {
            Row row = sheet.getRow(count2 + data.length);
            if(row != null) {
                for(int j = 0; j < data[0].length; j++) {
                    Cell cell = row.getCell(j);
                    if(cell != null) {
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                if(cell.getStringCellValue().equals("$date")) {
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                    Date currentDate = new Date();
                                    String strDate = formatter.format(currentDate);
                                    cell.setCellValue(strDate);
                                }
                                for(int k = 0; k < lstExportParameter.size(); k++) {
                                    if (cell.getStringCellValue().equals(lstExportParameter.get(k).get(0))) {
                                        if(lstExportParameter.get(k).get(1) instanceof Double) {
                                            cell.setCellValue((double)lstExportParameter.get(k).get(1));
                                        } else {
                                            cell.setCellValue(lstExportParameter.get(k).get(1).toString());
                                        }
                                    }
                                }
                                break;
                        }
                    }
                }
            }
            count2++;
        }
        workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
        //Ghi ra file
        FileOutputStream out = new FileOutputStream(new File(outputFile));
        try {
            workbook.write(out);
        } finally {
            out.close();
        }
    }
    
    /**
     * Hàm xuất dữ liệu ra file excel với template
     *
     * @param data dữ liệu
     * @param outputFile đường dẫn file
     * @param header header chứa tên các cột
     */
    public static void exportLargeDataToExcel(Object[][] data, String outputFile, Object[] header) throws Exception {
        SXSSFWorkbook workbook = new SXSSFWorkbook();        
        SXSSFSheet sheet = (SXSSFSheet)workbook.createSheet("Data"); //Tạo sheet
        // Tạo style
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setWrapText(true);
        Font hSSFFont = workbook.createFont();
        hSSFFont.setFontName("Times New Roman");
        hSSFFont.setFontHeightInPoints((short) 13);
        style.setFont(hSSFFont);
        
        // Ghi Header
        Row rowHeader = sheet.createRow(0);
        for (int j = 0; j < header.length; j++) {
            Cell cell = rowHeader.createCell(j);
            cell.setCellStyle(style);
            if (header[j] instanceof String) {
                cell.setCellValue((String) header[j]);
            } else if (header[j] instanceof Integer) {
                cell.setCellValue((Integer) header[j]);
            }
        }        
        
        // Ghi dữ liệu báo cáo
        for (int i = 0; i < data.length; i++) {
            Row row = sheet.createRow(i + 1);
            Object[] objArr = data[i];
            for (int j = 0; j < objArr.length; j++) {
                Cell cell = row.createCell(j);
                cell.setCellStyle(style);
                if (objArr[j] instanceof String) {
                    cell.setCellValue((String) objArr[j]);
                } else if (objArr[j] instanceof Integer) {
                    cell.setCellValue((Integer) objArr[j]);
                }
            }
        }

        //Ghi ra file
        FileOutputStream out = new FileOutputStream(new File(outputFile));
        try {
            workbook.write(out);
        } finally {
            out.close();
        }
    }        

    /**
     * Hàm xuất dữ liệu ra file excel với template
     *
     * @param data dữ liệu
     * @param outputFile đường dẫn file
     */
    public static void exportLargeDataToExcel(Object[][] data, String outputFile) throws Exception {
        SXSSFWorkbook workbook = new SXSSFWorkbook();        
        SXSSFSheet sheet = (SXSSFSheet)workbook.createSheet("Data"); //Tạo sheet
        // Tạo style
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setWrapText(true);
        Font hSSFFont = workbook.createFont();
        hSSFFont.setFontName("Times New Roman");
        hSSFFont.setFontHeightInPoints((short) 13);
        style.setFont(hSSFFont);
        
        // Ghi dữ liệu báo cáo
        for (int i = 0; i < data.length; i++) {
            Row row = sheet.createRow(i);
            Object[] objArr = data[i];
            for (int j = 0; j < objArr.length; j++) {
                Cell cell = row.createCell(j);
                cell.setCellStyle(style);
                if (objArr[j] instanceof String) {
                    cell.setCellValue((String) objArr[j]);
                } else if (objArr[j] instanceof Integer) {
                    cell.setCellValue((Integer) objArr[j]);
                }
            }
        }

        //Ghi ra file
        FileOutputStream out = new FileOutputStream(new File(outputFile));
        try {
            workbook.write(out);
        } finally {
            out.close();
        }
    }        
    
    /**
     * Hàm copy File
     *
     * @param srcPath File nguồn
     * @param destPath File đích
     * @param isReplace thay thế hay không
     * @return 1:Thành công, -1:Thất bại vì file nguồn không tồn tại, 0:Thất bại vì file đích đã tồn tại
     */
    public static int copyFile(String srcPath, String destPath, boolean isReplace) throws Exception {
        File srcFile = new File(srcPath);
        if(srcFile.exists()) {
            File destFile = new File(destPath);
            if(destFile.exists()) {
                if(isReplace) destFile.delete();
                else return 0;
            }
            Files.copy(srcFile, destFile);
        } else return -1;
        return 1;
    }    
}
