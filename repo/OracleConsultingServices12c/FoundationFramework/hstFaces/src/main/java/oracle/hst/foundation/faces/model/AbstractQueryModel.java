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
                    AbstractQueryModel.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    0.0.0.1     2013-05-31  DSteding    First release version
*/

package oracle.hst.foundation.faces.model;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import oracle.adf.view.rich.model.QueryModel;
import oracle.adf.view.rich.model.QueryDescriptor;
import oracle.adf.view.rich.model.AttributeDescriptor;

import oracle.hst.foundation.faces.backing.AbstractBean;
import oracle.hst.foundation.faces.backing.AbstractAttribute;

////////////////////////////////////////////////////////////////////////////////
// class AbstractQueryModel
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** The model for query component, it is used to manage {@link QueryDescriptor}
 ** objects.
 ** <p>
 ** A user can perform various operations on saved searches while interacting
 ** with a query component. These actions include creating, deleting,
 ** duplicating, selecting, resetting and updating a saved search.
 ** <p>
 ** The above actions result in a {@link QueryDescriptor} object to be created,
 ** deleted, duplicated, updated, reset or retrieved. The methods that perform
 ** the above actions are typically called during the 'Invoke Application'
 ** phase of the JSF lifecycle.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class AbstractQueryModel extends QueryModel {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final AbstractBean              model;
  private final List<AttributeDescriptor> attributes;
  private final List<QueryDescriptor>     systemQueries;
  private final List<QueryDescriptor>     userQueries;
  private       QueryDescriptor           current;

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
   ** @param  systemQueries      the {@link List} of {@link QueryDescriptor}s
   **                            providing the data.
   */
  public AbstractQueryModel(final AbstractBean model, final List<QueryDescriptor> systemQueries) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.model         = model;
    this.systemQueries = systemQueries;
    this.userQueries   = new ArrayList<QueryDescriptor>();
    if (this.systemQueries.size() > 0) {
      this.current = systemQueries.get(0);
    }
    this.attributes = new ArrayList<AttributeDescriptor>();
    for (String name : model.getAttributeNames()) {
      AbstractAttribute attribute = model.getAbstractAttribute(name);
      if (attribute.isQueryable()) {
        this.attributes.add(new AbstractAttributeDescriptor(attribute));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCurrentDescriptor (QueryDescriptor)
  /**
   ** Returns the current descriptor on the {@link QueryModel}.
   **
   ** @return                     the current descriptor on the
   **                             {@link QueryModel}.
   */
  public QueryDescriptor getCurrentDescriptor() {
    return this.current;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reset (QueryDescriptor)
  /**
   ** Resets the {@link QueryDescriptor} to its last saved state.
   ** <p>
   ** This method is invoked during the 'Invoke Application' phase of the JSF
   ** lifecyle. Subclasses can override this method to reset the
   ** {@link QueryDescriptor} to its original state.
   **
   ** @param  descriptor         the {@link QueryDescriptor} to be restored to
   **                            its last saved state.
   */
  @Override
  public void reset(final QueryDescriptor descriptor) {
    if (descriptor instanceof AbstractQueryDescriptor) {
      ((AbstractQueryDescriptor)descriptor).reset();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create (QueryDescriptor)
  /**
   ** Creates or retrieves a {@link QueryDescriptor} with the specified name.
   **
   ** @param  name                the unique name to use for the (about to be)
   **                             cloned {@link QueryDescriptor} or the name of
   **                             an existing {@link QueryDescriptor} if
   **                             retrieving an existing one.
   ** @param  descriptor          the {@link QueryDescriptor} that is used to
   **                             clone the new one or <code>null</code> (when
   **                             retrieving an existing one).
   **
   ** @return                     a {@link QueryDescriptor} newly created/cloned
   **                             {@link QueryDescriptor} or an existing
   **                             {@link QueryDescriptor}.
   */
  @Override
  public QueryDescriptor create(final String name, final QueryDescriptor descriptor) {
    this.userQueries.add(descriptor);
    return descriptor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   update (QueryDescriptor)
  /**
   ** Updates the {@link QueryDescriptor} using the changed UIHint values.
   ** <p>
   ** This method is called when the user personalizes a saved search by
   ** changing its name or its UI hints. This method is invoked during the
   ** 'Invoke Application' phase of the JSF lifecyle. The query component
   ** registers an internal listener for the QueryOperationEvent and invokes
   ** this method.
   ** <br>
   ** <b>NOTE</b>: For more information regarding the event and its contents
   **              refer to QueryOperationEvent.
   **
   ** @param  descriptor          the {@link QueryDescriptor} object to updated.
   ** @param  hint                the {@link Map} of uiHints that contain the
   **                             changed values.
   **
   **
   ** @see    QueryDescriptor
   */
  @Override
  public void update(final QueryDescriptor descriptor, final Map<String, Object> hint) {
    hint.remove(QueryDescriptor.UIHINT_NAME);
    descriptor.getUIHints().putAll(hint);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete (QueryDescriptor)
  /**
   ** Removes the specified QueryDescriptor.
   ** <p>
   ** This method in the 'Invoke Application' phase of the JSF lifecycle. The
   ** query component registers an internal listener for the QueryOperationEvent
   ** and invokes this method.
   ** <br>
   ** <b>NOTE</b>: For more information regarding the event and its contents
   **              refer to QueryOperationEvent.
   **
   ** @param  descriptor         the {@link QueryDescriptor} instance to be
   **                            deleted.
   */
  @Override
  public void delete(final QueryDescriptor descriptor) {
    this.userQueries.remove(descriptor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCurrentDescriptor (QueryDescriptor)
  /**
   ** Sets the current descriptor on the {@link QueryModel}.
   ** <p>
   ** If the queryDescriptor, is not valid or part of the {@link QueryModel},
   ** then current {@link QueryDescriptor} remains unchanged.
   **
   ** @param  descriptor          the {@link QueryDescriptor} that will be the
   **                             current descriptor.
   */
  @Override
  public void setCurrentDescriptor(final QueryDescriptor descriptor) {
    this.current = descriptor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAttributes (QueryDescriptor)
  /**
   ** Returns the List of {@link AttributeDescriptor} objects by their names.
   ** <p>
   ** An {@link AttributeDescriptor} represents an attribute of a business
   ** object in general terms or a view object (in ADF) and provides methods
   ** useful to render a component based on that attribute.
   **
   ** @return                    the List of {@link AttributeDescriptor} objects
   **                            by their names.
   **
   ** @see    AttributeDescriptor
   */
  @Override
  public List<AttributeDescriptor> getAttributes() {
    return this.attributes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSystemQueries (QueryDescriptor)
  /**
   ** Returns the list of {@link QueryDescriptor} objects that are 'system saved
   ** searches'.
   ** <p>
   ** System saved searches are typically setup by the administrator and come
   ** pre-cofigured with an application. They can also not be deleted by users
   ** although these restrictions are left to the discretion of application
   ** designers and model implementors.
   **
   ** @return                    a List of {@link QueryDescriptor} objects or
   **                            <code>null</code> or empty list.
   */
  @Override
  public List<QueryDescriptor> getSystemQueries() {
    return this.systemQueries;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUserQueries (QueryDescriptor)
  /**
   ** Returns the list of {@link QueryDescriptor} objects that are
   ** 'user saved searches'.
   ** <p>
   ** User saved searches are created by the user and its associated
   ** {@link QueryDescriptor} object can be updated by users.
   ** <br>
   ** When a <code>null</code> is returned by this method, then certain features
   ** on the query component are disabled. Please refer to the query component
   ** documentation for more details.
   **
   ** @return                    a List of {@link QueryDescriptor} objects or
   **                            <code>null</code> or empty list.
   */
  @Override
  public List<QueryDescriptor> getUserQueries() {
    return this.userQueries;
  }
}
