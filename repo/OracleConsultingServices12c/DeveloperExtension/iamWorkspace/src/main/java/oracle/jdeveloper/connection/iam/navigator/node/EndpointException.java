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

    File        :   EndpointException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EndpointException.


    Revisions        Date       Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.navigator.node;

import java.util.List;
import java.util.ArrayList;

import javax.swing.Icon;

import oracle.ide.model.DefaultElement;

import oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle;
import oracle.jdeveloper.workspace.iam.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class EndpointException
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** <code>EndpointException</code> is a complete, default implementation of the
 ** <code>Element</code> interface representing an excption occured during the
 ** travers of <code>EndpointElement</code>s.
 ** <br>
 ** This is a convenient starting point for the implementation of data classes
 ** which can be integrated with the IDE framework.
 ** <p>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class EndpointException extends DefaultElement {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final Icon      icon;
  final String    label;
  final String    hint;
  final String    description;
  final Throwable cause;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>EndpointException</code> with a causing exception.
   **
   ** @param  causing            cause the cause (which is saved for later
   **                            retrieval.
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   */
  public EndpointException(final Throwable causing) {
    // ensure inheritance
    this(null, null, null, null, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>EndpointException</code> with a name and the causing
   ** exception.
   **
   ** @param  label              the short message.
   ** @param  causing            cause the cause (which is saved for later
   **                            retrieval.
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   */
  public EndpointException(final String label, final Throwable causing) {
    // ensure inheritance
    this(label, label, null, null, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>EndpointException</code> with a name and the causing
   ** exception.
   **
   ** @param  label              the short message.
   ** @param  description        the detail message.
   ** @param  hint               the tooltip message.
   ** @param  icon               the image to be displayed by the label.
   ** @param  causing            cause the cause (which is saved for later
   **                            retrieval.
   **                            (A <code>null</code> value is permitted, and
   **                            indicates that the cause is nonexistent or
   **                            unknown.)
   */
  public EndpointException(final String label, final String description, final String hint, final Icon icon, final Throwable causing) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.label        = label;
    this.hint         = hint;
    this.description  = description;
    this.icon         = icon;
    this.cause        = causing;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIcon
  /**
   ** Returns the {@link Icon} that represents the <code>EndpointFolder</code>
   ** in the UI.
   **
   ** @return                    the {@link Icon} of the
   **                            <code>EndpointFolder</code>.
   */
  @Override
  public Icon getIcon() {
    return (this.icon == null) ? Bundle.icon(Bundle.ACTION_EXCEPTION) : this.icon;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getShortLabel (overridden)
  /**
   ** Provides the label that represents the Wizard in the Object Gallery.
   ** <p>
   ** The label should be a noun or noun phrase naming the item that is created
   ** by this Wizard and should omit the word "new".
   ** <br>
   ** Examples: "Java Class", "Java Interface", "XML Document",
   ** "Database Connection".
   **
   ** @return                    the human readable label of the Wizard.
   */
  @Override
  public String getShortLabel() {
    return StringUtility.empty(this.label) ? unwrappedMessage(this.cause) : this.label;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLongLabel (overridden)
  /**
   ** Provides the long label that represents the catalog element.
   **
   ** @return                    the human readable long label of the catalog
   **                            element.
   */
  @Override
  public String getLongLabel() {
    return StringUtility.empty(this.description) ? getShortLabel() : this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getToolTipText (overridden)
  /**
   ** Returns the string to be used as the tooltip for <code>cause</code>.
   ** <p>
   ** By default this returns any string set using <code>setToolTipText</code>.
   ** If a component provides more extensive API to support differing tooltips
   ** at different locations, this method should be overridden.
   **
   ** @return                    the string to be used as the tooltip for
   **                            <code>cause</code>.
   */
  @Override
  public String getToolTipText() {
    if (!StringUtility.empty(this.hint))
      return this.hint;

    final StringBuilder builder = new StringBuilder("<html><ul>");
    final List<String>  errors  = collectErrors(this.cause.getCause(), new ArrayList<String>());
    for (String error : errors) {
      builder.append("<li>").append(error).append("</li>");
    }
    builder.append("</ul></html>");
    return builder.toString();
  }

  private String unwrappedMessage(final Throwable cause) {
    if (cause == null)
      return "EXCEPTION_NODE_NO_EXCEPTION_MESSAGE";

    if (!StringUtility.empty(cause.getLocalizedMessage())) {
      return cause.getLocalizedMessage();
    }
    return unwrappedMessage(cause.getCause() == cause ? null : cause.getCause());
  }

  private List<String> collectErrors(final Throwable cause, final ArrayList<String> list) {
    if (cause == null) {
      return list;
    }

    if (!StringUtility.empty(cause.getLocalizedMessage())) {
      list.add(cause.getLocalizedMessage());
    }
    if (cause == cause.getCause())
      return list;
    return collectErrors(cause.getCause(), list);
  }
}