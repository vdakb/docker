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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Directory Service Utility Library
    Subsystem   :   Deployment Utilities

    File        :   DirectoryScope.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryScope.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.directory.common.spi.handler;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;

import java.io.File;

import javax.naming.NamingException;
import javax.naming.NameAlreadyBoundException;

import javax.naming.directory.Attributes;
import javax.naming.directory.SchemaViolationException;

import javax.naming.ldap.LdapName;
import javax.naming.ldap.LdapContext;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceException;

import oracle.hst.deployment.type.ODSServerContext;

import oracle.iam.directory.common.FeatureError;
import oracle.iam.directory.common.FeatureException;
import oracle.iam.directory.common.FeatureResourceBundle;

import oracle.iam.directory.common.FeatureMessage;
import oracle.iam.directory.common.spi.instance.FileInstance;
import oracle.iam.directory.common.spi.instance.ImportInstance;

import oracle.iam.directory.common.spi.support.LDAPRecord;
import oracle.iam.directory.common.spi.support.LDAPReader;
import oracle.iam.directory.common.spi.support.LDAPAddContent;
import oracle.iam.directory.common.spi.support.LDAPModDNContent;
import oracle.iam.directory.common.spi.support.LDAPModifyContent;
import oracle.iam.directory.common.spi.support.LDAPDeleteContent;

import oracle.iam.directory.common.task.FeatureDirectoryTask;

