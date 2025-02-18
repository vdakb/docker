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

    File        :   Attribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the annotation
                    Attribute.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import javax.lang.model.type.NullType;

////////////////////////////////////////////////////////////////////////////////
// annotation Attribute
// ~~~~~~~~~~ ~~~~~~~~~
/**
 ** Annotation for getter methods of a SCIM object.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Target(value   =ElementType.FIELD)
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Attribute {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   caseExact
  /**
   ** Determines if the attribute value is case sensitive.
   ** <p>
   ** Following <a href="https://tools.ietf.org/html/rfc7643">RFC 7643</a>
   ** <code>caseExact</code> is <code>false</code> (i.e., case-insensitive).
   **
   ** @return                    <code>true</code> if the attribute value's
   **                            case sensitivity; <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  boolean caseExact() default false;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   required
  /**
   ** Determines if the attribute value is required.
   ** <p>
   ** Following <a href="https://tools.ietf.org/html/rfc7643">RFC 7643</a>
   ** <code>required</code> is <code>false</code> (i.e., not REQUIRED).
   **
   ** @return                    <code>true</code> if the attribute value is
   **                            required; <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  boolean required() default false;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the description of the attribute.
   **
   ** @return                    the description of the attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String description() default "";

  //////////////////////////////////////////////////////////////////////////////
  // Method:   multiValueClass
  /**
   ** Determines if the attribute is multi-valued, this holds the type of the
   ** child object.
   **
   ** @return                    for a multi-valued attribute, the type of the
   **                            child object.
   **                            <br>
   **                            Possible object is {@link Class}.
   */
  Class multiValueClass() default NullType.class;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uniqueness
  /**
   ** Returns uniqueness constraint for the attribute.
   ** <p>
   ** Following <a href="https://tools.ietf.org/html/rfc7643">RFC 7643</a>
   ** <code>uniqueness</code> is <code>none</code> (has no uniqueness enforced).
   **
   ** @return                    the uniqueness constraint for the attribute.
   **                            <br>
   **                            Possible object is
   **                            {@link Definition.Uniqueness}.
   */
  Definition.Uniqueness uniqueness() default Definition.Uniqueness.NONE;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mutability
  /**
   ** Returns mutability constraint for the attribute.
   ** <p>
   ** Following <a href="https://tools.ietf.org/html/rfc7643">RFC 7643</a>
   ** <code>mutability</code> is <code>readWrite</code> (i.e., modifiable).
   **
   ** @return                    the mutability constraint for the attribute.
   **                            <br>
   **                            Possible object is
   **                            {@link Definition.Mutability}.
   */
  Definition.Mutability mutability() default Definition.Mutability.READ_WRITE;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   returned
  /**
   ** Returns the return constraint for the attribute.
   ** <p>
   ** Following <a href="https://tools.ietf.org/html/rfc7643">RFC 7643</a>
   ** <code>returned</code> is <code>default</code> (the attribute value is
   ** returned by default).
   **
   ** @return                    the return constraint for the attribute.
   **                            <br>
   **                            Possible object is
   **                            {@link Definition.Returned}.
   */
  Definition.Returned returned() default Definition.Returned.DEFAULT;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   canonical
  /**
   ** Returns canonical values that may appear in an attribute.
   ** <p>
   ** Following <a href="https://tools.ietf.org/html/rfc7643">RFC 7643</a>
   ** <code>canonicalValues</code> is none assigned.
   **
   ** @return                    the canonical values that may appear in an
   **                            attribute.
   **                            <br>
   **                            Possible object is array of {@link String}.
   */
  String[] canonical() default {};

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reference
  /**
   ** Returns the reference types for the attribute.
   **
   ** @return                    the reference types for the attribute.
   **                            <br>
   **                            Possible object is array of {@link String}.
   */
  String[] reference() default {};
}