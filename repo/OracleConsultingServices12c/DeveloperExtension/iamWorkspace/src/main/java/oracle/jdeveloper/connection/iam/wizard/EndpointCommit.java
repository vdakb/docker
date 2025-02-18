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

    System      :   JDeveloper Identity and Access Extensions
    Subsystem   :   Identity and Access Management Facilities

    File        :   EndpointCommit.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EndpointCommit.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.wizard;

import java.beans.VetoableChangeListener;

import javax.naming.Referenceable;
import javax.naming.NamingException;

import oracle.adf.share.jndi.AdfJndiContext;

import oracle.ide.util.Namespace;

import oracle.jdeveloper.connection.iam.adapter.EndpointContextFactory;

////////////////////////////////////////////////////////////////////////////////
// abstract class EndpointCommit
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Implementation of the commit to persists connections.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public abstract class EndpointCommit implements VetoableChangeListener {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final Namespace data;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EndpointCommit</code> that observes the specified
   ** {@link Namespace} at commit time.
   **
   ** @param  data               the {@link Namespace} to observe and commit.
   **                            <br>
   **                            Allowed object is {@link Namespace}.
   */
  protected EndpointCommit(final Namespace data) {
    // ensure inheritance
    super();

    // initialize instance data
    this.data = data;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   data
  /**
   ** Returns the namespace data of the Server Instance to handle.
   **
   ** @return                    the namespace data of the Server Instance to
   **                            handle.
   **                            <br>
   **                            Possible object is {@link Namespace}.
   */
  public final Namespace data() {
    return this.data;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods gouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   doCommit
  /**
   ** Persists the changes of the endpoint descriptor.
   **
   ** @param  name               the name to bind; may not be empty.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param isNew               determines if the object has to be bind or
   **                            rebind.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param descriptor          the object to bind; possibly <code>null</code>
   **                            <br>
   **                            Allowed object is {@link Referenceable}.
   **
   ** @throws NamingException    if a naming exception is encountered
   */
  protected final void doCommit(final String name, final boolean isNew, final Referenceable descriptor)
    throws NamingException {

    final AdfJndiContext context = EndpointContextFactory.connectionContext();
    if (isNew)
      context.bind(name, descriptor);
    else {
      context.rebind(name, descriptor);
    }
    context.save();
  }
}