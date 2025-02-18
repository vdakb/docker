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

    File        :   TenantView.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TenantView.


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

import oracle.hst.platform.jsf.Message;

import oracle.hst.platform.jpa.SearchRequest;

import bka.iam.identity.igs.model.Role;
import bka.iam.identity.igs.model.User;
import bka.iam.identity.igs.model.Claim;
import bka.iam.identity.igs.model.Tenant;

import bka.iam.identity.uid.api.TenantFacade;

import bka.iam.identity.uid.state.Preference;

import bka.iam.identity.uid.model.SearchModel;

////////////////////////////////////////////////////////////////////////////////
// class TenantView
// ~~~~~ ~~~~~~~~~~
/**
 ** The <code>TenantView</code> managed bean for maintain {@link Tenant}s.
 **
 ** @author dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@ViewScoped
@Named("tenant")
public class TenantView extends AbstractView<Tenant> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2608316750259500664")
  private static final long serialVersionUID = -8833017608382915116L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Inject
  private TenantFacade      facade;

  private ClaimView         claim;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class ClaimView
  // ~~~~~ ~~~~~~~~~
  /**
   ** The <code>ClaimView</code> managed bean for maintain users associated with
   ** a {@link Tenant}.
   */
  public static class ClaimView extends SearchModel<Claim> {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-4761106807688550458")
    private static final long serialVersionUID = -2641695484288042568L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private boolean           changed;
    private final List<Claim> current;

    private final List<Claim> assign  = new ArrayList<Claim>();
    private final List<Claim> revoke  = new ArrayList<Claim>();
    
    
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>ClaimView</code> to show the users granted to a
     ** tenant.
     **
     ** @param  pageSize         the initial value of the page size to display.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  collection       the collection of {@link Claim}s associated
     **                          with the current selected tenant.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is od type {@link Claim}.
     */
    public ClaimView(final int pageSize, final List<Claim> collection) {
      // ensure inheritance
      super(pageSize);

      // initialize instance attributes
      this.current = collection;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: getRowKey (overridden)
    /**
     ** Returns the identifier of the entry specified by <code>entry</code>.
     **
     ** @param  entry            the {@link Claim} to return the identifier
     **                          for.
     **                          <br>
     **                          Allowed object is {@link Claim}.
     **
     ** @return                  the identifier of the {@link Claim}
     **                          <code>entry</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public String getRowKey(final Claim entry) {
      // the table is beside the metadata attributes driven by foreign keys
      // unfortunately Primefaces expects a String as the identifying key hence
      // we have to compose a surrogate to fulfill this requirement
      return String.format("%s#%d#%s", entry.getTenant().getId(), entry.getUser().getId(), entry.getRole());
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
     ** @param  identifier       the identifier of an {@link Claim} bean to
     **                          lookup.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the {@link Claim} bean belonging to the
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
    public Claim getRowData(final String identifier) {
      // prevent bogus input
      if (identifier == null)
        throw new FacesException("getRowData");

      // the table is beside the metadata attributes driven by foreign keys
      // unfortunately Primefaces expects a String as the identifying key hence
      // we have to decompose the surrogate build by getRowKey
      final String[] segment = identifier.split("#");
      if (segment.length == 3) {
        final Claim.Identifier lookup = Claim.Identifier.of(segment[0], Long.valueOf(segment[1]), segment[2]);
        for (Claim cursor : getWrappedData()) {
          if (lookup.equals(cursor.getId())) {
            return cursor;
          }
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
      return this.current.size();
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
     **                          element is of type {@link Claim}.
     */
    @Override
    public List<Claim> load(final int start, final int count, final Map<String, SortMeta> sort, final Map<String, FilterMeta> filter) {
      recalculateFirst(start, count, this.current.size());
      return this.current;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   refresh (SearchModel)
    /**
     ** Callback method invoke by the action <code>refresh</code> in the UI to
     ** update the {@link Tenant} view with the current state of the persistence
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
      this.current.removeAll(this.assign);
      // push back any potentially revoked users to the model
      this.current.addAll(this.revoke);
      reset();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: assign
    /**
     ** Callback method invoke by the action <code>assign</code> in the UI to
     ** assign a user to a {@link Tenant}.
     **
     ** @param  claim            the claim to assign to the current selected
     **                          tenant.
     **                          <br>
     **                          Allowed object is {@link Claim}.
     */
    public void assign(final Claim claim) {
      // mark the claim as an candidate to assign if its an already persisted
      // entity
      // mark the claim as an candidate to assign
      if (!this.revoke.contains(claim))
        this.assign.add(claim);
      // always remove it from the entities that recently removed but not
      // persisted
      this.revoke.remove(claim);
        // always add it to the current view
      this.current.add(claim);
      this.changed = true;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: revoke
    /**
     ** Callback method invoke by the action <code>revoke</code> in the UI to
     ** revoke the selected user from a {@link Tenant}.
     */
    public void revoke() {
      if (this.selected != null) {
        // mark the claim as an candidate to delete if its an already persisted
        // entity
        if (!this.assign.contains(this.selected))
          this.revoke.add(this.selected);

        // always remove it from the potentially added but not persisted
        // entities
        this.assign.remove(this.selected);
        // always remove it from the current view
        this.current.remove(this.selected);
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
      this.revoke.clear();
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
   ** Constructs a <code>TenantView</code> that allows use as a JavaBean.
   **
   ** @param  preference         the preferences for the view.
   **                            <br>
   **                            Allowed object is {@link Preference}.
   */
  @Inject
  public TenantView(final Preference preference) {
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
    return this.changed || (this.claim != null && this.claim.changed);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRowKey (overridden)
  /**
   ** Returns the identifier of the entry specified by <code>entry</code>.
   **
   ** @param  entry              the {@link Tenant} to return the identifier
   **                            for.
   **                            <br>
   **                            Allowed object is {@link Tenant}.
   **
   ** @return                    the identifier of the {@link Tenant}
   **                            <code>entry</code>
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getRowKey(final Tenant entry) {
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
   ** @param  identifier         the identifier of an {@link Tenant} bean to
   **                            lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Tenant} bean belonging to the
   **                            specified <code>identifier</code>.
   **                            <br>
   **                            Possible object is {@link Tenant}.
   **
   ** @throws FacesException           if an error occurs getting the row
   **                                  data.
   ** @throws IllegalArgumentException if now row data is available at the
   **                                  currently specified row index.
   */
  @Override
  public Tenant getRowData(final String identifier) {
    // prevent bogus input
    if (identifier == null)
      throw new FacesException("getRowData");

    for (Tenant cursor : getWrappedData()) {
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
   **                            Allowed object is {@link Tenant}.
   */
  @Override
  public void setSelected(final Tenant value) {
    super.setSelected(value);
    onSelected();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getClaim
  /**
   ** Returns an object representing the claims of a {@link Tenant}
   **
   ** @return                    the {@link Claim} beans belonging to the
   **                            {@link Tenant} under control.
   **                            <br>
   **                            Possible object is {@link ClaimView}.
   **
   ** @throws FacesException           if an error occurs getting the row
   **                                  data.
   ** @throws IllegalArgumentException if now row data is available at the
   **                                  currently specified row index.
   */
  public ClaimView getClaim() {
    return this.claim;
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
   ** <br>
   ** The parameters of this method are used to build a JPA query, which should
   ** return data, relative to what the user is looking for in the table. The
   ** key parameter in this process is a <i>Map%lt;String, FilterMeta&gt;</i>
   ** <code>filter</code>, which actually contains all criteria specified by the
   ** user, along with operators for each attribute (Less than, equals, ...).
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
   **                            element is of type {@link Tenant}.
   */
  @Override
  public List<Tenant> load(final int start, final int count, final Map<String, SortMeta> sort, final Map<String, FilterMeta> filter) {
    return this.facade.list(SearchRequest.of(start, count, Boolean.FALSE, applyFilter(filter), applySort(sort)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh (SearchModel)
  /**
   ** Callback method invoke by the action <code>refresh</code> in the UI to
   ** update the {@link Tenant} view with the current state of the persistence
   ** layer.
   */
  @Override
  public void refresh() {
    this.claim.reset();
    reset();
    Message.show(Message.information(Message.resourceValue(APP, "action.refresh.label"), Message.resourceValue(UID, "tnt.refresh.feedback")));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create (AbstractView)
  /**
   ** Callback method invoke by the action <code>new</code> in the UI to
   ** create a new {@link Tenant}.
   */
  @Override
  public void create() {
    this.modify   = false;
    this.selected = new Tenant();
    this.selected.setActive(true);
    // the created tenant needs an empty claim collection to be operational
    // the standard Collections behavior is not an option due to the collection
    // assigned here needs to be modifiable
    this.selected.setClaim(CollectionUtility.list());
    // create the view model of assigned claims
    onSelected();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete (AbstractView)
  /**
   ** Callback method invoke by the action <code>delete</code> in the UI to
   ** delete the selected {@link Tenant}.
   */
  @Override
  public void delete() {
    try {
      this.facade.delete(this.selected);
      Message.show(Message.information(Message.resourceValue(APP, "action.delete.label"), Message.resourceValue(UID, "tnt.deleted.feedback"), this.selected.getId()));
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
   ** persist a created or modified {@link Tenant}.
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
        // modify the tenant itself
        this.facade.modify(this.selected);
        Message.show(Message.information(Message.resourceValue(APP, "action.modify.label"), Message.resourceValue(UID, "tnt.modified.feedback"), this.selected.getId()));
      }
      else {
        // create the tenant itself
        this.facade.create(this.selected);
        this.modify = true;
        Message.show(Message.information(Message.resourceValue(APP, "action.create.label"), Message.resourceValue(UID, "tnt.created.feedback"), this.selected.getId()));
      }
      this.changed = false;
      // reset the claims view so that the tracked changes disappear
      this.claim.reset();
    }
    catch (Exception e) {
      // rollback the claims view to its modified state
      this.claim.discard();
      Message.show(Message.error(Message.resourceValue(APP, this.modify ? "action.modify.label" : "action.create.label"), e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by funtionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onSelected
  /**
   ** Perform action to refersh the claims assigned to a tenant.
   */
  public void onSelected() {
    if (this.selected != null)
      this.claim = new ClaimView(this.preference.getPageSize(), this.selected.getClaim());
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
   ** Perform action to assign the selected user as a claim to the tenant entity
   ** under control.
   **
   ** @param  event                the {@link SelectEvent} represents the
   **                              selection of user interface component (such
   **                              as a UICommand).
   **                              <br>
   **                              Allowed object is {@link SelectEvent}.
   */
  public void onUserReturn(final SelectEvent event) {
    if (event.getObject() != null) {
      this.claim.assign(Claim.build(this.selected, (User)event.getObject(), "uid.register"));
    }
  }
}