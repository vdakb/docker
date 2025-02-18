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
    Subsystem   :   Identity Manager Facility

    File        :   Composite.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Composite.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oim.model;

import java.text.SimpleDateFormat;

import java.util.Date;

import oracle.javatools.data.HashStructure;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// abstract class Composite
// ~~~~~~~~ ~~~~~ ~~~~~~~~~
/**
 ** The model to support the configuration wizard panel for creating the
 ** OIM Approval Workflows.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
abstract class Composite extends BuildArtifact {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String NAME             = "name";
  private static final String REVISION         = "revision";
  private static final String LABEL            = "label";
  private static final String MODE             = "mode";
  private static final String STATE            = "state";
  private static final String SERVICE          = "service";

  private static final String DEFAULT_REVISION = "1.0.0.0";
  private static final String DEFAULT_MODE     = "active";
  private static final String DEFAULT_STATE    = "on";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Composite</code>.
   ** <br>
   ** This constructor is protected to prevent other classes to use
   ** "new Composite()" and enforces use of the public factory method below.
   **
   ** @param  structure          the data provider to initialize the instance.
   */
  protected Composite(final HashStructure structure) {
    // ensure inheritance
    super(structure);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Sets the technical name of the workflow used by the approval process.
   **
   ** @param  name               the technical name of the workflow used by the
   **                            approval process.
   */
  public void name(final String name) {
    this._hash.putString(NAME, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the technical name of the workflow used by the approval process.
   **
   ** @return                    the technical name of the workflow used by the
   **                            approval process.
   */
  public String name() {
    return string(NAME, StringUtility.EMPTY);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   revision
  /**
   ** Sets the revision ID the workflow will be deployed with on Oracle SOA
   ** server.
   **
   ** @param  revision           the revision ID the workflow will be deployed
   **                            with on Oracle SOA server.
   */
  public void revision(final String revision) {
    this._hash.putString(REVISION, revision);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revision
  /**
   ** Returns the revision ID the workflow will be deployed with on Oracle SOA
   ** server.
   **
   ** @return                    the revision ID the workflow will be deployed
   **                            with on Oracle SOA server.
   */
  public String revision() {
    return string(REVISION, DEFAULT_REVISION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Returns the composite label of the workflow used by the approval process.
   **
   ** @return                    the composite label of the workflow used by the
   **                            approval process.
   */
  public String label() {
    return string(LABEL, defaultLabel());
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Sets the composite label of the workflow used by the approval process.
   **
   ** @param  label              the composite label of the workflow used
   **                            by the approval process.
   */
  public void label(final String label) {
    this._hash.putString(LABEL, label);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   mode
  /**
   ** Sets the mode of the workflow used by the approval process.
   ** <p>
   ** The first important piece to understand is that there are two parts to the
   ** state <code>mode</code> &amp; <code>state</code>.
   ** <p>
   ** <code>active</code> | <code>retired</code> this setting decides
   ** whether new instances can be created [<code>active</code>], or old ones
   ** are allowed to finish w/o new ones being allowed to be created
   ** [<code>retired</code>] - this is referred to as composite <b>mode</b>.
   **
   ** @param  mode               the technical mode of the workflow used by the
   **                            approval process.
   **
   ** @see    #mode()
   ** @see    #state()
   ** @see    #state(String)
   */
  public void mode(final String mode) {
    this._hash.putString(MODE, mode);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mode
  /**
   ** Returns the mode of the workflow used by the approval process.
   ** <p>
   ** The first important piece to understand is that there are two parts to the
   ** state <code>mode</code> &amp; <code>state</code>.
   ** <p>
   ** <code>active</code> | <code>retired</code> this setting decides
   ** whether new instances can be created [<code>active</code>], or old ones
   ** are allowed to finish w/o new ones being allowed to be created
   ** [<code>retired</code>] - this is referred to as composite <b>mode</b>.
   **
   ** @return                    the mode of the workflow used by the
   **                            approval process.
   **
   ** @see    #mode(String)
   ** @see    #state()
   ** @see    #state(String)
   */
  public String mode() {
    return string(MODE, DEFAULT_MODE);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   state
  /**
   ** Sets the state of the workflow used by the approval process.
   ** <p>
   ** The first important piece to understand is that there are two parts to the
   ** state <code>mode</code> &amp; <code>state</code>.
   ** <p>
   ** <code>on</code> | <code>off</code> this is the composite <b>state</b> and
   ** overrides <b>mode</b> in either allowing call access [<code>invoke</code>
   ** / <code>callback</code>] to the composite revision [<code>on</code>] or
   ** not at all [<code>off</code>]. This is referred to as composite
   ** <b>state</b>.
   **
   ** @param  state              the state of the workflow used by the
   **                            approval process.
   **
   ** @see    #mode()
   ** @see    #mode(String)
   ** @see    #state()
   */
  public void state(final String state) {
    this._hash.putString(STATE, state);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   state
  /**
   ** Returns the state of the workflow used by the approval process.
   ** <p>
   ** The first important piece to understand is that there are two parts to the
   ** state <code>mode</code> &amp; <code>state</code>.
   ** <p>
   ** <code>on</code> | <code>off</code> this is the composite <b>state</b> and
   ** overrides <b>mode</b> in either allowing call access [<code>invoke</code>
   ** / <code>callback</code>] to the composite revision [<code>on</code>] or
   ** not at all [<code>off</code>]. This is referred to as composite
   ** <b>state</b>.
   **
   ** @return                    the state of the workflow used by the
   **                            approval process.
   **
   ** @see    #mode()
   ** @see    #mode(String)
   ** @see    #state(String)
   */
  public String state() {
    return string(STATE, DEFAULT_STATE);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   service
  /**
   ** Sets the service name of the workflow used by the approval process.
   **
   ** @param  service            the service name of the workflow used
   **                            by the approval process.
   */
  public void service(final String service) {
    this._hash.putString(SERVICE, service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   service
  /**
   ** Returns the service name of the workflow used by the approval process.
   **
   ** @return                    the service name of the workflow used
   **                            by the approval process.
   */
  public String service() {
    return string(SERVICE, StringUtility.EMPTY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   defaultLabel
  /**
   ** Returns the default for a composite label of the workflow used by the
   ** approval process.
   **
   ** @return                    the default for a composite label of the
   **                            workflow used by the approval process.
   */
  public static String defaultLabel() {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-dd-MM_HH-mm-ss_SSS");
    return formatter.format(new Date());
  }
}