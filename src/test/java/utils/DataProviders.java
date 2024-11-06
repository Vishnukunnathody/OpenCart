package utils;

import org.testng.annotations.DataProvider;
import java.util.logging.Logger;

public class DataProviders {

    private static final String EXCEL_FILE_PATH = System.getProperty("user.dir") + "/src/test/resources/OpenCartTestData.xlsx";
    private MyXLSReader myXLSReader;
    private static final Logger logger = Logger.getLogger(DataProviders.class.getName());

    public DataProviders() {
        try {
            myXLSReader = new MyXLSReader(EXCEL_FILE_PATH);
        } catch (Exception e) {
            logger.severe("Failed to initialize MyXLSReader: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Object[][] getData(String sheetName) {
        try {
            return ExcelUtil.getTestData(myXLSReader, sheetName, "data");
        } catch (Exception e) {
            logger.severe("Error reading data from sheet: " + sheetName + ". " + e.getMessage());
            e.printStackTrace();
            return new Object[0][0]; // Return an empty array if there's an error
        }
    }

    @DataProvider(name = "ValidloginDataSupplier")
    public Object[][] dataProviderMethodForLoginTest() {
        return getData("LoginTest Valid Credentials");
    }

    @DataProvider(name = "InvalidloginDataSupplier")
    public Object[][] dataProviderMethodForInvalidLoginTest() {
        return getData("LoginTest Invalid Credentials");
    }

    @DataProvider(name = "PwdComplexityDataSupplier")
    public Object[][] dataProviderMethodForValidatingPwdComplexity() {
        return getData("RegisterTest- Password complexity");
    }

    @DataProvider(name = "invalidPhoneNumbersDataSupplier")
    public Object[][] dataProviderMethodForValidatingInvalidPhoneNumbers() {
        return getData("RegisterTest- InvalidPhone Numbers");
    }
}