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

    Copyright © 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extensions
    Subsystem   :   Identity and Access Management Facilities

    File        :   OptionListSelectionModel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    OptionListSelectionModel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.list;

import javax.swing.ListModel;
import javax.swing.DefaultListSelectionModel;

////////////////////////////////////////////////////////////////////////////////
// class OptionListSelectionModel
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>OptionTreeSelectionModel</code> is a selection model based on
 ** {@link DefaultListSelectionModel} and used in {@link OptionList} to keep
 ** track of the checked tree paths.
 ** <p>
 ** Alterations of a node state may propagate to descendants/ancestors,
 ** according to the behaviour of the propagation model.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class OptionListSelectionModel extends DefaultListSelectionModel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:6608877258300422406")
  private static final long    serialVersionUID = 986175173296419112L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final     OptionList list;
  private transient ListModel  model;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a {@link DefaultListSelectionModel}.
   **
   ** @param  list               the {@link OptionList} this selection model
   **                            supports.
   */
  public OptionListSelectionModel(final OptionList list) {
    // ensure inheritance
    super();

    // initialize instance
    this.list = list;
    model(this.list.getModel());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   model
  /**
   ** Sets the {@link ListModel} that will provide the data.
   ** <p>
   ** The current selection set is cleared.
   **
   ** @param  model              the {@link ListModel} that is to provide the
   **                            data.
   */
  public void model(final ListModel model) {
    int oldLength = (this.model == null) ? 0 : this.model.getSize();
    int newLength = (model == null)      ? 0 : model.getSize();

    this.model = model;

    if (oldLength > newLength)
      removeIndexInterval(newLength, oldLength);

//    clearSelection();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   model
  /**
   ** Returns the {@link ListModel} that is providing the data.
   **
   ** @return                    the {@link ListModel} that is to provide the
   **                            data.
   */
  public ListModel model() {
    return this.model;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   insertIndexInterval (overridden)
  /**
   ** Insert length indices beginning before/after index.
   ** <p>
   ** If the value  at index is itself selected and the selection mode is not
   ** SINGLE_SELECTION, set all of the newly inserted items as selected.
   ** Otherwise leave them unselected. This method is typically called to sync
   ** the selection model with a corresponding change in the data model.
   ** <p>
   ** Overriden so that inserting a row will not be selected automatically if
   ** the row after it is selected.
   **
   ** @param  index              the index where the rows will be inserted.
   ** @param  length             the number of the rows that will be inserted.
   ** @param  before             it's before or after the index.
   */
  @Override
  public void insertIndexInterval(final int index, final int length, final boolean before) {
    if (before) {
      boolean old = isSelectedIndex(index);
      // indicate that upcoming selection changes should be considered part of
      // a single change
      super.setValueIsAdjusting(true);
      try {
        if (old)
          // changes the selection to be the set difference of the current
          // selection
          removeSelectionInterval(index, index);

        // insert length indices
        super.insertIndexInterval(index, length, before);

        if (old)
          addSelectionInterval(index + length, index + length);
      }
      finally {
        // indicate that upcoming selection changes should not need to
        // considered as part of a single change
        super.setValueIsAdjusting(false);
      }
    }
    else {
      super.insertIndexInterval(index, length, before);
    }
  }
}