////////////////////////////////////////////////////////////////////////////////
// class ImportHandler
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Imports Directory Service objects from file.
 ** <p>
 ** The <code>ImportHandler</code> handles enables you to convert from LDIF all
 ** or part of the information residing in a Directory Service. Once you have
 ** converted the information, you can download it into a new file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ImportHandler extends ObjectHandler {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** a single file to import. */
  private ImportInstance                   single        = null;

  /** the already registered files to import. */
  private final Collection<ImportInstance> multiple     = new ArrayList<ImportInstance>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ImportHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public ImportHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Called to add a single set of files to the collection of files to import.
   **
   ** @param  instance           the {@link ImportInstance} where an import has
   **                            get from.
   **
   ** @throws BuildException     if the specified {@link File} is already part
   **                            of this import operation.
   */
  public void add(final ImportInstance instance)
    throws BuildException {

    // verify the instance as is
    instance.validate();

    this.multiple.add(instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add the specified {@link Collection} of {@link File}s to the managed
   ** {@link Collection} of file sets.
   **
   ** @param  instanceSet        the collection of {@link ImportInstance}s to be
   **                            added.
   **
   ** @throws BuildException     if one of the files provided by
   **                            <code>importSet</code> was added earlier by a
   **                            collection of files.
   */
  public void add(final Collection<ImportInstance> instanceSet) {
    Iterator<ImportInstance> i = instanceSet.iterator();
    while (i.hasNext())
      add(i.next());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Imports all object definition from a Directory Service through the
   ** discovered {@link LdapContext}.
   **
   ** @throws FeatureException   in case an error does occur.
   */
  public void execute()
    throws FeatureException {

    // initialize the business logic layer to operate on
    if (this.single != null)
      importSet(this.single);

    for (ImportInstance cursor : this.multiple)
      importSet(cursor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importSet
  /**
   ** Imports the object definition for the specfied {@link ImportInstance}.
   **
   ** @param  instance           the {@link ImportInstance} to import.
   **
   ** @throws FeatureException   in case an error does occur.
   */
  private void importSet(final ImportInstance instance)
    throws FeatureException {

    if (instance.importFile() != null)
      importFile(instance.importFile());

    for (FileInstance cursor : instance.importSet())
      importFile(cursor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importSet
  /**
   ** Imports the object definition for the specfied {@link FileInstance}.
   **
   ** @param  instance           the {@link FileInstance} to import.
   **
   ** @throws FeatureException   in case an error does occur.
   */
  private void importFile(final FileInstance instance)
    throws FeatureException {

    final LDAPReader reader = LDAPReader.create(instance.format(), instance.version(), instance.file());
    try {
      // this while loop is used to read the LDAP entries in one by one.
      // this should decrease memory usage and help with server load.
      do {
        final LDAPRecord binding = reader.nextRecord();
        if (binding == null)
          break;

        if (binding instanceof LDAPModifyContent) {
          importContent((LDAPModifyContent)binding);
        }
        else if (binding instanceof LDAPModDNContent) {
          importContent((LDAPModDNContent)binding);
        }
        else if (binding instanceof LDAPDeleteContent) {
          importContent((LDAPDeleteContent)binding);
        }
        // fall through treat everything as an create
        else {
          importContent((LDAPAddContent)binding);
        }
      } while(true);
    }
    finally {
      reader.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importContent
  /**
   ** Imports the object definition for the specfied {@link LDAPAddContent}.
   **
   ** @param  content            the {@link LDAPAddContent} to import.
   **
   ** @throws FeatureException   in case an error does occur.
   */
  private void importContent(final LDAPAddContent content)
    throws FeatureException {

    final String[]         parameter = new String[2];
    final ODSServerContext server    = ((FeatureDirectoryTask)frontend()).context();
    LdapContext            context   = null;
    try {
      final LdapName dn = new LdapName(content.namespace());
      parameter[0] = dn.getPrefix(dn.size() - 1).toString();
      parameter[1] = dn.getSuffix(dn.size() - 1).toString();

      // create object
      info(FeatureResourceBundle.string(FeatureMessage.OBJECT_CREATE_BEGIN), parameter);
      final Attributes attributes = content.attributes();
      if (attributes.size() > 0) {
        context = server.connect(server.environment(parameter[0]));
        context.createSubcontext(parameter[1], attributes);
        warning(FeatureResourceBundle.string(FeatureMessage.OBJECT_CREATE_SUCCESS), parameter);
      }
      // break out if the source record does not provide any attribute
      else {
        warning(FeatureResourceBundle.string(FeatureMessage.OBJECT_CREATE_SKIPPED), parameter);
      }
    }
    catch (SchemaViolationException e) {
      error(FeatureResourceBundle.string(FeatureError.OBJECT_CREATE), parameter);
      error(FeatureResourceBundle.string(FeatureError.ATTRIBUTE_SCHEMA), parameter);
      if (failonerror())
        throw new FeatureException(FeatureError.ABORT, e.getLocalizedMessage());
      else
        error(e.getLocalizedMessage());
    }
    catch (NameAlreadyBoundException e) {
      error(FeatureResourceBundle.string(FeatureError.OBJECT_CREATE), parameter);
      if (failonerror())
        throw new FeatureException(FeatureError.ABORT, e.getLocalizedMessage());
      else
        error(e.getLocalizedMessage());
    }
    catch (NamingException e) {
      error(FeatureResourceBundle.string(FeatureError.OBJECT_CREATE), parameter);
      if (failonerror())
        throw new FeatureException(FeatureError.ABORT, e.getLocalizedMessage());
      else
        error(e.getLocalizedMessage());
    }
    catch (ServiceException e) {
      throw new FeatureException(FeatureError.ABORT, e.getLocalizedMessage());
    }
    finally {
      if (context != null)
        try {
          context.close();
        }
        catch (NamingException e) {
          ;
        }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importContent
  /**
   ** Imports the object definition for the specfied {@link LDAPModifyContent}.
   **
   ** @param  content            the {@link LDAPModifyContent} to import.
   **
   ** @throws FeatureException   in case an error does occur.
   */
  private void importContent(final LDAPModifyContent content)
    throws FeatureException {

    final String[]         parameter = new String[2];
    final ODSServerContext server    = ((FeatureDirectoryTask)frontend()).context();
    LdapContext            context   = null;
    try {
      final LdapName dn = new LdapName(content.namespace());
      parameter[0] = dn.getPrefix(dn.size() - 1).toString();
      parameter[1] = dn.getSuffix(dn.size() - 1).toString();
      context = server.connect(server.environment(parameter[0]));

      // modify object
      info(FeatureResourceBundle.string(FeatureMessage.OBJECT_MODIFY_BEGIN), parameter);
      context.modifyAttributes(parameter[1], content.modifications());
      warning(FeatureResourceBundle.string(FeatureMessage.OBJECT_MODIFY_SUCCESS), parameter);
    }
    catch (SchemaViolationException e) {
      error(FeatureResourceBundle.string(FeatureError.OBJECT_MODIFY), parameter);
      error(FeatureResourceBundle.string(FeatureError.ATTRIBUTE_SCHEMA), parameter);
      if (failonerror())
        throw new FeatureException(FeatureError.ABORT, e.getLocalizedMessage());
      else
        error(e.getLocalizedMessage());
    }
    catch (NamingException e) {
      error(FeatureResourceBundle.string(FeatureError.OBJECT_MODIFY), parameter);
      if (failonerror())
        throw new FeatureException(FeatureError.ABORT, e.getLocalizedMessage());
      else
        error(e.getLocalizedMessage());
    }
    catch (ServiceException e) {
      throw new FeatureException(FeatureError.ABORT, e.getLocalizedMessage());
    }
    finally {
      if (context != null)
        try {
          context.close();
        }
        catch (NamingException e) {
          ;
        }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importContent
  /**
   ** Imports the object definition for the specfied {@link LDAPModDNContent}.
   **
   ** @param  content            the {@link LDAPModDNContent} to import.
   **
   ** @throws FeatureException   in case an error does occur.
   */
  private void importContent(final LDAPModDNContent content)
    throws FeatureException {

    final String[]         parameter = new String[2];
    final ODSServerContext server    = ((FeatureDirectoryTask)frontend()).context();
    LdapContext            context   = null;
    try {
      final LdapName dn = new LdapName(content.namespace());
      parameter[0] = dn.getPrefix(dn.size() - 1).toString();
      parameter[1] = dn.getSuffix(dn.size() - 1).toString();
      context = server.connect(server.environment(parameter[0]));

      // rename or move object
      info(FeatureResourceBundle.string(FeatureMessage.OBJECT_RENAME_BEGIN), parameter);
      warning(FeatureResourceBundle.string(FeatureMessage.OBJECT_RENAME_SUCCESS), parameter);
    }
    catch (NamingException e) {
      error(FeatureResourceBundle.string(FeatureError.OBJECT_RENAME), parameter);
      if (failonerror())
        throw new FeatureException(FeatureError.ABORT, e.getLocalizedMessage());
      else
        error(e.getLocalizedMessage());
    }
    catch (ServiceException e) {
      throw new FeatureException(FeatureError.ABORT, e.getLocalizedMessage());
    }
    finally {
      if (context != null)
        try {
          context.close();
        }
        catch (NamingException e) {
          ;
        }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importContent
  /**
   ** Imports the object definition for the specfied {@link LDAPDeleteContent}.
   **
   ** @param  content            the {@link LDAPDeleteContent} to import.
   **
   ** @throws FeatureException   in case an error does occur.
   */
  private void importContent(final LDAPDeleteContent content)
    throws FeatureException {

    final String[]         parameter = new String[2];
    final ODSServerContext server    = ((FeatureDirectoryTask)frontend()).context();
    LdapContext            context   = null;
    try {
      final LdapName dn = new LdapName(content.namespace());
      parameter[0] = dn.getPrefix(dn.size() - 1).toString();
      parameter[1] = dn.getSuffix(dn.size() - 1).toString();
      context = server.connect(server.environment(parameter[0]));

      // delete object
      info(FeatureResourceBundle.string(FeatureMessage.OBJECT_DELETE_BEGIN), parameter);
      context = server.connect(server.environment(parameter[0]));
      context.destroySubcontext(parameter[1]);
      warning(FeatureResourceBundle.string(FeatureMessage.OBJECT_DELETE_SUCCESS), parameter);
    }
    catch (NamingException e) {
      error(FeatureResourceBundle.string(FeatureError.OBJECT_DELETE), parameter);
      if (failonerror())
        throw new FeatureException(FeatureError.ABORT, e.getLocalizedMessage());
      else
        error(e.getLocalizedMessage());
    }
    catch (ServiceException e) {
      throw new FeatureException(FeatureError.ABORT, e.getLocalizedMessage());
    }
    finally {
      if (context != null)
        try {
          context.close();
        }
        catch (NamingException e) {
          ;
        }
    }
  }
}