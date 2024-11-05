package pagesobjects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import base.BasePage;
import utils.Variables;

public class RegisterPage extends BasePage {

	public RegisterPage(WebDriver driver) {
		super(driver);

	}
	
	private final String mandantoryFieldsLabel="(//label[@class='col-sm-2 control-label'])[position() > 1][position() < last()]";
	private final String inputTextFields ="//input[@class='form-control']";
	
	@FindBy(how = How.XPATH, using = "//input[@id='input-firstname']")
	private WebElement fristNameText;

	@FindBy(how = How.XPATH, using = "//input[@id='input-lastname']")
	private WebElement lastNameText;

	@FindBy(how = How.XPATH, using = "//input[@id='input-email']")
	private WebElement emailText;

	@FindBy(how = How.XPATH, using = "//input[@id='input-telephone']")
	private WebElement phoneText;

	@FindBy(how = How.XPATH, using = "//input[@id='input-password']")
	private WebElement passwordText;

	@FindBy(how = How.XPATH, using = "//input[@id='input-confirm']")
	private WebElement ConfirmpasswordText;

	@FindBy(how = How.XPATH, using = "//input[@name='newsletter'][@value=0]")
	private WebElement SubscribeRadioBtnNo;

	@FindBy(how = How.XPATH, using = "//input[@name='newsletter'][@value=1]")
	private WebElement SubscribeRadioBtnyes;

	@FindBy(how = How.XPATH, using = "//input[@name='agree'][@value=1]")
	private WebElement privacyPolicyCheckBox;

	@FindBy(how = How.XPATH, using = "//input[@value='Continue'][@class='btn btn-primary']")
	private WebElement continueBtn;

	@FindBy(how = How.XPATH, using = "//*[@id='input-firstname']/following-sibling::div")
	private WebElement firstNameWarningMsg;

	@FindBy(how = How.XPATH, using = "//*[@id='input-lastname']/following-sibling::div")
	private WebElement lastNameWarningMsg;

	@FindBy(how = How.XPATH, using = "//*[@id='input-email']/following-sibling::div")
	private WebElement emailWarningMsg;

	@FindBy(how = How.XPATH, using = "//*[@id='input-telephone']/following-sibling::div")
	private WebElement telephoneWarningMsg;

	@FindBy(how = How.XPATH, using = "//*[@id='input-password']/following-sibling::div")
	private WebElement passwordWarningMsg;

	@FindBy(how = How.XPATH, using = "//div[@class='alert alert-danger alert-dismissible']")
	private WebElement privacypolicyWarningMsg;

	@FindBy(how = How.XPATH, using = "//div[text()='Password confirmation does not match password!']")
	public WebElement passMissmatchMsg;

	@FindBy(how = How.XPATH, using = "//*[text()='Warning: E-Mail Address is already registered!']")
	public WebElement emailAddressAlreadyRegMsg;

	@FindBy(how = How.XPATH, using = "//a[text()='Register'][@class='list-group-item']")
	private WebElement registerlink;

	public void enterFristName(String fristnameData) {
		enterText(fristNameText, fristnameData);
	}

	public void enterLastName(String lastnameData) {
		enterText(lastNameText, lastnameData);
	}

	public void enterValidEmail(String emailData) {
		enterText(emailText, emailData);

	}

	public void enterTelephoneNumber(String telephoneData) {
		enterText(phoneText, telephoneData);
	}

	public void enterValidPassword(String passwordData) {
		enterText(passwordText, passwordData);
	}

	public void enterConfirmPassword(String passwordData) {
		enterText(ConfirmpasswordText, passwordData);
	}

	public void selectNoToSubscribeNewsLetter() {
		clickElement(SubscribeRadioBtnNo);
	}

	public void selectYesToSubscribeNewsLetter() {
		clickElement(SubscribeRadioBtnyes);
	}

	public void checkPrivacyPolicy() {
		clickElement(privacyPolicyCheckBox);
	}

	public void clickOnContinueButton() {
		clickElement(continueBtn);
	}

	public String getFirstNameWarnigMsg() {
		return getText(firstNameWarningMsg);
	}

	public String getLastNameWarnigMsg() {
		return getText(lastNameWarningMsg);
	}

	public String getEmailWarnigMsg() {
		return getText(emailWarningMsg);
	}

