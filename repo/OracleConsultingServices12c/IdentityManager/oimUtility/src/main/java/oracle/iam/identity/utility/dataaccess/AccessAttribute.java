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

    Copyright © 2008. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Data Access Facilities

    File        :   AccessAttribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    AccessAttribute.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.dataaccess;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.ClassUtility;

////////////////////////////////////////////////////////////////////////////////
// class AccessAttribute
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The <code>AccessAttribute</code> describes a dataobject element logically.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class AccessAttribute {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the logical name of the form field */
  private final String  name;

  /** the flag indicating that the field content is encrypted */
  private final boolean encrypted;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   AccessAttribute
  /**
   ** Constructs a <code>AccessAttribute</code> with the passed information.
   **
   ** @param  name               the logical name of the form field.
   */
  public AccessAttribute(final String name) {
    this(name, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   AccessAttribute
  /**
   ** Constructs a <code>AccessAttribute</code> with the passed information.
   **
   ** @param  name               the raw name of the form field.
   ** @param  encrypted          the indicator whether the field content is
   **                            encrypted.
   */
  public AccessAttribute(final String name, final boolean encrypted) {
    this.name      = name;
    this.encrypted = encrypted;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the logical name of the form field.
   **
   ** @return                    the raw name of the form field.
   */
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encrpyted
  /**
   ** Returns the indicator whether the field content is encrypted.
   **
   ** @return                    the indicator whether the field content is
   **                            encrypted.
   */
  public final boolean encrypted() {
    return this.encrypted;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns the hash code value for this <code>AccessAtribute</code>. To get
   ** the hash code of this <code>AccessAtribute</code>.
   ** <p>
   ** This ensures that <code>s1.equals(s2)</code> implies that
   ** <code>s1.hashCode()==s2.hashCode()</code> for any two
   ** <code>AccessAtribute</code> <code>s1</code> and <code>s2</code>, as
   ** required by the general contract of <code>Object.hashCode()</code>.
   **
   ** @return                    the hash code value for this
   **                            <code>AccessAtribute</code>.
   */
  @Override
  public int hashCode() {
    return this.name.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Determines if the current AccessAttribute is equal to the specified
   ** Object.
   ** <p>
   ** The comparization is based on the name of the AccessAttribute.
   **
   ** @param  other              Object to compare against the this
   **                            AccessAttribute
   **
   ** @return                    <code>true</code> if the two AccessAttribute
   **                            are the same.
   */
  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof AccessAttribute))
      return false;
    return ((AccessAttribute)other).name.equals(this.name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this instance. The string
   ** representation consists of a list of the instance attributs, enclosed in
   ** square brackets ("[]"). Adjacent elements are separated by the characters
   ** ", " (comma and space). Elements are converted to strings as by
   ** String.valueOf(Object).
   ** <br>
   ** This implementation creates a string buffer initialized with the short
   ** name of the class, appends a left square bracket. A string is obtained
   ** from the string buffer, and returned.
   **
   ** @return  String            A string representation of this collection.
   */
  @Override
  public String toString() {
    final StringBuilder buffer = new StringBuilder(ClassUtility.shortName(this.getClass()));
    buffer.append(SystemConstant.LIST_OPENLIST);
    buffer.append(this.name).append(SystemConstant.COMMA);
    buffer.append(Boolean.valueOf(this.encrypted));
    buffer.append(SystemConstant.LIST_CLOSELIST);
    return buffer.toString();
  }
}