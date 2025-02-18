/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license  agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2008. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common Shared Collection Utility

    File        :   MarkableStack.java

    Compiler    :   Oracle JDeveloper 10g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    MarkableStack.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2008-21-03  DSteding    First release version
*/

package oracle.hst.foundation.collection;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

////////////////////////////////////////////////////////////////////////////////
// class MarkableStack
// ~~~~~ ~~~~~~~~~~~~~
public class MarkableStack<E> extends Stack<E> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Integer[] INTERNED = new Integer[100];

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-8952921102983411451")
  private static final long      serialVersionUID = 77339563366964674L;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    for (int i = 0; i < INTERNED.length; i++) {
      INTERNED[i] = new Integer(i);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final List<Integer> marks = new ArrayList<Integer>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new, empty stack; the backing <code>Map</code>
   ** instance has default initial capacity (16) and load factor (0.75).
   */
  public MarkableStack() {
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   push (overridden)
  /**
   ** Pushes an item on the top of this stack.
   ** <p>
   ** This has exactly the same effect as:
   ** <blockquote>
   ** <pre>
   **   addElement(item)
   ** </pre>
   ** </blockquote>
   **
   ** @param  item               the item to be pushed onto this stack.
   **
   ** @return                    the <code>item</code> argument.
   **
   ** @see    java.util.Vector#addElement
   */
  @Override
  public E push(final E item) {
    if (this.marks.size() == 0)
      new Throwable("WARNING: push called without an enclosing mark").printStackTrace();

    return super.push(item);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pop (overridden)
  /**
   ** Removes the object at the top of this stack and returns that object as the
   ** value of this function.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The entire work is delegated to the super class. But we have to manage our
   ** marker properties that are implemented by classes that are not thread safe
   ** hence we  marking this as <code>synchronized</code> too.
   **
   ** @return                    the object at the top of this stack (the last
   **                            item of the embedded <code>Vector</code>
   **                            object).
   */
  @Override
  public synchronized E pop() {
    if (this.marks.size() == 0)
      new Throwable("WARNING: pop called without an encosing mark").printStackTrace();

    return super.pop();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pop
  /**
   ** Removes the object at the top of this stack and returns that object as the
   ** value of this function.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The entire work is delegated to the super class. But we have to manage our
   ** marker properties that are implemented by classes that are not thread safe
   ** hence we  marking this as <code>synchronized</code> too.
   **
   ** @param  object             the object at the top of this stack to remove.
  */
  public synchronized void pop(final Object object) {
    if (this.marks.size() == 0)
      new Throwable("WARNING: pop called without an encosing mark").printStackTrace();
    else {
      final int mark = this.marks.get(this.marks.size() - 1).intValue();
      if (this.size() <= mark) {
        new Throwable("WARNING: pop called before inner unwind - debugger stack for thread " + Thread.currentThread().getName() + " may be incorrect. ").printStackTrace();
        return;
      }

      final Object last = this.get(this.size() - 1);
      if (last != object) {
        new Throwable("WARNING: attempt to pop an object not on the stack - debugger stack for thread " + Thread.currentThread().getName() + " may be incorrect. ").printStackTrace();
        return;
      }
      this.remove(this.size() - 1);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mark
  public void mark() {
    this.marks.add(intern(this.size()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unwind
  public void unwind() {
    if (this.marks.size() == 0)
      new Throwable("WARNING: stack underflow - debugger stack for thread " + Thread.currentThread().getName() + " may be incorrect. ").printStackTrace();
    else {
      final int mark = this.marks.remove(this.marks.size() - 1).intValue();
      if (this.size() < mark) {
        new Throwable("WARNING: stack mark is less than stack size").printStackTrace();
        while (this.size() > mark)
          this.remove(this.size() - 1);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intern
  private static Integer intern(final int i) {
    return ((i >= 0) && (i < INTERNED.length)) ? INTERNED[i] : new Integer(i);
  }
}