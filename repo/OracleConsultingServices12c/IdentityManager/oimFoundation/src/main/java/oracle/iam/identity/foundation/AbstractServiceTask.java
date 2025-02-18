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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Scheduler Facilities

    File        :   AbstractServiceTask.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractServiceTask.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000129
                                         Batch Size can be defined with a
                                         negative value.
                                         Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation;

import com.thortech.xl.dataaccess.tcDataProvider;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractServiceTask
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractServiceTask</code> implements the base functionality of an
 ** service end point for the Oracle Identity Manager either Provisioning or
 ** Reconciliation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public interface AbstractServiceTask extends AbstractTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** value which can be used on this task to transform a timestamp to string.
   */
  public static final String TIMESTAMP_PATTERN = "yyyyMMddHHmmss.SSS'Z'";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Batch
  // ~~~~~ ~~~~~
  /**
   ** Batch provides the capabilities to control the result set size the
   ** EntityManager returns
   */
  public static class Batch {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final int size;
    private int       start = 1;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Creates a <code>Batch</code> instance with the given size.
     **
     ** @param  size             the desired size of a result set.
     */
    public Batch(final int size) {
      // Fixed Defect DE-000129
      // Batch Size can be defined with a negative value
      this.size = (size == Integer.MIN_VALUE || size < 0) ? 1 : size;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   size
    /**
     ** Returns the size of an expected result set.
     **
     ** @return                  the desired size of a result set.
     */
    public int size() {
      return this.size;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   start
    /**
     ** Returns the start row of an expected result set.
     **
     ** @return                  the start row of an expected result set.
     */
    public int start() {
      return this.start;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   end
    /**
     ** Returns the end row of an expected result set.
     **
     ** @return                  the end row of an expected result set.
     */
    public int end() {
      return (this.start - 1) + this.size;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   next
    /**
     ** Evaluates the start attributes to the next row based on the size of the
     ** expected result set size.
     */
    public void next() {
      this.start += size;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provider
  /**
   ** Returns the session provider connection associated with this task.
   **
   ** @return                    the session provider connection associated with
   **                            this task.
   */
  tcDataProvider provider();
}