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

    Copyright © 2013. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Consulting Services Foundation Shared Library
    Subsystem   :   Java Server Faces Foundation

    File        :   AbstractConjunctionCriterion.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractConjunctionCriterion.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    0.0.0.1     2013-05-31  DSteding    First release version
*/

package oracle.hst.foundation.faces.model;

import java.util.List;
import java.util.ArrayList;

import oracle.adf.view.rich.model.Criterion;
import oracle.adf.view.rich.model.ConjunctionCriterion;

////////////////////////////////////////////////////////////////////////////////
// class AbstractConjunctionCriterion
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Represents a group of criterion objects that use one conjuction operator
 ** between them. Since this class contains a list of {@link Criterion} objects
 ** it is possible to build complex hierarchies of {@link Criterion} objects as
 ** follows:
 ** <pre>
 **   entityCriterion E
 **     |__attributeCriterion E1 (and)
 **     |__entityCriterion E2
 **          |__attributeCriterion E21 (or)
 **          |__attributeCriterion E22 (or)
 **          |__attributeCriterion E22
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class AbstractConjunctionCriterion extends ConjunctionCriterion {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private ConjunctionCriterion.Conjunction conjunction = ConjunctionCriterion.Conjunction.AND;

  private final List<Criterion>            criterion   = new ArrayList<Criterion>();

  public AbstractConjunctionCriterion() {
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getKey (ConjunctionCriterion)
  /**
   ** Gets a unique key of the Criterion object within its list, for the current
   ** {@link ConjunctionCriterion}. If there are nested
   ** {@link ConjunctionCriterion} objects below the current level then calling
   ** this method should return the key of the {@link Criterion} object if it is
   ** found or <code>null</code>.
   ** <p>
   ** All Criterion objects can be retrieved from the root
   ** {@link ConjunctionCriterion}.
   **
   ** @param  criterion          the {@link Criterion} to search for.
   **
   ** @return                    a unique key identifying the {@link Criterion}
   **                            object. It's important that the key be unique
   **                            across all {@link Criterion} objects belonging
   **                            to the root {@link ConjunctionCriterion}.
   */
  @Override
  public Object getKey(final Criterion criterion) {
    return this.criterion.indexOf(criterion);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCriterion (ConjunctionCriterion)
  /**
   ** Returnss the {@link Criterion} object located at key within the list.
   ** <p>
   ** Calling this method from the root {@link ConjunctionCriterion} object will
   ** yield the {@link Criterion} object (nested at any level in the hierarchy)
   ** or <code>null</code> if not found. Calling this from a nested
   ** {@link ConjunctionCriterion} will delegate the call to the root
   ** {@link ConjunctionCriterion}.
   **
   ** @param  key                the unique key identifying the
   **                            {@link Criterion} to search for.
   **
   ** @return                    a {@link Criterion} object identified by the
   **                            key or <code>null</code> if there is no
   **                            {@link Criterion} for the specified key.
   */
  @Override
  public Criterion getCriterion(final Object key) {
    return this.criterion.get(Integer.parseInt(key.toString()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCriterionList (ConjunctionCriterion)
  /**
   ** Returns the List of {@link Criterion} objects that are children on this
   ** {@link ConjunctionCriterion}.
   **
   ** @return                    a  List of {@link Criterion}s.
   */
  @Override
  public List<Criterion> getCriterionList() {
    return this.criterion;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setConjunction (ConjunctionCriterion)
  /**
   ** Sets the conjunction to use with this {@link ConjunctionCriterion} object.
   **
   ** @param  conjunction        a ConjunctionType instance.
   */
  @Override
  public void setConjunction(final ConjunctionCriterion.Conjunction conjunction) {
    this.conjunction = conjunction;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getConjunction (ConjunctionCriterion)
  /**
   ** Returns a conjunction to be used with this {@link ConjunctionCriterion}
   ** object.
   **
   ** @return                    a ConjunctionType.
   */
  @Override
  public ConjunctionCriterion.Conjunction getConjunction() {
    return this.conjunction;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  public void addCriterion(final Criterion criterion) {
    this.criterion.add(criterion);
  }

  public void removeCriterion(final Criterion criterion) {
    this.criterion.remove(criterion);
  }
}
