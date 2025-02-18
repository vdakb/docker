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
    Subsystem   :   System Authorization Management

    File        :   PublicationState.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    PublicationState.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2016-03-03  DSteding    First release version
*/

package oracle.iam.identity.sysauthz.app.backing;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import java.io.Serializable;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import oracle.jbo.Row;

import oracle.iam.identity.vo.Identity;

import oracle.adf.view.rich.event.DialogEvent;

import oracle.iam.ui.common.model.org.OrganizationAdapterBean;

import oracle.hst.foundation.faces.JSF;
import oracle.hst.foundation.faces.ADF;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.sysauthz.schema.EntityPublicationAdapter;
import oracle.iam.identity.sysauthz.schema.ApplicationInstanceAdapter;

import oracle.iam.identity.frontend.train.AbstractSearch;

import oracle.iam.identity.sysauthz.app.state.Train;

///////////////////////////////////////////////////////////////////////////////
// class PublicationState
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Declares methods the user interface service needs to search for associated
 ** <code>Entity Publication</code>s of an <code>Application Instance</code>
 ** and modify them.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class PublicationState extends AbstractSearch {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String PICK_SCOPE       = "assign_orgs_to_appinstance";
  private static final String PICK_SCOPE_TFS   = "/WEB-INF/oracle/iam/ui/common/tfs/org-picker-tf.xml#org-picker-tf";

  private static final String BUNDLE           = "oracle.iam.identity.bundle.Authorization";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:1429864218590690299")
  private static final long   serialVersionUID = -8705944304270625146L;

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
   ** Constructs a <code>PublicationState</code> backing bean
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public PublicationState() {
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
    final Train train = train();
    final String mode = ADF.pageFlowScopeStringValue(PARAMETER_MODE);
    if (MODE_VIEW.equals(mode) || MODE_EDIT.equals(mode) || (!CollectionUtility.empty(train.scopeAssign())))
      this.displayInfo = false;
    else
      this.displayInfo = true;

    return this.displayInfo;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   isRevokeDisabled
  /**
   ** Whether the revoke button of a Application Instance/Organization Object
   ** relationship is disabled.
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
      case GENERIC_DELETE : return ADF.resourceBundleValue(BUNDLE, "APP_PUBLICATION_DELETE_CONFIRM");
      case GENERIC_UNDO   : return ADF.resourceBundleValue(BUNDLE, "APP_PUBLICATION_UNDO_CONFIRM");
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
   ** to pick a organization by the event handler.
   ** <br>
   ** The event handler itself is registered for every region.
   ** <p>
   ** The string of the created identifier consists of
   ** <ol>
   **   <li>the hardcoded prefix <code>app</code>
   **   <li>the instance identifier of the current taskflow obtained from the
   **       page flow scope.
   ** </ol>
   **
   ** @return                    the created unique event name.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String entityDistinguisher() {
    return String.format("app#%s", ADF.pageFlowScopeStringValue(ApplicationInstanceAdapter.PK));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pickListener
  /**
   ** Raise a <code>Contextual Event</code> programmatically to launch the
   ** taskflow to select organizational scopes to be added.
   **
   ** @param  event              the {@link ActionEvent} delivered from the
   **                            action occured in a UIXForm component.
   **                            <br>
   **                            Allowed object is {@link ActionEvent}.
   */
  public void pickListener(final @SuppressWarnings("unused") ActionEvent event) {
    final Map<String, Object> parameter = new HashMap<String, Object>();
    // it's required to distinct the taskflow of the picker to give the event
    // handler the ability to bypass taskflows which didn't raise the event due
    // to that the event handler is registered for every region and there can be
    // more than one taskflow on the same page
    parameter.put(EntityPublicationAdapter.ENTITY_ID, entityDistinguisher());
    parameter.put(EVENT_DISTINGUISHER,                PICK_SCOPE);
    parameter.put(PARAMETER_MODE,                     MODE_ASSIGN);
    raiseTaskFlowLaunchEvent(PICK_SCOPE, PICK_SCOPE_TFS, ADF.resourceBundleValue(BUNDLE, "APP_PUBLICATION_PICKER"), "/images/add_ena.png", null, null, true, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dialogListener
  /**
   ** Perfoms all actions belonging to the UI to reflect action events belonging
   ** to entries in the publication table after confirmation.
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
  // Method:   undoListener
  /**
   ** Perfoms all actions belonging to the UI to reflect action events to
   ** undo entries from the publication table.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   **                            <br>
   **                            Allowed object is {@link ActionEvent}.
   */
  public void undoListener(final @SuppressWarnings("unused") ActionEvent event) {
    final List<Row> selection = selectedRow(getSearchTable());
    undo(selection);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeListener
  /**
   ** Perfoms all actions belonging to the UI to reflect action events to
   ** remove entries from the publication table.
   **
   ** @param  event              the {@link ActionEvent} object that
   **                            characterizes the action to perform.
   **                            <br>
   **                            Allowed object is {@link ActionEvent}.
   */
  public void removeListener(final @SuppressWarnings("unused") ActionEvent event) {
   final List<Row> selection = selectedRow(getSearchTable());
   remove(selection);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revert (overridden)
  /**
   ** Reverts all changes belonging to the publication region of the
   ** <code>Application Instance</code>.
   */
  @Override
  public void revert() {
    train().clearScope();
    refresh();
    refreshRegion();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hierarchyChanged
  /**
   ** Perfoms all actions belonging to the UI to reflect value change events on
   ** particular components in the attribute region.
   **
   ** @param  event              the {@link ValueChangeEvent} object that
   **                            characterizes the action to perform.
   **                            <br>
   **                            Allowed object is {@link ValueChangeEvent}.
   */
  public void hierarchyChanged(final ValueChangeEvent event) {
    if (event.getNewValue() == null || (event.getOldValue() != null && event.getNewValue().toString().equals(event.getOldValue().toString())))
      return;

    final Train                                 train      = train();
    final Boolean                               hierarchy  = (Boolean)event.getNewValue();
    final Map<String, Object>                   attributes = event.getComponent().getAttributes();
    final String                                scope      = (attributes.get(EntityPublicationAdapter.SCOPE_ID) == null) ? null : attributes.get(EntityPublicationAdapter.SCOPE_ID).toString();
    final Map<String, EntityPublicationAdapter> assign     = train.scopeAssign();
    final Map<String, EntityPublicationAdapter> modify     = train.scopeModify();
    if (assign.containsKey(scope)) {
      assign.get(scope).setHierarchicalScope(hierarchy);
    }
    else if (modify.containsKey(scope)) {
      modify.get(scope).setHierarchicalScope(hierarchy);
    }
    else {
      final String                   pub = (attributes.get(EntityPublicationAdapter.KEY) == null) ? null : attributes.get(EntityPublicationAdapter.KEY).toString();
      final EntityPublicationAdapter mab = new EntityPublicationAdapter();
      mab.setPublicationId(Long.valueOf(pub));
      mab.setScopeId(scope);
      mab.setHierarchicalScope(hierarchy);
      modify.put(scope, mab);
    }
    train.markDirty();
    refresh();
    refreshRegion();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshListener (overridden)
  /**
   ** Refresh the publication relationships belonging to a certain
   ** <code>Application Instance</code>.
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
   ** Adds the given targets to the collection of scopes to be allowed.
   **
   ** @param  selection          the collection of scopes to be added to the
   **                            scopes to be allowed.
   **                            <br>
   **                            Allowed object is {@link List} here each
   **                            element is of type {@link Serializable}.
   */
  public void assign(final List<Serializable> selection) {
    if (!CollectionUtility.empty(selection)) {
      final Train                                 train  = train();
      final Map<String, EntityPublicationAdapter> assign = train.scopeAssign();
      final Map<String, EntityPublicationAdapter> remove = train.scopeRemove();
      final Map<String, EntityPublicationAdapter> modify = train.scopeModify();
      boolean                                     changed = false;
      for (int i = 0; i < selection.size(); i++) {
        final EntityPublicationAdapter mab    = new EntityPublicationAdapter();
        final Identity                 cursor = ((OrganizationAdapterBean)selection.get(i)).getIdentity();
        mab.setPublicationId(-1L);
        mab.setScopeId(String.valueOf(cursor.getAttribute("act_key")));
        mab.setScopeName((String)cursor.getAttribute("Organization Name"));
        mab.setScopeType((String)cursor.getAttribute("Organization Customer Type"));
        mab.setScopeStatus((String)cursor.getAttribute("Organization Status"));
        mab.setHierarchicalScope((Boolean)cursor.getAttribute("act_hierarchy"));
        mab.setPendingAction(EntityPublicationAdapter.ADD);
        final String scope = mab.getScopeId();
        if (remove.containsKey(scope)) {
          remove.remove(scope);
        }
        // prevent adding the same organization twice to avoid potential
        // conflicts in checking the pending action
        if (!(assign.containsKey(scope) || modify.containsKey(scope))) {
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
  // Methode:   remove
  /**
   ** Removes the given targets to the collection of scopes to be allowed.
   **
   ** @param  selection          the collection of scopes to be removed from
   **                            the scopes to be allowed.
   **                            <br>
   **                            Allowed object is {@link List} here each
   **                            element is of type {@link Row}.
   */
  public void remove(final List<Row> selection) {
    if (selection != null && !selection.isEmpty()) {
      final Train                                 train  = train();
      final Map<String, EntityPublicationAdapter> assign = train.scopeAssign();
      final Map<String, EntityPublicationAdapter> modify = train.scopeModify();
      final Map<String, EntityPublicationAdapter> remove = train.scopeRemove();
      for (Row cursor : selection) {
        final EntityPublicationAdapter mab   = new EntityPublicationAdapter(cursor);
        final String                   scope = mab.getScopeId();
        if (assign.containsKey(scope)) {
          assign.remove(scope);
        }
        if (modify.containsKey(scope)) {
          modify.remove(scope);
        }
        if (!remove.containsKey(scope)) {
          mab.setPendingAction(EntityPublicationAdapter.DEL);
          remove.put(scope, mab);
        }
      }
      train.markDirty();
      refresh();
      refreshRegion();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   undo
  /**
   ** Removes the given targets from the collection of publications to be
   ** provisioned.
   **
   ** @param  selection          the collection of publications to be removed from
   **                            the publications to be provisioned.
   **                            <br>
   **                            Allowed object is {@link List} here each
   **                            element is of type {@link Row}.
   */
  public void undo(final List<Row> selection) {
    if (!CollectionUtility.empty(selection)) {
      final Train                                 train  = train();
      final Map<String, EntityPublicationAdapter> assign = train.scopeAssign();
      final Map<String, EntityPublicationAdapter> modify = train.scopeModify();
      final Map<String, EntityPublicationAdapter> remove = train.scopeRemove();
      for (Row cursor : selection) {
        final String scope = (String)cursor.getAttribute(EntityPublicationAdapter.SCOPE_ID);
        if (assign.containsKey(scope)) {
          assign.remove(scope);
        }
        if (remove.containsKey(scope)) {
          remove.remove(scope);
        }
        if (modify.containsKey(scope)) {
          modify.remove(scope);
        }
      }
      refresh();
      refreshRegion();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   partialRenderSubmitRevert
  /**
   ** Add the global action components (Save/Apply/Revert) as a partial
   ** rendering target.
   ** <p>
   ** In response to a partial event, only components registered as partial
   ** targets are re-rendered. For a component to be successfully re-rendered
   ** when it is manually added with this API, it should have the
   ** "clientComponent" attribute set to <code>true</code>. If not, partial
   ** re-rendering may or may not work depending on the component.
   */
  public void partialRenderSubmitRevert() {
    super.partialRenderSubmitRevert();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Refreshs the publication region of the <code>Application Instance</code>.
   */
  private void refresh() {
    final Train                                       train   = train();
    final List<Map<String, EntityPublicationAdapter>> pending = new ArrayList<Map<String, EntityPublicationAdapter>>();
    pending.add(train.scopeAssign());
    pending.add(train.scopeRemove());
    pending.add(train.scopeModify());

    final Map<String, Object> parameter = new HashMap<String, Object>();
    parameter.put("identifier", ADF.pageFlowScopeStringValue(ApplicationInstanceAdapter.PK));
    parameter.put("pending",    pending);
    ADF.executeOperation("requeryPublication", parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   train
  /**
   ** Returns the {@link Train} bound to the task flow in the PageFlow scope.
   */
  private Train train() {
    return JSF.valueFromExpression("#{pageFlowScope.applicationTrain}", Train.class);
  }
}