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

    Copyright © 2016. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   System Provisioning Management

    File        :   AdministratorState.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AdministratorState.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.sysprov.svr.backing;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import java.io.Serializable;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;

import oracle.adf.view.rich.event.DialogEvent;

import oracle.adf.view.rich.util.ResetUtils;

import oracle.iam.identity.vo.Identity;

import oracle.iam.ui.common.model.role.RoleAdapterBean;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.hst.foundation.faces.JSF;
import oracle.hst.foundation.faces.ADF;

import oracle.iam.identity.frontend.train.AbstractSearch;

import oracle.iam.identity.sysprov.schema.EndpointAdapter;
import oracle.iam.identity.sysprov.schema.EndpointAdministratorAdapter;

import oracle.iam.identity.sysprov.svr.state.Train;

////////////////////////////////////////////////////////////////////////////////
// class AdministratorState
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Declares methods the user interface service provides to manage the execution
 ** administrators of <code>IT Resource</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class AdministratorState extends AbstractSearch {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String  PICK_ROLE        = "catalog_role";
  public static final String  PICK_ENTITY      = "endpointEntity";

  private static final String PICK_FLOW        = "/WEB-INF/oracle/iam/ui/common/tfs/role-picker-tf.xml#role-picker-tf";

  private static final String ACCESS           = "accessMode";
  private static final String ACCESS_READ      = "read";
  private static final String ACCESS_WRITE     = "write";
  private static final String ACCESS_DELETE    = "delete";

  private static final String BUNDLE           = "oracle.iam.identity.bundle.Provisioning";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-8605262650792496833")
  private static final long   serialVersionUID = 4385856266006638934L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean             displayInfo;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AdministratorState</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AdministratorState() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDisplayInfo
  /**
   ** Sets the value of the displayInfo property.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @param  value              the value of the displayInfo property.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  public void setDisplayInfo(final boolean value) {
    this.displayInfo = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isDisplayInfo
  /**
   ** Returns the value of the displayInfo property.
   ** <p>
   ** Method needs to be declared as <b><code>public</code></b> because it used
   ** in the UI.
   **
   ** @return                    the value of the displayInfo.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean isDisplayInfo() {
    final String mode = ADF.pageFlowScopeStringValue(PARAMETER_MODE);
    if (MODE_VIEW.equals(mode) || MODE_EDIT.equals(mode) || (MODE_CREATE.equals(mode) && iteratorHasRows("AdministratorIterator")))
      this.displayInfo = false;
    else
      this.displayInfo = true;
    return this.displayInfo;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   isRevokeDisabled
  /**
   ** Whether the delete button of an IT Resource relationship is disabled.
   **
   ** @return                    <code>true</code> revoke button of a
   **                            relationship is disabled; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean isRevokeDisabled() {
    return !(getSelectedRowCount() > 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getLocalizedMessage
  /**
   ** Returns the localized action message.
   **
   ** @return                    the localized action message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getLocalizedMessage() {
    String localized = train().getActionName();
    if (StringUtility.isEmpty(localized))
      return "???-emptyornull-???";

    switch (localized) {
      case GENERIC_DELETE : return ADF.resourceBundleValue(BUNDLE, "SVR_ADMINISTRATOR_DELETE_CONFIRM");
      case GENERIC_UNDO   : return ADF.resourceBundleValue(BUNDLE, "SVR_ADMINISTRATOR_UNDO_CONFIRM");
      default             : return String.format("???-%s-???", localized);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entityDistinguisher
  /**
   ** Factory method to create a unique entity distinguisher to provide the
   ** possibility to identify the taskflow which raised the contextual event
   ** to pick a administrative role by the event handler.
   ** <br>
   ** The event handler itself is registered for every region.
   ** <p>
   ** The string of the created identifier consists of
   ** <ol>
   **   <li>the hardcoded prefix <code>svr</code>
   **   <li>the instance identifier of the current taskflow obtained from the
   **       page flow scope.
   ** </ol>
   **
   ** @return                    the created unique event name.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String entityDistinguisher() {
    return String.format("svr#%s", ADF.pageFlowScopeStringValue(EndpointAdapter.PK));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pickListener
  /**
   ** Raise a <code>Contextual Event</code> programmatically to launch the
   ** taskflow to select administrative roles to be added.
   **
   ** @param  event              the {@link ActionEvent} delivered from the
   **                            action occured in a UIXForm component.
   **                            <br>
   **                            Allowed object is {@link ActionEvent}.
   */
  public void pickListener(final @SuppressWarnings("unused") ActionEvent event) {
    ADF.pageFlowScopeStringValue(PICK_ENTITY, entityDistinguisher());
    final Map<String, Object> parameter = new HashMap<String, Object>();
    // it's required to distinct the taskflow of the picker to give the event
    // handler the ability to bypass taskflows which didn't raise the event due
    // to that the event handler is registered for every region and there can be
    // more than one taskflow on the same page
    parameter.put(EVENT_DISTINGUISHER, PICK_ROLE);
    parameter.put(EVENT_SELECTIONTYPE, SELECTIONTYPE_MULTIPLE);
    raiseTaskFlowLaunchEvent(PICK_ROLE, PICK_FLOW, ADF.resourceBundleValue(BUNDLE, "SVR_ADMINISTRATOR_PICKER"), "/images/add_ena.png", null, null, true, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dialogListener
  /**
   ** Perfoms all actions belonging to the UI to reflect action events belonging
   ** to entries in the value table after confirmation.
   **
   ** @param  event              the {@link DialogEvent} object that
   **                            characterizes the action to perform.
   **                            <br>
   **                            Allowed object is {@link DialogEvent}.
   */
  public void dialogListener(final DialogEvent event) {
    if (DialogEvent.Outcome.yes.equals(event.getOutcome())) {
      final String    actionName = train().getActionName();
      final List<Row> selection  = selectedRow(getSearchTable());
      switch(actionName) {
        case GENERIC_DELETE : remove(selection);
                              break;
        case GENERIC_UNDO   : undo(selection);
                              break;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createListener
  /**
   ** Create a administrator belonging to a certain <code>IT Resource</code>.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   **                            <br>
   **                            Allowed object is {@link ActionEvent}.
   */
  public void createListener(final ActionEvent event) {
    final Train                        train = train();
    final EndpointAdministratorAdapter mab   = new EndpointAdministratorAdapter();

    // keep the row created above in the cache so that the row survives the next
    // refresh request
    final Map<Long, EndpointAdministratorAdapter> assign = train.administratorAssign();
    assign.put(mab.getGroupKey(), mab);
    refreshListener(event);

    train.markDirty();
    ADF.partialRender(getSearchTable());
    partialRenderAction();
    partialRenderSubmitRevert();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revert (overridden)
  /**
   ** Reverts all changes belonging to the administrator region of the
   ** <code>IT Resource</code>.
   */
  @Override
  public void revert() {
    train().clearAdministrator();
    refresh();
    refreshRegion();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshListener (overridden)
  /**
   ** Refresh the history belonging to a certain <code>Scheduled Endpoint</code>.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   **                            <br>
   **                            Allowed object is {@link ActionEvent}.
   */
  @Override
  public void refreshListener(final ActionEvent event) {
    refresh();
    // ensure inheritance
    super.refreshListener(event);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   assign
  /**
   ** Adds the given targets to the collection of administrative roles to be
   ** allowed.
   **
   ** @param  selection          the collection of administrative roles to be
   **                            added to the scopes to be allowed.
   **                            <br>
   **                            Allowed object is {@link List} here each
   **                            element is of type {@link Serializable}.
   */
  public void assign(final List<Serializable> selection) {
    if (!CollectionUtility.empty(selection)) {
      final Train                                   train   = train();
      final Map<Long, EndpointAdministratorAdapter> assign  = train.administratorAssign();
      final Map<Long, EndpointAdministratorAdapter> remove  = train.administratorRemove();
      final Map<Long, EndpointAdministratorAdapter> modify  = train.administratorModify();
      boolean                                       changed = false;
      for (int i = 0; i < selection.size(); i++) {
        final Identity                     cursor = ((RoleAdapterBean)selection.get(i)).getIdentity();
        final EndpointAdministratorAdapter mab    = new EndpointAdministratorAdapter();
        mab.setEndpointKey(Long.valueOf(ADF.pageFlowScopeStringValue(EndpointAdapter.PK)));
        mab.setGroupKey((Long)cursor.getAttribute("Role Key"));
        mab.setGroupName((String)cursor.getAttribute("Role Name"));
        mab.setReadAccess(Boolean.TRUE);
        mab.setWriteAccess(Boolean.TRUE);
        mab.setDeleteAccess(Boolean.TRUE);
        mab.setPendingAction(EndpointAdministratorAdapter.ADD);
        final Long scope = mab.getGroupKey();
        if (remove.containsKey(scope)) {
          remove.remove(scope);
          mab.setPendingAction(EndpointAdministratorAdapter.MOD);
          modify.put(scope, mab);
          changed = true;
        }
        // prevent adding the same role twice to avoid potential
        // conflicts in checking the pending action
        if (!(assign.containsKey(scope) || modify.containsKey(scope))) {
          mab.setPendingAction(EndpointAdministratorAdapter.ADD);
          assign.put(scope, mab);
          changed = true;
        }
      }
      if (changed) {
        train.markDirty();
        refresh();
        refreshRegion();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accessChanged
  /**
   ** Perfoms all actions belonging to the UI to reflect value change events on
   ** particular components in the attribute region.
   **
   ** @param  event              the {@link ValueChangeEvent} object that
   **                            characterizes the action to perform.
   **                            <br>
   **                            Allowed object is {@link ValueChangeEvent}.
   */
  public void accessChanged(final ValueChangeEvent event) {
    if (event.getNewValue() == null || (event.getOldValue() != null && event.getNewValue().toString().equals(event.getOldValue().toString())))
      return;
 
    final Train                                   train      = train();
    final Boolean                                 value      = (Boolean)event.getNewValue();
    final Map<String, Object>                     attributes = event.getComponent().getAttributes();
    final String                                  scope      = (attributes.get(EndpointAdministratorAdapter.UGP_PK) == null) ? null : attributes.get(EndpointAdministratorAdapter.UGP_PK).toString();
    final String                                  mode       = attributes.get(ACCESS).toString();
    final Map<Long, EndpointAdministratorAdapter> assign     = train.administratorAssign();
    final Map<Long, EndpointAdministratorAdapter> modify     = train.administratorModify();
    if (assign.containsKey(scope)) {
      if (ACCESS_READ.equals(mode)) {
        assign.get(scope).setReadAccess(value);
      }
      else if (ACCESS_WRITE.equals(mode)) {
        assign.get(scope).setWriteAccess(value);
      }
      else if (ACCESS_DELETE.equals(mode)) {
        assign.get(scope).setDeleteAccess(value);
      }
    }
    else if (modify.containsKey(scope)) {
      if (ACCESS_READ.equals(mode)) {
        modify.get(scope).setReadAccess(value);
      }
      else if (ACCESS_WRITE.equals(mode)) {
        modify.get(scope).setWriteAccess(value);
      }
      else if (ACCESS_DELETE.equals(mode)) {
        modify.get(scope).setDeleteAccess(value);
      }
    }
    else {
      final String                       svr = (attributes.get(EndpointAdministratorAdapter.SVR_PK) == null) ? null : attributes.get(EndpointAdministratorAdapter.SVR_PK).toString();
      final String                       ugp = (attributes.get(EndpointAdministratorAdapter.UGP_PK) == null) ? null : attributes.get(EndpointAdministratorAdapter.UGP_PK).toString();
      final EndpointAdministratorAdapter mab = new EndpointAdministratorAdapter();
      mab.setEndpointKey(Long.valueOf(svr));
      mab.setGroupKey(Long.valueOf(ugp));
      modify.put(Long.valueOf(ugp), mab);
    }
    train.markDirty();
    refresh();
    refreshRegion();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   modify
  /**
   ** Add the given targets to the collection of modified values.
   **
   ** @param  selection          the collection of values to be modified.
   **                            <br>
   **                            Allowed object is {@link List} here each
   **                            element is of type {@link Row}.
   */
  public void modify(final List<Row> selection) {
    if (selection != null && !selection.isEmpty()) {
      final Train                                   train   = train();
      boolean                                       touched = false;
      final Map<Long, EndpointAdministratorAdapter> modify  = train.administratorModify();

      // access the name of the iterator "AdministratorIterator" the table is
      // bound to.
      final RowSetIterator rsi = rowSetIterator("AdministratorIterator");
      // latch the current position of the iterator to restore it later
      final int            old = rsi.getCurrentRowIndex();
      for (Row cursor : selection) {
        rsi.setCurrentRow(cursor);
        final String action = (String)cursor.getAttribute(EndpointAdministratorAdapter.ACTION);
        if (StringUtility.isEmpty(action)) {
          cursor.setAttribute(EndpointAdministratorAdapter.ACTION, EndpointAdministratorAdapter.MOD);
          final EndpointAdministratorAdapter value = new EndpointAdministratorAdapter(cursor);
          modify.put(value.getGroupKey(), value);
          touched = true;
        }
      }
      // restore the current position of the row iterator
      rsi.setCurrentRowAtRangeIndex(old);
      // restore the current position of the row iterator
      rsi.setCurrentRowAtRangeIndex(old);
      if (touched) {
        train.markDirty();
        ADF.partialRender(getSearchTable());
        partialRenderAction();
        partialRenderSubmitRevert();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   remove
  /**
   ** Removes the given targets from the collection of values which triggers
   ** this <code>IT Resource</code>.
   **
   ** @param  selection          the collection of values to be removed.
   **                            <br>
   **                            Allowed object is {@link List} here each
   **                            element is of type {@link Row}.
   */
  public void remove(final List<Row> selection) {
    if (selection != null && !selection.isEmpty()) {
      final Train                                   train   = train();
      boolean                                       touched = false;
      final Map<Long, EndpointAdministratorAdapter> assign  = train.administratorAssign();
      final Map<Long, EndpointAdministratorAdapter> remove  = train.administratorRemove();
      for (Row cursor : selection) {
        final String action = (String)cursor.getAttribute(EndpointAdministratorAdapter.ACTION);
        if (StringUtility.isEmpty(action) || EndpointAdministratorAdapter.MOD.equals(action)) {
          cursor.setAttribute(EndpointAdministratorAdapter.ACTION, EndpointAdministratorAdapter.DEL);
          final EndpointAdministratorAdapter value = new EndpointAdministratorAdapter(cursor);
          remove.put(value.getGroupKey(), value);
          touched = true;
        }
        else if (EndpointAdministratorAdapter.ADD.equals(action)) {
          cursor.remove();
          assign.remove(cursor.getAttribute(EndpointAdministratorAdapter.UGP_PK));
          touched = true;
        }
      }
      if (touched) {
        train.markDirty();
        refresh();
        refreshRegion();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   undo
  /**
   ** Removes the given targets from the collection of values to be triggering
   ** the <code>IT Resource</code>
   **
   ** @param  selection          the collection of values to be removed.
   **                            <br>
   **                            Allowed object is {@link List} here each
   **                            element is of type {@link Row}.
   */
  public void undo(final List<Row> selection) {
    if (!CollectionUtility.empty(selection)) {
      final Train                                   train   = train();
      boolean                                       touched = false;
      final Map<Long, EndpointAdministratorAdapter> modify  = train.administratorModify();

      // access the name of the iterator "AdministratorIterator" the table is
      // bound to.
      final RowSetIterator rsi = rowSetIterator("AdministratorIterator");
      // latch the current position of the iterator to restore it later
      final int            old = rsi.getCurrentRowIndex();
      for (Row cursor : selection) {
        // no need to cast
        final Object administrator = cursor.getAttribute(EndpointAdministratorAdapter.UGP_PK);
        if (modify.containsKey(administrator)) {
          modify.remove(administrator);
          touched = true;
        }
      }
      // restore the current position of the row iterator
      rsi.setCurrentRowAtRangeIndex(old);
      if (touched) {
        ResetUtils.reset(getSearchTable());
//        ADF.partialRender(getSearchTable());
        partialRenderAction();
        partialRenderSubmitRevert();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Refreshs the administrator region of the <code>IT Resource</code>.
   */
  private void refresh() {
    final Train                                                train   = train();
    final Map<String, Map<Long, EndpointAdministratorAdapter>> pending = new HashMap<String, Map<Long, EndpointAdministratorAdapter>>();

    pending.put(EndpointAdministratorAdapter.ADD, train.administratorAssign());
    pending.put(EndpointAdministratorAdapter.DEL, train.administratorRemove());
    pending.put(EndpointAdministratorAdapter.MOD, train.administratorModify());

    final Map<String, Object> administrator = new HashMap<String, Object>();
    // looks like regardless which class type is defined on the task flow input
    // parameter definition ADF stores any values always as of type String
    administrator.put("identifier", Long.valueOf(ADF.pageFlowScopeStringValue(EndpointAdapter.PK)));
    administrator.put("pending",    pending);
    ADF.executeOperation("requeryAdministrator", administrator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   train
  /**
   ** Returns the {@link Train} bound to the task flow in the PageFlow scope.
   */
  private Train train() {
    return JSF.valueFromExpression("#{pageFlowScope.endpointTrain}", Train.class);
  }
}