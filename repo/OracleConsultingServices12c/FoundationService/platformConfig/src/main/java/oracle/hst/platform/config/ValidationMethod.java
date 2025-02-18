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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Configuration Extension
    Subsystem   :   Common Shared Utility

    File        :   ValidationMethod.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ValidationMethod.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.config;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Payload;
import javax.validation.Constraint;

////////////////////////////////////////////////////////////////////////////////
// annotation ValidationMethod
// ~~~~~~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Validates a bean predicate method as returning <code>true</code>.
 ** <br>
 ** Bean predicates must be of the form {@code isSomething} or they'll be
 ** silently ignored.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=MethodValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.METHOD})
public  @interface ValidationMethod {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   message
  /**
   ** Return the message to return if constraint violations detected.
   **
   ** @return                    the message to return if constraint violations
   **                            detected.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String message() default "is not valid";

  //////////////////////////////////////////////////////////////////////////////
  // Method:   group
  /**
   ** Return an optional collection of grouping {@link Class}es targeted for
   ** validation.
   **
   ** @return                    the optional collection of grouping
   **                            {@link Class}es targeted for validation.
   **                            <br>
   **                            Possible object is array of {@link Class} for
   **                            type any.
   */
  Class<?>[] group() default { };

  //////////////////////////////////////////////////////////////////////////////
  // Method:   payload
  /**
   ** Return an optional collection of {@link Class}es representing the
   ** {@link Payload} of a violations.
   **
   ** @return                    the collection of {@link Class}es as
   **                            {@link Payload} of a violations.
   **                            <br>
   **                            Possible object is array of {@link Class} for
   **                            type {@link Payload}.
   */
  Class<? extends Payload>[] payload() default { };
}