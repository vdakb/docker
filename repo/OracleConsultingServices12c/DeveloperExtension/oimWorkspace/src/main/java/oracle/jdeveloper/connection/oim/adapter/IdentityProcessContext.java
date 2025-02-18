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
    Subsystem   :   Identity Manager Facility

    File        :   IdentityContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    IdentityContext.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.oim.adapter;

import java.util.Hashtable;

import javax.naming.NamingException;

import oracle.adf.rc.core.CatalogReference;
import oracle.adf.rc.core.RepositoryReference;

import oracle.jdeveloper.connection.oim.model.IdentityServer;

////////////////////////////////////////////////////////////////////////////////
// class IdentityProcessContext
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>IdentityProcessContext</code> represents a structural elements
 ** capturing Provisioning Workflows in a Identity Server.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class IdentityProcessContext extends IdentityContext {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String LOCAL = "Process";

  IdentityProcessContext(final IdentityServer provider, final RepositoryReference repository, final CatalogReference catalog, final Hashtable env)
    throws NamingException {

    // ensure inheritance
    super(LOCAL, provider, repository, catalog, env);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by finctionality
  //////////////////////////////////////////////////////////////////////////////

 //////////////////////////////////////////////////////////////////////////////
  // Method:   loadCache (InMemoryContext)
  @Override
  protected void loadCache()
    throws NamingException {
  }
}