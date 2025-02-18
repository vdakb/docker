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

    System      :   Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   Match.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Match.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.1.0.0     2014-11-29  DSteding    First release version
*/

package oracle.hst.deployment.mds.type;

import java.util.List;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Reference;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class Match
// ~~~~~ ~~~~~
/**
 ** The environment wrapper of a specific cutomization path mapping in a Oracle
 ** Metadata Store.
 ** <p>
 ** A static, ordered list of CustomizationClasses.
 ** <p>
 ** The order implies the order of application of customization layers: the
 ** customization layer(s) implied by the name and value(s) returned by the
 ** first customization class in the list are applied first, then those for the
 ** next one in the list, etc. The last customization layer to be applied (also
 ** known as the tip layer) is that corresponding to the last customization
 ** class in the list.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.1.0.0
 ** @since   1.1.0.0
 */
public class Match extends Namespace {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final List<Clazz> clazz = new ArrayList<Clazz>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Match</code> type that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Match() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid
  /**
   ** Call by the ANT kernel to inject the argument for parameter refid.
   ** <p>
   ** Makes this instance in effect a reference to another
   ** <code>Match</code> instance.
   ** <p>
   ** You must not set another attribute or nest elements inside this element
   ** if you make it a reference.
   **
   ** @param  reference          the id of this instance.
   **
   ** @throws BuildException     if any other instance attribute is already set.
   */
  @Override
  public void setRefid(final Reference reference)
    throws BuildException {

    Object other = reference.getReferencedObject(getProject());
    if (other instanceof Match) {
      final Match that = (Match)other;
      this.clazz.clear();
      this.clazz.addAll(that.clazz);
      // ensure inheritance
      super.setRefid(reference);
    }
    else {
      final Object[] parameter = {reference.getRefId(), "match", reference.getRefId(), other.getClass().getName() };
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_REFERENCE_MISMATCH, parameter), getLocation());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clazz
  /**
   ** Returns the registered customization classes.
   **
   ** @return                  the registered customization classes.
   */
  public List<Clazz> clazz() {
    return this.clazz;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredClass
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Clazz}.
   **
   ** @param  clazz              the subject of maintenance.
   **
   ** @throws BuildException     if this instance is referencing a declared
   **                            <code>Clazz</code>
   */
  public void addConfiguredClass(final Clazz clazz)
    throws BuildException {

    // check if we have this file already
    if (this.clazz.contains(clazz))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.METADATA_CUSTCLASS_ONLYONCE, clazz.delegate().getName()));

    // ensure inheritance
    this.clazz.add(clazz);
  }
}