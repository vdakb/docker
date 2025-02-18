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

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   FeatureInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    FeatureInstance.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.oauth.common.spi;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.spi.AbstractInstance;

import oracle.iam.access.common.FeatureProperty;

////////////////////////////////////////////////////////////////////////////////
// abstract class FeatureInstance
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** <code>FeatureInstance</code> represents a <code>OAuth Service</code>
 ** artifact in Oracle Access Manager infrastructure that might be created,
 ** deleted or configured after or during an import operation.
 ** <p>
 ** The following artifacts are the components configured within an OAuth
 ** Services:.
 ** <ul>
 **   <li><code>Identity Domain</code>s
 **   <li><code>Resource Client</code>s
 **   <li><code>Resource Server</code>s
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class FeatureInstance extends AbstractInstance {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static final String ENTITY_OPEN        = "{";
  protected static final String ENTITY_CLOSE       = "}";
  protected static final String ARRAY_OPEN         = "[";
  protected static final String ARRAY_CLOSE        = "]";
  protected static final String ATTRIBUTE_RELAX    = "\"%s\":%s";
  protected static final String ATTRIBUTE_STRING   = "\"%s\":\"%s\"";
  protected static final String ADJACENT_SEPARATOR = ",";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String id;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FeatureInstance</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected FeatureInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Called to inject the argument for attribute <code>id</code>.
   **
   ** @param  value              the id of the entity instance.
   */
  public final void id(final String value) {
    this.id = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Returns the id of the entity instance.
   **
   ** @return                    the id of the entity instance.
   */
  public final String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  property           the name of the parameter to create a mapping
   **                            for on this instance.
   ** @param  value              the value for <code>property</code> to set on
   **                            this instance.
   **
   ** @throws BuildException     if the specified property id is already part of
   **                            the parameter mapping.
   */
  public void add(final FeatureProperty property, final String value)
    throws BuildException {

    // validate basic requirements
    if (property.required() && StringUtility.isEmpty(value))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MANDATORY, property.id()));

    // ensure inheritance and apply further validation
    super.addParameter(property.id(), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Sets the specified value pairs to the parameters that has to be applied.
   **
   ** @param  property           the name of the parameter to create a mapping
   **                            for on this instance.
   ** @param  value              the value for <code>property</code> to set on
   **                            this instance.
   */
  void set(final FeatureProperty property, final String value) {
    // add the value pair to the parameters
    this.parameter().put(property.id(), value);
  }
}