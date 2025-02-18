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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   RuntimeRegistry.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RuntimeRegistry.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-06-23  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;

import java.util.Hashtable;

import org.apache.tools.ant.BuildException;

import oracle.iam.platform.pluginframework.PluginException;

import oracle.iam.platformservice.api.PlatformService;

import oracle.iam.platformservice.exception.PlatformServiceAccessDeniedException;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.OIMMigration.vo.MigrationDO;

import oracle.iam.OIMMigration.api.OIMMigrationService;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.task.ServiceProvider;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureMessage;
import oracle.iam.identity.common.FeatureException;
import oracle.iam.identity.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class RuntimeRegistry
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Provides basic implementations to handle runtime artifacts like libraries
 ** or resource bundles.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RuntimeRegistry extends ServiceProvider {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String           LIBARY = "Jars";
  public static final String           PLUGIN = "Plugins";
  public static final String           BUNDLE = "ResourceBundles";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the registered files either plugins, libraries or resource bundles to
   ** upload.
   */
  private final Map<String, Set<Item>> itemSet = new LinkedHashMap<String, Set<Item>>();

  /**
   ** the plugin classes to unregiister.
   */
  private final Set<String>            classSet = new LinkedHashSet<String>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Item
  // ~~~~~ ~~~~
  public static class Item extends File {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:4244296498696739840")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String location = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Creates a new <code>Item</code> instance by converting the given
     ** pathname string into an abstract pathname. If the given string is the
     ** empty string, then the result is the empty abstract pathname.
     **
     ** @param  pathname         a pathname string
     ** @param  location         the destination location of the file to
     **                          upload.
     **
     ** @throws NullPointerException if the <code>pathname</code> argument is
     **                              <code>null</code>
     */
    protected Item(final String pathname, final String location) {
      // ensure inheritance
      super(pathname);

      // initailize instance attributes
      this.location = location;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Creates a new <code>Item</code> instance from a parent abstract pathname
     ** and a child pathname string.
     ** <p>
     ** If <code>parent</code> is <code>null</code> then the new
     ** <code>Item</code> instance is created as if by invoking the
     ** single-argument <code>Item</code> constructor on the given
     ** <code>child</code> pathname string.
     ** <p>
     ** Otherwise the <code>parent</code> abstract pathname is taken to denote a
     ** directory, and the <code>child</code> pathname string is taken to denote
     ** either a directory or a file.  If the <code>child</code> pathname string
     ** is absolute then it is converted into a relative pathname in a
     ** system-dependent way. If <code>parent</code> is the empty abstract
     ** pathname then the new <code>File</code> instance is created by
     ** converting <code>child</code> into an abstract pathname and resolving
     ** the result against a system-dependent default directory. Otherwise each
     ** pathname string is converted into an abstract pathname and the child
     ** abstract pathname is resolved against the parent.
     **
     ** @param  parent             the parent abstract pathname
     ** @param  child              the child pathname string
     ** @param  location           the destination location of the file to upload.
     **
     ** @throws NullPointerException if <code>child</code> is <code>null</code>
     */
    public Item(final File parent, final String child, final String location) {
      // ensure inheritance
      super(parent, child);

      // initailize instance attributes
      this.location = location;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>RuntimeRegistry</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public RuntimeRegistry(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (ServiceProvider)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case no item is associated with this
   **                            registry.
   */
  @Override
  public void validate()
    throws BuildException {

    if (this.itemSet.isEmpty())
      throw new BuildException(FeatureResourceBundle.string(FeatureError.UPLOAD_FILE_MANDATORY));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Called to add a class for unregistration.
   **
   ** @param  className          the name of the plugin to unregister.
   */
  public void add(final String className) {
    // we cannot allow to unregister more than once
    if (this.classSet.contains(className))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.PLUGIN_ONLY_ONCE, className));

    this.classSet.add(className);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Called to add files for upload.
   **
   ** @param  type               the type locator if the files to upload.
   ** @param  upload             the location qualifier of the files to upload.
   */
  public void add(final String type, final Set<Item> upload) {
    for (File file : upload) {
      // we cannot allow to import a complete directory
      if (file.isDirectory())
        throw new BuildException(FeatureResourceBundle.format(FeatureError.UPLOAD_FILE_ISDIRECTORY, file.getName()));

      // check if we are able to import the file
      if (!file.exists())
        throw new BuildException(FeatureResourceBundle.format(FeatureError.UPLOAD_FILE_NOTEXISTS, file.getName()));

      // we need at least read permissions on the file to add
      if (!file.canRead())
        throw new BuildException(FeatureResourceBundle.format(FeatureError.UPLOAD_FILE_NOPERMISSION, file.getName()));

    }
    Set<Item> existing = this.itemSet.get(type);
    if (existing == null) {
      existing = new LinkedHashSet<Item>();
      this.itemSet.put(type, existing);
    }
    existing.addAll(upload);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   upload
  /**
   ** Uploading all resources registered on this task to the database of Oracle
   ** Identity Manager.
   **
   ** @param  facade             the {@link OIMMigrationService} used to perform
   **                            the operation.
   **
   ** @throws ServiceException   if the requested files cannot be uploaded to
   **                            the database.
   */
  public void upload(final OIMMigrationService facade)
    throws ServiceException {

    final HashMap<String, List<MigrationDO>> transfer = new HashMap<String, List<MigrationDO>>();
    for (String type : this.itemSet.keySet()) {
      try {
        Set<Item>               set = this.itemSet.get(type);
        final List<MigrationDO> files = new ArrayList<MigrationDO>();
        for (Item file : set) {
          final byte[]      content = fetchFile(file);
          final MigrationDO transport = new MigrationDO(file.getName(), type, file.location, content);
          files.add(transport);
        }
        transfer.put(type, files);

        info(FeatureResourceBundle.format(FeatureMessage.UPLOAD_FILE_BEGIN, type));
        facade.importPackage(transfer);
        info(FeatureResourceBundle.format(FeatureMessage.UPLOAD_FILE_COMPLETE, type));
      }
      catch (Exception e) {
        final String[] arguments = { e.getLocalizedMessage(), type };
        error(FeatureResourceBundle.format(FeatureError.UPLOAD_FILE_ERROR, arguments));
        if (failonerror())
          throw new FeatureException(FeatureError.UPLOAD_FILE_ERROR, arguments);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   download
  /**
   ** Uploading all resources registered on this task to the database of Oracle
   ** Identity Manager.
   **
   ** @param  facade             the {@link OIMMigrationService} used to perform
   **                            the operation.
   **
   ** @throws ServiceException   if the requested files cannot be uploaded to
   **                            the database.
   */
  public void download(final OIMMigrationService facade)
    throws ServiceException {

    for (String type : this.itemSet.keySet()) {
      final Set<Item> set = this.itemSet.get(type);
      for (Item file : set) {
        final SearchCriteria filter = new SearchCriteria(
          new SearchCriteria("OJ_NAME", file.getName(), SearchCriteria.Operator.EQUAL)
        , new SearchCriteria("OJ_TYPE", type,           SearchCriteria.Operator.EQUAL)
        , SearchCriteria.Operator.AND
        );
        final List<MigrationDO> itemSet = facade.exportJars(filter, file.getParent());
        if (itemSet != null && itemSet.size() == 1) {
          storeFile(itemSet.get(0));
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   register
  /**
   ** Registering a plugin by uploading it to the database.
   **
   ** @param  facade             the {@link PlatformService} used to perform the
   **                            operation.
   **
   ** @throws ServiceException   if the plugi cannot ne uploaded to the server
   */
  public void register(final PlatformService facade)
    throws ServiceException {

    Set<Item> set = this.itemSet.get(PLUGIN);
    for (Item file : set) {
      byte[] buffer = fetchFile(file);
      try {
        // call the service to upload the plugin
        final Hashtable<String, String[]> status = facade.registerPluginAndReturnStatus(buffer);
        if (verbose())
          reportStatus(status);
        info(FeatureResourceBundle.format(FeatureMessage.PLUGIN_REGISTERED, file.getName()));
      }
      catch (IOException e) {
        final String[] arguments = { e.getLocalizedMessage(), file.getName() };
        error(FeatureResourceBundle.format(FeatureError.PLUGIN_REGISTER_ERROR, arguments));
        if (failonerror())
          throw new FeatureException(FeatureError.PLUGIN_REGISTER_ERROR, arguments);
      }
      catch (PluginException e) {
        final String[] arguments = { e.getLocalizedMessage(), file.getName() };
        error(FeatureResourceBundle.format(FeatureError.PLUGIN_REGISTER_ERROR, arguments));
        if (failonerror())
          throw new FeatureException(FeatureError.PLUGIN_REGISTER_ERROR, arguments);
      }
      catch (PlatformServiceAccessDeniedException e) {
        error(ServiceResourceBundle.string(ServiceError.CONTEXT_ACCESS_DENIED));
        if (failonerror())
          throw new ServiceException(ServiceError.CONTEXT_ACCESS_DENIED);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unregister
  /**
   ** Unregistering the plugin with thespecified class name by deleting the
   ** content from the database.
   **
   ** @param  facade             the {@link PlatformService} used to perform the
   **                            operation.
   **
   ** @throws ServiceException   if  the deletion of the plugin fails in
   **                            general.
   */
  public void unregister(final PlatformService facade)
    throws ServiceException {

    for (String className : this.classSet) {
      try {
        // call the service to unregister the plugin
        final Hashtable<String, String[]> status = facade.unRegisterPluginAndReturnStatus(className);
        if (verbose())
          // Ranshid has again produced a stupid logging message
          reportStatus(status);
        info(FeatureResourceBundle.format(FeatureMessage.PLUGIN_UNREGISTRED, className));
      }
      catch (PluginException e) {
        error(FeatureResourceBundle.format(FeatureError.PLUGIN_UNREGISTER_ERROR, e.getLocalizedMessage()));
        if (failonerror())
          throw new ServiceException(ServiceError.UNHANDLED, e);
      }
      catch (PlatformServiceAccessDeniedException e) {
        error(ServiceResourceBundle.format(ServiceError.CONTEXT_ACCESS_DENIED, e.getLocalizedMessage()));
        if (failonerror())
          throw new ServiceException(ServiceError.UNHANDLED, e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchFile
  /**
   ** Loads a file into memory.
   **
   ** @param  path               the abstract {@link File} path to the file to
   **                            load.
   **
   ** @throws ServiceException   if the file cannot be loaded from the local file
   **                            system.
   */
  private byte[] fetchFile(final File path)
    throws ServiceException {

    byte[]          buffer = null;
    FileInputStream stream = null;
    try {
      if (verbose())
        warning(FeatureResourceBundle.format(FeatureMessage.UPLOAD_FILE_FETCH, path.getAbsolutePath()));
      // a file stream is needed to read the file in the memory buffer
      stream = new FileInputStream(path);
      // determine the buffer size that we need to load the entire file in
      // memory
      int size = stream.available();
      // create a buffer that can receive the entire file
      buffer = new byte[size];
      // fetch the file in memory
      stream.read(buffer);
      if (verbose())
        warning(FeatureResourceBundle.format(FeatureMessage.UPLOAD_FILE_FETCHED, path.getAbsolutePath(), size));
    }
    catch (IOException e) {
      error(FeatureResourceBundle.format(FeatureError.UPLOAD_FILE_FETCH, e.getLocalizedMessage()));
      if (failonerror())
        throw new FeatureException(FeatureError.UPLOAD_FILE_FETCH, e.getLocalizedMessage());
    }
    finally {
      if (stream != null)
        try {
          stream.close();
        }
        catch (IOException e) {
          error(FeatureResourceBundle.format(FeatureError.UPLOAD_FILE_FETCH, e.getLocalizedMessage()));
        }
    }
    return buffer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   storeFile
  /**
   ** Stores a file form memory to disk.
   **
   ** @param  content            the abstract {@link MigrationDO} path to the
   **                            file to store in the filesystem.
   **
   ** @throws ServiceException   if the file cannot be stored in the local file
   **                            system.
   */
  private void storeFile(final MigrationDO content)
    throws ServiceException {

    byte[]           buffer = content.getFileContent();
    FileOutputStream stream = null;
    final File       file = new File(content.getFileName());
    try {
      if (verbose())
        warning(FeatureResourceBundle.format(FeatureMessage.DOWNLOAD_FILE_STORE, file.getAbsolutePath()));
      // a file stream is needed to read the file in the memory buffer
      stream = new FileOutputStream(file);
      // determine the buffer size that we need to load the entire file in
      // memory
      long size = content.getFileLength();
      // fetch the file in memory
      stream.write(buffer);
      if (verbose())
        warning(FeatureResourceBundle.format(FeatureMessage.DOWNLOAD_FILE_STORED, file.getAbsolutePath(), size));
    }
    catch (IOException e) {
      error(FeatureResourceBundle.format(FeatureError.DOWNLOAD_FILE_ERROR, e.getLocalizedMessage()));
      if (failonerror())
        throw new FeatureException(FeatureError.DOWNLOAD_FILE_ERROR, e.getLocalizedMessage());
    }
    finally {
      if (stream != null)
        try {
          stream.close();
        }
        catch (IOException e) {
          error(FeatureResourceBundle.format(FeatureError.DOWNLOAD_FILE_ERROR, e.getLocalizedMessage()));
        }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reportStatus
  /**
   ** Reports the status of a plugin operation to the console.
   **
   ** @param  status             the mapping of the status the plugin operation
   **                            returned.
   */
  private void reportStatus(final Hashtable<String, String[]> status) {
    for (String key : status.keySet()) {
      final String[] payload = status.get(key);
      warning("%1$s %2$s", payload);
    }
  }
}