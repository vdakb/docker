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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Service Extension
    Subsystem   :   GlassFish Server Security Realm

    File        :   Realm.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Realm.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-03-11  DSteding    First release version
*/

package oracle.iam.access.glassfish.realm;

import java.util.Map;
import java.util.Vector;
import java.util.HashMap;
import java.util.Properties;

import com.sun.enterprise.security.BaseRealm;

import com.sun.enterprise.security.auth.realm.BadRealmException;
import com.sun.enterprise.security.auth.realm.NoSuchRealmException;

////////////////////////////////////////////////////////////////////////////////
// class Realm
// ~~~~~ ~~~~~
/**
 ** Implementation of <b>Realm</b> that authenticates users via the <em>Java
 ** Authentication and Authorization Service</em> (JAAS).
 ** <p>
 ** The value configured for the <code>realmName</code> property is passed to
 ** the <code>javax.security.auth.login.LoginContext</code> constructor, to
 ** specify the <em>application name</em> used to select the set of relevant
 ** <code>LoginModules</code> required.
 ** <p>
 ** The JAAS Specification describes the result of a successful login as a
 ** <code>javax.security.auth.Subject</code> instance, which can contain zero or
 ** more <code>java.security.Principal</code> objects in the return value of the
 ** <code>Subject.getPrincipals()</code> method.
 ** <br>
 ** However, it provides no guidance on how to distinguish Principals that
 ** describe the individual user (and are thus appropriate to return as the
 ** value of request.getUserPrincipal() in a web application) from the
 ** Principal(s) that describe the authorized roles for this user.
 ** <br>
 ** To maintain as much independence as possible from the underlying
 ** <code>LoginMethod</code> implementation executed by JAAS, the following
 ** policy is implemented by this Realm:
 ** <ul>
 **   <li>The JAAS <code>LoginModule</code> is assumed to return a
 **        <code>Subject</code> with at least one <code>Principal</code>
 **        instance representing the user himself or herself, and zero or more
 **        separate <code>Principals</code> representing the security roles
 **        authorized for this user.
 **    <li>On the <code>Principal</code> representing the user, the Principal
 **        name is an appropriate value to return via the Servlet API method
 **        <code>HttpServletRequest.getRemoteUser()</code>.
 **    <li>On the <code>Principals</code> representing the security roles, the
 **        name is the name of the authorized security role.
 **    <li>This Realm will be configured with two lists of fully qualified Java
 **        class names of classes that implement
 **        <code>java.security.Principal</code> - one that identifies class(es)
 **        representing a user, and one that identifies class(es) representing a
 **        security role.
 **    <li>As this Realm iterates over the <code>Principals</code> returned by
 **        <code>Subject.getPrincipals()</code>, it will identify the first
 **        <code>Principal</code> that matches the "user classes" list as the
 **        <code>Principal</code> for this user.
 **    <li>As this Realm iterates over the <code>Principals</code> returned by
 **        <code>Subject.getPrincipals()</code>, it will accumulate the set of
 **        all <code>Principals</code> matching the "role classes" list as
 **        identifying the security roles for this user.
 **    <li>It is a configuration error for the JAAS login method to return a
 **        validated <code>Subject</code> without a <code>Principal</code> that
 **        matches the "user classes" list.
 ** </ul>
 ** <P>
 ** The <b>Realm</b> needs the following properties in its configuration:
 ** <ul>
 **   <li>jaas-context - JAAS context name used to access LoginModule for
 **                      authentication.
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Realm extends BaseRealm {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected Map<String, Vector<String>> groupCache = new HashMap<>();;  

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a {@link BaseRealm} that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** This is followed by a call to the init() method.
   */
  public Realm() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   init
  /**
   ** Initialize a realm with some properties.
   ** <br>
   ** This can be used when instantiating realms from their descriptions. This
   ** method is invoked from Realm during initialization.
   ** <br>
   ** <b>Important</b>:
   ** It has to be ensured that subclasses calling
   ** {@code super.init(Properties)}. If not be aware of an
   ** {@code LoginExcaption} with the detail message
   ** <code>Invalid null input: name</code>.
   **
   ** @param  properties         the initialization parameters used by this
   **                            realm.
   **                            <br>
   **                            Allowed object is {@link Properties}.
   **
   ** @throws BadRealmException    if the configuration parameters identify a
   **                              corrupt realm.
   ** @throws NoSuchRealmException if the configuration parameters specify a
   **                              realm which doesn't exist.
   */
  @Override
  protected void init(final Properties properties)
    throws BadRealmException
    ,      NoSuchRealmException {

    // ensure inheritance
    super.init(properties);

    // add the required property called jaas-context
    // this property will make the bridge between the configured realm in
    // Glassfish and the authentication module to use
    // the web application will use the value provided by jaas-context to
    // configure the login configuration like
    // <login-config>
    //   <auth-method>CLIENT-CERT,DIGEST,FORM,BASIC</auth-method>
    //   <realm-name>MyRealm</realm-name>
    // </login-config>
    setProperty(JAAS_CONTEXT_PARAM, propertyRequired(properties, JAAS_CONTEXT_PARAM));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propertyRequired
  /**
   ** Obtain a property from the specified {@link Properties} by its
   ** <code>name</code>.
   **
   ** @param  store              the store providing the properties.
   **                            <br>
   **                            Allowed object is {@link Properties}.
   ** @param  name               the name of the property from the store.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value in the property <code>store</code>
   **                            with the specified key value,
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws BadRealmException  if the configuration parameters identify a
   **                            corrupt realm.
   */
  protected String propertyRequired(final Properties store, final String name)
    throws BadRealmException {

    final String value = store.getProperty(name);
    if (value == null || value.length() == 0)
      throw new BadRealmException(RealmBundle.string(RealmError.PROPERTY_IS_NULL, name));

    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propertyOptional
  /**
   ** Obtain a property from the specified {@link Properties} by its
   ** <code>name</code>.
   **
   ** @param  store              the store providing the properties.
   **                            <br>
   **                            Allowed object is {@link Properties}.
   ** @param  name               the name of the property from the store.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  defaultValue       a default value in case no value is mapped at
   **                            <code>name</code> in the property
   **                            <code>store</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value in the property <code>store</code>
   **                            with the specified key value,
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected String propertyOptional(final Properties store, final String name, final String defaultValue) {
    return store.getProperty(name, defaultValue);
  }
}