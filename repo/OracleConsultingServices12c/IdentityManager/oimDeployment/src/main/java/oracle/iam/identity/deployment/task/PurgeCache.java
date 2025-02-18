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

    File        :   PurgeCache.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    PurgeCache.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.deployment.task;

import java.util.List;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import oracle.iam.platformservice.api.PlatformUtilsService;

import oracle.iam.platformservice.exception.InvalidCacheCategoryException;

import oracle.hst.deployment.ServiceException;

import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureMessage;
import oracle.iam.identity.common.FeaturePlatformTask;
import oracle.iam.identity.common.FeatureResourceBundle;

import oracle.iam.identity.common.type.cache.Category;

////////////////////////////////////////////////////////////////////////////////
// class PurgeCache
// ~~~~~ ~~~~~~~~~~
/**
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class PurgeCache extends FeaturePlatformTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private List<Category> category = new ArrayList<Category>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PurgeCache</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public PurgeCache() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCategory
  /**
   ** Call by the ANT deployment to inject the argument for setting the
   ** categories.
   **
   ** @param  value              the name of the category to be purged.
   **
   ** @throws BuildException     if the specified is already assigned to the
   **                            task.
   */
  public void setCategory(final String value)
    throws BuildException {

    final String[] tmp = value.split(",", 0);
    for (int i = 0; i < tmp.length; i++) {
      final String name = tmp[i].trim();
      if (name.equalsIgnoreCase("all")) {
        this.category.clear();
        for (int j = 0; j < Category.registry.length; j++)
          this.category.add(new Category(Category.registry[j]));
      }
      else {
        addConfiguredCategory(new Category(name));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredCategory
  /**
   ** Call by the ANT deployment to inject the argument for adding a category.
   **
   ** @param  category         the cache category to purge.
   **
   ** @throws BuildException   if the specified is already assigned to the
   **                          task.
   */
  public void addConfiguredCategory(final Category category)
    throws BuildException {

    if (this.category.contains(category))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.CACHE_CATEGORY_ONLYONCE, category.getValue()));

    this.category.add(category);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractTask)
  /**
   ** Called by the project to let the task do its work.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @throws ServiceException   if something goes wrong with the build
   */
  @Override
  public void onExecution()
    throws ServiceException {

    PlatformUtilsService service = service(PlatformUtilsService.class);
    if (this.category.size() == 0) {
      warning(FeatureResourceBundle.string(FeatureMessage.CACHE_CATEGORY_MISSING));
    }
    else {
      List<String> success = new ArrayList<String>();
      List<String> failure = new ArrayList<String>();
      for (Category category : this.category) {
        try {
          service.purgeCache(category.value());
          success.add(category.value());
        }
        catch (InvalidCacheCategoryException e) {
          failure.add(category.value());
        }
      }
      // record the result in the logging output only if the verbosity is
      // switched on
      if (verbose()) {
        if (success.size() > 0)
          info(FeatureResourceBundle.format(FeatureMessage.CACHE_CATEGORY_PROCEED, success.toString()));

        if (failure.size() > 0)
          error(FeatureResourceBundle.format(FeatureMessage.CACHE_CATEGORY_FAILED, failure.toString()));
      }
    }
  }
}