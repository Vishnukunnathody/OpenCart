package utils;

import java.util.HashMap;
import org.testng.annotations.DataProvider;

public class ExcelUtil {

    /**
     * DataProvider method to retrieve test data from Excel in the form of a HashMap for each test case.
     *
     * @param xls_received  MyXLSReader instance for reading Excel data
     * @param testName      Name of the test case to retrieve data for
     * @param sheetName     Name of the sheet containing the test data
     * @return              2D Object array compatible with TestNG's DataProvider
     * @throws Exception    if there is an error in retrieving the test data
     */
    @DataProvider(name = "hashDataProvider")
    public static Object[][] getTestData(MyXLSReader xls_received, String testName, String sheetName) throws Exception {

        int testStartRowNumber = 1;
        // Find the starting row for the specified test case
        while (!xls_received.getCellData(sheetName, 1, testStartRowNumber).equals(testName)) {
            testStartRowNumber++;
        }

        int columnStartRowNumber = testStartRowNumber + 1;
        int dataStartRowNumber = testStartRowNumber + 2;

        // Calculate the number of data rows
        int rows = 0;
        while (!xls_received.getCellData(sheetName, 1, dataStartRowNumber + rows).isEmpty()) {
            rows++;
        }

        // Calculate the number of data columns
        int columns = 1;
        while (!xls_received.getCellData(sheetName, columns, columnStartRowNumber).isEmpty()) {
            columns++;
        }

        Object[][] testDataArray = new Object[rows][1];

        // Read data into HashMap and populate the test data array
        for (int i = 0, row = dataStartRowNumber; i < rows; i++, row++) {
            HashMap<String, String> dataMap = new HashMap<>();

            for (int j = 0, column = 1; j < columns - 1; j++, column++) {
                String key = xls_received.getCellData(sheetName, column, columnStartRowNumber);
                String value = xls_received.getCellData(sheetName, column, row);
                dataMap.put(key, value);
            }

            testDataArray[i][0] = dataMap;
        }

        return testDataArray;
    }

    /**
     * Checks if a given test case is marked as runnable based on the 'Runmode' flag in the Excel sheet.
     *
     * @param xls_received  MyXLSReader instance for reading Excel data
     * @param testName      Name of the test case to check
     * @param sheetName     Name of the sheet containing the test data
     * @return              true if the test case is marked as runnable, otherwise false
     */
    public static boolean isRunnable(MyXLSReader xls_received, String testName, String sheetName) {

        int totalRows = xls_received.getRowCount(sheetName);

        for (int row = 2; row <= totalRows; row++) {
            String currentTestName = xls_received.getCellData(sheetName, 1, row);

            if (currentTestName.equals(testName)) {
                String runmode = xls_received.getCellData(sheetName, "Runmode", row);
                return "Y".equalsIgnoreCase(runmode);
            }
        }

        return false;
    }
}