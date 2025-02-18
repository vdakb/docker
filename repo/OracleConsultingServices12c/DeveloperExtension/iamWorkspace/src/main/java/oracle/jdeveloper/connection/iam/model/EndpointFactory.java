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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   Endpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Endpoint.


    Revisions        Date       Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.model;

import java.util.Hashtable;
import java.util.Enumeration;

import javax.naming.Name;
import javax.naming.Context;

import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.directory.Attributes;
import javax.naming.spi.DirObjectFactory;

import oracle.adf.share.jndi.SecureRefAddr;

import oracle.jdeveloper.connection.iam.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class EndpointFactory
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The model support for connection to store them into and fetch them from a
 ** JNDI context
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public abstract class EndpointFactory implements DirObjectFactory {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EndpointFactory</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected EndpointFactory() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getObjectInstance (ObjectFactory)
  /**
   ** Creates an object using the location or reference information specified.
   ** <p>
   ** Special requirements of this object are supplied using
   ** <code>environment</code>. An example of such an environment property is
   ** user identity information.
   ** <p>
   ** <code>NamingManager.getObjectInstance()</code> successively loads in
   ** object factories and invokes this method on them until one produces a
   ** non-<code>null</code> answer.When an exception is thrown by an object
   ** factory, the exception is passed on to the caller of
   ** <code>NamingManager.getObjectInstance()</code> (and no search is made for
   ** other factories that may produce a non-null answer).
   ** <br>
   ** An object factory should only throw an exception if it is sure that it is
   ** the only intended factory and that no other object factories should be
   ** tried.
   ** <br>
   ** If this factory cannot create an object using the arguments supplied, it
   ** should return <code>null</code>.
   ** <p>
   ** A <em>URL context factory</em> is a special ObjectFactory that creates
   ** contexts for resolving URLs or objects whose locations are specified by
   ** URLs. The <code>getObjectInstance()</code> method of a URL context factory
   ** will obey the following rules.
   ** <ol>
   **   <li>If <code>obj</code> is <code>null</code>, create a context for
   **       resolving URLs of the scheme associated with this factory. The
   **       resulting context is not tied to a specific URL: it is able to
   **       handle arbitrary URLs with this factory's scheme id. For example,
   **       invoking <code>getObjectInstance()</code> with <code>obj</code> set
   **       to <code>null</code> on an LDAP URL context factory would return a
   **       context that can resolve LDAP URLs such as
   **       "ldap://ldap.wiz.com/o=wiz,c=us" and
   **       "ldap://ldap.umich.edu/o=umich,c=us".
   **   <li>If <code>obj</code> is a URL string, create an object (typically a
   **       context) identified by the URL. For example, suppose this is an LDAP
   **       URL context factory. If <code>obj</code> is
   **       "ldap://ldap.wiz.com/o=wiz,c=us", getObjectInstance() would return
   **       the context named by the distinguished name "o=wiz, c=us" at the
   **       LDAP server ldap.wiz.com. This context can then be used to resolve
   **       LDAP names (such as "cn=George") relative to that context.
   **   <li>If <code>obj</code> is an array of URL strings, the assumption is
   **       that the URLs are equivalent in terms of the context to which they
   **       refer. Verification of whether the URLs are, or need to be,
   **       equivalent is up to the context factory. The order of the URLs in
   **       the array is not significant.
   **       <br>
   **       The object returned by getObjectInstance() is like that of the
   **       single URL case.  It is the object named by the URLs.
   **   <li>If <code>obj</code> is of any other type, the behavior of
   **       <code>getObjectInstance()</code> is determined by the context
   **       implementation.
   ** </ol>
   ** The <code>name</code> and <code>environment</code> parameters are owned by
   ** the caller. The implementation will not modify these objects or keep
   ** references to them, although it may keep references to clones or copies.
   ** <p>
   ** <b>Name and Context Parameters.</b> &nbsp;&nbsp;&nbsp; <a name=NAMECTX></a>
   ** The <code>name</code> and <code>context</code> parameters may optionally
   ** be used to specify the name of the object being created. <code>name</code>
   ** is the name of the object, relative to context <code>context</code>.
   ** <br>
   ** If there are several possible contexts from which the object could be
   ** named -- as will often be the case -- it is up to the caller to select
   ** one. A good rule of thumb is to select the "deepest" context available. If
   ** <code>context</code> is <code>null</code>, <code>name</code> is relative
   ** to the default initial context. If no name is being specified, the
   ** <code>name</code> parameter should be <code>null</code>.
   ** <br>
   ** If a factory uses <code>context</code> it should synchronize its use
   ** against concurrent access, since context implementations are not
   ** guaranteed to be thread-safe.
   **
   ** @param  obj                the possibly <code>null</code> object
   **                            containing location or reference information
   **                            that can be used in creating an object.
   ** @param  name               the name of this object relative to
   **                            <code>context</code>, or <code>null</code> if
   **                            no name is specified.
   ** @param  context            the context relative to which the
   **                            <code>name</code> parameter is specified, or
   **                            <code>null</code> if <code>name</code> is
   **                            relative to the default initial context.
   ** @param environment         the possibly <code>null</code> environment that
   **                            is used in creating the object.
   **
   ** @return                    the object created; <code>null</code> if an
   **                            object cannot be created.
   **
   ** @throws Exception          if this object factory encountered an exception
   **                            while attempting to create an object, and no
   **                            other object factories are to be tried.
   **
   */
  @Override
  public final Object getObjectInstance(final Object obj, final Name name, final Context context, final Hashtable<?, ?> environment)
    throws Exception {

    return getObjectInstance(obj, name, context, environment, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getObjectInstance (DirObjectFactory)
  /**
   ** Creates an object using the location or reference information, and
   ** attributes specified.
   ** <p>
   ** Special requirements of this object are supplied using
   ** <code>environment</code>.
   ** An example of such an environment property is user identity information.
   ** <p>
   ** <code>DirectoryManager.getObjectInstance()</code> successively loads in
   ** object factories. If it encounters a <code>DirObjectFactory</code>, it
   ** will invoke <code>DirObjectFactory.getObjectInstance()</code>; otherwise,
   ** it invokes <code>ObjectFactory.getObjectInstance()</code>. It does this
   ** until a factory produces a non-<code>null</code> answer.
   ** <p>
   ** When an exception is thrown by an object factory, the exception is passed
   ** on to the caller of <code>DirectoryManager.getObjectInstance()</code>. The
   ** search for other factories that may produce a non-<code>null</code> answer
   ** is halted. An object factory should only throw an exception if it is sure
   ** that it is the only intended factory and that no other object factories
   ** should be tried. If this factory cannot create an object using the
   ** arguments supplied, it should return <code>null</code>.
   ** <p>
   ** Since <code>DirObjectFactory</code> extends <code>ObjectFactory</code>, it
   ** effectively has two <code>getObjectInstance()</code> methods, where one
   ** differs from the other by the attributes argument. Given a factory that
   ** implements <code>DirObjectFactory</code>,
   ** <code>DirectoryManager.getObjectInstance()</code> will only use the method
   ** that accepts the attributes argument, while
   ** <code>NamingManager.getObjectInstance()</code> will only use the one that
   ** does not accept the attributes argument.
   ** <p>
   ** See <code>ObjectFactory</code> for a description URL context factories and
   ** other properties of object factories that apply equally to
   ** <code>DirObjectFactory</code>.
   ** <p>
   ** The <code>name</code>, <code>attrs</code>, and <code>environment</code>
   ** parameters are owned by the caller. The implementation will not modify
   ** these objects or keep references to them, although it may keep references
   ** to clones or copies.
   **
   ** @param  obj                the possibly <code>null</code> object
   **                            containing location or reference information
   **                            that can be used in creating an object.
   ** @param  name               the name of this object relative to
   **                            <code>context</code>, or <code>null</code> if
   **                            no name is specified.
   ** @param  context            the context relative to which the
   **                            <code>name</code> parameter is specified, or
   **                            <code>null</code> if <code>name</code> is
   **                            relative to the default initial context.
   ** @param environment         the possibly <code>null</code> environment that
   **                            is used in creating the object.
   ** @param attrs               the possibly <code>null</code> attributes
   **                            containing some of <code>obj</code>'s
   **                            attributes. <code>attrs</code> might not
   **                            necessarily have all of <code>obj</code>'s
   **                            attributes. If the object factory requires more
   **                            attributes, it needs to get it, either using
   **                            <code>obj</code>, or <code>name</code> and
   **                            <code>context</code>. The factory must not
   **                            modify attrs.
   **
   ** @return                    the object created; <code>null</code> if an
   **                            object cannot be created.
   **
   ** @throws Exception          if this object factory encountered an exception
   **                            while attempting to create an object, and no
   **                            other object factories are to be tried.
   */
  @Override
  public Object getObjectInstance(final Object obj, Name name, final Context context, final Hashtable environment, final Attributes attrs)
    throws Exception {

    // prevent bogus input
    if (!(obj instanceof Reference))
      throw new Exception(Bundle.string(Bundle.RESOURCE_FACTORY_SOURCE));

    final Storage provider = storage();
    for (Enumeration<RefAddr> cursor = ((Reference)obj).getAll(); cursor.hasMoreElements(); ) {
      final RefAddr next = cursor.nextElement();
      if ((next instanceof SecureRefAddr)) {
        final SecureRefAddr secure = (SecureRefAddr)next;
        provider.property(secure.getName(), new String(secure.getCharValue()));
      }
      else {
        provider.property(next.getType(), (String)next.getContent());
      }
    }
    return provider;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   storage
  /**
   ** Factory method to create an {@link Storage} provider implementation.
   **
   ** @return                    the concrete implementation of an
   **                            {@link Storage} provider.
   **                            <br>
   **                            Possible object is {@link Storage}.
   */
  protected abstract Storage storage();
}