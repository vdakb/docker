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

    File        :   TemplateObjectSequence.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    TemplateObjectSequence.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import java.awt.Component;

import javax.ide.util.MetaClass;

import oracle.bali.ewt.wizard.WizardPage;

import oracle.bali.ewt.wizard.dWizard.ArraySequence;
import oracle.bali.ewt.wizard.dWizard.WizardSequence2;
import oracle.bali.ewt.wizard.dWizard.DynamicSequence2;
import oracle.bali.ewt.wizard.dWizard.AbstractWizardSequence;

import oracle.ide.model.TechId;
import oracle.ide.model.TechnologyScope;
import oracle.ide.model.TechnologyRegistry;

import oracle.ide.panels.Traversable;
import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.model.JDevTechnologyInfo;
import oracle.jdeveloper.model.JDevTechnologyRegistry;

import oracle.jdeveloper.template.wizard.TemplateWizardArb;

import oracle.jdeveloper.workspace.iam.Manifest;

////////////////////////////////////////////////////////////////////////////////
// abstract class TemplateObjectSequence
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>TemplateObjectSequence</code> provides the abstraction that the
 ** <code>DWizard</code> class uses to navigate among wizard pages.
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
public abstract class TemplateObjectSequence extends DynamicSequence2 {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String                LABEL_PREFIX_KEY  = "wizard-step-label-prefix";
  static final String                TEMPLATE_NAME_KEY = "template-name-key";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected TechnologySequence       sequence;

