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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities

    File        :   WSVQualifiedName.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    WSVQualifiedName.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.type;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Reference;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceDataType;

////////////////////////////////////////////////////////////////////////////////
// class WSVQualifiedName
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** <code>WSVQualifiedName</code> server is a special server and runtime
 ** implementation of {@link ServiceDataType} tooling that can adjust its
 ** behaviour by a server type definition file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class WSVQualifiedName extends ServiceDataType {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String TYPE      = "qualifiedName";


  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Namespace URI of the <code>QName</code>. */
  private String             nameSpace = null;

  /** local part of the <code>QName</code>. */
  private String             local     = null;

  /** prefix of the <code>QName</code>. */
  private String             prefix    = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>WSVQualifiedName</code> handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public WSVQualifiedName() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>refid</code>.
   **
   ** @param  reference          the id of this instance.
   **                            <br>
   **                            Allowed object is {@link Reference}.
   **
   ** @throws BuildException     if any other instance attribute is already set.
   */
  @Override
  public void setRefid(final Reference reference)
    throws BuildException {

    // prevent bogus input
    if (!StringUtility.isEmpty(this.nameSpace) || !StringUtility.isEmpty(this.prefix) || !StringUtility.isEmpty(this.local))
      handleAttributeError("refid");

    // ensure inheritance
    super.setRefid(reference);

    Object other = reference.getReferencedObject(getProject());
    if(other instanceof WSVQualifiedName) {
      WSVQualifiedName that = (WSVQualifiedName)other;
      this.local     = that.local;
      this.prefix    = that.prefix;
      this.nameSpace = that.nameSpace;
    }
    else
      handleReferenceError(reference, TYPE, other.getClass());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setNameSpace
  /**
   ** Sets the nameSpace property of the <code>QName</code>.
   **
   ** @param  nameSpace          the nameSpace property of the
   **                            <code>QName</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void setNameSpace(final String nameSpace) {
    this.nameSpace = nameSpace;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nameSpace
  /**
   ** Returns the nameSpace property of the <code>QName</code>.
   **
   ** @return                    the nameSpace property of the
   **                            <code>QName</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String nameSpace() {
    return this.nameSpace;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLocal
  /**
   ** Sets the local property of the <code>QName</code>.
   **
   ** @param  local              the local property of the
   **                            <code>QName</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void setLocal(final String local) {
    this.local = local;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   local
  /**
   ** Returns the local property of the <code>QName</code>.
   **
   ** @return                    the local property of the
   **                            <code>QName</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String local() {
    return this.local;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPrefix
  /**
   ** Sets the prefix property of the <code>QName</code>.
   **
   ** @param  prefix             the prefix property of the
   **                            <code>QName</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void setPrefix(final String prefix) {
    this.prefix = prefix;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prefix
  /**
   ** Returns the prefix property of the <code>QName</code>.
   **
   ** @return                    the prefix property of the
   **                            <code>QName</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String prefix() {
    return this.prefix;
  }
}