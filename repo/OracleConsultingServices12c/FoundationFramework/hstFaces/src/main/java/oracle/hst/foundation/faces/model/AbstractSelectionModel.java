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
                    AbstractSelectionModel.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    0.0.0.1     2013-05-31  DSteding    First release version
*/

package oracle.hst.foundation.faces.model;


import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;

import javax.faces.model.SelectItem;

import oracle.hst.foundation.faces.backing.AbstractBean;
import oracle.hst.foundation.faces.backing.AbstractModel;

public class AbstractSelectionModel<T extends AbstractModel> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final  AbstractBean   model;
  private final DataProvider<T> provider;

  private Map<Object, T>        data;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractSelectionModel</code> which use the specified
   ** {@link AbstractModel} as the model.
   **
   ** @param  model              the {@link AbstractModel} providing the model.
   ** @param  provider           the {@link DataProvider} providing the data.
   */
  public AbstractSelectionModel(final Class<T> model, final DataProvider<T> provider) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.model    = AbstractBean.create(model);
    this.provider = provider;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getModelBeanById
  public T getModelBeanById(final Object key) {
    return getData().get(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDataProvider
  public DataProvider<T> getDataProvider() {
    return this.provider;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSelectItems
  public List<SelectItem> getSelectItems() {
    final List<SelectItem> selectItems = new ArrayList<SelectItem>();
    final String id   = this.model.getIdentifier().getName();
    final String name = this.model.getDisplayName().getName();
    Collection<? extends AbstractModel> entries = getData().values();
    for (AbstractModel entry : entries) {
      selectItems.add(new SelectItem(entry.get(id), entry.get(name).toString()));
    }
    return selectItems;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getData
  private Map<Object, T> getData() {
    if (this.data == null) {
      this.data = new LinkedHashMap<Object, T>();
      List<T> entries = this.provider.search(null);
      for (T entry : entries) {
        this.data.put(entry.get(this.model.getIdentifier().getName()), entry);
      }
    }
    return this.data;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  public void refresh() {
    this.data = null;
  }
}
