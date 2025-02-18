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

    File        :   OAuthUtil.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   jovan.j.lakic@oracle.com

    Purpose     :   This file implements the class
                    OAuthUtil.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-19-07  JLakic     First release version
*/
package bka.iam.access.oidc.extension.utils;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Base64;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;

import java.util.logging.Logger;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.NoSuchAlgorithmException;

import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;

import bka.iam.access.oidc.extension.Extension;

public class OAuthUtil {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Class name captured for logging purpose
   */
  private static final String   CLASS  = OAuthUtil.class.getName();
  /**
   ** Logger created based on the class name
   */
  private static final Logger   LOGGER = Logger.getLogger(CLASS);

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domainIdentityStore
  /**
   ** Returns the <code>IdentityStore</code> configured for an
   ** <code>IdentityDomain</code>.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** The <code>IdentityStore</code> does not have to match any of the
   ** <code>IdentityStore</code>s specified in the configuration of the Access
   ** Manager itself either as <code>$_DEFAULt_$</code> or
   ** <code>$_SYSTEM_$</code>.
   **
   ** @param  domain             the name of the <code>IdentityDomain</code> for
   **                            which the <code>IdentityStore</code> should be
   **                            returned.
   **
   ** @return                    the <code>IdentityStore</code> attached to the
   **                            <code>IdentityDomain</code> mapped as
   **                            <code>domain</code> in the configuration.
   **
   ** @throws Exception          if domain entity configuration could not be
   **                            parsed.
   */
  public static String domainIdentityStore(final String domain)
    throws Exception {

    final String method = "domainIdentityStore";
    LOGGER.entering(CLASS, method, domain);

    final Map<String, String> entity  = OAMUtil.entityAttributes(Extension.DOMAIN_PATH + domain);
    final String              generic = entity.get("genericattr");

    String identityStore = "";
    if (generic != null) {
      try {
        final HashMap config = Extension.unmarshal(generic, HashMap.class);
        LOGGER.info("Identity Domain Map for " + domain + ":" + config);
        identityStore = (String)config.get("identityProvider");
      }
      catch (Throwable t) {
        LOGGER.throwing(CLASS, method, t);
        LOGGER.exiting(CLASS, method, domain);
        // rethrow exception occured
        throw t;
      }
    }
    LOGGER.info("Identity Store " + domain + "::" + identityStore);
    LOGGER.exiting(CLASS, method, identityStore);
    return identityStore.trim();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domainPrivateKey
  /**
   ** Returns the {@link PrivateKey} of the <code>IdentityDomain</code> used for
   ** signing purpose.
   **
   ** @param  domain             the name of the <code>IdentityDomain</code> for
   **                            which the{@link PrivateKey} should be returned.
   **
   ** @return                    the {@link PrivateKey} attached to the
   **                            <code>IdentityDomain</code> mapped as
   **                            <code>domain</code> in the configuration.
   **
   ** @throws Exception          if key algorithm or key specification isn't
   **                            supported.
   */
  public static PrivateKey domainPrivateKey(final String domain)
    throws Exception {

    final String method = "domainPrivateKey";
    LOGGER.entering(CLASS, method, domain);
    final Map<String, String> entity = OAMUtil.entityAttributes(Extension.KEYSTORE_PATH + domain + "-key");
    final String              generic = entity.get("genericattr");
    try {
      final KeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(generic));
      return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    }
    catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      LOGGER.throwing(CLASS, method, e);
      throw e;
    }
    finally {
      LOGGER.exiting(CLASS, method, domain);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceServerFromScope
  /**
   ** Returns the collection of <code>ResourceServer</code> names for the
   ** specified collection of scopes.
   **
   ** @param  scopes             the collection of scopes to examine.
   **
   ** @return                    the collection of <code>ResourceServer</code>
   **                            names derived from <code>scopes</code>.
   */
  public static Collection<String> resourceServerFromScope(final Collection<String> scopes) {
    final String method = "resourceServerFromScope";
    LOGGER.entering(CLASS, method, scopes);
    final Collection<String> collector = new HashSet<String>();
    if (scopes != null) {
      for (String scope : scopes) {
        if (scope.contains(".")) {
          collector.add(scope.split("\\.")[0]);
        }
      }
    }
    LOGGER.info("Resource servers from scopes " + scopes + ":" + collector);
    LOGGER.exiting(CLASS, method, scopes);
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceServerGroupAttribute
  public static Collection<String> resourceServerGroupAttribute(final String domain, final Collection<String> resources)
    throws Exception {

    final String   method = "resourceServerGroupAttribute";
    final Object[] args   = new Object[]{domain, resources};
    LOGGER.entering(CLASS, method, args);

    final Set<String> collector = new HashSet<String>();
    for (String cursor : resources) {
      final String attribute = resourceServerGroupAttribute(domain, cursor);
      if (attribute != null)
        collector.add(attribute);
    }
    LOGGER.exiting(CLASS, method, collector);
    return collector;
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceServerGroupAttribute
  public static String resourceServerGroupAttribute(final String domain, final String resource)
    throws Exception {

    final String   method = "resourceServerGroupAttribute";
    final String[] args   = new String[]{domain, resource};
    LOGGER.entering(CLASS, method, args);

    String attribute = null;
    final List<Map<String, String>> attributes = resourceServerTokenAttributes(domain, resource);
    for (Map<String, String> cursor : attributes) {
      final String expression = cursor.get("attrValue");
      if ("$user.groups".equals(expression)) {
        attribute = cursor.get("attrName");
      }
    }
    LOGGER.exiting(CLASS, method, attribute);
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceServerTokenAttributes
  public static List<Map<String, String>> resourceServerTokenAttributes(final String domain, final String resource)
    throws Exception {

    final String   method = "getResourceServerTokenAttributes";
    final String[] args   = new String[]{domain, resource};
    LOGGER.entering(CLASS, method, args);

    final Map<String, String> entity  = OAMUtil.entityAttributes(Extension.DOMAIN_PATH + domain + "/OAuthResourceServer/" + resource);
    final String              generic = entity.get("genericattr");
    LOGGER.info("Resource Server Attributes for " + resource + ":" + generic);

    List<Map<String, String>> collector = new ArrayList<Map<String, String>>();
    if (generic != null) {
      try {
        final Map<String, ?> map = Extension.unmarshal(generic.trim(), HashMap.class);
        String value = "";
        final Object subject = map.get("tokenAttributes");
        if (subject != null) {
          value = (subject instanceof String)  ? (String)subject :  (subject instanceof ArrayList) ? ((ArrayList)subject) + "" : "";
        }
        value = value.replace(",\"attrType\":DYNAMIC", "").replace(",\"attrType\":STATIC", "");
        collector.addAll(Extension.unmarshal(value, ArrayList.class));
        LOGGER.info("Token Attributes for " + resource + ":" + collector);
      }
      catch (Throwable t) {
        LOGGER.throwing(CLASS, method, t);
        LOGGER.exiting(CLASS, method, args);
        // rethrow exception occured
        throw t;
      }
    }
    LOGGER.exiting(CLASS, method, args);
    return collector;
  }
}