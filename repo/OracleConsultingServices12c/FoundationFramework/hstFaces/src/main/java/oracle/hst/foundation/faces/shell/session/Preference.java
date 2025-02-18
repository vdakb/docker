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

    File        :   Preference.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Preference.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.faces.shell.session;

import java.util.Map;

import java.io.Serializable;

import oracle.hst.foundation.faces.JSF;
import oracle.hst.foundation.faces.ManagedBean;

import oracle.hst.foundation.faces.shell.model.Preferences;

////////////////////////////////////////////////////////////////////////////////
// class Preference
// ~~~~~ ~~~~~~~~~~
/**
 ** Internal use only.
 ** <p>
 ** Backing bean for system preference access within a session.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 0.0.0.0
 ** @since   0.0.0.0
 */
public class Preference extends    ManagedBean
                        implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String EXPRESSION = "#{preference}";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:4353986649422523350")
  private static final long serialVersionUID = 5366266603501915795L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Preference</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Preference() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getContext
  /**
   ** Returns the value of the preference cache property.
   **
   ** @return                    the value of the context property.
   **                            Possible object is {@link Preferences}.
   */
  public final Preferences getContext() {
    return Preferences.instance();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setGroup
  /**
   ** Sets or replace all cached <code>SystemPreference</code>s with the mapping
   ** specified by the provided {@link Map}.
   **
   ** @param  group              the name of the system preference group to set
   **                            or replace the value mapping for
   ** @param  mapping            the value mapping to mapped to
   **                            <code>group</code>.
   */
  public final void setGroup(final String group, final Map<String, Object> mapping) {
    getContext().group(group, mapping);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getGroup
  /**
   ** Returns all cached <code>SystemPreference</code>s wrapped in a
   ** {@link Map}.
   **
   ** @param  group              the name of the system preference group
   **
   ** @return                    all cached <code>SystemPreference</code>s
   **                            wrapped in a {@link Map}.
   */
  public final Map<String, Object> getGroup(final String group) {
    return getContext().group(group);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPreference
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
  public final void setPreference(final String group, final String name, final Object value) {
    getContext().preference(group, name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getBoolean
  /**
   ** Returns the {@link Boolean} <code>SystemPreference</code> mapped in the
   ** cache for <code>group</code> and <code>name</code>.
   **
   ** @param  group              the name of the system preference group
   ** @param  name               the name of the system preference in group
   **                            <code>group</code>.
   **
   ** @return                    the {@link Boolean}
   **                            <code>SystemPreference</code> mapped for
   **                            <code>group</code> and <code>name</code> or
   **                            <code>null</code> if there is no mapping for
   **                            <code>group</code> and <code>name</code> in
   **                            the <code>SystemPreference</code> cache.
   */
  public final Boolean getBoolean(final String group, final String name) {
    return (Boolean)getContext().preference(group, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getInteger
  /**
   ** Returns the {@link Integer} <code>SystemPreference</code> mapped in the
   ** cache for <code>group</code> and <code>name</code>.
   **
   ** @param  group              the name of the system preference group
   ** @param  name               the name of the system preference in group
   **                            <code>group</code>.
   **
   ** @return                    the {@link Integer}
   **                            <code>SystemPreference</code> mapped for
   **                            <code>group</code> and <code>name</code> or
   **                            <code>null</code> if there is no mapping for
   **                            <code>group</code> and <code>name</code> in
   **                            the <code>SystemPreference</code> cache.
   */
  public final Integer getInteger(final String group, final String name) {
    return (Integer)getContext().preference(group, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLong
  /**
   ** Returns the {@link Long} <code>SystemPreference</code> mapped in the
   ** cache for <code>group</code> and <code>name</code>.
   **
   ** @param  group              the name of the system preference group
   ** @param  name               the name of the system preference in group
   **                            <code>group</code>.
   **
   ** @return                    the {@link Long}
   **                            <code>SystemPreference</code> mapped for
   **                            <code>group</code> and <code>name</code> or
   **                            <code>null</code> if there is no mapping for
   **                            <code>group</code> and <code>name</code> in
   **                            the <code>SystemPreference</code> cache.
   */
  public final Long getLong(final String group, final String name) {
    return (Long)getContext().preference(group, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getString
  /**
   ** Returns the {@link String} <code>SystemPreference</code> mapped in the
   ** cache for <code>group</code> and <code>name</code>.
   **
   ** @param  group              the name of the system preference group
   ** @param  name               the name of the system preference in group
   **                            <code>group</code>.
   **
   ** @return                    the {@link String}
   **                            <code>SystemPreference</code> mapped for
   **                            <code>group</code> and <code>name</code> or
   **                            <code>null</code> if there is no mapping for
   **                            <code>group</code> and <code>name</code> in
   **                            the <code>SystemPreference</code> cache.
   */
  public final String getString(final String group, final String name) {
    return (String)getContext().preference(group, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPreference
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
  public final Object getPreference(final String group, final String name) {
    return getContext().preference(group, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Preference Context that is in the current session scope.
   **
   ** @return                    Preference Context.
   **                            <code>null</code> if you are not running in
   **                            Shell env and not taking UI shell as parameter-
   */
  public static Preference instance() {
    return JSF.valueFromExpression(EXPRESSION, Preference.class);
  }
}