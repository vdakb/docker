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

    System      :   Oracle Access Manager
    Subsystem   :   OpenIdConnect Extension

    File        :   UserProfileUtil.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   jovan.j.lakic@oracle.com

    Purpose     :   This file implements the class
                    UserProfileUtil.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-19-07  JLakic     First release version
*/

package bka.iam.access.oidc.extension.utils;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;

import java.util.logging.Logger;

import java.util.stream.Collectors;

import java.security.Principal;

import oracle.security.am.engines.common.identity.provider.UserInfo;
import oracle.security.am.engines.common.identity.provider.GroupInfo;
import oracle.security.am.engines.common.identity.provider.UserIdentityProvider;
import oracle.security.am.engines.common.identity.provider.UserIdentityProviderFactory;

import oracle.security.am.engines.common.identity.provider.util.ConfigUtil;

import oracle.security.am.engines.common.identity.provider.exceptions.IdentityProviderException;

import oracle.security.restsec.jwt.JwtToken;

import bka.iam.access.oidc.extension.Extension;

public class UserProfileUtil {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Class name captured for logging purpose
   */
  private static final String   CLASS  = UserProfileUtil.class.getName();
  /**
   ** Logger created based on the class name
   */
  private static final Logger   LOGGER = Logger.getLogger(CLASS);
  private static final String[] NOARGS = new String[0];

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepare
  /**
   ** Populates the collection of attributes belonging to the specified
   ** <code>accessToken</code>.
   ** <p>
   ** The implementation obtains the {@link UserIdentityProvider} from the
   ** configuration of the <code>IdentityDomain</code> specified by
   ** <code>domain</code> to populate the collection of groups across the
   ** <code>ResourceServer</code> in scope.
   ** <p>
   ** The <code>ResourceServer</code> in scope are examined by the token claim
   ** <code>scope</code>.
   ** <p>
   ** The attribute mappings returned are evaluated by a <code>Template</code>
   ** per <code>ResourceServer</code> that name have to exactly match the
   ** scope.
   **
   ** @param  accessToken        the access token string issued by
   **                            <code>IdentityDomain</code>
   **                            <code>domain</code>.
   ** @param  domain             the name of the <code>IdentityDomain</code>
   **                            that issued <code>accessToken</code>.
   **
   ** @return                    the collection of tagged-value pairs.
   **
   ** @throws Exception          in case any error occured.
   */
  public static Map<String, ?> prepare(final String accessToken, final String domain)
    throws Exception {

    final String   method = "prepare";
    final String[] args   = new String[]{domain, accessToken};
    LOGGER.entering(CLASS, method, args);

    final JwtToken             jwtToken      = new JwtToken(accessToken);
    final String               identityStore = OAuthUtil.domainIdentityStore(domain);
    final Map<String, Object>  property      = jwtToken.getClaimParameters();
    // obtains the scopes from the token
    final Collection<String>   scopes        = (Collection<String>)property.get("scope");
    // populate the resource servers for the remaining scopes
    final Collection<String>   resources     = OAuthUtil.resourceServerFromScope(scopes);
    LOGGER.info("Resource servers in scope: " + scopes);

    final Set<String> claims = new LinkedHashSet<String>();
    for (String cursor : resources) {
      final Map<String, String> entity   = OAMUtil.entityAttributes(Extension.TEMPLATE_PATH + cursor);
      final String              template = entity.get("template");
      LOGGER.info("Template for " + cursor + "::" + template);
      if (template != null) {
        final Map<String, ?> map = Extension.unmarshal(template, HashMap.class);
        final String         def = (String)map.get("valueMapping");
        // split def based on colon, remove white-spaces and collect to Set
        Arrays.stream(def.split("\\:")).map(str -> str.trim()).collect(Collectors.toCollection(() -> claims));
      }
    }
    // copy the custom claims from the token to the collector
    final Map<String, Object> collector = claims.isEmpty()
      ? Collections.emptyMap()
      : property.entrySet().stream().filter(claim -> !"groups".equals(claim) && claims.contains(claim.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    ;
    // extend the returned information with the group permissions if requested
    // by the template value mapping
    if (claims.contains("groups"))
      collector.put("groups", memberOf(jwtToken.getSubject(), identityStore, resources));
    LOGGER.exiting(CLASS, method, collector);
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   memberOf
  /**
   ** Populates the collection of groups belonging to the specified
   ** <code>username</code> in the <code>IdentityStore</code> given by
   ** <code>identityStore</code>.
   ** <p>
   ** The implementation obtains the {@link UserIdentityProvider} from the
   ** configuration mapped at <code>identityStore</code>.
   ** <p>
   ** The collection of <code>ResourceServer</code> in scope are examined by
   ** <code>resources</code>.
   **
   ** @param  username           the name of the user to locate in the
   **                            <code>IdentityStore</code> configured at
   **                            <code>identityStore</code>.
   ** @param  identityStore      the name of the <code>IdentityStore</code>
   **                            providing access to the underlying directory.
   ** @param  resources          the colection of <code>ResourceServer</code>s
   **                            to examine.
   **
   ** @return                    the collection of simple group names.
   */
  public static List<String> memberOf(final String username, final String identityStore, final Collection<String> resources) {
    final String method="memberOf";
    LOGGER.entering(CLASS, method);
    final List<String> collector = new ArrayList<String>();
    try {
      final Map<String, Object>  config   = ConfigUtil.getInitParams(identityStore);
      final String               context  = rootDN((String)config.get("USER_SEARCH_BASE"));
      final UserIdentityProvider provider = UserIdentityProviderFactory.getProvider(identityStore);
      for (String cursor : resources) {
        final List<GroupInfo> memberOf = memberOf(provider, context, cursor, username);
        if (memberOf != null)
          memberOf.stream().map(GroupInfo::getGroupId).collect(Collectors.toCollection(() -> collector));
      }
    }
    catch (Exception e) {
      LOGGER.finer("Filtering groups for " + username + " failed:");
      LOGGER.throwing(CLASS, method, e);
      LOGGER.exiting(CLASS, method, collector);
    }
    LOGGER.info("Filtered Groups: " + collector);
    LOGGER.exiting(CLASS, method, collector);
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   memberOf
  /**
   ** Populates the collection of  {@link GroupInfo}s belonging to the specified
   ** <code>username</code> in the <code>IdentityStore</code> given by
   ** <code>provider</code>.
   ** <p>
   ** The <code>ResourceServer</code> in scope is examined by <code>resource</code>.
   **
   ** @param  provider           the <code>IdentityStore</code> providing access
   **                            to the underlying directory.
   ** @param  context            the generell search base for
   **                            <code>ResourceServer</code>s.
   ** @param  resource           the <code>ResourceServer</code> to examine.
   ** @param  username           the simple name of the user to locate in the
   **                            <code>IdentityStore</code> at
   **                            <code>context</code>.
   **
   ** @return                    the collection of {@link GroupInfo}s.
   **
   ** @throws IdentityProviderException if the user cannot be located.
   */
  public static final List<GroupInfo> memberOf(final UserIdentityProvider provider, final String context, final String resource, final String username)
    throws IdentityProviderException {

    final String method="memberOf";
    final String[] args   = new String[]{provider.getName(), context, resource, username};
    LOGGER.entering(CLASS, method, args);

    final String    base      = "ou=" + resource + ",cn=Services," + context;
    final String    filter    = "(uid=" + username + ")";
    List<GroupInfo> collector = null;
    // don't fail if a user cannot be located due to not provisioned to the
    // application
    LOGGER.finer(String.format("Locate user in Resource Server [%s] search base: [%s] filter: [%s]", resource, base, filter));
    try {
      Collection<UserInfo> users = provider.locateUser(base, filter, NOARGS);
      for (UserInfo user : users) {
        final Principal principal = user.getPrincipal();
        LOGGER.finer(String.format("Search membership for [%s] in Resource Server [%s]", principal, resource));
        collector = provider.getGroupInfoListForUser(principal);
        LOGGER.finer(String.format("Membership found for [%s] in Resource Server [%s]: [%s]", username, resource, collector.toString()));
      }
    }
    catch (IdentityProviderException e) {
      // failure locating user with the search base and filter.
      // error code OAMSSA-20070 // die silently
      if ("OAMSSA-20070".equals(e.getMessageID()))
        LOGGER.warning(String.format("User [%s] was not provisioned in Resource Server [%s]", username, resource));
      // re-throw exception
      else
        throw e;
    }
    finally {
      LOGGER.exiting(CLASS, method);
    }
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rootDN
  public static String rootDN(final String dn) {
    final StringBuilder builder = new StringBuilder();
    // the string needs to be splitted and traveresed in reverse order
    // the first occurence of a segment thats not starts with dc is the stop key
    // of the loop
    final String[] segment = dn.split(",");
    for (int i = segment.length; i >= 0; i--) {
      final int j = i - 1;
      // if the first non-suffix segement is detected assume we are on the right
      // track
      if (!segment[j].trim().toLowerCase().startsWith("dc")) {
        break;
      }
      if (builder.length() > 0)
        builder.insert(0, ",");
      builder.insert(0, segment[j]);
    }
    return builder.toString();
  }
}