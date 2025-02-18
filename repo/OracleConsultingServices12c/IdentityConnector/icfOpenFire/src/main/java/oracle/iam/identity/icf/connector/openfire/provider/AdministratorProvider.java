/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license agreement you enthered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Openfire Database Connector

    File        :   AdministratorProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AdministratorProvider.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-10-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.openfire.provider;

import java.util.List;
import java.util.Objects;

import java.util.stream.Stream;
import java.util.stream.Collectors;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.connector.openfire.Context;

import oracle.iam.identity.icf.connector.openfire.schema.SystemProperty;

////////////////////////////////////////////////////////////////////////////////
// final class AdministratorProvider
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The descriptor to handle system administrator property.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class AdministratorProvider {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final String         domain;
  final List<String>   collection;
  final SystemProperty property;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AdministratorProvider</code> that allows use as a JavaBean.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new AdministratorProvider(Context)" and enforces use of the public method
   ** below.
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   **
   ** @throws SystemException    if initialization fails.
   */
  private AdministratorProvider(final Context context)
    throws SystemException {

    // ensure inheritance
    super();

    // initialize instance attributes
    this.domain     = context.systemProperty(SystemProperty.DOMAIN).value();
    this.property   = context.systemProperty(SystemProperty.ADMINISTRATOR);
    this.collection = stream(this.property.value());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an AdministratorProvider.
   **
   ** @param  context            the {@link Context} where the provider operates
   **                            on.
   **                            <br>
   **                            Allowed object is {@link Context}.
   **
   ** @return                    the provider of the administrator system
   **                            property.
   **                            <br>
   **                            Possible object is
   **                            {@link AdministratorProvider}.
   **
   ** @throws SystemException    in case an error does occur.
   */
  public static AdministratorProvider build(final Context context)
    throws SystemException {

    return new AdministratorProvider(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   granted
  /**
   ** Verifies if the supplied <code>username</code> has granted admin access.
   **
   ** @param  username           the name of the user to verify.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the supplied
   **                            <code>username</code> has granted admin access.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean granted(final String username) {
    return this.collection.contains(jid(this.domain, username));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assign
  /**
   ** Assigns admin access to the supplied <code>username</code>.
   **
   ** @param  username           the name of the user to grant admin access to.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AdministratorProvider</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>AdministratorProvider</code>.
   */
  public final AdministratorProvider assign(final String username) {
    this.collection.add(String.format("%s@%s", username, this.domain).toLowerCase());
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revoke
  /**
   ** Revokes admin access from the supplied <code>username</code>.
   **
   ** @param  username           the name of the user to revoke admin access
   **                            from.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AdministratorProvider</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>AdministratorProvider</code>.
   */
  public final AdministratorProvider revoke(final String username) {
    this.collection.remove(String.format("%s@%s", username, this.domain).toLowerCase());
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   post
  /**
   ** Post the current state of the collection to th esystem property.
   **
   ** @return                    the system administrator property in the
   **                            current state.
   **                            <br>
   **                            Possible object is {@link SystemProperty}.
   */
  public SystemProperty post() {
    return (SystemProperty)this.property.value(stream(this.collection));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stream
  /**
   ** Converting a comma separated {@link String} to a {@link List} of
   ** {@link String}s.
   ** <br>
   ** Only non-<code>null</code> elements of the supplied collection are
   ** accepted.
   ** <br>
   ** Any remaining value is converted to lower case.
   **
   ** @param  value              the {@link String} to convert.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the converted collection where each value is
   **                            separated by <code>,</code>.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  private static List<String> stream(final String value) {
    // prevent bogus input
    return StringUtility.empty(value) ? CollectionUtility.list() : Stream.of(value.split(",", -1)).map(String::toLowerCase).filter(Objects::nonNull).collect(Collectors.toList());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stream
  /**
   ** Converting a {@link List} of {@link String}s to a {@link String} with all
   ** the values of the {@link List} comma separated.
   ** <br>
   ** Only non-<code>null</code> elements of the supplied collection are
   ** accepted.
   ** <br>
   ** Any remaining value is converted to lower case.
   **
   ** @param  value              the collection {@link String}s to convert.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the converted {@link String} value.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  private static String stream(final List<String> value) {
    // prevent bogus input
    return CollectionUtility.empty(value) ? null : value.stream().map(String::toLowerCase).filter(Objects::nonNull).sorted().collect(Collectors.joining(","));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stream
  private static String jid(final String domain, final String name) {
    return String.format("%s@%s", name, domain).toLowerCase();
  }
}