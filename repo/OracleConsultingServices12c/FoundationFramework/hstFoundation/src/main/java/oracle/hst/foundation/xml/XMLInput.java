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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common Shared XML Stream Facilities

    File        :   XMLInput.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    XMLInput.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2012-03-11  DSteding    First release version
*/

package oracle.hst.foundation.xml;

import java.util.Stack;

import oracle.hst.foundation.logging.Loggable;

////////////////////////////////////////////////////////////////////////////////
// class XMLInput
// ~~~~~ ~~~~~~~~
/**
 ** Default base class for real-time handling of XML events.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public abstract class XMLInput extends XMLOperation {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Stack<Object> values = new Stack<Object>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>XMLInput</code>.
   ** <p>
   ** The logger is an object to which all logging and tracing messages for this
   ** XML document operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** If the <code>XMLOperation</code> object is created with a logger initally
   ** set to <code>null</code> logging is disabled.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   */
  public XMLInput(final Loggable loggable) {
    // ensure inheritance
    super(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   position
  /**
   ** Returns the location (line number and colum) where the current event
   ** ends.
   **
   ** @return                    the location (line number and colum) where the
   **                            current event ends.
   */
  public abstract int[] position();

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   push
  /**
   ** Pushes an item onto the top of the object stack.
   ** <p>
   ** This has exactly the same effect as:
   ** <blockquote><pre>
   ** addElement(item)</pre></blockquote>
   **
   ** @param  item               the item to be pushed onto this stack.
   **
   ** @return                    the <code>item</code> argument.
   **
   ** @see    Stack#push
   */
  protected Object push(final Object item) {
    return this.values.push(item);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   peek
  /**
   ** Looks at the object at the top of this stack without removing it from the
   ** value stack.
   **
   ** @return                    the object at the top of the value stack (the
   **                            last item of the {@link Stack} object).
   */
  protected Object peek() {
    return this.values.peek();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pop
  /**
   ** Removes the object at the top of this stack and returns that object as
   ** the value of this function.
   **
   ** @return                    the object at the top of the value stack (the
   **                            last item of the {@link Stack} object).
   */
  protected Object pop() {
    return this.values.pop();
  }
}