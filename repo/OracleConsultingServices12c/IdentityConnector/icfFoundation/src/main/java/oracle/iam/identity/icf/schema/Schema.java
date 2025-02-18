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
    Subsystem   :   Generic SCIM Library

    File        :   Schema.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the annotation
                    Schema.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.schema;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

////////////////////////////////////////////////////////////////////////////////
// annotation Schema
// ~~~~~~~~~~ ~~~~~~
/**
 ** Annotation for classes indicating the schema of a SCIM object.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Target(value   =ElementType.TYPE)
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Schema {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Special value that indicates that handlers should use the default name
   ** (derived from field, method or class name) for property.
   */
  final static String DEFAULT    = "";

  /**
   ** This constant defines a specific value of ObjectClass that is reserved for
   ** <code>ObjectClass.ACCOUNT</code>.
   ** <p>
   ** When a annotated name of a class matching this constant is found within,
   ** this indicates that the corresponding <code>ConnectorObject</code>
   ** represents a human being (actual or fictional) within the context of a
   ** specific system or application.
   */
  static final String ACCOUNT = "__ACCOUNT__";

  /**
   ** This constant defines a specific value of ObjectClass that is reserved for
   ** <code>ObjectClass.GROUP</code>.
   ** <p>
   ** When a annotated name of a class matching this constant is found within,
   ** this indicates that the <code>ConnectorObject</code> represents a group.
   */
  static final String GROUP  = "__GROUP__";

  /**
   ** This constant defines a specific value of ObjectClass that is reserved for
   ** <code>ObjectClass.GROUP</code>.
   ** <p>
   ** When a annotated name of a class matching this constant is found within,
   ** this indicates that the <code>ConnectorObject</code> represents a role.
   */
  static final String ROLE  = "__ROLE__";

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the name of the logical property, i.e. ICF
   ** <code>ConnectorObject</code> name to use for the property.
   ** <br>
   ** If value is empty String (which is the default), will try to use name of
   ** the class that is annotated.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** There is <b>no default name available for constructor arguments</b>,
   ** meaning that <b>Empty String is not a valid value for constructor
   ** arguments</b>.
   **
   ** @return                    the name of the logical property
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String value() default DEFAULT;
}