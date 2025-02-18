/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Unique Identifier Administration 

    File        :   RoleView.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    RoleView.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.uid.controller;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import javax.inject.Named;
import javax.inject.Inject;

import javax.faces.FacesException;

import javax.faces.view.ViewScoped;

import javax.faces.event.ActionEvent;

import org.primefaces.model.SortMeta;
import org.primefaces.model.FilterMeta;

import org.primefaces.event.SelectEvent;

import oracle.hst.platform.core.utility.CollectionUtility;

import oracle.hst.platform.jpa.SearchRequest;

import oracle.hst.platform.jsf.Message;

import bka.iam.identity.igs.model.User;
import bka.iam.identity.igs.model.Role;
import bka.iam.identity.igs.model.UserRole;

import bka.iam.identity.igs.api.RoleFacade;

import bka.iam.identity.uid.state.Preference;

import bka.iam.identity.uid.model.SearchModel;

////////////////////////////////////////////////////////////////////////////////
// class RoleView
// ~~~~~ ~~~~~~~~
/**
 ** The <code>RoleView</code> managed bean for maintain {@link Role}s.
 **
 ** @author dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@ViewScoped
@Named("role")
public class RoleView extends AbstractView<Role> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2421603909806814084")
  private static final long serialVersionUID = 360332336430133072L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Inject
  private RoleFacade        facade;

  private UserView          member;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class UserView
  // ~~~~~ ~~~~~~~~
  /**
   ** The <code>UserView</code> managed bean for maintain users associated with
   ** a {@link Role}.
   */
  public static class UserView extends SearchModel<UserRole> {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:2602838686164347598")
    private static final long serialVersionUID = -5666229834354248533L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private boolean              changed;
    private final List<UserRole> member;

    private final List<UserRole> assign  = new ArrayList<UserRole>();
    private final List<UserRole> remove  = new ArrayList<UserRole>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>UserView</code> to show the roles granted to a user.
     **
     ** @param  pageSize         the initial value of the page size to display.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  collection       the collection of {@link UserRole}s associated
     **                          with the current selected role.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is od type {@link UserRole}.
     */
    public UserView(final int pageSize, final List<UserRole> collection) {
      // ensure inheritance
      super(pageSize);

      // initialize instance attributes
      this.member = collection;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: getRowKey (overridden)
    /**
     ** Returns the identifier of the entry specified by <code>entry</code>.
     **
     ** @param  entry            the {@link UserRole} to return the identifier
     **                          for.
     **                          <br>
     **                          Allowed object is {@link UserRole}.
     **
     ** @return                  the identifier of the {@link UserRole}
     **                          <code>entry</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public String getRowKey(final UserRole entry) {
      return entry.getUser().getUserName();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getRowData (overridden)
     /**
     ** Returns an object representing the data for the currentry selected row
     ** index.
     ** <br>
     ** If row data is available, return the array element at the index
     ** specified by <code>identifier</code>.
     ** <br>
     ** If no wrapped data is available, return <code>null</code>.
     **
     ** @param  identifier       the identifier of an {@link UserRole} bean to
     **                          lookup.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the {@link UserRole} bean belonging to the
     **                          specified <code>identifier</code>.
     **                          <br>
     **                          Possible object is {@link User}.
     **
     ** @throws FacesException           if an error occurs getting the row
     **                                  data.
     ** @throws IllegalArgumentException if now row data is available at the
     **                                  currently specified row index.
     */
    @Override
    public UserRole getRowData(final String identifier) {
      // prevent bogus input
      if (identifier == null)
        throw new FacesException("getRowData");

      for (UserRole cursor : getWrappedData()) {
        if (identifier.equals(cursor.getUser().getUserName())) {
          return cursor;
        }
      }
      return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: count  (LazyDataModel)
    /**
     ** Counts the all available data for the given filters.
     ** <p>
     ** In case of SQL, this would execute a "SELECT COUNT ... WHERE ...".
     ** <p>
     ** In case you dont use SQL and receive both <code>rowCount</code> and
     ** <code>data</code> within a single call, this method should just return
     ** <code>0</code>.
     ** <br>
     ** You must call {@link #recalculateFirst(int, int, int)} and
     ** {@link #setRowCount(int)} in your
     ** {@link #load(int, int, java.util.Map, java.util.Map)} method.
     **
     ** @param  filter           a {@link Map} with all filter information (only
     **                          relevant for <code>DataTable</code>, not for
     **                          eg. <code>DataView</code>).
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element is of type {@link String} as the key
     **                          and {@link FilterMeta} for the value.
     **
     ** @return                  the data row count.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public int count(final Map<String, FilterMeta> filter) {
      return this.member.size();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: load (LazyDataModel)
    /**
     ** Loads the data for the given parameters.
     **
     ** @param  start            the 0-based index of the first search result.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  count            the page size.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  sort             a {@link Map} with all sort information (only
     **                          relevant for <code>DataTable</code>, not for
     **                          eg. <code>DataView</code>).
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element is of type {@link String} as the key
     **                          and {@link SortMeta} for the value.
     ** @param  filter           a {@link Map} with all filter information (only
     **                          relevant for <code>DataTable</code>, not for
     **                          eg. <code>DataView</code>).
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element is of type {@link String} as the key
     **                          and {@link FilterMeta} for the value.
     **
     ** @return                  the data.
     **                          <br>
     **                          Possible object is {@link List} where each
     **                          element is of type {@link UserRole}.
     */
    @Override
    public List<UserRole> load(final int start, final int count, final Map<String, SortMeta> sort, final Map<String, FilterMeta> filter) {
      recalculateFirst(start, count, this.member.size());
      return this.member;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   refresh (SearchModel)
    /**
     ** Callback method invoke by the action <code>refresh</code> in the UI to
     ** update the {@link Role} view with the current state of the persistence
     ** layer.
     */
    @Override
    public void refresh() {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: discard
    /**
     ** Callback method invoke by the action <code>undo</code> in the UI to
     ** update the {@link Role} view with the current state of the persistence
     ** layer.
     */
    public void discard() {
      // remove any potentially assigned users from the model
      this.member.removeAll(this.assign);
      // push back any potentially revoked users to the model
      this.member.addAll(this.remove);
      // reset the view
      reset();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: assign
    /**
     ** Callback method invoke by the action <code>assign</code> in the UI to
     ** assign a user to a {@link Role}.
     **
     ** @param  user             the user to assign to the current selected
     **                          role.
     **                          <br>
     **                          Allowed object is {@link UserRole}.
     */
    public void assign(final UserRole user) {
      // mark the user as an candidate to assign if its an already persisted
      // entity
      // mark the user as an candidate to assign
      if (!this.remove.contains(user))
        this.assign.add(user);
      // always remove it from the entities that recently removed but not
      // persisted
      this.remove.remove(user);
      // always add it to the current view
      this.member.add(user);
      this.changed = true;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: revoke
    /**
     ** Callback method invoke by the action <code>revoke</code> in the UI to
     ** revoke the selected user from a {@link Role}.
     */
    public void revoke() {
      if (this.selected != null) {
        // mark the role as an candidate to delete if its an already persisted
        // entity
        if (!this.assign.contains(this.selected))
          this.remove.add(this.selected);

        // always remove it from the potentially added but not persisted
        // entities
        this.assign.remove(this.selected);
        // always remove it from the current view
        this.member.remove(this.selected);
        this.changed = true;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   reset (overridden)
    /**
     ** Method invoke by the action callbacks to reset the state of the current
     ** view with the current state of the persistence layer.
     */
    @Override
    protected void reset() {
      this.assign.clear();
      this.remove.clear();
      this.changed  = false;
      // ensure inheritance
      super.reset();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RoleView</code> that allows use as a JavaBean.
   **
   ** @param  preference         the preferences for the view.
   **                            <br>
   **                            Allowed object is {@link Preference}.
   */
  @Inject
  public RoleView(final Preference preference) {
    // ensure inheritance
    super(preference);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isChanged (overridden)
  /**
   ** Returns <code>true</code> if the view is changed to by the user.
   **
   ** @return                    <code>true</code> if the view is changed to by
   **                            the user.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean isChanged() {
    return this.changed || (this.member != null && this.member.changed);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRowKey (overridden)
  /**
   ** Returns the identifier of the entry specified by <code>entry</code>.
   **
   ** @param  entry              the {@link Role} to return the identifier for.
   **                            <br>
   **                            Allowed object is {@link Role}.
   **
   ** @return                    the identifier of the {@link Role}
   **                            <code>entry</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getRowKey(final Role entry) {
    return entry.getId();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRowData (overridden)
  /**
   ** Returns an object representing the data for the currentry selected row
   ** index.
   ** <br>
   ** If row data is available, return the array element at the index
   ** specified by <code>identifier</code>.
   ** <br>
   ** If no wrapped data is available, return <code>null</code>.
   **
   ** @param  identifier         the identifier of an {@link Role} bean to
   **                            lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Role} bean belonging to the
   **                            specified <code>identifier</code>.
   **                            <br>
   **                            Possible object is {@link Role}.
   **
   ** @throws FacesException           if an error occurs getting the row
   **                                  data.
   ** @throws IllegalArgumentException if now row data is available at the
   **                                  currently specified row index.
   */
  @Override
  public Role getRowData(final String identifier) {
    // prevent bogus input
    if (identifier == null)
      throw new FacesException("getRowData");

    for (Role cursor : getWrappedData()) {
      if (identifier.equals(cursor.getId())) {
        return cursor;
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSelected (overridden)
  /**
   ** Sets the selected table entry.
   **
   ** @param  value              the selected table entry.
   **                            <br>
   **                            Allowed object is {@link Role}.
   */
  @Override
  public void setSelected(final Role value) {
    super.setSelected(value);
    if (value != null)
      this.member = new UserView(this.preference.getPageSize(), value.getUser());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMember
  /**
   ** Returns an object representing the grantees of a {@link Role}
   **
   ** @return                    the {@link UserRole} beans belonging to the
   **                            {@link Role} under control.
   **                            <br>
   **                            Possible object is {@link UserView}.
   **
   ** @throws FacesException           if an error occurs getting the row
   **                                  data.
   ** @throws IllegalArgumentException if now row data is available at the
   **                                  currently specified row index.
   */
  public UserView getMember() {
    return this.member;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   count  (LazyDataModel)
  /**
   ** Counts the all available data for the given filters.
   ** <p>
   ** In case of SQL, this would execute a "SELECT COUNT ... WHERE ...".
   ** <p>
   ** In case you dont use SQL and receive both <code>rowCount</code> and
   ** <code>data</code> within a single call, this method should just return
   ** <code>0</code>.
   ** <br>
   ** You must call {@link #recalculateFirst(int, int, int)} and
   ** {@link #setRowCount(int)} in your
   ** {@link #load(int, int, java.util.Map, java.util.Map)} method.
   **
   ** @param  filter             a {@link Map} with all filter information (only
   **                            relevant for <code>DataTable</code>, not for
   **                            eg. <code>DataView</code>).
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link FilterMeta} for the value.
   **
   ** @return                    the data row count.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int count(final Map<String, FilterMeta> filter) {
    return this.facade.count(applyFilter(filter));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   load (LazyDataModel)
  /**
   ** Loads the data for the given parameters.
   **
   ** @param  start              the 0-based index of the first search result.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  count              the page size.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  sort               a {@link Map} with all sort information (only
   **                            relevant for <code>DataTable</code>, not for
   **                            eg. <code>DataView</code>).
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link SortMeta} for the value.
   ** @param  filter             a {@link Map} with all filter information (only
   **                            relevant for <code>DataTable</code>, not for
   **                            eg. <code>DataView</code>).
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link FilterMeta} for the value.
   **
   ** @return                    the data.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Role}.
   */
  @Override
  public List<Role> load(final int start, final int count, final Map<String, SortMeta> sort, final Map<String, FilterMeta> filter) {
    return this.facade.list(SearchRequest.of(start, count, Boolean.FALSE, applyFilter(filter), applySort(sort)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh (SearchModel)
  /**
   ** Callback method invoke by the action <code>refresh</code> in the UI to
   ** update the {@link Role} view with the current state of the persistence
   ** layer.
   */
  @Override
  public void refresh() {
    this.member.reset();
    reset();
    Message.show(Message.information(Message.resourceValue(APP, "action.refresh.label"), Message.resourceValue(UID, "rol.refresh.feedback")));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create (AbstractView)
  /**
   ** Callback method invoke by the action <code>new</code> in the UI to create
   ** a new {@link Role}.
   */
  public void create() {
    this.modify   = false;
    this.selected = new Role();
    this.selected.setActive(Boolean.TRUE);
    // the created role needs an empty user collection to be operational
    // the standard Collections behavior is not an option due to the collection
    // assigned here needs to be modifiable
    this.selected.setUser(CollectionUtility.list());
    // create the view model of assigned users
    onSelected();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete (AbstractView)
  /**
   ** Callback method invoke by the action <code>delete</code> in the UI to
   ** delete the selected {@link Role}.
   */
  @Override
  public void delete() {
    try {
      this.facade.delete(this.selected);
      Message.show(Message.information(Message.resourceValue(APP, "action.delete.label"), Message.resourceValue(UID, "rol.deleted.feedback"), this.selected.getId()));
      reset();
    }
    catch (Exception e) {
      Message.error(Message.resourceValue(APP, "action.delete.label"), e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   save (AbstractView)
  /**
   ** Action method invoke by the action <code>submit</code> in the UI to
   ** persist a created or modified {@link Role}.
   **
   ** @param  event              the {@link ActionEvent} represents the
   **                            activation of a user interface component (such
   **                            as a UICommand).
   **                            <br>
   **                            Allowed object is {@link ActionEvent}.
   */
  @Override
  @SuppressWarnings("unused") 
  public void save(final ActionEvent event) {
    try {
      if (this.modify) {
        this.facade.modify(this.selected);
        Message.show(Message.information(Message.resourceValue(APP, "action.modify.label"), Message.resourceValue(UID, "rol.modified.feedback"), this.selected.getId()));
      }
      else {
        this.facade.create(this.selected);
        this.modify = true;
        Message.show(Message.information(Message.resourceValue(APP, "action.create.label"), Message.resourceValue(UID, "rol.created.feedback"), this.selected.getId()));
      }
      this.changed = false;
      // reset the users view so that the tracked changes disappear
      this.member.reset();
    }
    catch (Exception e) {
      // rollback the users view to its modified state
      this.member.discard();
      Message.show(Message.error(Message.resourceValue(APP, this.modify ? "action.modify.label" : "action.create.label"), e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by funtionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onSelected
  /**
   ** Perform action to populte the users that are member to the selected role
   ** entity.
   */
  public void onSelected() {
    if (this.selected != null)
      this.member = new UserView(this.preference.getPageSize(), this.selected.getUser());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onUserPick
  /**
   ** Action method invoke by the action <code>onUserPick</code> in the UI to
   ** lookup a {@link User}.
   */
  public void onUserPick() {
    showPicker("/page/usr/picker.jsf");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onUserReturn
  /**
   ** Perform action to assign the user under control as a member to the
   ** selected role entity.
   **
   ** @param  event                the {@link SelectEvent} represents the
   **                              selection of user interface component (such
   **                              as a UICommand).
   **                              <br>
   **                              Allowed object is {@link SelectEvent}.
   */
  public void onUserReturn(final SelectEvent event) {
    if (event.getObject() != null) {
      this.member.assign(UserRole.build((User)event.getObject(), this.selected));
    }
  }
}