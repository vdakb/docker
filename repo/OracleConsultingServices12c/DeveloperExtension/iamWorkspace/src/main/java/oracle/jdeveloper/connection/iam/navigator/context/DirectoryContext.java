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

    File        :   DirectoryContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryContext.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.navigator.context;

import java.util.List;
import java.util.ArrayList;

import javax.naming.NamingException;

import javax.naming.directory.SearchResult;

import javax.naming.ldap.Rdn;

import oracle.ide.Ide;

import oracle.javatools.dialogs.ExceptionDialog;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.connection.iam.model.DirectoryName;
import oracle.jdeveloper.connection.iam.model.DirectoryEntry;
import oracle.jdeveloper.connection.iam.model.DirectoryServer;
import oracle.jdeveloper.connection.iam.model.DirectorySchema;
import oracle.jdeveloper.connection.iam.model.DirectoryAttribute;

import oracle.jdeveloper.connection.iam.service.DirectoryService;
import oracle.jdeveloper.connection.iam.service.DirectoryException;

//////////////////////////////////////////////////////////////////////////////
// class DirectoryContext
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** An entry context of the connected Directory Service.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class DirectoryContext implements Manageable.Removeable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5118890320709933226")
  private static final long        serialVersionUID = 6239314141734724206L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final DirectoryContext           parent;
  final transient DirectoryService service;

  DirectoryName                    name       = null;
  List<DirectoryContext>           subcontext = new ArrayList<DirectoryContext>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryContext</code> that belongs to the
   ** specified {@link DirectoryServer}.
   **
   ** @param  endpoint           the {@link DirectoryServer} this
   **                            <code>DirectoryContext</code> belongs to.
   **                            <br>
   **                            Allowed object is {@link DirectoryServer}.
   **
   ** @throws DirectoryException if a name syntax violation is detected.
   */
  private DirectoryContext(final DirectoryServer endpoint)
    throws DirectoryException {

    // ensure inheritance
    this(endpoint, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryContext</code> that belongs to the
   ** specified service endpoint and is owned by the parent context
   ** <code>parent</code>.
   **
   ** @param  endpoint           the @link DirectoryServer} endpoint.
   **                            <br>
   **                            Allowed object is {@link DirectoryServer}.
   ** @param  parent             the @link DirectoryContext} owning this
   **                            context.
   **                            <br>
   **                            Allowed object is
   **                            <code>DirectoryContext</code>.
   **
   ** @throws DirectoryException if a name syntax violation is detected.
   */
  private DirectoryContext(final DirectoryServer endpoint, final DirectoryContext parent)
    throws DirectoryException {

    // ensure inheritance
    this(DirectoryService.build(endpoint), parent);

    // initialize instance
    try {
      this.name = DirectoryName.build(StringUtility.EMPTY);
    }
    catch (NamingException e) {
      throw new DirectoryException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryContext</code> that belongs to the
   ** specified service endpoint and is owned by the parent context
   ** <code>parent</code>.
   **
   ** @param  service            the {@link DirectoryService} endpoint.
   **                            <br>
   **                            Allowed object is {@link DirectoryService}.
   ** @param  parent             the <code>DirectoryContext</code> owning this
   **                            context.
   **                            <br>
   **                            Allowed object is
   **                            <code>DirectoryContext</code>.
   */
  protected DirectoryContext(final DirectoryService service, final DirectoryContext parent) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.parent  = parent;
    this.service = service;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   service
  /**
   ** Returns the @{link DirectoryService} endpoint this context belongs to 
   **
   ** @return                    the @{link DirectoryService} endpoint.
   **                            <br>
   **                            Possible object is {@link DirectoryService}.
   */
  public final DirectoryService service() {
    return this.service;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rdn
  /**
   ** Provides the short label that represents the context element.
   **
   ** @return                    the human readable short label of the context
   **                            element.
   **                            <br>
   **                            Possible object is {@link Rdn}.
   */
  public Rdn rdn() {
    return this.name.getRdn(this.name.size() - 1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Provides the long label that represents the context element.
   **
   ** @return                    the human readable long label of the context
   **                            element.
   **                            <br>
   **                            Possible object is {@link DirectoryName}.
   */
  public DirectoryName name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  remove (Manageable.Removeable)
  /**
   ** Performs all action to delete an element.
   */
  @Override
  public void remove() {
    DirectoryException exception = null;
    try {
      this.service.connect(this.name.suffix().toString());
      this.service.entryDelete(this.name.prefix(), null);
    }
    catch (DirectoryException e) {
      exception = e;
    }
    finally {
      release();
    }
    confirmException(exception);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>DirectoryContext</code> that populates
   ** its data object from the specified {@link DirectoryServer}.
   **
   ** @param  endpoint           the {@link DirectoryServer} the
   **                            <code>DirectoryContext</code> belongs to.
   **                            <br>
   **                            Allowed object is {@link DirectoryServer}.
   **
   ** @return                    the validated <code>DirectoryContext</code>.
   **                            Possible object <code>DirectoryContext</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryContext</code>.
   **
   ** @throws DirectoryException if a name syntax violation is detected.
   */
  public static DirectoryContext build(final DirectoryServer endpoint)
    throws DirectoryException {

    return new DirectoryContext(endpoint);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>DirectoryContext</code> that populates
   ** its data object from the specified {@link DirectoryServer}.
   **
   **
   ** @param  endpoint           the @{link DirectoryServer} endpoint.
   **                            <br>
   **                            Allowed object is {@link DirectoryServer}.
   ** @param  parent             the @{link DirectoryContext} owning this
   **                            context.
   **                            <br>
   **                            Allowed object is {@link DirectoryContext}.
   **
   ** @return                    the validated <code>DirectoryContext</code>.
   **                            Possible object <code>DirectoryContext</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryContext</code>.
   **
   ** @throws DirectoryException if a name syntax violation is detected.
   */
  protected static DirectoryContext build(final DirectoryServer endpoint, final DirectoryContext parent)
    throws DirectoryException {

    return new DirectoryContext(endpoint, parent);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   release
  /**
   ** Closes the managed directory context.
   ** <br>
   ** This method releases the context's resources immediately, instead of
   ** waiting for them to be released automatically by the garbage collector.
   ** <p>
   ** This method is idempotent:  invoking it on a context that has already been
   ** closed has no effect. Invoking any other method on a closed context is not
   ** allowed, and results in undefined behaviour.
   */
  void release() {
    DirectoryException exception = null;
    try {
      this.service.disconnect();
    }
    catch (DirectoryException e) {
      exception = e;
    }
    confirmException(exception);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schema
  /**
   ** Return the schema upon request.
   **
   ** @return                    the schema.
   **                            <br>
   **                            Possible object is {@link DirectorySchema}.
   */ 
  public DirectorySchema schema() {
    DirectorySchema    result    = null;
    DirectoryException exception = null;
    try {
      result = this.service.schema();
    }
    catch (DirectoryException e) {
      exception = e;
    }
    confirmException(exception);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   ** Searches in the named context or object for entries in the sub-tree.
   **
   ** @return                    the {@link List} containing the names of the
   **                            LDAP objects find for the specified filter.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type
   **                            <code>DirectoryContext</code>.
   */
  public List<DirectoryContext> search() {
    Exception exception = null;
    this.subcontext.clear();
    try {
      this.service.connect();
      final List<SearchResult> result = this.service.search(this.name);
      for (SearchResult cursor : result) {
        final DirectoryContext entry = new DirectoryContext(this.service, this);
        entry.name = DirectoryName.build(cursor.getNameInNamespace()).symbol(DirectorySchema.classify(cursor));
        this.subcontext.add(entry);
      }
    }
    catch (Exception e) {
      exception = e;
    }
    finally {
      release();
    }
    confirmException(exception);
    return this.subcontext;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entry
  /**
   ** Returns the attributes for the current object scope.
   **
   ** @return                    the collection of {@link DirectoryAttribute}s
   **                            of an directory entry and their correspondening
   **                            values.
   **                            <br>
   **                            Possible object is {@link DirectoryEntry}.
   */
  public DirectoryEntry entry() {
    DirectoryEntry     result    = null;
    DirectoryException exception = null;
    try {
      this.service.connect(this.name.toString());
      result = this.service.attributes();
    }
    catch (DirectoryException e) {
      exception = e;
    }
    finally {
      release();
    }
    confirmException(exception);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   confirmException
  protected void confirmException(final Exception e) {
    if (e == null)
      return;

    ExceptionDialog.showExceptionDialog(Ide.getMainWindow(), e);  
  }
}