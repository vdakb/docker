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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Java Server Faces Foundation

    File        :   Accessibility.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Accessibility.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.faces.shell.model;

import java.io.Serializable;

import org.apache.myfaces.trinidad.context.AccessibilityProfile;

import oracle.hst.foundation.faces.ManagedBean;

////////////////////////////////////////////////////////////////////////////////
// class Accessibility
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Bean used to store session-specific accessibility mode.
 ** <br>
 ** This object is exposed as a session-scope managed bean with the name
 ** "accessibility".
 ** <p>
 ** Internal use only.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Accessibility extends    ManagedBean
                           implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:8579872563476718193")
  private static final long serialVersionUID = -1082119449781749414L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // session's current preferences.
  private boolean           screenReader;
  private boolean           highContrast;
  private boolean           largeFont;

  // We hold a reference to a cached copy of the AccessibilityProfile so that we
  // do not need to re-create this object each time getAccessibilityProfile() is
  // called.
  // Note that we mark this as volatile so that we can safely perform
  // double-checked locking on this variable.
  private transient AccessibilityProfile profile;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Accessibility</code> event handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Accessibility() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMode
  /**
   ** Returns the user's preferred accessibility mode.
   ** <p>
   ** The trinidad-config.xml's accessibility-mode setting is bound to this
   ** value (ie. "#{accessibility.mode}").
   **
   ** @return                    the user's preferred accessibility mode.
   **                            Possible object is {@link String}.
   */
  public String getMode() {
    return this.screenReader ? "screenReader" : "default";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getProfile
  /**
   ** Returns the user's preferred accessibility profile.
   ** <p>
   ** The trinidad-config.xml's accessibility-profile setting is bound to this
   ** value (ie. "#{accessibility.profile}").
   **
   ** @return                    the user's preferred accessibility mode.
   **                            Possible object is
   **                            {@link AccessibilityProfile}.
   */
  public AccessibilityProfile getProfile() {
    // use safe double-check locking just in case we have multiple threads
    // coming through.
    if (this.profile == null) {
      this.profile = createAccessibilityProfile();
    }
    return this.profile;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setScreenReader
  /**
   ** Sets the value of the screenReader property.
   **
   ** @param  value              the value of the screenReader property.
   **                            Allowed object is <code>boolean</code>.
   */
  public void setScreenReader(final boolean value) {
    this.screenReader = value;
    // we need to re-create the AccessibilityProfile instance if any of the
    // profile properties change.
    // Null out our old cached copy.
    // It will be re-created the next time that getAccessibilityProfile() is
    // called.
    this.profile = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isScreenReader
  /**
   ** Returns the value of the screenReader property.
   **
   ** @return                    the value of the screenReader property.
   **                            Possible object is <code>boolean</code>.
   */
  public boolean isScreenReader() {
    return this.screenReader;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setHighContrast
  /**
   ** Sets the value of the highContrast property.
   **
   ** @param  value              the value of the highContrast property.
   **                            Allowed object is <code>boolean</code>.
   */
  public void setHighContrast(final boolean value) {
    this.highContrast = value;
    // we need to re-create the AccessibilityProfile instance if any of the
    // profile properties change.
    // Null out our old cached copy.
    // It will be re-created the next time that getAccessibilityProfile() is
    // called.
    this.profile = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isHighContrast
  /**
   ** Returns the value of the highContrast property.
   **
   ** @return                    the value of the highContrast property.
   **                            Possible object is <code>boolean</code>.
   */
  public boolean isHighContrast() {
    return this.highContrast;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLargeFont
  /**
   ** Sets the value of the largeFont property.
   **
   ** @param  value              the value of the largeFont property.
   **                            Allowed object is <code>boolean</code>.
   */
  public void setLargeFont(final boolean value) {
    this.largeFont = value;
    // we need to re-create the AccessibilityProfile instance if any of the
    // profile properties change.
    // Null out our old cached copy.
    // It will be re-created the next time that getAccessibilityProfile() is
    // called.
    this.profile = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isLargeFont
  /**
   ** Returns the value of the largeFont property.
   **
   ** @return                    the value of the largeFont property.
   **                            Possible object is <code>boolean</code>.
   */
  public boolean isLargeFont() {
    return this.largeFont;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAccessibilityProfile
  /**
   ** Creates the {@link AccessibilityProfile} instance based on the user's
   ** current accessibility preferences.
   **
   ** @return                   the {@link AccessibilityProfile} instance based
   **                           on the user's current accessibility preferences.
   */
  private AccessibilityProfile createAccessibilityProfile() {
    final AccessibilityProfile.FontSize      fontSize      = isLargeFont()    ? AccessibilityProfile.FontSize.LARGE     : AccessibilityProfile.FontSize.MEDIUM;
    final AccessibilityProfile.ColorContrast colorContrast = isHighContrast() ? AccessibilityProfile.ColorContrast.HIGH : AccessibilityProfile.ColorContrast.STANDARD;
    return AccessibilityProfile.getInstance(colorContrast, fontSize);
  }
}
