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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   CatalogElement.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    CatalogElement.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import org.apache.tools.ant.BuildException;

import oracle.iam.platform.utils.vo.OIMType;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceOperation;

import oracle.hst.deployment.spi.AbstractInstance;

////////////////////////////////////////////////////////////////////////////////
// class CatalogElement
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** <code>CatalogElement</code> represents a element of the catalog in
 ** Identity Manager that might be created, deleted or changed after or during
 ** an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class CatalogElement extends AbstractInstance {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the internal type for the <code>Catalog Element</code> */
  private OIMType type;

  /**
   ** the internal identifier never in the user interface but needed to handle
   ** assignment and/or revocations.
   */
  private String  entityID    = null;

  /**
   ** the string displayed for the <code>Catalog Element</code> displayed in the
   ** user interface
   */
  private String  displayName = null;

  /**
   ** the description of the <code>Catalog Element</code> displayed in the user
   ** interface
   */
  private String  description = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CatalogElement</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public CatalogElement() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CatalogElement</code> fora specific type
   **
   ** @param  type               the <code>Catalog Type</code> an
   **                            <code>Catalog Element</code> belongs to.
   */
  protected CatalogElement(final OIMType type) {
    // ensure inheritance
    super();

    // initialize instance
    this.type = type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Called to inject the argument for parameter <code>type</code>.
   **
   ** @param  type               the type of the catalog element in Identity
   **                            Manager to handle.
   */
  public void type(final String type) {
    type(OIMType.valueOf(type));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Called to inject the argument for parameter <code>type</code>.
   **
   ** @param  type               the type of the catalog element in Identity
   **                            Manager to handle.
   */
  public void type(final OIMType type) {
    this.type = type;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns type of the catalog element in Identity Manager to handle.
   **
   ** @return                    the type of the catalog element in Identity
   **                            Manager to handle.
   */
  public final OIMType type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entityID
  /**
   ** Called to inject the <code>entityID</code> of the
   ** <code>Entitlement</code> related to Identity Manager.
   **
   ** @param  entityID           the internal identifier passed to Identity
   **                            Manager to handle enrolements.
   */
  public void entityID(final String entityID) {
    this.entityID = entityID;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entityID
  /**
   ** Returns the <code>entityID</code> to handle enrolments of an
   ** <code>Entitlement</code>.
   **
   ** @return                    the internal identifier passed to Identity
   **                            Manager to handle enrolements.
   */
  public String entityID() {
    return this.entityID;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   displayName
  /**
   ** Called to inject the <code>displayName</code> of the
   ** <code>Entitlement</code> related to Identity Manager.
   **
   ** @param  displayName        the <code>displayName</code> presented in the
   **                            user interface for an <code>Entitlement</code>.
   */
  public void displayName(final String displayName) {
    this.displayName = displayName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   displayName
  /**
   ** Returns the <code>displayName</code> presented in the user interface for
   ** an <code>Entitlement</code>.
   **
   ** @return                    the <code>displayName</code> presented in the
   **                            user interface for an <code>Entitlement</code>.
   */
  public String displayName() {
    return this.displayName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Called to inject the <code>description</code> of the
   ** <code>Entitlement</code> related to Identity Manager.
   **
   ** @param  description        the string displayed in the user interface as
   **                            the description.
   */
  public void description(final String description) {
    this.description = description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the <code>description</code> presented in the user interface for
   ** an <code>Entitlement</code>.
   **
   ** @return                    the <code>description</code> presented in the
   **                            user interface for an
   **                            <code>Entitlement</code>.
   */
  public String description() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the type to use.
   ** <p>
   ** The validation is performed in two ways depended on the passed in mode
   ** requested by argument <code>strict</code>. If <code>strict</code> is set
   ** to <code>true</code> the validation is extended to check for all the
   ** mandatory attributes of an application instance like display name,
   ** description, etc. If it's <code>false</code> only the name of the
   ** application instance has to be present.
   **
   ** @param  operation          the operational mode of validation.
   **                            If it's set to <code>create</code> the
   **                            validation is extended to check for all the
   **                            mandatory parameters of a catalog element like
   **                            displayName etc. If it's something else only
   **                            the name of the catalog element has to be
   **                            present.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  public void validate(final ServiceOperation operation)
    throws BuildException {

    // ensure inhertitance
    super.validate();

    // enforce validation of mandatory attributes if requested
    if (operation == ServiceOperation.create) {
      if (StringUtility.isEmpty(this.displayName))
        handleAttributeMissing("displayName");

      if (StringUtility.isEmpty(this.description))
        handleAttributeMissing("description");
    }
  }
}