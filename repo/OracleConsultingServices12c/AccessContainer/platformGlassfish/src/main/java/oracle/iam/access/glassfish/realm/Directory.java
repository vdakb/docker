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

    File        :   Directory.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Directory.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-03-11  DSteding    First release version
*/

package oracle.iam.access.glassfish.realm;

import java.util.Set;
import java.util.Properties;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;

import java.util.regex.Matcher;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.NamingEnumeration;

import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchResult;
import javax.naming.directory.SearchControls;
import javax.naming.directory.InitialDirContext;

import javax.naming.ldap.Rdn;
import javax.naming.ldap.LdapName;

import javax.security.auth.login.LoginException;

import com.sun.enterprise.security.auth.realm.BadRealmException;
import com.sun.enterprise.security.auth.realm.NoSuchRealmException;

////////////////////////////////////////////////////////////////////////////////
// class Directory
// ~~~~~ ~~~~~~~~~
/**
 ** Implementation of a <b>Realm</b> that authenticates users via the <em>Java
 ** Authentication and Authorization Service</em> (JAAS) leveraging a directory
 ** service.
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
 ** The <b>Directory</b> needs the following properties in its
 ** configuration:
 ** <br>
 ** <b>Required</b>
 ** <ul>
 **   <li>jaas-context     - JAAS context name used to access LoginModule for
 **                          authentication.
 **   <li>context-url      -
 **   <li>base-dn          -
 **   <li>bind-dn          -
 **   <li>bind-password    -
 ** </ul>
 ** <br>
 ** <b>Optional</b>
 ** <ul>
 **   <li>user-base-dn     - the base DN to use for user searches.
 **                          <br>
 **                          By default its value is the same as base-dn.
 **   <li>user-filter      - the LDAP filter to use for searching users.
 **                          <br>
 **                          By default none.
 **   <li>group-base-dn    - the base DN to use for group searches.
 **                          <br>
 **                          By default its value is the same as base-dn.
 **   <li>group-filter     - the LDAP filter to use for searching group
 **                          membership of a given user.
 **                          <br>
 **                          By default <code>uniquemember=%d</code> where %d is
 **                          expanded to the DN of the user found by the user
 **                          search.
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Directory extends Realm {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String      SOCKET_FACTORY                       = "java.naming.ldap.factory.socket";
  static final String      SOCKET_FACTORY_DEFAULT               = "com.sun.enterprise.security.auth.realm.ldap.CustomSocketFactory";
  static final String      CONTEXT_FACTORY                      = "java.naming.factory.initial";
  static final String      CONTEXT_FACTORY_DEFAULT              = "com.sun.jndi.ldap.LdapCtxFactory";
  // expansion strings

  static final String      PREFIX_USER                          = "prefix-user";
  static final String      PREFIX_GROUP_DIRECT                  = "prefix-group-direct";
  static final String      PREFIX_GROUP_INDIRECT                = "prefix-group-indirect";

  static final String      PREFIX_USER_DEFAULT                  = "uid";
  static final String      PREFIX_GROUP_DIRECT_DEFAULT          = "cn";
  static final String      PREFIX_GROUP_INDIRECT_DEFAULT        = "memberOf";

  static final String      SEARCH_BASE_USER                     = "search-base-user";
  static final String      SEARCH_BASE_GROUP                    = "search-base-group";
  static final String      SEARCH_FILTER_USER                   = "search-filter-user";
  static final String      SEARCH_FILTER_GROUP_DIRECT           = "search-filter-group-direct";
  static final String      SEARCH_FILTER_GROUP_INDIRECT         = "search-filter-group-indirect";

  static final String      SEARCH_FILTER_USER_DEFAULT           = "uid=%u";
  static final String      SEARCH_FILTER_GROUP_DIRECT_DEFAULT   = "uniqueMember=%m";
  static final String      SEARCH_FILTER_GROUP_INDIRECT_DEFAULT = "uniqueMember=%m";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String           baseDN;
  private String           searchBaseUser;
  private String           userPrefix;
  private String           userFilter;
  private String           searchBaseGroup;
  private String           groupDirectPrefix;
  private String           groupDirectFilter;
  private String           groupIndirectPrefix;
  private String           groupIndirectFilter;

  private final String[]   returning   = {"dn"};
  private final Properties environment = new Properties();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Directory</code> {@link Realm} that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** This is followed by a call to the init() method.
   */
  public Directory() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstrcat base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAuthType (Realm)
  /**
   ** Returns a short (preferably less than fifteen characters) description of
   ** the kind of authentication which is supported by this realm.
   **
   ** @return                    the description of the kind of authentication
   **                            that is directly supported by this realm.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getAuthType() {
    return "ldap";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getGroupNames (Realm)
  /**
   ** Returns the name of all the groups that the specified user belongs to.
   ** <br>
   ** It loads the result from <code>groupCache</code> first. This is called
   ** from web path group verification, though it should not be.
   ** <p>
   ** <b>WARNING</b>:
   ** <br>
   ** Does not have access to user's entitlements, so it does not return groups
   ** based on assertion.
   **
   ** @param  username           the name of the user in this realm whose group
   **                            listing is needed.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the enumeration of group names assigned to all
   **                            users authenticated by this realm belonging to
   **                            the specified <code>username</code>.
   **                            <br>
   **                            Possible object is {@link Enumeration} where
   **                            each element is of type {@link String}.
   */
  @Override
  public Enumeration<String> getGroupNames(final String username) {
    return Collections.emptyEnumeration();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   init (overridden)
  /**
   ** Initialize a realm with some properties.
   ** <br>
   ** This can be used when instantiating realms from their descriptions. This
   ** method is invoked from Realm during initialization.
   **
   ** @param  store              the initialization parameters used by this
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
  protected void init(final Properties store)
    throws BadRealmException
    ,      NoSuchRealmException {

    // ensure inheritance
    super.init(store);

    this.baseDN              = propertyRequired(store, "base-dn");
    this.searchBaseUser      = propertyOptional(store, SEARCH_BASE_USER,             this.baseDN);
    this.searchBaseGroup     = propertyOptional(store, SEARCH_BASE_GROUP,            this.baseDN);

    this.userFilter          = propertyOptional(store, SEARCH_FILTER_USER,           SEARCH_FILTER_USER_DEFAULT);
    this.groupDirectFilter   = propertyOptional(store, SEARCH_FILTER_GROUP_DIRECT,   SEARCH_FILTER_GROUP_DIRECT_DEFAULT);
    this.groupIndirectFilter = propertyOptional(store, SEARCH_FILTER_GROUP_INDIRECT, SEARCH_FILTER_GROUP_INDIRECT_DEFAULT);

    this.userPrefix          = propertyOptional(store, PREFIX_USER,                  PREFIX_USER_DEFAULT);
    this.groupDirectPrefix   = propertyOptional(store, PREFIX_GROUP_DIRECT,          PREFIX_GROUP_DIRECT_DEFAULT);
    this.groupIndirectPrefix = propertyOptional(store, PREFIX_GROUP_INDIRECT,        PREFIX_GROUP_INDIRECT_DEFAULT);

    // transfer all LDAP context specific configurations to the environment
    // holder
    final Enumeration<?> cursor = store.propertyNames();
    while (cursor.hasMoreElements()) {
      final String name = (String)cursor.nextElement();
      if (name.startsWith("java.naming") || name.startsWith("javax.security") || name.startsWith("com.sun.jndi.ldap")) {
        this.environment.setProperty(name, store.getProperty(name));
      }
    }
    // set the context url explicitly to ensure proper validation
    final String contextURL = propertyRequired(store, "context-url");
    if (contextURL.startsWith("ldaps"))
      this.environment.setProperty(SOCKET_FACTORY, SOCKET_FACTORY_DEFAULT);

    this.environment.put(Context.PROVIDER_URL,            contextURL);
    this.environment.put(Context.INITIAL_CONTEXT_FACTORY, propertyOptional(store, "context-factory", CONTEXT_FACTORY_DEFAULT));
    // put the username credentials in the environment created above
    this.environment.put(Context.SECURITY_AUTHENTICATION, "simple");
    this.environment.put(Context.SECURITY_PRINCIPAL,      propertyRequired(store, "bind-dn"));
    this.environment.put(Context.SECURITY_CREDENTIALS,    propertyRequired(store, "bind-pwd"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authenticateUser
  /**
   ** Invoke the native authentication call.
   **
   ** @param  username           the username to authenticate.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the given password.
   **                            <br>
   **                            Allowed object is array of <code>char</code>.
   **
   ** @return                    the groups belonging to a valid user or
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is array of {@link String}.
   **
   ** @throws LoginException     if the validation (aka bind) is not
   **                            successfull.
   **/
  public String[] authenticateUser(final String username, final char[] password)
    throws LoginException {

    final String fqdn = validate(username, password);
    return (fqdn == null) ? null : addAssignGroups(authorize(fqdn));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertUser
  /**
   ** Invoke the native authentication call.
   **
   ** @param  username           the username to authenticate.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the groups belonging to a valid user or
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is array of {@link String}.
   **
   ** @throws LoginException     if the validation (aka bind) is not
   **                            successfull.
   **/
  public String[] assertUser(final String username)
    throws LoginException {

    final String fqdn = validate(username);
    return (fqdn == null) ? null : addAssignGroups(authorize(fqdn));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Validates the user credetials by lookup the user in the connected
   ** directory service and compares the password hashes.
   **
   ** @param  username           the username to authenticate.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the given password.
   **                            <br>
   **                            Allowed object is array of <code>char</code>.
   **
   ** @return                    the fullqualified distinguished name of an
   **                            authenticated user.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws LoginException     if the validation (aka bind) is not
   **                            successfull.
   */
  private String validate(final String username, final char[] password)
    throws LoginException {

    // a LDAP user must have a name not null and non-empty.
    if ( (username == null) || (username.length() == 0))
      throw new LoginException(sm.getString("ldaprealm.usernotfound", username));

    // do search for user, substituting %s for username
    final String searchFilter = substitute(new StringBuilder(this.userFilter), "%u", escape(username)).toString();
    DirContext context = aquire();
    try {
      final String fqdn = searchUser(context, this.searchBaseUser, searchFilter);
      if (!bindUser(fqdn, password))
        throw new LoginException(sm.getString("ldaprealm.bindfailed", username));

      return fqdn;
    }
    finally {
      release(context);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Validates the user credetials by lookup the user in the connected
   ** directory service and compares the password hashes.
   **
   ** @param  username           the username to authenticate.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the given password.
   **                            <br>
   **                            Allowed object is array of <code>char</code>.
   **
   ** @return                    the fullqualified distinguished name of an
   **                            authenticated user.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws LoginException     if the validation (aka bind) is not
   **                            successfull.
   */
  private String validate(final String username)
    throws LoginException {

    // a LDAP user must have a name not null and non-empty.
    if ( (username == null) || (username.length() == 0))
      throw new LoginException(sm.getString("ldaprealm.usernotfound", username));

    // do search for user, substituting %s for username
    final String searchFilter = substitute(new StringBuilder(this.userFilter), "%u", escape(username)).toString();
    DirContext context = aquire();
    try {
      final String fqdn = searchUser(context, this.searchBaseUser, searchFilter);
      return fqdn;
    }
    finally {
      release(context);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authorize
  /**
   ** Populates the permissions assigned to the given principal username.
   **
   ** @param  fqdn               the login name of the user principal the
   **                            permissions are populated for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the collected autorization names.
   **                            <br>
   **                          Possible object is array of {@link String}.
   **  
   ** @throws LoginException     if populating the permission fails.
   */
  private String[] authorize(final String fqdn)
    throws LoginException {

    final Set<String> collector = new LinkedHashSet<>();
    final DirContext  context   = aquire();
    try {
      directMemberOf(context, fqdn, collector);
      // search filter is constructed internally as as a groupOfURLS
      indirectMemberOf(context, fqdn, collector);
    }
    finally {
      release(context);
    }
    final String[] array = new String[collector.size()];
    return collector.toArray(array);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchUser
  /**
   ** Retrieves the RDN of the object with the specified objectGUID attribute.
   **
   ** @param  searchBase         the base DN to search from.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  searchFilter       the filter expression to use for the search
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the ful-qualified distinguished name of the
   **                            entry looked up by the applied search filter.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  private String searchUser(final DirContext context, final String searchBase, final String searchFilter) {
    final SearchControls searchControl = new SearchControls();
    searchControl.setCountLimit(1);
    searchControl.setReturningAttributes(this.returning);
    searchControl.setSearchScope(SearchControls.SUBTREE_SCOPE);
    NamingEnumeration resultSet = null;
    try {
      resultSet = context.search(searchBase, searchFilter, searchControl);
      if (resultSet.hasMore())
        return ((SearchResult)resultSet.next()).getNameInNamespace();
    }
    catch (Exception e) {
      // die silentliy
      e.printStackTrace(System.err);
    }
    finally {
      // free up resources aquired
      if (resultSet != null) {
        try {
          resultSet.close();
        }
        catch (Exception e) {
          // die silentliy
          e.printStackTrace(System.err);
        }
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bindUser
  /**
   ** Attemp to bind as specific distinguished name representing a user
   **
   ** @param  fqdn               the ful-qualified distinguished name
   **                            representing a user to authenticate.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the password to validate.
   **                            <br>
   **                            Allowed object is array of <code>char</code>.
   **
   ** @return                    <code>true</code> if the user password match;
   **                            otheriwse <code>false</code>.
   **
   ** @throws LoginException     if the validation (aka bind) is not
   **                            successfull.
   */
  private boolean bindUser(final String fqdn, final char[] password) {
    // create a shallow clone of the currently configured environment
    final Properties environment = (Properties)this.environment.clone();
    // set the context URL to bind to the fully qualified distinguished name,
    // assuming each entry is permitted to access itself at least (check ACL of
    // the connected directory service) if this assumption is not met, it is
    // highly likely that autehntication in general will not be possible
    environment.put(Context.PROVIDER_URL,         this.environment.getProperty(Context.PROVIDER_URL) + "/" + fqdn);
    // substitute the username credentials in the environment created above with
    // the credentials of the user to authenticate
    environment.put(Context.SECURITY_PRINCIPAL,   fqdn);
    environment.put(Context.SECURITY_CREDENTIALS, new String(password));
    DirContext context = null;
    try {
      context = aquire(environment);
      return true;
    }
    catch (LoginException e) {
      return false;
    }
    finally {
      release(context);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   directMemberOf
  /**
   ** Search for group membership using the given connection.
   *
   */
  private Set<String> directMemberOf(final DirContext context, final String fqdn, final Set<String> collector) {
    final String         filter  = substitute(new StringBuilder(this.groupDirectFilter), "%m", fqdn).toString();
    final SearchControls control = new SearchControls();
    // set this to false to avoid objects and hence exposing ldap object
    // injection
    control.setReturningObjFlag(false);
    control.setSearchScope(SearchControls.SUBTREE_SCOPE);
    control.setReturningAttributes(new String[]{this.groupDirectPrefix});
    try {
      final NamingEnumeration<SearchResult> e = context.search(this.searchBaseGroup, filter.replaceAll(Matcher.quoteReplacement("\\"), Matcher.quoteReplacement("\\\\")), control);
      while (e.hasMore()) {
        final SearchResult entry   = e.next();
        final Attribute    member  = entry.getAttributes().get(this.groupDirectPrefix);
        for (int i = 0; i < member.size(); i++) {
          collector.add((String)member.get(i));
        }
      }
    }
    catch (Exception e) {
      // die silentliy
      e.printStackTrace(System.err);
    }
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   indirectMemberOf
  /**
   ** Search for group membership using the given connection.
   */
  private Set<String> indirectMemberOf(final DirContext context, final String fqdn, final Set<String> collector) {
    final String         filter  = substitute(new StringBuilder(this.groupIndirectFilter), "%m", fqdn).toString();
    final SearchControls control = new SearchControls();
    // set this to false to avoid objects and hence exposing ldap object
    // injection
    control.setReturningObjFlag(false);
    control.setSearchScope(SearchControls.SUBTREE_SCOPE);
    control.setReturningAttributes(new String[]{this.groupIndirectPrefix});
    try {
      final NamingEnumeration e = context.search(this.searchBaseGroup, filter, control);
      while (e.hasMore()) {
        final SearchResult entry    = (SearchResult)e.next();
        final Attribute    memberOf = entry.getAttributes().get(this.groupIndirectPrefix);
        if (memberOf != null) {
          for (Enumeration values = memberOf.getAll(); values.hasMoreElements();) {
            final LdapName name   = new LdapName((String)values.nextElement());
            for (Rdn rdn : name.getRdns()) {
              if (rdn.getType().equalsIgnoreCase(this.groupDirectPrefix)) {
                collector.add(rdn.getValue().toString());
                break;
              }
            }
          }
        }
      }
    }
    catch (Exception e) {
      // die silentliy
      e.printStackTrace(System.err);
    }
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   substitute
  /**
   ** Do string substitution.
   ** <br>
   ** <code>pattern</code> is replaced by value for all occurences.
   **
   ** @param  buffer             the {@link StringBuilder} providing access to
   **                            the character sequence to manipulate.
   **                            <br>
   **                            Allowed object is {@link StringBuilder}.
   ** @param  pattern            the character sequence to replace.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the character sequence to replace all
   **                            occurences of <code>pattern</code> with.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link StringBuilder} passed in weher each
   **                            occurence of <code>pattern</code> is replaced
   **                            with <code>value</code>.
   **                            <br>
   **                            Possible object is {@link StringBuilder}.
   */
  private static StringBuilder substitute(final StringBuilder buffer, final String pattern, final String value) {
    int i = buffer.indexOf(pattern);
    while (i >= 0) {
      buffer.replace(i, i + pattern.length(), value);
      i = buffer.indexOf(pattern);
    }
    return buffer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   aquire
  /**
   ** Constructs an initial {@link DirContext} using the pre-configured
   ** environment.
   **
   ** @return                    the context this connector use to communicate
   **                            with the directory server.
   **                            <br>
   **                            Possible object is {@link DirContext}.
   **
   ** @throws LoginException     if the {@link DirContext} could not be
   **                            created.
   */
  private DirContext aquire()
   throws LoginException {

    return aquire(this.environment);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   aquire
  /**
   ** Constructs an initial {@link DirContext} using the pre-configured
   ** environment.
   **
   ** @param  environment        environment used to create the
   **                            {@link InitialDirContext}.
   **                            <code>null</code> indicates an empty
   **                            environment.
   **                            <br>
   **                            Allowed object is {@link Properties}.
   **
   ** @return                    the context this connector use to communicate
   **                            with the directory server.
   **                            <br>
   **                            Possible object is {@link DirContext}.
   **
   ** @throws LoginException     if the {@link InitialDirContext} could not be
   **                            created.
   */
  private DirContext aquire(final Properties environment)
   throws LoginException {

    try {
      // constructs a directory context object using environment properties
      // See javax.naming.InitialContext for a discussion of environment
      // properties.
      return new InitialDirContext(environment);
    }
    catch (NamingException e) {
      throw new LoginException(RealmBundle.string(RealmError.CONTEXT_INITIALIZE, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   release
  /**
   ** Closes an unmanaged directory context.
   ** <br>
   ** This method releases the context's resources immediately, instead of
   ** waiting for them to be released automatically by the garbage collector.
   ** <p>
   ** This method is idempotent:  invoking it on a context that has already been
   ** closed has no effect. Invoking any other method on a closed context is not
   ** allowed, and results in undefined behaviour.
   **
   ** @param  context            the {@link DirContext} to close.
   **                            <br>
   **                            Allowed object is {@link DirContext}.
   */
  private void release(final DirContext context) {
    if (context != null) {
      try {
        context.close();
      }
      catch (NamingException e) {
        // die silentliy
        e.printStackTrace(System.err);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   escape
  /**
   ** Escape special characters, according to RFC2254 (The String Representation
   ** of LDAP Search Filters)
   **
   ** @param  name               a name representing a LDAP serch filter.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the name representing a LDAP serch filter with
   **                            special characters escaped, according to
   **                            RFC2254.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  private String escape(final String inName) {
    final int           length = inName.length();
    final StringBuilder buffer = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      char ch = inName.charAt(i);
      switch (ch) {
        case '*'  : buffer.append("\\2a");
                    break;
        case '('  : buffer.append("\\28");
                    break;
        case ')'  : buffer.append("\\29");
                    break;
        case '\\' : buffer.append("\\5c");
                    break;
        case 0    : buffer.append("\\00");
                    break;
        default   : buffer.append(ch);
      }
    }
    return buffer.toString();
  }
}