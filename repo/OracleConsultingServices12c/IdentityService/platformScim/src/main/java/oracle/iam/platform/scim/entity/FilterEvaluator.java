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

    System      :   Identity Service Library
    Subsystem   :   Generic SCIM Interface

    File        :   FilterEvaluator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    FilterEvaluator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim.entity;

import oracle.iam.platform.scim.AttributeDefinition;
import oracle.iam.platform.scim.ResourceTypeDefinition;

////////////////////////////////////////////////////////////////////////////////
// class FilterEvaluator
// ~~~~~ ~~~~~~~~~~~~~~~
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
public class FilterEvaluator extends Evaluator {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final ResourceTypeDefinition type;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FilterEvaluator</code> that is aware of the schema of
   ** the specified {@link ResourceTypeDefinition}.
   **
   ** @param  type               the resource type to take in account.
   **                            <br>
   **                            Allowed object is
   **                            {@link ResourceTypeDefinition}.
   */
  private FilterEvaluator(final ResourceTypeDefinition type) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.type = type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a new <code>FilterEvaluator</code> that is aware
   ** of the schema of the specified {@link ResourceTypeDefinition}.
   **
   ** @param  type               the resource type to take in account.
   **                            <br>
   **                            Allowed object is
   **                            {@link ResourceTypeDefinition}.
   **
   ** @return                    an instance of
   **                            <code>ResourceComparator</code> populated with
   **                            the values provided.
   **                            <br>
   **                            Possible object is
   **                            <code>ResourcePreparer</code>.
   */
  public static final FilterEvaluator build(final ResourceTypeDefinition type) {
    return new FilterEvaluator(type);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   definition
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
   **                            Possible object is {@link AttributeDefinition}.
   */
  @SuppressWarnings("unused") 
  protected AttributeDefinition definition(final Path path) {
    return null;
  }
}