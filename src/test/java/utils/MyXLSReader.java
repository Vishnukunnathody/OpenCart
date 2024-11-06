package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MyXLSReader {

    private String filepath;
    private Workbook workbook;
    private Sheet sheet;

    public MyXLSReader(String filepath) throws IOException {
        this.filepath = filepath;
        String fileExtension = filepath.substring(filepath.indexOf("."));

        try (FileInputStream fis = new FileInputStream(filepath)) {
            if (fileExtension.equals(".xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else if (fileExtension.equals(".xls")) {
                workbook = new HSSFWorkbook(fis);
            }
            sheet = workbook.getSheetAt(0);
        }
    }

    // Returns the row count in a sheet
    public int getRowCount(String sheetName) {
        int sheetIndex = workbook.getSheetIndex(sheetName);
        if (sheetIndex == -1) {
            return 0;
        } else {
            sheet = workbook.getSheetAt(sheetIndex);
            return sheet.getLastRowNum() + 1; // Last row number is zero-based
        }
    }

    // Returns the data from a cell by column name and row number
    public String getCellData(String sheetName, String colName, int rowNum) {
        if (rowNum <= 0) return "";

        int sheetIndex = workbook.getSheetIndex(sheetName);
        if (sheetIndex == -1) return "";

        sheet = workbook.getSheetAt(sheetIndex);
        Row headerRow = sheet.getRow(0);
        int colNum = findColumnIndex(headerRow, colName);
        
        if (colNum == -1) return "";

        Row row = sheet.getRow(rowNum - 1);
        if (row == null) return "";

        Cell cell = row.getCell(colNum);
        return getCellValueAsString(cell);
    }

    // Returns the data from a cell by column number and row number
    public String getCellData(String sheetName, int colNum, int rowNum) {
        if (rowNum <= 0) return "";

        int sheetIndex = workbook.getSheetIndex(sheetName);
        if (sheetIndex == -1) return "";

        sheet = workbook.getSheetAt(sheetIndex);
        Row row = sheet.getRow(rowNum - 1);
        if (row == null) return "";

        Cell cell = row.getCell(colNum - 1);
        return getCellValueAsString(cell);
    }

    // Helper method to get cell value as a string
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(DateUtil.getJavaDate(cell.getNumericCellValue()));
                    return String.format("%d/%d/%d", cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, String.valueOf(cal.get(Calendar.YEAR)).substring(2));
                }
                return String.valueOf(cell.getNumericCellValue());
            case FORMULA:
                return String.valueOf(cell.getNumericCellValue());
            case BLANK:
                return "";
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    // Returns true if data is set successfully, else false
    public boolean setCellData(String sheetName, String colName, int rowNum, String data) {
        if (rowNum <= 0) return false;

        int sheetIndex = workbook.getSheetIndex(sheetName);
        if (sheetIndex == -1) return false;

        sheet = workbook.getSheetAt(sheetIndex);
        Row headerRow = sheet.getRow(0);
        int colNum = findColumnIndex(headerRow, colName);
        if (colNum == -1) return false;

        Row row = sheet.getRow(rowNum - 1);
        if (row == null) {
            row = sheet.createRow(rowNum - 1);
        }

        Cell cell = row.getCell(colNum);
        if (cell == null) {
            cell = row.createCell(colNum);
        }

        cell.setCellValue(data);
        return writeWorkbook();
    }

    // Overloaded method to set cell data with hyperlink
    public boolean setCellData(String sheetName, String colName, int rowNum, String data, String url) {
        if (rowNum <= 0) return false;

        int sheetIndex = workbook.getSheetIndex(sheetName);
        if (sheetIndex == -1) return false;

        sheet = workbook.getSheetAt(sheetIndex);
        Row headerRow = sheet.getRow(0);
        int colNum = findColumnIndex(headerRow, colName);
        if (colNum == -1) return false;

        Row row = sheet.getRow(rowNum - 1);
        if (row == null) {
            row = sheet.createRow(rowNum - 1);
        }

        Cell cell = row.getCell(colNum);
        if (cell == null) {
            cell = row.createCell(colNum);
        }

        cell.setCellValue(data);
        createHyperlink(cell, url);
        return writeWorkbook();
    }

    // Creates a hyperlink in a cell
    private void createHyperlink(Cell cell, String url) {
        CreationHelper createHelper = workbook.getCreationHelper();
        Hyperlink hyperlink = createHelper.createHyperlink(HyperlinkType.FILE);
        hyperlink.setAddress(url);
        cell.setHyperlink(hyperlink);
        CellStyle hlinkStyle = workbook.createCellStyle();
        Font hlinkFont = workbook.createFont();
        hlinkFont.setUnderline(Font.U_SINGLE);
        hlinkFont.setColor(IndexedColors.BLUE.getIndex());
        hlinkStyle.setFont(hlinkFont);
        cell.setCellStyle(hlinkStyle);
    }

    // Returns true if sheet is created successfully, else false
    public boolean addSheet(String sheetName) {
        try {
            workbook.createSheet(sheetName);
            return writeWorkbook();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Returns true if sheet is removed successfully, else false
    public boolean removeSheet(String sheetName) {
        int index = workbook.getSheetIndex(sheetName);
        if (index == -1) return false;

        try {
            workbook.removeSheetAt(index);
            return writeWorkbook();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Returns true if column is created successfully
    public boolean addColumn(String sheetName, String colName) {
        int index = workbook.getSheetIndex(sheetName);
        if (index == -1) return false;

        try {
            sheet = workbook.getSheetAt(index);
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) headerRow = sheet.createRow(0);

            Cell cell = headerRow.createCell(headerRow.getLastCellNum() == -1 ? 0 : headerRow.getLastCellNum());
            cell.setCellValue(colName);
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cell.setCellStyle(style);
            return writeWorkbook();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Removes a column and all its contents
    public boolean removeColumn(String sheetName, int colNum) {
        try {
            if (!isSheetExist(sheetName)) return false;

            sheet = workbook.getSheet(sheetName);
            for (int i = 0; i < getRowCount(sheetName); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell cell = row.getCell(colNum - 1);
                    if (cell != null) {
                        row.removeCell(cell);
                    }
                }
            }
            return writeWorkbook();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Find whether the sheet exists
    public boolean isSheetExist(String sheetName) {
        return workbook.getSheetIndex(sheetName) != -1 || workbook.getSheetIndex(sheetName.toUpperCase()) != -1;
    }

    // Returns the number of columns in a sheet
    public int getColumnCount(String sheetName) {
        if (!isSheetExist(sheetName)) return -1;

        sheet = workbook.getSheet(sheetName);
        Row row = sheet.getRow(0);
        return (row != null) ? row.getLastCellNum() : -1;
    }

    // Adds a hyperlink to a specific cell in the sheet
    public boolean addHyperLink(String sheetName, String screenShotColName, String linkColName, int rowNum) {
        if (rowNum <= 0) return false;

        int sheetIndex = workbook.getSheetIndex(sheetName);
        if (sheetIndex == -1) return false;

        sheet = workbook.getSheetAt(sheetIndex);
        Row headerRow = sheet.getRow(0);
        int linkColNum = findColumnIndex(headerRow, linkColName);
        int screenshotColNum = findColumnIndex(headerRow, screenShotColName);
        if (linkColNum == -1 || screenshotColNum == -1) return false;

        Row row = sheet.getRow(rowNum - 1);
        if (row == null) return false;

        Cell linkCell = row.getCell(linkColNum);
        Cell screenshotCell = row.getCell(screenshotColNum);
        if (linkCell != null && screenshotCell != null) {
            createHyperlink(screenshotCell, linkCell.getStringCellValue());
            return writeWorkbook();
        }
        return false;
    }

    // Writes the workbook to file
    private boolean writeWorkbook() {
        try (FileOutputStream fos = new FileOutputStream(filepath)) {
            workbook.write(fos);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Finds the column index based on the header row
    private int findColumnIndex(Row headerRow, String colName) {
        if (headerRow == null) return -1;
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            if (headerRow.getCell(i).getStringCellValue().equalsIgnoreCase(colName)) {
                return i;
            }
        }
        return -1;
    }

    // Closes the workbook
    public void close() throws IOException {
        if (workbook != null) {
            workbook.close();
        }
    }
}
