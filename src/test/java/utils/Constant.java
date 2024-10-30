package utils;

import java.util.Arrays;
import java.util.List;

public class Constant {
	
	
	public final static String Your_Account_Created_PageTitle ="Your Account Has Been Created!";
	public final static String Register_Account_PageTitle ="Register Account";
	
	public final static String FirstName_Label = "First Name";
	public final static String Last_Name_Label = "Last Name";
	public final static String E_Mail_Label = "E-Mail";
	public final static String Telephone_Label = "Telephone";
	public final static String Password_Label = "Password";
	public final static String Password_Confirm_Label = "Password Confirm";
	
	public final static String color_Red = "rgb(255, 0, 0)";
	public final static String Asteric_Symbol = "*";
	
	
	
	
	public final static String Expected_FirstName_WarningMsg = "First Name must be between 1 and 32 characters!";
	public final static String Expected_LastName_WarningMsg = "Last Name must be between 1 and 32 characters!";
	public final static String Expected_Email_WarningMsg = "E-Mail Address does not appear to be valid!";
	public final static String Expected_Telephone_WarningMsg = "Telephone must be between 3 and 32 characters!";
	public final static String Expected_Password_WarningMsg = "Password must be between 4 and 20 characters!";
	public final static String Expected_Confirm_Password_WarningMsg = "Password confirmation does not match password!";
	public final static String Expected_Privacy_WarningMsg = "Warning: You must agree to the Privacy Policy!";
	public final static String Expected_EmailAlreadyExists_WarningMsg = "Warning: E-Mail Address is already registered!";
	
	
	public final static String Email_TooltipErrmMsg_NoAtSign ="Please include an '@' in the email address." + " 'vish' is missing an '@'.";
	public final static String Email_TooltipErrmMsg_NoTextAfterAtSign ="Please enter a part following '@'." + " 'vish@' is incomplete.";
	public final static String Email_TooltipErrmMsg_DotInWrongPosition ="'.' is used at a wrong position in 'gmail.'.";
	
	public final static String Fname_PlaceHolder ="First Name";
	public final static String Lname_PlaceHolder ="Last Name";
	public final static String Email_PlaceHolder ="E-Mail";
	public final static String Phone_PlaceHolder ="Telephone";
	public final static String Pwd_PlaceHolder ="Password";
	public final static String ConfirmPwd_PlaceHolder ="Password Confirm";
	
	public final static String Expected_Login_warningMsg ="Warning: No match for E-Mail Address and/or Password.";
	public final static String Forgot_Password_Label ="Forgot Your Password?";
	
	
	public final static String EmailAddress_Label_loginPage ="E-Mail Address";
	public final static String Password_Label_loginPage ="Password";
	public final static String EmailAddress_PlaceHolder_loginPage ="E-Mail Address";
	public final static String Password_PlaceHolder_loginPage ="Password";
	
	public static List<String> expectedRedirectURLs = Arrays.asList(
		    "https://expected-final-url1.com",
		    "https://expected-final-url2.com"	);
	
	
	
}
