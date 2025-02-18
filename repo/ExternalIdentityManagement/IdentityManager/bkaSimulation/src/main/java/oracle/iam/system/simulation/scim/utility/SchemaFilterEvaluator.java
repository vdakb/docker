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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   SchemaFilterEvaluator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SchemaFilterEvaluator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.utility;

import oracle.iam.system.simulation.scim.Path;

import oracle.iam.system.simulation.scim.annotation.Definition;

import oracle.iam.system.simulation.scim.object.Evaluator;

import oracle.iam.system.simulation.scim.v2.ResourceTypeDefinition;

////////////////////////////////////////////////////////////////////////////////
// class SchemaFilterEvaluator
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** A filter visitor that will evaluate a filter on a <code>JsonNode</code> and
 ** return whether the <code>JsonNode</code> matches the filter.
 ** <br>
 ** This filter visitor consults the schema aware to take case sensitivity in
 ** account.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SchemaFilterEvaluator extends Evaluator {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final ResourceTypeDefinition resourceType;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SchemaFilterEvaluator</code> handler that is aware
   ** of the schema of the specified {@link ResourceTypeDefinition}.
   **
   ** @param  resourceType       the resource type to take in account.
   **                            <br>
   **                            Allowed object is
   **                            {@link ResourceTypeDefinition}.
   */
  public SchemaFilterEvaluator(final ResourceTypeDefinition resourceType) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.resourceType = resourceType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   definition (overridden)
  /**
   ** Returns the attribute definition for the attribute specified by the path
   ** to determine case sensitivity during string matching.
   **
   ** @param  path               the path to the attribute whose definition to
   **                            retrieve.
   **                            <br>
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the attribute definition or <code>null</code>
   **                            if not available, in which case case
   **                            insensitive string value matching will be
   **                            performed.
   **                            <br>
   **                            Possible object is {@link Definition}.
   */
  @Override
  protected Definition definition(@SuppressWarnings("unused") final Path path) {
    return null;
  }
}