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

    File        :   ParticipantView.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ParticipantView.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.uid.controller;

import java.util.Map;
import java.util.List;

import javax.inject.Named;
import javax.inject.Inject;

import javax.faces.FacesException;

import javax.faces.view.ViewScoped;

import javax.faces.event.ActionEvent;

import org.primefaces.model.SortMeta;
import org.primefaces.model.FilterMeta;

import oracle.hst.platform.jpa.SearchRequest;

import oracle.hst.platform.jsf.Message;

import bka.iam.identity.uid.state.Preference;

import bka.iam.identity.uid.model.Participant;

import bka.iam.identity.uid.api.ParticipantFacade;

////////////////////////////////////////////////////////////////////////////////
// class ParticipantView
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The <code>ParticipantView</code> managed bean for maintain
 ** {@link Participant}s.
 **
 ** @author dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@ViewScoped
@Named("participant")
public class ParticipantView extends AbstractView<Participant> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4881058624867872197")
  private static final long serialVersionUID = -1163004842283664932L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Inject
  private ParticipantFacade facade;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ParticipantView</code> that allows use as a JavaBean.
   **
   ** @param  preference         the preferences for the view.
   **                            <br>
   **                            Allowed object is {@link Preference}.
   */
  @Inject
  public ParticipantView(final Preference preference) {
    // ensure inheritance
    super(preference);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRowKey (overridden)
  /**
   ** Returns the identifier of the entry specified by <code>entry</code>.
   **
   ** @param  entry              the {@link Participant} to return the
   **                            identifier for.
   **                            <br>
   **                            Allowed object is {@link Participant}.
   **
   ** @return                    the identifier of the {@link Participant}
   **                            <code>entry</code>
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getRowKey(final Participant entry) {
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
   ** @param  identifier         the identifier of an {@link Participant} bean
   **                            to lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Participant} bean belonging to the
   **                            specified <code>identifier</code>.
   **                            <br>
   **                            Possible object is {@link Participant}.
   **
   ** @throws FacesException           if an error occurs getting the row
   **                                  data.
   ** @throws IllegalArgumentException if now row data is available at the
   **                                  currently specified row index.
   */
  @Override
  public Participant getRowData(final String identifier) {
    // prevent bogus input
    if (identifier == null)
      throw new FacesException("getRowData");

    for (Participant cursor : getWrappedData()) {
      if (identifier.equals(cursor.getId())) {
        return cursor;
      }
    }
    return null;
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
   **                            element is of type {@link Participant}.
   */
  @Override
  public List<Participant> load(final int start, final int count, final Map<String, SortMeta> sort, final Map<String, FilterMeta> filter) {
    return this.facade.list(SearchRequest.of(start, count, Boolean.FALSE, applyFilter(filter), applySort(sort)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh (SearchModel)
  /**
   ** Callback method invoke by the action <code>refresh</code> in the UI to
   ** update the {@link Participant} view with the current state of the
   ** persistence layer.
   */
  @Override
  public void refresh() {
    reset();
    Message.show(Message.information(Message.resourceValue(APP, "action.refresh.label"), Message.resourceValue(UID, "pts.refresh.feedback")));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create (AbstractView)
  /**
   ** Callback method invoke by the action <code>new</code> in the UI to create
   ** a new {@link Participant}.
   */
  @Override
  public void create() {
    this.modify   = false;
    this.selected = new Participant();
    this.selected.setActive(Boolean.TRUE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete (AbstractView)
  /**
   ** Callback method invoke by the action <code>delete</code> in the UI to
   ** delete the selected {@link Participant}.
   */
  @Override
  public void delete() {
    try {
      this.facade.delete(this.selected);
      Message.show(Message.information(Message.resourceValue(APP, "action.delete.label"), Message.resourceValue(UID, "pts.deleted.feedback"), this.selected.getId()));
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
   ** persist a created or modified {@link Participant}.
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
        Message.show(Message.information(Message.resourceValue(APP, "action.modify.label"), Message.resourceValue(UID, "pts.modified.feedback"), this.selected.getId()));
      }
      else {
        this.facade.create(this.selected);
        this.modify = true;
        Message.show(Message.information(Message.resourceValue(APP, "action.create.label"), Message.resourceValue(UID, "pts.created.feedback"), this.selected.getId()));
      }
      this.changed = false;
    }
    catch (Exception e) {
      Message.show(Message.error(Message.resourceValue(APP, this.modify ? "action.modify.label" : "action.create.label"), e.getLocalizedMessage()));
    }
  }
}