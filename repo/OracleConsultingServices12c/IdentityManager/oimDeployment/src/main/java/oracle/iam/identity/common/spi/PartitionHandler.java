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

    File        :   PartitionHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    PartitionHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.ArrayList;
import java.util.Collection;

import javax.management.ObjectName;

import org.apache.tools.ant.BuildException;

import oracle.fabric.management.folder.FolderManager;

import oracle.hst.deployment.ServiceError;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.hst.foundation.collection.Filter;
import oracle.hst.foundation.collection.FilteringIterator;

import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.task.ServiceProvider;

import oracle.hst.deployment.type.SOAServerContext;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureMessage;
import oracle.iam.identity.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class PartitionHandler
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** <code>PartitionHandler</code> creates, deletes, starts and stops partitions
 ** in Oracle SOA Suite.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class PartitionHandler extends ServiceProvider {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private ObjectName invoker = null;

  /** a single composite to deploy. */
  private PartitionInstance single = null;

  /** the collevtion of composite to deploy. */
  private final Collection<PartitionInstance> multiple = new ArrayList<PartitionInstance>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>PartitionHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public PartitionHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   partition
  /**
   ** Called to inject the <code>partition</code> of the partition related to
   ** Oracle SOA Suite.
   **
   ** @param  partition          the partition name belonging to SOA instance.
   */
  public void partition(final String partition) {
    if (this.single == null)
      this.single = new PartitionInstance();

    this.single.name(partition);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   partition
  /**
   ** Returns the <code>partition</code> of the partition related to Oracle SOA
   ** Suite.
   **
   ** @return                    the partition name belonging to SOA instance.
   */
  public final String partition() {
    return this.single == null ? null : this.single.name();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   command
  /**
   ** Called to inject the <code>command</code> of the partition related to
   ** Oracle SOA Suite.
   **
   ** @param  command            the command of the SOA composite partition.
   */
  public void command(final PartitionInstance.Command command) {
    if (this.single == null)
      this.single = new PartitionInstance();

    this.single.command(command);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   command
  /**
   ** Returns the <code>command</code> of the partition related to Oracle SOA
   ** Suite.
   **
   ** @return                    the command of the partition related to Oracle
   **                            SOA Suite.
   */
  public final PartitionInstance.Command command() {
    return this.single == null ? null : this.single.command();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (ServiceProvider)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case an error does occur.
   */
  @Override
  public void validate() {
    if (this.single == null && this.multiple.size() == 0)
      throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

    try {
      if (this.single != null)
        this.single.validate();

      for (PartitionInstance i : this.multiple)
        i.validate();
    }
    catch (Exception e) {
      throw new BuildException(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addInstance
  /**
   ** Called to add a sinlge file to the set of file to import.
   **
   ** @param  partition          the name of the partition to add
   **                            deployment has to do for.
   **
   ** @throws BuildException     if the specified {@link PartitionInstance} is
   **                            already part of this operation.
   */
  public void addInstance(final PartitionInstance partition) {
    // check if we have this file already
    if ((this.single != null && this.single.equals(partition)) || (this.multiple != null && this.multiple.contains(partition))) {
      final String message = FeatureResourceBundle.format(FeatureError.COMPOSITE_PARTITION_ONLYONCE, partition.name());
      error(message);
      if (failonerror())
        throw new BuildException(message);
    }
    else
      this.multiple.add(partition);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   manage
  /**
   ** Manage all partitions in Oracle SOA Suite through the given
   ** {@link SOAServerContext}.
   **
   ** @param  invoker            the {@link ObjectName} used to perform
   **                            the operation.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void invoke(final ObjectName invoker)
    throws ServiceException {

    this.invoker = invoker;
    if (this.single != null) {
      switch (this.single.command()) {
        case create   : create(this.single);
                        break;
        case destroy  : delete(this.single);
                        break;
        case activate : activate(this.single);
                        break;
        case retire   : retire(this.single);
                        break;
        case start    : start(this.single);
                        break;
        case stop     : stop(this.single);
                        break;
      }
    }
    if (!CollectionUtility.empty(this.multiple)) {
      manage(PartitionInstance.Command.destroy);
      manage(PartitionInstance.Command.create);
      manage(PartitionInstance.Command.start);
      manage(PartitionInstance.Command.stop);
      manage(PartitionInstance.Command.retire);
      manage(PartitionInstance.Command.activate);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   activate
  /**
   ** Manages all composites deployed in a partition from Oracle SOA Suite.
   **
   ** @param  command            the {@link PartitionInstance.Command} to
   **                            perform.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void manage(final PartitionInstance.Command command)
    throws ServiceException {

    final FilteringIterator<PartitionInstance> iterator = new FilteringIterator<PartitionInstance>(
      this.multiple.iterator()
    , new Filter<PartitionInstance>() {
      public boolean accept(final PartitionInstance instance) {
        return command == instance.command();
      }
      }
    );
    while (iterator.hasNext()) {
      switch (command) {
        case create   : create(iterator.next());
                        break;
        case destroy  : delete(iterator.next());
                        break;
        case activate : activate(iterator.next());
                        break;
        case retire   : retire(iterator.next());
                        break;
        case start    : start(iterator.next());
                        break;
        case stop     : stop(iterator.next());
                        break;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a partition to Oracle SOA Suite.
   **
   ** @param  partition          the {@link PartitionInstance} to create.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void create(final PartitionInstance partition)
    throws ServiceException {

    if (!exists(partition)) {
      info(FeatureResourceBundle.format(FeatureMessage.PARTITION_CREATE, partition.name()));
      try {
        // TODO:
        // since 12c creating a new folder requires a WorkManagerGroup an an optional description of such
        // WorkManagerGroup
        // for the time being we apply this hack below to get a clean compile without testing
        FolderManager.createFolder(this.invoker, partition.name(), "default", "");
        info(FeatureResourceBundle.format(FeatureMessage.PARTITION_CREATED, partition.name()));
      }
      catch (Exception e) {
        final String message = FeatureResourceBundle.format(FeatureError.COMPOSITE_PARTITION_CREATE, partition.name(), e.getLocalizedMessage());
        if (failonerror())
          throw new BuildException(message);
        else
          error(message);
      }
    }
    else {
      final String message = FeatureResourceBundle.format(FeatureError.COMPOSITE_PARTITION_EXISTS, partition.name());
      warning(message);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Deletes a partition from Oracle SOA Suite.
   **
   ** @param  partition          the {@link PartitionInstance} to delete.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void delete(final PartitionInstance partition)
    throws ServiceException {

    if (exists(partition)) {
      info(FeatureResourceBundle.format(FeatureMessage.PARTITION_DELETE, partition.name()));
      try {
        FolderManager.destroyFolder(this.invoker, partition.name());
        info(FeatureResourceBundle.format(FeatureMessage.PARTITION_DELETED, partition.name()));
      }
      catch (Exception e) {
        final String message = FeatureResourceBundle.format(FeatureError.COMPOSITE_PARTITION_DELETE, partition.name(), e.getLocalizedMessage());
        if (failonerror())
          throw new BuildException(message);
        else
          error(message);
      }
    }
    else {
      final String message = FeatureResourceBundle.format(FeatureError.COMPOSITE_PARTITION_NOTEXISTS, partition.name());
      if (failonerror())
        throw new BuildException(message);
      else
        error(message);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   start
  /**
   ** Starts a partition to Oracle SOA Suite.
   **
   ** @param  partition          the {@link PartitionInstance} to start.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void start(final PartitionInstance partition)
    throws ServiceException {

    if (exists(partition)) {
      info(FeatureResourceBundle.format(FeatureMessage.PARTITION_START, partition.name()));
      try {
        FolderManager.startCompositesInFolder(this.invoker, partition.name());
        info(FeatureResourceBundle.format(FeatureMessage.PARTITION_STARTED, partition.name()));
      }
      catch (Exception e) {
        final String message = FeatureResourceBundle.format(FeatureError.COMPOSITE_PARTITION_START, partition.name(), e.getLocalizedMessage());
        if (failonerror())
          throw new BuildException(message);
        else
          error(message);
      }
    }
    else {
      final String message = FeatureResourceBundle.format(FeatureError.COMPOSITE_PARTITION_NOTEXISTS, partition.name());
      if (failonerror())
        throw new BuildException(message);
      else
        error(message);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stop
  /**
   ** Starts a partition to Oracle SOA Suite.
   **
   ** @param  partition          the {@link PartitionInstance} to start.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void stop(final PartitionInstance partition)
    throws ServiceException {

    if (exists(partition)) {
      info(FeatureResourceBundle.format(FeatureMessage.PARTITION_STOP, partition.name()));
      try {
        FolderManager.stopCompositesInFolder(this.invoker, partition.name());
        info(FeatureResourceBundle.format(FeatureMessage.PARTITION_STOPPED, partition.name()));
      }
      catch (Exception e) {
        final String message = FeatureResourceBundle.format(FeatureError.COMPOSITE_PARTITION_STOP, partition.name(), e.getLocalizedMessage());
        if (failonerror())
          throw new BuildException(message);
        else
          error(message);
      }
    }
    else {
      final String message = FeatureResourceBundle.format(FeatureError.COMPOSITE_PARTITION_NOTEXISTS, partition.name());
      if (failonerror())
        throw new BuildException(message);
      else
        error(message);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   activate
  /**
   ** Activates all composites deployed in a partition from Oracle SOA Suite.
   **
   ** @param  partition          the {@link PartitionInstance} to delete.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void activate(final PartitionInstance partition)
    throws ServiceException {

    if (exists(partition)) {
      info(FeatureResourceBundle.format(FeatureMessage.PARTITION_ACTIVATE, partition.name()));
      try {
        FolderManager.destroyFolder(this.invoker, partition.name());
        info(FeatureResourceBundle.format(FeatureMessage.PARTITION_ACTIVATED, partition.name()));
      }
      catch (Exception e) {
        final String message = FeatureResourceBundle.format(FeatureError.COMPOSITE_PARTITION_ACTIVATE, partition.name(), e.getLocalizedMessage());
        if (failonerror())
          throw new BuildException(message);
        else
          error(message);
      }
    }
    else {
      final String message = FeatureResourceBundle.format(FeatureError.COMPOSITE_PARTITION_NOTEXISTS, partition.name());
      if (failonerror())
        throw new BuildException(message);
      else
        error(message);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   retire
  /**
   ** Retires all composites deployed in a partition from Oracle SOA Suite.
   **
   ** @param  partition          the {@link PartitionInstance} to delete.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  private void retire(final PartitionInstance partition)
    throws ServiceException {

    if (exists(partition)) {
      info(FeatureResourceBundle.format(FeatureMessage.PARTITION_RETIRE, partition.name()));
      try {
        FolderManager.destroyFolder(this.invoker, partition.name());
        info(FeatureResourceBundle.format(FeatureMessage.PARTITION_RETIRED, partition.name()));
      }
      catch (Exception e) {
        final String message = FeatureResourceBundle.format(FeatureError.COMPOSITE_PARTITION_RETIRE, partition.name(), e.getLocalizedMessage());
        if (failonerror())
          throw new BuildException(message);
        else
          error(message);
      }
    }
    else {
      final String message = FeatureResourceBundle.format(FeatureError.COMPOSITE_PARTITION_NOTEXISTS, partition.name());
      if (failonerror())
        throw new BuildException(message);
      else
        error(message);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Checks if the specified {@link PartitionInstance} exists in Oracle SOA
   ** Suite through the given {@link SOAServerContext}.
   **
   ** @param  partition          the {@link PartitionInstance} to check for
   **                            existance.
   */
  private boolean exists(final PartitionInstance partition) {
    String result = null;
    try {
      result = FolderManager.listFolders(this.invoker);
    }
    catch (Exception e) {
      final String message = FeatureResourceBundle.format(FeatureError.COMPOSITE_PARTITION_LOOKUP, partition.name(), e.getLocalizedMessage());
      if (failonerror())
        throw new BuildException(message);
      else
        error(message);
    }
    return result == null ? false : result.indexOf(partition.name()) != -1;
  }
}