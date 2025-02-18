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
    Subsystem   :   Identity and Access Management Facility

    File        :   EntryEditor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EntryEditor.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.ods;

import oracle.ide.Context;

import oracle.ide.model.Node;
import oracle.ide.model.Element;

import oracle.jdeveloper.connection.iam.model.DirectorySchema;

import oracle.jdeveloper.connection.iam.editor.EndpointContent;
import oracle.jdeveloper.connection.iam.editor.EndpointEditor;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;

import oracle.jdeveloper.connection.iam.editor.ods.panel.AbstractPage;

import oracle.jdeveloper.connection.iam.navigator.ods.DirectoryServiceRoot;

import oracle.jdeveloper.connection.iam.editor.ods.panel.schema.SyntaxPage;
import oracle.jdeveloper.connection.iam.editor.ods.panel.schema.AttributePage;
import oracle.jdeveloper.connection.iam.editor.ods.panel.schema.ObjectClassPage;
import oracle.jdeveloper.connection.iam.editor.ods.panel.schema.MatchingRulePage;

////////////////////////////////////////////////////////////////////////////////
// class SchemaEditor
// ~~~~~ ~~~~~~~~~~~
/**
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class SchemaEditor extends EndpointEditor {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The DATA key should be a hard-coded String to guarantee that its value
   ** stays constant across releases.
   */
  public static final String          PATH          = "ods/schema";
  public static final String          OBJECT        = PATH + ".objectClass";
  public static final String          ATTRIBUTE     = PATH + ".attribute";
  public static final String          SYNTAX        = PATH + ".syntax";
  public static final String          MATCHING      = PATH + ".matching";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String                      label         = null;
  private AbstractPage                syntax        = null;
  private AbstractPage                objectClass   = null;
  private AbstractPage                attributeType = null;
  private AbstractPage                matchingRule  = null;
  private final EntryEditorController controller    = EntryEditorController.build();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>SchemaEditor</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public SchemaEditor() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setContext (overridden)
  /**
   ** Set the context behind this editor.
   */
  @Override
  public synchronized void setContext(final Context context) {
    // ensure inheritance to avoid NPE
    super.setContext(context);
    if (context != null) {
      final Node node = context.getNode();
      // Do some sanity checking in case something goes wrong in the context or
      // element - don't blow up bringing up the editor.
      // This will bring up a blank tab (no editor) instead of blowing up.
      if (node instanceof DirectoryServiceRoot) {
        if (this.content == null) {
          // first time initialization for our editor.
          // go ahead and create all our UI components based on this context.
          this.label = ((DirectoryServiceRoot)node).manageable().service().resource().name();
          initializeEditor();
        }
        else {
          updateEditor();
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close (overridden)
  /**
   ** Close this editor - this gives us a chance to clean up any resources.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** Although the editor is closed, the UndoableEdits continue to survive in
   ** the CommandProcessor queue!  This means that we cannot clean out anything
   ** that that functionality (undo) depends on.
   ** <br>
   ** Once closed an <code>EntryEditor</code> instance should not be used again
   ** as behaviour cannot be guaranteed and exceptions may occur while calling
   ** methods.
   */
  @Override
  public void close() {
    removeViewListener(this.controller);
    this.controller.unregisterAction();

    // ensure inheritance
    super.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initializeEditor
  /**
   ** This method is reponsible for initializing the view.
   ** <br>
   ** It should be called at the end of the view constructor.
   */
  private void initializeEditor() {
    // *** DON"T DO ANY INITIALIZATION WORK UNTIL AFTER COMMENT BELOW **
    //
    // Reason: initialization maybe aborted up to that point

    // Following detector can abort initialization, so don't do any set up
    // before this.
    populate();

    // *** INITIALIZATION FROM HERE ONWARDS ***
    this.content        = new EndpointContent();
    this.syntax         = SyntaxPage.build(this.data);
    this.objectClass    = ObjectClassPage.build(this.data);
    this.matchingRule   = MatchingRulePage.build(this.data);
    this.attributeType  = AttributePage.build(this.data);
    addContent(Bundle.string(Bundle.SCHEMA_OBJECT_PANEL_TITLE),        this.objectClass);
    addContent(Bundle.string(Bundle.SCHEMA_ATTRIBUTE_PANEL_TITLE),     this.attributeType);
    addContent(Bundle.string(Bundle.SCHEMA_SYNTAX_PANEL_TITLE),        this.syntax);
    addContent(Bundle.string(Bundle.SCHEMA_MATCHING_RULE_PANEL_TITLE), this.matchingRule);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateEditor
  /**
   ** Update the Context associated with this entry editor and editor component.
   **
   ** @param  context            the new context to use
   */
  private void updateEditor() {
    populate();
    this.syntax.updateView();
    this.objectClass.updateView();
    this.matchingRule.updateView();
    this.attributeType.updateView();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populate
  /**
   ** Puts the objects to the data container in the cache.
   */
  private void populate() {
    final Element element = getContext().getNode();
    if (element instanceof DirectoryServiceRoot) {
      final DirectorySchema schema = ((DirectoryServiceRoot)element).manageable().schema();
      this.data.put(PATH,      element.getShortLabel());
      this.data.put(SYNTAX,    schema.syntax());
      this.data.put(OBJECT,    schema.objectClass());
      this.data.put(MATCHING,  schema.matchingRule());
      this.data.put(ATTRIBUTE, schema.attributeType());
    }
  }
}