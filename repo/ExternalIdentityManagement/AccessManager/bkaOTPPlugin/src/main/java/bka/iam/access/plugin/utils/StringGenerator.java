package bka.iam.access.plugin.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import oracle.security.am.common.utilities.constant.Component;
import oracle.security.am.common.utilities.log.OAMLogger;


/**
 * StringGenerator has static method, used to generate random string. Generated string are used as PINs
 */
public class StringGenerator {
  protected static Logger LOGGER = OAMLogger.getLogger(Component.PLGN);
  private static String CLASS_NAME = StringGenerator.class.getName();

  
  private static String ALPHA_NUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz";
  private static String NUMERIC = "0123456789";


  /**
   ** Generate random string which contains values from following list 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz
   ** @param size lenght of the returned string
   ** @return
   **/
  public static String getAlphaNumericToken(int size) {
    // choose a Character random from this String
    return getToken(ALPHA_NUMERIC, size);
  }

  /**
   ** Generate random string which contains values from following list 0123456789
   ** @param size lenght of returned string
   ** @return
   **/
  public static String getNumericToken(int size) {
    return getToken(NUMERIC, size);
  }

  /**
   ** Generate random string based on the allowed characters
   ** @param allowedCharacters list of allowed characters like 0123456789
   ** @param size lenght of returned string
   ** @return
   */
  public static String getToken(String allowedCharacters, int size) {
    String METHOD = "getToken";
    LOGGER.entering(CLASS_NAME, METHOD);
    
    // create StringBuffer size of allowedCharacters
    StringBuilder sb = new StringBuilder(size);

    for (int i = 0; i < size; i++) {
      // generate a random number between
      // 0 to AlphaNumericString variable length
      int index = (int) (allowedCharacters.length() * Math.random());
      // add Character one by one in end of sb
      sb.append(allowedCharacters.charAt(index));
    }
    
    LOGGER.exiting(CLASS_NAME, METHOD);
    return sb.toString();
  }

  /**
   * 
   * @param emailsString comma separated list of the email addressed
   * @param startChars number of characters from start which are not masked
   * @param endChars number of chracters before @ character which are not masked
   * @return comma separated list of email which are masked. Only user name are masked, domain is not masked.
   */
  public static String maskEmailAddress(String emailsString, int startChars, int endChars) {
    
    String METHOD = "maskEmailAddress";
    LOGGER.entering(CLASS_NAME, METHOD);
    
    String outputEmails = null;
    if (emailsString != null) {
      StringBuffer maskedEmails = new StringBuffer();
      String[] emails = emailsString.split(",");
      for (String email : emails) {
	if (email != null) {
	  String[] parts = email.trim().split("@");
	  if (parts.length == 2) {
	    String name = parts[0];
	    String domain = parts[1];
	    StringBuffer maskedName = new StringBuffer(name.length());
	    for (int i = 0; i < name.length(); i++) {
	      if (i < startChars || i >= name.length() - endChars) {
		maskedName.append(name.charAt(i));
	      } else {
		maskedName.append("*");
	      }
	    }
	    maskedEmails.append(maskedName.toString())
			.append("@")
			.append(domain)
			.append(",");
	  }
	}
      }
      if (maskedEmails.length() > 0)
	maskedEmails.deleteCharAt(maskedEmails.length() - 1);
      outputEmails = maskedEmails.toString();
    }
    LOGGER.logp(Level.FINEST,  CLASS_NAME, METHOD,"Masked email is: "+outputEmails);
    LOGGER.exiting(CLASS_NAME, METHOD);
    return outputEmails;
  }


  public static void main(String[] args) {
    System.out.println(StringGenerator.getAlphaNumericToken(6));
    System.out.println(StringGenerator.getNumericToken(6));
    System.out.println(StringGenerator.getToken("0123456789", 6));


    System.out.println(StringGenerator.maskEmailAddress("tomas.sebo@imguru.eu,tomas.t.sebo@oracle.com", 1, 1));
    System.out.println(StringGenerator.maskEmailAddress("tomas.sebo@oracle.com", 0, 0));
    System.out.println(StringGenerator.maskEmailAddress(null, 2, -2));
  }
}

