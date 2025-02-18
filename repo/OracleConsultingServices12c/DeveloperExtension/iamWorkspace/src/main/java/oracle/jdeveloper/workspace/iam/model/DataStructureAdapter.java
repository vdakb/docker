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

    Copyright © 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   DataStructureAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DataStructureAdapter.


    Revisions        Date       Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.model;

import java.net.URL;

import oracle.javatools.data.HashStructure;
import oracle.javatools.data.HashStructureAdapter;
import oracle.javatools.data.StructureChangeListener;

import oracle.ide.exception.ChangeVetoException;

import oracle.ide.model.Observer;
import oracle.ide.model.UpdateMessage;
import oracle.ide.model.VetoableSubject;

////////////////////////////////////////////////////////////////////////////////
// abstract class DataStructureAdapter
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>DataStructureAdapter</code> provides the basic implementation of any
 ** data context.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class DataStructureAdapter extends    HashStructureAdapter
                                           implements VetoableSubject {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String OVERRIDE = "override";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DataStructureAdapter</code>.
   ** <p>
   ** This constructor is protected to prevent other classes to use
   ** "new DataStructureAdapter()" and enforces use of the public factory of the
   ** implementing sub classes.
   ** <p>
   ** Constructor version is existing to satisfy the wizard state engine
   ** requirements
   */
  protected DataStructureAdapter() {
    // ensure inheritance
    this(HashStructure.newInstance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DataStructureAdapter</code>.
   ** <p>
   ** This constructor is protected to prevent other classes to use
   ** "new DataStructureAdapter()" and enforces use of the public factory of the
   ** implementing sub classes.
   **
   ** @param  hash               the {@link HashStructure} this adapter wrappes.
   */
  protected DataStructureAdapter(final HashStructure hash) {
    // ensure inheritance
    super(hash);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   override
  /**
   ** Returns <code>true</code> if the file this {@link HashStructureAdapter}
   ** belongs to can be overridden.
   **
   ** @param  override           <code>true</code> if the file this
   **                            {@link HashStructureAdapter} belongs to can be
   **                            overridden; otherwise <code>false</code>.
   */
  public void override(final boolean override) {
    this._hash.putBoolean(OVERRIDE, override);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   override
  /**
   ** Returns <code>true</code> if the file this {@link HashStructureAdapter}
   ** belongs to can be overridden.
   ** <p>
   ** If the override mode is not defined at the time the method is invoked it
   ** returns <code>false</code> to ensure that no file will be accidentally.
   ** overriden.
   **
   ** @return                    <code>true</code> if the file this
   **                            {@link HashStructureAdapter} belongs to can be
   **                            overridden; otherwise <code>false</code>.
   */
  public boolean override() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return bool(OVERRIDE, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bool
  /**
   ** Returns the <code>boolean</code> value that is bound at the specified key.
   ** <p>
   ** If the value is <code>null</code>, <code>null</code> is returned. If the
   ** value is non-<code>null</code> and is not a String, the result of calling
   ** toString() on the value is returned. If the key is not bound,
   ** <code>null</code> is returned.
   **
   ** @param  key                the key for the desired <code>boolean</code>
   **                            value.
   **
   ** @return                    the <code>boolean</code> value that is bound at
   **                            the specified key.
   **
   ** @throws ClassCastException if the object bound at the name cannot be cast
   **                            to <code>boolean</code> or if a leaf object is
   **                            encountered when a substructure is expected.
   */
  public final boolean bool(final String key) {
    return this._hash.getBoolean(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bool
  /**
   ** Returns the <code>boolean</code> value that is bound at the specified key.
   ** <p>
   ** If <code>null</code> is bound or there is no bound value, the value of the
   ** defaultValue parameter is returned, and the defaultValue string is put
   ** into the {@link HashStructure} as a <code>placeholder</code>.
   **
   ** @param  key                the key for the desired <code>boolean</code>
   **                            value.
   ** @param  defaultValue       the value that should be returned if no value
   **                            is bound at the specified key.
   **
   ** @return                    the <code>boolean</code> value that is bound at
   **                            the specified key.
   **
   ** @throws ClassCastException if the object bound at the name cannot be cast
   **                            to <code>boolean</code> or if a leaf object is
   **                            encountered when a substructure is expected.
   */
  public final boolean bool(final String key, final boolean defaultValue) {
    // the second parameter to getString() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return this._hash.getBoolean(key, defaultValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string
  /**
   ** Returns the <code>String</code> value that is bound at the specified key.
   ** <p>
   ** If the value is <code>null</code>, <code>null</code> is returned. If the
   ** value is non-<code>null</code> and is not a String, the result of calling
   ** toString() on the value is returned. If the key is not bound,
   ** <code>null</code> is returned.
   **
   ** @param  key                the key for the desired string.
   **
   ** @return                    the <code>String</code> value that is bound at
   **                            the specified key.
   **
   ** @throws ClassCastException if the object bound at the name cannot be cast
   **                            to <code>String</code> or if a leaf object is
   **                            encountered when a substructure is expected.
   */
  public final String string(final String key) {
    return this._hash.getString(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string
  /**
   ** Returns the <code>String</code> value that is bound at the specified key.
   ** <p>
   ** If <code>null</code> is bound or there is no bound value, the value of the
   ** defaultValue parameter is returned, and the defaultValue string is put
   ** into the {@link HashStructure} as a <code>placeholder</code>.
   **
   ** @param  key                the key for the desired string.
   ** @param  defaultValue       the value that should be returned if no value
   **                            is bound at the specified key.
   **
   ** @return                    the <code>String</code> value that is bound at
   **                            the specified key.
   **
   ** @throws ClassCastException if the object bound at the name cannot be cast
   **                            to <code>String</code> or if a leaf object is
   **                            encountered when a substructure is expected.
   */
  public final String string(final String key, final String defaultValue) {
    // the second parameter to getString() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return this._hash.getString(key, defaultValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   url
  /**
   ** Returns the {@link URL} value that is bound at the specified key.
   ** <p>
   ** If the value is <code>null</code>, <code>null</code> is returned. If the
   ** value is non-<code>null</code> and is not a {@link URL}, the result of
   ** calling toString() on the value is returned. If the key is not bound,
   ** <code>null</code> is returned.
   **
   ** @param  key                the key for the desired {@link URL}.
   **
   ** @return                    the {@link URL} value that is bound at the
   **                            specified key.
   **
   ** @throws ClassCastException if the object bound at the name cannot be cast
   **                            to {@link URL} or if a leaf object is
   **                            encountered when a substructure is expected.
   */
  public final URL url(final String key) {
    return this._hash.getURL(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   url
  /**
   ** Returns the {@link URL} value that is bound at the specified key.
   ** <p>
   ** If <code>null</code> is bound or there is no bound value, the value of the
   ** defaultValue parameter is returned, and the defaultValue {@link URL} is
   ** put into the {@link HashStructure} as a <code>placeholder</code>.
   **
   ** @param  key                the key for the desired {@link URL}.
   ** @param  defaultValue       the {@link URL} that should be returned if no
   **                            value is bound at the specified key.
   **
   ** @return                    the {@link URL} value that is bound at the
   **                            specified key.
   **
   ** @throws ClassCastException if the object bound at the name cannot be cast
   **                            to {@link URL} or if a leaf object is
   **                            encountered when a substructure is expected.
   */
  public final URL url(final String key, final URL defaultValue) {
    // The second parameter to getURL() is a default value. Defaults are stored
    // in the persistent preferences file using a "placeholder".
    return this._hash.getURL(key, defaultValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attach (Subject)
  /**
   ** Registers an {@link Observer} interested in being notified when the
   ** internal state changes.
   **
   ** @param  observer           the {@link Observer} interested in change
   **                            notification messages.
   */
  @Override
  public void attach(final Observer observer) {
    this.attach(observer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   detach (Subject)
  /**
   ** Unregisters an {@link Observer} that is not interested anymore in being
   ** notified when the internal state changes.
   **
   ** @param  observer           the {@link Observer} desinterested in change
   **                            notification messages.
   */
  @Override
  public void detach(final Observer observer) {
    this.detach(observer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notifyObservers (Subject)
  /**
   ** Notifies all {@link Observer}s that the state of the subject has changed.
   **
   ** @param  subject            the subject whose state has changed.
   ** @param  change             what changed.
   */
  @Override
  public final void notifyObservers(final Object subject, final UpdateMessage change) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notifyVetoObservers (VetoableSubject)
  /**
   ** Notifies all observers that the state of the subject has changed.
   **
   ** @param  subject            the subject whose state has changed.
   ** @param  change             what changed.
   **
   ** @throws ChangeVetoException if any VetoObserver rejected the pending
   **                             change.
   */
  @Override
  public final void notifyVetoObservers(final Object subject, final UpdateMessage change)
     throws ChangeVetoException {

    ((VetoableSubject)this._hash).notifyVetoObservers(subject, change);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   last
  /**
   ** Returns the last part of a segment in an URL string.
   **
   ** @param  source             the {@link URL} string providing the values for
   **                            the split process.
   **
   ** @return                    the last segment of an {@link URL}.
   */
  public static String last(final String source){
    // .*/      find anything up to the last / character
    // ([^/?]+) find (and capture) all following characters up to the next / or ?
    //          the + makes sure that at least 1 character is matched
    // .*       find all following characters
    return source.replaceFirst(".*/([^/?]+).*", "$1");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addChangeListener
  /**
   ** Adds the specified {@link StructureChangeListener} to the data structure
   ** maintained by this adpater.
   **
   ** @param  listener           the {@link StructureChangeListener} to add.
   */
  public final void addChangeListener(final StructureChangeListener listener) {
    this._hash.addStructureChangeListener(listener);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeChangeListener
  /**
   ** Removes the specified {@link StructureChangeListener} from the data
   ** structure maintained by this adpater.
   **
   ** @param  listener           the {@link StructureChangeListener} to remove.
   */
  public final void removeChangeListener(StructureChangeListener listener) {
    this._hash.removeStructureChangeListener(listener);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clone
  /**
   ** Clones this configuration from an existing file.
   **
   ** @param  source             the {@link URL} providing the values for the
   **                            cloning process.
   */
  public void clone(final URL source) {
    // intentionally left blank
  }
}