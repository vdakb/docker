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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Apache Archiva Connector

    File        :   Marshaller.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Marshaller.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.archiva;

import java.util.Set;

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.framework.common.objects.Attribute;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;
import oracle.iam.identity.icf.foundation.utility.CredentialAccessor;

import oracle.iam.identity.icf.connector.archiva.schema.User;

////////////////////////////////////////////////////////////////////////////////
// class Marshaller
// ~~~~~ ~~~~~~~~~~
/**
 ** An interface to transfer Archiva REST resource to and from Identity
 ** Connector attribute collections.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Marshaller {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The name of the identifier name attribute. */
  public static final String SPECIAL_UID      = "__UID__";

  /** The name of the uniue name attribute. */
  public static final String SPECIAL_NAME     = "__NAME__";

  /** The name of the password attribute. */
  public static final String SPECIAL_PASSWORD = "__PASSWORD__";

  /** The name of the password attribute. */
  public static final String SPECIAL_STATUS   = "__ENABLE__";

  /** The name of the user name attribute. */
  public static final String USERNAME         = "userName";

  /** The name of the user email address attribute. */
  public static final String EMAIL            = "email";

  /** The name of the last name attribute. */
  public static final String FULLNAME         = "fullName";

  /** The name of the password attribute. */
  public static final String PASSWORD         = "password";

  /** The name of the password confirmation attribute. */
  public static final String CONFIRMATION     = "confirmation";

  /** The name of the password change status attribute. */
  public static final String PASSWORDCHANGE   = "passwordChangeRequired";

  /** The name of the locked status attribute. */
  public static final String LOCKED           = "locked";

  /** The name of the permanent status attribute. */
  public static final String PERMANENT        = "permanent";

  /** The name of the validated status attribute. */
  public static final String VALIDATED        = "validated";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor (private)
  /**
   ** Default constructor
   ** <br>
   ** Access modifier private prevents other classes using
   ** "new Marshaller()"
   */
  private Marshaller() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inboundUser
  /**
   ** Factory method to create a new {@link User} instance and transfer the
   ** specified {@link Set} of {@link Attribute}s to the Apigee user resource.
   **
   ** @param  attribute          the {@link Set} of {@link Attribute}s to set
   **                            on the Apigee user resource.
   **                            <br>
   **                            Allowed object is a {@link Set} where each
   **                            elemment is of type {@link Attribute}.
   **
   ** @return                    the Apigee user resource populated by the
   **                            {@link Set} of {@link Attribute}s
   **                            <br>
   **                            Possible object is a {@link UserResource}.
   */
  public static User inboundUser(final Set<Attribute> attribute) {
    final User resource = new User();
    for (Attribute cursor : attribute) {
      if (!CollectionUtility.empty(cursor.getValue())) {
        collect(resource, cursor);
      }
    }
    return resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collect
  /**
   ** Converts the specified PCF user resource to the transfer options
   ** required by the Identity Connector Framework.
   **
   ** @param  resource           the PCF user resource to manipulate.
   **                            <br>
   **                            Allowed object is {@link UserResource}.
   ** @param  name               the name of the attribute to manipulate.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link List} of values to transfer.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Object}.
   */
  private static void collect(final User resource, final Attribute attribute) {
    Object value = attribute.getValue().get(0);
    // only NON-READ-ONLY fields here
    switch (attribute.getName()) {
      case SPECIAL_UID      :
      case USERNAME         : resource.id(String.class.cast(value));
                              break;
      case EMAIL            : resource.email(String.class.cast(value));
                              break;
      case FULLNAME         : resource.fullName(String.class.cast(value));
                              break;
      case SPECIAL_PASSWORD :
      case PASSWORD         : resource.password(CredentialAccessor.string(GuardedString.class.cast(value)));
                              break;
      case CONFIRMATION     : resource.confirmation(CredentialAccessor.string(GuardedString.class.cast(value)));
                              break;
      case PASSWORDCHANGE   : resource.passwordChangeRequired(Boolean.class.cast(value));
                              break;
      case LOCKED           : resource.locked(Boolean.class.cast(value));
                              break;
      case PERMANENT        : resource.permanent(Boolean.class.cast(value));
                              break;
      case VALIDATED        : resource.validated(Boolean.class.cast(value));
                              break;
    }
  }
}