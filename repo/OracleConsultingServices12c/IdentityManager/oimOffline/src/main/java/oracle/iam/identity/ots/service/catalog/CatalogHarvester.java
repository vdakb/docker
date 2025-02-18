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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Offline Target Connector

    File        :   CatalogHarvester.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    CatalogHarvester.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.service.catalog;

import oracle.iam.identity.foundation.offline.CatalogListener;

////////////////////////////////////////////////////////////////////////////////
// abstract class CatalogHarvester
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The <code>CatalogHarvester</code> implements the base functionality of a
 ** service end point for the Oracle Identity Manager Scheduler which handles
 ** a XML file either to export or import Catalog Item definitions.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
abstract class CatalogHarvester extends    Harvester
                                implements CatalogListener {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on a scheduled task to specify if
   ** application instances needes to be handled unmarshalling from or
   ** mashalling to an XML file.
   */
  protected static final String PROCESS_APPLICATION  = "Process Application Instances";

  /**
   ** Attribute tag which must be defined on a scheduled task to specify if
   ** entitlements needes to be handled unmarshalling from or mashalling to an
   ** XML file.
   */
  protected static final String PROCESS_ENTITELEMENT = "Process Entitlements";

  /**
   ** Attribute tag which must be defined on a scheduled task to specify if
   ** roles needes to be handled unmarshalling from or mashalling to an XML
   ** file.
   */
  protected static final String PROCESS_ROLE         = "Process Roles";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final Summary       catalog              = new Summary();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>CatalogHarvester</code> with the specified data
   ** direction.
   **
   ** @param  inbound            <code>true</code> if this {@link Harvester} is
   **                            importing catalog data; otherwise
   **                            <code>false</code>.
   */
  public CatalogHarvester(final boolean inbound) {
    // ensure inheritance
    super(inbound);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nullValue (EntityListener)
  /**
   ** Returns the value which represents a <code>null</code> for an attribute
   ** element.
   ** <p>
   ** Such specification is required to distinct between empty attribute
   ** elements which are not passed through and overriding an already existing
   ** metadata to make it <code>null</code>.
   **
   ** @return                    the value which represents a <code>null</code> for
   **                            an attribute element.
   */
  @Override
  public String nullValue() {
    return ((HarvesterDescriptor)this.descriptor).nullValue();
  }
}