	public String getTelephoneWarnigMsg() {
		return getText(telephoneWarningMsg);
	}

	public String getpasswordWarnigMsg() {
		return getText(passwordWarningMsg);
	}

	public String getpassMissmatchMsg() {
		return getText(passMissmatchMsg);
	}

	public String getprivacyPolicyWarnigMsg() {
		return getText(privacypolicyWarningMsg);
	}

	public String getEmailAlreadyRegMsg() {
		return getText(emailAddressAlreadyRegMsg);
	}

	public AccountSuccessPage registerAnAccount(String fristnameData, String lastnameData, String emailData, String telephoneData,
			String passwordData, String confirmPassData, Boolean subscribeNewsLetter, boolean checkPrivacyPolicy) {
		enterFristName(fristnameData);
		enterLastName(lastnameData);
		enterValidEmail(emailData);
		enterTelephoneNumber(telephoneData);
		enterValidPassword(passwordData);
		enterConfirmPassword(confirmPassData);

		if (subscribeNewsLetter != null) {
			if (subscribeNewsLetter) {
				if (!SubscribeRadioBtnyes.isSelected())
					selectYesToSubscribeNewsLetter();
			} else {
				if (!SubscribeRadioBtnNo.isSelected())
					selectNoToSubscribeNewsLetter();
			}
		}
		if (checkPrivacyPolicy) {
			checkPrivacyPolicy();
		}
		clickOnContinueButton();
		return new AccountSuccessPage(driver);
		
	}

	public RegisterPage clickOnRightregisterGrplink() {
		clickElement(registerlink);
		return new RegisterPage(driver);
	}

	public boolean getDynamicToolTipMsg(String enterText, String enterExpectedErrMsg) throws InterruptedException {
		Map<String, String> optionMap = new HashMap<>();
		optionMap.put(enterText, enterExpectedErrMsg);

		for (Map.Entry<String, String> entry : optionMap.entrySet()) {
			String enteredText = entry.getKey();
			String expectedMsg = entry.getValue();
			enterText(emailText, enteredText);
			checkPrivacyPolicy();
			clickOnContinueButton();
			Thread.sleep(1000);
			String actualtooltipMessage = getAttributeVal(emailText, "validationMessage");
			Variables.emailFieldLevelMsg = actualtooltipMessage;

			if (actualtooltipMessage.equals(expectedMsg)) {
				return true;
			} else
				return false;
		}

		return false;
	}

	public AccountSuccessPage validateTelephoneField(String fristnameData, String lastnameData, String emailData,
			String telephoneData, String passwordData, String confirmPassData) {
		enterFristName(fristnameData);
		enterLastName(lastnameData);
		enterValidEmail(emailData);
		enterTelephoneNumber(telephoneData);
		enterValidPassword(passwordData);
		enterConfirmPassword(confirmPassData);
		checkPrivacyPolicy();
		clickOnContinueButton();
		return new AccountSuccessPage(driver);
	}
	
	public boolean getplaceHolderText(String labelName,String expectedPlaceholderValue) {
		List<WebElement>elements = getElements(inputTextFields);
		
		for(WebElement element:elements) {
			String placeholder=getAttributeVal(element,"placeholder" );
			if (labelName.equals(placeholder)) {
				 return placeholder.equals(expectedPlaceholderValue);
			 }
		}
			 return false;// returns false if the placeholder  is not found or condition not met.
	}
	public boolean getAstericValues(String labelName,String expectedColor,String expectedSymbol) {
		List<WebElement>elements = getElements(mandantoryFieldsLabel);
		for(WebElement element:elements) {
			String label=element.getText();
			 if (labelName.equals(label)) {
				 String color = getCssValue(element, "color");
		            String content = getCssValue(element, "content");
		            return color.equals(expectedColor) && content.contains(expectedSymbol);
}}
		return false;// returns false if the label is not found or condition not met.
		
	}
	
	public boolean getPrivacyPolicyStatus () {
		return checkIfSelected(privacyPolicyCheckBox);
	}
	public boolean getTypeStatus (String testData) {
		enterValidPassword(testData);
		enterConfirmPassword(testData);
		String passType=getAttributeVal(passwordText,"type" );
		String confirmpassType=getAttributeVal(ConfirmpasswordText,"type" );
		 return (passType.equals("password")&&confirmpassType.equals("password"));
	}
}
	



























