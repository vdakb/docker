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
    Subsystem   :   Common Shared Runtime Facilities

    File        :   TaskAttribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TaskAttribute.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation;

////////////////////////////////////////////////////////////////////////////////
// class TaskAttribute
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** The <code>TaskAttribute</code> implements the base functionality
 ** for describing the attributes of an scheduled task.
 ** <br>
 ** A scheduled task may have attributes or not. Some of them are mandatory for
 ** the functionality of a scheduled task.
 ** <p>
 ** This class extends the {@link AbstractAttribute} to achive type safetivity.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class TaskAttribute extends AbstractAttribute {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TaskAttribute</code> which has a name and is
   ** optional for a specific task.
   **
   ** @param  name               the name of the attribute.
   */
  private TaskAttribute(String name) {
    // ensure inheritance
    super(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TaskAttribute</code> which has a name and the default
   ** value which this attribute provides in case it is not defined.
   ** <br>
   ** The attribute will be an optional attribute.
   **
   ** @param  name               the name of the attribute.
   ** @param  defaultValue       the default value for this attribute.
   */
  private TaskAttribute(String name, String defaultValue) {
    // ensure inheritance
    super(name, defaultValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TaskAttribute</code> which has a name and is
   ** optional/mandatory according to the passed parameter.
   **
   ** @param  name               the name of the attribute.
   ** @param  mandatory          <code>true</code> the attribute must be
   **                            declared for the task; otherwise false.
   */
  private TaskAttribute(final String name, final boolean mandatory) {
    // ensure inheritance
    super(name, mandatory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>TaskAttribute</code> which has a name and
   ** is optional for a specific task.
   ** defined.
   **
   ** @param  name               the name of the attribute.
   **
   ** @return                    an instance of <code>TaskAttribute</code>
   **                            with the value provided.
   */
  public static TaskAttribute build(final String name) {
    return new TaskAttribute(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>TaskAttribute</code> which has a name and
   ** the default value which this attribute provides in case it is not
   ** defined.
   ** <br>
   ** The attribute will be an optional attribute.
   **
   ** @param  name               the name of the attribute.
   ** @param  defaultValue       the default value for this attribute.
   **
   ** @return                    an instance of <code>TaskAttribute</code>
   **                            with the value provided.
   */
  public static TaskAttribute build(final String name, final int defaultValue) {
    return build(name, String.valueOf(defaultValue));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>TaskAttribute</code> which has a name and
   ** the default value which this attribute provides in case it is not
   ** defined.
   ** <br>
   ** The attribute will be an optional attribute.
   **
   ** @param  name               the name of the attribute.
   ** @param  defaultValue       the default value for this attribute.
   **
   ** @return                    an instance of <code>TaskAttribute</code>
   **                            with the value provided.
   */
  public static TaskAttribute build(final String name, final long defaultValue) {
    return build(name, String.valueOf(defaultValue));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>TaskAttribute</code> which has a name and
   ** the default value which this attribute provides in case it is not
   ** defined.
   ** <br>
   ** The attribute will be an optional attribute.
   **
   ** @param  name               the name of the attribute.
   ** @param  defaultValue       the default value for this attribute.
   **
   ** @return                    an instance of <code>TaskAttribute</code>
   **                            with the value provided.
   */
  public static TaskAttribute build(final String name, final String defaultValue) {
    return new TaskAttribute(name, defaultValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>TaskAttribute</code> which has a name
   ** and is optional/mandatory according to the passed parameter.
   **
   ** @param  name               the name of the attribute.
   ** @param  mandatory          <code>true</code> the attribute must be
   **                            declared for the task; otherwise false.
   **
   ** @return                    an instance of <code>TaskAttribute</code>
   **                            with the value provided.
   */
  public static TaskAttribute build(final String name, final boolean mandatory) {
    return new TaskAttribute(name, mandatory);
  }
}