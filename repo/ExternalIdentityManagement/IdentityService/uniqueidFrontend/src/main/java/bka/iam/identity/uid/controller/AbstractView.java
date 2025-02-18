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

    File        :   AbstractView.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractView.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.uid.controller;

import javax.faces.event.ActionEvent;

import org.primefaces.PrimeFaces;

import org.primefaces.model.LazyDataModel;

import org.primefaces.extensions.component.masterdetail.SelectLevelEvent;

import bka.iam.identity.jpa.provider.Base;

import bka.iam.identity.uid.state.Preference;

import bka.iam.identity.uid.model.DialogModel;
import bka.iam.identity.uid.model.SearchModel;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractView
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** The <code>AbstractView</code> common behavior definition of a paginated
 ** table view leveraging a {@link LazyDataModel}.
 **
 ** @param  <T>                  the type of the source entity representations.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the request payload
 **                              extending this class (requests can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>.
 **
 ** @author dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractView<T extends Base> extends SearchModel<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:3558932687520593863")
  private static final long serialVersionUID = 7592836400768328973L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected boolean         modify;
  protected boolean         changed;
  protected int             level            = 1;
  protected boolean         error            = false;

  protected Preference      preference;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractView</code> that allows use as a JavaBean.
   **
   ** @param  preference         the preferences for the view.
   **                            <br>
   **                            Allowed object is {@link Preference}.
   */
  protected AbstractView(final Preference preference) {
    // ensure inheritance
    super(preference.getPageSize());

    // initialize instance
    this.preference = preference;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLevel
  /**
   ** Sets the <code>level</code> property of the current view navigation.
   **
   ** @param  value              the <code>level</code> property of the current
   **                            view navigation.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  public void setLevel(final int value) {
    this.level = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLevel
  /**
   ** Retruns the <code>level</code> property of the current view navigation.
   **
   ** @return                    the <code>level</code> property of the current
   **                            view navigation.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int getLevel() {
    return this.level;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPageSize
  /**
   ** Sets the page size of the current view.
   **
   ** @param  value              the initial value of the page size to display.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  public void setPageSize(final int value) {
    // update session value
    this.preference.setPageSize(value);

    // ensure inheritance
    super.setPageSize(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   abort
  /**
   ** Callback method invoke by the action <code>cancel</code> in the UI to
   ** reset the changes done at a form.
   */
  public void abort() {
    this.selected = null;
    discard();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   discard
  /**
   ** Callback method invoke by the action <code>discard</code> in the UI to
   ** reset the changes done at a form.
   */
  public void discard() {
    // reset the state of the current view
    this.changed = false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changed
  /**
   ** Sets the view to be changed due to values are changed.
   */
  public void changed() {
     this.changed = true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isChanged
  /**
   ** Returns <code>true</code> if the view is changed to by the user.
   **
   ** @return                    <code>true</code> if the view is changed to by
   **                            the user.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean isChanged() {
    return this.changed;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isModify
  /**
   ** Returns <code>true</code> if the view is in mode <code>modify</code> to
   ** ensure the appropritate components can honor this state.
   **
   ** @return                    <code>true</code> if the view is in mode
   **                            <code>modify</code> to ensure the appropritate
   **                            components can honor this state.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean isModify() {
    return this.modify;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigate
  /**
   ** Listener method attached to a master-detail component by property
   ** <code>selectLevelListener</code> in the UI to navigate.
   **
   ** @param  event              the {@link SelectLevelEvent} represents the
   **                            activation of a user interface component (such
   **                            as a UICommand).
   **                            <br>
   **                            Allowed object is {@link SelectLevelEvent}.
   **
   ** @return                    the level in the master-detail component
   **                            hierarchie to navigate to.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int navigate(final SelectLevelEvent event) {
    // stay on the current level in case an error occured
    return (this.error) ? -1 : event.getNewLevel();
  }
 
  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Callback method invoke by the action <code>new</code> in the UI to create
   ** a new entity.
   */
  public abstract void create();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Callback method invoke by the action <code>edit</code> in the UI to
   ** modify the selected entity.
   */
  public void modify() {
    this.modify = true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Callback method invoke by the action <code>delete</code> in the UI to
   ** delete the selected entity.
   */
  public abstract void delete();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   save
  /**
   ** Callback method invoke by the action <code>save</code> in the UI to
   ** persist a created or modified entity.
   **
   ** @param  event              the {@link ActionEvent} represents the
   **                            activation of a user interface component (such
   **                            as a UICommand).
   **                            <br>
   **                            Allowed object is {@link ActionEvent}.
   */
  public abstract void save(final ActionEvent event);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dialogReturn
  /**
   ** Action method invoke by the action <code>dialogReturn</code> in the UI to
   ** close the dialog and pushing the selected element to the context.
   */
  public void dialogReturn() {
    PrimeFaces.current().dialog().closeDynamic(this.selected);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dialogCancel
  /**
   ** Action method invoke by the action <code>dialogCancel</code> in the UI to
   ** close the dialog without pushing the selected element to the context.
   */
  public void dialogCancel() {
    PrimeFaces.current().dialog().closeDynamic(null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   showPicker
  /**
   ** Action method invoke by the action <code>lookupp</code> in the UI to
   ** üick an entity.
   ** <br>
   ** Opens a view in a dynamic dialog.
   **
   ** @param  view               the logical outcome used to resolve the
   **                            navigation case.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected void showPicker(final String view) {
    PrimeFaces.current().dialog().openDynamic(
      view
    , DialogModel.option()
        .modal(true)
        .closeOnEscape(true)
        .showEffect("fold")
        .hideEffect("clip")
        .build()
    , DialogModel.parameter().build()
    );
  }
}