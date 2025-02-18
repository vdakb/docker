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
    Subsystem   :   Identity and Access Management Facilities

    File        :   Observer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    Observer.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.83  2019-02-14  DSteding    First release version
    12.2.1.3.42.60.94  2021-03-09  DSteding    Content Set's shipped to standard
                                               PathConfiguration container
*/

package oracle.jdeveloper.workspace.iam.project.content.site;

import oracle.javatools.dialogs.ExceptionDialog;

import oracle.ide.Ide;

import oracle.ide.exception.SingletonClassException;

import oracle.ide.model.Project;
import oracle.ide.model.ProjectChangeListener;

////////////////////////////////////////////////////////////////////////////////
// final class Observer
// ~~~~~ ~~~~~ ~~~~~~~~
/**
 ** Observes projects as they are cached or uncached from the node factory.
 ** <br>
 ** The {@link Provider} is responsible for registering this observer at
 ** registration time.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   12.2.1.3.42.60.83
 */
final class Observer extends ProjectChangeListener {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Object LOCK     = new Object();

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The {@link Observer} implements the singleton pattern.
   ** <br>
   ** The static attribute {@link #instance} holds this single instance.
   */
  private static Observer     instance = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Observer</code>.
   ** <br>
   ** This constructor is private to prevent other classes outside of this
   ** package to use "new FeatureProjectObserver()".
   **
   ** @throws SingletonClassException when this class is instantiated more than
   **                                 once.
   */
  private Observer ()
    throws SingletonClassException {

    if (instance != null)
      throw new SingletonClassException(Observer.class.getName());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** The <code>Observer</code> is a singleton class.
   ** This method gets this folder's single instance.
   **
   ** @return                    the <code>Observer</code>
   **                            single instance.
   */
  public static Observer instance() {
    if (null == instance) {
      Observer observer = null;
      synchronized(LOCK) {
        try {
          observer = new Observer();
        }
        catch (SingletonClassException e) {
          // should never happens
          ExceptionDialog.showExceptionDialog(Ide.getMainWindow(), e);
        }
      }
      instance = observer;
    }
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   projectOpened (overridden)
  /**
   ** Called after a {@link Project} has been opened.
   **
   ** @param  project            the {@link Project} has been opened by the
   **                            JDeveloper IDE.
   */
  @Override
  public void projectOpened(final Project project) {
    // ensure inheritance
    super.projectOpened(project);

    // if the default project do nothing
    if (project.isDefault())
      return;

    final Settings settings = Provider.settings(project);
    if (settings != null) {
      // TODO: what we need
      ;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   projectClosed (overridden)
  /**
   ** Called after a {@link Project} has been closed.
   **
   ** @param  project            the {@link Project} has been closed by the
   **                            JDeveloper IDE.
   */
  @Override
  public void projectClosed(final Project project) {
    // ensure inheritance
    super.projectClosed(project);

    // if the default project do nothing
    if (project.isDefault())
      return;
  }
}