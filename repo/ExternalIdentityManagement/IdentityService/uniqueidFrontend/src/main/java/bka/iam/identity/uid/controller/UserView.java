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

    File        :   UserView.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    UserView.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.uid.controller;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

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
import bka.iam.identity.igs.model.Language;

import bka.iam.identity.igs.api.UserFacade;

import bka.iam.identity.uid.state.Domain;

import bka.iam.identity.uid.state.Preference;

import bka.iam.identity.uid.model.SearchModel;

////////////////////////////////////////////////////////////////////////////////
// class UserView
// ~~~~~ ~~~~~~~~
/**
 ** The <code>UserView</code> managed bean for maintain {@link User}s.
 **
 ** @author dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@ViewScoped
@Named("user")
public class UserView extends AbstractView<User> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:3847195815890448382")
  private static final long serialVersionUID = 8245747319156785317L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Inject
  private UserFacade        facade;

  @Inject
  private Domain            domain;

  private List<Language>    locale;
  private RoleView          memberOf;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class RoleView
  // ~~~~~ ~~~~~~~~
  /**
   ** The <code>RoleView</code> managed bean for maintain roles associated with
   ** a {@link User}.
   */
  public static class RoleView extends SearchModel<UserRole> {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:4286628331215372110")
    private static final long serialVersionUID = 3593133159899664852L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private boolean              changed;
    private final List<UserRole> memberOf;

    private final List<UserRole> assign  = new ArrayList<UserRole>();
    private final List<UserRole> remove  = new ArrayList<UserRole>();
    
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>RoleView</code> to show the roles granted to a user.
     **
     ** @param  pageSize         the initial value of the page size to display.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  collection       the collection of {@link UserRole}s associated
     **                          with the current selected user.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is od type {@link UserRole}.
     */
    public RoleView(final int pageSize, final List<UserRole> collection) {
      // ensure inheritance
      super(pageSize);

      // initialize instance attributes
      this.memberOf = collection;
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
      return entry.getRole().getId();
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
        if (identifier.equals(cursor.getRole().getId())) {
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
      return this.memberOf.size();
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
      recalculateFirst(start, count, this.memberOf.size());
      return this.memberOf;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: refresh (SearchModel)
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
      // remove any potentially assigned roles from the model
      this.memberOf.removeAll(this.assign);
      // push back any potentially revoked roles to the model
      this.memberOf.addAll(this.remove);
      // reset the view
      reset();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: assign
    /**
     ** Callback method invoke by the action <code>assign</code> in the UI to
     ** assign a role to a {@link User}.
     **
     ** @param  role             the role to assign to the current selected
     **                          user.
     **                          <br>
     **                          Allowed object is {@link UserRole}.
     */
    public void assign(final UserRole role) {
      // mark the role as an candidate to assign if its an already persisted
      // entity
      // mark the role as an candidate to assign
      if (!this.remove.contains(role))
        this.assign.add(role);
      // always remove it from the entities that recently removed but not
      // persisted
      this.remove.remove(role);
      // always add it to the current view
      this.memberOf.add(role);
      this.changed = true;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: revoke
    /**
     ** Callback method invoke by the action <code>revoke</code> in the UI to
     ** revoke the selected role from a {@link User}.
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
        this.memberOf.remove(this.selected);
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
      this.changed = false;
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
   ** Constructs a <code>UserView</code> that allows use as a JavaBean.
   **
   ** @param  preference         the preferences for the view.
   **                            <br>
   **                            Allowed object is {@link Preference}.
   */
  @Inject
  public UserView(final Preference preference) {
    // ensure inheritance
    super(preference);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
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
    return this.changed || (this.memberOf != null && this.memberOf.changed);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRowKey (overridden)
  /**
   ** Returns the identifier of the entry specified by <code>entry</code>.
   **
   ** @param  entry              the {@link User} to return the identifier for.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @return                    the identifier of the {@link User}
   **                            <code>entry</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getRowKey(final User entry) {
    return String.valueOf(entry.getId());
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
   ** @param  identifier         the identifier of an {@link User} bean to
   **                            lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link User} bean belonging to the
   **                            specified <code>identifier</code>.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws FacesException           if an error occurs getting the row
   **                                  data.
   ** @throws IllegalArgumentException if now row data is available at the
   **                                  currently specified row index.
   */
  @Override
  public User getRowData(final String identifier) {
    // prevent bogus input
    if (identifier == null)
      throw new FacesException("getRowData");

    final Long lookup = Long.valueOf(identifier);
    for (User cursor : getWrappedData()) {
      if (lookup.equals(cursor.getId())) {
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
   **                            Allowed object is {@link User}.
   */
  @Override
  public void setSelected(final User value) {
    super.setSelected(value);
    onSelected();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLocale
  /**
   ** Returns an object representing of available languages for a {@link User}
   **
   ** @return                    the {@link Language} beans belonging to the
   **                            {@link User} under control.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Language}.
   */
  public List<Language> getLocale() {
    return this.locale;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMemberOf
  /**
   ** Returns an object representing the grants of a {@link User}
   **
   ** @return                    the {@link UserRole} beans belonging to the
   **                            {@link User} under control.
   **                            <br>
   **                            Possible object is {@link RoleView}.
   **
   ** @throws FacesException           if an error occurs getting the row
   **                                  data.
   ** @throws IllegalArgumentException if now row data is available at the
   **                                  currently specified row index.
   */
  public RoleView getMemberOf() {
    return this.memberOf;
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
   **                            element is of type {@link User}.
   */
  @Override
  public List<User> load(final int start, final int count, final Map<String, SortMeta> sort, final Map<String, FilterMeta> filter) {
    return this.facade.list(SearchRequest.of(start, count, Boolean.FALSE, applyFilter(filter), applySort(sort)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh (SearchModel)
  /**
   ** Callback method invoke by the action <code>refresh</code> in the UI to
   ** update the {@link User} view with the current state of the persistence
   ** layer.
   */
  @Override
  public void refresh() {
    this.memberOf.reset();
    reset();
    Message.show(Message.information(Message.resourceValue(APP, "action.refresh.label"), Message.resourceValue(UID, "usr.refresh.feedback")));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create (AbstractView)
  /**
   ** Callback method invoke by the action <code>new</code> in the UI to create
   ** a new {@link User}.
   */
  @Override
  public void create() {
    this.modify   = false;
    this.selected = new User();
    this.selected.setActive(Boolean.TRUE);
    // the created user needs an empty role collection to be operational
    // the standard Collections behavior is not an option due to the collection
    // assigned here needs to be modifiable
    this.selected.setRole(CollectionUtility.list());
    // create the view model of assigned roles
    onSelected();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete (AbstractView)
  /**
   ** Callback method invoke by the action <code>delete</code> in the UI to
   ** delete the selected {@link User}.
   */
  @Override
  public void delete() {
    try {
      this.facade.delete(this.selected);
      Message.show(Message.information(Message.resourceValue(APP, "action.delete.label"), Message.resourceValue(UID, "usr.deleted.feedback"), this.selected.getUserName()));
      refresh();
    }
    catch (Exception e) {
      Message.error(Message.resourceValue(APP, "action.delete.label"), e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   save (AbstractView)
  /**
   ** Action method invoke by the action <code>submit</code> in the UI to
   ** persist a created or modified {@link User}.
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
        Message.show(Message.information(Message.resourceValue(APP, "action.modify.label"), Message.resourceValue(UID, "usr.modified.feedback"), this.selected.getUserName()));
      }
      else {
        this.facade.create(this.selected);
        this.modify = true;
        Message.show(Message.information(Message.resourceValue(APP, "action.create.label"), Message.resourceValue(UID, "usr.created.feedback"), this.selected.getUserName()));
      }
      this.changed = false;
      // reset the roles view so that the tracked changes disappear
      this.memberOf.reset();
    }
    catch (Exception e) {
      // rollback the roles view to its modified state
      this.memberOf.discard();
      Message.show(Message.error(Message.resourceValue(APP, this.modify ? "action.modify.label" : "action.create.label"), e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by funtionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize
  /**
   ** Callback notification to signal dependency injection is done to perform
   ** any initialization and the instance becomes to put in service.
   */
  @PostConstruct
  public void initialize() {
    this.locale = this.domain.getLocale();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onSelected
  /**
   ** Perform action to populte the roles that are granted to the selected user
   ** entity.
   */
  public void onSelected() {
    if (this.selected != null)
      this.memberOf = new RoleView(this.preference.getPageSize(), this.selected.getRole());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onRolePick
  /**
   ** Action method invoke by the action <code>pickRole</code> in the UI to
   ** lookup a {@link Role}.
   */
  public void onRolePick() {
    showPicker("/page/rol/picker.jsf");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onRoleReturn
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
  public void onRoleReturn(final SelectEvent event) {
    if (event.getObject() != null) {
      this.memberOf.assign(UserRole.build(this.selected, (Role)event.getObject()));
    }
  }
}