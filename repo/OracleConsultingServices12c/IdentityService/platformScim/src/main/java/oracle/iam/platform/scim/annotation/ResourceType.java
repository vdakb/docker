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

    System      :   Identity Service Library
    Subsystem   :   Generic SCIM Interface

    File        :   ResourceType.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the annotation
                    ResourceType.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

////////////////////////////////////////////////////////////////////////////////
// annotation ResourceType
// ~~~~~~~~~~ ~~~~~~~~~~~~
/**
 ** Annotation for SCIM resource classes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Target(value   =ElementType.TYPE)
@Retention(value=RetentionPolicy.RUNTIME)
public @interface ResourceType {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the description of the object.
   **
   ** @return                    the description of the object.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String description();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the object.
   ** <br>
   ** This is a human readable name.
   **
   ** @return                    the name of the object.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String name();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schema
  /**
   ** Returns the primary/base resource class.
   **
   ** @return                    the primary/base resource class.
   **                            <br>
   **                            Possible object is {@link Class}.
   */
  Class<?> schema();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   required
  /**
   ** Returns the required schema extension resource classes.
   **
   ** @return                    the required schema extension resource classes.
   **                            <br>
   **                            Possible object array of {@link Class}es.
   */
  Class<?>[] required() default {};

  //////////////////////////////////////////////////////////////////////////////
  // Method:   optional
  /**
   ** Returns the optional schema extension resource classes.
   **
   ** @return                    the optional schema extension resource classes.
   **                            <br>
   **                            Possible object array of {@link Class}es.
   */
  Class<?>[] optional() default {};

  //////////////////////////////////////////////////////////////////////////////
  // Method:   discoverable
  /**
   ** Whether this resource type and its associated schemas should be
   ** discoverable using the SCIM 2 standard /resourceTypes and /schemas
   ** endpoints.
   **
   ** @return                    a flag indicating the discoverability of this
   **                            resource type and its associated schemas.
   */
  boolean discoverable() default true;
}