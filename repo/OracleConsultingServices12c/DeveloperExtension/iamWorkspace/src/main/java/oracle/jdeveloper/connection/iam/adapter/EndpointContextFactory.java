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

    File        :   EndpointContextFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EndpointContextFactory.


    Revisions        Date       Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.adapter;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import javax.naming.Reference;
import javax.naming.NamingException;

import javax.naming.spi.InitialContextFactory;

import oracle.adf.share.jndi.AdfJndiContext;

import oracle.jdeveloper.rescat2.model.registry.RescatContext;
import oracle.jdeveloper.rescat2.model.registry.RescatContextRegistry;

import oracle.jdeveloper.connection.iam.model.Endpoint;

////////////////////////////////////////////////////////////////////////////////
// abstract class EndpointContextFactory
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The model to support the Connection dialog for creating or modifiying the
 ** connection properties.
 ** <p>
 ** This data might also been used to display a specific node in the Resource
 ** Catalog.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public abstract class EndpointContextFactory implements InitialContextFactory {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EndpointContextFactory</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected EndpointContextFactory() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find
  /**
   ** Returns the {@link Endpoint} associated with the Resource Palette that
   ** matches the specified class name and identifier.
   ** <p>
   ** The class itslef cannot be used as a matching criteria because it can be
   ** deployed in a different bundle hence the OSGi class loading behavior
   ** prevents to access any method of such a class.
   **
   ** @param  className          the type of the {@link Endpoint} to find.
   ** @param  identifier         the name of {@link Endpoint} to match.
   **
   ** @return                    the {@link Endpoint} associated with the
   **                            Resource Palette that matches the specified
   **                            class and name.
   */
  public static Endpoint find(final String className, final String identifier) {
    return find(connectionContext(), className, identifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find
  /**
   ** Returns the {@link Endpoint} associated with the Resource Palette that
   ** matches the specified class name and identifier.
   ** <p>
   ** The class itslef cannot be used as a matching criteria because it can be
   ** deployed in a different bundle hence the OSGi class loading behavior
   ** prevents to access any method of such a class.
   **
   ** @param  context            the context to been searched.
   ** @param  className          the type of the {@link Endpoint} to find.
   ** @param  identifier         the identifier of {@link Endpoint} to match.
   **
   ** @return                    the {@link Endpoint} associated with the
   **                            Resource Palette that matches the specified
   **                            class and name.
   */
  public static Endpoint find(final AdfJndiContext context, final String className, final String identifier) {
    if ((context == null) || (identifier == null) || (identifier.isEmpty())) {
      return null;
    }
    final List<Endpoint> descriptor = descriptor(context, className);
    if ((descriptor == null) || (descriptor.isEmpty())) {
      return null;
    }
    for (Endpoint cursor : descriptor) {
      if (identifier.equals(cursor.name())) {
        return cursor;
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectionContext
  /**
   ** Returns the {@link AdfJndiContext} connections associated with the
   ** Resource Palette.
   **
   ** @return                    the {@link AdfJndiContext} connections
   **                            associated with the Resource Palette.
   */
  public static AdfJndiContext connectionContext() {
    return resourcePalette().getConnectionContext();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourcePalette
  /**
   ** Returns the {@link RescatContext} associated with the Resource Palette.
   **
   ** @return                    the {@link RescatContext} associated with the
   **                            Resource Palette.
   */
  public static RescatContext resourcePalette() {
    return RescatContextRegistry.getInstance().getResourcePaletteContext();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   descriptor
  /**
   ** Returns a {@link List} of {@link Endpoint}s from the specified
   ** {@link AdfJndiContext} that match the specified class name.
   ** <p>
   ** The class itslef cannot be used as a matching criteria because it can be
   ** deployed in a different bundle hence the OSGi class loading behavior
   ** prevents to access any method of such a class.
   ** 
   **
   ** @param  context            the {@link AdfJndiContext} providing the
   **                            configured.
   ** @param  className          the name of an {@link Endpoint} subclass to
   **                            match.
   **
   ** @return                    the {@link List} of {@link Endpoint}s
   **                            matching the requirements.
   */
  public static List<Endpoint> descriptor(final AdfJndiContext context, final String className) {
    final List<Endpoint> endpoint = new ArrayList<Endpoint>(0);
    // prevent bogus input
    if (context == null) {
      return endpoint;
    }

    try {
      final Map<String, Reference> reference = context.getReferenceMap();
      if (reference != null) {
        for (Map.Entry<String, Reference> cursor : reference.entrySet()) {
          if (cursor.getValue().getClassName().equals(className)) {
            final Object entry = context.lookup(cursor.getKey());
            endpoint.add((Endpoint)entry);
          }
        }
      }
    }
    catch (NamingException e) {
      // intentionally left blank
      ;
    }
    return endpoint;
  }
}