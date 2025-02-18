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

    System      :   JDeveloper Identity and Access Extensions
    Subsystem   :   Identity and Access Management Facilities

    File        :   FixedLengthDocument.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    FixedLengthDocument.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.swing.widget;

import javax.swing.text.AttributeSet;
import javax.swing.text.PlainDocument;
import javax.swing.text.BadLocationException;

////////////////////////////////////////////////////////////////////////////////
// class FixedLengthDocument
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** A plain document that maintains no character attributes.
 ** <br>
 ** The default element structure for this document is a map of the lines in
 ** the text. The Element returned by getDefaultRootElement is a map of the
 ** lines, and each child element represents a line.
 ** <br>
 ** This model does not maintain any character level attributes, but each line
 ** can be tagged with an arbitrary set of attributes. Line to offset, and
 ** offset to line translations can be quickly performed using the default
 ** root element. The structure information of the DocumentEvent's fired by
 ** edits will indicate the line structure changes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class FixedLengthDocument extends PlainDocument {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5388863398455055530")
  private static final long serialVersionUID = -1110927896796351027L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final int limit;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>FixedLengthDocument</code> that limits the input
   ** upon the specified <code>limit</code>.
   **
   ** @param  limit              the limit of the input length.
   **                            <br>
   **                            Allowed object is <code>int</code>
   */
  private FixedLengthDocument(final int limit) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.limit = limit;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>FixedLengthDocument</code> that
   ** limits the input upon <code>255</code>.
   **
   ** @return                    the <code>FixedLengthDocument</code> created.
   **                            <br>
   **                            Possible object is
   **                            <code>FixedLengthDocument</code>.
   */
  public static FixedLengthDocument build() {
    return build(255);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>FixedLengthDocument</code> that
   ** limits the input upon the specified <code>limit</code>.
   **
   ** @param  limit              the limit of the input length.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>FixedLengthDocument</code> created.
   **                            <br>
   **                            Possible object is
   **                            <code>FixedLengthDocument</code>.
   */
  public static FixedLengthDocument build(final int limit) {
    return new FixedLengthDocument(limit);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   insertString (overridden)
  /**
   ** Inserts some content into the document.
   ** <br>
   ** Inserting content causes a write lock to be held while the actual
   ** changes are taking place, followed by notification to the observers on
   ** the thread that grabbed the write lock.
   ** <p>
   ** This method is thread safe, although most Swing methods are not. Please
   ** see <a href="https://docs.oracle.com/javase/tutorial/uiswing/concurrency/index.html">Concurrency in Swing</a>
   ** for more information.
   **
   ** @param  offset           the starting offset &gt;= 0.
   **                          <br>
   **                          Allowed object is <code>int</code>.
   ** @param  string           the string to insert; does nothing with
   **                          <code>null</code>/empty strings.
   **                          <br>
   **                          Allowed object is {@link String}.
   ** @param  attribute        the attributes for the inserted content.
   **                          <br>
   **                          Allowed object is {@link AttributeSet}.
   **
   ** @throws BadLocationException the given insert position is not a valid
   **                              position within the document.
   */
  @Override
  public void insertString(final int offset, final String string, final AttributeSet attribute)
    throws BadLocationException {

    // prevent bogus input
    if (string == null)
      return;
    // ensure inheritance
    super.insertString(offset, string, attribute);

    if (getLength() > this.limit)
      remove(this.limit, getLength() - this.limit);
  }
}