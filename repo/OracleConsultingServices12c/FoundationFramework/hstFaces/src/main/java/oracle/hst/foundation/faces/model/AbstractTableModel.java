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
                    AbstractTableModel.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    0.0.0.1     2013-05-31  DSteding    First release version
*/

package oracle.hst.foundation.faces.model;

import java.util.List;
import java.util.ArrayList;

import org.apache.myfaces.trinidad.model.ModelUtils;
import org.apache.myfaces.trinidad.model.CollectionModel;

import oracle.adf.view.rich.model.ColumnDescriptor;
import oracle.adf.view.rich.model.TableModel;

import oracle.hst.foundation.faces.backing.AbstractBean;
import oracle.hst.foundation.faces.backing.AbstractModel;

////////////////////////////////////////////////////////////////////////////////
// class AbstractTableModel
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** A <code>AbstractTableModel</code> model describes a model used to create a
 ** table dynamically.
 */
public class AbstractTableModel extends TableModel {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final AbstractBean            model;
  private final List<ColumnDescriptor>  columns;

  private List<? extends AbstractModel> entries;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractQueryModel</code> which use the specified
   ** {@link AbstractBean} as the model.
   **
   ** @param  model              the {@link AbstractBean} providing the model.
   ** @param  entries            the {@link List} of {@link AbstractModel}s to
   **                            associate with the model to build.
   */
  public AbstractTableModel(AbstractBean model, List<? extends AbstractModel> entries) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.model   = model;
    this.columns = new ArrayList<ColumnDescriptor>();
    for (String name : model.getAttributeNames()) {
      this.columns.add(new AbstractColumnDescriptor(model.getAbstractAttribute(name)));
    }
    this.entries = entries;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEntries
  public void setEntries(List<AbstractModel> entries) {
    this.entries = entries;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCollectionModel (TableModel)
  /**
   ** Returns the collection model used to set the data for the table component.
   **
   ** @return                    the collection model used to set the data for
   **                            the table component.
   */
  @Override
  public CollectionModel getCollectionModel() {
    return ModelUtils.toCollectionModel(this.entries);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getColumnDescriptors (TableModel)
  /**
   ** Returns the list of column descriptors used to create the column component
   ** inside the table,
   **
   ** @return                    the list of column descriptors used to create
   **                            the column component inside the table.
   */
  @Override
  public List<ColumnDescriptor> getColumnDescriptors() {
    return this.columns;
  }
}