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
    Subsystem   :   Foundation Shared Library

    File        :   Attribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the annotation
                    Attribute.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.schema;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import javax.lang.model.type.NullType;

////////////////////////////////////////////////////////////////////////////////
// annotation Attribute
// ~~~~~~~~~~ ~~~~~~~~~
/**
 ** Marker annotation that can be used to define a non-static method as a
 ** "setter" or "getter" for a logical property (depending on its signature), or
 ** non-static object field to be used as a logical property.
 ** <p>
 ** Annotation used to attach meta information to attributes and subattributes
 ** of ICF resources (descendants of interface {@link Resource}).
 ** <p>
 ** Default value ("") indicates that the field name is used as the property
 ** name without any modifications, but it can be specified to non-empty value
 ** to specify different name (e.g. "<code>__UID__</code>" or
 ** "<code>__NAME__</code>" etc). Property name refers to name used externally,
 ** as the field name in <code>ConnectorObject</code>.
 ** <p>
 ** ICF includes a number of special attributes, that all begin and end with __
 ** (for example __NAME__, and __UID__). These special attributes are
 ** essentially functional aliases for specific attributes or object types. The
 ** purpose of the special attributes is to enable a connector developer to
 ** create a contract regarding how a property can be referenced, regardless of
 ** the application that is using the connector. In this way, the connector can
 ** map specific object information between an arbitrary application and the
 ** resource, without knowing how that information is referenced in the
 ** application. 
 ** <p>
 ** ICF doesn't support meta information like description hence it's useless to
 ** declare such meta tags.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Target(value   =ElementType.FIELD)
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Attribute {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Special value that indicates that handlers should use the default name
   ** (derived from method or field name) for property.
   */
  final static String DEFAULT    = "";
  /**
   ** A single-valued attribute that represents the <i>unique identifier</i> of
   ** an object within the name-space of the target resource.
   ** <br>
   ** If possible, this unique identifier also should be immutable.
   */
  static final String IDENTIFIER = "__UID__";
  /**
   ** A single-valued attribute that represents the
   ** <i>user-friendly identifier</i> of an object on a target resource.
   ** <br>
   ** For instance, the name of an <code>Account</code> will most often be its
   ** loginName. The value of <code>Name</code> need not be unique within
   ** <code>ObjectClass</code>.
   */
  static final String UNIQUE     = "__NAME__";
  /**
   ** A single-valued attribute that represents the <i>password</i> of an object
   ** on a target resource.
   ** <br>
   ** Normally this is a write-only attribute.
   */
  static final String PASSWORD = "__PASSWORD__";

  /**
   ** A single-valued attribute that represents the <i>status</i> of an object
   ** on a target resource.
   */
  static final String STATUS   = "__ENABLE__";

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the name of the logical property, i.e. ICF
   ** <code>ConnectorObject</code> attribute name to use for the property.
   ** <br>
   ** If value is empty String (which is the default), will try to use name of
   ** the attribute that is annotated.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** There is <b>no default name available for constructor arguments</b>,
   ** meaning that <b>Empty String is not a valid value for constructor
   ** arguments</b>.
   ** <p>
   ** This meta information is usually used to specify the special name
   ** attributes like <code>__UID__</code> or <code>__NAME__</code> etc.
   **
   ** @return                    the name of the attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String value() default DEFAULT;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   required
  /**
   ** Determines if the property that indicates whether a value (which may be
   ** explicit null) is expected or not.
   ** <br>
   ** If expected, <code>Connector</code> should indicate this as a validity
   ** problem (usually by throwing an exception.
   **
   ** @return                    <code>true</code> if the attribute value is
   **                            required; <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  boolean required() default false;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sensitive
  /**
   ** Determines if the property that indicates whether a value (which may be
   ** explicit null) do not want to be disclosed.
   **
   ** @return                    <code>true</code> if the attribute value is
   **                            disclosed; <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  boolean sensitive() default false;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operational
  /**
   ** Determines if the property that indicates whether a value (which may be
   ** explicit null) is stored in and used by the target system for
   ** administrative purposes.
   ** <br>
   ** For example, you might maintain an operational attribute to log the date
   ** and time when another attribute is modified.
   **
   ** @return                    <code>true</code> if the attribute value is
   **                            stored in and used by the target system for
   **                            administrative purposes; <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  boolean operational() default false;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   returned
  /**
   ** Returns the return constraint for the attribute.
   **
   ** @return                    the return constraint for the attribute.
   **                            <br>
   **                            Possible object is {@link Returned}.
   */
  Returned returned() default Returned.DEFAULT;

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
   **                            Possible object is {@link Mutability}.
   */
  Mutability mutability() default Mutability.MUTABLE;

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
}