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

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   Operation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Operation.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.wsm.type;

import java.util.List;
import java.util.ArrayList;

import oracle.hst.deployment.ServiceDataType;

////////////////////////////////////////////////////////////////////////////////
// class Operation
// ~~~~~ ~~~~~~~~~
public abstract class Operation extends ServiceDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the collection of policies attached to or detached from a webservice
  private final List<Policy> policy = new ArrayList<Policy>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Attach
  // ~~~~~ ~~~~~~
  /**
   ** <code>Attach</code> defines the container to accumulate policies side to
   ** be attached to a WebService.
   */
  public static class Attach extends Operation {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Attach</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Attach() {
      // ensure inheritance
      super();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Detach
  // ~~~~~ ~~~~~~
  /**
   ** <code>Detach</code> defines the container to accumulate policies side to
   ** be detached to a WebService.
   */
  public static class Detach extends Operation {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Detach</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Detach() {
      // ensure inheritance
      super();
    }
  }

  ////////////////////////////////////////////////////////////////////////////
  // class Enable
  // ~~~~~ ~~~~~~
  /**
   ** <code>Enable</code> defines the container to accumulate policies side to
   ** be enabled to a WebService.
   */
  public static class Enable extends Operation {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Enable</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Enable() {
      // ensure inheritance
      super();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Disable
  // ~~~~~ ~~~~~~~
  /**
   ** <code>Disable</code> defines the container to accumulate policies side to
   ** be enabled to a WebService.
   */
  public static class Disable extends Operation {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Disable</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Disable() {
      // ensure inheritance
      super();
    }
  }

  ////////////////////////////////////////////////////////////////////////////
  // class Report
  // ~~~~~ ~~~~~~
  /**
   ** <code>Report</code> defines the container to accumulate policies side to
   ** be reported on a WebService.
   */
  public static class Report extends Operation {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Report</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Report() {
      // ensure inheritance
      super();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Operation</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Operation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   policy
  /**
   ** Returns the {@link Policy} delegates.
   **
   ** @return                    the {@link Policy} delegates.
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Policy}.
   */
  public final List<Policy> policy() {
    return this.policy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredPolicy
  /**
   ** Add the specified policy to the operations that has to be attached or
   ** detached.
   **
   ** @param  policy             the policy to be passed to the Oracle WebLogic
   **                            Domain entity instance.
   **                            Allowed object is {@link Policy}.
   */
  public void addConfiguredPolicy(final Policy policy) {
    // add the value pair to the parameters
    this.policy.add(policy);
  }
}