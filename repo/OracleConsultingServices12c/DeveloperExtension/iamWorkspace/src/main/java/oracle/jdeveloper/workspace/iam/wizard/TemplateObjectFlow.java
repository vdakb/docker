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
    Subsystem   :   Identity and Access Management Facilities

    File        :   TemplateObjectFlow.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    TemplateObjectFlow.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.wizard;

import oracle.bali.ewt.wizard.WizardPage;
import oracle.bali.ewt.wizard.dWizard.DWizard;
import oracle.bali.ewt.wizard.dWizard.WizardSequence2;

////////////////////////////////////////////////////////////////////////////////
// class TemplateObjectFlow
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Implementation of delegated navigation to a user-supplied object
 ** implementing the {@link WizardSequence2} interface.
 ** <p>
 ** While most simple wizards can easily be written using the normal wizard
 ** functionality, very complex wizards, especially wizards composed of several
 ** sub-wizards, may be more easily implemented with this model.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
final class TemplateObjectFlow extends DWizard {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2588501197278570179")
  private static final long serialVersionUID = -1363007146251277535L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean           forceValidation;

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   selectPage (overridden)
  /**
   ** Selects a given page.
   ** <p>
   ** The change is conditionally first validated by a
   ** <code>VALIDATE_PAGE</code> event, then confirmed with a
   ** <code>SELECTION_CHANGED</code> event.
   **
   ** @param  page             the page to select
   ** @param  validate         if <code>true</code>, validate the currently
   **                          selected page before changing selection.
   */
  @Override
  protected void selectPage(final WizardPage page, final boolean validate) {
    if (!validate && page != null) {
      boolean doValidation = this.forceValidation || this.shouldDoValidation(page);
      // ensure inheritance
      super.selectPage(page, doValidation);
    }
    else {
      // ensure inheritance
      super.selectPage(page, validate);
    }
    this.forceValidation = false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   doPrevious (overridden)
  /**
   ** Handles presses on the "Previous" button.
   */
  @Override
  protected void doPrevious() {
    this.forceValidation = false;
    // ensure inheritance
    super.doPrevious();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   shouldDoValidation
  /**
   ** Determines the necessety of validation.
   **
   ** @return                    if <code>true</code>, validate the specified
   **                            page before changing selection.
   */
  private boolean shouldDoValidation(final WizardPage page) {
    final int currentPageIndex = this.getCurrentPageIndex();
    for (int i = 0; i < currentPageIndex; ++i) {
      if (page == this.getPageAt(i))
        return true;
    }
    return false;
  }
}