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

    File        :   Preferences.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Preferences.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.faces.shell.model;

import java.util.Map;
import java.util.HashMap;

import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class Preferences
// ~~~~~ ~~~~~~~~~~~
/**
 ** Bean used to store session-specific system preferences mode.
 ** <br>
 ** This object is exposed as a session-scope managed bean with the name
 ** "preference".
 ** <p>
 ** Internal use only.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Preferences {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Preferences instance = new Preferences();

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // session's current preferences.
  private Map<String, Map<String, Object>> cache;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Preferences</code> event handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Preferences() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   group
  /**
   ** Sets or replace all cached <code>SystemPreference</code>s with the mapping
   ** specified by the provided {@link Map}.
   **
   ** @param  group              the name of the system preference group to set
   **                            or replace the value mapping for
   ** @param  mapping            the value mapping to mapped to
   **                            <code>group</code>.
   */
  public final void group(final String group, final Map<String, Object> mapping) {
    // prevent bogus input
    if (StringUtility.isEmpty(group))
      return;

    if (this.cache == null) {
      this.cache = new HashMap<String, Map<String, Object>>();
    }
    this.cache.put(group, mapping);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   group
  /**
   ** Returns all cached <code>SystemPreference</code>s wrapped in a
   ** {@link Map}.
   **
   ** @param  group              the name of the system preference group
   **
   ** @return                    all cached <code>SystemPreference</code>s
   **                            wrapped in a {@link Map}.
   */
  public final Map<String, Object> group(final String group) {
    // prevent bogus input
    if (StringUtility.isEmpty(group))
      return null;

    return (this.cache == null) ? null : this.cache.get(group);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preference
  /**
   ** Sets or replace the <code>SystemPreference</code> mapped in the cache for
   ** <code>group</code> and <code>name</code>.
   **
   ** @param  group              the name of the system preference group
   ** @param  name               the name of the system preference in group
   **                            <code>group</code>.
   ** @param  value              the value of the system preference to be mapped
   **                            in the cache for <code>group</code> and
   **                            <code>name</code>.
   */
  public final void preference(final String group, final String name, final Object value) {
    // prevent bogus input
    if (StringUtility.isEmpty(group) || StringUtility.isEmpty(name))
      return;

    if (this.cache == null) {
      this.cache = new HashMap<String, Map<String, Object>>();
    }
    Map<String, Object> mapping = this.cache.get(group);
    if (mapping == null) {
      mapping = new HashMap<String, Object>();
      this.cache.put(group, mapping);
    }
    mapping.put(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preference
  /**
   ** Returns the <code>SystemPreference</code> mapped in the cache for
   ** <code>group</code> and <code>name</code>.
   **
   ** @param  group              the name of the system preference group
   ** @param  name               the name of the system preference in group
   **                            <code>group</code>.
   **
   ** @return                    the system preference mapped for
   **                            <code>group</code> and <code>name</code> or
   **                            <code>null</code> if there is no mapping for
   **                            <code>group</code> and <code>name</code> in
   **                            the <code>SystemPreference</code> cache.
   */
  public final Object preference(final String group, final String name) {
    // prevent bogus input or state
    if (StringUtility.isEmpty(group) || StringUtility.isEmpty(name) || this.cache == null)
      return null;

    final Map<String, Object> mapping = this.cache.get(group);
    return (mapping == null) ? null : mapping.get(name);
  }


  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Human Capital Shell Context that is in the current page flow scope.
   **
   ** @return                    Human Capital Shell Context.
   **                            <code>null</code> if you are not running in
   **                            Shell env and not taking UI shell as parameter-
   */
  public static Preferences instance() {
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear
  /**
   ** Clears a cached <code>SystemPreference</code> group.
   **
   ** @param  name               the name of the group to clear (remove).
   **
   ** @return                    <code>true</code> if the group could be
   **                            cleared; otherwise <code>false</code>.
   */
  public boolean clear(final String name) {
    // prevent bogus input or state
    if (StringUtility.isEmpty(name) || this.cache == null)
      return false;

    return this.cache.remove(name) != null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear
  /**
   ** Clears a cached <code>SystemPreference</code> value.
   **
   ** @param  group              the name of the group to clear (remove).
   ** @param  name               the name of the value to clear (remove).
   **
   ** @return                    <code>true</code> if the value could be
   **                            cleared; otherwise <code>false</code>.
   */
  public boolean clear(final String group, final String name) {
    // prevent bogus input or state
    if (StringUtility.isEmpty(group) || StringUtility.isEmpty(name) || this.cache == null)
      return false;

    final Map<String, Object> mapping = this.cache.get(group);
    if (mapping == null)
      return false;

    return mapping.remove(name) != null;
  }
}