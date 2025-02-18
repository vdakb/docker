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

    File        :   AbstractAttributeCriterion.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractAttributeCriterion.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    0.0.0.1     2013-05-31  DSteding    First release version
*/

package oracle.hst.foundation.faces.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import oracle.adf.view.rich.model.AttributeCriterion;
import oracle.adf.view.rich.model.AttributeDescriptor;

////////////////////////////////////////////////////////////////////////////////
// class AbstractAttributeCriterion
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** An <code>AbstractAttributeCriterion</code> contains information specific to
 ** a search field that is based off an attribute as defined by an
 ** {@link AttributeDescriptor}.
 ** <p>
 ** A search field contains various parts such as a label, a list of operators
 ** that are relevant to its attribute's type, one or more value fields (date or
 ** number support searching between ranges for e.g.).
 ** <p>
 ** This interface defines a contract that a model implementor needs to support
 ** to facilitate displaying such a search field.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class AbstractAttributeCriterion extends AttributeCriterion {

  private AttributeDescriptor.Operator operator;
  private final AttributeDescriptor    attributeDescriptor;
  private final List<? extends Object> values;
  private final boolean                removable;

  public AbstractAttributeCriterion(final AttributeDescriptor attributeDescriptor, final List<? extends Object> values, final boolean removable) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.attributeDescriptor = attributeDescriptor;
    this.values              = values;
    this.removable           = removable;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isRemovable (AttributeCriterion)
  /**
   ** Whether the criterion can be removed.
   ** <p>
   ** A <code>true</code> value indicates that the user can remove the criterion
   ** from a QueryDescriptor. For e.g., if Criterion objects that are part of a
   ** system QueryDescriptor cannot be removed then this method returns
   ** <code>false</code>.
   **
   ** @return                    <code>true</code> if the criterion can be
   **                            removed; otherwise <code>false</code>.
   */
  @Override
  public boolean isRemovable() {
    return this.removable;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOperator (AttributeCriterion)
  /**
   ** Sets a value for the operator to use for a search field when performing a
   ** query. This determines the condition used in the search criteria.
   **
   ** @param  operator           the default operator to use for the search
   **                            field.
   */
  @Override
  public void setOperator(final AttributeDescriptor.Operator operator) {
    this.operator = operator;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOperator (AttributeCriterion)
  /**
   ** Rets the default operator to use for the search field.
   ** <p>
   ** This is particularly useful when performing a quick query.
   **
   ** @return                    the default operator to use for the search
   **                            field.
   */
  @Override
  public AttributeDescriptor.Operator getOperator() {
    return this.operator;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOperators (AttributeCriterion)
  /**
   ** Gets a map of operators supported by a criterion.
   ** <p>
   ** A criterion may choose to support only a subset of operators supported by
   ** its associated attribute or may choose to support an entirely different
   ** set of operators all together.
   **
   ** @return                    a {@link Map}&lt;String, AttributeDescriptor.Operator&gt;
   **                            of operators supported by a criterion.
   */
  @Override
  public Map<String, AttributeDescriptor.Operator> getOperators() {
    return Collections.emptyMap();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAttribute (AttributeCriterion)
  /**
   ** Returns the {@link AttributeDescriptor} instance that this
   ** {@link AttributeCriterion} is based on.
   **
   ** @return                    an {@link AttributeDescriptor} instance.
   */
  @Override
  public AttributeDescriptor getAttribute() {
    return this.attributeDescriptor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAttribute (AttributeCriterion)
  /**
   ** Returns a list of values to be used for a search field.
   ** <p>
   ** This is used when search fields display more than one value component for
   ** entry. For e.g., when searching between a range of values it is often
   ** required to present 2 entry fields for users to enter the range. For
   ** <code>boolean</code> data type if the componentType happens to be
   ** selectBooleanCheckbox, a {@link List} with a single element containing a
   ** <code>Boolean</code> value is expected.
   **
   ** @return                    a {@link List} of Objects.
   */
  public List<? extends Object> getValues() {
    return this.values;
  }
}
