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

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractQueryDescriptor.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    0.0.0.1     2013-05-31  DSteding    First release version
*/

package oracle.hst.foundation.faces.model;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import oracle.adf.view.rich.model.Criterion;
import oracle.adf.view.rich.model.QueryDescriptor;
import oracle.adf.view.rich.model.AttributeCriterion;
import oracle.adf.view.rich.model.AttributeDescriptor;
import oracle.adf.view.rich.model.ConjunctionCriterion;

import oracle.hst.foundation.faces.backing.AbstractBean;
import oracle.hst.foundation.faces.backing.AbstractAttribute;

////////////////////////////////////////////////////////////////////////////////
// class AbstractQueryDescriptor
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** An <code>AbstractQueryDescriptor</code> describes a saved search by
 ** providing {@link Criterion} objects (used to render search fields; by
 ** providing an ability to add remove {@link Criterion} objects. In addition,
 ** it also provides methods to change the definition of a
 ** {@link QueryDescriptor}, such as its name, UI hints and mode.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class AbstractQueryDescriptor extends QueryDescriptor {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String                 name;
  private final AbstractBean           model;

  private AbstractConjunctionCriterion conjunction;
  private AttributeCriterion           current;
  private Map<String, Object>          hints;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractQueryDescriptor</code> which use the specified
   ** {@link AbstractBean} as the model.
   **
   ** @param  model              the {@link AbstractBean} providing the model.
   ** @param  name               the name of {@link QueryDescriptor}.
   */
  public AbstractQueryDescriptor(final AbstractBean model, final String name) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.model = model;
    this.name  = name;

    reset();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getName (QueryDescriptor)
  /**
   ** Returns the name of the {@link QueryDescriptor}.
   ** <p>
   ** Each {@link QueryDescriptor} has a unique name associated with it. This
   ** also is the name of the saved search.
   **
   ** @return                    the name of {@link QueryDescriptor}.
   */
  @Override
  public String getName() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUIHints (QueryDescriptor)
  /**
   ** Return a {@link Map} of UI hints to values, for this
   ** {@link QueryDescriptor}. Currently, the following hints are supported:
   ** <ul>
   **   <li>UIHINT_AUTO_EXECUTE
   **   <li>UIHINT_DEFAULT
   **   <li>UIHINT_IMMUTABLE
   **   <li>UIHINT_MODE
   **   <li>UIHINT_NAME
   **   <li>UIHINT_RESULTS_COMPONENT_ID
   **   <li>UIHINT_SAVE_RESULTS_LAYOUT
   **   <li>UIHINT_SHOW_IN_LIST
   ** </ul>
   **
   ** @return                    a {@link Map}&lt;String, Object&gt; pairs.
   */
  @Override
  public Map<String, Object> getUIHints() {
    return this.hints;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changeMode (QueryDescriptor)
  /**
   ** Called when the {@link QueryMode} changes.
   ** <p>
   ** This method is invoked during the 'Invoke Application' phase of the JSF
   ** lifecyle.
   **
   ** @param  mode               the new value for {@link QueryMode}.
   */
  @Override
  public void changeMode(final QueryDescriptor.QueryMode mode) {
    this.hints.put(UIHINT_MODE, mode);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addCriterion (QueryDescriptor)
  /**
   ** Adds a criterion to the current {@link QueryDescriptor}.
   ** <p>
   ** This method is invoked during the 'Invoke Application' phase. Subclasses
   ** can create a new {@link Criterion} based on the
   ** {@link AttributeDescriptor} (retrieved using the name). The query
   ** component registers an internal ActionListener to invoke this method when
   ** the user chooses to add an attribute ({@link AttributeDescriptor}) as a
   ** search field ({@link AttributeDescriptor}).
   **
   ** @param  name               the name of the attribute that is to be added
   **                            as a {@link Criterion} to the search criteria.
   **
   ** @see    AttributeCriterion
   ** @see    AttributeDescriptor
   */
  @Override
  public void addCriterion(final String name) {
    final AbstractAttribute   attribute  = this.model.getAbstractAttribute(name);
    final AttributeDescriptor descriptor = new AbstractAttributeDescriptor(attribute);
    final AttributeCriterion  criterion  = new AbstractAttributeCriterion(descriptor, Arrays.asList(""), true);
    this.conjunction.addCriterion(criterion);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeCriterion (QueryDescriptor)
  /**
   ** Removes a criterion/item from the {@link QueryDescriptor}.
   ** <p>
   ** This method is invoked during the 'Invoke Application' phase. The query
   ** component registers an internal ActionListener to invoke this method when
   ** the user chooses to delete a search field.
   **
   ** @param  object             the criterion instance that is to be removed.
   **                            Typically an AttributeCriterion.
   **
   ** @see    AttributeCriterion
   */
  @Override
  public void removeCriterion(final Criterion object) {
    this.conjunction.removeCriterion(object);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getConjunctionCriterion (QueryDescriptor)
  /**
   ** Returns the {@link ConjunctionCriterion} object associated with the
   ** {@link QueryDescriptor}.
   ** <p>
   ** A {@link ConjunctionCriterion} contains one or more {@link Criterion}
   ** objects (and possibly other {@link ConjunctionCriterion} objects) combined
   ** using a conjunction operator.
   **
   ** @return                    the {@link ConjunctionCriterion} object
   **                            associated with the {@link QueryDescriptor}.
   **
   ** @see    ConjunctionCriterion
   */
  @Override
  public ConjunctionCriterion getConjunctionCriterion() {
    return this.conjunction;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCurrentCriterion (QueryDescriptor)
  /**
   ** Sets the {@link AttributeCriterion} object as the current one.
   ** This is used when performing queries against a single {@link Criterion}
   ** object (as is the case in the quickQuery mode). Values entered (or
   ** defaults set) in other {@link Criterion} objects may be ignored.
   ** <p>
   ** Additionally its an implementation detail whether the argument passed in
   ** is used as the current or its copied into a separate instance. A separate
   ** instance may be desirable if the values entered by the user for this
   ** {@link Criterion} alone, should not in any way change the values entered
   ** for this {@link Criterion} object when used with a
   ** {@link ConjunctionCriterion}.
   **
   ** @param  criterion          an {@link AttributeCriterion} instance to use
   **                            as the current one. A <code>null</code> value
   **                            throws an <code>IllegalArgumentException</code>.
   */
  @Override
  public void setCurrentCriterion(final AttributeCriterion criterion) {
    this.current = criterion;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCurrentCriterion (QueryDescriptor)
  /**
   ** Returns the {@link Criterion} to use as the default or the current
   ** criterion. This is used when performing searches on a single
   ** {@link Criterion} object rather than a collection as is the case with
   ** {@link ConjunctionCriterion}.
   ** <p>
   ** It is an implementation detail whether a separate instance of
   ** {@link Criterion} is returned or the one belonging to the
   ** List&lt;Criterion&gt; returned by
   ** <code>ConjunctionCriterion.getCriterionList()</code>. A separate instance
   ** may be desired if the values returned for this {@link AttributeCriterion}
   ** are meant to be different from when the {@link AttributeCriterion} is used
   ** as part of a collection (in a {@link ConjunctionCriterion}).
   **
   ** @return                    a valid {@link AttributeCriterion} or
   **                            <code>null</code>.
   **
   ** @see    AttributeCriterion
   */
  @Override
  public AttributeCriterion getCurrentCriterion() {
    return this.current;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  public void reset() {
    final QueryDescriptor.QueryMode currentMode = (QueryDescriptor.QueryMode)this.hints.get(UIHINT_MODE);
    this.conjunction = new AbstractConjunctionCriterion();
    this.hints       = new HashMap<String, Object>();
    this.hints.put(UIHINT_AUTO_EXECUTE,        false);
    this.hints.put(UIHINT_DEFAULT,             false);
    this.hints.put(UIHINT_MODE,                QueryDescriptor.QueryMode.BASIC);
    this.hints.put(UIHINT_SHOW_IN_LIST,        true);
    this.hints.put(UIHINT_NAME,                name);
    this.hints.put(UIHINT_IMMUTABLE,           true);
    this.hints.put(UIHINT_SAVE_RESULTS_LAYOUT, false);
    for (String name : this.model.getAttributeNames()) {
      final AbstractAttribute attribute = model.getAbstractAttribute(name);
      if (attribute.isQueryable()) {
        AttributeDescriptor descriptor = new AbstractAttributeDescriptor(attribute);
        AttributeCriterion  criterion  = new AbstractAttributeCriterion(descriptor, Arrays.asList(""), false);
        this.conjunction.addCriterion(criterion);
      }
    }
    this.hints.put(UIHINT_MODE, currentMode);
  }
}