  protected final TraversableContext wizardContext;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class EmptySequence
  // ~~~~~ ~~~~~~~~~~~~~
  /**
   ** Provides an implementation of an empty sequence, namely one that contains
   ** no pages. Obviously, <code>EmptySequence</code> aren't useful as a
   ** sequence for an entire wizard, but they are essential as subsequences.
   ** <p>
   ** <code>EmptySequence</code> are especially useful when used with
   ** DynamicSequences, where they can be used to indicate "skip over this
   ** sequence".
   */
  protected static class EmptySequence extends AbstractWizardSequence {

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: getPageCount (WizardSequence)
    /**
     ** Returns the number of pages in this sequence.
     **
     ** @return                  always return <code>0</code>.
     */
    @Override
    public int getPageCount() {
      return 0;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: goToFirstPage (WizardSequence)
    /**
     ** Returns the sequence to its first page.
     ** <p>
     ** A no-op for  <code>EmptySequence</code>.
     */
    @Override
    public void goToFirstPage() {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: goToLastPage (WizardSequence)
    /**
     ** Returns the sequence to its last page.
     ** <p>
     ** A no-op for  <code>EmptySequence</code>.
     */
    @Override
    public void goToLastPage() {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: goForward (WizardSequence)
    /**
     ** Advances the sequence to its next page.
     ** <p>
     ** Always throws an exception.
     **
     ** @throws NoSuchElementException if there are no more pages
     */
    @Override
    public void goForward() {
      throw new NoSuchElementException();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: goBackwards (WizardSequence)
    /**
     ** Moves the sequence back to its previous page.
     ** <p>
     ** Always throws an exception..
     **
     ** @throws NoSuchElementException if there are no more pages
     */
    @Override
    public void goBackwards() {
      throw new NoSuchElementException();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getCurrentPage (WizardSequence)
    /**
     ** Returns the current page of the wizard sequence.
     ** <p>
     ** <code>EmptySequence</code> always return <code>null</code>.
     **
     ** @return                  always return <code>null</code>.
     */
    @Override
    public WizardPage getCurrentPage() {
      return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getNextPage (WizardSequence)
    /**
     ** Returns the page immediately after the current page, or
     ** <code>null</code> if there are no more pages.
     ** <p>
     ** <code>EmptySequence</code> always return <code>null</code>.
     **
     ** @return                  always return <code>null</code>.
     */
    @Override
    public WizardPage getNextPage() {
      return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getPreviousPage (WizardSequence)
    /**
     ** Returns the page immediately before the current page, or
     ** <code>null</code> if there are no more pages.
     ** <p>
     ** <code>EmptySequence</code> always return <code>null</code>.
     **
     ** @return                  always return <code>null</code>.
     */
    @Override
    public WizardPage getPreviousPage() {
      return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getPageAt (WizardSequence2)
    /**
     ** Returns ...
     **
     ** @return                  ...
     */
    @Override
    public WizardPage getPageAt(final int index) {
      return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: selectPage (WizardSequence2)
    /**
     ** Returns ...
     **
     ** @return                  ...
     */
    @Override
    public boolean selectPage(final WizardPage page) {
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class TechnologySequence
  // ~~~~~ ~~~~~~~~~~~~~~~~~~
  protected static class TechnologySequence extends    DynamicSequence2
                                            implements PropertyChangeListener {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String[]                 sequence;
    private TechId[]                 technology;
    private Map<String, Component>   pageFlow;

    private final TraversableContext context;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>TechnologySequence</code> that use the
     ** specified {@link TechnologyScope} to create a sequence of wizard pages
     ** to configure the scope if a wizard page is attachd to a particular key.
     **
     ** @param  context          the {@link TraversableContext} the page flow
     **                          will be based on.
     **                          <br>
     **                          Allowed object is {@link TraversableContext}.
     ** @param  technology       the {@link TechId} to traverse by the wizard
     **                          sequence.
     **                          <br>
     **                          Allowed object is array of {@link TechId}.
     */
    public TechnologySequence(final TraversableContext context, final TechId[] technology) {
      this.context    = context;
      this.technology = technology != null ? technology : new TechId[0];
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: technology
    /**
     ** Set the technology keys to traverse by the wizard sequence.
     **
     ** @param  technology       the {@link TechId} to traverse by the wizard
     **                          sequence.
     */
    public final void technology(final TechId[] technology) {
      if (!Arrays.equals(this.technology, technology)) {
        this.technology = technology != null ? technology : new TechId[0];
        this.rechooseSequence();
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: propertyChange (PropertyChangeListener)
    /**
     ** This method gets called when a bound property is changed.
     **
     ** @param  event            a {@link PropertyChangeEvent} object describing
     **                          the event source and the property that has
     **                          changed.
     */
    @Override
    public void propertyChange(final PropertyChangeEvent event) {
      technology((TechId[])event.getNewValue());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abtract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: chooseSequence (DynamicSequence2)
    @Override
    protected WizardSequence2 chooseSequence() {
      List<TemplateObjectPage> list = new ArrayList<TemplateObjectPage>(this.technology.length);
      if (this.technology.length > 0) {
        final String[] sequence = order();
        for (int i = 0; i < sequence.length; ++i) {
          final String key = sequence[i];
          for (int j = 0; j < this.technology.length; ++j) {
            if (key.equals(this.technology[j].getKey())) {
              final String             prefix = (String)this.context.get(LABEL_PREFIX_KEY);
              final String             label  = Manifest.string(key);
              final TemplateObjectPage page   = new TemplateObjectPage(
                pageFlow().get(key)
              , TemplateWizardArb.format(TemplateWizardArb.WIZARD_FROM_TEMPLATE_PROJECT_CONFIGURATION_PAGE_STEP_LABEL_FORMAT, prefix, label == null ? key : label)
              , TemplateWizardArb.format(TemplateWizardArb.WIZARD_FROM_TEMPLATE_PROJECT_CONFIGURATION_PAGE_TITLE_FORMAT, label == null ? key : label)
              , this.context
              );
              list.add(page);
            }
          }
        }
      }

      if (list.size() > 0)
        return new ArraySequence(list.toArray(new TemplateObjectPage[0]));
      else
        return new EmptySequence();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: pageFlow
    private Map<String, Component> pageFlow() {
      if (this.pageFlow == null) {
        this.pageFlow = new HashMap<String, Component>();
        final JDevTechnologyRegistry registry = JDevTechnologyRegistry.getInstance();
        final String[] id = order();
        for (int i = 0; i < id.length; ++i) {
          final JDevTechnologyInfo info = registry.getJDevTechnologyInfo(id[i]);
          if (info != null) {
            final MetaClass<Traversable>[] panels = info.getWizardPanels();
            if (panels != null && panels.length > 0) {
              final MetaClass<Traversable>[] clazz = info.getWizardPanels();
              for (int j = 0; j < clazz.length; ++j) {
                final MetaClass<Traversable> metaClass = clazz[j];
                try {
                  final Traversable traversable = metaClass.newInstance();
                  this.pageFlow.put(id[i], traversable.getComponent());
                }
                catch (Exception e) {
                  e.printStackTrace();
                }
              }
            }
          }
        }
      }
      return this.pageFlow;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: order
    /**
     ** Evaluates the sequence of the page flow in order of the assigned
     ** tecnologies.
     **
     ** @return                  the sequence of the page flow in order of the
     **                          assigned technologies.
     */
    private String[] order() {
      if (this.sequence == null) {
        List<TechId>       list     = new ArrayList<TechId>(Arrays.asList(this.technology));
        TechnologyRegistry registry = TechnologyRegistry.getInstance();
        for (int t = 0; t < this.technology.length; ++t) {
          TechId   id       = this.technology[t];
          TechId[] depended = registry.getDependenciesFor(id);
          for (int panels = 0; panels < depended.length; ++panels) {
            TechId dependency = depended[panels];
            int index = list.indexOf(id);
            if (list.indexOf(dependency) > index) {
              list.remove(dependency);
              list.add(index, dependency);
            }
          }
        }

        JDevTechnologyRegistry jdeveloper = JDevTechnologyRegistry.getInstance();
        int i = 0;
        String[] sorted = new String[list.size()];
        Iterator<TechId> j = list.iterator();
        while (j.hasNext()) {
          final TechId id = j.next();
          if (id.isVisible()) {
            JDevTechnologyInfo info = jdeveloper.getJDevTechnologyInfo(id);
            if (info != null) {
              MetaClass[] clazz = info.getWizardPanels();
              if (clazz != null && clazz.length > 0)
                sorted[i++] = id.getKey();
            }
          }
        }

        this.sequence = new String[i];
        System.arraycopy(sorted, 0, this.sequence, 0, i);
      }
      return this.sequence;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TemplateObjectSequence</code> that use the
   ** specified {@link List} of templates to create the sequence of wirad pages.
   **
   ** @param  wizardContext      the {@link TraversableContext} the page flow
   **                            will be based on.
   */
  protected TemplateObjectSequence(final TraversableContext wizardContext) {
    // ensure inheritance
    super();

    // initialize instance
    this.wizardContext = wizardContext;
  }
}