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

    System      :   Oracle Directory Service Utility Library
    Subsystem   :   Deployment Utilities 11g

    File        :   EntryHandler.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    EntryHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.directory.common.spi.handler;

import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.NamingEnumeration;

import javax.naming.directory.SearchResult;
import javax.naming.directory.SearchControls;
import javax.naming.directory.InvalidAttributesException;
import javax.naming.directory.InvalidAttributeIdentifierException;

import javax.naming.ldap.LdapContext;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.spi.AbstractHandler;

import oracle.iam.directory.common.FeatureError;
import oracle.iam.directory.common.FeatureException;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractFeatureHandler
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Invokes common operation on LDAP Server.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class AbstractFeatureHandler extends AbstractHandler {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the business logic layer to operate on DIT */
  protected LdapContext facade;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>EntryHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  protected AbstractFeatureHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (AbstractHandler)
  /**
   ** Called to inject the argument for parameter <code>name</code>.
   **
   ** @param  name               the name of the alias.
   **
   ** @throws BuildException     always.
   */
  @Override
  public void name(final String name)
    throws BuildException {

    throw new BuildException(ServiceResourceBundle.string(ServiceError.NOTIMPLEMENTED));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pair to the parameters that has to be applied
   ** after an import operation.
   **
   ** @param  name               the name of the parameter of the Oracle
   **                            WebLogic Domain entity instance.
   ** @param  value              the value for <code>name</code> to set on the
   **                            LDAP Context instance.
   **
   ** @throws BuildException     always.
   */
  @Override
  public void addParameter(final String name, final Object value)
    throws BuildException {

    throw new BuildException(ServiceResourceBundle.string(ServiceError.NOTIMPLEMENTED));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  parameter          the named value pairs to be applied on the
   **                            LDAP Context instance.
   **
   ** @throws BuildException     always.
   */
  @Override
  public void addParameter(final Map<String, Object> parameter)
    throws BuildException {

    throw new BuildException(ServiceResourceBundle.string(ServiceError.NOTIMPLEMENTED));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   ** Searches in the specified context or object for entries that satisfy the
   ** given search filter. Performs the search as specified by the search
   ** controls.
   ** <p>
   ** The format and interpretation of <code>filter</code> fullows RFC 2254 with
   ** the fullowing interpretations for <code>attr</code> and <code>value</code>
   ** mentioned in the RFC.
   ** <p>
   ** <code>attr</code> is the attribute's identifier.
   ** <p>
   ** <code>value</code> is the string representation the attribute's value.
   ** The translation of this string representation into the attribute's value
   ** is directory-specific.
   ** <p>
   ** For the assertion "someCount=127", for example, <code>attr</code>
   ** is "someCount" and <code>value</code> is "127". The provider determines,
   ** based on the attribute ID ("someCount") (and possibly its schema), that
   ** the attribute's value is an integer. It then parses the string "127"
   ** appropriately.
   ** <p>
   ** Any non-ASCII characters in the filter string should be represented by the
   ** appropriate Java (Unicode) characters, and not encoded as UTF-8 octets.
   ** Alternately, the "backslash-hexcode" notation described in RFC 2254 may be
   ** used.
   ** <p>
   ** If the directory does not support a string representation of some or all
   ** of its attributes, the form of <code>search</code> that accepts filter
   ** arguments in the form of Objects can be used instead. The service provider
   ** for such a directory would then translate the filter arguments to its
   ** service-specific representation for filter evaluation.
   ** See <code>search(Name, String, Object[], SearchControls)</code>.
   ** <p>
   ** RFC 2254 defines certain operators for the filter, including substring
   ** matches, equality, approximate match, greater than, less than.  These
   ** operators are mapped to operators with corresponding semantics in the
   ** underlying directory. For example, for the equals operator, suppose
   ** the directory has a matching rule defining "equality" of the
   ** attributes in the filter. This rule would be used for checking
   ** equality of the attributes specified in the filter with the attributes
   ** of objects in the directory. Similarly, if the directory has a
   ** matching rule for ordering, this rule would be used for
   ** making "greater than" and "less than" comparisons.
   ** <p>
   ** Not all of the operators defined in RFC 2254 are applicable to all
   ** attributes. When an operator is not applicable, the exception
   ** <code>InvalidSearchFilterException</code> is thrown.
   ** <p>
   ** The result is returned in an enumeration of <code>SearchResult</code>s.
   ** Each <code>SearchResult</code> contains the name of the object and other
   ** information about the object (see SearchResult). The name is either
   ** relative to the target context of the search (which is named by the
   ** <code>name</code> parameter), or it is a URL string. If the target context
   ** is included in the enumeration (as is possible when <code>controls</code>
   ** specifies a search scope of <code>SearchControls.OBJECT_SCOPE</code> or
   ** <code>SearchControls.SUBSTREE_SCOPE</code>), its name is the empty string.
   ** The <code>SearchResult</code> may also contain attributes of the matching
   ** object if the <code>controls</code> argument specified that attributes be
   ** returned.
   ** <p>
   ** If the object does not have a requested attribute, that nonexistent
   ** attribute will be ignored. Those requested attributes that the object does
   ** have will be returned.
   ** <p>
   ** A directory might return more attributes than were requested (see
   ** <strong>Attribute Type Names</strong> in the class description) but is not
   ** allowed to return arbitrary, unrelated attributes.
   ** <p>
   ** See also <strong>Operational Attributes</strong> in the class description.
   **
   ** @param  searchBase         the base DN to search from
   ** @param  searchFilter       the filter expression to use for the search;
   **                            may not be null, e.g.
   **                            "(&amp; (objectclass=*)(cn=abraham))"
   ** @param  searchControls     the search controls that control the search. If
   **                            <code>null</code>, the default search controls
   **                            are used (equivalent to
   **                            <code>(new SearchControls())</code>).
   **
   ** @return                    an enumeration of <code>SearchResult</code>s of
   **                            the objects that satisfy the filter; never
   **                            <code>null</code>.
   **
   ** @throws FeatureException  in case the search operation cannot be
   **                            performed.
   */
  protected NamingEnumeration<SearchResult> search(final String searchBase, final String searchFilter, final SearchControls searchControls)
    throws FeatureException {

    try {
      return this.facade.search(searchBase, searchFilter, searchControls);
    }
    catch (InvalidAttributesException e) {
      throw new FeatureException(FeatureError.ATTRIBUTE_DATA, searchFilter, e);
    }
    catch (InvalidAttributeIdentifierException e) {
      throw new FeatureException(FeatureError.ATTRIBUTE_TYPE, searchFilter, e);
    }
    catch (NamingException e) {
      throw new FeatureException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextName
  /**
   ** Retrieves the full name of this context within its own namespace.
   ** <p>
   ** Many naming services have a notion of a "full name" for objects in their
   ** respective namespaces. For example, an LDAP entry has a distinguished
   ** name, and a DNS record has a fully qualified name.
   ** <br>
   ** This method allows the client application to retrieve this name.
   ** <br>
   ** The string returned by this method is not a JNDI composite name and should
   ** not be passed directly to context methods.
   ** <br>
   ** In naming systems for which the notion of full name does not make sense,
   ** <code>OperationNotSupportedException</code> is thrown.
   **
   ** @return                   this context's name in its own namespace;
   **                           never <code>null</code>.
   **
   ** @throws FeatureException  if a naming exception is encountered.
   */
  protected String contextName()
    throws FeatureException {

    try {
      return this.facade.getNameInNamespace();
    }
    catch (NamingException e) {
      throw new FeatureException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextClose
  /**
   ** Releases a {@link Context}'s resources immediately, instead of waiting
   ** for them to be released automatically by the garbage collector.
   ** <p>
   ** This method is idempotent: invoking it on a context that has already been
   ** closed has no effect. Invoking any other method on a closed context is not
   ** allowed, and results in undefined behaviour.
   **
   ** @param context             the {@link Context} to close.
   **
   ** @throws FeatureException in the specified context cannot be closed.
   */
  protected static void contextClose(final Context context)
    throws FeatureException {

    try {
      if (context != null) {
        context.close();
      }
    }
    catch (NamingException e) {
      throw new FeatureException(FeatureError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextClose
  /**
   ** Releases a {@link NamingEnumeration}'s resources immediately, instead of
   ** waiting for them to be released automatically by the garbage collector.
   ** <p>
   ** This method is idempotent: invoking it on a context that has already been
   ** closed has no effect. Invoking any other method on a closed context is not
   ** allowed, and results in undefined behaviour.
   **
   ** @param context             the {@link NamingEnumeration} to close.
   **
   ** @throws FeatureException in the specified context cannot be closed.
   */
  protected static void contextClose(final NamingEnumeration<?> context)
    throws FeatureException {

    try {
      if (context != null)
        context.close();
    }
    catch (NamingException e) {
      throw new FeatureException(FeatureError.UNHANDLED, e);
    }
  }
}