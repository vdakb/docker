/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2024. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Nextcloud Connector

    File        :   User.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    User.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.nextcloud.schema;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import oracle.iam.identity.icf.schema.Schema;
import oracle.iam.identity.icf.schema.Returned;
import oracle.iam.identity.icf.schema.Attribute;
import oracle.iam.identity.icf.schema.Mutability;

////////////////////////////////////////////////////////////////////////////////
// final class User
// ~~~~~ ~~~~~ ~~~~
/**
 ** The Nextcloud REST user entity representation.
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(Schema.ACCOUNT)
public final class User extends Entity<User> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Unique identifier for the User typically used as a label for the
   ** <code>User</code> by Service Provider.
   */
  @JsonProperty(Service.LOGIN)
  @Attribute(value=Attribute.UNIQUE, required=true, mutability=Mutability.IMMUTABLE, returned=Returned.ALWAYS)
  protected String                 login;
  /**
   ** The public identifier for the <code>User</code> typically used identify
   ** the <code>User</code>.
   */
  @Attribute
  @JsonProperty(Service.DISPLAY_NAME)
  private String                   displayName;
  /**
   ** The activation status of the <code>User</code>.
   ** <p>
   ** Per-design its defaults to <code>true</code>.
   */
  @JsonProperty(Service.ENABLED)
  @Attribute(required=true, mutability=Mutability.MUTABLE, returned=Returned.ALWAYS)
  protected boolean                enabled = Boolean.TRUE;
  /**
   ** The E-mail addresse for the <code>User</code>.
   */
  @JsonProperty(Service.EMAIL)
  @Attribute(value=Service.EMAIL, mutability=Mutability.MUTABLE, returned=Returned.ALWAYS)
  private String                   email;
  /**
   ** The preference value language of the <code>User</code>.
   */
  @JsonProperty(Service.LANGUAGE)
  @Attribute(mutability=Mutability.MUTABLE, returned=Returned.ALWAYS)
  private String                   language;

  public User() {
    super();
  }
}
