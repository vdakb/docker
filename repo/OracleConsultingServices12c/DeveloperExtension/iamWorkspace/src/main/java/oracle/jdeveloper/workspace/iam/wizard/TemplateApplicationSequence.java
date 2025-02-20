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

    File        :   TemplateApplicationSequence.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    TemplateProjectSequence.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.wizard;

import java.util.List;

import oracle.bali.ewt.wizard.dWizard.SequenceSeries2;
import oracle.bali.ewt.wizard.dWizard.WizardSequence2;

import oracle.ide.model.TechId;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.template.wizard.TemplateWizardArb;

////////////////////////////////////////////////////////////////////////////////
// class TemplateApplicationSequence
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>TemplateApplicationSequence</code> provides the abstraction that
 ** the <code>DWizard</code> class uses to navigate among wizard pages during
 ** the creation of Oracl JDebeloper Applications.
 ** <p>
 ** In general, clients won't use a single WizardSequence implementation, but
 ** compose several sequences together (for example, by using the SequenceSeries
 ** class) into a larger whole.
 ** <p>
 ** Clients that need to pick a sequence dynamically at runtime may find the
 ** DynamicSequence class useful.
 ** <p>
 ** WizardSequences cannot be shared among multiple running wizards, but they
 ** can be shared among multiple wizards as long as no two will be running
 ** (i.e., visible) at the same time. WizardSequences can even be used more than
 ** once in a single wizard.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class TemplateApplicationSequence extends TemplateObjectSequence {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final TechId[] technology;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TemplateApplicationSequence</code> that use the
   ** specified {@link List} of templates to create the sequence of wizard
   ** pages.
   **
   ** @param  context            the {@link TraversableContext} the page flow
   **                            will be based on.
   ** @param  technology         the array of String with technology keys the
   **                            page flow to create is based on.
   */
  public TemplateApplicationSequence(final TraversableContext context, final TechId[] technology) {
    // ensure inheritance
    super(context);

    this.technology = technology;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   chooseSequence (DynamicSequence2)
  protected WizardSequence2 chooseSequence() {
    final String prefix = TemplateWizardArb.getString(TemplateWizardArb.NEW_APPLICATION_ROOT_NAME);
    this.wizardContext.put(LABEL_PREFIX_KEY, prefix);

    WizardSequence2[] pages = new WizardSequence2[1];
    pages[0] = new TechnologySequence(this.wizardContext, this.technology);
    return new SequenceSeries2(pages);
  }
}