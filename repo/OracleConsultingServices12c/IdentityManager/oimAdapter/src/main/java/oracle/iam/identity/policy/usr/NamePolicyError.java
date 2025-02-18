package oracle.iam.identity.policy.usr;

////////////////////////////////////////////////////////////////////////////////
// interface NamePolicyError
// ~~~~~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface NamePolicyError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String PREFIX                       = "OIM-";

  static final String USERNAME_FAILURE             = "IAM-3050083";
  static final String COMMONNAME_FAILURE           = "IAM-3050084";

  static final String PROPERTY_NOTFOUND            = "IAM-3050085";
  static final String PROPERTY_INVALID             = "IAM-3050086";

  // 00091 - 00100 process related errors
  static final String IDENTITY_PRE_PROCESS_DATA    = PREFIX + "00091";
  static final String IDENTITY_POST_PROCESS_DATA   = PREFIX + "00092";
  static final String IDENTITY_POST_PROCESS_SIMPLE = PREFIX + "00093";
  static final String IDENTITY_POST_PROCESS_BULK   = PREFIX + "00094";
}