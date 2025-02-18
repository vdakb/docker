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

    Copyright © 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   AbstractWizard.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    AbstractWizard.


    Revisions        Date       Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam;

import oracle.ide.panels.ApplyEvent;
import oracle.ide.panels.CommitListener;
import oracle.ide.panels.TraversalException;
import oracle.ide.panels.TraversableContext;

import oracle.ide.wizard.Wizard;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractWizard
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Implementation of the "JDeveloper Gallery" item.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class AbstractWizard extends Wizard {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Commit
  // ~~~~~ ~~~~~~
  /**
   ** Consumes the messages that are sent when commiting (finishing) or
   ** canceling from a {@link Wizard}.
   ** <p>
   ** A commit operation is done as a two-phase commit. Each listener is asked
   ** if it can commit. Should any listener say that it is not ready to commit,
   ** no further listeners are asked and the commit is aborted. Once every
   ** listener has approved the commit, the listeners are called in order to
   ** perform the commit. Should any commit operation fail, the commit is
   ** aborted, and any commits already performed are rolled back. In this way,
   ** all the listeners commit or fail together.
   */
  protected class Commit implements CommitListener {

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: cancel (CommitListener)
    /**
     ** Called to cancel the {@link Wizard}.
     **
     ** @param  event            the {@link ApplyEvent} that provides contextual
     **                          information about when the {@link ApplyEvent}
     **                          was fired.
     */
    @Override
    public void cancel(final ApplyEvent event) {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: checkCommit (CommitListener)
    /**
     ** Called to see if this listener is ready to commit.
     ** <p>
     ** If the current state does not permit a commit, or if this listener can
     ** determine that the commit will fail if attempted, the listener aborts
     ** the commit.
     **
     ** @param  event            the {@link ApplyEvent} that provides contextual
     **                          information about when the {@link ApplyEvent}
     **                          was fired.
     */
    @Override
    public void checkCommit(final ApplyEvent event) {
      final TraversableContext context = event.getTraversableContext();
      try {
        AbstractWizard.this.validate(context);
      }
      catch (TraversalException e) {
        AbstractWizard.this.rollback(context);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: commit (CommitListener)
    /**
     ** Called to have this listener perform the commit action.
     ** <p>
     ** Prior to calling commit on any listeners, FSMWizard will first call
     ** checkCommit on every listener.
     **
     ** @param  event            the {@link ApplyEvent} that provides contextual
     **                          information about when the {@link ApplyEvent}
     **                          was fired.
     **
     ** @throws TraversalException if the commit should be aborted.
     */
    @Override
    public void commit(final ApplyEvent event)
      throws TraversalException {

      AbstractWizard.this.commit(event.getTraversableContext());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: rollback (CommitListener)
    /**
     ** Called to have this listener rollback the commit.
     ** <p>
     ** If a listener aborts a commit, each previous listener is asked in
     ** reverse order to rollback its commit.
     **
     ** @param  event            the {@link ApplyEvent} that provides contextual
     **                          information about when the {@link ApplyEvent}
     **                          was fired.
     */
    @Override
    public void rollback(final ApplyEvent event) {
      // intentionally left blank
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AbstractWizard</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractWizard() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Called to see if this {@link Wizard} is ready to commit.
   ** <p>
   ** If the current state does not permit a commit, or if this {@link Wizard}
   ** can determine that the commit will fail if attempted, the
   ** {@link Wizard} aborts the commit.
   **
   ** @param  context            the {@link TraversableContext} that provides
   **                            contextual information.
   **
   ** @throws TraversalException if validation fails.
   */
  protected void validate(final TraversableContext context)
    throws TraversalException {

    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commit
  /**
   ** Called to have this {@link Wizard} perform the commit action.
   ** <p>
   ** Prior to calling commit on any {@link Wizard}, FSMWizard will first call
   ** {@link CommitListener#checkCommit(ApplyEvent)} on every listener.
   **
   **
   ** @param  context            the {@link TraversableContext} that provides
   **                            contextual information.
   **
   ** @throws TraversalException if the commit should be aborted.
   */
  protected void commit(final TraversableContext context)
    throws TraversalException {

    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rollback
  /**
   ** Called to have this {@link Wizard} rollback the commit.
   ** <p>
   ** If a listener aborts a commit, each previous {@link CommitListener} is
   ** asked in reverse order to rollback its commit.
   **
   ** @param  context            the {@link TraversableContext} that provides
   **                            contextual information.
   */
  protected void rollback(final TraversableContext context) {
    // intentionally left blank
  }
}