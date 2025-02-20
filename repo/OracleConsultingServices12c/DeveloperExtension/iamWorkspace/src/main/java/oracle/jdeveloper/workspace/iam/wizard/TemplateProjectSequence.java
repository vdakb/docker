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

    File        :   TemplateProjectSequence.java

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
import java.util.ArrayList;

import oracle.bali.ewt.wizard.WizardPage;

import oracle.bali.ewt.wizard.dWizard.ArraySequence;
import oracle.bali.ewt.wizard.dWizard.WizardSequence2;
import oracle.bali.ewt.wizard.dWizard.SequenceSeries2;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.template.ProjectTemplate;

import oracle.jdeveloper.template.wizard.TemplateWizardArb;
import oracle.jdeveloper.template.wizard.TemplateWizardUtil;

////////////////////////////////////////////////////////////////////////////////
// class TemplateProjectSequence
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>TemplateProjectSequence</code> generates the pages flow that the
 ** <code>DWizard</code> class uses to navigate among wizard pages during the
 ** creation of Oracl JDebeloper Projects.
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
public class TemplateProjectSequence extends TemplateObjectSequence {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final List<ProjectTemplate> templates;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TemplateProjectSequence</code> that use the
   ** specified {@link List} of templates to create the sequence of wizard
   ** pages.
   **
   ** @param  templates          the {@link List} of {@link ProjectTemplate}s
   **                            that assinged to the
   **                            <code>ApplicationTemplate</code>.
   ** @param  wizardContext      the {@link TraversableContext} the page flow
   **                            will be based on.
   */
  public TemplateProjectSequence(final List<ProjectTemplate> templates, final TraversableContext wizardContext) {
    // ensure inheritance
    super(wizardContext);

    // initialize instance
    this.templates = templates;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templates
  /**
   ** Return the {@link List} of {@link ProjectTemplate}s the page flow has to
   ** proceed.
   ** <p>
   ** Returns never <code>null</code>.
   **
   ** @return                    the {@link List} of {@link ProjectTemplate}s
   **                            the page flow has to proceed.
   **                            Returns never <code>null</code>.
   */
  final List<ProjectTemplate> templates() {
    return this.templates != null ? this.templates : new ArrayList<ProjectTemplate>(0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   chooseSequence (DynamicSequence2)
  protected WizardSequence2 chooseSequence() {
    List<ProjectTemplate> template  = this.templates();
    TraversableContext[]  panelData = new TraversableContext[template.size()];
    TemplateWizardUtil.setProjectTraversableContexts(panelData, this.wizardContext);

    List<WizardSequence2> pages = new ArrayList<WizardSequence2>();
    int                   count = template.size();
    for(int i = 0; i < count; ++i) {
      final TraversableContext cursor = TemplateWizardUtil.newContextForProjectPages(this.wizardContext);
      panelData[i] = cursor;
      TemplateWizardUtil.setProjectTemplate(template.get(i), cursor);
      TemplateProjectPanel projectPanel = new TemplateProjectPanel();
      String label   = TemplateWizardArb.getString(TemplateWizardArb.WIZARD_FROM_TEMPLATE_PROJECT_NAME_PAGE_STEP_LABEL_PREFIX);
      String step    = count > 1 ? String.format("%s %s", new Object[]{label, Integer.valueOf(i + 1)}) : label;
      String project = nameFromTemplate(template.get(i));

      cursor.put(LABEL_PREFIX_KEY,  step);
      cursor.put(TEMPLATE_NAME_KEY, project);

      final TemplateObjectPage projectPage = new TemplateObjectPage(
        projectPanel
      , TemplateWizardArb.format(TemplateWizardArb.WIZARD_FROM_TEMPLATE_PROJECT_NAME_PAGE_STEP_LABEL_FORMAT, step)
      , TemplateWizardArb.getString(TemplateWizardArb.WIZARD_FROM_TEMPLATE_PROJECT_NAME_PAGE_TITLE)
      , cursor);
      projectPage.setBranching(true);
      projectPage.setInitialFocus(projectPanel.defaultFocusComponent());
      pages.add(new ArraySequence(new WizardPage[]{projectPage}));

      this.sequence = new TechnologySequence(cursor, TemplateWizardUtil.getProjectTemplate(cursor).getTechnologyScope().toArray());
      projectPanel.addTechnologyPropertyChangeListener(this.sequence);
      pages.add(this.sequence);
    }
    return new SequenceSeries2(pages.toArray(new WizardSequence2[0]));
  }


  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nameFromTemplate
  protected String nameFromTemplate(final ProjectTemplate template) {
    String prefix = TemplateWizardArb.getString(TemplateWizardArb.WIZARD_FROM_TEMPLATE_PROJECT_NAME_PAGE_STEP_LABEL_PREFIX);
    String name   = template.getName().trim();
    if (name.toLowerCase().endsWith(prefix.toLowerCase()) && name.length() > prefix.length())
      name = name.substring(0, name.length() - prefix.length());

    return name.trim();
  }
}