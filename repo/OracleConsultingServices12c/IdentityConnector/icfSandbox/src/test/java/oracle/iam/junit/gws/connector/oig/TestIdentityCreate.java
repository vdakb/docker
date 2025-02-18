package oracle.iam.junit.gws.connector.oig;

import java.util.Set;

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.AttributesAccessor;
import org.identityconnectors.framework.common.objects.OperationalAttributes;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.icf.foundation.SystemError;
import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.connector.service.AttributeFactory;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;
import oracle.iam.identity.icf.scim.v2.schema.UserResource;

import oracle.iam.identity.icf.scim.v2.schema.Marshaller;

public class TestIdentityCreate extends IdentityFixture {

  static final Set<Attribute> CREATE_AN4711124 = AttributeFactory.set(
    new String[]{
        "schemas"
      ,  Name.NAME
//      , OperationalAttributes.PASSWORD_NAME
//      , OperationalAttributes.ENABLE_NAME
//      , "name.givenName"
//      , "name.familyName"
//      , "emails.work.value"
//      , "emails.work.primary"
//      , "phoneNumbers.work.value"
//      , "phoneNumbers.work.primary"
      , "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User:organization"
      , "urn:ietf:params:scim:schemas:extension:oracle:2.0:OIG:User:participant"
    }
    , new Object[]{
        CollectionUtility.list("urn:ietf:params:scim:schemas:extension:enterprise:2.0:User")
     ,  "an4711124"
//      , new GuardedString("wmkah1mdkh".toCharArray())
//      , Boolean.TRUE
//      , "Gerald"
//      , "Cambrault"
//      , "gerald.cambrault@polizei-an.de"
//      , Boolean.TRUE
//      , "+49 177 5948 437"
//      , Boolean.TRUE
      , "AN"
      , "AN"
    }
  );

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestIdentityCreate</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestIdentityCreate() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Simple command line interface to execute the test case.
   **
   ** @param  args               the command line arguments.
   **
   ** @throws Exception          if the test case fails.
   */
  @SuppressWarnings("unused")
  public static void main(String[] args) {
    try {
      final AttributesAccessor accessor = new AttributesAccessor(CREATE_AN4711124);
      Attribute username = accessor.find(Marshaller.USERNAME);
      if (username == null) {
        username = accessor.find(Name.NAME);
      }
      // prevent bogus state
      if (username == null) {
        propagate(SystemError.NAME_IDENTIFIER_REQUIRED, Marshaller.USERNAME);
      }

      Attribute password = accessor.find(Marshaller.PASSWORD);
      if (password == null) {
        password = accessor.find(OperationalAttributes.PASSWORD_NAME);
      }
      Attribute status = accessor.find(Marshaller.STATUS);
      if (status == null) {
        status = accessor.find(OperationalAttributes.ENABLE_NAME);
      }
      // prevent bogus state
      if (status == null || status.getValue() == null || status.getValue().isEmpty()) {
         status = AttributeBuilder.build(Marshaller.STATUS, Boolean.TRUE);
      }

      // marshall a SCIM user resource transfered to the Service Provider
      UserResource result = Marshaller.transferUser(UserResource.class, CREATE_AN4711124);

      TestIdentitySearch.beforeClass();
//      result = context.createAccount(result);
      System.out.println(result);
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (SystemException e) {
      System.out.println(e.getClass().getSimpleName().concat("::").concat(e.code()).concat("::").concat(e.getLocalizedMessage()));
    }
    catch (Exception e) {
      failed(e);
    }
  }
}