/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Red Hat Keycloak Connector

    File        :   Unmarshall.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Unmarshall.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/
package oracle.iam.junit.keycloak.connector.account;


import java.util.List;

import java.io.IOException;

import javax.ws.rs.ProcessingException;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.fasterxml.jackson.core.type.TypeReference;

import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.JUnitCore;

import oracle.iam.identity.icf.foundation.object.Pair;

import oracle.iam.identity.icf.rest.utility.MapperFactory;

import oracle.iam.identity.icf.connector.keycloak.schema.User;

import oracle.iam.identity.icf.connector.keycloak.marshal.AttributeSerializer;
import oracle.iam.identity.icf.connector.keycloak.marshal.AttributeDeserializer;

import oracle.iam.junit.keycloak.connector.TestBaseConnector;

////////////////////////////////////////////////////////////////////////////////
// class Unmarshall
// ~~~~~ ~~~~~~~~~~
/**
 ** The test case unmarshalling identities from a simulated response.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Unmarshall extends TestBaseConnector {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String foreGuys  = "[{\"id\":\"a742c9a7-c216-4a4d-9d1a-0c5c1d0851b7\",\"createdTimestamp\":1702818368053,\"username\":\"an4711123\",\"enabled\":true,\"totp\":false,\"emailVerified\":true,\"firstName\":\"Zitterbacke\",\"lastName\":\"Alfons\",\"email\":\"alfons.zitterbacke@vm.oracle.com\",\"disableableCredentialTypes\":[],\"requiredActions\":[\"update_user_locale\",\"CONFIGURE_TOTP\",\"VERIFY_EMAIL\",\"webauthn-register-passwordless\",\"webauthn-register\",\"UPDATE_PASSWORD\",\"UPDATE_PROFILE\"],\"notBefore\":0,\"access\":{\"manageGroupMembership\":true,\"view\":true,\"mapRoles\":true,\"impersonate\":false,\"manage\":true}},{\"id\":\"e45b53dd-7fe6-4259-8f7f-c9f30ccb4676\",\"createdTimestamp\":1702818368011,\"username\":\"an4711124\",\"enabled\":true,\"totp\":false,\"emailVerified\":true,\"firstName\":\"Cambrault\",\"lastName\":\"Gerald\",\"email\":\"gerald.cambrault@vm.oracle.com\",\"disableableCredentialTypes\":[],\"requiredActions\":[\"update_user_locale\",\"CONFIGURE_TOTP\",\"VERIFY_EMAIL\",\"webauthn-register-passwordless\",\"webauthn-register\",\"UPDATE_PASSWORD\",\"UPDATE_PROFILE\"],\"notBefore\":0,\"access\":{\"manageGroupMembership\":true,\"view\":true,\"mapRoles\":true,\"impersonate\":false,\"manage\":true}},{\"id\":\"4a8b77ed-5325-4f03-8d8b-07ea7c01b407\",\"createdTimestamp\":1702818368119,\"username\":\"bkbk4711123\",\"enabled\":true,\"totp\":false,\"emailVerified\":true,\"firstName\":\"Mustermann\",\"lastName\":\"Max\",\"email\":\"max.mustermann@vm.oracle.com\",\"disableableCredentialTypes\":[],\"requiredActions\":[\"update_user_locale\",\"CONFIGURE_TOTP\",\"VERIFY_EMAIL\",\"webauthn-register-passwordless\",\"webauthn-register\",\"UPDATE_PASSWORD\",\"UPDATE_PROFILE\"],\"notBefore\":0,\"access\":{\"manageGroupMembership\":true,\"view\":true,\"mapRoles\":true,\"impersonate\":false,\"manage\":true}},{\"id\":\"e08a24be-0e45-43ee-9882-36f3cca95ce0\",\"createdTimestamp\":1702818368099,\"username\":\"bp4711123\",\"enabled\":true,\"totp\":false,\"emailVerified\":true,\"firstName\":\"Musterfrau\",\"lastName\":\"Agathe\",\"email\":\"agathe.musterfrau@vm.oracle.com\",\"disableableCredentialTypes\":[],\"requiredActions\":[\"update_user_locale\",\"CONFIGURE_TOTP\",\"VERIFY_EMAIL\",\"webauthn-register-passwordless\",\"webauthn-register\",\"UPDATE_PASSWORD\",\"UPDATE_PROFILE\"],\"notBefore\":0,\"access\":{\"manageGroupMembership\":true,\"view\":true,\"mapRoles\":true,\"impersonate\":false,\"manage\":true}}]";
  private static final String ATTR      =
  "{ \"attributes\" :\n" + 
  "  { \"division\"           : \"AN_1\"\n" + 
  "  , \"uid\"                : \"aaaaaaa\"\n" + 
  "  , \"organization\"       : \"BK\"\n" + 
  "  , \"pid\"                : [\"ddddddd\"]\n" + 
  "  , \"participant\"        : [\"BK\",\"BP\"]\n" + 
  "  , \"organizationalUnit\" : [\"AN_1_1\"]\n" + 
  "  }\n" + 
  "}";

  public static class Value {
    
    @JsonProperty("attributes")
    @JsonSerialize(using=AttributeSerializer.class)
    @JsonDeserialize(using=AttributeDeserializer.class)
    private List<Pair<String, String>> attributes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Unmarshall</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Unmarshall() {
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
   **                            <br>
   **                            Allowed object is array of {@link String}.
   */
  @SuppressWarnings("unused")
  public static void main(final String[] args) {
    final String[] parameter = {Unmarshall.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   threeGuys
  /**
   ** Test paginated serach request leveraging server context.
   */
  @Test
  public void threeGuys() {
    try {
      final List<User> u2 = MapperFactory.instance.readValue(foreGuys, new TypeReference<List<User>>(){});
      assertTrue("size", u2.size() > 0);
    }
    catch (ProcessingException e) {
      Assert.fail(e.getMessage());
    }
    catch (IOException e) {
      Assert.fail(e.getMessage());
    }
    catch (Exception e) {
      Assert.fail(e.getMessage());
    }
  }
  @Test
  public void listPair() {
    try {
      final Value origin = MapperFactory.instance.readValue(ATTR, Value.class);
      System.out.println(origin);
      final String target =  MapperFactory.instance.writeValueAsString(origin);
      System.out.println(target);
    }
    catch (ProcessingException e) {
      Assert.fail(e.getMessage());
    }
    catch (Exception e) {
      Assert.fail(e.getMessage());
    }
  }
}